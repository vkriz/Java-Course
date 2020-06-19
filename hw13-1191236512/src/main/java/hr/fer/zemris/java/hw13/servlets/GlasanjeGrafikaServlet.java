package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartUtils;
import org.jfree.data.general.DefaultPieDataset;

import hr.fer.zemris.java.hw13.utils.PieChart;
import hr.fer.zemris.java.hw13.utils.GlasanjeUtils;


/**
 * Servlet generira png sliku kružnog
 * grafikona s rezultatima glasanja
 * za najbolji bend.
 * 
 * @author Valentina Križ
 *
 */
@WebServlet(name = "glasanjeGrafikaServlet", urlPatterns = {"/glasanje-grafika"})
public class GlasanjeGrafikaServlet extends HttpServlet {

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
		
		DefaultPieDataset dataset = new DefaultPieDataset();
		for(String key : bands.keySet()) {
			dataset.setValue(bands.get(key), results.get(key));
		}
		
		resp.setContentType("image/png");
		
		PieChart pieChart = new PieChart(
								"Rezultati glasanja", 
								dataset);
        
        OutputStream outputStream = resp.getOutputStream();
        
        int width = 500;
        int height = 270;
        ChartUtils.writeChartAsPNG(outputStream, pieChart.chart, width, height);
        outputStream.flush();
	}
}
