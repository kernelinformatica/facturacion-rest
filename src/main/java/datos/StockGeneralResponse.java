package datos;

import java.math.BigDecimal;

/**
 *
 * @author FrancoSili
 */
public class StockGeneralResponse implements Payload {
    private BigDecimal ingresos;
    private BigDecimal egresos;
    private String deposito;
    private Boolean trazable;
    private String rubro;
    private String subRubro;
    private String codProducto;
    private String descripcion;
    
    public StockGeneralResponse(BigDecimal ingresos, BigDecimal egresos, String deposito, boolean trazable, String rubro, String subRubro, String codProducto, String descripcion) {
        this.ingresos = ingresos;
        this.egresos = egresos;
        this.deposito = deposito;
        this.trazable = trazable;
        this.rubro = rubro;
        this.subRubro = subRubro;
        this.codProducto = codProducto;
        this.descripcion = descripcion;
    }

    public BigDecimal getIngresos() {
        return ingresos;
    }

    public void setIngresos(BigDecimal ingresos) {
        this.ingresos = ingresos;
    }

    public BigDecimal getEgresos() {
        return egresos;
    }

    public void setEgresos(BigDecimal egresos) {
        this.egresos = egresos;
    }

    public String getDeposito() {
        return deposito;
    }

    public void setDeposito(String deposito) {
        this.deposito = deposito;
    }

    public Boolean getTrazable() {
        return trazable;
    }

    public void setTrazable(Boolean trazable) {
        this.trazable = trazable;
    }

    public String getRubro() {
        return rubro;
    }

    public void setRubro(String rubro) {
        this.rubro = rubro;
    }

    public String getSubRubro() {
        return subRubro;
    }

    public void setSubRubro(String subRubro) {
        this.subRubro = subRubro;
    }

    public String getCodProducto() {
        return codProducto;
    }

    public void setCodProducto(String codProducto) {
        this.codProducto = codProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    
    @Override
    public String getClassName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
