package game2d;

import entity.Entity;

public class CollisionChecker {

	GamePanel gp;
	int tileSize;
	
	public CollisionChecker(GamePanel gp) {
		this.gp = gp;
		tileSize = gp.getTileSize();
	}
	
	public void checkTile(Entity entity) {

		int entityLeftWorldX = entity.getWorldX() + entity.getSolidArea().x;
		int entityRightWorldX = entity.getWorldX() + entity.getSolidArea().x
			+ entity.getSolidArea().width;
		int entityTopWorldY = entity.getWorldY() + entity.getSolidArea().y;
		int entityBottomWorldY = entity.getWorldY() + entity.getSolidArea().y
			+ entity.getSolidArea().height;
		
		int entityLeftCol = entityLeftWorldX / tileSize;
		int entityRightCol = entityRightWorldX / tileSize;
		int entityTopRow = entityTopWorldY / tileSize;
		int entityBottomRow = entityBottomWorldY / tileSize;
		
		int tileNum1, tileNum2;
		
		// check separately, so that we can detect collisions at angle movements		
		switch (entity.getDirection()) {
			case "up":
				entityTopRow = (entityTopWorldY - entity.getSpeed()) / tileSize;
				tileNum1 = gp.getTileManager().getMapTileCodes()[entityLeftCol][entityTopRow];
				tileNum2 = gp.getTileManager().getMapTileCodes()[entityRightCol][entityTopRow];
				
				if (gp.getTileManager().getTiles()[tileNum1].isCollision() ||
					gp.getTileManager().getTiles()[tileNum2].isCollision()) {
					entity.setCollisionOn(true);
				}
				break;
			case "down":
				entityBottomRow = (entityBottomWorldY + entity.getSpeed()) / tileSize;
				tileNum1 = gp.getTileManager().getMapTileCodes()[entityLeftCol][entityBottomRow];
				tileNum2 = gp.getTileManager().getMapTileCodes()[entityRightCol][entityBottomRow];
				
				if (gp.getTileManager().getTiles()[tileNum1].isCollision() ||
					gp.getTileManager().getTiles()[tileNum2].isCollision()) {
					entity.setCollisionOn(true);
				}
				break;
		}
				
		switch (entity.getDirection()) {
			case "left":
				entityLeftCol = (entityLeftWorldX - entity.getSpeed()) / tileSize;
				tileNum1 = gp.getTileManager().getMapTileCodes()[entityLeftCol][entityTopRow];
				tileNum2 = gp.getTileManager().getMapTileCodes()[entityLeftCol][entityBottomRow];
				
				if (gp.getTileManager().getTiles()[tileNum1].isCollision() ||
					gp.getTileManager().getTiles()[tileNum2].isCollision()) {
					entity.setCollisionOn(true);
				}
				break;
			case "right":
				entityRightCol = (entityRightWorldX + entity.getSpeed()) / tileSize;
				tileNum1 = gp.getTileManager().getMapTileCodes()[entityRightCol][entityTopRow];
				tileNum2 = gp.getTileManager().getMapTileCodes()[entityRightCol][entityBottomRow];
				
				if (gp.getTileManager().getTiles()[tileNum1].isCollision() ||
					gp.getTileManager().getTiles()[tileNum2].isCollision()) {
					entity.setCollisionOn(true);
				}
				break;
		}
	}
	
	public int checkObject(Entity entity, boolean isPlayer) {
		
		int index = 999;
		
		//for (SuperObject object : gp.getObjects()) {
		for (int counter = 0; counter < gp.getObjects().length; counter++) {
			Entity object = gp.getObjects()[counter];
		
			if (object != null) {
				// get entity's solid area position
				entity.getSolidArea().x = entity.getWorldX() + entity.getSolidArea().x;
				entity.getSolidArea().y = entity.getWorldY() + entity.getSolidArea().y;
				
				// get object's solid area position
				object.getSolidArea().x = object.getWorldX() + object.getSolidArea().x;
				object.getSolidArea().y = object.getWorldY() + object.getSolidArea().y;
				
				switch (entity.getDirection()) {
					case "up":
						entity.getSolidArea().y -= entity.getSpeed();
						if (entity.getSolidArea().intersects(object.getSolidArea())) {
							if (object.isCollision()) {
								entity.setCollisionOn(true);
							}
							if (isPlayer) {
								index = counter;
							}
						}
						break;
					case "down":
						entity.getSolidArea().y += entity.getSpeed();
						if (entity.getSolidArea().intersects(object.getSolidArea())) {
							if (object.isCollision()) {
								entity.setCollisionOn(true);
							}
							if (isPlayer) {
								index = counter;
							}
						}
						break;
					case "left":
						entity.getSolidArea().x -= entity.getSpeed();
						if (entity.getSolidArea().intersects(object.getSolidArea())) {
							if (object.isCollision()) {
								entity.setCollisionOn(true);
							}
							if (isPlayer) {
								index = counter;
							}
						}
						break;
					case "right":
						entity.getSolidArea().x += entity.getSpeed();
						if (entity.getSolidArea().intersects(object.getSolidArea())) {
							if (object.isCollision()) {
								entity.setCollisionOn(true);
							}
							if (isPlayer) {
								index = counter;
							}
						}
						break;
				}
				// reset numbers back to defaults
				entity.getSolidArea().x = entity.getSolidAreaDefaultX();
				entity.getSolidArea().y = entity.getSolidAreaDefaultY();
				object.getSolidArea().x = object.getSolidAreaDefaultX();
				object.getSolidArea().y = object.getSolidAreaDefaultY();
			}
		}
		
		return index;
	}
	
	public int checkEntity(Entity entity, Entity[] target) {
		
		int index = 999;
		
		//for (SuperObject object : gp.getObjects()) {
		for (int counter = 0; counter < target.length; counter++) {
		
			if (target[counter] != null) {
				// get entity's solid area position
				entity.getSolidArea().x = entity.getWorldX() + entity.getSolidArea().x;
				entity.getSolidArea().y = entity.getWorldY() + entity.getSolidArea().y;
				
				// get object's solid area position
				target[counter].getSolidArea().x = target[counter].getWorldX() + target[counter].getSolidArea().x;
				target[counter].getSolidArea().y = target[counter].getWorldY() + target[counter].getSolidArea().y;
				
				switch (entity.getDirection()) {
					case "up":
						entity.getSolidArea().y -= entity.getSpeed();
						break;
					case "down":
						entity.getSolidArea().y += entity.getSpeed();
						break;
					case "left":
						entity.getSolidArea().x -= entity.getSpeed();
						break;
					case "right":
						entity.getSolidArea().x += entity.getSpeed();
						break;
				}

				if (entity.getSolidArea().intersects(target[counter].getSolidArea())) {
					if (target[counter] != entity) {
						entity.setCollisionOn(true);
						index = counter;
					}
				}
				
				// reset numbers back to defaults
				entity.getSolidArea().x = entity.getSolidAreaDefaultX();
				entity.getSolidArea().y = entity.getSolidAreaDefaultY();
				target[counter].getSolidArea().x = target[counter].getSolidAreaDefaultX();
				target[counter].getSolidArea().y = target[counter].getSolidAreaDefaultY();
			}
		}
		
		return index;
	}
	
	public boolean checkPlayer(Entity entity) {
		boolean contactPlayer = false;
		
		// get entity's solid area position
		entity.getSolidArea().x = entity.getWorldX() + entity.getSolidArea().x;
		entity.getSolidArea().y = entity.getWorldY() + entity.getSolidArea().y;
		
		// get player's solid area position
		gp.getPlayer().getSolidArea().x = gp.getPlayer().getWorldX() + gp.getPlayer().getSolidArea().x;
		gp.getPlayer().getSolidArea().y = gp.getPlayer().getWorldY() + gp.getPlayer().getSolidArea().y;
		
		switch (entity.getDirection()) {
			case "up":
				entity.getSolidArea().y -= entity.getSpeed();
				break;
			case "down":
				entity.getSolidArea().y += entity.getSpeed();
				break;
			case "left":
				entity.getSolidArea().x -= entity.getSpeed();
				break;
			case "right":
				entity.getSolidArea().x += entity.getSpeed();
				break;
		}
		
		if (entity.getSolidArea().intersects(gp.getPlayer().getSolidArea())) {
			entity.setCollisionOn(true);
			contactPlayer = true;
		}

		// reset numbers back to defaults
		entity.getSolidArea().x = entity.getSolidAreaDefaultX();
		entity.getSolidArea().y = entity.getSolidAreaDefaultY();
		gp.getPlayer().getSolidArea().x = gp.getPlayer().getSolidAreaDefaultX();
		gp.getPlayer().getSolidArea().y = gp.getPlayer().getSolidAreaDefaultY();
		
		return contactPlayer;
	}
}
