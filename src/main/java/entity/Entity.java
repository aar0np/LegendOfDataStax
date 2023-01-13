package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import game2d.GamePanel;
import game2d.GraphicsTools;

public class Entity {	

	protected BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
	protected BufferedImage upAttack1, upAttack2, downAttack1, downAttack2, leftAttack1, leftAttack2, rightAttack1, rightAttack2;
	protected BufferedImage image;
	protected BufferedImage image2;
	protected BufferedImage image3;

	protected Rectangle solidArea = new Rectangle(0, 0, 48, 48);
	protected Rectangle attackArea = new Rectangle(0, 0, 0, 0);

	protected GamePanel gp;

	protected String direction = "down";

	protected int worldX;
	protected int worldY;
	protected int spriteCounter = 0;
	protected int spriteNum = 1;
	protected int solidAreaDefaultX;
	protected int solidAreaDefaultY;
	protected int tileSize;
	protected int actionLockCounter = 0;
	protected int invincibleCounter = 0;
	protected int dyingCounter = 0;
	protected int shotAvailableCounter = 0;
	
	protected final int PLAYER = 0;
	protected final int NPC = 1;
	protected final int MONSTER = 2;
	protected final int SWORD = 3;
	protected final int AXE = 4;
	protected final int SHIELD = 5;
	protected final int CONSUMABLE = 6;
	protected final int REGULAR_ITEM = 7;
	
	protected boolean collisionOn = false;
	protected boolean collision = false;
	protected boolean invincible = false;
	protected boolean alive = true;
	protected boolean dying = false;
	
	String dialogs[] = new String[20];
	
	int dialogIndex = 0;
	int hpBarCounter = 0;
	
	boolean attacking = false;
	boolean hpBarOn = false;
	
	// character statistics
	protected String name;
	protected int speed;
	protected int type;
	protected int maxHealth;
	protected int currentHealth;
	protected int maxMana;
	protected int currentMana;
	protected int ammunition;
	protected int level;
	protected int strength; 
	protected int dexterity;
	protected int attack;
	protected int defense;
	protected int experiencePoints;
	protected int nextLevelExp;
	protected int coin;
	protected Entity currentWeapon;
	protected Entity currentShield;
	protected Projectile projectile;
	
	// item attributes
	protected int attackValue;
	protected int defenseValue;
	protected String description;
	protected int useCost;
	
	public Entity(GamePanel gp) {
		this.gp = gp;
		tileSize = gp.getTileSize();
	}
	
	public void setAction() {
		
	}
	
	public void damageReaction() {
		
	}
	
	public void checkDrop() {
		
	}
	
	public void dropItem(Entity droppedItem) {
		
		for (int objectIndex = 0; objectIndex < gp.getObjects().length; objectIndex++) {
			if (gp.getObjects()[objectIndex] == null) {
				// look for null entry in array, and set
				gp.getObjects()[objectIndex] = droppedItem;
				gp.getObjects()[objectIndex].setWorldX(worldX);
				gp.getObjects()[objectIndex].setWorldY(worldY);
				
				// once we've found one, and dropped our item,
				// there's no reason to look at the rest of the array
				break;
			}
		}
	}
	
	protected int computeAttack() {
		return strength * currentWeapon.getAttackValue();
	}
	
	protected int computeDefense() {
		// return dexterity * currentShield.getDefenseValue();
		return dexterity;
	}
	
	public void speak() {
		if (dialogs[dialogIndex] == null) {
			dialogIndex = 0;
		}
		
		gp.getGameUI().setCurrentDialog(dialogs[dialogIndex]);
		dialogIndex++;
		
		if (speed > 0) {
			switch (gp.getPlayer().getDirection()) {
			case "up":
				direction = "down";
				break;
			case "down":
				direction = "up";			
				break;
			case "left":
				direction = "right";
				break;
			case "right":
				direction = "left";
				break;
			}		
		} else {
			direction = "down";
		}
	}
	
	public void use(Entity entity) {
		
	}
	
	public void update() {
		setAction();
		
		collisionOn = false;
		gp.getCollisionChecker().checkTile(this);
		gp.getCollisionChecker().checkObject(this, false);
		gp.getCollisionChecker().checkEntity(this, gp.getNPCs());
		gp.getCollisionChecker().checkEntity(this, gp.getMonsters());
		boolean contactPlayer = gp.getCollisionChecker().checkPlayer(this);
		
		if (this.type == MONSTER && contactPlayer) {
			damagePlayer(this.attack);
		}
		
		if (!collisionOn) {
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
		}
		
		// how to influence the walking animation
		if (speed > 0) {
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

		if (invincible) {
			invincibleCounter++;
			if (invincibleCounter >= 40) {
				invincible = false;
				invincibleCounter = 0;
			}
		}
		
		if (shotAvailableCounter < 30) {
			shotAvailableCounter++;
		}
	}
	
	protected BufferedImage setupEntityImage(String imagePath, int width, int height) {
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream(imagePath));
			// scale player tile
			image = GraphicsTools.scaleTile(image, width, height);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		return image;
	}
	
	public void damagePlayer(int attack) {

		if (!gp.getPlayer().isInvincible()) {
			gp.playSoundEffect(10);
			
			int damage = attack - gp.getPlayer().getDefense();
			if (damage < 0) {
				damage = 0;
			}

			gp.getPlayer().decreaseHealth(damage);
			gp.getPlayer().setInvincible(true);
		}
	}
	
	public void draw(Graphics2D g2) {
	
		BufferedImage image = null;
		int screenX = worldX - gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX();
		int screenY = worldY - gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY();
		
		// if check so that we only draw tiles which are visible.
		if (worldX + tileSize > gp.getPlayer().getWorldX() - gp.getPlayer().getScreenX() &&
			worldX - tileSize < gp.getPlayer().getWorldX() + gp.getPlayer().getScreenX() &&
			worldY + tileSize > gp.getPlayer().getWorldY() - gp.getPlayer().getScreenY() &&
			worldY - tileSize < gp.getPlayer().getWorldY() + gp.getPlayer().getScreenY()) {

			switch(direction) {
			case "up":
				if (spriteNum == 1) {
					image = up1;
				} else {
					image = up2;
				}
				break;
				
			case "down":
				if (spriteNum == 1) {
					image = down1;
				} else {
					image = down2;
				}
				break;
				
			case "left":
				if (spriteNum == 1) {
					image = left1;
				} else {
					image = left2;
				}
				break;
				
			case "right":
				if (spriteNum == 1) {
					image = right1;
				} else {
					image = right2;
				}
				break;
			}

			if (type == MONSTER && hpBarOn == true) {
				// monster HP bar
				double oneScale = (double) tileSize / maxHealth;
				double hpBarValue = oneScale * currentHealth;
				
				g2.setColor(new Color(35, 35, 35));
				g2.fillRect(screenX - 1, screenY - 16, tileSize+2, 12);
				g2.setColor(new Color(255, 0, 30));
				g2.fillRect(screenX, screenY - 15, (int) hpBarValue, 10);
				
				hpBarCounter++;
				
				if (hpBarCounter >= 300) {
					// show bar for 300 frames or 5 seconds
					hpBarCounter = 0;
					hpBarOn = false;
				}
			}
			
			if (invincible) {
				hpBarCounter = 0;
				hpBarOn = true;
				// while invincible, make slightly transparent
				//g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
				changeAlpha(g2, 0.4f);
			}
			
			if (dying) {
				dyingAnimation(g2);
			}
			
			g2.drawImage(image, screenX, screenY, null);
			
			//g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			changeAlpha(g2, 1f);
		}
	}
	
	private void dyingAnimation(Graphics2D g2) {
		dyingCounter++;
		
		int deathRate = 5;
		
		changeAlpha(g2, 1f - (dyingCounter * deathRate * 0.01f));
		
		if (dyingCounter >= 20) {
			dying = false;
			alive = false;
		}
	}
	
	protected void changeAlpha(Graphics2D g2, float alpha) {
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
	}
	
	public int getWorldX() {
		return worldX;
	}

	public void setWorldX(int coordX) {
		this.worldX = coordX;
	}
	
	public int getWorldY() {
		return worldY;
	}
	
	public void setWorldY(int coordY) {
		this.worldY = coordY;
	}
	
	public int getSpeed() {
		return speed;
	}

	public Rectangle getSolidArea() {
		return solidArea;
	}

	public int getSolidAreaDefaultX() {
		return solidAreaDefaultX;
	}
	
	public int getSolidAreaDefaultY() {
		return solidAreaDefaultY;
	}
	
	public boolean isCollisionOn() {
		return collisionOn;
	}

	public void setCollisionOn(boolean collisionOn) {
		this.collisionOn = collisionOn;
	}
	
	public String getDirection() {
		return direction;
	}
	
	public BufferedImage getEntityImage() {
		return down1;
	}

	public int getCurrentHealth() {
		return this.currentHealth;
	}
	
	public void increaseHealth(int healthPoints) {
		if (currentHealth < maxHealth) {
			this.currentHealth += healthPoints;
			
			if (currentHealth > maxHealth) {
				// currentHealth cannot exceed the maximum
				currentHealth = maxHealth;
			}
		}
	}
	
	public void decreaseHealth(int healthPoints) {
		if (currentHealth > 0) {
			currentHealth -= healthPoints;
			
			if (currentHealth < 0) {
				// currentHealth cannot go below zero
				currentHealth = 0;
			}
		}
	}
	
	public void replenishHealth() {
		this.currentHealth = this.maxHealth;
	}

	public int getCurrentMana() {
		return this.currentMana;
	}
	
	public void increaseMana(int manaPoints) {
		if (currentMana < maxMana) {
			this.currentMana += manaPoints;
			
			if (currentMana > maxMana) {
				// currentMana cannot exceed the maximum
				currentMana = maxMana;
			}
		}
	}
	
	public void decreaseMana(int manaPoints) {
		if (currentMana > 0) {
			currentMana -= manaPoints;
			
			if (currentMana < 0) {
				// currentMana cannot go below zero
				currentMana = 0;
			}
		}
	}
	
	public void replenishMana() {
		this.currentMana = this.maxMana;
	}
	
	public BufferedImage getDn1() {
		return this.down1;
	}
	
	public BufferedImage getImage() {
		return this.image;
	}
	
	public BufferedImage getImage2() {
		return this.image2;
	}

	public BufferedImage getImage3() {
		return this.image3;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isCollision() {
		return collision;
	}

	public void setCollision(boolean collisionOn) {
		this.collision = collisionOn;
	}
	
	public int getType() {
		return this.type;
	}
	
	public void setType(int type) {
		this.type = type;
	}

	public boolean isInvincible() {
		return this.invincible;
	}
	
	public void setInvincible(boolean invincible) {
		this.invincible = invincible;
	}
	
	public boolean isAlive() {
		return this.alive;
	}
	
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	public boolean isDying() {
		return this.dying;
	}
	
	public void setDying(boolean dying) {
		this.dying = dying;
	}
	
	protected int getAttackValue() {
		return this.attackValue;
	}
	
	protected int getDefenseValue() {
		return this.defenseValue;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public int getLevel() {
		return level;
	}

	public int getStrength() {
		return strength;
	}

	public int getDexterity() {
		return dexterity;
	}

	public int getAttack() {
		return attack;
	}

	public int getDefense() {
		return defense;
	}

	public int getExperiencePoints() {
		return experiencePoints;
	}

	public void incrementExperiencePoints(int xp) {
		this.experiencePoints += xp;
	}
	
	public int getNextLevelExp() {
		return nextLevelExp;
	}

	public int getCoin() {
		return coin;
	}
	
	public void incrementCoin(int value) {
		this.coin += value;
	}

	public Entity getCurrentWeapon() {
		return currentWeapon;
	}

	public Entity getCurrentShield() {
		return currentShield;
	}
	
	public String getItemDescription() {
		return this.description;
	}
	
	public int getUseCost() {
		return this.useCost;
	}
	
	public Rectangle getAttackArea() {
		return this.attackArea;
	}
	
	public int getAmmunition() {
		return this.ammunition;
	}
	
	public void decreaseAmmunition(int amount) {
		this.ammunition -= amount;
	}
}
