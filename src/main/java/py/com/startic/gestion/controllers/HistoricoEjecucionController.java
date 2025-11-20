package py.com.startic.gestion.controllers;


import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import py.com.startic.gestion.models.HistoricoEjecucion;

@Named(value = "historicoEjecucionController")
@ViewScoped
public class HistoricoEjecucionController extends AbstractController<HistoricoEjecucion> {

   

    public HistoricoEjecucionController() {
        // Inform the Abstract parent controller of the concrete HistoricoEjecucion Entity
        super(HistoricoEjecucion.class);
    }
     /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
     
    }


    /**
     * Sets the "items" attribute with a collection of Formulario entities that
 are retrieved from HistoricoEjecucion?cap_first and returns the
 navigation outcome.
     *
     * @return navigation outcome for Formulario page
     */
    public String navigateFormularioCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Formulario_items", this.getSelected().getFormularioCollection());
        }
        return null;
    }

}
