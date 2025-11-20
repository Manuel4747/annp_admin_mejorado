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
import jakarta.persistence.Id;
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
@Table(name = "estados_transferencia_documento_administrativo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadosTransferenciaDocumentoAdministrativo.findAll", query = "SELECT e FROM EstadosTransferenciaDocumentoAdministrativo e ORDER BY e.orden")
    , @NamedQuery(name = "EstadosTransferenciaDocumentoAdministrativo.findByCodigo", query = "SELECT e FROM EstadosTransferenciaDocumentoAdministrativo e WHERE e.codigo = :codigo ORDER BY e.orden")
    , @NamedQuery(name = "EstadosTransferenciaDocumentoAdministrativo.findByOrden", query = "SELECT e FROM EstadosTransferenciaDocumentoAdministrativo e WHERE e.orden = :orden")
    , @NamedQuery(name = "EstadosTransferenciaDocumentoAdministrativo.findByMenorOrden", query = "SELECT e FROM EstadosTransferenciaDocumentoAdministrativo e WHERE e.orden <= :orden ORDER BY e.orden")
    , @NamedQuery(name = "EstadosTransferenciaDocumentoAdministrativo.findByDescripcion", query = "SELECT e FROM EstadosTransferenciaDocumentoAdministrativo e WHERE e.descripcion = :descripcion")})
public class EstadosTransferenciaDocumentoAdministrativo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "codigo")
    private String codigo;
    @Size(max = 200)
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = true)
    @Column(name = "orden")
    private Integer orden;

    public EstadosTransferenciaDocumentoAdministrativo() {
    }

    public EstadosTransferenciaDocumentoAdministrativo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
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
        if (!(object instanceof EstadosTransferenciaDocumentoAdministrativo)) {
            return false;
        }
        EstadosTransferenciaDocumentoAdministrativo other = (EstadosTransferenciaDocumentoAdministrativo) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.EstadosTransferenciaDocumentoAdministrativo[ codigo=" + codigo + " ]";
    }
    
}
