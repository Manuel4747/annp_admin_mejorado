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
import jakarta.persistence.Lob;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eduardo
 */
@Entity
@Table(name = "parametros_sistema")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ParametrosSistema.findAll", query = "SELECT p FROM ParametrosSistema p")
    , @NamedQuery(name = "ParametrosSistema.findById", query = "SELECT p FROM ParametrosSistema p WHERE p.id = :id")})
public class ParametrosSistema implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 100)
    @Column(name = "ip_servidor")
    private String ipServidor;
    @Size(max = 5)
    @Column(name = "puerto_servidor")
    private String puertoServidor;
    @Column(name = "ip_servidor_email")
    private String ipServidorEmail;
    @Size(max = 5)
    @Column(name = "puerto_servidor_email")
    private String puertoServidorEmail;
    @Size(max = 100)
    @Column(name = "usuario_servidor_email")
    private String usuarioServidorEmail;
    @Size(max = 100)
    @Column(name = "contrasena_servidor_email")
    private String contrasenaServidorEmail;
    @Size(max = 200)
    @Column(name = "ruta_recursos")
    private String rutaRecursos;
    @Size(max = 20)
    @Column(name = "layout_menu_por_omision")
    private String layoutMenuPorOmision;
    @Size(max = 20)
    @Column(name = "tema_por_omision")
    private String temaPorOmision;
    @Size(max = 200)
    @Column(name = "ruta_antecedentes")
    private String rutaAntecedentes;
    @Size(max = 200)
    @Column(name = "protocolo")
    private String protocolo;
    @Size(max = 200)
    @Column(name = "ruta_archivos")
    private String rutaArchivos;
    @Size(max = 200)
    @Column(name = "ruta_avisos")
    private String rutaAvisos;
    @Size(max = 200)
    @Column(name = "ruta_fotos_legajo")
    private String rutaFotosLegajo;
    @Size(max = 200)
    @Column(name = "ruta_legajos")
    private String rutaLegajos;
    @Size(max = 200)
    @Column(name = "ruta_solicitudes")
    private String rutaSolicitudes;
    @Size(max = 200)
    @Column(name = "ruta_archivos_administrativo")
    private String rutaArchivosAdministrativo;
    @Column(name = "hora_inicio")
    private Integer horaInicio;
    @Column(name = "hora_fin")
    private Integer horaFin;
    @Column(name = "minuto_inicio")
    private Integer minutoInicio;
    @Column(name = "minuto_fin")
    private Integer minutoFin;
    @Basic(optional = true)
    @Lob
    @Size(max = 65535)
    @Column(name = "formato_actuaciones")
    private String formatoActuaciones;
    @Size(max = 200)
    @Column(name = "keystore")
    private String keystore;
    @Size(max = 200)
    @Column(name = "contrasena_keystore")
    private String contrasenaKeystore;
    @Size(max = 200)
    @Column(name = "ruta_archivos_resoluciones")
    private String rutaArchivosResoluciones;

    public ParametrosSistema() {
    }

    public ParametrosSistema(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getIpServidor() {
        return ipServidor;
    }

    public void setIpServidor(String ipServidor) {
        this.ipServidor = ipServidor;
    }

    public String getPuertoServidor() {
        return puertoServidor;
    }

    public void setPuertoServidor(String puertoServidor) {
        this.puertoServidor = puertoServidor;
    }

    public String getRutaRecursos() {
        return rutaRecursos;
    }

    public void setRutaRecursos(String rutaRecursos) {
        this.rutaRecursos = rutaRecursos;
    }

    public String getLayoutMenuPorOmision() {
        return layoutMenuPorOmision;
    }

    public void setLayoutMenuPorOmision(String layoutMenuPorOmision) {
        this.layoutMenuPorOmision = layoutMenuPorOmision;
    }

    public String getTemaPorOmision() {
        return temaPorOmision;
    }

    public void setTemaPorOmision(String temaPorOmision) {
        this.temaPorOmision = temaPorOmision;
    }

    public String getIpServidorEmail() {
        return ipServidorEmail;
    }

    public void setIpServidorEmail(String ipServidorEmail) {
        this.ipServidorEmail = ipServidorEmail;
    }

    public String getPuertoServidorEmail() {
        return puertoServidorEmail;
    }

    public void setPuertoServidorEmail(String puertoServidorEmail) {
        this.puertoServidorEmail = puertoServidorEmail;
    }

    public String getUsuarioServidorEmail() {
        return usuarioServidorEmail;
    }

    public void setUsuarioServidorEmail(String usuarioServidorEmail) {
        this.usuarioServidorEmail = usuarioServidorEmail;
    }

    public String getContrasenaServidorEmail() {
        return contrasenaServidorEmail;
    }

    public void setContrasenaServidorEmail(String contrasenaServidorEmail) {
        this.contrasenaServidorEmail = contrasenaServidorEmail;
    }

    public String getRutaAntecedentes() {
        return rutaAntecedentes;
    }

    public void setRutaAntecedentes(String rutaAntecedentes) {
        this.rutaAntecedentes = rutaAntecedentes;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public String getRutaArchivos() {
        return rutaArchivos;
    }

    public void setRutaArchivos(String rutaArchivos) {
        this.rutaArchivos = rutaArchivos;
    }

    public Integer getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Integer horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Integer getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Integer horaFin) {
        this.horaFin = horaFin;
    }

    public Integer getMinutoInicio() {
        return minutoInicio;
    }

    public void setMinutoInicio(Integer minutoInicio) {
        this.minutoInicio = minutoInicio;
    }

    public Integer getMinutoFin() {
        return minutoFin;
    }

    public void setMinutoFin(Integer minutoFin) {
        this.minutoFin = minutoFin;
    }

    public String getFormatoActuaciones() {
        return formatoActuaciones;
    }

    public void setFormatoActuaciones(String formatoActuaciones) {
        this.formatoActuaciones = formatoActuaciones;
    }

    public String getRutaFotosLegajo() {
        return rutaFotosLegajo;
    }

    public void setRutaFotosLegajo(String rutaFotosLegajo) {
        this.rutaFotosLegajo = rutaFotosLegajo;
    }

    public String getRutaLegajos() {
        return rutaLegajos;
    }

    public void setRutaLegajos(String rutaLegajos) {
        this.rutaLegajos = rutaLegajos;
    }

    public String getRutaArchivosAdministrativo() {
        return rutaArchivosAdministrativo;
    }

    public void setRutaArchivosAdministrativo(String rutaArchivosAdministrativo) {
        this.rutaArchivosAdministrativo = rutaArchivosAdministrativo;
    }

    public String getKeystore() {
        return keystore;
    }

    public void setKeystore(String keystore) {
        this.keystore = keystore;
    }

    public String getContrasenaKeystore() {
        return contrasenaKeystore;
    }

    public void  setContrasenaKeystore(String contrasenaKeystore) {
        this.contrasenaKeystore = contrasenaKeystore;
    }

    public String getRutaSolicitudes() {
        return rutaSolicitudes;
    }

    public void setRutaSolicitudes(String rutaSolicitudes) {
        this.rutaSolicitudes = rutaSolicitudes;
    }

    public String getRutaAvisos() {
        return rutaAvisos;
    }

    public void setRutaAvisos(String rutaAvisos) {
        this.rutaAvisos = rutaAvisos;
    }

    public String getRutaArchivosResoluciones() {
        return rutaArchivosResoluciones;
    }

    public void setRutaArchivosResoluciones(String rutaArchivosResoluciones) {
        this.rutaArchivosResoluciones = rutaArchivosResoluciones;
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
        if (!(object instanceof ParametrosSistema)) {
            return false;
        }
        ParametrosSistema other = (ParametrosSistema) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.models.ParametrosSistema[ id=" + id + " ]";
    }
    
}
