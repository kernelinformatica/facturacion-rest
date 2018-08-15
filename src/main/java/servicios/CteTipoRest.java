package servicios;

import com.google.gson.JsonObject;
import datos.AppCodigo;
import datos.CteTipoResponse;
import datos.Payload;
import datos.ServicioResponse;
import entidades.Acceso;
import entidades.CteTipo;
import entidades.SisComprobante;
import entidades.Usuario;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import persistencia.AccesoFacade;
import persistencia.CteTipoFacade;
import persistencia.SisComprobanteFacade;
import persistencia.UsuarioFacade;
import utils.Utils;

/**
 *
 * @author FrancoSili
 */

@Stateless
@Path("cteTipo")
public class CteTipoRest {
    @Inject UsuarioFacade usuarioFacade;
    @Inject AccesoFacade accesoFacade;
    @Inject CteTipoFacade cteTipoFacade;
    @Inject SisComprobanteFacade sisComprobanteFacade;
    @Inject Utils utils;
    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getComprobantes(  
        @HeaderParam ("token") String token,  
        @QueryParam("sisModulo") Integer sisModulo,
        @QueryParam("sisComprobante") Integer sisComprobante,
        @QueryParam("idCteTipo") Integer idCteTipo,
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
            //valido que tenga comprobantes disponibles
            if(user.getIdPerfil().getIdSucursal().getIdEmpresa().getCteTipoCollection().isEmpty()) {
                respuesta.setControl(AppCodigo.ERROR, "No hay Tipos de Comprobantes disponibles");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }           
            //Armo la respuesta de cteTipos
            List<Payload> cteTipos = new ArrayList<>();
            //devuelvo todos los cteTipo
            if(sisModulo == null && sisComprobante == null) {
                for(CteTipo p : user.getIdPerfil().getIdSucursal().getIdEmpresa().getCteTipoCollection()){
                    CteTipoResponse pr = new CteTipoResponse(p);
                    cteTipos.add(pr);
                }
            //Devuelvo cteTipo por modulo
            } else if(sisModulo != null && sisComprobante == null) {
                List<CteTipo> cteTipoList = cteTipoFacade.getByModulo(user.getIdPerfil().getIdSucursal().getIdEmpresa(), sisModulo);           
                //valido que tenga comprobantes disponibles
                if(cteTipoList.isEmpty()) {
                    respuesta.setControl(AppCodigo.ERROR, "No hay Tipos de Comprobantes disponibles");
                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                }
                for(CteTipo p : cteTipoList){
                    CteTipoResponse pr = new CteTipoResponse(p);
                    cteTipos.add(pr);
                }
            //Devuelvo cteTipo por comprobante
            } else if(sisModulo == null && sisComprobante != null && idCteTipo == null) {
                SisComprobante sisComprobanteEncontrado = sisComprobanteFacade.find(sisComprobante);            
                if(sisComprobanteEncontrado == null) {
                    respuesta.setControl(AppCodigo.ERROR, "Error, no existe el sis comprobante");
                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                }
                List<CteTipo> cteTipo = cteTipoFacade.getBySisComprobanteEmpresa(sisComprobanteEncontrado, user.getIdPerfil().getIdSucursal().getIdEmpresa());
                if(cteTipo == null || cteTipo.isEmpty()) {
                    respuesta.setControl(AppCodigo.ERROR, "Error, no hay cte tipo disponibles para ese sis comprobante");
                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                }
                for(CteTipo c : cteTipo) {
                    CteTipoResponse cteTipoResponse = new CteTipoResponse(c);
                    cteTipos.add(cteTipoResponse);
                }
            //Devuelvo los numeradores de ese cteTipo   
            } else if(sisModulo == null && sisComprobante != null && idCteTipo != null) {
                CteTipo cteTipo = cteTipoFacade.find(idCteTipo);
                if(cteTipo == null) {
                    respuesta.setControl(AppCodigo.ERROR, "Error, no existe ese tipo de comprobamte");
                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                }
                CteTipoResponse cteTipoResponse = new CteTipoResponse(cteTipo);
                if(cteTipo.getCteNumeradorCollection().isEmpty()) {
                    respuesta.setControl(AppCodigo.ERROR, "Error, no existen numeradores para ese tipo de comprobante");
                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                }
                //Agrego los numeradores a la coleccion en cteTipoResponse y le sumo uno al punto de venta
                cteTipoResponse.agregarNumeradores(cteTipo.getCteNumeradorCollection());
                cteTipos.add(cteTipoResponse);                      
            } else {
                respuesta.setControl(AppCodigo.ERROR, "No hay Tipos de Comprobantes disponibles");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            respuesta.setArraydatos(cteTipos);
            respuesta.setControl(AppCodigo.OK, "Lista de Tipos de Comprobantes");
            return Response.status(Response.Status.CREATED).entity(respuesta.toJson()).build();
        } catch (Exception e) {
            respuesta.setControl(AppCodigo.ERROR, e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
        }
    }
    
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
            Integer codigoComp = (Integer) Utils.getKeyFromJsonObject("codigoComp", jsonBody, "Integer");
            String descCorta = (String) Utils.getKeyFromJsonObject("descCorta", jsonBody, "String");
            String descripcion = (String) Utils.getKeyFromJsonObject("descripcion", jsonBody, "String");
            boolean cursoLegal = (boolean) Utils.getKeyFromJsonObject("cursoLegal", jsonBody, "boolean");
            Integer codigoAfip = (Integer) Utils.getKeyFromJsonObject("codigoAfip", jsonBody, "Integer"); 
            String surenu = (String) Utils.getKeyFromJsonObject("surenu", jsonBody, "String");
            String observaciones = (String) Utils.getKeyFromJsonObject("observaciones", jsonBody, "String");
            Integer idSisComprobante = (Integer) Utils.getKeyFromJsonObject("idSisComprobante", jsonBody, "Integer");
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

            //Me fijo que descCorta, descripcion, surenu, codigoComp y codigoAfip no sean nulos
            if(descCorta == null || descripcion == null || surenu == null || codigoComp == 0 || codigoAfip == 0) {
                respuesta.setControl(AppCodigo.ERROR, "Error, algun campos esta vacio");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            SisComprobante sisComprobante = sisComprobanteFacade.find(idSisComprobante);
            
            if(sisComprobante == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, no existe el Comprobante");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            boolean transaccion;
            CteTipo newCte = new CteTipo();
            newCte.setCodigoAfip(codigoAfip);
            newCte.setCodigoComp(codigoComp);
            newCte.setCursoLegal(cursoLegal);
            newCte.setDescCorta(descCorta);
            newCte.setDescripcion(descripcion);
            newCte.setIdEmpresa(user.getIdPerfil().getIdSucursal().getIdEmpresa());
            newCte.setObservaciones(observaciones);
            newCte.setSurenu(surenu);
            newCte.setIdSisComprobante(sisComprobante);
            transaccion = cteTipoFacade.setCteTipoNuevo(newCte, user);
            if(!transaccion) {
                respuesta.setControl(AppCodigo.ERROR, "No se pudo dar de alta el Tipo de Comprobante, Codigo de Comprobante existente");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            respuesta.setControl(AppCodigo.OK, "Tipo de Comprobante creado con exito");
            return Response.status(Response.Status.CREATED).entity(respuesta.toJson()).build();
        } catch (Exception ex) { 
            respuesta.setControl(AppCodigo.ERROR, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
        }
    }   
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editComprobante(  
        @HeaderParam ("token") String token,
        @Context HttpServletRequest request) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        
        ServicioResponse respuesta = new ServicioResponse();
        try {
            // Obtengo el body de la request
            JsonObject jsonBody = Utils.getJsonObjectFromRequest(request);
            
            // Obtengo los atributos del body
            Integer idCteTipo = (Integer) Utils.getKeyFromJsonObject("idCteTipo", jsonBody, "Integer");
            Integer codigoComp = (Integer) Utils.getKeyFromJsonObject("codigoComp", jsonBody, "Integer");
            String descCorta = (String) Utils.getKeyFromJsonObject("descCorta", jsonBody, "String");
            String descripcion = (String) Utils.getKeyFromJsonObject("descripcion", jsonBody, "String");
            boolean cursoLegal = (boolean) Utils.getKeyFromJsonObject("cursoLegal", jsonBody, "boolean");
            Integer codigoAfip = (Integer) Utils.getKeyFromJsonObject("codigoAfip", jsonBody, "Integer"); 
            String surenu = (String) Utils.getKeyFromJsonObject("surenu", jsonBody, "String");
            String observaciones = (String) Utils.getKeyFromJsonObject("observaciones", jsonBody, "String");
            Integer idSisComprobante = (Integer) Utils.getKeyFromJsonObject("idSisComprobante", jsonBody, "Integer");
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

            //Me fijo que descCorta, descripcion, surenu, codigoComp y codigoAfip no sean nulos
            if(idCteTipo == 0 || descCorta == null || descripcion == null || surenu == null || codigoComp == 0 || codigoAfip == 0) {
                respuesta.setControl(AppCodigo.ERROR, "Error, algun campo esta vacio");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            CteTipo newCte = cteTipoFacade.find(idCteTipo);
            
            SisComprobante sisComprobante = sisComprobanteFacade.find(idSisComprobante);
            
            if(sisComprobante == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, no existe el Comprobante");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            //Pregunto si existe el Cte
            if(newCte == null) {
                respuesta.setControl(AppCodigo.ERROR, "No existe el tipo de comprobonta");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            boolean transaccion;
            newCte.setCodigoAfip(codigoAfip);
            newCte.setCodigoComp(codigoComp);
            newCte.setCursoLegal(cursoLegal);
            newCte.setDescCorta(descCorta);
            newCte.setDescripcion(descripcion);
            newCte.setIdEmpresa(user.getIdPerfil().getIdSucursal().getIdEmpresa());
            newCte.setObservaciones(observaciones);
            newCte.setSurenu(surenu);
            newCte.setIdSisComprobante(sisComprobante);
            transaccion = cteTipoFacade.editCteTipo(newCte);
            if(!transaccion) {
                respuesta.setControl(AppCodigo.ERROR, "No se pudo editar el Tipo de Comprobante");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            respuesta.setControl(AppCodigo.OK, "Tipo de Comprobante editado con exito");
            return Response.status(Response.Status.CREATED).entity(respuesta.toJson()).build();
        } catch (Exception ex) { 
            respuesta.setControl(AppCodigo.ERROR, ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
        }
    }   

    @DELETE
    @Path ("/{idComprobante}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON) 
    public Response deleteComprobante(  
        @HeaderParam ("token") String token,
        @PathParam ("idComprobante") int idComprobante,
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
            
            //busco el cteTipo a borrar
            CteTipo cteTipo = cteTipoFacade.find(idComprobante);
            if(cteTipo == null) {
                respuesta.setControl(AppCodigo.ERROR, "El Tipo de Comprobante no existe");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            boolean transaccion;
            transaccion = cteTipoFacade.deleteCteTipo(cteTipo);
            if(!transaccion) {
                respuesta.setControl(AppCodigo.ERROR, "No se pudo borrar el Tipo de Comprobante");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            respuesta.setControl(AppCodigo.OK, "Tipo de Comprobante borrado con exito");
            return Response.status(Response.Status.CREATED).entity(respuesta.toJson()).build();
        } catch (Exception e) {
            respuesta.setControl(AppCodigo.ERROR, e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
        } 
    }    
}
