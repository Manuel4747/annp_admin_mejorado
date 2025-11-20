package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.EstadosBienes;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

@Named(value = "estadosBienesController")
@ViewScoped
public class EstadosBienesController extends AbstractController<EstadosBienes> {


    public EstadosBienesController() {
        // Inform the Abstract parent controller of the concrete EstadosBienes Entity
        super(EstadosBienes.class);
    }

    /**
     * Sets the "items" attribute with a collection of Bienes entities that are
     * retrieved from EstadosBienes?cap_first and returns the navigation
     * outcome.
     *
     * @return navigation outcome for Bienes page
     */
    public String navigateBienesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Bienes_items", this.getSelected().getBienesCollection());
        }
        return "/pages/bienes/index";
    }

}
