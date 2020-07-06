package hr.fer.zemris.java.hw14.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartUtils;
import org.jfree.data.general.DefaultPieDataset;

import hr.fer.zemris.java.hw14.utils.PieChart;
import hr.fer.zemris.java.hw14.dao.DAOProvider;
import hr.fer.zemris.java.hw14.model.PollOption;


/**
 * Servlet generira png sliku kružnog
 * grafikona s rezultatima glasanja u 
 * anketi sa zadanim ID-jem.
 * 
 * @author Valentina Križ
 *
 */
@WebServlet(name = "glasanjeGrafikaServlet", urlPatterns = {"/servleti/glasanje-grafika"})
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
		long pollId = Long.parseLong(req.getParameter("pollID"));
	
		List<PollOption> results = DAOProvider.getDao().getAllPollOptions(pollId);
		DefaultPieDataset dataset = new DefaultPieDataset();
		for(PollOption option : results) {
			dataset.setValue(option.getOptionTitle(), option.getVotesCount());
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
