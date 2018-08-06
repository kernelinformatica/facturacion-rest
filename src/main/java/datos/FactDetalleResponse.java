package datos;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author FrancoSili
 */
public class FactDetalleResponse implements Payload{
    private String comprobante;
    private long numero;
    private Date fechaEmision;
    private String codProducto;
    private String articulo;
    private BigDecimal original;
    private BigDecimal pendiente;
    private BigDecimal precio;
    private BigDecimal dolar;
    private String moneda;
    private BigDecimal porCalc;
    private BigDecimal ivaPorc;
    private int deposito;

    public FactDetalleResponse(String comprobante, long numero, Date fechaEmision, String codProducto, String articulo, BigDecimal original, BigDecimal pendiente, BigDecimal precio, BigDecimal dolar, String moneda, BigDecimal porCalc, BigDecimal ivaPorc, int deposito) {
        this.comprobante = comprobante;
        this.numero = numero;
        this.fechaEmision = fechaEmision;
        this.codProducto = codProducto;
        this.articulo = articulo;
        this.original = original;
        this.pendiente = pendiente;
        this.precio = precio;
        this.dolar = dolar;
        this.moneda = moneda;
        this.porCalc = porCalc;
        this.ivaPorc = ivaPorc;
        this.deposito = deposito;
    }

    
    public String getComprobante() {
        return comprobante;
    }

    public void setComprobante(String comprobante) {
        this.comprobante = comprobante;
    }

    public long getNumero() {
        return numero;
    }

    public void setNumero(long numero) {
        this.numero = numero;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getCodProducto() {
        return codProducto;
    }

    public void setCodProducto(String codProducto) {
        this.codProducto = codProducto;
    }

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
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

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
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

    public BigDecimal getIvaPorc() {
        return ivaPorc;
    }

    public void setIvaPorc(BigDecimal ivaPorc) {
        this.ivaPorc = ivaPorc;
    }

    public int getDeposito() {
        return deposito;
    }

    public void setDeposito(int deposito) {
        this.deposito = deposito;
    }

    @Override
    public String getClassName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
