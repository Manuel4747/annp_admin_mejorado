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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "motivos_movimientos_componentes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MotivosMovimientosComponentes.findAll", query = "SELECT m FROM MotivosMovimientosComponentes m")
    , @NamedQuery(name = "MotivosMovimientosComponentes.findByCodigo", query = "SELECT m FROM MotivosMovimientosComponentes m WHERE m.codigo = :codigo")
    , @NamedQuery(name = "MotivosMovimientosComponentes.findByDescripcion", query = "SELECT m FROM MotivosMovimientosComponentes m WHERE m.descripcion = :descripcion")})
public class MotivosMovimientosComponentes implements Serializable {

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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "motivoMovimientoComponente")
    private Collection<MovimientosComponentes> movimientosComponentesCollection;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;

    public MotivosMovimientosComponentes() {
    }

    public MotivosMovimientosComponentes(String codigo) {
        this.codigo = codigo;
    }

    public MotivosMovimientosComponentes(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcon() {
        return descripcion;
    }

    public void setDescripcon(String descripcon) {
        this.descripcion = descripcon;
    }

    @XmlTransient
    public Collection<MovimientosComponentes> getMovimientosComponentesCollection() {
        return movimientosComponentesCollection;
    }

    public void setMovimientosComponentesCollection(Collection<MovimientosComponentes> movimientosComponentesCollection) {
        this.movimientosComponentesCollection = movimientosComponentesCollection;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
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
        if (!(object instanceof MotivosMovimientosComponentes)) {
            return false;
        }
        MotivosMovimientosComponentes other = (MotivosMovimientosComponentes) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models10.MotivosMovimientosComponentes[ codigo=" + codigo + " ]";
    }
    
}
