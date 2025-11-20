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
 * @author grecia
 */
@Entity
@Table(name = "controlar_permisos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ControlarPermisos.findAll", query = "SELECT t FROM ControlarPermisos t"),
    @NamedQuery(name = "ControlarPermisos.findById", query = "SELECT t FROM ControlarPermisos t WHERE t.id = :id"),
    @NamedQuery(name = "ControlarPermisos.findByFuncionario", query = "SELECT t FROM ControlarPermisos t WHERE t.funcionario = :funcionario"),
    @NamedQuery(name = "ControlarPermisos.findByFechaHoraUltimoEstado", query = "SELECT t FROM ControlarPermisos t WHERE t.fecha = :fecha")})
public class ControlarPermisos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    @JoinColumn(name = "funcionario", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios funcionario;

    @JoinColumn(name = "tipo_permiso", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private TiposPermisos tipoPermiso;

    @Column(name = "cantidad_acumulada")
    private Integer cantidadAcumulada;
    
    @Column(name = "cantidad_usada")
    private Integer cantidadUsada;
    
    @Column(name = "anio")
    private Integer anio;

    public ControlarPermisos() {
    }

    public ControlarPermisos(Integer id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Usuarios getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Usuarios funcionario) {
        this.funcionario = funcionario;
    }

    public Integer getCantidadAcumulada() {
        return cantidadAcumulada;
    }

    public void setCantidadAcumulada(Integer cantidadAcumulada) {
        this.cantidadAcumulada = cantidadAcumulada;
    }

    public TiposPermisos getTipoPermiso() {
        return tipoPermiso;
    }

    public void setTipoPermiso(TiposPermisos tipoPermiso) {
        this.tipoPermiso = tipoPermiso;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }
    
    public ControlarPermisos(Integer id, Date fecha) {
        this.id = id;
        this.fecha = fecha;
        
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCantidadUsada() {
        return cantidadUsada;
    }

    public void setCantidadUsada(Integer cantidadUsada) {
        this.cantidadUsada = cantidadUsada;
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
        if (!(object instanceof ControlarPermisos)) {
            return false;
        }
        ControlarPermisos other = (ControlarPermisos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
       return "py.com.startic.gestion.models.ControlarPermisos[ id=" + id + " ]";
    }

}
