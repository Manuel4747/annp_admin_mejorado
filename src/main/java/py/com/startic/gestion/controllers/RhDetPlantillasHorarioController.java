package py.com.startic.gestion.controllers;


import py.com.startic.gestion.models.RhDetPlantillasHorario;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;

@Named(value = "rhDetPlantillasHorarioController")
@ViewScoped
public class RhDetPlantillasHorarioController extends AbstractController<RhDetPlantillasHorario> {

    @Inject
    private RhPlantillasHorarioController plantillaHorarioController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;
    @Inject
    private EmpresasController empresaController;
    @Inject
    private UsuariosController usuarioAltaController;

    public RhDetPlantillasHorarioController() {
        // Inform the Abstract parent controller of the concrete RhDetPlantillasHorario Entity
        super(RhDetPlantillasHorario.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        plantillaHorarioController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
        empresaController.setSelected(null);
        usuarioAltaController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the RhPlantillasHorario controller in
     * order to display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void preparePlantillaHorario(ActionEvent event) {
        if (this.getSelected() != null && plantillaHorarioController.getSelected() == null) {
            plantillaHorarioController.setSelected(this.getSelected().getPlantillaHorario());
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
}
