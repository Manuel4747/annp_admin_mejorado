package py.com.startic.gestion.controllers;

//import com.google.gson.Gson;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.FacesContext;

import py.com.startic.gestion.models.DocumentosJudiciales;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.validator.Validator;
import jakarta.faces.validator.ValidatorException;
import jakarta.inject.Inject;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;
import org.apache.poi.util.IOUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.models.Archivos;
import py.com.startic.gestion.models.CanalesEntradaDocumentoJudicial;
import py.com.startic.gestion.models.Departamentos;
import py.com.startic.gestion.models.Destinatarios;
import py.com.startic.gestion.models.DetallesDestinatarios;
import py.com.startic.gestion.models.DocumentosResoluciones;
import py.com.startic.gestion.models.EntradasDocumentosResolucion;
import py.com.startic.gestion.models.EnviarADepartamentos;
import py.com.startic.gestion.models.EstadosDocumento;
import py.com.startic.gestion.models.EstadosProcesalesDocumentosJudiciales;
import py.com.startic.gestion.models.EstadosUsuario;
import py.com.startic.gestion.models.FlujosDocumento;
import py.com.startic.gestion.models.OrgChartData;
import py.com.startic.gestion.models.ParametrosSistema;
import py.com.startic.gestion.models.RolesPorUsuarios;
import py.com.startic.gestion.models.SubcategoriasDocumentosJudiciales;
import py.com.startic.gestion.models.TiposDocumentosJudiciales;
import py.com.startic.gestion.models.TiposEnvio;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "entradaDocumentosResolucionesController")
@ViewScoped
public class EntradaDocumentosResolucionesController extends AbstractController<DocumentosResoluciones> implements Validator {

    @Inject
    private EmpresasController empresaController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private EstadosDocumentoController estadoController;
    @Inject
    private DepartamentosController departamentoController;
    @Inject
    private CanalesEntradaDocumentoJudicialController canalesEntradaDocumentoJudicialController;
    @Inject
    private EntradasDocumentosResolucionController entradasDocumentosResolucionController;
    @Inject
    private EstadosDocumentoController estadosDocumentoController;
    @Inject
    private TiposDocumentosJudicialesController tiposDocumentosJudicialesController;
    @Inject
    private EstadosProcesalesDocumentosJudicialesController estadosProcesalesDocumentosJudicialesController;
    @Inject
    private DestinatariosController destinatariosController;
    @Inject
    private ArchivosController archivosController;
    private Collection<Destinatarios> destinatarios;

    private Collection<DocumentosResoluciones> documentosBuscadorItems;

    private String descripcionMesaEntrada;
    private String folios;
    private String descripcion;
    private String nroResolucion;
    private String tituloResolucion;
    private Collection<DocumentosResoluciones> destalles;
    private DocumentosResoluciones destalleSelected;
    private String nuevoRecurrente;
    private CanalesEntradaDocumentoJudicial canal;
    private EntradasDocumentosResolucion entradaDocumentoResolucion;
    private Departamentos departamento;
    private Usuarios usuario;
    private Date fechaDesde;
    private Date fechaHasta;
    private TiposDocumentosJudiciales tipoDocumentoJudicial;
    private SubcategoriasDocumentosJudiciales subcategoriaDocumentoJudicial;
    private List<SubcategoriasDocumentosJudiciales> listaSubcategoriaDocumentoJudicial;
    private Collection<Usuarios> listaUsuariosOpciones;
    private Collection<Usuarios> listaUsuariosTransf;
    private List<Destinatarios> listaDestinatarios;
    private Collection<Destinatarios> detallesDestinatarios;
    private List<Usuarios> listaUsuarios;
    private String contrasena;
    private String oculto;
    private Usuarios responsableDestino;
    private Integer responsableDestinoId;
    private SimpleDateFormat formatAno = new SimpleDateFormat("YY");
    private List<Archivos> listaArchivos;
    private String descripcionArchivo;
    private FlujosDocumento flujoDoc;
    private ParametrosSistema par;
    private Integer activeTab;
    private Archivos docImprimir;
    private String content;
    private String nombre;
    private String url;
    private String endpoint;
    private Date fechaHoraAlta;
    private HttpSession session;
    private String eleccionFiltro;
    private String asuntoFiltro;
    private String textoFiltro;
    private final FiltroURL filtroURL = new FiltroURL();
    private DocumentosResoluciones documentoResolucionSelected;

    private UploadedFile file;
    private UploadedFile fileAnexo;
    private boolean existeDocumento;
    private boolean existeDocumentoAnexo;
    private String tipo;
    private String busqueda;
    private List<UploadedFile> listaFiles;
    private List<UploadedFile> listaFilesEdit;
    private int anexoIndex;

    public String getEleccionFiltro() {
        return eleccionFiltro;
    }

    public void setEleccionFiltro(String eleccionFiltro) {
        this.eleccionFiltro = eleccionFiltro;
    }

    public String getAsuntoFiltro() {
        return asuntoFiltro;
    }

    public void setAsuntoFiltro(String asuntoFiltro) {
        this.asuntoFiltro = asuntoFiltro;
    }

    public String getTextoFiltro() {
        return textoFiltro;
    }

    public void setTextoFiltro(String textoFiltro) {
        this.textoFiltro = textoFiltro;
    }

    public Integer getResponsableDestinoId() {
        return responsableDestinoId;
    }

    public void setResponsableDestinoId(Integer responsableDestinoId) {
        this.responsableDestinoId = responsableDestinoId;
    }

    public Usuarios getResponsableDestino() {
        return responsableDestino;
    }

    public void setResponsableDestino(Usuarios responsableDestino) {
        this.responsableDestino = responsableDestino;
    }

    public String getOculto() {
        return oculto;
    }

    public void setOculto(String oculto) {
        this.oculto = oculto;
    }

    public String getNroResolucion() {
        return nroResolucion;
    }

    public void setNroResolucion(String nroResolucion) {
        this.nroResolucion = nroResolucion;
    }

    public FlujosDocumento getFlujoDoc() {
        return flujoDoc;
    }

    public void setFlujoDoc(FlujosDocumento flujoDoc) {
        this.flujoDoc = flujoDoc;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTituloResolucion() {
        return tituloResolucion;
    }

    public void setTituloResolucion(String tituloResolucion) {
        this.tituloResolucion = tituloResolucion;
    }

    public List<SubcategoriasDocumentosJudiciales> getListaSubcategoriaDocumentoJudicial() {
        return listaSubcategoriaDocumentoJudicial;
    }

    public void setListaSubcategoriaDocumentoJudicial(List<SubcategoriasDocumentosJudiciales> listaSubcategoriaDocumentoJudicial) {
        this.listaSubcategoriaDocumentoJudicial = listaSubcategoriaDocumentoJudicial;
    }

    public SubcategoriasDocumentosJudiciales getSubcategoriaDocumentoJudicial() {
        return subcategoriaDocumentoJudicial;
    }

    public void setSubcategoriaDocumentoJudicial(SubcategoriasDocumentosJudiciales subcategoriaDocumentoJudicial) {
        this.subcategoriaDocumentoJudicial = subcategoriaDocumentoJudicial;
    }

    public Collection<Usuarios> getListaUsuariosTransf() {
        return listaUsuariosTransf;
    }

    public void setListaUsuariosTransf(Collection<Usuarios> listaUsuariosTransf) {
        this.listaUsuariosTransf = listaUsuariosTransf;
    }

    public DocumentosResoluciones getDocumentoResolucionSelected() {
        return documentoResolucionSelected;
    }

    public void setDocumentoResolucionSelected(DocumentosResoluciones documentoResolucionSelected) {
        this.documentoResolucionSelected = documentoResolucionSelected;
    }

    public TiposDocumentosJudiciales getTipoDocumentoJudicial() {
        return tipoDocumentoJudicial;
    }

    public void setTipoDocumentoJudicial(TiposDocumentosJudiciales tipoDocumentoJudicial) {
        this.tipoDocumentoJudicial = tipoDocumentoJudicial;
    }

    public Collection<Destinatarios> getDestinatarios() {
        return destinatarios;
    }

    public void setDestinatarios(Collection<Destinatarios> destinatarios) {
        this.destinatarios = destinatarios;
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

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public Date getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Date fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public String getNuevoRecurrente() {
        return nuevoRecurrente;
    }

    public void setNuevoRecurrente(String nuevoRecurrente) {
        this.nuevoRecurrente = nuevoRecurrente;
    }

    public Collection<DocumentosResoluciones> getDestalles() {
        return destalles;
    }

    public void setDestalles(Collection<DocumentosResoluciones> destalles) {
        this.destalles = destalles;
    }

    public DocumentosResoluciones getDestalleSelected() {
        return destalleSelected;
    }

    public void setDestalleSelected(DocumentosResoluciones destalleSelected) {
        this.destalleSelected = destalleSelected;
    }

    public String getFolios() {
        return folios;
    }

    public void setFolios(String folios) {
        this.folios = folios;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getDescripcionMesaEntrada() {
        return descripcionMesaEntrada;
    }

    public void setDescripcionMesaEntrada(String descripcionMesaEntrada) {
        this.descripcionMesaEntrada = descripcionMesaEntrada;
    }

    public EntradasDocumentosResolucion getEntradaDocumentoResolucion() {
        return entradaDocumentoResolucion;
    }

    public void setEntradaDocumentoResolucion(EntradasDocumentosResolucion entradaDocumentoResolucion) {
        this.entradaDocumentoResolucion = entradaDocumentoResolucion;
    }

    public List<Destinatarios> getListaDestinatarios() {
        return listaDestinatarios;
    }

    public void setListaDestinatarios(List<Destinatarios> listaDestinatarios) {
        this.listaDestinatarios = listaDestinatarios;
    }

    public Collection<Destinatarios> getDetallesDestinatarios() {
        return detallesDestinatarios;
    }

    public void setDetallesDestinatarios(Collection<Destinatarios> detallesDestinatarios) {
        this.detallesDestinatarios = detallesDestinatarios;
    }

    public Collection<Usuarios> getListaUsuariosOpciones() {
        return listaUsuariosOpciones;
    }

    public void setListaUsuariosOpciones(Collection<Usuarios> listaUsuariosOpciones) {
        this.listaUsuariosOpciones = listaUsuariosOpciones;
    }

    public UploadedFile getFileAnexo() {
        return fileAnexo;
    }

    public void setFileAnexo(UploadedFile fileAnexo) {
        this.fileAnexo = fileAnexo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Collection<DocumentosResoluciones> getDocumentosBuscadorItems() {
        return documentosBuscadorItems;
    }

    public void setDocumentosBuscadorItems(Collection<DocumentosResoluciones> documentosBuscadorItems) {
        this.documentosBuscadorItems = documentosBuscadorItems;
    }

    public String getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(String busqueda) {
        this.busqueda = busqueda;
    }

    public List<UploadedFile> getListaFiles() {
        return listaFiles;
    }

    public void setListaFiles(List<UploadedFile> listaFiles) {
        this.listaFiles = listaFiles;
    }

    public List<UploadedFile> getListaFilesEdit() {
        return listaFilesEdit;
    }

    public void setListaFilesEdit(List<UploadedFile> listaFilesEdit) {
        this.listaFilesEdit = listaFilesEdit;
    }

    @PostConstruct
    @Override
    public void initParams() {
        departamento = departamentoController.prepareCreate(null);
        departamento.setId(2); // Mesa entrada
        canal = canalesEntradaDocumentoJudicialController.prepareCreate(null);
        canal.setCodigo("SE");

        par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID).getSingleResult();

        eleccionFiltro = "1";
        activeTab = 0;
        try {
            tipoDocumentoJudicial = this.ejbFacade.getEntityManager().createNamedQuery("TiposDocumentosJudiciales.findByCodigo", TiposDocumentosJudiciales.class).setParameter("codigo", Constantes.TIPO_DOCUMENTO_JUDICIAL_DR).getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
            JsfUtil.addErrorMessage("Error de configuracion. No se puede iniciar pantalla");
            return;
        }

        // listaSubcategoriaDocumentoJudicial = this.ejbFacade.getEntityManager().createNamedQuery("SubcategoriasDocumentosJudiciales.findByTipoDocumentoJudicial", SubcategoriasDocumentosJudiciales.class).setParameter("tipoDocumentoJudicial", tipoDocumentoJudicial).getResultList();
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        int pos = url.lastIndexOf(uri);
        url = url.substring(0, pos);

        String[] array = uri.split("/");
        endpoint = array[1];

        usuario = (Usuarios) session.getAttribute("Usuarios");
        // usuario.setContrasena("");
        fechaDesde = ejbFacade.getSystemDateOnly(-30);
        fechaHasta = ejbFacade.getSystemDateLastSecond();
        super.initParams();
        buscarPorFechaPresentacion();
        documentosBuscadorItems = new ArrayList<>();
    }

    public String getChartUrl(DocumentosResoluciones doc) {
        prepareChart(doc);
        return "/pages/registroDocumentoResolucionesChart/index";
    }

    public String datePattern3() {
        return "dd/MM/yyyy HH:mm:ss";
    }

    public String customFormatDate3(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern3());
            return format.format(date);
        }
        return "";
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object submittedAndConvertedValue) throws ValidatorException {
        Usuarios usuarioElegido = (Usuarios) submittedAndConvertedValue;

        UIInput otherInput = (UIInput) context.getViewRoot().findComponent("@form:contrasena");
        String otherValue = (String) otherInput.getSubmittedValue();

        EstadosUsuario est = new EstadosUsuario("AC");

        Usuarios usu = ejbFacade.getEntityManager().createNamedQuery("Usuarios.control", Usuarios.class).setParameter("usuario", usuarioElegido.getUsuario()).setParameter("contrasena", otherValue).setParameter("estado", est).getSingleResult();

        if (usu == null) {
            JsfUtil.addErrorMessage("Contraseña invalida");
            throw new ValidatorException(new FacesMessage("Contraseña invalida"));
        }

        /*
        String nroMesaEntrada = (String) submittedAndConvertedValue;

        if (nroMesaEntrada == null || nroMesaEntrada.isEmpty()) {
            return; // Let required="true" or @NotNull handle it.
        }
        
        Collection<DocumentosJudiciales> bienes = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByNroMesaEntradaAdministrativa", DocumentosJudiciales.class).setParameter("nroMesaEntrada",nroMesaEntrada).getResultList();

        if (bienes != null) {
            if(bienes.size() > 0 ){
                JsfUtil.addErrorMessage("Nro de Mesa de Entrada ya existe");
                throw new ValidatorException(new FacesMessage("Nro de Mesa de Entrada ya existe"));
            }
        }
         */
    }

    public EntradaDocumentosResolucionesController() {
        // Inform the Abstract parent controller of the concrete DocumentosJudiciales Entity
        super(DocumentosResoluciones.class);
    }

    @RequestScoped
    public void getChartData() {

        String respchart;
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        documentoResolucionSelected = (DocumentosResoluciones) session.getAttribute("entrada_documento_resolucion");

        System.out.println("documentoResolucionSelected:" + documentoResolucionSelected!=null?documentoResolucionSelected.getDescripcion():"documentoResolucionSelected ES NULO");
        List<DetallesDestinatarios> resp = ejbFacade.getEntityManager()
                .createNamedQuery("DetallesDestinatarios.findByDetallesResolucion", DetallesDestinatarios.class)
                .setParameter("documentosResoluciones", documentoResolucionSelected)
                .getResultList();
        System.out.println("REP SIZE: " + (resp!=null && !resp.isEmpty()?resp.size():"-0-"));
        
        ArrayList<OrgChartData> chrd = new ArrayList<>();
        for (DetallesDestinatarios d : resp) {
            Collection<EnviarADepartamentos> enviar = d.getEnviarADepartamentos();
            boolean b = true;
            for(EnviarADepartamentos e: enviar) {
                String fechaHoraAlta = "";
                if (d.getFechaHoraAlta() != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    fechaHoraAlta = sdf.format(d.getFechaHoraAlta());
                }
                OrgChartData ochd;
                if(b){
                    b=false;
                    ochd = new OrgChartData(
                            d.getResponsable().getNombresApellidos(),
                            d.getDepartamento().getNombre(),
                            d.getResponsableAnterior().getNombresApellidos(),
                            d.getTipoDocumento().getDescripcion(), fechaHoraAlta
                    );
                }else{
                    ochd = new OrgChartData(
                            e.getResponsable().getNombresApellidos(),
                            e.getDepartamento().getNombre(),
                            e.getResponsableAnterior().getNombresApellidos(),
                            d.getTipoDocumento().getDescripcion(), fechaHoraAlta
                    );
                }
                
                chrd.add(ochd);
            }
            if (enviar.isEmpty()) {
                String fechaHoraAlta = "";
                if (d.getFechaHoraAlta() != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    fechaHoraAlta = sdf.format(d.getFechaHoraAlta());
                }
                OrgChartData ochd = new OrgChartData(
                        d.getResponsable().getNombresApellidos(),
                        d.getDepartamento().getNombre(),
                        d.getResponsableAnterior().getNombresApellidos(),
                        d.getTipoDocumento().getDescripcion(), fechaHoraAlta
                );
                chrd.add(ochd);
            }
        }
//       Gson gson = new Gson();
  //      respchart = gson.toJson(chrd);
    //    System.out.println("respchart:" + respchart);
      //  PrimeFaces.current().ajax().addCallbackParam("data", respchart);
        //return respchart;
    }

    public void prepareTransferir() {
        prepareTransferir(getSelected());
    }

    public void prepareTransferir(DocumentosResoluciones res) {
        if (res != null) {
            listaUsuariosOpciones = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferir", Usuarios.class).setParameter("tipoDocumento", res.getTipoDocumentoJudicial().getCodigo()).setParameter("estadoDocumentoActual", res.getEstado().getCodigo()).setParameter("rolEncargado", Constantes.ROL_RECIBIRRESOLUCIONESDESDESG).getResultList();

            if (res.getResponsable() != null) {
                List<Usuarios> respAnt = ejbFacade.getEntityManager().createNamedQuery("Usuarios.findById", Usuarios.class).setParameter("id", res.getResponsable().getId()).getResultList();
                if (!respAnt.isEmpty()) {
                    boolean encontro = false;
                    for (Usuarios trans : listaUsuariosOpciones) {
                        if (trans.equals(respAnt.get(0))) {
                            encontro = true;
                        }
                    }

                    if (!encontro) {
                        listaUsuariosOpciones.add(respAnt.get(0));
                    }
                }
            }

            List<FlujosDocumento> flujos = null;
            try {
                flujos = this.ejbFacade.getEntityManager().createNamedQuery("FlujosDocumento.findByEstadoDocumentoActualFin", FlujosDocumento.class).setParameter("tipoDocumento", res.getTipoDocumentoJudicial().getCodigo()).setParameter("estadoDocumentoActual", res.getEstado().getCodigo()).getResultList();

                int contador = -1;
                for (FlujosDocumento flujo : flujos) {

                    EstadosDocumento est = this.ejbFacade.getEntityManager().createNamedQuery("EstadosDocumento.findByCodigo", EstadosDocumento.class).setParameter("codigo", flujo.getEstadoDocumentoFinal()).getSingleResult();

                    Usuarios usu = new Usuarios(contador, est.getDescripcion(), est);
                    listaUsuariosOpciones.add(usu);
                    contador--;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            listaUsuariosOpciones = new ArrayList<>();
        }

        responsableDestino = null;
    }

    public List<Usuarios> getListaUsuarios() {
        if (getSelected() != null && getSelected().getEstado() != null && Constantes.DOCUMENTO_INGRESADO.equals(getSelected().getEstado().getCodigo())) {

            if (getSelected().getTipoDocumentoJudicial() != null) {

                listaUsuarios = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferirDpto", Usuarios.class).setParameter("tipoDocumento", getSelected().getTipoDocumentoJudicial().getCodigo()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).getResultList();
            } else {
                listaUsuarios = new ArrayList<>();
            }

        } else {
            if (getSelected().getTipoDocumentoJudicial() != null && getSelected().getEstado() != null) {
                listaUsuarios = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferirPedido", Usuarios.class).setParameter("tipoDocumento", getSelected().getTipoDocumentoJudicial().getCodigo()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).getResultList();
            } else {
                listaUsuarios = new ArrayList<>();
            }
        }
        return listaUsuarios;

    }

    public void cambiarSubcategorias() {
        listaSubcategoriaDocumentoJudicial = this.ejbFacade.getEntityManager().createNamedQuery("SubcategoriasDocumentosJudiciales.findByTipoDocumentoJudicialEstado", SubcategoriasDocumentosJudiciales.class).setParameter("tipoDocumentoJudicial", tipoDocumentoJudicial).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();
    }

    public void cambiarSubcategoriasEdit() {
        listaSubcategoriaDocumentoJudicial = this.ejbFacade.getEntityManager().createNamedQuery("SubcategoriasDocumentosJudiciales.findByTipoDocumentoJudicialEstado", SubcategoriasDocumentosJudiciales.class).setParameter("tipoDocumentoJudicial", getSelected().getTipoDocumentoJudicial()).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();
    }

    public void prepareEdit() {
        if (getSelected() != null) {
            cambiarSubcategoriasEdit();
            listaArchivos = (List<Archivos>) getSelected().getArchivos();
        }
    }

    public void prepareChart(DocumentosResoluciones doc) {
        setSelected(doc);
        documentoResolucionSelected = getSelected();
        System.out.println("docResolSel:" + getSelected().getDescripcion());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("entrada_documento_resolucion", getSelected());
    }

    public boolean desabilitarBotonCambioEstado() {

        if (getSelected() != null) {
            // if (getSelected().getUsuarioAlta() != null) {
            if (getSelected().getResponsable() != null) {

                if (getSelected().getResponsable().equals(usuario)) {

                    try {
                        flujoDoc = this.ejbFacade.getEntityManager().createNamedQuery("FlujosDocumento.findByEstadoDocumentoActual", FlujosDocumento.class).setParameter("tipoDocumento", getSelected().getTipoDocumentoJudicial().getCodigo()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).getSingleResult();
                        return false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
            //}

        }

        return true;
    }

    @Override
    public DocumentosResoluciones prepareCreate(ActionEvent event) {
        DocumentosResoluciones doc = super.prepareCreate(event);

        entradaDocumentoResolucion = entradasDocumentosResolucionController.prepareCreate(event);
        //entradaDocumentoResolucion.setRecibidoPor(usuario);
        entradaDocumentoResolucion.setNroIdentificacion(obtenerSgteNroMesaEntradaAdministrativa());

        destalles = null;
        destinatarios = null;
        destalleSelected = null;
        folios = "";
        nuevoRecurrente = "";
        descripcion = "";
        nroResolucion = "";
        //subcategoriaDocumentoJudicial = null;

        destalles = null;

        // cambiarSubcategorias();
        return doc;
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
        estadoController.setSelected(null);
        departamentoController.setSelected(null);
        tiposDocumentosJudicialesController.setSelected(null);
        if (this.getSelected() == null && this.getItems() != null) {
            if (!this.getItems().isEmpty()) {
                this.setSelected(getItems().iterator().next());
            }
        }
        obtenerDestinatarios();
        verDocumentoResolucionEnDpto();
        seleccionar();
    }

    /**
     * Sets the "selected" attribute of the Empresas controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEmpresa(ActionEvent event) {
        if (this.getSelected() != null && empresaController.getSelected() == null) {
            empresaController.setSelected(this.getSelected().getEmpresa());
        }
    }

    /**
     * Sets the "selected" attribute of the Usuarios controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareUsuarioAlta(ActionEvent event) {
        if (this.getSelected() != null && usuarioAltaController.getSelected() == null) {
            usuarioAltaController.setSelected(this.getSelected().getUsuarioAlta());
        }
    }

    /**
     * Sets the "selected" attribute of the Usuarios controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareUsuarioUltimoEstado(ActionEvent event) {
        if (this.getSelected() != null && usuarioUltimoEstadoController.getSelected() == null) {
            usuarioUltimoEstadoController.setSelected(this.getSelected().getUsuarioUltimoEstado());
        }
    }

    /**
     * Sets the "selected" attribute of the EstadosDocumento controller in order
     * to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEstado(ActionEvent event) {
        if (this.getSelected() != null && estadoController.getSelected() == null) {
            estadoController.setSelected(this.getSelected().getEstado());
        }
    }

    /**
     * Sets the "selected" attribute of the Departamentos controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareDepartamento(ActionEvent event) {
        if (this.getSelected() != null && departamentoController.getSelected() == null) {
            departamentoController.setSelected(this.getSelected().getDepartamento());
        }
    }

    @Override
    public Collection<DocumentosResoluciones> getItems() {
        return super.getItems2();
    }

    public void prepareVerDocs(DocumentosResoluciones doc) {
        List<Archivos> lista = ejbFacade.getEntityManager()
                .createNamedQuery("Archivos.findByDocumentoResolucionesOrdered", Archivos.class)
                .setParameter("documentosResolucion", doc)
                .getResultList();
        tipo = "";
        for (Archivos ar : lista) {
            if (!ar.getRuta().contains("anexo")) {
                docImprimir = ar;
            }
        }
        //PrimeFaces.current().ajax().update("ExpAcusacionesViewForm");
    }
    
    public void prepareVerDocsLista(Archivos item){
        docImprimir = item;
        tipo = item.getRuta().contains("anexo")?"anexo":"";
    }

    public String getContent() {
        nombre = "";
        try {
            if (docImprimir != null) {

                byte[] fileByte = null;

                if (docImprimir.getRuta() != null) {
                    try {
                        fileByte = Files.readAllBytes(new File(par.getRutaArchivosResoluciones() + "/" + docImprimir.getRuta()).toPath());
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

                    nombre = session.getId() + "_" + simpleDateFormat.format(fecha) + tipo + "." + ext;
                    FileOutputStream outputStream = new FileOutputStream(Constantes.RUTA_ARCHIVOS_TEMP + "/tmp/" + nombre);
                    outputStream.write(fileByte);

                    outputStream.close();

                    // content = new DefaultStreamedContent(new ByteArrayInputStream(fileByte), "application/pdf");
                }
            }
            content = url + "/tmp/" + nombre;
        } catch (final Exception e) {
            e.printStackTrace();
            content = null;
        }

        // return par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/tmp/" + nombre;
        return content;
    }

    public void prepareCerrarDialogoVerDoc() {
        if (docImprimir != null) {
            File f = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/tmp/" + nombre);
            f.delete();

            docImprimir = null;
        }
    }

    public void prepareAlzarArchivo() {
        descripcionArchivo = "";
    }

    public boolean deshabilitarFechaDesdeFiltro() {
        return !(eleccionFiltro == null ? false : eleccionFiltro.equals("1"));
    }

    public boolean deshabilitarFechaHastaFiltro() {
        return !(eleccionFiltro == null ? false : eleccionFiltro.equals("1"));
    }

    public boolean deshabilitarAsuntoFiltro() {
        return !(eleccionFiltro == null ? false : eleccionFiltro.equals("2"));
    }

    public boolean deshabilitarTextoFiltro() {
        return !(eleccionFiltro == null ? false : eleccionFiltro.equals("3"));
    }

    public boolean desabilitarBotonAlzarArchivo() {
        return false;
    }

    public boolean deshabilitarVerDoc(DocumentosResoluciones doc) {
        verificarDocs(doc);
        return !existeDocumento;
    }

    public boolean deshabilitarVerDocAnexo(DocumentosResoluciones doc) {
        verificarDocs(doc);
        return !existeDocumentoAnexo;
    }

    public void verificarDocs(DocumentosResoluciones doc) {
        if (doc != null) {
            existeDocumento = false;
            existeDocumentoAnexo = false;
            // if (doc.getDepartamento().equals(usuario.getDepartamento())) {
            try {
                List<Archivos> lista = ejbFacade.getEntityManager().createNamedQuery("Archivos.findByDocumentoResolucionesOrdered", Archivos.class).setParameter("documentosResolucion", doc).getResultList();

                for (Archivos ar : lista) {
                    if (ar.getRuta().contains("anexo")) {
                        existeDocumentoAnexo = true;
                    } else {
                        existeDocumento = true;
                    }
                }

            } catch (Exception e) {
                existeDocumento = false;
                existeDocumentoAnexo = false;
            }
            // }
        }
    }

    private void obtenerDestinatarios() {
        if (getSelected() != null) {
            listaDestinatarios = ejbFacade.getEntityManager().createNamedQuery("Destinatarios.findByDocumentoResolucion", Destinatarios.class).setParameter("documentosResoluciones", getSelected()).getResultList();
        } else {
            listaDestinatarios = new ArrayList<>();
        }
    }

    private void seleccionar() {
        if (getSelected() != null) {
            detallesDestinatarios = this.ejbFacade.getEntityManager().createNamedQuery("Destinatarios.findByDetallesResolucion", Destinatarios.class).setParameter("documentosResoluciones", getSelected()).getResultList();
        } else {
            detallesDestinatarios = new ArrayList<>();//null;
        }

    }

    public void alzarArchivo(Archivos arch, UploadedFile elFile, boolean esAnexo) {
        anexoIndex++;
        if (getSelected() != null) {

            if (elFile == null) {
                return;
            } else if (elFile.getContent().length == 0) {
                return;
            }

            Date fecha = ejbFacade.getSystemDate();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String nombreArchivo = simpleDateFormat.format(fecha);
            nombreArchivo += "_" + getSelected().getId() + (esAnexo ? "_anexo"+anexoIndex : "") + "_.pdf";

            boolean rr = crearArchivoEnUbicacion(elFile, par.getRutaArchivosResoluciones() + File.separator + nombreArchivo);
            if (!rr) {
                return;
            }
            if(getSelected().getArchivos()==null) {
                getSelected().setArchivos(new ArrayList<>());
            }

            if (arch == null) {
                Archivos archivo = new Archivos();

                archivo.setDocumentosResolucion(getSelected());
                archivo.setDescripcion(getSelected().getDescripcion());
                archivo.setRuta(nombreArchivo);
                archivo.setFechaHoraAlta(fecha);
                // archivo.setTipoDocumento(tipoDocumentoJudicial);
                archivo.setFechaHoraUltimoEstado(fecha);
                archivo.setUsuarioAlta(usuario);
                archivo.setUsuarioUltimoEstado(usuario);

                archivosController.setSelected(archivo);
                archivosController.saveNew(null);
                getSelected().getArchivos().add(archivo);

            } else {
                arch.setDocumentosResolucion(getSelected());
                arch.setDescripcion(getSelected().getDescripcion());
                arch.setRuta(nombreArchivo);
                arch.setFechaHoraAlta(fecha);
                arch.setFechaHoraUltimoEstado(fecha);
                // arch.setTipoDocumento(tipoDocumentoJudicial);
                arch.setUsuarioAlta(usuario);
                arch.setUsuarioUltimoEstado(usuario);

                archivosController.setSelected(arch);
                archivosController.save(null);
                getSelected().getArchivos().add(arch);
            }
        }

    }

    public boolean crearArchivoEnUbicacion(UploadedFile fileToUpload, String nombreArchivoCompleto) {

        byte[] bytes = null;
        try {
            bytes = IOUtils.toByteArray(fileToUpload.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
            JsfUtil.addErrorMessage("Error al leer archivo");
            return false;
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(nombreArchivoCompleto);
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            JsfUtil.addErrorMessage("No se pudo guardar archivo");
            fos = null;
            return false;
        } catch (IOException ex) {
            ex.printStackTrace();
            JsfUtil.addErrorMessage("No se pudo guardar archivo");
            fos = null;
            return false;
        }
        return true;
    }

    public void buscarPorTexto(String criterio) {
        List<DocumentosResoluciones> lista;
        String criterioFinal = "%" + criterio + "%";
        lista = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosResoluciones.findByNroMesaEntradaOrderedDptoTipoDoc", DocumentosResoluciones.class).setParameter("nroMesaEntrada", criterioFinal).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("departamento", departamento).setParameter("tipoDocumentoJudicial", tipoDocumentoJudicial).getResultList();
        setItems(lista);

    }

    public void buscarPorAsunto(String criterio) {
        List<DocumentosResoluciones> lista;
        String criterioFinal = "%" + criterio + "%";
        lista = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosResoluciones.findByDescripcionMesaEntradaOrderedDptoTipoDoc", DocumentosResoluciones.class).setParameter("descripcion", criterioFinal).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("departamento", departamento).setParameter("tipoDocumentoJudicial", tipoDocumentoJudicial).getResultList();

        setItems(lista);

    }

    public void buscarPorFiltro() {
        if (eleccionFiltro != null) {
            switch (eleccionFiltro) {
                case "1":
                    long diffInMillies = Math.abs(fechaHasta.getTime() - fechaDesde.getTime());
                    long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                    if (diff > 31) {
                        JsfUtil.addErrorMessage("El rango de fecha no puede ser de mas de 31 dias");
                        return;
                    }
                    buscarPorFechaPresentacion();
                    break;
                case "2":
                    if (asuntoFiltro != null) {
                        if (!asuntoFiltro.equals("")) {
                            buscarPorAsunto(asuntoFiltro);
                        } else {
                            JsfUtil.addErrorMessage("Debe completar descripción");
                            return;
                        }
                    } else {
                        JsfUtil.addErrorMessage("Debe completar descripción.");
                        return;
                    }
                    break;
                case "3":
                    if (textoFiltro != null) {
                        if (!textoFiltro.equals("")) {
                            buscarPorTexto(textoFiltro);
                        } else {
                            JsfUtil.addErrorMessage("Debe completar nro mesa entrada");
                        }
                    } else {
                        JsfUtil.addErrorMessage("Debe completar nro mesa entrada.");
                    }
                default:
                    break;
            }

        }
    }

    public void buscarPorFechaPresentacion() {
        if (fechaDesde == null || fechaHasta == null) {
            JsfUtil.addErrorMessage("Debe ingresar Rango de Fechas");
        } else {
            setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosResoluciones.findOrderedDptoTipoDoc", DocumentosResoluciones.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("departamento", departamento).setParameter("tipoDocumentoJudicial", tipoDocumentoJudicial).getResultList());
        }
    }

    public boolean verDocumentoResolucionEnDpto() {

        try {
            this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuarioRol", FlujosDocumento.class).setParameter("usuario", usuario.getId()).setParameter("rol", 51).getSingleResult();
            return true;
        } catch (Exception e) {
            // e.printStackTrace();
        }

        return false;
    }

    public void agregar() {
        if (destalles == null) {
            destalles = new ArrayList<>();
        }

        if ("".equals(descripcionMesaEntrada)) {
            JsfUtil.addErrorMessage("Debe completar la descripcion");
            return;
        }

        if (folios == null) {
            JsfUtil.addErrorMessage("Debe ingresar folios");
            return;
        }

        if ("".equals(folios)) {
            JsfUtil.addErrorMessage("Debe ingresar folios");
            return;
        }

        boolean encontro = false;

        for (DocumentosResoluciones dest : destalles) {
            if (dest.getDescripcion().equals(descripcion)) {
                encontro = true;
                break;
            }
        }

        if (!encontro) {
            DocumentosResoluciones dea = super.prepareCreate(null);

            SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");

            Date fecha = ejbFacade.getSystemDate();

            // dea.setFolios(folios);
            dea.setDescripcion(descripcion);

            dea.setId(Integer.valueOf(format.format(fecha)));

            destalles.add(dea);

            folios = "";
            descripcion = null;
        } else {
            JsfUtil.addErrorMessage("Un documento con la misma descripcion ya fue agregado anteriormente.");
        }

    }

    public void borrarDetalle() {
        if (destalles != null && destalleSelected != null) {
            for (DocumentosResoluciones dest : destalles) {
                if (dest.getId().equals(destalleSelected.getId())) {
                    destalles.remove(dest);
                    break;
                }
            }
        }
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

    /*
    public String obtenerSgteNroMesaEntradaAdministrativa() {
        jakarta.persistence.Query query = ejbFacade.getEntityManager().createNativeQuery(
                "select ifnull(max(CONVERT(nro_mesa_entrada,UNSIGNED INTEGER)),0) as VALOR from documentos_judiciales as d, entradas_documentos_judiciales as e where d.entrada_documento = e.id AND d.tipo_documento_judicial = 'AD' AND nro_mesa_entrada not like 'AUTO%';", NroMesaEntrada.class);

        NroMesaEntrada cod = (NroMesaEntrada) query.getSingleResult();

        return String.valueOf(cod.getCodigo() + 1);
    }
     */
    public String obtenerSgteNroMesaEntradaAdministrativa() {

        Date fecha = ejbFacade.getSystemDate();

        jakarta.persistence.Query query = ejbFacade.getEntityManager().createNativeQuery(
                // "select ifnull(max(CONVERT(nro_mesa_entrada,UNSIGNED INTEGER)),0) as VALOR from documentos_judiciales as d, entradas_documentos_judiciales as e where d.entrada_documento = e.id AND d.tipo_documento_judicial = 'AD' AND nro_mesa_entrada not like 'AUTO%';", NroMesaEntrada.class);
                //"select ifnull(max(CONVERT(substring(nro_mesa_entrada, 1, LENGTH(nro_mesa_entrada) - 3),UNSIGNED INTEGER)),0) as VALOR from documentos_judiciales as d, entradas_documentos_judiciales as e where d.entrada_documento = e.id AND d.tipo_documento_judicial = 'AD' AND nro_mesa_entrada not like 'AUTO%' AND nro_mesa_entrada like '%-" + formatAno.format(fecha) + "'", NroMesaEntrada.class);
                "select ifnull(max(CONVERT(substring(nro_identificacion, 1, LENGTH(nro_identificacion) - 3),UNSIGNED INTEGER)),0) as VALOR from documentos_resoluciones as d, entradas_documentos_resolucion as e where d.entrada_documento = e.id AND d.tipo_documento_judicial in ('AD','JU','DR') AND nro_identificacion not like 'AUTO%' AND nro_identificacion like '%-" + formatAno.format(fecha) + "'", NroResolucion.class);

        NroResolucion cod = (NroResolucion) query.getSingleResult();
        return Utils.padLeft(String.valueOf(cod.getCodigo() + 1) + "-" + formatAno.format(fecha), "0", 6);
    }

    public void saveNew() {
        try {
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            boolean guardando = (boolean) ((params.get("guardandoNew") == null) ? false : params.get("guardandoNew"));
            if (!guardando) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("guardandoNew", true);
                if (getSelected() != null) {
                    //if (destalles != null) {
                    //if (destalles.size() > 0) {
                    if (entradaDocumentoResolucion.getNroIdentificacion() != null) {
                        if (!entradaDocumentoResolucion.getNroIdentificacion().equals("")) {
                            if (tipoDocumentoJudicial != null) {
                                if (entradaDocumentoResolucion != null) {
                                    if (entradaDocumentoResolucion.getNroIdentificacion() == null) {
                                        JsfUtil.addErrorMessage("Debe ingresar Nro Mesa Entrada");
                                        return;
                                    } else if ("".equals(entradaDocumentoResolucion.getNroIdentificacion())) {
                                        JsfUtil.addErrorMessage("Debe ingresar Nro Mesa Entrada");
                                        return;
                                    }
                                }

                                EstadosDocumento estado = null;
                                try {
                                    estado = this.ejbFacade.getEntityManager().createNamedQuery("EstadosDocumento.findByCodigo", EstadosDocumento.class).setParameter("codigo", "-1").getSingleResult();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    JsfUtil.addErrorMessage("Error de configuracion. No se pudo guardar documento");
                                    return;
                                }

                                Date fecha = ejbFacade.getSystemDate();
                                EntradasDocumentosResolucion entradaDoc = entradasDocumentosResolucionController.prepareCreate(null);

                                entradaDoc.setFechaHoraUltimoEstado(fecha);
                                entradaDoc.setUsuarioUltimoEstado(usuario);
                                entradaDoc.setFechaHoraAlta(fecha);
                                entradaDoc.setUsuarioAlta(usuario);
                                entradaDoc.setEmpresa(usuario.getEmpresa());
                                entradaDoc.setNroIdentificacion(obtenerSgteNroMesaEntradaAdministrativa());

                                //entradasDocumentosJudicialesController.setSelected(entradaDoc);
                                //entradasDocumentosJudicialesController.saveNew(null);
                                getSelected().setFechaHoraUltimoEstado(fecha);
                                getSelected().setUsuarioUltimoEstado(usuario);
                                getSelected().setFechaHoraAlta(fecha);
                                getSelected().setUsuarioAlta(usuario);
                                getSelected().setEmpresa(usuario.getEmpresa());
                                getSelected().setDepartamento(usuario.getDepartamento());
                                getSelected().setEntradaDocumento(entradaDoc);
                                getSelected().setDescripcion(descripcion);
                                getSelected().setTituloResolucion(tituloResolucion);
                                getSelected().setNroResolucion(nroResolucion);
                                getSelected().setEstadoProcesal(estado.getDescripcion());
                                //  getSelected().setFolios(folios);
                                // getSelected().setFechaResolucion(fecha);
                                getSelected().setFechaRegistro(fecha);
                                getSelected().setEstado(estado);

                                getSelected().setResponsable(usuario);
                                getSelected().setCanalEntradaDocumentoJudicial(canal);
                                getSelected().setTipoDocumentoJudicial(tipoDocumentoJudicial);
                                // getSelected().setSubcategoriaDocumentoJudicial(subcategoriaDocumentoJudicial);
                                getSelected().setMostrarWeb("SI");

                                super.saveNew(null);
                                if (file != null && file.getContent().length != 0) {
                                    alzarArchivo(null, file, false);
                                }
                                /*
                                if (fileAnexo != null && fileAnexo.getContent().length != 0) {
                                    alzarArchivo(null, fileAnexo, true);
                                }
                                */
                                //EntradasDocumentosJudiciales ent = this.ejbFacade.getEntityManager().createNamedQuery("EntradasDocumentosResolucion.findById", EntradasDocumentosResolucion.class).setParameter("id", entradasDocumentosJudicialesController.getSelected().getId()).getSingleResult();
                                if (estado == null) {

                                    estado = estadosDocumentoController.prepareCreate(null);

                                    estado.setCodigo("-1");

                                    estado.setDescripcion("Entro por Secretaria General");
                                    estado.setEmpresa(usuario.getEmpresa());

                                    estadoController.setSelected(estado);

                                    estadoController.saveNew(null);
                                }
                                anexoIndex = 0;
                                if (listaFiles != null) {
                                    for (UploadedFile f : listaFiles) {
                                        byte[] bytes = null;
                                        if (f != null) {
                                            try {
                                                bytes = IOUtils.toByteArray(f.getInputStream());
                                            } catch (IOException ex) {
                                                ex.printStackTrace();
                                                JsfUtil.addErrorMessage("Error al leer archivo");
                                                return;
                                            }
                                            //alzarArchivo("Archivo adjunto", bytes, getSelected());
                                            alzarArchivo(null, f, true);
                                        }
                                    }
                                }

                                /* EstadosProcesalesDocumentosJudiciales estadoProc = estadosProcesalesDocumentosJudicialesController.prepareCreate(null);

                                estadoProc.setUsuarioAlta(usuario);
                                estadoProc.setUsuarioUltimoEstado(usuario);
                                estadoProc.setFechaHoraAlta(fecha);
                                estadoProc.setFechaHoraUltimoEstado(fecha);
                                estadoProc.setEmpresa(usuario.getEmpresa());
                                estadoProc.setEstadoProcesal(estado.getDescripcion());
                                estadoProc.s(getSelected());

                                estadosProcesalesDocumentosJudicialesController.setSelected(estadoProc);
                                estadosProcesalesDocumentosJudicialesController.saveNew(null);*/
                                // getSelected().setEstadoProcesalDocumentoJudicial(estadoProc);
                                getSelected().setUsuarioEstadoProcesal(usuario);
                                getSelected().setFechaHoraEstadoProcesal(fecha);

                                super.save(null);

                                if (fechaDesde == null) {
                                    fechaDesde = ejbFacade.getSystemDateOnly(-30);
                                }
                                if (fechaHasta == null) {
                                    fechaHasta = ejbFacade.getSystemDateOnly();
                                }

                                buscarPorFechaPresentacion();

                            } else {
                                JsfUtil.addErrorMessage("Tipo Documento: campo requerido");
                            }
                        } else {
                            JsfUtil.addErrorMessage("Nro Mesa Entrada: campo requerido");
                        }
                    } else {
                        JsfUtil.addErrorMessage("Nro Mesa Entrada: campo requerido");
                    }
                    /*    } else {
                    JsfUtil.addErrorMessage("Debe guardar al menos un documento");
                }
            } else {
                JsfUtil.addErrorMessage("Debe guardar al menos un documento");
            }*/
                }
            }
        } finally {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("guardandoNew", false);
        }
    }

    public void save() {
        try {
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            boolean guardando = (boolean) ((params.get("guardandoEdit") == null) ? false : params.get("guardandoEdit"));
            if (!guardando) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("guardandoEdit", true);
                if (getSelected() != null) {
                    if (getSelected().getResponsable().getDepartamento().getId().equals(usuario.getDepartamento().getId())) {

                        Date fecha = ejbFacade.getSystemDate();

                        getSelected().setFechaHoraUltimoEstado(fecha);
                        getSelected().setUsuarioUltimoEstado(usuario);
                        // getSelected().setDepartamento(getSelected().getResponsable().getDepartamento());
                        // getSelected().setTituloResolucion(tituloResolucion);
                        // getSelected().setDescripcion(descripcion);

                        super.save(null);
                        this.verDocumentoResolucionEnDpto();

                        List<Archivos> lista = ejbFacade.getEntityManager().createNamedQuery("Archivos.findByDocumentoResolucionesOrdered", Archivos.class).setParameter("documentosResolucion", getSelected()).getResultList();

                        if (lista.isEmpty()) {
                            if (file != null && file.getContent().length != 0) {
                                alzarArchivo(null, file, false);
                            }
                            if (fileAnexo != null && fileAnexo.getContent().length != 0) {
                                alzarArchivo(null, fileAnexo, true);
                            }
                        } else {
                            for (Archivos ar : lista) {
                                boolean esAnexo = ar.getRuta().contains("anexo");
                                alzarArchivo(ar, esAnexo ? fileAnexo : file, esAnexo);
                            }
                        }

                    } else {
                        JsfUtil.addErrorMessage("Solo el responsable del documento puede editarlo");
                    }

                }
            }
        } finally {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("guardandoEdit", false);
        }
    }

    public void guardarTranferirDocumento() {
        if (getSelected() != null) {
            for (Usuarios usu : listaUsuariosTransf) {
                // if (usu.getId().equals(responsableDestinoId)) {
                responsableDestino = usu;
                // }
                //if (usu.isSeleccionado()) {
                Date fecha = ejbFacade.getSystemDate();
                Destinatarios dest = destinatariosController.prepareCreate(null);

                dest.setFechaHoraUltimoEstado(fecha);
                dest.setUsuarioUltimoEstado(usuario);
                dest.setFechaHoraAlta(fecha);
                dest.setUsuarioAlta(usuario);
                dest.setEmpresa(usuario.getEmpresa());

                dest.setDocumentosResoluciones(getSelected());
                dest.setDepartamento(responsableDestino.getDepartamento());
                dest.setResponsable(responsableDestino);
                dest.setResponsableAnterior(getSelected().getResponsable());

                if (responsableDestino.getDepartamento().getId() == 47) {
                    dest.setTipoDocumento(new TiposDocumentosJudiciales("DE"));
                    dest.setEstado(new EstadosDocumento("-3"));
                    dest.setTipoEnvio(new TiposEnvio("DE"));
                }
                if (responsableDestino.getDepartamento().getId() == 27) {
                    dest.setTipoDocumento(new TiposDocumentosJudiciales("AL"));
                    dest.setEstado(new EstadosDocumento("-3"));
                    dest.setTipoEnvio(new TiposEnvio("DE"));
                }
                if (responsableDestino.getDepartamento().getId() == 43) {
                    dest.setTipoDocumento(new TiposDocumentosJudiciales("SG"));
                    dest.setEstado(new EstadosDocumento("-3"));
                    dest.setTipoEnvio(new TiposEnvio("DE"));
                }
                if (responsableDestino.getDepartamento().getId() == 16) {
                    dest.setTipoDocumento(new TiposDocumentosJudiciales("AG"));
                    dest.setEstado(new EstadosDocumento("-3"));
                    dest.setTipoEnvio(new TiposEnvio("DE"));
                }
                if (responsableDestino.getDepartamento().getId() == 4) {
                    dest.setTipoDocumento(new TiposDocumentosJudiciales("AF"));
                    dest.setEstado(new EstadosDocumento("-3"));
                    dest.setTipoEnvio(new TiposEnvio("DE"));
                }
                if (responsableDestino.getDepartamento().getId() == 46) {
                    dest.setTipoDocumento(new TiposDocumentosJudiciales("GG"));
                    dest.setEstado(new EstadosDocumento("-3"));
                    dest.setTipoEnvio(new TiposEnvio("DE"));
                }
                if (responsableDestino.getDepartamento().getId() == 10) {
                    dest.setTipoDocumento(new TiposDocumentosJudiciales("TH"));
                    dest.setEstado(new EstadosDocumento("-3"));
                    dest.setTipoEnvio(new TiposEnvio("DE"));
                }
                destinatariosController.setSelected(dest);
                destinatariosController.saveNew(null);

                getSelected().setFechaHoraUltimoEstado(fecha);
                getSelected().setUsuarioUltimoEstado(usuario);
                EstadosDocumento estado = ejbFacade.getEntityManager().createNamedQuery("EstadosDocumento.findByCodigo", EstadosDocumento.class).setParameter("codigo", Constantes.DOCUMENTOREENVIAR).getSingleResult();

                getSelected().setEstado(estado);
                setSelected(getSelected());

                super.save(null);

                //}
                // }
            }
        }
    }
     public void saveDpto2() {
        if (getSelected() != null) {
            if (getSelected().getResponsable() != null) {
            for (Usuarios usu : listaUsuariosTransf) {
                // if (usu.getId().equals(responsableDestinoId)) {
                responsableDestino = usu;
                // }
                //if (usu.isSeleccionado()) {
                Date fecha = ejbFacade.getSystemDate();

                getSelected().setFechaHoraUltimoEstado(fecha);
                getSelected().setResponsable(responsableDestino);
                getSelected().setResponsableAnterior(usuario);
                getSelected().setUsuarioUltimoEstado(usuario);
                EstadosDocumento estado = ejbFacade.getEntityManager().createNamedQuery("EstadosDocumento.findByCodigo", EstadosDocumento.class).setParameter("codigo", Constantes.ENVIADOPARAVERIFICAR).getSingleResult();

                getSelected().setEstado(estado);
                setSelected(getSelected());
              
                super.save(null);

                //}
                 }
            }
        }
    }
     
    public void saveDpto() {

        if (getSelected() != null) {
            //if (getSelected().getResponsable().getDepartamento().getId().equals(usuario.getDepartamento().getId())) {
            Date fecha = ejbFacade.getSystemDate();

            FlujosDocumento flujoDoc = null;
            EstadosDocumento estado = null;

            for (Usuarios usu : listaUsuariosOpciones) {
                if (usu.getId().equals(responsableDestinoId)) {
                    responsableDestino = usu;
                }
            }

            if (responsableDestino.getId() > 0) {
                if (responsableDestino.equals(getSelected().getResponsableAnterior())) {
                    estado = getSelected().getEstadoAnterior();
                } else {
                    try {
                        RolesPorUsuarios rol = this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuarioRolFlujo", RolesPorUsuarios.class).setParameter("usuario", responsableDestino.getId()).getSingleResult();

                        flujoDoc = this.ejbFacade.getEntityManager().createNamedQuery("FlujosDocumento.findSgteEstadoSegunRol", FlujosDocumento.class).setParameter("tipoDocumento", getSelected().getTipoDocumentoJudicial().getCodigo()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).setParameter("rolFinal", rol.getRoles()).getSingleResult();
                    } catch (Exception e) {
                        e.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo desterminar flujo del documento. Documento no se puede transferir");
                        return;
                    }

                    try {
                        // Codigo de enviado a secretaria
                        estado = this.ejbFacade.getEntityManager().createNamedQuery("EstadosDocumento.findByCodigo", EstadosDocumento.class
                        ).setParameter("codigo", flujoDoc.getEstadoDocumentoFinal()).getSingleResult();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo desterminar sgte estado. Documento no se puede transferir");
                        return;
                    }
                }

            } else {
                estado = responsableDestino.getEstadoDocumentoFinal();
            }

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
            if (responsableDestino.getId() > 0) {
                getSelected().setResponsableAnterior(getSelected().getResponsable());
                getSelected().setDepartamentoAnterior(getSelected().getDepartamento());
                getSelected().setResponsable(responsableDestino);
                getSelected().setDepartamento(responsableDestino.getDepartamento());
            }
            getSelected().setEstadoAnterior(getSelected().getEstado());
            getSelected().setEstado(estado);
            getSelected().setEstadoProcesal(estado.getDescripcion());

            /* EstadosProcesalesDocumentosJudiciales estadoProc = estadosProcesalesDocumentosJudicialesController.prepareCreate(null);

            estadoProc.setUsuarioAlta(usuario);
            estadoProc.setUsuarioUltimoEstado(usuario);
            estadoProc.setFechaHoraAlta(fecha);
            estadoProc.setFechaHoraUltimoEstado(fecha);
            estadoProc.setEmpresa(usuario.getEmpresa());
            estadoProc.setEstadoProcesal(estado.getDescripcion());
            estadoProc.setDocumentoResoluciones(getSelected());

            // estadosProcesalesDocumentosJudicialesController.setSelected(estadoProc);
            // estadosProcesalesDocumentosJudicialesController.saveNew2(null);
            getSelected().setEstadoProcesalDocumentoJudicial(estadoProc);
            getSelected().setFechaHoraEstadoProcesal(fecha);
            getSelected().setUsuarioEstadoProcesal(usuario);

            super.save(null);*/
            for (Destinatarios dest : destinatarios) {
                dest.setDocumentosResoluciones(getSelected());
                dest.setFechaHoraUltimoEstado(fecha);
                dest.setUsuarioUltimoEstado(usuario);
                dest.setFechaHoraAlta(fecha);
                dest.setUsuarioAlta(usuario);
                dest.setEmpresa(usuario.getEmpresa());
                dest.setResponsable(responsableDestino);
                dest.setDepartamento(responsableDestino.getDepartamento());
                dest.setId(null);

                destinatariosController.setSelected(dest);

                destinatariosController.saveNew(null);

            }

            /*
            if (col != null) {
                for (DocumentosJudiciales doc : col) {

                    doc.setFechaHoraUltimoEstado(fecha);
                    doc.setUsuarioUltimoEstado(usuario);
                    doc.setResponsable(resp);
                    doc.setDepartamento(resp.getDepartamento());
                    doc.setEstado(estado);
                    doc.setEstadoProcesal(estado.getDescripcion());

                    //setSelected(doc);
                    //super.save(null);
                    EstadosProcesalesDocumentosJudiciales estadoProc = estadosProcesalesDocumentosJudicialesController.prepareCreate(null);

                    estadoProc.setUsuarioAlta(usuario);
                    estadoProc.setUsuarioUltimoEstado(usuario);
                    estadoProc.setFechaHoraAlta(fecha);
                    estadoProc.setFechaHoraUltimoEstado(fecha);
                    estadoProc.setEmpresa(usuario.getEmpresa());
                    estadoProc.setEstadoProcesal(estado.getDescripcion());
                    estadoProc.setDocumentoJudicial(doc);
                    
                                        // estadosProcesalesDocumentosJudicialesController.setSelected(estadoProc);
                                         //estadosProcesalesDocumentosJudicialesController.saveNew2(null);
                     
                    doc.setEstadoProcesalDocumentoJudicial(estadoProc);
                    doc.setFechaHoraEstadoProcesal(fecha);
                    doc.setUsuarioEstadoProcesal(usuario);

                    setSelected(doc);

                    super.save(null);
                }

                buscarPorFechaPresentacion();
           
        }
             */
 /*
            } else {
                JsfUtil.addErrorMessage("Solo el responsable del documento puede transferirlo");
            }*/
        }
    }

    /*
    public void saveNew() {
        if (getSelected() != null) {
            if (destalles != null) {
                if (destalles.size() > 0) {

                    if (entradaDocumentoJudicial.getNroMesaEntrada() != null) {
                        if (!entradaDocumentoJudicial.getNroMesaEntrada().equals("")) {
                            if (tipoDocumentoJudicial != null) {
                                if (subcategoriaDocumentoJudicial != null) {
                                    if (entradaDocumentoJudicial != null) {
                                        if (entradaDocumentoJudicial.getNroMesaEntrada() == null) {
                                            JsfUtil.addErrorMessage("Debe ingresar Nro Mesa Entrada");
                                            return;
                                        } else if ("".equals(entradaDocumentoJudicial.getNroMesaEntrada())) {
                                            JsfUtil.addErrorMessage("Debe ingresar Nro Mesa Entrada");
                                            return;
                                        }
                                    }

                                    Date fecha = ejbFacade.getSystemDate();
                                    EntradasDocumentosResolucion entradaDoc = entradasDocumentosJudicialesController.prepareCreate(null);

                                    entradaDoc.setFechaHoraUltimoEstado(fecha);
                                    entradaDoc.setUsuarioUltimoEstado(usuario);
                                    entradaDoc.setFechaHoraAlta(fecha);
                                    entradaDoc.setUsuarioAlta(usuario);
                                    entradaDoc.setEmpresa(usuario.getEmpresa());
                                    if (entradaDocumentoJudicial.getRecibidoPor() != null) {
                                        entradaDoc.setRecibidoPor(entradaDocumentoJudicial.getRecibidoPor());
                                    } else {
                                        entradaDoc.setRecibidoPor(usuario);
                                    }
                                    entradaDoc.setEntregadoPor(entradaDocumentoJudicial.getEntregadoPor());
                                    entradaDoc.setTelefono(entradaDocumentoJudicial.getTelefono());
                                    entradaDoc.setNroCedulaRuc(entradaDocumentoJudicial.getNroCedulaRuc());
                                    entradaDoc.setNroMesaEntrada(entradaDocumentoJudicial.getNroMesaEntrada());
                                    entradaDoc.setNroMesaEntrada(obtenerSgteNroMesaEntradaAdministrativa());

                                    entradasDocumentosJudicialesController.setSelected(entradaDoc);

                                    entradasDocumentosJudicialesController.saveNew(null);

                                    //EntradasDocumentosJudiciales ent = this.ejbFacade.getEntityManager().createNamedQuery("EntradasDocumentosResolucion.findById", EntradasDocumentosResolucion.class).setParameter("id", entradasDocumentosJudicialesController.getSelected().getId()).getSingleResult();
                                    EstadosDocumento estado = null;
                                    try {
                                        estado = this.ejbFacade.getEntityManager().createNamedQuery("EstadosDocumento.findByCodigo", EstadosDocumento.class).setParameter("codigo", "-1").getSingleResult();
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }

                                    if (estado == null) {

                                        estado = estadosDocumentoController.prepareCreate(null);

                                        estado.setCodigo("-1");

                                        estado.setDescripcion("Entro por Mesa de Entrada");
                                        estado.setEmpresa(usuario.getEmpresa());

                                        estadoController.setSelected(estado);

                                        estadoController.saveNew(null);
                                    }

                                    Iterator<DocumentosJudiciales> it = destalles.iterator();

                                    DocumentosJudiciales doc = null;

                                    while (it.hasNext()) {
                                        doc = it.next();

                                        DocumentosJudiciales docGuardar = super.prepareCreate(null);

                                        docGuardar.setFechaHoraUltimoEstado(fecha);
                                        docGuardar.setUsuarioUltimoEstado(usuario);
                                        docGuardar.setFechaHoraAlta(fecha);
                                        docGuardar.setUsuarioAlta(usuario);
                                        docGuardar.setEmpresa(usuario.getEmpresa());
                                        docGuardar.setDepartamento(usuario.getDepartamento());
                                        docGuardar.setEntradaDocumento(entradaDoc);
                                        docGuardar.setDescripcionMesaEntrada(doc.getDescripcionMesaEntrada());
                                        docGuardar.setCaratula(doc.getDescripcionMesaEntrada());
                                        docGuardar.setEstadoProcesal(estado.getDescripcion());
                                        docGuardar.setFolios(doc.getFolios());
                                        docGuardar.setFechaPresentacion(fecha);
                                        docGuardar.setEstado(estado);

                                        docGuardar.setResponsable(usuario);
                                        docGuardar.setCanalEntradaDocumentoJudicial(canal);
                                        docGuardar.setTipoDocumentoJudicial(tipoDocumentoJudicial);
                                        docGuardar.setSubcategoriaDocumentoJudicial(subcategoriaDocumentoJudicial);
                                        docGuardar.setMostrarWeb("SI");

                                        setSelected(docGuardar);

                                        super.saveNew2(null);

                                        DocumentosJudiciales docGuardar2 = null;
                                        try {
                                            docGuardar2 = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByEntradaDocumentoMaxDoc", DocumentosJudiciales.class).setParameter("entradaDocumento", getSelected().getEntradaDocumento()).getSingleResult();
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }

                                        if (docGuardar2 != null) {
                                            
                                            EstadosProcesalesDocumentosJudiciales estadoProc = estadosProcesalesDocumentosJudicialesController.prepareCreate(null);

                                            estadoProc.setUsuarioAlta(usuario);
                                            estadoProc.setUsuarioUltimoEstado(usuario);
                                            estadoProc.setFechaHoraAlta(fecha);
                                            estadoProc.setFechaHoraUltimoEstado(fecha);
                                            estadoProc.setEmpresa(usuario.getEmpresa());
                                            estadoProc.setEstadoProcesal(estado.getDescripcion());
                                            estadoProc.setDocumentoJudicial(docGuardar2);
                                            
                                        //estadosProcesalesDocumentosJudicialesController.setSelected(estadoProc);
                                        //estadosProcesalesDocumentosJudicialesController.saveNew2(null);
                                             
                                            docGuardar2.setEstadoProcesalDocumentoJudicial(estadoProc);
                                            docGuardar2.setFechaHoraEstadoProcesal(fecha);
                                            docGuardar2.setUsuarioEstadoProcesal(usuario);

                                            setSelected(docGuardar2);

                                            super.save(null);
                                        }

                                        setSelected(null);
                                    }

                                    if (fechaDesde == null) {
                                        fechaDesde = ejbFacade.getSystemDateOnly(-30);
                                    }
                                    if (fechaHasta == null) {
                                        fechaHasta = ejbFacade.getSystemDateOnly();
                                    }

                                    buscarPorFechaPresentacion();

                                } else {
                                    JsfUtil.addErrorMessage("Subcategoria: campo requerido");
                                }
                            } else {
                                JsfUtil.addErrorMessage("Tipo Documento: campo requerido");
                            }
                        } else {
                            JsfUtil.addErrorMessage("Nro Mesa Entrada: campo requerido");
                        }
                    } else {
                        JsfUtil.addErrorMessage("Nro Mesa Entrada: campo requerido");
                    }
                } else {
                    JsfUtil.addErrorMessage("Debe guardar al menos un documento");
                }
            } else {
                JsfUtil.addErrorMessage("Debe guardar al menos un documento");
            }
        }
    }
    
    public void saveDpto() {

        if (getSelected() != null) {
            //if (getSelected().getResponsable().getDepartamento().getId().equals(usuario.getDepartamento().getId())) {
            Date fecha = ejbFacade.getSystemDate();

            Collection<DocumentosJudiciales> col = null;
            try {
                col = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByEntradaDocumento", DocumentosJudiciales.class).setParameter("entradaDocumento", getSelected().getEntradaDocumento()).getResultList();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            Usuarios resp = getSelected().getResponsable();
            FlujosDocumento flujoDoc = null;

            try {
                RolesPorUsuarios rol = this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuario", RolesPorUsuarios.class).setParameter("usuario", getSelected().getResponsable().getId()).getSingleResult();
                flujoDoc = this.ejbFacade.getEntityManager().createNamedQuery("FlujosDocumento.findSgteEstado", FlujosDocumento.class).setParameter("tipoDocumento", getSelected().getTipoDocumentoJudicial().getCodigo()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).setParameter("rolFinal", rol.getRoles().getId()).getSingleResult();
            } catch (Exception e) {
                e.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo desterminar flujo del documento. Documento no se puede transferir");
                return;
            }

            EstadosDocumento estado = null;
            try {
                // Codigo de enviado a secretaria
                estado = this.ejbFacade.getEntityManager().createNamedQuery("EstadosDocumento.findByCodigo", EstadosDocumento.class).setParameter("codigo", flujoDoc.getEstadoDocumentoFinal().getCodigo()).getSingleResult();
            } catch (Exception ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo desterminar sgte estado. Documento no se puede transferir");
                return;
            }

            if (col != null) {
                for (DocumentosJudiciales doc : col) {

                    doc.setFechaHoraUltimoEstado(fecha);
                    doc.setUsuarioUltimoEstado(usuario);
                    doc.setResponsable(resp);
                    doc.setDepartamento(resp.getDepartamento());
                    doc.setEstado(estado);
                    doc.setEstadoProcesal(estado.getDescripcion());

                    //setSelected(doc);
                    //super.save(null);
                    
                    EstadosProcesalesDocumentosJudiciales estadoProc = estadosProcesalesDocumentosJudicialesController.prepareCreate(null);

                    estadoProc.setUsuarioAlta(usuario);
                    estadoProc.setUsuarioUltimoEstado(usuario);
                    estadoProc.setFechaHoraAlta(fecha);
                    estadoProc.setFechaHoraUltimoEstado(fecha);
                    estadoProc.setEmpresa(usuario.getEmpresa());
                    estadoProc.setEstadoProcesal(estado.getDescripcion());
                    estadoProc.setDocumentoJudicial(doc);
                    
                    //                    estadosProcesalesDocumentosJudicialesController.setSelected(estadoProc);
                    //                    estadosProcesalesDocumentosJudicialesController.saveNew2(null);
                     
                    doc.setEstadoProcesalDocumentoJudicial(estadoProc);
                    doc.setFechaHoraEstadoProcesal(fecha);
                    doc.setUsuarioEstadoProcesal(usuario);

                    setSelected(doc);

                    super.save(null);
                }

                buscarPorFechaPresentacion();
            }
            
        }
    }

     */
 /*
    public void verDoc() {

        HttpServletResponse httpServletResponse = null;
        if (getSelected() != null) {
            if (getSelected().getDocumento() != null) {
                try {
                    httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                    httpServletResponse.setContentType("application/pdf");
                    httpServletResponse.setHeader("Content-Length", String.valueOf(getSelected().getDocumento().length));
                    httpServletResponse.addHeader("Content-disposition", "filename=documento.pdf");

                    ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

                    FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
                    servletOutputStream.write(getSelected().getDocumento());
                    FacesContext.getCurrentInstance().responseComplete();

                } catch (Exception e) {
                    FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.verdoc", "true", Collections.<String, Object>emptyMap());
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
            } else {
                JsfUtil.addErrorMessage("No tiene documento adjunto");
            }
        } else {
            JsfUtil.addErrorMessage("Debe seleccionar un documento");
        }

        ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
    }
     */
    public void pdf() {

        HttpServletResponse httpServletResponse = null;
        if (getSelected() != null) {
            try {
                HashMap map = new HashMap();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");

                map.put("nro_mesa_entrada", getSelected().getEntradaDocumento().getNroIdentificacion());
                map.put("descripcion", getSelected().getDescripcion());

                Collection<DocumentosJudiciales> col = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByEntradaDocumento", DocumentosJudiciales.class).setParameter("entradaDocumento", getSelected().getEntradaDocumento()).getResultList();

                String tramite = "";
                String foliosImp = "";
                boolean primero = true;
                for (DocumentosJudiciales doc : col) {
                    if (primero) {
                        tramite += "\n";
                        primero = false;
                    }

                    map.put("fecha_alta", format.format(doc.getFechaHoraAlta()));
                    map.put("hora_alta", format2.format(doc.getFechaHoraAlta()));
                    //   tramite += doc.getDescripcion();
                    // foliosImp += doc.getFolios();
                }
                /*
                if (!primero) {
                    tramite += "\n-----------------------------------------\n";
                }
                tramite += Character.toString((char) 27) + "@" + Character.toString((char) 29) + "V" + Character.toString((char) 1);

                 */
                map.put("tramite", tramite);
                map.put("folios", foliosImp);

                JRBeanCollectionDataSource beanCollectionDataSource = null;

                String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/comprobanteDocumento.jasper");
                JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                httpServletResponse.setContentType("application/pdf");
                httpServletResponse.addHeader("Content-disposition", "filename=ticket.pdf");
                ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

                FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
                JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);

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

            ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
        }
    }

    public void pdf2() {

        HttpServletResponse httpServletResponse = null;
        if (getSelected() != null) {
            try {
                HashMap map = new HashMap();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                fechaHoraAlta = this.ejbFacade.getSystemDate();

                map.put("fecha_hora_alta", format.format(fechaHoraAlta));

                map.put("nro_mesa_entrada", getSelected().getEntradaDocumento().getNroIdentificacion());
                map.put("descripcion", getSelected().getDescripcion());

                Collection<DocumentosJudiciales> col = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByEntradaDocumento", DocumentosJudiciales.class
                ).setParameter("entradaDocumento", getSelected().getEntradaDocumento()).getResultList();

                String tramite = "";
                boolean primero = true;
                for (DocumentosJudiciales doc : col) {
                    if (primero) {
                        tramite += "\n";
                        primero = false;
                    }
                    //   tramite += doc.getDescripcion() + ", Detalles :" + doc.getFolios() + "\n-----------------------------------------\n";
                }

                if (!primero) {
                    tramite += "\n-----------------------------------------\n";
                }
                tramite += Character.toString((char) 27) + "@" + Character.toString((char) 29) + "V" + Character.toString((char) 1);

                map.put("tramite", tramite);

                JRBeanCollectionDataSource beanCollectionDataSource = null;

                String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/ticketDocumento.jasper");
                JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                httpServletResponse.setContentType("application/pdf");
                httpServletResponse.addHeader("Content-disposition", "filename=ticket.pdf");
                ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

                FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
                JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);

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

            ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
        }
    }

    public void txt() {

        HttpServletResponse httpServletResponse = null;
        if (getSelected() != null) {
            try {
                HashMap map = new HashMap();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                fechaHoraAlta = this.ejbFacade.getSystemDate();

                map.put("fecha_hora_alta", format.format(fechaHoraAlta));

                map.put("nro_mesa_entrada", getSelected().getEntradaDocumento().getNroIdentificacion());
                map.put("descripcion", getSelected().getDescripcion());

                Collection<DocumentosJudiciales> col = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByEntradaDocumento", DocumentosJudiciales.class
                ).setParameter("entradaDocumento", getSelected().getEntradaDocumento()).getResultList();

                String tramite = "";
                for (DocumentosJudiciales doc : col) {
                    //      tramite += doc.getDescripcion() + ", Detalles :" + doc.getFolios() + "\n\n";
                }

                // tramite += Character.toString ((char) 27) + "@" + Character.toString ((char) 29) + "V" + Character.toString ((char) 1);
                map.put("tramite", tramite);

                JRBeanCollectionDataSource beanCollectionDataSource = null;

                String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/ticketDocumento.jasper");
                JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                //httpServletResponse.setContentType("application/pdf");
                //httpServletResponse.addHeader("Content-disposition", "filename=ticket.pdf");
                ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

                FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
                // JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);

                JRTextExporter exporter = new JRTextExporter();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                exporter.setParameter(JRTextExporterParameter.PAGE_WIDTH, 80);
                exporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT, 40);
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
                exporter.exportReport();
                byte[] output = baos.toByteArray();
                httpServletResponse.setContentType("text/plain");
                httpServletResponse.setContentLength(output.length);
                servletOutputStream.write(output);
                servletOutputStream.flush();
                servletOutputStream.close();

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

            ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
        }
    }

    public void realizarBusqueda() {
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(fechaDesde);
        cal2.add(Calendar.DATE, -1);
        fechaDesde = cal2.getTime();

        documentosBuscadorItems = ejbFacade.getEntityManager().createNamedQuery("DocumentosResoluciones.findByCaratulaFecha", DocumentosResoluciones.class)
                .setParameter("fechaDesde", fechaDesde)
                .setParameter("fechaHasta", fechaHasta)
                .setParameter("tituloResolucion", "%" + busqueda + "%")
                .getResultList();

    }
    
    public String getTipoArchivo(String ruta) {
        if(ruta.contains("anexo")) return "Anexo";
        else return "Resolucion";
    }

    public void txt2() {

        HttpServletResponse httpServletResponse = null;
        if (getSelected() != null) {
            try {
                HashMap map = new HashMap();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");

                fechaHoraAlta = getSelected().getFechaHoraAlta();
                /*
                map.put("fecha_hora_alta", format.format(fechaHoraAlta));

                map.put("nro_mesa_entrada", getSelected().getEntradaDocumento().getNroMesaEntrada());
                map.put("entregado_por", getSelected().getEntradaDocumento().getEntregadoPor());
                map.put("nro_cedula_ruc", getSelected().getEntradaDocumento().getNroCedulaRuc());
                map.put("telefono", getSelected().getEntradaDocumento().getTelefono());
                map.put("descripcion", getSelected().getDescripcionMesaEntrada());
                map.put("recibido_por", getSelected().getEntradaDocumento().getRecibidoPor().getNombresApellidos());

                Collection<DocumentosJudiciales> col = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByEntradaDocumento", DocumentosJudiciales.class
                ).setParameter("entradaDocumento", getSelected().getEntradaDocumento()).getResultList();

                String tramite = "";
                for (DocumentosJudiciales doc : col) {
                    tramite += doc.getDescripcionMesaEntrada() + ", folios :" + doc.getFolios() + "\n\n";
                }
                
                // tramite += Character.toString ((char) 27) + "@" + Character.toString ((char) 29) + "V" + Character.toString ((char) 1);
                map.put("tramite", tramite);

                JRBeanCollectionDataSource beanCollectionDataSource = null;

                String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/ticketDocumento.jasper");
                JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);
                 */
                httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                //httpServletResponse.setContentType("application/pdf");
                //httpServletResponse.addHeader("Content-disposition", "filename=ticket.pdf");
                ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

                FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
                // JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
/*
                JRTextExporter exporter = new JRTextExporter();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                exporter.setParameter(JRTextExporterParameter.PAGE_WIDTH, 80);
                exporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT, 40);
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
                exporter.exportReport();

                byte[] output = baos.toByteArray();
                 */

                // String impresion = "Hola como estas que tal todo bien como esta bien nomas que tal" +  Character.toString ((char) 27) + "@" + Character.toString ((char) 29) + "V" + Character.toString ((char) 1);
                String impresion
                        = " JURADO DE ENJUICIAMIENTO\n"
                        + "     DE MAGISTRADOS\n"
                        + "     MESA DE ENTRADA\n"
                        + "        NRO " + getSelected().getEntradaDocumento().getNroIdentificacion() + "\n"
                        + "Fecha: " + format.format(fechaHoraAlta) + "\n"
                        + "Hora: " + format2.format(fechaHoraAlta) + "\n"

                        + // "nro CI: " + getSelected().getEntradaDocumento().getNroCedulaRuc() + "\n" +
                        // "Telefono: " + getSelected().getEntradaDocumento().getTelefono() + "\n"
                        "Documentos:\n";

                Collection<DocumentosJudiciales> col = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByEntradaDocumento", DocumentosJudiciales.class
                ).setParameter("entradaDocumento", getSelected().getEntradaDocumento()).getResultList();

                for (DocumentosJudiciales doc : col) {
                    //  impresion += " -- " + doc.getDescripcion() + " (Detalles :" + doc.getFolios() + ")\n_______________________\n";
                }

                impresion += "\n\n\n\nRecibido: ______________\n"
                        + "Telef.: (021) 442662, (021) 443389\n"
                        + "        (021) 443375/6\n"
                        + "Email: mesadeentrada@jem.gov.py\n\n";

                httpServletResponse.setContentType("text/plain");
                httpServletResponse.setContentLength(impresion.getBytes().length);
                servletOutputStream.write(impresion.getBytes());
                servletOutputStream.flush();
                servletOutputStream.close();

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

            ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
        }
    }

    public void handleFileUpload(FileUploadEvent event) {

        if (listaFiles == null) {
            listaFiles = new ArrayList<>();
        }
        /*
        if(listaFiles.size() > 2){
            JsfUtil.addErrorMessage("Solo se puede agregar hasta 3 documentos");
            return;
        }
         */
        listaFiles.add(event.getFile());

    }
    public void handleFileUploadEdit(FileUploadEvent event) {

        if (listaFilesEdit == null) {
            listaFilesEdit = new ArrayList<>();
        }
        listaFilesEdit.add(event.getFile());

    }
    public void borrarFile(UploadedFile item) {
        if (listaFiles != null) {
            listaFiles.remove(item);
        }
    }
    public void borrarFileEdit(UploadedFile item) {
        if (listaFilesEdit != null) {
            listaFilesEdit.remove(item);
        }
    }
}
