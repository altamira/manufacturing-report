package br.com.altamira.report.manufacturing;

import java.io.IOException;

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

import br.com.altamira.report.util.ReportConfig;



/**
 * Root resource (exposed at "reports" path)
 */

@Path("manufacturing")
public class Reports  extends ReportConfig{

	/**
	 * The Auth Token
	 */
	private String token;

	/**
	 * Constructor
	 *
	 */
	public Reports(@QueryParam("token") String token) {
		super();
		this.token = token;
	}



	/**
	 * Method handling HTTP GET requests. The returned object will be sent
	 * to the client as "application/pdf" media type.
	 * @return 
	 * @return 
	 *
	 */
	@GET @Path("/process/{id}")
	@Produces("application/pdf") 
	public  Response manufacturingProcess(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse resp, 
			@PathParam("id") String id)
					throws ServletException, IOException {

		//CHECK FOR AUTH TOKEN
		if (checkAuth(token).getStatus() != 200) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid Token: " + token).build();
		}

		ProcessReport processReport = new ProcessReport();
		return processReport.getReport(id);	

	}
	
	/**
	 * Method handling HTTP GET requests. The returned object will be sent
	 * to the client as "application/pdf" media type.
	 * @return 
	 * @return 
	 *
	 */
	@GET @Path("/bom/{id}/checklist")
	@Produces("application/pdf") 
	public  Response materialListReport(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse resp, 
			@PathParam("id") String id) 
					throws ServletException, IOException {

		//CHECK FOR AUTH TOKEN
		if (checkAuth(token).getStatus() != 200) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid Token: " + token).build();
		}
		
		MaterialListReport materialListReport = new MaterialListReport();
		return materialListReport.getReport(id);	

	}
	
	/**
	 * Method handling HTTP GET requests. The returned object will be sent
	 * to the client as "application/pdf" media type.
	 * @return 
	 * @return 
	 *
	 */
	@GET @Path("/bom/{id}/editor")
	@Produces("application/pdf") 
	public  Response serviceOrderEditor(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse resp, 
			@PathParam("id") String id) 
					throws ServletException, IOException {

		//CHECK FOR AUTH TOKEN
		if (checkAuth(token).getStatus() != 200) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid Token: " + token).build();
		}
		
		EditorReport serviceOrderEditorReport = new EditorReport();
		return serviceOrderEditorReport.getReport(id);	

	}
	
	/**
	 * Method handling HTTP GET requests. The returned object will be sent
	 * to the client as "application/pdf" media type.
	 * @return 
	 * @return 
	 *
	 */
	@GET @Path("/bom/{id}/painting")
	@Produces("application/pdf") 
	public  Response serviceOrderPainting(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse resp, 
			@PathParam("id") String id) 
					throws ServletException, IOException {

		//CHECK FOR AUTH TOKEN
		if (checkAuth(token).getStatus() != 200) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid Token: " + token).build();
		}
		
		PaintingReport serviceOrderPaintingReport = new PaintingReport();
		return serviceOrderPaintingReport.getReport(id);	

	}
	
	/**
	 * Method handling HTTP GET requests. The returned object will be sent
	 * to the client as "application/pdf" media type.
	 * @return 
	 * @return 
	 *
	 */
	@GET @Path("/bom/{id}/production")
	@Produces("application/pdf") 
	public  Response serviceOrderProduction(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse resp, 
			@PathParam("id") String id) 
					throws ServletException, IOException {

		//CHECK FOR AUTH TOKEN
		if (checkAuth(token).getStatus() != 200) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid Token: " + token).build();
		}
		
		ProductionReport serviceOrderProductionReport = new ProductionReport();
		return serviceOrderProductionReport.getReport(id);	

	}
	
	/**
	 * Method handling HTTP GET requests. The returned object will be sent
	 * to the client as "application/pdf" media type.
	 * @return 
	 * @return 
	 *
	 */
	@GET @Path("/bom/{id}/shipping")
	@Produces("application/pdf") 
	public  Response serviceOrderShipping(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse resp, 
			@PathParam("id") String id) 
					throws ServletException, IOException {

		//CHECK FOR AUTH TOKEN
		if (checkAuth(token).getStatus() != 200) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid Token: " + token).build();
		}
		
		ShippingReport serviceOrderShippingReport = new ShippingReport();
		return serviceOrderShippingReport.getReport(id);	

	}
	
	/**
	 * Method handling HTTP GET requests. The returned object will be sent
	 * to the client as "application/pdf" media type.
	 * @return 
	 * @return 
	 *
	 */
	@GET @Path("/bom/{id}/transportation")
	@Produces("application/pdf") 
	public  Response serviceOrderTransportation(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse resp, 
			@PathParam("id") String id) 
					throws ServletException, IOException {
		
		//CHECK FOR AUTH TOKEN
		if (checkAuth(token).getStatus() != 200) {
			return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid Token: " + token).build();
		}

		TransportationReport serviceOrderTransportationReport = new TransportationReport();
		return serviceOrderTransportationReport.getReport(id);	

	}

}
