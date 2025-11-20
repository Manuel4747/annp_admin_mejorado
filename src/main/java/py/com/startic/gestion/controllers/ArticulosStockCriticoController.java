package py.com.startic.gestion.controllers;


import java.util.Collection;
import py.com.startic.gestion.models.Articulos;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;


@Named(value = "articulosStockCriticoController")
@ViewScoped
public class ArticulosStockCriticoController extends AbstractController<Articulos> {

    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    private boolean hayArticulosStockCritico = false;

    public ArticulosStockCriticoController() {
        // Inform the Abstract parent controller of the concrete Articulos Entity
        super(Articulos.class);
    }

    
    public boolean isHayArticulosStockCritico() {
        return hayArticulosStockCritico;
    }

    public void setHayArticulosStockCritico(boolean hayArticulosStockCritico) {
        this.hayArticulosStockCritico = hayArticulosStockCritico;
    }
    
    public boolean verifArticulosStockCritico(){
        hayArticulosStockCritico = false;
        Collection<Articulos> art = this.getItems();
        
        if( art != null){
            if( art.size() >  0 ){
                hayArticulosStockCritico = true;
            }
        }
        return hayArticulosStockCritico;
    }
    
    @Override
    public Collection<Articulos> getItems() {
        Collection<Articulos> art = this.ejbFacade.getEntityManager().createNamedQuery("Articulos.findByStockCritico", Articulos.class).getResultList();
        
        hayArticulosStockCritico = false;
        
        if( art != null){
            if( art.size() >  0 ){
                hayArticulosStockCritico = true;
            }
        }
        
        return art;
    }

    /**
     * Sets the "items" attribute with a collection of DetallesSalidaArticulo
     * entities that are retrieved from Articulos?cap_first and returns the
     * navigation outcome.
     *
     * @return navigation outcome for DetallesSalidaArticulo page
     */
    public String navigateDetallesSalidaArticuloCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("DetallesSalidaArticulo_items", this.getSelected().getDetallesSalidaArticuloCollection());
        }
        return "/pages/detallesSalidaArticulo/index";
    }
        
        
    public String navigateArticulosStockCritico() {
        return "/pages/articulosStockCritico/index";
    }


    /**
     * Sets the "selected" attribute of the Usuarios controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareUsuarioAlta(ActionEvent event) {
        if (this.getSelected() != null && usuarioAltaController.getSelected() == null) {
            usuarioAltaController.setSelected(this.getSelected().getUsuarioAlta());
        }
    }

    /**
     * Sets the "selected" attribute of the Usuarios controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareUsuarioUltimoEstado(ActionEvent event) {
        if (this.getSelected() != null && usuarioUltimoEstadoController.getSelected() == null) {
            usuarioUltimoEstadoController.setSelected(this.getSelected().getUsuarioUltimoEstado());
        }
    }
}
