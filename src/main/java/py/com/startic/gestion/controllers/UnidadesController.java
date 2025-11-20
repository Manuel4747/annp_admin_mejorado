package py.com.startic.gestion.controllers;

import java.util.Collection;
import java.util.Date;

import py.com.startic.gestion.models.Unidades;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "unidadesController")
@ViewScoped
public class UnidadesController extends AbstractController<Unidades> {

    @Inject
    private EmpresasController empresaController;

    public UnidadesController() {
        // Inform the Abstract parent controller of the concrete EstadosDocumento Entity
        super(Unidades.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
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
     * Sets the "items" attribute with a collection of Articulos
     * entities that are retrieved from Unidades?cap_first and returns
     * the navigation outcome.
     *
     * @return navigation outcome for DocumentosJudiciales page
     */
    public String navigateArticulosCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Articulos_items", this.getSelected().getArticulosCollection());
        }
        return "/pages/articulos/index";
    }
    

    @Override
    public Collection<Unidades> getItems() {
        return this.ejbFacade.getEntityManager().createNamedQuery("Unidades.findAll", Unidades.class).getResultList();
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
            getSelected().setEmpresa(usu.getEmpresa());

            super.saveNew(event);

            setSelected(ejbFacade.getEntityManager().createNamedQuery("Unidades.findByCodigo", Unidades.class).setParameter("codigo", getSelected().getCodigo()).getSingleResult());

        }

    }

}
