package datos;

import entidades.ModeloDetalle;
import java.math.BigDecimal;

/**
 *
 * @author FrancoSili
 */
public class ModeloDetalleResponse implements Payload {
    private Integer idModeloDetalle;   
    private String ctaContable;   
    //private Integer idSisModelos;
    private Integer orden;
    private String descripcion;
    private String dh;
    private Boolean prioritario;
    private ModeloCabResponse ModeloCab;
    private BigDecimal totalModelo;

    public ModeloDetalleResponse(ModeloDetalle s) {
        this.idModeloDetalle = s.getIdModeloDetalle();
        this.ctaContable = s.getCtaContable();
        //this.idSisModelos = s.getIdSisModelos();
        this.orden = s.getOrden();
        this.descripcion = s.getDescripcion();
        this.dh = s.getDh();
        this.prioritario = s.getPrioritario();
        this.ModeloCab = new ModeloCabResponse(s.getIdModeloCab());
    }
    
    public ModeloDetalleResponse(ModeloDetalle s, BigDecimal total) {
        this.idModeloDetalle = s.getIdModeloDetalle();
        this.ctaContable = s.getCtaContable();
        //this.idSisModelos = s.getIdSisModelos();
        this.orden = s.getOrden();
        this.descripcion = s.getDescripcion();
        this.dh = s.getDh();
        this.prioritario = s.getPrioritario();
        this.ModeloCab = new ModeloCabResponse(s.getIdModeloCab());
        this.totalModelo = total;
    }

    public Integer getIdModeloDetalle() {
        return idModeloDetalle;
    }

    public void setIdModeloDetalle(Integer idModeloDetalle) {
        this.idModeloDetalle = idModeloDetalle;
    }

    public String getCtaContable() {
        return ctaContable;
    }

    public void setCtaContable(String ctaContable) {
        this.ctaContable = ctaContable;
    }

//    public Integer getIdSisModelos() {
//        return idSisModelos;
//    }
//
//    public void setIdSisModelos(Integer idSisModelos) {
//        this.idSisModelos = idSisModelos;
//    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDh() {
        return dh;
    }

    public void setDh(String dh) {
        this.dh = dh;
    }

    public Boolean getPrioritario() {
        return prioritario;
    }

    public void setPrioritario(Boolean prioritario) {
        this.prioritario = prioritario;
    }

    public ModeloCabResponse getModeloCab() {
        return ModeloCab;
    }

    public void setModeloCab(ModeloCabResponse ModeloCab) {
        this.ModeloCab = ModeloCab;
    }

    public BigDecimal getTotalModelo() {
        return totalModelo;
    }

    public void setTotalModelo(BigDecimal totalModelo) {
        this.totalModelo = totalModelo;
    }

    @Override
    public String getClassName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
