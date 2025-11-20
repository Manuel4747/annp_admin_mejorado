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
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
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
@Table(name = "cambios_resolucion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CambiosResolucion.findAll", query = "SELECT r FROM CambiosResolucion r"),
    @NamedQuery(name = "CambiosResolucion.findByResolucion", query = "SELECT r FROM CambiosResolucion r WHERE r.resolucion = :resolucion")
})
public class CambiosResolucion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "resolucion", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Resoluciones resolucion;
    @Basic(optional = true)
    @Column(name = "fecha_anterior")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAnterior;
    @Basic(optional = true)
    @Size(max = 20)
    @Column(name = "nro_resolucion_anterior")
    private String nroResolucionAnterior;
    @Basic(optional = true)
    @Lob
    @Size(max = 65535)
    @Column(name = "caratula_anterior")
    private String caratulaAnterior;
    @Basic(optional = true)
    @Column(name = "resolucion_escaneada_anterior")
    private Integer resolucionEscaneadaAnterior;
    @JoinColumn(name = "tipo_resolucion_anterior", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private TiposResolucion tipoResolucionAnterior;
    @JoinColumn(name = "documento_judicial_anterior", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private DocumentosJudiciales documentoJudicialAnterior;
    @JoinColumn(name = "persona_anterior", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Personas personaAnterior;
    @JoinColumn(name = "resuelve_anterior", referencedColumnName = "codigo")
    @ManyToOne(optional = true)
    private Resuelve resuelveAnterior;
    @Column(name = "firmado_anterior")
    private boolean firmadoAnterior;
    @Basic(optional = true)
    @Column(name = "fecha_actual")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaActual;
    @Basic(optional = true)
    @Size(max = 20)
    @Column(name = "nro_resolucion_actual")
    private String nroResolucionActual;
    @Basic(optional = true)
    @Lob
    @Size(max = 65535)
    @Column(name = "caratula_actual")
    private String caratulaActual;
    @Basic(optional = true)
    @Column(name = "resolucion_escaneada_actual")
    private Integer resolucionEscaneadaActual;
    @JoinColumn(name = "tipo_resolucion_actual", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private TiposResolucion tipoResolucionActual;
    @JoinColumn(name = "documento_judicial_actual", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private DocumentosJudiciales documentoJudicialActual;
    @JoinColumn(name = "persona_actual", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Personas personaActual;
    @JoinColumn(name = "resuelve_actual", referencedColumnName = "codigo")
    @ManyToOne(optional = true)
    private Resuelve resuelveActual;
    @Column(name = "firmado_actual")
    private boolean firmadoActual;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "empresa", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empresas empresa;

    public CambiosResolucion() {
    }

    public CambiosResolucion(Integer id) {
        this.id = id;
    }
    
    public CambiosResolucion(Resoluciones resAnterior, Resoluciones resActual, Usuarios usuarioAlta, Date fechaHoraAlta, Empresas empresa) {
        this.resolucion = resActual;
        this.caratulaAnterior = resAnterior.getCaratula();
        this.nroResolucionAnterior = resAnterior.getNroResolucion();
        this.tipoResolucionAnterior = resAnterior.getTipoResolucion();
        this.fechaAnterior = resAnterior.getFecha();
        this.resolucionEscaneadaAnterior = resAnterior.getResolucionEscaneada();
        this.documentoJudicialAnterior = resAnterior.getDocumentoJudicial();
        this.firmadoAnterior = resAnterior.isFirmado();
        this.caratulaActual = resActual.getCaratula();
        this.nroResolucionActual = resActual.getNroResolucion();
        this.tipoResolucionActual = resActual.getTipoResolucion();
        this.fechaActual = resActual.getFecha();
        this.resolucionEscaneadaActual = resActual.getResolucionEscaneada();
        this.documentoJudicialActual = resActual.getDocumentoJudicial();
        this.firmadoActual = resActual.isFirmado();
        this.usuarioAlta = usuarioAlta;
        this.fechaHoraAlta = fechaHoraAlta;
        this.empresa = empresa;
    }

    public CambiosResolucion(Resoluciones resolucion,
                            String caratulaAnterior, 
                            String nroResolucionAnterior, 
                            TiposResolucion tipoResolucionAnterior,
                            Date fechaAnterior,
                            Personas personaAnterior,
                            DocumentosJudiciales documentoJudicialAnterior,
                            boolean firmadoAnterior,
                            Resuelve resuelveAnterior,
                            String caratulaActual, 
                            String nroResolucionActual, 
                            TiposResolucion tipoResolucionActual,
                            Date fechaActual,
                            Personas personaActual,
                            DocumentosJudiciales documentoJudicialActual,
                            boolean firmadoActual,
                            Resuelve resuelveActual,
                            Usuarios usuarioAlta,
                            Date fechaHoraAlta,
                            Empresas empresa) {
        this.resolucion = resolucion;
        this.caratulaAnterior = caratulaAnterior;
        this.nroResolucionAnterior = nroResolucionAnterior;
        this.tipoResolucionAnterior = tipoResolucionAnterior;
        this.fechaAnterior = fechaAnterior;
        this.personaAnterior = personaAnterior;
        this.documentoJudicialAnterior = documentoJudicialAnterior;
        this.firmadoAnterior = firmadoAnterior;
        this.resuelveAnterior = resuelveAnterior;
        this.caratulaActual = caratulaActual;
        this.nroResolucionActual = nroResolucionActual;
        this.tipoResolucionActual = tipoResolucionActual;
        this.fechaActual = fechaActual;
        this.personaActual = personaActual;
        this.documentoJudicialActual = documentoJudicialActual;
        this.firmadoActual = firmadoActual;
        this.resuelveActual = resuelveActual;
        this.usuarioAlta = usuarioAlta;
        this.fechaHoraAlta = fechaHoraAlta;
        this.empresa = empresa;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Resoluciones getResolucion() {
        return resolucion;
    }

    public void setResolucion(Resoluciones resolucion) {
        this.resolucion = resolucion;
    }

    public Date getFechaAnterior() {
        return fechaAnterior;
    }

    public void setFechaAnterior(Date fechaAnterior) {
        this.fechaAnterior = fechaAnterior;
    }

    public String getNroResolucionAnterior() {
        return nroResolucionAnterior;
    }

    public void setNroResolucionAnterior(String nroResolucionAnterior) {
        this.nroResolucionAnterior = nroResolucionAnterior;
    }

    public String getCaratulaAnterior() {
        return caratulaAnterior;
    }

    public void setCaratulaAnterior(String caratulaAnterior) {
        this.caratulaAnterior = caratulaAnterior;
    }

    public Integer getResolucionEscaneadaAnterior() {
        return resolucionEscaneadaAnterior;
    }

    public void setResolucionEscaneadaAnterior(Integer resolucionEscaneadaAnterior) {
        this.resolucionEscaneadaAnterior = resolucionEscaneadaAnterior;
    }

    public TiposResolucion getTipoResolucionAnterior() {
        return tipoResolucionAnterior;
    }

    public void setTipoResolucionAnterior(TiposResolucion tipoResolucionAnterior) {
        this.tipoResolucionAnterior = tipoResolucionAnterior;
    }

    public DocumentosJudiciales getDocumentoJudicialAnterior() {
        return documentoJudicialAnterior;
    }

    public void setDocumentoJudicialAnterior(DocumentosJudiciales documentoJudicialAnterior) {
        this.documentoJudicialAnterior = documentoJudicialAnterior;
    }

    public Personas getPersonaAnterior() {
        return personaAnterior;
    }

    public void setPersonaAnterior(Personas personaAnterior) {
        this.personaAnterior = personaAnterior;
    }

    public Resuelve getResuelveAnterior() {
        return resuelveAnterior;
    }

    public void setResuelveAnterior(Resuelve resuelveAnterior) {
        this.resuelveAnterior = resuelveAnterior;
    }

    public boolean isFirmadoAnterior() {
        return firmadoAnterior;
    }

    public void setFirmadoAnterior(boolean firmadoAnterior) {
        this.firmadoAnterior = firmadoAnterior;
    }

    public Date getFechaActual() {
        return fechaActual;
    }

    public void setFechaActual(Date fechaActual) {
        this.fechaActual = fechaActual;
    }

    public String getNroResolucionActual() {
        return nroResolucionActual;
    }

    public void setNroResolucionActual(String nroResolucionActual) {
        this.nroResolucionActual = nroResolucionActual;
    }

    public String getCaratulaActual() {
        return caratulaActual;
    }

    public void setCaratulaActual(String caratulaActual) {
        this.caratulaActual = caratulaActual;
    }

    public Integer getResolucionEscaneadaActual() {
        return resolucionEscaneadaActual;
    }

    public void setResolucionEscaneadaActual(Integer resolucionEscaneadaActual) {
        this.resolucionEscaneadaActual = resolucionEscaneadaActual;
    }

    public TiposResolucion getTipoResolucionActual() {
        return tipoResolucionActual;
    }

    public void setTipoResolucionActual(TiposResolucion tipoResolucionActual) {
        this.tipoResolucionActual = tipoResolucionActual;
    }

    public DocumentosJudiciales getDocumentoJudicialActual() {
        return documentoJudicialActual;
    }

    public void setDocumentoJudicialActual(DocumentosJudiciales documentoJudicialActual) {
        this.documentoJudicialActual = documentoJudicialActual;
    }

    public Personas getPersonaActual() {
        return personaActual;
    }

    public void setPersonaActual(Personas personaActual) {
        this.personaActual = personaActual;
    }

    public Resuelve getResuelveActual() {
        return resuelveActual;
    }

    public void setResuelveActual(Resuelve resuelveActual) {
        this.resuelveActual = resuelveActual;
    }

    public boolean isFirmadoActual() {
        return firmadoActual;
    }

    public void setFirmadoActual(boolean firmadoActual) {
        this.firmadoActual = firmadoActual;
    }

    public Date getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Date fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public Usuarios getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(Usuarios usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CambiosResolucion)) {
            return false;
        }
        CambiosResolucion other = (CambiosResolucion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.CambiosResolucion[ id=" + id + " ]";
    }
    
}
