/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import java.util.Collection;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "motivos_movimientos_bienes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MotivosMovimientosBienes.findAll", query = "SELECT m FROM MotivosMovimientosBienes m")
    , @NamedQuery(name = "MotivosMovimientosBienes.findByCodigo", query = "SELECT m FROM MotivosMovimientosBienes m WHERE m.codigo = :codigo")
    , @NamedQuery(name = "MotivosMovimientosBienes.findByDescripcion", query = "SELECT m FROM MotivosMovimientosBienes m WHERE m.descripcion = :descripcion")
    , @NamedQuery(name = "MotivosMovimientosBienes.findByEmpresa", query = "SELECT m FROM MotivosMovimientosBienes m WHERE m.empresa = :empresa")})
public class MotivosMovimientosBienes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "empresa")
    private int empresa;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "motivoMovimientoBien")
    private Collection<MovimientosBienes> movimientosBienesCollection;

    public MotivosMovimientosBienes() {
    }

    public MotivosMovimientosBienes(String codigo) {
        this.codigo = codigo;
    }

    public MotivosMovimientosBienes(String codigo, String descripcion, int empresa) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.empresa = empresa;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getEmpresa() {
        return empresa;
    }

    public void setEmpresa(int empresa) {
        this.empresa = empresa;
    }

    @XmlTransient
    public Collection<MovimientosBienes> getMovimientosBienesCollection() {
        return movimientosBienesCollection;
    }

    public void setMovimientosBienesCollection(Collection<MovimientosBienes> movimientosBienesCollection) {
        this.movimientosBienesCollection = movimientosBienesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MotivosMovimientosBienes)) {
            return false;
        }
        MotivosMovimientosBienes other = (MotivosMovimientosBienes) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models7.MotivosMovimientosBienes[ codigo=" + codigo + " ]";
    }
    
}
