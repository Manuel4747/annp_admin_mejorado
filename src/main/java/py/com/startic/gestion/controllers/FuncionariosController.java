package py.com.startic.gestion.controllers;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;

import py.com.startic.gestion.models.Usuarios;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
//import jakarta.imageio.ImageIO;
import jakarta.inject.Inject;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.util.IOUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.model.file.UploadedFile;
import py.com.startic.gestion.controllers.util.JsfUtil;
import py.com.startic.gestion.models.Beneficios;
import py.com.startic.gestion.models.BeneficiosPorUsuarios;
import py.com.startic.gestion.models.Contactos;
import py.com.startic.gestion.models.Departamentos;
import py.com.startic.gestion.models.DepartamentosPersona;
import py.com.startic.gestion.models.Estados;
import py.com.startic.gestion.models.EstadosUsuario;
import py.com.startic.gestion.models.LocalidadesPersona;
import py.com.startic.gestion.models.Pantallas;
import py.com.startic.gestion.models.ParametrosSistema;
import py.com.startic.gestion.models.ReportesInventario;
import py.com.startic.gestion.models.RhHistCargo;
import py.com.startic.gestion.models.RhHistCategoria;
import py.com.startic.gestion.models.RhSalarios;
import py.com.startic.gestion.models.Vinculos;

@Named(value = "funcionariosController")
@ViewScoped
public class FuncionariosController extends AbstractController<Usuarios> {

    @Inject
    private DepartamentosController departamentoController;
    @Inject
    private EmpresasController empresaController;
    @Inject
    private EstadosUsuarioController estadoController;
    @Inject
    private PantallasController pantallaPrincipalController;
    @Inject
    private SexosController sexoController;
    @Inject
    private FuncionariosController usuarioAltaController;
    @Inject
    private FuncionariosController usuarioUltimoEstadoController;
    @Inject
    private RhSalariosController salarioController;
    @Inject
    private RhHistCargoController histCargoController;
    @Inject
    private RhCargosController cargoController;
    @Inject
    private RhCategoriasController categoriaController;
    @Inject
    private RhHistCategoriaController histCategoriaController;
    @Inject
    private ContactosController contactosController;
    @Inject
    private BeneficiosPorUsuariosController beneficiosPorUsuariosController;

    private final Query query = new Query();

    private Usuarios usuario;
    private String contrasena;
    private String nombreUsu;
    private String cambioContrasena1;
    private String cambioContrasena2;
    private String home;
    private Date fechaDesdeSalario;
    private BigDecimal salario;
    private Date fechaActual;
    private Collection<Usuarios> usuariosDpto;
    private UploadedFile legajoFile;
    private UploadedFile fotoFile;
    private List<ParametrosSistema> par;

    private Collection<Usuarios> funcionariosSecretaria;

    private List<DepartamentosPersona> listaDepartamentosPersona;

    private List<LocalidadesPersona> listaLocalidadesPersona;
    private String content;
    private String nombre;
    private String contentFoto;
    private String nombreFoto;
    private String url;
    private Usuarios docImprimir;
    private Usuarios docImprimirFoto;
    private HttpSession session;
    private Contactos contacto;
    private List<Vinculos> listaVinculos;
    private List<Contactos> listaContactos;
    private Departamentos departamento1;
    private Departamentos departamento2;
    private Departamentos departamento3;
    private Departamentos departamento4;
    private Departamentos departamentoLegajo1;
    private Departamentos departamentoLegajo2;
    private Departamentos departamentoLegajo3;
    private Departamentos departamentoLegajo4;
    private Departamentos dependencia;
    private List<Departamentos> listaDependencias;
    private List<Departamentos> listaDepartamentos1;
    private List<Usuarios> listaFuncionarios;
    private Usuarios funcionario;
    private FiltroURL filtroURL = new FiltroURL();
    private boolean existeFoto;
    private boolean existeLegajo;
    private List<Beneficios> listaBeneficios;
    private BeneficiosPorUsuarios beneficioPorUsuario;
    private List<BeneficiosPorUsuarios> listaBeneficiosPorUsuarios;

    public List<BeneficiosPorUsuarios> getlistaBeneficiosPorUsuarios() {
        return listaBeneficiosPorUsuarios;
    }

    public void setlistaBeneficiosPorUsuarios(List<BeneficiosPorUsuarios> listaBeneficiosPorUsuarios) {
        this.listaBeneficiosPorUsuarios = listaBeneficiosPorUsuarios;
    }
    
    

    public BeneficiosPorUsuarios getBeneficioPorUsuario() {
        return beneficioPorUsuario;
    }

    public void setBeneficioPorUsuario(BeneficiosPorUsuarios beneficioPorUsuario) {
        this.beneficioPorUsuario = beneficioPorUsuario;
    }

    public List<Beneficios> getListaBeneficios() {
        return listaBeneficios;
    }

    public void setListaBeneficios(List<Beneficios> listaBeneficios) {
        this.listaBeneficios = listaBeneficios;
    }

    public Departamentos getDepartamentoLegajo1() {
        return departamentoLegajo1;
    }

    public void setDepartamentoLegajo1(Departamentos departamentoLegajo1) {
        this.departamentoLegajo1 = departamentoLegajo1;
    }

    public Departamentos getDepartamentoLegajo2() {
        return departamentoLegajo2;
    }

    public void setDepartamentoLegajo2(Departamentos departamentoLegajo2) {
        this.departamentoLegajo2 = departamentoLegajo2;
    }

    public Departamentos getDepartamentoLegajo3() {
        return departamentoLegajo3;
    }

    public void setDepartamentoLegajo3(Departamentos departamentoLegajo3) {
        this.departamentoLegajo3 = departamentoLegajo3;
    }

    public Departamentos getDepartamentoLegajo4() {
        return departamentoLegajo4;
    }

    public void setDepartamentoLegajo4(Departamentos departamentoLegajo4) {
        this.departamentoLegajo4 = departamentoLegajo4;
    }

    public Usuarios getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Usuarios funcionario) {
        this.funcionario = funcionario;
    }

    public List<Usuarios> getListaFuncionarios() {
        return listaFuncionarios;
    }

    public void setListaFuncionarios(List<Usuarios> listaFuncionarios) {
        this.listaFuncionarios = listaFuncionarios;
    }

    public List<Departamentos> getListaDepartamentos1() {
        return listaDepartamentos1;
    }

    public void setListaDepartamentos1(List<Departamentos> listaDepartamentos1) {
        this.listaDepartamentos1 = listaDepartamentos1;
    }

    public List<Departamentos> getListaDependencias() {
        return listaDependencias;
    }

    public void setListaDependencias(List<Departamentos> listaDependencias) {
        this.listaDependencias = listaDependencias;
    }

    public Departamentos getDependencia() {
        return dependencia;
    }

    public void setDependencia(Departamentos dependencia) {
        this.dependencia = dependencia;
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

    public List<Contactos> getListaContactos() {
        return listaContactos;
    }

    public void setListaContactos(List<Contactos> listaContactos) {
        this.listaContactos = listaContactos;
    }

    public List<Vinculos> getListaVinculos() {
        return listaVinculos;
    }

    public void setListaVinculos(List<Vinculos> listaVinculos) {
        this.listaVinculos = listaVinculos;
    }

    public Contactos getContacto() {
        return contacto;
    }

    public void setContacto(Contactos contacto) {
        this.contacto = contacto;
    }

    public UploadedFile getLegajoFile() {
        return legajoFile;
    }

    public void setLegajoFile(UploadedFile legajoFile) {
        this.legajoFile = legajoFile;
    }

    public UploadedFile getFotoFile() {
        return fotoFile;
    }

    public void setFotoFile(UploadedFile fotoFile) {
        this.fotoFile = fotoFile;
    }

    public List<DepartamentosPersona> getListaDepartamentosPersona() {
        return listaDepartamentosPersona;
    }

    public void setListaDepartamentosPersona(List<DepartamentosPersona> listaDepartamentosPersona) {
        this.listaDepartamentosPersona = listaDepartamentosPersona;
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

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        int pos = url.lastIndexOf(uri);
        url = url.substring(0, pos);

        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        par = ejbFacade.getEntityManager().createNamedQuery("ParametrosSistema.findById", ParametrosSistema.class).setParameter("id", Constantes.PARAMETRO_ID).getResultList();

        usuario = (Usuarios) session.getAttribute("Usuarios");
        List<Integer> ids = new ArrayList<>();
        ids.add(117);
        listaDepartamentosPersona = ejbFacade.getEntityManager().createNamedQuery("DepartamentosPersona.findByButIds", DepartamentosPersona.class).setParameter("ids", ids).getResultList();

        buscarFuncionarios();

        listaDepartamentos1 = ejbFacade.getEntityManager().createNamedQuery("Departamentos.findByDepartamentoPadreISNULL", Departamentos.class).setParameter("estado", new Estados(Constantes.ESTADO_USUARIO_AC)).getResultList();
        listaBeneficios = ejbFacade.getEntityManager().createNamedQuery("Beneficios.findAll", Beneficios.class).getResultList();

        beneficioPorUsuario = new BeneficiosPorUsuarios();
        funcionario = usuario;
        actualizarFuncionario();
        
    }
    
    public void actualizarFuncionario(){
        existeFoto = false;
        existeLegajo = false;
        docImprimirFoto = funcionario;
        if (funcionario != null) {
            if (funcionario.getFoto() != null) {
                File f = new File(par.get(0).getRutaFotosLegajo() + "/" + funcionario.getFoto());
                existeFoto = f.exists();
            }

            if (funcionario.getLegajo() != null) {
                File f = new File(par.get(0).getRutaLegajos() + "/" + funcionario.getLegajo());
                existeLegajo = f.exists();
            }
        }
        departamentoLegajo1 = funcionario.getDepartamento();
        actualizarDepartamentosLegajo(departamentoLegajo1);
    }
    
    public boolean renderedUsuario(){
        return filtroURL.verifPermiso(Constantes.PERMISO_VER_TODOS_LEGAJOS_SOLO_LECTURA);
    }

    public boolean deshabilitarVerLegajo() {
        return !existeLegajo;
    }

    private void buscarFuncionarios() {
        if (filtroURL.verifPermiso(Constantes.PERMISO_ADMIN_LEGAJOS)) {
            listaFuncionarios = ejbFacade.getEntityManager().createNamedQuery("Usuarios.findAllButAdmin", Usuarios.class).getResultList();
        } else {
            listaFuncionarios = null;
        }
    }

    public void imprimirLegajo() {

        HttpServletResponse httpServletResponse = null;
        if (funcionario != null) {
            try {
                JRBeanCollectionDataSource beanCollectionDataSource = null;

                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");
                HashMap map = new HashMap();

                Date fecha = ejbFacade.getSystemDate();

                map.put("fecha", format.format(fecha));
                map.put("hora", format2.format(fecha));
                map.put("usuario", usuario.getNombresApellidos());
                map.put("nombre", funcionario.getNombresApellidos());
                map.put("ci", funcionario.getCi());
                map.put("cantHijos", funcionario.getCantHijos());
                map.put("sexo", funcionario.getSexo().getDescripcion());
                map.put("estadoCivil", funcionario.getEstadoCivil() == null ? "" : funcionario.getEstadoCivil().getDescripcion());
                map.put("fechaNacimiento", funcionario.getFechaNacimiento());
                map.put("grupoSanguineo", funcionario.getGrupoSanguineo() == null ? "" : funcionario.getGrupoSanguineo().getDescripcion());
                map.put("alergias", funcionario.getAlergias());
                map.put("seguroMedico", funcionario.getSeguroMedico());
                map.put("direccion", funcionario.getDireccion());
                map.put("telefono", funcionario.getTelefono());
                map.put("celular", funcionario.getCelular());
                map.put("email", funcionario.getEmail());
                map.put("departamentoPais", funcionario.getDepartamentoPersona() == null ? "" : funcionario.getDepartamentoPersona().getDescripcion());
                map.put("ciudad", funcionario.getLocalidadPersona() == null ? "" : funcionario.getLocalidadPersona().getDescripcion());
                map.put("barrio", funcionario.getBarrio());
                map.put("tituloGrado", funcionario.getTitulosGrado());
                map.put("tituloPosgrado", funcionario.getTitulosPosgrado());
                map.put("otrosCursos", funcionario.getOtrosCursos());
                map.put("fechaIngreso", funcionario.getFechaIngreso());
                map.put("salario", funcionario.getSalario());
                map.put("bonificacion", funcionario.getBonificacion() == null ? "" : funcionario.getBonificacion().getDescripcion());
                map.put("horasExtras", (funcionario.isHorasExtra() ? "Si" : "No"));
                map.put("cargo", funcionario.getEstadoCivil() == null ? "" : funcionario.getCargo().getDescripcion());
                map.put("categoria", funcionario.getCategoria() == null ? "" : funcionario.getCategoria().getDescripcion());
                map.put("situacionLaboral", funcionario.getSituacionLaboral() == null ? "" : funcionario.getSituacionLaboral().getDescripcion());
                map.put("autoridad", departamentoLegajo1 == null ? "" : departamentoLegajo1.getNombre());
                map.put("direccionGeneral", departamentoLegajo2 == null ? "" : departamentoLegajo2.getNombre());
                map.put("direccionDpto", departamentoLegajo3 == null ? "" : departamentoLegajo3.getNombre());
                map.put("departamento", departamentoLegajo4 == null ? "" : departamentoLegajo4.getNombre());

                /*
                byte[] fileByte = null;
                try {
                    fileByte = Files.readAllBytes(new File(par.get(0).getRutaFotosLegajo() + "/" + getSelected().getFoto()).toPath());
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return;
                }
                
                Image image = ImageIO.read(new ByteArrayInputStream(fileByte));
                 */
                if (existeFoto) {
                    map.put("imagen", par.get(0).getRutaFotosLegajo() + "/" + funcionario.getFoto());
                } else {
                    map.put("imagen", par.get(0).getRutaFotosLegajo() + "/sinimagen.jpg");
                }

                String reportPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/reportes/reporteLegajo.jasper");
                JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, map, beanCollectionDataSource);

                httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

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
        }

        ///     JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte.pdf");
    }

    public void prepareEdit() {

        fechaActual = ejbFacade.getPrimerDiaMes(ejbFacade.getSystemDateOnly());

        if (getSelected() != null) {

            actualizarLocalidadPersona();

            dependencia = getSelected().getDepartamentoContrato();
            actualizarDepartamentos(dependencia);

            listaDependencias = ejbFacade.getEntityManager().createNamedQuery("Departamentos.findByEstado", Departamentos.class).setParameter("estado", new Estados(Constantes.ESTADO_USUARIO_AC)).getResultList();

            contacto = new Contactos();
            beneficioPorUsuario = new BeneficiosPorUsuarios();
            listaVinculos = ejbFacade.getEntityManager().createNamedQuery("Vinculos.findAll", Vinculos.class).getResultList();
            listaContactos = ejbFacade.getEntityManager().createNamedQuery("Contactos.findByUsuario", Contactos.class).setParameter("usuario", getSelected()).getResultList();
            listaBeneficiosPorUsuarios = ejbFacade.getEntityManager().createNamedQuery("BeneficiosPorUsuarios.findByUsuario", BeneficiosPorUsuarios.class).setParameter("usuario", getSelected()).getResultList();

            salario = getSelected().getSalario();
            getSelected().setSalarioAux(salario);
            if (getSelected().getFechaDesdeSalario() != null) {
                if (fechaActual.getTime() < getSelected().getFechaDesdeSalario().getTime()) {
                    fechaDesdeSalario = getSelected().getFechaDesdeSalario();
                } else {
                    fechaDesdeSalario = fechaActual;
                }
            } else {
                fechaDesdeSalario = fechaActual;
            }
        } else {
            fechaDesdeSalario = fechaActual;
        }
    }

    @Override
    public Usuarios prepareCreate(ActionEvent event) {
        Usuarios usu = super.prepareCreate(event);

        Pantallas pantalla = ejbFacade.getEntityManager().createNamedQuery("Pantallas.findById", Pantallas.class).setParameter("id", 1).getSingleResult();

        EstadosUsuario estado = ejbFacade.getEntityManager().createNamedQuery("EstadosUsuario.findByCodigo", EstadosUsuario.class).setParameter("codigo", "AC").getSingleResult();

        usu.setPantallaPrincipal(pantalla);
        usu.setEstado(estado);

        beneficioPorUsuario = new BeneficiosPorUsuarios();
        listaBeneficiosPorUsuarios = new ArrayList<>();
        
        contacto = new Contactos();
        listaContactos = new ArrayList<>();

        listaVinculos = ejbFacade.getEntityManager().createNamedQuery("Vinculos.findAll", Vinculos.class).getResultList();

        dependencia = null;
        departamento1 = null;
        departamento2 = null;
        departamento3 = null;
        departamento4 = null;

        listaDependencias = ejbFacade.getEntityManager().createNamedQuery("Departamentos.findByEstado", Departamentos.class).setParameter("estado", new Estados(Constantes.ESTADO_USUARIO_AC)).getResultList();

        return usu;
    }

    public void prepareVerDoc(Usuarios doc) {
        docImprimir = doc;
    }

    public void prepareVerFoto(Usuarios doc) {
        docImprimirFoto = doc;
    }

    public FuncionariosController() {
        // Inform the Abstract parent controller of the concrete Usuarios Entity
        super(Usuarios.class);
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

        if (usu != null) {

            if ("FE".equals(usu.getSexo().getCodigo())) {
                nombreUsu = "Bienvenida ";
            } else {
                nombreUsu = "Bienvenido ";
            }
            nombreUsu += usu.getNombresApellidos() + " - (" + usu.getUsuario() + ")";
        }
    }

    /*
    public Collection<Usuarios> getUsuariosDpto() {

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        Usuarios usu = (Usuarios) session.getAttribute("Usuarios");
        return ejbFacade.getEntityManager().createNamedQuery("Usuarios.findByDepartamento", Usuarios.class).setParameter("departamento", usu.getDepartamento()).getResultList();

    }

    public Collection<Usuarios> getFuncionariosSecretaria() {
        Departamentos dpto = departamentoController.prepareCreate(null);
        dpto.setId(5); // Secretaria
        return ejbFacade.getEntityManager().createNamedQuery("Usuarios.findByDepartamento", Usuarios.class).setParameter("departamento", dpto).getResultList();
    }

    public void setFuncionariosSecretaria(Collection<Usuarios> funcionariosSecretaria) {
        this.funcionariosSecretaria = funcionariosSecretaria;
    }
     */
    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public Date getFechaDesdeSalario() {
        return fechaDesdeSalario;
    }

    public void setFechaDesdeSalario(Date fechaDesdeSalario) {
        this.fechaDesdeSalario = fechaDesdeSalario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getNombreUsu() {
        return nombreUsu;
    }

    public void setNombreUsu(String nombreUsu) {
        this.nombreUsu = nombreUsu;
    }

    public String getCambioContrasena1() {
        return cambioContrasena1;
    }

    public void setCambioContrasena1(String cambioContrasena1) {
        this.cambioContrasena1 = cambioContrasena1;
    }

    public String getCambioContrasena2() {
        return cambioContrasena2;
    }

    public void setCambioContrasena2(String cambioContrasena2) {
        this.cambioContrasena2 = cambioContrasena2;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String obtenerHome() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        return (String) session.getAttribute("Home");
    }

    /**
     * Resets the "selected" attribute of any parent Entity controllers.
     */
    public void resetParents() {
        departamentoController.setSelected(null);
        empresaController.setSelected(null);
        estadoController.setSelected(null);
        cargoController.setSelected(null);
        categoriaController.setSelected(null);
        pantallaPrincipalController.setSelected(null);
        sexoController.setSelected(null);
    }

    /*
    public String navigateDepartamentosCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Departamentos_items", this.getSelected().getDepartamentosCollection());
        }
        return "/pages/departamentos/index";
    }

    public String navigateDepartamentosCollection1() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Departamentos_items", this.getSelected().getDepartamentosCollection1());
        }
        return "/pages/departamentos/index";
    }

    public String navigateRolesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Roles_items", this.getSelected().getRolesCollection());
        }
        return "/pages/roles/index";
    }

    public String navigateRolesCollection1() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Roles_items", this.getSelected().getRolesCollection1());
        }
        return "/pages/roles/index";
    }

    public void prepareDepartamento(ActionEvent event) {
        if (this.getSelected() != null && departamentoController.getSelected() == null) {
            departamentoController.setSelected(this.getSelected().getDepartamento());
        }
    }

    public void prepareEmpresa(ActionEvent event) {
        if (this.getSelected() != null && empresaController.getSelected() == null) {
            empresaController.setSelected(this.getSelected().getEmpresa());
        }
    }

    public void prepareEstado(ActionEvent event) {
        if (this.getSelected() != null && estadoController.getSelected() == null) {
            estadoController.setSelected(this.getSelected().getEstado());
        }
    }

    public void prepareCargo(ActionEvent event) {
        if (this.getSelected() != null && cargoController.getSelected() == null) {
            cargoController.setSelected(this.getSelected().getCargo());
        }
    }

    public void prepareCategoria(ActionEvent event) {
        if (this.getSelected() != null && categoriaController.getSelected() == null) {
            categoriaController.setSelected(this.getSelected().getCategoria());
        }
    }

    public void preparePantallaPrincipal(ActionEvent event) {
        if (this.getSelected() != null && pantallaPrincipalController.getSelected() == null) {
            pantallaPrincipalController.setSelected(this.getSelected().getPantallaPrincipal());
        }
    }

    public void prepareSexo(ActionEvent event) {
        if (this.getSelected() != null && sexoController.getSelected() == null) {
            sexoController.setSelected(this.getSelected().getSexo());
        }
    }

    public String navigateUsuariosCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Usuarios_items", this.getSelected().getUsuariosCollection());
        }
        return "/pages/usuarios/index";
    }

    public void prepareUsuarioAlta(ActionEvent event) {
        if (this.getSelected() != null && usuarioAltaController.getSelected() == null) {
            usuarioAltaController.setSelected(this.getSelected().getUsuarioAlta());
        }
    }

    public String navigateUsuariosCollection1() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Usuarios_items", this.getSelected().getUsuariosCollection1());
        }
        return "/pages/usuarios/index";
    }

    public void prepareUsuarioUltimoEstado(ActionEvent event) {
        if (this.getSelected() != null && usuarioUltimoEstadoController.getSelected() == null) {
            usuarioUltimoEstadoController.setSelected(this.getSelected().getUsuarioUltimoEstado());
        }
    }

    public String navigateRolesPorUsuariosCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("RolesPorUsuarios_items", this.getSelected().getRolesPorUsuariosCollection());
        }
        return "/pages/rolesPorUsuarios/index";
    }

    public String navigateRolesPorUsuariosCollection1() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("RolesPorUsuarios_items", this.getSelected().getRolesPorUsuariosCollection1());
        }
        return "/pages/rolesPorUsuarios/index";
    }

    public String navigateRolesPorUsuariosCollection2() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("RolesPorUsuarios_items", this.getSelected().getRolesPorUsuariosCollection2());
        }
        return "/pages/rolesPorUsuarios/index";
    }

    public String navigateProveedoresCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Proveedores_items", this.getSelected().getProveedoresCollection());
        }
        return "/pages/proveedores/index";
    }

    public String navigateProveedoresCollection1() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Proveedores_items", this.getSelected().getProveedoresCollection1());
        }
        return "/pages/proveedores/index";
    }

    public String navigateMovimientosReparacionBienesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("MovimientosReparacionBienes_items", this.getSelected().getMovimientosReparacionBienesCollection());
        }
        return "/pages/movimientosReparacionBienes/index";
    }

    public String navigateMovimientosReparacionBienesCollection1() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("MovimientosReparacionBienes_items", this.getSelected().getMovimientosReparacionBienesCollection1());
        }
        return "/pages/movimientosReparacionBienes/index";
    }

    public String navigateBienesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Bienes_items", this.getSelected().getBienesCollection());
        }
        return "/pages/bienes/index";
    }

    public String navigateBienesCollection1() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Bienes_items", this.getSelected().getBienesCollection1());
        }
        return "/pages/bienes/index";
    }

    public String navigateBienesCollection2() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("Bienes_items", this.getSelected().getBienesCollection2());
        }
        return "/pages/bienes/index";
    }

    public String navigateMovimientosBienesCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("MovimientosBienes_items", this.getSelected().getMovimientosBienesCollection());
        }
        return "/pages/movimientosBienes/index";
    }

    public String navigateMovimientosBienesCollection1() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("MovimientosBienes_items", this.getSelected().getMovimientosBienesCollection1());
        }
        return "/pages/movimientosBienes/index";
    }

    public String navigateMovimientosBienesCollection2() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("MovimientosBienes_items", this.getSelected().getMovimientosBienesCollection2());
        }
        return "/pages/movimientosBienes/index";
    }

    public String navigateMovimientosBienesCollection3() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("MovimientosBienes_items", this.getSelected().getMovimientosBienesCollection3());
        }
        return "/pages/movimientosBienes/index";
    }
     */
    public void actualizarDepartamentos(Departamentos dpto) {
        departamento1 = null;
        departamento2 = null;
        departamento3 = null;
        departamento4 = null;
        List<Departamentos> listaDptos = new ArrayList<>();
        listaDptos.add(dpto);
        Departamentos dptoActual = dpto;
        while (dptoActual != null) {
            if (dptoActual.getDepartamentoPadre() != null) {
                listaDptos.add(dptoActual.getDepartamentoPadre());
            }
            dptoActual = dptoActual.getDepartamentoPadre();
        }

        int contador = 0;
        for (int i = listaDptos.size() - 1; i >= 0; i--) {
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
            if (contador == 4) {
                break;
            }
        }

    }

    public void actualizarDepartamentosLegajo(Departamentos dpto) {
        departamentoLegajo1 = null;
        departamentoLegajo2 = null;
        departamentoLegajo3 = null;
        departamentoLegajo4 = null;
        List<Departamentos> listaDptos = new ArrayList<>();
        listaDptos.add(dpto);
        Departamentos dptoActual = dpto;
        while (dptoActual != null) {
            if (dptoActual.getDepartamentoPadre() != null) {
                listaDptos.add(dptoActual.getDepartamentoPadre());
            }
            dptoActual = dptoActual.getDepartamentoPadre();
        }

        int contador = 0;
        for (int i = listaDptos.size() - 1; i >= 0; i--) {
            switch (contador) {
                case 0:
                    departamentoLegajo1 = listaDptos.get(i);
                    break;
                case 1:
                    departamentoLegajo2 = listaDptos.get(i);
                    break;
                case 2:
                    departamentoLegajo3 = listaDptos.get(i);
                    break;
                case 3:
                    departamentoLegajo4 = listaDptos.get(i);
                    break;
                default:
                    break;
            }
            contador++;
            if (contador == 4) {
                break;
            }
        }

    }

    public List<Departamentos> obtenerDepartamentos(Departamentos dpto) {
        return ejbFacade.getEntityManager().createNamedQuery("Departamentos.findByEstado", Departamentos.class).setParameter("estado", new Estados(Constantes.ESTADO_USUARIO_AC)).getResultList();
    }

    
    public String loginControl() {

        Usuarios usu = query.loginControl(usuario.getUsuario(), contrasena, "AC");
        if (usu != null) {
            if (usu.getPantallaPrincipal() != null) {
                home = usu.getPantallaPrincipal().getUrl();
            } else {
                home = "index";
            }

            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Home", home);

            return home + ".xhtml?faces-redirect=true";
        }

        PrimeFaces.current().ajax().update("growl");
        FacesContext context = FacesContext.getCurrentInstance();

        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error de acceso"));
        return "";

    }

    public String cerrarSession() {
        this.usuario = null;
        this.contrasena = null;
        HttpSession httpSession;
        httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        httpSession.invalidate();//para borrar la session
        return "/login?faces-redirect=true";

    }
     
    public String navigateSalariosCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario_origen", getSelected());
            // FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ObservacionesDocumentosJudiciales_items", ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosJudiciales.findByDocumentoJudicial", ObservacionesDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected()).getResultList());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paginaVolver", "/pages/funcionarios/index");

        }
        return "/pages/rhSalarios/index";
    }

    public String navigateHistCargoCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario_origen", getSelected());
            // FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ObservacionesDocumentosJudiciales_items", ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosJudiciales.findByDocumentoJudicial", ObservacionesDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected()).getResultList());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paginaVolver", "/pages/funcionarios/index");

        }
        return "/pages/rhHistCargo/index";
    }

    public String navigateHistCategoriaCollection() {
        if (this.getSelected() != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario_origen", getSelected());
            // FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ObservacionesDocumentosJudiciales_items", ejbFacade.getEntityManager().createNamedQuery("ObservacionesDocumentosJudiciales.findByDocumentoJudicial", ObservacionesDocumentosJudiciales.class).setParameter("documentoJudicial", getSelected()).getResultList());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paginaVolver", "/pages/funcionarios/index");

        }
        return "/pages/rhHistCategoria/index";
    }

    public void decrementFechaDesdeSalario() throws Exception {

        if (fechaActual.getTime() < fechaDesdeSalario.getTime()) {
            Calendar c = Calendar.getInstance();
            c.setTime(fechaDesdeSalario);
            c.add(Calendar.MONTH, -1);

            fechaDesdeSalario = c.getTime();
        }

    }

    public void incrementFechaDesdeSalario() throws Exception {
        Calendar c = Calendar.getInstance();
        c.setTime(fechaDesdeSalario);
        c.add(Calendar.MONTH, 1);

        fechaDesdeSalario = c.getTime();
    }

    public void modificarSalario() {
        if (getSelected() != null) {
            getSelected().setSalarioAux(salario);
            getSelected().setFechaDesdeSalario(fechaDesdeSalario);
        }
    }

    public void actualizarLocalidadPersona() {
        if (getSelected() != null) {
            if (getSelected().getDepartamentoPersona() != null) {
                listaLocalidadesPersona = ejbFacade.getEntityManager().createNamedQuery("LocalidadesPersona.findByDepartamentoPersona", LocalidadesPersona.class).setParameter("departamentoPersona", getSelected().getDepartamentoPersona()).getResultList();
            }
        }

    }

    public boolean deshabilitarVerDoc(Usuarios doc) {
        if (doc != null) {
            return doc.getLegajo() == null;
        }
        return true;
    }

    public boolean deshabilitarVerFoto(Usuarios doc) {
        if (doc != null) {
            return doc.getFoto() == null;
        }
        return true;
    }

    public String getContent() {

        nombre = "";
        try {
            if (docImprimir != null) {

                byte[] fileByte = null;

                if (docImprimir.getLegajo() != null) {
                    try {
                        fileByte = Files.readAllBytes(new File(par.get(0).getRutaLegajos() + "/" + docImprimir.getLegajo()).toPath());
                    } catch (IOException ex) {
                        JsfUtil.addErrorMessage("No tiene documento adjunto");
                        content = "";
                    }
                }

                if (fileByte != null) {
                    Date fecha = ejbFacade.getSystemDate();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

                    String partes[] = docImprimir.getLegajo().split("[.]");
                    String ext = "pdf";

                    if (partes.length > 1) {
                        ext = partes[partes.length - 1];
                    }

                    nombre = session.getId() + "_" + simpleDateFormat.format(fecha) + "." + ext;
                    FileOutputStream outputStream = new FileOutputStream(Constantes.RUTA_ARCHIVOS_TEMP + "/tmp/" + nombre);
                    outputStream.write(fileByte);

                    outputStream.close();

                    // content = new DefaultStreamedContent(new ByteArrayInputStream(fileByte), "application/pdf");
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
            content = null;
        }
        // return par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/tmp/" + nombre;
        return url + "/tmp/" + nombre;
    }

    public void prepareCerrarDialogoVerDoc() {
        if (docImprimir != null) {
            File f = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/tmp/" + nombre);
            f.delete();

            docImprimir = null;
        }
    }

    public void verDoc() {

        if (existeLegajo) {
            HttpServletResponse httpServletResponse = null;
            try {
                httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

                httpServletResponse.setContentType("application/pdf");
                // httpServletResponse.setHeader("Content-Length", String.valueOf(getSelected().getDocumento().length));
                httpServletResponse.addHeader("Content-disposition", "filename=documento.pdf");

                ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
                FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.exporting", "true", Collections.<String, Object>emptyMap());

                byte[] fileByte = null;

                try {
                    fileByte = Files.readAllBytes(new File(par.get(0).getRutaLegajos() + "/" + funcionario.getLegajo()).toPath());
                    servletOutputStream.write(fileByte);
                    FacesContext.getCurrentInstance().responseComplete();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            } catch (Exception e) {
                FacesContext.getCurrentInstance().getExternalContext().addResponseCookie("cookie.chart.verdoc", "true", Collections.<String, Object>emptyMap());
                e.printStackTrace();

                if (httpServletResponse != null) {
                    if (httpServletResponse.getHeader("Content-disposition") == null) {
                        httpServletResponse.addHeader("Content-disposition", "inline");
                    } else {
                        httpServletResponse.setHeader("Content-disposition", "inline");
                    }

                }
            } finally {
                if (nombre != null) {
                    if (!"".equals(nombre)) {
                        File f = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/" + nombre);
                        f.delete();

                        docImprimir = null;
                    }
                }
            }
        }
    }

    public String getContentFoto() {

        nombreFoto = "";
        try {
            if (docImprimirFoto != null) {

                byte[] fileByte = null;

                if (docImprimirFoto.getFoto() != null) {
                    try {
                        fileByte = Files.readAllBytes(new File(par.get(0).getRutaFotosLegajo() + "/" + docImprimirFoto.getFoto()).toPath());
                    } catch (IOException ex) {
                        JsfUtil.addErrorMessage("No tiene documento adjunto");
                        contentFoto = "";
                    }
                }

                if (fileByte != null) {
                    Date fecha = ejbFacade.getSystemDate();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

                    String partes[] = docImprimirFoto.getFoto().split("[.]");
                    String ext = "jpg";

                    if (partes.length > 1) {
                        ext = partes[partes.length - 1];
                    }

                    nombreFoto = session.getId() + "_" + simpleDateFormat.format(fecha) + "." + ext;
                    FileOutputStream outputStream = new FileOutputStream(Constantes.RUTA_ARCHIVOS_TEMP + "/tmp/" + nombreFoto);
                    outputStream.write(fileByte);

                    outputStream.close();

                    // content = new DefaultStreamedContent(new ByteArrayInputStream(fileByte), "application/pdf");
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
            content = null;
        }
        // return par.getProtocolo() + "://" + par.getIpServidor() + ":" + par.getPuertoServidor() + "/tmp/" + nombre;
        return url + "/tmp/" + nombreFoto;
    }

    public void prepareCerrarDialogoVerFoto() {
        if (docImprimirFoto != null) {
            File f = new File(Constantes.RUTA_ARCHIVOS_TEMP + "/tmp/" + nombreFoto);
            f.delete();

            docImprimirFoto = null;
        }
    }

    public void agregarContacto() {

        if (contacto != null) {

            if (listaContactos == null) {
                listaContactos = new ArrayList<>();
            }

            Date fecha = ejbFacade.getSystemDateLastSecond();

            SimpleDateFormat format = new SimpleDateFormat("HHmmss");

            Contactos con = new Contactos(Integer.valueOf(format.format(fecha)), contacto.getNombresApellidos(), contacto.getTelefono(), contacto.getVinculo(), contacto.getUsuario());

            listaContactos.add(con);

            contacto = new Contactos();
        }
    }

    public void borrarContacto(Contactos contactoActual) {

        if (listaContactos != null) {
            listaContactos.remove(contactoActual);
        }
    }

    public void agregarBeneficio() {

        if (beneficioPorUsuario != null) {

            if (listaBeneficiosPorUsuarios == null) {
                listaBeneficiosPorUsuarios = new ArrayList<>();
            }

            Date fecha = ejbFacade.getSystemDateLastSecond();

            SimpleDateFormat format = new SimpleDateFormat("HHmmss");

            BeneficiosPorUsuarios con = new BeneficiosPorUsuarios(Integer.valueOf(format.format(fecha)), beneficioPorUsuario.getMonto(), beneficioPorUsuario.getBeneficio(), beneficioPorUsuario.getUsuario());

            listaBeneficiosPorUsuarios.add(con);

            beneficioPorUsuario = new BeneficiosPorUsuarios();
        }
    }

    public void borrarBeneficio(BeneficiosPorUsuarios beneficioPorUsuarioActual) {

        if (listaBeneficiosPorUsuarios != null) {
            listaBeneficiosPorUsuarios.remove(beneficioPorUsuarioActual);
        }
    }

    @Override
    public void save(ActionEvent event) {

        if (getSelected() != null) {
            if (getSelected().getCi() == null) {
                JsfUtil.addErrorMessage("Debe ingresar CI");
                return;
            } else if ("".equals(getSelected().getCi())) {
                JsfUtil.addErrorMessage("Debe ingresar CI.");
                return;
            }
            /*
            if(getSelected().getDepartamentoContrato() == null){
                JsfUtil.addErrorMessage("Debe ingresar dependencia");
                return;
            }
             */

            if (departamento4 != null) {
                getSelected().setDepartamentoContrato(departamento4);
            } else if (departamento3 != null) {
                getSelected().setDepartamentoContrato(departamento3);
            } else if (departamento2 != null) {
                getSelected().setDepartamentoContrato(departamento2);
            } else if (departamento1 != null) {
                getSelected().setDepartamentoContrato(departamento1);
            } else {
                JsfUtil.addErrorMessage("Debe elegir dependencia");
                return;
            }

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

            Date fecha = ejbFacade.getSystemDate();
            byte[] legajoBytes = null;
            if (legajoFile != null) {
                if (legajoFile.getContent().length > 0) {

                    try {
                        legajoBytes = IOUtils.toByteArray(legajoFile.getInputStream());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("Error al leer archivo de legajo");
                        return;
                    }

                }
            }

            byte[] fotoBytes = null;
            if (fotoFile != null) {
                if (fotoFile.getContent().length > 0) {

                    try {
                        fotoBytes = IOUtils.toByteArray(fotoFile.getInputStream());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("Error al leer foto");
                        return;
                    }

                }
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

            FileOutputStream fos = null;

            if (fotoBytes != null) {
                String nombreArchivoFoto = "fotolegajo_" + simpleDateFormat.format(fecha);
                nombreArchivoFoto += "_" + getSelected().getId() + ".jpg";
                try {
                    fos = new FileOutputStream(par.get(0).getRutaFotosLegajo() + File.separator + nombreArchivoFoto);
                    fos.write(fotoBytes);
                    fos.flush();
                    fos.close();
                    getSelected().setFoto(nombreArchivoFoto);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                    JsfUtil.addErrorMessage("No se pudo guadar foto");
                    return;
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JsfUtil.addErrorMessage("No se pudo guadar foto");
                    return;
                }
            }

            if (legajoBytes != null) {
                String nombreArchivoLegajo = "legajo_" + simpleDateFormat.format(fecha);
                nombreArchivoLegajo += "_" + getSelected().getId() + ".pdf";
                try {
                    fos = new FileOutputStream(par.get(0).getRutaLegajos() + File.separator + nombreArchivoLegajo);
                    fos.write(legajoBytes);
                    fos.flush();
                    fos.close();
                    getSelected().setLegajo(nombreArchivoLegajo);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                    JsfUtil.addErrorMessage("No se pudo guadar legajo");
                    return;
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JsfUtil.addErrorMessage("No se pudo guadar legajo");
                    return;
                }
            }

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usu);

            if (getSelected().verifSalario()) {

                // Verificar si el salario anterior caduco a mas de un mes
                RhSalarios salarioAnterior = null;
                Calendar fechaMasUnMes = null;
                try {
                    salarioAnterior = ejbFacade.getEntityManager().createNamedQuery("RhSalarios.findSalarioAnterior", RhSalarios.class).setParameter("usuario", getSelected()).getSingleResult();

                    fechaMasUnMes = Calendar.getInstance();
                    fechaMasUnMes.setTime(salarioAnterior.getFechaCaducidad());
                    fechaMasUnMes.add(Calendar.MONTH, 1);

                } catch (jakarta.persistence.NoResultException ex) {
                    ex.printStackTrace();

                    // Si no hay salario anterior, entonces es el primer salario y hacemos con que se guarde en rh_salarios con este codigo
                    fechaMasUnMes = Calendar.getInstance();
                    fechaMasUnMes.setTime(fecha);
                    fechaMasUnMes.add(Calendar.MONTH, -1);

                }

                // Ya paso mas de un mes desde que caduco, asi que trataremos el nuevo salario como un nuevo registro
                if (fechaMasUnMes.getTime().getTime() < fecha.getTime()) {

                    // Caducar el salario anterior
                    RhSalarios salarioVigente = null;
                    try {
                        salarioVigente = ejbFacade.getEntityManager().createNamedQuery("RhSalarios.findSalarioVigente", RhSalarios.class).setParameter("usuario", getSelected()).getSingleResult();

                        Calendar c = Calendar.getInstance();
                        c.setTime(getSelected().getFechaDesdeSalario());
                        c.add(Calendar.DATE, -1);

                        salarioVigente.setFechaCaducidad(c.getTime());
                        salarioController.setSelected(salarioVigente);
                        salarioController.save(null);
                    } catch (jakarta.persistence.NoResultException ex) {

                    }

                    // Insertar nuevo salario
                    getSelected().transferirSalario();
                    RhSalarios salario = salarioController.prepareCreate(null);
                    salario.setUsuarioAlta(usu);
                    salario.setUsuarioUltimoEstado(usu);
                    salario.setFechaHoraAlta(fecha);
                    salario.setFechaHoraUltimoEstado(fecha);
                    salario.setEmpresa(usu.getEmpresa());
                    salario.setSalario(getSelected().getSalario());
                    salario.setUsuario(getSelected());
                    salario.setFechaCaducidad(null);

                    salarioController.setSelected(salario);
                    salarioController.saveNew(null);
                } else {
                    Calendar c = Calendar.getInstance();
                    c.setTime(getSelected().getFechaDesdeSalario());
                    c.add(Calendar.DATE, -1);

                    salarioAnterior.setFechaCaducidad(c.getTime());
                    salarioController.setSelected(salarioAnterior);
                    salarioController.save(null);

                    // Caducar el salario anterior
                    RhSalarios salarioVigente = null;
                    try {
                        salarioVigente = ejbFacade.getEntityManager().createNamedQuery("RhSalarios.findSalarioVigente", RhSalarios.class).setParameter("usuario", getSelected()).getSingleResult();

                        getSelected().transferirSalario();
                        salarioVigente.setSalario(getSelected().getSalario());
                        salarioController.setSelected(salarioVigente);
                        salarioController.save(null);
                    } catch (jakarta.persistence.NoResultException ex) {

                    }
                }
            }

            if (getSelected().verifCargo()) {

                // Caducar Cargo anterior
                RhHistCargo cargoVigente = null;
                try {
                    cargoVigente = ejbFacade.getEntityManager().createNamedQuery("RhHistCargo.findCargoVigente", RhHistCargo.class).setParameter("usuario", getSelected()).getSingleResult();
                    cargoVigente.setFechaCaducidad(fecha);
                    histCargoController.setSelected(cargoVigente);
                    histCargoController.save(null);
                } catch (jakarta.persistence.NoResultException ex) {

                }

                // Insertar nuevo cargo
                getSelected().transferirCargo();
                RhHistCargo cargo = histCargoController.prepareCreate(null);
                cargo.setUsuarioAlta(usu);
                cargo.setUsuarioUltimoEstado(usu);
                cargo.setFechaHoraAlta(fecha);
                cargo.setFechaHoraUltimoEstado(fecha);
                cargo.setEmpresa(usu.getEmpresa());
                cargo.setCargo(getSelected().getCargo());
                cargo.setUsuario(getSelected());
                cargo.setFechaCaducidad(null);

                histCargoController.setSelected(cargo);
                histCargoController.saveNew(null);
            }

            if (getSelected().verifCategoria()) {

                // Caducar Cargo anterior
                RhHistCategoria categoriaVigente = null;
                try {
                    categoriaVigente = ejbFacade.getEntityManager().createNamedQuery("RhHistCategoria.findCategoriaVigente", RhHistCategoria.class).setParameter("usuario", getSelected()).getSingleResult();
                    categoriaVigente.setFechaCaducidad(fecha);
                    histCategoriaController.setSelected(categoriaVigente);
                    histCategoriaController.save(null);
                } catch (jakarta.persistence.NoResultException ex) {

                }

                // Insertar nueva categoria
                getSelected().transferirCategoria();
                RhHistCategoria categoria = histCategoriaController.prepareCreate(null);
                categoria.setUsuarioAlta(usu);
                categoria.setUsuarioUltimoEstado(usu);
                categoria.setFechaHoraAlta(fecha);
                categoria.setFechaHoraUltimoEstado(fecha);
                categoria.setEmpresa(usu.getEmpresa());
                categoria.setCategoria(getSelected().getCategoria());
                categoria.setUsuario(getSelected());
                categoria.setFechaCaducidad(null);

                histCategoriaController.setSelected(categoria);
                histCategoriaController.saveNew(null);
            }

            if (listaContactos != null) {

                List<Contactos> listaContactosActual = ejbFacade.getEntityManager().createNamedQuery("Contactos.findByUsuario", Contactos.class).setParameter("usuario", getSelected()).getResultList();

                for (Contactos con : listaContactos) {

                    boolean encontro = false;
                    for (Contactos c : listaContactosActual) {
                        if (c.equals(con)) {
                            encontro = true;
                        }
                    }

                    if (!encontro) {
                        con.setUsuario(getSelected());

                        contactosController.setSelected(con);
                        contactosController.saveNew(null);
                    }
                }

                for (Contactos c : listaContactosActual) {
                    boolean encontro = false;
                    for (Contactos con : listaContactos) {
                        if (c.equals(con)) {
                            encontro = true;
                        }
                    }

                    if (!encontro) {
                        contactosController.setSelected(c);
                        contactosController.delete(null);
                    }
                }
            }

            if (listaBeneficiosPorUsuarios != null) {

                List<BeneficiosPorUsuarios> listaBeneficiosPorUsuariosActual = ejbFacade.getEntityManager().createNamedQuery("BeneficiosPorUsuarios.findByUsuario", BeneficiosPorUsuarios.class).setParameter("usuario", getSelected()).getResultList();

                for (BeneficiosPorUsuarios con : listaBeneficiosPorUsuarios) {

                    boolean encontro = false;
                    for (BeneficiosPorUsuarios c : listaBeneficiosPorUsuariosActual) {
                        if (c.equals(con)) {
                            encontro = true;
                        }
                    }

                    if (!encontro) {
                        con.setUsuario(getSelected());

                        beneficiosPorUsuariosController.setSelected(con);
                        beneficiosPorUsuariosController.saveNew(null);
                    }
                }

                for (BeneficiosPorUsuarios c : listaBeneficiosPorUsuariosActual) {
                    boolean encontro = false;
                    for (BeneficiosPorUsuarios con : listaBeneficiosPorUsuarios) {
                        if (c.equals(con)) {
                            encontro = true;
                        }
                    }

                    if (!encontro) {
                        beneficiosPorUsuariosController.setSelected(c);
                        beneficiosPorUsuariosController.delete(null);
                    }
                }
            }
        }

        super.save(event);
    }

    public void saveContrasena(ActionEvent event) {

        if (getSelected() != null) {

            if (cambioContrasena1.equals(cambioContrasena2)) {

                ((Usuarios) getSelected()).setContrasena(cambioContrasena1);
                cambioContrasena1 = "";
                cambioContrasena2 = "";
                this.save(event);

            } else {
                JsfUtil.addErrorMessage("Contrasenas no coinciden");
            }
        }
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

            if (getSelected().getCi() == null) {
                JsfUtil.addErrorMessage("Debe ingresar CI");
                return;
            }

            /*if(getSelected().getDepartamentoContrato() == null){
                JsfUtil.addErrorMessage("Debe ingresar dependencia segun contrato");
                return;
            }else*/ if ("".equals(getSelected().getCi())) {
                JsfUtil.addErrorMessage("Debe ingresar CI.");
                return;
            }

            if (departamento4 != null) {
                getSelected().setDepartamentoContrato(departamento4);
                getSelected().setDepartamento(departamento4);
            } else if (departamento3 != null) {
                getSelected().setDepartamentoContrato(departamento3);
                getSelected().setDepartamento(departamento3);
            } else if (departamento2 != null) {
                getSelected().setDepartamentoContrato(departamento2);
                getSelected().setDepartamento(departamento2);
            } else if (departamento1 != null) {
                getSelected().setDepartamentoContrato(departamento1);
                getSelected().setDepartamento(departamento1);
            } else {
                JsfUtil.addErrorMessage("Debe elegir dependencia");
                return;
            }

            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

            Usuarios usu = (Usuarios) session.getAttribute("Usuarios");

            byte[] legajoBytes = null;
            if (legajoFile != null) {
                if (legajoFile.getContent().length > 0) {

                    try {
                        legajoBytes = IOUtils.toByteArray(legajoFile.getInputStream());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("Error al leer archivo de legajo");
                        return;
                    }

                }
            }

            byte[] fotoBytes = null;
            if (fotoFile != null) {
                if (fotoFile.getContent().length > 0) {

                    try {
                        fotoBytes = IOUtils.toByteArray(fotoFile.getInputStream());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("Error al leer foto");
                        return;
                    }

                }
            }

            Date fecha = ejbFacade.getSystemDate();

            getSelected().setFechaHoraUltimoEstado(fecha);
            getSelected().setUsuarioUltimoEstado(usu);
            getSelected().setFechaHoraAlta(fecha);
            getSelected().setUsuarioAlta(usu);
            getSelected().setEmpresa(usu.getEmpresa());
            super.saveNew(event);

            if (getSelected().getId() != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

                FileOutputStream fos = null;

                if (fotoBytes != null) {
                    String nombreArchivoFoto = "fotolegajo_" + simpleDateFormat.format(fecha);
                    nombreArchivoFoto += "_" + getSelected().getId() + ".jpg";
                    try {
                        fos = new FileOutputStream(par.get(0).getRutaFotosLegajo() + File.separator + nombreArchivoFoto);
                        fos.write(fotoBytes);
                        fos.flush();
                        fos.close();
                        getSelected().setFoto(nombreArchivoFoto);
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo guadar foto");
                        return;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo guadar foto");
                        return;
                    }
                }

                if (legajoBytes != null) {
                    String nombreArchivoLegajo = "legajo_" + simpleDateFormat.format(fecha);
                    nombreArchivoLegajo += "_" + getSelected().getId() + ".pdf";
                    try {
                        fos = new FileOutputStream(par.get(0).getRutaLegajos() + File.separator + nombreArchivoLegajo);
                        fos.write(legajoBytes);
                        fos.flush();
                        fos.close();
                        getSelected().setLegajo(nombreArchivoLegajo);
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo guadar legajo");
                        return;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JsfUtil.addErrorMessage("No se pudo guadar legajo");
                        return;
                    }
                }

                if (fotoBytes != null || legajoBytes != null) {
                    super.save(event);
                }

                if (getSelected().getSalario() != null) {

                    getSelected().setFechaDesdeSalario(ejbFacade.getPrimerDiaMes(fecha));

                    // Insertar nuevo salario
                    RhSalarios salario = salarioController.prepareCreate(null);
                    salario.setUsuarioAlta(usu);
                    salario.setUsuarioUltimoEstado(usu);
                    salario.setFechaHoraAlta(fecha);
                    salario.setFechaHoraUltimoEstado(fecha);
                    salario.setEmpresa(usu.getEmpresa());
                    salario.setSalario(getSelected().getSalario());
                    salario.setUsuario(getSelected());
                    salario.setFechaCaducidad(null);

                    salarioController.setSelected(salario);
                    salarioController.saveNew(null);
                }

                if (getSelected().getCargo() != null) {

                    // Caducar Cargo anterior
                    // Insertar nuevo cargo
                    RhHistCargo cargo = histCargoController.prepareCreate(null);
                    cargo.setUsuarioAlta(usu);
                    cargo.setUsuarioUltimoEstado(usu);
                    cargo.setFechaHoraAlta(fecha);
                    cargo.setFechaHoraUltimoEstado(fecha);
                    cargo.setEmpresa(usu.getEmpresa());
                    cargo.setCargo(getSelected().getCargo());
                    cargo.setUsuario(getSelected());
                    cargo.setFechaCaducidad(null);

                    histCargoController.setSelected(cargo);
                    histCargoController.saveNew(null);
                }

                if (getSelected().getCategoria() != null) {

                    // Insertar nueva categoria
                    RhHistCategoria categoria = histCategoriaController.prepareCreate(null);
                    categoria.setUsuarioAlta(usu);
                    categoria.setUsuarioUltimoEstado(usu);
                    categoria.setFechaHoraAlta(fecha);
                    categoria.setFechaHoraUltimoEstado(fecha);
                    categoria.setEmpresa(usu.getEmpresa());
                    categoria.setCategoria(getSelected().getCategoria());
                    categoria.setUsuario(getSelected());
                    categoria.setFechaCaducidad(null);

                    histCategoriaController.setSelected(categoria);
                    histCategoriaController.saveNew(null);
                }

                if (listaContactos != null) {
                    for (Contactos con : listaContactos) {

                        con.setUsuario(getSelected());

                        contactosController.setSelected(con);
                        contactosController.saveNew(null);
                    }
                }

                if (listaBeneficiosPorUsuarios != null) {
                    for (BeneficiosPorUsuarios con : listaBeneficiosPorUsuarios) {

                        con.setUsuario(getSelected());

                        beneficiosPorUsuariosController.setSelected(con);
                        beneficiosPorUsuariosController.saveNew(null);
                    }
                }
            }
            buscarFuncionarios();
        }
    }
}
