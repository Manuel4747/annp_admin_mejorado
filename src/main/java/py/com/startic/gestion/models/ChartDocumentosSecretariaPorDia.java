/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
 * @author eduardo
 */
@Entity
@Table(name = "chart_documentos_secretaria_por_dia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ChartDocumentosSecretariaPorDia.findAll", query = "SELECT e FROM ChartDocumentosSecretariaPorDia e")
    , @NamedQuery(name = "ChartDocumentosSecretariaPorDia.findByRangoFechaPresentacion", query = "SELECT e FROM ChartDocumentosSecretariaPorDia e WHERE e.fechaPresentacion >= :fechaDesde and e.fechaPresentacion <= :fechaHasta AND e.tipoDocumentoJudicial = :tipoDocumentoJudicial ORDER BY e.fechaPresentacion")
})
public class ChartDocumentosSecretariaPorDia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "tipo_documento_judicial")
    private String tipoDocumentoJudicial;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "descripcion_tipo_documento_judicial")
    private String descripcionTipoDocumentoJudicial;
    @Column(name = "fecha_presentacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPresentacion;
    @Basic(optional = false)
    @Column(name = "cantidad")
    private Integer cantidad;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;

    public ChartDocumentosSecretariaPorDia() {
    }

    public ChartDocumentosSecretariaPorDia(String codigo) {
        this.codigo = codigo;
    }

    public String getTipoDocumentoJudicial() {
        return tipoDocumentoJudicial;
    }

    public void setTipoDocumentoJudicial(String tipoDocumentoJudicial) {
        this.tipoDocumentoJudicial = tipoDocumentoJudicial;
    }

    public String getDescripcionTipoDocumentoJudicial() {
        return descripcionTipoDocumentoJudicial;
    }

    public void setDescripcionTipoDocumentoJudicial(String descripcionTipoDocumentoJudicial) {
        this.descripcionTipoDocumentoJudicial = descripcionTipoDocumentoJudicial;
    }

    public Date getFechaPresentacion() {
        return fechaPresentacion;
    }

    public void setFechaPresentacion(Date fechaPresentacion) {
        this.fechaPresentacion = fechaPresentacion;
    }

    public String getCodigo() {
        return codigo;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
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
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ChartDocumentosSecretariaPorDia)) {
            return false;
        }
        ChartDocumentosSecretariaPorDia other = (ChartDocumentosSecretariaPorDia) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return codigo;
    }
    
}
