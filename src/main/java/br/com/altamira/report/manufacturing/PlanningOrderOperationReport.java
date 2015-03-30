package br.com.altamira.report.manufacturing;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.ServletException;
import javax.ws.rs.core.Response;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import br.com.altamira.data.dao.manufacture.planning.OperationDao;
import br.com.altamira.data.model.manufacture.planning.Component;
import br.com.altamira.report.util.ReportConfig;

@Stateless
public class PlanningOrderOperationReport extends ReportConfig {

	@EJB
	protected OperationDao operationDao;
	
	private List<String> selectedoperations;

	/**
	 * 
	 * @param orderId
	 * @param operationId
	 * @return
	 */
	public List<Component> getData(Long orderId, Long operationId) {
		
		List<Component> reportData = operationDao.getOperationDataForReport(orderId, operationId);
		
		return reportData;
	}
	
	public Response getReport(Long orderId, List<String> selectedoperations) throws ServletException, IOException {

		byte[] pdf = null;
		JasperPrint jasperPrintAll = null;
		List<JRPrintPage> pages;
		this.selectedoperations = selectedoperations;

		for(String operationId : selectedoperations)
		{
			JasperPrint jasperPrint = getPDF(orderId, Long.parseLong(operationId));

			if (jasperPrintAll == null) {
				jasperPrintAll = jasperPrint;
			}
			else {
				pages = jasperPrint.getPages();
				for (int j = 0; j < pages.size(); j++) {
					JRPrintPage object = (JRPrintPage) pages.get(j);
					jasperPrintAll.addPage(object);
				}
			}
		}

		try {
			pdf = JasperExportManager.exportReportToPdf(jasperPrintAll);
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ByteArrayInputStream pdfStream = new ByteArrayInputStream(pdf);

		Response.ResponseBuilder response = Response.ok(pdfStream);
		response.header("Content-Disposition", "inline; filename=Request Report.pdf");
		return response.build();
	}
	
	 public JasperPrint getPDF(long orderId, long operationId) throws ServletException, IOException {
		 
		 // TODO - generate report for operation report.
		 
		 return null;
	 }
	
}
