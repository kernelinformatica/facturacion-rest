package datos;


import entidades.OrdenesPagosPCab;
import entidades.OrdenesPagosPie;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author DarioQuiroga
 */
public class OrdenPagoPieResponse implements Payload {
   private  Integer idOPPie; 
   private List<OrdenPagoCabResponse> idOPCab; 
   private  Integer idImpuesto;
   private String detalle;
   private BigDecimal alicuota;
    
    
    
    public OrdenPagoPieResponse(OrdenesPagosPie p) {
        this.idOPPie = p.getIdOPPie();
        this.idOPCab =  new ArrayList<>();
        this.idImpuesto = p.getIdImpuesto();
        this.detalle = p.getDetalle();
        this.alicuota = p.getAlicuota();
       
                
    }

    public Integer getIdOPPie() {
        return idOPPie;
    }

    public void setIdOPPie(Integer idOPPie) {
        this.idOPPie = idOPPie;
    }

    public List<OrdenPagoCabResponse> getIdOPCab() {
        return idOPCab;
    }

    public void setIdOPCab(List<OrdenPagoCabResponse> idOPCab) {
        this.idOPCab = idOPCab;
    }

    public Integer getIdImpuesto() {
        return idImpuesto;
    }

    public void setIdImpuesto(Integer idImpuesto) {
        this.idImpuesto = idImpuesto;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public BigDecimal getAlicuota() {
        return alicuota;
    }

    public void setAlicuota(BigDecimal alicuota) {
        this.alicuota = alicuota;
    }

   
    
    
    
    @Override
    public String getClassName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

 
}
