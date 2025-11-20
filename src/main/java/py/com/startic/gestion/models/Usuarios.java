/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
//import org.apache.commons.lang3.StringUtils;
import py.com.startic.gestion.controllers.Utils;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "usuarios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuarios.control", query = "SELECT u FROM Usuarios u WHERE u.usuario = :usuario AND u.contrasena = :contrasena AND u.estado = :estado")
    , @NamedQuery(name = "Usuarios.findAll", query = "SELECT u FROM Usuarios u")
    , @NamedQuery(name = "Usuarios.findAllButAdmin", query = "SELECT u FROM Usuarios u WHERE u.id <> 2 ORDER BY u.nombresApellidos")
    , @NamedQuery(name = "Usuarios.findByUsuarioNOTNULL", query = "SELECT u FROM Usuarios u WHERE u.usuario is not null AND u.estado.codigo = 'AC' AND u.id <> 2 ORDER BY u.nombresApellidos")
    , @NamedQuery(name = "Usuarios.findSuperiorInmediato", query = "SELECT u FROM Usuarios u WHERE u.usuario is not null AND u.estado.codigo = 'AC' AND u.id <> 2 AND u.departamento = :departamento AND u.id in (SELECT o.rolesPorUsuariosPK.usuario FROM RolesPorUsuarios o WHERE o.rolesPorUsuariosPK.rol = :rol AND o.estado.codigo = 'AC') ORDER BY u.nombresApellidos")
    , @NamedQuery(name = "Usuarios.findByDepartamento", query = "SELECT u FROM Usuarios u WHERE u.departamento = :departamento AND u.estado.codigo = 'AC'")
    , @NamedQuery(name = "Usuarios.findByEstadoANDEstadoDepartamentoANDRol", query = "SELECT u FROM Usuarios u WHERE u.usuario is not null AND u.estado.codigo = 'AC' AND u.id <> 2 AND u.departamento.estado.codigo = 'AC' AND u.id IN (select r.rolesPorUsuariosPK.usuario from RolesPorUsuarios r WHERE r.rolesPorUsuariosPK.rol = :rol AND r.estado.codigo = 'AC') ORDER BY u.departamento.nombre, u.nombresApellidos")
    , @NamedQuery(name = "Usuarios.findByEstadoANDEstadoDepartamentoANDRolANDDepartamentoId", query = "SELECT u FROM Usuarios u WHERE u.usuario is not null AND u.estado.codigo = 'AC' AND u.id <> 2 AND u.departamento.estado.codigo = 'AC' AND u.departamento.id = :departamento AND u.id IN (select r.rolesPorUsuariosPK.usuario from RolesPorUsuarios r WHERE r.rolesPorUsuariosPK.rol = :rol AND r.estado.codigo = 'AC') ORDER BY u.departamento.nombre, u.nombresApellidos")
    , @NamedQuery(name = "Usuarios.findByEstado", query = "SELECT u FROM Usuarios u WHERE u.estado.codigo = :estado ORDER BY u.nombresApellidos")
    , @NamedQuery(name = "Usuarios.findTransferir", query = "SELECT r.usuarios FROM RolesPorUsuarios r WHERE r.usuarios in (SELECT u.usuarios FROM RolesPorUsuarios u WHERE u.roles in (SELECT d.rolFinal FROM FlujosDocumento d WHERE d.flujosDocumentoPK.tipoDocumento = :tipoDocumento AND d.flujosDocumentoPK.estadoDocumentoActual = :estadoDocumentoActual) AND u.usuarios.estado.codigo = 'AC' AND u.estado.codigo = 'AC') AND r.rolesPorUsuariosPK.rol = :rolEncargado AND r.estado.codigo = 'AC' ORDER BY r.usuarios.nombresApellidos")
    , @NamedQuery(name = "Usuarios.findTransferirArchivoAdm", query = "SELECT r FROM Usuarios r WHERE r.id in (SELECT u.usuarios.id FROM RolesPorUsuarios u WHERE u.roles in (SELECT d.rolFinal FROM FlujosDocumentoAdministrativo d WHERE d.flujosDocumentoAdministrativoPK.tipoDocumentoAdministrativo = :tipoDocumentoAdministrativo AND d.flujosDocumentoAdministrativoPK.estadoDocumentoActual = :estadoDocumentoActual) AND u.usuarios.estado.codigo = 'AC' AND u.estado.codigo = 'AC') ORDER BY r.nombresApellidos")
    , @NamedQuery(name = "Usuarios.findTransferirPedido", query = "SELECT r.usuarios FROM RolesPorUsuarios r WHERE r.usuarios in (SELECT u.usuarios FROM RolesPorUsuarios u WHERE u.roles in (SELECT d.rolFinal FROM FlujosDocumento d WHERE d.flujosDocumentoPK.tipoDocumento = :tipoDocumento AND d.flujosDocumentoPK.estadoDocumentoActual = :estadoDocumentoActual) AND u.usuarios.estado.codigo = 'AC' AND u.estado.codigo = 'AC') AND r.estado.codigo = 'AC' ORDER BY r.usuarios.nombresApellidos")
    , @NamedQuery(name = "Usuarios.findTransferirDpto", query = "SELECT u.usuarios FROM RolesPorUsuarios u WHERE u.roles in (SELECT d.rolFinal FROM FlujosDocumento d WHERE d.flujosDocumentoPK.tipoDocumento = :tipoDocumento AND d.flujosDocumentoPK.estadoDocumentoActual = :estadoDocumentoActual) AND u.usuarios.departamento = :departamento AND u.usuarios.estado.codigo = 'AC' AND u.estado.codigo = 'AC' ORDER BY u.usuarios.nombresApellidos")
    , @NamedQuery(name = "Usuarios.findTransferirDpto2", query = "SELECT u.usuarios FROM RolesPorUsuarios u WHERE u.roles in (SELECT d.rolFinal FROM FlujosDocumento d WHERE d.flujosDocumentoPK.tipoDocumento = :tipoDocumento AND d.flujosDocumentoPK.estadoDocumentoActual = :estadoDocumentoActual) AND u.usuarios.estado.codigo = 'AC'  ORDER BY u.usuarios.nombresApellidos")
    , @NamedQuery(name = "Usuarios.findById", query = "SELECT u FROM Usuarios u WHERE u.id = :id")
    , @NamedQuery(name = "Usuarios.findByUsuario", query = "SELECT u FROM Usuarios u WHERE u.usuario = :usuario")
    , @NamedQuery(name = "Usuarios.findByContrasena", query = "SELECT u FROM Usuarios u WHERE u.contrasena = :contrasena")
    , @NamedQuery(name = "Usuarios.findByNombresApellidos", query = "SELECT u FROM Usuarios u WHERE u.nombresApellidos = :nombresApellidos")
    , @NamedQuery(name = "Usuarios.findByDireccion", query = "SELECT u FROM Usuarios u WHERE u.direccion = :direccion")
    , @NamedQuery(name = "Usuarios.findByTelefono", query = "SELECT u FROM Usuarios u WHERE u.telefono = :telefono")
    , @NamedQuery(name = "Usuarios.findByCi", query = "SELECT u FROM Usuarios u WHERE u.ci = :ci")
    , @NamedQuery(name = "Usuarios.findByFechaHoraAlta", query = "SELECT u FROM Usuarios u WHERE u.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "Usuarios.findByFechaHoraUltimoEstado", query = "SELECT u FROM Usuarios u WHERE u.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")
    , @NamedQuery(name = "Usuarios.findByPersona", query = "SELECT u FROM Usuarios u WHERE u.persona = :persona")
    , @NamedQuery(name = "Usuarios.findByCelular", query = "SELECT u FROM Usuarios u WHERE u.celular = :celular")})
public class Usuarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "usuario")
    private String usuario;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "contrasena")
    private String contrasena;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "nombres_apellidos")
    private String nombresApellidos;
    @Size(max = 500)
    @Column(name = "direccion")
    private String direccion;
    @Size(max = 20)
    @Column(name = "telefono")
    private String telefono;
    @Basic(optional = true)
    @Size(max = 20)
    @Column(name = "ci")
    private String ci;
    @Basic(optional = true)
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.DATE)
    private Date fechaIngreso;
    @Basic(optional = true)
    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_ultimo_estado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraUltimoEstado;
    @Size(max = 200)
    @Column(name = "celular")
    private String celular;
    @Size(max = 200)
    @Column(name = "email")
    private String email;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "usuarioAlta")
    private Collection<Departamentos> departamentosCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "usuarioUltimoEstado")
    private Collection<Departamentos> departamentosCollection1;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "usuarioAlta")
    private Collection<Roles> rolesCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "usuarioUltimoEstado")
    private Collection<Roles> rolesCollection1;
    @JoinColumn(name = "departamento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Departamentos departamento;
    @JoinColumn(name = "departamento_contrato", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Departamentos departamentoContrato;
    @JoinColumn(name = "departamento_persona", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DepartamentosPersona departamentoPersona;
    @JoinColumn(name = "localidad_persona", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LocalidadesPersona localidadPersona;
    @Size(max = 200)
    @Column(name = "barrio")
    private String barrio;
    @Lob
    @Size(max = 65535)
    @Column(name = "titulos_grado")
    private String titulosGrado;
    @Lob
    @Size(max = 65535)
    @Column(name = "titulos_posgrado")
    private String titulosPosgrado;
    @Lob
    @Size(max = 65535)
    @Column(name = "cursando")
    private String cursando;
    @Lob
    @Size(max = 65535)
    @Column(name = "otros_cursos")
    private String otrosCursos;
    @Size(max = 200)
    @Column(name = "foto")
    private String foto;
    @Size(max = 200)
    @Column(name = "legajo")
    private String legajo;
    @Size(max = 200)
    @Column(name = "alergias")
    private String alergias;
    @Size(max = 200)
    @Column(name = "enfermedades_cronicas")
    private String enfermedadesCronicas;
    @Size(max = 200)
    @Column(name = "seguro_medico")
    private String seguroMedico;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "grupo_sanguineo", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private GruposSanguineo grupoSanguineo;
    @JoinColumn(name = "situacion_laboral", referencedColumnName = "codigo")
    @ManyToOne(optional = true)
    private SituacionesLaboral situacionLaboral;
    @JoinColumn(name = "estado_civil", referencedColumnName = "codigo")
    @ManyToOne(optional = true)
    private EstadosCivil estadoCivil;
    @JoinColumn(name = "estado", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private EstadosUsuario estado;
    @JoinColumn(name = "pantalla_principal", referencedColumnName = "id")
    @ManyToOne
    private Pantallas pantallaPrincipal;
    @JoinColumn(name = "sexo", referencedColumnName = "codigo")
    @ManyToOne(optional = true)
    private Sexos sexo;
    @JoinColumn(name = "persona", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Personas persona;
    @OneToMany(mappedBy = "usuarioAlta")
    private Collection<Usuarios> usuariosCollection;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne
    private Usuarios usuarioAlta;
    @OneToMany(mappedBy = "usuarioUltimoEstado")
    private Collection<Usuarios> usuariosCollection1;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "cargo", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private RhCargos cargo;
    @JoinColumn(name = "categoria", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private RhCategorias categoria;
    @JoinColumn(name = "bonificacion", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Bonificaciones bonificacion;
    @Basic(optional = true)
    @Column(name = "fecha_desde_salario")
    @Temporal(TemporalType.DATE)
    private Date fechaDesdeSalario;
    @Basic(optional = true)
    @Column(name = "salario")
    private BigDecimal salario;
    @Basic(optional = true)
    @Column(name = "cant_hijos")
    private Integer cantHijos;
    @Transient
    private BigDecimal salarioAux;
    @Transient
    private RhCargos cargoAux;
    @Transient
    private RhCategorias categoriaAux;
    @Transient
    private boolean incluidoAutomaticamente;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "usuarioAlta")
    private Collection<RolesPorUsuarios> rolesPorUsuariosCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "usuarioUltimoEstado")
    private Collection<RolesPorUsuarios> rolesPorUsuariosCollection1;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "usuarios")
    private Collection<RolesPorUsuarios> rolesPorUsuariosCollection2;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "usuarioAlta")
    private Collection<Proveedores> proveedoresCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "usuarioUltimoEstado")
    private Collection<Proveedores> proveedoresCollection1;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "usuarioAlta")
    private Collection<MovimientosReparacionBienes> movimientosReparacionBienesCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "usuarioUltimoEstado")
    private Collection<MovimientosReparacionBienes> movimientosReparacionBienesCollection1;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "usuarioAlta")
    private Collection<Bienes> bienesCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "usuarioUltimoEstado")
    private Collection<Bienes> bienesCollection1;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "responsable")
    private Collection<Bienes> bienesCollection2;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "usuarioAlta")
    private Collection<MovimientosBienes> movimientosBienesCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "responsableOrigen")
    private Collection<MovimientosBienes> movimientosBienesCollection1;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "responsableDestino")
    private Collection<MovimientosBienes> movimientosBienesCollection2;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "usuarioUltimoEstado")
    private Collection<MovimientosBienes> movimientosBienesCollection3;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "usuarioAlta")
    private Collection<ObservacionesDocumentosJudiciales> observacionesDocumentosJudicialesCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "usuarioUltimoEstado")
    private Collection<ObservacionesDocumentosJudiciales> observacionesDocumentosJudicialesCollection1;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "usuarioAlta")
    private Collection<TiposResolucion> tipoResolucionCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "usuarioUltimoEstado")
    private Collection<TiposResolucion> tipoResolucionCollectionCollection1;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "usuarioAlta")
    private Collection<DocumentosJudiciales> documentosJudicialesCollection;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "usuarioUltimoEstado")
    private Collection<DocumentosJudiciales> documentosJudicialesCollection1;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "usuarioUltimaObservacion")
    private Collection<DocumentosJudiciales> documentosJudicialesCollection2;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "responsable")
    private Collection<DocumentosJudiciales> documentosJudicialesCollection3;
    @Transient
    private EstadosDocumento estadoDocumentoFinal;
    @Transient
    private EstadosDocumentoAdministrativo estadoDocumentoFinalAdministrativo;
    @Column(name = "tiene_token")
    private boolean tieneToken;
    @Column(name = "horas_extra")
    private boolean horasExtra;

    public Usuarios() {
    }

    public Usuarios(Integer id) {
        this.id = id;
    }

    public Usuarios(Integer id, String nombresApellidos, EstadosDocumento estadoDocumentoFinal) {
        this.id = id;
        this.nombresApellidos = nombresApellidos;
        this.estadoDocumentoFinal = estadoDocumentoFinal;
    }

    public Usuarios(Integer id, String usuario, String contrasena, String nombresApellidos, String ci, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.nombresApellidos = nombresApellidos;
        this.ci = ci;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitulosGrado() {
        return titulosGrado;
    }

    public void setTitulosGrado(String titulosGrado) {
        this.titulosGrado = titulosGrado;
    }

    public String getTitulosPosgrado() {
        return titulosPosgrado;
    }

    public void setTitulosPosgrado(String titulosPosgrado) {
        this.titulosPosgrado = titulosPosgrado;
    }

    public String getCursando() {
        return cursando;
    }

    public void setCursando(String cursando) {
        this.cursando = cursando;
    }

    public String getOtrosCursos() {
        return otrosCursos;
    }

    public void setOtrosCursos(String otrosCursos) {
        this.otrosCursos = otrosCursos;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getLegajo() {
        return legajo;
    }

    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public EstadosCivil getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(EstadosCivil estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getNombresApellidos() {
        if(nombresApellidos != null){
            return Utils.capitalizeString(nombresApellidos);
            // return StringUtils.capitalize(nombresApellidos.toLowerCase());
        }
        return null;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Departamentos getDepartamentoContrato() {
        return departamentoContrato;
    }

    public void setDepartamentoContrato(Departamentos departamentoContrato) {
        this.departamentoContrato = departamentoContrato;
    }

    public DepartamentosPersona getDepartamentoPersona() {
        return departamentoPersona;
    }

    public void setDepartamentoPersona(DepartamentosPersona departamentoPersona) {
        this.departamentoPersona = departamentoPersona;
    }

    public LocalidadesPersona getLocalidadPersona() {
        return localidadPersona;
    }

    public void setLocalidadPersona(LocalidadesPersona localidadPersona) {
        this.localidadPersona = localidadPersona;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getAlergias() {
        return alergias;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    public String getEnfermedadesCronicas() {
        return enfermedadesCronicas;
    }

    public void setEnfermedadesCronicas(String enfermedadesCronicas) {
        this.enfermedadesCronicas = enfermedadesCronicas;
    }

    public String getSeguroMedico() {
        return seguroMedico;
    }

    public void setSeguroMedico(String seguroMedico) {
        this.seguroMedico = seguroMedico;
    }

    public SituacionesLaboral getSituacionLaboral() {
        return situacionLaboral;
    }

    public void setSituacionLaboral(SituacionesLaboral situacionLaboral) {
        this.situacionLaboral = situacionLaboral;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public void setNombresApellidos(String nombresApellidos) {
        this.nombresApellidos = nombresApellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Integer getCantHijos() {
        return cantHijos;
    }

    public void setCantHijos(Integer cantHijos) {
        this.cantHijos = cantHijos;
    }

    public Date getFechaDesdeSalario() {
        return fechaDesdeSalario;
    }

    public void setFechaDesdeSalario(Date fechaDesdeSalario) {
        this.fechaDesdeSalario = fechaDesdeSalario;
    }

    public BigDecimal getSalarioAux() {
        return salario;
    }

    public GruposSanguineo getGrupoSanguineo() {
        return grupoSanguineo;
    }

    public void setGrupoSanguineo(GruposSanguineo grupoSanguineo) {
        this.grupoSanguineo = grupoSanguineo;
    }

    public EstadosDocumentoAdministrativo getEstadoDocumentoFinalAdministrativo() {
        return estadoDocumentoFinalAdministrativo;
    }

    public void setEstadoDocumentoFinalAdministrativo(EstadosDocumentoAdministrativo estadoDocumentoFinalAdministrativo) {
        this.estadoDocumentoFinalAdministrativo = estadoDocumentoFinalAdministrativo;
    }
    
    @XmlTransient
    public BigDecimal getSalarioAuxReal() {
        return salarioAux;
    }

    public void setSalarioAux(BigDecimal salarioAux) {
        this.salarioAux = salarioAux;
    }

    public boolean isIncluidoAutomaticamente() {
        return incluidoAutomaticamente;
    }

    public void setIncluidoAutomaticamente(boolean incluidoAutomaticamente) {
        this.incluidoAutomaticamente = incluidoAutomaticamente;
    }

    public boolean isHorasExtra() {
        return horasExtra;
    }

    public void setHorasExtra(boolean horasExtra) {
        this.horasExtra = horasExtra;
    }
    
    @XmlTransient
    public boolean verifSalario(){
        if((salario == null &&  salarioAux != null) || (salario != null &&  salarioAux == null)){
            return true;
        }else if(salario == null &&  salarioAux == null){
            return false;
        }
        
        return !salario.equals(salarioAux);
    }
    
    @XmlTransient
    public void transferirSalario(){
        salario = salarioAux;
    }

    public RhCargos getCargoAux() {
        return cargo;
    }

    public void setCargoAux(RhCargos cargoAux) {
        this.cargoAux = cargoAux;
    }
    
    @XmlTransient
    public boolean verifCargo(){
        if((cargo == null &&  cargoAux != null) || (cargo != null &&  cargoAux == null)){
            return true;
        }else if(cargo == null &&  cargoAux == null){
            return false;
        }
        
        return !cargo.equals(cargoAux);
    }
    
    @XmlTransient
    public void transferirCargo(){
        cargo = cargoAux;
    }

    public RhCategorias getCategoriaAux() {
        return categoria;
    }

    public void setCategoriaAux(RhCategorias categoriaAux) {
        this.categoriaAux = categoriaAux;
    }
    
    @XmlTransient
    public boolean verifCategoria(){
        if((categoria == null &&  categoriaAux != null) || (categoria != null &&  categoriaAux == null)){
            return true;
        }else if(categoria == null &&  categoriaAux == null){
            return false;
        }
        
        return !categoria.equals(categoriaAux);
    }
    
    @XmlTransient
    public void transferirCategoria(){
        categoria = categoriaAux;
    }
    
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public Date getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Date fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public Date getFechaHoraUltimoEstado() {
        return fechaHoraUltimoEstado;
    }

    public void setFechaHoraUltimoEstado(Date fechaHoraUltimoEstado) {
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public RhCargos getCargo() {
        return cargo;
    }

    public void setCargo(RhCargos cargo) {
        this.cargo = cargo;
    }

    public RhCategorias getCategoria() {
        return categoria;
    }

    public void setCategoria(RhCategorias categoria) {
        this.categoria = categoria;
    }
    
    @XmlTransient
    public Collection<Departamentos> getDepartamentosCollection() {
        return departamentosCollection;
    }

    public void setDepartamentosCollection(Collection<Departamentos> departamentosCollection) {
        this.departamentosCollection = departamentosCollection;
    }

    @XmlTransient
    public Collection<Departamentos> getDepartamentosCollection1() {
        return departamentosCollection1;
    }

    public void setDepartamentosCollection1(Collection<Departamentos> departamentosCollection1) {
        this.departamentosCollection1 = departamentosCollection1;
    }

    @XmlTransient
    public Collection<Roles> getRolesCollection() {
        return rolesCollection;
    }

    public void setRolesCollection(Collection<Roles> rolesCollection) {
        this.rolesCollection = rolesCollection;
    }

    @XmlTransient
    public Collection<Roles> getRolesCollection1() {
        return rolesCollection1;
    }

    public void setRolesCollection1(Collection<Roles> rolesCollection1) {
        this.rolesCollection1 = rolesCollection1;
    }

    public Departamentos getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamentos departamento) {
        this.departamento = departamento;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public EstadosUsuario getEstado() {
        return estado;
    }

    public void setEstado(EstadosUsuario estado) {
        this.estado = estado;
    }

    public Pantallas getPantallaPrincipal() {
        return pantallaPrincipal;
    }

    public void setPantallaPrincipal(Pantallas pantallaPrincipal) {
        this.pantallaPrincipal = pantallaPrincipal;
    }

    public Sexos getSexo() {
        return sexo;
    }

    public void setSexo(Sexos sexo) {
        this.sexo = sexo;
    }

    @XmlTransient
    public Collection<Usuarios> getUsuariosCollection() {
        return usuariosCollection;
    }

    public void setUsuariosCollection(Collection<Usuarios> usuariosCollection) {
        this.usuariosCollection = usuariosCollection;
    }

    public Usuarios getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(Usuarios usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    @XmlTransient
    public Collection<Usuarios> getUsuariosCollection1() {
        return usuariosCollection1;
    }

    public void setUsuariosCollection1(Collection<Usuarios> usuariosCollection1) {
        this.usuariosCollection1 = usuariosCollection1;
    }

    public Usuarios getUsuarioUltimoEstado() {
        return usuarioUltimoEstado;
    }

    public void setUsuarioUltimoEstado(Usuarios usuarioUltimoEstado) {
        this.usuarioUltimoEstado = usuarioUltimoEstado;
    }

    public Personas getPersona() {
        return persona;
    }

    public void setPersona(Personas persona) {
        this.persona = persona;
    }

    public Bonificaciones getBonificacion() {
        return bonificacion;
    }

    public void setBonificacion(Bonificaciones bonificacion) {
        this.bonificacion = bonificacion;
    }

    @XmlTransient
    public Collection<RolesPorUsuarios> getRolesPorUsuariosCollection() {
        return rolesPorUsuariosCollection;
    }

    public void setRolesPorUsuariosCollection(Collection<RolesPorUsuarios> rolesPorUsuariosCollection) {
        this.rolesPorUsuariosCollection = rolesPorUsuariosCollection;
    }

    @XmlTransient
    public Collection<RolesPorUsuarios> getRolesPorUsuariosCollection1() {
        return rolesPorUsuariosCollection1;
    }

    public void setRolesPorUsuariosCollection1(Collection<RolesPorUsuarios> rolesPorUsuariosCollection1) {
        this.rolesPorUsuariosCollection1 = rolesPorUsuariosCollection1;
    }

    @XmlTransient
    public Collection<RolesPorUsuarios> getRolesPorUsuariosCollection2() {
        return rolesPorUsuariosCollection2;
    }

    public void setRolesPorUsuariosCollection2(Collection<RolesPorUsuarios> rolesPorUsuariosCollection2) {
        this.rolesPorUsuariosCollection2= rolesPorUsuariosCollection2;
    }
    
    @XmlTransient
    public Collection<Proveedores> getProveedoresCollection() {
        return proveedoresCollection;
    }

    public void setProveedoresCollection(Collection<Proveedores> proveedoresCollection) {
        this.proveedoresCollection = proveedoresCollection;
    }

    @XmlTransient
    public Collection<Proveedores> getProveedoresCollection1() {
        return proveedoresCollection1;
    }

    public void setProveedoresCollection1(Collection<Proveedores> proveedoresCollection1) {
        this.proveedoresCollection1 = proveedoresCollection1;
    }

    @XmlTransient
    public Collection<DocumentosJudiciales> getDocumentosJudicialesCollection() {
        return documentosJudicialesCollection;
    }

    public void setDocumentosJudicialesCollection(Collection<DocumentosJudiciales> documentosJudicialesCollection) {
        this.documentosJudicialesCollection = documentosJudicialesCollection;
    }

    @XmlTransient
    public Collection<DocumentosJudiciales> getDocumentosJudicialesCollection1() {
        return documentosJudicialesCollection1;
    }

    public void setDocumentosJudicialesCollection1(Collection<DocumentosJudiciales> documentosJudicialesCollection1) {
        this.documentosJudicialesCollection1 = documentosJudicialesCollection1;
    }

    @XmlTransient
    public Collection<DocumentosJudiciales> getDocumentosJudicialesCollection2() {
        return documentosJudicialesCollection2;
    }

    public void setDocumentosJudicialesCollection2(Collection<DocumentosJudiciales> documentosJudicialesCollection2) {
        this.documentosJudicialesCollection2 = documentosJudicialesCollection2;
    }

    @XmlTransient
    public Collection<DocumentosJudiciales> getDocumentosJudicialesCollection3() {
        return documentosJudicialesCollection3;
    }

    public void setDocumentosJudicialesCollection3(Collection<DocumentosJudiciales> documentosJudicialesCollection3) {
        this.documentosJudicialesCollection3 = documentosJudicialesCollection3;
    }
    
    @XmlTransient
    public Collection<MovimientosReparacionBienes> getMovimientosReparacionBienesCollection() {
        return movimientosReparacionBienesCollection;
    }

    public void setMovimientosReparacionBienesCollection(Collection<MovimientosReparacionBienes> movimientosReparacionBienesCollection) {
        this.movimientosReparacionBienesCollection = movimientosReparacionBienesCollection;
    }

    @XmlTransient
    public Collection<MovimientosReparacionBienes> getMovimientosReparacionBienesCollection1() {
        return movimientosReparacionBienesCollection1;
    }

    public void setMovimientosReparacionBienesCollection1(Collection<MovimientosReparacionBienes> movimientosReparacionBienesCollection1) {
        this.movimientosReparacionBienesCollection1 = movimientosReparacionBienesCollection1;
    }

    @XmlTransient
    public Collection<Bienes> getBienesCollection() {
        return bienesCollection;
    }

    public void setBienesCollection(Collection<Bienes> bienesCollection) {
        this.bienesCollection = bienesCollection;
    }

    @XmlTransient
    public Collection<Bienes> getBienesCollection1() {
        return bienesCollection1;
    }

    public void setBienesCollection1(Collection<Bienes> bienesCollection1) {
        this.bienesCollection1 = bienesCollection1;
    }

    @XmlTransient
    public Collection<Bienes> getBienesCollection2() {
        return bienesCollection2;
    }

    public void setBienesCollection2(Collection<Bienes> bienesCollection2) {
        this.bienesCollection2 = bienesCollection2;
    }
    
    @XmlTransient
    public Collection<MovimientosBienes> getMovimientosBienesCollection() {
        return movimientosBienesCollection;
    }

    public void setMovimientosBienesCollection(Collection<MovimientosBienes> movimientosBienesCollection) {
        this.movimientosBienesCollection = movimientosBienesCollection;
    }

    @XmlTransient
    public Collection<MovimientosBienes> getMovimientosBienesCollection1() {
        return movimientosBienesCollection1;
    }

    public void setMovimientosBienesCollection1(Collection<MovimientosBienes> movimientosBienesCollection1) {
        this.movimientosBienesCollection1 = movimientosBienesCollection1;
    }

    @XmlTransient
    public Collection<MovimientosBienes> getMovimientosBienesCollection2() {
        return movimientosBienesCollection2;
    }

    public void setMovimientosBienesCollection2(Collection<MovimientosBienes> movimientosBienesCollection2) {
        this.movimientosBienesCollection2 = movimientosBienesCollection2;
    }

    @XmlTransient
    public Collection<MovimientosBienes> getMovimientosBienesCollection3() {
        return movimientosBienesCollection3;
    }

    public void setMovimientosBienesCollection3(Collection<MovimientosBienes> movimientosBienesCollection3) {
        this.movimientosBienesCollection3 = movimientosBienesCollection3;
    }

    @XmlTransient
    public Collection<ObservacionesDocumentosJudiciales> getObservacionesDocumentosJudicialesCollection() {
        return observacionesDocumentosJudicialesCollection;
    }

    public void setObservacionesDocumentosJudicialesCollection(Collection<ObservacionesDocumentosJudiciales> observacionesDocumentosJudicialesCollection) {
        this.observacionesDocumentosJudicialesCollection = observacionesDocumentosJudicialesCollection;
    }

    @XmlTransient
    public Collection<ObservacionesDocumentosJudiciales> getObservacionesDocumentosJudicialesCollection1() {
        return observacionesDocumentosJudicialesCollection1;
    }

    public void setObservacionesDocumentosJudicialesCollection1(Collection<ObservacionesDocumentosJudiciales> observacionesDocumentosJudicialesCollection1) {
        this.observacionesDocumentosJudicialesCollection1 = observacionesDocumentosJudicialesCollection1;
    }

    @XmlTransient
    public Collection<TiposResolucion> getTipoResolucionCollection() {
        return tipoResolucionCollection;
    }

    public void setTipoResolucionCollection(Collection<TiposResolucion> tipoResolucionCollection) {
        this.tipoResolucionCollection = tipoResolucionCollection;
    }

    @XmlTransient
    public Collection<TiposResolucion> getTipoResolucionCollectionCollection1() {
        return tipoResolucionCollectionCollection1;
    }

    public void setTipoResolucionCollectionCollection1(Collection<TiposResolucion> tipoResolucionCollectionCollection1) {
        this.tipoResolucionCollectionCollection1 = tipoResolucionCollectionCollection1;
    }

    public EstadosDocumento getEstadoDocumentoFinal() {
        return estadoDocumentoFinal;
    }

    public void setEstadoDocumentoFinal(EstadosDocumento estadoDocumentoFinal) {
        this.estadoDocumentoFinal = estadoDocumentoFinal;
    }

    public boolean isTieneToken() {
        return tieneToken;
    }

    public void setTieneToken(boolean tieneToken) {
        this.tieneToken = tieneToken;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuarios)) {
            return false;
        }
        Usuarios other = (Usuarios) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        
        return nombresApellidos + ((departamento==null)?"":" - " + departamento.getNombre());
    }
    
}
