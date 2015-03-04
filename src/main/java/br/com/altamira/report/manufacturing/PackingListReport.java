package br.com.altamira.report.manufacturing;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.ServletException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import br.com.altamira.data.dao.shipping.execution.PackingListDao;
import br.com.altamira.data.model.shipping.execution.BOM;
import br.com.altamira.data.model.shipping.execution.Delivered;
import br.com.altamira.data.model.shipping.execution.PackingList;
import br.com.altamira.report.util.ReportConfig;

@Stateless
public class PackingListReport extends ReportConfig {

	@EJB
	protected PackingListDao packingListDao;
	
    public PackingList getData(Long id) {

    	PackingList packingListData = null;
    	
		//Client client = ClientBuilder.newClient();
        //WebTarget webTarget = client.target("http://localhost:8080/manufacturing-report/mfg-order.json");
        //MfgOrderDataBean mfgOrderData = webTarget.path("").request(MediaType.APPLICATION_JSON).get(MfgOrderDataBean.class);
        //Hardcode data for the PackingListReport since the real API is not ready.
        
    	packingListData = packingListDao.find(id);

        return packingListData;
    }
    
    public Response getReport(Long id) {
    	JasperPrint jasperPrint;
    	byte[] pdf = null;
    	
    	//PRINT THE PDF REPORT
    	try {
    		jasperPrint = this.getPDF(id);
    		if (jasperPrint == null) {
                return Response.status(Status.NOT_FOUND).entity("Não foi possivel carregar o relatorio !").build();
            }
    		pdf = JasperExportManager.exportReportToPdf(jasperPrint);
    		ByteArrayInputStream pdfStream = new ByteArrayInputStream(pdf);
    		
    		Response.ResponseBuilder response = Response.ok(pdfStream);
            response.header("Content-Disposition", "inline; filename=PackingList Report.pdf");
            return response.build();
    	} catch(JRException | ServletException | IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	return null;
    }
    
    public JasperPrint getPDF(Long id) throws ServletException, IOException {
    	JasperPrint jasperPrint = null;
    	
    	PackingList packingListData = this.getData(id);
    	if (packingListData == null) {
            return jasperPrint;
        }
    	
    	BOM bomData = packingListData.getBOM();
    	
    	//FORMATING ORDER DATE
        String orderDateDisplay = new java.text.SimpleDateFormat("dd/MM/yyyy").format(bomData.getCreated());
        
        //FORMATING DELIVERT DATE
        String deliveryDateDisplay = new java.text.SimpleDateFormat("w/yyyy").format(bomData.getDelivery());
        String weekString = new java.text.SimpleDateFormat("w").format(bomData.getDelivery());
        int weekNo = Integer.parseInt(weekString);
        String yearString = new java.text.SimpleDateFormat("yyyy").format(bomData.getDelivery());
        int yearNo = Integer.parseInt(yearString);
        
        // CALCULATE THE 1ST DAY AND LAST DAY OF THE WEEK
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.WEEK_OF_YEAR, weekNo);
        calendar.set(Calendar.YEAR, yearNo);
        // Get the FIRST day of week.
        Date date = calendar.getTime();
        String startDateDisplay = new java.text.SimpleDateFormat("dd/MM/yyyy").format(date);
        // Get the LAST day of week.
        calendar.add(Calendar.DATE, 6);
        Date endDate = calendar.getTime();
        String endDateDisplay = new java.text.SimpleDateFormat("dd/MM/yyyy").format(endDate);
        String deliveryWeekDisplay = startDateDisplay + " a " + endDateDisplay;
        
        Map<String, Object> parameters = new HashMap<String, Object>();
        //SET THE PARAMETERS FOR JASPER REPORT
        parameters.put("Title", "Romaneio de Expedição("+id+")");
        parameters.put("UserName", ReportConfig.userName);
        parameters.put("Customer", bomData.getCustomer());
        parameters.put("Representative", bomData.getRepresentative());
        parameters.put("OrderID", bomData.getNumber());
        parameters.put("LogoImage", this.getLogo());
        parameters.put("DateRequest", orderDateDisplay);
        parameters.put("DeliveryTime", deliveryDateDisplay);
        parameters.put("DeliveryWeek", deliveryWeekDisplay);
        parameters.put("NoBudget", bomData.getQuotation());
        parameters.put("NoProject", bomData.getProject());
        parameters.put("Finish", bomData.getFinish());
        parameters.put("Comment", bomData.getComment());
        
        ArrayList<OrderItemProductDataBean> dataList = new ArrayList<OrderItemProductDataBean>();
        Set<Delivered> OrderItemList = packingListData.getDelivered();
        
        for(Delivered delivered : OrderItemList)
        {
        	OrderItemProductDataBean dataListObj = new OrderItemProductDataBean();
        	dataListObj.setColor(delivered.getComponent().getColor().getCode());
        	dataListObj.setDescription(delivered.getComponent().getDescription());
        	dataListObj.setQuantity(delivered.getQuantity().getValue());
        	dataListObj.setWeight(delivered.getComponent().getWeight().getValue());
        	
        	dataList.add(dataListObj);
        }
        
        Collections.sort(dataList, new Comparator<OrderItemProductDataBean>() {
        	
        	@Override
        	public int compare(OrderItemProductDataBean o1, OrderItemProductDataBean o2) {
        		
        		return o1.getDescription().compareTo(o2.getDescription());
        	}
        	
        });
        
        //GET THE JASPER FILE
        InputStream reportStream = getClass().getResourceAsStream("/reports/packinglist/packinglist-report.jasper");
        if (reportStream == null) {
            return null;
        }
        
        //PRINT THE PDF REPORT
        try {
            JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);
            jasperPrint = JasperFillManager.fillReport(reportStream, parameters, beanColDataSource);
            return jasperPrint;
        } catch (JRException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
        
    }

/*    public Response getReport(Long code) {

        JasperPrint jasperPrint;
        byte[] pdf = null;

        //MfgOrderReport mfgOrderReport = new MfgOrderReport();
        //MfgOrderDataBean mfgReportData = this.getData(code);
        PackingList mfgReportData = this.getData(code);
        String componentCodeData = "";
        String componentNameData = "";

        Map<String, Object> parameters = new HashMap<String, Object>();
        //SET THE PARAMETERS
        parameters.put("Title", "ORDEM FABRICAÇÃO");
        parameters.put("UserName", ReportConfig.userName);
        parameters.put("Code", mfgReportData.getCode());
        parameters.put("Description", mfgReportData.getDescription());
        parameters.put("Date", mfgReportData.getDate());
        parameters.put("Sector", mfgReportData.getSector());
        parameters.put("IssuedBy", mfgReportData.getIssuedBy());
        parameters.put("Operation", mfgReportData.getOperation());
        parameters.put("LogoImage", this.getLogo());

        List<MfgOrderComponent> components = mfgReportData.getComponents();
        for (int i = 0; i < components.size(); i++) {
            String newLineText = "\r\n";
            if (i == (components.size() - 1)) {
                newLineText = "";
            }
            componentCodeData = componentCodeData + components.get(i).getCode() + newLineText;
            componentNameData = componentNameData + components.get(i).getName() + newLineText;
        }
        parameters.put("ComponentCodeDataList", componentCodeData);
        parameters.put("ComponentNameDataList", componentNameData);

        List<MfgOrderMaterial> OrderMaterial = mfgReportData.getMaterials();

        //GET THE JASPER FILE
        InputStream reportStream = getClass().getResourceAsStream("/reports/packinglist/packinglist-report.jasper");
        if (reportStream == null) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Não foi possivel carregar o relatorio !").build();
        }

        //PRINT THE PDF REPORT
        try {
            JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(OrderMaterial);
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
    }*/

}
