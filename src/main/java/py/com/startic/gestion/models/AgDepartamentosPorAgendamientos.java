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
@Table(name = "ag_departamentos_por_agendamientos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AgDepartamentosPorAgendamientos.findAll", query = "SELECT t FROM AgDepartamentosPorAgendamientos t")
    , @NamedQuery(name = "AgDepartamentosPorAgendamientos.findById", query = "SELECT t FROM AgDepartamentosPorAgendamientos t WHERE t.id = :id")
    , @NamedQuery(name = "AgDepartamentosPorAgendamientos.findByAgendamiento", query = "SELECT t FROM AgDepartamentosPorAgendamientos t WHERE t.agendamiento = :agendamiento ORDER BY t.id")})
public class AgDepartamentosPorAgendamientos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "departamento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Departamentos departamento;
    @JoinColumn(name = "agendamiento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AgAgendamientos agendamiento;
    @JoinColumn(name = "departamento_por_agendamiento_padre", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AgDepartamentosPorAgendamientos departamentoPorAgendamientoPadre;

    public AgDepartamentosPorAgendamientos(Departamentos departamento, AgAgendamientos agendamiento, AgDepartamentosPorAgendamientos departamentoPorAgendamientoPadre) {
        this.departamento = departamento;
        this.agendamiento = agendamiento;
        this.departamentoPorAgendamientoPadre = departamentoPorAgendamientoPadre;
    }

    public AgDepartamentosPorAgendamientos() {
    }

    public AgDepartamentosPorAgendamientos(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Departamentos getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamentos departamento) {
        this.departamento = departamento;
    }

    public AgAgendamientos getAgendamiento() {
        return agendamiento;
    }

    public void setAgendamiento(AgAgendamientos agendamiento) {
        this.agendamiento = agendamiento;
    }

    public AgDepartamentosPorAgendamientos getDepartamentoPorAgendamientoPadre() {
        return departamentoPorAgendamientoPadre;
    }

    public void setDepartamentoPorAgendamientoPadre(AgDepartamentosPorAgendamientos departamentoPorAgendamientoPadre) {
        this.departamentoPorAgendamientoPadre = departamentoPorAgendamientoPadre;
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
        if (!(object instanceof AgDepartamentosPorAgendamientos)) {
            return false;
        }
        AgDepartamentosPorAgendamientos other = (AgDepartamentosPorAgendamientos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "";
    }
    
}
