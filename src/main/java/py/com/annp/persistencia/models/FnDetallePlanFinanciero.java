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
@Table(name = "fn_detalle_plan_financiero")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FnDetallePlanFinanciero.findAll", query = "SELECT f FROM FnDetallePlanFinanciero f")
    , @NamedQuery(name = "FnDetallePlanFinanciero.findById", query = "SELECT f FROM FnDetallePlanFinanciero f WHERE f.id = :id")
    , @NamedQuery(name = "FnDetallePlanFinanciero.findByDetallePlanFinancierocol", query = "SELECT f FROM FnDetallePlanFinanciero f WHERE f.detallePlanFinancierocol = :detallePlanFinancierocol")
    , @NamedQuery(name = "FnDetallePlanFinanciero.findByMes", query = "SELECT f FROM FnDetallePlanFinanciero f WHERE f.mes = :mes")
    , @NamedQuery(name = "FnDetallePlanFinanciero.findByValor", query = "SELECT f FROM FnDetallePlanFinanciero f WHERE f.valor = :valor")})
public class FnDetallePlanFinanciero implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Size(max = 45)
    @Column(name = "detalle_plan_financierocol")
    private String detallePlanFinancierocol;
    @Column(name = "mes")
    private Integer mes;
    @Column(name = "valor")
    private Long valor;
    @JoinColumn(name = "departamento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Departamentos departamento;
    @JoinColumn(name = "distribucion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private FnDistribucion distribucion;
    @JoinColumn(name = "fuentes_de_financiamiento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private FnFuentesDeFinanciamiento fuentesDeFinanciamiento;
    @JoinColumn(name = "objetos_de_gasto", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private FnObjetosDeGasto objetosDeGasto;
    @JoinColumn(name = "plan_financiero", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private FnPlanFinanciero planFinanciero;
    @JoinColumn(name = "proyecto", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private FnProyectos proyecto;
    @JoinColumn(name = "sub_distribucion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private FnSubDistribucion subDistribucion;
    @OneToMany(mappedBy = "idDetallePtoAumentar")
    private Collection<FnDetalleReprogramacion> fnDetalleReprogramacionCollection;
    @OneToMany(mappedBy = "idDetallePtoDisminuir")
    private Collection<FnDetalleReprogramacion> fnDetalleReprogramacionCollection1;

    public FnDetallePlanFinanciero() {
    }

    public FnDetallePlanFinanciero(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDetallePlanFinancierocol() {
        return detallePlanFinancierocol;
    }

    public void setDetallePlanFinancierocol(String detallePlanFinancierocol) {
        this.detallePlanFinancierocol = detallePlanFinancierocol;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Long getValor() {
        return valor;
    }

    public void setValor(Long valor) {
        this.valor = valor;
    }

    public Departamentos getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamentos departamento) {
        this.departamento = departamento;
    }

    public FnDistribucion getDistribucion() {
        return distribucion;
    }

    public void setDistribucion(FnDistribucion distribucion) {
        this.distribucion = distribucion;
    }

    public FnFuentesDeFinanciamiento getFuentesDeFinanciamiento() {
        return fuentesDeFinanciamiento;
    }

    public void setFuentesDeFinanciamiento(FnFuentesDeFinanciamiento fuentesDeFinanciamiento) {
        this.fuentesDeFinanciamiento = fuentesDeFinanciamiento;
    }

    public FnObjetosDeGasto getObjetosDeGasto() {
        return objetosDeGasto;
    }

    public void setObjetosDeGasto(FnObjetosDeGasto objetosDeGasto) {
        this.objetosDeGasto = objetosDeGasto;
    }

    public FnPlanFinanciero getPlanFinanciero() {
        return planFinanciero;
    }

    public void setPlanFinanciero(FnPlanFinanciero planFinanciero) {
        this.planFinanciero = planFinanciero;
    }

    public FnProyectos getProyecto() {
        return proyecto;
    }

    public void setProyecto(FnProyectos proyecto) {
        this.proyecto = proyecto;
    }

    public FnSubDistribucion getSubDistribucion() {
        return subDistribucion;
    }

    public void setSubDistribucion(FnSubDistribucion subDistribucion) {
        this.subDistribucion = subDistribucion;
    }

    @XmlTransient
    public Collection<FnDetalleReprogramacion> getFnDetalleReprogramacionCollection() {
        return fnDetalleReprogramacionCollection;
    }

    public void setFnDetalleReprogramacionCollection(Collection<FnDetalleReprogramacion> fnDetalleReprogramacionCollection) {
        this.fnDetalleReprogramacionCollection = fnDetalleReprogramacionCollection;
    }

    @XmlTransient
    public Collection<FnDetalleReprogramacion> getFnDetalleReprogramacionCollection1() {
        return fnDetalleReprogramacionCollection1;
    }

    public void setFnDetalleReprogramacionCollection1(Collection<FnDetalleReprogramacion> fnDetalleReprogramacionCollection1) {
        this.fnDetalleReprogramacionCollection1 = fnDetalleReprogramacionCollection1;
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
        if (!(object instanceof FnDetallePlanFinanciero)) {
            return false;
        }
        FnDetallePlanFinanciero other = (FnDetallePlanFinanciero) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.staric.gestion.models.FnDetallePlanFinanciero[ id=" + id + " ]";
    }
    
}
