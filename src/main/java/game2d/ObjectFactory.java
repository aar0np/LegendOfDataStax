package game2d;

import entity.Patrick;
import monster.GreenDragon;
//import monster.GreenSlime;
//import object.Axe;
//import object.BlueShield;
//import object.BronzeCoin;
//import object.Heart;
//import object.Boots;
//import object.Chest;
//import object.Door;
//import object.Key;
//import object.ManaCrystal;
//import object.RedPotion;
import monster.RedDragon;
import monster.YellowDragon;
import object.Sign;

public class ObjectFactory {

	GamePanel gp;
	int tileSize;
	
	public ObjectFactory(GamePanel gamePanel) {
		this.gp = gamePanel;
		tileSize = gp.getTileSize();
	}
	
	// RPG version generateObjects
	//
	public void generateObjects() {
		
		gp.getObjects()[0] = new Sign(gp);
		gp.getObjects()[0].setWorldX(18 * tileSize);
		gp.getObjects()[0].setWorldY(35 * tileSize);
		
		// keys
//		gp.getObjects()[0] = new Key(gp);
//		gp.getObjects()[0].setWorldX(23 * tileSize);
//		gp.getObjects()[0].setWorldY(7 * tileSize);
//		
//		gp.getObjects()[1] = new Key(gp);
//		gp.getObjects()[1].setWorldX(23 * tileSize);
//		gp.getObjects()[1].setWorldY(40 * tileSize);
//
//		gp.getObjects()[2] = new Key(gp);
//		gp.getObjects()[2].setWorldX(38 * tileSize);
//		gp.getObjects()[2].setWorldY(8 * tileSize);
//		
//		// Woodcutter's Axe
//		gp.getObjects()[3] = new Axe(gp);
//		gp.getObjects()[3].setWorldX(37 * tileSize);
//		gp.getObjects()[3].setWorldY(21 * tileSize);
//
//		// Blue Shield
//		gp.getObjects()[4] = new BlueShield(gp);
//		gp.getObjects()[4].setWorldX(35 * tileSize);
//		gp.getObjects()[4].setWorldY(23 * tileSize);
//
//		// Red Potion
//		gp.getObjects()[5] = new RedPotion(gp);
//		gp.getObjects()[5].setWorldX(22 * tileSize);
//		gp.getObjects()[5].setWorldY(27 * tileSize);
//
//		// coins
//		gp.getObjects()[6] = new BronzeCoin(gp);
//		gp.getObjects()[6].setWorldX(26 * tileSize);
//		gp.getObjects()[6].setWorldY(21 * tileSize);
//
//		gp.getObjects()[7] = new BronzeCoin(gp);
//		gp.getObjects()[7].setWorldX(21 * tileSize);
//		gp.getObjects()[7].setWorldY(19 * tileSize);
//
//		gp.getObjects()[8] = new BronzeCoin(gp);
//		gp.getObjects()[8].setWorldX(23 * tileSize);
//		gp.getObjects()[8].setWorldY(27 * tileSize);
//
//		// Mana Crystals
//		gp.getObjects()[9] = new ManaCrystal(gp);
//		gp.getObjects()[9].setWorldX(22 * tileSize);
//		gp.getObjects()[9].setWorldY(31 * tileSize);
//
//		// Hearts
//		gp.getObjects()[10] = new Heart(gp);
//		gp.getObjects()[10].setWorldX(22 * tileSize);
//		gp.getObjects()[10].setWorldY(29 * tileSize);

	}
	
	public void generateNPCs() {
		
		gp.npcs[0] = new Patrick(gp);
		gp.npcs[0].setWorldX(tileSize * 25);
		gp.npcs[0].setWorldY(tileSize * 12);		
	}
	
	public void generateMonsters() {

		gp.monsters[0] = new GreenDragon(gp);
		gp.monsters[0].setWorldX(tileSize * 23);
		gp.monsters[0].setWorldY(tileSize * 30);

		gp.monsters[1] = new RedDragon(gp);
		gp.monsters[1].setWorldX(tileSize * 23);
		gp.monsters[1].setWorldY(tileSize * 31);

		gp.monsters[2] = new YellowDragon(gp);
		gp.monsters[2].setWorldX(tileSize * 24);
		gp.monsters[2].setWorldY(tileSize * 27);

//		gp.monsters[3] = new GreenSlime(gp);
//		gp.monsters[3].setWorldX(tileSize * 34);
//		gp.monsters[3].setWorldY(tileSize * 42);
//
//		gp.monsters[4] = new GreenSlime(gp);
//		gp.monsters[4].setWorldX(tileSize * 38);
//		gp.monsters[4].setWorldY(tileSize * 42);
	}
	
// treasure hunting verison generateObjects
//
// 	public void generateObjects() {
//
//		// keys
//		gp.getObjects()[0] = new Key(tileSize);
//		gp.getObjects()[0].setWorldX(23 * tileSize);
//		gp.getObjects()[0].setWorldY(7 * tileSize);
//		
//		gp.getObjects()[1] = new Key(tileSize);
//		gp.getObjects()[1].setWorldX(23 * tileSize);
//		gp.getObjects()[1].setWorldY(40 * tileSize);
//
//		gp.getObjects()[2] = new Key(tileSize);
//		gp.getObjects()[2].setWorldX(38 * tileSize);
//		gp.getObjects()[2].setWorldY(8 * tileSize);
//
//		// doors
//		gp.getObjects()[3] = new Door(tileSize);
//		gp.getObjects()[3].setWorldX(10 * tileSize);
//		gp.getObjects()[3].setWorldY(11 * tileSize);
//
//		gp.getObjects()[4] = new Door(tileSize);
//		gp.getObjects()[4].setWorldX(8 * tileSize);
//		gp.getObjects()[4].setWorldY(28 * tileSize);
//
//		gp.getObjects()[5] = new Door(tileSize);
//		gp.getObjects()[5].setWorldX(12 * tileSize);
//		gp.getObjects()[5].setWorldY(22 * tileSize);
//
//		// chests
//		gp.getObjects()[6] = new Chest(tileSize);
//		gp.getObjects()[6].setWorldX(10 * tileSize);
//		gp.getObjects()[6].setWorldY(7 * tileSize);
//		
//		// boots
//		gp.getObjects()[7] = new Boots(tileSize);
//		gp.getObjects()[7].setWorldX(37 * tileSize);
//		gp.getObjects()[7].setWorldY(42 * tileSize);
//	}
}
