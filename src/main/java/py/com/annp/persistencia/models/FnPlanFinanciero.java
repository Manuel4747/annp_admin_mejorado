/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.annp.persistencia.models;

import py.com.startic.gestion.models.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 *
 * @author lichi
 */
@Entity
@Table(name = "fn_plan_financiero")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FnPlanFinanciero.findAll", query = "SELECT f FROM FnPlanFinanciero f")
    , @NamedQuery(name = "FnPlanFinanciero.findById", query = "SELECT f FROM FnPlanFinanciero f WHERE f.id = :id")
    , @NamedQuery(name = "FnPlanFinanciero.findByPeriodo", query = "SELECT f FROM FnPlanFinanciero f WHERE f.periodo = :periodo")
    , @NamedQuery(name = "FnPlanFinanciero.findByFechaHoraCreacion", query = "SELECT f FROM FnPlanFinanciero f WHERE f.fechaHoraCreacion = :fechaHoraCreacion")
    , @NamedQuery(name = "FnPlanFinanciero.findByUsuarionCreacion", query = "SELECT f FROM FnPlanFinanciero f WHERE f.usuarionCreacion = :usuarionCreacion")
    , @NamedQuery(name = "FnPlanFinanciero.findByFechaHoraModificacion", query = "SELECT f FROM FnPlanFinanciero f WHERE f.fechaHoraModificacion = :fechaHoraModificacion")
    , @NamedQuery(name = "FnPlanFinanciero.findByUsuarioModificacion", query = "SELECT f FROM FnPlanFinanciero f WHERE f.usuarioModificacion = :usuarioModificacion")})
public class FnPlanFinanciero implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Column(name = "periodo")
    private Integer periodo;
    @Column(name = "fecha_hora_creacion")
    @Temporal(TemporalType.DATE)
    private Date fechaHoraCreacion;
    @Column(name = "usuarion_creacion")
    private Integer usuarionCreacion;
    @Column(name = "fecha_hora_modificacion")
    @Temporal(TemporalType.DATE)
    private Date fechaHoraModificacion;
    @Column(name = "usuario_modificacion")
    private Integer usuarioModificacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "planFinanciero")
    private Collection<FnDetallePlanFinanciero> fnDetallePlanFinancieroCollection;
    @JoinColumn(name = "programa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private FnProgramas programa;
    @JoinColumn(name = "tipos_presupuesto", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private FnTiposPresupuesto tiposPresupuesto;

    public FnPlanFinanciero() {
    }

    public FnPlanFinanciero(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }

    public Date getFechaHoraCreacion() {
        return fechaHoraCreacion;
    }

    public void setFechaHoraCreacion(Date fechaHoraCreacion) {
        this.fechaHoraCreacion = fechaHoraCreacion;
    }

    public Integer getUsuarionCreacion() {
        return usuarionCreacion;
    }

    public void setUsuarionCreacion(Integer usuarionCreacion) {
        this.usuarionCreacion = usuarionCreacion;
    }

    public Date getFechaHoraModificacion() {
        return fechaHoraModificacion;
    }

    public void setFechaHoraModificacion(Date fechaHoraModificacion) {
        this.fechaHoraModificacion = fechaHoraModificacion;
    }

    public Integer getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(Integer usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    @XmlTransient
    public Collection<FnDetallePlanFinanciero> getFnDetallePlanFinancieroCollection() {
        return fnDetallePlanFinancieroCollection;
    }

    public void setFnDetallePlanFinancieroCollection(Collection<FnDetallePlanFinanciero> fnDetallePlanFinancieroCollection) {
        this.fnDetallePlanFinancieroCollection = fnDetallePlanFinancieroCollection;
    }

    public FnProgramas getPrograma() {
        return programa;
    }

    public void setPrograma(FnProgramas programa) {
        this.programa = programa;
    }

    public FnTiposPresupuesto getTiposPresupuesto() {
        return tiposPresupuesto;
    }

    public void setTiposPresupuesto(FnTiposPresupuesto tiposPresupuesto) {
        this.tiposPresupuesto = tiposPresupuesto;
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
        if (!(object instanceof FnPlanFinanciero)) {
            return false;
        }
        FnPlanFinanciero other = (FnPlanFinanciero) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.staric.gestion.models.FnPlanFinanciero[ id=" + id + " ]";
    }
    
}
