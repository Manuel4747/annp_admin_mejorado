package py.com.startic.gestion.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.Application;
import jakarta.faces.context.FacesContext;
//import jakarta.faces.el.ValueBinding;

import py.com.startic.gestion.models.DespachosPersona;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;
import org.primefaces.PrimeFaces;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.models.LocalidadesPersona;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "despachosPersonaController")
@ViewScoped
public class DespachosPersonaController extends AbstractController<DespachosPersona> {

    @Inject
    private CargosPersonaController cargoPersonaController;
    @Inject
    private EmpresasController empresaController;
    private HttpSession session;
    private Usuarios usuario;
    private String pantalla;
    private String updateCerrar;
    private String nombreVariable;
    private List <LocalidadesPersona> listaLocalidadesPersona;
    private DespachosPersona despachoPersonaOriginal;

    public List<LocalidadesPersona> getListaLocalidadesPersona() {
        return listaLocalidadesPersona;
    }

    public void setListaLocalidadesPersona(List<LocalidadesPersona> listaLocalidadesPersona) {
        this.listaLocalidadesPersona = listaLocalidadesPersona;
    }

    public DespachosPersonaController() {
        // Inform the Abstract parent controller of the concrete DespachosPersona Entity
        super(DespachosPersona.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        usuario = (Usuarios) session.getAttribute("Usuarios");

        refrescar();
    }
    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        cargoPersonaController.setSelected(null);
        empresaController.setSelected(null);
    }


    /**
     * Sets the "selected" attribute of the Empresas controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareEmpresa(ActionEvent event) {
        DespachosPersona selected = this.getSelected();
        if (selected != null && empresaController.getSelected() == null) {
            empresaController.setSelected(selected.getEmpresa());
        }
    }
    
    public void actualizarListaLocalidades(){
        if(getSelected() != null){
            if(getSelected().getDepartamentoPersona() != null){
                listaLocalidadesPersona = ejbFacade.getEntityManager().createNamedQuery("LocalidadesPersona.findByDepartamentoPersona", LocalidadesPersona.class).setParameter("departamentoPersona", getSelected().getDepartamentoPersona()).getResultList();
            }else{
                listaLocalidadesPersona = new ArrayList<>();
            }        
        }
    }

    @Override
    public DespachosPersona prepareCreate(ActionEvent event) {
        DespachosPersona doc = super.prepareCreate(event);
        listaLocalidadesPersona = new ArrayList<>();
        return doc;
    }

    public void prepareEdit() {
        despachoPersonaOriginal = new DespachosPersona(getSelected());
        actualizarListaLocalidades();
    }

    public String abrirCreate(String desde, String updateCerrar, String nombreVariable) {
        pantalla = Constantes.NO;
        if (Constantes.ABIERTO_DESDE_PANTALLA.equals(desde)) {
            pantalla = Constantes.SI;
            this.updateCerrar = updateCerrar;
            this.nombreVariable = nombreVariable;
        }

        prepareCreate(null);
        PrimeFaces current = PrimeFaces.current();
        current.executeScript("PF('DespachosPersonaCreateDialog').show();");
        return "";
    }

    public String updateCreate() {
        if (getSelected() != null) {
            if (Constantes.SI.equals(pantalla)) {
                return updateCerrar;
            } else {
                return "display,:DespachosPersonaListForm:datalist,:growl";
            }
        }
        return "";
    }

    
    public void refrescar(){
        setItems(this.ejbFacade.getEntityManager().createNamedQuery("DespachosPersona.findAll", DespachosPersona.class).getResultList());
    }

    @Override
    public Collection<DespachosPersona> getItems() {
        return super.getItems2();
    }
    public void saveNew() {
        if (getSelected() != null) {
            
            if(getSelected().getDescripcion() == null){
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDespachosPersonaHelpText_descripcion"));
                return;
            }else if("".equals(getSelected().getDescripcion())){
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDespachosPersonaHelpText_descripcion"));
                return;
            }
            /*
            if(getSelected().getDepartamentoPersona() == null){
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDespachosPersonaHelpText_departamentoPersona"));
                return;
            }
            
            if(getSelected().getLocalidadPersona() == null){
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDespachosPersonaHelpText_localidadPersona"));
                return;
            }
*/
            List<DespachosPersona> listaPer = ejbFacade.getEntityManager().createNamedQuery("DespachosPersona.findByDescripcion", DespachosPersona.class).setParameter("descripcion", getSelected().getDescripcion().trim()).getResultList();
            if (listaPer != null) {
                if (!listaPer.isEmpty()) {
                    JsfUtil.addErrorMessage("Ya existe un cargo con la descripcion " + getSelected().getDescripcion().trim());
                    return;
                }
            }
            
            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setUsuarioAlta(usuario);
            getSelected().setEmpresa(usuario.getEmpresa());

            super.saveNew(null);
            
            if (!this.isErrorPersistencia()) {
                if (Constantes.SI.equals(pantalla)) {
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(nombreVariable, getSelected());
                }
            }
            // Parte solo aplicable cuando la pantalla Create Comprobante Egreso es llamada desde la pantalla Edit Viajes
            FacesContext context = FacesContext.getCurrentInstance();
            Application application = context.getApplication();
            //ValueBinding binding = application.createValueBinding("#{personasController}");
            //PersonasController personasController = (PersonasController) binding.getValue(context);
            PersonasController personasController = application.evaluateExpressionGet(context, "#{personasController}", PersonasController.class);
            
            if (personasController != null) {
                personasController.actualizarDptoYLocalidadEnCreate(getSelected());
            }

            refrescar();

        }
    }

    public void save() {
        if (getSelected() != null) {
            
            if(getSelected().getDescripcion() == null){
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDespachosPersonaHelpText_descripcion"));
                return;
            }else if("".equals(getSelected().getDescripcion())){
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDespachosPersonaHelpText_descripcion"));
                return;
            }
            /*
            if(getSelected().getDepartamentoPersona() == null){
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDespachosPersonaHelpText_departamentoPersona"));
                return;
            }
            
            if(getSelected().getLocalidadPersona() == null){
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditDespachosPersonaHelpText_localidadPersona"));
                return;
            }
*/
            if (!despachoPersonaOriginal.getDescripcion().equals(getSelected().getDescripcion())) {
                List<DespachosPersona> listaPer = ejbFacade.getEntityManager().createNamedQuery("DespachosPersona.findByDescripcion", DespachosPersona.class).setParameter("descripcion", getSelected().getDescripcion().trim()).getResultList();
                if (listaPer != null) {
                    if (!listaPer.isEmpty()) {
                        JsfUtil.addErrorMessage("Ya existe un cargo con la descripcion " + getSelected().getDescripcion().trim());
                        getSelected().setDescripcion(despachoPersonaOriginal.getDescripcion());
                        return;
                    }
                }
            }
            
            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);

            super.save(null);

            refrescar();

        }
    }

}
