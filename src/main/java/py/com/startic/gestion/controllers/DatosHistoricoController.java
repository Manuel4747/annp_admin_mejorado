package py.com.startic.gestion.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import py.com.startic.gestion.models.DatosHistorico;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.models.Acciones;
import py.com.startic.gestion.models.Archivos;
import py.com.startic.gestion.models.ChartObjetivoPrincipal;
import py.com.startic.gestion.models.ParametrosSistema;
import py.com.startic.gestion.models.Periodo;
import py.com.startic.gestion.models.TiposObjetivos;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "datosHistoricoController")
@ViewScoped
public class DatosHistoricoController extends AbstractController<DatosHistorico> {

    @Inject
    private AccionesController accionesController;
    @Inject
    private DatosHistoricoController datosHistoricoController;
    private List<Acciones> listaAcciones;
    private TiposObjetivos tiposObjetivos;
    private Acciones accion;
    private Periodo periodo;
    private Date fechaDesde;
    private Date fechaHasta;
    private String accionSeleccionada;
    private String etiqueta;
    private double nuevaCantidadProgramada;
    private double nuevaCantidadAvance;
    private double resultado;
    private String nombre;
    List<Archivos> listaArchivos;
    private Archivos docImprimir;
    private BigDecimal totalCausas;
    private BigDecimal totalCausasResultas;
    private ParametrosSistema par;
    private String content;
    private HttpSession session;
    private String url;
    private Collection<Archivos> detalles;
    private Integer tipo;
    private Integer periodo2;
    private List<ChartObjetivoPrincipal> listaChartObjetivoPrincipal;

    private double porcentajeMisional;
    private double porcentajeApoyo;

    @PostConstruct
    @Override
    public void initParams() {
        par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID).getSingleResult();

    }

    public List<Acciones> getListaAcciones() {
        return listaAcciones;
    }

    public void setListaAcciones(List<Acciones> listaAcciones) {
        this.listaAcciones = listaAcciones;
    }

    public Acciones getAccion() {
        return accion;
    }

    public void setAccion(Acciones accion) {
        this.accion = accion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public TiposObjetivos getTiposObjetivos() {
        return tiposObjetivos;
    }

    public void setTiposObjetivos(TiposObjetivos tiposObjetivos) {
        this.tiposObjetivos = tiposObjetivos;
    }

    public String getAccionSeleccionada() {
        return accionSeleccionada;
    }

    public void setAccionSeleccionada(String accionSeleccionada) {
        this.accionSeleccionada = accionSeleccionada;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public double getNuevaCantidadProgramada() {
        return nuevaCantidadProgramada;
    }

    public void setNuevaCantidadProgramada(double nuevaCantidadProgramada) {
        this.nuevaCantidadProgramada = nuevaCantidadProgramada;
    }

    public double getNuevaCantidadAvance() {
        return nuevaCantidadAvance;
    }

    public void setNuevaCantidadAvance(double nuevaCantidadAvance) {
        this.nuevaCantidadAvance = nuevaCantidadAvance;
    }

    public List<Archivos> getListaArchivos() {
        return listaArchivos;
    }

    public void setListaArchivos(List<Archivos> listaArchivos) {
        this.listaArchivos = listaArchivos;
    }

    public ParametrosSistema getPar() {
        return par;
    }

    public void setPar(ParametrosSistema par) {
        this.par = par;
    }

    public void prepareVerDoc(Archivos doc) {
        docImprimir = doc;

        //PrimeFaces.current().ajax().update("ExpAcusacionesViewForm");
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Integer getPeriodo2() {
        return periodo2;
    }

    public void setPeriodo2(Integer periodo2) {
        this.periodo2 = periodo2;
    }

    public List<ChartObjetivoPrincipal> getListaChartObjetivoPrincipal() {
        return listaChartObjetivoPrincipal;
    }

    public void setListaChartObjetivoPrincipal(List<ChartObjetivoPrincipal> listaChartObjetivoPrincipal) {
        this.listaChartObjetivoPrincipal = listaChartObjetivoPrincipal;
    }

    public DatosHistoricoController() {
        // Inform the Abstract parent controller of the concrete DatosHistorico Entity
        super(DatosHistorico.class);
    }

    public void resetParents() {
        accionesController.setSelected(null);
        obtenerArchivos();
        seleccionar();
        consultar();
    }

    public double getResultado() {
        return resultado;
    }

    public void setResultado(double resultado) {
        this.resultado = resultado;
    }

    public BigDecimal getTotalCausas() {
        return totalCausas;
    }

    public void setTotalCausas(BigDecimal totalCausas) {
        this.totalCausas = totalCausas;
    }

    public BigDecimal getTotalCausasResultas() {
        return totalCausasResultas;
    }

    public void setTotalCausasResultas(BigDecimal totalCausasResultas) {
        this.totalCausasResultas = totalCausasResultas;
    }

    public Archivos getDocImprimir() {
        return docImprimir;
    }

    public void setDocImprimir(Archivos docImprimir) {
        this.docImprimir = docImprimir;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Collection<Archivos> getDetalles() {
        return detalles;
    }

    public void setDetalles(Collection<Archivos> detalles) {
        this.detalles = detalles;
    }

    public String datePattern() {
        return "dd/MM/yyyy";
    }

    public String customFormatDate(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern());
            return format.format(date);
        }
        return "";
    }

    private List<Acciones> obtenerListaTiposObjetivos(TiposObjetivos tiposObjetivos) {
        if (tiposObjetivos != null) {
            return this.ejbFacade.getEntityManager().createNamedQuery("Acciones.findByTiposObjetivos", Acciones.class
            ).setParameter("tiposObjetivos", tiposObjetivos).getResultList();
        }
        return null;
    }

    public void actualizarListas(TiposObjetivos tiposObjetivos) {
        listaAcciones = obtenerListaTiposObjetivos(tiposObjetivos);

    }

    public void resetearListas(TiposObjetivos tiposObjetivos) {
        accion = null;
        //  programacion = null;

        if (getSelected() != null) {
            getSelected().setAccion(null);
            //getSelected().setProgramacion(null);

        }
        actualizarListas(tiposObjetivos);
    }

    public String getColorSemaforo(DatosHistorico item) {

        if (item.getResultado() < item.getProgramacion().getMetaMinima()) {
            return "red";
        } else if (item.getResultado() < item.getProgramacion().getMetaAceptable()) {
            return "yellow";
        } else {
            return "green";
        }

    }

    private void seleccionar() {
        if (getSelected() != null) {
            detalles = this.ejbFacade.getEntityManager().createNamedQuery("Archivos.findByDetallesPlanEstrategica", Archivos.class).setParameter("accionOperativa", getSelected().getDetallesPlan()).getResultList();
        } else {
            detalles = null;
        }

    }

    @Override
    public void save(ActionEvent event) {

        if (getSelected() != null) {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usu);
        }

        super.save(event);
    }

    /**
     * Store a new item in the data layer.
     *
     * @param event an event from the widget that wants to save a new Entity to
     * the data layer
     */
    @Override
    public void saveNew(ActionEvent event) {
        if (getSelected() != null) {

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usu);
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setUsuarioAlta(usu);
            getSelected().setEmpresa(usu.getEmpresa());

            super.saveNew(event);

        }

    }

    private void obtenerArchivos() {
        if (getSelected() != null) {
            listaArchivos = ejbFacade.getEntityManager().createNamedQuery("Archivos.findByDocumentoPlanificacion", Archivos.class).setParameter("accionOperativa", getSelected().getDetallesPlan()).getResultList();
        }
    }

    public void prepareCerrarDialogoVerDoc() {
        if (docImprimir != null) {
            File f = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/tmp/" + nombre);

            f.delete();

            docImprimir = null;
        }
    }

    public String getContent() {

        nombre = "";
        try {
            if (docImprimir != null) {

                byte[] fileByte = null;

                if (docImprimir.getRuta() != null) {
                    try {
                        fileByte = Files.readAllBytes(new File(par.getRutaArchivos() + "/" + docImprimir.getRuta()).toPath());

                    } catch (IOException ex) {
                        JsfUtil.addErrorMessage("No tiene documento adjunto");
                        content = "";
                    }
                }

                if (fileByte != null) {
                    Date fecha = ejbFacade.getSystemDate();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

                    String partes[] = docImprimir.getRuta().split("[.]");
                    String ext = "pdf";

                    if (partes.length > 1) {
                        ext = partes[partes.length - 1];
                    }

                    nombre = session.getId() + "_" + simpleDateFormat.format(fecha) + "." + ext;
                    FileOutputStream outputStream = new FileOutputStream(Constantes.RUTA_ARCHIVOS_TEMP + "/tmp/" + nombre);

                    outputStream.write(fileByte);

                    outputStream.close();

                    // content = new DefaultStreamedContent(new ByteArrayInputStream(fileByte), "application/pdf");
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
            content = null;
        }
        // return par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/tmp/" + nombre;
        return url + "/tmp/" + nombre;

    }

    public void buscarAccionesPorPeriodo() {

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        DatosHistorico datos = (DatosHistorico) session.getAttribute("DatosHistorico");
        setItems(this.ejbFacade.getEntityManager().createNamedQuery("DatosHistorico.findByBuscarAccionesPorFecha", DatosHistorico.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).setParameter("accion", accion).getResultList());

        setSelected(null);
        resetParents();

    }

    public void buscar() {
        if (accion != null && fechaDesde != null && fechaHasta != null) {
            this.ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
            setItems(this.ejbFacade.getEntityManager().createNamedQuery("DatosHistorico.findByBuscarAccionesPorFecha", DatosHistorico.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).setParameter("accion", accion).getResultList());

            // setItems(this.ejbFacade.getEntityManager().createNamedQuery("DatosHistorico.findByBuscarAccionesPorPeriodo", DatosHistorico.class).setParameter("accion", accion).setParameter("periodo", periodo).getResultList());
            if (accion.getId() == 14 || accion.getId() == 15) {
                //if ("A1".equals(accionSeleccionada)) {
                etiqueta = "Causas en trámite (sin enjuiciamiento)";
            }
            if (accion.getId() == 16 || accion.getId() == 17) {
                etiqueta = "Cantidad Programada";

            }
        }

    }

    public void guardarEdit() {

        if (getSelected().getCantidadProgramada() != nuevaCantidadProgramada
                || getSelected().getCantidadProgramada() != nuevaCantidadAvance) {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            double totalAvance = 0.0;
            resultado = (nuevaCantidadAvance / nuevaCantidadProgramada) * 100;
            Usuarios usu = (Usuarios) session.getAttribute("Usuarios");
            Date fecha = ejbFacade.getSystemDate();
            if (getSelected().getCantidadProgramada() != nuevaCantidadProgramada) {
                resultado = (getSelected().getCantidadAvance() / nuevaCantidadProgramada) * 100;
                getSelected().setCantidadProgramada(nuevaCantidadProgramada);
            }
            if (getSelected().getCantidadProgramada() != nuevaCantidadAvance) {
                resultado = (nuevaCantidadAvance / getSelected().getCantidadProgramada()) * 100;
                getSelected().setCantidadProgramada(nuevaCantidadAvance);
            }

            getSelected().setCantidadProgramada(nuevaCantidadProgramada);
            getSelected().setCantidadAvance(nuevaCantidadAvance);
            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usu);
            totalAvance = getSelected().getTotalAvance();
            //getSelected().setTotalAvance(totalAvance);
            getSelected().setResultado(resultado);
            datosHistoricoController.setSelected(getSelected());
            datosHistoricoController.save(null);

            // resetParents();
        } else {
            JsfUtil.addErrorMessage("No hay ningun cambio que registrar");
        }
    }

    

    public void consultar() {
        ChartObjetivoPrincipal stSu = new ChartObjetivoPrincipal();
        if (tipo != null && periodo2 != null) {
            stSu.setTipo(tipo);
            stSu.setPeriodo(periodo2);
        }
        if(stSu.getTipo() == 1){
           porcentajeMisional = (stSu.getVariable2() / stSu.getValorVariable()) * 100;
        } else {
            porcentajeMisional = 0;
        }
         porcentajeApoyo = (stSu.getMetasAlcanzada() / 8) * 100;
        }

        

}
