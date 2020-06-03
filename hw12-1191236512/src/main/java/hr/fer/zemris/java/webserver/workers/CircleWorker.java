package hr.fer.zemris.java.webserver.workers;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Razred implementira sučelje IWebWorker,
 * a zahtjev obrađuje tako da kreira PNG
 * sliku dimenzija 200x200 s jednim ispunjenim
 * krugom.
 * 
 * @author Valentina Križ
 *
 */
public class CircleWorker implements IWebWorker {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processRequest(RequestContext context) throws Exception {
		BufferedImage bim = new BufferedImage(200, 200, BufferedImage.TYPE_3BYTE_BGR);
		
		Graphics2D g2d = bim.createGraphics();
		g2d.fillOval(0, 0, 200, 200);
		g2d.dispose();
		
		context.setMimeType("image/png");
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ImageIO.write(bim, "png", bos);
			context.write(bos.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
