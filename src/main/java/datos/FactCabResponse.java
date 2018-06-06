package datos;

import entidades.FactCab;
import java.math.BigDecimal;
import java.util.Date;


/**
 *
 * @author FrancoSili
 */
public class FactCabResponse {
    private Integer idFactCab;
    private String letra;
    private long numero;
    private Date fechaEmision;
    private Date fechaVto;
    private Date fechaConta;
    private String cai;
    private Date caiVto;
    private String codBarra;    
    private Integer idPadron;    
    private String nombre;    
    private String cuit;       
    private String codigoPostal;
    private String idListaPrecios;
    private BigDecimal cotDolar;
    private Date fechaDolar;
    private Integer idDepositos;
    private Object observaciones;
    private SisSitIvaResponse sitIVA;
    private CteTipoResponse cteTipo;
    private SisMonedasResponse idmoneda;
    
    FactCabResponse(FactCab f) {
        this.idFactCab = f.getIdFactCab();
        this.letra = f.getLetra();
        this.numero = f.getNumero();
        this.fechaEmision = f.getFechaEmision();
        this.fechaVto = f.getFechaVto();
        this.fechaConta = f.getFechaConta();
        this.cai = f.getCai();
        this.caiVto = f.getCaiVto();
        this.codBarra = f.getCodBarra();
        this.idPadron = f.getIdPadron();
        this.nombre = f.getNombre();
        this.cuit = f.getCuit();
        this.codigoPostal = f.getCodigoPostal();
        this.idListaPrecios = f.getIdListaPrecios();
        this.cotDolar = f.getCotDolar();       
        this.fechaDolar = f.getFechaDolar();
        this.idDepositos = f.getIdDepositos();
        this.observaciones = f.getObservaciones();
        this.sitIVA = new SisSitIvaResponse(f.getSitIVA());
        this.cteTipo = new CteTipoResponse(f.getIdCteTipo());
        this.idmoneda = new SisMonedasResponse(f.getIdmoneda());
    }

    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    public long getNumero() {
        return numero;
    }

    public void setNumero(long numero) {
        this.numero = numero;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaVto() {
        return fechaVto;
    }

    public void setFechaVto(Date fechaVto) {
        this.fechaVto = fechaVto;
    }

    public String getCai() {
        return cai;
    }

    public void setCai(String cai) {
        this.cai = cai;
    }

    public Date getCaiVto() {
        return caiVto;
    }

    public void setCaiVto(Date caiVto) {
        this.caiVto = caiVto;
    }

    public String getCodBarra() {
        return codBarra;
    }

    public void setCodBarra(String codBarra) {
        this.codBarra = codBarra;
    }

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

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getIdListaPrecios() {
        return idListaPrecios;
    }

    public void setIdListaPrecios(String idListaPrecios) {
        this.idListaPrecios = idListaPrecios;
    }

    public CteTipoResponse getCteTipo() {
        return cteTipo;
    }

    public void setCteTipo(CteTipoResponse cteTipo) {
        this.cteTipo = cteTipo;
    }

    public SisMonedasResponse getIdmoneda() {
        return idmoneda;
    }

    public void setIdmoneda(SisMonedasResponse idmoneda) {
        this.idmoneda = idmoneda;
    }
    
    public Integer getIdFactCab() {
        return idFactCab;
    }

    public void setIdFactCab(Integer idFactCab) {
        this.idFactCab = idFactCab;
    }

    public Date getFechaConta() {
        return fechaConta;
    }

    public void setFechaConta(Date fechaConta) {
        this.fechaConta = fechaConta;
    }

    public BigDecimal getCotDolar() {
        return cotDolar;
    }

    public void setCotDolar(BigDecimal cotDolar) {
        this.cotDolar = cotDolar;
    }

    public Object getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(Object observaciones) {
        this.observaciones = observaciones;
    }

    public Date getFechaDolar() {
        return fechaDolar;
    }

    public void setFechaDolar(Date fechaDolar) {
        this.fechaDolar = fechaDolar;
    }

    public Integer getIdDepositos() {
        return idDepositos;
    }

    public void setIdDepositos(Integer idDepositos) {
        this.idDepositos = idDepositos;
    }

    public SisSitIvaResponse getSitIVA() {
        return sitIVA;
    }

    public void setSitIVA(SisSitIvaResponse sitIVA) {
        this.sitIVA = sitIVA;
    }
}