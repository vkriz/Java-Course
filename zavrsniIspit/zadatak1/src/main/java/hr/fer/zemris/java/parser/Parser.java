package hr.fer.zemris.java.parser;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.model.DrawingModel;
import hr.fer.zemris.java.model.Line;
import hr.fer.zemris.java.model.Oval;

public class Parser {
	public static DrawingModel parse(String text) {		
		String[] lines = text.trim().split("\\r?\\n");
		List<String> cleanLines = new ArrayList<>();
		
		for(String line : lines) {
			if(!line.startsWith("#") && !line.isBlank()) {
				cleanLines.add(line);
			}
		}
		
		if(cleanLines.size() < 1) {
			throw new ParserException("Neispravan format");
		}
	
		Color backColor = null;
		int width = 0;
		int height = 0;
		
		if(cleanLines.get(0).startsWith("SIZE")) {
			 String[] lineParts = cleanLines.get(0).split("\\s+");
			 for(String part : lineParts) {
				 if(part.startsWith("dim=")) {
					 String[] dimensions = part.substring(4).split(",");
					 try {
						 width = Integer.parseInt(dimensions[0]);
						 height = Integer.parseInt(dimensions[1]);
					 } catch(Exception ex) {
						 throw new ParserException("Neispravan format");
					 }
				 } else if(part.startsWith("background:rgb=")) {
					 String[] color = part.substring(15).split(",");
					 try {
						int r = Integer.parseInt(color[0]);
						int g = Integer.parseInt(color[1]);
						int b = Integer.parseInt(color[2]);
						 
						 backColor = new Color(r, g, b);
					 } catch(Exception ex) {
						 throw new ParserException("Neispravan format");
					 }
				 } else {
					 if(!part.startsWith("SIZE")) {
						 throw new ParserException("Neispravan format");
					 } 
				 }
			 }
		} else {
			throw new ParserException("Neispravan format");
		}
		
		DrawingModel model = new DrawingModel(backColor, height, width);
		
		if(cleanLines.size() > 1) {
			for(int i = 1, size = cleanLines.size(); i < size; ++i) {
				String[] lineParts = cleanLines.get(i).split("\\s+");
				if(lineParts[0].equals("LINE")) {
					if(lineParts.length != 4) {
						throw new ParserException("Neispravan format");
					}
					
					int startX = 0;
					int startY = 0;
					int endX = 0;
					int endY = 0;
					Color strokeColor = Color.black;
					
					boolean startSet = false;
					boolean endSet = false;
					boolean strokeColorSet = false;
					
					for(int j = 1; j < lineParts.length; ++j) {
						if(lineParts[j].startsWith("start=")) {
							String[] start = lineParts[j].substring(6).split(",");
							 try {
								startX = Integer.parseInt(start[0]);
								startY = Integer.parseInt(start[1]);
								startSet = true;
							 } catch(Exception ex) {
								 throw new ParserException("Neispravan format");
							 }
						} else if(lineParts[j].startsWith("end=")) {
							String[] end = lineParts[j].substring(4).split(",");
							 try {
								endX = Integer.parseInt(end[0]);
								endY = Integer.parseInt(end[1]);
								endSet = true;
							 } catch(Exception ex) {
								 throw new ParserException("Neispravan format");
							 }
						} else if(lineParts[j].startsWith("stroke:rgb=")) {
							String[] color = lineParts[j].substring(11).split(",");
							 try {
								int r = Integer.parseInt(color[0]);
								int g = Integer.parseInt(color[1]);
								int b = Integer.parseInt(color[2]);
								 
								strokeColor = new Color(r, g, b);
								strokeColorSet = true;
							 } catch(Exception ex) {
								 throw new ParserException("Neispravan format");
							 }
						} else {
							throw new ParserException("Neispravan format");
						}
					}
					if(startSet == true && endSet == true && strokeColorSet == true) {
						model.addLine(new Line(startX, startY, endX, endY, strokeColor));
					} else {
						throw new ParserException("Neispravan format");
					}
					
				} else if(lineParts[0].equals("OVAL")) {
					if(lineParts.length != 6) {
						throw new ParserException("Neispravan format");
					}
					
					int centerX = 0;
					int centerY = 0;
					int rx = 0;
					int ry = 0;
					Color strokeColor = Color.black;
					Color fillColor = Color.black;
					
					boolean centerSet = false;
					boolean rxSet = false;
					boolean rySet = false;
					boolean strokeColorSet = false;
					boolean fillColorSet = false;
										
					for(int j = 1; j < lineParts.length; ++j) {
						if(lineParts[j].startsWith("center=")) {
							String[] center = lineParts[j].substring(7).split(",");
							 try {
								centerX = Integer.parseInt(center[0]);
								centerY = Integer.parseInt(center[1]);
								centerSet = true;
							 } catch(Exception ex) {
								 throw new ParserException("Neispravan format");
							 }
						} else if(lineParts[j].startsWith("rx=")) {
							 try {
								rx = Integer.parseInt(lineParts[j].substring(3));
								rxSet = true;
							 } catch(Exception ex) {
								 throw new ParserException("Neispravan format");
							 }
						} else if(lineParts[j].startsWith("ry=")) {
							try {
								ry = Integer.parseInt(lineParts[j].substring(3));
								rySet = true;
							 } catch(Exception ex) {
								 throw new ParserException("Neispravan format");
							 }
						} else if(lineParts[j].startsWith("stroke:rgb=")) {
							String[] color = lineParts[j].substring(11).split(",");
							 try {
								int r = Integer.parseInt(color[0]);
								int g = Integer.parseInt(color[1]);
								int b = Integer.parseInt(color[2]);
								 
								strokeColor = new Color(r, g, b);
								strokeColorSet = true;
							 } catch(Exception ex) {
								 throw new ParserException("Neispravan format");
							 }
						} else if(lineParts[j].startsWith("fill:rgb=")) {
							String[] color = lineParts[j].substring(9).split(",");
							 try {
								int r = Integer.parseInt(color[0]);
								int g = Integer.parseInt(color[1]);
								int b = Integer.parseInt(color[2]);
								 
								fillColor = new Color(r, g, b);
								fillColorSet = true;
							 } catch(Exception ex) {
								 throw new ParserException("Neispravan format");
							 }
						} else {
							throw new ParserException("Neispravan format");
						}
					}
					if(centerSet == true && rxSet == true && rySet == true && fillColorSet == true && strokeColorSet == true) {
						model.addOval(new Oval(centerX, centerY, rx, ry, strokeColor, fillColor));
					} else {
						throw new ParserException("Neispravan format");
					}
					
				} else {
					throw new ParserException("Neispravan format");
				}
			}
		}
		
		
		return model;
	}
}
