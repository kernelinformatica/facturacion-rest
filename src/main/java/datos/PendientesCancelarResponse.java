package datos;

import entidades.Producto;
import java.math.BigDecimal;

/**
 *
 * @author FrancoSili
 */
public class PendientesCancelarResponse implements Payload{
    private String comprobante;
    private String numero;   
    private BigDecimal original;
    private BigDecimal pendiente;
    private BigDecimal precio;
    private BigDecimal dolar;
    private String moneda;
    private BigDecimal porCalc;
    private BigDecimal ivaPorc;
    private Integer deposito;
    private ProductoResponse producto;

    public PendientesCancelarResponse(String comprobante, String numero, BigDecimal original, BigDecimal pendiente, BigDecimal precio, BigDecimal dolar, String moneda, BigDecimal porCalc, BigDecimal ivaPorc, Integer deposito, ProductoResponse producto) {
        this.comprobante = comprobante;
        this.numero = numero;
        this.original = original;
        this.pendiente = pendiente;
        this.precio = precio;
        this.dolar = dolar;
        this.moneda = moneda;
        this.porCalc = porCalc;
        this.ivaPorc = ivaPorc;
        this.deposito = deposito;
        this.producto = producto;
    }

    public ProductoResponse getProducto() {
        return producto;
    }

    public void setProducto(ProductoResponse producto) {
        this.producto = producto;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getIvaPorc() {
        return ivaPorc;
    }

    public void setIvaPorc(BigDecimal ivaPorc) {
        this.ivaPorc = ivaPorc;
    }

    public Integer getDeposito() {
        return deposito;
    }

    public void setDeposito(Integer deposito) {
        this.deposito = deposito;
    }

    public String getComprobante() {
        return comprobante;
    }

    public void setComprobante(String comprobante) {
        this.comprobante = comprobante;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public BigDecimal getOriginal() {
        return original;
    }

    public void setOriginal(BigDecimal original) {
        this.original = original;
    }

    public BigDecimal getPendiente() {
        return pendiente;
    }

    public void setPendiente(BigDecimal pendiente) {
        this.pendiente = pendiente;
    }

    public BigDecimal getDolar() {
        return dolar;
    }

    public void setDolar(BigDecimal dolar) {
        this.dolar = dolar;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public BigDecimal getPorCalc() {
        return porCalc;
    }

    public void setPorCalc(BigDecimal porCalc) {
        this.porCalc = porCalc;
    }

    @Override
    public String getClassName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
