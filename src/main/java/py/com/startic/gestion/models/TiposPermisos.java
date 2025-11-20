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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author grecia
 */
@Entity
@Table(name = "tipos_permisos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TiposPermisos.findAll", query = "SELECT r FROM TiposPermisos r")
    , @NamedQuery(name = "TiposPermisos.findById", query = "SELECT r FROM TiposPermisos r WHERE r.id = :id")
    , @NamedQuery(name = "TiposPermisos.findByListas", query = "SELECT r FROM TiposPermisos r WHERE r.tiposFormulario = :tiposFormulario")
    , @NamedQuery(name = "TiposPermisos.findByDescripcion", query = "SELECT r FROM TiposPermisos r WHERE r.descripcion = :descripcion")})
public class TiposPermisos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "cantidad")
    private Integer cantidad;
     @Size(max = 45)
    @Column(name = "especificacion")
    private String especificacion;
   // @Basic(optional = false)
    @JoinColumn(name = "tipo_formulario", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private TiposFormulario tiposFormulario;
    
   
   
    
    public TiposPermisos() {
    }

    public TiposPermisos(Integer id) {
        this.id = id;
    }

    public TiposPermisos(Integer id, String descripcion) {
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

    public TiposFormulario getTiposFormulario() {
        return tiposFormulario;
    }

    public void setTiposFormulario(TiposFormulario tiposFormulario) {
        this.tiposFormulario = tiposFormulario;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getEspecificacion() {
        return especificacion;
    }

    public void setEspecificacion(String especificacion) {
        this.especificacion = especificacion;
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
        if (!(object instanceof TiposPermisos)) {
            return false;
        }
        TiposPermisos other = (TiposPermisos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.Roles[ id=" + id + " ]";
    }
    
}
