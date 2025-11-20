/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

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
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;


@Entity
@Table(name = "historico_contrato")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HistoricoContrato.findAll", query = "SELECT h FROM HistoricoContrato h")
    , @NamedQuery(name = "HistoricoContrato.findById", query = "SELECT h FROM HistoricoContrato h WHERE h.id = :id")
    , @NamedQuery(name = "HistoricoContrato.findByNroPac", query = "SELECT h FROM HistoricoContrato h WHERE h.nroPac = :nroPac")
    , @NamedQuery(name = "HistoricoContrato.findByNroContrato", query = "SELECT h FROM HistoricoContrato h WHERE h.nroContrato = :nroContrato")
    , @NamedQuery(name = "HistoricoContrato.findByVigencia", query = "SELECT h FROM HistoricoContrato h WHERE h.vigencia = :vigencia")
    , @NamedQuery(name = "HistoricoContrato.findByDesde", query = "SELECT h FROM HistoricoContrato h WHERE h.desde = :desde")
    , @NamedQuery(name = "HistoricoContrato.findByHasta", query = "SELECT h FROM HistoricoContrato h WHERE h.hasta = :hasta")
    , @NamedQuery(name = "HistoricoContrato.findByValorTotal", query = "SELECT h FROM HistoricoContrato h WHERE h.valorTotal = :valorTotal")
    , @NamedQuery(name = "HistoricoContrato.findByEstado", query = "SELECT h FROM HistoricoContrato h WHERE h.estado = :estado")})
public class HistoricoContrato implements Serializable {

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
    @Column(name = "vigencia")
    private String vigencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "desde")
    @Temporal(TemporalType.DATE)
    private Date desde;
    @Basic(optional = false)
    @NotNull
    @Column(name = "hasta")
    @Temporal(TemporalType.DATE)
    private Date hasta;
    @Column(name = "valor_total")
    private Long valorTotal;
    @Column(name = "estado")
    private Boolean estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "historicoContrato")
    private Collection<Formulario> formularioCollection;

    public HistoricoContrato() {
    }

    public HistoricoContrato(Integer id) {
        this.id = id;
    }

    public HistoricoContrato(Integer id, int nroPac) {
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

    public String getVigencia() {
        return vigencia;
    }

    public void setVigencia(String vigencia) {
        this.vigencia = vigencia;
    }

    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
    }

    public Long getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Long valorTotal) {
        this.valorTotal = valorTotal;
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
        if (!(object instanceof HistoricoContrato)) {
            return false;
        }
        HistoricoContrato other = (HistoricoContrato) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.model.HistoricoContrato[ id=" + id + " ]";
    }
    
}
