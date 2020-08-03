package hr.fer.zemris.java.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class Oval {
	private int centerX;
	private int centerY;
	private int rx;
	private int ry;
	private Color strokeColor;
	private Color fillColor;
	
	public Oval(int centerX, int centerY, int rx, int ry, Color strokeColor, Color fillColor) {
		this.centerX = centerX;
		this.centerY = centerY;
		this.rx = rx;
		this.ry = ry;
		this.fillColor = fillColor;
		this.strokeColor = strokeColor;
	}
	
	public int getCenterX() {
		return centerX;
	}
	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}
	public int getCenterY() {
		return centerY;
	}
	public void setCenterY(int centerY) {
		this.centerY = centerY;
	}
	public int getRx() {
		return rx;
	}
	public void setRx(int rx) {
		this.rx = rx;
	}
	public int getRy() {
		return ry;
	}
	public void setRy(int ry) {
		this.ry = ry;
	}
	public Color getStrokeColor() {
		return strokeColor;
	}
	public void setStrokeColor(Color strokeColor) {
		this.strokeColor = strokeColor;
	}
	public Color getFillColor() {
		return fillColor;
	}
	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}
	
	public void draw(Graphics2D g2d) {
		g2d.setColor(fillColor);
		g2d.fill(new Ellipse2D.Double(centerX - rx / 2, centerY - ry / 2, rx, ry));
		g2d.setColor(strokeColor);
	    g2d.draw(new Ellipse2D.Double(centerX - rx / 2, centerY - ry / 2, rx, ry));
	}
}
