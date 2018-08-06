//package servicios;
//
//import com.google.gson.JsonObject;
//import datos.AppCodigo;
//import datos.CteTipoResponse;
//import datos.Payload;
//import datos.ServicioResponse;
//import entidades.Acceso;
//import entidades.CteTipo;
//import entidades.SisComprobante;
//import entidades.SisModulo;
//import entidades.Usuario;
//import java.io.UnsupportedEncodingException;
//import java.security.NoSuchAlgorithmException;
//import java.util.ArrayList;
//import java.util.List;
//import javax.ejb.Stateless;
//import javax.inject.Inject;
//import javax.servlet.http.HttpServletRequest;
//import javax.ws.rs.Consumes;
//import javax.ws.rs.GET;
//import javax.ws.rs.HeaderParam;
//import javax.ws.rs.POST;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import persistencia.AccesoFacade;
//import persistencia.CteTipoFacade;
//import persistencia.SisComprobanteFacade;
//import persistencia.SisModuloFacade;
//import persistencia.UsuarioFacade;
//import utils.Utils;
//
///**
// *
// * @author FrancoSili
// */
//
//@Stateless
//@Path("buscaCteTipoNro")
//public class BuscaCteTipoNro {
//    @Inject UsuarioFacade usuarioFacade;
//    @Inject AccesoFacade accesoFacade;
//    @Inject SisComprobanteFacade sisComprobanteFacade;
//    @Inject CteTipoFacade cteTipoFacade;
//    @Inject SisModuloFacade sisModuloFacade;
//
//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getCteTipoNro(  
//        @HeaderParam ("token") String token,    
//        @Context HttpServletRequest request
//    ) throws NoSuchAlgorithmException, UnsupportedEncodingException {
//        ServicioResponse respuesta = new ServicioResponse();
//        try {
//            // Obtengo el body de la request
//            JsonObject jsonBody = Utils.getJsonObjectFromRequest(request);
//            
//            // Obtengo los atributos del body
//            Integer idSisComprobante = (Integer) Utils.getKeyFromJsonObject("idSisComprobante", jsonBody, "Integer");
//            
//            if(token == null ||token.trim().isEmpty()) {
//                respuesta.setControl(AppCodigo.ERROR, "Error, token vacio");
//                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
//            }
//
//            //Busco el token
//            Acceso userToken = accesoFacade.findByToken(token);
//
//            //valido que Acceso no sea null
//            if(userToken == null) {
//                respuesta.setControl(AppCodigo.ERROR, "Error, Acceso nulo");
//                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
//            }
//
//            //Busco el usuario
//            Usuario user = usuarioFacade.getByToken(userToken);
//
//            //valido que el Usuario no sea null
//            if(user == null) {
//                respuesta.setControl(AppCodigo.ERROR, "Error, Usuario nulo");
//                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
//            }
//
//            //valido vencimiento token
//            if(!accesoFacade.validarToken(userToken, user)) {
//                respuesta.setControl(AppCodigo.ERROR, "Credenciales incorrectas");
//                return Response.status(Response.Status.UNAUTHORIZED).entity(respuesta.toJson()).build();
//            }
//            
//            if(idSisComprobante == null) {
//                respuesta.setControl(AppCodigo.ERROR, "Error, idSisComprobante nulo");
//                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
//            }
//            
//            SisComprobante sisComprobante = sisComprobanteFacade.find(idSisComprobante);
//            
//            if(sisComprobante == null) {
//                respuesta.setControl(AppCodigo.ERROR, "Error, no existe el sis comprobante");
//                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
//            }
//            
//            List<CteTipo> cteTipo = cteTipoFacade.getBySisComprobanteEmpresa(sisComprobante, user.getIdPerfil().getIdSucursal().getIdEmpresa());
//            
//            if(cteTipo == null || cteTipo.isEmpty()) {
//                respuesta.setControl(AppCodigo.ERROR, "Error, no hay cte tipo disponibles para ese sis comprobante");
//                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
//            }
//            List<Payload> cteRespuesta = new ArrayList<>();
//            for(CteTipo c : cteTipo) {
//                if(c.getCteNumeradorCollection().isEmpty()) {
//                    respuesta.setControl(AppCodigo.ERROR, "Error, no hay coleccion de numeradores para el cteTipo: " + c.getDescripcion());
//                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
//                }
//                CteTipoResponse cteTipoResponse = new CteTipoResponse(c);
//                cteTipoResponse.agregarNumeradores(c.getCteNumeradorCollection());
//                cteRespuesta.add(cteTipoResponse);
//            }
//            respuesta.setArraydatos(cteRespuesta);
//            respuesta.setControl(AppCodigo.OK, "Cte Numeros");
//            return Response.status(Response.Status.CREATED).entity(respuesta.toJson()).build();
//        } catch (Exception e) {
//            respuesta.setControl(AppCodigo.ERROR, e.getMessage());
//            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
//        }
//    }
//    
//    @GET
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    @Path ("/{idSisModulo}")
//    public Response getCteTipo(  
//        @HeaderParam ("token") String token,
//        @PathParam ("idSisModulo") Integer idSisModulo,
//        @Context HttpServletRequest request) throws NoSuchAlgorithmException, UnsupportedEncodingException {
//        ServicioResponse respuesta = new ServicioResponse();
//        try {
//            //valido que token no sea null
//            if(token == null || token.trim().isEmpty()) {
//                respuesta.setControl(AppCodigo.ERROR, "Error, token vacio");
//                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
//            }
//
//            //Busco el token
//            Acceso userToken = accesoFacade.findByToken(token);
//
//            //valido que Acceso no sea null
//            if(userToken == null) {
//                respuesta.setControl(AppCodigo.ERROR, "Error, Acceso nulo");
//                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
//            }
//
//            //Busco el usuario
//            Usuario user = usuarioFacade.getByToken(userToken);
//
//            //valido que el Usuario no sea null
//            if(user == null) {
//                respuesta.setControl(AppCodigo.ERROR, "Error, Usuario nulo");
//                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
//            }
//
//            //valido vencimiento token
//            if(!accesoFacade.validarToken(userToken, user)) {
//                respuesta.setControl(AppCodigo.ERROR, "Credenciales incorrectas");
//                return Response.status(Response.Status.UNAUTHORIZED).entity(respuesta.toJson()).build();
//            }
//            
//            //valido que idSisModulo no sea nulo
//            if(idSisModulo == null) {
//                respuesta.setControl(AppCodigo.ERROR, "SisModulo es nulo");
//                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
//            }
//            
//            SisModulo sisModulo = sisModuloFacade.find(idSisModulo);
//            
//            if(sisModulo == null) {
//                respuesta.setControl(AppCodigo.ERROR, "No existe ese modulo");
//                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
//            }
//            
//            if(sisModulo.getSisComprobanteCollection().isEmpty()) {
//                respuesta.setControl(AppCodigo.ERROR, "No hay comprobantes disponibles para ese modulo");
//                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
//            }
//            
//            List<Payload> cteTipos = new ArrayList<>();
//            
//            for(SisComprobante s : sisModulo.getSisComprobanteCollection()) {
//                for(CteTipo c : s.getCteTipoCollection()) {
//                    CteTipoResponse cte = new CteTipoResponse(c);
//                    cteTipos.add(cte);
//                }  
//            }
//            respuesta.setArraydatos(cteTipos);
//            respuesta.setControl(AppCodigo.OK, "Lista de CteTipos por modulos");
//            return Response.status(Response.Status.OK).entity(respuesta.toJson()).build();
//        } catch (Exception e) {
//            respuesta.setControl(AppCodigo.ERROR, e.getMessage());
//            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
//        }
//    }
//}
