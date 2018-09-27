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

//    public FacturaResponse(String cuentaContable, String descripcion, String tipoModelo, BigDecimal importeTotal, BigDecimal porcentaje) {
//        this.cuentaContable = cuentaContable;
//        this.descripcion = descripcion;
//        this.importeTotal = importeTotal;        
//    }
    
    public FacturaResponse(String cuentaContable, String descripcion, BigDecimal importeTotal, BigDecimal porcentaje) {
        this.cuentaContable = cuentaContable;
        this.descripcion = descripcion;
        this.importeTotal = importeTotal;
        this.porcentaje = porcentaje;
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
    
    @Override
    public String getClassName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
