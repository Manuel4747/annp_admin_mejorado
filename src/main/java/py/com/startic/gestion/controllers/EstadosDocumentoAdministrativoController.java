package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.EstadosDocumentoAdministrativo;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;

@Named(value = "estadosDocumentoAdministraticoController")
@ViewScoped
public class EstadosDocumentoAdministrativoController extends AbstractController<EstadosDocumentoAdministrativo> {


    public EstadosDocumentoAdministrativoController() {
        // Inform the Abstract parent controller of the concrete EstadosDocumentoAdministrativo Entity
        super(EstadosDocumentoAdministrativo.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

}
