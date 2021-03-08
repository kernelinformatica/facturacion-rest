package datos;

import entidades.CteTipo;
import entidades.OrdenesPagosPCab;
import entidades.OrdenesPagosDetalle;
import entidades.FactCab;
import entidades.Sucursal;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author DaarioQuiroga
 */
public class OrdenPagoDetalleResponse implements Payload {
    private Integer idOPDetalle; 
   private List<OrdenesPagosPCab> idOPCab; 
   private  Integer item;
   private  Integer idFactCab;
   private BigDecimal pagadoDolar;  
   private BigDecimal importePesificado;
   private Integer idFormaPago;
   private BigDecimal cotDolarFact;
   private BigDecimal difCotizacion;
   private BigDecimal ivaDifCotizacion;
   private Integer idIVA;
    
    
    
    public OrdenPagoDetalleResponse(OrdenesPagosDetalle c) {
        this.idOPDetalle = c.getIdOPDetalle();
        this.idOPCab =  new ArrayList<>();
        this.item = c.getItem();
        this.idFactCab = c.getIdFactCab();
        this.pagadoDolar = c.getPagadoDolar();
        this.importePesificado = c.getImportePesificado();
        this.idFormaPago = c.getIdFormaPago();
        this.cotDolarFact = c.getCotDolarFact();
        this.difCotizacion = c.getDifCotizacion();
        this.idIVA = c.getIdIVA();
        this.ivaDifCotizacion = c.getIvaDifCotizacion();
                
    }

    public Integer getIdOPDetalle() {
        return idOPDetalle;
    }

    public void setIdOPDetalle(Integer idOPDetalle) {
        this.idOPDetalle = idOPDetalle;
    }

    public List<OrdenesPagosPCab> getIdOPCab() {
        return idOPCab;
    }

    public void setIdOPCab(List<OrdenesPagosPCab> idOPCab) {
        this.idOPCab = idOPCab;
    }

    public Integer getItem() {
        return item;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public Integer getIdFactCab() {
        return idFactCab;
    }

    public void setIdFactCab(Integer idFactCab) {
        this.idFactCab = idFactCab;
    }

    public BigDecimal getPagadoDolar() {
        return pagadoDolar;
    }

    public void setPagadoDolar(BigDecimal pagadoDolar) {
        this.pagadoDolar = pagadoDolar;
    }

    public BigDecimal getImportePesificado() {
        return importePesificado;
    }

    public void setImportePesificado(BigDecimal importePesificado) {
        this.importePesificado = importePesificado;
    }

    public Integer getIdFormaPago() {
        return idFormaPago;
    }

    public void setIdFormaPago(Integer idFormaPago) {
        this.idFormaPago = idFormaPago;
    }

    public BigDecimal getCotDolarFact() {
        return cotDolarFact;
    }

    public void setCotDolarFact(BigDecimal cotDolarFact) {
        this.cotDolarFact = cotDolarFact;
    }

    public BigDecimal getDifCotizacion() {
        return difCotizacion;
    }

    public void setDifCotizacion(BigDecimal difCotizacion) {
        this.difCotizacion = difCotizacion;
    }

    public BigDecimal getIvaDifCotizacion() {
        return ivaDifCotizacion;
    }

    public void setIvaDifCotizacion(BigDecimal ivaDifCotizacion) {
        this.ivaDifCotizacion = ivaDifCotizacion;
    }

    public Integer getIdIVA() {
        return idIVA;
    }

    public void setIdIVA(Integer idIVA) {
        this.idIVA = idIVA;
    }

    
    
    
    @Override
    public String getClassName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

 
}
