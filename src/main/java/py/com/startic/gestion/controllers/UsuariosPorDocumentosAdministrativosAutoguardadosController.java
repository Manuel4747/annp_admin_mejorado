package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.UsuariosPorDocumentosAdministrativosAutoguardados;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;

@Named(value = "usuariosPorDocumentosAdministrativosAutoguardadosController")
@ViewScoped
public class UsuariosPorDocumentosAdministrativosAutoguardadosController extends AbstractController<UsuariosPorDocumentosAdministrativosAutoguardados> {


    public UsuariosPorDocumentosAdministrativosAutoguardadosController() {
        // Inform the Abstract parent controller of the concrete UsuariosPorDocumentosAdministrativosAutoguardados Entity
        super(UsuariosPorDocumentosAdministrativosAutoguardados.class);
    }
}
