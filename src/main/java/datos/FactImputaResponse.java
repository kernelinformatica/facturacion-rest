package datos;

import entidades.FactImputa;
import java.math.BigDecimal;

/**
 *
 * @author FrancoSili
 */

public class FactImputaResponse implements Payload {
    private Integer idFactImputa;
    private Integer masAsiento;
    private BigDecimal importeImputado;
    private Integer masAsientoImputado;
    private BigDecimal cantidadImputada;
    private FactDetalleResponse detalle;
    private FactDetalleResponse detalleImputado;

    public FactImputaResponse(FactImputa f) {
        this.idFactImputa = f.getIdFactImputa();
        this.masAsiento = f.getMasAsiento();
        this.importeImputado = f.getImporteImputado();
        this.masAsientoImputado = f.getMasAsiento();
        this.cantidadImputada = f.getCantidadImputada();
        this.detalle = new FactDetalleResponse(f.getIdFactDetalle());
        this.detalleImputado = new FactDetalleResponse(f.getIdFactDetalleImputa());
    }
    
    

    public Integer getIdFactImputa() {
        return idFactImputa;
    }

    public void setIdFactImputa(Integer idFactImputa) {
        this.idFactImputa = idFactImputa;
    }

    public Integer getMasAsiento() {
        return masAsiento;
    }

    public void setMasAsiento(Integer masAsiento) {
        this.masAsiento = masAsiento;
    }

    public BigDecimal getImporteImputado() {
        return importeImputado;
    }

    public void setImporteImputado(BigDecimal importeImputado) {
        this.importeImputado = importeImputado;
    }

    public Integer getMasAsientoImputado() {
        return masAsientoImputado;
    }

    public void setMasAsientoImputado(Integer masAsientoImputado) {
        this.masAsientoImputado = masAsientoImputado;
    }

    public BigDecimal getCantidadImputada() {
        return cantidadImputada;
    }

    public void setCantidadImputada(BigDecimal cantidadImputada) {
        this.cantidadImputada = cantidadImputada;
    }
    
    
    
    
    @Override
    public String getClassName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
