package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.TiposArchivoAdministrativo;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;

@Named(value = "tiposArchivoAdministrativoController")
@ViewScoped
public class TiposArchivoAdministrativoController extends AbstractController<TiposArchivoAdministrativo> {

    public TiposArchivoAdministrativoController() {
        // Inform the Abstract parent controller of the concrete TiposPersona Entity
        super(TiposArchivoAdministrativo.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }


}
