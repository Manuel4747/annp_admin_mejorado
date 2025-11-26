/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.annp.persistencia.models;

import java.io.Serializable;
import java.util.Collection;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 *
 * @author lichi
 */
@Entity
@Table(name = "fn_articulos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FnArticulos.findAll", query = "SELECT f FROM FnArticulos f")
    , @NamedQuery(name = "FnArticulos.findById", query = "SELECT f FROM FnArticulos f WHERE f.id = :id")
    , @NamedQuery(name = "FnArticulos.findByDescipcion", query = "SELECT f FROM FnArticulos f WHERE f.descipcion = :descipcion")})
public class FnArticulos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    //@NotNull
    @Column(name = "id")
    private Integer id;
    @Size(max = 5000)
    @Column(name = "descipcion")
    private String descipcion;
    @OneToMany(mappedBy = "articulo")
    private Collection<FnArticuloCaracteristicas> fnArticuloCaracteristicasCollection;
    @JoinColumn(name = "categoria", referencedColumnName = "id")
    @ManyToOne
    private FnCategoriaArticulo categoria;
    @JoinColumn(name = "rubro", referencedColumnName = "id")
    @ManyToOne
    private FnRubroArticulo rubro;
    @JoinColumn(name = "tipo", referencedColumnName = "id")
    @ManyToOne
    private FnTipoArticulo tipo;
    @OneToMany(mappedBy = "articulo")
    private Collection<FnDetalleFactura> fnDetalleFacturaCollection;

    public FnArticulos() {
    }

    public FnArticulos(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescipcion() {
        return descipcion;
    }

    public void setDescipcion(String descipcion) {
        this.descipcion = descipcion;
    }

    @XmlTransient
    public Collection<FnArticuloCaracteristicas> getFnArticuloCaracteristicasCollection() {
        return fnArticuloCaracteristicasCollection;
    }

    public void setFnArticuloCaracteristicasCollection(Collection<FnArticuloCaracteristicas> fnArticuloCaracteristicasCollection) {
        this.fnArticuloCaracteristicasCollection = fnArticuloCaracteristicasCollection;
    }

    public FnCategoriaArticulo getCategoria() {
        return categoria;
    }

    public void setCategoria(FnCategoriaArticulo categoria) {
        this.categoria = categoria;
    }

    public FnRubroArticulo getRubro() {
        return rubro;
    }

    public void setRubro(FnRubroArticulo rubro) {
        this.rubro = rubro;
    }

    public FnTipoArticulo getTipo() {
        return tipo;
    }

    public void setTipo(FnTipoArticulo tipo) {
        this.tipo = tipo;
    }

    @XmlTransient
    public Collection<FnDetalleFactura> getFnDetalleFacturaCollection() {
        return fnDetalleFacturaCollection;
    }

    public void setFnDetalleFacturaCollection(Collection<FnDetalleFactura> fnDetalleFacturaCollection) {
        this.fnDetalleFacturaCollection = fnDetalleFacturaCollection;
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
        if (!(object instanceof FnArticulos)) {
            return false;
        }
        FnArticulos other = (FnArticulos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.staric.gestion.models.FnArticulos[ id=" + id + " ]";
    }
    
}
