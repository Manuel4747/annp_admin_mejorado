package py.com.annp.persistencia.controllers;

import py.com.startic.gestion.controllers.*;
import py.com.annp.persistencia.models.FnLicitaciones;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "fnLicitacionesController")
@ViewScoped
public class FnLicitacionesController extends AbstractController<FnLicitaciones> {

    @Inject
    private FnProveedoresController adjudicadoController;

    public FnLicitacionesController() {
        // Inform the Abstract parent controller of the concrete FnLicitaciones Entity
        super(FnLicitaciones.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        adjudicadoController.setSelected(null);
    }

    /**
     * Sets the "items" attribute with a collection of FnContratos entities that
     * are retrieved from FnLicitaciones?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for FnContratos page
     */
    public String navigateFnContratosCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("FnContratos_items", this.getSelected().getFnContratosCollection());
        }
        return "/pages/fnContratos/index";
    }

    /**
     * Sets the "selected" attribute of the FnProveedores controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareAdjudicado(ActionEvent event) {
        if (this.getSelected() != null && adjudicadoController.getSelected() == null) {
            adjudicadoController.setSelected(this.getSelected().getAdjudicado());
        }
    }

    /**
     * Sets the "items" attribute with a collection of FnDetalleLicitacion
     * entities that are retrieved from FnLicitaciones?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for FnDetalleLicitacion page
     */
    public String navigateFnDetalleLicitacionCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("FnDetalleLicitacion_items", this.getSelected().getFnDetalleLicitacionCollection());
        }
        return "/pages/fnDetalleLicitacion/index";
    }

}
