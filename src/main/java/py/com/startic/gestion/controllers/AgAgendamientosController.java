package py.com.startic.gestion.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import jakarta.annotation.PostConstruct;

import py.com.startic.gestion.models.AgAgendamientos;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.datasource.RepAgendamientos;
import py.com.startic.gestion.models.AgDepartamentosPorAgendamientos;
import py.com.startic.gestion.models.AgDetallesAgendamiento;
import py.com.startic.gestion.models.Bienes;
import py.com.startic.gestion.models.Departamentos;
import py.com.startic.gestion.models.Estados;
import py.com.startic.gestion.models.LocalidadesPersona;
import py.com.startic.gestion.models.PersonasAgendamiento;
import py.com.startic.gestion.models.RepBienes;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "agendamientosController")
@ViewScoped
public class AgAgendamientosController extends AbstractController<AgAgendamientos> {

    @Inject
    private PersonasAgendamientoController personasController;
    @Inject
    private AgDetallesAgendamientoController detalleAgendamientosController;
    @Inject
    private AgDepartamentosPorAgendamientosController departamentosPorAgendamientosController;
    
    private Date fechaDesde;
    private Date fechaHasta;
    private Usuarios usuario;
    private List<Usuarios> listaUsuarios;
    private PersonasAgendamiento personaOrigen;
    private PersonasAgendamiento personaOrigenSelected;
    private String cedula;
    private List<LocalidadesPersona> listaLocalidadesPersona;
    private List<PersonasAgendamiento> listaPersonaOrigen;
    private String promocion;
    private List<AgDetallesAgendamiento> listaDetAgentamiento;
    private Departamentos departamento1;
    private Departamentos departamento2;
    private Departamentos departamento3;
    private Departamentos departamento4;
    private List<Departamentos> listaDepartamentos1;
    private Departamentos dependencia;
    private List<Departamentos> listaDependencias;
    private FiltroURL filtroURL = new FiltroURL();

    public Departamentos getDependencia() {
        return dependencia;
    }

    public void setDependencia(Departamentos dependencia) {
        this.dependencia = dependencia;
    }

    public List<Departamentos> getListaDependencias() {
        return listaDependencias;
    }

    public void setListaDependencias(List<Departamentos> listaDependencias) {
        this.listaDependencias = listaDependencias;
    }

    public Departamentos getDepartamento1() {
        return departamento1;
    }

    public void setDepartamento1(Departamentos departamento1) {
        this.departamento1 = departamento1;
    }

    public Departamentos getDepartamento2() {
        return departamento2;
    }

    public void setDepartamento2(Departamentos departamento2) {
        this.departamento2 = departamento2;
    }

    public Departamentos getDepartamento3() {
        return departamento3;
    }

    public void setDepartamento3(Departamentos departamento3) {
        this.departamento3 = departamento3;
    }

    public Departamentos getDepartamento4() {
        return departamento4;
    }

    public void setDepartamento4(Departamentos departamento4) {
        this.departamento4 = departamento4;
    }

    public List<Departamentos> getListaDepartamentos1() {
        return listaDepartamentos1;
    }

    public void setListaDepartamentos1(List<Departamentos> listaDepartamentos1) {
        this.listaDepartamentos1 = listaDepartamentos1;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public List<Usuarios> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuarios> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public PersonasAgendamiento getPersonaOrigen() {
        return personaOrigen;
    }

    public void setPersonaOrigen(PersonasAgendamiento personaOrigen) {
        this.personaOrigen = personaOrigen;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public List<LocalidadesPersona> getListaLocalidadesPersona() {
        return listaLocalidadesPersona;
    }

    public void setListaLocalidadesPersona(List<LocalidadesPersona> listaLocalidadesPersona) {
        this.listaLocalidadesPersona = listaLocalidadesPersona;
    }

    public List<PersonasAgendamiento> getListaPersonaOrigen() {
        return listaPersonaOrigen;
    }

    public void setListaPersonaOrigen(List<PersonasAgendamiento> listaPersonaOrigen) {
        this.listaPersonaOrigen = listaPersonaOrigen;
    }

    public PersonasAgendamiento getPersonaOrigenSelected() {
        return personaOrigenSelected;
    }

    public void setPersonaOrigenSelected(PersonasAgendamiento personaOrigenSelected) {
        this.personaOrigenSelected = personaOrigenSelected;
    }

    public String getPromocion() {
        return promocion;
    }

    public void setPromocion(String promocion) {
        this.promocion = promocion;
    }

    public List<AgDetallesAgendamiento> getListaDetAgentamiento() {
        return listaDetAgentamiento;
    }

    public void setListaDetAgentamiento(List<AgDetallesAgendamiento> listaDetAgentamiento) {
        this.listaDetAgentamiento = listaDetAgentamiento;
    }

    public AgAgendamientosController() {
        // Inform the Abstract parent controller of the concrete Roles Entity
        super(AgAgendamientos.class);
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        usuario = (Usuarios) session.getAttribute("Usuarios");

        fechaDesde = ejbFacade.getSystemDateOnly(-7);
        fechaHasta = ejbFacade.getSystemDateOnly();

        buscarPorFecha();
    }

    public void buscarPorFecha() {
        if (fechaDesde == null || fechaHasta == null) {
            JsfUtil.addErrorMessage("Debe ingresar Rango de Fechas");
        } else {
            if(filtroURL.verifPermiso("adminAgendamientos")){
                setItems(this.ejbFacade.getEntityManager().createNamedQuery("AgAgendamientos.findByFecha", AgAgendamientos.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).getResultList());
            }else{
                setItems(this.ejbFacade.getEntityManager().createNamedQuery("AgAgendamientos.findByFechaANDDepartamentoDestino", AgAgendamientos.class).setParameter("fechaDesde", fechaDesde).setParameter("fechaHasta", fechaHasta).setParameter("departamentoDestino", usuario.getDepartamento()).getResultList());
            }
        }
    }

    public String obtenerPersonas(AgAgendamientos doc) {
        PersonasAgendamiento per = null;

        String respuesta = "";

        if (doc != null) {
            try {
                List<AgDetallesAgendamiento> listaPersonasActual = ejbFacade.getEntityManager().createNamedQuery("AgDetallesAgendamiento.findByAgendamiento", AgDetallesAgendamiento.class).setParameter("agendamiento", doc).getResultList();
                for (int i = 0; i < listaPersonasActual.size(); i++) {
                    per = listaPersonasActual.get(i).getPersona();
                    if ("".equals(respuesta)) {
                        respuesta = per.getNombresApellidos();
                    } else {
                        respuesta += ", " + per.getNombresApellidos();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return respuesta;
    }

    public AgAgendamientos prepareCreate() {
        AgAgendamientos ag = super.prepareCreate(null);
        
        ag.setFecha(ejbFacade.getSystemDate());
        
        cedula = "";
        promocion = "";
        dependencia = null;
        departamento1 = null;
        departamento2 = null;
        departamento3 = null;
        departamento4 = null;
        personaOrigen = new PersonasAgendamiento();
        listaPersonaOrigen = new ArrayList<>();
        listaUsuarios = ejbFacade.getEntityManager().createNamedQuery("Usuarios.findByEstado", Usuarios.class).setParameter("estado", Constantes.ESTADO_USUARIO_AC).getResultList();
        listaDependencias = ejbFacade.getEntityManager().createNamedQuery("Departamentos.findByEstado", Departamentos.class).setParameter("estado", new Estados(Constantes.ESTADO_USUARIO_AC)).getResultList();
        listaDepartamentos1 = ejbFacade.getEntityManager().createNamedQuery("Departamentos.findByDepartamentoPadreISNULL", Departamentos.class).setParameter("estado", new Estados(Constantes.ESTADO_USUARIO_AC)).getResultList();

        if(!filtroURL.verifPermiso("adminAgendamientos")){
            
            actualizarDepartamentos(usuario.getDepartamento());
            
            if(departamento4 != null){
                dependencia = departamento4;
            }else if(departamento3 != null){
                dependencia = departamento3;
            }else if(departamento2 != null){
                dependencia = departamento2;
            }else if(departamento1 != null){
                dependencia = departamento1;
            }
        }
        
        return ag;
    }
    
    
    
    public void actualizarDepartamentos(Departamentos dpto){
        departamento1 = null;
        departamento2 = null;
        departamento3 = null;
        departamento4 = null;
        List<Departamentos> listaDptos = new ArrayList<>();
        listaDptos.add(dpto);
        Departamentos dptoActual = dpto;
        while(dptoActual != null){
            if(dptoActual.getDepartamentoPadre() != null){
                listaDptos.add(dptoActual.getDepartamentoPadre());
            }
            dptoActual = dptoActual.getDepartamentoPadre();
        }

        int contador = 0;
        for(int i = listaDptos.size() - 1; i >= 0; i--){
            switch (contador) {
                case 0:
                    departamento1 = listaDptos.get(i);
                    break;
                case 1:
                    departamento2 = listaDptos.get(i);
                    break;
                case 2:
                    departamento3 = listaDptos.get(i);
                    break;
                case 3:
                    departamento4 = listaDptos.get(i);
                    break;
                default:
                    break;
            }
            contador++;
            if(contador == 4){
                break;
            }
        }
        
    }
    
    public List<Departamentos> obtenerDepartamentos(Departamentos dpto){
        if(!filtroURL.verifPermiso("adminAgendamientos")){
            return ejbFacade.getEntityManager().createNamedQuery("Departamentos.findByDepartamentoPadre", Departamentos.class).setParameter("departamentoPadre", dpto).setParameter("estado", new Estados(Constantes.ESTADO_USUARIO_AC)).getResultList();
        }else{
            // return ejbFacade.getEntityManager().createNamedQuery("Departamentos.findAll", Departamentos.class).getResultList();
            return ejbFacade.getEntityManager().createNamedQuery("Departamentos.findByEstado", Departamentos.class).setParameter("estado", new Estados(Constantes.ESTADO_USUARIO_AC)).getResultList();
        }
    }

    @Override
    public Collection<AgAgendamientos> getItems() {
        return super.getItems2();
    }

    public void buscarPersona() {
        if (cedula != null) {
            if (!"".equals(cedula)) {
                
                if(listaPersonaOrigen != null){
                    boolean encontro = false;
                    for(PersonasAgendamiento per : listaPersonaOrigen){
                        if(cedula.equals(per.getCi())){
                            encontro = true;
                        }
                    }

                    if(encontro){
                        cedula = "";
                        promocion = "";
                        personaOrigen = null;
                        JsfUtil.addErrorMessage("Esta persona ya fue agregada");
                        return;
                    }
                }
                
                List<PersonasAgendamiento> lista = ejbFacade.getEntityManager().createNamedQuery("PersonasAgendamiento.findByCi", PersonasAgendamiento.class).setParameter("ci", cedula).getResultList();
                if (lista.size() > 0) {
                    personaOrigen = lista.get(0);
                    promocion = personaOrigen.getPromocion();
                    actualizarListaLocalidades();
                } else {
                    Date fecha = ejbFacade.getSystemDate();
                    SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
                    personaOrigen = new PersonasAgendamiento(-1 * Integer.valueOf(format.format(fecha)));
                    promocion = "";
                }
            }else{
                personaOrigen = null;
            }
        }
    }
    
    public boolean deshabilitarDepartamentos(){
        return !filtroURL.verifPermiso("adminAgendamientos");
    }

    public boolean deshabilitarCampos() {
        return (cedula == null)?true:("".equals(cedula));
    }

    public void actualizarListaLocalidades() {
        if (personaOrigen != null) {
            if (personaOrigen.getDepartamentoPersona() != null) {
                listaLocalidadesPersona = ejbFacade.getEntityManager().createNamedQuery("LocalidadesPersona.findByDepartamentoPersona", LocalidadesPersona.class).setParameter("departamentoPersona", personaOrigen.getDepartamentoPersona()).getResultList();
            } else {
                listaLocalidadesPersona = new ArrayList<>();
            }
        }
    }

    public void agregarNuevaPersonaOrigen() {
        if (personaOrigen != null) {
                
            if(cedula == null){
                JsfUtil.addErrorMessage("Debe completar cédula");
                return;
            }else if("".equals(cedula)){
                JsfUtil.addErrorMessage("Debe completar cédula");
                return;
            } 
            
            if(personaOrigen.getNombresApellidos() == null){
                JsfUtil.addErrorMessage("Debe completar Nombre y Apellido");
                return;
            }else if("".equals(personaOrigen.getNombresApellidos())){
                JsfUtil.addErrorMessage("Debe completar Nombre y Apellido");
                return;
            } 
            
            personaOrigen.setPromocion(promocion);
            personaOrigen.setCi(cedula);
            listaPersonaOrigen.add(personaOrigen);
            cedula = "";
            buscarPersona();
            actualizarListaLocalidades();
        }

    }

    public void borrarPersonaOrigen(PersonasAgendamiento personaActual) {

        if (listaPersonaOrigen != null) {

            listaPersonaOrigen.remove(personaActual);

        }
    }
    
    public String datePattern() {
        return "dd/MM/yyyy";
    }

    public String customFormatDate(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern());
            return format.format(date);
        }
        return "";
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        buscarPersonas();
    }
    
    private void buscarPersonas(){
        if(getSelected() != null){
            listaDetAgentamiento = ejbFacade.getEntityManager().createNamedQuery("AgDetallesAgendamiento.findByAgendamiento", AgDetallesAgendamiento.class).setParameter("agendamiento", getSelected()).getResultList();
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
    }
    
    private boolean verifPersonas(PersonasAgendamiento perOri, PersonasAgendamiento per){
        boolean guardar = false;
        
        if((perOri.getCi() == null && per.getCi() != null) || (perOri.getCi() != null && per.getCi() == null)){
            guardar = true;
        }

        if(perOri.getCi() != null && per.getCi() != null){
            if(!perOri.getCi().equals(per.getCi())){
                guardar = true;
            }
        }
        
        if((perOri.getNombresApellidos() == null && per.getNombresApellidos() != null) || (perOri.getNombresApellidos() != null && per.getNombresApellidos() == null)){
            guardar = true;
        }

        if(perOri.getNombresApellidos() != null && per.getNombresApellidos() != null){
            if(!perOri.getNombresApellidos().equals(per.getNombresApellidos())){
                guardar = true;
            }
        }
        
        if((perOri.getEmail() == null && per.getEmail() != null) || (perOri.getEmail() != null && per.getEmail() == null)){
            guardar = true;
        }

        if(perOri.getEmail() != null && per.getEmail() != null){
            if(!perOri.getEmail().equals(per.getEmail())){
                guardar = true;
            }
        }
        
        if((perOri.getTelefono1()== null && per.getTelefono1() != null) || (perOri.getTelefono1() != null && per.getTelefono1() == null)){
            guardar = true;
        }

        if(perOri.getTelefono1() != null && per.getTelefono1() != null){
            if(!perOri.getTelefono1().equals(per.getTelefono1())){
                guardar = true;
            }
        }
        
        if((perOri.getTelefono2()== null && per.getTelefono2() != null) || (perOri.getTelefono2() != null && per.getTelefono2() == null)){
            guardar = true;
        }

        if(perOri.getTelefono2() != null && per.getTelefono2() != null){
            if(!perOri.getTelefono2().equals(per.getTelefono2())){
                guardar = true;
            }
        }
        
        if((perOri.getDespachoPersona()== null && per.getDespachoPersona() != null) || (perOri.getDespachoPersona() != null && per.getDespachoPersona() == null)){
            guardar = true;
        }

        if(perOri.getDespachoPersona() != null && per.getDespachoPersona() != null){
            if(!perOri.getDespachoPersona().equals(per.getDespachoPersona())){
                guardar = true;
            }
        }
        
        if((perOri.getDepartamentoPersona()== null && per.getDepartamentoPersona() != null) || (perOri.getDepartamentoPersona() != null && per.getDepartamentoPersona() == null)){
            guardar = true;
        }

        if(perOri.getDepartamentoPersona() != null && per.getDepartamentoPersona() != null){
            if(!perOri.getDepartamentoPersona().equals(per.getDepartamentoPersona())){
                guardar = true;
            }
        }
        
        if((perOri.getLocalidadPersona()== null && per.getLocalidadPersona() != null) || (perOri.getLocalidadPersona() != null && per.getLocalidadPersona() == null)){
            guardar = true;
        }

        if(perOri.getLocalidadPersona() != null && per.getLocalidadPersona() != null){
            if(!perOri.getLocalidadPersona().equals(per.getLocalidadPersona())){
                guardar = true;
            }
        }
        
        if((perOri.getFechaNacimento()== null && per.getFechaNacimento() != null) || (perOri.getFechaNacimento() != null && per.getFechaNacimento() == null)){
            guardar = true;
        }

        if(perOri.getFechaNacimento() != null && per.getFechaNacimento() != null){
            if(!perOri.getFechaNacimento().equals(per.getFechaNacimento())){
                guardar = true;
            }
        }

        if(perOri.isUniversidadNacional() != per.isUniversidadNacional()){
            guardar = true;
        }
        
        if((perOri.getPromocion() == null && per.getPromocion() != null) || (perOri.getPromocion() != null && per.getPromocion() == null)){
            guardar = true;
        }

        if(perOri.getPromocion() != null && per.getPromocion() != null){
            if(!perOri.getPromocion().equals(per.getPromocion())){
                guardar = true;
            }
        }

        return guardar;
    }
    
    public String obtenerListDepartamentos(AgAgendamientos ag){
        String result = "";
        if(ag != null){
            List<AgDepartamentosPorAgendamientos> da = ejbFacade.getEntityManager().createNamedQuery("AgDepartamentosPorAgendamientos.findByAgendamiento", AgDepartamentosPorAgendamientos.class).setParameter("agendamiento", ag).getResultList();
            for(AgDepartamentosPorAgendamientos dpto : da){
                if(!"".equals(result)){
                    result += " --> ";
                }
                result += dpto.getDepartamento().getNombre();
            }
        }
        
        return result;
    }
    
    public void saveNew() {
        if (getSelected() != null) {
            
            if(listaPersonaOrigen == null){
                JsfUtil.addErrorMessage("Debe agregar personas");
                return;
            }
            
            if(listaPersonaOrigen.isEmpty()){
                JsfUtil.addErrorMessage("Debe agregar personas.");
                return;
            }
            
            if(getSelected().getDescripcion() == null){
                JsfUtil.addErrorMessage("Debe completar descripción");
                return;
            }else if("".equals(getSelected().getDescripcion())){
                JsfUtil.addErrorMessage("Debe completar descripción");
                return;
            } 
            
            if(departamento4 != null){
                getSelected().setDepartamentoDestino(departamento4);
            }else if(departamento3 != null){
                getSelected().setDepartamentoDestino(departamento3);
            }else if(departamento2 != null){
                getSelected().setDepartamentoDestino(departamento2);
            }else if(departamento1 != null){
                getSelected().setDepartamentoDestino(departamento1);
            }else{
                JsfUtil.addErrorMessage("Debe elegir dependencia");
                return;
            }
            

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setUsuarioAlta(usuario);
            
            super.saveNew(null);

            for(PersonasAgendamiento per : listaPersonaOrigen){
                if(per.getId() < 0){
                    per.setId(null);
                    per.setUsuarioAlta(usuario);
                    per.setUsuarioUltimoEstado(usuario);
                    per.setFechaHoraAlta(fecha);
                    per.setFechaHoraUltimoEstado(fecha);
                    per.setEstado(Constantes.ESTADO_USUARIO_AC);
                    
                    personasController.setSelected(per);
                    personasController.saveNew2();
                }else{
                    try{
                        PersonasAgendamiento perOri = ejbFacade.getEntityManager().createNamedQuery("PersonasAgendamiento.findById", PersonasAgendamiento.class).setParameter("id", per.getId()).getSingleResult();
                        
                        if(verifPersonas(perOri, per)){
                            
                            personasController.setSelected(per);
                            
                            personasController.save2();
                        }                        
                        
                    }catch(Exception e){
                        per.setId(null);
                        per.setUsuarioAlta(usuario);
                        per.setUsuarioUltimoEstado(usuario);
                        per.setFechaHoraAlta(fecha);
                        per.setFechaHoraUltimoEstado(fecha);
                        personasController.setSelected(per);
                        personasController.saveNew2();
                    }
                }
                
                AgDetallesAgendamiento det = new AgDetallesAgendamiento();
                
                det.setPersona(per);
                det.setAgendamiento(getSelected());
                
                detalleAgendamientosController.setSelected(det);
                detalleAgendamientosController.saveNew(null);
                
            }
            
            AgDepartamentosPorAgendamientos da1 = null;
            if(departamento1 != null){
                da1 = new AgDepartamentosPorAgendamientos(departamento1, getSelected(), null);
                departamentosPorAgendamientosController.setSelected(da1);
                departamentosPorAgendamientosController.saveNew(null);
            }
            
            AgDepartamentosPorAgendamientos da2 = null;
            if(departamento2 != null){
                da2 = new AgDepartamentosPorAgendamientos(departamento2, getSelected(), da1);
                departamentosPorAgendamientosController.setSelected(da2);
                departamentosPorAgendamientosController.saveNew(null);
            }
            
            AgDepartamentosPorAgendamientos da3 = null;
            if(departamento3 != null){
                da3 = new AgDepartamentosPorAgendamientos(departamento3, getSelected(), da2);
                departamentosPorAgendamientosController.setSelected(da3);
                departamentosPorAgendamientosController.saveNew(null);
            }
            
            AgDepartamentosPorAgendamientos da4 = null;
            if(departamento4 != null){
                da4 = new AgDepartamentosPorAgendamientos(departamento4, getSelected(), da3);
                departamentosPorAgendamientosController.setSelected(da4);
                departamentosPorAgendamientosController.saveNew(null);
            }
            
            
            
            buscarPorFecha();
            buscarPersonas();
        }
    }
    public void pdf() {

        HttpServletResponse httpServletResponse = null;
        try {
            Collection<AgDetallesAgendamiento> terminales = this.ejbFacade.getEntityManager().createNativeQuery("select d.* from ag_detalles_agendamiento as d, ag_agendamientos as a where d.agendamiento = a.id and a.fecha > ?1 and a.fecha < ?2  order by fecha, fecha_hora_alta", AgDetallesAgendamiento.class).setParameter(1, fechaDesde).setParameter(2, fechaHasta).getResultList();

            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            SimpleDateFormat format2 = new SimpleDateFormat("hh:mm:ss");
            SimpleDateFormat format3 = new SimpleDateFormat("yyyy/MM");

            Collection<RepAgendamientos> repBienes = new ArrayList<>();
            RepAgendamientos repBien = null;
            for (AgDetallesAgendamiento bien : terminales) {
                repBien = new RepAgendamientos();
                repBien.setFecha(format.format(bien.getAgendamiento().getFecha()));
                repBien.setMes(format3.format(bien.getAgendamiento().getFecha()));
                repBien.setNombresApellidos(bien.getPersona().getNombresApellidos());
                repBien.setDescripcion(bien.getAgendamiento().getDescripcion());
                repBien.setEmail(bien.getPersona().getEmail());
                repBien.setTelefono1(bien.getPersona().getTelefono1());
                repBien.setCi(bien.getPersona().getCi());
                repBien.setAgendamiento(bien.getAgendamiento().getId());

                repBienes.add(repBien);
            }

            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(repBienes);
            HashMap map = new HashMap();

            Date fecha = ejbFacade.getSystemDate();
            map.put("fecha", format.format(fecha));
            map.put("hora", format2.format(fecha));

            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteAgendamientos.jasper");
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.setContentType("application/pdf");
            httpServletResponse.addHeader("Content-disposition", "filename=reporte.pdf");

            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);

            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            e.printStackTrace();

            if (httpServletResponse != null) {
                if (httpServletResponse.getHeader("Content-disposition") == null) {
                    httpServletResponse.addHeader("Content-disposition", "inline");
                } else {
                    httpServletResponse.setHeader("Content-disposition", "inline");
                }

            }
            JsfUtil.addErrorMessage("No se pudo generar el reporte.");

        }

        ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
    }

    public void xls() {

        HttpServletResponse httpServletResponse = null;
        try {
            Collection<Bienes> terminales = this.ejbFacade.getEntityManager().createNamedQuery("Bienes.findReporte", Bienes.class).getResultList();

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat format2 = new SimpleDateFormat("hh:mm:ss");

            Collection<RepBienes> repBienes = new ArrayList<>();
            RepBienes repBien = null;
            for (Bienes bien : terminales) {
                repBien = new RepBienes();
                repBien.setCostoUnitario(bien.getCostoUnitario());
                repBien.setCuenta(bien.getEspecificacionSubcuenta().getSubcuenta().getCuenta().getCodigo() + " " + bien.getEspecificacionSubcuenta().getSubcuenta().getCuenta().getDescripcion());
                repBien.setSubcuenta(bien.getEspecificacionSubcuenta().getSubcuenta().getCodigo() + " " + bien.getEspecificacionSubcuenta().getSubcuenta().getDescripcion());
                repBien.setFechaAdquisicion(format.format(bien.getFechaAdquisicion()));
                repBien.setObservacion(bien.getUltimaObservacionBien());
                repBien.setRotulado(bien.getRotulado());
                repBien.setOrigen(bien.getOrigenBien().getDescripcion());
                repBien.setVidaUtil(String.valueOf(bien.getEspecificacionSubcuenta().getSubcuenta().getCuenta().getVidaUtil()));

                repBienes.add(repBien);
            }

            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(repBienes);
            HashMap map = new HashMap();

            Date fecha = ejbFacade.getSystemDate();
            map.put("fecha", format.format(fecha));
            map.put("hora", format.format(fecha));

            String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteBienes.jasper");
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            httpServletResponse.addHeader("Content-disposition", "attachment;filename=reporte.xlsx");

            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);

            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            //JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            exporter.exportReport();

            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            e.printStackTrace();

            if (httpServletResponse != null) {
                if (httpServletResponse.getHeader("Content-disposition") == null) {
                    httpServletResponse.addHeader("Content-disposition", "inline");
                } else {
                    httpServletResponse.setHeader("Content-disposition", "inline");
                }

            }
            JsfUtil.addErrorMessage("No se pudo generar el reporte.");

        }

        ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
    }

}
