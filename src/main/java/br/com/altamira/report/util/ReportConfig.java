package br.com.altamira.report.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.altamira.report.manufacturing.AuthTokenChkRespDataBean;
import sun.misc.BASE64Decoder;

public abstract class ReportConfig {
	
	/**
	 *
	 */
	public static String userName = "";

	/**
	 *
	 */
	public static final String AUTH_URL = "http://data.altamira.com.br/security-oauth2-0.1.0-SNAPSHOT/authz/permission";
	
	/**
	 *
	 */
	public static final String DATA_BASE_URL = "http://data.altamira.com.br/data-rest-0.8.0-SNAPSHOT";
	/**
	 *
	 */
	public static final String RESOURCE = "REPORT";
	
	/**
	 * Check the Auth Token
	 * @param Token String
	 * @return Response
	 */
	public Response checkAuth(String token) {
		Response response = null;

		try {
			String url = AUTH_URL + "?token=" + token + "&resource=" + RESOURCE + "&permission=READ";
			Client client = ClientBuilder.newClient();
			WebTarget webTarget = client.target(url);
			Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
			response = invocationBuilder.get();
			AuthTokenChkRespDataBean authTokenData  = response.readEntity(AuthTokenChkRespDataBean.class);
			userName = authTokenData.getUserName();
			return response;
		} catch (Exception e) {            
			System.out.println(e.getMessage());
			
		}
		return response;
	}


	/**
	 * Get the logo image
	 * @param 
	 * @return buffered logo image
	 */
	public BufferedImage getLogo() {
		//GET THE LOGO FILE FROM RESOURCE
		InputStream reportLogo = getClass().getResourceAsStream("/images/report-header-logo.png");

		BufferedImage imfg = null;
		try {
			//InputStream in = new ByteArrayInputStream(requestReportAltamiraimage);
			imfg = ImageIO.read(reportLogo);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return imfg;
	}
	
	/**
     * Decode string to image
     * @param imageString The string to decode
     * @return decoded image
     */
    public static BufferedImage decodeToImage(String imageString) {

        BufferedImage image = null;
        byte[] imageByte;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }


}
