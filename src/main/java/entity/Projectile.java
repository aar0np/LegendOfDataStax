package entity;

import game2d.GamePanel;

public class Projectile extends Entity {

	Entity user;
	
	public Projectile(GamePanel gp) {
		super(gp);
	}
	
	public void set(int worldX, int worldY, String direction, boolean alive, Entity entity) {
		this.worldX = worldX;
		this.worldY = worldY;
		this.direction = direction;
		this.alive = alive;
		this.user = entity;
		this.currentHealth = this.maxHealth;
	}
	
	public void update() {

		if (user == gp.getPlayer()) {
			int monsterIndex = gp.getCollisionChecker().checkEntity(this, gp.getMonsters());
			
			if (monsterIndex != 999) {
				gp.getPlayer().damageMonster(monsterIndex, this.attack);
				// "kill" the projectile
				alive = false;
			}
			
		} else {
			boolean contactPlayer = gp.getCollisionChecker().checkPlayer(this);
			
			if (contactPlayer && !gp.getPlayer().isInvincible()) {
				damagePlayer(this.attack);
				alive = false;
			}
		}
		
		switch (direction) {
		case "up":
			worldY -= speed;
			break;
		case "down":
			worldY += speed;
			break;						
		case "left":
			worldX -= speed;
			break;
		case "right":
			worldX += speed;
			break;						
		}
		
		currentHealth--;
		if (currentHealth <= 0) {
			alive = false;
		}
		
		// how to influence the movement animation
		spriteCounter++;
		if (spriteCounter > 12) {
			if (spriteNum == 1) {
				spriteNum = 2;
			// } else if ( spriteNum == 2) {
			} else {
				spriteNum = 1;
			}
			spriteCounter = 0;
		}
	}
	
	public boolean hasResource(Entity user) {
		return false;
	}
	
	public void decreaseResource(Entity user) {

	}
}
