package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.RhModosVerificacion;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

@Named(value = "rhModosVerificacionController")
@ViewScoped
public class RhModosVerificacionController extends AbstractController<RhModosVerificacion> {


    public RhModosVerificacionController() {
        // Inform the Abstract parent controller of the concrete RhModosVerificacion Entity
        super(RhModosVerificacion.class);
    }

    /**
     * Sets the "items" attribute with a collection of RhMarcaciones entities
     * that are retrieved from RhModosVerificacion?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for RhMarcaciones page
     */
    public String navigateRhMarcacionesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("RhMarcaciones_items", this.getSelected().getRhMarcacionesCollection());
        }
        return "/pages18/rhMarcaciones/index";
    }

}
