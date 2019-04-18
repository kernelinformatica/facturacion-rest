package servicios;

import datos.AppCodigo;
import datos.FactCabResponse;
import datos.Payload;
import datos.ServicioResponse;
import entidades.Acceso;
import entidades.FactCab;
import entidades.SisComprobante;
import entidades.SisOperacionComprobante;
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
import persistencia.SisOperacionComprobanteFacade;
import persistencia.UsuarioFacade;

/**
 *
 * @author FrancoSili
 */

@Stateless
@Path("relacionContrato")
public class ContratoComprobanteRest {
    @Inject UsuarioFacade usuarioFacade;
    @Inject AccesoFacade accesoFacade;
    @Inject FactCabFacade factCabFacade;
    @Inject SisOperacionComprobanteFacade sisOperacionComprobanteFacade;
    
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getContratosPendientes(
        @HeaderParam ("token") String token,
        @QueryParam("codPadron") Integer codPadron,
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

            List<Payload> factResp = new ArrayList<>();
            
            List<SisOperacionComprobante> sisOpCom = sisOperacionComprobanteFacade.findIdByEmpresaUsaContrato(user.getIdPerfil().getIdSucursal().getIdEmpresa().getIdEmpresa());
            if(sisOpCom.isEmpty()) {
                respuesta.setControl(AppCodigo.ERROR, "Error, no existen comprobantes a los cuales se les deba asignar un contrato");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }                

            //saco los siscomprobantes repetidos
            List<SisComprobante> sisComprobantes = new ArrayList<>();
            for(SisOperacionComprobante s : sisOpCom){
                if(sisComprobantes.contains(s.getIdSisComprobantes())) {
                    continue;
                } else {
                    sisComprobantes.add(s.getIdSisComprobantes());
                }
            }

            System.out.println(sisComprobantes.size());
            List<FactCab> comprobantesEncontrados = new ArrayList<>();
            for(SisComprobante s : sisComprobantes) {
                if(!factCabFacade.findPendientesContratos(user.getIdPerfil().getIdSucursal().getIdEmpresa(),s).isEmpty() && factCabFacade.findPendientesContratos(user.getIdPerfil().getIdSucursal().getIdEmpresa(),s) != null) {
                    comprobantesEncontrados.addAll(factCabFacade.findPendientesContratos(user.getIdPerfil().getIdSucursal().getIdEmpresa(), s));
                }
            }

            if(comprobantesEncontrados.isEmpty()) {
                respuesta.setControl(AppCodigo.ERROR, "Error, no se encontraron comprobantes");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
               
            if(codPadron == null) {                   
               List<FactCab> comprobantesFiltrados = new ArrayList<>();
                for(FactCab f : comprobantesEncontrados) {
                    if(f.getContratoDetCollection().isEmpty()) {
                        SisOperacionComprobante sop = sisOperacionComprobanteFacade.find(f.getIdSisOperacionComprobantes());
                        if(sop != null) {
                            if(sop.getUsaContrato()) {
                                comprobantesFiltrados.add(f);
                            }
                        }
                    }
                }
                
                if(comprobantesFiltrados.isEmpty()) {
                    respuesta.setControl(AppCodigo.ERROR, "Error, no se encontraron comprobantes sin contratos relacionados");
                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                }
                
                for(FactCab f : comprobantesFiltrados) {
                    FactCabResponse fr = new FactCabResponse(f);
                    factResp.add(fr);
                }
            } else {
                List<FactCab> comprobantesFiltrados = new ArrayList<>();
                for(FactCab f : comprobantesEncontrados) {
                    if(f.getContratoDetCollection().isEmpty() && f.getIdPadron() == codPadron) {
                        SisOperacionComprobante sop = sisOperacionComprobanteFacade.find(f.getIdSisOperacionComprobantes());
                        if(sop != null) {
                            if(sop.getUsaContrato()) {
                                comprobantesFiltrados.add(f);
                            }
                        }
                    }
                }
                
                if(comprobantesFiltrados.isEmpty()) {
                    respuesta.setControl(AppCodigo.ERROR, "Error, no se encontraron comprobantes sin contratos relacionados");
                    return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
                }
                
                for(FactCab f : comprobantesFiltrados) {
                    FactCabResponse fr = new FactCabResponse(f);
                    factResp.add(fr);
                }
            }
            respuesta.setArraydatos(factResp);
            respuesta.setControl(AppCodigo.OK, "Comprobantes sin contratos");
            return Response.status(Response.Status.CREATED).entity(respuesta.toJson()).build();
        } catch (Exception e) {
            respuesta.setControl(AppCodigo.ERROR, e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
        }
    }
}
