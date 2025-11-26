/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.annp.persistencia.models;

import py.com.startic.gestion.models.*;
import java.io.Serializable;
import java.util.Date;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lichi
 */
@Entity
@Table(name = "fn_detalle_presupuesto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FnDetallePresupuesto.findAll", query = "SELECT f FROM FnDetallePresupuesto f")
    , @NamedQuery(name = "FnDetallePresupuesto.findById", query = "SELECT f FROM FnDetallePresupuesto f WHERE f.id = :id")
    , @NamedQuery(name = "FnDetallePresupuesto.findByDescripcion", query = "SELECT f FROM FnDetallePresupuesto f WHERE f.descripcion = :descripcion")
    , @NamedQuery(name = "FnDetallePresupuesto.findByFechaHoraCreacion", query = "SELECT f FROM FnDetallePresupuesto f WHERE f.fechaHoraCreacion = :fechaHoraCreacion")
    , @NamedQuery(name = "FnDetallePresupuesto.findByUsuarioCreacion", query = "SELECT f FROM FnDetallePresupuesto f WHERE f.usuarioCreacion = :usuarioCreacion")
    , @NamedQuery(name = "FnDetallePresupuesto.findByFechaHoraModificacion", query = "SELECT f FROM FnDetallePresupuesto f WHERE f.fechaHoraModificacion = :fechaHoraModificacion")
    , @NamedQuery(name = "FnDetallePresupuesto.findByUsuarioModificacion", query = "SELECT f FROM FnDetallePresupuesto f WHERE f.usuarioModificacion = :usuarioModificacion")
    , @NamedQuery(name = "FnDetallePresupuesto.findByValorInicial", query = "SELECT f FROM FnDetallePresupuesto f WHERE f.valorInicial = :valorInicial")
    , @NamedQuery(name = "FnDetallePresupuesto.findByValor", query = "SELECT f FROM FnDetallePresupuesto f WHERE f.valor = :valor")})
public class FnDetallePresupuesto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10000)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_creacion")
    @Temporal(TemporalType.DATE)
    private Date fechaHoraCreacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "usuario_creacion")
    private int usuarioCreacion;
    @Column(name = "fecha_hora_modificacion")
    @Temporal(TemporalType.DATE)
    private Date fechaHoraModificacion;
    @Column(name = "usuario_modificacion")
    private Integer usuarioModificacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor_inicial")
    private long valorInicial;
    @Basic(optional = false)
    @NotNull
    @Column(name = "valor")
    private long valor;
    @JoinColumn(name = "distribucion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private FnDistribucion distribucion;
    @JoinColumn(name = "objeto_de_gasto", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private FnObjetosDeGasto objetoDeGasto;
    @JoinColumn(name = "presupuesto", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private FnPresupuesto presupuesto;
    @JoinColumn(name = "proyecto", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private FnProyectos proyecto;
    @JoinColumn(name = "subdistribucion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private FnSubDistribucion subdistribucion;

    public FnDetallePresupuesto() {
    }

    public FnDetallePresupuesto(Integer id) {
        this.id = id;
    }

    public FnDetallePresupuesto(Integer id, String descripcion, Date fechaHoraCreacion, int usuarioCreacion, long valorInicial, long valor) {
        this.id = id;
        this.descripcion = descripcion;
        this.fechaHoraCreacion = fechaHoraCreacion;
        this.usuarioCreacion = usuarioCreacion;
        this.valorInicial = valorInicial;
        this.valor = valor;
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

    public Date getFechaHoraCreacion() {
        return fechaHoraCreacion;
    }

    public void setFechaHoraCreacion(Date fechaHoraCreacion) {
        this.fechaHoraCreacion = fechaHoraCreacion;
    }

    public int getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(int usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
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

    public long getValorInicial() {
        return valorInicial;
    }

    public void setValorInicial(long valorInicial) {
        this.valorInicial = valorInicial;
    }

    public long getValor() {
        return valor;
    }

    public void setValor(long valor) {
        this.valor = valor;
    }

    public FnDistribucion getDistribucion() {
        return distribucion;
    }

    public void setDistribucion(FnDistribucion distribucion) {
        this.distribucion = distribucion;
    }

    public FnObjetosDeGasto getObjetoDeGasto() {
        return objetoDeGasto;
    }

    public void setObjetoDeGasto(FnObjetosDeGasto objetoDeGasto) {
        this.objetoDeGasto = objetoDeGasto;
    }

    public FnPresupuesto getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(FnPresupuesto presupuesto) {
        this.presupuesto = presupuesto;
    }

    public FnProyectos getProyecto() {
        return proyecto;
    }

    public void setProyecto(FnProyectos proyecto) {
        this.proyecto = proyecto;
    }

    public FnSubDistribucion getSubdistribucion() {
        return subdistribucion;
    }

    public void setSubdistribucion(FnSubDistribucion subdistribucion) {
        this.subdistribucion = subdistribucion;
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
        if (!(object instanceof FnDetallePresupuesto)) {
            return false;
        }
        FnDetallePresupuesto other = (FnDetallePresupuesto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.staric.gestion.models.FnDetallePresupuesto[ id=" + id + " ]";
    }
    
}
