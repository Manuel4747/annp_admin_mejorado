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


@Entity
@Table(name = "formulario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Formulario.findAll", query = "SELECT f FROM Formulario f")
    , @NamedQuery(name = "Formulario.findById", query = "SELECT f FROM Formulario f WHERE f.id = :id")
    , @NamedQuery(name = "Formulario.findByDenominacion", query = "SELECT f FROM Formulario f WHERE f.denominacion = :denominacion")
    , @NamedQuery(name = "Formulario.findByOjetivo", query = "SELECT f FROM Formulario f WHERE f.ojetivo = :ojetivo")
    , @NamedQuery(name = "Formulario.findByCaracterSolicitud", query = "SELECT f FROM Formulario f WHERE f.caracterSolicitud = :caracterSolicitud")
    , @NamedQuery(name = "Formulario.findByVigenciaDesde", query = "SELECT f FROM Formulario f WHERE f.vigenciaDesde = :vigenciaDesde")
    , @NamedQuery(name = "Formulario.findByVigenciaHasta", query = "SELECT f FROM Formulario f WHERE f.vigenciaHasta = :vigenciaHasta")
    , @NamedQuery(name = "Formulario.findByLlamadoSbe", query = "SELECT f FROM Formulario f WHERE f.llamadoSbe = :llamadoSbe")
    , @NamedQuery(name = "Formulario.findByValorTotal", query = "SELECT f FROM Formulario f WHERE f.valorTotal = :valorTotal")
    , @NamedQuery(name = "Formulario.findByMontoTotal", query = "SELECT f FROM Formulario f WHERE f.montoTotal = :montoTotal")
    , @NamedQuery(name = "Formulario.findByMontoAnho1", query = "SELECT f FROM Formulario f WHERE f.montoAnho1 = :montoAnho1")
    , @NamedQuery(name = "Formulario.findByMontoAnho2", query = "SELECT f FROM Formulario f WHERE f.montoAnho2 = :montoAnho2")
    , @NamedQuery(name = "Formulario.findByMesLlamado", query = "SELECT f FROM Formulario f WHERE f.mesLlamado = :mesLlamado")
    , @NamedQuery(name = "Formulario.findByMesSusContrato", query = "SELECT f FROM Formulario f WHERE f.mesSusContrato = :mesSusContrato")
    , @NamedQuery(name = "Formulario.findByContratroExcepcion", query = "SELECT f FROM Formulario f WHERE f.contratroExcepcion = :contratroExcepcion")
    , @NamedQuery(name = "Formulario.findByJustContExcep", query = "SELECT f FROM Formulario f WHERE f.justContExcep = :justContExcep")
    , @NamedQuery(name = "Formulario.findByContratoAbiero", query = "SELECT f FROM Formulario f WHERE f.contratoAbiero = :contratoAbiero")
    , @NamedQuery(name = "Formulario.findByTipoContratoAbierto", query = "SELECT f FROM Formulario f WHERE f.tipoContratoAbierto = :tipoContratoAbierto")
    , @NamedQuery(name = "Formulario.findByTipoContratoMonto", query = "SELECT f FROM Formulario f WHERE f.tipoContratoMonto = :tipoContratoMonto")
    , @NamedQuery(name = "Formulario.findByIdioma", query = "SELECT f FROM Formulario f WHERE f.idioma = :idioma")
    , @NamedQuery(name = "Formulario.findByFechaVt", query = "SELECT f FROM Formulario f WHERE f.fechaVt = :fechaVt")
    , @NamedQuery(name = "Formulario.findByLugarVt", query = "SELECT f FROM Formulario f WHERE f.lugarVt = :lugarVt")
    , @NamedQuery(name = "Formulario.findByHoraVt", query = "SELECT f FROM Formulario f WHERE f.horaVt = :horaVt")
    , @NamedQuery(name = "Formulario.findByProcedimientoVt", query = "SELECT f FROM Formulario f WHERE f.procedimientoVt = :procedimientoVt")
    , @NamedQuery(name = "Formulario.findByNombreFunVt", query = "SELECT f FROM Formulario f WHERE f.nombreFunVt = :nombreFunVt")
    , @NamedQuery(name = "Formulario.findByParticipacionVt", query = "SELECT f FROM Formulario f WHERE f.participacionVt = :participacionVt")
    , @NamedQuery(name = "Formulario.findByAutorizacionFabricante", query = "SELECT f FROM Formulario f WHERE f.autorizacionFabricante = :autorizacionFabricante")
    , @NamedQuery(name = "Formulario.findByMuestra", query = "SELECT f FROM Formulario f WHERE f.muestra = :muestra")
    , @NamedQuery(name = "Formulario.findByNombreSolicitante", query = "SELECT f FROM Formulario f WHERE f.nombreSolicitante = :nombreSolicitante")
    , @NamedQuery(name = "Formulario.findByDependenciaSolicitante", query = "SELECT f FROM Formulario f WHERE f.dependenciaSolicitante = :dependenciaSolicitante")
    , @NamedQuery(name = "Formulario.findByCargoSolicitante", query = "SELECT f FROM Formulario f WHERE f.cargoSolicitante = :cargoSolicitante")
    , @NamedQuery(name = "Formulario.findByDescripcionBien", query = "SELECT f FROM Formulario f WHERE f.descripcionBien = :descripcionBien")
    , @NamedQuery(name = "Formulario.findByUnidadMedida", query = "SELECT f FROM Formulario f WHERE f.unidadMedida = :unidadMedida")
    , @NamedQuery(name = "Formulario.findByLugarSede", query = "SELECT f FROM Formulario f WHERE f.lugarSede = :lugarSede")
    , @NamedQuery(name = "Formulario.findByFechaEntrega", query = "SELECT f FROM Formulario f WHERE f.fechaEntrega = :fechaEntrega")
    , @NamedQuery(name = "Formulario.findByMultaSerAplicar", query = "SELECT f FROM Formulario f WHERE f.multaSerAplicar = :multaSerAplicar")
    , @NamedQuery(name = "Formulario.findByAnticipo", query = "SELECT f FROM Formulario f WHERE f.anticipo = :anticipo")
    , @NamedQuery(name = "Formulario.findByTotalAdjAnho", query = "SELECT f FROM Formulario f WHERE f.totalAdjAnho = :totalAdjAnho")
    , @NamedQuery(name = "Formulario.findByTotales", query = "SELECT f FROM Formulario f WHERE f.totales = :totales")
    , @NamedQuery(name = "Formulario.findByPromedUtilizA\u00f1o", query = "SELECT f FROM Formulario f WHERE f.promedUtilizA\u00f1o = :promedUtilizA\u00f1o")
    , @NamedQuery(name = "Formulario.findByElaboradoFuncionario", query = "SELECT f FROM Formulario f WHERE f.elaboradoFuncionario = :elaboradoFuncionario")
    , @NamedQuery(name = "Formulario.findByAprobadoFuncionario", query = "SELECT f FROM Formulario f WHERE f.aprobadoFuncionario = :aprobadoFuncionario")})
public class Formulario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "denominacion")
    private String denominacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ojetivo")
    private String ojetivo;
    @Size(max = 10)
    @Column(name = "caracter_solicitud")
    private String caracterSolicitud;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vigencia_desde")
    @Temporal(TemporalType.DATE)
    private Date vigenciaDesde;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vigencia_hasta")
    @Temporal(TemporalType.DATE)
    private Date vigenciaHasta;
    @Size(max = 3)
    @Column(name = "llamado_sbe")
    private String llamadoSbe;
    @Column(name = "valor_total")
    private Long valorTotal;
    @Column(name = "monto_total")
    private Long montoTotal;
    @Column(name = "monto_anho1")
    private Long montoAnho1;
    @Column(name = "monto_anho2")
    private Long montoAnho2;
    @Size(max = 255)
    @Column(name = "mes_llamado")
    private String mesLlamado;
    @Size(max = 255)
    @Column(name = "mes_sus_contrato")
    private String mesSusContrato;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "contratro_excepcion")
    private String contratroExcepcion;
    @Size(max = 255)
    @Column(name = "just_cont_excep")
    private String justContExcep;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "just_sist_adju")
    private String justSistAdju;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "contrato_abiero")
    private String contratoAbiero;
    @Size(max = 28)
    @Column(name = "tipo_contrato_abierto")
    private String tipoContratoAbierto;
    @Column(name = "tipo_contrato_monto")
    private Long tipoContratoMonto;
    @Lob
    @Size(max = 65535)
    @Column(name = "fundamento_cont_abierto")
    private String fundamentoContAbierto;
    @Size(max = 11)
    @Column(name = "idioma")
    private String idioma;
    @Column(name = "fecha_vt")
    @Temporal(TemporalType.DATE)
    private Date fechaVt;
    @Size(max = 255)
    @Column(name = "lugar_vt")
    private String lugarVt;
    @Column(name = "hora_vt")
    @Temporal(TemporalType.TIME)
    private Date horaVt;
    @Size(max = 255)
    @Column(name = "procedimiento_vt")
    private String procedimientoVt;
    @Size(max = 255)
    @Column(name = "nombre_fun_vt")
    private String nombreFunVt;
    @Size(max = 3)
    @Column(name = "participacion_vt")
    private String participacionVt;
    @Size(max = 3)
    @Column(name = "autorizacion_fabricante")
    private String autorizacionFabricante;
    @Lob
    @Size(max = 65535)
    @Column(name = "justificacion_solicitud")
    private String justificacionSolicitud;
    @Size(max = 3)
    @Column(name = "muestra")
    private String muestra;
    @Lob
    @Size(max = 65535)
    @Column(name = "reque_presentacion")
    private String requePresentacion;
    @Lob
    @Size(max = 65535)
    @Column(name = "garantia_bien_servicio")
    private String garantiaBienServicio;
    @Lob
    @Size(max = 65535)
    @Column(name = "plazo_repar_reempl")
    private String plazoReparReempl;
    @Lob
    @Size(max = 65535)
    @Column(name = "composicion_precio")
    private String composicionPrecio;
    @Lob
    @Size(max = 65535)
    @Column(name = "experiencia_solicitada")
    private String experienciaSolicitada;
    @Lob
    @Size(max = 65535)
    @Column(name = "requisito_experiencia")
    private String requisitoExperiencia;
    @Lob
    @Size(max = 65535)
    @Column(name = "capacidad_tecnica")
    private String capacidadTecnica;
    @Lob
    @Size(max = 65535)
    @Column(name = "requisitos_documentales")
    private String requisitosDocumentales;
    @Size(max = 255)
    @Column(name = "nombre_solicitante")
    private String nombreSolicitante;
    @Size(max = 255)
    @Column(name = "dependencia_solicitante")
    private String dependenciaSolicitante;
    @Size(max = 255)
    @Column(name = "cargo_solicitante")
    private String cargoSolicitante;
    @Lob
    @Size(max = 65535)
    @Column(name = "justificion_necesidad")
    private String justificionNecesidad;
    @Lob
    @Size(max = 65535)
    @Column(name = "justificar_planificacion")
    private String justificarPlanificacion;
    @Lob
    @Size(max = 65535)
    @Column(name = "justificar_espe_tecn")
    private String justificarEspeTecn;
    @Lob
    @Size(max = 65535)
    @Column(name = "especificaciones_tecnicas")
    private String especificacionesTecnicas;
    @Size(max = 255)
    @Column(name = "descripcion_bien")
    private String descripcionBien;
    @Size(max = 255)
    @Column(name = "unidad_medida")
    private String unidadMedida;
    @Size(max = 255)
    @Column(name = "lugar_sede")
    private String lugarSede;
    @Column(name = "fecha_entrega")
    @Temporal(TemporalType.DATE)
    private Date fechaEntrega;
    @Lob
    @Size(max = 65535)
    @Column(name = "indicador_cumplimiento")
    private String indicadorCumplimiento;
    @Lob
    @Size(max = 65535)
    @Column(name = "planos_disenho")
    private String planosDisenho;
    @Lob
    @Size(max = 65535)
    @Column(name = "inspeccion_prueba")
    private String inspeccionPrueba;
    
    @Column(name = "multa_ser_aplicar")
     @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    private String multaSerAplicar;
    
@Column(name = "anticipo")
     @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    private String anticipo;

    @Column(name = "dictamen_tecnico")
    private String dictamenTecnico;
    @Lob
    @Size(max = 65535)
    @Column(name = "reporte_porcentaje")
    private String reportePorcentaje;
    @Column(name = "total_adj_anho")
    private Long totalAdjAnho;
    @Lob
    @Size(max = 250)
    @Column(name = "promedio_total_adjud")
    private String promedioTotalAdjud;
    @Column(name = "totales")
    private Long totales;
    @Size(max = 250)
    @Column(name = "promed_utiliz_a\u00f1o")
    private String promedUtilizAño;
    @Size(max = 255)
    @Column(name = "elaborado_funcionario")
    private String elaboradoFuncionario;
    @Size(max = 255)
    @Column(name = "aprobado_funcionario")
    private String aprobadoFuncionario;
    @JoinColumn(name = "historico_contrato", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private HistoricoContrato historicoContrato;
    @JoinColumn(name = "historico_ejecucion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private HistoricoEjecucion historicoEjecucion;
    @JoinColumn(name = "sistema_adjudicacion", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SistemaAdjudicacion sistemaAdjudicacion;
    @JoinColumn(name = "tipo_modalidad", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TipoModalidad tipoModalidad;

    public Formulario() {
    }

    public Formulario(Integer id) {
        this.id = id;
    }

    public Formulario(Integer id, String denominacion, String ojetivo, Date vigenciaDesde, Date vigenciaHasta, String contratroExcepcion, String justSistAdju, String contratoAbiero) {
        this.id = id;
        this.denominacion = denominacion;
        this.ojetivo = ojetivo;
        this.vigenciaDesde = vigenciaDesde;
        this.vigenciaHasta = vigenciaHasta;
        this.contratroExcepcion = contratroExcepcion;
        this.justSistAdju = justSistAdju;
        this.contratoAbiero = contratoAbiero;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public String getOjetivo() {
        return ojetivo;
    }

    public void setOjetivo(String ojetivo) {
        this.ojetivo = ojetivo;
    }

    public String getCaracterSolicitud() {
        return caracterSolicitud;
    }

    public void setCaracterSolicitud(String caracterSolicitud) {
        this.caracterSolicitud = caracterSolicitud;
    }

    public Date getVigenciaDesde() {
        return vigenciaDesde;
    }

    public void setVigenciaDesde(Date vigenciaDesde) {
        this.vigenciaDesde = vigenciaDesde;
    }

    public Date getVigenciaHasta() {
        return vigenciaHasta;
    }

    public void setVigenciaHasta(Date vigenciaHasta) {
        this.vigenciaHasta = vigenciaHasta;
    }

    public String getLlamadoSbe() {
        return llamadoSbe;
    }

    public void setLlamadoSbe(String llamadoSbe) {
        this.llamadoSbe = llamadoSbe;
    }

    public Long getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Long valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Long getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(Long montoTotal) {
        this.montoTotal = montoTotal;
    }

    public Long getMontoAnho1() {
        return montoAnho1;
    }

    public void setMontoAnho1(Long montoAnho1) {
        this.montoAnho1 = montoAnho1;
    }

    public Long getMontoAnho2() {
        return montoAnho2;
    }

    public void setMontoAnho2(Long montoAnho2) {
        this.montoAnho2 = montoAnho2;
    }

    public String getMesLlamado() {
        return mesLlamado;
    }

    public void setMesLlamado(String mesLlamado) {
        this.mesLlamado = mesLlamado;
    }

    public String getMesSusContrato() {
        return mesSusContrato;
    }

    public void setMesSusContrato(String mesSusContrato) {
        this.mesSusContrato = mesSusContrato;
    }

    public String getContratroExcepcion() {
        return contratroExcepcion;
    }

    public void setContratroExcepcion(String contratroExcepcion) {
        this.contratroExcepcion = contratroExcepcion;
    }

    public String getJustContExcep() {
        return justContExcep;
    }

    public void setJustContExcep(String justContExcep) {
        this.justContExcep = justContExcep;
    }

    public String getJustSistAdju() {
        return justSistAdju;
    }

    public void setJustSistAdju(String justSistAdju) {
        this.justSistAdju = justSistAdju;
    }

    public String getContratoAbiero() {
        return contratoAbiero;
    }

    public void setContratoAbiero(String contratoAbiero) {
        this.contratoAbiero = contratoAbiero;
    }

    public String getTipoContratoAbierto() {
        return tipoContratoAbierto;
    }

    public void setTipoContratoAbierto(String tipoContratoAbierto) {
        this.tipoContratoAbierto = tipoContratoAbierto;
    }

    public Long getTipoContratoMonto() {
        return tipoContratoMonto;
    }

    public void setTipoContratoMonto(Long tipoContratoMonto) {
        this.tipoContratoMonto = tipoContratoMonto;
    }

    public String getFundamentoContAbierto() {
        return fundamentoContAbierto;
    }

    public void setFundamentoContAbierto(String fundamentoContAbierto) {
        this.fundamentoContAbierto = fundamentoContAbierto;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Date getFechaVt() {
        return fechaVt;
    }

    public void setFechaVt(Date fechaVt) {
        this.fechaVt = fechaVt;
    }

    public String getLugarVt() {
        return lugarVt;
    }

    public void setLugarVt(String lugarVt) {
        this.lugarVt = lugarVt;
    }

    public Date getHoraVt() {
        return horaVt;
    }

    public void setHoraVt(Date horaVt) {
        this.horaVt = horaVt;
    }

    public String getProcedimientoVt() {
        return procedimientoVt;
    }

    public void setProcedimientoVt(String procedimientoVt) {
        this.procedimientoVt = procedimientoVt;
    }

    public String getNombreFunVt() {
        return nombreFunVt;
    }

    public void setNombreFunVt(String nombreFunVt) {
        this.nombreFunVt = nombreFunVt;
    }

    public String getParticipacionVt() {
        return participacionVt;
    }

    public void setParticipacionVt(String participacionVt) {
        this.participacionVt = participacionVt;
    }

    public String getAutorizacionFabricante() {
        return autorizacionFabricante;
    }

    public void setAutorizacionFabricante(String autorizacionFabricante) {
        this.autorizacionFabricante = autorizacionFabricante;
    }

    public String getJustificacionSolicitud() {
        return justificacionSolicitud;
    }

    public void setJustificacionSolicitud(String justificacionSolicitud) {
        this.justificacionSolicitud = justificacionSolicitud;
    }

    public String getMuestra() {
        return muestra;
    }

    public void setMuestra(String muestra) {
        this.muestra = muestra;
    }

    public String getRequePresentacion() {
        return requePresentacion;
    }

    public void setRequePresentacion(String requePresentacion) {
        this.requePresentacion = requePresentacion;
    }

    public String getGarantiaBienServicio() {
        return garantiaBienServicio;
    }

    public void setGarantiaBienServicio(String garantiaBienServicio) {
        this.garantiaBienServicio = garantiaBienServicio;
    }

    public String getPlazoReparReempl() {
        return plazoReparReempl;
    }

    public void setPlazoReparReempl(String plazoReparReempl) {
        this.plazoReparReempl = plazoReparReempl;
    }

    public String getComposicionPrecio() {
        return composicionPrecio;
    }

    public void setComposicionPrecio(String composicionPrecio) {
        this.composicionPrecio = composicionPrecio;
    }

    public String getExperienciaSolicitada() {
        return experienciaSolicitada;
    }

    public void setExperienciaSolicitada(String experienciaSolicitada) {
        this.experienciaSolicitada = experienciaSolicitada;
    }

    public String getRequisitoExperiencia() {
        return requisitoExperiencia;
    }

    public void setRequisitoExperiencia(String requisitoExperiencia) {
        this.requisitoExperiencia = requisitoExperiencia;
    }

    public String getCapacidadTecnica() {
        return capacidadTecnica;
    }

    public void setCapacidadTecnica(String capacidadTecnica) {
        this.capacidadTecnica = capacidadTecnica;
    }

    public String getRequisitosDocumentales() {
        return requisitosDocumentales;
    }

    public void setRequisitosDocumentales(String requisitosDocumentales) {
        this.requisitosDocumentales = requisitosDocumentales;
    }

    public String getNombreSolicitante() {
        return nombreSolicitante;
    }

    public void setNombreSolicitante(String nombreSolicitante) {
        this.nombreSolicitante = nombreSolicitante;
    }

    public String getDependenciaSolicitante() {
        return dependenciaSolicitante;
    }

    public void setDependenciaSolicitante(String dependenciaSolicitante) {
        this.dependenciaSolicitante = dependenciaSolicitante;
    }

    public String getCargoSolicitante() {
        return cargoSolicitante;
    }

    public void setCargoSolicitante(String cargoSolicitante) {
        this.cargoSolicitante = cargoSolicitante;
    }

    public String getJustificionNecesidad() {
        return justificionNecesidad;
    }

    public void setJustificionNecesidad(String justificionNecesidad) {
        this.justificionNecesidad = justificionNecesidad;
    }

    public String getJustificarPlanificacion() {
        return justificarPlanificacion;
    }

    public void setJustificarPlanificacion(String justificarPlanificacion) {
        this.justificarPlanificacion = justificarPlanificacion;
    }

    public String getJustificarEspeTecn() {
        return justificarEspeTecn;
    }

    public void setJustificarEspeTecn(String justificarEspeTecn) {
        this.justificarEspeTecn = justificarEspeTecn;
    }

    public String getEspecificacionesTecnicas() {
        return especificacionesTecnicas;
    }

    public void setEspecificacionesTecnicas(String especificacionesTecnicas) {
        this.especificacionesTecnicas = especificacionesTecnicas;
    }

    public String getDescripcionBien() {
        return descripcionBien;
    }

    public void setDescripcionBien(String descripcionBien) {
        this.descripcionBien = descripcionBien;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public String getLugarSede() {
        return lugarSede;
    }

    public void setLugarSede(String lugarSede) {
        this.lugarSede = lugarSede;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getIndicadorCumplimiento() {
        return indicadorCumplimiento;
    }

    public void setIndicadorCumplimiento(String indicadorCumplimiento) {
        this.indicadorCumplimiento = indicadorCumplimiento;
    }

    public String getPlanosDisenho() {
        return planosDisenho;
    }

    public void setPlanosDisenho(String planosDisenho) {
        this.planosDisenho = planosDisenho;
    }

    public String getInspeccionPrueba() {
        return inspeccionPrueba;
    }

    public void setInspeccionPrueba(String inspeccionPrueba) {
        this.inspeccionPrueba = inspeccionPrueba;
    }

    public String getMultaSerAplicar() {
        return multaSerAplicar;
    }

    public void setMultaSerAplicar(String multaSerAplicar) {
        this.multaSerAplicar = multaSerAplicar;
    }

    public String getAnticipo() {
        return anticipo;
    }

    public void setAnticipo(String anticipo) {
        this.anticipo = anticipo;
    }

    public String getDictamenTecnico() {
        return dictamenTecnico;
    }

    public void setDictamenTecnico(String dictamenTecnico) {
        this.dictamenTecnico = dictamenTecnico;
    }

    public String getReportePorcentaje() {
        return reportePorcentaje;
    }

    public void setReportePorcentaje(String reportePorcentaje) {
        this.reportePorcentaje = reportePorcentaje;
    }

    public Long getTotalAdjAnho() {
        return totalAdjAnho;
    }

    public void setTotalAdjAnho(Long totalAdjAnho) {
        this.totalAdjAnho = totalAdjAnho;
    }

    public String getPromedioTotalAdjud() {
        return promedioTotalAdjud;
    }

    public void setPromedioTotalAdjud(String promedioTotalAdjud) {
        this.promedioTotalAdjud = promedioTotalAdjud;
    }

    public Long getTotales() {
        return totales;
    }

    public void setTotales(Long totales) {
        this.totales = totales;
    }

    public String getPromedUtilizAño() {
        return promedUtilizAño;
    }

    public void setPromedUtilizAño(String promedUtilizAño) {
        this.promedUtilizAño = promedUtilizAño;
    }

    public String getElaboradoFuncionario() {
        return elaboradoFuncionario;
    }

    public void setElaboradoFuncionario(String elaboradoFuncionario) {
        this.elaboradoFuncionario = elaboradoFuncionario;
    }

    public String getAprobadoFuncionario() {
        return aprobadoFuncionario;
    }

    public void setAprobadoFuncionario(String aprobadoFuncionario) {
        this.aprobadoFuncionario = aprobadoFuncionario;
    }

    public HistoricoContrato getHistoricoContrato() {
        return historicoContrato;
    }

    public void setHistoricoContrato(HistoricoContrato historicoContrato) {
        this.historicoContrato = historicoContrato;
    }

    public HistoricoEjecucion getHistoricoEjecucion() {
        return historicoEjecucion;
    }

    public void setHistoricoEjecucion(HistoricoEjecucion historicoEjecucion) {
        this.historicoEjecucion = historicoEjecucion;
    }

   

    public SistemaAdjudicacion getSistemaAdjudicacion() {
        return sistemaAdjudicacion;
    }

    public void setSistemaAdjudicacion(SistemaAdjudicacion sistemaAdjudicacion) {
        this.sistemaAdjudicacion = sistemaAdjudicacion;
    }

    public TipoModalidad getTipoModalidad() {
        return tipoModalidad;
    }

    public void setTipoModalidad(TipoModalidad tipoModalidad) {
        this.tipoModalidad = tipoModalidad;
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
        if (!(object instanceof Formulario)) {
            return false;
        }
        Formulario other = (Formulario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "py.com.startic.gestion.model.Formulario[ id=" + id + " ]";
    }
    
}
