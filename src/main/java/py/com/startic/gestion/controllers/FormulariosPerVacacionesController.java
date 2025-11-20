package py.com.startic.gestion.controllers;

import java.util.Collection;
import py.com.startic.gestion.models.FormulariosPerVacaciones;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;

@Named(value = "formulariosPerVacacionesController")
@ViewScoped
public class FormulariosPerVacacionesController extends AbstractController<FormulariosPerVacaciones> {

    public FormulariosPerVacacionesController() {
        // Inform the Abstract parent controller of the concrete FormulariosPerVacacionesController Entity
        super(FormulariosPerVacaciones.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    @Override
    public Collection<FormulariosPerVacaciones> getItems() {
        setItems(this.ejbFacade.getEntityManager().createNamedQuery("FormulariosPerVacaciones.findAll", FormulariosPerVacaciones.class).getResultList());
        return super.getItems2();
    }


}
