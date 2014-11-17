package br.com.altamira.report.manufacturing;

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

import sun.misc.BASE64Decoder;

public abstract class ReportConfig {
	
	/**
	 *
	 */
	public static String userName = "";

	/**
	 *
	 */
	public static final String AUTH_URL = "http://localhost:8080/security-oauth2/auth/authz";
	
	/**
	 * Check the Auth Token
	 * @param Token String
	 * @return Response
	 */
	public Response checkAuth(String token) {
		//Response response = null;

		return Response.status(Response.Status.OK).entity("success").build();
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
