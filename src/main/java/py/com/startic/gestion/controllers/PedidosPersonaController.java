package py.com.startic.gestion.controllers;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import py.com.startic.gestion.models.PedidosPersona;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.models.DepartamentosPersona;
import py.com.startic.gestion.models.Empresas;
import py.com.startic.gestion.models.LocalidadesPersona;
import py.com.startic.gestion.models.Personas;

@Named(value = "pedidosPersonaController")
@ViewScoped
public class PedidosPersonaController extends AbstractController<PedidosPersona> {
    
    @Inject
    private CargosPersonaController cargoPersonaController;
    @Inject
    private EmpresasController empresaController;
    private HttpSession session;
    private Personas persona;
    private List<LocalidadesPersona> listaLocalidades;
    private DepartamentosPersona departamentoPersona;

    public DepartamentosPersona getDepartamentoPersona() {
        return departamentoPersona;
    }

    public void setDepartamentoPersona(DepartamentosPersona departamentoPersona) {
        this.departamentoPersona = departamentoPersona;
    }

    public List<LocalidadesPersona> getListaLocalidades() {
        return listaLocalidades;
    }

    public void setListaLocalidades(List<LocalidadesPersona> listaLocalidades) {
        this.listaLocalidades = listaLocalidades;
    }
    
    public PedidosPersonaController() {
        // Inform the Abstract parent controller of the concrete PedidosPersona Entity
        super(PedidosPersona.class);
    }
    
    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        
        if (session != null) {
            persona = (Personas) session.getAttribute("Persona");
        }
        prepareCreate(null);
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        cargoPersonaController.setSelected(null);
        empresaController.setSelected(null);
    }

    /**
     * Sets the "selected" attribute of the CargosPersona controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareCargoPersona(ActionEvent event) {
        PedidosPersona selected = this.getSelected();
        if (selected != null && cargoPersonaController.getSelected() == null) {
            cargoPersonaController.setSelected(selected.getCargoPersona());
        }
    }

    /**
     * Sets the "selected" attribute of the Empresas controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEmpresa(ActionEvent event) {
        PedidosPersona selected = this.getSelected();
        if (selected != null && empresaController.getSelected() == null) {
            empresaController.setSelected(selected.getEmpresa());
        }
    }
    
    public boolean deshabilitarLocalidad(){
        if(getSelected() != null){
            if(getSelected().getDepartamentoPersona() != null){
                return false;
            }
        }
        return true;
    }
    
    public void actualizarListaLocalidad(){
        if(getSelected() != null){
            if(getSelected().getDepartamentoPersona() != null){
                listaLocalidades = ejbFacade.getEntityManager().createNamedQuery("LocalidadesPersona.findByDepartamentoPersona", LocalidadesPersona.class).setParameter("departamentoPersona", getSelected().getDepartamentoPersona()).getResultList();
            }
        }
    }
    
    public void prepareEdit() {
    }
    
    @Override
    public PedidosPersona prepareCreate(ActionEvent event) {
        PedidosPersona doc = super.prepareCreate(event);
        return doc;
    }
    
    @Override
    public Collection<PedidosPersona> getItems() {
        return super.getItems2();
    }

    public String saveNew() {
        if (getSelected() != null) {
            Date fecha = ejbFacade.getSystemDate();
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setDepartamentoPersona(departamentoPersona);
            getSelected().setEmpresa(new Empresas(1));
            getSelected().setEstado("AC");
            super.saveNew(null);
            
            if(!this.isErrorPersistencia()){
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/antecedentes/faces/pages/pedidosPersona/Gracias.xhtml");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return "/pages/pedidosPersona/Gracias";
            }
        }
        return "";        
    }
    
    public void save() {
        if (getSelected() != null) {
            
            if (getSelected().getTipoPersona() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditPedidosPersonaHelpText_tipoPersona"));
                return;
            }
            
            if (getSelected().getCargoPersona() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditPedidosPersonaHelpText_cargoPersona"));
                return;
            }
            
            if (getSelected().getCi() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditPedidosPersonaHelpText_ci"));
                return;
            }
            
            if (getSelected().getNombresApellidos() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditPedidosPersonaHelpText_nombresApellidos"));
                return;
            }
            
            super.save(null);
        }
    }
}
