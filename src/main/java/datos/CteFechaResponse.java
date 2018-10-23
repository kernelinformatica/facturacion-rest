package datos;

import entidades.CteFecha;
import java.util.Date;

/**
 *
 * @author FrancoSili
 */
public class CteFechaResponse implements Payload{
    private Integer idCteFechas;
    private Integer puntoVenta;
    private Date fechaApertura;
    private Date fechaCierre;
    private CteTipoResponse cteTipo;

    public CteFechaResponse(CteFecha c) {
        this.idCteFechas = c.getIdCteFechas();
        this.puntoVenta = c.getPuntoVenta();
        this.fechaApertura = c.getFechaApertura();
        this.fechaCierre = c.getFechaCierre();
        this.cteTipo = new CteTipoResponse(c.getIdCteTipo());
    }
    

    public Integer getIdCteFechas() {
        return idCteFechas;
    }

    public void setIdCteFechas(Integer idCteFechas) {
        this.idCteFechas = idCteFechas;
    }

    public Integer getPuntoVenta() {
        return puntoVenta;
    }

    public void setPuntoVenta(Integer puntoVenta) {
        this.puntoVenta = puntoVenta;
    }

    public Date getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(Date fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public Date getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public CteTipoResponse getCteTipo() {
        return cteTipo;
    }

    public void setCteTipo(CteTipoResponse cteTipo) {
        this.cteTipo = cteTipo;
    }
    
    

    @Override
    public String getClassName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
