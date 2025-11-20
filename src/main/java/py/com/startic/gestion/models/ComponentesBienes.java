/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "componentes_bienes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComponentesBienes.findAll", query = "SELECT c FROM ComponentesBienes c")
    , @NamedQuery(name = "ComponentesBienes.findById", query = "SELECT c FROM ComponentesBienes c WHERE c.id = :id")
    , @NamedQuery(name = "ComponentesBienes.findByBien", query = "SELECT c FROM ComponentesBienes c WHERE c.bien = :bien ORDER BY c.descripcion")
    , @NamedQuery(name = "ComponentesBienes.findByDescripcion", query = "SELECT c FROM ComponentesBienes c WHERE c.descripcion = :descripcion")
    , @NamedQuery(name = "ComponentesBienes.findByNroSerie", query = "SELECT c FROM ComponentesBienes c WHERE c.nroSerie = :nroSerie")
    , @NamedQuery(name = "ComponentesBienes.findByFechaHoraAlta", query = "SELECT c FROM ComponentesBienes c WHERE c.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "ComponentesBienes.findByFechaHoraUltimoEstado", query = "SELECT c FROM ComponentesBienes c WHERE c.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class ComponentesBienes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "nro_serie")
    private String nroSerie;
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
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "componente")
    private Collection<MovimientosComponentes> movimientosComponentesCollection;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "responsable", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios responsable;
    @JoinColumn(name = "departamento", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Departamentos departamento;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "bien", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Bienes bien;

    public ComponentesBienes() {
    }

    public ComponentesBienes(Integer id) {
        this.id = id;
    }

    public ComponentesBienes(Integer id, String descripcion, String nroSerie, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.descripcion = descripcion;
        this.nroSerie = nroSerie;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuarios getResponsable() {
        return responsable;
    }

    public void setResponsable(Usuarios responsable) {
        this.responsable = responsable;
    }

    public Departamentos getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamentos departamento) {
        this.departamento = departamento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNroSerie() {
        return nroSerie;
    }

    public void setNroSerie(String nroSerie) {
        this.nroSerie = nroSerie;
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

    @XmlTransient
    public Collection<MovimientosComponentes> getMovimientosComponentesCollection() {
        return movimientosComponentesCollection;
    }

    public void setMovimientosComponentesCollection(Collection<MovimientosComponentes> movimientosComponentesCollection) {
        this.movimientosComponentesCollection = movimientosComponentesCollection;
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

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public Bienes getBien() {
        return bien;
    }

    public void setBien(Bienes bien) {
        this.bien = bien;
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
        if (!(object instanceof ComponentesBienes)) {
            return false;
        }
        ComponentesBienes other = (ComponentesBienes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descripcion;
    }
    
}
