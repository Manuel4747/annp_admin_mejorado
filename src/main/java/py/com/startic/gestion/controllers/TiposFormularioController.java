package py.com.startic.gestion.controllers;


import java.util.Collection;
import py.com.startic.gestion.models.TiposFormulario;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;

@Named(value = "tiposFormularioController")
@ViewScoped
public class TiposFormularioController extends AbstractController<TiposFormulario> {

    public TiposFormularioController() {
        // Inform the Abstract parent controller of the concrete TiposFormulario Entity
        super(TiposFormulario.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    @Override
    public Collection<TiposFormulario> getItems() {
        setItems(this.ejbFacade.getEntityManager().createNamedQuery("TiposFormulario.findAll", TiposFormulario.class).getResultList());
        return super.getItems2();
    }


}
