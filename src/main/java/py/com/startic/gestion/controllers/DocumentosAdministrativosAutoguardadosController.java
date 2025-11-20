package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.DocumentosAdministrativosAutoguardados;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;

@Named(value = "documentosAdministrativosAutoguardadosController")
@ViewScoped
public class DocumentosAdministrativosAutoguardadosController extends AbstractController<DocumentosAdministrativosAutoguardados> {


    public DocumentosAdministrativosAutoguardadosController() {
        // Inform the Abstract parent controller of the concrete DocumentosAdministrativosAutoguardados Entity
        super(DocumentosAdministrativosAutoguardados.class);
    }
}
