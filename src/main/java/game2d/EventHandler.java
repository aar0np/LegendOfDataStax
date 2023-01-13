package game2d;

public class EventHandler {

	GamePanel gp;
	EventRect eventRectangle[][];
	int tileSize;
	int previousEventX;
	int previousEventY;
	boolean canTouchEvent;
	
	public EventHandler(GamePanel gp) {
		this.gp = gp;
		tileSize = gp.getTileSize();
		
		eventRectangle = new EventRect[gp.getMaxWorldColumn()][gp.getMaxWorldRow()];
		
		int col = 0;
		int row = 0;
		while (col < gp.getMaxWorldColumn() && row < gp.getMaxWorldRow()) {

			eventRectangle[col][row] = new EventRect();
			eventRectangle[col][row].x = 23;
			eventRectangle[col][row].y = 23;
			eventRectangle[col][row].width = 2;
			eventRectangle[col][row].height = 2;
			eventRectangle[col][row].setEventRectDefaultX(eventRectangle[col][row].x);
			eventRectangle[col][row].setEventRectDefaultY(eventRectangle[col][row].y);		

			col++;
			if (col >= gp.getMaxWorldColumn()) {
				col = 0;
				row++;
			}
		}	
	}
	
	public void checkEvent() {
		
		canTouchEvent = computeIfPlayerCanReTouch(gp.getPlayer().getWorldX(), gp.getPlayer().getWorldY());
		
//		// damage pit
//		if (hit(27, 16, "right") && canTouchEvent) {
//			damagePit(gp.DIALOG_STATE);
//			//eventRectangle[27][16].setEventDone(true);
//			canTouchEvent = false;
//		}
//
//		// teleport
//		if (hit(23, 7, "any")) {
//			teleport(gp.DIALOG_STATE);
//		}
//
//		// healing pool
//		if (hit(23, 12, "up")) {
//			healingPool(gp.DIALOG_STATE);
//		}
	}
	
	private boolean computeIfPlayerCanReTouch(int playerX, int playerY) {
		// check if player is more than 1 tile away from last event
		boolean canTouch = false;
		
		int xDistance = Math.abs(playerX - previousEventX);
		int yDistance = Math.abs(playerY - previousEventY);
		int distance = Math.max(xDistance, yDistance);
		
		if (distance > tileSize) {
			canTouch = true;
		}
		
		return canTouch;
	}
	
	public boolean hit(int eventCol, int eventRow, String requiredDirection) {
	
		boolean hit = false;
		
		gp.getPlayer().getSolidArea().x = gp.getPlayer().getWorldX() + gp.getPlayer().getSolidArea().x;
		gp.getPlayer().getSolidArea().y = gp.getPlayer().getWorldY() + gp.getPlayer().getSolidArea().y;
		eventRectangle[eventCol][eventRow].x = (eventCol * tileSize) + (eventRectangle[eventCol][eventRow].x);
		eventRectangle[eventCol][eventRow].y = (eventRow * tileSize) + (eventRectangle[eventCol][eventRow].y);
		
		if (gp.getPlayer().getSolidArea().intersects(eventRectangle[eventCol][eventRow])) {
			if (gp.getPlayer().getDirection().equals(requiredDirection)	|| requiredDirection.equals("any")) {
				hit = true;
				
				previousEventX = gp.getPlayer().getWorldX();
				previousEventY = gp.getPlayer().getWorldY();
			}	
		}
		
		// reset player solid area to defaults
		gp.getPlayer().getSolidArea().x = gp.getPlayer().getSolidAreaDefaultX();
		gp.getPlayer().getSolidArea().y = gp.getPlayer().getSolidAreaDefaultY();
		eventRectangle[eventCol][eventRow].x = eventRectangle[eventCol][eventRow].getEventRectDefaultX();
		eventRectangle[eventCol][eventRow].y = eventRectangle[eventCol][eventRow].getEventRectDefaultY();
		
		return hit;
	}
	
	public void damagePit(int gameState) {
		gp.setGameState(gameState);
		gp.getGameUI().setCurrentDialog("You have fallen into the pit!");
		gp.getPlayer().decreaseHealth(1);
	}
	
	public void healingPool(int gameState) {
		if (gp.getKeyHandler().isEnterPressed()) {
			gp.setGameState(gameState);
			gp.getPlayer().setAttackCanceled(true);
			gp.getGameUI().setCurrentDialog("You enjoy some healing water.");
			gp.getPlayer().replenishHealth();
			gp.getPlayer().replenishMana();
			
			// new twist - if you use healing water, monsters respawn!
			gp.getObjectFactory().generateMonsters();
		}
	}
	
	public void teleport(int gameState) {
		gp.setGameState(gameState);
		gp.playSoundEffect(2);
		gp.gameUI.setCurrentDialog("You have teleported!");
		// teleport player to 37,10
		gp.getPlayer().setWorldX(tileSize * 37);
		gp.getPlayer().setWorldY(tileSize * 10);
	}
}
