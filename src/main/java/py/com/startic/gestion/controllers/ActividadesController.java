package py.com.startic.gestion.controllers;

import java.util.List;
import py.com.startic.gestion.models.Actividades;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import py.com.startic.gestion.models.Acciones;
import py.com.startic.gestion.models.TiposObjetivos;

@Named(value = "actividadesController")
@ViewScoped
public class ActividadesController extends AbstractController<Actividades> {

    @Inject
    private AccionesController accionesController;
    private List<Acciones> listaAcciones;
    private TiposObjetivos tiposObjetivos;
    private Acciones accion;

    public List<Acciones> getListaAcciones() {
        return listaAcciones;
    }

    public void setListaAcciones(List<Acciones> listaAcciones) {
        this.listaAcciones = listaAcciones;
    }

    public Acciones getAccion() {
        return accion;
    }

    public void setAccion(Acciones accion) {
        this.accion = accion;
    }

    public TiposObjetivos getTiposObjetivos() {
        return tiposObjetivos;
    }

    public void setTiposObjetivos(TiposObjetivos tiposObjetivos) {
        this.tiposObjetivos = tiposObjetivos;
    }

    public ActividadesController() {
        // Inform the Abstract parent controller of the concrete Actividades Entity
        super(Actividades.class);
    }

    public void resetParents() {
        accionesController.setSelected(null);
        
    }

    private List<Acciones> obtenerListaTiposObjetivos(TiposObjetivos tiposObjetivos) {
        if (tiposObjetivos != null) {
            return this.ejbFacade.getEntityManager().createNamedQuery("Acciones.findByTiposObjetivos", Acciones.class
            ).setParameter("tiposObjetivos", tiposObjetivos).getResultList();
        }
        return null;
    }

    public void actualizarListas(TiposObjetivos tiposObjetivos) {
        listaAcciones = obtenerListaTiposObjetivos(tiposObjetivos);
    }

    public void resetearListas(TiposObjetivos tiposObjetivos) {
        accion = null;

        if (getSelected() != null) {
            getSelected().setAccion(null);

        }
        actualizarListas(tiposObjetivos);
    }

}
