package py.com.startic.gestion.controllers;

import java.util.Collection;
import jakarta.annotation.PostConstruct;
import py.com.startic.gestion.models.FirmasArchivoAdministrativo;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;

@Named(value = "firmasArchivoAdministrativoController")
@ViewScoped
public class FirmasArchivoAdministrativoController extends AbstractController<FirmasArchivoAdministrativo> {


    public FirmasArchivoAdministrativoController() {
        // Inform the Abstract parent controller of the concrete DespachosPersona Entity
        super(FirmasArchivoAdministrativo.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        setItems(this.ejbFacade.getEntityManager().createNamedQuery("FirmasArchivoAdministrativo.findAll", FirmasArchivoAdministrativo.class).getResultList());
    }
    
    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    @Override
    public Collection<FirmasArchivoAdministrativo> getItems() {
        return super.getItems2();
    }

}
