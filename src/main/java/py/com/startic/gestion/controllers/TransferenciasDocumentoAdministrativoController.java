package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.TransferenciasDocumentoAdministrativo;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;

@Named(value = "cambiosEstadoDocumentoAdministrativoController")
@ViewScoped
public class TransferenciasDocumentoAdministrativoController extends AbstractController<TransferenciasDocumentoAdministrativo> {


    public TransferenciasDocumentoAdministrativoController() {
        // Inform the Abstract parent controller of the concrete CambiosEstadoDocumento Entity
        super(TransferenciasDocumentoAdministrativo.class);
    }

}
