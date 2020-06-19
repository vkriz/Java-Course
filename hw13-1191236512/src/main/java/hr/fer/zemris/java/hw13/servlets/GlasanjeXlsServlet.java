package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import hr.fer.zemris.java.hw13.utils.GlasanjeUtils;


/**
 * Servlet vraća xls datoteku s rezultatima
 * glasanja za najbolji bend.
 * 
 * @author Valentina Križ
 *
 */
@WebServlet(name = "glasanjeXlsServlet", urlPatterns = {"/glasanje-xls"})
public class GlasanjeXlsServlet extends HttpServlet {

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
	
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
		Map<String, Integer> results = GlasanjeUtils.getResults(fileName);
		
		fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
		Map<String, String> bands = GlasanjeUtils.getBands(fileName);
		
		resp.setContentType("application/vnd.ms-excel");
		resp.setHeader("Content-Disposition", "attachment; filename=\"results.xls\"");
		
		try{
			HSSFWorkbook hwb = new HSSFWorkbook();
			
			HSSFSheet sheet =  hwb.createSheet("results");
			
			HSSFRow rowhead = sheet.createRow(0);
			rowhead.createCell(0).setCellValue("Band");
			rowhead.createCell(1).setCellValue("Votes");
				
			int rowCounter = 1;
			for(String id : bands.keySet()) {
				rowhead = sheet.createRow(rowCounter++);
				rowhead.createCell(0).setCellValue(bands.get(id));
				rowhead.createCell(1).setCellValue(results.get(id));
			}
			
			OutputStream out = resp.getOutputStream();
			hwb.write(out);
			
			hwb.close();
		} catch ( Exception ex ) {
			System.out.println(ex.getMessage());
		}
	}
}
