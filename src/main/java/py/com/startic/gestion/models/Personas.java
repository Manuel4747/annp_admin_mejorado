/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Basic;
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
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "personas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Personas.findAll", query = "SELECT p FROM Personas p WHERE p.estado = 'AC' ORDER BY p.fechaHoraUltimoEstado DESC"),
    @NamedQuery(name = "Personas.findById", query = "SELECT p FROM Personas p WHERE p.id = :id"),
    @NamedQuery(name = "Personas.control", query = "SELECT u FROM Personas u WHERE u.usuario = :usuario AND u.contrasena = :contrasena"),
    @NamedQuery(name = "Personas.findByNombresApellidos", query = "SELECT p FROM Personas p WHERE p.nombresApellidos = :nombresApellidos"),
    @NamedQuery(name = "Personas.findByCi", query = "SELECT p FROM Personas p WHERE p.ci = :ci"),
    @NamedQuery(name = "Personas.findByNombresApellidosEstado", query = "SELECT p FROM Personas p WHERE p.nombresApellidos = :nombresApellidos and p.estado = :estado"),
    @NamedQuery(name = "Personas.findByCiEstado", query = "SELECT p FROM Personas p WHERE p.ci = :ci and p.estado = :estado"),
    @NamedQuery(name = "Personas.findByEstado", query = "SELECT p FROM Personas p WHERE p.estado = :estado"),
    @NamedQuery(name = "Personas.findByFechaHoraAlta", query = "SELECT p FROM Personas p WHERE p.fechaHoraAlta = :fechaHoraAlta"),
    @NamedQuery(name = "Personas.findByUsuarioAlta", query = "SELECT p FROM Personas p WHERE p.usuarioAlta = :usuarioAlta"),
    @NamedQuery(name = "Personas.findByFechaHoraUltimoEstado", query = "SELECT p FROM Personas p WHERE p.fechaHoraUltimoEstado = :fechaHoraUltimoEstado"),
    @NamedQuery(name = "Personas.findByUsuarioUltimoEstado", query = "SELECT p FROM Personas p WHERE p.usuarioUltimoEstado = :usuarioUltimoEstado")})
public class Personas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "nombres_apellidos")
    private String nombresApellidos;
    @Basic(optional = true)
    @Size(max = 20)
    @Column(name = "ci")
    private String ci;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "estado")
    private String estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne
    private Usuarios usuarioAlta;
    @Column(name = "fecha_hora_ultimo_estado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraUltimoEstado;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "cargo_persona", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private CargosPersona cargoPersona;
    @JoinColumn(name = "despacho_persona", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private DespachosPersona despachoPersona;
    @JoinColumn(name = "tipo_persona", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private TiposPersona tipoPersona;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "localidad_persona", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private LocalidadesPersona localidadPersona;
    @JoinColumn(name = "departamento_persona", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private DepartamentosPersona departamentoPersona;
    @Transient
    private boolean tipoExpedienteAnterior;
    @Basic(optional = true)
    @Size(max = 20)
    @Column(name = "usuario")
    private String usuario;
    @Basic(optional = true)
    @Size(max = 20)
    @Column(name = "contrasena")
    private String contrasena;
    @Basic(optional = true)
    @Size(max = 100)
    @Column(name = "telefono1")
    private String telefono1;
    @Basic(optional = true)
    @Size(max = 100)
    @Column(name = "telefono2")
    private String telefono2;
    @Basic(optional = true)
    @Size(max = 100)
    @Column(name = "email")
    private String email;
    @Basic(optional = true)
    @Column(name = "habilitar_antecedentes")
    private boolean habilitarAntecedentes;
    @Basic(optional = true)
    @Column(name = "email_validado")
    private boolean emailValidado;
    @Column(name = "universidad_nacional")
    private boolean universidadNacional;
    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimento;
    @Basic(optional = true)
    @Size(max = 100)
    @Column(name = "promocion")
    private String promocion;
    @Lob
    @Size(max = 65535)
    @Column(name = "observacion")
    private String observacion;
    @JoinColumn(name = "profesion", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Profesiones profesion;

    public Personas() {
    }

    public Personas(Personas per) {
        this.id = per.id;
        this.nombresApellidos = per.nombresApellidos;
        this.ci = per.ci;
        this.estado = per.estado;
        this.fechaHoraAlta = per.fechaHoraAlta;
        this.usuarioAlta = per.usuarioAlta;
        this.fechaHoraUltimoEstado = per.fechaHoraUltimoEstado;
        this.usuarioUltimoEstado = per.usuarioUltimoEstado;
        this.cargoPersona = per.cargoPersona;
        this.despachoPersona = per.despachoPersona;
        this.tipoPersona = per.tipoPersona;
        this.empresa = per.empresa;
        this.localidadPersona = per.localidadPersona;
        this.departamentoPersona = per.departamentoPersona;
        this.tipoExpedienteAnterior = per.tipoExpedienteAnterior;
        this.usuario = per.usuario;
        this.contrasena = per.contrasena;
        this.telefono1 = per.telefono1;
        this.telefono2 = per.telefono2;
        this.email = per.email;
        this.habilitarAntecedentes = per.habilitarAntecedentes;
        this.emailValidado = per.emailValidado;
    }

    public Personas(Integer id) {
        this.id = id;
    }

    public Personas(Integer id, String nombresApellidos, String ci, String estado, Date fechaHoraAlta) {
        this.id = id;
        this.nombresApellidos = nombresApellidos;
        this.ci = ci;
        this.estado = estado;
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombresApellidos() {
        return nombresApellidos;
    }

    public void setNombresApellidos(String nombresApellidos) {
        this.nombresApellidos = nombresApellidos;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public Usuarios getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(Usuarios usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    public Usuarios getUsuarioUltimoEstado() {
        return usuarioUltimoEstado;
    }

    public void setUsuarioUltimoEstado(Usuarios usuarioUltimoEstado) {
        this.usuarioUltimoEstado = usuarioUltimoEstado;
    }

    public CargosPersona getCargoPersona() {
        return cargoPersona;
    }

    public void setCargoPersona(CargosPersona cargoPersona) {
        this.cargoPersona = cargoPersona;
    }

    public TiposPersona getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(TiposPersona tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public DespachosPersona getDespachoPersona() {
        return despachoPersona;
    }

    public void setDespachoPersona(DespachosPersona despachoPersona) {
        this.despachoPersona = despachoPersona;
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

    public boolean isTipoExpedienteAnterior() {
        return tipoExpedienteAnterior;
    }

    public void setTipoExpedienteAnterior(boolean tipoExpedienteAnterior) {
        this.tipoExpedienteAnterior = tipoExpedienteAnterior;
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

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isHabilitarAntecedentes() {
        return habilitarAntecedentes;
    }

    public void setHabilitarAntecedentes(boolean habilitarAntecedentes) {
        this.habilitarAntecedentes = habilitarAntecedentes;
    }

    public boolean isEmailValidado() {
        return emailValidado;
    }

    public void setEmailValidado(boolean emailValidado) {
        this.emailValidado = emailValidado;
    }

    public boolean isUniversidadNacional() {
        return universidadNacional;
    }

    public void setUniversidadNacional(boolean universidadNacional) {
        this.universidadNacional = universidadNacional;
    }

    public Date getFechaNacimento() {
        return fechaNacimento;
    }

    public void setFechaNacimento(Date fechaNacimento) {
        this.fechaNacimento = fechaNacimento;
    }

    public String getPromocion() {
        return promocion;
    }

    public void setPromocion(String promocion) {
        this.promocion = promocion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Profesiones getProfesion() {
        return profesion;
    }

    public void setProfesion(Profesiones profesion) {
        this.profesion = profesion;
    }

    @XmlTransient
    public String getEstaHabilitarAntecedentes() {
        if (habilitarAntecedentes) {
            return "SI";
        } else {
            return "NO";
        }
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
        if (!(object instanceof Personas)) {
            return false;
        }
        Personas other = (Personas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return (nombresApellidos != null)?nombresApellidos:"";
    }

}
