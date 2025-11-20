package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.AvisosPorUsuarios;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;

@Named(value = "avisoesPorUsuariosContavisoler")
@ViewScoped
public class AvisosPorUsuariosController extends AbstractController<AvisosPorUsuarios> {


    public AvisosPorUsuariosController() {
        // Inform the Abstract parent contavisoler of the concrete AvisosPorUsuarios Entity
        super(AvisosPorUsuarios.class);
    }

    @Override
    protected void setEmbeddableKeys() {
        this.getSelected().getAvisosPorUsuariosPK().setUsuario(this.getSelected().getUsuarios().getId());
        this.getSelected().getAvisosPorUsuariosPK().setAviso(this.getSelected().getAvisos().getId());
    }

    @Override
    protected void initializeEmbeddableKey() {
        this.getSelected().setAvisosPorUsuariosPK(new py.com.startic.gestion.models.AvisosPorUsuariosPK());
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }
    
}
