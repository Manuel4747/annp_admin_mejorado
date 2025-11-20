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
@Table(name = "rh_plantillas_horario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RhPlantillasHorario.findAll", query = "SELECT r FROM RhPlantillasHorario r")
    , @NamedQuery(name = "RhPlantillasHorario.findById", query = "SELECT r FROM RhPlantillasHorario r WHERE r.id = :id")
    , @NamedQuery(name = "RhPlantillasHorario.findByDescripcion", query = "SELECT r FROM RhPlantillasHorario r WHERE r.descripcion = :descripcion")})
public class RhPlantillasHorario implements Serializable {

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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "plantillaHorario")
    private Collection<RhDetPlantillasHorario> rhDetPlantillasHorarioCollection;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;

    public RhPlantillasHorario() {
    }

    public RhPlantillasHorario(Integer id) {
        this.id = id;
    }

    public RhPlantillasHorario(Integer id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
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

    @XmlTransient
    public Collection<RhDetPlantillasHorario> getRhDetPlantillasHorarioCollection() {
        return rhDetPlantillasHorarioCollection;
    }

    public void setRhDetPlantillasHorarioCollection(Collection<RhDetPlantillasHorario> rhDetPlantillasHorarioCollection) {
        this.rhDetPlantillasHorarioCollection = rhDetPlantillasHorarioCollection;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RhPlantillasHorario)) {
            return false;
        }
        RhPlantillasHorario other = (RhPlantillasHorario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models21.RhPlantillasHorario[ id=" + id + " ]";
    }
    
}
