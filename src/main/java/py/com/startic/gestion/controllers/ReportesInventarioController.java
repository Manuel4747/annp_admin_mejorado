package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.ReportesInventario;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;

@Named(value = "reportesInventarioController")
@ViewScoped
public class ReportesInventarioController extends AbstractController<ReportesInventario> {


    public ReportesInventarioController() {
        // Inform the Abstract parent controller of the concrete ReportesInventario Entity
        super(ReportesInventario.class);
    }

}
