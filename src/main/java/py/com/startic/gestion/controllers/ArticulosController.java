package py.com.startic.gestion.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import py.com.startic.gestion.models.Articulos;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;
import org.primefaces.PrimeFaces;
import py.com.startic.gestion.controllers.util.JsfUtil;

import py.com.startic.gestion.models.DetallesInventario;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "articulosController")
@ViewScoped
public class ArticulosController extends AbstractController<Articulos> {

    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private DetallesInventarioController detalleInventarioController;
    private Collection<Articulos> filtrados;

    public Collection<Articulos> getFiltrados() {
        return filtrados;
    }

    public void setFiltrados(Collection<Articulos> filtrados) {
        this.filtrados = filtrados;
    }

    public ArticulosController() {
        // Inform the Abstract parent controller of the concrete Articulos Entity
        super(Articulos.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
    }

    @Override
    public void delete(ActionEvent event) {
        Articulos art = ejbFacade.getEntityManager().createNamedQuery("Articulos.findByCodigo", Articulos.class).setParameter("codigo", getSelected().getCodigo()).getSingleResult();

        boolean borrar = true;

        Collection<DetallesInventario> colInv = new ArrayList<>();

        for (DetallesInventario det : art.getDetallesInventarioCollection()) {
            if ("TE".equals(det.getInventario().getEstado().getCodigo())
                    || "CE".equals(det.getInventario().getEstado().getCodigo())) {
                borrar = false;
            }
            if ("PR".equals(det.getInventario().getEstado().getCodigo())) {
                colInv.add(det);
            }
        }

        if (borrar) {

            if (colInv.size() > 0) {
                for (DetallesInventario inv : colInv) {
                    detalleInventarioController.setSelected(inv);
                    detalleInventarioController.delete(event);
                }
            }

            super.delete(event);
            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('wDatalist').clearFilters()");
            //setItems(this.ejbFacade.getEntityManager().createNamedQuery("Articulos.findOrdered", Articulos.class).getResultList());

        } else {
            JsfUtil.addErrorMessage("No se puede borrar un articulo que ya fue incluido en un inventario vigente o cerrado");
        }
    }

    @Override
    public Collection<Articulos> getItems() {
        return this.ejbFacade.getEntityManager().createNamedQuery("Articulos.findOrdered", Articulos.class).getResultList();
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

    @Override
    public void save(ActionEvent event) {

        if (getSelected() != null) {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usu);
        }

        super.save(event);
    }

    /**
     * Store a new item in the data layer.
     *
     * @param event an event from the widget that wants to save a new Entity to
     * the data layer
     */
    @Override
    public void saveNew(ActionEvent event) {
        if (getSelected() != null) {
            /*
            if (getSelected().getStock() <= 0) {
                JsfUtil.addErrorMessage("El stock debe ser mayor a cero");
                return;
            }
             */
            getSelected().setStock(0);

            if (getSelected().getStockCritico() <= 0) {
                JsfUtil.addErrorMessage("El stock critico debe ser mayor a cero");
                return;
            }

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usu);
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setUsuarioAlta(usu);
            getSelected().setEmpresa(usu.getEmpresa());

            jakarta.persistence.Query query = ejbFacade.getEntityManager().createNativeQuery(
                    "select ifnull(max(CONVERT(codigo,SIGNED INTEGER)),0) as VALOR from articulos;", NroMesaEntrada.class);

            NroMesaEntrada cod = (NroMesaEntrada) query.getSingleResult();

            getSelected().setCodigo(String.valueOf(cod.getCodigo() + 1));

            super.saveNew(event);

            setSelected(ejbFacade.getEntityManager().createNamedQuery("Articulos.findByCodigo", Articulos.class).setParameter("codigo", String.valueOf(cod.getCodigo() + 1)).getSingleResult());

            // requestContext = RequestContext.getCurrentInstance();
            //requestContext.execute("PF('wDatalist').update()");
        }

    }
}
