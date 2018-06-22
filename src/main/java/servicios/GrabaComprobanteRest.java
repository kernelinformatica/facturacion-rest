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
import entidades.FactImputa;
import entidades.FormaPago;
import entidades.SisMonedas;
import entidades.Usuario;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
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
import persistencia.FactImputaFacade;
import persistencia.FormaPagoFacade;
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
            String cuit = (String) Utils.getKeyFromJsonObject("nombre", jsonBody, "String");
            String sisSitIva = (String) Utils.getKeyFromJsonObject("sisSitIva", jsonBody, "String");
            String codigoPostal = (String) Utils.getKeyFromJsonObject("nombre", jsonBody, "String");
            String listaPrecio = (String) Utils.getKeyFromJsonObject("listaPrecio", jsonBody, "String");
            //Integer idDeposito = (Integer) Utils.getKeyFromJsonObject("idDeposito", jsonBody, "Integer");
            BigDecimal cotDolar = (BigDecimal) Utils.getKeyFromJsonObject("cotDolar", jsonBody, "BigDecimal");
            Date fechaDolar = (Date) Utils.getKeyFromJsonObject("fechaDolar", jsonBody, "Date");
            String observaciones = (String) Utils.getKeyFromJsonObject("observaciones", jsonBody, "String");
            Integer idModeloCab = (Integer) Utils.getKeyFromJsonObject("idModeloCab", jsonBody, "Integer");
            String relComprobante = (String) Utils.getKeyFromJsonObject("relComprobante", jsonBody, "String");
            Integer relPuntoVenta = (Integer) Utils.getKeyFromJsonObject("relPuntoVenta", jsonBody, "Integer");
            Integer relNumero = (Integer) Utils.getKeyFromJsonObject("relNumero", jsonBody, "Integer");
            //boolean pendiente = (Boolean) Utils.getKeyFromJsonObject("pendiente", jsonBody, "boolean");
            Integer idFactCab = (Integer) Utils.getKeyFromJsonObject("idFactCab", jsonBody, "Integer");
            boolean factCabecera = (Boolean) Utils.getKeyFromJsonObject("factCabecera", jsonBody, "boolean");
            boolean factDet = (Boolean) Utils.getKeyFromJsonObject("factDet", jsonBody, "boolean");
            boolean factFormaPago = (Boolean) Utils.getKeyFromJsonObject("factFormaPago", jsonBody, "boolean");
            boolean factImputa = (Boolean) Utils.getKeyFromJsonObject("factImputa", jsonBody, "boolean");
            boolean factPie = (Boolean) Utils.getKeyFromJsonObject("factPie", jsonBody, "boolean");
            boolean produmo = (Boolean) Utils.getKeyFromJsonObject("produmo", jsonBody, "boolean");
            List<JsonElement> grillaArticulos = (List<JsonElement>) Utils.getKeyFromJsonObjectArray("grillaArticulos", jsonBody, "ArrayList");
            List<JsonElement> grillaTrazabilidad = (List<JsonElement>) Utils.getKeyFromJsonObjectArray("grillaTrazabilidad", jsonBody, "ArrayList");
            List<JsonElement> grillaSubtotales = (List<JsonElement>) Utils.getKeyFromJsonObjectArray("grillaSubtotales", jsonBody, "ArrayList");
            
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
            
            //Metodos de busquedas de clases por id
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
            if(grillaArticulos.isEmpty()) {
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
                //Comienzo con la transaccion de FactCab
                boolean transaccion;
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
                //factCab.setIdDepositos(null);
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
                transaccion = factCabFacade.setFactCabNuevo(factCab);
                if(!transaccion) {
                    respuesta.setControl(AppCodigo.ERROR, "No se pudo dar de alta la Cabecera, clave primaria repetida");
                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                }
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
                //Contador para factDetalle
                int item = 0;
                //Recorro el array de grillaArticulos y creo facDetalle para cada articulo
                for(JsonElement j : grillaArticulos) {
                    boolean transaccion2;
                    //Creo el factDetalle nuevo
                    FactDetalle factDetalle = new FactDetalle();
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
                        return Response.status(Response.Status.CREATED).entity(respuesta.toJson()).build();
                    }

                    //Busco el deposito por id, si no encuentro alguno desarmo la transaccion.
                    Deposito deposito = depositoFacade.find(idDeposito);
                    if(deposito == null) {
                        factCabFacade.deleteFactCab(factCab);
                        respuesta.setControl(AppCodigo.ERROR, "Error al cargar detalles, el deposito con id " + idDeposito + " no existe");
                        return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                    }
                    //Seteo los valores a factDetalle
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
                    transaccion2 = factDetalleFacade.setFactDetalleNuevo(factDetalle);
                    //si la trnsaccion fallo, elimino el factCab.
                    if(!transaccion2) {
                        factCabFacade.deleteFactCab(factCab);
                        respuesta.setControl(AppCodigo.ERROR, "No se pudo dar de alta el Detalle con el articulo: " + articulo);
                        return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                    }
                    //Empiezo la transaccion para la grabacion de FactImputa
                    if(factImputa && idFactCabImputa != null) {
                        boolean transaccion3; 
                        FactCab imputa = factCabFacade.find(idFactCabImputa);
                        if(imputa == null) {
                            respuesta.setControl(AppCodigo.ERROR, "Error al cargar Imputacion, la factura con id " + idFactCabImputa + " no existe");
                            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                        }
                        FactImputa facturaImputa = new FactImputa();
                        facturaImputa.setCantidadImputada(pendiente);
                        facturaImputa.setIdFactCab(imputa);
                        facturaImputa.setIdFactCabImputa(factCab);
                        facturaImputa.setIdProducto(idProducto);
                        facturaImputa.setImporteImputado(pendiente.multiply(porCalc).multiply(precio));
                        facturaImputa.setItem(item);
                        facturaImputa.setItemImputada(itemImputada);
                        facturaImputa.setMasAsiento(0);
                        facturaImputa.setMasAsientoImputado(0);
                        transaccion3 = factImputaFacade.setFactImputaNuevo(facturaImputa);
                        //si la trnsaccion fallo, elimino el factCab.
                        if(!transaccion3) {
                            factCabFacade.deleteFactCab(factCab);
                            respuesta.setControl(AppCodigo.ERROR, "No se pudo dar de alta la imputacion con el articulo: " + idProducto);
                            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                        }
                    }
                    //Le sumo uno al contador de items
                    item++;
                }
            }            
            respuesta.setControl(AppCodigo.OK, "Comprobante creado con exito, con detalles");
            return Response.status(Response.Status.CREATED).entity(respuesta.toJson()).build();
        } catch (Exception ex) { 
            respuesta.setControl(AppCodigo.ERROR, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
        }
    } 
}
