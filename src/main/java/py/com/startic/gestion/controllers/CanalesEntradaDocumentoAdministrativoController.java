package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.CanalesEntradaDocumentoAdministrativo;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;

@Named(value = "canalesEntradaDocumentoAdministrativoController")
@ViewScoped
public class CanalesEntradaDocumentoAdministrativoController extends AbstractController<CanalesEntradaDocumentoAdministrativo> {


    public CanalesEntradaDocumentoAdministrativoController() {
        // Inform the Abstract parent controller of the concrete EstadosDocumento Entity
        super(CanalesEntradaDocumentoAdministrativo.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

}
