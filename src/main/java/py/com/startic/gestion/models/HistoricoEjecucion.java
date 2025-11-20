/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import java.util.Collection;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;


@Entity
@Table(name = "historico_ejecucion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HistoricoEjecucion.findAll", query = "SELECT h FROM HistoricoEjecucion h")
    , @NamedQuery(name = "HistoricoEjecucion.findById", query = "SELECT h FROM HistoricoEjecucion h WHERE h.id = :id")
    , @NamedQuery(name = "HistoricoEjecucion.findByNroPac", query = "SELECT h FROM HistoricoEjecucion h WHERE h.nroPac = :nroPac")
    , @NamedQuery(name = "HistoricoEjecucion.findByNroContrato", query = "SELECT h FROM HistoricoEjecucion h WHERE h.nroContrato = :nroContrato")
    , @NamedQuery(name = "HistoricoEjecucion.findByPreveedor", query = "SELECT h FROM HistoricoEjecucion h WHERE h.preveedor = :preveedor")
    , @NamedQuery(name = "HistoricoEjecucion.findByValorTotal", query = "SELECT h FROM HistoricoEjecucion h WHERE h.valorTotal = :valorTotal")
    , @NamedQuery(name = "HistoricoEjecucion.findByEjecucion", query = "SELECT h FROM HistoricoEjecucion h WHERE h.ejecucion = :ejecucion")
    , @NamedQuery(name = "HistoricoEjecucion.findBySaldoContractual", query = "SELECT h FROM HistoricoEjecucion h WHERE h.saldoContractual = :saldoContractual")
    , @NamedQuery(name = "HistoricoEjecucion.findByPorcentajeEjecucion", query = "SELECT h FROM HistoricoEjecucion h WHERE h.porcentajeEjecucion = :porcentajeEjecucion")
    , @NamedQuery(name = "HistoricoEjecucion.findByEstado", query = "SELECT h FROM HistoricoEjecucion h WHERE h.estado = :estado")})
public class HistoricoEjecucion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nro_pac")
    private int nroPac;
    @Size(max = 9)
    @Column(name = "nro_contrato")
    private String nroContrato;
     @Basic(optional = false)
    @NotNull
    @Size(max = 50)
    @Column(name = "preveedor")
    private String preveedor;
    @Column(name = "valor_total")
    private Long valorTotal;
    @Column(name = "ejecucion")
    private Long ejecucion;
    @Column(name = "saldo_contractual")
    private Long saldoContractual;
    @Size(max = 50)
    @Column(name = "porcentaje_ejecucion")
    private String porcentajeEjecucion;
    @Column(name = "estado")
    private Boolean estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "historicoEjecucion")
    private Collection<Formulario> formularioCollection;

    public HistoricoEjecucion() {
    }

    public HistoricoEjecucion(Integer id) {
        this.id = id;
    }

    public HistoricoEjecucion(Integer id, int nroPac) {
        this.id = id;
        this.nroPac = nroPac;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getNroPac() {
        return nroPac;
    }

    public void setNroPac(int nroPac) {
        this.nroPac = nroPac;
    }

    public String getNroContrato() {
        return nroContrato;
    }

    public void setNroContrato(String nroContrato) {
        this.nroContrato = nroContrato;
    }

    public String getPreveedor() {
        return preveedor;
    }

    public void setPreveedor(String preveedor) {
        this.preveedor = preveedor;
    }

    public Long getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Long valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Long getEjecucion() {
        return ejecucion;
    }

    public void setEjecucion(Long ejecucion) {
        this.ejecucion = ejecucion;
    }

    public Long getSaldoContractual() {
        return saldoContractual;
    }

    public void setSaldoContractual(Long saldoContractual) {
        this.saldoContractual = saldoContractual;
    }

    public String getPorcentajeEjecucion() {
        return porcentajeEjecucion;
    }

    public void setPorcentajeEjecucion(String porcentajeEjecucion) {
        this.porcentajeEjecucion = porcentajeEjecucion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    @XmlTransient
    public Collection<Formulario> getFormularioCollection() {
        return formularioCollection;
    }

    public void setFormularioCollection(Collection<Formulario> formularioCollection) {
        this.formularioCollection = formularioCollection;
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
        if (!(object instanceof HistoricoEjecucion)) {
            return false;
        }
        HistoricoEjecucion other = (HistoricoEjecucion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.model.HistoricoEjecucion[ id=" + id + " ]";
    }
    
}
