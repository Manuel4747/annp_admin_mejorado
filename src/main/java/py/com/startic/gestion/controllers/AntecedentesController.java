package py.com.startic.gestion.controllers;

import java.util.Collection;
import java.util.Date;

import py.com.startic.gestion.models.Antecedentes;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import py.com.startic.gestion.models.Empresas;

@Named(value = "antecedentesController")
@ViewScoped
public class AntecedentesController extends AbstractController<Antecedentes> {

    @Inject
    private EmpresasController empresaController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;

    public AntecedentesController() {
        // Inform the Abstract parent controller of the concrete Antecedentes Entity
        super(Antecedentes.class);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        empresaController.setSelected(null);
        usuarioAltaController.setSelected(null);
        usuarioUltimoEstadoController.setSelected(null);
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
    
    @Override
    public Collection<Antecedentes> getItems() {
        setItems(this.ejbFacade.getEntityManager().createNamedQuery("Antecedentes.findAll", Antecedentes.class).getResultList());
        return super.getItems2();
    }

    @Override
    public void save(ActionEvent event) {

        if (getSelected() != null) {

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
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


            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setEmpresa(new Empresas(1));

            super.saveNew(event);

        }

    }
}
