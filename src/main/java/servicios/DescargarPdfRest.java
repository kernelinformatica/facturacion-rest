package servicios;

import com.google.gson.JsonObject;
import datos.AppCodigo;
import datos.ServicioResponse;
import entidades.Acceso;
import entidades.FactCab;
import entidades.Usuario;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import persistencia.AccesoFacade;
import persistencia.FactCabFacade;
import persistencia.UsuarioFacade;
import utils.Utils;

/**
 *
 * @author FrancoSili
 */

@Stateless
@Path("descargarPdf")
public class DescargarPdfRest extends HttpServlet {
    @Inject UsuarioFacade usuarioFacade;
    @Inject AccesoFacade accesoFacade;
    @Inject Utils utils;
    @Inject FactCabFacade factCabFacade;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPDF(  
        @HeaderParam ("token") String token,
        @Context HttpServletRequest request) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        ServicioResponse respuesta = new ServicioResponse();
        try {
            // Obtengo el body de la request
            JsonObject jsonBody = Utils.getJsonObjectFromRequest(request);
            
            // Obtengo los atributos del body
            Integer idFactCab = (Integer) Utils.getKeyFromJsonObject("idFactCab", jsonBody, "Integer");
            
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
            if(idFactCab == null) {
                respuesta.setControl(AppCodigo.ERROR, "IdFacCab esta vacio");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            FactCab factCab = factCabFacade.find(idFactCab);
            
            if(factCab == null) {
                respuesta.setControl(AppCodigo.ERROR, "No existe ese comprobante");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            String nombreReporte = factCab.getIdCteTipo().getIdReportes().getNombre();
            System.out.println(nombreReporte);
            
             if(nombreReporte == null) {
                respuesta.setControl(AppCodigo.ERROR, "El comprobante nro: "+factCab.getNumero()+" no tiene un reporte asociado");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            
            //Codigo verificador
            String codigoVerificador = "";
            if(factCab.getNumeroAfip() != null && factCab.getIdCteTipo().getCursoLegal() && !" ".equals(factCab.getCai())) {
                SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyyMMdd");
                Formatter obj = new Formatter();
                String txtCuit = factCab.getCuit();
                String txtCodComp = String.valueOf(obj.format("%02d",factCab.getCodigoAfip()));
                String numero = String.valueOf(factCab.getNumero());
                String ptoVenta = numero.substring(0, numero.length()-8);                
                String txtPtoVta  = String.valueOf(obj.format("%04d", Integer.parseInt(ptoVenta)));
                String txtCae = factCab.getCai();
                String txtVtoCae = formatoFecha.format(factCab.getFechaVto());
                codigoVerificador = utils.calculoDigitoVerificador(txtCuit, txtCodComp, txtPtoVta, txtCae, txtVtoCae);
            } 
             
            HashMap hm = new HashMap();
            hm.put("idFactCab", idFactCab);
            hm.put("codigoVerificador", codigoVerificador);
            hm.put("prefijoEmpresa", "05");
            System.out.println(idFactCab + " - " + codigoVerificador);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] bytes = utils.generateJasperReportPDF(request, nombreReporte, hm, user, outputStream);
            System.out.println(request.toString() + " - " + nombreReporte + " - " + user.toString());
            String nomeRelatorio= nombreReporte + ".pdf";
            return Response.ok(bytes).type("application/pdf").header("Content-Disposition", "filename=\"" + nomeRelatorio + "\"").build();
        } catch (Exception e) {
            respuesta.setControl(AppCodigo.ERROR, e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
        }
    } 
}

