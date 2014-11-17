package br.com.altamira.report.manufacturing;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.altamira.data.model.manufacturing.process.Consume;
import br.com.altamira.data.model.manufacturing.process.Operation;
import br.com.altamira.data.model.manufacturing.process.Process;
import br.com.altamira.data.model.manufacturing.process.Produce;
import br.com.altamira.data.model.manufacturing.process.Revision;
import br.com.altamira.data.model.manufacturing.process.Use;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class ProcessReport extends ReportConfig {
	
	public Process getData(String id) {
		
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target("http://data.altamira.com.br/manufacturing/process/");
		Process processData = webTarget.path(id).request(MediaType.APPLICATION_JSON).get(Process.class);
		return processData;
	}
	
	public Response getReport(String id) {
		
		ArrayList<ProcessReportDisplayDataBean> dataList = new ArrayList<ProcessReportDisplayDataBean>();
		JasperPrint jasperPrint;
		byte[] pdf = null;
		
		Process processReportData = this.getData(id);
		List<Consume> consumes = null;
		String revisionByData = "";
		String revisionDateData = "";
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		//SET THE PARAMETERS
		parameters.put("Title", "Processo de Fabricação");
     	parameters.put("UserName", ReportConfig.userName);
     	parameters.put("Code", processReportData.getCode());
     	parameters.put("Description", processReportData.getDescription());
     	parameters.put("Color", processReportData.getColor());
     	parameters.put("Weight", processReportData.getWeight());
     	parameters.put("Width", processReportData.getWidth());
     	parameters.put("Length", processReportData.getLength());
     	parameters.put("Finish", processReportData.getFinish());
     	parameters.put("LogoImage", this.getLogo());
		
		List<Revision> revisions = processReportData.getRevision();
		for (int i = 0; i < revisions.size(); i++) {	
			String newLineText = "\r\n";
			if(i == (revisions.size() - 1)) {
				newLineText = "";
			}
            revisionByData = revisionByData + revisions.get(i).getBy()  + newLineText;
            //TODO check data for date format
            //revisionDateData = revisionDateData + new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date(revision.get(i).getDate())) + newLineText;
            revisionDateData = revisions.get(i).getDate() + newLineText;

        }
		parameters.put("RevisionByData", revisionByData);
     	parameters.put("RevisionDateData", revisionDateData);
     	
		List<Operation> operations = processReportData.getOperation();
		for (int i = 0; i < operations.size(); i++) {
			String operationName = operations.get(i).getSequence() + " - " + operations.get(i).getName();
			String inputCodeList = "";
			String inputMaterialList = "";
			String inputQtyList = "";
			String outputCodeList = "";
			String outputMaterialList = "";
			String outputQtyList = "";
			String useCodeList = "";
			String useMaterialList = "";
			String useQtyList = "";
			String descriptionOperation = operations.get(i).getDescription();
			
            consumes = operations.get(i).getConsume();

            for (int j = 0; j < consumes.size(); j++) {
            	String newLineText = "\r\n";
            	if(j == (consumes.size() - 1)) {
            		newLineText = "";
            	}
            	
            	String qtyText = consumes.get(j).getQuantity().getValue() + 
            			" " + 
            			consumes.get(j).getQuantity().getUnit().getSymbol();
            	inputCodeList = inputCodeList + consumes.get(j).getCode()  + newLineText;
            	inputMaterialList = inputMaterialList + consumes.get(j).getDescription()  + newLineText;
            	inputQtyList = inputQtyList + qtyText  + newLineText;

            }
    		
    		List<Produce> produces = operations.get(i).getProduce();
    		for (int j = 0; j < produces.size(); j++) {
    			String newLineText = "\r\n";
    			if(j == (produces.size() - 1)) {
    				newLineText = "";
    			}
    			
    			String qtyText = produces.get(j).getQuantity().getValue() +
    					" " +
    					produces.get(j).getQuantity().getUnit().getSymbol();
    			outputCodeList = outputCodeList + produces.get(j).getCode()  + newLineText;
    			outputMaterialList = outputMaterialList + produces.get(j).getDescription()  + newLineText;
    			outputQtyList = outputQtyList + qtyText  + newLineText;
    			
            }
    		
    		List<Use> uses = operations.get(i).getUse();
    		for (int j = 0; j < uses.size(); j++) {
    			String newLineText = "\r\n";
    			if(j == (uses.size() - 1)) {
    				newLineText = "";
    			}
    			
    			String qtyText = uses.get(j).getQuantity().getValue() + 
    					" " +
    					uses.get(j).getQuantity().getUnit().getSymbol();
    			
    			useCodeList = useCodeList + uses.get(j).getCode()  + newLineText;
    			useMaterialList = useMaterialList + uses.get(j).getDescription()  + newLineText;
    			useQtyList = useQtyList + qtyText  + newLineText;
    			
            }
    		
    		//CREATE AND ADD THE OBJECT FOR MFG OPERATION DATA LIST
    		//CREATE THE OBJECT
    		ProcessReportDisplayDataBean dataBean = new ProcessReportDisplayDataBean();
			dataBean.setName(operationName);
			dataBean.setDescriptionOperation(descriptionOperation);
			dataBean.setScetchOfOperation(decodeToImage(operations.get(i).getSketch().getImage()));
			dataBean.setInputCodeList(inputCodeList);
			dataBean.setInputMaterialList(inputMaterialList);
			dataBean.setInputQtyList(inputQtyList);
			dataBean.setOutputCodeList(outputCodeList);
			dataBean.setOutputMaterialList(outputMaterialList);
			dataBean.setOutputQtyList(outputQtyList);
			dataBean.setUseCodeList(useCodeList);
			dataBean.setUseMaterialList(useMaterialList);
			dataBean.setUseQtyList(useQtyList);

			//ADD THE OBJECT TO DATALIST
			dataList.add(dataBean);	
            
        }
		
		//GET THE JASPER FILE
		InputStream reportStream = getClass().getResourceAsStream("/reports/process/process-report.jasper");
		if (reportStream == null) {
			return Response.status(Status.NOT_FOUND).entity("Não foi possivel carregar o relatorio !").build();
		}
		
		//PRINT THE PDF REPORT
    	try {
    		JRBeanCollectionDataSource beanColDataSource =  new JRBeanCollectionDataSource(dataList);
    		jasperPrint = JasperFillManager.fillReport(reportStream, parameters, beanColDataSource);
    		pdf = JasperExportManager.exportReportToPdf(jasperPrint);
    		ByteArrayInputStream pdfStream = new ByteArrayInputStream(pdf);

            Response.ResponseBuilder response = Response.ok(pdfStream);
            response.header("Content-Disposition", "inline; filename=Request Report.pdf");
            return response.build();
    	} catch (JRException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
		return null;
	}

}
