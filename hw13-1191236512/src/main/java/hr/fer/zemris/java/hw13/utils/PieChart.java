package hr.fer.zemris.java.hw13.utils;
import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.PieDataset;
import org.jfree.chart.util.Rotation;

/**
 * Pomoćni razred, služi za izradu
 * kružnog dijagrama sa zadanim podacima.
 * 
 * @author Valentina Križ
 *
 */
public class PieChart extends JFrame {
	/**
	 * Referenca na graf
	 */
	public JFreeChart chart;

    private static final long serialVersionUID = 1L;

    /**
     * Konstruktor 
     * 
     * @param applicationTitle
     * @param chartTitle
     * @param dataset
     */
    public PieChart(String chartTitle, PieDataset dataset) {
        // based on the dataset we create the chart
        chart = createChart(dataset, chartTitle);
        // we put the chart into a panel
        ChartPanel chartPanel = new ChartPanel(chart);
        // default size
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
    }

    /**
     * Metoda generira dijagram
     */
    private JFreeChart createChart(PieDataset dataset, String title) {

        JFreeChart chart = ChartFactory.createPieChart3D(
            title,                  // chart title
            dataset,                // data
            true,                   // include legend
            true,
            false
        );

        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        
        return chart;
    }
}