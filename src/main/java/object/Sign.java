package object;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import entity.Entity;
import game2d.GamePanel;

public class Sign extends Entity {
	
	public Sign(GamePanel gp) {
		super(gp);

		name = "Sign";
		description = "San Jose ↓";
		down1 = setupEntityImage("/tiles/003.png", tileSize, tileSize);
	}
	
	public void draw(Graphics2D g2) {
	
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,36F));
		
		int textSize = (int) g2.getFontMetrics().getStringBounds(description, g2).getWidth();

		int signX = worldX - gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX();
		int signY = worldY - gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY();
		
		int widthInTiles = getNumTilesToDraw(textSize) + 1;
		
		for (int tileCount = 0; tileCount < widthInTiles; tileCount++) {
			g2.drawImage(down1,
					signX - (tileSize / 2) + (tileSize * tileCount), signY,
					tileSize, tileSize, null);
		}

		// shadow
		g2.setColor(Color.black);
		g2.drawString(description, signX + 2, signY + 38);
		// text
		g2.setColor(Color.white);
		g2.drawString(description, signX, signY + 36);
	}
	
	private int getNumTilesToDraw(int textSize) {
		
		return textSize / tileSize;
	}
}
