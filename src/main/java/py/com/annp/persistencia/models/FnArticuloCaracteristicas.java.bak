/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.annp.persistencia.models;

import py.com.startic.gestion.models.*;
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
 * @author lichi
 */
@Entity
@Table(name = "fn_articulo_caracteristicas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FnArticuloCaracteristicas.findAll", query = "SELECT f FROM FnArticuloCaracteristicas f")
    , @NamedQuery(name = "FnArticuloCaracteristicas.findById", query = "SELECT f FROM FnArticuloCaracteristicas f WHERE f.id = :id")
    , @NamedQuery(name = "FnArticuloCaracteristicas.findByCaracteristica", query = "SELECT f FROM FnArticuloCaracteristicas f WHERE f.caracteristica = :caracteristica")
    , @NamedQuery(name = "FnArticuloCaracteristicas.findByValor", query = "SELECT f FROM FnArticuloCaracteristicas f WHERE f.valor = :valor")})
public class FnArticuloCaracteristicas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Size(max = 5000)
    @Column(name = "caracteristica")
    private String caracteristica;
    @Size(max = 5000)
    @Column(name = "valor")
    private String valor;
    @JoinColumn(name = "articulo", referencedColumnName = "id")
    @ManyToOne
    private FnArticulos articulo;

    public FnArticuloCaracteristicas() {
    }

    public FnArticuloCaracteristicas(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCaracteristica() {
        return caracteristica;
    }

    public void setCaracteristica(String caracteristica) {
        this.caracteristica = caracteristica;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public FnArticulos getArticulo() {
        return articulo;
    }

    public void setArticulo(FnArticulos articulo) {
        this.articulo = articulo;
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
        if (!(object instanceof FnArticuloCaracteristicas)) {
            return false;
        }
        FnArticuloCaracteristicas other = (FnArticuloCaracteristicas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.staric.gestion.models.FnArticuloCaracteristicas[ id=" + id + " ]";
    }
    
}
