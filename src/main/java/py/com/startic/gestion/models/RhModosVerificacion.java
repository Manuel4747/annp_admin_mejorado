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
@Table(name = "rh_modos_verificacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RhModosVerificacion.findAll", query = "SELECT r FROM RhModosVerificacion r")
    , @NamedQuery(name = "RhModosVerificacion.findById", query = "SELECT r FROM RhModosVerificacion r WHERE r.id = :id")
    , @NamedQuery(name = "RhModosVerificacion.findByDescripcion", query = "SELECT r FROM RhModosVerificacion r WHERE r.descripcion = :descripcion")
    , @NamedQuery(name = "RhModosVerificacion.findByCodigo", query = "SELECT r FROM RhModosVerificacion r WHERE r.codigo = :codigo")
    , @NamedQuery(name = "RhModosVerificacion.findByEmpresa", query = "SELECT r FROM RhModosVerificacion r WHERE r.empresa = :empresa")})
public class RhModosVerificacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "codigo")
    private int codigo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "empresa")
    private int empresa;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modoVerificacion")
    private Collection<RhMarcaciones> rhMarcacionesCollection;

    public RhModosVerificacion() {
    }

    public RhModosVerificacion(Integer id) {
        this.id = id;
    }

    public RhModosVerificacion(Integer id, String descripcion, int codigo, int empresa) {
        this.id = id;
        this.descripcion = descripcion;
        this.codigo = codigo;
        this.empresa = empresa;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getEmpresa() {
        return empresa;
    }

    public void setEmpresa(int empresa) {
        this.empresa = empresa;
    }

    @XmlTransient
    public Collection<RhMarcaciones> getRhMarcacionesCollection() {
        return rhMarcacionesCollection;
    }

    public void setRhMarcacionesCollection(Collection<RhMarcaciones> rhMarcacionesCollection) {
        this.rhMarcacionesCollection = rhMarcacionesCollection;
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
        if (!(object instanceof RhModosVerificacion)) {
            return false;
        }
        RhModosVerificacion other = (RhModosVerificacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models18.RhModosVerificacion[ id=" + id + " ]";
    }
    
}
