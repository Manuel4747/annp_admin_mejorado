package py.com.startic.gestion.controllers;

import java.io.File;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.primefaces.PrimeFaces;
import org.primefaces.model.file.UploadedFile;
import py.com.startic.gestion.models.ParametrosSistema;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "parametrosSistemaController")
@ViewScoped
public class ParametrosSistemaController extends AbstractController<ParametrosSistema> {

    @Inject
    private UsuariosController usuariosController;
    private ParametrosSistema par;
    private String imagenFondo;
    private HttpSession session;
    private UploadedFile fileLogo;
    private UploadedFile fileLogin;
    private UploadedFile fileFondo;
    private Usuarios usu;
    private String servidorPuerto;
    private static boolean mostrarPanelAlertas;

    public enum LayoutMode {
        SLIM,
        STATIC,
        OVERLAY,
        HORIZONTAL,
        TOGGLE
    };
    private String theme = "blue-grey";
    private LayoutMode layoutMode = LayoutMode.HORIZONTAL;
    private boolean lightMenu = false;

    public ParametrosSistemaController() {
        // Inform the Abstract parent controller of the concrete ParametrosSistema Entity
        super(ParametrosSistema.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        try {
            par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID).getSingleResult();
            
            theme = par.getTemaPorOmision();
            updateLayoutMode(par.getLayoutMenuPorOmision());

            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String url = request.getRequestURL().toString();
            String uri = request.getRequestURI();
            int pos = url.lastIndexOf(uri);
            url = url.substring(0, pos);
            String[] array = uri.split("/");
            String endpoint = array[1];
            
            // servidorPuerto = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor();
            servidorPuerto = url;
            
            /*
            usu = (Usuarios) session.getAttribute("Usuarios");
            if (usu != null) {
                if(usu.getLayoutMenu() != null){
                    updateLayoutMode(usu.getLayoutMenu());
                }
                if(usu.getTema() != null){
                    theme = usu.getTema();
                }
            }
            */
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        setSelected(par);

        PrimeFaces current = PrimeFaces.current();
        current.executeScript("PF('ParametrosSistemaEditDialog').show();");

    }

    public boolean isMostrarPanelAlertas() {
        return mostrarPanelAlertas;
    }

    public void setMostrarPanelAlertas(boolean mostrarPanelAlertas) {
        this.mostrarPanelAlertas = mostrarPanelAlertas;
    }
    
    public String getOff() {
        return (!mostrarPanelAlertas)?"":"_off";
    }

    public void confPanelAlertas(){
        mostrarPanelAlertas = !mostrarPanelAlertas;
    }

    public String getImagenFondo() {
        
        imagenFondo = "";
        
        if (par != null) {

            File file = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/" + par.getRutaRecursos());
            boolean bool = file.mkdir();

            if (session != null) {
                Usuarios usu = (Usuarios) session.getAttribute("Usuarios");
                if (usu != null) {
                    File tmpDir = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/" + par.getRutaRecursos() + "/" + usu.getId() + "/" + Constantes.NOMBRE_ARCHIVO_FONDO);
                    if (tmpDir.exists()) {
                        imagenFondo = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + par.getRutaRecursos() + "/" + usu.getId() + "/" + Constantes.NOMBRE_ARCHIVO_FONDO;
                    } else {
                        imagenFondo = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + par.getRutaRecursos() + "/" + Constantes.NOMBRE_ARCHIVO_FONDO;
                    }
                }
            }
        }


        return imagenFondo;
    }

    public String getServidorPuerto() {
        return servidorPuerto;
    }

    public void setServidorPuerto(String servidorPuerto) {
        this.servidorPuerto = servidorPuerto;
    }

    public UploadedFile getFileLogo() {
        return fileLogo;
    }

    public void setFileLogo(UploadedFile fileLogo) {
        this.fileLogo = fileLogo;
    }

    public UploadedFile getFileLogin() {
        return fileLogin;
    }

    public void setFileLogin(UploadedFile fileLogin) {
        this.fileLogin = fileLogin;
    }

    public UploadedFile getFileFondo() {
        return fileFondo;
    }

    public void setFileFondo(UploadedFile fileFondo) {
        this.fileFondo = fileFondo;
    }

    public void setImagenFondo(String imagenFondo) {
        this.imagenFondo = imagenFondo;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public LayoutMode getLayoutMode() {
        return layoutMode;
    }

    public void setLayoutMode(LayoutMode layoutMode) {
        this.layoutMode = layoutMode;
    }

    public void updateLayoutMode(String mode) {
        this.layoutMode = LayoutMode.valueOf(mode);
    }
/*
    public void actualizarUsuarioTema(String mode) {
        
        this.theme = mode;
        
        usu.setTema(theme);
        usuariosController.setSelected(usu);
        usuariosController.save(null);
    }

    public void actualizarUsuarioLayout(String mode) {
        this.layoutMode = ParametrosSistemaController.LayoutMode.valueOf(mode);
        
        usu.setLayoutMenu(mode);
        usuariosController.setSelected(usu);
        usuariosController.save(null);
    }
    
    public void resetearPersonalizacion(){
        
        File file = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/" + par.getRutaRecursos() + "/" + usu.getId() + "/" + Constantes.NOMBRE_ARCHIVO_FONDO); 
          
        file.delete();
        
        usu.setLayoutMenu(null);
        usu.setTema(null);
        usuariosController.setSelected(usu);
        usuariosController.save(null);
        
        theme = par.getTemaPorOmision();
        updateLayoutMode(par.getLayoutMenuPorOmision());
        imagenFondo = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + par.getRutaRecursos() + "/" + Constantes.NOMBRE_ARCHIVO_FONDO;
    }
*/
    public boolean isLightMenu() {
        return this.lightMenu;
    }

    public void setLightMenu(boolean value) {
        this.lightMenu = value;
    }
    
    public String getRightPanelLayoutStyleClass(){
        return "layout-static2";
    }

    public String getLayoutStyleClass() {
        String layoutStyleClass;
        switch (this.layoutMode) {
            case SLIM:
                layoutStyleClass = "layout-slim";
                break;

            case STATIC:
                layoutStyleClass = "layout-static";
                break;

            case OVERLAY:
                layoutStyleClass = "layout-overlay";
                break;

            case HORIZONTAL:
                layoutStyleClass = "layout-horizontal";
                break;

            case TOGGLE:
                layoutStyleClass = "layout-toggle";
                break;

            default:
                layoutStyleClass = "layout-horizontal";
                break;
        }

        if (this.lightMenu) {
            layoutStyleClass += " layout-light";
        }

        return layoutStyleClass;
    }
    
    public void prepareEdit() {

        fileLogo = null;
        fileLogin = null;
        fileFondo = null;
    }
/*
    public void save() {

        if (getSelected() != null) {
            updateLayoutMode(getSelected().getLayoutMenuPorOmision());
            setTheme(getSelected().getTemaPorOmision());

            super.save(null, true);
            

            if (par == null) {
                JsfUtil.addErrorMessage("Error al obtener parametros");
                return;
            } else if (par.size() <= 0) {
                JsfUtil.addErrorMessage("Error al obtener parametros.");
                return;
            }
            
            if(fileLogo != null){
                
                if (fileLogo.getContents().length > 0) {
                    byte[] fileByte = null;
                    try {
                        fileByte = IOUtils.toByteArray(fileLogo.getInputstream());
                    
                        if (fileByte != null) {
                            FileOutputStream outputStream = new FileOutputStream(Constantes.RUTA_ARCHIVOS_TEMP + "/" + par.getRutaRecursos() + "/" + Constantes.NOMBRE_ARCHIVO_LOGO);
                            outputStream.write(fileByte);

                            outputStream.close();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("Error al leer archivo logo");
                        return;
                    }
                    
                }
            }
            
            if(fileLogin != null){
                
                if (fileLogin.getContents().length > 0) {
                    
                    byte[] fileByte = null;
                    try {
                        fileByte = IOUtils.toByteArray(fileLogin.getInputstream());
                    
                        if (fileByte != null) {
                            FileOutputStream outputStream = new FileOutputStream(Constantes.RUTA_ARCHIVOS_TEMP + "/" + par.getRutaRecursos() + "/" + Constantes.NOMBRE_ARCHIVO_INICIO);
                            outputStream.write(fileByte);

                            outputStream.close();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("Error al leer archivo login");
                        return;
                    }
                    
                }
            }
            
            if(fileFondo != null){
                
                if (fileFondo.getContents().length > 0) {
                    
                    byte[] fileByte = null;
                    try {
                        fileByte = IOUtils.toByteArray(fileFondo.getInputstream());
                    
                        if (fileByte != null) {
                            FileOutputStream outputStream = new FileOutputStream(Constantes.RUTA_ARCHIVOS_TEMP + "/" + par.getRutaRecursos() + "/" + Constantes.NOMBRE_ARCHIVO_FONDO);
                            outputStream.write(fileByte);

                            outputStream.close();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("Error al leer archivo fondo");
                        return;
                    }
                    
                }
            }
            
                        
            NavigationHandler handler = FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
            handler.handleNavigation(FacesContext.getCurrentInstance(), null, "/index.xhtml?faces-redirect=true");
            // ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            // ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
        }
    }
*/
}
