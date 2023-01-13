package object;

import entity.Entity;
import game2d.GamePanel;

public class Heart extends Entity {

	int healingValue;
	
	public Heart(GamePanel gp) {
		super(gp);
		
		name = "Heart";
		type = REGULAR_ITEM;
		healingValue = 2;
		
		image = setupEntityImage("/objects/heart_full.png", tileSize, tileSize);
		image2 = setupEntityImage("/objects/heart_half.png", tileSize, tileSize);
		image3 = setupEntityImage("/objects/heart_blank.png", tileSize, tileSize);
		
		down1 = setupEntityImage("/objects/heart_full.png", tileSize, tileSize);
	}
	
	public void use(Entity user) {
		gp.playSoundEffect(2);
		gp.getGameUI().addMessage("Health +" + healingValue);
		user.increaseHealth(healingValue);
	}
}
