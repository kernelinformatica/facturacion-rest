package servicios;

import datos.AppCodigo;
import datos.FactCabResponse;
import datos.FactDetalleResponse;
import datos.FactImputaResponse;
import datos.Payload;
import datos.ServicioResponse;
import entidades.Acceso;
import entidades.FactCab;
import entidades.FactDetalle;
import entidades.FactImputa;
import entidades.Usuario;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import persistencia.AccesoFacade;
import persistencia.FactCabFacade;
import persistencia.UsuarioFacade;


/**
 *
 * @author FrancoSili
 */
@Stateless
@Path("imputaciones")
public class ImputacionesRest {
    @Inject UsuarioFacade usuarioFacade;
    @Inject AccesoFacade accesoFacade;
    @Inject FactCabFacade factCabFacade;
    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getImputaciones(  
        @HeaderParam ("token") String token,
        @QueryParam("idFactCab") Integer idFactCab,    
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
            
            //valido que tenga idFactCab no venga nulo
            if(idFactCab == null) {
                respuesta.setControl(AppCodigo.ERROR, "No selecciono un comprobante");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            FactCab factCab = factCabFacade.find(idFactCab);            
            if(factCab == null) {
                respuesta.setControl(AppCodigo.ERROR, "No selecciono un comprobante");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            if(factCab.getFactDetalleCollection().isEmpty()) {
                respuesta.setControl(AppCodigo.ERROR, "No existen detalles para ese comprobante");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            String imputada = "SI";
            String vendedor = "";
            if(factCab.getIdVendedor() != null) {
                vendedor = factCab.getIdVendedor().toString();
            }
            
            List<FactCabResponse> listaCabeceras = new ArrayList<>();
            List<Payload> respuestas = new ArrayList<>();
            
            for(FactDetalle fd : factCab.getFactDetalleCollection()) {
                if(fd.getFactImputaCollection().isEmpty()) {
                    continue;
                }
                for(FactImputa fci1 : fd.getFactImputaCollection()) {                  
                    FactCabResponse factCabResponse = new FactCabResponse(fci1.getIdFactDetalleImputa().getIdFactCab().getIdFactCab(), fci1.getIdFactDetalleImputa().getIdFactCab().getIdCteTipo().getDescripcion(),fd.getIdFactCab().getNumero(),fci1.getIdFactDetalleImputa().getIdFactCab().getFechaEmision(),fd.getIdFactCab().getIdPadron(),fci1.getIdFactDetalleImputa().getIdFactCab().getNombre(),fd.getIdFactCab().getCuit(), fci1.getIdFactDetalleImputa().getIdFactCab().getCotDolar(), fci1.getIdFactDetalleImputa().getIdFactCab().getIdmoneda().getDescripcion(),imputada ,fci1.getIdFactDetalleImputa().getIdFactCab().getIdCteTipo().getIdSisComprobante().getIdSisModulos().getDescripcion(),vendedor);
                    if(!listaCabeceras.isEmpty()) {
                        factCabResponse = listaCabeceras.get(listaCabeceras.size()-1);
                        FactImputaResponse firi = new FactImputaResponse(fci1);
                        factCabResponse.getImputa().add(firi);
                    } else {
                        FactImputaResponse firi = new FactImputaResponse(fci1);
                        factCabResponse.getImputa().add(firi);
                        listaCabeceras.add(factCabResponse);
                    }
                }                
            }                        
            if(!listaCabeceras.isEmpty()) {
                respuestas.addAll(listaCabeceras);
            }
            
            respuesta.setArray(respuestas);
            respuesta.setControl(AppCodigo.OK, "Imputaciones");
            return Response.status(Response.Status.CREATED).entity(respuesta.toJson()).build();
        } catch (Exception e) {
            respuesta.setControl(AppCodigo.ERROR, e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
        }
    }
}
