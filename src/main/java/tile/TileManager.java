package tile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import game2d.GamePanel;
import game2d.GraphicsTools;

public class TileManager {

	GamePanel gp;
	int tileSize;
	Tile[] tiles;
	int mapTileCodes[][];
	
	public TileManager(GamePanel gp) {
		
		this.gp = gp;
		tileSize = gp.getTileSize();
		tiles = new Tile[50];
		mapTileCodes = new int[gp.getMaxWorldColumn()][gp.getMaxWorldRow()];
		
		getTileImage();
		// loadMap("/maps/world01.txt"); // original tiles 0-9
		// loadMap("/maps/worldv2.txt");    // new tiles 10-41
		loadMap("/maps/nosqlKingdom.txt");
	}
	
	private void getTileImage() {

		// water
		setupTile(0, "000.png", false);
		setupTile(1, "001.png", false);
		setupTile(2, "002.png", false);
		setupTile(5, "005.png", true); // main
		setupTile(18, "018.png", false);
		setupTile(19, "019.png", false);
		setupTile(20, "020.png", false);
		setupTile(22, "022.png", false);
		setupTile(24, "024.png", false);
		
		// wood
		setupTile(3, "003.png", false);
		
		// wood/log wall
		setupTile(16, "016.png", true);
		
		// black
		setupTile(4, "004.png", false);
		
		// stone wall
		setupTile(6, "006.png", true);

		// slate
		setupTile(10, "010.png", false);
		
		// road
		setupTile(7, "007.png", false);
		setupTile(9, "009.png", false);
		setupTile(11, "011.png", false);
		setupTile(13, "013.png", false);
		setupTile(14, "014.png", false);
		setupTile(15, "015.png", false);
		setupTile(17, "017.png", false);
		setupTile(21, "021.png", false);
		setupTile(23, "023.png", false);

		// grass
		setupTile(8, "008.png", false);

		// tree
		setupTile(12, "012.png", true);
	}
	
	private void setupTile(int tileIndex, String imagePath, boolean collision) {

		try {
			tiles[tileIndex] = new Tile();
			BufferedImage scaledImage = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imagePath));
			scaledImage = GraphicsTools.scaleTile(scaledImage, tileSize, tileSize);
			tiles[tileIndex].setImage(scaledImage);
			tiles[tileIndex].setCollision(collision);
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private void loadMap(String mapFile) {
		
		try {
			InputStream inputStream = getClass().getResourceAsStream(mapFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			
			int col = 0;
			int row = 0;
			
			while (col < gp.getMaxWorldColumn() && row < gp.getMaxWorldRow()) {
				
				String inputLine = br.readLine();
				
				while (col < gp.getMaxWorldColumn()) {
					String tileCodes[] = inputLine.split(" ");
					
					int tileCode = Integer.parseInt(tileCodes[col]);
					mapTileCodes[col][row] = tileCode;
					col++;
				}
				
				if (col >= gp.getMaxWorldColumn()) {
					col = 0;
					row++;
				}
			}
			
			br.close();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	public void draw(Graphics2D g2) {
		// test background
		//g2.drawImage(tiles[2].image, 0, 0, gp.getTileSize(), gp.getTileSize(), null);
		//g2.drawImage(tiles[0].image, 48, 0, gp.getTileSize(), gp.getTileSize(), null);
		//g2.drawImage(tiles[1].image, 96, 0, gp.getTileSize(), gp.getTileSize(), null);
		
		int worldCol = 0;
		int worldRow = 0;

		
		while (worldCol < gp.getMaxWorldColumn() && worldRow < gp.getMaxWorldRow()) {

			int tileNum = mapTileCodes[worldCol][worldRow];
			
			int worldX = worldCol * tileSize;
			int worldY = worldRow * tileSize;
			int screenX = worldX - gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX();
			int screenY = worldY - gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY();
			
			// if check so that we only draw tiles which are visible.
			if (worldX + tileSize > gp.getPlayer().getWorldX() - gp.getPlayer().getScreenX() &&
				worldX - tileSize < gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX() &&
				worldY + tileSize > gp.getPlayer().getWorldY() - gp.getPlayer().getScreenY() &&
				worldY - tileSize < gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY()) {

				g2.drawImage(tiles[tileNum].getImage(), screenX, screenY, null);
			}
			
			worldCol++;
			
			if (worldCol >= gp.getMaxWorldColumn()) {
				worldCol = 0;
				worldRow++;
			}
		}
	}
	
	public int[][] getMapTileCodes() {
		return mapTileCodes;
	}
	
	public Tile[] getTiles() {
		return tiles;
	}
}
