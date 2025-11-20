package py.com.startic.gestion.controllers;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;

import py.com.startic.gestion.models.FormatosArchivoAdministrativo;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import py.com.startic.gestion.models.ParametrosSistema;
import py.com.startic.gestion.models.TiposArchivoAdministrativo;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "formatosArchivoAdministrativoController")
@ViewScoped
public class FormatosArchivoAdministrativoController extends AbstractController<FormatosArchivoAdministrativo> {

    private List<TiposArchivoAdministrativo> listaTiposArchivo;
    private HttpSession session;
    private ParametrosSistema par;
    private String url;
    private String endpoint;
    private String ckeditorConfig;
    private Usuarios usuario;
    private String titulo;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCkeditorConfig() {
        return ckeditorConfig;
    }

    public void setCkeditorConfig(String ckeditorConfig) {
        this.ckeditorConfig = ckeditorConfig;
    }

    public List<TiposArchivoAdministrativo> getListaTiposArchivo() {
        return listaTiposArchivo;
    }

    public void setListaTiposArchivo(List<TiposArchivoAdministrativo> listaTiposArchivo) {
        this.listaTiposArchivo = listaTiposArchivo;
    }

    public FormatosArchivoAdministrativoController() {
        // Inform the Abstract parent controller of the concrete Formatos Entity
        super(FormatosArchivoAdministrativo.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();

        par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID).getSingleResult();

        
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        int pos = url.lastIndexOf(uri);
        url = url.substring(0, pos);

        String[] array = uri.split("/");
        endpoint = array[1];
        
        ckeditorConfig = url + "/config.js";

        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        usuario = (Usuarios) session.getAttribute("Usuarios");

        listaTiposArchivo = ejbFacade.getEntityManager().createNamedQuery("TiposArchivoAdministrativo.findByMostrarEnMenu", TiposArchivoAdministrativo.class).setParameter("mostrarEnMenu", true).getResultList();

        titulo = "FORMATO DE DOCUMENTOS DE " + usuario.getDepartamento().getNombre();
        
        buscar();
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    private void buscar() {
        this.ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
        setItems(this.ejbFacade.getEntityManager().createNamedQuery("FormatosArchivoAdministrativo.findByDepartamento", FormatosArchivoAdministrativo.class).setParameter("departamento", usuario.getDepartamento()).getResultList());
    }

    @Override
    public Collection<FormatosArchivoAdministrativo> getItems() {
        return super.getItems2();
    }
    
    
    public FormatosArchivoAdministrativo prepareCreate() {

        FormatosArchivoAdministrativo doc = super.prepareCreate(null);
        
        doc.setTexto(par.getFormatoActuaciones());
        
        return doc;
    }

    public void borrar(FormatosArchivoAdministrativo item) {
        item.setUsuarioBorrado(usuario);
        item.setFechaHoraBorrado(ejbFacade.getSystemDate());
        setSelected(item);
        
        super.save(null);
        
        super.delete(null);
        buscar();
    }

    public void guardar() {
        if (getSelected() != null) {

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraAlta(fecha);
            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioAlta(usuario);
            getSelected().setUsuarioUltimoEstado(usuario);
            getSelected().setDepartamento(usuario.getDepartamento());

            super.saveNew(null);
            buscar();
        }
    }
    
    public void guardarEdit() {
        if (getSelected() != null) {

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);

            super.save(null);
            buscar();
        }
    }
}
