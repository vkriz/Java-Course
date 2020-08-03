package hr.fer.zemris.java.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class DrawingModel {
	private Graphics2D g2d;
	
	private Color background;
	private int height;
	private int width;
	private List<Line> lines;
	private List<Oval> ovals;
	
	public DrawingModel(Color background, int height, int width) {
		this.background = background;
		this.height = height;
		this.width = width;
		this.lines = new ArrayList<>();
		this.ovals = new ArrayList<>();
	}
	
	public DrawingModel() {
		this.lines = new ArrayList<>();
		this.ovals = new ArrayList<>();
	}
	
	public Color getBackground() {
		return background;
	}
	public void setBackground(Color background) {
		this.background = background;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public List<Line> getLines() {
		return lines;
	}
	public void setLines(List<Line> lines) {
		this.lines = lines;
	}
	public List<Oval> getOvals() {
		return ovals;
	}
	public void setOvals(List<Oval> ovals) {
		this.ovals = ovals;
	}
	
	public void addOval(Oval oval) {
		ovals.add(oval);
	}
	
	public void addLine(Line line) {
		lines.add(line);
	}
	
	public BufferedImage draw() {	
		BufferedImage bImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		g2d = bImg.createGraphics();
		
		g2d.setBackground(background);
		
		for(Oval oval : ovals) {
			oval.draw(g2d);
		}
		
		for(Line line : lines) {
			line.draw(g2d);
		}
		
	    try {
            if (ImageIO.write(bImg, "png", new File("./output_image.png")))
            {
                System.out.println("-- saved");
            }
	    } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
	    }
	    
	    return bImg;
	}
}
