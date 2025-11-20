package py.com.startic.gestion.controllers;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import py.com.startic.gestion.models.Personas;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.xml.bind.DatatypeConverter;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.datasource.RepAntecedentesDocumentosJudiciales;
import py.com.startic.gestion.datasource.RepAntecedentesDocumentosJudicialesRes;
import py.com.startic.gestion.models.Antecedentes;
import py.com.startic.gestion.models.CanalesEntradaDocumentoJudicial;
import py.com.startic.gestion.models.DepartamentosPersona;
import py.com.startic.gestion.models.DespachosPersona;
import py.com.startic.gestion.models.DocumentosJudiciales;
import py.com.startic.gestion.models.Empresas;
import py.com.startic.gestion.models.Estados;
import py.com.startic.gestion.models.LocalidadesPersona;
import py.com.startic.gestion.models.ParametrosSistema;
import py.com.startic.gestion.models.PersonasPorDocumentosJudiciales;
import py.com.startic.gestion.models.ResuelvePorResolucionesPorPersonas;
import py.com.startic.gestion.models.Usuarios;

@Named(value = "personasController")
@ViewScoped
public class PersonasController extends AbstractController<Personas> {

    @Inject
    private CargosPersonaController cargoPersonaController;
    @Inject
    private EmpresasController empresaController;
    @Inject
    private DespachosPersonaController despachoController;
    @Inject
    private AntecedentesController antecedentesController;
    @Inject
    private CanalesEntradaDocumentoJudicialController canalesEntradaDocumentoJudicialController;
    @Inject
    private ResuelvePorResolucionesPorPersonasController resuelvePorResolucionesPorPersonasController;
    private LocalidadesPersona localidadPersona;
    private DepartamentosPersona departamentoPersona;
    private HttpSession session;
    private Usuarios usuario;
    private CanalesEntradaDocumentoJudicial canal;
    private ParametrosSistema par;
    private DespachosPersona despachoPersona;
    private List<LocalidadesPersona> listaLocalidadesPersona;
    private final String borrador = "SI";
    private final String titulo = "BORRADOR";
    private final boolean imprimirQR = false;
    private final boolean valido = false;
    private final FiltroURL filtroURL = new FiltroURL();
    private Personas personaOriginal;
    private String endpoint;

    public PersonasController() {
        // Inform the Abstract parent controller of the concrete Personas Entity
        super(Personas.class);
    }

    public LocalidadesPersona getLocalidadPersona() {
        return localidadPersona;
    }

    public void setLocalidadPersona(LocalidadesPersona localidadPersona) {
        this.localidadPersona = localidadPersona;
    }

    public DepartamentosPersona getDepartamentoPersona() {
        return departamentoPersona;
    }

    public void setDepartamentoPersona(DepartamentosPersona departamentoPersona) {
        this.departamentoPersona = departamentoPersona;
    }

    public DespachosPersona getDespachoPersona() {
        if (session.getAttribute("despachoPersonaSelected") != null) {
            despachoPersona = (DespachosPersona) session.getAttribute("despachoPersonaSelected");
            session.removeAttribute("despachoPersonaSelected");
        }
        return despachoPersona;
    }

    public void setDespachoPersona(DespachosPersona despachoPersona) {
        this.despachoPersona = despachoPersona;
    }

    public List<LocalidadesPersona> getListaLocalidadesPersona() {
        return listaLocalidadesPersona;
    }

    public void setListaLocalidadesPersona(List<LocalidadesPersona> listaLocalidadesPersona) {
        this.listaLocalidadesPersona = listaLocalidadesPersona;
    }

    @PostConstruct
    @Override
    public void initParams() {
        super.initParams();
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        usuario = (Usuarios) session.getAttribute("Usuarios");
        canal = canalesEntradaDocumentoJudicialController.prepareCreate(null);
        canal.setCodigo(Constantes.CANAL_ENTRADA_DOCUMENTO_JUDICIAL_SE);
        try {
            par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID).getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

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
     * Sets the "selected" attribute of the CargosPersona controller in order to
     * display its data in its View dialog.
     *
     * @param event Event object for the widget that triggered an action
     */
    public void prepareCargoPersona(ActionEvent event) {
        Personas selected = this.getSelected();
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
        Personas selected = this.getSelected();
        if (selected != null && empresaController.getSelected() == null) {
            empresaController.setSelected(selected.getEmpresa());
        }
    }

    public void prepareEdit() {
        departamentoPersona = null;
        localidadPersona = null;
        // actualizarDptoYLocalidadEnCreate();
        departamentoPersona = getSelected().getDepartamentoPersona();
        localidadPersona = getSelected().getLocalidadPersona();
        actualizarListaLocalidades();

        if (getSelected() != null) {
            personaOriginal = new Personas(getSelected());
            despachoPersona = getSelected().getDespachoPersona();
        }
    }

    @Override
    public Personas prepareCreate(ActionEvent event) {
        Personas doc = super.prepareCreate(event);
        departamentoPersona = null;
        localidadPersona = null;
        despachoPersona = null;
        listaLocalidadesPersona = new ArrayList<>();
        doc.setHabilitarAntecedentes(true);
        return doc;
    }

    public void actualizarDptoYLocalidadEnEdit() {
        actualizarDptoYLocalidadEnCreate(despachoPersona);
    }

    public void actualizarDptoYLocalidadEnEdit(DespachosPersona despachoPersona) {
        if (getSelected() != null) {
            if (despachoPersona != null) {
                departamentoPersona = despachoPersona.getDepartamentoPersona();
                localidadPersona = despachoPersona.getLocalidadPersona();
            } else {
                departamentoPersona = getSelected().getDepartamentoPersona();
                localidadPersona = getSelected().getLocalidadPersona();
            }

            actualizarListaLocalidades();
        }
    }

    public void actualizarListaLocalidades() {
        if (departamentoPersona != null) {
            listaLocalidadesPersona = ejbFacade.getEntityManager().createNamedQuery("LocalidadesPersona.findByDepartamentoPersona", LocalidadesPersona.class).setParameter("departamentoPersona", departamentoPersona).getResultList();
        } else {
            listaLocalidadesPersona = new ArrayList<>();
        }
    }

    public void actualizarDptoYLocalidadEnCreate() {
        actualizarDptoYLocalidadEnCreate(despachoPersona);
    }

    public void actualizarDptoYLocalidadEnCreate(DespachosPersona despachoPersona) {
        if (getSelected() != null) {
            if (despachoPersona != null) {
                departamentoPersona = despachoPersona.getDepartamentoPersona();
                localidadPersona = despachoPersona.getLocalidadPersona();
            } else {
                departamentoPersona = getSelected().getDepartamentoPersona();
                localidadPersona = getSelected().getLocalidadPersona();
            }

            actualizarListaLocalidades();
        }
    }

    public void refrescar() {
        setItems(this.ejbFacade.getEntityManager().createNamedQuery("Personas.findAll", Personas.class).getResultList());
    }

    @Override
    public Collection<Personas> getItems() {
        return super.getItems2();
    }

    public boolean deshabilitarHabilitarAntecedentes() {
        //if (getSelected() != null) {
            if (filtroURL.verifPermiso(Constantes.PERMITIR_HABILITAR_ANTECEDENTES)) {
                return false;
            }
        //}

        return true;
    }

    public boolean deshabilitarRepAntecedentes() {
        /*
        if(getSelected() != null){
            if(filtroURL.verifPermiso(Constantes.IMPRIMIR_REP_ANTECEDENTES)){
                return false;
            }
        }
        
        return true;
         */
        return getSelected() == null;
    }

    @Override
    public void delete(ActionEvent event) {

        if (getSelected() != null) {
            List<PersonasPorDocumentosJudiciales> lista1 = ejbFacade.getEntityManager().createNamedQuery("PersonasPorDocumentosJudiciales.findByPersonaEstado", PersonasPorDocumentosJudiciales.class).setParameter("persona", getSelected().getId()).setParameter("estado", new Estados("AC")).getResultList();
            boolean encontro = false;
            for (PersonasPorDocumentosJudiciales res : lista1) {
                encontro = true;
                break;
            }

            if (encontro) {
                JsfUtil.addErrorMessage("La persona tiene expedientes asociados.");
                return;
            }

            List<ResuelvePorResolucionesPorPersonas> lista = ejbFacade.getEntityManager().createNamedQuery("ResuelvePorResolucionesPorPersonas.findByPersonaEstado", ResuelvePorResolucionesPorPersonas.class).setParameter("persona", getSelected()).setParameter("estado", new Estados("AC")).getResultList();
            encontro = false;
            for (ResuelvePorResolucionesPorPersonas res : lista) {
                encontro = true;
                break;
            }

            if (encontro) {
                JsfUtil.addErrorMessage("La persona tiene resoluciones asociadas.");
                return;
            }

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setEstado("IN");
            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);

            super.save(null);

            refrescar();
        }
    }
    
    public void saveNew2() {
        super.saveNew(null);
    }
    
    public void save2() {
        super.save(null);
    }

    public void saveNew() {
        if (getSelected() != null) {

            if (departamentoPersona == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreatePersonasHelpText_departamentoPersona"));
                return;
            }

            if (localidadPersona == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreatePersonasHelpText_localidadPersona"));
                return;
            }
            /*
            if (getSelected().getTipoPersona() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreatePersonasHelpText_tipoPersona"));
                return;
            }
             */
 /*
            if (getSelected().getCargoPersona() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreatePersonasHelpText_cargoPersona"));
                return;
            }
             */
 /*
            if (getSelected().getCi() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreatePersonasHelpText_ci"));
                return;
            }
             */
            if (getSelected().getNombresApellidos() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreatePersonasHelpText_nombresApellidos"));
                return;
            }else if ("".equals(getSelected().getNombresApellidos())) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("CreatePersonasHelpText_nombresApellidos"));
                return;
            }

            if (getSelected().getCi() != null) {
                if (!"".equals(getSelected().getCi())) {
                    List<Personas> listaPer = ejbFacade.getEntityManager().createNamedQuery("Personas.findByCiEstado", Personas.class).setParameter("ci", getSelected().getCi()).setParameter("estado", "AC").getResultList();
                    if (listaPer != null) {
                        if (!listaPer.isEmpty()) {
                            JsfUtil.addErrorMessage("Ya existe una persona con cedula " + getSelected().getCi());
                            return;
                        }
                    }
                }
            }

            List<Personas> listaPer = ejbFacade.getEntityManager().createNamedQuery("Personas.findByNombresApellidosEstado", Personas.class).setParameter("nombresApellidos", getSelected().getNombresApellidos().trim()).setParameter("estado", "AC").getResultList();
            if (listaPer != null) {
                if (!listaPer.isEmpty()) {
                    JsfUtil.addErrorMessage("Ya existe una persona con el nombre " + getSelected().getNombresApellidos().trim());
                    return;
                }
            }

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setUsuarioAlta(usuario);
            getSelected().setEmpresa(usuario.getEmpresa());
            getSelected().setDepartamentoPersona(departamentoPersona);
            getSelected().setLocalidadPersona(localidadPersona);
            getSelected().setDespachoPersona(despachoPersona);
            getSelected().setEstado("AC");

            super.saveNew(null);
/*
            if (getSelected().getDespachoPersona() != null) {
                if (!departamentoPersona.equals(getSelected().getDespachoPersona().getDepartamentoPersona())
                        || !localidadPersona.equals(getSelected().getDespachoPersona().getLocalidadPersona())) {

                    DespachosPersona despacho = ejbFacade.getEntityManager().createNamedQuery("DespachosPersona.findById", DespachosPersona.class).setParameter("id", getSelected().getDespachoPersona().getId()).getSingleResult();

                    despacho.setDepartamentoPersona(departamentoPersona);
                    despacho.setLocalidadPersona(localidadPersona);

                    despachoController.setSelected(despacho);
                    despachoController.save(null);

                }
            }
*/
            refrescar();

        }
    }

    public void save() {
        if (getSelected() != null) {

            if (departamentoPersona == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditPersonasHelpText_departamentoPersona"));
                return;
            }

            if (localidadPersona == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditPersonasHelpText_localidadPersona"));
                return;
            }
            /*
            if (getSelected().getTipoPersona() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditPersonasHelpText_tipoPersona"));
                return;
            }
             */
 /*
            if (getSelected().getCargoPersona() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditPersonasHelpText_cargoPersona"));
                return;
            }
             */
 /*
            if (getSelected().getCi() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditPersonasHelpText_ci"));
                return;
            }
             */
            if (getSelected().getNombresApellidos() == null) {
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Mensajes").getString("EditPersonasHelpText_nombresApellidos"));
                return;
            }

            boolean ciOriginalNulo = false;

            if (personaOriginal != null) {
                if (personaOriginal.getCi() != null) {
                    if ("".equals(personaOriginal.getCi()) || "NULL".equals(personaOriginal.getCi())) {
                        ciOriginalNulo = true;
                    }
                } else {
                    ciOriginalNulo = true;
                }
            }

            if (getSelected().getCi() != null) {
                if (!"".equals(getSelected().getCi()) && !"NULL".equals(getSelected().getCi())) {
                    //if (!ciOriginalNulo) {
                        if (!getSelected().getCi().equals(personaOriginal.getCi())) {
                            List<Personas> listaPer = ejbFacade.getEntityManager().createNamedQuery("Personas.findByCiEstado", Personas.class).setParameter("ci", getSelected().getCi()).setParameter("estado", "AC").getResultList();
                            if (listaPer != null) {
                                if (!listaPer.isEmpty()) {
                                    JsfUtil.addErrorMessage("Ya existe una persona con cedula " + getSelected().getCi());
                                    getSelected().setCi(personaOriginal.getCi());
                                    return;
                                }
                            }
                        }
                    //}
                } else if (!ciOriginalNulo) {
                    JsfUtil.addErrorMessage("Debe cargar la cedula");
                    return;
                }
            } else if (!ciOriginalNulo) {
                JsfUtil.addErrorMessage("Debe cargar la cedula");
                return;
            }

            if (!"".equals(getSelected().getNombresApellidos())) {
                if (!personaOriginal.getNombresApellidos().equals(getSelected().getNombresApellidos())) {
                    List<Personas> listaPer = ejbFacade.getEntityManager().createNamedQuery("Personas.findByNombresApellidosEstado", Personas.class).setParameter("nombresApellidos", getSelected().getNombresApellidos().trim()).setParameter("estado", "AC").getResultList();
                    if (listaPer != null) {
                        if (!listaPer.isEmpty()) {
                            JsfUtil.addErrorMessage("Ya existe una persona con el nombre " + getSelected().getNombresApellidos().trim());
                            getSelected().setNombresApellidos(personaOriginal.getNombresApellidos());
                            return;
                        }
                    }
                }
            }else{
                JsfUtil.addErrorMessage("Debe Ingresar Nombres y Apellidos");
                return; 
            }

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usuario);
            getSelected().setDepartamentoPersona(departamentoPersona);
            getSelected().setLocalidadPersona(localidadPersona);
            getSelected().setDespachoPersona(despachoPersona);

            super.save(null);
/*
            if (getSelected().getDespachoPersona() != null) {
                if (!departamentoPersona.equals(getSelected().getDespachoPersona().getDepartamentoPersona())
                        || !localidadPersona.equals(getSelected().getDespachoPersona().getLocalidadPersona())) {

                    DespachosPersona despacho = ejbFacade.getEntityManager().createNamedQuery("DespachosPersona.findById", DespachosPersona.class).setParameter("id", getSelected().getDespachoPersona().getId()).getSingleResult();

                    despacho.setDepartamentoPersona(departamentoPersona);
                    despacho.setLocalidadPersona(localidadPersona);

                    despachoController.setSelected(despacho);
                    despachoController.save(null);

                }
            }
            */

            refrescar();

        }
    }

    public String generarCodigoArchivo() {
        Random aleatorio;
        int valor = 0;
        String pin = "";
        aleatorio = new Random(System.currentTimeMillis());
        valor = aleatorio.nextInt(999999);
        pin = String.valueOf(valor);
        while (pin.trim().length() < 6) {
            pin = '0' + pin;
        }
        return pin;
    }
    
    public void imprimirRepAntecedentesViejo(Personas persona) {
        HttpServletResponse httpServletResponse = null;
        try {

            ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
            
            List<RepAntecedentesDocumentosJudiciales> listaDenuncias = null;
            List<RepAntecedentesDocumentosJudiciales> listaAcusaciones = null;
            List<RepAntecedentesDocumentosJudiciales> listaPreliminar = null;
            List<RepAntecedentesDocumentosJudiciales> listaEnjuiciamientos = null;
            List<RepAntecedentesDocumentosJudiciales> listaFiniquitados = null;
            List<RepAntecedentesDocumentosJudiciales> listaAntecedentesCSJ = null;
            List<RepAntecedentesDocumentosJudiciales> listaInformacionSumaria = null;
            List<RepAntecedentesDocumentosJudiciales> listaPedidoDesafuero = null;
            RepAntecedentesDocumentosJudiciales datoRep = null;

            Date fecha = ejbFacade.getSystemDate();

            // DateFormat format = new SimpleDateFormat("%d 'de' %M 'del' %Y");
            if (persona == null) {
                JsfUtil.addErrorMessage("Debe seleccionar una persona");
                return;
            }

            // List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_DENUNCIA).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            //List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and d.canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r, resoluciones as e where r.resolucion = e.id and e.documento_judicial = p.documento_judicial and r.persona = p.persona and r.estado = 'AC' and r.resuelve in (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_DENUNCIA).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),true)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_DENUNCIA).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_DENUNCIA).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaDenuncias == null) {
                    listaDenuncias = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Denuncia:".toUpperCase());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_DENUNCIA) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }

                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaDenuncias.add(datoRep);
            }

            HashMap map = new HashMap();
            // listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_ACUSACION).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            // listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ACUSACION).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),true)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_ACUSACION).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_ACUSACION).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaAcusaciones == null) {
                    listaAcusaciones = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Acusacion:".toUpperCase());
                // datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_ACUSACION) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }

                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaAcusaciones.add(datoRep);
            }
            // listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),true)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaPreliminar == null) {
                    listaPreliminar = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Investigacion Preliminar:".toUpperCase());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaPreliminar.add(datoRep);
            }
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),true)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaAntecedentesCSJ == null) {
                    listaAntecedentesCSJ = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Antecedentes C.S.J.:".toUpperCase());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaAntecedentesCSJ.add(datoRep);
            }
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),true)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_INFORMACION_SUMARIA).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_INFORMACION_SUMARIA).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaInformacionSumaria == null) {
                    listaInformacionSumaria = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Informacion Sumaria:".toUpperCase());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_INFORMACION_SUMARIA) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaInformacionSumaria.add(datoRep);
            }
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),true)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_PEDIDO_DESAFUERO).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_PEDIDO_DESAFUERO).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaPedidoDesafuero == null) {
                    listaPedidoDesafuero = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("");
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_PEDIDO_DESAFUERO) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaPedidoDesafuero.add(datoRep);
            }
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) >= 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7) and r.fecha_hora_ultimo_estado in (select max(fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o where r.persona = o.persona and r.resolucion = o.resolucion and o.estado = 'AC' ))", DocumentosJudiciales.class).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and d.canal_entrada_documento_judicial = ?1\n"
                    + "and p.estado = ?2\n"
                    + "and p.persona = ?3\n"
                    + "and ifnull((select e.tipo_resuelve = ?4\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?7\n"
                    + "	and r.estado = ?5 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?8 and o.estado = ?6 )),false)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, canal.getCodigo()).
                    setParameter(2, "AC").
                    setParameter(3, persona.getId()).
                    setParameter(4, Constantes.TIPO_RESUELVE_ENJUICIAMIENTO).
                    setParameter(5, "AC").
                    setParameter(6, "AC").
                    setParameter(7, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaEnjuiciamientos == null) {
                    listaEnjuiciamientos = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("");
                //datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratula() + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaEnjuiciamientos.add(datoRep);
            }
//            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) >= 1 from resuelve_por_resoluciones_por_personas as r, resoluciones as s where r.resolucion = s.id and s.documento_judicial = p.documento_judicial and r.persona = p.persona and r.estado = 'AC' and r.resuelve NOT IN (?6,?7) and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l where o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and o.resolucion = r.resolucion and o.estado = 'AC' ))", DocumentosJudiciales.class).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and d.canal_entrada_documento_judicial = ?1\n"
                    + "and p.estado = ?2\n"
                    + "and p.persona = ?3\n"
                    + "and ifnull((select e.tipo_resuelve = ?4\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?7\n"
                    + "	and r.estado = ?5 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?8 and o.estado = ?6 )),false)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, canal.getCodigo()).
                    setParameter(2, "AC").
                    setParameter(3, persona.getId()).
                    setParameter(4, Constantes.TIPO_RESUELVE_SENTENCIA_SANCIONATORIA).
                    setParameter(5, "AC").
                    setParameter(6, "AC").
                    setParameter(7, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                RepAntecedentesDocumentosJudicialesRes res = (RepAntecedentesDocumentosJudicialesRes) ejbFacade.getEntityManager().createNativeQuery("select r.id, t.descripcion_corta as tipo_resolucion, s.nro_resolucion, upper(e.descripcion) as resuelve, p.descripcion_corta as tipo_resolucion_alt, date_format(s.fecha,'%d de %M del %Y') as fecha\n"
                        + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e, tipos_resolucion as t, tipos_resolucion as p\n"
                        + "	where r.resolucion = s.id \n"
                        + "       and s.tipo_resolucion = t.id\n"
                        + "       and e.tipo_resolucion = p.id\n"
                        + "	and r.resuelve = e.codigo\n"
                        + "	and s.documento_judicial = ?1 \n"
                        + "	and r.persona = ?2\n"
                        + "       and e.tipo_resuelve = ?3\n"
                        + "	and r.estado = ?4 \n"
                        + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve = ?6 and o.estado = ?5 )", RepAntecedentesDocumentosJudicialesRes.class).
                        setParameter(1, doc.getId()).
                        setParameter(2, persona.getId()).
                        setParameter(3, Constantes.TIPO_RESUELVE_SENTENCIA_SANCIONATORIA).
                        setParameter(4, "AC").
                        setParameter(5, "AC").
                        setParameter(6, Constantes.TIPO_RESUELVE_SENTENCIA_SANCIONATORIA)
                        .getSingleResult();

                if (listaFiniquitados == null) {
                    listaFiniquitados = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel(res.getResuelve());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " RESOLUCION " + res.getResolucion().getTipoResolucion().getDescripcionCorta() + " N° " + res.getResolucion().getNroResolucion() + " SENTIDO: " + res.getResuelve().getDescripcion() + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String tipoResolucion = " RESOLUCION " + ((res.getTipoResolucion() != null) ? res.getTipoResolucion() : res.getTipoResolucionAlt());

                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " caratulada \"" + doc.getCaratula() + "\"" + tipoResolucion + " N° " + res.getNroResolucion() + " de fecha " + res.getFecha());

                listaFiniquitados.add(datoRep);
            }
            map.put(JRParameter.REPORT_LOCALE, Locale.GERMAN);
            System.out.println("Nombre: " + persona.getNombresApellidos());
            map.put("nombre", persona.getNombresApellidos());
            map.put("cedula", persona.getCi());
            map.put("fechaEmision", fecha);
            map.put("titulo", titulo);
            map.put("borrador", borrador);
            if (valido) {
                map.put("valido", "1");
            }
            if (persona.getDespachoPersona() != null) {
                map.put("despacho", persona.getDespachoPersona().getDescripcion());
            } else {
                map.put("despacho", "");
            }
            if (persona.getDespachoPersona() != null) {
                map.put("departamento", persona.getDepartamentoPersona().getDescripcion());
            } else {
                map.put("departamento", "");
            }

            String myHash = "";

            String codigoArchivo = "";
            if (imprimirQR) {

                codigoArchivo = generarCodigoArchivo();
                map.put("codigoArchivo", codigoArchivo);
                DateFormat format2 = new SimpleDateFormat("yyyyMMddhhmmss");

                // String pathAntecedentes = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + par.getRutaAntecedentes() + "/";
                String pathAntecedentes = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + Constantes.URL_VALIDACION;

                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update((persona.getId() + "_" + format2.format(fecha)).getBytes());
                byte[] digest = md.digest();
                myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();

                // map.put("qr", pathAntecedentes + myHash + ".pdf");
                map.put("qr", pathAntecedentes + "?hash=" + myHash);
            }

            String reportPath;

            //if (listaDenuncias.size() > 0 || listaAcusaciones.size() > 0 || listaPreliminar.size() > 0 || listaEnjuiciamientos.size() > 0 || listaFiniquitados.size() > 0 || listaAntecedentesCSJ.size() > 0) {
            if (listaDenuncias != null || listaAcusaciones != null || listaPreliminar != null || listaEnjuiciamientos != null || listaFiniquitados != null || listaAntecedentesCSJ != null || listaInformacionSumaria != null || listaPedidoDesafuero != null) {
                if (listaDenuncias != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceDenuncias = new JRBeanCollectionDataSource(listaDenuncias);
                    map.put("datasourceDenuncias", beanCollectionDataSourceDenuncias);
                } else {
                    map.put("datasourceDenuncias", null);
                }
                if (listaAcusaciones != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceAcusaciones = new JRBeanCollectionDataSource(listaAcusaciones);
                    map.put("datasourceAcusaciones", beanCollectionDataSourceAcusaciones);
                } else {
                    map.put("datasourceAcusaciones", null);
                }
                if (listaPreliminar != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourcePreliminar = new JRBeanCollectionDataSource(listaPreliminar);
                    map.put("datasourcePreliminares", beanCollectionDataSourcePreliminar);
                } else {
                    map.put("datasourcePreliminares", null);
                }
                if (listaEnjuiciamientos != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceEnjuiciamientos = new JRBeanCollectionDataSource(listaEnjuiciamientos);
                    map.put("datasourceEnjuiciamientos", beanCollectionDataSourceEnjuiciamientos);
                } else {
                    map.put("datasourceEnjuiciamientos", null);
                }
                if (listaFiniquitados != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceFiniquitados = new JRBeanCollectionDataSource(listaFiniquitados);
                    map.put("datasourceFiniquitados", beanCollectionDataSourceFiniquitados);
                } else {
                    map.put("datasourceFiniquitados", null);
                }
                if (listaAntecedentesCSJ != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceAntecedentesCSJ = new JRBeanCollectionDataSource(listaAntecedentesCSJ);
                    map.put("datasourceAntecedentesCSJ", beanCollectionDataSourceAntecedentesCSJ);
                } else {
                    map.put("datasourceAntecedentesCSJ", null);
                }
                if (listaInformacionSumaria != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceInformacionSumaria = new JRBeanCollectionDataSource(listaInformacionSumaria);
                    map.put("datasourceInformacionSumaria", beanCollectionDataSourceInformacionSumaria);
                } else {
                    map.put("datasourceInformacionSumaria", null);
                }
                if (listaPedidoDesafuero != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourcePedidoDesafuero = new JRBeanCollectionDataSource(listaPedidoDesafuero);
                    map.put("datasourcePedidoDesafuero", beanCollectionDataSourcePedidoDesafuero);
                } else {
                    map.put("datasourcePedidoDesafuero", null);
                }

                reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/repConAntecedentes.jasper");
            } else {
                reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/repSinAntecedentes.jasper");
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, new JREmptyDataSource());

            if (imprimirQR) {

                Antecedentes ant = new Antecedentes();

                ant.setEmpresa(new Empresas(1));
                ant.setFechaHoraAlta(fecha);
                ant.setFechaHoraUltimoEstado(fecha);
                ant.setPersona(persona);
                ant.setPathArchivo(myHash + ".pdf");
                ant.setHash(myHash);
                ant.setCodigoArchivo(codigoArchivo);

                antecedentesController.setSelected(ant);
                antecedentesController.saveNew(null);

                // JasperExportManager.exportReportToPdfFile(jasperPrint, Constantes.RUTA_ARCHIVOS_TEMP + "/" + par.getRutaAntecedentes() + "/" + myHash + ".pdf");
                JasperExportManager.exportReportToPdfFile(jasperPrint, Constantes.RUTA_ARCHIVOS_TEMP + "/" + par.getRutaAntecedentes() + "/" + myHash + ".pdf");

            }

            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.addHeader("Content-disposition", "attachment;filename=antecedentes.pdf");

            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void imprimirRepAntecedentes(Personas persona) {
        HttpServletResponse httpServletResponse = null;
        try {

            ejbFacade.getEntityManager().getEntityManagerFactory().getCache().evictAll();
            
            List<RepAntecedentesDocumentosJudiciales> listaDenuncias = null;
            List<RepAntecedentesDocumentosJudiciales> listaAcusaciones = null;
            List<RepAntecedentesDocumentosJudiciales> listaPreliminar = null;
            List<RepAntecedentesDocumentosJudiciales> listaEnjuiciamientos = null;
            List<RepAntecedentesDocumentosJudiciales> listaFiniquitados = null;
            List<RepAntecedentesDocumentosJudiciales> listaAntecedentesCSJ = null;
            List<RepAntecedentesDocumentosJudiciales> listaInformacionSumaria = null;
            List<RepAntecedentesDocumentosJudiciales> listaPedidoDesafuero = null;
            RepAntecedentesDocumentosJudiciales datoRep = null;

            Date fecha = ejbFacade.getSystemDate();

            // DateFormat format = new SimpleDateFormat("%d 'de' %M 'del' %Y");
            if (persona == null) {
                JsfUtil.addErrorMessage("Debe seleccionar una persona");
                return;
            }

            // List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_DENUNCIA).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            //List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and d.canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r, resoluciones as e where r.resolucion = e.id and e.documento_judicial = p.documento_judicial and r.persona = p.persona and r.estado = 'AC' and r.resuelve in (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_DENUNCIA).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )), d.estado_proceso <> ?11)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_DENUNCIA).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_DENUNCIA).
                    setParameter(11, Constantes.ESTADO_PROCESO_FINALIZADO).
                    getResultList();
            

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaDenuncias == null) {
                    listaDenuncias = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Denuncia:".toUpperCase());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_DENUNCIA) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }

                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaDenuncias.add(datoRep);
            }

            HashMap map = new HashMap();
            // listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_ACUSACION).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            // listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ACUSACION).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )), d.estado_proceso != ?11)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_ACUSACION).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_ACUSACION).
                    setParameter(11, Constantes.ESTADO_PROCESO_FINALIZADO).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaAcusaciones == null) {
                    listaAcusaciones = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Acusacion:".toUpperCase());
                // datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_ACUSACION) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }

                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaAcusaciones.add(datoRep);
            }
            // listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )), d.estado_proceso != ?11)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).
                    setParameter(11, Constantes.ESTADO_PROCESO_FINALIZADO).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaPreliminar == null) {
                    listaPreliminar = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Investigacion Preliminar:".toUpperCase());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaPreliminar.add(datoRep);
            }
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),d.estado_proceso != ?11)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).
                    setParameter(11, Constantes.ESTADO_PROCESO_FINALIZADO).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaAntecedentesCSJ == null) {
                    listaAntecedentesCSJ = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Antecedentes C.S.J.:".toUpperCase());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaAntecedentesCSJ.add(datoRep);
            }
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),d.estado_proceso != ?11)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_INFORMACION_SUMARIA).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_INFORMACION_SUMARIA).
                    setParameter(11, Constantes.ESTADO_PROCESO_FINALIZADO).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaInformacionSumaria == null) {
                    listaInformacionSumaria = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Informacion Sumaria:".toUpperCase());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_INFORMACION_SUMARIA) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaInformacionSumaria.add(datoRep);
            }
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),d.estado_proceso != ?11)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_PEDIDO_DESAFUERO).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_PEDIDO_DESAFUERO).
                    setParameter(11, Constantes.ESTADO_PROCESO_FINALIZADO).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaPedidoDesafuero == null) {
                    listaPedidoDesafuero = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("");
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_PEDIDO_DESAFUERO) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaPedidoDesafuero.add(datoRep);
            }
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) >= 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7) and r.fecha_hora_ultimo_estado in (select max(fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o where r.persona = o.persona and r.resolucion = o.resolucion and o.estado = 'AC' ))", DocumentosJudiciales.class).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and d.canal_entrada_documento_judicial = ?1\n"
                    + "and p.estado = ?2\n"
                    + "and p.persona = ?3\n"
                    + "and ifnull((select e.tipo_resuelve = ?4\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?7\n"
                    + "	and r.estado = ?5 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?8 and o.estado = ?6 )),false)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, canal.getCodigo()).
                    setParameter(2, "AC").
                    setParameter(3, persona.getId()).
                    setParameter(4, Constantes.TIPO_RESUELVE_ENJUICIAMIENTO).
                    setParameter(5, "AC").
                    setParameter(6, "AC").
                    setParameter(7, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaEnjuiciamientos == null) {
                    listaEnjuiciamientos = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("");
                //datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratula() + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaEnjuiciamientos.add(datoRep);
            }
//            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) >= 1 from resuelve_por_resoluciones_por_personas as r, resoluciones as s where r.resolucion = s.id and s.documento_judicial = p.documento_judicial and r.persona = p.persona and r.estado = 'AC' and r.resuelve NOT IN (?6,?7) and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l where o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and o.resolucion = r.resolucion and o.estado = 'AC' ))", DocumentosJudiciales.class).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and d.canal_entrada_documento_judicial = ?1\n"
                    + "and p.estado = ?2\n"
                    + "and p.persona = ?3\n"
                    + "and ifnull((select e.tipo_resuelve = ?4\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?7\n"
                    + "	and r.estado = ?5 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?8 and o.estado = ?6 )),false)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, canal.getCodigo()).
                    setParameter(2, "AC").
                    setParameter(3, persona.getId()).
                    setParameter(4, Constantes.TIPO_RESUELVE_SENTENCIA_SANCIONATORIA).
                    setParameter(5, "AC").
                    setParameter(6, "AC").
                    setParameter(7, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                RepAntecedentesDocumentosJudicialesRes res = (RepAntecedentesDocumentosJudicialesRes) ejbFacade.getEntityManager().createNativeQuery("select r.id, t.descripcion_corta as tipo_resolucion, s.nro_resolucion, upper(e.descripcion) as resuelve, p.descripcion_corta as tipo_resolucion_alt, date_format(s.fecha,'%d de %M del %Y') as fecha\n"
                        + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e, tipos_resolucion as t, tipos_resolucion as p\n"
                        + "	where r.resolucion = s.id \n"
                        + "       and s.tipo_resolucion = t.id\n"
                        + "       and e.tipo_resolucion = p.id\n"
                        + "	and r.resuelve = e.codigo\n"
                        + "	and s.documento_judicial = ?1 \n"
                        + "	and r.persona = ?2\n"
                        + "       and e.tipo_resuelve = ?3\n"
                        + "	and r.estado = ?4 \n"
                        + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve = ?6 and o.estado = ?5 )", RepAntecedentesDocumentosJudicialesRes.class).
                        setParameter(1, doc.getId()).
                        setParameter(2, persona.getId()).
                        setParameter(3, Constantes.TIPO_RESUELVE_SENTENCIA_SANCIONATORIA).
                        setParameter(4, "AC").
                        setParameter(5, "AC").
                        setParameter(6, Constantes.TIPO_RESUELVE_SENTENCIA_SANCIONATORIA)
                        .getSingleResult();

                if (listaFiniquitados == null) {
                    listaFiniquitados = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel(res.getResuelve());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " RESOLUCION " + res.getResolucion().getTipoResolucion().getDescripcionCorta() + " N° " + res.getResolucion().getNroResolucion() + " SENTIDO: " + res.getResuelve().getDescripcion() + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String tipoResolucion = " RESOLUCION " + ((res.getTipoResolucion() != null) ? res.getTipoResolucion() : res.getTipoResolucionAlt());

                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " caratulada \"" + doc.getCaratula() + "\"" + tipoResolucion + " N° " + res.getNroResolucion() + " de fecha " + res.getFecha());

                listaFiniquitados.add(datoRep);
            }
            map.put(JRParameter.REPORT_LOCALE, Locale.GERMAN);
            System.out.println("Nombre: " + persona.getNombresApellidos());
            map.put("nombre", persona.getNombresApellidos());
            map.put("cedula", persona.getCi());
            map.put("fechaEmision", fecha);
            map.put("titulo", titulo);
            map.put("borrador", borrador);
            if (valido) {
                map.put("valido", "1");
            }
            if (persona.getDespachoPersona() != null) {
                map.put("despacho", persona.getDespachoPersona().getDescripcion());
            } else {
                map.put("despacho", "");
            }
            if (persona.getDespachoPersona() != null) {
                map.put("departamento", persona.getDepartamentoPersona().getDescripcion());
            } else {
                map.put("departamento", "");
            }

            String myHash = "";

            String codigoArchivo = "";
            if (imprimirQR) {

                codigoArchivo = generarCodigoArchivo();
                map.put("codigoArchivo", codigoArchivo);
                DateFormat format2 = new SimpleDateFormat("yyyyMMddhhmmss");

                // String pathAntecedentes = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + par.getRutaAntecedentes() + "/";
                String pathAntecedentes = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + Constantes.URL_VALIDACION;

                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update((persona.getId() + "_" + format2.format(fecha)).getBytes());
                byte[] digest = md.digest();
                myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();

                // map.put("qr", pathAntecedentes + myHash + ".pdf");
                map.put("qr", pathAntecedentes + "?hash=" + myHash);
            }

            String reportPath;

            //if (listaDenuncias.size() > 0 || listaAcusaciones.size() > 0 || listaPreliminar.size() > 0 || listaEnjuiciamientos.size() > 0 || listaFiniquitados.size() > 0 || listaAntecedentesCSJ.size() > 0) {
            if (listaDenuncias != null || listaAcusaciones != null || listaPreliminar != null || listaEnjuiciamientos != null || listaFiniquitados != null || listaAntecedentesCSJ != null || listaInformacionSumaria != null || listaPedidoDesafuero != null) {
                if (listaDenuncias != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceDenuncias = new JRBeanCollectionDataSource(listaDenuncias);
                    map.put("datasourceDenuncias", beanCollectionDataSourceDenuncias);
                } else {
                    map.put("datasourceDenuncias", null);
                }
                if (listaAcusaciones != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceAcusaciones = new JRBeanCollectionDataSource(listaAcusaciones);
                    map.put("datasourceAcusaciones", beanCollectionDataSourceAcusaciones);
                } else {
                    map.put("datasourceAcusaciones", null);
                }
                if (listaPreliminar != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourcePreliminar = new JRBeanCollectionDataSource(listaPreliminar);
                    map.put("datasourcePreliminares", beanCollectionDataSourcePreliminar);
                } else {
                    map.put("datasourcePreliminares", null);
                }
                if (listaEnjuiciamientos != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceEnjuiciamientos = new JRBeanCollectionDataSource(listaEnjuiciamientos);
                    map.put("datasourceEnjuiciamientos", beanCollectionDataSourceEnjuiciamientos);
                } else {
                    map.put("datasourceEnjuiciamientos", null);
                }
                if (listaFiniquitados != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceFiniquitados = new JRBeanCollectionDataSource(listaFiniquitados);
                    map.put("datasourceFiniquitados", beanCollectionDataSourceFiniquitados);
                } else {
                    map.put("datasourceFiniquitados", null);
                }
                if (listaAntecedentesCSJ != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceAntecedentesCSJ = new JRBeanCollectionDataSource(listaAntecedentesCSJ);
                    map.put("datasourceAntecedentesCSJ", beanCollectionDataSourceAntecedentesCSJ);
                } else {
                    map.put("datasourceAntecedentesCSJ", null);
                }
                if (listaInformacionSumaria != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceInformacionSumaria = new JRBeanCollectionDataSource(listaInformacionSumaria);
                    map.put("datasourceInformacionSumaria", beanCollectionDataSourceInformacionSumaria);
                } else {
                    map.put("datasourceInformacionSumaria", null);
                }
                if (listaPedidoDesafuero != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourcePedidoDesafuero = new JRBeanCollectionDataSource(listaPedidoDesafuero);
                    map.put("datasourcePedidoDesafuero", beanCollectionDataSourcePedidoDesafuero);
                } else {
                    map.put("datasourcePedidoDesafuero", null);
                }

                reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/repConAntecedentes.jasper");
            } else {
                reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/repSinAntecedentes.jasper");
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, new JREmptyDataSource());

            if (imprimirQR) {

                Antecedentes ant = new Antecedentes();

                ant.setEmpresa(new Empresas(1));
                ant.setFechaHoraAlta(fecha);
                ant.setFechaHoraUltimoEstado(fecha);
                ant.setPersona(persona);
                ant.setPathArchivo(myHash + ".pdf");
                ant.setHash(myHash);
                ant.setCodigoArchivo(codigoArchivo);

                antecedentesController.setSelected(ant);
                antecedentesController.saveNew(null);

                // JasperExportManager.exportReportToPdfFile(jasperPrint, Constantes.RUTA_ARCHIVOS_TEMP + "/" + par.getRutaAntecedentes() + "/" + myHash + ".pdf");
                JasperExportManager.exportReportToPdfFile(jasperPrint, Constantes.RUTA_ARCHIVOS_TEMP + "/" + par.getRutaAntecedentes() + "/" + myHash + ".pdf");

            }

            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.addHeader("Content-disposition", "attachment;filename=antecedentes.pdf");

            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /*
    public void imprimirRepAntecedentes(Personas persona) {
        HttpServletResponse httpServletResponse = null;
        try {

            List<RepAntecedentesDocumentosJudiciales> listaDenuncias = null;
            List<RepAntecedentesDocumentosJudiciales> listaAcusaciones = null;
            List<RepAntecedentesDocumentosJudiciales> listaPreliminar = null;
            List<RepAntecedentesDocumentosJudiciales> listaEnjuiciamientos = null;
            List<RepAntecedentesDocumentosJudiciales> listaFiniquitados = null;
            List<RepAntecedentesDocumentosJudiciales> listaAntecedentesCSJ = null;
            RepAntecedentesDocumentosJudiciales datoRep = null;

            Date fecha = ejbFacade.getSystemDate();

            // DateFormat format = new SimpleDateFormat("%d 'de' %M 'del' %Y");
            if (persona == null) {
                JsfUtil.addErrorMessage("Debe seleccionar una persona");
                return;
            }

            // List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_DENUNCIA).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            //List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and d.canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r, resoluciones as e where r.resolucion = e.id and e.documento_judicial = p.documento_judicial and r.persona = p.persona and r.estado = 'AC' and r.resuelve in (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_DENUNCIA).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),true)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_DENUNCIA).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_DENUNCIA).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaDenuncias == null) {
                    listaDenuncias = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Denuncia:".toUpperCase());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_DENUNCIA) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }

                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaDenuncias.add(datoRep);
            }

            HashMap map = new HashMap();
            // listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_ACUSACION).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            // listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ACUSACION).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),true)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_ACUSACION).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_ACUSACION).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaAcusaciones == null) {
                    listaAcusaciones = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Acusacion:".toUpperCase());
                // datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_ACUSACION) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }

                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaAcusaciones.add(datoRep);
            }
            // listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),true)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaPreliminar == null) {
                    listaPreliminar = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Investigacion Preliminar:".toUpperCase());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaPreliminar.add(datoRep);
            }
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and (d.tipo_expediente = ?1 or d.tipo_expediente_anterior = ?10)\n"
                    + "and d.canal_entrada_documento_judicial = ?2\n"
                    + "and p.estado = ?3\n"
                    + "and p.persona = ?4\n"
                    + "and ifnull((select e.tipo_resuelve = ?5\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?8\n"
                    + "	and r.estado = ?6 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?9 and o.estado = ?7 )),true)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(9, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(10, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaAntecedentesCSJ == null) {
                    listaAntecedentesCSJ = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Antecedentes C.S.J.:".toUpperCase());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String caratula = doc.getCaratula();
                if (doc.getTipoExpedienteAnterior() != null) {
                    if (doc.getTipoExpedienteAnterior().getId() == Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ) {
                        caratula = doc.getCaratulaAnterior();
                    }
                }
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + caratula + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaAntecedentesCSJ.add(datoRep);
            }
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) >= 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7) and r.fecha_hora_ultimo_estado in (select max(fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o where r.persona = o.persona and r.resolucion = o.resolucion and o.estado = 'AC' ))", DocumentosJudiciales.class).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and d.canal_entrada_documento_judicial = ?1\n"
                    + "and p.estado = ?2\n"
                    + "and p.persona = ?3\n"
                    + "and ifnull((select e.tipo_resuelve = ?4\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?7\n"
                    + "	and r.estado = ?5 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?8 and o.estado = ?6 )),false)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, canal.getCodigo()).
                    setParameter(2, "AC").
                    setParameter(3, persona.getId()).
                    setParameter(4, Constantes.TIPO_RESUELVE_ENJUICIAMIENTO).
                    setParameter(5, "AC").
                    setParameter(6, "AC").
                    setParameter(7, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if (listaEnjuiciamientos == null) {
                    listaEnjuiciamientos = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("");
                //datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }
                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratula() + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaEnjuiciamientos.add(datoRep);
            }
//            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) >= 1 from resuelve_por_resoluciones_por_personas as r, resoluciones as s where r.resolucion = s.id and s.documento_judicial = p.documento_judicial and r.persona = p.persona and r.estado = 'AC' and r.resuelve NOT IN (?6,?7) and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l where o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and o.resolucion = r.resolucion and o.estado = 'AC' ))", DocumentosJudiciales.class).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n"
                    + "where d.id = p.documento_judicial \n"
                    + "and d.canal_entrada_documento_judicial = ?1\n"
                    + "and p.estado = ?2\n"
                    + "and p.persona = ?3\n"
                    + "and ifnull((select e.tipo_resuelve = ?4\n"
                    + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n"
                    + "	where r.resolucion = s.id \n"
                    + "	and r.resuelve = e.codigo\n"
                    + "	and s.documento_judicial = p.documento_judicial \n"
                    + "	and r.persona = p.persona \n"
                    + "       and e.tipo_resuelve <> ?7\n"
                    + "	and r.estado = ?5 \n"
                    + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve <> ?8 and o.estado = ?6 )),false)\n"
                    + "order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, canal.getCodigo()).
                    setParameter(2, "AC").
                    setParameter(3, persona.getId()).
                    setParameter(4, Constantes.TIPO_RESUELVE_SENTENCIA_SANCIONATORIA).
                    setParameter(5, "AC").
                    setParameter(6, "AC").
                    setParameter(7, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    setParameter(8, Constantes.TIPO_RESUELVE_NO_TENER_EN_CUENTA).
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                RepAntecedentesDocumentosJudicialesRes res = (RepAntecedentesDocumentosJudicialesRes) ejbFacade.getEntityManager().createNativeQuery("select r.id, t.descripcion_corta as tipo_resolucion, s.nro_resolucion, upper(e.descripcion) as resuelve, p.descripcion_corta as tipo_resolucion_alt, date_format(s.fecha,'%d de %M del %Y') as fecha\n"
                        + "	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e, tipos_resolucion as t, tipos_resolucion as p\n"
                        + "	where r.resolucion = s.id \n"
                        + "       and s.tipo_resolucion = t.id\n"
                        + "       and e.tipo_resolucion = p.id\n"
                        + "	and r.resuelve = e.codigo\n"
                        + "	and s.documento_judicial = ?1 \n"
                        + "	and r.persona = ?2\n"
                        + "       and e.tipo_resuelve = ?3\n"
                        + "	and r.estado = ?4 \n"
                        + "	and s.fecha in (select max(l.fecha) from resuelve_por_resoluciones_por_personas as o, resoluciones as l, resuelve as f where o.resuelve = f.codigo AND o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and f.tipo_resuelve = ?6 and o.estado = ?5 )", RepAntecedentesDocumentosJudicialesRes.class).
                        setParameter(1, doc.getId()).
                        setParameter(2, persona.getId()).
                        setParameter(3, Constantes.TIPO_RESUELVE_SENTENCIA_SANCIONATORIA).
                        setParameter(4, "AC").
                        setParameter(5, "AC").
                        setParameter(6, Constantes.TIPO_RESUELVE_SENTENCIA_SANCIONATORIA)
                        .getSingleResult();

                if (listaFiniquitados == null) {
                    listaFiniquitados = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel(res.getResuelve());
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " RESOLUCION " + res.getResolucion().getTipoResolucion().getDescripcionCorta() + " N° " + res.getResolucion().getNroResolucion() + " SENTIDO: " + res.getResuelve().getDescripcion() + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if (array.length <= 0) {
                    array = doc.getCausa().split(".");
                }

                String ano = "";
                if (array.length > 0) {
                    ano = array[array.length - 1];
                }

                String tipoResolucion = " RESOLUCION " + ((res.getTipoResolucion() != null) ? res.getTipoResolucion() : res.getTipoResolucionAlt());

                datoRep.setTexto("En el EXP. JEM N° " + doc.getCausa() + " caratulada \"" + doc.getCaratula() + "\"" + tipoResolucion + " N° " + res.getNroResolucion() + " de fecha " + res.getFecha());

                listaFiniquitados.add(datoRep);
            }
            map.put(JRParameter.REPORT_LOCALE, Locale.GERMAN);
            System.out.println("Nombre: " + persona.getNombresApellidos());
            map.put("nombre", persona.getNombresApellidos());
            map.put("cedula", persona.getCi());
            map.put("fechaEmision", fecha);
            map.put("titulo", titulo);
            map.put("borrador", borrador);
            if (valido) {
                map.put("valido", "1");
            }
            if (persona.getDespachoPersona() != null) {
                map.put("despacho", persona.getDespachoPersona().getDescripcion());
            } else {
                map.put("despacho", "");
            }
            if (persona.getDespachoPersona() != null) {
                map.put("departamento", persona.getDepartamentoPersona().getDescripcion());
            } else {
                map.put("departamento", "");
            }

            String myHash = "";

            String codigoArchivo = "";
            if (imprimirQR) {

                codigoArchivo = generarCodigoArchivo();
                map.put("codigoArchivo", codigoArchivo);
                DateFormat format2 = new SimpleDateFormat("yyyyMMddhhmmss");

                // String pathAntecedentes = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + par.getRutaAntecedentes() + "/";
                String pathAntecedentes = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + Constantes.URL_VALIDACION;

                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update((persona.getId() + "_" + format2.format(fecha)).getBytes());
                byte[] digest = md.digest();
                myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();

                // map.put("qr", pathAntecedentes + myHash + ".pdf");
                map.put("qr", pathAntecedentes + "?hash=" + myHash);
            }

            String reportPath;

            //if (listaDenuncias.size() > 0 || listaAcusaciones.size() > 0 || listaPreliminar.size() > 0 || listaEnjuiciamientos.size() > 0 || listaFiniquitados.size() > 0 || listaAntecedentesCSJ.size() > 0) {
            if (listaDenuncias != null || listaAcusaciones != null || listaPreliminar != null || listaEnjuiciamientos != null || listaFiniquitados != null || listaAntecedentesCSJ != null) {
                if (listaDenuncias != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceDenuncias = new JRBeanCollectionDataSource(listaDenuncias);
                    map.put("datasourceDenuncias", beanCollectionDataSourceDenuncias);
                } else {
                    map.put("datasourceDenuncias", null);
                }
                if (listaAcusaciones != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceAcusaciones = new JRBeanCollectionDataSource(listaAcusaciones);
                    map.put("datasourceAcusaciones", beanCollectionDataSourceAcusaciones);
                } else {
                    map.put("datasourceAcusaciones", null);
                }
                if (listaPreliminar != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourcePreliminar = new JRBeanCollectionDataSource(listaPreliminar);
                    map.put("datasourcePreliminares", beanCollectionDataSourcePreliminar);
                } else {
                    map.put("datasourcePreliminares", null);
                }
                if (listaEnjuiciamientos != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceEnjuiciamientos = new JRBeanCollectionDataSource(listaEnjuiciamientos);
                    map.put("datasourceEnjuiciamientos", beanCollectionDataSourceEnjuiciamientos);
                } else {
                    map.put("datasourceEnjuiciamientos", null);
                }
                if (listaFiniquitados != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceFiniquitados = new JRBeanCollectionDataSource(listaFiniquitados);
                    map.put("datasourceFiniquitados", beanCollectionDataSourceFiniquitados);
                } else {
                    map.put("datasourceFiniquitados", null);
                }
                if (listaAntecedentesCSJ != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceAntecedentesCSJ = new JRBeanCollectionDataSource(listaAntecedentesCSJ);
                    map.put("datasourceAntecedentesCSJ", beanCollectionDataSourceAntecedentesCSJ);
                } else {
                    map.put("datasourceAntecedentesCSJ", null);
                }

                reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/repConAntecedentes.jasper");
            } else {
                reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/repSinAntecedentes.jasper");
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, new JREmptyDataSource());

            if (imprimirQR) {

                Antecedentes ant = new Antecedentes();

                ant.setEmpresa(new Empresas(1));
                ant.setFechaHoraAlta(fecha);
                ant.setFechaHoraUltimoEstado(fecha);
                ant.setPersona(persona);
                ant.setPathArchivo(myHash + ".pdf");
                ant.setHash(myHash);
                ant.setCodigoArchivo(codigoArchivo);

                antecedentesController.setSelected(ant);
                antecedentesController.saveNew(null);

                // JasperExportManager.exportReportToPdfFile(jasperPrint, Constantes.RUTA_ARCHIVOS_TEMP + "/" + par.getRutaAntecedentes() + "/" + myHash + ".pdf");
                JasperExportManager.exportReportToPdfFile(jasperPrint, Constantes.RUTA_ARCHIVOS_TEMP + "/" + par.getRutaAntecedentes() + "/" + myHash + ".pdf");

            }

            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.addHeader("Content-disposition", "attachment;filename=antecedentes.pdf");

            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */

    /*
    
    public void imprimirRepAntecedentes(Personas persona) {
        HttpServletResponse httpServletResponse = null;
        try {

            List<RepAntecedentesDocumentosJudiciales> listaDenuncias = null;
            List<RepAntecedentesDocumentosJudiciales> listaAcusaciones = null;
            List<RepAntecedentesDocumentosJudiciales> listaPreliminar = null;
            List<RepAntecedentesDocumentosJudiciales> listaEnjuiciamientos = null;
            List<RepAntecedentesDocumentosJudiciales> listaFiniquitados = null;
            List<RepAntecedentesDocumentosJudiciales> listaAntecedentesCSJ = null;
            RepAntecedentesDocumentosJudiciales datoRep = null;
            
            Date fecha = ejbFacade.getSystemDate();

            DateFormat format = new SimpleDateFormat("yyyy");

            if (persona == null) {
                JsfUtil.addErrorMessage("Debe seleccionar una persona");
                return;
            }

            // List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_DENUNCIA).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            //List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and d.canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r, resoluciones as e where r.resolucion = e.id and e.documento_judicial = p.documento_judicial and r.persona = p.persona and r.estado = 'AC' and r.resuelve in (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_DENUNCIA).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n" +
"where d.id = p.documento_judicial \n" +
"and d.tipo_expediente = ?1\n" +
"and d.canal_entrada_documento_judicial = ?2\n" +
"and p.estado = ?3\n" +
"and p.persona = ?4\n" +
"and ifnull((select e.tipo_resuelve = ?5\n" +
"	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n" +
"	where r.resolucion = s.id \n" +
"	and r.resuelve = e.codigo\n" +
"	and s.documento_judicial = p.documento_judicial \n" +
"	and r.persona = p.persona \n" +
"	and r.estado = ?6 \n" +
"	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l where o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and o.estado = ?7 )),true)\n" +
"order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_DENUNCIA).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if(listaDenuncias == null){
                    listaDenuncias = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Denuncia:");
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if(array.length <= 0){
                    array = doc.getCausa().split(".");
                }
                
                String ano = "";
                if(array.length > 0){
                    ano = array[array.length - 1];
                }
                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratula() + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaDenuncias.add(datoRep);
            }

            HashMap map = new HashMap();
            // listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_ACUSACION).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            // listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ACUSACION).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n" +
"where d.id = p.documento_judicial \n" +
"and d.tipo_expediente = ?1\n" +
"and d.canal_entrada_documento_judicial = ?2\n" +
"and p.estado = ?3\n" +
"and p.persona = ?4\n" +
"and ifnull((select e.tipo_resuelve = ?5\n" +
"	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n" +
"	where r.resolucion = s.id \n" +
"	and r.resuelve = e.codigo\n" +
"	and s.documento_judicial = p.documento_judicial \n" +
"	and r.persona = p.persona \n" +
"	and r.estado = ?6 \n" +
"	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l where o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and o.estado = ?7 )),true)\n" +
"order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_ACUSACION).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    getResultList();
            
            for (DocumentosJudiciales doc : listaDocs) {
                if(listaAcusaciones == null){
                    listaAcusaciones = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Acusacion:");
                // datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if(array.length <= 0){
                    array = doc.getCausa().split(".");
                }
                
                String ano = "";
                if(array.length > 0){
                    ano = array[array.length - 1];
                }
                
                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratula() + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaAcusaciones.add(datoRep);
            }
            // listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).setParameter("persona", persona).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n" +
"where d.id = p.documento_judicial \n" +
"and d.tipo_expediente = ?1\n" +
"and d.canal_entrada_documento_judicial = ?2\n" +
"and p.estado = ?3\n" +
"and p.persona = ?4\n" +
"and ifnull((select e.tipo_resuelve = ?5\n" +
"	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n" +
"	where r.resolucion = s.id \n" +
"	and r.resuelve = e.codigo\n" +
"	and s.documento_judicial = p.documento_judicial \n" +
"	and r.persona = p.persona \n" +
"	and r.estado = ?6 \n" +
"	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l where o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and o.estado = ?7 )),true)\n" +
"order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if(listaPreliminar == null){
                    listaPreliminar = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Investigacion Preliminar:");
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if(array.length <= 0){
                    array = doc.getCausa().split(".");
                }
                
                String ano = "";
                if(array.length > 0){
                    ano = array[array.length - 1];
                }
                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratula() + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaPreliminar.add(datoRep);
            }
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7))", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n" +
"where d.id = p.documento_judicial \n" +
"and d.tipo_expediente = ?1\n" +
"and d.canal_entrada_documento_judicial = ?2\n" +
"and p.estado = ?3\n" +
"and p.persona = ?4\n" +
"and ifnull((select e.tipo_resuelve = ?5\n" +
"	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n" +
"	where r.resolucion = s.id \n" +
"	and r.resuelve = e.codigo\n" +
"	and s.documento_judicial = p.documento_judicial \n" +
"	and r.persona = p.persona \n" +
"	and r.estado = ?6 \n" +
"	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l where o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and o.estado = ?7 )),true)\n" +
"order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).
                    setParameter(2, canal.getCodigo()).
                    setParameter(3, "AC").
                    setParameter(4, persona.getId()).
                    setParameter(5, Constantes.TIPO_RESUELVE_EN_TRAMITE).
                    setParameter(6, "AC").
                    setParameter(7, "AC").
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if(listaAntecedentesCSJ == null){
                    listaAntecedentesCSJ = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Antecedentes C.S.J.:");
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if(array.length <= 0){
                    array = doc.getCausa().split(".");
                }
                
                String ano = "";
                if(array.length > 0){
                    ano = array[array.length - 1];
                }
                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratula() + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaAntecedentesCSJ.add(datoRep);
            }
            //listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) >= 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve IN (?6,?7) and r.fecha_hora_ultimo_estado in (select max(fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o where r.persona = o.persona and r.resolucion = o.resolucion and o.estado = 'AC' ))", DocumentosJudiciales.class).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n" +
"where d.id = p.documento_judicial \n" +
"and d.canal_entrada_documento_judicial = ?1\n" +
"and p.estado = ?2\n" +
"and p.persona = ?3\n" +
"and ifnull((select e.tipo_resuelve = ?4\n" +
"	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n" +
"	where r.resolucion = s.id \n" +
"	and r.resuelve = e.codigo\n" +
"	and s.documento_judicial = p.documento_judicial \n" +
"	and r.persona = p.persona \n" +
"	and r.estado = ?5 \n" +
"	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l where o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and o.estado = ?6 )),false)\n" +
"order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, canal.getCodigo()).
                    setParameter(2, "AC").
                    setParameter(3, persona.getId()).
                    setParameter(4, Constantes.TIPO_RESUELVE_ENJUICIAMIENTO).
                    setParameter(5, "AC").
                    setParameter(6, "AC").
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                if(listaEnjuiciamientos == null){
                    listaEnjuiciamientos = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("");
                //datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if(array.length <= 0){
                    array = doc.getCausa().split(".");
                }
                
                String ano = "";
                if(array.length > 0){
                    ano = array[array.length - 1];
                }
                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratula() + " AÑO:" + ano + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaEnjuiciamientos.add(datoRep);
            }
//            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and d.estado_proceso <> ?5 and (select count(*) >= 1 from resuelve_por_resoluciones_por_personas as r, resoluciones as s where r.resolucion = s.id and s.documento_judicial = p.documento_judicial and r.persona = p.persona and r.estado = 'AC' and r.resuelve NOT IN (?6,?7) and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l where o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and o.resolucion = r.resolucion and o.estado = 'AC' ))", DocumentosJudiciales.class).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, persona.getId()).setParameter(5, Constantes.ESTADO_PROCESO_FINALIZADO).setParameter(6, Constantes.RESUELVE_ENJUICIAMIENTO_SIN_SUSPENCION).setParameter(7, Constantes.RESUELVE_ENJUICIAMIENTO_CON_SUSPENCION).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p \n" +
"where d.id = p.documento_judicial \n" +
"and d.canal_entrada_documento_judicial = ?1\n" +
"and p.estado = ?2\n" +
"and p.persona = ?3\n" +
"and ifnull((select e.tipo_resuelve = ?4\n" +
"	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e\n" +
"	where r.resolucion = s.id \n" +
"	and r.resuelve = e.codigo\n" +
"	and s.documento_judicial = p.documento_judicial \n" +
"	and r.persona = p.persona \n" +
"	and r.estado = ?5 \n" +
"	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l where o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and o.estado = ?6 )),false)\n" +
"order by fecha_hora_ultimo_estado", DocumentosJudiciales.class).
                    setParameter(1, canal.getCodigo()).
                    setParameter(2, "AC").
                    setParameter(3, persona.getId()).
                    setParameter(4, Constantes.TIPO_RESUELVE_SENTENCIA_SANCIONATORIA).
                    setParameter(5, "AC").
                    setParameter(6, "AC").
                    getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                RepAntecedentesDocumentosJudicialesRes res = (RepAntecedentesDocumentosJudicialesRes) ejbFacade.getEntityManager().createNativeQuery("select s.id, t.descripcion_corta as tipo_resolucion, s.nro_resolucion, e.descripcion as resuelve\n" +
"	from resuelve_por_resoluciones_por_personas as r, resoluciones as s, resuelve as e, tipos_resolucion as t\n" +
"	where r.resolucion = s.id \n" +
"       and s.tipo_resolucion = t.id\n" + 
"	and r.resuelve = e.codigo\n" +
"	and s.documento_judicial = ?1 \n" +
"	and r.persona = ?2\n" +
"       and e.tipo_resuelve = ?3\n" +
"	and r.estado = ?4 \n" +
"	and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l where o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and o.estado = ?5 )", RepAntecedentesDocumentosJudicialesRes.class).
                        setParameter(1, doc.getId()).
                        setParameter(2, persona.getId()).
                        setParameter(3, Constantes.TIPO_RESUELVE_SENTENCIA_SANCIONATORIA).
                        setParameter(4, "AC").
                        setParameter(5, "AC").getSingleResult();

                if(listaFiniquitados == null){
                    listaFiniquitados = new ArrayList<>();
                }
                datoRep = new RepAntecedentesDocumentosJudiciales();
//                datoRep.setNroCausa(doc.getCausa());
//                datoRep.setCaratula(doc.getCaratulaString());
//                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
//                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("");
//                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " RESOLUCION " + res.getResolucion().getTipoResolucion().getDescripcionCorta() + " N° " + res.getResolucion().getNroResolucion() + " SENTIDO: " + res.getResuelve().getDescripcion() + " EN LA CAUSA " + doc.getMotivoProcesoString());
                String[] array = doc.getCausa().split("-");
                if(array.length <= 0){
                    array = doc.getCausa().split(".");
                }
                
                String ano = "";
                if(array.length > 0){
                    ano = array[array.length - 1];
                }
                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratula() + " AÑO:" + ano + " RESOLUCION " + res.getTipoResolucion() + " N° " + res.getNroResolucion() + " SENTIDO: " + res.getResuelve() + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaFiniquitados.add(datoRep);
            }
            map.put(JRParameter.REPORT_LOCALE, Locale.GERMAN);
            System.out.println("Nombre: " + persona.getNombresApellidos());
            map.put("nombre", persona.getNombresApellidos());
            map.put("cedula", persona.getCi());
            map.put("fechaEmision", fecha);
            map.put("titulo", titulo);
            map.put("borrador", borrador);
            if (persona.getDespachoPersona() != null) {
                map.put("despacho", persona.getDespachoPersona().getDescripcion());
            } else {
                map.put("despacho", "");
            }
            if (persona.getDespachoPersona() != null) {
                map.put("departamento", persona.getDepartamentoPersona().getDescripcion());
            } else {
                map.put("departamento", "");
            }
            
            String myHash = "";
            
            String codigoArchivo = "";
            if(imprimirQR){
                
                codigoArchivo = generarCodigoArchivo();
                map.put("codigoArchivo", codigoArchivo);
                DateFormat format2 = new SimpleDateFormat("yyyyMMddhhmmss");

                // String pathAntecedentes = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + par.getRutaAntecedentes() + "/";
                String pathAntecedentes = par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/" + Constantes.URL_VALIDACION;

                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update((persona.getId() + "_" + format2.format(fecha)).getBytes());
                byte[] digest = md.digest();
                myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();

                // map.put("qr", pathAntecedentes + myHash + ".pdf");
                map.put("qr", pathAntecedentes + "?hash=" + myHash);
            }
            
            String reportPath;
            
            //if (listaDenuncias.size() > 0 || listaAcusaciones.size() > 0 || listaPreliminar.size() > 0 || listaEnjuiciamientos.size() > 0 || listaFiniquitados.size() > 0 || listaAntecedentesCSJ.size() > 0) {
            if (listaDenuncias != null || listaAcusaciones != null || listaPreliminar != null || listaEnjuiciamientos != null || listaFiniquitados != null || listaAntecedentesCSJ != null) {
                if (listaDenuncias != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceDenuncias = new JRBeanCollectionDataSource(listaDenuncias);
                    map.put("datasourceDenuncias", beanCollectionDataSourceDenuncias);
                }else{
                    map.put("datasourceDenuncias", null);
                }
                if (listaAcusaciones != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceAcusaciones = new JRBeanCollectionDataSource(listaAcusaciones);
                    map.put("datasourceAcusaciones", beanCollectionDataSourceAcusaciones);
                }else{
                    map.put("datasourceAcusaciones", null);
                }
                if (listaPreliminar != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourcePreliminar = new JRBeanCollectionDataSource(listaPreliminar);
                    map.put("datasourcePreliminares", beanCollectionDataSourcePreliminar);
                }else{
                    map.put("datasourcePreliminares", null);
                }
                if (listaEnjuiciamientos != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceEnjuiciamientos = new JRBeanCollectionDataSource(listaEnjuiciamientos);
                    map.put("datasourceEnjuiciamientos", beanCollectionDataSourceEnjuiciamientos);
                }else{
                    map.put("datasourceEnjuiciamientos", null);
                }
                if (listaFiniquitados != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceFiniquitados = new JRBeanCollectionDataSource(listaFiniquitados);
                    map.put("datasourceFiniquitados", beanCollectionDataSourceFiniquitados);
                }else{
                    map.put("datasourceFiniquitados", null);
                }
                if (listaAntecedentesCSJ != null) {
                    JRBeanCollectionDataSource beanCollectionDataSourceAntecedentesCSJ = new JRBeanCollectionDataSource(listaAntecedentesCSJ);
                    map.put("datasourceAntecedentesCSJ", beanCollectionDataSourceAntecedentesCSJ);
                }else{
                    map.put("datasourceAntecedentesCSJ", null);
                }

                reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/repConAntecedentes.jasper");
            } else {
                reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/repSinAntecedentes.jasper");
            }
            
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, new JREmptyDataSource());
            
            if(imprimirQR){
            
                Antecedentes ant = new Antecedentes();

                ant.setEmpresa(new Empresas(1));
                ant.setFechaHoraAlta(fecha);
                ant.setFechaHoraUltimoEstado(fecha);
                ant.setPersona(persona);
                ant.setPathArchivo(myHash + ".pdf");
                ant.setCodigoArchivo(generarCodigoArchivo());

                antecedentesController.setSelected(ant);
                antecedentesController.saveNew(null);

                // JasperExportManager.exportReportToPdfFile(jasperPrint, Constantes.RUTA_ARCHIVOS_TEMP + "/" + par.getRutaAntecedentes() + "/" + myHash + ".pdf");
                JasperExportManager.exportReportToPdfFile(jasperPrint, Constantes.RUTA_ARCHIVOS_TEMP + "/" + par.getRutaAntecedentes() + "/" + myHash + ".pdf");

            }
            
            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.addHeader("Content-disposition", "attachment;filename=antecedentes.pdf");

            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     */
 /*

    public void imprimirRepAntecedentes() {
        HttpServletResponse httpServletResponse = null;
        try {

            List<RepAntecedentesDocumentosJudiciales> listaDenuncias = new ArrayList<>();
            List<RepAntecedentesDocumentosJudiciales> listaAcusaciones = new ArrayList<>();
            List<RepAntecedentesDocumentosJudiciales> listaPreliminar = new ArrayList<>();
            List<RepAntecedentesDocumentosJudiciales> listaEnjuiciamientos = new ArrayList<>();
            List<RepAntecedentesDocumentosJudiciales> listaFiniquitados = new ArrayList<>();
            List<RepAntecedentesDocumentosJudiciales> listaAntecedentesCSJ = new ArrayList<>();
            RepAntecedentesDocumentosJudiciales datoRep = null;
            
            Date fecha = ejbFacade.getSystemDate();

            DateFormat format = new SimpleDateFormat("yyyy");

            if (getSelected() == null) {
                JsfUtil.addErrorMessage("Debe seleccionar una persona");
                return;
            }

            // List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_DENUNCIA).setParameter("persona", getSelected()).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            List<DocumentosJudiciales> listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and (d.estado_proceso = ?5 or d.estado_proceso = ?6) and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve = 'EN')", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_DENUNCIA).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, getSelected().getId()).setParameter(5, Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter(6, Constantes.ESTADO_PROCESO_PRIMERA_PROVIDENCIA).getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                datoRep = new RepAntecedentesDocumentosJudiciales();
                datoRep.setNroCausa(doc.getCausa());
                datoRep.setCaratula(doc.getCaratulaString());
                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Denuncia:");
                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaDenuncias.add(datoRep);
            }

            HashMap map = new HashMap();
            // listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_ACUSACION).setParameter("persona", getSelected()).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and (d.estado_proceso = ?5 or d.estado_proceso = ?6) and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve = 'EN')", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ACUSACION).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, getSelected().getId()).setParameter(5, Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter(6, Constantes.ESTADO_PROCESO_PRIMERA_PROVIDENCIA).getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                datoRep = new RepAntecedentesDocumentosJudiciales();
                datoRep.setNroCausa(doc.getCausa());
                datoRep.setCaratula(doc.getCaratulaString());
                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Acusacion:");
                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaAcusaciones.add(datoRep);
            }
            // listaDocs = ejbFacade.getEntityManager().createNamedQuery("DocumentosJudiciales.findbyTipoExpedienteEstadoProcesoPersona", DocumentosJudiciales.class).setParameter("estadoProceso1", Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter("estadoProceso2", Constantes.ESTADO_PROCESO_1RA_PROVIDENCIA).setParameter("tipoExpediente", Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).setParameter("persona", getSelected()).setParameter("estadoPersona", "AC").setParameter("canalEntradaDocumentoJudicial", canal).getResultList();
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and (d.estado_proceso = ?5 or d.estado_proceso = ?6) and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve = 'EN')", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_INVESTIGACION_PRELIMINAR).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, getSelected().getId()).setParameter(5, Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter(6, Constantes.ESTADO_PROCESO_PRIMERA_PROVIDENCIA).getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                datoRep = new RepAntecedentesDocumentosJudiciales();
                datoRep.setNroCausa(doc.getCausa());
                datoRep.setCaratula(doc.getCaratulaString());
                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Investigacion Preliminar:");
                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaPreliminar.add(datoRep);
            }
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.tipo_expediente = ?1 and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and (d.estado_proceso = ?5 or d.estado_proceso = ?6) and (select count(*) < 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve = 'EN')", DocumentosJudiciales.class).setParameter(1, Constantes.TIPO_EXPEDIENTE_ANTECEDENTES_CSJ).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, getSelected().getId()).setParameter(5, Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter(6, Constantes.ESTADO_PROCESO_PRIMERA_PROVIDENCIA).getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                datoRep = new RepAntecedentesDocumentosJudiciales();
                datoRep.setNroCausa(doc.getCausa());
                datoRep.setCaratula(doc.getCaratulaString());
                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("Antecedentes C.S.J.:");
                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaAntecedentesCSJ.add(datoRep);
            }
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and (d.estado_proceso = ?5 or d.estado_proceso = ?6) and (select count(*) >= 1 from resuelve_por_resoluciones_por_personas as r where r.persona = p.persona and r.estado = 'AC' and r.resuelve = 'EN' and r.fecha_hora_ultimo_estado in (select max(fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o where r.persona = o.persona and r.resolucion = o.resolucion and o.estado = 'AC' ))", DocumentosJudiciales.class).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, getSelected().getId()).setParameter(5, Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter(6, Constantes.ESTADO_PROCESO_PRIMERA_PROVIDENCIA).getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                datoRep = new RepAntecedentesDocumentosJudiciales();
                datoRep.setNroCausa(doc.getCausa());
                datoRep.setCaratula(doc.getCaratulaString());
                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("");
                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaEnjuiciamientos.add(datoRep);
            }
            listaDocs = ejbFacade.getEntityManager().createNativeQuery("select d.* from documentos_judiciales as d, personas_por_documentos_judiciales as p where d.id = p.documento_judicial and d.canal_entrada_documento_judicial = ?2 and p.estado = ?3 and p.persona = ?4 and (d.estado_proceso = ?5 or d.estado_proceso = ?6) and (select count(*) >= 1 from resuelve_por_resoluciones_por_personas as r, resoluciones as s where r.resolucion = s.id and s.documento_judicial = p.documento_judicial and r.persona = p.persona and r.estado = 'AC' and r.resuelve <> 'EN' and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l where o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and o.resolucion = r.resolucion and o.estado = 'AC' ))", DocumentosJudiciales.class).setParameter(2, canal.getCodigo()).setParameter(3, "AC").setParameter(4, getSelected().getId()).setParameter(5, Constantes.ESTADO_PROCESO_EN_TRAMITE).setParameter(6, Constantes.ESTADO_PROCESO_PRIMERA_PROVIDENCIA).getResultList();

            for (DocumentosJudiciales doc : listaDocs) {
                ResuelvePorResolucionesPorPersonas res = (ResuelvePorResolucionesPorPersonas) ejbFacade.getEntityManager().createNativeQuery("select count(*) >= 1 from resuelve_por_resoluciones_por_personas as r, resoluciones as s where r.resolucion = s.id and s.documento_judicial = ?1 and r.persona = ?2 and r.estado = 'AC' and r.resuelve <> 'EN' and r.fecha_hora_ultimo_estado in (select max(o.fecha_hora_ultimo_estado) from resuelve_por_resoluciones_por_personas as o, resoluciones as l where o.resolucion = l.id and l.documento_judicial = s.documento_judicial and o.persona = r.persona and o.resolucion = r.resolucion and o.estado = 'AC' )", ResuelvePorResolucionesPorPersonas.class).setParameter(1, doc.getId()).setParameter(2, getSelected().getId()).getSingleResult();

                datoRep = new RepAntecedentesDocumentosJudiciales();
                datoRep.setNroCausa(doc.getCausa());
                datoRep.setCaratula(doc.getCaratulaString());
                datoRep.setMotivoProceso(doc.getMotivoProcesoString());
                datoRep.setAno(format.format(doc.getFechaPresentacion()));
                datoRep.setLabel("");
                datoRep.setTexto("EXP. JEM N° " + doc.getCausa() + " " + doc.getCaratulaString() + " AÑO:" + format.format(doc.getFechaPresentacion()) + " RESOLUCION " + res.getResolucion().getTipoResolucion().getDescripcionCorta() + " N° " + res.getResolucion().getNroResolucion() + " SENTIDO: " + res.getResuelve().getDescripcion() + " EN LA CAUSA " + doc.getMotivoProcesoString());
                listaFiniquitados.add(datoRep);
            }
            map.put(JRParameter.REPORT_LOCALE, Locale.GERMAN);
            map.put("nombre", getSelected().getNombresApellidos());
            map.put("cedula", getSelected().getCi());
            map.put("fechaEmision", fecha);
            if (getSelected().getDespachoPersona() != null) {
                map.put("despacho", getSelected().getDespachoPersona().getDescripcion());
            } else {
                map.put("despacho", "");
            }
            if (getSelected().getDespachoPersona() != null) {
                map.put("departamento", getSelected().getDepartamentoPersona().getDescripcion());
            } else {
                map.put("departamento", "");
            }
            String reportPath;
            if (listaDenuncias.size() > 0 || listaAcusaciones.size() > 0 || listaPreliminar.size() > 0 || listaEnjuiciamientos.size() > 0 || listaFiniquitados.size() > 0 || listaAntecedentesCSJ.size() > 0) {
                if (listaAcusaciones.isEmpty()) {
                    listaAcusaciones.add(new RepAntecedentesDocumentosJudiciales("", "No tiene registros"));
                }
                if (listaPreliminar.isEmpty()) {
                    listaPreliminar.add(new RepAntecedentesDocumentosJudiciales("", "No tiene registros"));
                }
                if (listaEnjuiciamientos.isEmpty()) {
                    listaEnjuiciamientos.add(new RepAntecedentesDocumentosJudiciales("", "No tiene registros"));
                }
                if (listaFiniquitados.isEmpty()) {
                    listaFiniquitados.add(new RepAntecedentesDocumentosJudiciales("", "No tiene registros"));
                }
                if (listaDenuncias.isEmpty()) {
                    listaDenuncias.add(new RepAntecedentesDocumentosJudiciales("", "No tiene registros"));
                }
                if (listaAntecedentesCSJ.isEmpty()) {
                    listaAntecedentesCSJ.add(new RepAntecedentesDocumentosJudiciales("", "No tiene registros"));
                }

                JRBeanCollectionDataSource beanCollectionDataSourceDenuncias = new JRBeanCollectionDataSource(listaDenuncias);
                map.put("datasourceDenuncias", beanCollectionDataSourceDenuncias);

                JRBeanCollectionDataSource beanCollectionDataSourceAcusaciones = new JRBeanCollectionDataSource(listaAcusaciones);
                map.put("datasourceAcusaciones", beanCollectionDataSourceAcusaciones);

                JRBeanCollectionDataSource beanCollectionDataSourcePreliminar = new JRBeanCollectionDataSource(listaPreliminar);
                map.put("datasourcePreliminares", beanCollectionDataSourcePreliminar);

                JRBeanCollectionDataSource beanCollectionDataSourceEnjuiciamientos = new JRBeanCollectionDataSource(listaEnjuiciamientos);
                map.put("datasourceEnjuiciamientos", beanCollectionDataSourceEnjuiciamientos);

                JRBeanCollectionDataSource beanCollectionDataSourceFiniquitados = new JRBeanCollectionDataSource(listaFiniquitados);
                map.put("datasourceFiniquitados", beanCollectionDataSourceFiniquitados);

                JRBeanCollectionDataSource beanCollectionDataSourceAntecedentesCSJ = new JRBeanCollectionDataSource(listaAntecedentesCSJ);
                map.put("datasourceAntecedentesCSJ", beanCollectionDataSourceAntecedentesCSJ);

                reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/repConAntecedentes.jasper");
            } else {
                reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/repSinAntecedentes.jasper");
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, new JREmptyDataSource());

            httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.addHeader("Content-disposition", "attachment;filename=rep_borrador_antecedentes.pdf");

            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

            FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     */
}
