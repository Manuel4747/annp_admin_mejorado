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
import jakarta.persistence.Lob;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "avisos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Avisos.findAll", query = "SELECT r FROM Avisos r")
    , @NamedQuery(name = "Avisos.findById", query = "SELECT r FROM Avisos r WHERE r.id = :id")
    , @NamedQuery(name = "Avisos.findByUsuario", query = "SELECT u FROM Avisos u WHERE u.fechaHoraDesde <= :fechaHoraDesde and u.fechaHoraHasta >= :fechaHoraHasta AND u.id in (SELECT r.avisosPorUsuariosPK.aviso FROM AvisosPorUsuarios r WHERE r.avisosPorUsuariosPK.usuario = :usuario)")
    , @NamedQuery(name = "Avisos.findByRangoFecha", query = "SELECT u FROM Avisos u WHERE u.fechaHoraDesde <= :fechaHoraDesde and u.fechaHoraHasta >= :fechaHoraHasta")})
public class Avisos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "url")
    private String url;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "url_texto")
    private String urlTexto;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "video")
    private String video;
    @Basic(optional = true)
    @Size(max = 200)
    @Column(name = "imagen")
    private String imagen;
    @Size(max = 200)
    @Column(name = "pdf")
    private String pdf;
    @Lob
    @Size(max = 65535)
    @Column(name = "texto")
    private String texto;
    @Basic(optional = true)
    @Column(name = "fecha_hora_alta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraAlta;
    @Basic(optional = true)
    @Column(name = "fecha_hora_desde")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraDesde;
    @Basic(optional = true)
    @Column(name = "fecha_hora_hasta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHoraHasta;
    
    public Avisos() {
    }

    public String getUrlTexto() {
        return urlTexto;
    }

    public void setUrlTexto(String urlTexto) {
        this.urlTexto = urlTexto;
    }

    public Avisos(Integer id) {
        this.id = id;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Date getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(Date fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public Date getFechaHoraDesde() {
        return fechaHoraDesde;
    }

    public void setFechaHoraDesde(Date fechaHoraDesde) {
        this.fechaHoraDesde = fechaHoraDesde;
    }

    public Date getFechaHoraHasta() {
        return fechaHoraHasta;
    }

    public void setFechaHoraHasta(Date fechaHoraHasta) {
        this.fechaHoraHasta = fechaHoraHasta;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
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
        if (!(object instanceof Avisos)) {
            return false;
        }
        Avisos other = (Avisos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.Avisos[ id=" + id + " ]";
    }
    
}
