package hr.fer.zemris.java.draw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.model.DrawingModel;
import hr.fer.zemris.java.parser.Parser;

public class App  extends JFrame {
	private static final long serialVersionUID = 1L;

	public App() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setTitle("Image Drawer");
		initGUI();
		setSize(800, 800);
		setLocationRelativeTo(null);
	}

	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
		JTextArea textArea = new JTextArea("\n SIZE dim=300,200 background:rgb=255,255,255\n"
				+ "LINE start=10,10 end=290,10	stroke:rgb=255,0,0\n"
				+ "OVAL center=70,100 rx=50 ry=30 	stroke:rgb=255,0,0 fill:rgb=0,255,255");
		textArea.setBorder(BorderFactory.createLoweredBevelBorder());
		
		JButton closeButton = new JButton("Zatvori");
		JButton drawButton = new JButton("Nacrtaj");
		
		closeButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        dispose();
		    }
		});
		
		JPanel center = new JPanel();
		
		center.setLayout(new GridLayout(0, 1));
		
		center.add(textArea);
		JPanel drawSpace = new JPanel();
		center.add(drawSpace);
		
		drawButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        DrawingModel model = Parser.parse(textArea.getText());
		        BufferedImage image = model.draw();
		        JLabel label = new JLabel(new ImageIcon(image));
		        drawSpace.add(label);
		        drawSpace.setVisible(true);
		    }
		});
		
		cp.add(center, BorderLayout.CENTER);
		
		JToolBar bottomToolBar = new JToolBar();
		bottomToolBar.setFloatable(true);
		bottomToolBar.add(closeButton);
		bottomToolBar.add(drawButton);
		
		cp.add(bottomToolBar, BorderLayout.PAGE_END);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new App().setVisible(true);
			}
		});
	}
}
