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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "tipos_expediente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TiposExpediente.findAll", query = "SELECT t FROM TiposExpediente t ORDER BY t.descripcion")
    , @NamedQuery(name = "TiposExpediente.findByMostrarMenu", query = "SELECT t FROM TiposExpediente t WHERE t.mostrarMenu = :mostrarMenu ORDER BY t.descripcion")
    , @NamedQuery(name = "TiposExpediente.findById", query = "SELECT t FROM TiposExpediente t WHERE t.id = :id")
    , @NamedQuery(name = "TiposExpediente.findByDescripcion", query = "SELECT t FROM TiposExpediente t WHERE t.descripcion = :descripcion")})
public class TiposExpediente implements Serializable {

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
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @Column(name = "mostrar_menu")
    private boolean mostrarMenu;

    public TiposExpediente() {
    }

    public TiposExpediente(Integer id) {
        this.id = id;
    }

    public TiposExpediente(Integer id, String descripcion) {
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

    public boolean isMostrarMenu() {
        return mostrarMenu;
    }

    public void setMostrarMenu(boolean mostrarMenu) {
        this.mostrarMenu = mostrarMenu;
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
        if (!(object instanceof TiposExpediente)) {
            return false;
        }
        TiposExpediente other = (TiposExpediente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.TiposExpediente[ id=" + id + " ]";
    }
    
}
