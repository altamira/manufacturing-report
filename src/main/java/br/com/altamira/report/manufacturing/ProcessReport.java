package br.com.altamira.report.manufacturing;

import br.com.altamira.data.dao.manufacture.process.ProcessDao;
import br.com.altamira.data.model.manufacture.process.Process;
import br.com.altamira.data.model.manufacture.process.Consume;
import br.com.altamira.data.model.manufacture.process.Operation;
import br.com.altamira.data.model.manufacture.process.Produce;
import br.com.altamira.data.model.manufacture.process.Revision;
import br.com.altamira.data.model.manufacture.process.Use;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import br.com.altamira.report.util.ReportConfig;
import javax.ejb.Stateless;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Stateless
@Resource(name = "MANUFACTURE_PROCESS")
@Path("manufacture/process")
public class ProcessReport extends ReportConfig {

    @Inject
    protected ProcessDao processDao;

    public Process getData(Long id) throws IOException {
        Process processData = null;

        /*Client client = ClientBuilder.newClient();
         WebTarget webTarget = client.target(DATA_BASE_URL + "/manufacturing/process/");
         processData = webTarget.path(id.toString()).request(MediaType.APPLICATION_JSON).get(Process.class);*/
        processData = processDao.find(id);

        return processData;
    }

    public Response getReport(Long id) throws IOException {

        ArrayList<ProcessReportDisplayDataBean> dataList = new ArrayList<ProcessReportDisplayDataBean>();
        JasperPrint jasperPrint;
        byte[] pdf = null;

        Process processReportData;
        try {
            processReportData = this.getData(id);
        } catch (Exception e) {
            return Response.status(Status.NOT_FOUND).entity("Não foi possivel carregar o relatorio !").build();
        }

        //List<Consume> consumes = null;
        String revisionByData = "";
        String revisionDateData = "";

        Map<String, Object> parameters = new HashMap<String, Object>();
        //SET THE PARAMETERS
        parameters.put("Title", "Processo de Fabricação");
        parameters.put("UserName", ReportConfig.userName);
        parameters.put("Code", processReportData.getCode());
        parameters.put("Description", processReportData.getDescription());
        parameters.put("LogoImage", this.getLogo());

        List<Revision> revisions = processReportData.getRevision();
        for (int i = 0; i < revisions.size(); i++) {
            String newLineText = "\r\n";
            if (i == (revisions.size() - 1)) {
                newLineText = "";
            }
            revisionByData = revisionByData + revisions.get(i).getBy() + newLineText;
            revisionDateData = revisionDateData + new java.text.SimpleDateFormat("dd/MM/yyyy").format(revisions.get(i).getDate()) + newLineText;

        }
        parameters.put("RevisionByData", revisionByData);
        parameters.put("RevisionDateData", revisionDateData);

        List<Operation> operations = processReportData.getOperation();
        for (int i = 0; i < operations.size(); i++) {
            String operationName = operations.get(i).getSequence() + " - " + operations.get(i).getOperation().getName();
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

            List<Consume> consumes = operations.get(i).getConsume();

            for (int j = 0; j < consumes.size(); j++) {
                String newLineText = "\r\n";
                if (j == (consumes.size() - 1)) {
                    newLineText = "";
                }

                String qtyText = consumes.get(j).getQuantity().getValue()
                        + " "
                        + consumes.get(j).getQuantity().getUnit().getSymbol();
                inputCodeList = inputCodeList + consumes.get(j).getMaterial().getCode() + newLineText;
                inputMaterialList = inputMaterialList + consumes.get(j).getMaterial().getDescription() + newLineText;
                inputQtyList = inputQtyList + qtyText + newLineText;

            }

            List<Produce> produces = operations.get(i).getProduce();
            for (int j = 0; j < produces.size(); j++) {
                String newLineText = "\r\n";
                if (j == (produces.size() - 1)) {
                    newLineText = "";
                }

                String qtyText = produces.get(j).getQuantity().getValue()
                        + " "
                        + produces.get(j).getQuantity().getUnit().getSymbol();
                outputCodeList = outputCodeList + produces.get(j).getMaterial().getCode() + newLineText;
                outputMaterialList = outputMaterialList + produces.get(j).getMaterial().getDescription() + newLineText;
                outputQtyList = outputQtyList + qtyText + newLineText;

            }

            List<Use> uses = operations.get(i).getUse();
            for (int j = 0; j < uses.size(); j++) {
                String newLineText = "\r\n";
                if (j == (uses.size() - 1)) {
                    newLineText = "";
                }

                String qtyText = uses.get(j).getQuantity().getValue()
                        + " "
                        + uses.get(j).getQuantity().getUnit().getSymbol();

                useCodeList = useCodeList + uses.get(j).getMaterial().getCode() + newLineText;
                useMaterialList = useMaterialList + uses.get(j).getMaterial().getDescription() + newLineText;
                useQtyList = useQtyList + qtyText + newLineText;

            }

            InputStream in = new ByteArrayInputStream(operations.get(i).getSketch().getImage());
            BufferedImage imfg = null;
            imfg = ImageIO.read(in);
            //CREATE AND ADD THE OBJECT FOR MFG OPERATION DATA LIST
            //CREATE THE OBJECT
            ProcessReportDisplayDataBean dataBean = new ProcessReportDisplayDataBean();
            dataBean.setName(operationName);
            dataBean.setDescriptionOperation(descriptionOperation);
            dataBean.setScetchOfOperation(imfg);
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
            JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(dataList);
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
    public Response manufacturingProcess(
            @Context HttpServletRequest req,
            @Context HttpServletResponse resp,
            @PathParam("id") Long id)
            throws ServletException, IOException {

        //CHECK FOR AUTH TOKEN
        /*if (checkAuth(token).getStatus() != 200) {
         return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid Token: " + token).build();
         }*/
        //ProcessReport processReport = new ProcessReport();
        return getReport(id);

    }
}
