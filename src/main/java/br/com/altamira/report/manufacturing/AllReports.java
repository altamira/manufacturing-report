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

	private List<String> selectedReports;

	public AllReports(List<String> selectedReports) {
		this.selectedReports = selectedReports;
	}

	public  Response mergeAllReports(@PathParam("id") String id) throws ServletException, IOException {

		byte[] pdf = null;
		JasperPrint jasperPrintAll = null;
		List<JRPrintPage> pages;

		if(this.selectedReports.contains("checklist")) {
			JasperPrint jasperPrintChecklist;
			MaterialListReport materialListReport = new MaterialListReport();
			jasperPrintChecklist = materialListReport.getPDF(id);

			if(jasperPrintAll == null) {
				jasperPrintAll = jasperPrintChecklist;
			}
		}

		if(this.selectedReports.contains("painting")) {
			JasperPrint jasperPrintPainting;
			PaintingReport serviceOrderPaintingReport = new PaintingReport();
			jasperPrintPainting = serviceOrderPaintingReport.getPDF(id);
			
			if(jasperPrintAll == null) {
				jasperPrintAll = jasperPrintPainting;
			}
			else {
				pages = jasperPrintPainting.getPages();
				for (int j = 0; j < pages.size(); j++) {
					JRPrintPage object = (JRPrintPage)pages.get(j);
					jasperPrintAll.addPage(object);

				}
			}
		}

		if(this.selectedReports.contains("production")) {
			JasperPrint jasperPrintProduction;
			ProductionReport serviceOrderProductionReport = new ProductionReport();
			jasperPrintProduction = serviceOrderProductionReport.getPDF(id);
			if(jasperPrintAll == null) {
				jasperPrintAll = jasperPrintProduction;
			}
			else {
				pages = jasperPrintProduction.getPages();
				for (int j = 0; j < pages.size(); j++) {
					JRPrintPage object = (JRPrintPage)pages.get(j);
					jasperPrintAll.addPage(object);

				}
			}
		}

		if(this.selectedReports.contains("shipping")) {
			JasperPrint jasperPrintShipping;
			ShippingReport serviceOrderShippingReport = new ShippingReport();
			jasperPrintShipping = serviceOrderShippingReport.getPDF(id);
			if(jasperPrintAll == null) {
				jasperPrintAll = jasperPrintShipping;
			}
			else {
				pages = jasperPrintShipping.getPages();
				for (int j = 0; j < pages.size(); j++) {
					JRPrintPage object = (JRPrintPage)pages.get(j);
					jasperPrintAll.addPage(object);

				}
			}
		}

		if(this.selectedReports.contains("transport")) {
			JasperPrint jasperPrintTransportation;
			TransportationReport serviceOrderTransportationReport = new TransportationReport();
			jasperPrintTransportation = serviceOrderTransportationReport.getPDF(id);
			if(jasperPrintAll == null) {
				jasperPrintAll = jasperPrintTransportation;
			}
			else {
				pages = jasperPrintTransportation.getPages();
				for (int j = 0; j < pages.size(); j++) {
					JRPrintPage object = (JRPrintPage)pages.get(j);
					jasperPrintAll.addPage(object);

				}
			}
		}

		if(this.selectedReports.contains("warehouse")) {
			JasperPrint jasperPrintWarehouse;
			WarehouseReport serviceOrderWarehouseReport = new WarehouseReport();
			jasperPrintWarehouse = serviceOrderWarehouseReport.getPDF(id);
			if(jasperPrintAll == null) {
				jasperPrintAll = jasperPrintWarehouse;
			}
			else {
				pages = jasperPrintWarehouse.getPages();
				for (int j = 0; j < pages.size(); j++) {
					JRPrintPage object = (JRPrintPage)pages.get(j);
					jasperPrintAll.addPage(object);

				}
			}
		}

		if(this.selectedReports.contains("weld")) {
			JasperPrint jasperPrintWeld;
			WeldReport serviceOrderWeldReport = new WeldReport();
			jasperPrintWeld = serviceOrderWeldReport.getPDF(id);
			if(jasperPrintAll == null) {
				jasperPrintAll = jasperPrintWeld;
			}
			else {
				pages = jasperPrintWeld.getPages();
				for (int j = 0; j < pages.size(); j++) {
					JRPrintPage object = (JRPrintPage)pages.get(j);
					jasperPrintAll.addPage(object);

				}
			}
		}

		if(this.selectedReports.contains("editor")) {
			JasperPrint jasperPrintEditor;
			EditorReport serviceOrderEditorReport = new EditorReport();
			jasperPrintEditor = serviceOrderEditorReport.getPDF(id);
			if(jasperPrintAll == null) {
				jasperPrintAll = jasperPrintEditor;
			}
			else {

				pages = jasperPrintEditor.getPages();
				for (int j = 0; j < pages.size(); j++) {
					JRPrintPage object = (JRPrintPage)pages.get(j);
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

}
