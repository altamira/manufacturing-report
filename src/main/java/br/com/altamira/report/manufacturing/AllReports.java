package br.com.altamira.report.manufacturing;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

public class AllReports {
	
	 public  Response mergeAllReports(@PathParam("id") String id) throws ServletException, IOException {
			
			byte[] pdf = null;
			JasperPrint jasperPrintAll;
			JasperPrint jasperPrintChecklist;
			JasperPrint jasperPrintPainting;
			JasperPrint jasperPrintProduction;
			JasperPrint jasperPrintShipping;
			JasperPrint jasperPrintTransportation;
			JasperPrint jasperPrintwarehouse;
			JasperPrint jasperPrintWeld;
			JasperPrint jasperPrintEditor;
			
			MaterialListReport materialListReport = new MaterialListReport();
			PaintingReport serviceOrderPaintingReport = new PaintingReport();
			ProductionReport serviceOrderProductionReport = new ProductionReport();
			ShippingReport serviceOrderShippingReport = new ShippingReport();
			EditorReport serviceOrderEditorReport = new EditorReport();
			WeldReport serviceOrderWeldReport = new WeldReport();
			WarehouseReport serviceOrderWarehouseReport = new WarehouseReport();
			TransportationReport serviceOrderTransportationReport = new TransportationReport();
				
			jasperPrintChecklist = materialListReport.getPDF(id);
			jasperPrintPainting = serviceOrderPaintingReport.getPDF(id);
			jasperPrintProduction = serviceOrderProductionReport.getPDF(id);
			jasperPrintShipping = serviceOrderShippingReport.getPDF(id);
			jasperPrintTransportation = serviceOrderTransportationReport.getPDF(id);
			jasperPrintwarehouse = serviceOrderWarehouseReport.getPDF(id);
			jasperPrintWeld = serviceOrderWeldReport.getPDF(id);
			jasperPrintEditor = serviceOrderEditorReport.getPDF(id);
			
			//Initialize with the 1st Report
			jasperPrintAll = jasperPrintChecklist;
			
			List<JRPrintPage> pages;
			pages = jasperPrintPainting .getPages();
	        for (int j = 0; j < pages.size(); j++) {
	        	JRPrintPage object = (JRPrintPage)pages.get(j);
	        	jasperPrintAll.addPage(object);
	    
	        }
	        
	        pages = jasperPrintProduction .getPages();
	        for (int j = 0; j < pages.size(); j++) {
	        	JRPrintPage object = (JRPrintPage)pages.get(j);
	        	jasperPrintAll.addPage(object);
	    
	        }
	        
	        pages = jasperPrintShipping .getPages();
	        for (int j = 0; j < pages.size(); j++) {
	        	JRPrintPage object = (JRPrintPage)pages.get(j);
	        	jasperPrintAll.addPage(object);
	    
	        }
	        
	        pages = jasperPrintTransportation .getPages();
	        for (int j = 0; j < pages.size(); j++) {
	        	JRPrintPage object = (JRPrintPage)pages.get(j);
	        	jasperPrintAll.addPage(object);
	    
	        }
	        
	        pages = jasperPrintwarehouse .getPages();
	        for (int j = 0; j < pages.size(); j++) {
	        	JRPrintPage object = (JRPrintPage)pages.get(j);
	        	jasperPrintAll.addPage(object);
	    
	        }
	        
	        pages = jasperPrintWeld .getPages();
	        for (int j = 0; j < pages.size(); j++) {
	        	JRPrintPage object = (JRPrintPage)pages.get(j);
	        	jasperPrintAll.addPage(object);
	    
	        }
	        
	        pages = jasperPrintEditor .getPages();
	        for (int j = 0; j < pages.size(); j++) {
	        	JRPrintPage object = (JRPrintPage)pages.get(j);
	        	jasperPrintAll.addPage(object);
	    
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
}
