package py.com.startic.gestion.controllers;

import java.awt.Component;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import py.com.startic.gestion.models.PlanEstrategicas;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import org.primefaces.model.file.UploadedFile;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.datasource.RepAvanceAcciones;
import py.com.startic.gestion.models.Acciones;
import py.com.startic.gestion.models.Actividades;
import py.com.startic.gestion.models.CambiosValor;
import py.com.startic.gestion.models.ChartObjetivoPrincipal;
import py.com.startic.gestion.models.DetallesPlanEstrategicas;
import py.com.startic.gestion.models.EstadosActividad;
import py.com.startic.gestion.models.Periodo;
import py.com.startic.gestion.models.Programacion;
import py.com.startic.gestion.models.TiposDocumentosJudiciales;
import py.com.startic.gestion.models.TiposObjetivos;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "planEstrategicasController")
@ViewScoped
public class PlanEstrategicasController extends AbstractController<PlanEstrategicas> {

    @Inject
    private EmpresasController empresaController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private TiposObjetivosController tiposObjetivosController;
    @Inject
    private DetallesPlanEstrategicasController detallesPlanEstrategicasController;
    @Inject
    private PlanEstrategicasController planEstrategicasController;
    @Inject
    private CambiosValorController cambiosValorController;
    @Inject
    private ProgramacionController programacionController;
    private Usuarios usuario;
    private List<Acciones> listaAcciones;
    private List<Programacion> listaProgramacion;
    private List<Actividades> listaActividades;
    private Collection<DetallesPlanEstrategicas> detalles;
    private DetallesPlanEstrategicas detalleSelected;
    private Acciones accion;
    private DetallesPlanEstrategicas detallesPlan;
    private Programacion programacion;
    private Actividades actividad;
    private TiposDocumentosJudiciales tiposDocumento;
    private PlanEstrategicas planEstrategicas;
    private TiposObjetivos tiposObjetivos;
    private HttpSession session;
    private double variable2;
    private String año;
    private String menu;
    private Date fechaDesde;
    private Date fechaHasta;
    private Integer activeTab;
    private UploadedFile file;
    private Periodo periodo;
    private String descripcionArchivo;
    List<EstadosActividad> ListEstadosActividad;
    private Programacion programacionSelected;
    private PlanEstrategicas planEstrategicaSelected;
    private double nuevaVariable;
    private double nuevaVariable2;
    private double resultado;
    private double valor;
    private double variable;
    private double valorVariable;
    private double programaPresupuestario;
    private double presupuesto;
    private double resultadoPresupuesto;
    private List<Acciones> acciones;
    private double metaMinima;
    private double metaAceptable;
    private double metaOptima;
    private List<Actividades> actividades;
    private Acciones accionesSeleccionada;
    private String titulo = "";
    private Integer tipo;
    private Integer periodo2;
    private List<ChartObjetivoPrincipal> listaChartObjetivoPrincipal;

    private double porcentajeMisional;
    private double porcentajeApoyo;

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        detalles = null;
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        usuario = (Usuarios) session.getAttribute("Usuarios");

        menu = Constantes.NO;
        setItems(ejbFacade.getEntityManager().createNamedQuery("PlanEstrategicas.findOrdered", PlanEstrategicas.class).getResultList());
        if (!getItems2().isEmpty()) {
            PlanEstrategicas art = getItems2().iterator().next();
            setSelected(art);

        } else {
            setSelected(null);
        }
    }

    @Override
    public PlanEstrategicas prepareCreate(ActionEvent event) {
        PlanEstrategicas plan = super.prepareCreate(event);
        detalles = null;
        tiposObjetivos = null;
        accion = null;
        periodo = null;
        actividad = null;
        return plan;
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

    public double getVariable2() {
        return variable2;
    }

    public void setVariable2(double variable2) {
        this.variable2 = variable2;
    }

    public Integer getActiveTab() {
        return activeTab;
    }

    public void setActiveTab(Integer activeTab) {
        this.activeTab = activeTab;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getDescripcionArchivo() {
        return descripcionArchivo;
    }

    public void setDescripcionArchivo(String descripcionArchivo) {
        this.descripcionArchivo = descripcionArchivo;
    }

    public String getAño() {
        return año;
    }

    public void setAño(String año) {
        this.año = año;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public List<Acciones> getAcciones() {
        return acciones;
    }

    public void setAcciones(List<Acciones> acciones) {
        this.acciones = acciones;
    }

    public double getMetaMinima() {
        return metaMinima;
    }

    public void setMetaMinima(double metaMinima) {
        this.metaMinima = metaMinima;
    }

    public double getMetaAceptable() {
        return metaAceptable;
    }

    public void setMetaAceptable(double metaAceptable) {
        this.metaAceptable = metaAceptable;
    }

    public double getMetaOptima() {
        return metaOptima;
    }

    public void setMetaOptima(double metaOptima) {
        this.metaOptima = metaOptima;
    }

    public List<Actividades> getActividades() {
        return actividades;
    }

    public void setActividades(List<Actividades> actividades) {
        this.actividades = actividades;
    }

    public Acciones getAccionesSeleccionada() {
        return accionesSeleccionada;
    }

    public void setAccionesSeleccionada(Acciones accionesSeleccionada) {
        this.accionesSeleccionada = accionesSeleccionada;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
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

    public double getPorcentajeMisional() {
        return porcentajeMisional;
    }

    public void setPorcentajeMisional(double porcentajeMisional) {
        this.porcentajeMisional = porcentajeMisional;
    }

    public double getPorcentajeApoyo() {
        return porcentajeApoyo;
    }

    public void setPorcentajeApoyo(double porcentajeApoyo) {
        this.porcentajeApoyo = porcentajeApoyo;
    }
    

    public TiposObjetivos getTiposObjetivos() {
        if (session.getAttribute("tipoSelected") != null) {
            tiposObjetivos = (TiposObjetivos) session.getAttribute("tipoSelected");
            session.removeAttribute("tipoSelected");
        }

        return tiposObjetivos;
    }

    public void setTiposObjetivos(TiposObjetivos tiposObjetivos) {
        this.tiposObjetivos = tiposObjetivos;
    }

    public Collection<DetallesPlanEstrategicas> getDetalles() {
        return detalles;
    }

    public void setDetalles(Collection<DetallesPlanEstrategicas> detalles) {
        this.detalles = detalles;
    }

    public DetallesPlanEstrategicas getDetalleSelected() {
        return detalleSelected;
    }

    public void setDetalleSelected(DetallesPlanEstrategicas detalleSelected) {
        this.detalleSelected = detalleSelected;
    }

    public Actividades getActividad() {
        return actividad;
    }

    public void setActividad(Actividades actividad) {
        this.actividad = actividad;
    }

    public PlanEstrategicas getPlanEstrategicas() {
        return planEstrategicas;
    }

    public void setPlanEstrategicas(PlanEstrategicas planEstrategicas) {
        this.planEstrategicas = planEstrategicas;
    }

    public List<Actividades> getListaActividades() {
        return listaActividades;
    }

    public void setListaActividades(List<Actividades> listaActividades) {
        this.listaActividades = listaActividades;
    }

    public List<Programacion> getListaProgramacion() {
        return listaProgramacion;
    }

    public void setListaProgramacion(List<Programacion> listaProgramacion) {
        this.listaProgramacion = listaProgramacion;
    }

    public PlanEstrategicas getPlanEstrategicaSelected() {
        return planEstrategicaSelected;
    }

    public void setPlanEstrategicaSelected(PlanEstrategicas planEstrategicaSelected) {
        this.planEstrategicaSelected = planEstrategicaSelected;
    }
    

    public PlanEstrategicasController() {
        // Inform the Abstract parent controller of the concrete PlanEstrategicas Entity
        super(PlanEstrategicas.class);
    }

    public Programacion getProgramacion() {
        return programacion;
    }

    public void setProgramacion(Programacion programacion) {
        this.programacion = programacion;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
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

    public List<EstadosActividad> getListEstadosActividad() {
        return ListEstadosActividad;
    }

    public void setListEstadosActividad(List<EstadosActividad> ListEstadosActividad) {
        this.ListEstadosActividad = ListEstadosActividad;
    }

    public DetallesPlanEstrategicas getDetallesPlan() {
        return detallesPlan;
    }

    public void setDetallesPlan(DetallesPlanEstrategicas detallesPlan) {
        this.detallesPlan = detallesPlan;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public TiposDocumentosJudiciales getTiposDocumento() {
        return tiposDocumento;
    }

    public void setTiposDocumento(TiposDocumentosJudiciales tiposDocumento) {
        this.tiposDocumento = tiposDocumento;
    }

    public Programacion getProgramacionSelected() {
        return programacionSelected;
    }

    public void setProgramacionSelected(Programacion programacionSelected) {
        this.programacionSelected = programacionSelected;
    }

    public double getNuevaVariable() {
        return nuevaVariable;
    }

    public void setNuevaVariable(double nuevaVariable) {
        this.nuevaVariable = nuevaVariable;
    }

    public double getVariable() {
        return variable;
    }

    public void setVariable(double variable) {
        this.variable = variable;
    }

    public double getValorVariable() {
        return valorVariable;
    }

    public void setValorVariable(double valorVariable) {
        this.valorVariable = valorVariable;
    }

    public double getNuevaVariable2() {
        return nuevaVariable2;
    }

    public void setNuevaVariable2(double nuevaVariable2) {
        this.nuevaVariable2 = nuevaVariable2;
    }
    

    public double getResultado() {
        return resultado;
    }

    public void setResultado(double resultado) {
        this.resultado = resultado;
    }

    public double getProgramaPresupuestario() {
        return programaPresupuestario;
    }

    public void setProgramaPresupuestario(double programaPresupuestario) {
        this.programaPresupuestario = programaPresupuestario;
    }

    public double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(double presupuesto) {
        this.presupuesto = presupuesto;
    }

    public double getResultadoPresupuesto() {
        return resultadoPresupuesto;
    }

    public void setResultadoPresupuesto(double resultadoPresupuesto) {
        this.resultadoPresupuesto = resultadoPresupuesto;
    }

    public void resetParents() {
        empresaController.setSelected(null);
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);

        seleccionar();
         //consultar();
    }

    public void buscarPorFecha() {

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        PlanEstrategicas plan = (PlanEstrategicas) session.getAttribute("PlanEstrategicas");

        //setItems(this.ejbFacade.getEntityManager().createNamedQuery("PlanEstrategicas.findByFecha", PlanEstrategicas.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).getResultList());
        setItems(this.ejbFacade.getEntityManager().createNamedQuery("PlanEstrategicas.findByPeriodo", PlanEstrategicas.class).setParameter("año", año).getResultList());

        setSelected(null);
        resetParents();
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
        programacion = null;

        if (getSelected() != null) {
            getSelected().setAccion(null);
            getSelected().setProgramacion(null);

        }
        actualizarListas(tiposObjetivos);
    }

    public void prepararCambioVariable2(PlanEstrategicas item) {
        planEstrategicaSelected = item;
        nuevaVariable2 = getSelected().getVariable2();

    }
    public void prepararCambio(Programacion item) {
        programacionSelected = item;
        nuevaVariable = getSelected().getProgramacion().getValorVariable();

    }
     public void prepararCambioPresupuestario(Programacion item) {
        programacionSelected = item;
        nuevaVariable = getSelected().getProgramacion().getProgramaPresupuestario();

    }

    public void agregar() {

        if (actividad == null) {
            JsfUtil.addErrorMessage("Debe seleccionar una Accion Operativa");
            return;
        }

        if (detalles == null) {
            detalles = new ArrayList<>();
        }

        boolean encontro = false;

        int contador = 0;

        for (DetallesPlanEstrategicas det : detalles) {
            contador++;
            if (det.getActividad().getId().equals(actividad.getId())) {
                detalles.remove(det);

                DetallesPlanEstrategicas dsa = detallesPlanEstrategicasController.prepareCreate(null);

                SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");

                Date fecha = ejbFacade.getSystemDate();
                dsa.setPlanEstrategica(getSelected());
                dsa.setActividad(actividad);
                dsa.setTipoDocumentos(actividad.getTiposDocumento());
                dsa.setDepartamento(actividad.getDepartamento());
                dsa.setId(Integer.valueOf(format.format(fecha)));
                detalles.add(dsa);
                encontro = true;
                break;
                //}
            }
        }

        if (!encontro) {
            DetallesPlanEstrategicas dsa = detallesPlanEstrategicasController.prepareCreate(null);

            SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");

            Date fecha = ejbFacade.getSystemDate();

            dsa.setItem(contador + 1);
            dsa.setPlanEstrategica(getSelected());
            dsa.setActividad(actividad);
            dsa.setTipoDocumentos(actividad.getTiposDocumento());
            dsa.setDepartamento(actividad.getDepartamento());
            dsa.setId(Integer.valueOf(format.format(fecha)));

            detalles.add(dsa);
        }

    }

    public void borrarDetalle() {
        if (detalles != null && detalleSelected != null) {
            for (DetallesPlanEstrategicas det : detalles) {
                if (det.getActividad().getId().equals(detalleSelected.getActividad().getId())) {
                    detalles.remove(det);
                    break;
                }
            }
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

            if (detalles != null) {
                if (detalles.isEmpty()) {
                    JsfUtil.addErrorMessage("Debe habar al menos un detalle");
                    return;
                }
            } else {
                JsfUtil.addErrorMessage("Debe haber al menos un detalle.");
                return;
            }

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usu);
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setUsuarioAlta(usu);
            getSelected().setEmpresa(usu.getEmpresa());
            getSelected().setTiposObjetivos(tiposObjetivos);
            getSelected().setAccion(accion);
            getSelected().setPeriodo(periodo);
            //getSelected().setValorAnterior(detallesPlan.getVariable2());

            super.saveNew(event);

            for (DetallesPlanEstrategicas det : detalles) {
                det.setPlanEstrategica(getSelected());
                det.setFechaHoraUltimoEstado(fecha);
                det.setUsuarioUltimoEstado(usu);
                det.setFechaHoraAlta(fecha);
                det.setUsuarioAlta(usu);
                det.setEmpresa(usu.getEmpresa());
                //det.setDepartamento(actividad.getDepartamento());
                det.setProgramacion(getSelected().getProgramacion());
                //det.setTipoDocumentos(actividad.getTiposDocumento());
                // det.setValorAnterior(getSelected().getVariable2());

                det.setId(null);

                detallesPlanEstrategicasController.setSelected(det);

                detallesPlanEstrategicasController.saveNew(event);

            }

        }

    }

    private void seleccionar() {
        if (getSelected() != null) {
            detalles = this.ejbFacade.getEntityManager().createNamedQuery("DetallesPlanEstrategicas.findByPlanEstrategicica", DetallesPlanEstrategicas.class).setParameter("planEstrategica", getSelected()).getResultList();
        } else {
            detalles = null;
        }

    }
   public String getColorSemaforo(PlanEstrategicas item) {
       
        if (item.getResultado() < item.getProgramacion().getMetaMinima()) {
            return "red";
        } else if (item.getResultado() < item.getProgramacion().getMetaAceptable()) {
            return "yellow";
        } else {
            return "green";
        }
        
    }
     
   

    private List<Programacion> obtenerListaProgramacion(Acciones accion) {
        if (accion != null) {
            return this.ejbFacade.getEntityManager().createNamedQuery("Programacion.findByProgramacion", Programacion.class
            ).setParameter("accion", accion).getResultList();
        }
        return null;
    }

    public void actualizarListasProgramacion(Acciones accion) {
        listaProgramacion = obtenerListaProgramacion(accion);
    }

    public void resetearListasProgramacion(Acciones accion) {
        programacion = null;

        if (getSelected() != null) {
            getSelected().setProgramacion(null);

        }
        actualizarListasProgramacion(accion);
    }

    private List<Actividades> obtenerListaAcciones(Acciones accion) {
        if (accion != null) {
            return this.ejbFacade.getEntityManager().createNamedQuery("Actividades.findByAcciones", Actividades.class
            ).setParameter("accion", accion).getResultList();
        }
        return null;
    }

    public void actualizarListas(Acciones accion) {
        listaActividades = obtenerListaAcciones(accion);
        listaProgramacion = obtenerListaProgramacion(accion);
    }

    public void resetearListas(Acciones accion) {
        actividad = null;
        programacion = null;

        if (getSelected() != null) {
            getSelected().setActividad(null);
            getSelected().setProgramacion(null);

        }
        actualizarListas(accion);
    }

    public String calculaColor1() {
        if (getSelected() != null) {
            if (getSelected().getEstado().getCodigo().equals("RO")) {

                return "red";
            }
            if (getSelected().getEstado().getCodigo().equals("NA")) {
                return "yellow";
            }
            if (getSelected().getEstado().getCodigo().equals("VE")) {
                return "green";
            }
        }

        return null;

    }

    public String calculaColor() {
        if (getSelected() != null) {
            if (getSelected().getResultado() <= 10 && getSelected().getResultadoPresupuesto() <= 10) {

                return "red";
            }
            if (getSelected().getResultado() >= 60 && getSelected().getResultadoPresupuesto() >= 60) {
                return "yellow";
            }
            if (getSelected().getResultado() == 100 && getSelected().getResultadoPresupuesto() == 100) {
                return "green";
            }
        }
        return null;

    }

    public void guardarCambio() {

        if (getSelected() != null) {
            //if (getSelected().getVariable2() != 0) {
            valorVariable = getSelected().getProgramacion().getValorVariable();
            variable2 = getSelected().getVariable2();
            variable = (valor + variable2);
            // resultado = (variable / valorVariable) * 100;
            resultado = ((valor + variable2) / valorVariable) * 100;
            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
            getSelected().setValor(valor);
            getSelected().setVariable2(variable);
            getSelected().setResultado(Math.round(resultado));

            planEstrategicasController.setSelected(getSelected());
            planEstrategicasController.save(null);

            DetallesPlanEstrategicas detallesPlan = ejbFacade.getEntityManager().createNamedQuery("DetallesPlanEstrategicas.findByPlanEstrategicica", DetallesPlanEstrategicas.class
            ).setParameter("planEstrategica", getSelected()).getSingleResult();
            if (getSelected().getResultado() < 30) {
                getSelected().setEstado(new EstadosActividad("RO"));

            }
            if (getSelected().getResultado() > 30) {
                getSelected().setEstado(new EstadosActividad("NA"));
            }
            if (getSelected().getResultado() == 100) {
                getSelected().setEstado(new EstadosActividad("VE"));

            }
            //detallesPan.setEstado(new EstadosInforme("1"));
            //detallesPan.setResponsable(usuario);
            detallesPlan.setFechaHoraUltimoEstado(fecha);
            detallesPlan.setUsuarioUltimoEstado(usuario);
            detallesPlan.setValor(valor);
            // plan.setValorVariable(Long.MAX_VALUE);
            detallesPlan.setVariable2(variable);
            detallesPlan.setValorVariable(valorVariable);
            detallesPlan.setResultado(Math.round(resultado));
            detallesPlanEstrategicasController.setSelected(detallesPlan);
            detallesPlanEstrategicasController.save(null);

            // } else {
            //    JsfUtil.addErrorMessage("No hay ningun cambio que registrar");
            // }
        } else {
            JsfUtil.addErrorMessage("No hay ningun cambio que registrar");
        }
    }

    public void guardarPrepuestoActual() {

        if (getSelected() != null) {
            // if (getSelected().getVariable2() != valor) {
            programaPresupuestario = getSelected().getProgramacion().getProgramaPresupuestario();
            variable2 = getSelected().getVariable2();
            variable = (valor + variable2);
            resultadoPresupuesto = (variable / programaPresupuestario) * 100;
             Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
            getSelected().setValor(valor);
            getSelected().setVariable2(variable);
            getSelected().setResultado(Math.round(resultadoPresupuesto));

            planEstrategicasController.setSelected(getSelected());
            planEstrategicasController.save(null);

            DetallesPlanEstrategicas detallesPlan3 = ejbFacade.getEntityManager().createNamedQuery("DetallesPlanEstrategicas.findByPlanEstrategicica", DetallesPlanEstrategicas.class
            ).setParameter("planEstrategica", getSelected()).getSingleResult();
            if (getSelected().getResultado() < 30) {
                getSelected().setEstado(new EstadosActividad("RO"));

            }
            if (getSelected().getResultado() > 30) {
                getSelected().setEstado(new EstadosActividad("NA"));
            }
            if (getSelected().getResultado() == 100) {
                getSelected().setEstado(new EstadosActividad("VE"));

            }
            detallesPlan3.setFechaHoraUltimoEstado(fecha);
            detallesPlan3.setUsuarioUltimoEstado(usuario);
            detallesPlan3.setValor(valor);
            detallesPlan3.setVariable2(variable);
            detallesPlan3.setResultado(Math.round(resultadoPresupuesto));
            detallesPlanEstrategicasController.setSelected(detallesPlan3);
            detallesPlanEstrategicasController.save(null);

        } else {
            JsfUtil.addErrorMessage("No hay ningun cambio que registrar");
        }

        // }
    }

    public void guardarEdit() {

        if (getSelected().getProgramacion() != null) {

            if (getSelected().getProgramacion().getValorVariable() != nuevaVariable) {
                CambiosValor cambio = cambiosValorController.prepareCreate(null);

                cambio.setCantidadOriginal(getSelected().getProgramacion().getValorVariable());
                cambio.setCantidadFinal(nuevaVariable);
                cambio.setProgramacion(getSelected().getProgramacion());

                getSelected().getProgramacion().setValorVariable(nuevaVariable);
                getSelected().setVariable2(nuevaVariable);
                
                programacionController.setSelected(getSelected().getProgramacion());
                programacionController.save(null);

                cambiosValorController.setSelected(cambio);
                cambiosValorController.saveNew(null);
                // resetParents();
            } else {
                JsfUtil.addErrorMessage("No hay ningun cambio que registrar");
            }
        }
    }
    public void guardarEdit2() {

        if (getSelected() != null) {

            if (getSelected().getVariable2() != nuevaVariable2) {
                CambiosValor cambio = cambiosValorController.prepareCreate(null);

                cambio.setCantidadOriginal(getSelected().getVariable2());
                cambio.setCantidadFinal(nuevaVariable2);
                cambio.setPlanEstrategica(getSelected());

                
                getSelected().setVariable2(nuevaVariable2);
                planEstrategicasController.setSelected(getSelected());
                planEstrategicasController.save(null);

                cambiosValorController.setSelected(cambio);
                cambiosValorController.saveNew(null);
                // resetParents();
            } else {
                JsfUtil.addErrorMessage("No hay ningun cambio que registrar");
            }
        }
    }

    public void guardarSumatoria() {

        if (getSelected() != null) {
            // if (getSelected().getVariable2() != 0) {
            valorVariable = getSelected().getProgramacion().getValorVariable();
            resultado = valor + getSelected().getVariable2();
            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
            getSelected().setValor(valor);
            getSelected().setVariable2(resultado);
            getSelected().setResultado(Math.round(resultado));

            planEstrategicasController.setSelected(getSelected());
            planEstrategicasController.save(null);

            DetallesPlanEstrategicas detallesPlan2 = ejbFacade.getEntityManager().createNamedQuery("DetallesPlanEstrategicas.findByPlanEstrategicica", DetallesPlanEstrategicas.class
            ).setParameter("planEstrategica", getSelected()).getSingleResult();
            if (getSelected().getResultado() < 30) {
                getSelected().setEstado(new EstadosActividad("RO"));

            }
            if (getSelected().getResultado() > 30) {
                getSelected().setEstado(new EstadosActividad("NA"));
            }
            if (getSelected().getResultado() == 100) {
                getSelected().setEstado(new EstadosActividad("VE"));

            }

            detallesPlan2.setValor(valor);
            detallesPlan2.setVariable2(resultado);
            detallesPlan2.setResultado(Math.round(resultado));
            if (detallesPlan2.getTipoDocumentos().getCodigo().equals("CO")) {
                resultado = valor + valorVariable;
                detallesPlan2.setValor(valor);
                detallesPlan2.setVariable2(resultado);
                detallesPlan2.setResultado(Math.round(resultado));
                detallesPlan2.setFechaHoraUltimoEstado(fecha);
                detallesPlan2.setUsuarioUltimoEstado(usuario);
            }
            detallesPlanEstrategicasController.setSelected(detallesPlan2);
            detallesPlanEstrategicasController.save(null);

        } else {
            JsfUtil.addErrorMessage("No hay ningun cambio que registrar");
        }

        // }
    }

    public void actualizarMetasAlcanzada(PlanEstrategicas item) {
        setSelected(item);
        super.save(null);
        setSelected(null);
    }

    public void reporteAvanceResultado(boolean generarPdf) {

        HttpServletResponse httpServletResponse = null;
        try {
            JRBeanCollectionDataSource beanCollectionDataSource = null;

            ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

            // Calendar cal = Calendar.getInstance();
            // cal.setTime(fechaHasta);
            // cal.add(Calendar.DATE, 1);
            // Date nuevaFechaHasta = cal.getTime();
            List<PlanEstrategicas> lista = ejbFacade.getEntityManager().createNamedQuery("PlanEstrategicas.findByPeriodo", PlanEstrategicas.class
            ).setParameter("año", año).getResultList();

            SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
            List<RepAvanceAcciones> listafinal2 = new ArrayList<>();
            RepAvanceAcciones item = null;
            for (PlanEstrategicas det : lista) {

                item = new RepAvanceAcciones();

                item.setObjetivoDescripcion(det.getTiposObjetivos() == null ? "" : (det.getTiposObjetivos() == null ? "" : det.getTiposObjetivos().getObjetivo()));
                item.setAcciones(det.getProgramacion().getAccion() == null ? "" : (det.getProgramacion().getAccion() == null ? "" : det.getProgramacion().getAccion().getDescripcion()));
                item.setMeta(det.getProgramacion().getMeta());
                item.setIndicador(det.getProgramacion().getFormula());
                item.setVariable(det.getVariable2());
                item.setResultado(det.getResultado());
                item.setValorVariable(det.getProgramacion().getValorVariable());
                // item.setAño(det.getProgramacion().getAño());
                // item.setResultadoPresupuestario(det.getResultadoPresupuesto());

                listafinal2.add(item);

            }

            beanCollectionDataSource = new JRBeanCollectionDataSource(listafinal2);

            HashMap map = new HashMap();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            Date fecha = ejbFacade.getSystemDate();

            map.put("fecha", format.format(fecha));
            //map.put("fechaDesde", format2.format(fechaDesde));
            // map.put("fechaHasta", format2.format(fechaHasta));
            map.put("año", año);

            JasperPrint jasperPrint = null;
            ServletOutputStream servletOutputStream = null;
            if (generarPdf) {
                String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteSalidaArticuloNroFormulario.jasper");
                jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                httpServletResponse.addHeader("Content-disposition", "filename=reporte.pdf");

                servletOutputStream = httpServletResponse.getOutputStream();
                JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);

            } else {
                String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteAvanceResultadoExcel.jasper");
                jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                httpServletResponse.addHeader("Content-disposition", "filename=reporte.xlsx");

                servletOutputStream = httpServletResponse.getOutputStream();

                JRXlsxExporter exporter = new JRXlsxExporter();
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
                exporter.exportReport();
            }

            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            e.printStackTrace();

            if (httpServletResponse != null) {
                if (httpServletResponse.getHeader("Content-disposition") == null) {
                    httpServletResponse.addHeader("Content-disposition", "inline");
                } else {
                    httpServletResponse.setHeader("Content-disposition", "inline");
                }

            }
            JsfUtil.addErrorMessage("No se pudo generar el reporte.");

        }
    }
    public void AccionEstrategicaBean() {
        acciones = Arrays.asList(
                new Acciones(14, "Cantidad de AI(Sin trámites)"),
                new Acciones(15, "Cantidad de SD(Sin Enjuiciamiento)"),
                new Acciones(16, "Cantidad Programada"),
                new Acciones(17, "Sesiones Realizada"),
                new Acciones(18, "Cantidad de convenios celebrados"),
                new Acciones(19, "Cantidad de Promociones"),
                new Acciones(20, "Total de Funcionarios"),
                new Acciones(21, "Procedimientos diseñados e implementados"),
                new Acciones(22, "Inversión programada"),
                new Acciones(23, "Total de  presupuesto")
                
        );

    }

    public void seleccionarAccion() {
        AccionEstrategicaBean();
        if (getSelected().getAccion() != null) {
            //if (actividadSeleccionada != null) {
            Acciones a = null;
            for (Acciones acc : acciones) {
                if (acc.getId().equals(getSelected().getAccion().getId())) {
                    accionesSeleccionada = acc;
                    break;
                }
            }

            titulo = accionesSeleccionada.getDescripcion();
            //}
        }
    }
    public void consultar() {
        ChartObjetivoPrincipal chart = new ChartObjetivoPrincipal();
        if (tipo != null && periodo2 != null) {
            chart.setTipo(tipo);
            chart.setPeriodo(periodo2);
        }
        if(tipo == 1){
           porcentajeMisional = (chart.getVariable2() / chart.getValorVariable()) * 100;
        } else {
            porcentajeMisional = 0;
        }
         porcentajeApoyo = (chart.getMetasAlcanzada() / 8) * 100;
        }

}
