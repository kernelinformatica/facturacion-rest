package servicios;

import com.google.gson.JsonObject;
import datos.AppCodigo;
import datos.CteNumeradorResponse;
import datos.Payload;
import datos.ServicioResponse;
import entidades.Acceso;
import entidades.CteNumerador;
import entidades.CteNumero;
import entidades.CteTipo;
import entidades.Usuario;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import persistencia.AccesoFacade;
import persistencia.CteNumeradorFacade;
import persistencia.CteNumeroFacade;
import persistencia.CteTipoFacade;
import persistencia.UsuarioFacade;
import utils.Utils;

/**
 *
 * @author FrancoSili
 */

@Stateless
@Path("cteNumerador")
public class NumeradorRest {
    @Inject UsuarioFacade usuarioFacade;
    @Inject AccesoFacade accesoFacade;
    @Inject CteTipoFacade cteTipoFacade;
    @Inject CteNumeroFacade cteNumeroFacade;
    @Inject CteNumeradorFacade cteNumeradorFacade;
    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNumerador(  
        @HeaderParam ("token") String token,  
        @Context HttpServletRequest request) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        ServicioResponse respuesta = new ServicioResponse();
        try {
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
            
            //valido que tenga Rubros disponibles
            if(user.getIdPerfil().getIdSucursal().getIdEmpresa().getCteTipoCollection().isEmpty()) {
                respuesta.setControl(AppCodigo.ERROR, "No hay Tipos de Comprobantes disponibles");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            //busco los numeradores de la empresa del usuario
            List<Payload> numeradores = new ArrayList<>();
            for(CteTipo p : user.getIdPerfil().getIdSucursal().getIdEmpresa().getCteTipoCollection()){
                for(CteNumerador c : p.getCteNumeradorCollection()) {
                    CteNumeradorResponse t = new CteNumeradorResponse(c);
                    numeradores.add(t);
                }               
            }
            
            respuesta.setArraydatos(numeradores);
            respuesta.setControl(AppCodigo.OK, "Lista de Numeradores");
            return Response.status(Response.Status.CREATED).entity(respuesta.toJson()).build();
        } catch (Exception e) {
            respuesta.setControl(AppCodigo.ERROR, e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
        }
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setNumerador(  
        @HeaderParam ("token") String token,
        @Context HttpServletRequest request) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        
        ServicioResponse respuesta = new ServicioResponse();
        try {
            // Obtengo el body de la request
            JsonObject jsonBody = Utils.getJsonObjectFromRequest(request);
            
            // Obtengo los atributos del body            
            Integer ptoVenta = (Integer) Utils.getKeyFromJsonObject("ptoVenta", jsonBody, "Integer");
            Integer numero = (Integer) Utils.getKeyFromJsonObject("numero", jsonBody, "Integer");
            String descripcion = (String) Utils.getKeyFromJsonObject("descripcion", jsonBody, "String");
            Date fechaApertura = (Date) Utils.getKeyFromJsonObject("fechaApertura", jsonBody, "Date");
            Date fechaCierre = (Date) Utils.getKeyFromJsonObject("fechaCierre", jsonBody, "Date");
            Integer idCteTipo = (Integer) Utils.getKeyFromJsonObject("idCteTipo", jsonBody, "Integer");
            Integer idCteNumero = (Integer) Utils.getKeyFromJsonObject("idCteNumero", jsonBody, "Integer");
            
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

            //Me fijo que  los campos no sean nulos
            if(fechaApertura == null || fechaCierre == null || idCteTipo == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, algun campo esta vacio");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            CteTipo cteTipo = cteTipoFacade.find(idCteTipo);
            if(cteTipo == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, no existe el tipo de comprobante seleccionado");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            boolean transaccion;
            boolean transaccion1;
            if(idCteNumero == null && ptoVenta != null && numero != null) {            
                CteNumero cteNumero = new CteNumero();
                CteNumerador cteNumerador = new CteNumerador();
                cteNumero.setNumero(numero);
                cteNumero.setPtoVenta(ptoVenta);
                cteNumerador.setDescripcion(descripcion);
                cteNumerador.setFechaApertura(fechaApertura);
                cteNumerador.setFechaCierre(fechaCierre);
                cteNumerador.setIdCteNumero(cteNumero);
                cteNumerador.setIdCteTipo(cteTipo);
                try {
                    transaccion = cteNumeroFacade.setCteNumeroNuevo(cteNumero);
                    transaccion1 = cteNumeradorFacade.setCteNumeradorNuevo(cteNumerador);
                    if(!transaccion || !transaccion1) {
                        respuesta.setControl(AppCodigo.ERROR, "No se pudo dar de alta el Numerador");
                        return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                    }
                    respuesta.setControl(AppCodigo.CREADO, "Numerador creado con exito");
                    return Response.status(Response.Status.CREATED).entity(respuesta.toJson()).build();
                } catch (Exception ex) {
                    respuesta.setControl(AppCodigo.ERROR, "No se pudo dar de alta el Numerador");
                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                }
            } else if(idCteNumero != null){
                CteNumero cteNumero = cteNumeroFacade.find(idCteNumero);
                if(cteNumero == null) {
                    respuesta.setControl(AppCodigo.ERROR, "Error, no existe el nuemero seleccionado");
                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                }
                CteNumerador cteNumerador = new CteNumerador();
                cteNumerador.setDescripcion(descripcion);
                cteNumerador.setFechaApertura(fechaApertura);
                cteNumerador.setFechaCierre(fechaCierre);
                cteNumerador.setIdCteNumero(cteNumero);
                cteNumerador.setIdCteTipo(cteTipo);
                transaccion1 = cteNumeradorFacade.setCteNumeradorNuevo(cteNumerador);
                if(!transaccion1) {
                    respuesta.setControl(AppCodigo.ERROR, "No se pudo dar de alta el Numerador");
                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                }
                respuesta.setControl(AppCodigo.CREADO, "Numerador creado con exito");
                return Response.status(Response.Status.CREATED).entity(respuesta.toJson()).build();
            }
            respuesta.setControl(AppCodigo.ERROR, "No se pudo dar de alta el Numerador");
            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
        } catch (Exception ex) { 
            respuesta.setControl(AppCodigo.ERROR, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
        }
    } 
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editNumerador(  
        @HeaderParam ("token") String token,
        @Context HttpServletRequest request) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        
        ServicioResponse respuesta = new ServicioResponse();
        try {
            // Obtengo el body de la request
            JsonObject jsonBody = Utils.getJsonObjectFromRequest(request);
            
            // Obtengo los atributos del body
            Integer idCteNumerador = (Integer) Utils.getKeyFromJsonObject("idCteNumerador", jsonBody, "Integer");            
            Integer ptoVenta = (Integer) Utils.getKeyFromJsonObject("ptoVenta", jsonBody, "Integer");
            Integer numero = (Integer) Utils.getKeyFromJsonObject("numero", jsonBody, "Integer");
            String descripcion = (String) Utils.getKeyFromJsonObject("descripcion", jsonBody, "String");
            Date fechaApertura = (Date) Utils.getKeyFromJsonObject("fechaApertura", jsonBody, "Date");
            Date fechaCierre = (Date) Utils.getKeyFromJsonObject("fechaCierre", jsonBody, "Date");
            Integer idCteTipo = (Integer) Utils.getKeyFromJsonObject("idCteTipo", jsonBody, "Integer");
            Integer idCteNumero = (Integer) Utils.getKeyFromJsonObject("idCteNumero", jsonBody, "Integer");
            
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

            //Me fijo que  los campos no sean nulos
            if(fechaApertura == null || fechaCierre == null || idCteTipo == null || idCteNumerador == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, algun campo esta vacio");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            CteNumerador cteNumerador = cteNumeradorFacade.find(idCteNumerador);
            if(cteNumerador == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, no existe el Numerador seleccionado");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            CteTipo cteTipo = cteTipoFacade.find(idCteTipo);
            if(cteTipo == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, no existe el tipo de comprobante seleccionado");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }

            boolean transaccion1;
            if(idCteNumero == null && ptoVenta != null && numero != null) {            
                cteNumerador.setDescripcion(descripcion);
                cteNumerador.setFechaApertura(fechaApertura);
                cteNumerador.setFechaCierre(fechaCierre);
                cteNumerador.setIdCteTipo(cteTipo);
                cteNumerador.getIdCteNumero().setNumero(numero);
                cteNumerador.getIdCteNumero().setPtoVenta(ptoVenta);
                transaccion1 = cteNumeradorFacade.editCteNumerador(cteNumerador);
                if(!transaccion1) {
                    respuesta.setControl(AppCodigo.ERROR, "No se pudo editar el Numerador");
                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                }
                respuesta.setControl(AppCodigo.CREADO, "Numerador editado con exito");
                return Response.status(Response.Status.CREATED).entity(respuesta.toJson()).build();
            } else if(idCteNumero != null) {
                CteNumero cteNumero = cteNumeroFacade.find(idCteNumero);
                if(cteNumero == null) {
                    respuesta.setControl(AppCodigo.ERROR, "Error, no existe el nuemero seleccionado");
                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                }
                cteNumerador.setDescripcion(descripcion);
                cteNumerador.setFechaApertura(fechaApertura);
                cteNumerador.setFechaCierre(fechaCierre);
                cteNumerador.setIdCteTipo(cteTipo);
                cteNumerador.setIdCteNumero(cteNumero);            
                transaccion1 = cteNumeradorFacade.editCteNumerador(cteNumerador);           
                if(!transaccion1) {
                    respuesta.setControl(AppCodigo.ERROR, "No se pudo editar el Numerador");
                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                }
                respuesta.setControl(AppCodigo.CREADO, "Numerador editado con exito");
                return Response.status(Response.Status.CREATED).entity(respuesta.toJson()).build();
            }
            respuesta.setControl(AppCodigo.ERROR, "No se pudo editar el Numerador");
            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
        } catch (Exception ex) { 
            respuesta.setControl(AppCodigo.ERROR, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
        }
    }   
    
    
    @DELETE
    @Path ("/{idNumerador}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON) 
    public Response deleteNumerador(  
        @HeaderParam ("token") String token,
        @PathParam ("idNumerador") Integer idNumerador,
        @Context HttpServletRequest request) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        ServicioResponse respuesta = new ServicioResponse();
        try {
            //valido que token y el id no sea null
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
            
            //Me fijo que descCorta y idRubro no sean nulos
            if(idNumerador == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, debe seleccionar un Numerador");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
           
            CteNumerador cteNumerador = cteNumeradorFacade.find(idNumerador);
            
            //Pregunto si existe el Numerador
            if(cteNumerador == null) {
                respuesta.setControl(AppCodigo.ERROR, "No existe la Marca");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            boolean transaccion;
            boolean transaccion2;
            if(cteNumerador.getIdCteNumero().getCteNumeradorCollection().size() > 1) {
                transaccion = cteNumeradorFacade.deleteCteNumerador(cteNumerador);
                if(!transaccion) {
                    respuesta.setControl(AppCodigo.ERROR, "No se pudo borrar el Numerador");
                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                }
            } else {
                try {
                    transaccion = cteNumeradorFacade.deleteCteNumerador(cteNumerador);
                    transaccion2 = cteNumeroFacade.deleteCteNumero(cteNumerador.getIdCteNumero());
                    if(!transaccion || !transaccion2) {
                        respuesta.setControl(AppCodigo.ERROR, "No se pudo borrar el Numerador");
                        return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                    }
                } catch (Exception e) {
                    respuesta.setControl(AppCodigo.ERROR, e.getMessage());
                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                } 
            }               
            respuesta.setControl(AppCodigo.OK, "Numerador borrado con exito");
            return Response.status(Response.Status.CREATED).entity(respuesta.toJson()).build();
        } catch (Exception e) {
            respuesta.setControl(AppCodigo.ERROR, e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
        } 
    }    
}
