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
@Table(name = "pedidos_persona")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PedidosPersona.findAll", query = "SELECT p FROM PedidosPersona p order by p.fechaHoraAlta")
    , @NamedQuery(name = "PedidosPersona.control", query = "SELECT u FROM PedidosPersona u WHERE u.usuario = :usuario AND u.contrasena = :contrasena")
    , @NamedQuery(name = "PedidosPersona.findById", query = "SELECT p FROM PedidosPersona p WHERE p.id = :id")
    , @NamedQuery(name = "PedidosPersona.findByNombresApellidos", query = "SELECT p FROM PedidosPersona p WHERE p.nombresApellidos = :nombresApellidos")
    , @NamedQuery(name = "PedidosPersona.findByCi", query = "SELECT p FROM PedidosPersona p WHERE p.ci = :ci")
    , @NamedQuery(name = "PedidosPersona.findByEstado", query = "SELECT p FROM PedidosPersona p WHERE p.estado = :estado")
    , @NamedQuery(name = "PedidosPersona.findByFechaHoraAlta", query = "SELECT p FROM PedidosPersona p WHERE p.fechaHoraAlta = :fechaHoraAlta")})
public class PedidosPersona implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nombres_apellidos")
    private String nombresApellidos;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "ci")
    private String ci;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "estado")
    private String estado;
    @Basic(optional = true)
    @Size(max = 20)
    @Column(name = "usuario")
    private String usuario;
    @Basic(optional = true)
    @Size(max = 20)
    @Column(name = "contrasena")
    private String contrasena;
    @Basic(optional = true)
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_pedido")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraPedido;
    @JoinColumn(name = "cargo_persona", referencedColumnName = "id")
    @ManyToOne
    private CargosPersona cargoPersona;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "descripcion_cargo_persona")
    private String descripcionCargoPersona;
    @JoinColumn(name = "despacho_persona", referencedColumnName = "id")
    @ManyToOne
    private DespachosPersona despachoPersona;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "descripcion_despacho_persona")
    private String descripcionDespachoPersona;
    @JoinColumn(name = "tipo_persona", referencedColumnName = "id")
    @ManyToOne
    private TiposPersona tipoPersona;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "descripcion_tipo_persona")
    private String descripcionTipoPersona;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "usuarioAlta", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "localidad_persona", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private LocalidadesPersona localidadPersona;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "descripcion_localidad_persona")
    private String descripcionLocalidadPersona;
    @JoinColumn(name = "departamento_persona", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DepartamentosPersona departamentoPersona;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "descripcion_departamento_persona")
    private String descripcionDepartamentoPersona;

    public PedidosPersona() {
    }

    public PedidosPersona(Integer id) {
        this.id = id;
    }

    public PedidosPersona(Integer id, String nombresApellidos, String ci, String estado, Date fechaHoraAlta) {
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

    public String getDescripcionCargoPersona() {
        return descripcionCargoPersona;
    }

    public void setDescripcionCargoPersona(String descripcionCargoPersona) {
        this.descripcionCargoPersona = descripcionCargoPersona;
    }

    public String getDescripcionDespachoPersona() {
        return descripcionDespachoPersona;
    }

    public void setDescripcionDespachoPersona(String descripcionDespachoPersona) {
        this.descripcionDespachoPersona = descripcionDespachoPersona;
    }

    public String getDescripcionTipoPersona() {
        return descripcionTipoPersona;
    }

    public void setDescripcionTipoPersona(String descripcionTipoPersona) {
        this.descripcionTipoPersona = descripcionTipoPersona;
    }

    public String getDescripcionLocalidadPersona() {
        return descripcionLocalidadPersona;
    }

    public void setDescripcionLocalidadPersona(String descripcionLocalidadPersona) {
        this.descripcionLocalidadPersona = descripcionLocalidadPersona;
    }

    public String getDescripcionDepartamentoPersona() {
        return descripcionDepartamentoPersona;
    }

    public void setDescripcionDepartamentoPersona(String descripcionDepartamentoPersona) {
        this.descripcionDepartamentoPersona = descripcionDepartamentoPersona;
    }

    public Date getFechaHoraPedido() {
        return fechaHoraPedido;
    }

    public void setFechaHoraPedido(Date fechaHoraPedido) {
        this.fechaHoraPedido = fechaHoraPedido;
    }

    public Usuarios getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(Usuarios usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
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
        if (!(object instanceof PedidosPersona)) {
            return false;
        }
        PedidosPersona other = (PedidosPersona) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.PedidosPersona[ id=" + id + " ]";
    }
    
}
