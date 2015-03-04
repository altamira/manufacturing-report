package br.com.altamira.report.manufacturing;

import java.io.IOException;

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
@Path("shipping/execution")
public class ShippingExecutionReports extends ReportConfig {
	
	@Inject
	private PackingListReport packingListReport;
	
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
    public ShippingExecutionReports() {
    	
    }
    
    /**
     * Constructor
     *
     */
    public ShippingExecutionReports(@QueryParam("token") String token) {
    	super();
        this.token = token;
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
    @Path("/packinglist/{id}")
    @Produces("application/pdf")
    public Response packingList(
            @Context HttpServletRequest req,
            @Context HttpServletResponse resp,
            @PathParam("id") Long id)
            throws ServletException, IOException {

        //CHECK FOR AUTH TOKEN
        /*if (checkAuth(token).getStatus() != 200) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid Token: " + token).build();
        }*/

        return packingListReport.getReport(id);

    }
}
