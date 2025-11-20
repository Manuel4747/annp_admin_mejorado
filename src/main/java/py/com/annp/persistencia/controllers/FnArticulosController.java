package py.com.annp.persistencia.controllers;

import py.com.startic.gestion.controllers.*;
import py.com.annp.persistencia.models.FnArticulos;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "fnArticulosController")
@ViewScoped
public class FnArticulosController extends AbstractController<FnArticulos> {

    @Inject
    private FnCategoriaArticuloController categoriaController;
    @Inject
    private FnRubroArticuloController rubroController;
    @Inject
    private FnTipoArticuloController tipoController;

    public FnArticulosController() {
        // Inform the Abstract parent controller of the concrete FnArticulos Entity
        super(FnArticulos.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        categoriaController.setSelected(null);
        rubroController.setSelected(null);
        tipoController.setSelected(null);
    }

    /**
     * Sets the "items" attribute with a collection of FnArticuloCaracteristicas
     * entities that are retrieved from FnArticulos?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for FnArticuloCaracteristicas page
     */
    public String navigateFnArticuloCaracteristicasCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("FnArticuloCaracteristicas_items", this.getSelected().getFnArticuloCaracteristicasCollection());
        }
        return "/pages/fnArticuloCaracteristicas/index";
    }

    /**
     * Sets the "selected" attribute of the FnCategoriaArticulo controller in
     * order to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareCategoria(ActionEvent event) {
        if (this.getSelected() != null && categoriaController.getSelected() == null) {
            categoriaController.setSelected(this.getSelected().getCategoria());
        }
    }

    /**
     * Sets the "selected" attribute of the FnRubroArticulo controller in order
     * to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareRubro(ActionEvent event) {
        if (this.getSelected() != null && rubroController.getSelected() == null) {
            rubroController.setSelected(this.getSelected().getRubro());
        }
    }

    /**
     * Sets the "selected" attribute of the FnTipoArticulo controller in order
     * to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareTipo(ActionEvent event) {
        if (this.getSelected() != null && tipoController.getSelected() == null) {
            tipoController.setSelected(this.getSelected().getTipo());
        }
    }

    /**
     * Sets the "items" attribute with a collection of FnDetalleFactura entities
     * that are retrieved from FnArticulos?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for FnDetalleFactura page
     */
    public String navigateFnDetalleFacturaCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("FnDetalleFactura_items", this.getSelected().getFnDetalleFacturaCollection());
        }
        return "/pages/fnDetalleFactura/index";
    }

}
