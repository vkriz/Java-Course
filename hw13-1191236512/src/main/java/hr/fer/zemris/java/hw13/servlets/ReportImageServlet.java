package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.data.general.DefaultPieDataset;

import hr.fer.zemris.java.hw13.utils.PieChart;

import org.jfree.chart.ChartUtils;


/**
 * Servlet pomoću jfreechart 
 * biblioteke kreira sliku na kojoj 
 * je prikazan kružni dijagram.
 * 
 * @author Valentina Križ
 *
 */
@WebServlet(name = "reportImageServlet", urlPatterns = {"/reportImage"})
public class ReportImageServlet extends HttpServlet {

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
		
		resp.setContentType("image/png");
	
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Android", 37.81);
        dataset.setValue("Windows", 35.83);
        dataset.setValue("iOS", 15.28);
        dataset.setValue("OS X", 8.54);
        dataset.setValue("Linux", 0.79);
        dataset.setValue("Other", 1.75);
		
		PieChart pieChart = new PieChart(
								"Which operating system are you using?", 
								dataset);
        
        OutputStream outputStream = resp.getOutputStream();
        
        int width = 500;
        int height = 270;
        ChartUtils.writeChartAsPNG(outputStream, pieChart.chart, width, height);
        outputStream.flush();
	}
}
