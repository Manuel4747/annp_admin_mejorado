package py.com.startic.gestion.controllers;


import java.util.Collection;
import py.com.startic.gestion.models.GruposSanguineo;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;

@Named(value = "gruposSanguineoController")
@ViewScoped
public class GruposSanguineoController extends AbstractController<GruposSanguineo> {

    public GruposSanguineoController() {
        // Inform the Abstract parent controller of the concrete GruposSanguineo Entity
        super(GruposSanguineo.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    @Override
    public Collection<GruposSanguineo> getItems() {
        setItems(this.ejbFacade.getEntityManager().createNamedQuery("GruposSanguineo.findAll", GruposSanguineo.class).getResultList());
        return super.getItems2();
    }


}
