package py.com.startic.gestion.controllers;

import py.com.startic.gestion.models.UsuariosPorDocumentosAdministrativos;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;

@Named(value = "usuariosPorDocumentosAdministrativosController")
@ViewScoped
public class UsuariosPorDocumentosAdministrativosController extends AbstractController<UsuariosPorDocumentosAdministrativos> {


    public UsuariosPorDocumentosAdministrativosController() {
        // Inform the Abstract parent controller of the concrete UsuariosPorDocumentosAdministrativos Entity
        super(UsuariosPorDocumentosAdministrativos.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

}
