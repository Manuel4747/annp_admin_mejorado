package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.MovimientosReparacionBienes;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "movimientosReparacionBienesController")
@ViewScoped
public class MovimientosReparacionBienesController extends AbstractController<MovimientosReparacionBienes> {

    @Inject
    private BienesController bienController;
    @Inject
    private EmpresasController empresaController;
    @Inject
    private ProveedoresController proveedorController;
    @Inject
    private TiposMovimientoController tiposMovimientoController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;

    public MovimientosReparacionBienesController() {
        // Inform the Abstract parent controller of the concrete MovimientosReparacionBienes Entity
        super(MovimientosReparacionBienes.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        bienController.setSelected(null);
        empresaController.setSelected(null);
        proveedorController.setSelected(null);
        tiposMovimientoController.setSelected(null);
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the Bienes controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareBien(ActionEvent event) {
        if (this.getSelected() != null && bienController.getSelected() == null) {
            bienController.setSelected(this.getSelected().getBien());
        }
    }

    /**
     * Sets the "selected" attribute of the Empresas controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEmpresa(ActionEvent event) {
        if (this.getSelected() != null && empresaController.getSelected() == null) {
            empresaController.setSelected(this.getSelected().getEmpresa());
        }
    }

    /**
     * Sets the "selected" attribute of the Proveedores controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareProveedor(ActionEvent event) {
        if (this.getSelected() != null && proveedorController.getSelected() == null) {
            proveedorController.setSelected(this.getSelected().getProveedor());
        }
    }

    /**
     * Sets the "selected" attribute of the TiposMovimiento controller in order
     * to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareTiposMovimiento(ActionEvent event) {
        if (this.getSelected() != null && tiposMovimientoController.getSelected() == null) {
            tiposMovimientoController.setSelected(this.getSelected().getTiposMovimiento());
        }
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
