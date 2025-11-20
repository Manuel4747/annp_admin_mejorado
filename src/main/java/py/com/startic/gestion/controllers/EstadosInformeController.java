package py.com.startic.gestion.controllers;



import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import py.com.startic.gestion.models.EstadosInforme;

@Named(value = "estadosInformeController")
@ViewScoped
public class EstadosInformeController extends AbstractController<EstadosInforme> {

    @Inject
    private EmpresasController empresaController;

    public EstadosInformeController() {
        // Inform the Abstract parent controller of the concrete Estados Entity
        super(EstadosInforme.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        //empresaController.setSelected(null);
    }

}
