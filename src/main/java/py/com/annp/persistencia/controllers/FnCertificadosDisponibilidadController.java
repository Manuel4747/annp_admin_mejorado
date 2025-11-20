package py.com.annp.persistencia.controllers;

import py.com.startic.gestion.controllers.*;
import py.com.annp.persistencia.models.FnCertificadosDisponibilidad;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;

@Named(value = "fnCertificadosDisponibilidadController")
@ViewScoped
public class FnCertificadosDisponibilidadController extends AbstractController<FnCertificadosDisponibilidad> {

    public FnCertificadosDisponibilidadController() {
        // Inform the Abstract parent controller of the concrete FnCertificadosDisponibilidad Entity
        super(FnCertificadosDisponibilidad.class);
    }

}
