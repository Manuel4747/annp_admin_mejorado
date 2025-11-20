package py.com.annp.persistencia.controllers;

import py.com.startic.gestion.controllers.*;
import py.com.annp.persistencia.models.FnDetalleReprogramacion;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "fnDetalleReprogramacionController")
@ViewScoped
public class FnDetalleReprogramacionController extends AbstractController<FnDetalleReprogramacion> {

    @Inject
    private FnDetallePlanFinancieroController idDetallePtoAumentarController;
    @Inject
    private FnDetallePlanFinancieroController idDetallePtoDisminuirController;
    @Inject
    private FnTipoDeVariacionRpController tipoDeVariacionRpController;
    @Inject
    private FnDetalleReprogramacionController reprogramacionesController;

    public FnDetalleReprogramacionController() {
        // Inform the Abstract parent controller of the concrete FnDetalleReprogramacion Entity
        super(FnDetalleReprogramacion.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        idDetallePtoAumentarController.setSelected(null);
        idDetallePtoDisminuirController.setSelected(null);
        tipoDeVariacionRpController.setSelected(null);
        reprogramacionesController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the FnDetallePlanFinanciero controller
     * in order to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareIdDetallePtoAumentar(ActionEvent event) {
        if (this.getSelected() != null && idDetallePtoAumentarController.getSelected() == null) {
            idDetallePtoAumentarController.setSelected(this.getSelected().getIdDetallePtoAumentar());
        }
    }

    /**
     * Sets the "selected" attribute of the FnDetallePlanFinanciero controller
     * in order to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareIdDetallePtoDisminuir(ActionEvent event) {
        if (this.getSelected() != null && idDetallePtoDisminuirController.getSelected() == null) {
            idDetallePtoDisminuirController.setSelected(this.getSelected().getIdDetallePtoDisminuir());
        }
    }

    /**
     * Sets the "selected" attribute of the FnTipoDeVariacionRp controller in
     * order to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareTipoDeVariacionRp(ActionEvent event) {
        if (this.getSelected() != null && tipoDeVariacionRpController.getSelected() == null) {
            tipoDeVariacionRpController.setSelected(this.getSelected().getTipoDeVariacionRp());
        }
    }

    /**
     * Sets the "items" attribute with a collection of FnDetalleReprogramacion
     * entities that are retrieved from FnDetalleReprogramacion?cap_first and
     * returns the navigation outcome.
     *
     * @return navigation outcome for FnDetalleReprogramacion page
     */
    public String navigateFnDetalleReprogramacionCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("FnDetalleReprogramacion_items", this.getSelected().getFnDetalleReprogramacionCollection());
        }
        return "/pages/fnDetalleReprogramacion/index";
    }

    /**
     * Sets the "selected" attribute of the FnDetalleReprogramacion controller
     * in order to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareReprogramaciones(ActionEvent event) {
        if (this.getSelected() != null && reprogramacionesController.getSelected() == null) {
            reprogramacionesController.setSelected(this.getSelected().getReprogramaciones());
        }
    }
}
