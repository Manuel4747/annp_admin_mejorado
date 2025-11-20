package py.com.startic.gestion.controllers;

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
import jakarta.faces.context.FacesContext;

import py.com.startic.gestion.models.DocumentosJudiciales;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
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
import org.primefaces.model.file.UploadedFile;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.models.Archivos;
import py.com.startic.gestion.models.CambiosEstadoDocumento;
import py.com.startic.gestion.models.CanalesEntradaDocumentoJudicial;
import py.com.startic.gestion.models.Departamentos;
import py.com.startic.gestion.models.EntradasDocumentosJudiciales;
import py.com.startic.gestion.models.EstadosDocumento;
import py.com.startic.gestion.models.EstadosProcesalesDocumentosJudiciales;
import py.com.startic.gestion.models.FlujosDocumento;
import py.com.startic.gestion.models.ParametrosSistema;
import py.com.startic.gestion.models.RolesPorUsuarios;
import py.com.startic.gestion.models.SubcategoriasDocumentosJudiciales;
import py.com.startic.gestion.models.TiposDocumentosJudiciales;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "entradaMesaEntradaDocumentosJudicialesController")
@ViewScoped
public class EntradaMesaEntradaDocumentosJudicialesController extends AbstractController<DocumentosJudiciales> {

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
    private EntradasDocumentosJudicialesController entradasDocumentosJudicialesController;
    @Inject
    private EstadosDocumentoController estadosDocumentoController;
    @Inject
    private TiposDocumentosJudicialesController tiposDocumentosJudicialesController;
    @Inject
    private EstadosProcesalesDocumentosJudicialesController estadosProcesalesDocumentosJudicialesController;
    @Inject
    private CambiosEstadoDocumentoController cambiosEstadoDocumentoController;
    @Inject
    private ArchivosController archivosController;

    private String descripcionMesaEntrada;
    private String folios;
    private Collection<DocumentosJudiciales> detalles;
    private DocumentosJudiciales detalleSelected;
    private String nuevoRecurrente;
    private CanalesEntradaDocumentoJudicial canal;
    private EntradasDocumentosJudiciales entradaDocumentoJudicial;
    private Departamentos departamento;
    private Usuarios usuario;
    private Date fechaDesde;
    private Date fechaHasta;
    private TiposDocumentosJudiciales tipoDocumentoJudicial;
    private SubcategoriasDocumentosJudiciales subcategoriaDocumentoJudicial;
    private List<SubcategoriasDocumentosJudiciales> listaSubcategoriaDocumentoJudicial;
    private Collection<Usuarios> listaUsuariosTransf;
    private String denunciaEnContraDe;
    private String denunciante;
    private String patrocinio;
    private String nroOficio;
    private String acusacion;
    private Date fechaHoraAlta;
    private Usuarios responsableDestino;
    private Integer responsableDestinoId;
    private SimpleDateFormat formatAno = new SimpleDateFormat("YY");
    private List<Archivos> listaArchivos;
    private String descripcionArchivo;
    private ParametrosSistema par;
    private Archivos docImprimir;
    private String content;
    private String nombre;
    private String url;
    private String endpoint;
    private HttpSession session;
    private String eleccionFiltro;
    private String asuntoFiltro;
    private String textoFiltro;
    private final FiltroURL filtroURL = new FiltroURL();

    private UploadedFile file;

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

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getAcusacion() {
        return acusacion;
    }

    public void setAcusacion(String acusacion) {
        this.acusacion = acusacion;
    }

    public String getNroOficio() {
        return nroOficio;
    }

    public void setNroOficio(String nroOficio) {
        this.nroOficio = nroOficio;
    }

    public String getDenunciaEnContraDe() {
        return denunciaEnContraDe;
    }

    public void setDenunciaEnContraDe(String denunciaEnContraDe) {
        this.denunciaEnContraDe = denunciaEnContraDe;
    }

    public String getDenunciante() {
        return denunciante;
    }

    public void setDenunciante(String denunciante) {
        this.denunciante = denunciante;
    }

    public String getPatrocinio() {
        return patrocinio;
    }

    public void setPatrocinio(String patrocinio) {
        this.patrocinio = patrocinio;
    }

    /*
    public String obtenerSgteNroMesaEntradaJudicial() {
        jakarta.persistence.Query query = ejbFacade.getEntityManager().createNativeQuery(
                "select ifnull(max(CONVERT(nro_mesa_entrada,UNSIGNED INTEGER)),0) as VALOR from documentos_judiciales as d, entradas_documentos_judiciales as e where d.entrada_documento = e.id AND d.tipo_documento_judicial = 'JU' AND nro_mesa_entrada not like 'AUTO%';", NroMesaEntrada.class);

        NroMesaEntrada cod = (NroMesaEntrada) query.getSingleResult();

        return String.valueOf(cod.getCodigo() + 1);
    }
     */
    public String obtenerSgteNroMesaEntradaJudicial() {

        Date fecha = ejbFacade.getSystemDate();

        jakarta.persistence.Query query = ejbFacade.getEntityManager().createNativeQuery(
                // "select ifnull(max(CONVERT(nro_mesa_entrada,UNSIGNED INTEGER)),0) as VALOR from documentos_judiciales as d, entradas_documentos_judiciales as e where d.entrada_documento = e.id AND d.tipo_documento_judicial = 'AD' AND nro_mesa_entrada not like 'AUTO%';", NroMesaEntrada.class);
                //"select ifnull(max(CONVERT(substring(nro_mesa_entrada, 1, LENGTH(nro_mesa_entrada) - 3),UNSIGNED INTEGER)),0) as VALOR from documentos_judiciales as d, entradas_documentos_judiciales as e where d.entrada_documento = e.id AND d.tipo_documento_judicial = 'JU' AND nro_mesa_entrada not like 'AUTO%' AND nro_mesa_entrada like '%-" + formatAno.format(fecha) + "'", NroMesaEntrada.class);
                "select ifnull(max(CONVERT(substring(nro_mesa_entrada, 1, LENGTH(nro_mesa_entrada) - 3),UNSIGNED INTEGER)),0) as VALOR from documentos_judiciales as d, entradas_documentos_judiciales as e where d.entrada_documento = e.id AND d.tipo_documento_judicial in ('AD','JU') AND nro_mesa_entrada not like 'AUTO%' AND nro_mesa_entrada like '%-" + formatAno.format(fecha) + "'", NroMesaEntrada.class);

        NroMesaEntrada cod = (NroMesaEntrada) query.getSingleResult();

        return Utils.padLeft(String.valueOf(cod.getCodigo() + 1) + "-" + formatAno.format(fecha), "0", 6);
    }

    @PostConstruct
    @Override
    public void initParams() {
        departamento = departamentoController.prepareCreate(null);
        departamento.setId(2); // Mesa de Entrada
        canal = canalesEntradaDocumentoJudicialController.prepareCreate(null);
        canal.setCodigo(Constantes.CANAL_ENTRADA_DOCUMENTO_JUDICIAL_ME);

        par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID).getSingleResult();

        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        int pos = url.lastIndexOf(uri);
        url = url.substring(0, pos);

        String[] array = uri.split("/");
        endpoint = array[1];
        
        eleccionFiltro = "1";

        try {
            tipoDocumentoJudicial = this.ejbFacade.getEntityManager().createNamedQuery("TiposDocumentosJudiciales.findByCodigo", TiposDocumentosJudiciales.class).setParameter("codigo", Constantes.TIPO_DOCUMENTO_JUDICIAL_JU).getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
            JsfUtil.addErrorMessage("Error de configuracion. No se puede iniciar pantalla");
            return;
        }

        listaSubcategoriaDocumentoJudicial = this.ejbFacade.getEntityManager().createNamedQuery("SubcategoriasDocumentosJudiciales.findByTipoDocumentoJudicialEstado", SubcategoriasDocumentosJudiciales.class).setParameter("tipoDocumentoJudicial", tipoDocumentoJudicial).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        usuario = (Usuarios) session.getAttribute("Usuarios");
        fechaDesde = ejbFacade.getSystemDateOnly(-30);
        fechaHasta = ejbFacade.getSystemDateLastSecond();
        super.initParams();
        buscarPorFechaPresentacion();
    }

    public void actualizarDescripcionAcusacion() {
        if (!"".equals(acusacion)) {
            descripcionMesaEntrada = "Acusacion: " + acusacion;
        }

    }

    public void actualizarDescripcionOficio() {
        if (!"".equals(nroOficio)) {
            descripcionMesaEntrada = "Oficio Nº " + nroOficio;
        }

    }

    public void actualizarDescripcionDenuncia() {
        descripcionMesaEntrada = "";
        if (!"".equals(denunciaEnContraDe)) {
            descripcionMesaEntrada += "Denuncia en contra de " + denunciaEnContraDe;
        }

        if (!"".equals(denunciante)) {
            if (!"".equals(descripcionMesaEntrada)) {
                descripcionMesaEntrada += ", ";
            }
            descripcionMesaEntrada += "Denunciante " + denunciante;
        }

        if (!"".equals(patrocinio)) {
            if (!"".equals(descripcionMesaEntrada)) {
                descripcionMesaEntrada += ", ";
            }
            descripcionMesaEntrada += "Patrocinio de " + patrocinio;
        }
    }

    public void prepareVerDoc(DocumentosJudiciales doc) {

        List<Archivos> lista = ejbFacade.getEntityManager().createNamedQuery("Archivos.findByDocumentoJudicialOrdered", Archivos.class).setParameter("documentoJudicial", doc).getResultList();

        if (!lista.isEmpty()) {
            docImprimir = lista.get(0);
        }

        //PrimeFaces.current().ajax().update("ExpAcusacionesViewForm");
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

    public boolean desabilitarBotonAlzarArchivo() {
        return false;
    }

    public boolean deshabilitarVerDoc(DocumentosJudiciales doc) {
        if (doc != null) {
            if (doc.getDepartamento().equals(usuario.getDepartamento())) {
                List<Archivos> lista = ejbFacade.getEntityManager().createNamedQuery("Archivos.findByDocumentoJudicialOrdered", Archivos.class).setParameter("documentoJudicial", doc).getResultList();

                return lista.isEmpty();
            }
        }
        return true;
    }

    public void alzarArchivo(Archivos arch) {

        if (getSelected() != null) {

            if (file == null) {
                return;
            } else if (file.getContent().length == 0) {
                return;
            }

            Date fecha = ejbFacade.getSystemDate();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String nombreArchivo = simpleDateFormat.format(fecha);
            nombreArchivo += "_" + getSelected().getId() + ".pdf";

            byte[] bytes = null;
            try {
                bytes = IOUtils.toByteArray(file.getInputStream());
            } catch (IOException ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("Error al leer archivo");
                return;
            }

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(par.getRutaArchivos() + File.separator + nombreArchivo);
                fos.write(bytes);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException ex) {
                JsfUtil.addErrorMessage("No se pudo guardar archivo");
                fos = null;
            } catch (IOException ex) {
                JsfUtil.addErrorMessage("No se pudo guardar archivo");
                fos = null;
            }

            if (arch == null) {
                Archivos archivo = new Archivos();

                archivo.setDocumentoJudicial(getSelected());
                archivo.setDescripcion(getSelected().getDescripcionMesaEntrada());
                archivo.setRuta(nombreArchivo);
                archivo.setFechaHoraAlta(fecha);
                archivo.setFechaHoraUltimoEstado(fecha);
                archivo.setUsuarioAlta(usuario);
                archivo.setUsuarioUltimoEstado(usuario);

                archivosController.setSelected(archivo);
                archivosController.saveNew(null);
            } else {
                arch.setDocumentoJudicial(getSelected());
                arch.setDescripcion(getSelected().getDescripcionMesaEntrada());
                arch.setRuta(nombreArchivo);
                arch.setFechaHoraAlta(fecha);
                arch.setFechaHoraUltimoEstado(fecha);
                arch.setUsuarioAlta(usuario);
                arch.setUsuarioUltimoEstado(usuario);

                archivosController.setSelected(arch);
                archivosController.save(null);
            }
        }

    }

    public boolean esDenuncia() {
        if (subcategoriaDocumentoJudicial != null) {
            if (subcategoriaDocumentoJudicial.getId() == Constantes.SUBCATEGORIA_DENUNCIA) {
                return true;
            }
        }

        denunciaEnContraDe = "";
        denunciante = "";
        patrocinio = "";
        nroOficio = "";
        acusacion = "";
        return false;
    }

    public boolean esOficio() {
        if (subcategoriaDocumentoJudicial != null) {
            if (subcategoriaDocumentoJudicial.getId() == Constantes.SUBCATEGORIA_OFICIO) {
                return true;
            }
        }

        denunciaEnContraDe = "";
        denunciante = "";
        patrocinio = "";
        nroOficio = "";
        acusacion = "";

        return false;
    }

    public boolean esAcusacion() {
        if (subcategoriaDocumentoJudicial != null) {
            if (subcategoriaDocumentoJudicial.getId() == Constantes.SUBCATEGORIA_ACUSACION) {
                return true;
            }
        }

        denunciaEnContraDe = "";
        denunciante = "";
        patrocinio = "";
        nroOficio = "";
        acusacion = "";

        return false;
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

    public TiposDocumentosJudiciales getTipoDocumentoJudicial() {
        return tipoDocumentoJudicial;
    }

    public void setTipoDocumentoJudicial(TiposDocumentosJudiciales tipoDocumentoJudicial) {
        this.tipoDocumentoJudicial = tipoDocumentoJudicial;
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

    public DocumentosJudiciales getDetalleSelected() {
        return detalleSelected;
    }

    public void setDetalleSelected(DocumentosJudiciales detalleSelected) {
        this.detalleSelected = detalleSelected;
    }

    public Collection<DocumentosJudiciales> getDetalles() {
        return detalles;
    }

    public void setDetalles(Collection<DocumentosJudiciales> detalles) {
        this.detalles = detalles;
    }

    public String getFolios() {
        return folios;
    }

    public void setFolios(String folios) {
        this.folios = folios;
    }

    public String getDescripcionMesaEntrada() {
        return descripcionMesaEntrada;
    }

    public void setDescripcionMesaEntrada(String descripcionMesaEntrada) {
        this.descripcionMesaEntrada = descripcionMesaEntrada;
    }

    public EntradasDocumentosJudiciales getEntradaDocumentoJudicial() {
        return entradaDocumentoJudicial;
    }

    public void setEntradaDocumentoJudicial(EntradasDocumentosJudiciales entradaDocumentoJudicial) {
        this.entradaDocumentoJudicial = entradaDocumentoJudicial;
    }

    public EntradaMesaEntradaDocumentosJudicialesController() {
        // Inform the Abstract parent controller of the concrete DocumentosJudiciales Entity
        super(DocumentosJudiciales.class);
    }
/*
    public void prepareTransferir() {
        listaUsuariosTransf = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferir", Usuarios.class).setParameter("tipoDocumento", getSelected().getTipoDocumentoJudicial().getCodigo()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).setParameter("rolEncargado", Constantes.ROL_ENCARGADO).getResultList();
        responsableDestino = null;
    }
    */
    

    public void prepareTransferir() {
        prepareTransferir(getSelected());
    }

    public void prepareTransferir(DocumentosJudiciales doc) {
        if (doc != null) {
            
            listaUsuariosTransf = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findTransferir", Usuarios.class).setParameter("tipoDocumento", doc.getTipoDocumentoJudicial().getCodigo()).setParameter("estadoDocumentoActual", doc.getEstado().getCodigo()).setParameter("rolEncargado", Constantes.ROL_ENCARGADO).getResultList();

            if (doc.getResponsableAnterior() != null) {
                List<Usuarios> respAnt = ejbFacade.getEntityManager().createNamedQuery("Usuarios.findById", Usuarios.class).setParameter("id", doc.getResponsableAnterior().getId()).getResultList();
                if (!respAnt.isEmpty()) {
                    boolean encontro = false;
                    for (Usuarios trans : listaUsuariosTransf) {
                        if (trans.equals(respAnt.get(0))) {
                            encontro = true;
                        }
                    }

                    if (!encontro) {
                        listaUsuariosTransf.add(respAnt.get(0));
                    }
                }
            }
            

            List<FlujosDocumento> flujos = null;
            try {
                flujos = this.ejbFacade.getEntityManager().createNamedQuery("FlujosDocumento.findByEstadoDocumentoActualFin", FlujosDocumento.class).setParameter("tipoDocumento", doc.getTipoDocumentoJudicial().getCodigo()).setParameter("estadoDocumentoActual", doc.getEstado().getCodigo()).getResultList();

                int contador = -1;
                for (FlujosDocumento flujo : flujos) {

                    EstadosDocumento est = this.ejbFacade.getEntityManager().createNamedQuery("EstadosDocumento.findByCodigo", EstadosDocumento.class).setParameter("codigo", flujo.getEstadoDocumentoFinal()).getSingleResult();
                    /*
                    Usuarios usu = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findById", Usuarios.class).setParameter("id", 2).getSingleResult();
                    usu.setId(contador);
                    usu.setNombresApellidos(est.getDescripcion());
                    usu.setEstadoDocumentoFinal(est);
                     */
                    Usuarios usu = new Usuarios(contador, est.getDescripcion(), est);
                    listaUsuariosTransf.add(usu);
                    contador--;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            listaUsuariosTransf = new ArrayList<>();
        }

        responsableDestino = null;
    }

    @Override
    public DocumentosJudiciales prepareCreate(ActionEvent event) {
        DocumentosJudiciales doc = super.prepareCreate(event);

        entradaDocumentoJudicial = entradasDocumentosJudicialesController.prepareCreate(event);
        entradaDocumentoJudicial.setRecibidoPor(usuario);
        entradaDocumentoJudicial.setNroMesaEntrada(obtenerSgteNroMesaEntradaJudicial());

        detalles = null;
        detalleSelected = null;
        folios = "";
        nuevoRecurrente = "";
        descripcionMesaEntrada = "";
        subcategoriaDocumentoJudicial = null;

        detalles = null;

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
    public Collection<DocumentosJudiciales> getItems() {
        return super.getItems2();
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
    
    public void buscarPorTexto(String criterio) {
        List<DocumentosJudiciales> lista;
        String criterioFinal = "%" + criterio + "%";
        lista = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByNroMesaEntradaOrderedDptoTipoDoc", DocumentosJudiciales.class).setParameter("nroMesaEntrada", criterioFinal).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("departamento", departamento).setParameter("tipoDocumentoJudicial", tipoDocumentoJudicial).getResultList();
        setItems(lista);

    }
    
    public void buscarPorAsunto(String criterio) {
        List<DocumentosJudiciales> lista;
        String criterioFinal = "%" + criterio + "%";
        lista = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByDescripcionMesaEntradaOrderedDptoTipoDoc", DocumentosJudiciales.class).setParameter("descripcion", criterioFinal).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("departamento", departamento).setParameter("tipoDocumentoJudicial", tipoDocumentoJudicial).getResultList();

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
            setItems(this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findOrderedDptoTipoDoc", DocumentosJudiciales.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).setParameter("canalEntradaDocumentoJudicial", canal).setParameter("departamento", departamento).setParameter("tipoDocumentoJudicial", tipoDocumentoJudicial).getResultList());
        }
    }

    public void agregar() {
        if (detalles == null) {
            detalles = new ArrayList<>();
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

        for (DocumentosJudiciales det : detalles) {
            if (det.getDescripcionMesaEntrada().equals(descripcionMesaEntrada)) {
                encontro = true;
                break;
            }
        }

        if (!encontro) {
            DocumentosJudiciales dea = super.prepareCreate(null);

            SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");

            Date fecha = ejbFacade.getSystemDate();

            dea.setFolios(folios);
            dea.setDescripcionMesaEntrada(descripcionMesaEntrada);

            dea.setId(Integer.valueOf(format.format(fecha)));

            detalles.add(dea);

            folios = "";
            descripcionMesaEntrada = null;
        } else {
            JsfUtil.addErrorMessage("Un documento con la misma descripcion ya fue agregado anteriormente.");
        }

    }

    public void borrarDetalle() {
        if (detalles != null && detalleSelected != null) {
            for (DocumentosJudiciales det : detalles) {
                if (det.getId().equals(detalleSelected.getId())) {
                    detalles.remove(det);
                    break;
                }
            }
        }
    }

    public String datePattern() {
        return "dd/MM/yyyy HH:mm:ss";
    }

    public String customFormatDate(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern());
            return format.format(date);
        }
        return "";
    }

    public void saveNew() {
        try {
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            boolean guardando = (boolean) ((params.get("guardandoNew") == null) ? false : params.get("guardandoNew"));
            if (!guardando) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("guardandoNew", true);
                if (getSelected() != null) {
                    //if (detalles != null) {
                    //if (detalles.size() > 0) {
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

                                    EstadosDocumento estado = null;
                                    try {
                                        estado = this.ejbFacade.getEntityManager().createNamedQuery("EstadosDocumento.findByCodigo", EstadosDocumento.class).setParameter("codigo", "-1").getSingleResult();
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        JsfUtil.addErrorMessage("Error de configuracion. No se pudo guardar documento");
                                        return;
                                    }

                                    Date fecha = ejbFacade.getSystemDate();
                                    EntradasDocumentosJudiciales entradaDoc = entradasDocumentosJudicialesController.prepareCreate(null);

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
                                    entradaDoc.setNroMesaEntrada(obtenerSgteNroMesaEntradaJudicial());

                                    //entradasDocumentosJudicialesController.setSelected(entradaDoc);
                                    //entradasDocumentosJudicialesController.saveNew(null);
                                    getSelected().setFechaHoraUltimoEstado(fecha);
                                    getSelected().setUsuarioUltimoEstado(usuario);
                                    getSelected().setFechaHoraAlta(fecha);
                                    getSelected().setUsuarioAlta(usuario);
                                    getSelected().setEmpresa(usuario.getEmpresa());
                                    getSelected().setDepartamento(usuario.getDepartamento());
                                    getSelected().setEntradaDocumento(entradaDoc);
                                    getSelected().setDescripcionMesaEntrada(descripcionMesaEntrada);
                                    getSelected().setCaratula(descripcionMesaEntrada);
                                    getSelected().setEstadoProcesal(estado.getDescripcion());
                                    getSelected().setFolios(folios);
                                    getSelected().setFechaPresentacion(fecha);
                                    getSelected().setEstado(estado);

                                    getSelected().setResponsable(usuario);
                                    getSelected().setCanalEntradaDocumentoJudicial(canal);
                                    getSelected().setTipoDocumentoJudicial(tipoDocumentoJudicial);
                                    getSelected().setSubcategoriaDocumentoJudicial(subcategoriaDocumentoJudicial);
                                    getSelected().setMostrarWeb("SI");

                                    super.saveNew(null);

                                    alzarArchivo(null);
                                    //EntradasDocumentosJudiciales ent = this.ejbFacade.getEntityManager().createNamedQuery("EntradasDocumentosJudiciales.findById", EntradasDocumentosJudiciales.class).setParameter("id", entradasDocumentosJudicialesController.getSelected().getId()).getSingleResult();
                                    if (estado == null) {

                                        estado = estadosDocumentoController.prepareCreate(null);

                                        estado.setCodigo("-1");

                                        estado.setDescripcion("Entro por Mesa de Entrada");
                                        estado.setEmpresa(usuario.getEmpresa());

                                        estadoController.setSelected(estado);

                                        estadoController.saveNew(null);
                                    }

                                    CambiosEstadoDocumento cambioEstadoDocumento = cambiosEstadoDocumentoController.prepareCreate(null);
                                    cambioEstadoDocumento.setDocumentoJudicial(getSelected());

                                    cambioEstadoDocumento.setResponsableOrigen(getSelected().getResponsable());
                                    cambioEstadoDocumento.setDepartamentoOrigen(getSelected().getDepartamento());
                                    cambioEstadoDocumento.setEstadoOriginal(estado);

                                    cambioEstadoDocumento.setResponsableDestino(getSelected().getResponsable());
                                    cambioEstadoDocumento.setDepartamentoDestino(getSelected().getDepartamento());
                                    cambioEstadoDocumento.setEstadoFinal(estado);

                                    cambioEstadoDocumento.setEmpresa(getSelected().getEmpresa());
                                    cambioEstadoDocumento.setFechaHoraAlta(fecha);
                                    cambioEstadoDocumento.setFechaHoraUltimoEstado(fecha);
                                    cambioEstadoDocumento.setUsuarioAlta(usuario);
                                    cambioEstadoDocumento.setUsuarioUltimoEstado(usuario);

                                    cambiosEstadoDocumentoController.setSelected(cambioEstadoDocumento);
                                    cambiosEstadoDocumentoController.save(null);

                                    EstadosProcesalesDocumentosJudiciales estadoProc = estadosProcesalesDocumentosJudicialesController.prepareCreate(null);

                                    estadoProc.setUsuarioAlta(usuario);
                                    estadoProc.setUsuarioUltimoEstado(usuario);
                                    estadoProc.setFechaHoraAlta(fecha);
                                    estadoProc.setFechaHoraUltimoEstado(fecha);
                                    estadoProc.setEmpresa(usuario.getEmpresa());
                                    estadoProc.setEstadoProcesal(estado.getDescripcion());
                                    estadoProc.setDocumentoJudicial(getSelected());

                                    estadosProcesalesDocumentosJudicialesController.setSelected(estadoProc);
                                    estadosProcesalesDocumentosJudicialesController.saveNew(null);

                                    getSelected().setEstadoProcesalDocumentoJudicial(estadoProc);
                                    getSelected().setUsuarioEstadoProcesal(usuario);
                                    getSelected().setFechaHoraEstadoProcesal(fecha);

                                    super.save(null);

                                    /*
                                    Iterator<DocumentosJudiciales> it = detalles.iterator();

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
                                            
                                            docGuardar2.setEstadoProcesalDocumentoJudicial(estadoProc);
                                            docGuardar2.setFechaHoraEstadoProcesal(fecha);
                                            docGuardar2.setUsuarioEstadoProcesal(usuario);

                                            setSelected(docGuardar2);

                                            super.save(null);
                                        }

                                        setSelected(null);
                                    }
                            
                                     */
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
                        getSelected().setDepartamento(getSelected().getResponsable().getDepartamento());
                        getSelected().setCaratula(getSelected().getDescripcionMesaEntrada());

                        super.save(null);

                        List<Archivos> lista = ejbFacade.getEntityManager().createNamedQuery("Archivos.findByDocumentoJudicialOrdered", Archivos.class).setParameter("documentoJudicial", getSelected()).getResultList();

                        if (lista.isEmpty()) {
                            alzarArchivo(null);
                        } else {
                            alzarArchivo(lista.get(0));
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

    public void saveDpto() {

        if (getSelected() != null) {
            //if (getSelected().getResponsable().getDepartamento().getId().equals(usuario.getDepartamento().getId())) {
            Date fecha = ejbFacade.getSystemDate();
            /*
            Collection<DocumentosJudiciales> col = null;
            try {
                col = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByEntradaDocumento", DocumentosJudiciales.class).setParameter("entradaDocumento", getSelected().getEntradaDocumento()).getResultList();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
             */
            /*
            FlujosDocumento flujoDoc = null;

            try {
                // RolesPorUsuarios rol = this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findRolFlujo", RolesPorUsuarios.class).setParameter("usuario", responsableDestino.getId()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).setParameter("tipoDocumento", tipoDocumentoJudicial.getCodigo()).getSingleResult();
                flujoDoc = this.ejbFacade.getEntityManager().createNamedQuery("FlujosDocumento.findSgteEstado", FlujosDocumento.class).setParameter("tipoDocumento", getSelected().getTipoDocumentoJudicial().getCodigo()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).getSingleResult();
            } catch (Exception e) {
                e.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo determinar flujo del documento. Documento no se puede transferir");
                return;
            }
            EstadosDocumento estado = null;
            try {
                // Codigo de enviado a secretaria
                estado = this.ejbFacade.getEntityManager().createNamedQuery("EstadosDocumento.findByCodigo", EstadosDocumento.class).setParameter("codigo", flujoDoc.getEstadoDocumentoFinal()).getSingleResult();
            } catch (Exception ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo determinar sgte estado. Documento no se puede transferir");
                return;
            }
            */
            
            FlujosDocumento flujoDoc = null;
            EstadosDocumento estado = null;

            for (Usuarios usu : listaUsuariosTransf) {
                if (usu.getId().equals(responsableDestinoId)) {
                    responsableDestino = usu;
                }
            }

            if (responsableDestino.getId() > 0) {
                if (responsableDestino.equals(getSelected().getResponsableAnterior())) {
                    estado = getSelected().getEstadoAnterior();
                } else {
                    try {
                        //RolesPorUsuarios rol = this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuario", RolesPorUsuarios.class
                        //).setParameter("usuario", getSelected().getResponsable().getId()).getSingleResult();
                        //RolesPorUsuarios rol = this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findRolFlujo", RolesPorUsuarios.class).setParameter("usuario", responsableDestino.getId()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).setParameter("tipoDocumento", tipoDoc.getCodigo()).getSingleResult();

                        //                flujoDoc
                        //                        = this.ejbFacade.getEntityManager().createNamedQuery("FlujosDocumento.findSgteEstado", FlujosDocumento.class
                        //                        ).setParameter("tipoDocumento", getSelected().getTipoDocumentoJudicial().getCodigo()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).getSingleResult();
                        RolesPorUsuarios rol = this.ejbFacade.getEntityManager().createNamedQuery("RolesPorUsuarios.findByUsuarioRolFlujo", RolesPorUsuarios.class).setParameter("usuario", responsableDestino.getId()).getSingleResult();

                        flujoDoc = this.ejbFacade.getEntityManager().createNamedQuery("FlujosDocumento.findSgteEstadoSegunRol", FlujosDocumento.class).setParameter("tipoDocumento", getSelected().getTipoDocumentoJudicial().getCodigo()).setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo()).setParameter("rolFinal", rol.getRoles()).getSingleResult();
                    } catch (Exception e) {
                        e.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo determinar flujo del documento. Documento no se puede transferir");
                        return;
                    }

                    try {
                        // Codigo de enviado a secretaria
                        estado = this.ejbFacade.getEntityManager().createNamedQuery("EstadosDocumento.findByCodigo", EstadosDocumento.class
                        ).setParameter("codigo", flujoDoc.getEstadoDocumentoFinal()).getSingleResult();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo determinar sgte estado. Documento no se puede transferir");
                        return;
                    }
                }

            } else {
                estado = responsableDestino.getEstadoDocumentoFinal();
            }


            CambiosEstadoDocumento cambioEstadoDocumento = cambiosEstadoDocumentoController.prepareCreate(null);
            cambioEstadoDocumento.setDocumentoJudicial(getSelected());

            cambioEstadoDocumento.setResponsableOrigen(getSelected().getResponsable());
            cambioEstadoDocumento.setDepartamentoOrigen(getSelected().getDepartamento());
            cambioEstadoDocumento.setEstadoOriginal(getSelected().getEstado());


            if (responsableDestino.getId() > 0) {
                cambioEstadoDocumento.setResponsableDestino(responsableDestino);
                cambioEstadoDocumento.setDepartamentoDestino(responsableDestino.getDepartamento());
            } else {
                cambioEstadoDocumento.setResponsableDestino(getSelected().getResponsable());
                cambioEstadoDocumento.setDepartamentoDestino(getSelected().getDepartamento());
            }
            cambioEstadoDocumento.setEstadoFinal(estado);

            cambioEstadoDocumento.setEmpresa(getSelected().getEmpresa());
            cambioEstadoDocumento.setFechaHoraAlta(fecha);
            cambioEstadoDocumento.setFechaHoraUltimoEstado(fecha);
            cambioEstadoDocumento.setUsuarioAlta(usuario);
            cambioEstadoDocumento.setUsuarioUltimoEstado(usuario);

            cambiosEstadoDocumentoController.setSelected(cambioEstadoDocumento);
            cambiosEstadoDocumentoController.save(null);

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

            EstadosProcesalesDocumentosJudiciales estadoProc = estadosProcesalesDocumentosJudicialesController.prepareCreate(null);

            estadoProc.setUsuarioAlta(usuario);
            estadoProc.setUsuarioUltimoEstado(usuario);
            estadoProc.setFechaHoraAlta(fecha);
            estadoProc.setFechaHoraUltimoEstado(fecha);
            estadoProc.setEmpresa(usuario.getEmpresa());
            estadoProc.setEstadoProcesal(estado.getDescripcion());
            estadoProc.setDocumentoJudicial(getSelected());

            // estadosProcesalesDocumentosJudicialesController.setSelected(estadoProc);
            // estadosProcesalesDocumentosJudicialesController.saveNew2(null);
            getSelected().setEstadoProcesalDocumentoJudicial(estadoProc);
            getSelected().setFechaHoraEstadoProcesal(fecha);
            getSelected().setUsuarioEstadoProcesal(usuario);

            super.save(null);

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
 /*
    public void pdf() {

        HttpServletResponse httpServletResponse = null;
        try {
            JRBeanCollectionDataSource beanCollectionDataSource = null;
            if (filtrado != null) {
                beanCollectionDataSource = new JRBeanCollectionDataSource(filtrado);
            } else {
                beanCollectionDataSource = new JRBeanCollectionDataSource(getItems());
            }
            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/repLotes.jasper");
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, new HashMap(), beanCollectionDataSource);

            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.addHeader("Content-disposition", "attachment;filename=reporte.pdf");

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
     */
    public void pdf() {

        HttpServletResponse httpServletResponse = null;
        if (getSelected() != null) {
            try {
                HashMap map = new HashMap();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");

                map.put("nro_mesa_entrada", getSelected().getEntradaDocumento().getNroMesaEntrada());
                map.put("entregado_por", getSelected().getEntradaDocumento().getEntregadoPor());
                map.put("nro_cedula_ruc", getSelected().getEntradaDocumento().getNroCedulaRuc());
                map.put("telefono", getSelected().getEntradaDocumento().getTelefono());
                map.put("descripcion", getSelected().getDescripcionMesaEntrada());
                map.put("recibido_por", getSelected().getEntradaDocumento().getRecibidoPor().getNombresApellidos());

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
                    tramite += doc.getDescripcionMesaEntrada();
                    foliosImp += doc.getFolios();
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

                map.put("nro_mesa_entrada", getSelected().getEntradaDocumento().getNroMesaEntrada());
                map.put("entregado_por", getSelected().getEntradaDocumento().getEntregadoPor());
                map.put("nro_cedula_ruc", getSelected().getEntradaDocumento().getNroCedulaRuc());
                map.put("telefono", getSelected().getEntradaDocumento().getTelefono());
                map.put("descripcion", getSelected().getDescripcionMesaEntrada());
                map.put("recibido_por", getSelected().getEntradaDocumento().getRecibidoPor().getNombresApellidos());

                Collection<DocumentosJudiciales> col = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByEntradaDocumento", DocumentosJudiciales.class
                ).setParameter("entradaDocumento", getSelected().getEntradaDocumento()).getResultList();

                String tramite = "";
                boolean primero = true;
                for (DocumentosJudiciales doc : col) {
                    if (primero) {
                        tramite += "\n";
                        primero = false;
                    }
                    tramite += doc.getDescripcionMesaEntrada() + ", Detalles :" + doc.getFolios() + "\n-----------------------------------------\n";
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
                    tramite += doc.getDescripcionMesaEntrada() + ", Detalles :" + doc.getFolios() + "\n\n";
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
                        + "        NRO " + getSelected().getEntradaDocumento().getNroMesaEntrada() + "\n"
                        + "Fecha: " + format.format(fechaHoraAlta) + "\n"
                        + "Hora: " + format2.format(fechaHoraAlta) + "\n"
                        + "Entregado por: " + getSelected().getEntradaDocumento().getEntregadoPor() + "\n"
                        + // "nro CI: " + getSelected().getEntradaDocumento().getNroCedulaRuc() + "\n" +
                        "Telefono: " + getSelected().getEntradaDocumento().getTelefono() + "\n"
                        + "Documentos:\n";

                Collection<DocumentosJudiciales> col = this.ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findByEntradaDocumento", DocumentosJudiciales.class
                ).setParameter("entradaDocumento", getSelected().getEntradaDocumento()).getResultList();

                for (DocumentosJudiciales doc : col) {
                    impresion += " -- " + doc.getDescripcionMesaEntrada() + " (Detalles :" + doc.getFolios() + ")\n_______________________\n";
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
}
