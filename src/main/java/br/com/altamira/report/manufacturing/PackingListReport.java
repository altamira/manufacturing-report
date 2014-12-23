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

import br.com.altamira.report.util.ReportConfig;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class PackingListReport extends ReportConfig {

    public MfgOrderDataBean getData(Long code) {

		//Client client = ClientBuilder.newClient();
        //WebTarget webTarget = client.target("http://localhost:8080/manufacturing-report/mfg-order.json");
        //MfgOrderDataBean mfgOrderData = webTarget.path("").request(MediaType.APPLICATION_JSON).get(MfgOrderDataBean.class);
        //Hardcode data for the PackingListReport since the real API is not ready.
        MfgOrderDataBean mfgOrderData = new MfgOrderDataBean();
        mfgOrderData.setCode("PPLCOL00113000000000");
        mfgOrderData.setDescription("COLUNA NORMAL CH12 2800MM");
        mfgOrderData.setDate("10/09/14");
        mfgOrderData.setSector("ESTAMPARIA");
        mfgOrderData.setIssuedBy("HELIO");
        mfgOrderData.setOperation("10 - PERFILAMENTO");

        ArrayList<MfgOrderComponent> components = new ArrayList<MfgOrderComponent>();
        components.add(new MfgOrderComponent());
        components.get(0).setCode("ALPRFQ30 - KG20000F330");
        components.get(0).setName("ACO FINA QUENTE PRETO ROLO 2,00 MM 330 MM");
        components.add(new MfgOrderComponent());
        components.get(1).setCode("PERF - PPLTUBO 113");
        components.get(1).setName("FERFILADEIRA ");
        mfgOrderData.setComponents(components);

        ArrayList<MfgOrderMaterial> materials = new ArrayList<MfgOrderMaterial>();
        materials.add(new MfgOrderMaterial());
        materials.add(new MfgOrderMaterial());
        materials.add(new MfgOrderMaterial());
        materials.add(new MfgOrderMaterial());

        materials.get(0).setAmount(11);
        materials.get(0).setClient("MULT");
        materials.get(0).setColor("CZ-PAD");
        materials.get(0).setDelivery("10/09/14");
        materials.get(0).setMaterial("COLUNA NORMAL CH12 2800MM");
        materials.get(0).setRequest(1234);
        materials.get(0).setWeightTotal(111);

        materials.get(1).setAmount(22);
        materials.get(1).setClient("MULT");
        materials.get(1).setColor("CZ-PAD");
        materials.get(1).setDelivery("10/09/14");
        materials.get(1).setMaterial("COLUNA NORMAL CH12 2800MM");
        materials.get(1).setRequest(112);
        materials.get(1).setWeightTotal(21343);

        materials.get(2).setAmount(33);
        materials.get(2).setClient("MULT");
        materials.get(2).setColor("CZ-PAD");
        materials.get(2).setDelivery("10/09/14");
        materials.get(2).setMaterial("COLUNA NORMAL CH12 2800MM");
        materials.get(2).setRequest(113);
        materials.get(2).setWeightTotal(21343);

        materials.get(3).setAmount(44);
        materials.get(3).setClient("MULT");
        materials.get(3).setColor("CZ-PAD");
        materials.get(3).setDelivery("10/09/14");
        materials.get(3).setMaterial("COLUNA NORMAL CH12 2800MM");
        materials.get(3).setRequest(1234);
        materials.get(3).setWeightTotal(114);

        mfgOrderData.setMaterials(materials);

        return mfgOrderData;
    }

    public Response getReport(Long code) {

        JasperPrint jasperPrint;
        byte[] pdf = null;

        //MfgOrderReport mfgOrderReport = new MfgOrderReport();
        MfgOrderDataBean mfgReportData = this.getData(code);
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
    }

}
