package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.TiposArchivoAdministrativoPorDepartamentos;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;

@Named(value = "tiposArchivoAdministrativoPorDepartamentoController")
@ViewScoped
public class TiposArchivoAdministrativoPorDepartamentosController extends AbstractController<TiposArchivoAdministrativoPorDepartamentos> {

    public TiposArchivoAdministrativoPorDepartamentosController() {
        // Inform the Abstract parent controller of the concrete TiposPersona Entity
        super(TiposArchivoAdministrativoPorDepartamentos.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }


}
