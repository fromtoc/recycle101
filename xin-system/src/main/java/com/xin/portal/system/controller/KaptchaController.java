package com.xin.portal.system.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;

@Controller
@RequestMapping("/kaptcha")
public class KaptchaController {
	
	private static Logger logger = LoggerFactory.getLogger(KaptchaController.class);

	@Autowired
	private Producer kaptchaProducer;

	@RequestMapping("/create")
	public ModelAndView getKaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();

		response.setDateHeader("Expires", 0);

		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");

		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");

		// return a jpeg
		response.setContentType("image/jpeg");

		// create the text for the image
		String capText = kaptchaProducer.createText();
		logger.info("store the text in the session");
		// store the text in the session
		session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);

		String code = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
		logger.info("kaptcha code : {}",code);

		// create the image with the text
		BufferedImage bi = kaptchaProducer.createImage(capText);
		ServletOutputStream out = response.getOutputStream();

		// write the data out
		ImageIO.write(bi, "jpg", out);
		try {
			out.flush();
		} finally {
			out.close();
		}
		return null;
	}


	@RequestMapping("/new")
	public void getCheckImage(HttpServletRequest request, HttpServletResponse response){
		try {
			HttpSession session = request.getSession();
			response.setDateHeader("Expires", 0);

			// Set standard HTTP/1.1 no-cache headers.
			response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

			// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
			response.addHeader("Cache-Control", "post-check=0, pre-check=0");

			// Set standard HTTP/1.0 no-cache header.
			response.setHeader("Pragma", "no-cache");

			// return a jpeg
			response.setContentType("image/jpeg");
			int width = 60, height = 30;
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			
			Graphics g = image.getGraphics();
			Random random = new Random();
			g.setColor(getRandColor(200, 250));
			g.fillRect(0, 0, width, height);
			g.setFont(new Font("Arial", Font.PLAIN, 20));
			g.setColor(getRandColor(160, 200));
			for (int i = 0; i < 155; i++) {
				int x = random.nextInt(width);
				int y = random.nextInt(height);
				int xl = random.nextInt(12);
				int yl = random.nextInt(12);
				g.drawLine(x, y, x + xl, y + yl);
			}
			String sRand = "";
			for (int j = 0; j < 4; j++) {
				String rand = String.valueOf(random.nextInt(10));
				sRand += rand;
				g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
				g.drawString(rand, 13 * j + 6, 22);
			}
			// store the text in the session
			session.setAttribute(Constants.KAPTCHA_SESSION_KEY, sRand);

			String code = (String) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
			logger.info("kaptcha code : {}",code);
			g.dispose();
			
			OutputStream os = response.getOutputStream();
			ImageIO.write(image, "JPEG", os);
			
			try {
				os.flush();
			} finally {
				os.close();
			}
			
			// out.clear();
			// out = pageContext.pushBody();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

}
