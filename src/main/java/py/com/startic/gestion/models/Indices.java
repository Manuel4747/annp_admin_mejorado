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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "indices")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Indices.findAll", query = "SELECT i FROM Indices i")
    , @NamedQuery(name = "Indices.findById", query = "SELECT i FROM Indices i WHERE i.id = :id")
    , @NamedQuery(name = "Indices.findByNombreIndice", query = "SELECT i FROM Indices i WHERE i.nombreIndice = :nombreIndice")
    , @NamedQuery(name = "Indices.findByOrden", query = "SELECT i FROM Indices i WHERE i.orden = :orden")
    , @NamedQuery(name = "Indices.findByValorX", query = "SELECT i FROM Indices i WHERE i.valorX = :valorX")
    , @NamedQuery(name = "Indices.findByValorY", query = "SELECT i FROM Indices i WHERE i.valorY = :valorY")})
public class Indices implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nombre_indice")
    private String nombreIndice;
    @Basic(optional = false)
    @NotNull
    @Column(name = "orden")
    private int orden;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "valor_x")
    private String valorX;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "valor_y")
    private String valorY;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;

    public Indices() {
    }

    public Indices(Integer id) {
        this.id = id;
    }

    public Indices(Integer id, String nombreIndice, int orden, String valorX, String valorY) {
        this.id = id;
        this.nombreIndice = nombreIndice;
        this.orden = orden;
        this.valorX = valorX;
        this.valorY = valorY;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreIndice() {
        return nombreIndice;
    }

    public void setNombreIndice(String nombreIndice) {
        this.nombreIndice = nombreIndice;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public String getValorX() {
        return valorX;
    }

    public void setValorX(String valorX) {
        this.valorX = valorX;
    }

    public String getValorY() {
        return valorY;
    }

    public void setValorY(String valorY) {
        this.valorY = valorY;
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
        if (!(object instanceof Indices)) {
            return false;
        }
        Indices other = (Indices) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.Indices[ id=" + id + " ]";
    }
    
}
