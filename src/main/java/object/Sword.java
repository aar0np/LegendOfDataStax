package object;

import entity.Entity;
import game2d.GamePanel;

public class Sword extends Entity {

	public Sword(GamePanel gp) {
		super(gp);
		
		name = "Sword";
		type = SWORD;
		description = "[" + name + "]\nAn old sword.";
		down1 = setupEntityImage("/objects/sword.png", tileSize, tileSize);
		attackArea.width = 36;
		attackArea.height = 36;
		attackValue = 1;
	}	
}
