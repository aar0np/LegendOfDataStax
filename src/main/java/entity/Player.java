package entity;

//import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import game2d.GamePanel;
import game2d.KeyHandler;
//import object.Fireball;
//import object.Key;
//import object.Rock;
//import object.ShieldWood;
//import object.SwordNormal;
import object.Sword;

public class Player extends Entity {
	int tileSize;
	int maxInventorySize = 20;
	// int hasKey = 0;

	boolean attackCanceled = false;

	List<Entity> inventory = new ArrayList<>();
	KeyHandler keyHandler;

	final int screenX;
	final int screenY;
	
	public Player(GamePanel gp, KeyHandler keyH) {
		super(gp);
		
		this.keyHandler = keyH;
		
		tileSize = gp.getTileSize();
		
		// computing the middle of the screen
		screenX = (gp.getScreenWidth() / 2) - (tileSize / 2);
		screenY = (gp.getScreenHeight() / 2) - (tileSize / 2);
		
		// solid pixel area of player starts at 8,16 and is a 32x32 square
		solidArea = new Rectangle(8, 16, 32, 32);
		
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
				
		setDefaultValues();
		setItems();
		getPlayerImages();
		getPlayerAttackImages();
	}
	
	private void setDefaultValues() {
		// position
		worldX = gp.getTileSize() * 25;
		worldY = gp.getTileSize() * 25;
		direction = "down";

		// stats
		speed = 4;
		maxHealth = 6;
		currentHealth = maxHealth;
		maxMana = 4;
		currentMana = maxMana;
		ammunition = 10;
		level = 1;
		strength = 1;
		dexterity = 1;
		experiencePoints = 0;
		nextLevelExp = 5;
		coin = 0;
		currentWeapon = new Sword(gp);
		//currentShield = new ShieldWood(gp);
		//projectile = new Fireball(gp);
		//projectile = new Rock(gp);
		attack = computeAttack();
		defense = computeDefense();
	}
	
	private void setItems() {
		// add starting weapon and shield
		inventory.add(currentWeapon);
		inventory.add(currentShield);
		
		// add two keys
		//inventory.add(new Key(gp));
		//inventory.add(new Key(gp));
	}
	
	private void getPlayerImages() {
		
		// opening
		image = setupEntityImage("/player/stax_raising_sword.png", tileSize, tileSize);
		// movement
		up1 = setupEntityImage("/player/stax_up_1.png", tileSize, tileSize);
		up2 = setupEntityImage("/player/stax_up_2.png", tileSize, tileSize);
		down1 = setupEntityImage("/player/stax_down_1.png", tileSize, tileSize);
		down2 = setupEntityImage("/player/stax_down_2.png", tileSize, tileSize);
		right1 = setupEntityImage("/player/stax_right_1.png", tileSize, tileSize);
		right2 = setupEntityImage("/player/stax_right_2.png", tileSize, tileSize);
		left1 = setupEntityImage("/player/stax_left_1.png", tileSize, tileSize);
		left2 = setupEntityImage("/player/stax_left_2.png", tileSize, tileSize);
	}
	
	private void getPlayerAttackImages() {
		
		if (currentWeapon.type == SWORD) {
			upAttack1 = setupEntityImage("/player/stax_attack_up_1.png", tileSize, tileSize * 2);
			upAttack2 = setupEntityImage("/player/stax_attack_up_2.png", tileSize, tileSize * 2);
			downAttack1 = setupEntityImage("/player/stax_attack_down_1.png", tileSize, tileSize * 2);
			downAttack2 = setupEntityImage("/player/stax_attack_down_2.png", tileSize, tileSize * 2);
			rightAttack1 = setupEntityImage("/player/stax_attack_right_1.png", tileSize * 2, tileSize);
			rightAttack2 = setupEntityImage("/player/stax_attack_right_2.png", tileSize * 2, tileSize);
			leftAttack1 = setupEntityImage("/player/stax_attack_left_1.png", tileSize * 2, tileSize);
			leftAttack2 = setupEntityImage("/player/stax_attack_left_2.png", tileSize * 2, tileSize);
		}
	}
	
	public void update() {
		
		if (attacking) {
			attack();
		} else if (keyHandler.isUpPressed() || keyHandler.isDownPressed()
				|| keyHandler.isRightPressed() || keyHandler.isLeftPressed()
				|| keyHandler.isEnterPressed()) {

			// This used to be one big if/else, but I separated them
			// to allow diagonal movement.
			if (keyHandler.isUpPressed()) {
				direction = "up";
			} else if (keyHandler.isDownPressed()) {
				direction = "down";
			}

			// check tile collision
			collisionOn = false;
			gp.getCollisionChecker().checkTile(this);

			// check object collision
			int objectIndex = gp.getCollisionChecker().checkObject(this, true);
			//pickUpObject(objectIndex);

			// check NPC collision
			int npcIndex = gp.getCollisionChecker().checkEntity(this, gp.getNPCs());

			// check Monster collision
			int monsterIndex = gp.getCollisionChecker().checkEntity(this, gp.getMonsters());
			//contactMonster(monsterIndex);
			
			if (!collisionOn && !gp.getKeyHandler().isEnterPressed()) {
				switch (direction) {
					case "up":
						worldY -= speed;
						break;
					case "down":
						worldY += speed;
						break;
				}
			}
			
			// now check left/right for collision
			if (keyHandler.isLeftPressed()) {
				direction = "left";
			} else if (keyHandler.isRightPressed()) {
				direction = "right";
			}

			// check tile collision again
			collisionOn = false;
			gp.getCollisionChecker().checkTile(this);

			// check object collision again
			objectIndex = gp.getCollisionChecker().checkObject(this, true);
			pickUpObject(objectIndex);

			// check NPC collision again
			npcIndex = gp.getCollisionChecker().checkEntity(this, gp.getNPCs());
			interactWNPC(npcIndex, gp.getKeyHandler().isEnterPressed());

			// check Monster collision
			monsterIndex = gp.getCollisionChecker().checkEntity(this, gp.getMonsters());
			contactMonster(monsterIndex);

			// check events (should only need to do this once
			// ...maybe?
			gp.getEventHandler().checkEvent();
						
			if (!collisionOn && !gp.getKeyHandler().isEnterPressed()) {
				switch (direction) {
					case "left":
						worldX -= speed;
						break;
					case "right":
						worldX += speed;
						break;						
				}
			}
			
			if (gp.getKeyHandler().isEnterPressed() && !gp.getPlayer().isAttackCanceled()) {
				gp.playSoundEffect(13);
				attacking = true;
				spriteCounter = 0;
			}
			
			gp.getPlayer().setAttackCanceled(false);
			gp.getKeyHandler().setEnterPressed(false);
			
			// how to influence the walking animation
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
		
		if (gp.getKeyHandler().isShotKeyPressed() && !projectile.isAlive()
				&& shotAvailableCounter >= 30 && projectile.hasResource(this)) {
			// did the player press the "shot" key, and is another projectile already on the screen
			projectile.set(worldX, worldY, direction, true, this);
			projectile.decreaseResource(this);
			gp.getProjectiles().add(projectile);
			gp.playSoundEffect(16);
			shotAvailableCounter = 0;
		}
		
		if (invincible) {
			invincibleCounter++;
			if (invincibleCounter >= 60) {
				invincible = false;
				invincibleCounter = 0;
			}
		}
		
		// controlling "autofire bug" for projectiles
		if (shotAvailableCounter < 30) {
			shotAvailableCounter++;
		}
	}
	
// RPG version of pickUpObject
//
	private void pickUpObject(int index) {

		if (index != 999) {
			
			if (gp.getObjects()[index].getType() == REGULAR_ITEM) {
				// regular items
				gp.getObjects()[index].use(this);
				gp.getObjects()[index] = null;
			} else {
				//inventoried items				
				if (inventory.size() < maxInventorySize) {
					
					inventory.add(gp.getObjects()[index]);
					gp.getGameUI().addMessage("Picked up " + gp.getObjects()[index].getName() + "!");
					gp.playSoundEffect(1);
					gp.getObjects()[index] = null;
				} else {
					// inventory is full!
					gp.getGameUI().addMessage("Inventory is full!");				
				}
			}

		}
		
	}
	
	private void interactWNPC(int index, boolean attackOrInteract) {
		
		if (attackOrInteract) {
			if (index != 999) {
				gp.playSoundEffect(11);
				gp.setGameState(gp.DIALOG_STATE);
				gp.getNPCs()[index].speak();
				attackCanceled = true;
			}
		}
	}
	
	private void contactMonster(int index) {
		if (index != 999) {
			if (!invincible) {
				gp.playSoundEffect(10);
				
				int damage = gp.getMonsters()[index].getAttack() - defense;
				if (damage < 0) {
					damage = 0;
				}
				
				decreaseHealth(damage);
				invincible = true;
			}
		}
	}

	public void damageMonster(int index, int attack) {
		if (index != 999) {

			if (!gp.getMonsters()[index].isInvincible()) {
				gp.playSoundEffect(8);

				int damage = attack - gp.getMonsters()[index].getDefense();
				if (damage < 0) {
					damage = 0;
				}
				
				gp.getMonsters()[index].decreaseHealth(damage);
				gp.getGameUI().addMessage(damage + " damage!");
				gp.getMonsters()[index].setInvincible(true);
				gp.getMonsters()[index].damageReaction();
				
				if (gp.getMonsters()[index].getCurrentHealth() <= 0) {
					// monster begins to die
					gp.getMonsters()[index].setDying(true);
					gp.getGameUI().addMessage(gp.getMonsters()[index].name + " killed!");
					gp.getGameUI().addMessage("XP + " + gp.getMonsters()[index].getExperiencePoints());
					incrementExperiencePoints(gp.getMonsters()[index].getExperiencePoints());
					checkLevelUp();
				}
			}
		}
	}

	private void checkLevelUp() {
		if (experiencePoints >= nextLevelExp) {
			// level up!
			level++;
			nextLevelExp = nextLevelExp * 4;
			maxHealth += 2;
			strength++;
			dexterity++;
			// recompute attack and defense modifiers
			attack = computeAttack();
			defense = computeDefense();
			
			// play sound
			gp.playSoundEffect(14);
			
			// show message in dialog window
			gp.setGameState(gp.DIALOG_STATE);
			gp.getGameUI().setCurrentDialog("You are now level #" + level + "!");
		}
	}
	
	private void attack() {
		
		spriteCounter++;
		if (spriteCounter <= 5) {
			// first 5 frames show attack1 image
			spriteNum = 1;
		} else if (spriteCounter > 5 && spriteCounter <= 25) {
			// second 20 frames show attack2 image
			spriteNum = 2;
			
			// check if attack hits
			int currentWorldX = worldX;
			int currentWorldY = worldY;
			int solidAreaWidth = solidArea.width;
			int solidAreaHeight = solidArea.height;
			
			switch (direction) {
				case "up":
					worldY -= attackArea.height;
					break;
				case "down":
					worldY += attackArea.height;
					break;
				case "left":
					worldX -= attackArea.width;
					break;
				case "right":
					worldX += attackArea.width;
					break;
			}
			
			solidArea.width = attackArea.width;
			solidArea.height = attackArea.height;
			
			// check if our attack has struck any of the monsters
			int monsterIndex = gp.getCollisionChecker().checkEntity(this, gp.getMonsters());
			damageMonster(monsterIndex, this.attack);
			
			// restore original values
			worldX = currentWorldX;
			worldY = currentWorldY;
			solidArea.width = solidAreaWidth;
			solidArea.height = solidAreaHeight;
			
		} else {
			// show attack 1 image for remainder of frames
			spriteNum = 1;
			spriteCounter = 0;
			attacking = false;
		}
	}
	
	protected int computeAttack() {
		attackArea = currentWeapon.getAttackArea();
		return strength * currentWeapon.getAttackValue();
	}
	
// Treasure hunt version pickUpObject	
//	
//	public void pickUpObject(int index) {
//		
//		if (index != 999) {
//			String objName = gp.getObjects()[index].getName();
//			
//			switch (objName) {
//				case "Key":
//					hasKey++;
//					gp.playSoundEffect(1);
//					gp.getObjects()[index] = null;
//					gp.getGameUI().showMessage("Key found!");
//					break;
//				case "Door":
//					if (hasKey > 0) {
//						gp.playSoundEffect(3);
//						gp.getObjects()[index] = null;
//						gp.getGameUI().showMessage("Door opened!");
//						hasKey--;
//					} else {
//						gp.getGameUI().showMessage("Door requires a key!");
//						// gp.playSoundEffect(5);
//					}
//					break;
//				case "Boots":
//					speed += 2;
//					gp.playSoundEffect(2);
//					gp.getObjects()[index] = null;
//					gp.getGameUI().showMessage("Boots of speed acquired!");
//					break;
//				case "Chest":
//					gp.getGameUI().setIsGameFinished(true);
//					gp.stopMusic();
//					gp.playSoundEffect(4);
//					break;
//			}
//		}
//	}
	
	public void draw(Graphics2D g2) {

		// testing
		//g2.setColor(Color.white);
		//g2.fillRect(x, y, tileSize, tileSize);
		
		BufferedImage image = null;
		
		int tempScreenX = screenX;
		int tempScreenY = screenY;
		
		switch(direction) {
			case "up":
				if (!attacking) {
					if (spriteNum == 1) {
						image = up1;
					} else {
						image = up2;
					}
				} else {
					tempScreenY -= tileSize; 
					if (spriteNum == 1) {
						image = upAttack1;
					} else {
						image = upAttack2;
					}
				}
				break;
				
			case "down":
				if (!attacking) {
					if (spriteNum == 1) {
						image = down1;
					} else {
						image = down2;
					}
				} else {
					if (spriteNum == 1) {
						image = downAttack1;
					} else {
						image = downAttack2;
					}
				}
				break;
				
			case "left":
				if (!attacking) {
					if (spriteNum == 1) {
						image = left1;
					} else {
						image = left2;
					}
				} else {
					tempScreenX -= tileSize;
					if (spriteNum == 1) {
						image = leftAttack1;
					} else {
						image = leftAttack2;
					}					
				}
				break;
				
			case "right":
				if (!attacking) {
					if (spriteNum == 1) {
						image = right1;
					} else {
						image = right2;
					}
				} else {
					if (spriteNum == 1) {
						image = rightAttack1;
					} else {
						image = rightAttack2;
					}
				}
				break;
		}
		
		if (invincible) {
			// while invincible, make slightly transparent
			// g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
			changeAlpha(g2, 0.5f);
		}
		
		g2.drawImage(image, tempScreenX, tempScreenY, null);
		
		//g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		changeAlpha(g2, 1f);
	}
	
	public int getScreenX() {
		return this.screenX;
	}
	
	public int getScreenY() {
		return this.screenY;
	}
	
//	public int getKeys() {
//		return hasKey;
//	}
	
	public int getMaxHealth() {
		return this.maxHealth;
	}
	
	public int getCurrentHealth() {
		return this.currentHealth;
	}
	
	public int getMaxMana() {
		return this.maxMana;
	}
	
	public int getCurrentMana() {
		return this.currentMana;
	}
	
	public boolean isInvincible() {
		return this.invincible;
	}
	
	public void setInvincible(boolean invincible) {
		this.invincible = invincible;
	}
	
	public boolean isAttackCanceled() {
		return this.attackCanceled;
	}
	
	public void setAttackCanceled(boolean attackCanceled) {
		this.attackCanceled = attackCanceled;
	}
	
	public List<Entity> getInventory() {
		return this.inventory;
	}
	
	public void equipItem () {
		
		int itemIndex = gp.getGameUI().getItemIndexFromSlot();
		
		if (itemIndex < maxInventorySize) {
			Entity selectedItem = inventory.get(itemIndex);
			
			if (selectedItem.type == SWORD || selectedItem.type == AXE) {
				currentWeapon = selectedItem;
				attack = computeAttack();
				getPlayerAttackImages();
			} else if (selectedItem.type == SHIELD) {
				currentShield = selectedItem;
				defense = computeDefense();
			} else if (selectedItem.type == CONSUMABLE) {
				selectedItem.use(this);
				inventory.remove(itemIndex);
			}
		}
	}
 }
