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
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "tipos_prioridad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TiposPrioridad.findAll", query = "SELECT p FROM TiposPrioridad p ORDER BY p.descripcion")
    , @NamedQuery(name = "TiposPrioridad.findById", query = "SELECT p FROM TiposPrioridad p WHERE p.id = :id")
    , @NamedQuery(name = "TiposPrioridad.findByCodigo", query = "SELECT p FROM TiposPrioridad p WHERE p.codigo = :codigo")
    , @NamedQuery(name = "TiposPrioridad.findByDescripcion", query = "SELECT p FROM TiposPrioridad p WHERE p.descripcion = :descripcion")})
public class TiposPrioridad implements Serializable {

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
    @Size(max = 2)
    @Column(name = "codigo")
    private String codigo;

    public TiposPrioridad() {
    }

    public TiposPrioridad(Integer id) {
        this.id = id;
    }

    public TiposPrioridad(Integer id, String descripcion) {
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
        if (!(object instanceof TiposPrioridad)) {
            return false;
        }
        TiposPrioridad other = (TiposPrioridad) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descripcion==null?"":descripcion;
    }
    
}
