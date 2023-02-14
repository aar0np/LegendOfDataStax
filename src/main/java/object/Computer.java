package object;

import entity.Entity;
import game2d.GamePanel;

public class Computer extends Entity {
	
	public Computer(GamePanel gp) {
		super(gp);

		name = "Computer";
		description = "An old PC.";
		down1 = setupEntityImage("/objects/computer.png", tileSize, tileSize);
		collision = false;
	}
}
