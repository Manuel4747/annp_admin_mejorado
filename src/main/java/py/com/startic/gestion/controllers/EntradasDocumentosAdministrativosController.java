package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.EntradasDocumentosAdministrativos;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;

@Named(value = "entradasDocumentosAdministrativosController")
@ViewScoped
public class EntradasDocumentosAdministrativosController extends AbstractController<EntradasDocumentosAdministrativos> {


    public EntradasDocumentosAdministrativosController() {
        // Inform the Abstract parent controller of the concrete EntradasDocumentosAdministrativos Entity
        super(EntradasDocumentosAdministrativos.class);
    }

}
