/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "personas_por_documentos_judiciales")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PersonasPorDocumentosJudiciales.findAll", query = "SELECT r FROM PersonasPorDocumentosJudiciales r")
    , @NamedQuery(name = "PersonasPorDocumentosJudiciales.findByPersona", query = "SELECT r FROM PersonasPorDocumentosJudiciales r WHERE r.personasPorDocumentosJudicialesPK.persona = :persona")
    , @NamedQuery(name = "PersonasPorDocumentosJudiciales.findByPersonaEstado", query = "SELECT r FROM PersonasPorDocumentosJudiciales r WHERE r.personasPorDocumentosJudicialesPK.persona = :persona AND r.estado = :estado")
    , @NamedQuery(name = "PersonasPorDocumentosJudiciales.findByDocumentoJudicialEstado", query = "SELECT r FROM PersonasPorDocumentosJudiciales r WHERE r.personasPorDocumentosJudicialesPK.documentoJudicial = :documentoJudicial AND r.estado = :estado")
    , @NamedQuery(name = "PersonasPorDocumentosJudiciales.findByDocumentoJudicial", query = "SELECT r FROM PersonasPorDocumentosJudiciales r WHERE r.personasPorDocumentosJudicialesPK.documentoJudicial = :documentoJudicial")})
public class PersonasPorDocumentosJudiciales implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PersonasPorDocumentosJudicialesPK personasPorDocumentosJudicialesPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_ultimo_estado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraUltimoEstado;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @JoinColumn(name = "estado", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private Estados estado;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "documento_judicial", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private DocumentosJudiciales documentosJudiciales;
    @JoinColumn(name = "persona", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Personas persona;
    @Column(name = "tipo_expediente_anterior")
    private boolean tipoExpedienteAnterior;

    public PersonasPorDocumentosJudiciales() {
    }

    public PersonasPorDocumentosJudiciales(PersonasPorDocumentosJudicialesPK personasPorDocumentosJudicialesPK) {
        this.personasPorDocumentosJudicialesPK = personasPorDocumentosJudicialesPK;
    }

    public PersonasPorDocumentosJudiciales(PersonasPorDocumentosJudicialesPK personasPorDocumentosJudicialesPK, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.personasPorDocumentosJudicialesPK = personasPorDocumentosJudicialesPK;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public PersonasPorDocumentosJudiciales(int persona, int documentoJudicial) {
        this.personasPorDocumentosJudicialesPK = new PersonasPorDocumentosJudicialesPK(persona, documentoJudicial);
    }

    public PersonasPorDocumentosJudicialesPK getPersonasPorDocumentosJudicialesPK() {
        return personasPorDocumentosJudicialesPK;
    }

    public void setPersonasPorDocumentosJudicialesPK(PersonasPorDocumentosJudicialesPK personasPorDocumentosJudicialesPK) {
        this.personasPorDocumentosJudicialesPK = personasPorDocumentosJudicialesPK;
    }

    public Date getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Date fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public Date getFechaHoraUltimoEstado() {
        return fechaHoraUltimoEstado;
    }

    public void setFechaHoraUltimoEstado(Date fechaHoraUltimoEstado) {
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public Estados getEstado() {
        return estado;
    }

    public void setEstado(Estados estado) {
        this.estado = estado;
    }

    public Usuarios getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(Usuarios usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    public Usuarios getUsuarioUltimoEstado() {
        return usuarioUltimoEstado;
    }

    public void setUsuarioUltimoEstado(Usuarios usuarioUltimoEstado) {
        this.usuarioUltimoEstado = usuarioUltimoEstado;
    }

    public DocumentosJudiciales getDocumentosJudiciales() {
        return documentosJudiciales;
    }

    public void setDocumentosJudiciales(DocumentosJudiciales documentosJudiciales) {
        this.documentosJudiciales = documentosJudiciales;
    }

    public Personas getPersona() {
        return persona;
    }

    public void setPersona(Personas persona) {
        this.persona = persona;
    }

    public boolean isTipoExpedienteAnterior() {
        return tipoExpedienteAnterior;
    }

    public void setTipoExpedienteAnterior(boolean tipoExpedienteAnterior) {
        this.tipoExpedienteAnterior = tipoExpedienteAnterior;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (personasPorDocumentosJudicialesPK != null ? personasPorDocumentosJudicialesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PersonasPorDocumentosJudiciales)) {
            return false;
        }
        PersonasPorDocumentosJudiciales other = (PersonasPorDocumentosJudiciales) object;
        if ((this.personasPorDocumentosJudicialesPK == null && other.personasPorDocumentosJudicialesPK != null) || (this.personasPorDocumentosJudicialesPK != null && !this.personasPorDocumentosJudicialesPK.equals(other.personasPorDocumentosJudicialesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.PersonasPorDocumentosJudiciales[ personasPorDocumentosJudicialesPK=" + personasPorDocumentosJudicialesPK + " ]";
    }
    
}
