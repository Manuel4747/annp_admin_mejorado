/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import java.math.BigDecimal;
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
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "beneficios_por_usuarios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BeneficiosPorUsuarios.findAll", query = "SELECT r FROM BeneficiosPorUsuarios r")
    , @NamedQuery(name = "BeneficiosPorUsuarios.findById", query = "SELECT r FROM BeneficiosPorUsuarios r WHERE r.id = :id")
    , @NamedQuery(name = "BeneficiosPorUsuarios.findByUsuario", query = "SELECT r FROM BeneficiosPorUsuarios r WHERE r.usuario = :usuario ORDER BY r.beneficio.descripcion")})
public class BeneficiosPorUsuarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "monto")
    private BigDecimal monto;
    @JoinColumn(name = "beneficio", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Beneficios beneficio;
    @JoinColumn(name = "usuario", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuario;
    
    public BeneficiosPorUsuarios() {
    }

    public BeneficiosPorUsuarios(Integer id) {
        this.id = id;
    }

    public BeneficiosPorUsuarios(Integer id, BigDecimal monto, Beneficios beneficio, Usuarios usuario) {
        this.id = id;
        this.monto = monto;
        this.beneficio = beneficio;
        this.usuario = usuario;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Beneficios getBeneficio() {
        return beneficio;
    }

    public void setBeneficio(Beneficios beneficio) {
        this.beneficio = beneficio;
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
        if (!(object instanceof BeneficiosPorUsuarios)) {
            return false;
        }
        BeneficiosPorUsuarios other = (BeneficiosPorUsuarios) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.gov.jem.expedientes.BeneficiosPorUsuarios[ id=" + id + " ]";
    }
    
}
