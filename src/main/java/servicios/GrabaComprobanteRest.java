package servicios;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import datos.AppCodigo;
import datos.DatosResponse;
import datos.ServicioResponse;
import entidades.Acceso;
import entidades.CteNumerador;
import entidades.CteTipo;
import entidades.Deposito;
import entidades.FactCab;
import entidades.FactDetalle;
import entidades.FactFormaPago;
import entidades.FactImputa;
import entidades.FactPie;
import entidades.FormaPagoDet;
import entidades.Lote;
import entidades.Master;
import entidades.Producto;
import entidades.Produmo;
import entidades.SisMonedas;
import entidades.SisOperacionComprobante;
import entidades.SisTipoModelo;
import entidades.SisTipoOperacion;
import entidades.Usuario;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import persistencia.AccesoFacade;
import persistencia.CteNumeradorFacade;
import persistencia.CteTipoFacade;
import persistencia.DepositoFacade;
import persistencia.FactCabFacade;
import persistencia.FactDetalleFacade;
import persistencia.FactFormaPagoFacade;
import persistencia.FactImputaFacade;
import persistencia.FactPieFacade;
import persistencia.FormaPagoDetFacade;
import persistencia.FormaPagoFacade;
import persistencia.LoteFacade;
import persistencia.MasterFacade;
import persistencia.ProductoFacade;
import persistencia.ProdumoFacade;
import persistencia.SisMonedasFacade;
import persistencia.SisOperacionComprobanteFacade;
import persistencia.SisTipoModeloFacade;
import persistencia.SisTipoOperacionFacade;
import persistencia.UsuarioFacade;
import utils.Utils;

/**
 *
 * @author FrancoSili
 */
@Stateless
@Path("grabaComprobante")
public class GrabaComprobanteRest {
    @Inject UsuarioFacade usuarioFacade;
    @Inject AccesoFacade accesoFacade;
    @Inject SisMonedasFacade sisMonedasFacade;
    @Inject CteTipoFacade cteTipoFacade;
    @Inject FormaPagoFacade formaPagoFacade;
    @Inject FactCabFacade factCabFacade;
    @Inject DepositoFacade depositoFacade;
    @Inject FactDetalleFacade factDetalleFacade;
    @Inject FactImputaFacade factImputaFacade;
    @Inject FactPieFacade factPieFacade;
    @Inject ProductoFacade productoFacade;
    @Inject ProdumoFacade produmoFacade;
    @Inject LoteFacade loteFacade;
    @Inject FactFormaPagoFacade factFormaPagoFacade;
    @Inject FormaPagoDetFacade formaPagoDetFacade;
    @Inject SisTipoOperacionFacade  sisTipoOperacionFacade;
    @Inject CteNumeradorFacade cteNumeradorFacade;
    @Inject SisTipoModeloFacade sisTipoModeloFacade;
    @Inject MasterFacade masterFacade;
    @Inject SisOperacionComprobanteFacade sisOperacionComprobanteFacade;
          
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setComprobante(
    @HeaderParam ("token") String token,
    @Context HttpServletRequest request) throws NoSuchAlgorithmException, UnsupportedEncodingException {       
        ServicioResponse respuesta = new ServicioResponse();
        try {
            // Obtengo el body de la request
            JsonObject jsonBody = Utils.getJsonObjectFromRequest(request);
            
            //Obtengo los atributos del body
            //Datos de FactCab
            Integer idCteTipo = (Integer) Utils.getKeyFromJsonObject("idCteTipo", jsonBody, "Integer");
            String letra = (String) Utils.getKeyFromJsonObject("letra", jsonBody, "String");
            BigDecimal numero = (BigDecimal) Utils.getKeyFromJsonObject("numero", jsonBody, "BigDecimal");
            Date fechaEmision = (Date) Utils.getKeyFromJsonObject("fechaEmision", jsonBody, "Date");
            Date fechaVencimiento = (Date) Utils.getKeyFromJsonObject("fechaVencimiento", jsonBody, "Date");
            Date fechaConta = (Date) Utils.getKeyFromJsonObject("fechaConta", jsonBody, "Date");
            String cai = (String) Utils.getKeyFromJsonObject("cai", jsonBody, "String");
            Date caiVto = (Date) Utils.getKeyFromJsonObject("caiVto", jsonBody, "Date");
            String codBarra =(String) Utils.getKeyFromJsonObject("codBarra", jsonBody, "String");
            Integer idPadron = (Integer) Utils.getKeyFromJsonObject("idPadron", jsonBody, "Integer");            
            String productoCanje = (String) Utils.getKeyFromJsonObject("productoCanje", jsonBody, "String");
            BigDecimal precioReferenciaCanje = (BigDecimal) Utils.getKeyFromJsonObject("precioReferenciaCanje", jsonBody, "BigDecimal");
            BigDecimal interesCanje = (BigDecimal) Utils.getKeyFromJsonObject("interesCanje", jsonBody, "BigDecimal");
            Integer idMoneda = (Integer) Utils.getKeyFromJsonObject("idMoneda", jsonBody, "Integer");
            String nombre = (String) Utils.getKeyFromJsonObject("nombre", jsonBody, "String");
            String cuit = (String) Utils.getKeyFromJsonObject("cuit", jsonBody, "String");
            String sisSitIva = (String) Utils.getKeyFromJsonObject("sisSitIva", jsonBody, "String");
            String codigoPostal = (String) Utils.getKeyFromJsonObject("codigoPostal", jsonBody, "String");
            String listaPrecio = (String) Utils.getKeyFromJsonObject("listaPrecio", jsonBody, "String");            
            BigDecimal cotDolar = (BigDecimal) Utils.getKeyFromJsonObject("cotDolar", jsonBody, "BigDecimal");
            Date fechaDolar = (Date) Utils.getKeyFromJsonObject("fechaDolar", jsonBody, "Date");
            String observaciones = (String) Utils.getKeyFromJsonObject("observaciones", jsonBody, "String");
            Integer idSisTipoOperacion = (Integer) Utils.getKeyFromJsonObject("idSisTipoOperacion", jsonBody, "Integer");           
            Integer idFactCab = (Integer) Utils.getKeyFromJsonObject("idFactCab", jsonBody, "Integer");
            Integer idNumero = (Integer) Utils.getKeyFromJsonObject("idNumero", jsonBody, "Integer");
            Integer idVendedor = (Integer) Utils.getKeyFromJsonObject("idVendedor", jsonBody, "Integer");
            Integer codigoAfip = (Integer) Utils.getKeyFromJsonObject("codigoAfip", jsonBody, "Integer");
            Integer idSisOperacionComprobante = (Integer) Utils.getKeyFromJsonObject("idSisOperacionComprobante", jsonBody, "Integer");
            
            //Booleanos para ver que se guarda y que no.
            boolean factCabecera = (Boolean) Utils.getKeyFromJsonObject("factCabecera", jsonBody, "boolean");
            boolean factDet = (Boolean) Utils.getKeyFromJsonObject("factDet", jsonBody, "boolean");          
            boolean factImputa = (Boolean) Utils.getKeyFromJsonObject("factImputa", jsonBody, "boolean");
            boolean factPie = (Boolean) Utils.getKeyFromJsonObject("factPie", jsonBody, "boolean");
            boolean produmo = (Boolean) Utils.getKeyFromJsonObject("produmo", jsonBody, "boolean");
            boolean factFormaPago = (Boolean) Utils.getKeyFromJsonObject("factFormaPago", jsonBody, "boolean");
            boolean lote = (Boolean) Utils.getKeyFromJsonObject("lote", jsonBody, "boolean");
            boolean grabaFactura = (Boolean) Utils.getKeyFromJsonObject("grabaFactura", jsonBody, "boolean");
            
            //Datos de la factura relacionada a un remito
            Integer tipoFact = (Integer) Utils.getKeyFromJsonObject("tipoFact", jsonBody, "Integer");
            String letraFact = (String) Utils.getKeyFromJsonObject("letraFact", jsonBody, "String");
            BigDecimal numeroFact = (BigDecimal) Utils.getKeyFromJsonObject("numeroFact", jsonBody, "BigDecimal");
            Date fechaVencimientoFact = (Date) Utils.getKeyFromJsonObject("fechaVencimientoFact", jsonBody, "Date");
            Date fechaContaFact = (Date) Utils.getKeyFromJsonObject("fechaContaFact", jsonBody, "Date");
            Integer idNumeroFact = (Integer) Utils.getKeyFromJsonObject("idNumeroFact", jsonBody, "Integer");
            Integer codigoAfipFact = (Integer) Utils.getKeyFromJsonObject("codigoAfipFact", jsonBody, "Integer");
                        
            //Grillas de articulos, subTotales, elementos trazables y formas de pago
            List<JsonElement> grillaArticulos = (List<JsonElement>) Utils.getKeyFromJsonObjectArray("grillaArticulos", jsonBody, "ArrayList");          
            List<JsonElement> grillaSubTotales = (List<JsonElement>) Utils.getKeyFromJsonObjectArray("grillaSubTotales", jsonBody, "ArrayList");
            List<JsonElement> grillaTrazabilidad = (List<JsonElement>) Utils.getKeyFromJsonObjectArray("grillaTrazabilidad", jsonBody, "ArrayList");
            List<JsonElement> grillaFormaPago = (List<JsonElement>) Utils.getKeyFromJsonObjectArray("grillaFormaPago", jsonBody, "ArrayList");
           
            
            //valido que token no sea null
            if(token == null || token.trim().isEmpty()) {
                respuesta.setControl(AppCodigo.ERROR, "Error, token vacio");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }

            //Busco el token
            Acceso userToken = accesoFacade.findByToken(token);

            //valido que Acceso no sea null
            if(userToken == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, Acceso nulo");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }

            //Busco el usuario
            Usuario user = usuarioFacade.getByToken(userToken);

            //valido que el Usuario no sea null
            if(user == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, Usuario nulo");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }

            //valido vencimiento token
            if(!accesoFacade.validarToken(userToken, user)) {
                respuesta.setControl(AppCodigo.ERROR, "Credenciales incorrectas");
                return Response.status(Response.Status.UNAUTHORIZED).entity(respuesta.toJson()).build();
            }

            //Me fijo que  los campos que tienen el atributo NotNull no sean nulos
            if(idCteTipo == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, tipo de comprobante nulo");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            if(letra == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, letra nula");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            if(numero == null ) {
                respuesta.setControl(AppCodigo.ERROR, "Error, numero nulo");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            if(fechaEmision == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, fecha emision nula");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            if(fechaVencimiento == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, fecha vencimiento nula");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            if(idPadron == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, padron nulo");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            if(productoCanje == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, producto canje nulo");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            if(precioReferenciaCanje == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, precio referencia canje nulo");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            } 
            if(interesCanje == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, interes canje nulo");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            } 
            if(idMoneda == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, moneda en nulo");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            if(nombre == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, nombre nulo");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            if(cuit == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, cuit nulo");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            } 
            if (sisSitIva == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, sisSitIva nulo");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            if(codigoPostal == null){
                respuesta.setControl(AppCodigo.ERROR, "Error, codigo postal nulo");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            } 
            if (cotDolar== null){
                respuesta.setControl(AppCodigo.ERROR, "Error, cot dolar nulo");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            if(idSisTipoOperacion == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, sisTipoOperacion nulo");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            if(codigoAfip == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, codigo Afip nulo");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }            
            
            //----------Metodos de busquedas de clases por id----------//
            
            CteTipo cteTipo = cteTipoFacade.find(idCteTipo);
            //Pregunto si existe el CteTipo
            if(cteTipo == null) {
                respuesta.setControl(AppCodigo.ERROR, "No existe el tipo de Comprobante");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
                       
            SisMonedas sisMonedas = sisMonedasFacade.find(idMoneda);
            //Pregunto si existe SisMonedas
            if(sisMonedas == null) {
                respuesta.setControl(AppCodigo.ERROR, "No existe la Moneda");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            SisTipoOperacion sisTipoOperacion = sisTipoOperacionFacade.find(idSisTipoOperacion);
            //Pregunto si existe sisTipoOperacion
            if(sisTipoOperacion == null) {
                respuesta.setControl(AppCodigo.ERROR, "No existe el tipo de operacion");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            //Pregunto si la grilla de articulos no esta vacia
            if(grillaArticulos == null) {
                respuesta.setControl(AppCodigo.ERROR, "Lista de Articulos Vacia");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            //Hago la sumatoria para comprobar que pendientes sea distinto de cero para continuar
            BigDecimal sumatoriaPendientes = new BigDecimal(0);
            for(JsonElement j : grillaArticulos) {
                BigDecimal pendiente = (BigDecimal) Utils.getKeyFromJsonObject("pendiente", j.getAsJsonObject(), "BigDecimal");
                sumatoriaPendientes = sumatoriaPendientes.add(pendiente);
            }
            
            //Pregunto si la sumatoria de pendientes es igual a 0
            if(sumatoriaPendientes.compareTo(BigDecimal.ZERO) == 0) {
                respuesta.setControl(AppCodigo.ERROR, "La sumatoria de Pendientes es igual a 0");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            //Valido que la fecha de emision no sea menor a la de un comprobante con el mismo tipo dado de alta.
            List<FactCab> posteriores = factCabFacade.getByFechaEmpresaComp(fechaEmision,cteTipo,user.getIdPerfil().getIdSucursal().getIdEmpresa());
            if(posteriores != null && !posteriores.isEmpty() && posteriores.size() > 0) {
                if(posteriores.size() > 1) {
                    Collections.sort(posteriores, (o1, o2) -> o1.getFechaEmision().compareTo(o2.getFechaEmision()));
                }
                SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yy");
                respuesta.setControl(AppCodigo.ERROR, "Error al dar de alta el Comprobante, la fecha debe ser mayor a: "  + formateador.format(posteriores.get(posteriores.size()-1).getFechaEmision()));
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            FactCab factCab = new FactCab();
            //Pregunto si se guarda una cabecera
            if(factCabecera) {               
                factCab.setCai(cai);
                factCab.setCaiVto(caiVto);
                factCab.setCodBarra(codBarra);
                factCab.setCodigoPostal(codigoPostal);
                factCab.setCotDolar(cotDolar);
                factCab.setCuit(cuit);
                factCab.setFechaConta(fechaConta);
                factCab.setFechaDolar(fechaDolar);
                factCab.setFechaEmision(fechaEmision);
                factCab.setFechaVto(caiVto);
                factCab.setIdCteTipo(cteTipo);
                factCab.setIdListaPrecios(listaPrecio);
                factCab.setIdPadron(idPadron);
                factCab.setIdProductoCanje(productoCanje);
                factCab.setIdmoneda(sisMonedas);
                factCab.setInteresCanje(interesCanje);
                factCab.setLetra(letra);
                factCab.setNombre(nombre);
                factCab.setNumero(numero.longValue());
                factCab.setObservaciones(observaciones);
                factCab.setPrecioReferenciaCanje(precioReferenciaCanje);
                factCab.setSitIVA(sisSitIva);
                factCab.setIdSisTipoOperacion(sisTipoOperacion);
                factCab.setIdVendedor(idVendedor);
                factCab.setCodigoAfip(codigoAfip);
            } else {
                if(idFactCab == null) {
                    respuesta.setControl(AppCodigo.ERROR, "El parametro de cabecera no puede ser nulo");
                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                }
                //busco fact cab
                factCab = factCabFacade.find(idFactCab);
                if(factCab == null) {
                    respuesta.setControl(AppCodigo.ERROR, "No existe Fact Cab");
                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                }
            }
                //Pregunto si guarda Detalles
                if(factDet) {
                    //Armo la listas de Fact par luego parsearlas todas juntas
                    List<FactDetalle> listaDetalles = new ArrayList<>();
                    List<FactImputa> listaImputa = new ArrayList<>();
                    List<Produmo> listaProdumo = new ArrayList<>();
                    List<FactPie> listaPie = new ArrayList<>();
                    List<Lote> listaLotes = new ArrayList<>();
                    List<FactFormaPago> listaFormaPago = new ArrayList<>();
                    //Contador para factDetalle
                    int item = 0;
                    //Recorro el array de grillaArticulos y creo facDetalle para cada articulo
                    for(JsonElement j : grillaArticulos) {
                        //Obtengo los atributos del body
                        Integer idProducto = (Integer) Utils.getKeyFromJsonObject("idProducto", j.getAsJsonObject(), "Integer");  
                        String articulo = (String) Utils.getKeyFromJsonObject("articulo", j.getAsJsonObject(), "String");
                        BigDecimal pendiente = (BigDecimal) Utils.getKeyFromJsonObject("pendiente", j.getAsJsonObject(), "BigDecimal");
                        BigDecimal precio = (BigDecimal) Utils.getKeyFromJsonObject("precio", j.getAsJsonObject(), "BigDecimal");
                        BigDecimal porCalc = (BigDecimal) Utils.getKeyFromJsonObject("porCalc", j.getAsJsonObject(), "BigDecimal");
                        String descuento = (String) Utils.getKeyFromJsonObject("descuento", j.getAsJsonObject(), "String");
                        BigDecimal ivaPorc = (BigDecimal) Utils.getKeyFromJsonObject("ivaPorc", j.getAsJsonObject(), "BigDecimal");
                        Integer cantidadBulto = (Integer) Utils.getKeyFromJsonObject("cantidadBulto", j.getAsJsonObject(), "Integer");
                        String despacho = (String) Utils.getKeyFromJsonObject("despacho", j.getAsJsonObject(), "String");
                        Boolean trazable = (Boolean) Utils.getKeyFromJsonObject("trazable", j.getAsJsonObject(), "boolean");
                        Integer idDeposito = (Integer) Utils.getKeyFromJsonObject("idDeposito", j.getAsJsonObject(), "Integer");
                        String observacionDetalle = (String) Utils.getKeyFromJsonObject("observacionDetalle", j.getAsJsonObject(), "String");
                        String imputacion = (String) Utils.getKeyFromJsonObject("imputacion", j.getAsJsonObject(), "String");
                        Integer idFactDetalleImputa = (Integer) Utils.getKeyFromJsonObject("idFactDetalleImputa", j.getAsJsonObject(), "Integer");
                        BigDecimal importe = (BigDecimal) Utils.getKeyFromJsonObject("importe", j.getAsJsonObject(), "BigDecimal");                       

                        //Pregunto por los campos que son NOTNULL
                        if(idProducto == null || articulo == null || pendiente == null || precio == null || porCalc == null ||
                           ivaPorc == null || cantidadBulto == null || despacho== null || trazable== null || idDeposito== null ||
                           observacionDetalle  == null) {
                            respuesta.setControl(AppCodigo.ERROR, "Error al cargar detalles, algun campo esta vacio");
                            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                        }
                        

                        //Busco el deposito por id, si no encuentro alguno desarmo la transaccion.
                        Deposito deposito = depositoFacade.find(idDeposito);
                        if(deposito == null) {
                            respuesta.setControl(AppCodigo.ERROR, "Error al cargar detalles, el deposito con id " + idDeposito + " no existe");
                            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                        }

                        //Busco el producto 
                        Producto producto = productoFacade.find(idProducto);
                        if(producto == null) {
                            respuesta.setControl(AppCodigo.ERROR, "Error al cargar detalles, el producto con id " + idProducto + " no existe");
                            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                        }
                        
                        //Si la cantidad es igual a 0 no guarda ese articulo
                        if(pendiente.compareTo(BigDecimal.ZERO) == 0) {
                            continue;
                        }
                        
                        if(idSisOperacionComprobante == null) {
                            respuesta.setControl(AppCodigo.ERROR, "No se pudo dar de alta el pie de la factura, Comprobante no encontrado");
                            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                        }
                            
                        SisOperacionComprobante sisOperacionComprobante = sisOperacionComprobanteFacade.find(idSisOperacionComprobante);
                        if(sisOperacionComprobante == null) {
                            respuesta.setControl(AppCodigo.ERROR, "No se pudo dar de alta el pie de la factura, Comprobante no encontrado");
                            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                        }

                        if(!sisOperacionComprobante.getIncluyeNeto()) {
                            importe = BigDecimal.ZERO;
                        }
                        
                        //Creo el factDetalle nuevo y seteo los valores
                        FactDetalle factDetalle = new FactDetalle();
                        factDetalle.setDetalle(articulo);
                        factDetalle.setCantBultos(cantidadBulto);
                        factDetalle.setCantidad(pendiente);
                        factDetalle.setDescuento(descuento);
                        factDetalle.setDespacho(despacho);
                        factDetalle.setIdDepositos(deposito);
                        factDetalle.setIdFactCab(factCab);
                        factDetalle.setIdProducto(idProducto);
                        factDetalle.setImputacion(imputacion);
                        factDetalle.setItem(item);
                        factDetalle.setIvaPorc(ivaPorc);
                        factDetalle.setObservaciones(observacionDetalle);
                        factDetalle.setPorcCalc(porCalc);
                        factDetalle.setPrecio(precio);
                        factDetalle.setTrazable(trazable);
                        factDetalle.setImporte(importe);
                        factDetalle.setCodProducto(producto.getCodProducto());
                        listaDetalles.add(factDetalle);
                        
                        //Empiezo la transaccion para la grabacion de FactImputa
                        if(factImputa && idFactDetalleImputa != null) {  
                            FactDetalle imputa = factDetalleFacade.find(idFactDetalleImputa);
                            if(imputa == null) {
                                respuesta.setControl(AppCodigo.ERROR, "Error al cargar Imputacion, la factura con id " + idFactDetalleImputa + " no existe");
                                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                            }
                            FactImputa facturaImputa = new FactImputa();
                            facturaImputa.setCantidadImputada(pendiente);
                            facturaImputa.setIdFactDetalle(imputa);
                            facturaImputa.setIdFactDetalleImputa(factDetalle);
                            facturaImputa.setImporteImputado(pendiente.multiply(porCalc).multiply(precio));
                            facturaImputa.setMasAsiento(0);
                            facturaImputa.setMasAsientoImputado(0);
                            listaImputa.add(facturaImputa);
                        }
                        
                        //Pregunto si se graba produmo y empiezo con la transaccion
                        if(produmo && (cteTipo.getIdSisComprobante().getStock().equals(1) || cteTipo.getIdSisComprobante().getStock().equals(2))) {
                            Produmo prod = new Produmo();
                            if(cteTipo.getSurenu().equals("D")){
                                prod.setCantidad(pendiente.negate());
                            } else {
                                prod.setCantidad(pendiente);
                            }   
                            prod.setDetalle(articulo);
                            prod.setIdCteTipo(cteTipo);
                            prod.setIdDepositos(deposito);
                            prod.setIdFactDetalle(factDetalle.getIdFactDetalle());
                            prod.setIdProductos(producto);
                            prod.setItem(item);
                            
                            CteNumerador cteNumerador = null;
                            if(idNumero != null) {
                                cteNumerador = cteNumeradorFacade.find(idNumero);
                                if(cteNumerador == null) {
                                    respuesta.setControl(AppCodigo.ERROR, "Error al cargar la factura, no existe el numero de comprobante");
                                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                                }
                                String ptoVenta = cteNumerador.getIdPtoVenta().getPtoVenta().toString();
                                String numeroVentaFormat = String.format("%08d",cteNumerador.getNumerador());
                                String concatenado = ptoVenta.concat(numeroVentaFormat);
                                prod.setNumero(Long.parseLong(concatenado,10));
                            } else {
                                prod.setNumero(numero.longValue());
                            }                                                       
                            prod.setFecha(fechaEmision);
                            prod.setStock(cteTipo.getIdSisComprobante().getStock());
                            //prod.setIdLotes(item);
                            listaProdumo.add(prod);
                        }                        
                        //Le sumo uno al contador de items
                        item++;
                    }
                    
                    //Termina el recorrido de la Grilla de articulos y empiezo con la de factFormaPago
                    if(factFormaPago && grillaFormaPago != null) {
                        for(JsonElement je : grillaFormaPago) {
                            Integer plazo = (Integer) Utils.getKeyFromJsonObject("plazo", je.getAsJsonObject(), "Integer"); 
                            BigDecimal interes = (BigDecimal) Utils.getKeyFromJsonObject("interes", je.getAsJsonObject(), "BigDecimal");
                            BigDecimal monto = (BigDecimal) Utils.getKeyFromJsonObject("monto", je.getAsJsonObject(), "BigDecimal");
                            String detalle = (String) Utils.getKeyFromJsonObject("detalle", je.getAsJsonObject(), "String");
                            String observacionesFormaPago = (String) Utils.getKeyFromJsonObject("observaciones", je.getAsJsonObject(), "String");
                            String cuentaContable = (String) Utils.getKeyFromJsonObject("cuentaContable", je.getAsJsonObject(), "String");
                            Integer idFormaPagoDet = (Integer) Utils.getKeyFromJsonObject("idFormaPagoDet", je.getAsJsonObject(), "Integer");
                            //Pregunto si son nulos 
                            if(observacionesFormaPago == null || monto == null || interes == null || plazo == null || idFormaPagoDet == null) {
                                respuesta.setControl(AppCodigo.ERROR, "No se pudo dar de alta Forma de Pago, algun campo de la grilla es nulo");
                                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                            }
                            
                            if(monto.compareTo(BigDecimal.ZERO) == 0) {
                                continue;
                            }
                            
                            FormaPagoDet formaPagoDet = formaPagoDetFacade.getByidFormaPagoDet(idFormaPagoDet);
                            
                            if(formaPagoDet == null) {
                                respuesta.setControl(AppCodigo.ERROR, "No se pudo dar de alta Forma de Pago, la forma de pago no existe");
                                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                            }
                            
                            //Creo FacForma de pago
                            FactFormaPago factFPago = new FactFormaPago();
                            factFPago.setDetalle(observacionesFormaPago);
                            factFPago.setDiasPago(plazo);
                            //creo la fecha con la cantidad de dias de plazo
                            Calendar calendar = Calendar.getInstance();	
                            calendar.setTime(fechaEmision);	
                            calendar.add(Calendar.DAY_OF_YEAR, plazo);
                            //seteo la fecha 
                            factFPago.setFechaPago(calendar.getTime());
                            factFPago.setIdFormaPago(formaPagoDet.getIdFormaPago());
                            factFPago.setImporte(monto);
                            factFPago.setPorcentaje(interes);
                            factFPago.setCtaContable(cuentaContable);
                            factFPago.setIdFactCab(factCab);
                            listaFormaPago.add(factFPago);
                        }                        
                    }
                    
                    //Empiezo con la grilla de SubTotales para grabar FactPie
                    if(factPie) {
                        for(JsonElement je : grillaSubTotales) {
                            String cuenta = (String) Utils.getKeyFromJsonObject("cuenta", je.getAsJsonObject(), "String");
                            String descripcionPie = (String) Utils.getKeyFromJsonObject("descripcionPie", je.getAsJsonObject(), "String");
                            BigDecimal importe = (BigDecimal) Utils.getKeyFromJsonObject("importe", je.getAsJsonObject(), "BigDecimal");
                            BigDecimal totalComprobante = (BigDecimal) Utils.getKeyFromJsonObject("totalComprobante", je.getAsJsonObject(), "BigDecimal");
                            BigDecimal porcentaje = (BigDecimal) Utils.getKeyFromJsonObject("porcentaje", je.getAsJsonObject(), "BigDecimal");
                            Integer idSisTipoModelo = (Integer) Utils.getKeyFromJsonObject("idSisTipoModelo", je.getAsJsonObject(), "Integer");
                            BigDecimal baseImponible = (BigDecimal) Utils.getKeyFromJsonObject("baseImponible", je.getAsJsonObject(), "BigDecimal");
                            //Pregunto por los que no pueden ser Null
                            if(cuenta == null || descripcionPie == null ||importe == null || totalComprobante == null || idSisTipoModelo == null) {
                                respuesta.setControl(AppCodigo.ERROR, "No se pudo dar de alta el pie de la factura, algun campo de la grilla es nulo");
                                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                            }
                            
                            SisTipoModelo sisTipoModelo = sisTipoModeloFacade.find(idSisTipoModelo);                     
                            if(sisTipoModelo == null) {
                                respuesta.setControl(AppCodigo.ERROR, "No se pudo dar de alta el pie de la factura, sisTipoOperacion no encontrado");
                                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                            }
                            
                            if(idSisOperacionComprobante == null) {
                                respuesta.setControl(AppCodigo.ERROR, "No se pudo dar de alta el pie de la factura, Comprobante no encontrado");
                                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                            }
                            
                            SisOperacionComprobante sisOperacionComprobante = sisOperacionComprobanteFacade.find(idSisOperacionComprobante);
                            if(sisOperacionComprobante == null) {
                                respuesta.setControl(AppCodigo.ERROR, "No se pudo dar de alta el pie de la factura, Comprobante no encontrado");
                                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                            }
                            
                            if(!sisOperacionComprobante.getIncluyeIva()) {
                                importe = BigDecimal.ZERO;
                            }
                            //Creo el pie 
                            FactPie facturacionPie = new FactPie();
                            facturacionPie.setCtaContable(cuenta);
                            facturacionPie.setDetalle(descripcionPie);
                            facturacionPie.setImporte(importe);
                            facturacionPie.setIdFactCab(factCab);
                            facturacionPie.setPorcentaje(porcentaje);
                            facturacionPie.setIdSisTipoModelo(sisTipoModelo);
                            facturacionPie.setBaseImponible(baseImponible);
                            listaPie.add(facturacionPie);
                        }
                    }
                    //Termina la griila de sub totales y empieza la de trasabilidad
                    if(lote && cteTipo.getIdSisComprobante().getStock().equals(1) && grillaTrazabilidad != null && cteTipo.getIdSisComprobante().getIdSisModulos().getIdSisModulos() == 1) {
                        int itemTrazabilidad = 0;
                        for(JsonElement gt : grillaTrazabilidad) {
                            //Obtengo los atributos del body
                            String nroLote = (String) Utils.getKeyFromJsonObject("nroLote", gt.getAsJsonObject(), "String");
                            String serie = (String) Utils.getKeyFromJsonObject("serie", gt.getAsJsonObject(), "String");
                            Date fechaElab = (Date) Utils.getKeyFromJsonObject("fechaElab", gt.getAsJsonObject(), "Date");
                            Date fechaVto = (Date) Utils.getKeyFromJsonObject("fechaVto", gt.getAsJsonObject(), "Date");
                            Boolean vigencia = (Boolean) Utils.getKeyFromJsonObject("vigencia", gt.getAsJsonObject(), "boolean");
                            Integer idProducto = (Integer) Utils.getKeyFromJsonObject("idProducto", gt.getAsJsonObject(), "Integer");
                            
                            //Pregunto por los que no pueden ser null
                            if(nroLote == null || serie == null || vigencia == null || idProducto == null) {
                                respuesta.setControl(AppCodigo.ERROR, "No se pudo dar de alta el lote de la factura, algun campo de la grilla es nulo");
                                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                            }
                            
                            //Busco el producto por id
                            Producto prod = productoFacade.find(idProducto);
                            if(prod == null) {
                                respuesta.setControl(AppCodigo.ERROR, "Error al cargar lote, el producto con id " + idProducto + " no existe");
                                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                            }
                            
                            //Armo el lote
                            Lote loteNuevo = new Lote();
                            loteNuevo.setFechaElab(fechaElab);
                            loteNuevo.setFechaVto(fechaVto);
                            loteNuevo.setIdEmpresa(user.getIdPerfil().getIdSucursal().getIdEmpresa().getIdEmpresa());
                            loteNuevo.setIdproductos(prod);
                            loteNuevo.setItem(itemTrazabilidad);
                            loteNuevo.setNroLote(nroLote);
                            loteNuevo.setSerie(serie);
                            loteNuevo.setVigencia(vigencia);
                            //Recorro produmo y si es el mismo producto le agreego el lote
                            for(Produmo p : listaProdumo) {
                                if(p.getIdProductos().equals(prod)) {
                                    p.setNroLote(nroLote);
                                }
                            }                            
                            listaLotes.add(loteNuevo);
                            
                            //Le sumo uno al item
                            itemTrazabilidad ++;
                        }
                    }
                    //Me fijo si guarda la factura del remito asociado
                    if(!grabaFactura) {
                        CteNumerador cteNumerador = null;
                        if(idNumero != null) {
                            cteNumerador = cteNumeradorFacade.find(idNumero);
                            if(cteNumerador == null) {
                                respuesta.setControl(AppCodigo.ERROR, "Error al cargar la factura, no existe el numero de comprobante");
                                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                            }
                            String ptoVenta = cteNumerador.getIdPtoVenta().getPtoVenta().toString();
                            String numeroVentaFormat = String.format("%08d",cteNumerador.getNumerador());
                            String concatenado = ptoVenta.concat(numeroVentaFormat);
                            factCab.setNumero(Long.parseLong(concatenado,10));
                        }
                        return this.persistirObjetos(factCab, listaDetalles, listaImputa, listaProdumo, listaPie, listaLotes, listaFormaPago, cteNumerador);
                    } else if(tipoFact != null || letraFact != null || numeroFact != null || fechaVencimientoFact != null || fechaContaFact != null){                                              
                        CteNumerador cteNumerador = null;
                        if(idNumero != null) {
                            cteNumerador = cteNumeradorFacade.find(idNumero);
                            if(cteNumerador == null) {
                                respuesta.setControl(AppCodigo.ERROR, "Error al cargar la factura, no existe el numero de comprobante");
                                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                            }
                            String ptoVenta = cteNumerador.getIdPtoVenta().getPtoVenta().toString();
                            String numeroVentaFormat = String.format("%08d",cteNumerador.getNumerador());
                            String concatenado = ptoVenta.concat(numeroVentaFormat);
                            factCab.setNumero(Long.parseLong(concatenado,10));
                        }
                        
                       //Persisto Primero los objetos del remito
                        this.persistirObjetos(factCab, listaDetalles, listaImputa, listaProdumo, listaPie, listaLotes, listaFormaPago, cteNumerador);
                        
                        //Luego empiezo con los datos de la factura relacionada
                        CteTipo cteTipoFac = cteTipoFacade.find(tipoFact);
                        if(cteTipoFac == null) {
                            respuesta.setControl(AppCodigo.ERROR, "Error al cargar la factura, no existe el tipo de comprobante");
                            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                        }
                        
                        //Creo la nueva FactCab
                        FactCab fc = new FactCab();                        
                        //Busco el numerador del relacionado                        
                        CteNumerador cteNumeradorRel = null;
                        if(idNumeroFact != null) {
                            cteNumeradorRel = cteNumeradorFacade.find(idNumeroFact);
                            if(cteNumeradorRel == null) {
                                respuesta.setControl(AppCodigo.ERROR, "Error al cargar la factura, no existe el numero de comprobante");
                                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                            }
                            String ptoVenta = cteNumeradorRel.getIdPtoVenta().getPtoVenta().toString();
                            String numeroVentaFormat = String.format("%08d",cteNumeradorRel.getNumerador());
                            String concatenado = ptoVenta.concat(numeroVentaFormat);
                            fc.setNumero(Long.parseLong(concatenado,10));
                        } else {
                            fc.setNumero(numeroFact.longValue());
                        }
                        fc.setCodigoAfip(codigoAfipFact);
                        fc.setCai(cai);
                        fc.setCaiVto(caiVto);
                        fc.setCodBarra(codBarra);
                        fc.setCodigoPostal(codigoPostal);
                        fc.setCotDolar(cotDolar);
                        fc.setCuit(cuit);
                        fc.setFechaConta(fechaContaFact);
                        fc.setFechaDolar(fechaDolar);
                        fc.setFechaEmision(fechaEmision);
                        fc.setFechaVto(fechaVencimientoFact);
                        fc.setIdCteTipo(cteTipoFac);
                        fc.setIdListaPrecios(listaPrecio);
                        fc.setIdPadron(idPadron);
                        fc.setIdProductoCanje(productoCanje);
                        fc.setIdmoneda(sisMonedas);
                        fc.setInteresCanje(interesCanje);
                        fc.setLetra(letraFact);
                        fc.setNombre(nombre);
                        fc.setObservaciones(observaciones);
                        fc.setPrecioReferenciaCanje(precioReferenciaCanje);
                        fc.setSitIVA(sisSitIva);
                        fc.setIdSisTipoOperacion(sisTipoOperacion);
                        fc.setIdVendedor(idVendedor);
                        return this.generarFacturaRelacionada(fc, listaDetalles, listaImputa, listaProdumo, listaPie, listaLotes, listaFormaPago, cteNumeradorRel);
                    } else {
                        respuesta.setControl(AppCodigo.ERROR, "No pudo grabar la factura asociada, algun campo no es valido");
                        return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                    }
                } else {
                    respuesta.setControl(AppCodigo.ERROR, "No se graban detalles");
                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                }
        } catch (Exception ex) { 
            respuesta.setControl(AppCodigo.ERROR, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
        }
    }
    
    public Response persistirObjetos(FactCab factCab, List<FactDetalle> factDetalle, List<FactImputa> factImputa, List<Produmo> produmo, List<FactPie> factPie, List<Lote> listaLotes, List<FactFormaPago> factFormaPago, CteNumerador cteNumerador) {
        ServicioResponse respuesta = new ServicioResponse();
        try {
            //Comienzo con la transaccion de FactCab
            boolean transaccion;
            transaccion = factCabFacade.setFactCabNuevo(factCab);
            if(!transaccion) {
                respuesta.setControl(AppCodigo.ERROR, "No se pudo dar de alta la Cabecera, clave primaria repetida");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            //Comienzo con la transaccion de FacDetalle
            if(!factDetalle.isEmpty()) {
                for(FactDetalle d : factDetalle) {
                    boolean transaccion2;
                    transaccion2 = factDetalleFacade.setFactDetalleNuevo(d);
                    //si la trnsaccion fallo devuelvo el mensaje
                    if(!transaccion2) {
                        respuesta.setControl(AppCodigo.ERROR, "No se pudo dar de alta el Detalle con el articulo: " + d.getDetalle());
                        return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                    }
                }
            }
            //Comienzo con la transaccion de FactImputa
            if(!factImputa.isEmpty()) {
                for(FactImputa i : factImputa) {
                   boolean transaccion3;
                    transaccion3 = factImputaFacade.setFactImputaNuevo(i);
                    //si la trnsaccion fallo devuelvo el mensaje
                    if(!transaccion3) {
                        respuesta.setControl(AppCodigo.ERROR, "No se pudo dar de alta la imputacion con el articulo: " + i.getIdFactDetalle().getCodProducto());
                        return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                    }      
                }
            }
            //Comienzo con la transaccion de FactPie
            if(!factPie.isEmpty()) {
                for(FactPie p : factPie) {
                    boolean transaccion4;           
                    transaccion4 = factPieFacade.setFactPieNuevo(p);
                    //si la trnsaccion fallo devuelvo el mensaje
                    if(!transaccion4) {
                        respuesta.setControl(AppCodigo.ERROR, "No se pudo dar de alta el pie de la factura con la cuenta nro:: " + p.getCtaContable());
                        return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                    }         
                }
            }
            if(!produmo.isEmpty()) {
                //Comienzo con la transaccion de produmo
                for(Produmo pr : produmo) {
                    boolean transaccion5;
                    transaccion5 = produmoFacade.setProdumoNuevo(pr);
                    //si la trnsaccion fallo devuelvo el mensaje
                    if(!transaccion5) {
                        respuesta.setControl(AppCodigo.ERROR, "No se pudo dar de alta produmo con el articulo: " + pr.getDetalle());
                        return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                    }
                }
            }
            if(!listaLotes.isEmpty()) {
                //Comienzo con la transaccion de Lotes
                for(Lote l : listaLotes) {
                    boolean transaccion6;
                    transaccion6 = loteFacade.setLoteNuevo(l);
                    //si la trnsaccion fallo devuelvo el mensaje
                    if(!transaccion6) {
                        respuesta.setControl(AppCodigo.ERROR, "No se pudo dar de alta el lote con el articulo: " + l.getIdproductos().getDescripcion());
                        return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                    }
                }
            }
            if(!factFormaPago.isEmpty()) {
                //Comienzo con la transaccion de Lotes
                for(FactFormaPago f : factFormaPago) {
                    boolean transaccion7;
                    transaccion7 = factFormaPagoFacade.setFactFormaPagoNuevo(f);
                    //si la trnsaccion fallo devuelvo el mensaje
                    if(!transaccion7) {
                        respuesta.setControl(AppCodigo.ERROR, "No se pudo dar de alta la forma de pago: " + f.getDetalle());
                        return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                    }
                }
            }           
            if(factCab.getIdCteTipo().getCursoLegal()) {
                this.grabarMaster(factCab,factDetalle,factFormaPago,factPie);
            }
        } catch(Exception ex) {
            respuesta.setControl(AppCodigo.ERROR, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
        } try {
            //Edito produmo para agregarle la el idDetalle y el lote
            if(!produmo.isEmpty() && !factDetalle.isEmpty()) {
                //Comienzo con la transaccion de produmo para agregarle el idFactDetalle
                int i = 0;
                for(Produmo pr : produmo) {
                    boolean transaccion7;
                    pr.setIdFactDetalle(factDetalle.get(i).getIdFactDetalle());                    
                    transaccion7 = produmoFacade.editProdumo(pr);
                    //si la trnsaccion fallo devuelvo el mensaje
                    if(!transaccion7) {
                        respuesta.setControl(AppCodigo.ERROR, "No se pudo dar de alta produmo con el articulo: " + pr.getDetalle());
                        return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                    }
                    i++;
                }
            }
            //Edito el numero si es distinto de null
            if(cteNumerador != null) {
                cteNumerador.setNumerador(cteNumerador.getNumerador()+1);
                cteNumeradorFacade.edit(cteNumerador);
            }
            if(factCab.getIdFactCab() != null) {
                DatosResponse r = new DatosResponse(factCab.getIdFactCab());
                respuesta.setDatos(r);
            }
            respuesta.setControl(AppCodigo.CREADO, "Comprobante creado con exito, con detalles");
            return Response.status(Response.Status.CREATED).entity(respuesta.toJson()).build();
        } catch(Exception ex) {
            respuesta.setControl(AppCodigo.ERROR, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
        } 
    }
    
    public Response generarFacturaRelacionada(FactCab factCab, List<FactDetalle> factDetalle, List<FactImputa> factImputa, List<Produmo> produmo, List<FactPie> factPie, List<Lote> listaLotes, List<FactFormaPago> factFormaPago, CteNumerador cteNumerador) {
        ServicioResponse respuesta = new ServicioResponse();
        try {
            List<FactDetalle> listaDetalles = new ArrayList<>();
            List<FactImputa> listaImputa = new ArrayList<>();
            List<FactPie> listaPie = new ArrayList<>();
            List<FactFormaPago> listaFormaPago = new ArrayList<>();
            
            //Le asigno el nuevo FactCab a la lista de detalles
            for(FactDetalle d : factDetalle) {
                FactDetalle factDet = new FactDetalle();
                factDet.setCantBultos(d.getCantBultos());
                factDet.setCantidad(d.getCantidad());
                factDet.setCodProducto(d.getCodProducto());
                factDet.setDescuento(d.getDescuento());
                factDet.setDespacho(d.getDespacho());
                factDet.setDetalle(d.getDetalle());
                factDet.setIdDepositos(d.getIdDepositos());
                factDet.setIdFactCab(factCab);
                factDet.setIdProducto(d.getIdProducto());
                factDet.setImporte(d.getImporte());
                factDet.setImputacion(d.getImputacion());
                factDet.setItem(d.getItem());
                factDet.setIvaPorc(d.getIvaPorc());
                factDet.setObservaciones(d.getObservaciones());
                factDet.setPorcCalc(d.getPorcCalc());
                factDet.setPrecio(d.getPrecio());
                factDet.setTrazable(d.getTrazable());
                listaDetalles.add(factDet);
            }
            
            //Le asigno el remito y el nuevo factCab en imputa
            if(!factImputa.isEmpty()) {
                for(FactImputa l : factImputa) {
                    FactImputa factImp = new FactImputa();
                    factImp.setCantidadImputada(l.getCantidadImputada());                   
                    for(FactDetalle d : listaDetalles) {
                        if(d.getCodProducto().equals(l.getIdFactDetalle().getCodProducto())) {   
                            //Factura
                            factImp.setIdFactDetalleImputa(d);
                        } else {
                            continue;
                        }
                    }
                    //Remito
                    factImp.setIdFactDetalle(l.getIdFactDetalle());                   
                    factImp.setImporteImputado(l.getImporteImputado());
                    factImp.setMasAsiento(l.getMasAsiento());
                    factImp.setMasAsientoImputado(l.getMasAsientoImputado());
                    listaImputa.add(factImp);
                }
            } else {
                for(FactDetalle d : factDetalle) {
                    FactImputa facturaImputa = new FactImputa();
                    facturaImputa.setCantidadImputada(d.getCantidad());
                    for(FactDetalle det : listaDetalles) {
                        if(d.getCodProducto().equals(det.getCodProducto())) {   
                            //Factura
                            facturaImputa.setIdFactDetalleImputa(det);
                        } else {
                            continue;
                        }
                    }
                    facturaImputa.setIdFactDetalle(d);
                    facturaImputa.setImporteImputado(d.getCantidad().multiply(d.getPorcCalc()).multiply(d.getPrecio()));
                    facturaImputa.setMasAsiento(0);
                    facturaImputa.setMasAsientoImputado(0);
                    listaImputa.add(facturaImputa);
                }
            }
            //Limpio la coleccion de produmo porque no se guarda
            produmo.clear();
            
            //Guardo el nuevo factCab en factPie
            for(FactPie p : factPie) {
                FactPie faPie = new FactPie();
                faPie.setCtaContable(p.getCtaContable());
                faPie.setDetalle(p.getDetalle());
                faPie.setIdConceptos(p.getIdConceptos());
                faPie.setIdFactCab(factCab);
                faPie.setImporte(p.getImporte());
                faPie.setPorcentaje(p.getPorcentaje());
                faPie.setIdSisTipoModelo(p.getIdSisTipoModelo());
                listaPie.add(faPie);
            }
            
            //Limpio la lista de lotes porque no se guardan
            listaLotes.clear();
            
            //Guardo el nuevo FactCab en la forma de pago
            for(FactFormaPago fp : factFormaPago) {
                FactFormaPago ffp = new FactFormaPago();
                ffp.setCtaContable(fp.getCtaContable());
                ffp.setDetalle(fp.getDetalle());
                ffp.setDiasPago(fp.getDiasPago());
                ffp.setFechaPago(fp.getFechaPago());
                ffp.setIdFactCab(factCab);
                ffp.setIdFormaPago(fp.getIdFormaPago());
                ffp.setImporte(fp.getImporte());
                ffp.setPorcentaje(fp.getPorcentaje());
                listaFormaPago.add(ffp);
            }
            
            //Persisto los objetos y devuelvo la respuesta          
            return this.persistirObjetos(factCab, listaDetalles, listaImputa, produmo, listaPie, listaLotes, listaFormaPago, cteNumerador);
        } catch(Exception ex) {
            respuesta.setControl(AppCodigo.ERROR, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
        }
    }
    
    public Response grabarMaster(FactCab factCab, List<FactDetalle> factDetalle, List<FactFormaPago> factFormaPago, List<FactPie> factPie) {
        ServicioResponse respuesta = new ServicioResponse();                 
        //Seteo la fecha de hoy
        Calendar calendario = new GregorianCalendar();           
        Date fechaHoy = calendario.getTime();
        
        //Busco el proximo numero del asiento
        Integer masAsiento = 0;
        Integer idMaster = masterFacade.findProximoByEmpresa(factCab.getIdCteTipo().getIdEmpresa());
        if(idMaster != null) {
            Master master = masterFacade.find(idMaster);
            if(master != null) {
                masAsiento = master.getMAsiento();
            }
        }    
        masAsiento = masAsiento + 1;
        
        //Contadores para los pases
        Integer paseDetalle = 1;
        
        //Me fijo si es debe o haber
        BigDecimal signo = new BigDecimal(1);
        if(factCab.getIdCteTipo().getSurenu().equals("D")) {
            signo = signo.negate();
        }
        try{
            for(FactDetalle det : factDetalle) {
                Master masterDetalle = new Master();
                masterDetalle.setCodigoLibro(Short.valueOf(Integer.toString(factCab.getIdCteTipo().getIdSisComprobante().getIdSisModulos().getIdSisModulos())));
                masterDetalle.setCotizacion(factCab.getCotDolar());
                masterDetalle.setFechayhora(fechaHoy);
                masterDetalle.setIdEmpresa(factCab.getIdCteTipo().getIdEmpresa().getIdEmpresa());
                masterDetalle.setMAsiento(masAsiento);
                masterDetalle.setMDetalle(det.getDetalle());
                masterDetalle.setMFechaEmi(factCab.getFechaEmision());
                masterDetalle.setMImporte(det.getImporte().multiply(signo));
                masterDetalle.setMIngreso(fechaHoy);
                masterDetalle.setMPase(Short.valueOf(Integer.toString(paseDetalle)));
                masterDetalle.setMVence(factCab.getFechaVto());
                masterDetalle.setNroComp(factCab.getNumero());
                masterDetalle.setPadronCodigo(factCab.getIdPadron());
                masterDetalle.setPlanCuentas(det.getImputacion());
                masterDetalle.setTipoComp(Short.valueOf(Integer.toString(factCab.getIdCteTipo().getIdCteTipo())));
                
                
                //Parametros que van en 0
                masterDetalle.setAutorizaCodigo(Short.valueOf("0"));                
                masterDetalle.setTipoCompAsoc(Short.valueOf("0"));
                masterDetalle.setConceptoCodigo(Short.valueOf("0"));
                masterDetalle.setCondGan(Short.valueOf("0"));
                masterDetalle.setCondIva(Short.valueOf("0"));
                masterDetalle.setMColumIva(Short.valueOf("0"));
                masterDetalle.setMUnidades(BigDecimal.ZERO);
                masterDetalle.setNroCompAsoc(Long.valueOf("0"));
                masterDetalle.setNroCompPreimp(Long.valueOf("0"));
                masterDetalle.setCodActividad(Long.valueOf("0"));
                masterDetalle.setMMinuta(Long.valueOf("0"));
                masterDetalle.setMCtacte("0");
                masterDetalle.setOperadorCodigo("0");
                masterDetalle.setMAsientoRub(0);
                
                boolean transaccion1; 
                transaccion1 = masterFacade.setMasterNuevo(masterDetalle);
                //si la trnsaccion fallo devuelvo el mensaje
                if(!transaccion1) {
                    respuesta.setControl(AppCodigo.ERROR, "No se pudo dar de alta la master con el detalle: " + det.getDetalle());
                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                }
                
                //Sumo uno al contador de pases
                paseDetalle++;
            }
            for(FactFormaPago fp : factFormaPago) {
                Master masterFormaPago = new Master();
                masterFormaPago.setCodigoLibro(Short.valueOf(Integer.toString(factCab.getIdCteTipo().getIdSisComprobante().getIdSisModulos().getIdSisModulos())));
                masterFormaPago.setCotizacion(factCab.getCotDolar());
                masterFormaPago.setFechayhora(fechaHoy);
                masterFormaPago.setIdEmpresa(factCab.getIdCteTipo().getIdEmpresa().getIdEmpresa());
                masterFormaPago.setMAsiento(masAsiento);
                masterFormaPago.setMDetalle(fp.getDetalle());
                masterFormaPago.setMFechaEmi(factCab.getFechaEmision());
                masterFormaPago.setMImporte(fp.getImporte().multiply(signo).negate());
                masterFormaPago.setMIngreso(fechaHoy);
                masterFormaPago.setMPase(Short.valueOf(Integer.toString(paseDetalle)));
                masterFormaPago.setMVence(factCab.getFechaVto());
                masterFormaPago.setNroComp(factCab.getNumero());
                masterFormaPago.setPadronCodigo(factCab.getIdPadron());
                masterFormaPago.setPlanCuentas(fp.getCtaContable());
                masterFormaPago.setTipoComp(Short.valueOf(Integer.toString(factCab.getIdCteTipo().getIdCteTipo())));
                if(fp.getIdFormaPago().getTipo().getIdSisFormaPago().equals(2) ||
                   fp.getIdFormaPago().getTipo().getIdSisFormaPago().equals(3) ||
                   fp.getIdFormaPago().getTipo().getIdSisFormaPago().equals(4)) {
                    masterFormaPago.setMCtacte("1");
                } else {
                    masterFormaPago.setMCtacte("0");
                }
                
                //Parametros que van en 0
                masterFormaPago.setAutorizaCodigo(Short.valueOf("0"));                
                masterFormaPago.setTipoCompAsoc(Short.valueOf("0"));
                masterFormaPago.setConceptoCodigo(Short.valueOf("0"));
                masterFormaPago.setCondGan(Short.valueOf("0"));
                masterFormaPago.setCondIva(Short.valueOf("0"));
                masterFormaPago.setMColumIva(Short.valueOf("0"));
                masterFormaPago.setMUnidades(BigDecimal.ZERO);
                masterFormaPago.setNroCompAsoc(Long.valueOf("0"));
                masterFormaPago.setNroCompPreimp(Long.valueOf("0"));
                masterFormaPago.setCodActividad(Long.valueOf("0"));
                masterFormaPago.setMMinuta(Long.valueOf("0"));
                masterFormaPago.setOperadorCodigo("0");
                masterFormaPago.setMAsientoRub(0);
                
                boolean transaccion2; 
                transaccion2 = masterFacade.setMasterNuevo(masterFormaPago);
                //si la trnsaccion fallo devuelvo el mensaje
                if(!transaccion2) {
                    respuesta.setControl(AppCodigo.ERROR, "No se pudo dar de alta la master con la forma de pago: " + fp.getDetalle());
                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                }
                
                //Sumo uno al contador de pases
                paseDetalle++;
            }
            
            for(FactPie fi : factPie) {
                if(fi.getImporte().equals(BigDecimal.ZERO)) {
                    continue;
                }
                Master masterImputa = new Master();
                masterImputa.setCodigoLibro(Short.valueOf(Integer.toString(factCab.getIdCteTipo().getIdSisComprobante().getIdSisModulos().getIdSisModulos())));
                masterImputa.setCotizacion(factCab.getCotDolar());
                masterImputa.setFechayhora(fechaHoy);
                masterImputa.setIdEmpresa(factCab.getIdCteTipo().getIdEmpresa().getIdEmpresa());
                masterImputa.setMAsiento(masAsiento);
                masterImputa.setMDetalle(fi.getDetalle());
                masterImputa.setMFechaEmi(factCab.getFechaEmision());
                masterImputa.setMImporte(fi.getImporte().multiply(signo));
                masterImputa.setMIngreso(fechaHoy);
                masterImputa.setMPase(Short.valueOf(Integer.toString(paseDetalle)));
                masterImputa.setMVence(factCab.getFechaVto());
                masterImputa.setNroComp(factCab.getNumero());
                masterImputa.setPadronCodigo(factCab.getIdPadron());
                masterImputa.setPlanCuentas(fi.getCtaContable());
                masterImputa.setTipoComp(Short.valueOf(Integer.toString(factCab.getIdCteTipo().getIdCteTipo())));
                
                //Parametros que van en 0
                masterImputa.setAutorizaCodigo(Short.valueOf("0"));                
                masterImputa.setTipoCompAsoc(Short.valueOf("0"));
                masterImputa.setConceptoCodigo(Short.valueOf("0"));
                masterImputa.setCondGan(Short.valueOf("0"));
                masterImputa.setCondIva(Short.valueOf("0"));
                masterImputa.setMColumIva(Short.valueOf("0"));
                masterImputa.setMUnidades(BigDecimal.ZERO);
                masterImputa.setNroCompAsoc(Long.valueOf("0"));
                masterImputa.setNroCompPreimp(Long.valueOf("0"));
                masterImputa.setCodActividad(Long.valueOf("0"));
                masterImputa.setMMinuta(Long.valueOf("0"));
                masterImputa.setMCtacte("0");
                masterImputa.setOperadorCodigo("0");
                masterImputa.setMAsientoRub(0);
                
                boolean transaccion3; 
                transaccion3 = masterFacade.setMasterNuevo(masterImputa);
                //si la trnsaccion fallo devuelvo el mensaje
                if(!transaccion3) {
                    respuesta.setControl(AppCodigo.ERROR, "No se pudo dar de alta la master con la imputacion: " + fi.getDetalle());
                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                }
                
                //Sumo uno al contador de pases
                paseDetalle++;
            }
        } catch(Exception ex) {
            respuesta.setControl(AppCodigo.ERROR, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
        }
        respuesta.setControl(AppCodigo.CREADO, "Comprobante creado con exito, con detalles");
        return Response.status(Response.Status.CREATED).entity(respuesta.toJson()).build();
    }
}
