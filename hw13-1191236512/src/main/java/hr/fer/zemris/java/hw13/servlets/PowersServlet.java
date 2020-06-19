package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


/**
 * Servlet kreira Microsoft Excell 
 * dokument sa zadanim brojem stranica (n) u
 * kojem su izračunate potencije zadanih
 * brojeva (a i b). 
 * 
 * @author Valentina Križ
 *
 */
@WebServlet(name = "powersServlet", urlPatterns = {"/powers"})
public class PowersServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	
		int a = 1;
		int b = 100;
		int n = 3;
		
		try {
			a = Integer.parseInt(req.getParameter("a"));
			b = Integer.parseInt(req.getParameter("b"));
			n = Integer.parseInt(req.getParameter("n"));
		} catch(NumberFormatException ex) {
			req.getRequestDispatcher("/errorMessage.jsp").forward(req, resp);
		}
		
		if(a < -100 || a > 100 || b < -100 || b > 100 || n < 1 || n > 5) {
			req.getRequestDispatcher("/errorMessage.jsp").forward(req, resp);
		}
		
		resp.setContentType("application/vnd.ms-excel");
		resp.setHeader("Content-Disposition", "attachment; filename=\"table.xls\"");
		
		try{
			HSSFWorkbook hwb = new HSSFWorkbook();
			
			for(int i = 1; i <= n; ++i) {
				HSSFSheet sheet =  hwb.createSheet("sheet " + i);
				
				int rowCounter = 0;
				for(int x = a; x <= b; ++x) {
					HSSFRow rowhead = sheet.createRow(rowCounter++);
					rowhead.createCell(0).setCellValue(x);
					rowhead.createCell(1).setCellValue(Math.pow(x, i));
				}
			}
			
			OutputStream out = resp.getOutputStream();
			hwb.write(out);
			
			hwb.close();
		} catch ( Exception ex ) {
			System.out.println(ex.getMessage());
		}
	}
}
