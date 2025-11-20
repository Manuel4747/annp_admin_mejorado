package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.MotivosMovimientosBienes;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

@Named(value = "motivosMovimientosBienesController")
@ViewScoped
public class MotivosMovimientosBienesController extends AbstractController<MotivosMovimientosBienes> {


    public MotivosMovimientosBienesController() {
        // Inform the Abstract parent controller of the concrete MotivosMovimientosBienes Entity
        super(MotivosMovimientosBienes.class);
    }

    /**
     * Sets the "items" attribute with a collection of MovimientosBienes
     * entities that are retrieved from MotivosMovimientosBienes?cap_first and
     * returns the navigation outcome.
     *
     * @return navigation outcome for MovimientosBienes page
     */
    public String navigateMovimientosBienesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("MovimientosBienes_items", this.getSelected().getMovimientosBienesCollection());
        }
        return "/pages/movimientosBienes/index";
    }

}
