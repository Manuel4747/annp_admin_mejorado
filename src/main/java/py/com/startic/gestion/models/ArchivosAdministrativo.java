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
@Table(name = "archivos_administrativo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ArchivosAdministrativo.findAll", query = "SELECT t FROM ArchivosAdministrativo t")
    , @NamedQuery(name = "ArchivosAdministrativo.findById", query = "SELECT t FROM ArchivosAdministrativo t WHERE t.id = :id")
    , @NamedQuery(name = "ArchivosAdministrativo.findByDocumentoAdministrativoANDTiposArchivo", query = "SELECT t FROM ArchivosAdministrativo t WHERE t.documentoAdministrativo = :documentoAdministrativo AND t.tipoArchivo IN :tiposArchivo ORDER BY t.fechaHoraAlta DESC")
    , @NamedQuery(name = "ArchivosAdministrativo.findByDocumentoAdministrativoOrdered", query = "SELECT t FROM ArchivosAdministrativo t WHERE t.documentoAdministrativo = :documentoAdministrativo ORDER BY t.fechaHoraAlta")
    , @NamedQuery(name = "ArchivosAdministrativo.findByFechaHoraAlta", query = "SELECT t FROM ArchivosAdministrativo t WHERE t.fechaHoraAlta = :fechaHoraAlta")
    , @NamedQuery(name = "ArchivosAdministrativo.findByFechaHoraUltimoEstado", query = "SELECT t FROM ArchivosAdministrativo t WHERE t.fechaHoraUltimoEstado = :fechaHoraUltimoEstado")})
public class ArchivosAdministrativo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "ruta")
    private String ruta;
    @NotNull
    @Lob
    @Size(max = 65535)
    @Column(name = "descripcion")
    private String descripcion;
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
    @JoinColumn(name = "usuario_alta", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioAlta;
    @JoinColumn(name = "usuario_ultimo_estado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Usuarios usuarioUltimoEstado;
    @JoinColumn(name = "documento_administrativo", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private DocumentosAdministrativos documentoAdministrativo;
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
    @Lob
    @Size(max = 65535)
    @Column(name = "texto")
    private String texto;
    @JoinColumn(name = "tipo_archivo", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private TiposArchivoAdministrativo tipoArchivo;
    @JoinColumn(name = "formato", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private FormatosArchivoAdministrativo formato;
    @JoinColumn(name = "usuario_borrado", referencedColumnName = "id")
    @ManyToOne(optional = true)
    private Usuarios usuarioBorrado;
    @Basic(optional = true)
    @Column(name = "fecha_hora_borrado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraBorrado;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "codigo_archivo")
    private String codigoArchivo;

    public ArchivosAdministrativo() {
    }

    public ArchivosAdministrativo(Integer id) {
        this.id = id;
    }

    public ArchivosAdministrativo(Integer id, Date fechaHoraAlta, Date fechaHoraUltimoEstado) {
        this.id = id;
        this.fechaHoraAlta = fechaHoraAlta;
        this.fechaHoraUltimoEstado = fechaHoraUltimoEstado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public DocumentosAdministrativos getDocumentoAdministrativo() {
        return documentoAdministrativo;
    }

    public void setDocumentoAdministrativo(DocumentosAdministrativos documentoAdministrativo) {
        this.documentoAdministrativo = documentoAdministrativo;
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

    public String getNomenclaturaFinal() {
        return nomenclaturaFinal;
    }

    public void setNomenclaturaFinal(String nomenclaturaFinal) {
        this.nomenclaturaFinal = nomenclaturaFinal;
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

    public TiposArchivoAdministrativo getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(TiposArchivoAdministrativo tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public FormatosArchivoAdministrativo getFormato() {
        return formato;
    }

    public void setFormato(FormatosArchivoAdministrativo formato) {
        this.formato = formato;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Usuarios getUsuarioBorrado() {
        return usuarioBorrado;
    }

    public void setUsuarioBorrado(Usuarios usuarioBorrado) {
        this.usuarioBorrado = usuarioBorrado;
    }

    public Date getFechaHoraBorrado() {
        return fechaHoraBorrado;
    }

    public void setFechaHoraBorrado(Date fechaHoraBorrado) {
        this.fechaHoraBorrado = fechaHoraBorrado;
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
        if (!(object instanceof ArchivosAdministrativo)) {
            return false;
        }
        ArchivosAdministrativo other = (ArchivosAdministrativo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return (ruta!=null)?ruta:"";
    }
    
}
