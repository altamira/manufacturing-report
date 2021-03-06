package br.com.altamira.report.manufacturing;

import java.io.IOException;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import br.com.altamira.report.util.ReportConfig;

/**
 * Root resource (exposed at "reports" path)
 */
@Stateless
@Resource(name = "MANUFACTURE_BOM")
@Path("manufacture/bom")
public class BOMReports extends ReportConfig {

    @Inject
    private MaterialListReport materialListReport;

    @Inject
    private PaintingReport serviceOrderPaintingReport;

    @Inject
    private WarehouseReport serviceOrderWarehouseReport;

    @Inject
    private ShippingReport serviceOrderShippingReport;

    @Inject
    private EditorReport serviceOrderEditorReport;

    @Inject
    private ProductionReport serviceOrderProductionReport;

    @Inject
    private TransportationReport serviceOrderTransportationReport;

    @Inject
    private WeldReport serviceOrderWeldReport;

    @Inject
    private AllReports allReports;

    /**
     * To get the url parameters.
     */
    @Context
    protected UriInfo info;

    /**
     * The Auth Token
     */
    private String token;

    /**
     * Constructor
     *
     */
    public BOMReports() {

    }

    /**
     * Constructor
     *
     */
    public BOMReports(@QueryParam("token") String token) {
        super();
        this.token = token;
    }

    /**
     * Method handling HTTP GET requests. The returned object will be sent to
     * the client as "application/pdf" media type.
     *
     * @param req
     * @param resp
     * @param id
     * @return
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     *
     */
    @GET
    @Permission(name = "PRINT")
    @Path("/{id}")
    @Produces("application/pdf")
    public Response getAllReport(
            @Context HttpServletRequest req,
            @Context HttpServletResponse resp,
            @PathParam("id") Long id)
            throws ServletException, IOException {

        //GET THE SELECTED REPORTS REQUEST
        List<String> selectedReports = info.getQueryParameters().get("report");

        //CHECK FOR AUTH TOKEN
        /*if (checkAuth(token).getStatus() != 200) {
         return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid Token: " + token).build();
         }*/
        //AllReports allReports = new AllReports(selectedReports);
        return allReports.mergeAllReports(id, selectedReports);
    }

    /**
     * Method handling HTTP GET requests. The returned object will be sent to
     * the client as "application/pdf" media type.
     *
     * @param req
     * @param resp
     * @param id
     * @return
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     *
     */
    @GET
    @Permission(name = "PRINT")
    @Path("/{id}/checklist")
    @Produces("application/pdf")
    public Response materialListReport(
            @Context HttpServletRequest req,
            @Context HttpServletResponse resp,
            @PathParam("id") Long id)
            throws ServletException, IOException {

        //CHECK FOR AUTH TOKEN
        /*if (checkAuth(token).getStatus() != 200) {
         return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid Token: " + token).build();
         }*/
        //MaterialListReport materialListReport = new MaterialListReport();
        return materialListReport.getReport(id);

    }

    /**
     * Method handling HTTP GET requests. The returned object will be sent to
     * the client as "application/pdf" media type.
     *
     * @param req
     * @param resp
     * @param id
     * @return
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     *
     */
    @GET
    @Permission(name = "PRINT")
    @Path("/{id}/editor")
    @Produces("application/pdf")
    public Response serviceOrderEditor(
            @Context HttpServletRequest req,
            @Context HttpServletResponse resp,
            @PathParam("id") Long id)
            throws ServletException, IOException {

        //CHECK FOR AUTH TOKEN
        /*if (checkAuth(token).getStatus() != 200) {
         return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid Token: " + token).build();
         }*/
        //EditorReport serviceOrderEditorReport = new EditorReport();
        return serviceOrderEditorReport.getReport(id);

    }

    /**
     * Method handling HTTP GET requests. The returned object will be sent to
     * the client as "application/pdf" media type.
     *
     * @return
     * @return
     *
     */
    @GET
    @Permission(name = "PRINT")
    @Path("/{id}/painting")
    @Produces("application/pdf")
    public Response serviceOrderPainting(
            @Context HttpServletRequest req,
            @Context HttpServletResponse resp,
            @PathParam("id") Long id)
            throws ServletException, IOException {

        //CHECK FOR AUTH TOKEN
        /*if (checkAuth(token).getStatus() != 200) {
         return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid Token: " + token).build();
         }*/
        //PaintingReport serviceOrderPaintingReport = new PaintingReport();
        return serviceOrderPaintingReport.getReport(id);

    }

    /**
     * Method handling HTTP GET requests. The returned object will be sent to
     * the client as "application/pdf" media type.
     *
     * @return
     * @return
     *
     */
    @GET
    @Permission(name = "PRINT")
    @Path("/{id}/production")
    @Produces("application/pdf")
    public Response serviceOrderProduction(
            @Context HttpServletRequest req,
            @Context HttpServletResponse resp,
            @PathParam("id") Long id)
            throws ServletException, IOException {

        //CHECK FOR AUTH TOKEN
        /*if (checkAuth(token).getStatus() != 200) {
         return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid Token: " + token).build();
         }*/
        //ProductionReport serviceOrderProductionReport = new ProductionReport();
        return serviceOrderProductionReport.getReport(id);

    }

    /**
     * Method handling HTTP GET requests. The returned object will be sent to
     * the client as "application/pdf" media type.
     *
     * @return
     * @return
     *
     */
    @GET
    @Permission(name = "PRINT")
    @Path("/{id}/shipping")
    @Produces("application/pdf")
    public Response serviceOrderShipping(
            @Context HttpServletRequest req,
            @Context HttpServletResponse resp,
            @PathParam("id") Long id)
            throws ServletException, IOException {

        //CHECK FOR AUTH TOKEN
        /*if (checkAuth(token).getStatus() != 200) {
         return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid Token: " + token).build();
         }*/
        //ShippingReport serviceOrderShippingReport = new ShippingReport();
        return serviceOrderShippingReport.getReport(id);

    }

    /**
     * Method handling HTTP GET requests. The returned object will be sent to
     * the client as "application/pdf" media type.
     *
     * @return
     * @return
     *
     */
    @GET
    @Permission(name = "PRINT")
    @Path("/{id}/transportation")
    @Produces("application/pdf")
    public Response serviceOrderTransportation(
            @Context HttpServletRequest req,
            @Context HttpServletResponse resp,
            @PathParam("id") Long id)
            throws ServletException, IOException {

        //CHECK FOR AUTH TOKEN
        /*if (checkAuth(token).getStatus() != 200) {
         return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid Token: " + token).build();
         }*/
        //TransportationReport serviceOrderTransportationReport = new TransportationReport();
        return serviceOrderTransportationReport.getReport(id);

    }

    /**
     * Method handling HTTP GET requests. The returned object will be sent to
     * the client as "application/pdf" media type.
     *
     * @return
     * @return
     *
     */
    @GET
    @Permission(name = "PRINT")
    @Path("/{id}/warehouse")
    @Produces("application/pdf")
    public Response serviceOrderWarehouse(
            @Context HttpServletRequest req,
            @Context HttpServletResponse resp,
            @PathParam("id") Long id)
            throws ServletException, IOException {

        //CHECK FOR AUTH TOKEN
        /*if (checkAuth(token).getStatus() != 200) {
         return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid Token: " + token).build();
         }*/
        //WarehouseReport serviceOrderWarehouseReport = new WarehouseReport();
        return serviceOrderWarehouseReport.getReport(id);

    }

    /**
     * Method handling HTTP GET requests. The returned object will be sent to
     * the client as "application/pdf" media type.
     *
     * @return
     * @return
     *
     */
    @GET
    @Permission(name = "PRINT")
    @Path("/{id}/weld")
    @Produces("application/pdf")
    public Response serviceOrderWeld(
            @Context HttpServletRequest req,
            @Context HttpServletResponse resp,
            @PathParam("id") Long id)
            throws ServletException, IOException {

        //CHECK FOR AUTH TOKEN
        /*if (checkAuth(token).getStatus() != 200) {
         return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid Token: " + token).build();
         }*/
        //WeldReport serviceOrderWeldReport = new WeldReport();
        return serviceOrderWeldReport.getReport(id);

    }

}
