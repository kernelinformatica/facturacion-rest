package datos;

import java.math.BigDecimal;

/**
 *
 * @author FrancoSili
 */
public class SubTotalResponse implements Payload {
    private BigDecimal subTotal;
    private BigDecimal subTotalIva;

    public SubTotalResponse(BigDecimal subTotal, BigDecimal subTotalIva) {
        this.subTotal = subTotal;
        this.subTotalIva = subTotalIva;
    }

    public SubTotalResponse() {
        this.subTotal = new BigDecimal(0);
        this.subTotalIva = new BigDecimal(0);
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getSubTotalIva() {
        return subTotalIva;
    }

    public void setSubTotalIva(BigDecimal subTotalIva) {
        this.subTotalIva = subTotalIva;
    }

    @Override
    public String getClassName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
