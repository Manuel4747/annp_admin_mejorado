/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package py.com.startic.gestion.datasource;

/**
 *
 * @author Grecia
 */
public class RepAvanceAcciones {

    private String acciones;
    private String objetivoDescripcion;
    private double meta; 
    private String año;
    private String indicador;
    private double variable;
    private double valorVariable;
    private double resultado;
    private double resultadoPresupuestario;

    public String getAcciones() {
        return acciones;
    }

    public void setAcciones(String acciones) {
        this.acciones = acciones;
    }

    public String getObjetivoDescripcion() {
        return objetivoDescripcion;
    }

    public void setObjetivoDescripcion(String objetivoDescripcion) {
        this.objetivoDescripcion = objetivoDescripcion;
    }

    public double getMeta() {
        return meta;
    }

    public void setMeta(double meta) {
        this.meta = meta;
    }

    public String getIndicador() {
        return indicador;
    }

    public void setIndicador(String indicador) {
        this.indicador = indicador;
    }

    public double getVariable() {
        return variable;
    }

    public void setVariable(double variable) {
        this.variable = variable;
    }

    public double getResultado() {
        return resultado;
    }

    public String getAño() {
        return año;
    }

    public void setAño(String año) {
        this.año = año;
    }

    public double getValorVariable() {
        return valorVariable;
    }

    public void setValorVariable(double valorVariable) {
        this.valorVariable = valorVariable;
    }  

    public void setResultado(double resultado) {
        this.resultado = resultado;
    }

    public double getResultadoPresupuestario() {
        return resultadoPresupuestario;
    }

    public void setResultadoPresupuestario(double resultadoPresupuestario) {
        this.resultadoPresupuestario = resultadoPresupuestario;
    }
    
    
    
}
