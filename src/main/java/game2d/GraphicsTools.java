package game2d;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class GraphicsTools {

	public static BufferedImage scaleTile(BufferedImage smallImage, int width, int height) {

		BufferedImage scaledImage = new BufferedImage(width, height, smallImage.getType());
		Graphics2D g2 = scaledImage.createGraphics();
		g2.drawImage(smallImage, 0, 0, width, height, null);
		g2.dispose();
		
		return scaledImage;
	}
}
