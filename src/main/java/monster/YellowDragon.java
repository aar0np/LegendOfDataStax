package monster;

import java.util.Random;

import entity.Entity;
import game2d.GamePanel;
import object.Fireball;

public class YellowDragon extends Entity {

	public YellowDragon(GamePanel gp) {
		super(gp);
		
		name = "Yellow Dragon";
		type = MONSTER;
		speed = 3;
		maxHealth = 2;
		currentHealth = maxHealth;
		attack = 2;
		defense = 0;
		projectile = new Fireball(gp);
		experiencePoints = 2;
		
		solidArea.x = 3;
		solidArea.y = 18;
		solidArea.width = 42;
		solidArea.height = 30;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
		getYellowDragonImages();
	}
	
	public void getYellowDragonImages() {
		up1 = setupEntityImage("/monsters/yellowdragon_down_1.png", tileSize, tileSize);
		up2 = setupEntityImage("/monsters/yellowdragon_down_2.png", tileSize, tileSize);
		down1 = setupEntityImage("/monsters/yellowdragon_down_1.png", tileSize, tileSize);
		down2 = setupEntityImage("/monsters/yellowdragon_down_2.png", tileSize, tileSize);
		right1 = setupEntityImage("/monsters/yellowdragon_down_1.png", tileSize, tileSize);
		right2 = setupEntityImage("/monsters/yellowdragon_down_2.png", tileSize, tileSize);
		left1 = setupEntityImage("/monsters/yellowdragon_down_1.png", tileSize, tileSize);
		left2 = setupEntityImage("/monsters/yellowdragon_down_2.png", tileSize, tileSize);
	}

	public void setAction() {
		//Green Slime AI
		actionLockCounter++;
		Random random = new Random();
		
		// only let the dragon move every 120 frames (2 seconds)
		if (actionLockCounter >= 120) {
			int rndNum = random.nextInt(100) + 1;
			
			if (rndNum <= 25) {
				direction = "up";
			} else if (rndNum > 25 && rndNum <= 50) {
				direction = "down";
			} else if (rndNum > 50 && rndNum <= 75) {
				direction = "left";
			} else {
				direction = "right";
			}
			
			actionLockCounter = 0;					
		}
		
		// throw/shoot projectile
		int rndShotNum = random.nextInt(100) + 1;

		if (rndShotNum > 99 && !projectile.isAlive() && shotAvailableCounter >= 30) {
			projectile.set(worldX, worldY, direction, true, this);
			gp.getProjectiles().add(projectile);
			shotAvailableCounter = 0;
		}		
	}
	
	public void damageReaction() {

		actionLockCounter = 0;
		direction = gp.getPlayer().getDirection();
	}

}
