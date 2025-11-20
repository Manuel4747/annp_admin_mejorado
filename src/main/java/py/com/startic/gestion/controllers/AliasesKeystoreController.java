package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.AliasesKeystore;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;

@Named(value = "aliasesKeystoreController")
@ViewScoped
public class AliasesKeystoreController extends AbstractController<AliasesKeystore> {

    @Inject
    private EmpresasController empresaController;

    public AliasesKeystoreController() {
        // Inform the Abstract parent controller of the concrete AliasesKeystore Entity
        super(AliasesKeystore.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }
    
}