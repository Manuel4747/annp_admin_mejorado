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
@Table(name = "tipos_archivo_administrativo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TiposArchivoAdministrativo.findAll", query = "SELECT t FROM TiposArchivoAdministrativo t ORDER BY t.descripcion")
    , @NamedQuery(name = "TiposArchivoAdministrativo.findById", query = "SELECT t FROM TiposArchivoAdministrativo t WHERE t.id = :id")
    , @NamedQuery(name = "TiposArchivoAdministrativo.findByMostrarEnMenu", query = "SELECT t FROM TiposArchivoAdministrativo t WHERE t.mostrarEnMenu = :mostrarEnMenu ORDER BY t.descripcion")
    , @NamedQuery(name = "TiposArchivoAdministrativo.findByDescripcion", query = "SELECT t FROM TiposArchivoAdministrativo t WHERE t.descripcion = :descripcion")})
public class TiposArchivoAdministrativo implements Serializable {

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
    @Column(name = "mostrar_en_menu")
    private boolean mostrarEnMenu;
    
    public TiposArchivoAdministrativo() {
    }

    public TiposArchivoAdministrativo(Integer id) {
        this.id = id;
    }

    public TiposArchivoAdministrativo(Integer id, String descripcion) {
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

    public boolean isMostrarEnMenu() {
        return mostrarEnMenu;
    }

    public void setMostrarEnMenu(boolean mostrarEnMenu) {
        this.mostrarEnMenu = mostrarEnMenu;
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
        if (!(object instanceof TiposArchivoAdministrativo)) {
            return false;
        }
        TiposArchivoAdministrativo other = (TiposArchivoAdministrativo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return (descripcion != null)?descripcion:"";
    }
    
}
