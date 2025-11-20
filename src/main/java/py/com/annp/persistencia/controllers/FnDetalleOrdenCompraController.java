package py.com.annp.persistencia.controllers;

import py.com.startic.gestion.controllers.*;
import py.com.annp.persistencia.models.FnDetalleOrdenCompra;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;

@Named(value = "fnDetalleOrdenCompraController")
@ViewScoped
public class FnDetalleOrdenCompraController extends AbstractController<FnDetalleOrdenCompra> {

    public FnDetalleOrdenCompraController() {
        // Inform the Abstract parent controller of the concrete FnDetalleOrdenCompra Entity
        super(FnDetalleOrdenCompra.class);
    }

}
