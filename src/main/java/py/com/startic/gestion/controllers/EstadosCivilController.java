package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.EstadosCivil;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;

@Named(value = "estadosCivilController")
@ViewScoped
public class EstadosCivilController extends AbstractController<EstadosCivil> {

    public EstadosCivilController() {
        // Inform the Abstract parent controller of the concrete EstadosCivil Entity
        super(EstadosCivil.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }
}
