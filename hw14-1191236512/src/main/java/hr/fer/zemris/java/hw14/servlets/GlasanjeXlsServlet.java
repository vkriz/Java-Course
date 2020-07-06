package hr.fer.zemris.java.hw14.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import hr.fer.zemris.java.hw14.dao.DAOProvider;
import hr.fer.zemris.java.hw14.model.PollOption;


/**
 * Servlet vraća xls datoteku s rezultatima
 * ankete sa zadanim ID-jem.
 * 
 * @author Valentina Križ
 *
 */
@WebServlet(name = "glasanjeXlsServlet", urlPatterns = {"/servleti/glasanje-xls"})
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
	
		long pollId = Long.parseLong(req.getParameter("pollID"));
		
		List<PollOption> results = DAOProvider.getDao().getAllPollOptions(pollId);
		
		resp.setContentType("application/vnd.ms-excel");
		resp.setHeader("Content-Disposition", "attachment; filename=\"results.xls\"");
		
		try{
			HSSFWorkbook hwb = new HSSFWorkbook();
			
			HSSFSheet sheet =  hwb.createSheet("results");
			
			HSSFRow rowhead = sheet.createRow(0);
			rowhead.createCell(0).setCellValue("Option ID");
			rowhead.createCell(1).setCellValue("Votes");
				
			int rowCounter = 1;
			for(PollOption option : results) {
				rowhead = sheet.createRow(rowCounter++);
				rowhead.createCell(0).setCellValue(option.getId());
				rowhead.createCell(1).setCellValue(option.getVotesCount());
			}
			
			OutputStream out = resp.getOutputStream();
			hwb.write(out);
			
			hwb.close();
		} catch ( Exception ex ) {
			System.out.println(ex.getMessage());
		}
	}
}
