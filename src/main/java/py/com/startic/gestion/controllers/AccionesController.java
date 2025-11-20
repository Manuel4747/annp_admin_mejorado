package py.com.startic.gestion.controllers;

import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import org.primefaces.PrimeFaces;
import py.com.startic.gestion.models.Acciones;

@Named(value = "accionesController")
@ViewScoped
public class AccionesController extends AbstractController<Acciones> {

    @Inject
    private EmpresasController empresaController;

    // Flags to indicate if child collections are empty
    private boolean isPersonasCollectionEmpty;
    private String menu;
    private String pantalla;
    private String updateCerrar;

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getPantalla() {
        return pantalla;
    }

    public void setPantalla(String pantalla) {
        this.pantalla = pantalla;
    }

    public String getUpdateCerrar() {
        return updateCerrar;
    }

    public void setUpdateCerrar(String updateCerrar) {
        this.updateCerrar = updateCerrar;
    }
    
    

    public AccionesController() {
        // Inform the Abstract parent controller of the concrete CargosPersona Entity
        super(Acciones.class);
    }
    public String abrirCreate(String desde, String updateCerrar) {
        menu = Constantes.NO;
        pantalla = Constantes.NO;
        if (Constantes.ABIERTO_DESDE_MENU.equals(desde)) {
            menu = Constantes.SI;
        } else if (Constantes.ABIERTO_DESDE_PANTALLA.equals(desde)) {
            pantalla = Constantes.SI;
            this.updateCerrar = updateCerrar;
        }

        prepareCreate(null);
        PrimeFaces current = PrimeFaces.current();
        current.executeScript("PF('AccionesCreateDialog').show();");
        return "";
    }
     public String updateCerrar() {
        return updateCerrar;
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
    }

    

    public boolean getIsPersonasCollectionEmpty() {
        return this.isPersonasCollectionEmpty;
    }

}
