package br.com.altamira.report.manufacturing;

import br.com.altamira.data.dao.manufacture.bom.BOMDao;
import br.com.altamira.data.model.manufacture.bom.BOM;
import br.com.altamira.data.model.manufacture.bom.BOMItem;
import br.com.altamira.data.model.manufacture.bom.BOMItemPart;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.altamira.report.util.ReportConfig;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@RequestScoped
public class MaterialListReport extends ReportConfig {

    @Inject
    private BOMDao bomDao;

    /**
     *
     * @param id
     * @return object OrderData
     */
    public BOM getData(Long id) {

        BOM OrderData = null;

        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(DATA_BASE_URL + "/manufacturing/bom/");
        OrderData = webTarget.path(id.toString()).request(MediaType.APPLICATION_JSON).get(BOM.class);

        return OrderData;
    }

    public Response getReport(Long id) throws ServletException, IOException {
        try {
            JasperPrint jasperPrint;
            byte[] pdf = null;

            jasperPrint = this.getPDF(id);
            if (jasperPrint == null) {
                return Response.status(Status.NOT_FOUND).entity("Não foi possivel carregar o relatorio !").build();
            }
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

    public JasperPrint getPDF(long id) throws ServletException, IOException {
        try {
            JasperPrint jasperPrint = null;
            BOM reportData = this.getData(id);

            if (reportData == null) {
                return jasperPrint;
            }

            //FORMATING ORDER DATE
            String orderDateDisplay = new java.text.SimpleDateFormat("dd/MM/yyyy").format(reportData.getCreated());

            //FORMATING DELIVERT DATE
            String deliveryDateDisplay = new java.text.SimpleDateFormat("w/yyyy").format(reportData.getDelivery());
            String weekString = new java.text.SimpleDateFormat("w").format(reportData.getDelivery());
            int weekNo = Integer.parseInt(weekString);
            String yearString = new java.text.SimpleDateFormat("yyyy").format(reportData.getDelivery());
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
            parameters.put("Title", "Lista de Material - " + reportData.getNumber());
            parameters.put("UserName", ReportConfig.userName);
            parameters.put("Customer", reportData.getCustomer());
            parameters.put("Representative", reportData.getRepresentative());
            parameters.put("OrderID", reportData.getNumber());
            parameters.put("LogoImage", this.getLogo());
            parameters.put("DateRequest", orderDateDisplay);
            parameters.put("DeliveryTime", deliveryDateDisplay);
            parameters.put("DeliveryWeek", deliveryWeekDisplay);
            parameters.put("NoBudget", reportData.getQuotation());
            parameters.put("NoProject", reportData.getProject());
            parameters.put("Finish", reportData.getFinish());
            parameters.put("Comment", reportData.getComment());

            ArrayList<OrderItemProductDataBean> dataList = new ArrayList<OrderItemProductDataBean>();
            List<BOMItem> OrderItemList = reportData.getItems();
            for (int i = 0; i < OrderItemList.size(); i++) {
                List<BOMItemPart> OrderItemProductList = OrderItemList.get(i).getParts();
                for (int j = 0; j < OrderItemProductList.size(); j++) {
                    //System.out.println(OrderItemProductList.get(j).getQuantity());
                    OrderItemProductDataBean dataListObj = new OrderItemProductDataBean();
                    dataListObj.setItemCode(OrderItemList.get(i).getItem());
                    dataListObj.setItemDescription(OrderItemList.get(i).getDescription());
                    dataListObj.setColor(OrderItemProductList.get(j).getColor().getCode());
                    dataListObj.setDescription(OrderItemProductList.get(j).getDescription());
                    dataListObj.setNote("NOTE");
                    dataListObj.setQuantity(OrderItemProductList.get(j).getQuantity().getValue());
                    dataListObj.setWeight(OrderItemProductList.get(j).getWeight().getValue());

                    dataList.add(dataListObj);
                }
            }

            //GET THE JASPER FILE
            InputStream reportStream = getClass().getResourceAsStream("/reports/checklist/checklist-report.jasper");
            if (reportStream == null) {
                return null;
            }

            //PRINT THE PDF REPORT
            JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);
            jasperPrint = JasperFillManager.fillReport(reportStream, parameters, beanColDataSource);

            return jasperPrint;
        } catch (JRException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
