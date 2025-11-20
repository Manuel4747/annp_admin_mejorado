/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.startic.gestion.models;

import java.util.Date;

public class ReporteMarcaciones {

    private int id;
    private int usuarioId;
    private String nombresApellidos;
    private Date fecha;
    private Date entrada;
    private Date salida;
    private Date horarioSalida;
    private Date total;
    private Date extras;
    private Date adicionales;

    public ReporteMarcaciones() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombresApellidos() {
        return nombresApellidos;
    }

    public void setNombresApellidos(String nombresApellidos) {
        this.nombresApellidos = nombresApellidos;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getEntrada() {
        return entrada;
    }

    public void setEntrada(Date entrada) {
        this.entrada = entrada;
    }

    public Date getSalida() {
        return salida;
    }

    public void setSalida(Date salida) {
        this.salida = salida;
    }

    public Date getHorarioSalida() {
        return horarioSalida;
    }

    public void setHorarioSalida(Date horarioSalida) {
        this.horarioSalida = horarioSalida;
    }

    public Date getTotal() {
        return total;
    }

    public void setTotal(Date total) {
        this.total = total;
    }

    public Date getExtras() {
        return extras;
    }

    public void setExtras(Date extras) {
        this.extras = extras;
    }

    public Date getAdicionales() {
        return adicionales;
    }

    public void setAdicionales(Date adicionales) {
        this.adicionales = adicionales;
    }
    
}
