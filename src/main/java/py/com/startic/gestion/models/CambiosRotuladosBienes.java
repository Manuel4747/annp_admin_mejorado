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
@Table(name = "cambios_rotulados_bienes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CambiosRotuladosBienes.findAll", query = "SELECT c FROM CambiosRotuladosBienes c")
    , @NamedQuery(name = "CambiosRotuladosBienes.findById", query = "SELECT c FROM CambiosRotuladosBienes c WHERE c.id = :id")
    , @NamedQuery(name = "CambiosRotuladosBienes.findByIdMax", query = "SELECT c FROM CambiosRotuladosBienes c WHERE c.id IN (SELECT MAX(d.id) FROM CambiosRotuladosBienes d WHERE d.bien = :bien)")
    , @NamedQuery(name = "CambiosRotuladosBienes.findByBien", query = "SELECT c FROM CambiosRotuladosBienes c WHERE c.bien = :bien ORDER BY c.fechaDesde DESC, c.fechaHoraAlta DESC")
    , @NamedQuery(name = "CambiosRotuladosBienes.findByRotulado", query = "SELECT c FROM CambiosRotuladosBienes c WHERE c.rotulado = :rotulado")
    , @NamedQuery(name = "CambiosRotuladosBienes.findByFechaDesde", query = "SELECT c FROM CambiosRotuladosBienes c WHERE c.fechaDesde = :fechaDesde")
    , @NamedQuery(name = "CambiosRotuladosBienes.findByFechaHasta", query = "SELECT c FROM CambiosRotuladosBienes c WHERE c.fechaHasta = :fechaHasta")
    , @NamedQuery(name = "CambiosRotuladosBienes.findByFechaHoraAlta", query = "SELECT c FROM CambiosRotuladosBienes c WHERE c.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "CambiosRotuladosBienes.findByFechaHoraUltimoEstado", query = "SELECT c FROM CambiosRotuladosBienes c WHERE c.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class CambiosRotuladosBienes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "rotulado")
    private String rotulado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_desde")
    @Temporal(TemporalType.DATE)
    private Date fechaDesde;
    @Column(name = "fecha_hasta")
    @Temporal(TemporalType.DATE)
    private Date fechaHasta;
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
    @JoinColumn(name = "bien", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Bienes bien;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;

    public CambiosRotuladosBienes() {
    }

    public CambiosRotuladosBienes(Integer id) {
        this.id = id;
    }

    public CambiosRotuladosBienes(Integer id, String rotulado, Date fechaDesde, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.rotulado = rotulado;
        this.fechaDesde = fechaDesde;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRotulado() {
        return rotulado;
    }

    public void setRotulado(String rotulado) {
        this.rotulado = rotulado;
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

    public Bienes getBien() {
        return bien;
    }

    public void setBien(Bienes bien) {
        this.bien = bien;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CambiosRotuladosBienes)) {
            return false;
        }
        CambiosRotuladosBienes other = (CambiosRotuladosBienes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models7.CambiosRotuladosBienes[ id=" + id + " ]";
    }
    
}
