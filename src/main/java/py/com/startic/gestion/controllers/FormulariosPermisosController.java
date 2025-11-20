package py.com.startic.gestion.controllers;

import groovy.json.internal.Chr;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;

import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.persistence.Transient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.apache.poi.util.IOUtils;
import org.primefaces.model.file.UploadedFile;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.models.Archivos;
import py.com.startic.gestion.models.ControlarPermisos;
import py.com.startic.gestion.models.Departamentos;
import py.com.startic.gestion.models.EstadosSolicitudPermiso;
import py.com.startic.gestion.models.FlujosDocumento;
import py.com.startic.gestion.models.FormulariosPerVacaciones;
import py.com.startic.gestion.models.FormulariosPermisos;
import py.com.startic.gestion.models.ObservacionesSolicitudesPermisos;
import py.com.startic.gestion.models.ParametrosSistema;
import py.com.startic.gestion.models.Roles;
import py.com.startic.gestion.models.RolesPorUsuarios;
import py.com.startic.gestion.models.TiposDocumentosJudiciales;
import py.com.startic.gestion.models.TiposFormulario;
import py.com.startic.gestion.models.TiposPermisos;
import py.com.startic.gestion.models.Usuarios;
import py.com.startic.gestion.models.VUsuarioVacaciones;

@Named(value = "formulariosPermisosController")
@ViewScoped
public class FormulariosPermisosController extends AbstractController<FormulariosPermisos> {

    @Inject
    private ArchivosController archivosController;
    @Inject
    private ObservacionesSolicitudesPermisosController obsController;
    @Inject
    private ControlarPermisosController controlarPermisosController;
    @Inject
    private FormulariosPerVacacionesController formularioPerVacacionesController;
    @Inject
    private RhFeriadosControllers rhFeriadosControllers;
    private List<TiposPermisos> listaTiposPermisos;
    private TiposPermisos tiposPermisos;
    private TiposFormulario tiposFormulario;
    private TiposDocumentosJudiciales tipoDoc;
    private Collection<Usuarios> listaUsuariosTransf;
    private Departamentos dependencia;
    private String jefatura;
    private String direccion;
    private Usuarios funcionario;
    private String nroDocumento;
    private String direccionGeneral;
    private UploadedFile file;
    private UploadedFile fotoReposo;
    private FlujosDocumento flujoDoc;
    private Usuarios usuario;
    private ParametrosSistema par;
    private Date fechaDesde;
    private Date fechaHasta;
    private HttpSession session;
    private String url;
    private String endpoint;
    private Archivos docImprimir;
    private Archivos imgImprimir;
    private String ultimaObservacion;
    private String nombre;
    private String content;
    private String contentImg;
    private Integer tipoAcumulacion;
    private Integer tipoVacaciones;

    private boolean campoDeshabilitado = true;
    private boolean desabilitarCantVacaciones;
    private boolean desabilitarCantTraslado;
    private String tipoUsufructo;
    private boolean acumulado;
    private boolean renderdatalistvacciones;

    private List<VUsuarioVacaciones> usuarioVacaciones;
    private List<VUsuarioVacaciones> usuarioVacacionesSelected;
    private Integer totalVacas;
    private Integer totalVacasLista;
    private boolean archivoFormularioGuardado = false;
    private FormulariosPermisos formulario;

    public List<TiposPermisos> getListaTiposPermisos() {
        return listaTiposPermisos;
    }

    public void setListaTiposPermisos(List<TiposPermisos> listaTiposPermisos) {
        this.listaTiposPermisos = listaTiposPermisos;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public UploadedFile getFotoReposo() {
        return fotoReposo;
    }

    public void setFotoReposo(UploadedFile fotoReposo) {
        this.fotoReposo = fotoReposo;
    }

    public String getUltimaObservacion() {
        return ultimaObservacion;
    }

    public void setUltimaObservacion(String ultimaObservacion) {
        this.ultimaObservacion = ultimaObservacion;
    }

    public TiposPermisos getTiposPermisos() {
        return tiposPermisos;
    }

    public void setTiposPermisos(TiposPermisos tiposPermisos) {
        this.tiposPermisos = tiposPermisos;
    }

    public TiposFormulario getTiposFormulario() {
        return tiposFormulario;
    }

    public void setTiposFormulario(TiposFormulario tiposFormulario) {
        this.tiposFormulario = tiposFormulario;
    }

    public Departamentos getDependencia() {
        return dependencia;
    }

    public void setDependencia(Departamentos dependencia) {
        this.dependencia = dependencia;
    }

    public Usuarios getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Usuarios funcionario) {
        this.funcionario = funcionario;
    }

    public String getNroDocumento() {
        return nroDocumento;
    }

    public void setNroDocumento(String nroDocumento) {
        this.nroDocumento = nroDocumento;
    }

    public String getDireccionGeneral() {
        return direccionGeneral;
    }

    public void setDireccionGeneral(String direccionGeneral) {
        this.direccionGeneral = direccionGeneral;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public FlujosDocumento getFlujoDoc() {
        return flujoDoc;
    }

    public void setFlujoDoc(FlujosDocumento flujoDoc) {
        this.flujoDoc = flujoDoc;
    }

    public Archivos getDocImprimir() {
        return docImprimir;
    }

    public void setDocImprimir(Archivos docImprimir) {
        this.docImprimir = docImprimir;
    }

    public Archivos getImgImprimir() {
        return imgImprimir;
    }

    public void setImgImprimir(Archivos imgImprimir) {
        this.imgImprimir = imgImprimir;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ParametrosSistema getPar() {
        return par;
    }

    public void setPar(ParametrosSistema par) {
        this.par = par;
    }

    public String getJefatura() {
        return jefatura;
    }

    public void setJefatura(String jefatura) {
        this.jefatura = jefatura;
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

    public TiposDocumentosJudiciales getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(TiposDocumentosJudiciales tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public boolean isCampoDeshabilitado() {
        return campoDeshabilitado;
    }

    public void setCampoDeshabilitado(boolean campoDeshabilitado) {
        this.campoDeshabilitado = campoDeshabilitado;
    }

    public Integer getTipoAcumulacion() {
        return Constantes.TIPO_SOLICITUD_TRANSLADO;
    }

    public Integer getTipoVacaciones() {
        return Constantes.TIPO_SOLICITUD_VACACIONES;
    }

    public String getTipoUsufructo() {
        return tipoUsufructo;
    }

    public void setTipoUsufructo(String tipoUsufructo) {
        this.tipoUsufructo = tipoUsufructo;
    }

    public List<VUsuarioVacaciones> getUsuarioVacaciones() {
        return usuarioVacaciones;
    }

    public void setUsuarioVacaciones(List<VUsuarioVacaciones> usuarioVacaciones) {
        this.usuarioVacaciones = usuarioVacaciones;
    }

    public List<VUsuarioVacaciones> getUsuarioVacacionesSelected() {
        return usuarioVacacionesSelected;
    }

    public void setUsuarioVacacionesSelected(List<VUsuarioVacaciones> usuarioVacacionesSelected) {
        this.usuarioVacacionesSelected = usuarioVacacionesSelected;
    }

    public boolean isRenderdatalistvacciones() {
        return renderdatalistvacciones;
    }

    public void setRenderdatalistvacciones(boolean renderdatalistvacciones) {
        this.renderdatalistvacciones = renderdatalistvacciones;
    }

    @PostConstruct
    @Override
    public void initParams() {
        par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID).getSingleResult();
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        int pos = url.lastIndexOf(uri);
        url = url.substring(0, pos);

        String[] array = uri.split("/");
        endpoint = array[1];

        usuario = (Usuarios) session.getAttribute("Usuarios");
        // funcionario = usuario;
        setItems(ejbFacade.getEntityManager().createNamedQuery("FormulariosPermisos.findOrdered", FormulariosPermisos.class).setParameter("funcionario", usuario).setParameter("responsableDestino", usuario).getResultList());
        if (!getItems2().isEmpty()) {
            FormulariosPermisos art = getItems2().iterator().next();
            setSelected(art);

        } else {
            setSelected(null);
        }
        funcionario = usuario;
        cargarDatosAdicionales();
        renderdatalistvacciones = false;
        // setearItems();
    }

    public FormulariosPermisosController() {
        // Inform the Abstract parent controller of the concrete TiposPersona Entity
        super(FormulariosPermisos.class);
    }

    public void prepareTransferir() {

    }

    public void prepareEdit() {
        ultimaObservacion = null;
        file = null;
    }

    public Collection<Usuarios> getListaUsuariosTransf() {
        if (Constantes.ESTADO_PEDIDO_ARTICULO_CA.equals(getSelected().getEstado().getCodigo())) {

            if (getSelected() != null) {

                listaUsuariosTransf = this.ejbFacade.getEntityManager()
                        .createNamedQuery("Usuarios.findTransferirDpto", Usuarios.class)
                        .setParameter("tipoDocumento", Constantes.TIPO_DOCUMENTO_JUDICIAL_SO)
                        .setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo())
                        .setParameter("departamento", usuario.getDepartamento())
                        .getResultList();
            } else {
                listaUsuariosTransf = new ArrayList<>();
            }
        } else {
            if (getSelected() != null) {

                listaUsuariosTransf = this.ejbFacade.getEntityManager()
                        .createNamedQuery("Usuarios.findTransferirPedido", Usuarios.class)
                        .setParameter("tipoDocumento", Constantes.TIPO_DOCUMENTO_JUDICIAL_SO)
                        .setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo())
                        .getResultList();
            } else {
                listaUsuariosTransf = new ArrayList<>();
            }
        }
        return listaUsuariosTransf;
    }

    public void setListaUsuariosTransf(Collection<Usuarios> listaUsuariosTransf) {
        this.listaUsuariosTransf = listaUsuariosTransf;
    }

    private List<TiposPermisos> obtenerListaTiposPermisos(TiposFormulario tiposFormulario) {
        if (tiposFormulario != null) {
            return this.ejbFacade.getEntityManager().createNamedQuery("TiposPermisos.findByListas", TiposPermisos.class
            ).setParameter("tiposFormulario", tiposFormulario).getResultList();
        }
        return null;
    }

    public void actualizarListas(TiposFormulario tiposFormulario) {
        listaTiposPermisos = obtenerListaTiposPermisos(tiposFormulario);
    }

    public void resetearListas(TiposFormulario tiposFormulario) {
        if (tiposFormulario != null) {
            if (tiposFormulario.getId().equals(Constantes.TIPOFORM_SOLICITUDVACACIONES)) {
                renderdatalistvacciones = true;
            } else {
                getSelected().setCantidadDia(null);
                getSelected().setAcumulacion(null);
                getSelected().setAño(null);
                renderdatalistvacciones = false;
            }
        } else {
            renderdatalistvacciones = false;
        }
        tiposPermisos = null;

        if (getSelected() != null) {
            getSelected().setTiposPermisos(null);

        }
        actualizarListas(tiposFormulario);
    }

    public void cargarDatosAdicionales() {
        if (funcionario != null) {
            jefatura = funcionario.getDepartamento().getNombre();
            nroDocumento = funcionario.getCi();
            direccionGeneral = funcionario.getDepartamento().getDepartamentoPadre().getNombre();
            // direccionGeneral = "";
            // nroDocumento= "";

        }
    }

    public void actualizarCampos() {
        // Lógica para deshabilitar campos según el tipo de permiso
        if (getSelected().getTiposPermisos().getId().equals(1) || getSelected().getTiposPermisos().getId().equals(2)) {
            campoDeshabilitado = true;
        } else {
            campoDeshabilitado = false;
        }
        if (getSelected().getTiposPermisos().getId().equals(3)) {
            campoDeshabilitado = true;
        } else {
            campoDeshabilitado = false;
        }
    }

    public void onPermisoChange() {
        // Deshabilitar campo solo si tipoPermiso es 
        campoDeshabilitado = getSelected().getTiposPermisos().getId().equals(1) || getSelected().getTiposPermisos().getId().equals(2);

    }

    public String navigateObservacionesDocumentosResolucionesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("doc_origen", getSelected());
            //FacesContext
            //        .getCurrentInstance().getExternalContext().getRequestMap().put("ObservacionesDocumentosJudiciales_items", ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosJudiciales.findByDocumentoJudicial", ObservacionesDocumentosJudiciales.class
            //        ).setParameter("documentoJudicial", getSelected()).getResultList());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paginaVolver", "/pages/solicitudPermiso/index");
        }
        return "/pages/observacionesSolicitudesPermisos/index";
    }

    public void agregarObs() {

        if (getSelected() != null) {
            if (getSelected().verifObs()) {
                Date fecha = ejbFacade.getSystemDate();

                getSelected().setFechaUltimaObservacion(fecha);

                getSelected().transferirObs();

                getSelected().setUsuarioUltimaObservacion(usuario);

                ObservacionesSolicitudesPermisos obs = obsController.prepareCreate(null);

                obs.setUsuarioAlta(usuario);
                obs.setUsuarioUltimoEstado(usuario);
                obs.setFechaHoraAlta(fecha);
                obs.setFechaHoraUltimoEstado(fecha);
                obs.setEmpresa(usuario.getEmpresa());
                obs.setObservacion(getSelected().getUltimaObservacionAux());

                obsController.setSelected(obs);
                obsController.saveNew(null);

                getSelected().setObservacionSolicitudesPermisos(obs);

                super.save(null);
                obs.setFormulariosPermisos(getSelected());

                obsController.save(null);
            }
            saveDptoRechazar();

        }
    }

    public boolean deshabilitarVerDoc(FormulariosPermisos doc) {
        if (doc != null) {
            // if (doc.getDepartamento().equals(usuario.getDepartamento())) {
            List<Archivos> lista = ejbFacade.getEntityManager().createNamedQuery("Archivos.findByDocumentoFormularioFiltroPdf", Archivos.class).setParameter("formulariosPermisos", doc).getResultList();

            return lista.isEmpty();
            // }
        }
        return true;
    }

    public boolean deshabilitarVerImagen(FormulariosPermisos doc) {
        if (doc != null) {
            // if (doc.getDepartamento().equals(usuario.getDepartamento())) {
            List<Archivos> lista = ejbFacade.getEntityManager().createNamedQuery("Archivos.findByDocumentoFormularioFiltroImagen", Archivos.class).setParameter("formulariosPermisos", doc).getResultList();

            return lista.isEmpty();
            // }
        }
        return true;
    }

    public Integer getTotalVacas() {
        return totalVacas;
    }

    public void setTotalVacas(Integer totalVacas) {
        this.totalVacas = totalVacas;
    }

    public Integer getTotalVacasLista() {
        return totalVacasLista;
    }

    public void setTotalVacasLista(Integer totalVacasLista) {
        this.totalVacasLista = totalVacasLista;
    }

    public void onChangeCalcularVacaciones() {

        desabilitarCantVacaciones = true;
        desabilitarCantTraslado = true;
        tiposPermisos = getSelected().getTiposPermisos();
        tipoUsufructo = "";
        acumulado = false;
        setCurrenYear();
        Long cantidad = 0L;
        BigDecimal utilizado = BigDecimal.ZERO;
        Integer resto = 0;
        //EL ESTADO EN QUE ESTÁN LAS SOLICITUDES QUE SE CONSULTAN
        EstadosSolicitudPermiso estado = new EstadosSolicitudPermiso(Constantes.ESTADO_SOLICITUD_PERMISO_5);

        if (getSelected().getTiposPermisos().getId().equals(Constantes.TIPO_SOLICITUD_VACACIONES)) {
            usuarioVacaciones = ejbFacade.getEntityManager()
                    .createNamedQuery("VUsuarioVacaciones.findByUsuarioNoCero", VUsuarioVacaciones.class)
                    .setParameter("usuario", funcionario.getUsuario())
                    .getResultList();
        } else {
            usuarioVacaciones = ejbFacade.getEntityManager()
                    .createNamedQuery("VUsuarioVacaciones.findByUsuarioNoAcumuladoNoCero", VUsuarioVacaciones.class)
                    .setParameter("usuario", funcionario.getUsuario())
                    .getResultList();
        }
        totalVacasLista = 0;
        for (VUsuarioVacaciones uv : usuarioVacaciones) {
            totalVacasLista = totalVacasLista + (uv.getDias() - uv.getCantidadUtilizada());
        }

        totalVacas = 0;

        /*
        for (VUsuarioVacaciones uv : usuarioVacaciones) {

            if (getSelected() != null) {
                //ControlarPermisos controlarper = verificarTrasladoVacaciones(funcionario);
                ControlarPermisos controlarper = null;
                if ("acumulado".equals(uv.getTipo())) {
                    controlarper = new ControlarPermisos();
                    controlarper.setCantidadAcumulada(uv.getDias());
                    controlarper.setCantidadUsada(uv.getCantidadUtilizada());
                }
                //boolean verif = controlarper != null;

                if (controlarper != null && controlarper.getCantidadAcumulada() > 0) {
                    cantidad = controlarper.getCantidadAcumulada() != null ? Long.valueOf(controlarper.getCantidadAcumulada()) : 0L;
                    utilizado = controlarper.getCantidadUsada() != null ? new BigDecimal(controlarper.getCantidadUsada()) : BigDecimal.ZERO;
                    tipoUsufructo = "(Acumuladas)";
                    acumulado = true;
                    resto = cantidad.intValue() - utilizado.intValue();
                }
                if (controlarper == null || resto <= 0) {
                    try {
                        //cantidad = getVacacionesDias(funcionario, getSelected().getAño());
                        cantidad = Long.valueOf(uv.getDias());
                    } catch (Exception e) {
                        cantidad = 0L;
                        e.printStackTrace();
                    }

                    try {
                        //utilizado = getVacacionesUtilizadas(funcionario, estado, tiposPermisos);
                        utilizado = new BigDecimal(uv.getCantidadUtilizada());
                    } catch (Exception e) {
                        utilizado = BigDecimal.ZERO;
                        e.printStackTrace();
                    }
                    tipoUsufructo = "(Vigentes)";
                    acumulado = false;
                }

                if (cantidad.intValue() >= utilizado.intValue()) {
                    resto = cantidad.intValue() - utilizado.intValue();
                }
                totalVacas = totalVacas + resto;
            }
        }*/
        if (tiposFormulario.getId().equals(Constantes.TIPOFORM_SOLICITUDVACACIONES)) {
            if (getSelected().getTiposPermisos().getId().equals(Constantes.TIPO_SOLICITUD_TRANSLADO)) {
                desabilitarCantTraslado = false;
                getSelected().setAcumulacion(totalVacas);
                getSelected().setCantidadDia(null);
            }

            if (getSelected().getTiposPermisos().getId().equals(Constantes.TIPO_SOLICITUD_VACACIONES)) {
                getSelected().setAcumulado(acumulado);
                desabilitarCantVacaciones = false;
                getSelected().setCantidadDia(totalVacas);
                getSelected().setAcumulacion(null);
            }
            getSelected().setAño(null);
        }
    }

    public String validarFechaPermiso(Date fechaParam) {
        if (getSelected() != null) {
            Date fecha = ejbFacade.getSystemDate();
            if (tiposFormulario != null && tiposFormulario.getId().equals(Constantes.TIPOFORM_AUSENCIAPORSALUD)
                    || getSelected().getTiposPermisos() != null && getSelected().getTiposPermisos().getId().equals(Constantes.TIPO_SOLICITUD_PARTICULAR)) {
                if (fecha.before(fechaParam)) {
                    return "La solicitud debe ser presentada después de de la fecha desde.";
                }
            }
            /*
            long diff = fecha.getTime() - fechaDesde.getTime();
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            diffHours = diffHours + diffDays * 24;
             */
            Date fechaUno = getSelected().getFechaHasta();
            if (getSelected().getTiposPermisos() != null && getSelected().getTiposPermisos().getId().equals(Constantes.TIPO_SOLICITUD_PARTICULAR)) {
                fechaUno = fechaParam != null ? fechaParam : getSelected().getFechaHasta();
            }
            //SE CUENTAN LOS DÍAS HÁBILES SIN CONTAR LOS FERIADOS DE LA TABLA RH_FERIADOS
            long diffDays = rhFeriadosControllers.diffFechas(fechaUno, fecha);
            long diffHours = diffDays * 24;

            if (tiposFormulario != null && tiposFormulario.getId().equals(Constantes.TIPOFORM_AUSENCIAPORSALUD)) {
                if (diffHours > Constantes.HORAS_PRESENTACION_SALUD) {
                    return "Permiso por salud debe ser presentada dentro de las " + Constantes.HORAS_PRESENTACION_SALUD + " horas.";
                }
            }
            if (getSelected().getTiposPermisos() != null && getSelected().getTiposPermisos().getId().equals(Constantes.TIPO_SOLICITUD_PARTICULAR)) {
                if (diffHours > Constantes.HORAS_PRESENTACION_PARTICULARES) {
                    return "Permiso tipo particulares debe ser presentada dentro de las " + Constantes.HORAS_PRESENTACION_PARTICULARES + " horas.";
                }
            }
            if (getSelected().getTiposPermisos() != null && getSelected().getTiposPermisos().getId().equals(Constantes.TIPO_SOLICITUD_FALLECIMIENTO)) {
                if (diffHours > Constantes.HORAS_PRESENTACION_FALLECIMIENTO) {
                    return "Permiso tipo particulares debe ser presentada dentro de las " + Constantes.HORAS_PRESENTACION_FALLECIMIENTO + " horas.";
                }
            }
            return "ok";
        } else {
            return "No se encuentra registro seleccionado.";
        }
    }

    public void onChangeHorasDias() {
        diferenciasHoras();
        diferenciasDias();
    }

    public void diferenciasHoras() {
        Date hoDsd = getSelected().getHoraDesde();
        Date hoHst = getSelected().getHoraHasta();
        if (hoDsd != null && hoHst != null) {
            long seconds = (hoHst.getTime() - hoDsd.getTime()) / 1000;
            int horas = Math.round(seconds / 3600);
            getSelected().setHoras(String.valueOf(horas));
        }
    }

    public void diferenciasDias() {
        Date feDsd = getSelected().getFechaDesde();
        Date feHst = getSelected().getFechaHasta();
        if (feDsd != null && feHst != null) {
            long diff = (feHst.getTime() - feDsd.getTime());

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            if (diffHours >= 6) {
                diffDays = diffDays + 1;
            }
            diffDays = diffDays + 1;

            getSelected().setDias(String.valueOf(diffDays));
        }
    }

    public void onRowSelectDatalistFormulario() {
        totalizarLista();
    }

    public void onRowUnSelectDatalistFormulario() {
        totalizarLista();
    }

    public void totalizarLista() {
        FormulariosPermisos sel = getSelected();
        if (sel != null) {
            Integer tot = 0;
            Integer elAnio = 0;//MENOR AÑO ENTRE LOS SELECCIONADOS
            boolean ban = false;
            for (VUsuarioVacaciones item : usuarioVacacionesSelected) {
                //int cantDias = sel.getCantidadDia()!=null?sel.getCantidadDia():0;
                int dias = item.getDias() != null ? item.getDias() : 0;
                int util = item.getCantidadUtilizada() != null ? item.getCantidadUtilizada() : 0;
                int ll = dias - util;
                tot = tot + ll;
                if (!ban) {
                    ban = true;
                    elAnio = item.getAnio();
                }
                if (elAnio > item.getAnio()) {
                    elAnio = item.getAnio();
                }
            }
            if (getSelected().getTiposPermisos().getId().equals(Constantes.TIPO_SOLICITUD_VACACIONES)) {
                sel.setCantidadDia(tot);
            } else {
                sel.setAcumulacion(tot);
            }
            sel.setAño(String.valueOf(elAnio));
        }
    }

    public boolean isDesabilitarCantVacaciones() {
        return desabilitarCantVacaciones;
    }

    public void setDesabilitarCantVacaciones(boolean desabilitarCantVacaciones) {
        this.desabilitarCantVacaciones = desabilitarCantVacaciones;
    }

    public boolean isDesabilitarCantTraslado() {
        return desabilitarCantTraslado;
    }

    public void setDesabilitarCantTraslado(boolean desabilitarCantTraslado) {
        this.desabilitarCantTraslado = desabilitarCantTraslado;
    }

    public void prepareVerDoc(FormulariosPermisos doc) {

        List<Archivos> lista = ejbFacade.getEntityManager().createNamedQuery("Archivos.findByDocumentoFormularioFiltroPdf", Archivos.class).setParameter("formulariosPermisos", doc).getResultList();

        if (!lista.isEmpty()) {
            docImprimir = lista.get(0);
        }

        //PrimeFaces.current().ajax().update("ExpAcusacionesViewForm");
    }

    public void prepareVerImagen(FormulariosPermisos doc) {

        List<Archivos> lista = ejbFacade.getEntityManager().createNamedQuery("Archivos.findByDocumentoFormularioFiltroImagen", Archivos.class).setParameter("formulariosPermisos", doc).getResultList();

        if (!lista.isEmpty()) {
            imgImprimir = lista.get(0);
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
        return getContent(docImprimir);
    }

    public String getContentImg() {
        return getContent(imgImprimir);
    }

    public String getContent(Archivos docImprimir) {

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

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        //empresaController.setSelected(null);
    }

    public boolean desabilitarBotonCambioEstado() {

        if (getSelected() != null) {
            try {
                FormulariosPermisos sel = getSelected();
                boolean habilitar = sel.getEstado().getCodigo().equals(Constantes.ESTADO_SOLICITUD_PERMISO_1) && sel.getFuncionario().equals(usuario)
                        || sel.getEstado().getCodigo().equals(Constantes.ESTADO_SOLICITUD_PERMISO_3) && sel.getResponsableDestino().equals(usuario);
                return !habilitar;

            } catch (Exception e) {

            }
        }
        return true;
    }

    public boolean desabilitarBotonAprobarRechazar() {

        if (getSelected() != null) {
            try {
                FormulariosPermisos sel = getSelected();
                boolean habilitar = sel.getEstado().getCodigo().equals(Constantes.ESTADO_SOLICITUD_PERMISO_2) && sel.getResponsableDestino().equals(usuario)
                        || sel.getEstado().getCodigo().equals(Constantes.ESTADO_SOLICITUD_PERMISO_4) && sel.getResponsableDestino().equals(usuario);
                return !habilitar;

            } catch (Exception e) {

            }
        }
        return true;
    }

    public boolean desabilitarBotonObs() {

        if (getSelected() != null) {
            try {
                FormulariosPermisos sel = getSelected();
                boolean habilitar = sel.getEstado().getCodigo().equals(Constantes.ESTADO_SOLICITUD_PERMISO_6) && sel.getFuncionario().equals(usuario)
                        || sel.getEstado().getCodigo().equals(Constantes.ESTADO_SOLICITUD_PERMISO_7) && sel.getFuncionario().equals(usuario);
                return !habilitar;

            } catch (Exception e) {

            }
        }
        return true;
    }

    public void save() {
        try {
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            boolean guardando = (boolean) ((params.get("guardandoEdit") == null) ? false : params.get("guardandoEdit"));
            if (!guardando) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("guardandoEdit", true);
                if (getSelected() != null) {
                    if (getSelected().getFuncionario().getDepartamento().getId().equals(usuario.getDepartamento().getId())) {

                        Date fecha = ejbFacade.getSystemDate();

                        getSelected().setFechaHoraUltimoEstado(fecha);
                        getSelected().setUsuarioUltimoEstado(usuario);

                        super.save(null);

                        List<Archivos> lista = ejbFacade.getEntityManager().createNamedQuery("Archivos.findByDocumentoFormularioSolicitudOrdered", Archivos.class).setParameter("formulariosPermisos", getSelected()).getResultList();
                        try {
                            if (lista.isEmpty()) {
                                alzarArchivo(null);
                            } else {
                                alzarArchivo(lista.get(0));
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JsfUtil.addErrorMessage("Error al subir archivo. " + ex.getMessage());
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

    @Override
    public void saveNew(ActionEvent event) {
        if (getSelected() != null) {
            if (getSelected().getFechaDesde() != null) {
                if (getSelected().getTiposPermisos().getId() != Constantes.TIPO_SOLICITUD_VACACIONES) {
                    //VALIDACION SALUD 72 HS, PARTICULAR 48 HS
                    String mensaje = validarFechaPermiso(getSelected().getFechaDesde());
                    if (!mensaje.equals("ok")) {
                        JsfUtil.addErrorMessage(mensaje);
                        return;
                    }
                }
            } else {
                if ((tiposFormulario != null && tiposFormulario.getId().equals(Constantes.TIPOFORM_AUSENCIAPORSALUD))
                        || getSelected().getTiposPermisos() != null && getSelected().getTiposPermisos().getId().equals(Constantes.TIPO_SOLICITUD_PARTICULAR)) {
                    JsfUtil.addErrorMessage("Fecha Hasta no puede quedar nula.");
                    return;
                }
            }
            if (getSelected().getFechaDesde() == null) {
                if ((tiposFormulario != null && tiposFormulario.getId().equals(Constantes.TIPOFORM_AUSENCIAPORSALUD))
                        || getSelected().getTiposPermisos() != null && getSelected().getTiposPermisos().getId().equals(Constantes.TIPO_SOLICITUD_PARTICULAR)) {
                    JsfUtil.addErrorMessage("Fecha Desde no puede quedar nula.");
                    return;
                }
            }
            if (tiposFormulario.getId() == Constantes.TIPOFORM_SOLICITUDVACACIONES) {
                if (getSelected().getTiposPermisos().getId() == Constantes.TIPO_SOLICITUD_VACACIONES
                        && (getSelected() == null || getSelected().getCantidadDia() == 0)) {
                    JsfUtil.addErrorMessage("La cantidad de días no puede ser igual a cero.");
                    return;
                }
                if (getSelected().getTiposPermisos().getId().equals(Constantes.TIPO_SOLICITUD_TRANSLADO)
                        && (getSelected() == null || getSelected().getAcumulacion() == 0)) {
                    JsfUtil.addErrorMessage("La cantidad de días no puede ser igual a cero.");
                    return;
                }
            }

            Long cantPermisosPartic = 0L;

            EstadosSolicitudPermiso estado = null;

            try {
                // Codigo de enviado a secretaria
                estado = this.ejbFacade.getEntityManager()
                        .createNamedQuery("EstadosSolicitudPermiso.findByCodigo", EstadosSolicitudPermiso.class)
                        .setParameter("codigo", Constantes.ESTADO_SOLICITUD_PERMISO_5)
                        .getSingleResult();

            } catch (Exception ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo determinar sgte estado. Documento no se puede transferir");
                return;
            }
            //PERMISO PARTICULARES. CONTROL CANTIDAD ANUAL AL GUARDAR NUEVA SOLICITUD
            if (tiposPermisos.getId().equals(Constantes.TIPO_PERMISO_PARTICULARES)) {
                try {
                    cantPermisosPartic = (Long) ejbFacade.getEntityManager()
                            .createNativeQuery("SELECT coalesce(count(*), 0) FROM formularios_permisos f\n"
                                    + "where f.funcionario=?1 and f.año = ?2 and f.tipo_permiso=?3 and f.estado =?4;")
                            .setParameter(1, funcionario.getId())
                            .setParameter(2, getSelected().getAño())
                            .setParameter(3, getSelected().getTiposPermisos().getId())
                            .setParameter(4, estado.getCodigo())
                            .getSingleResult();
                    if (cantPermisosPartic >= Constantes.CANTIDAD_PERM_PARTICULAR_POR_ANIO) {
                        JsfUtil.addErrorMessage("Ya no dispone de Permiso Particulares para este año ");
                        return;
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JsfUtil.addErrorMessage("Error en consulta de particulares:" + ex.getMessage());
                    return;
                }

                try {
                    Calendar cal = Calendar.getInstance();
                    fechaDesde = getSelected().getFechaDesde();

                    cal.setTime(fechaDesde);
                    int month = cal.get(Calendar.MONTH) + 1;

                    char[] mes = Chr.lpad(String.valueOf(month).toCharArray(), 2, '0');

                    Integer funcId = funcionario.getId();
                    String vanio = getSelected().getAño();
                    Integer vtipoper = getSelected().getTiposPermisos().getId();
                    String vestadoId = estado.getCodigo();

                    cantPermisosPartic = (Long) ejbFacade.getEntityManager()
                            .createNativeQuery("SELECT coalesce(count(*), 0) FROM formularios_permisos f \n"
                                    + "where f.funcionario=?1 and f.año = ?2 and f.tipo_permiso=?3 and f.estado =?4 and month(fecha_registro)=?5;")
                            .setParameter(1, funcId)
                            .setParameter(2, vanio)
                            .setParameter(3, vtipoper)
                            .setParameter(4, vestadoId)
                            .setParameter(5, month)
                            .getSingleResult();
                    if (cantPermisosPartic >= Constantes.CANTIDAD_PERM_PARTICULAR_POR_MES) {
                        JsfUtil.addErrorMessage("Ya no dispone de Permiso Particulares para este mes ");
                        return;
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JsfUtil.addErrorMessage("Error en consulta de particulares:" + ex.getMessage());
                    return;
                }
            }
            if (getSelected().getTiposPermisos() != null && getSelected().getTiposPermisos().getId().equals(Constantes.TIPO_SOLICITUD_PARTICULAR)
                    || getSelected().getTiposPermisos() != null && getSelected().getTiposPermisos().getId().equals(Constantes.TIPO_SOLICITUD_REPOSOMEDICO)) {
                /*if (file == null) {

                    JsfUtil.addErrorMessage("Error al leer archivo documento adjunto.");
                    return;
                } else {
                    if (file.getFileName() == null || file.getFileName().isEmpty()) {
                        JsfUtil.addErrorMessage("Error al leer archivo de imagen adjunta");
                        return;
                    } else {
                        if (file.getContent().length <= 0) {
                            JsfUtil.addErrorMessage("Error al leer archivo documento adjunto.");
                            return;
                        }
                    }
                }*/
            }
            if (getSelected().getTiposPermisos() != null && getSelected().getTiposPermisos().getId().equals(Constantes.TIPO_SOLICITUD_REPOSOMEDICO)) {
                if (fotoReposo == null) {
                    JsfUtil.addErrorMessage("Error al leer archivo de imagen adjunta");
                    return;
                } else {
                    if (fotoReposo.getFileName() == null || fotoReposo.getFileName().isEmpty()) {
                        JsfUtil.addErrorMessage("Error al leer archivo de imagen adjunta");
                        return;

                    } else {
                        if (fotoReposo.getContent().length <= 0) {
                            JsfUtil.addErrorMessage("Error al leer archivo de imagen adjunta");
                            return;
                        }
                    }
                }
            }
            Date fecha = ejbFacade.getSystemDate();
            getSelected().setEmpresa(usuario.getEmpresa());
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setFechaRegistro(fecha);
            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioAlta(usuario);
            getSelected().setUsuarioUltimoEstado(usuario);
            getSelected().setDependencia(usuario.getDepartamento());
            getSelected().setFuncionario(funcionario);
            getSelected().setNroDocumento(nroDocumento);
            getSelected().setJefatura(jefatura);
            getSelected().setDireccion(direccion);
            getSelected().setDireccionGeneral(direccionGeneral);
            getSelected().setTiposFormulario(tiposFormulario);
            getSelected().setEstado(new EstadosSolicitudPermiso("1"));

            super.saveNew(event);
            formulario = ejbFacade.find(super.getSelected().getId());

            if (usuarioVacacionesSelected != null) {
                for (VUsuarioVacaciones usuvaca : usuarioVacacionesSelected) {

                    FormulariosPerVacaciones formpervaca = formularioPerVacacionesController.prepareCreate(null);
                    formpervaca.setFormularioPermiso(formulario);
                    formpervaca.setAnio(String.valueOf(usuvaca.getAnio()));
                    Usuarios usuarioFuncionario = ejbFacade.getEntityManager()
                            .createNamedQuery("Usuarios.findById", Usuarios.class)
                            .setParameter("id", Integer.valueOf(usuvaca.getId()))
                            .getSingleResult();
                    formpervaca.setFuncionario(usuarioFuncionario);
                    formpervaca.setCantidadUsada(usuvaca.getCantidadUtilizada());

                    formpervaca.setCantidadAcumulada(usuvaca.getDias());
                    formpervaca.setFechaHoraInsert(fecha);
                    formularioPerVacacionesController.setSelected(formpervaca);
                    formularioPerVacacionesController.saveNew(event);
                }
            }

            if (getSelected().getTiposPermisos().getId() == 2 || getSelected().getTiposPermisos().getId() == 4
                    || getSelected().getTiposPermisos().getId() == 5
                    || getSelected().getTiposPermisos().getId() == 6
                    || getSelected().getTiposPermisos().getId() == 7
                    || getSelected().getTiposPermisos().getId() == 8
                    || getSelected().getTiposPermisos().getId() == 10
                    || getSelected().getTiposPermisos().getId() == 11
                    || getSelected().getTiposPermisos().getId() == 12
                    || getSelected().getTiposPermisos().getId() == 13
                    || getSelected().getTiposPermisos().getId() == 14
                    || getSelected().getTiposPermisos().getId() == 15) {
                try {
                    alzarArchivo(null);
                    if (getSelected().getTiposPermisos().getId() == Constantes.TIPO_SOLICITUD_REPOSOMEDICO) {
                        alzarArchivoFotoReposo();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    String mms = ex.getMessage() == null || ex.getMessage().equals("null") ? "Error al alzar archivo" : ex.getMessage();
                    JsfUtil.addErrorMessage(mms);
                    return;
                }
            }
            setearItems();
        }

    }

    public List<FormulariosPerVacaciones> getSeleccionados(FormulariosPermisos formularioPermiso) {
        List<FormulariosPerVacaciones> opcionesGuardadas = ejbFacade.getEntityManager()
                .createNamedQuery("FormulariosPerVacaciones.findByFormularioPermiso", FormulariosPerVacaciones.class)
                .setParameter("formulariosPermiso", formularioPermiso)
                .getResultList();

        return opcionesGuardadas;
    }

    public void alzarArchivo(Archivos arch) throws Exception {
        if (!desabilitarAdjunto()) {
            alzarArchivo(arch, "pdf", file);
        }
    }

    public void alzarArchivoFotoReposo() throws Exception {
        String filename = fotoReposo.getFileName();
        String extencion = filename.substring((filename.indexOf(".") + 1), filename.length());
        alzarArchivo(null, extencion, fotoReposo);
    }

    public void alzarArchivo(Archivos arch, String extencion, UploadedFile file) throws Exception {

        if (getSelected() != null) {

            if (file == null) {
                throw new Exception("Archivo Nulo.");
            } else if (file.getSize() == 0) {
                throw new Exception("No se seleccionó archivo.");
            }

            Date fecha = ejbFacade.getSystemDate();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String nombreArchivo = simpleDateFormat.format(fecha);
            nombreArchivo += "_" + getSelected().getId() + "." + extencion;

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
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo guardar archivo");
                fos = null;
            } catch (IOException ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo guardar archivo");
                fos = null;
            }

            if (arch == null) {
                Archivos archivo = new Archivos();
                if (formulario == null) {
                    formulario = getSelected();
                }
                archivo.setFormulariosPermisos(formulario);

                archivo.setRuta(nombreArchivo);
                archivo.setFechaHoraAlta(fecha);
                archivo.setFechaHoraUltimoEstado(fecha);
                archivo.setUsuarioAlta(usuario);
                archivo.setUsuarioUltimoEstado(usuario);

                archivosController.setSelected(archivo);
                archivosController.saveNew(null);
                archivoFormularioGuardado = true;
            } else {
                arch.setFormulariosPermisos(formulario);
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

    public void saveDptoRechazar() {
        if (getSelected() != null) {
            Date fecha = ejbFacade.getSystemDate();

            EstadosSolicitudPermiso estado = null;
            String stadoStr = getSelected().getEstado().getCodigo();
            //RECHAZO DE DIRECTOR
            if (getSelected().getEstado().getCodigo().equals(Constantes.ESTADO_SOLICITUD_PERMISO_2)) {
                stadoStr = Constantes.ESTADO_SOLICITUD_PERMISO_6;
            }
            //RECHAZO DE TALENTO H.
            if (getSelected().getEstado().getCodigo().equals(Constantes.ESTADO_SOLICITUD_PERMISO_4)) {
                stadoStr = Constantes.ESTADO_SOLICITUD_PERMISO_7;
            }

            try {
                // Codigo de enviado a secretaria
                estado = this.ejbFacade.getEntityManager()
                        .createNamedQuery("EstadosSolicitudPermiso.findByCodigo", EstadosSolicitudPermiso.class)
                        .setParameter("codigo", stadoStr)
                        .getSingleResult();

            } catch (Exception ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo determinar sgte estado. Documento no se puede transferir");
                return;
            }

            getSelected().setEstado(estado);
            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
            // getSelected().setResponsableDestino(responsableDestino);

            super.save(null);

        }
    }

    public void saveDpto() {
        if (getSelected() != null) {
            funcionario = getSelected().getFuncionario();
            tiposPermisos = getSelected().getTiposPermisos();
            Date fecha = ejbFacade.getSystemDate();
            FlujosDocumento flujoDoc = null;
            try {
                flujoDoc = this.ejbFacade.getEntityManager()
                        .createNamedQuery("FlujosDocumento.findSgteEstado", FlujosDocumento.class)
                        .setParameter("tipoDocumento", Constantes.TIPO_DOCUMENTO_JUDICIAL_SO)
                        .setParameter("estadoDocumentoActual", getSelected().getEstado().getCodigo())
                        .getSingleResult();
            } catch (Exception e) {
                e.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo determinar flujo del documento. Documento no se puede transferir");
                return;
            }

            EstadosSolicitudPermiso estado = null;

            try {
                // Codigo de enviado a secretaria
                estado = this.ejbFacade.getEntityManager()
                        .createNamedQuery("EstadosSolicitudPermiso.findByCodigo", EstadosSolicitudPermiso.class)
                        .setParameter("codigo", flujoDoc.getEstadoDocumentoFinal())
                        .getSingleResult();

            } catch (Exception ex) {
                ex.printStackTrace();
                JsfUtil.addErrorMessage("No se pudo determinar sgte estado. Documento no se puede transferir");
                return;
            }

            getSelected().setEstado(estado);
            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
            // getSelected().setResponsableDestino(responsableDestino);
            int cantDiasvacaciones = 0;
            int cantVacasCorresponde = 0;
            Long cantPermisosPartic = 0L;

            if (estado.getCodigo().equals(Constantes.ESTADO_SOLICITUD_PERMISO_5)) {

                if (getSelected().getTiposFormulario().getId().equals(Constantes.TIPOFORM_SOLICITUDVACACIONES)) {
                    //SOLICITUD DE VACACIONES
                    if (tiposPermisos.getId().equals(Constantes.TIPO_SOLICITUD_VACACIONES)) {
                        try {
                            ControlarPermisos controlarper = null;

                            List<FormulariosPerVacaciones> opciones = getSeleccionados(getSelected());
                            Integer cantidadSolicitada = getSelected().getCantidadDia();
                            Integer cantidadActualizar = 0;
                            for (FormulariosPerVacaciones op : opciones) {
                                Integer vanio = Integer.valueOf(op.getAnio() != null ? op.getAnio() : "0");

                                VUsuarioVacaciones usu = ejbFacade.getEntityManager()
                                        .createNamedQuery("VUsuarioVacaciones.findByUsuarioAnio", VUsuarioVacaciones.class)
                                        .setParameter("id", op.getFuncionario().getId())
                                        .setParameter("anio", vanio)
                                        .getSingleResult();

                                if ("acumulado".equals(usu.getTipo())) {

                                    controlarper = ejbFacade.getEntityManager()
                                            .createNamedQuery("ControlarPermisos.findById", ControlarPermisos.class)
                                            .setParameter("id", usu.getIdDocumento())
                                            .getSingleResult();
                                    Integer cantResto = controlarper.getCantidadAcumulada() - controlarper.getCantidadUsada();
                                    if (cantidadSolicitada > cantResto) {
                                        cantidadActualizar = cantResto;
                                    } else {
                                        cantidadActualizar = cantidadSolicitada;
                                    }
                                    //INDICA QUE LA SOLICITUD SE APLICA A LAS VACACIONES ACUMULADAS
                                    getSelected().setAcumulado(true);
                                    controlarper.setCantidadUsada(controlarper.getCantidadUsada() + cantidadActualizar);
                                    controlarPermisosController.setSelected(controlarper);
                                    controlarPermisosController.save(null);
                                    break;
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JsfUtil.addErrorMessage("Error en sumatoria de vacaciones:" + ex.getMessage());
                            return;
                        }
                    }
                    //SOLICITUD DE TRASLADO/ACUMULACION DE VACACIONES
                    if (tiposPermisos.getId().equals(Constantes.TIPO_SOLICITUD_TRANSLADO)) {

                        ControlarPermisos controlarper = verificarTrasladoVacaciones(funcionario);
                        boolean tieneAcumuladas = controlarper != null;
                        if (tieneAcumuladas) {
                            JsfUtil.addErrorMessage("El funcionario ya tiene un traslado registrado de " + (controlarper.getCantidadAcumulada() != null ? controlarper.getCantidadAcumulada() : "") + " días.");
                            return;
                        } else {
                            ControlarPermisos control = controlarPermisosController.prepareCreate(null);
                            try {
                                if (getSelected().getAño() == null || getSelected().getAño().equals("")) {
                                    JsfUtil.addErrorMessage("El año no puede ser nulo");
                                    return;
                                }
                                Integer anio = Integer.valueOf(getSelected().getAño().trim());
                                control.setAnio(anio);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                JsfUtil.addErrorMessage("Error en formato del año: " + ex.getMessage());
                                return;
                            }
                            control.setFecha(fecha);
                            control.setFuncionario(funcionario);
                            control.setTipoPermiso(tiposPermisos);
                            control.setCantidadAcumulada(getSelected().getAcumulacion());
                            control.setCantidadUsada(0);

                            controlarPermisosController.setSelected(control);
                            controlarPermisosController.saveNew(null);
                        }

                    }
                }
                if (getSelected().getTiposFormulario().getId().equals(Constantes.TIPOFORM_SOLICITUDPERMISO)) {

                    //PERMISO PARTICULARES
                    if (tiposPermisos.getId().equals(Constantes.TIPO_PERMISO_PARTICULARES)) {
                        try {
                            cantPermisosPartic = (Long) ejbFacade.getEntityManager()
                                    .createNativeQuery("SELECT coalesce(count(*), 0) FROM formularios_permisos f\n"
                                            + "where f.funcionario=?1 and f.año = ?2 and f.tipo_permiso=?3 and f.estado =?4;")
                                    .setParameter(1, funcionario.getId())
                                    .setParameter(2, getSelected().getAño())
                                    .setParameter(3, getSelected().getTiposFormulario())
                                    .setParameter(4, estado.getCodigo())
                                    .getSingleResult();
                            if (cantPermisosPartic >= Constantes.CANTIDAD_PERM_PARTICULAR_POR_ANIO) {
                                JsfUtil.addErrorMessage("Ya no dispone de Permiso Particulares para este año ");
                                return;
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JsfUtil.addErrorMessage("Error en consulta de particulares:" + ex.getMessage());
                            return;
                        }
                        try {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(getSelected().getFechaRegistro());
                            int month = cal.get(Calendar.MONTH) + 1;

                            char[] mes = Chr.lpad(String.valueOf(month).toCharArray(), 2, '0');

                            cantPermisosPartic = (Long) ejbFacade.getEntityManager()
                                    .createNativeQuery("SELECT coalesce(count(*), 0) FROM formularios_permisos f \n"
                                            + "where f.funcionario=?1 and f.año = ?2 and f.tipo_permiso=?3 and f.estado =?4 and month(fecha_registro)=?5;")
                                    .setParameter(1, funcionario.getId())
                                    .setParameter(2, getSelected().getAño())
                                    .setParameter(3, getSelected().getTiposFormulario())
                                    .setParameter(4, estado.getCodigo())
                                    .setParameter(5, mes.toString())
                                    .getSingleResult();
                            if (cantPermisosPartic >= Constantes.CANTIDAD_PERM_PARTICULAR_POR_MES) {
                                JsfUtil.addErrorMessage("Ya no dispone de Permiso Particulares para este mes ");
                                return;
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JsfUtil.addErrorMessage("Error en consulta de particulares:" + ex.getMessage());
                            return;
                        }
                    }

                }

            }
            super.save(null);
        }
    }

    public ControlarPermisos verificarTrasladoVacaciones(Usuarios funcionario) {

        ControlarPermisos controlarper;
        try {
            List<ControlarPermisos> contr = ejbFacade.getEntityManager().createNamedQuery("ControlarPermisos.findByFuncionario", ControlarPermisos.class)
                    .setParameter("funcionario", funcionario)
                    .getResultList();
            controlarper = contr.iterator().next();
            /*
            cantAcumu = controlarper.getCantidadAcumulada();
            cantUsada = controlarper.getCantidadUsada();
                    
            System.out.println("ACUMULADA:" + cantAcumu);
            System.out.println("UTILIZADA:" + cantUsada);
            
            if ((cantAcumu - cantUsada) < cantidadSolicitada) {
                JsfUtil.addErrorMessage("La cantidad de vacaciones pendientes acumuladas es de " + (cantAcumu - cantUsada) + " dias");
                return false;
            }
             */

        } catch (Exception e) {
            e.printStackTrace();
            controlarper = null;
        }
        return controlarper;
    }

    public boolean verificarVacaciones(EstadosSolicitudPermiso estado, Usuarios funcionario) throws Exception {
        int cantDiasvacaciones = 0;
        int cantVacasCorresponde = 0;
        int cantPermisosPartic = 0;
        setCurrenYear();
        //VACACIONES YA UTILIZADAS
        BigDecimal diasVacasBD = getVacacionesUtilizadas(funcionario, estado, getSelected().getTiposPermisos());

        //VACACIONES QUE CORRESPONDE POR ANTIGUEDAD
        Long diasVacasCorreLong = getVacacionesDias(funcionario, getSelected().getAño());

        cantVacasCorresponde = diasVacasCorreLong.intValue();
        cantDiasvacaciones = diasVacasBD.intValue();
        if (cantVacasCorresponde - (cantDiasvacaciones + getSelected().getCantidadDia()) < 0) {
            JsfUtil.addErrorMessage("La cantidad de vacaciones pendientes son de " + (cantVacasCorresponde - cantDiasvacaciones) + " dias");
            return false;
        }
        return true;
    }

    public BigDecimal getVacacionesUtilizadas(Usuarios funcionario, EstadosSolicitudPermiso estado, TiposPermisos tipo) {
        BigDecimal diasVacasBD = (BigDecimal) ejbFacade.getEntityManager()
                .createNativeQuery("SELECT coalesce(sum(cantidad_dia), 0) FROM formularios_permisos "
                        + "where funcionario=?1 and año = '" + getSelected().getAño() + "' and tipo_permiso=?2 and estado = ?3 and acumulado = 0")
                .setParameter(1, funcionario.getId())
                .setParameter(2, tipo.getId())
                .setParameter(3, estado.getCodigo())
                .getSingleResult();
        return diasVacasBD;
    }

    public Long getVacacionesDias(Usuarios funcionario, String anio) throws Exception {
        Long diasVacasCorreLong = (Long) ejbFacade.getEntityManager()
                .createNativeQuery("SELECT coalesce(dias, 0) FROM v_usuario_vacaciones where id=?1 and anio=?2")
                .setParameter(1, funcionario.getId())
                .setParameter(2, anio)
                .getSingleResult();
        return diasVacasCorreLong;
    }

    public void setCurrenYear() {
        SimpleDateFormat sf = new SimpleDateFormat("YYYY");
        Date ahora = new Date();
        if (getSelected().getAño() == null) {
            getSelected().setAño(sf.format(ahora));
        }
    }

    public void aprobar() {
        saveDpto();
    }

    public void setearItems() {
        Collection<RolesPorUsuarios> rolesPorUsu = usuario.getRolesPorUsuariosCollection2();
        Collection<Roles> roles = new ArrayList<>();
        boolean cargaSolicitud = false;
        boolean esdirector = false;
        boolean estalento = false;
        for (RolesPorUsuarios rpu : rolesPorUsu) {
            roles.add(rpu.getRoles());
        }
        for (Roles rr : roles) {
            if (rr.getId().equals(Constantes.ROL_CARGASOLICITUD)) {
                cargaSolicitud = true;
            }
            if (rr.getId().equals(Constantes.ROL_DIRECTOR)) {
                esdirector = true;
            }
            if (rr.getId().equals(Constantes.ROL_TALENTO)) {
                estalento = true;
            }
        }
        Collection<FormulariosPermisos> itn = new ArrayList<>();
        if (cargaSolicitud) {
            itn = ejbFacade.getEntityManager().createNamedQuery("FormulariosPermisos.findByFuncionario", FormulariosPermisos.class)
                    .setParameter("funcionario", usuario)
                    .getResultList();
        } else if (esdirector || estalento) {
            /*
            Collection<FlujosDocumento> fdoc = ejbFacade.getEntityManager().createNamedQuery("FlujosDocumento.findByTipoRol", FlujosDocumento.class)
                    .setParameter("tipoDocumento", Constantes.TIPO_DOCUMENTO_JUDICIAL_SO)
                    .setParameter("rolFinal", esdirector ? Constantes.ROL_TALENTO : (estalento ? 0 : -1))
                    .getResultList();
            if (fdoc.iterator().hasNext()) {
                FlujosDocumento row = fdoc.iterator().next();
                String codigoEstado = row.getEstadosDocumento();

                itn = ejbFacade.getEntityManager().createNamedQuery("FormulariosPermisos.findByCodigoEstado", FormulariosPermisos.class)
                        .setParameter("estado", codigoEstado)
                        .getResultList();
            }
             */
            itn = ejbFacade.getEntityManager().createNamedQuery("FormulariosPermisos.findByResponsableDestino", FormulariosPermisos.class)
                    .setParameter("responsableDestino", usuario)
                    .getResultList();
        }
        setItems(itn);
    }

    public boolean desabilitarObservaciones() {
        if (tiposFormulario != null) {
            if (tiposFormulario.getId().equals(Constantes.TIPOFORM_SOLICITUDPERMISO)
                    || tiposFormulario.getId().equals(Constantes.TIPOFORM_AUSENCIAPORSALUD)
                    || tiposFormulario.getId().equals(Constantes.TIPOFORM_SOLICITUDVACACIONES)) {
                return false;
            }
        }
        return true;
    }

    public boolean desabilitarFechaDesde() {
        if (tiposFormulario != null) {
            if (tiposFormulario.getId().equals(Constantes.TIPOFORM_SOLICITUDPERMISO)
                    || tiposFormulario.getId().equals(Constantes.TIPOFORM_AUSENCIAPORSALUD)
                    || tiposFormulario.getId().equals(Constantes.TIPOFORM_SOLICITUDVACACIONES)
                    || tiposFormulario.getId().equals(Constantes.TIPOFORM_COMISIONDETRABAJO)) {
                if (getSelected() != null && getSelected().getTiposPermisos() != null
                        && getSelected().getTiposPermisos().equals(Constantes.TIPO_SOLICITUD_TRANSLADO)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean desabilitarFechaHasta() {
        if (tiposFormulario != null) {
            if (tiposFormulario.getId().equals(Constantes.TIPOFORM_SOLICITUDPERMISO)
                    || tiposFormulario.getId().equals(Constantes.TIPOFORM_AUSENCIAPORSALUD)
                    || tiposFormulario.getId().equals(Constantes.TIPOFORM_SOLICITUDVACACIONES)
                    || tiposFormulario.getId().equals(Constantes.TIPOFORM_COMISIONDETRABAJO)) {
                if (getSelected() != null && getSelected().getTiposPermisos() != null
                        && getSelected().getTiposPermisos().equals(Constantes.TIPO_SOLICITUD_TRANSLADO)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean desabilitarHoraDesde() {
        if (tiposFormulario != null) {
            if (tiposFormulario.getId().equals(Constantes.TIPOFORM_COMISIONDETRABAJO)) {
                return false;
            }
        }
        return true;
    }

    public boolean desabilitarHoraHasta() {
        if (tiposFormulario != null) {
            if (tiposFormulario.getId().equals(Constantes.TIPOFORM_COMISIONDETRABAJO)) {
                return false;
            }
        }
        return true;
    }

    public boolean desabilitarAdjunto() {
        if (tiposFormulario != null && getSelected() != null) {
            tiposPermisos = getSelected().getTiposPermisos();
            if (tiposFormulario.getId().equals(Constantes.TIPOFORM_AUSENCIAPORSALUD)) {
                return false;
            }
            if (tiposPermisos != null) {
                /*if (tiposPermisos.getId().equals(Constantes.TIPO_SOLICITUD_FALLECIMIENTO)
                        || tiposPermisos.getId().equals(Constantes.TIPO_SOLICITUD_MATRIMONIO)
                        || tiposPermisos.getId().equals(Constantes.TIPO_SOLICITUD_MATERNIDAD)
                        || tiposPermisos.getId().equals(Constantes.TIPO_SOLICITUD_PATERNIDAD)) {
                */
                if (tiposFormulario.getId().equals(Constantes.TIPOFORM_SOLICITUDPERMISO) && !tiposPermisos.getId().equals(Constantes.TIPO_SOLICITUD_PARTICULAR)) {
                    return false;
                }
                if (tiposFormulario.getId().equals(Constantes.TIPOFORM_COMISIONDETRABAJO) && !tiposPermisos.getId().equals(Constantes.TIPO_SOLICITUD_SIN_ENTRADASALIDA)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean desabilitarFotoReposo() {
        if (tiposFormulario != null) {
            if (tiposFormulario.getId().equals(Constantes.TIPOFORM_AUSENCIAPORSALUD)
                    && getSelected().getTiposPermisos().getId().equals(Constantes.TIPO_SOLICITUD_REPOSOMEDICO)) {
                return false;
            }
        }
        return true;
    }

    public boolean desabilitarCantidadDias() {
        if (tiposFormulario != null) {
            if (tiposFormulario.getId().equals(Constantes.TIPOFORM_SOLICITUDVACACIONES)) {
                return false;
            }
        }
        return true;
    }

    public boolean desabilitarAcumulacion() {
        if (tiposFormulario != null) {
            if (tiposFormulario.getId().equals(Constantes.TIPOFORM_SOLICITUDVACACIONES)) {
                return false;
            }
        }
        return true;
    }

    public boolean desabilitarDias() {
        if (tiposFormulario != null) {
            if (tiposFormulario.getId().equals(Constantes.TIPOFORM_COMISIONDETRABAJO)) {
                return false;
            }
        }
        return true;
    }

    public boolean desabilitarHoras() {
        if (tiposFormulario != null) {
            if (tiposFormulario.getId().equals(Constantes.TIPOFORM_COMISIONDETRABAJO)) {
                return false;
            }
        }
        return true;
    }

    public boolean desabilitarAnio() {
        if (tiposFormulario != null) {
            if (tiposFormulario.getId().equals(Constantes.TIPOFORM_SOLICITUDVACACIONES)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets the "selected" attribute of the Empresas controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    /* public void prepareEmpresa(ActionEvent event) {
        if (this.getSelected() != null && empresaController.getSelected() == null) {
            empresaController.setSelected(this.getSelected().getEmpresa());
        }
    }*/
}
