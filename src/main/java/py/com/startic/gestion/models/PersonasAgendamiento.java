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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "personas_agendamiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PersonasAgendamiento.findAll", query = "SELECT p FROM PersonasAgendamiento p WHERE p.estado = 'AC' ORDER BY p.fechaHoraUltimoEstado DESC"),
    @NamedQuery(name = "PersonasAgendamiento.findById", query = "SELECT p FROM PersonasAgendamiento p WHERE p.id = :id"),
    @NamedQuery(name = "PersonasAgendamiento.findByNombresApellidos", query = "SELECT p FROM PersonasAgendamiento p WHERE p.nombresApellidos = :nombresApellidos"),
    @NamedQuery(name = "PersonasAgendamiento.findByCi", query = "SELECT p FROM PersonasAgendamiento p WHERE p.ci = :ci"),
    @NamedQuery(name = "PersonasAgendamiento.findByNombresApellidosEstado", query = "SELECT p FROM PersonasAgendamiento p WHERE p.nombresApellidos = :nombresApellidos and p.estado = :estado"),
    @NamedQuery(name = "PersonasAgendamiento.findByCiEstado", query = "SELECT p FROM PersonasAgendamiento p WHERE p.ci = :ci and p.estado = :estado"),
    @NamedQuery(name = "PersonasAgendamiento.findByEstado", query = "SELECT p FROM PersonasAgendamiento p WHERE p.estado = :estado"),
    @NamedQuery(name = "PersonasAgendamiento.findByFechaHoraAlta", query = "SELECT p FROM PersonasAgendamiento p WHERE p.fechaHoraAlta = :fechaHoraAlta"),
    @NamedQuery(name = "PersonasAgendamiento.findByUsuarioAlta", query = "SELECT p FROM PersonasAgendamiento p WHERE p.usuarioAlta = :usuarioAlta"),
    @NamedQuery(name = "PersonasAgendamiento.findByFechaHoraUltimoEstado", query = "SELECT p FROM PersonasAgendamiento p WHERE p.fechaHoraUltimoEstado = :fechaHoraUltimoEstado"),
    @NamedQuery(name = "PersonasAgendamiento.findByUsuarioUltimoEstado", query = "SELECT p FROM PersonasAgendamiento p WHERE p.usuarioUltimoEstado = :usuarioUltimoEstado")})
public class PersonasAgendamiento implements Serializable {

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
    @Basic(optional = true)
    @Column(name = "fecha_hora_borrado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraBorrado;
    @JoinColumn(name = "usuario_borrado", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuarioBorrado;
    @JoinColumn(name = "despacho_persona", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private DespachosPersona despachoPersona;
    @JoinColumn(name = "localidad_persona", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private LocalidadesPersona localidadPersona;
    @JoinColumn(name = "departamento_persona", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private DepartamentosPersona departamentoPersona;
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

    public PersonasAgendamiento() {
    }

    public PersonasAgendamiento(PersonasAgendamiento per) {
        this.id = per.id;
        this.nombresApellidos = per.nombresApellidos;
        this.ci = per.ci;
        this.estado = per.estado;
        this.fechaHoraAlta = per.fechaHoraAlta;
        this.usuarioAlta = per.usuarioAlta;
        this.fechaHoraUltimoEstado = per.fechaHoraUltimoEstado;
        this.usuarioUltimoEstado = per.usuarioUltimoEstado;
        this.despachoPersona = per.despachoPersona;
        this.localidadPersona = per.localidadPersona;
        this.departamentoPersona = per.departamentoPersona;
        this.telefono1 = per.telefono1;
        this.telefono2 = per.telefono2;
        this.email = per.email;
    }

    public PersonasAgendamiento(Integer id) {
        this.id = id;
    }

    public PersonasAgendamiento(Integer id, String nombresApellidos, String ci, String estado, Date fechaHoraAlta) {
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

    public Date getFechaHoraBorrado() {
        return fechaHoraBorrado;
    }

    public void setFechaHoraBorrado(Date fechaHoraBorrado) {
        this.fechaHoraBorrado = fechaHoraBorrado;
    }

    public Usuarios getUsuarioBorrado() {
        return usuarioBorrado;
    }

    public void setUsuarioBorrado(Usuarios usuarioBorrado) {
        this.usuarioBorrado = usuarioBorrado;
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
        if (!(object instanceof PersonasAgendamiento)) {
            return false;
        }
        PersonasAgendamiento other = (PersonasAgendamiento) object;
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
