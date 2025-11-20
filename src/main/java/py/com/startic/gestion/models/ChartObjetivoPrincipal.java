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
 * @author grecia
 */
@Entity
@Table(name = "chart_objetivo_principal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ChartObjetivoPrincipal.findAll", query = "SELECT e FROM ChartObjetivoPrincipal e"),
    @NamedQuery(name = "ChartObjetivoPrincipal.findByObjetivoPeriodo", query = "SELECT e FROM ChartObjetivoPrincipal e WHERE e.tipoObjetivo = :tipoObjetivo and e.periodo IN :periodoS"),
    @NamedQuery(name = "ChartObjetivoPrincipal.findByTipoPeriodo", query = "SELECT e FROM ChartObjetivoPrincipal e WHERE e.tipo = :tipo and e.periodo = :periodo"),
    @NamedQuery(name = "ChartObjetivoPrincipal.findByCodigo", query = "SELECT e FROM ChartObjetivoPrincipal e WHERE e.codigo = :codigo")
})
public class ChartObjetivoPrincipal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "codigo")
    private String codigo;
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "tipoObjetivo")
    private String tipoObjetivo;
    @Column(name = "tipo")
    private Integer tipo;
    @Column(name = "variable2")
    private Double variable2;
    @Column(name = "periodo")
    private Integer periodo;
    @Column(name = "valor_variable")
    private Double valorVariable;
    @Column(name = "metas_alcanzada")
    private Double metasAlcanzada;

    public ChartObjetivoPrincipal() {
    }

    public ChartObjetivoPrincipal(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public Double getVariable2() {
        return variable2;
    }

    public void setVariable2(Double variable2) {
        this.variable2 = variable2;
    }

    public String getTipoObjetivo() {
        return tipoObjetivo;
    }

    public void setTipoObjetivo(String tipoObjetivo) {
        this.tipoObjetivo = tipoObjetivo;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Integer getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }

    public Double getValorVariable() {
        return valorVariable;
    }

    public void setValorVariable(Double valorVariable) {
        this.valorVariable = valorVariable;
    }

    public Double getMetasAlcanzada() {
        return metasAlcanzada;
    }

    public void setMetasAlcanzada(Double metasAlcanzada) {
        this.metasAlcanzada = metasAlcanzada;
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
        if (!(object instanceof ChartObjetivoPrincipal)) {
            return false;
        }
        ChartObjetivoPrincipal other = (ChartObjetivoPrincipal) object;
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
