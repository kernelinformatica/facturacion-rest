/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicios;

import com.google.gson.JsonObject;
import datos.ServicioResponse;
import javax.ejb.Stateless;
import javax.inject.Inject;
import datos.AppCodigo;
import datos.OrdenesPagoResponse;
import datos.OrdenPagoDetalleResponse;
import datos.OrdenPagoFormaPagoResponse;
import datos.OrdenPagoPieResponse;
import datos.FactCabResponse;
import datos.Payload;
import entidades.Acceso;
import entidades.FactCab;
import entidades.FactDetalle;
import entidades.OrdenesPagosPCab;
import entidades.OrdenesPagosDetalle;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
//import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
//import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;

import java.security.NoSuchAlgorithmException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import persistencia.AccesoFacade;
import utils.Utils;
import entidades.Usuario;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import javax.ws.rs.POST;
import javax.ws.rs.QueryParam;
import persistencia.UsuarioFacade;
import persistencia.FactCabFacade;
import persistencia.FactDetalleFacade;

/**
 *
 * @author mertw
 */
@Stateless
@Path("buscarOrdenesPago")
public class BuscarOrdenesPagoRest {

    @Inject
    UsuarioFacade usuarioFacade;
    @Inject
    AccesoFacade accesoFacade;
    @Inject
    FactCabFacade comprobanteFacade;
    @Inject
    Utils utils;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrdenesPagos(
            @HeaderParam("token") String token,
            @Context HttpServletRequest request) throws NoSuchAlgorithmException, UnsupportedEncodingException, SQLException {
        ServicioResponse respuesta = new ServicioResponse();
        try {
            JsonObject jsonBody = Utils.getJsonObjectFromRequest(request);
            // Obtengo los atributos del body
            Integer idEmpresa = (Integer) Utils.getKeyFromJsonObject("idEmpresa", jsonBody, "Integer");
            Integer idFactCab = (Integer) Utils.getKeyFromJsonObject("idFacCab", jsonBody, "Integer");
            Integer idPadron = (Integer) Utils.getKeyFromJsonObject("idPadron", jsonBody, "Integer");
            Date fechaDesde = (Date) Utils.getKeyFromJsonObject("fechaDesde", jsonBody, "Date");
            Date fechaHasta = (Date) Utils.getKeyFromJsonObject("fechaHasta", jsonBody, "Date");

            //Date fechaDesde1=new SimpleDateFormat("yyyy-MM-dd").parse(fechaDesde); 
            //Date fechaHasta1=new SimpleDateFormat("yyyy-MM-dd").parse(fechaHasta); 
            //---------------===========================================================
            //valido que token no sea null
            if (token == null || token.trim().isEmpty()) {
                respuesta.setControl(AppCodigo.ERROR, "Error, token vacio");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }

            //Busco el token
            Acceso userToken = accesoFacade.findByToken(token);

            //valido que Acceso no sea null
            if (userToken == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, Acceso nulo");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }

            //Busco el usuario
            Usuario user = usuarioFacade.getByToken(userToken);

            //valido que el Usuario no sea null
            if (user == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, Usuario nulo");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }

            //valido vencimiento token
            if (!accesoFacade.validarToken(userToken, user)) {
                respuesta.setControl(AppCodigo.ERROR, "Credenciales incorrectas");
                return Response.status(Response.Status.UNAUTHORIZED).entity(respuesta.toJson()).build();
            }

            //Valido fechas
            if (fechaDesde.after(fechaHasta)) {
                respuesta.setControl(AppCodigo.ERROR, "Error, la fecha desde debe ser menor que la fecha hasta");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            if (idFactCab == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, Debe indicar un nro de cabecera de factura de compra");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            FactCab comprobanteCab = comprobanteFacade.getByIdFactCab(idFactCab);
            if (comprobanteCab == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, No se encontro la cabecera de origen");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }
            //seteo el nombre del store cabecera
            String nombreSP = "call s_buscaOrdenesPago(?,?,?,?)";
            //seteo el nombre del store detalle
            String nombreSPDetalle = "call s_buscaOrdenesPagoDetalles(?)";
            //seteo el nombre del store formaPago
            String nombreSPFormaPago = "call s_buscaComprobantesOrdenPagoFormaPago(?)";
            //seteo el nombre del store formaPago
            String nombreSPPie = "call s_buscaComprobantesOrdenPagoPie(?)";
            
            //defino las invocaciones  al store procedure de la base
            CallableStatement buscaOrdenesPagoRs = this.utils.procedimientoAlmacenado(user, nombreSP);
            CallableStatement buscaOrdenesPagoDetalleRs = this.utils.procedimientoAlmacenado(user, nombreSPDetalle);
            CallableStatement buscaOrdenesPagoFormaPagoRs = this.utils.procedimientoAlmacenado(user, nombreSPFormaPago);
            CallableStatement buscaOrdenesPagoPieRs = this.utils.procedimientoAlmacenado(user, nombreSPPie);

            //valido que el Procedimiento Almacenado no sea null
            if (buscaOrdenesPagoRs == null) {
                respuesta.setControl(AppCodigo.ERROR, "Error, no existe el procedimiento");
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            }

            //Parseo las fechas a sql.date
            java.sql.Date sqlFechaDesde = new java.sql.Date(fechaDesde.getTime());
            java.sql.Date sqlFechaHasta = new java.sql.Date(fechaHasta.getTime());

            //Seteo los parametros para la cabecera 
            buscaOrdenesPagoRs.setInt(1, idPadron);
            buscaOrdenesPagoRs.setInt(2, (int) comprobanteCab.getNumero());
            buscaOrdenesPagoRs.setDate(3, sqlFechaDesde);
            buscaOrdenesPagoRs.setDate(4, sqlFechaHasta);

            
            
            
            //Reccorro los resultados para la cabecera
            ResultSet rs = buscaOrdenesPagoRs.executeQuery();
            List<OrdenesPagoResponse> ordenesPago = new ArrayList<>();
            List<OrdenPagoDetalleResponse> ordenesPagoDetalles = new ArrayList<>();
            List<OrdenPagoFormaPagoResponse> ordenesPagoFormaPago = new ArrayList<>();
             List<OrdenPagoPieResponse> ordenesPagoPie = new ArrayList<>();
            while (rs.next()) {
                OrdenesPagoResponse ordenPago = new OrdenesPagoResponse(
                        rs.getInt("idOPCab"),
                        rs.getInt("idCteTipo"),
                        rs.getLong("numero"),
                        rs.getString("nombre"),
                        rs.getString("cuit"),
                        rs.getDate("fechaEmision"),
                        rs.getDate("fechaAutorizacion"),
                        rs.getBigDecimal("IngresosBrutos"),
                        rs.getBigDecimal("TotalCompra"),
                        rs.getBigDecimal("TotDifCotizacion"),
                        rs.getBigDecimal("TotIvaDifCotizacion"),
                        rs.getBigDecimal("SubtOp"));
                ordenesPago.add(ordenPago);

            }

            // seteo los parametros para el detalle
            buscaOrdenesPagoDetalleRs.setInt(1, comprobanteCab.getIdFactCab());

            //Recorro los resultados para los detalles
            ResultSet rsd = buscaOrdenesPagoDetalleRs.executeQuery();
            while (rsd.next()) {
                OrdenPagoDetalleResponse opDet = new OrdenPagoDetalleResponse(
                        rsd.getInt("idOPDetalle"),
                        rsd.getInt("idOPCab"),
                        rsd.getInt("item"),
                        rsd.getInt("idFactCab"),
                        rsd.getBigDecimal("pagadoDolar"),
                        rsd.getBigDecimal("importePesificado"),
                        rsd.getInt("idFormaPago"),
                        rsd.getBigDecimal("cotDolarFact"),
                        rsd.getBigDecimal("difCotizacion"),
                        rsd.getInt("idIVA"),
                        rsd.getBigDecimal("ivaDifCotizacion"));

                ordenesPagoDetalles.add(opDet);

            }

            List<Payload> comprobantes = new ArrayList<>();
            for (OrdenesPagoResponse c : ordenesPago) {
                for (OrdenPagoDetalleResponse d : ordenesPagoDetalles) {
                    if (c.getIdOPCab() == d.getIdOPCab()) {
                        c.getDetalle().add(d);
                    }
                }
                comprobantes.add(c);
            }
            
            
            // FORMA PAGO
            buscaOrdenesPagoFormaPagoRs.setInt(1, 0);
            ResultSet rsfp = buscaOrdenesPagoFormaPagoRs.executeQuery();
            while (rsfp.next()) {
                //idOPFormaPago, idOPCab, IdFormaPago, importe, fechaAcreditacion, numero, detalle
                OrdenPagoFormaPagoResponse opFp = new OrdenPagoFormaPagoResponse(
                        rsfp.getInt("idOPFormaPago"),
                        rsfp.getInt("idOPCab"),
                        rsfp.getInt("IdFormaPago"),
                        rsfp.getBigDecimal("importe"),
                        rsfp.getDate("fechaAcreditacion"),
                        rsfp.getLong("numero"),
                        rsfp.getString("detalle"));

                ordenesPagoFormaPago.add(opFp);

            }

            for (OrdenesPagoResponse c : ordenesPago) {
                for (OrdenPagoFormaPagoResponse fp : ordenesPagoFormaPago) {
                    if (c.getIdOPCab() == fp.getIdOPCab()) {
                        c.getFormaPago().add(fp); 
                    }
                }
                comprobantes.add(c);
            }
          
            buscaOrdenesPagoPieRs.setInt(1, 0);
            ResultSet rspie = buscaOrdenesPagoPieRs.executeQuery();
            while (rspie.next()) {
                //idOPFormaPago, idOPCab, IdFormaPago, importe, fechaAcreditacion, numero, detalle
                OrdenPagoPieResponse opPie = new OrdenPagoPieResponse(
                        rspie.getInt("idOPPie"),
                        rspie.getInt("idOPCab"),
                        rspie.getInt("idImpuesto"),
                         rspie.getString("detalle"),
                        rspie.getBigDecimal("alicuota"),
                        rspie.getBigDecimal("importeBase"),
                        rspie.getBigDecimal("importeImpuesto"),
                        rspie.getLong("numeroRetencion"));

                ordenesPagoPie.add(opPie);

            }
            
            for (OrdenesPagoResponse c : ordenesPago) {
                for (OrdenPagoPieResponse p : ordenesPagoPie) {
                    if (c.getIdOPCab() == p.getIdOPCab()) {
                        c.getPie().add(p); 
                    }
                }
                comprobantes.add(c);
            }
            
            

            buscaOrdenesPagoRs.getConnection().close();
            buscaOrdenesPagoDetalleRs.getConnection().close();
            buscaOrdenesPagoFormaPagoRs.getConnection().close();
            buscaOrdenesPagoPieRs.getConnection().close();
            respuesta.setArraydatos(comprobantes);
            if (ordenesPago.isEmpty()) {
                respuesta.setControl(AppCodigo.ERROR, "No se han encontrado ordenes de pago con los parametros especificados", "Cuenta: " + idPadron + " - Comprobante: " + comprobanteCab.getNumero() + " - Fecha Desde: " + sqlFechaDesde + " Fecha Hasta: " + sqlFechaHasta);
                return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
            } else {
                respuesta.setControl(AppCodigo.OK, "OrdenesPago");
                return Response.status(Response.Status.OK).entity(respuesta.toJson()).build();
            }

        } catch (Exception e) {
            respuesta.setControl(AppCodigo.ERROR, e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(respuesta.toJson()).build();
        }
    }

}
