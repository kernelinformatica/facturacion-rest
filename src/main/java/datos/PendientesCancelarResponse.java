package datos;

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
    private Integer idFactDetalleImputa;
    private Integer idFactCabImputada;
    private BigDecimal descuento;
    private String tipoDescuento;
    private BigDecimal cantBultos;
    private String despacho;
    private String observaciones;
    private Integer itemImputada;
    private BigDecimal importe;
    private ProductoResponse producto;

    public PendientesCancelarResponse(String comprobante, String numero, BigDecimal original, BigDecimal pendiente, BigDecimal precio, BigDecimal dolar, String moneda, BigDecimal porCalc, BigDecimal ivaPorc, Integer deposito, Integer idFactDetalleImputa, Integer idFactCabImputada, BigDecimal descuento, String tipoDescuento, BigDecimal cantBultos, String despacho, String observaciones, Integer itemImputada,BigDecimal importe, ProductoResponse p) {
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
        this.idFactDetalleImputa = idFactDetalleImputa;
        this.idFactCabImputada = idFactCabImputada;
        this.descuento = descuento;
        this.tipoDescuento = tipoDescuento;
        this.cantBultos = cantBultos;
        this.despacho = despacho;
        this.observaciones = observaciones;
        this.itemImputada = itemImputada;
        this.producto = p;
        this.importe = importe;
    }
    public PendientesCancelarResponse(ProductoResponse p) {
        this.comprobante = "";
        this.numero = "0";
        this.original = new BigDecimal(0);
        this.pendiente = new BigDecimal(0);
        this.precio = p.getPrecioVentaProv();
        this.dolar = new BigDecimal(0);
        this.moneda = " ";
        this.porCalc = new BigDecimal(0);
        this.ivaPorc = p.getIVA().getPorcIVA();
        this.deposito = 0;
        this.idFactDetalleImputa = null;
        this.idFactCabImputada = null;
        this.descuento = new BigDecimal(0);
        this.tipoDescuento = "%";
        this.cantBultos = new BigDecimal(0);;
        this.despacho = "";
        this.observaciones = "";
        this.itemImputada = 0;
        this.producto = p;
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

    public Integer getIdFactDetalleImputa() {
        return idFactDetalleImputa;
    }

    public void setIdFactDetalleImputa(Integer idFactDetalleImputa) {
        this.idFactDetalleImputa = idFactDetalleImputa;
    }

    public Integer getIdFactCabImputada() {
        return idFactCabImputada;
    }

    public void setIdFactCabImputada(Integer idFactCabImputada) {
        this.idFactCabImputada = idFactCabImputada;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getCantBultos() {
        return cantBultos;
    }

    public void setCantBultos(BigDecimal cantBultos) {
        this.cantBultos = cantBultos;
    }

    public String getDespacho() {
        return despacho;
    }

    public void setDespacho(String despacho) {
        this.despacho = despacho;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getTipoDescuento() {
        return tipoDescuento;
    }

    public void setTipoDescuento(String tipoDescuento) {
        this.tipoDescuento = tipoDescuento;
    }

    public Integer getItemImputada() {
        return itemImputada;
    }

    public void setItemImputada(Integer itemImputada) {
        this.itemImputada = itemImputada;
    }
    
    
    

    @Override
    public String getClassName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
