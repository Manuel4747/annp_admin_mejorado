/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author grecia
 */
@Entity
@Table(name = "tipos_objetivos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TiposObjetivos.findAll", query = "SELECT t FROM TiposObjetivos  t")
    , @NamedQuery(name = "TiposObjetivos.findById", query = "SELECT t FROM TiposObjetivos t WHERE t.id = :id")
    , @NamedQuery(name = "TiposObjetivos.findByDescripcion", query = "SELECT t FROM TiposObjetivos t WHERE t.descripcion = :descripcion")})
public class TiposObjetivos implements Serializable {

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
    @Size(min = 1, max = 200)
    @Column(name = "objetivo")
    private String objetivo;

    public TiposObjetivos() {
    }

    public TiposObjetivos(Integer id) {
        this.id = id;
    }

    public TiposObjetivos(Integer id, String descripcion) {
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

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
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
        if (!(object instanceof TiposObjetivos)) {
            return false;
        }
        TiposObjetivos other = (TiposObjetivos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.TiposObjetivos[ id=" + id + " ]";
    }
    
}
