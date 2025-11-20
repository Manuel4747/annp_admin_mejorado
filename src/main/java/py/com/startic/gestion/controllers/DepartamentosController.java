package py.com.startic.gestion.controllers;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import jakarta.annotation.PostConstruct;

import py.com.startic.gestion.models.Departamentos;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.models.Estados;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "departamentosController")
@ViewScoped
public class DepartamentosController extends AbstractController<Departamentos> {

    @Inject
    private EmpresasController empresaController;
    @Inject
    private UsuariosController usuarioAltaController;
    @Inject
    private UsuariosController usuarioUltimoEstadoController;

    private List<Departamentos> listaDepartamentosActivos;
    private List<Estados> listaEstados;
    private List<Estados> listaEstadosAIncluir;
    private Usuarios usuario;

    public List<Estados> getListaEstados() {
        return listaEstados;
    }

    public void setListaEstados(List<Estados> listaEstados) {
        this.listaEstados = listaEstados;
    }

    public List<Estados> getListaEstadosAIncluir() {
        return listaEstadosAIncluir;
    }

    public void setListaEstadosAIncluir(List<Estados> listaEstadosAIncluir) {
        this.listaEstadosAIncluir = listaEstadosAIncluir;
    }

    public List<Departamentos> getListaDepartamentosActivos() {
        return listaDepartamentosActivos;
    }

    public void setListaDepartamentosActivos(List<Departamentos> listaDepartamentosActivos) {
        this.listaDepartamentosActivos = listaDepartamentosActivos;
    }

    public DepartamentosController() {
        // Inform the Abstract parent controller of the concrete Departamentos Entity
        super(Departamentos.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        usuario = (Usuarios) session.getAttribute("Usuarios");
        
        listaEstados = ejbFacade.getEntityManager().createNamedQuery("Estados.findAll", Estados.class).getResultList();
        listaEstadosAIncluir = ejbFacade.getEntityManager().createNamedQuery("Estados.findAll", Estados.class).getResultList();

        buscar();
    }

    @Override
    public Collection<Departamentos> getItems() {
        return super.getItems2();
    }
    
    public void buscar(){
        setItems(ejbFacade.getEntityManager().createNamedQuery("Departamentos.findByEstados", Departamentos.class).setParameter("estados", listaEstadosAIncluir).getResultList());
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
    }

    public Departamentos prepareCreate() {
        Departamentos dpto = super.prepareCreate(null);

        listaDepartamentosActivos = ejbFacade.getEntityManager().createNamedQuery("Departamentos.findByEstado", Departamentos.class).setParameter("estado", new Estados(Constantes.ESTADO_USUARIO_AC)).getResultList();

        return dpto;
    }

    public void prepareEdit() {

        if (getSelected() != null) {

            listaDepartamentosActivos = ejbFacade.getEntityManager().createNamedQuery("Departamentos.findByEstado", Departamentos.class).setParameter("estado", new Estados(Constantes.ESTADO_USUARIO_AC)).getResultList();

            if (getSelected().getDepartamentoPadre() != null) {
                boolean encontro = false;
                for (Departamentos dpto : listaDepartamentosActivos) {
                    if (dpto.equals(getSelected().getDepartamentoPadre())) {
                        encontro = true;
                    }
                }
                if (!encontro) {
                    listaDepartamentosActivos.add(getSelected().getDepartamentoPadre());
                }
            }

        }
    }

    @Override
    public void save(ActionEvent event) {

        if (getSelected() != null) {

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
        }

        super.save(event);
        
        buscar();
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
            List<Estados> estados = ejbFacade.getEntityManager().createNamedQuery("Estados.findByCodigo", Estados.class).setParameter("codigo", Constantes.ESTADO_USUARIO_AC).getResultList();
            if (estados.isEmpty()) {
                JsfUtil.addErrorMessage("No se ha encontrado estado activo");
                return;
            }

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setUsuarioAlta(usuario);
            getSelected().setEmpresa(usuario.getEmpresa());
            getSelected().setEstado(estados.get(0));

            super.saveNew(event);
            
            buscar();

        }

    }
}
