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
@Table(name = "ag_detalles_agendamiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AgDetallesAgendamiento.findAll", query = "SELECT t FROM AgDetallesAgendamiento t")
    , @NamedQuery(name = "AgDetallesAgendamiento.findByAgendamiento", query = "SELECT r FROM AgDetallesAgendamiento r WHERE r.agendamiento = :agendamiento ORDER BY r.persona.nombresApellidos")
    , @NamedQuery(name = "AgDetallesAgendamiento.findById", query = "SELECT t FROM AgDetallesAgendamiento t WHERE t.id = :id")})
public class AgDetallesAgendamiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "agendamiento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AgAgendamientos agendamiento;
    @JoinColumn(name = "persona", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private PersonasAgendamiento persona;

    public AgDetallesAgendamiento() {
    }

    public AgDetallesAgendamiento(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AgAgendamientos getAgendamiento() {
        return agendamiento;
    }

    public void setAgendamiento(AgAgendamientos agendamiento) {
        this.agendamiento = agendamiento;
    }

    public PersonasAgendamiento getPersona() {
        return persona;
    }

    public void setPersona(PersonasAgendamiento persona) {
        this.persona = persona;
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
        if (!(object instanceof AgDetallesAgendamiento)) {
            return false;
        }
        AgDetallesAgendamiento other = (AgDetallesAgendamiento) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.AgDetallesAgendamiento[ id=" + id + " ]";
    }
    
}
