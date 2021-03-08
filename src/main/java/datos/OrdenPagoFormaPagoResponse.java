package datos;

import entidades.CteTipo;
import entidades.OrdenesPagosPCab;
import entidades.OrdenesPagosFormaPago;
import entidades.FactCab;
import entidades.Sucursal;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 *
 * @author DaarioQuiroga
 */
public class OrdenPagoFormaPagoResponse implements Payload {

    private Integer idOPFormaPago;
    private List<OrdenPagoCabResponse> idOPCab;
    private Integer idFormaPago;
    private BigDecimal importe;
    private Date fechaAcreditacion;
    private Long numero;
    private String detalle;

    public OrdenPagoFormaPagoResponse(OrdenesPagosFormaPago c) {
        this.idOPFormaPago = c.getIdFormaPago();

        this.idFormaPago = c.getIdFormaPago();
        this.importe = c.getImporte();
        this.fechaAcreditacion = c.getFechaAcreditacion();
        this.numero = c.getNumero();
        this.detalle = c.getDetalle();
        this.idOPCab = new ArrayList<>();

    }

    
     public void agregarIdOpCab(List<OrdenPagoCabResponse> f) {
        for(OrdenPagoCabResponse op: f) {
            this.idOPCab.add(op);
        }
    }
    public Integer getIdOPFormaPago() {
        return idOPFormaPago;
    }

    public void setIdOPFormaPago(Integer idOPFormaPago) {
        this.idOPFormaPago = idOPFormaPago;
    }

    public List<OrdenPagoCabResponse> getIdOPCab() {
        return idOPCab;
    }

    public void setIdOPCab(List<OrdenPagoCabResponse> idOPCab) {
        this.idOPCab = idOPCab;
    }

   

   

    public Integer getIdFormaPago() {
        return idFormaPago;
    }

    public void setIdFormaPago(Integer idFormaPago) {
        this.idFormaPago = idFormaPago;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public Date getFechaAcreditacion() {
        return fechaAcreditacion;
    }

    public void setFechaAcreditacion(Date fechaAcreditacion) {
        this.fechaAcreditacion = fechaAcreditacion;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    @Override
    public String getClassName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
