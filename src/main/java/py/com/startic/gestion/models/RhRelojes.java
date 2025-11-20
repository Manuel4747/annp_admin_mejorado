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
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "rh_relojes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RhRelojes.findAll", query = "SELECT r FROM RhRelojes r")
    , @NamedQuery(name = "RhRelojes.findById", query = "SELECT r FROM RhRelojes r WHERE r.id = :id")
    , @NamedQuery(name = "RhRelojes.findByDescripcion", query = "SELECT r FROM RhRelojes r WHERE r.descripcion = :descripcion")
    , @NamedQuery(name = "RhRelojes.findByCodigoMaquina", query = "SELECT r FROM RhRelojes r WHERE r.codigoMaquina = :codigoMaquina")
    , @NamedQuery(name = "RhRelojes.findByEmpresa", query = "SELECT r FROM RhRelojes r WHERE r.empresa = :empresa")})
public class RhRelojes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "codigo_maquina")
    private String codigoMaquina;
    @Basic(optional = false)
    @NotNull
    @Column(name = "empresa")
    private int empresa;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reloj")
    private Collection<RhMarcaciones> rhMarcacionesCollection;

    public RhRelojes() {
    }

    public RhRelojes(Integer id) {
        this.id = id;
    }

    public RhRelojes(Integer id, String descripcion, String codigoMaquina, int empresa) {
        this.id = id;
        this.descripcion = descripcion;
        this.codigoMaquina = codigoMaquina;
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

    public String getCodigoMaquina() {
        return codigoMaquina;
    }

    public void setCodigoMaquina(String codigoMaquina) {
        this.codigoMaquina = codigoMaquina;
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
        if (!(object instanceof RhRelojes)) {
            return false;
        }
        RhRelojes other = (RhRelojes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models18.RhRelojes[ id=" + id + " ]";
    }
    
}
