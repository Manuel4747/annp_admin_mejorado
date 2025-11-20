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
import py.com.startic.gestion.models.Empresas;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "origenes_bienes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OrigenesBienes.findAll", query = "SELECT o FROM OrigenesBienes o")
    , @NamedQuery(name = "OrigenesBienes.findByCodigo", query = "SELECT o FROM OrigenesBienes o WHERE o.codigo = :codigo")
    , @NamedQuery(name = "OrigenesBienes.findByDescripcion", query = "SELECT o FROM OrigenesBienes o WHERE o.descripcion = :descripcion")})
public class OrigenesBienes implements Serializable {

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
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "origenBien")
    private Collection<Bienes> bienesCollection;

    public OrigenesBienes() {
    }

    public OrigenesBienes(String codigo) {
        this.codigo = codigo;
    }

    public OrigenesBienes(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
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

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    @XmlTransient
    public Collection<Bienes> getBienesCollection() {
        return bienesCollection;
    }

    public void setBienesCollection(Collection<Bienes> bienesCollection) {
        this.bienesCollection = bienesCollection;
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
        if (!(object instanceof OrigenesBienes)) {
            return false;
        }
        OrigenesBienes other = (OrigenesBienes) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models7.OrigenesBienes[ codigo=" + codigo + " ]";
    }
    
}
