package servicios;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import datos.AppCodigo;
import datos.ServicioResponse;
import entidades.Acceso;
import entidades.CteTipo;
import entidades.Deposito;
import entidades.FactCab;
import entidades.FactDetalle;
import entidades.FactFormaPago;
import entidades.FactImputa;
import entidades.FactPie;
import entidades.FormaPago;
import entidades.Lote;
import entidades.Producto;
import entidades.Produmo;
import entidades.SisMonedas;
import entidades.Usuario;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import persistencia.CteTipoFacade;
import persistencia.DepositoFacade;
import persistencia.FactCabFacade;
import persistencia.FactDetalleFacade;
import persistencia.FactFormaPagoFacade;
import persistencia.FactImputaFacade;
import persistencia.FactPieFacade;
import persistencia.FormaPagoFacade;
import persistencia.LoteFacade;
import persistencia.ProductoFacade;
import persistencia.ProdumoFacade;
import persistencia.SisMonedasFacade;
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
            
            // Obtengo los atributos del body
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
            Integer idFormaPago = (Integer) Utils.getKeyFromJsonObject("idFormaPago", jsonBody, "Integer");
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
            Integer idModeloCab = (Integer) Utils.getKeyFromJsonObject("idModeloCab", jsonBody, "Integer");           
            Integer idFactCab = (Integer) Utils.getKeyFromJsonObject("idFactCab", jsonBody, "Integer");
            boolean factCabecera = (Boolean) Utils.getKeyFromJsonObject("factCabecera", jsonBody, "boolean");
            boolean factDet = (Boolean) Utils.getKeyFromJsonObject("factDet", jsonBody, "boolean");          
            boolean factImputa = (Boolean) Utils.getKeyFromJsonObject("factImputa", jsonBody, "boolean");
            boolean factPie = (Boolean) Utils.getKeyFromJsonObject("factPie", jsonBody, "boolean");
            boolean produmo = (Boolean) Utils.getKeyFromJsonObject("produmo", jsonBody, "boolean");
            boolean factFormaPago = (Boolean) Utils.getKeyFromJsonObject("factFormaPago", jsonBody, "boolean");
            boolean lote = (Boolean) Utils.getKeyFromJsonObject("lote", jsonBody, "boolean");
            List<JsonElement> grillaArticulos = (List<JsonElement>) Utils.getKeyFromJsonObjectArray("grillaArticulos", jsonBody, "ArrayList");          
            List<JsonElement> grillaSubTotales = (List<JsonElement>) Utils.getKeyFromJsonObjectArray("grillaSubTotales", jsonBody, "ArrayList");
            List<JsonElement> grillaTrazabilidad = (List<JsonElement>) Utils.getKeyFromJsonObjectArray("grillaTrazabilidad", jsonBody, "ArrayList");
            List<JsonElement> grillaFormaPago = (List<JsonElement>) Utils.getKeyFromJsonObjectArray("grillaFormaPago", jsonBody, "ArrayList");
            String relComprobante = (String) Utils.getKeyFromJsonObject("relComprobante", jsonBody, "String");
            Integer relPuntoVenta = (Integer) Utils.getKeyFromJsonObject("relPuntoVenta", jsonBody, "Integer");
            Integer relNumero = (Integer) Utils.getKeyFromJsonObject("relNumero", jsonBody, "Integer");
            //Integer idDeposito = (Integer) Utils.getKeyFromJsonObject("idDeposito", jsonBody, "Integer");
            //boolean pendiente = (Boolean) Utils.getKeyFromJsonObject("pendiente", jsonBody, "boolean");
            
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
            if(idCteTipo == null || letra == null || numero == null || fechaEmision == null || 
               fechaVencimiento == null || idPadron == null || idFormaPago == null || 
               productoCanje == null || precioReferenciaCanje == null || interesCanje == null || 
               idMoneda == null ||nombre == null || cuit == null || sisSitIva == null || 
               codigoPostal == null || cotDolar== null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, algun campo esta en nulo");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            //----------Metodos de busquedas de clases por id----------//
            
            CteTipo cteTipo = cteTipoFacade.find(idCteTipo);
            //Pregunto si existe el CteTipo
            if(cteTipo == null) {
                respuesta.setControl(AppCodigo.ERROR, "No existe el tipo de Comprobante");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            FormaPago formaPago = formaPagoFacade.find(idFormaPago);
            //Pregunto si existe la forma de pago
            if(formaPago == null) {
                respuesta.setControl(AppCodigo.ERROR, "No existe la Forma de Pago");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            SisMonedas sisMonedas = sisMonedasFacade.find(idMoneda);
            //Pregunto si existe SisMonedas
            if(sisMonedas == null) {
                respuesta.setControl(AppCodigo.ERROR, "No existe la Moneda");
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
            if(sumatoriaPendientes.equals(BigDecimal.ZERO)) {
                respuesta.setControl(AppCodigo.ERROR, "La sumatoria de Pendientes es igual a 0");
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
                factCab.setIdFormaPago(formaPago);
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
                factCab.setIdModeloCab(idModeloCab);
                //factCab.setIdDepositos(null);
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
                        Integer idFactCabImputa = (Integer) Utils.getKeyFromJsonObject("idFactCabImputa", j.getAsJsonObject(), "Integer");
                        Integer itemImputada = (Integer) Utils.getKeyFromJsonObject("itemImputada", j.getAsJsonObject(), "Integer");

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
                        listaDetalles.add(factDetalle);
                        
                        //Empiezo la transaccion para la grabacion de FactImputa
                        if(factImputa && idFactCabImputa != null) {  
                            FactCab imputa = factCabFacade.find(idFactCabImputa);
                            if(imputa == null) {
                                respuesta.setControl(AppCodigo.ERROR, "Error al cargar Imputacion, la factura con id " + idFactCabImputa + " no existe");
                                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                            }
                            FactImputa facturaImputa = new FactImputa();
                            facturaImputa.setCantidadImputada(pendiente);
                            facturaImputa.setIdFactCab(imputa.getIdFactCab());
                            facturaImputa.setIdFactCabImputa(factCab);
                            facturaImputa.setIdProducto(idProducto);
                            facturaImputa.setImporteImputado(pendiente.multiply(porCalc).multiply(precio));
                            facturaImputa.setItem(item);
                            facturaImputa.setItemImputada(itemImputada);
                            facturaImputa.setMasAsiento(0);
                            facturaImputa.setMasAsientoImputado(0);
                            listaImputa.add(facturaImputa);
                        }
                        
                        //Pregunto si se graba produmo y empiezo con la transaccion
                        if(produmo) {
                            Produmo prod = new Produmo();
                            prod.setCantidad(pendiente);
                            prod.setDetalle(articulo);
                            prod.setIdCteTipo(cteTipo);
                            prod.setIdDepositos(deposito);
                            prod.setIdFactDetalle(factDetalle.getIdFactDetalle());
                            prod.setIdProductos(producto);
                            prod.setItem(item);
                            prod.setNumero(numero.longValue());
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
                            
                            //Pregunto si son nulos 
                            if(observacionesFormaPago == null || monto == null || interes == null || plazo == null) {
                                respuesta.setControl(AppCodigo.ERROR, "No se pudo dar de alta Forma de Pago, algun campo de la grilla es nulo");
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
                            factFPago.setIdFormaPago(formaPago);
                            factFPago.setImporte(monto);
                            factFPago.setPorcentaje(interes);
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

                            //Pregunto por los que no pueden ser Null
                            if(cuenta == null || descripcionPie == null ||importe == null || totalComprobante == null) {
                                respuesta.setControl(AppCodigo.ERROR, "No se pudo dar de alta el pie de la factura, algun campo de la grilla es nulo");
                                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                            }

                            //Creo el pie 
                            FactPie facturacionPie = new FactPie();
                            facturacionPie.setCtaContable(cuenta);
                            facturacionPie.setDetalle(descripcionPie);
                            facturacionPie.setImporte(importe);
                            facturacionPie.setIdFactCab(factCab);
                            facturacionPie.setPorcentaje(porcentaje);
                            listaPie.add(facturacionPie);
                        }
                    }
                    //Termina la griila de sub totales y empieza la de trasabilidad
                    if(lote) {
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
                    return this.persistirObjetos(factCab, listaDetalles, listaImputa, listaProdumo, listaPie, listaLotes, listaFormaPago);
                } else {
                    respuesta.setControl(AppCodigo.OK, "No se graban detalles");
                    return Response.status(Response.Status.CREATED).entity(respuesta.toJson()).build();
                }
        } catch (Exception ex) { 
            respuesta.setControl(AppCodigo.ERROR, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
        }
    }
    
    public Response persistirObjetos(FactCab factCab, List<FactDetalle> factDetalle, List<FactImputa> factImputa, List<Produmo> produmo, List<FactPie> factPie, List<Lote> listaLotes, List<FactFormaPago> factFormaPago) {
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
                        respuesta.setControl(AppCodigo.ERROR, "No se pudo dar de alta la imputacion con el articulo: " + i.getIdProducto());
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
        } catch(Exception ex) {
            respuesta.setControl(AppCodigo.ERROR, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
        }
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
        respuesta.setControl(AppCodigo.OK, "Comprobante creado con exito, con detalles");
        return Response.status(Response.Status.CREATED).entity(respuesta.toJson()).build();
    }
}
