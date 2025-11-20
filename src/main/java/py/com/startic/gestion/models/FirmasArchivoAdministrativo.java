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
@Table(name = "firmas_archivo_administrativo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FirmasArchivoAdministrativo.findAll", query = "SELECT r FROM FirmasArchivoAdministrativo r")
    , @NamedQuery(name = "FirmasArchivoAdministrativo.findById", query = "SELECT r FROM FirmasArchivoAdministrativo r WHERE r.id = :id")
    , @NamedQuery(name = "FirmasArchivoAdministrativo.findByArchivoAdministrativo", query = "SELECT r FROM FirmasArchivoAdministrativo r WHERE r.archivoAdministrativo = :archivoAdministrativo")
    , @NamedQuery(name = "FirmasArchivoAdministrativo.findBySesion", query = "SELECT r FROM FirmasArchivoAdministrativo r WHERE r.sesion = :sesion and r.estado = :estado and r.fechaHora > :fechaHora")})
public class FirmasArchivoAdministrativo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "documento_administrativo", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private DocumentosAdministrativos documentoAdministrativo;
    @JoinColumn(name = "archivo_administrativo", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private ArchivosAdministrativo archivoAdministrativo;
    @Column(name = "sesion")
    private String sesion;
    @Column(name = "estado")
    private String estado;
    @Size(max = 200)
    @Column(name = "ruta")
    private String ruta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHora;
    @JoinColumn(name = "usuario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuario;
    @Basic(optional = true)
    @Column(name = "fecha_hora_ultimo_estado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraUltimoEstado;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "destinatario", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios destinatario;
    @Basic(optional = true)
    @Size(max = 20)
    @Column(name = "nro_final")
    private String nroFinal;
    @Basic(optional = true)
    @Size(max = 20)
    @Column(name = "nomenclatura_final")
    private String nomenclaturaFinal;
    @Column(name = "fecha_final")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFinal;
    @Lob
    @Size(max = 65535)
    @Column(name = "texto_final")
    private String textoFinal;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "codigo_archivo")
    private String codigoArchivo;

    public FirmasArchivoAdministrativo() {
    }

    public FirmasArchivoAdministrativo(Integer id) {
        this.id = id;
    }

    public FirmasArchivoAdministrativo(Integer id, DocumentosAdministrativos documentoAdministrativo) {
        this.id = id;
        this.documentoAdministrativo = documentoAdministrativo;
    }

    public String getSesion() {
        return sesion;
    }

    public void setSesion(String sesion) {
        this.sesion = sesion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public DocumentosAdministrativos getDocumentoAdministrativo() {
        return documentoAdministrativo;
    }

    public void setDocumentoAdministrativo(DocumentosAdministrativos documentoAdministrativo) {
        this.documentoAdministrativo = documentoAdministrativo;
    }

    public ArchivosAdministrativo getArchivoAdministrativo() {
        return archivoAdministrativo;
    }

    public void setArchivoAdministrativo(ArchivosAdministrativo archivoAdministrativo) {
        this.archivoAdministrativo = archivoAdministrativo;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public Date getFechaHoraUltimoEstado() {
        return fechaHoraUltimoEstado;
    }

    public void setFechaHoraUltimoEstado(Date fechaHoraUltimoEstado) {
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Usuarios getUsuarioUltimoEstado() {
        return usuarioUltimoEstado;
    }

    public void setUsuarioUltimoEstado(Usuarios usuarioUltimoEstado) {
        this.usuarioUltimoEstado = usuarioUltimoEstado;
    }

    public Usuarios getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Usuarios destinatario) {
        this.destinatario = destinatario;
    }

    public String getNomenclaturaFinal() {
        return nomenclaturaFinal;
    }

    public void setNomenclaturaFinal(String nomenclaturaFinal) {
        this.nomenclaturaFinal = nomenclaturaFinal;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getNroFinal() {
        return nroFinal;
    }

    public void setNroFinal(String nroFinal) {
        this.nroFinal = nroFinal;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public String getTextoFinal() {
        return textoFinal;
    }

    public void setTextoFinal(String textoFinal) {
        this.textoFinal = textoFinal;
    }

    public String getCodigoArchivo() {
        return codigoArchivo;
    }

    public void setCodigoArchivo(String codigoArchivo) {
        this.codigoArchivo = codigoArchivo;
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
        if (!(object instanceof FirmasArchivoAdministrativo)) {
            return false;
        }
        FirmasArchivoAdministrativo other = (FirmasArchivoAdministrativo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.FirmasArchivoAdministrativo[ id=" + id + " ]";
    }
    
}
