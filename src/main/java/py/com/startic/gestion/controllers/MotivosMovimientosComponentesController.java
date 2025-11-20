package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.MotivosMovimientosComponentes;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "motivosMovimientosComponentesController")
@ViewScoped
public class MotivosMovimientosComponentesController extends AbstractController<MotivosMovimientosComponentes> {

    @Inject
    private EmpresasController empresaController;

    public MotivosMovimientosComponentesController() {
        // Inform the Abstract parent controller of the concrete MotivosMovimientosComponentes Entity
        super(MotivosMovimientosComponentes.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }

    /**
     * Sets the "items" attribute with a collection of MovimientosComponentes
     * entities that are retrieved from MotivosMovimientosComponentes?cap_first
     * and returns the navigation outcome.
     *
     * @return navigation outcome for MovimientosComponentes page
     */
    public String navigateMovimientosComponentesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("MovimientosComponentes_items", this.getSelected().getMovimientosComponentesCollection());
        }
        return "/pages10/movimientosComponentes/index";
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
}
