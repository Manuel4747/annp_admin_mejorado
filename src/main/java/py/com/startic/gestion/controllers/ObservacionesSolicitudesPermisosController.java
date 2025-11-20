package py.com.startic.gestion.controllers;



import java.util.Collection;
import java.util.Date;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.servlet.http.HttpSession;
import py.com.startic.gestion.models.FormulariosPermisos;
import py.com.startic.gestion.models.ObservacionesSolicitudesPermisos;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "ObservacionesSolicitudesPermisosController")
@ViewScoped
public class ObservacionesSolicitudesPermisosController extends AbstractController<ObservacionesSolicitudesPermisos> {
 private String paginaVolver;
  private FormulariosPermisos docOrigen;

    public String getPaginaVolver() {
        return paginaVolver;
    }

    public void setPaginaVolver(String paginaVolver) {
        this.paginaVolver = paginaVolver;
    }

    public FormulariosPermisos getDocOrigen() {
        return docOrigen;
    }

    public void setDocOrigen(FormulariosPermisos docOrigen) {
        this.docOrigen = docOrigen;
    }
  

    public ObservacionesSolicitudesPermisosController() {
        // Inform the Abstract parent controller of the concrete Bonificaciones Entity
        super(ObservacionesSolicitudesPermisos.class);
    }
     @PostConstruct
    @Override
    public void initParams() {
        super.initParams();

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        docOrigen = (FormulariosPermisos) session.getAttribute("doc_origen");

        session.removeAttribute("doc_origen");

        paginaVolver = (String) session.getAttribute("paginaVolver");

        session.removeAttribute("paginaVolver");

    }

    public String navigateVolver() {
        return paginaVolver;
    }
     @Override
    public Collection<ObservacionesSolicitudesPermisos> getItems() {
        ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
        return ejbFacade.getEntityManager().createNamedQuery("ObservacionesSolicitudesPermisos.findBySolicitudPermiso", ObservacionesSolicitudesPermisos.class).setParameter("formulariosPermisos", docOrigen).getResultList();
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
     
}
