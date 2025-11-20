package py.com.startic.gestion.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;

import py.com.startic.gestion.models.Usuarios;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.primefaces.PrimeFaces;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.models.Avisos;
import py.com.startic.gestion.models.AvisosPorUsuarios;
import py.com.startic.gestion.models.AvisosPorUsuariosPK;
import py.com.startic.gestion.models.Departamentos;
import py.com.startic.gestion.models.DocumentosAdministrativosAutoguardados;
import py.com.startic.gestion.models.Estados;
import py.com.startic.gestion.models.EstadosTransferenciaDocumentoAdministrativo;
import py.com.startic.gestion.models.EstadosUsuario;
import py.com.startic.gestion.models.Pantallas;
import py.com.startic.gestion.models.ParametrosSistema;
import py.com.startic.gestion.models.Perfiles;
import py.com.startic.gestion.models.Personas;
import py.com.startic.gestion.models.SesionesLogin;
import py.com.startic.gestion.models.UsuariosAsociados;
import py.com.startic.gestion.models.UsuariosAsociadosPK;
import py.com.startic.gestion.models.VPermisosUsuarios;

@Named(value = "usuariosController")
@ViewScoped
public class UsuariosController extends AbstractController<Usuarios> {

    @Inject
    private DepartamentosController departamentoController;
    @Inject
    private EmpresasController empresaController;
    @Inject
    private EstadosUsuarioController estadoController;
    @Inject
    private PantallasController pantallaPrincipalController;
    @Inject
    private SexosController sexoController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private UsuariosAsociadosController usuariosAsociadosController;
    @Inject
    private SesionesLoginController sesionesLoginController;
    @Inject
    private DocumentosAdministrativosController documentosAdministrativosController;
    @Inject
    private VerDocumentosAdministrativosMesaController verDocumentosAdministrativosMesaController;
    @Inject
    private AvisosPorUsuariosController avisosPorUsuariosController;

    private String usuario;
    private String contrasena;
    private String contrasena2;
    private String nombreUsu;
    private String cambioContrasena1;
    private String cambioContrasena2;
    private String cambioContrasenaFirma1;
    private String cambioContrasenaFirma2;
    private String home;
    private Collection<Usuarios> usuariosDpto;
    private String endpoint;
    private List<Usuarios> listaUsuariosElegir;
    private List<Perfiles> listaPerfilesElegir;
    private Usuarios usuarioElegido;
    private Perfiles perfilElegido;
    private Usuarios usuarioOriginal;
    private Personas personaOriginal;
    private Usuarios usuarioBkp;
    private String politicas;
    private HttpSession session;
    private String nombre;
    private String nombreImagen;

    private Collection<Usuarios> funcionariosSecretaria;

    private String imagenInicio;
    // private String imagenFondo;
    private String imagenLogo;
    private ParametrosSistema par;
    private Usuarios usuarioAsociarSelected;
    private Usuarios usuarioAsociar;
    private List<Usuarios> listaUsuariosAAsociar;
    private List<Usuarios> listaUsuariosAsociar;
    private List<Personas> listaPersonas;
    private final FiltroURL filtroURL = new FiltroURL();
    private Usuarios usuLogin;
    private String pantalla;
    private String hash;
    private String sesionLogin;
    private Integer cantidadArchivosAdmEnProyecto;
    private Integer cantidadArchivosAdmEnRevision;
    private Integer cantidadArchivosAdmFinalizado;
    private Integer cantidadArchivosAdmParaRevision;
    private Integer cantidadArchivosAdmBandejaEntrada;
    private Integer cantidadArchivosAdmBandejaCC;
    private Integer cantidadMesaDeEntradaBandejaEntrada;
    private String stringCantidadArchivosAdmEnProyecto;
    private String stringCantidadArchivosAdmEnRevision;
    private String stringCantidadArchivosAdmFinalizado;
    private String stringCantidadArchivosAdmParaRevision;
    private String stringCantidadArchivosAdmBandejaEntrada;
    private String stringCantidadArchivosAdmBandejaCC;
    private String stringCantidadMesaDeEntradaBandejaEntrada;
    private List<Departamentos> listaDepartamentos;
    private Integer menu;
    private boolean autoGuardadoCreate;
    private boolean autoGuardado;
    private String textoAviso;
    private Avisos docImprimir;
    private String url;
    private String videoAviso;
    private String urlAviso;
    private String urlTexto;

    public String getUrlTexto() {
        return urlTexto;
    }

    public void setUrlTexto(String urlTexto) {
        this.urlTexto = urlTexto;
    }

    public String getUrlAviso() {
        return urlAviso;
    }

    public void setUrlAviso(String urlAviso) {
        this.urlAviso = urlAviso;
    }

    public String getVideoAviso() {
        return videoAviso;
    }

    public void setVideoAviso(String videoAviso) {
        this.videoAviso = videoAviso;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTextoAviso() {
        return textoAviso;
    }

    public void setTextoAviso(String textoAviso) {
        this.textoAviso = textoAviso;
    }

    public String getPoliticas() {
        return politicas;
    }

    public void setPoliticas(String politicas) {
        this.politicas = politicas;
    }

    public Usuarios getUsuLogin() {
        return usuLogin;
    }

    public void setUsuLogin(Usuarios usuLogin) {
        this.usuLogin = usuLogin;
    }

    public List<Departamentos> getListaDepartamentos() {
        return listaDepartamentos;
    }

    public void setListaDepartamentos(List<Departamentos> listaDepartamentos) {
        this.listaDepartamentos = listaDepartamentos;
    }

    public Integer getCantidadMesaDeEntradaBandejaEntrada() {
        return cantidadMesaDeEntradaBandejaEntrada;
    }

    public void setCantidadMesaDeEntradaBandejaEntrada(Integer cantidadMesaDeEntradaBandejaEntrada) {
        this.cantidadMesaDeEntradaBandejaEntrada = cantidadMesaDeEntradaBandejaEntrada;
    }

    public Integer getCantidadArchivosAdmBandejaCC() {
        return cantidadArchivosAdmBandejaCC;
    }

    public void setCantidadArchivosAdmBandejaCC(Integer cantidadArchivosAdmBandejaCC) {
        this.cantidadArchivosAdmBandejaCC = cantidadArchivosAdmBandejaCC;
    }

    public String getStringCantidadArchivosAdmBandejaCC() {
        return stringCantidadArchivosAdmBandejaCC;
    }

    public void setStringCantidadArchivosAdmBandejaCC(String stringCantidadArchivosAdmBandejaCC) {
        this.stringCantidadArchivosAdmBandejaCC = stringCantidadArchivosAdmBandejaCC;
    }

    public String getStringCantidadMesaDeEntradaBandejaEntrada() {
        return stringCantidadMesaDeEntradaBandejaEntrada;
    }

    public void setStringCantidadMesaDeEntradaBandejaEntrada(String stringCantidadMesaDeEntradaBandejaEntrada) {
        this.stringCantidadMesaDeEntradaBandejaEntrada = stringCantidadMesaDeEntradaBandejaEntrada;
    }

    public String getCambioContrasenaFirma1() {
        return cambioContrasenaFirma1;
    }

    public void setCambioContrasenaFirma1(String cambioContrasenaFirma1) {
        this.cambioContrasenaFirma1 = cambioContrasenaFirma1;
    }

    public String getCambioContrasenaFirma2() {
        return cambioContrasenaFirma2;
    }

    public void setCambioContrasenaFirma2(String cambioContrasenaFirma2) {
        this.cambioContrasenaFirma2 = cambioContrasenaFirma2;
    }

    public Integer getCantidadArchivosAdmBandejaEntrada() {
        return cantidadArchivosAdmBandejaEntrada;
    }

    public void setCantidadArchivosAdmBandejaEntrada(Integer cantidadArchivosAdmBandejaEntrada) {
        this.cantidadArchivosAdmBandejaEntrada = cantidadArchivosAdmBandejaEntrada;
    }

    public String getStringCantidadArchivosAdmBandejaEntrada() {
        return stringCantidadArchivosAdmBandejaEntrada;
    }

    public void setStringCantidadArchivosAdmBandejaEntrada(String stringCantidadArchivosAdmBandejaEntrada) {
        this.stringCantidadArchivosAdmBandejaEntrada = stringCantidadArchivosAdmBandejaEntrada;
    }

    public String getStringCantidadArchivosAdmEnProyecto() {
        return stringCantidadArchivosAdmEnProyecto;
    }

    public void setStringCantidadArchivosAdmEnProyecto(String stringCantidadArchivosAdmEnProyecto) {
        this.stringCantidadArchivosAdmEnProyecto = stringCantidadArchivosAdmEnProyecto;
    }

    public String getStringCantidadArchivosAdmEnRevision() {
        return stringCantidadArchivosAdmEnRevision;
    }

    public void setStringCantidadArchivosAdmEnRevision(String stringCantidadArchivosAdmEnRevision) {
        this.stringCantidadArchivosAdmEnRevision = stringCantidadArchivosAdmEnRevision;
    }

    public String getStringCantidadArchivosAdmFinalizado() {
        return stringCantidadArchivosAdmFinalizado;
    }

    public void setStringCantidadArchivosAdmFinalizado(String stringCantidadArchivosAdmFinalizado) {
        this.stringCantidadArchivosAdmFinalizado = stringCantidadArchivosAdmFinalizado;
    }

    public String getStringCantidadArchivosAdmParaRevision() {
        return stringCantidadArchivosAdmParaRevision;
    }

    public void setStringCantidadArchivosAdmParaRevision(String stringCantidadArchivosAdmParaRevision) {
        this.stringCantidadArchivosAdmParaRevision = stringCantidadArchivosAdmParaRevision;
    }

    public Integer getCantidadArchivosAdmParaRevision() {
        return cantidadArchivosAdmParaRevision;
    }

    public void setCantidadArchivosAdmParaRevision(Integer cantidadArchivosAdmParaRevision) {
        this.cantidadArchivosAdmParaRevision = cantidadArchivosAdmParaRevision;
    }

    public Integer getCantidadArchivosAdmEnRevision() {
        return cantidadArchivosAdmEnRevision;
    }

    public void setCantidadArchivosAdmEnRevision(Integer cantidadArchivosAdmEnRevision) {
        this.cantidadArchivosAdmEnRevision = cantidadArchivosAdmEnRevision;
    }

    public Integer getCantidadArchivosAdmFinalizado() {
        return cantidadArchivosAdmFinalizado;
    }

    public void setCantidadArchivosAdmFinalizado(Integer cantidadArchivosAdmFinalizado) {
        this.cantidadArchivosAdmFinalizado = cantidadArchivosAdmFinalizado;
    }

    public Integer getCantidadArchivosAdmEnProyecto() {
        return cantidadArchivosAdmEnProyecto;
    }

    public void setCantidadArchivosAdmEnProyecto(Integer cantidadArchivosAdmEnProyecto) {
        this.cantidadArchivosAdmEnProyecto = cantidadArchivosAdmEnProyecto;
    }

    public List<Usuarios> getListaUsuariosAsociar() {
        return listaUsuariosAsociar;
    }

    public void setListaUsuariosAsociar(List<Usuarios> listaUsuariosAsociar) {
        this.listaUsuariosAsociar = listaUsuariosAsociar;
    }

    public Usuarios getUsuarioAsociarSelected() {
        return usuarioAsociarSelected;
    }

    public void setUsuarioAsociarSelected(Usuarios usuarioAsociarSelected) {
        this.usuarioAsociarSelected = usuarioAsociarSelected;
    }

    public Usuarios getUsuarioAsociar() {
        return usuarioAsociar;
    }

    public void setUsuarioAsociar(Usuarios usuarioAsociar) {
        this.usuarioAsociar = usuarioAsociar;
    }

    public List<Usuarios> getListaUsuariosAAsociar() {
        return listaUsuariosAAsociar;
    }

    public void setListaUsuariosAAsociar(List<Usuarios> listaUsuariosAAsociar) {
        this.listaUsuariosAAsociar = listaUsuariosAAsociar;
    }

    public Usuarios getUsuarioElegido() {
        return usuarioElegido;
    }

    public void setUsuarioElegido(Usuarios usuarioElegido) {
        this.usuarioElegido = usuarioElegido;
    }

    public Perfiles getPerfilElegido() {
        return perfilElegido;
    }

    public void setPerfilElegido(Perfiles perfilElegido) {
        this.perfilElegido = perfilElegido;
    }

    public List<Usuarios> getListaUsuariosElegir() {
        return listaUsuariosElegir;
    }

    public void setListaUsuariosElegir(List<Usuarios> listaUsuariosElegir) {
        this.listaUsuariosElegir = listaUsuariosElegir;
    }

    public List<Perfiles> getListaPerfilesElegir() {
        return listaPerfilesElegir;
    }

    public void setListaPerfilesElegir(List<Perfiles> listaPerfilesElegir) {
        this.listaPerfilesElegir = listaPerfilesElegir;
    }

    public String getImagenInicio() throws IOException {
        return imagenInicio;
    }

    public String getImagenLogo() {

        return imagenLogo;
    }

    public List<Personas> getListaPersonas() {
        return listaPersonas;
    }

    public void setListaPersonas(List<Personas> listaPersonas) {
        this.listaPersonas = listaPersonas;
    }

    @PostConstruct
    public void init() {
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        hash = params.get("hasg");

        this.ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

        politicas = Utils.politicasContrasena;

        // usuario = null;
        // contrasena = null;    
        String hash = params.get("hash");
        if (hash == null) {
            usuario = params.get("usuario");
            contrasena = params.get("contrasena");
            contrasena2 = params.get("contrasena");
            pantalla = params.get("pantalla");
        } else {
            byte[] decoded = Base64.getDecoder().decode(hash);

            String hashFinal="";
            try {
                SimpleDateFormat format = new SimpleDateFormat("ddMMyyyyhh");
                Date fecha = ejbFacade.getSystemDate();
               // hashFinal = Utils.decryptMsg(decoded, Utils.generateKey(format.format(fecha) + "jemjem"));
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/validacion/HashNoValido.xhtml");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return;
            }
            String[] array = hashFinal.split(",");
            if (array.length > 2) {
                usuario = array[0];
                contrasena = array[1];
                contrasena2 = array[1];
                pantalla = array[2];
            }
        }

        sesionLogin = params.get("id");

        usuLogin = (Usuarios) session.getAttribute("Usuarios");
        usuarioBkp = (Usuarios) session.getAttribute("Backup");

        try {
            par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID).getSingleResult();

            File file = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/" + par.getRutaRecursos());
            boolean bool = file.mkdir();
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            url = request.getRequestURL().toString();
            String uri = request.getRequestURI();
            int pos = url.lastIndexOf(uri);
            url = url.substring(0, pos);
            String[] array = uri.split("/");
            endpoint = array[1];

            // imagenFondo = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + par.getRutaRecursos() + "/" + "imagen_fondo.jpg";
            // imagenInicio = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + par.getRutaRecursos() + "/" + "imagen_inicio.jpg";
            // imagenLogo = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + par.getRutaRecursos() + "/" + "imagen_logo.jpg";
            imagenInicio = url + "/" + par.getRutaRecursos() + "/" + "imagen_inicio.jpg";
            imagenLogo = url + "/" + par.getRutaRecursos() + "/" + "imagen_logo.jpg";

            Object appObj = session.getAttribute("esApp");

            boolean esApp = (appObj == null) ? false : (boolean) appObj;

            // System.out.println("Init: Usuario: " + (usuario == null ? "" : usuario) + ", contrasena: " + (contrasena == null ? "" : contrasena));
            // System.out.println("login: " + (uri.lastIndexOf("login.xhtml") > -1) + ", esApp: " + esApp);
            if (uri.lastIndexOf("login.xhtml") > -1 && esApp) {

                try {
                    // System.out.println("Redireccionando");
                    session.invalidate();
                    session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/login.xhtml?usuario=" + usuario + "&contrasena=" + contrasena + "&pantalla=" + pantalla);

                    return;
                } catch (IOException ex) {
                    Logger.getLogger(PersonasController.class.getName()).log(Level.SEVERE, null, ex);
                    return;
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            imagenInicio = "";
            // imagenFondo = "";
            imagenLogo = "";
        }

        Usuarios usu = (Usuarios) session.getAttribute("Usuarios");
        Usuarios bkp = (Usuarios) session.getAttribute("Backup");
        Perfiles perfil = (Perfiles) session.getAttribute("Perfil");

        if (usu != null) {

            verifAviso(usu);

            if ("FE".equals(usu.getSexo().getCodigo())) {
                nombreUsu = "Bienvenida ";
            } else {
                nombreUsu = "Bienvenido ";
            }
            nombreUsu += usu.getNombresApellidos() + " - (" + usu.getDepartamento().getNombre() + (perfil==null?"":" - " + perfil.getDescripcion()) + ")";

            if (bkp != null) {
                nombreUsu += " como backup de " + bkp.getNombresApellidos();
            }

            List<EstadosTransferenciaDocumentoAdministrativo> estados = null;
            if (filtroURL.verifPermiso("/pages/archivosAdministrativo/index.xhtml", "PROYECTO")) {
                estados = ejbFacade.getEntityManager().createNamedQuery("EstadosTransferenciaDocumentoAdministrativo.findByCodigo", EstadosTransferenciaDocumentoAdministrativo.class).setParameter("codigo", Constantes.ESTADO_ARCHIVO_ADM_PROYECTO).getResultList();
                if (!estados.isEmpty()) {
                    cantidadArchivosAdmEnProyecto = documentosAdministrativosController.buscarPorFechaAltaBorradores(estados.get(0), usuLogin, null).size();
                    if (cantidadArchivosAdmEnProyecto == 0) {
                        cantidadArchivosAdmEnProyecto = null;
                    }

                    stringCantidadArchivosAdmEnProyecto = "En elaboración " + ((cantidadArchivosAdmEnProyecto == null) ? "" : String.valueOf(cantidadArchivosAdmEnProyecto));
                }
            }

            if (filtroURL.verifPermiso("/pages/archivosAdministrativo/index.xhtml", "REVISION")) {
                estados = ejbFacade.getEntityManager().createNamedQuery("EstadosTransferenciaDocumentoAdministrativo.findByCodigo", EstadosTransferenciaDocumentoAdministrativo.class).setParameter("codigo", Constantes.ESTADO_ARCHIVO_ADM_REVISION).getResultList();
                if (!estados.isEmpty()) {
                    cantidadArchivosAdmEnRevision = documentosAdministrativosController.buscarPorFechaAltaBorradores(estados.get(0), usuLogin, null).size();
                    if (cantidadArchivosAdmEnRevision == 0) {
                        cantidadArchivosAdmEnRevision = null;
                    }

                    stringCantidadArchivosAdmEnRevision = "Remitidos p/ revisión " + ((cantidadArchivosAdmEnRevision == null) ? "" : String.valueOf(cantidadArchivosAdmEnRevision));

                }
            }

            if (filtroURL.verifPermiso("/pages/archivosAdministrativo/index.xhtml", "PARA_REVISION")) {
                estados = ejbFacade.getEntityManager().createNamedQuery("EstadosTransferenciaDocumentoAdministrativo.findByCodigo", EstadosTransferenciaDocumentoAdministrativo.class).setParameter("codigo", Constantes.ESTADO_ARCHIVO_ADM_REVISION).getResultList();
                if (!estados.isEmpty()) {
                    cantidadArchivosAdmParaRevision = documentosAdministrativosController.buscarPorFechaAltaBorradores(estados.get(0), null, usuLogin).size();
                    if (cantidadArchivosAdmParaRevision == 0) {
                        cantidadArchivosAdmParaRevision = null;
                    }

                    stringCantidadArchivosAdmParaRevision = "Recibidos p/ revisión " + ((cantidadArchivosAdmParaRevision == null) ? "" : String.valueOf(cantidadArchivosAdmParaRevision));

                }
            }

            if (filtroURL.verifPermiso("/pages/documentosAdministrativos/index.xhtml")) {
                // estados = ejbFacade.getEntityManager().createNamedQuery("EstadosTransferenciaDocumentoAdministrativo.findByCodigo", EstadosTransferenciaDocumentoAdministrativo.class).setParameter("codigo", Constantes.ESTADO_ARCHIVO_ADM_REVISION).getResultList();
                // if (!estados.isEmpty()) {
                cantidadArchivosAdmBandejaEntrada = documentosAdministrativosController.buscarBandejaEntrada(true).size();
                if (cantidadArchivosAdmBandejaEntrada == 0) {
                    cantidadArchivosAdmBandejaEntrada = null;
                }

                stringCantidadArchivosAdmBandejaEntrada = "Bandeja entrada " + ((cantidadArchivosAdmBandejaEntrada == null) ? "" : String.valueOf(cantidadArchivosAdmBandejaEntrada));

                // }
            }

            cantidadArchivosAdmBandejaCC = documentosAdministrativosController.buscarCantidadBandejaCC();
            if (cantidadArchivosAdmBandejaCC == 0) {
                cantidadArchivosAdmBandejaCC = null;
            }

            stringCantidadArchivosAdmBandejaCC = "Bandeja con copia " + ((cantidadArchivosAdmBandejaCC == null) ? "" : String.valueOf(cantidadArchivosAdmBandejaCC));

            if (filtroURL.verifPermiso("/pages/verDocumentosAdministrativosMesa/index.xhtml")) {
                // cantidadMesaDeEntradaBandejaEntrada = verDocumentosAdministrativosMesaController.buscarPorFechaAlta().size();
                cantidadMesaDeEntradaBandejaEntrada = 0;
                if (cantidadMesaDeEntradaBandejaEntrada == 0) {
                    cantidadMesaDeEntradaBandejaEntrada = null;
                }

                stringCantidadMesaDeEntradaBandejaEntrada = "Bandeja entrada " + ((cantidadMesaDeEntradaBandejaEntrada == null) ? "" : String.valueOf(cantidadMesaDeEntradaBandejaEntrada));

            }
        }
        autoGuardado = false;

        List<DocumentosAdministrativosAutoguardados> lista = ejbFacade.getEntityManager().createNamedQuery("DocumentosAdministrativosAutoguardados.findByUsuarioAlta", DocumentosAdministrativosAutoguardados.class).setParameter("usuarioAlta", usu).getResultList();

        if (!lista.isEmpty()) {
            autoGuardadoCreate = lista.get(0).getDocumentoAdministrativoOriginal() != null;
            autoGuardado = true;
            /*
            PrimeFaces.current().ajax().update("ViewAutoguardadosForm");

            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('ViewAutoguardadosDialog').show();");
             */
        }

        menu = 1;
    }

    private void verifAviso(Usuarios usu) {
        Date fechaHora = ejbFacade.getSystemDate();
        List<Avisos> av = ejbFacade.getEntityManager().createNamedQuery("Avisos.findByRangoFecha", Avisos.class).setParameter("fechaHoraDesde", fechaHora).setParameter("fechaHoraHasta", fechaHora).getResultList();

        if (!av.isEmpty()) {
            
            List<AvisosPorUsuarios> avUsuList = ejbFacade.getEntityManager().createNamedQuery("AvisosPorUsuarios.findByAvisoAndUsuario", AvisosPorUsuarios.class).setParameter("usuario", usu.getId()).setParameter("aviso", av.get(0).getId()).getResultList();

            boolean ver = false;
            if(avUsuList.isEmpty()){
                AvisosPorUsuarios avUsu = new AvisosPorUsuarios();
                AvisosPorUsuariosPK pk = new AvisosPorUsuariosPK();
                pk.setAviso(av.get(0).getId());
                pk.setUsuario(usu.getId());
                avUsu.setAvisosPorUsuariosPK(pk);
                avUsu.setAvisos(av.get(0));
                avUsu.setUsuarios(usu);
                avUsu.setFechaHoraVisto(fechaHora);
                avUsu.setProgramado(false);
                avUsu.setVisto(true);
                
                avisosPorUsuariosController.setSelected(avUsu);
                avisosPorUsuariosController.saveNew2(null);
                ver = true;
            }else{
                AvisosPorUsuarios avUsu = avUsuList.get(0);
                
                ver = !avUsu.isVisto();
                
                if(ver){
                    avUsu.setFechaHoraVisto(fechaHora);
                    avUsu.setVisto(true);
                    avisosPorUsuariosController.setSelected(avUsu);
                    avisosPorUsuariosController.save(null);
                }
            }
            
            if(ver){
                docImprimir = av.get(0);
                textoAviso = docImprimir.getTexto();
                videoAviso = docImprimir.getVideo();
                urlAviso = docImprimir.getUrl();
                urlTexto = docImprimir.getUrlTexto();

                PrimeFaces.current().executeScript("PF('AvisosCreateDialog').toggleMaximize();");
                PrimeFaces.current().executeScript("PF('AvisosCreateDialog').show();");
                PrimeFaces.current().ajax().update("AvisosCreateForm");
            }
        }
    }

    public boolean renderedTextoAviso() {
        return textoAviso != null ? (!textoAviso.equals("")) : false;
    }

    public boolean renderedDialogoAviso() {
        return renderedTextoAviso() || renderedPdfAviso() || renderedImagenAviso() || renderedUrlAviso() || renderedVideoAviso();
    }

    public boolean renderedPdfAviso() {
        return docImprimir != null?(docImprimir.getPdf()!=null?!docImprimir.getPdf().equals(""):false):false;
    }

    public boolean renderedImagenAviso() {
        return docImprimir != null?(docImprimir.getImagen()!=null?!docImprimir.getImagen().equals(""):false):false;
    }

    public boolean renderedVideoAviso() {
        return docImprimir != null?(docImprimir.getVideo()!=null?!docImprimir.getVideo().equals(""):false):false;
    }

    public boolean renderedUrlAviso() {
        return docImprimir != null?(docImprimir.getUrl()!=null?!docImprimir.getUrl().equals(""):false):false;
    }

    public void prepareCerrarDialogoAviso() {
        if (docImprimir != null) {
            if(docImprimir.getPdf() != null){
                File f = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/tmp/" + nombre);
                f.delete();
            }
            
            if(docImprimir.getImagen() != null){
                File f = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/tmp/" + nombreImagen);
                f.delete();
            }

            docImprimir = null;

        }
    }

    public String getAviso() {

        nombre = "";
        try {
            if (docImprimir != null) {
                if (docImprimir.getPdf() != null) {

                    byte[] fileByte = null;
                    Date fecha = ejbFacade.getSystemDate();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constantes.FORMATO_FECHA);

                    if (docImprimir.getPdf() != null) {

                        try {
                            fileByte = Files.readAllBytes(new File(par.getRutaAvisos() + "/" + docImprimir.getPdf()).toPath());
                        } catch (IOException ex) {
                            // JsfUtil.addErrorMessage("No tiene documento adjunto");
                        }

                        if (fileByte != null) {

                            String partes[] = docImprimir.getPdf().split("[.]");
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
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        // return par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/tmp/" + nombre;
        return url + "/tmp/" + nombre;

    }

    public String getImagenAviso() {

        nombreImagen = "";
        try {
            if (docImprimir != null) {

                byte[] fileByte = null;
                Date fecha = ejbFacade.getSystemDate();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constantes.FORMATO_FECHA);

                if (docImprimir.getImagen() != null) {

                    try {
                        fileByte = Files.readAllBytes(new File(par.getRutaAvisos() + "/" + docImprimir.getImagen()).toPath());
                    } catch (IOException ex) {
                        // JsfUtil.addErrorMessage("No tiene documento adjunto");
                    }

                    if (fileByte != null) {

                        String partes[] = docImprimir.getImagen().split("[.]");
                        String ext = "pdf";

                        if (partes.length > 1) {
                            ext = partes[partes.length - 1];
                        }

                        nombreImagen = session.getId() + "_" + simpleDateFormat.format(fecha) + "." + ext;
                        FileOutputStream outputStream = new FileOutputStream(Constantes.RUTA_ARCHIVOS_TEMP + "/tmp/" + nombreImagen);
                        outputStream.write(fileByte);

                        outputStream.close();

                        // content = new DefaultStreamedContent(new ByteArrayInputStream(fileByte), "application/pdf");
                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        // return par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/tmp/" + nombre;
        return url + "/tmp/" + nombreImagen;

    }

    public boolean renderedAutoguardado() {
        return autoGuardado;
    }

    public void redireccionarAutoguardado() {

        if (autoGuardadoCreate) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/archivosAdministrativo/index.xhtml");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/pages/documentosAdministrativos/index.xhtml");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    public UsuariosController() {
        // Inform the Abstract parent controller of the concrete Usuarios Entity
        super(Usuarios.class);
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void menu(Integer menu) {
        this.menu = menu;
    }

    public boolean deshabilitarMenues(Integer menu) {

        return !(this.menu.equals(menu));
    }

    public boolean deshabilitarMenues(String url, String permiso, Integer menu) {
        return !(filtroURL.verifPermiso(url, permiso) && this.menu.equals(menu));
    }

    public boolean deshabilitarMenues(String url, Integer menu) {
        return !(filtroURL.verifPermiso(url) && this.menu.equals(menu));
    }

    public Collection<Usuarios> getUsuariosDpto() {

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

        return ejbFacade.getEntityManager().createNamedQuery("Usuarios.findByDepartamento", Usuarios.class).setParameter("departamento", usu.getDepartamento()).getResultList();

        /*
        
        List<Usuarios> lista = ejbFacade.getEntityManager().createNamedQuery("Usuarios.findByDepartamento", Usuarios.class).setParameter("departamento", usu.getDepartamento()).getResultList();
        
        List<Usuarios> lista2 = new ArrayList<>();
        for(int i = 0; i < lista.size(); i++){
            Usuarios uno = lista.get(i);
            uno.setContrasena("");
            
            lista2.add(uno);
        }

        return lista2;
         */
    }

    public Collection<Usuarios> getFuncionariosSecretaria() {
        Departamentos dpto = departamentoController.prepareCreate(null);
        dpto.setId(5); // Secretaria
        return ejbFacade.getEntityManager().createNamedQuery("Usuarios.findByDepartamento", Usuarios.class).setParameter("departamento", dpto).getResultList();
    }

    public void setFuncionariosSecretaria(Collection<Usuarios> funcionariosSecretaria) {
        this.funcionariosSecretaria = funcionariosSecretaria;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getNombreUsu() {
        return nombreUsu;
    }

    public void setNombreUsu(String nombreUsu) {
        this.nombreUsu = nombreUsu;
    }

    public String getCambioContrasena1() {
        return cambioContrasena1;
    }

    public void setCambioContrasena1(String cambioContrasena1) {
        this.cambioContrasena1 = cambioContrasena1;
    }

    public String getCambioContrasena2() {
        return cambioContrasena2;
    }

    public void setCambioContrasena2(String cambioContrasena2) {
        this.cambioContrasena2 = cambioContrasena2;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String obtenerHome() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        return (String) session.getAttribute("Home");
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        departamentoController.setSelected(null);
        empresaController.setSelected(null);
        estadoController.setSelected(null);
        pantallaPrincipalController.setSelected(null);
        sexoController.setSelected(null);
    }

    /**
     * Sets the "items" attribute with a collection of Departamentos entities
     * that are retrieved from Usuarios?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for Departamentos page
     */
    public String navigateDepartamentosCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Departamentos_items", this.getSelected().getDepartamentosCollection());
        }
        return "/pages/departamentos/index";
    }

    /**
     * Sets the "items" attribute with a collection of Departamentos entities
     * that are retrieved from Usuarios?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for Departamentos page
     */
    public String navigateDepartamentosCollection1() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Departamentos_items", this.getSelected().getDepartamentosCollection1());
        }
        return "/pages/departamentos/index";
    }

    /**
     * Sets the "items" attribute with a collection of Roles entities that are
     * retrieved from Usuarios?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Roles page
     */
    public String navigateRolesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Roles_items", this.getSelected().getRolesCollection());
        }
        return "/pages/roles/index";
    }

    /**
     * Sets the "items" attribute with a collection of Roles entities that are
     * retrieved from Usuarios?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Roles page
     */
    public String navigateRolesCollection1() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Roles_items", this.getSelected().getRolesCollection1());
        }
        return "/pages/roles/index";
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
     * Sets the "selected" attribute of the EstadosUsuario controller in order
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
     * Sets the "selected" attribute of the Pantallas controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void preparePantallaPrincipal(ActionEvent event) {
        if (this.getSelected() != null && pantallaPrincipalController.getSelected() == null) {
            pantallaPrincipalController.setSelected(this.getSelected().getPantallaPrincipal());
        }
    }

    /**
     * Sets the "selected" attribute of the Sexos controller in order to display
     * its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareSexo(ActionEvent event) {
        if (this.getSelected() != null && sexoController.getSelected() == null) {
            sexoController.setSelected(this.getSelected().getSexo());
        }
    }

    /**
     * Sets the "items" attribute with a collection of Usuarios entities that
     * are retrieved from Usuarios?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Usuarios page
     */
    public String navigateUsuariosCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Usuarios_items", this.getSelected().getUsuariosCollection());
        }
        return "/pages/usuarios/index";
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
     * Sets the "items" attribute with a collection of Usuarios entities that
     * are retrieved from Usuarios?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Usuarios page
     */
    public String navigateUsuariosCollection1() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Usuarios_items", this.getSelected().getUsuariosCollection1());
        }
        return "/pages/usuarios/index";
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
     * Sets the "items" attribute with a collection of RolesPorUsuarios entities
     * that are retrieved from Usuarios?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for RolesPorUsuarios page
     */
    public String navigateRolesPorUsuariosCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("RolesPorUsuarios_items", this.getSelected().getRolesPorUsuariosCollection());
        }
        return "/pages/rolesPorUsuarios/index";
    }

    /**
     * Sets the "items" attribute with a collection of RolesPorUsuarios entities
     * that are retrieved from Usuarios?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for RolesPorUsuarios page
     */
    public String navigateRolesPorUsuariosCollection1() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("RolesPorUsuarios_items", this.getSelected().getRolesPorUsuariosCollection1());
        }
        return "/pages/rolesPorUsuarios/index";
    }

    /**
     * Sets the "items" attribute with a collection of RolesPorUsuarios entities
     * that are retrieved from Usuarios?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for RolesPorUsuarios page
     */
    public String navigateRolesPorUsuariosCollection2() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("RolesPorUsuarios_items", this.getSelected().getRolesPorUsuariosCollection2());
        }
        return "/pages/rolesPorUsuarios/index";
    }

    /**
     * Sets the "items" attribute with a collection of Proveedores entities that
     * are retrieved from Usuarios?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Proveedores page
     */
    public String navigateProveedoresCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Proveedores_items", this.getSelected().getProveedoresCollection());
        }
        return "/pages/proveedores/index";
    }

    /**
     * Sets the "items" attribute with a collection of Proveedores entities that
     * are retrieved from Usuarios?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Proveedores page
     */
    public String navigateProveedoresCollection1() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Proveedores_items", this.getSelected().getProveedoresCollection1());
        }
        return "/pages/proveedores/index";
    }

    /**
     * Sets the "items" attribute with a collection of
     * MovimientosReparacionBienes entities that are retrieved from
     * Usuarios?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for MovimientosReparacionBienes page
     */
    public String navigateMovimientosReparacionBienesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("MovimientosReparacionBienes_items", this.getSelected().getMovimientosReparacionBienesCollection());
        }
        return "/pages/movimientosReparacionBienes/index";
    }

    /**
     * Sets the "items" attribute with a collection of
     * MovimientosReparacionBienes entities that are retrieved from
     * Usuarios?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for MovimientosReparacionBienes page
     */
    public String navigateMovimientosReparacionBienesCollection1() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("MovimientosReparacionBienes_items", this.getSelected().getMovimientosReparacionBienesCollection1());
        }
        return "/pages/movimientosReparacionBienes/index";
    }

    /**
     * Sets the "items" attribute with a collection of Bienes entities that are
     * retrieved from Usuarios?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Bienes page
     */
    public String navigateBienesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Bienes_items", this.getSelected().getBienesCollection());
        }
        return "/pages/bienes/index";
    }

    /**
     * Sets the "items" attribute with a collection of Bienes entities that are
     * retrieved from Usuarios?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Bienes page
     */
    public String navigateBienesCollection1() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Bienes_items", this.getSelected().getBienesCollection1());
        }
        return "/pages/bienes/index";
    }

    /**
     * Sets the "items" attribute with a collection of Bienes entities that are
     * retrieved from Usuarios?cap_first and returns the navigation outcome.
     *
     * @return navigation outcome for Bienes page
     */
    public String navigateBienesCollection2() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Bienes_items", this.getSelected().getBienesCollection2());
        }
        return "/pages/bienes/index";
    }

    /**
     * Sets the "items" attribute with a collection of MovimientosBienes
     * entities that are retrieved from Usuarios?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for MovimientosBienes page
     */
    public String navigateMovimientosBienesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("MovimientosBienes_items", this.getSelected().getMovimientosBienesCollection());
        }
        return "/pages/movimientosBienes/index";
    }

    /**
     * Sets the "items" attribute with a collection of MovimientosBienes
     * entities that are retrieved from Usuarios?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for MovimientosBienes page
     */
    public String navigateMovimientosBienesCollection1() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("MovimientosBienes_items", this.getSelected().getMovimientosBienesCollection1());
        }
        return "/pages/movimientosBienes/index";
    }

    /**
     * Sets the "items" attribute with a collection of MovimientosBienes
     * entities that are retrieved from Usuarios?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for MovimientosBienes page
     */
    public String navigateMovimientosBienesCollection2() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("MovimientosBienes_items", this.getSelected().getMovimientosBienesCollection2());
        }
        return "/pages/movimientosBienes/index";
    }

    /**
     * Sets the "items" attribute with a collection of MovimientosBienes
     * entities that are retrieved from Usuarios?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for MovimientosBienes page
     */
    public String navigateMovimientosBienesCollection3() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("MovimientosBienes_items", this.getSelected().getMovimientosBienesCollection3());
        }
        return "/pages/movimientosBienes/index";
    }

    public void onload() {

        // System.out.println("Onload: Usuario: " + (usuario == null ? "" : usuario) + ", contrasena2: " + (contrasena2 == null ? "" : contrasena2));
        if (usuario != null && contrasena2 != null) {

            String usuarioActual = usuario;
            String contrasenaActual = contrasena2;

            if (!"".equals(usuarioActual) && !"".equals(contrasenaActual)) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("esApp", true);
                loginControl(usuarioActual, contrasenaActual);
            }
        }

        if (sesionLogin != null) {
            Date fecha = ejbFacade.getSystemDate();
            List<SesionesLogin> ses = ejbFacade.getEntityManager().createNamedQuery("SesionesLogin.findByHashFechaHoraCaducidadEstado", SesionesLogin.class).setParameter("hash", sesionLogin).setParameter("fechaHoraCaducidad", fecha).setParameter("estado", "AC").getResultList();
            sesionLogin = null;
            if (!ses.isEmpty()) {

                ses.get(0).setEstado(new Estados("IN"));

                sesionesLoginController.setSelected(ses.get(0));
                sesionesLoginController.save(null);
                if (ses.get(0).getUsuario() != null) {
                    String resp = loginControl(ses.get(0).getUsuario().getUsuario(), ses.get(0).getUsuario().getContrasena(), true);

                    if (resp != null) {
                        if (!"".equals(resp)) {
                            try {
                                FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/" + resp);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }

                if (usuarioOriginal == null) {
                    if (ses.get(0).getPersona() != null) {
                        loginControl(ses.get(0).getPersona().getUsuario(), ses.get(0).getPersona().getContrasena(), false);
                    } else {
                        JsfUtil.addErrorMessage("Sesion no tiene ni usuario ni persona");
                    }
                }

            } else {
                JsfUtil.addErrorMessage("No se encontro sesión o sesión caducada");
            }
        }
    }

    public Usuarios prepareCreate() {

        Usuarios usu = super.prepareCreate(null);

        cambioContrasenaFirma1 = "";
        cambioContrasenaFirma2 = "";

        listaPersonas = ejbFacade.getEntityManager().createNamedQuery("Personas.findAll", Personas.class).getResultList();
        listaDepartamentos = ejbFacade.getEntityManager().createNamedQuery("Departamentos.findByEstado", Departamentos.class).setParameter("estado", new Estados("AC")).getResultList();

        return usu;
    }

    public void prepareEdit() {
        cambioContrasenaFirma1 = "";
        cambioContrasenaFirma2 = "";

        listaPersonas = ejbFacade.getEntityManager().createNamedQuery("Personas.findAll", Personas.class).getResultList();
        listaDepartamentos = ejbFacade.getEntityManager().createNamedQuery("Departamentos.findByEstado", Departamentos.class).setParameter("estado", new Estados("AC")).getResultList();

        if (getSelected() != null) {
            if (!listaDepartamentos.contains(getSelected().getDepartamento())) {
                listaDepartamentos.add(getSelected().getDepartamento());
            }
        }
    }
    
    private boolean deshabilitarCamposAdm(){
        return filtroURL.verifPermiso("verTodosDocsAdm");
    }

    public boolean renderedUsuarioAsociar() {
        boolean resp = (filtroURL.verifPermiso(Constantes.PERMISO_USUARIOS_ASOCIAR) && usuarioBkp == null) && !deshabilitarCamposAdm();
        return resp;
    }

    public void prepareUsuarioAsociar() {
        if (usuLogin != null) {

            listaUsuariosAAsociar = ejbFacade.getEntityManager().createNamedQuery("Usuarios.findByEstado", Usuarios.class).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();

            List<UsuariosAsociados> lista = ejbFacade.getEntityManager().createNamedQuery("UsuariosAsociados.findByUsuario", UsuariosAsociados.class).setParameter("usuario", usuLogin.getId()).getResultList();
            listaUsuariosAsociar = new ArrayList<>();
            for (UsuariosAsociados per : lista) {
                listaUsuariosAsociar.add(per.getUsuarioAsociado());
                listaUsuariosAAsociar.remove(per.getUsuarioAsociado());
            }
        }
    }

    public void borrarUsuarioAsociar(Usuarios personaActual) {

        if (listaUsuariosAsociar != null) {

            if (personaActual != null) {
                listaUsuariosAsociar.remove(personaActual);
            }

        }
    }

    public void agregarUsuarioAsociar() {

        if (usuarioAsociar != null) {

            if (listaUsuariosAsociar == null) {
                listaUsuariosAsociar = new ArrayList<>();
            }

            boolean encontro = false;
            Usuarios usuActual = null;
            for (Usuarios usu : listaUsuariosAsociar) {
                if (usu.equals(usuarioAsociar)) {
                    usuActual = usu;
                    encontro = true;
                }
            }

            if (!encontro) {
                listaUsuariosAsociar.add(usuarioAsociar);
            }
        }
    }

    public void saveUsuariosAsociar() {
        if (usuLogin != null) {
            List<UsuariosAsociados> listaAsociarActual = ejbFacade.getEntityManager().createNamedQuery("UsuariosAsociados.findByUsuario", UsuariosAsociados.class).setParameter("usuario", usuLogin.getId()).getResultList();
            saveUsuariosAsociar(usuLogin, listaUsuariosAsociar, listaAsociarActual);
        }
    }

    public void saveUsuariosAsociar(Usuarios persona, List<Usuarios> listaUsuariosAsociar, List<UsuariosAsociados> listaAsociarActual) {

        Date fecha = ejbFacade.getSystemDate();
        Usuarios per2 = null;
        UsuariosAsociados perDocActual = null;
        boolean encontro = false;

        for (Usuarios per : listaUsuariosAsociar) {
            encontro = false;
            perDocActual = null;
            for (int i = 0; i < listaAsociarActual.size(); i++) {
                per2 = listaAsociarActual.get(i).getUsuarioAsociado();
                if (per2.equals(per)) {
                    encontro = true;
                    perDocActual = listaAsociarActual.get(i);
                    break;
                }
            }
            if (!encontro) {
                UsuariosAsociadosPK pk = new UsuariosAsociadosPK(persona.getId(), per.getId());
                UsuariosAsociados perDoc = new UsuariosAsociados(pk);
                perDoc.setUsuario(persona);
                perDoc.setUsuarioAsociado(per);

                usuariosAsociadosController.setSelected(perDoc);
                usuariosAsociadosController.saveNew(null);
            }
        }

        for (int i = 0; i < listaAsociarActual.size(); i++) {
            per2 = listaAsociarActual.get(i).getUsuarioAsociado();
            encontro = false;
            for (Usuarios per : listaUsuariosAsociar) {
                if (per2.equals(per)) {
                    encontro = true;
                    break;
                }
            }
            if (!encontro) {
                usuariosAsociadosController.setSelected(listaAsociarActual.get(i));
                usuariosAsociadosController.delete(null);
            }
        }
    }
    
    public String redirigir(){
        
        listaPerfilesElegir = ejbFacade.getEntityManager().createNamedQuery("Perfiles.findByRoles", Perfiles.class).setParameter("usuario", usuarioElegido.getId()).getResultList();
        /* if (usuAs.size() > 1) {
            return null;
        }
         */

        perfilElegido = null;

        if (listaPerfilesElegir.isEmpty()) {
            return redirigir2(usuarioElegido);
        } else {
            if(listaPerfilesElegir.size() > 1){
                PrimeFaces.current().ajax().update("PerfilesForm");
                PrimeFaces current = PrimeFaces.current();
                current.executeScript("PF('PerfilesDialog').show();");
                return "";
            }else{
                perfilElegido = listaPerfilesElegir.get(0);
                return redirigir2(usuarioElegido);
            }
        }
    }
    
    public String redirigir2() {
        return redirigir2(usuarioElegido);
    }

    public String redirigir2(Usuarios usu) {

        List<VPermisosUsuarios> p = null;
        if(perfilElegido == null){
            p = ejbFacade.getEntityManager().createNamedQuery("VPermisosUsuarios.findByUsuaId", VPermisosUsuarios.class).setParameter("usuaId", usu.getId()).getResultList();
        }else{
            p = ejbFacade.getEntityManager().createNamedQuery("VPermisosUsuarios.findByUsuaIdAndPerfId", VPermisosUsuarios.class).setParameter("usuaId", usu.getId()).setParameter("perfId", perfilElegido.getId()).getResultList();
        }
        
        if (!usu.equals(usuarioOriginal)) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Backup", usu);
        }

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("VPermisosUsuarios", p);

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Usuarios", usuarioOriginal);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Perfil", perfilElegido);

        // System.out.println("redirigir: pantalla: " + (pantalla==null?"":pantalla));
        // System.out.println("redirigir: usuario: " + (usuarioElegido.getPantallaPrincipal()==null?"":usuarioElegido.getPantallaPrincipal().getUrl()));
        if (usu.getPantallaPrincipal() != null) {
            if (pantalla == null) {
                home = usu.getPantallaPrincipal().getUrl();
            } else {
                List<Pantallas> lista = ejbFacade.getEntityManager().createNamedQuery("Pantallas.findByTag", Pantallas.class).setParameter("tag", pantalla).getResultList();
                if (lista.isEmpty()) {
                    home = usu.getPantallaPrincipal().getUrl();
                } else {
                    home = lista.get(0).getUrl();
                }
            }
        } else {
            home = "/index";
        }
        // System.out.println("redirigir: home: " + (home==null?"":home));

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Home", home);

        try {
            // FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/index.xhtml");
            FacesContext.getCurrentInstance().getExternalContext().redirect("/" + endpoint + "/faces/" + home + ".xhtml");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return home + ".xhtml?faces-redirect=true";
    }

    public String loginControl() {
        return loginControl(usuario, contrasena);
    }

    public String loginControl(String usuario, String contrasena) {
        return loginControl(usuario, contrasena, true);
    }

    public String loginControl(String usuario, String contrasena, boolean procesarError) {

        Usuarios usu = verifUsuario(usuario, contrasena, "AC");
        // System.out.println("usu: " + (usu == null ? "" : usu));
        if (usu == null) {
            if (procesarError) {
                PrimeFaces.current().ajax().update("growl");
                FacesContext context = FacesContext.getCurrentInstance();

                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error de acceso"));
                return "";
            }

        } else {
            List<UsuariosAsociados> usuAs = ejbFacade.getEntityManager().createNamedQuery("UsuariosAsociados.findByUsuarioAsociado", UsuariosAsociados.class).setParameter("usuarioAsociado", usu.getId()).getResultList();
            /* if (usuAs.size() > 1) {
                return null;
            }
             */

            List<VPermisosUsuarios> p = null;
            if (usuAs.isEmpty()) {
                usuarioElegido = usu;
                redirigir();
                
                p = ejbFacade.getEntityManager().createNamedQuery("VPermisosUsuarios.findByUsuaId", VPermisosUsuarios.class).setParameter("usuaId", usu.getId()).getResultList();
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("VPermisosUsuarios", p);

                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Usuarios", usu);

                if (usu.getPantallaPrincipal() != null) {
                    if (pantalla == null) {
                        home = usu.getPantallaPrincipal().getUrl();
                    } else {
                        List<Pantallas> lista = ejbFacade.getEntityManager().createNamedQuery("Pantallas.findByTag", Pantallas.class).setParameter("tag", pantalla).getResultList();
                        if (lista.isEmpty()) {
                            home = usuarioOriginal.getPantallaPrincipal().getUrl();
                        } else {
                            home = lista.get(0).getUrl();
                        }
                    }
                } else {
                    home = "index";
                }

                Object appObj = session.getAttribute("esApp");

                boolean esApp = (appObj == null) ? false : (boolean) appObj;

                if (esApp) {
                    listaUsuariosElegir = new ArrayList<>();
                    listaUsuariosElegir.add(usuarioOriginal);

                    PrimeFaces.current().ajax().update("DocumentosListForm");
                    PrimeFaces current = PrimeFaces.current();
                    current.executeScript("PF('DocumentosCreateDialog').show();");
                    return "";
                } else {
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Home", home);

                    return home + ".xhtml?faces-redirect=true";
                }
            } else {
                listaUsuariosElegir = new ArrayList<>();
                listaUsuariosElegir.add(usuarioOriginal);
                for (UsuariosAsociados us : usuAs) {
                    listaUsuariosElegir.add(us.getUsuario());
                }

                PrimeFaces.current().ajax().update("DocumentosListForm");
                PrimeFaces current = PrimeFaces.current();
                current.executeScript("PF('DocumentosCreateDialog').show();");
                return "";
            }
        }
        return "";
    }

    /*
    public Usuarios verifHash() {
        
    }
     */
 /*
    public boolean verifUsuario(String username, String password, String estado) {
        try {
            this.ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

            try {
                personaLogin = this.ejbFacade.getEntityManager().createNamedQuery("Personas.control", Personas.class).setParameter("usuario", username).setParameter("contrasena", password).getSingleResult();
                usuarioLogin = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findByPersona", Usuarios.class).setParameter("persona", personaLogin).getSingleResult();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (personaLogin == null) {
                usuarioLogin = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.control", Usuarios.class).setParameter("usuario", username).setParameter("contrasena", password).getSingleResult();
                if(usuarioLogin.getPersona() != null){
                    personaLogin = this.ejbFacade.getEntityManager().createNamedQuery("Personas.findById", Personas.class).setParameter("id", usuarioLogin.getPersona().getId()).getSingleResult();
                }
            }
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
     */
    public Usuarios verifUsuario(String username, String pass, String estado) {
        try {

            this.ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();

            String password = Utils.passwordToHash(pass);

            // List<Usuarios> usu = null;
            EstadosUsuario est = new EstadosUsuario();

            est.setCodigo(estado);

            try {
                usuarioOriginal = ejbFacade.getEntityManager().createNamedQuery("Usuarios.control", Usuarios.class).setParameter("usuario", username).setParameter("contrasena", password).setParameter("estado", est).getSingleResult();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (usuarioOriginal == null) {
                personaOriginal = this.ejbFacade.getEntityManager().createNamedQuery("Personas.control", Personas.class).setParameter("usuario", username).setParameter("contrasena", password).getSingleResult();
                usuarioOriginal = this.ejbFacade.getEntityManager().createNamedQuery("Usuarios.findByPersona", Usuarios.class).setParameter("persona", personaOriginal).getSingleResult();

                return usuarioOriginal;
            }

            return usuarioOriginal;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String cerrarSession() {
        this.usuario = null;
        this.contrasena = null;
        HttpSession httpSession;
        httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        httpSession.invalidate();//para borrar la session
        return "/login?faces-redirect=true";

    }

    public void setSelected(Usuarios selected) {
        super.setSelected(selected);
    }

    public void save2() {
        super.save(null);
    }

    @Override
    public void save(ActionEvent event) {

        if (getSelected() != null) {
            if (cambioContrasenaFirma1 != null && cambioContrasenaFirma2 != null) {
                if ("".equals(cambioContrasenaFirma1) || "".equals(cambioContrasenaFirma2)) {
                    if (!cambioContrasenaFirma1.equals(cambioContrasenaFirma2)) {
                        JsfUtil.addErrorMessage("Contrasena firma no coincide");
                        return;
                    }
                }
            }
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usu);
            super.save(event);
            if (cambioContrasenaFirma1 != null && cambioContrasenaFirma2 != null) {
                if (cambioContrasenaFirma1.equals(cambioContrasenaFirma2)) {
                    documentosAdministrativosController.generarToken(par, getSelected(), cambioContrasenaFirma1);
                }
            }
        }
    }

    public void saveContrasena(ActionEvent event) {

        if (getSelected() != null) {

            if (cambioContrasena1.equals(cambioContrasena2)) {

                String resp = Utils.politicasContrasena(cambioContrasena1);

                if (!"".equals(resp)) {
                    JsfUtil.addErrorMessage(resp);
                    return;
                }

                String password = Utils.passwordToHash(cambioContrasena1);

                ((Usuarios) getSelected()).setContrasena(password);
                cambioContrasena1 = "";
                cambioContrasena2 = "";
                this.save(event);

            } else {
                JsfUtil.addErrorMessage("Contrasenas no coinciden");
            }
        }
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

            if (cambioContrasena1 != null && cambioContrasena2 != null) {
                if ("".equals(cambioContrasena1) || "".equals(cambioContrasena2)) {
                    JsfUtil.addErrorMessage("Debe ingresar contrasena y confirmar contrasena.");

                } else if (cambioContrasena1.equals(cambioContrasena2)) {

                    String resp = Utils.politicasContrasena(cambioContrasena1);

                    if (!"".equals(resp)) {
                        JsfUtil.addErrorMessage("Contraseña de usuario: " + resp);
                        return;
                    }

                    if (cambioContrasenaFirma1 != null && cambioContrasenaFirma2 != null) {
                        if (!"".equals(cambioContrasenaFirma1) && !"".equals(cambioContrasenaFirma2)) {
                            if (!cambioContrasenaFirma1.equals(cambioContrasenaFirma2)) {
                                JsfUtil.addErrorMessage("Contrasena firma no coincide");
                                return;
                            } else {
                                resp = Utils.politicasContrasena(cambioContrasenaFirma1);

                                if (!"".equals(resp)) {
                                    JsfUtil.addErrorMessage("Contraseña de firma: " + resp);
                                    return;
                                }
                            }
                        }
                    }

                    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

                    Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

                    Date fecha = ejbFacade.getSystemDate();

                    getSelected().setFechaHoraUltimoEstado(fecha);
                    getSelected().setUsuarioUltimoEstado(usu);
                    getSelected().setFechaHoraAlta(fecha);
                    getSelected().setUsuarioAlta(usu);
                    getSelected().setEmpresa(usu.getEmpresa());

                    String password = Utils.passwordToHash(cambioContrasena1);
                    getSelected().setContrasena(password);
                    cambioContrasena1 = "";
                    cambioContrasena2 = "";
                    super.saveNew(event);

                    if (cambioContrasenaFirma1.equals(cambioContrasenaFirma2)) {
                        documentosAdministrativosController.generarToken(par, getSelected(), cambioContrasenaFirma1);
                    }

                } else {
                    JsfUtil.addErrorMessage("Contrasenas no coinciden");
                }
            } else {
                JsfUtil.addErrorMessage("Debe ingresar contrasena y confirmar contrasena");
            }
        }

    }
}
