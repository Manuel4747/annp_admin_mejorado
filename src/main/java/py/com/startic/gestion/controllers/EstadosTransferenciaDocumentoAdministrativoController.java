package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.EstadosTransferenciaDocumentoAdministrativo;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;

@Named(value = "estadosTransferenciaDocumentoAdministrativoController")
@ViewScoped
public class EstadosTransferenciaDocumentoAdministrativoController extends AbstractController<EstadosTransferenciaDocumentoAdministrativo> {

    public EstadosTransferenciaDocumentoAdministrativoController() {
        // Inform the Abstract parent controller of the concrete EstadosArchivo Entity
        super(EstadosTransferenciaDocumentoAdministrativo.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }
}
