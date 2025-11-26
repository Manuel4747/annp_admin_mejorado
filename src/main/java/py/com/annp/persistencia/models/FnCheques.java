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

/**
 *
 * @author lichi
 */
@Entity
@Table(name = "fn_cheques")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FnCheques.findAll", query = "SELECT f FROM FnCheques f")
    , @NamedQuery(name = "FnCheques.findById", query = "SELECT f FROM FnCheques f WHERE f.id = :id")
    , @NamedQuery(name = "FnCheques.findByBanco", query = "SELECT f FROM FnCheques f WHERE f.banco = :banco")
    , @NamedQuery(name = "FnCheques.findByCuenta", query = "SELECT f FROM FnCheques f WHERE f.cuenta = :cuenta")
    , @NamedQuery(name = "FnCheques.findByNumero", query = "SELECT f FROM FnCheques f WHERE f.numero = :numero")
    , @NamedQuery(name = "FnCheques.findByFechaEmision", query = "SELECT f FROM FnCheques f WHERE f.fechaEmision = :fechaEmision")
    , @NamedQuery(name = "FnCheques.findByFechaCobro", query = "SELECT f FROM FnCheques f WHERE f.fechaCobro = :fechaCobro")})
public class FnCheques implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Column(name = "banco")
    private Integer banco;
    @Size(max = 50)
    @Column(name = "cuenta")
    private String cuenta;
    @Size(max = 50)
    @Column(name = "numero")
    private String numero;
    @Column(name = "fecha_emision")
    @Temporal(TemporalType.DATE)
    private Date fechaEmision;
    @Column(name = "fecha_cobro")
    @Temporal(TemporalType.DATE)
    private Date fechaCobro;
    @OneToMany(mappedBy = "cheque")
    private Collection<FnPagos> fnPagosCollection;

    public FnCheques() {
    }

    public FnCheques(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBanco() {
        return banco;
    }

    public void setBanco(Integer banco) {
        this.banco = banco;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaCobro() {
        return fechaCobro;
    }

    public void setFechaCobro(Date fechaCobro) {
        this.fechaCobro = fechaCobro;
    }

    @XmlTransient
    public Collection<FnPagos> getFnPagosCollection() {
        return fnPagosCollection;
    }

    public void setFnPagosCollection(Collection<FnPagos> fnPagosCollection) {
        this.fnPagosCollection = fnPagosCollection;
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
        if (!(object instanceof FnCheques)) {
            return false;
        }
        FnCheques other = (FnCheques) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.staric.gestion.models.FnCheques[ id=" + id + " ]";
    }
    
}
