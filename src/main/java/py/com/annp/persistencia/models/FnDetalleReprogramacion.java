/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.annp.persistencia.models;

import py.com.startic.gestion.models.*;
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
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 *
 * @author lichi
 */
@Entity
@Table(name = "fn_detalle_reprogramacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FnDetalleReprogramacion.findAll", query = "SELECT f FROM FnDetalleReprogramacion f")
    , @NamedQuery(name = "FnDetalleReprogramacion.findById", query = "SELECT f FROM FnDetalleReprogramacion f WHERE f.id = :id")})
public class FnDetalleReprogramacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_detalle_pto_aumentar", referencedColumnName = "id")
    @ManyToOne
    private FnDetallePlanFinanciero idDetallePtoAumentar;
    @JoinColumn(name = "id_detalle_pto_disminuir", referencedColumnName = "id")
    @ManyToOne
    private FnDetallePlanFinanciero idDetallePtoDisminuir;
    @JoinColumn(name = "tipo_de_variacion_rp", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private FnTipoDeVariacionRp tipoDeVariacionRp;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reprogramaciones")
    private Collection<FnDetalleReprogramacion> fnDetalleReprogramacionCollection;
    @JoinColumn(name = "reprogramaciones", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private FnDetalleReprogramacion reprogramaciones;

    public FnDetalleReprogramacion() {
    }

    public FnDetalleReprogramacion(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public FnDetallePlanFinanciero getIdDetallePtoAumentar() {
        return idDetallePtoAumentar;
    }

    public void setIdDetallePtoAumentar(FnDetallePlanFinanciero idDetallePtoAumentar) {
        this.idDetallePtoAumentar = idDetallePtoAumentar;
    }

    public FnDetallePlanFinanciero getIdDetallePtoDisminuir() {
        return idDetallePtoDisminuir;
    }

    public void setIdDetallePtoDisminuir(FnDetallePlanFinanciero idDetallePtoDisminuir) {
        this.idDetallePtoDisminuir = idDetallePtoDisminuir;
    }

    public FnTipoDeVariacionRp getTipoDeVariacionRp() {
        return tipoDeVariacionRp;
    }

    public void setTipoDeVariacionRp(FnTipoDeVariacionRp tipoDeVariacionRp) {
        this.tipoDeVariacionRp = tipoDeVariacionRp;
    }

    @XmlTransient
    public Collection<FnDetalleReprogramacion> getFnDetalleReprogramacionCollection() {
        return fnDetalleReprogramacionCollection;
    }

    public void setFnDetalleReprogramacionCollection(Collection<FnDetalleReprogramacion> fnDetalleReprogramacionCollection) {
        this.fnDetalleReprogramacionCollection = fnDetalleReprogramacionCollection;
    }

    public FnDetalleReprogramacion getReprogramaciones() {
        return reprogramaciones;
    }

    public void setReprogramaciones(FnDetalleReprogramacion reprogramaciones) {
        this.reprogramaciones = reprogramaciones;
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
        if (!(object instanceof FnDetalleReprogramacion)) {
            return false;
        }
        FnDetalleReprogramacion other = (FnDetalleReprogramacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.staric.gestion.models.FnDetalleReprogramacion[ id=" + id + " ]";
    }
    
}
