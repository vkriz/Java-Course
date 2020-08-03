package hr.fer.zemris.java.servlets;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import hr.fer.zemris.java.model.DrawingModel;
import hr.fer.zemris.java.parser.Parser;
import hr.fer.zemris.java.parser.ParserException;

@WebServlet(urlPatterns = {"/"})
public class DrawServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/pages/TextArea.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	
		String text = req.getParameter("text");
		try {
			DrawingModel model = Parser.parse(text);
			BufferedImage img = model.draw();
			resp.setContentType("image/png");

	        OutputStream outputStream = resp.getOutputStream();
	        
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        ImageIO.write(img, "png", baos );
	        baos.flush();
	        byte[] imageBytes = baos.toByteArray();

	        outputStream.write(imageBytes);
	        outputStream.flush();
		} catch(ParserException ex) {
			req.setAttribute("error", "Tekst neispravnog formata");
			req.setAttribute("text", text);
			req.getRequestDispatcher("/WEB-INF/pages/TextArea.jsp").forward(req, resp);
		}
	}
}
