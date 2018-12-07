package datos;

import entidades.FactCab;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 *
 * @author FrancoSili
 */
public class FactCabResponse implements Payload{
    private Integer idFactCab;
//    private String letra;
      private long numero;
//    private Date fechaEmision;
//    private Date fechaVto;
//    private Date fechaConta;
//    private String cai;
//    private Date caiVto;
//    private String codBarra;    
    private Integer idPadron;    
    private String nombre;    
    private String cuit;       
//    private String codigoPostal;
//    private String idListaPrecios;
    private BigDecimal cotDolar;
//    private Date fechaDolar;
//    private Integer idDepositos;
//    private Object observaciones;
//    private String sitIVA;
//    private CteTipoResponse cteTipo;
//    private SisMonedasResponse idmoneda;
    private String modulo;
    private String comprobante;
    private String moneda;
    private String imputada;
    private Date fechaEmi;
    private String vendedor;
    private List<FactDetalleResponse> detalle;
    
//    FactCabResponse(FactCab f) {
//        this.idFactCab = f.getIdFactCab();
//        this.letra = f.getLetra();
//        this.numero = f.getNumero();
//        this.fechaEmision = f.getFechaEmision();
//        this.fechaVto = f.getFechaVto();
//        this.fechaConta = f.getFechaConta();
//        this.cai = f.getCai();
//        this.caiVto = f.getCaiVto();
//        this.codBarra = f.getCodBarra();
//        this.idPadron = f.getIdPadron();
//        this.nombre = f.getNombre();
//        this.cuit = f.getCuit();
//        this.codigoPostal = f.getCodigoPostal();
//        this.idListaPrecios = f.getIdListaPrecios();
//        this.cotDolar = f.getCotDolar();       
//        this.fechaDolar = f.getFechaDolar();
//        this.idDepositos = f.getIdDepositos();
//        this.observaciones = f.getObservaciones();
//        this.sitIVA = f.getSitIVA();
//        this.cteTipo = new CteTipoResponse(f.getIdCteTipo());
//        this.idmoneda = new SisMonedasResponse(f.getIdmoneda());
//    }

    public FactCabResponse(Integer idFactCab, String comprobante,long numero, Date fechaEmision, Integer idPadron, String nombre, String cuit, BigDecimal cotDolar, String moneda, String imputada, String modulo, String vendedor) {
        this.idFactCab = idFactCab;
        this.comprobante = comprobante;
        this.numero = numero;
        this.fechaEmi = fechaEmision;
        this.idPadron = idPadron;
        this.nombre = nombre;
        this.cuit = cuit;
        this.cotDolar = cotDolar;
        this.detalle = new ArrayList<>();
        this.moneda = moneda;
        this.imputada = imputada;
        this.modulo = modulo;
        this.vendedor = vendedor;
    }

    public FactCabResponse(Integer idFactCab) {
        this.idFactCab = idFactCab;
    }
    
//    public String getLetra() {
//        return letra;
//    }
//
//    public void setLetra(String letra) {
//        this.letra = letra;
//    }

    public long getNumero() {
        return numero;
    }

    public void setNumero(long numero) {
        this.numero = numero;
    }

//    public Date getFechaEmision() {
//        return fechaEmision;
//    }
//
//    public void setFechaEmision(Date fechaEmision) {
//        this.fechaEmision = fechaEmision;
//    }
//
//    public Date getFechaVto() {
//        return fechaVto;
//    }
//
//    public void setFechaVto(Date fechaVto) {
//        this.fechaVto = fechaVto;
//    }
//
//    public String getCai() {
//        return cai;
//    }
//
//    public void setCai(String cai) {
//        this.cai = cai;
//    }

//    public Date getCaiVto() {
//        return caiVto;
//    }
//
//    public void setCaiVto(Date caiVto) {
//        this.caiVto = caiVto;
//    }
//
//    public String getCodBarra() {
//        return codBarra;
//    }
//
//    public void setCodBarra(String codBarra) {
//        this.codBarra = codBarra;
//    }

    public int getIdPadron() {
        return idPadron;
    }

    public void setIdPadron(int idPadron) {
        this.idPadron = idPadron;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

//    public String getCodigoPostal() {
//        return codigoPostal;
//    }
//
//    public void setCodigoPostal(String codigoPostal) {
//        this.codigoPostal = codigoPostal;
//    }
//
//    public String getIdListaPrecios() {
//        return idListaPrecios;
//    }
//
//    public void setIdListaPrecios(String idListaPrecios) {
//        this.idListaPrecios = idListaPrecios;
//    }
//
//    public CteTipoResponse getCteTipo() {
//        return cteTipo;
//    }
//
//    public void setCteTipo(CteTipoResponse cteTipo) {
//        this.cteTipo = cteTipo;
//    }

//    public SisMonedasResponse getIdmoneda() {
//        return idmoneda;
//    }
//
//    public void setIdmoneda(SisMonedasResponse idmoneda) {
//        this.idmoneda = idmoneda;
//    }
    
    public Integer getIdFactCab() {
        return idFactCab;
    }

    public void setIdFactCab(Integer idFactCab) {
        this.idFactCab = idFactCab;
    }

//    public Date getFechaConta() {
//        return fechaConta;
//    }
//
//    public void setFechaConta(Date fechaConta) {
//        this.fechaConta = fechaConta;
//    }

    public BigDecimal getCotDolar() {
        return cotDolar;
    }

    public void setCotDolar(BigDecimal cotDolar) {
        this.cotDolar = cotDolar;
    }

//    public Object getObservaciones() {
//        return observaciones;
//    }
//
//    public void setObservaciones(Object observaciones) {
//        this.observaciones = observaciones;
//    }
//
//    public Date getFechaDolar() {
//        return fechaDolar;
//    }
//
//    public void setFechaDolar(Date fechaDolar) {
//        this.fechaDolar = fechaDolar;
//    }
//
//    public Integer getIdDepositos() {
//        return idDepositos;
//    }
//
//    public void setIdDepositos(Integer idDepositos) {
//        this.idDepositos = idDepositos;
//    }
//
//    public String getSitIVA() {
//        return sitIVA;
//    }
//
//    public void setSitIVA(String sitIVA) {
//        this.sitIVA = sitIVA;
//    }

    public List<FactDetalleResponse> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<FactDetalleResponse> detalle) {
        this.detalle = detalle;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getComprobante() {
        return comprobante;
    }

    public void setComprobante(String comprobante) {
        this.comprobante = comprobante;
    }

    public Date getFechaEmi() {
        return fechaEmi;
    }

    public void setFechaEmi(Date fechaEmi) {
        this.fechaEmi = fechaEmi;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getImputada() {
        return imputada;
    }

    public void setImputada(String imputada) {
        this.imputada = imputada;
    }
    
    
    
    public void agregarDetalles(List<FactDetalleResponse> f) {
        for(FactDetalleResponse fd: f) {
            this.detalle.add(fd);
        }
    }
    
    
    @Override
    public String getClassName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}