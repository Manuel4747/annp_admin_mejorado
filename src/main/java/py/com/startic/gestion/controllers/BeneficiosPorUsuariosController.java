package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.BeneficiosPorUsuarios;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;

@Named(value = "beneficiosPorUsuariosController")
@ViewScoped
public class BeneficiosPorUsuariosController extends AbstractController<BeneficiosPorUsuarios> {

    public BeneficiosPorUsuariosController() {
        // Inform the Abstract parent controller of the concrete BeneficiosPorUsuarios Entity
        super(BeneficiosPorUsuarios.class);
    }

}
