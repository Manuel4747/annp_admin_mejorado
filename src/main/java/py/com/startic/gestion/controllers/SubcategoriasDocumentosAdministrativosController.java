package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.SubcategoriasDocumentosAdministrativos;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;

@Named(value = "subcategoriasDocumentosAdministrativosController")
@ViewScoped
public class SubcategoriasDocumentosAdministrativosController extends AbstractController<SubcategoriasDocumentosAdministrativos> {


    public SubcategoriasDocumentosAdministrativosController() {
        // Inform the Abstract parent controller of the concrete SubcategoriasDocumentosAdministrativos Entity
        super(SubcategoriasDocumentosAdministrativos.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

}
