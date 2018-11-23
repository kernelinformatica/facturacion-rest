package datos;

import java.math.BigDecimal;

/**
 *
 * @author FrancoSili
 */
public class FacturaResponse implements Payload{
    private String cuentaContable;
    private String descripcion;  
    private BigDecimal importeTotal;
    private BigDecimal porcentaje;
    private Integer orden;
    private Integer idSisTipoModelo;

//    public FacturaResponse(String cuentaContable, String descripcion, String tipoModelo, BigDecimal importeTotal, BigDecimal porcentaje) {
//        this.cuentaContable = cuentaContable;
//        this.descripcion = descripcion;
//        this.importeTotal = importeTotal;        
//    }
    
    public FacturaResponse(String cuentaContable, String descripcion, BigDecimal importeTotal, BigDecimal porcentaje, Integer orden, Integer idSisTipoModelo) {
        this.cuentaContable = cuentaContable;
        this.descripcion = descripcion;
        this.importeTotal = importeTotal;
        this.porcentaje = porcentaje;
        this.orden = orden;
        this.idSisTipoModelo = idSisTipoModelo;
    }

    public String getCuentaContable() {
        return cuentaContable;
    }

    public void setCuentaContable(String cuentaContable) {
        this.cuentaContable = cuentaContable;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }
    
    public BigDecimal getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(BigDecimal importeTotal) {
        this.importeTotal = importeTotal;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    @Override
    public String getClassName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Integer getIdSisTipoModelo() {
        return idSisTipoModelo;
    }

    public void setIdSisTipoModelo(Integer idSisTipoModelo) {
        this.idSisTipoModelo = idSisTipoModelo;
    }    
}
