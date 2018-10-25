package datos;

import entidades.CteNumerador;
import java.util.Date;

/**
 *
 * @author FrancoSili
 */
public class CteNumeradorResponse implements Payload {
    private Integer idCteNumerador;
    private String descripcion;
    private Date fechaApertura;
    private Date fechaCierre;
    private CteNumeroResponse numero;

    public CteNumeradorResponse(CteNumerador c) {
        this.idCteNumerador = c.getIdCteNumerador();
        this.descripcion = c.getDescripcion();
        this.fechaApertura = c.getFechaApertura();
        this.fechaCierre = c.getFechaCierre();
        this.numero = new CteNumeroResponse(c.getIdCteNumero());
    }
    
    public Integer getIdCteNumerador() {
        return idCteNumerador;
    }

    public void setIdCteNumerador(Integer idCteNumerador) {
        this.idCteNumerador = idCteNumerador;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public CteNumeroResponse getNumero() {
        return numero;
    }

    public void setNumero(CteNumeroResponse numero) {
        this.numero = numero;
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

    @Override
    public String getClassName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
