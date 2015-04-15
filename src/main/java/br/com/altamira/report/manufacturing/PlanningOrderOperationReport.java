package br.com.altamira.report.manufacturing;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.ServletException;
import javax.ws.rs.core.Response;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import br.com.altamira.data.dao.manufacture.planning.OperationDao;
import br.com.altamira.data.model.manufacture.planning.Component;
import br.com.altamira.data.model.manufacture.planning.Operation;
import br.com.altamira.data.model.manufacture.planning.Produce;
import br.com.altamira.report.util.ReportConfig;

import com.google.common.base.Function;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

@Stateless
@Resource(name = "MANUFACTURE_PLANNING")
@Path("manufacture/planning")
public class PlanningOrderOperationReport extends ReportConfig {

    @EJB
    protected OperationDao operationDao;

    private List<String> selectedoperations;

    private Operation operation;

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
    public PlanningOrderOperationReport() {

    }

    /**
     * Constructor
     *
     */
    public PlanningOrderOperationReport(@QueryParam("token") String token) {
        super();
        this.token = token;
    }
    
    /**
     *
     * @param orderId
     * @param operationId
     * @return
     */
    public List<Component> getData(Long orderId, Long operationId) {

        List<Component> reportData = operationDao.getOperationDataForReport(orderId, operationId);
        operation = operationDao.find(operationId);
        return reportData;
    }

    public Response getReport(Long orderId, List<String> selectedoperations) throws ServletException, IOException {

        byte[] pdf = null;
        JasperPrint jasperPrintAll = null;
        List<JRPrintPage> pages;
        this.selectedoperations = selectedoperations;

        for (String operationId : selectedoperations) {
            JasperPrint jasperPrint = getPDF(orderId, Long.parseLong(operationId));

            if (jasperPrintAll == null) {
                jasperPrintAll = jasperPrint;
            } else {
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

        try {
            JasperPrint jasperPrint = null;
            List<Component> reportData = this.getData(orderId, operationId);

            if (reportData == null) {
                return jasperPrint;
            }

            Map<String, Object> parameters = new HashMap<String, Object>();
            //SET THE PARAMETERS FOR JASPER REPORT
            parameters.put("Title", "Ordem de Produção " + orderId);
            parameters.put("UserName", ReportConfig.userName);
            //parameters.put("operDescription", operation.getDescription());
            parameters.put("LogoImage", this.getLogo());

            ArrayList<PlanningOrderOperationDataBean> dataList = new ArrayList<PlanningOrderOperationDataBean>();
            for (int i = 0; i < reportData.size(); i++) {
                List<Produce> produceList = reportData.get(i).getProduce();
                for (int j = 0; j < produceList.size(); j++) {
                    PlanningOrderOperationDataBean dataListObj = new PlanningOrderOperationDataBean();
                    dataListObj.setQuantity(produceList.get(j).getQuantity().getValue());
                    dataListObj.setComponentDescription(reportData.get(i).getDescription());
                    dataListObj.setColor(reportData.get(i).getColor().getCode());
                    dataListObj.setBomNumber(reportData.get(i).getItem().getBOM().getNumber());
                    dataListObj.setCustomer(reportData.get(i).getItem().getBOM().getCustomer());
                    //dataListObj.setStartDate(new java.text.SimpleDateFormat("dd/MM/yyyy").format(produceList.get(j).getStartDate()));
                    dataListObj.setStartDate(produceList.get(j).getStartDate());
                    //dataListObj.setBomDelivery(new java.text.SimpleDateFormat("dd/MM/yyyy").format(reportData.get(i).getItem().getBOM().getDelivery()));
                    dataListObj.setBomDelivery(reportData.get(i).getItem().getBOM().getDelivery());
                    dataListObj.setOperDescription(operation.getName());
                    dataListObj.setWeight(reportData.get(i).getWeight().getValue());

                    dataList.add(dataListObj);
                }
            }

            Collections.sort(dataList, new Comparator<PlanningOrderOperationDataBean>() {

                @Override
                public int compare(PlanningOrderOperationDataBean o1, PlanningOrderOperationDataBean o2) {

                    return o1.getStartDate().compareTo(o2.getStartDate());
                }

            });

            Multimap<Date, PlanningOrderOperationDataBean> multimap = Multimaps.index(dataList,
                    new Function<PlanningOrderOperationDataBean, Date>() {

                        @Override
                        public Date apply(PlanningOrderOperationDataBean p1) {

                            return p1.getStartDate();
                        }
                    });

            JasperPrint combinedPrint = null;
            List<JRPrintPage> combinedPages;

            Map<Date, Collection<PlanningOrderOperationDataBean>> dataMap = multimap.asMap();
            for (Date startDate : dataMap.keySet()) {
                Collection<PlanningOrderOperationDataBean> data = dataMap.get(startDate);

                //GET THE JASPER FILE
                InputStream reportStream = getClass().getResourceAsStream("/reports/orderoperation/orderoperation-report.jasper");
                if (reportStream == null) {
                    return null;
                }

                //PRINT THE PDF REPORT
                JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(data);
                JasperPrint tempPrint = JasperFillManager.fillReport(reportStream, parameters, beanColDataSource);

                if (combinedPrint == null) {
                    combinedPrint = tempPrint;
                } else {
                    combinedPages = tempPrint.getPages();
                    for (int j = 0; j < combinedPages.size(); j++) {
                        JRPrintPage object = (JRPrintPage) combinedPages.get(j);
                        combinedPrint.addPage(object);
                    }
                }

                reportStream.close();
            }

            return combinedPrint;
        } catch (JRException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param req
     * @param resp
     * @param id
     * @return
     * @throws ServletException
     * @throws IOException
     */
    @GET
    @Permission(name = "PRINT")
    @Path("/{id}")
    @Produces("application/pdf")
    public Response getOrderOperationReport(
            @Context HttpServletRequest req,
            @Context HttpServletResponse resp,
            @PathParam("id") Long id)
            throws ServletException, IOException {

        // GET THE SELECTED OPERATIONS REQUEST
        List<String> selectedOperations = info.getQueryParameters().get("op");

    	//CHECK FOR AUTH TOKEN
        /*if (checkAuth(token).getStatus() != 200) {
         return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid Token: " + token).build();
         }*/
        return getReport(id, selectedOperations);
    }
}
