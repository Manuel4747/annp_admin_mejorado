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
 * @author grecia
 */
@Entity
@Table(name = "destinatarios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Destinatarios.findAll", query = "SELECT d FROM Destinatarios d"),
    @NamedQuery(name = "Destinatarios.findById", query = "SELECT d FROM Destinatarios d WHERE d.id = :id"),
    @NamedQuery(name = "Destinatarios.findOrdered", query = "SELECT d FROM Destinatarios d WHERE d.responsable = :responsable  ORDER BY d.id DESC"),
    @NamedQuery(name = "Destinatarios.findByDetallesResolucion", query = "SELECT d FROM Destinatarios d WHERE d.documentosResoluciones = :documentosResoluciones"),
    @NamedQuery(name = "Destinatarios.findByDocumentoResolucion", query = "SELECT d FROM Destinatarios d WHERE d.documentosResoluciones = :documentosResoluciones ORDER BY d.id"),
    @NamedQuery(name = "Destinatarios.findByArchivo", query = "SELECT d FROM Destinatarios d WHERE d.fechaHoraAlta = :fechaHoraAlta")})

public class Destinatarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
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
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "departamento", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Departamentos departamento;
    @JoinColumn(name = "responsable", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios responsable;
    @JoinColumn(name = "documento_resolucion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DocumentosResoluciones documentosResoluciones;
    @JoinColumn(name = "tipo_envio", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TiposEnvio tipoEnvio;
    @JoinColumn(name = "estado", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private EstadosDocumento estado;
    @JoinColumn(name = "tipo_documento", referencedColumnName = "codigo")
    @ManyToOne(optional = false)
    private TiposDocumentosJudiciales tipoDocumento;
    @JoinColumn(name = "responsable_anterior", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios responsableAnterior;

    public Destinatarios() {
    }

    public Destinatarios(Integer id) {
        this.id = id;
    }

    public Destinatarios(Integer id, Date fechaHoraAlta) {
        this.id = id;
        this.fechaHoraAlta = fechaHoraAlta;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Departamentos getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamentos departamento) {
        this.departamento = departamento;
    }

    public Usuarios getResponsable() {
        return responsable;
    }

    public void setResponsable(Usuarios responsable) {
        this.responsable = responsable;
    }

    public DocumentosResoluciones getDocumentosResoluciones() {
        return documentosResoluciones;
    }

    public void setDocumentosResoluciones(DocumentosResoluciones documentosResoluciones) {
        this.documentosResoluciones = documentosResoluciones;
    }

   

    public TiposEnvio getTipoEnvio() {
        return tipoEnvio;
    }

    public void setTipoEnvio(TiposEnvio tipoEnvio) {
        this.tipoEnvio = tipoEnvio;
    }

    public EstadosDocumento getEstado() {
        return estado;
    }

    public void setEstado(EstadosDocumento estado) {
        this.estado = estado;
    }

    public TiposDocumentosJudiciales getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TiposDocumentosJudiciales tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Usuarios getResponsableAnterior() {
        return responsableAnterior;
    }

    public void setResponsableAnterior(Usuarios responsableAnterior) {
        this.responsableAnterior = responsableAnterior;
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
        if (!(object instanceof Destinatarios)) {
            return false;
        }
        Destinatarios other = (Destinatarios) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.Destinatarios[ id=" + id + " ]";
    }

}
