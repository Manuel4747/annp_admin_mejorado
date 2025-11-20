package py.com.startic.gestion.controllers;



import java.util.Collection;
import java.util.Date;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.servlet.http.HttpSession;
import py.com.startic.gestion.models.DetallesPlanEstrategicas;
import py.com.startic.gestion.models.ObservacionesDocumentosPlanificacion;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "observacionesDocumentosPlanificacionController")
@ViewScoped
public class ObservacionesDocumentosPlanificacionController extends AbstractController<ObservacionesDocumentosPlanificacion> {
 private String paginaVolver;
  private DetallesPlanEstrategicas docOrigen;

    public String getPaginaVolver() {
        return paginaVolver;
    }

    public void setPaginaVolver(String paginaVolver) {
        this.paginaVolver = paginaVolver;
    }

    public DetallesPlanEstrategicas getDocOrigen() {
        return docOrigen;
    }

    public void setDocOrigen(DetallesPlanEstrategicas docOrigen) {
        this.docOrigen = docOrigen;
    }

   

    public ObservacionesDocumentosPlanificacionController() {
        // Inform the Abstract parent controller of the concrete Bonificaciones Entity
        super(ObservacionesDocumentosPlanificacion.class);
    }
     @PostConstruct
    @Override
    public void initParams() {
        super.initParams();

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        docOrigen = (DetallesPlanEstrategicas) session.getAttribute("doc_origen");

        session.removeAttribute("doc_origen");

        paginaVolver = (String) session.getAttribute("paginaVolver");

        session.removeAttribute("paginaVolver");

    }
     /*public String navigateDocumentosJudicialesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Informes_items", this.getSelected().getDocumentosJudicialesCollection());
        }
        return "/pages/accionOperativa/index";
    }*/

    public String navigateVolver() {
        return paginaVolver;
    }
     @Override
    public Collection<ObservacionesDocumentosPlanificacion> getItems() {
        ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
        return ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosPlanificacion.findByDocumentoPlanificacion", ObservacionesDocumentosPlanificacion.class).setParameter("detallePlanEstrategica", docOrigen).getResultList();
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
