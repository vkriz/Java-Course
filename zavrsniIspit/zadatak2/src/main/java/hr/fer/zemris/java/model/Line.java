package hr.fer.zemris.java.model;

import java.awt.Color;
import java.awt.Graphics2D;

public class Line {
	private int startX;
	private int startY;
	private int endX;
	private int endY;
	private Color color;
	
	public int getStartX() {
		return startX;
	}

	public void setStartX(int startX) {
		this.startX = startX;
	}

	public int getStartY() {
		return startY;
	}

	public void setStartY(int startY) {
		this.startY = startY;
	}

	public int getEndX() {
		return endX;
	}

	public void setEndX(int endX) {
		this.endX = endX;
	}

	public int getEndY() {
		return endY;
	}

	public void setEndY(int endY) {
		this.endY = endY;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public Line(int startX, int startY, int endX, int endY, Color color) {
		this.startX = startX;
		this.startY	 = startY;
		this.endX = endX;
		this.endY = endY;
		this.color = color;
	}

	public void draw(Graphics2D g2d) {
		g2d.setColor(color);
		g2d.drawLine(startX, startY, endX, endY);
	}
}
