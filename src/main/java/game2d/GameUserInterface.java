package game2d;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
//import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import entity.Entity;
import object.Heart;
//import object.ManaCrystal;

//import object.Key;

public class GameUserInterface {
	private boolean gameFinished = false;
	
	public int slotCol = 0;
	public int slotRow = 0;
	
	GamePanel gp;
	Graphics2D g2;
	Font arialP40 = new Font("Arial", Font.PLAIN, 40);
	Font arialB80 = new Font("Arial", Font.BOLD, 80);
	Font tron;
	BufferedImage heartFull;
	BufferedImage heartHalf;
	BufferedImage heartBlank;
	BufferedImage manaCrystalFull;
	BufferedImage manaCrystalBlank;
	int tileSize;
	int commandNum = 0;
	String currentDialog = "";
	List<String> messages = new ArrayList<>();
	List<Integer> messagesCounter = new ArrayList<>();

	public GameUserInterface(GamePanel gp) {
		this.gp = gp;
		
		tileSize = gp.getTileSize();
		
		//try {
		//	InputStream fontIS = getClass().getResourceAsStream("/fonts/TRON.TTF");
		//	tron = Font.createFont(Font.TRUETYPE_FONT, fontIS);
		//} catch (Exception ex) {
		//	ex.printStackTrace();
		//}
		
		
		// Key key = new Key(tileSize);
		// keyImage = key.getImage();
		// key = null;
		
		Entity heart = new Heart(gp);
		heartFull = heart.getImage();
		heartHalf = heart.getImage2();
		heartBlank = heart.getImage3();
		
//		Entity manaCrystal = new ManaCrystal(gp);
//		manaCrystalFull = manaCrystal.getImage();
//		manaCrystalBlank = manaCrystal.getImage2();
	}
	
	public void addMessage(String message) {
		
		messages.add(message);
		messagesCounter.add(0);
	}
	
// RPG version of draw	
	public void draw(Graphics2D g2) {
		this.g2 = g2;
		
		g2.setFont(arialB80);
		//g2.setFont(tron);
		g2.setColor(Color.white);
		

		if (gp.getGameState() == gp.PLAY_STATE) {
			// Play state
			drawPlayerHearts();
			drawMessages();
		} else if (gp.getGameState() == gp.TITLE_STATE) {
			// title screen
			drawTitleScreen();
	    } else if (gp.getGameState() == gp.PAUSE_STATE) {
			// Pause state
			drawPauseScreen();
		} else if (gp.getGameState() == gp.DIALOG_STATE) {
			drawPlayerHearts();
			drawDialogScreen();
		} else if (gp.getGameState() == gp.CHARACTER_SHEET_STATE) {
			drawCharacterScreen();
			drawInventoryScreen();
		}
	}
	
	private void drawPlayerHearts() {
		
		// draw max life
		int heartX = tileSize / 2;
		int heartY = tileSize / 2;
		
		for (int counter = 0; counter < gp.getPlayer().getMaxHealth() / 2; counter++) {
			g2.drawImage(heartBlank, heartX, heartY, null);
			heartX += tileSize;
		}

		// draw current life
		heartX = tileSize / 2;
		heartY = tileSize / 2;
		int heartCounter = 0;
		
		while (heartCounter < gp.getPlayer().getCurrentHealth()) {
			
			if (heartCounter + 1 < gp.getPlayer().getCurrentHealth()) {
				g2.drawImage(heartFull, heartX, heartY, null);
				heartCounter+=2;
			} else {
				g2.drawImage(heartHalf, heartX, heartY, null);
				heartCounter++;
			}
			
			heartX += tileSize;
		}
		
		// draw max mana
		int manaX = (tileSize / 2) - 5;
		int manaY = (int)(tileSize * 1.5);
		
		for (int counter = 0; counter < gp.getPlayer().getMaxMana(); counter++) {
			g2.drawImage(manaCrystalBlank, manaX, manaY, null);
			manaX += 35;
		}
		
		// draw current mana
		manaX = (tileSize / 2) - 5;
		manaY = (int)(tileSize * 1.5);
		int manaCounter = 0;
		
		while (manaCounter < gp.getPlayer().getCurrentMana()) {
			g2.drawImage(manaCrystalFull, manaX, manaY, null);
			manaX += 35;
			manaCounter++;
		}

	}
	
	private void drawMessages() {
		int messageX = tileSize;
		int messageY = tileSize * 4;
		
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 24F));
		
		for (int messageIndex = 0; messageIndex < messages.size(); messageIndex++) {
			
			if (messages.get(messageIndex) != null) {
				// shadow
				g2.setColor(Color.black);
				g2.drawString(messages.get(messageIndex), messageX + 2, messageY + 2);
				// text
				g2.setColor(Color.white);
				g2.drawString(messages.get(messageIndex), messageX, messageY);
				
				int counter = messagesCounter.get(messageIndex) + 1;
				messagesCounter.set(messageIndex, counter);
				messageY += 50;
				
				if (messagesCounter.get(messageIndex) > 180) {
					messages.remove(messageIndex);
					messagesCounter.remove(messageIndex);
				}
			}
		}
	}
	
	private void drawTitleScreen() {
		
		// background
		g2.setColor(new Color(0, 0, 120));
		g2.fillRect(0,  0, gp.getScreenWidth(), gp.getScreenHeight());
		
		// title
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,72F));
		String text1 = "The Legend of";
		String text2 = "DataStax";
		
		// text1
		int titleX = getXForCenteredText(text1);
		int titleY = tileSize * 2;
		
		// shadow
		g2.setColor(Color.darkGray);
		g2.drawString(text1, titleX + 5, titleY + 5);
		
		// main color
		g2.setColor(Color.white);
		g2.drawString(text1, titleX, titleY);

		// text2
		titleX = getXForCenteredText(text2);
		titleY += (tileSize * 1.5);
		
		// shadow
		g2.setColor(Color.darkGray);
		g2.drawString(text2, titleX + 5, titleY + 5);
		
		// main color
		g2.setColor(Color.white);
		g2.drawString(text2, titleX, titleY);

		// Stax image
		int bbX = gp.getScreenWidth() / 2;
		int bbY = tileSize + 220;
		//g2.drawImage(gp.getPlayer().getEntityImage(), bbX - tileSize + 5, bbY, tileSize *2, tileSize *2, null);
		bbY = tileSize * 4;
		g2.drawImage(gp.getPlayer().getImage(), bbX - tileSize, bbY, tileSize * 2, tileSize * 4, null);
		
		// menu
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,36F));
		
		String newGame = "New Game";
		int ngX = getXForCenteredText(newGame);
		int ngY = tileSize * 9;
		g2.drawString(newGame, ngX, ngY);
		
		String loadGame = "Load Game";
		int lgX = getXForCenteredText(loadGame);
		int lgY = tileSize * 10;
		g2.drawString(loadGame, lgX, lgY);
		
		String quitGame = "Quit";
		int qgX = getXForCenteredText(quitGame);
		int qgY = tileSize * 11;
		g2.drawString(quitGame, qgX, qgY);
		
		// arrow on menu
		if (commandNum == 0) {
			g2.drawString(">", ngX - tileSize, ngY);
		} else if (commandNum == 1) {
			g2.drawString(">", lgX - tileSize, lgY);
		} else if (commandNum == 2) {
			g2.drawString(">", qgX - tileSize, qgY);
		}
		
	}
	
	private void drawPauseScreen() {
		
		String text = "PAUSED";
		int textX = getXForCenteredText(text);
		int textY = gp.getScreenHeight() / 2;
		
		g2.drawString(text, textX, textY);	
	}
	
	private void drawDialogScreen() {
		
		int windowX = tileSize * 2;
		int windowY = tileSize / 2;
		int windowWidth = gp.getScreenWidth() - (tileSize * 4);
		int windowHeight = tileSize * 4;	
	
		drawSubWindow(windowX, windowY, windowWidth, windowHeight);
		
		// text
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN,28F));
		int textX = windowX + tileSize;
		int textY = windowY + tileSize;
		
		for (String line : currentDialog.split("\n")) {
			g2.drawString(line, textX, textY);
			textY += 40;
		}
			
	}
	
	private void drawCharacterScreen() {
		final int frameX = tileSize / 2;
		final int frameY = tileSize;
		final int frameWidth = tileSize * 7;
		final int frameHeight = tileSize * 10;
		
		drawSubWindow(frameX, frameY, frameWidth, frameHeight);
		
		// text
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(32F));
		int textX = frameX + 20;
		int textY = frameY + tileSize;
		final int lineHeight = 35;
		int tailX = frameX + frameWidth - 30;
		int valueY = textY;
	
		// attribute names
		g2.drawString("Level", textX, textY);
		textY += lineHeight;
		
		g2.drawString("Health", textX, textY);
		textY += lineHeight;

		g2.drawString("Mana", textX, textY);
		textY += lineHeight;

		g2.drawString("Strength", textX, textY);
		textY += lineHeight;

		g2.drawString("Dexterity", textX, textY);
		textY += lineHeight;

		g2.drawString("Attack", textX, textY);
		textY += lineHeight;

		g2.drawString("Defense", textX, textY);
		textY += lineHeight;

		g2.drawString("XP", textX, textY);
		textY += lineHeight;

		g2.drawString("Next Level @", textX, textY);
		textY += lineHeight;

		g2.drawString("Coin", textX, textY);
		textY += lineHeight + 10;

		g2.drawString("Weapon", textX, textY);
		textY += lineHeight + 10;

		g2.drawString("Shield", textX, textY);

		// values
		String value = String.valueOf(gp.getPlayer().getLevel());
		int valueX = getXForAlignToRightText(value, tailX);
		g2.drawString(value, valueX, valueY);
		
		valueY += lineHeight;
		value = String.valueOf(gp.getPlayer().getCurrentHealth())
				+ "/" + String.valueOf(gp.getPlayer().getMaxHealth());
		valueX = getXForAlignToRightText(value, tailX);
		g2.drawString(value, valueX, valueY);

		valueY += lineHeight;
		value = String.valueOf(gp.getPlayer().getCurrentMana())
				+ "/" + String.valueOf(gp.getPlayer().getMaxMana());
		valueX = getXForAlignToRightText(value, tailX);
		g2.drawString(value, valueX, valueY);

		valueY += lineHeight;
		value = String.valueOf(gp.getPlayer().getStrength());
		valueX = getXForAlignToRightText(value, tailX);
		g2.drawString(value, valueX, valueY);

		valueY += lineHeight;
		value = String.valueOf(gp.getPlayer().getDexterity());
		valueX = getXForAlignToRightText(value, tailX);
		g2.drawString(value, valueX, valueY);

		valueY += lineHeight;
		value = String.valueOf(gp.getPlayer().getAttack());
		valueX = getXForAlignToRightText(value, tailX);
		g2.drawString(value, valueX, valueY);

		valueY += lineHeight;
		value = String.valueOf(gp.getPlayer().getDefense());
		valueX = getXForAlignToRightText(value, tailX);
		g2.drawString(value, valueX, valueY);

		valueY += lineHeight;
		value = String.valueOf(gp.getPlayer().getExperiencePoints());
		valueX = getXForAlignToRightText(value, tailX);
		g2.drawString(value, valueX, valueY);

		valueY += lineHeight;
		value = String.valueOf(gp.getPlayer().getNextLevelExp());
		valueX = getXForAlignToRightText(value, tailX);
		g2.drawString(value, valueX, valueY);

		valueY += lineHeight;
		value = String.valueOf(gp.getPlayer().getCoin());
		valueX = getXForAlignToRightText(value, tailX);
		g2.drawString(value, valueX, valueY);

		// draw images for current sword and shield
		valueY += lineHeight;
		g2.drawImage(gp.getPlayer().getCurrentWeapon().getDn1(), tailX - tileSize, valueY - 24, null);
		
		//valueY += lineHeight;
		//g2.drawImage(gp.getPlayer().getCurrentShield().getDn1(), tailX - tileSize, valueY - 14, null);

	}
	
	private void drawInventoryScreen() {
		
		final int frameX = tileSize * 9;
		final int frameY = tileSize;
		final int frameWidth = tileSize * 6;
		final int frameHeight = tileSize * 5;
		
		drawSubWindow(frameX, frameY, frameWidth, frameHeight);
		
		// inventory slots
		final int slotXStart = frameX + (tileSize / 2);
		final int slotYStart = frameY + (tileSize / 2);
		int slotX = slotXStart;
		int slotY = slotYStart;
		int slotSize = tileSize + 3;
		
		// draw player's current items
		for (Entity item : gp.getPlayer().getInventory()) {
			
			// "equip" cursor
			if (item == gp.getPlayer().getCurrentWeapon() ||
					item == gp.getPlayer().getCurrentShield()) {
				g2.setColor(new Color(240,190,90));
				g2.fillRoundRect(slotX, slotY, tileSize, tileSize, 10, 10);
			}
			
			
			if (item != null) {
				// make sure item is not null
				g2.drawImage(item.getDn1(), slotX, slotY, null);
				slotX += slotSize;
				
				if (slotX > (slotXStart + (4 * slotSize))) {
					// if we've already drawn slot col #4,
					// then reset slotX and increment slotY.
					slotX = slotXStart;
					slotY += slotSize;
				}
			}
		}
		
		// cursor
		int cursorX = slotXStart + (slotSize * slotCol);
		int cursorY = slotYStart + (slotSize * slotRow);
		int cursorWidth = slotSize;
		int cursorHeight = slotSize;
		
		g2.setColor(Color.white);
		g2.setStroke(new BasicStroke(3));
		g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10); // "roundness" setting is 10, 10
		
		// item description frame
		int dFrameX = frameX;
		int dFrameY = frameY + frameHeight;
		int dFrameWidth = frameWidth;
		int dFrameHeight = tileSize * 3;
		
		// draw description text
		int textX = dFrameX + 20;
		int textY = dFrameY + tileSize;
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(28F));
		final int lineHeight = 32;
		int itemIndex = getItemIndexFromSlot();
		
		if (itemIndex < gp.getPlayer().getInventory().size()) {

			drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);

			for (String line : gp.getPlayer().getInventory().get(itemIndex).getItemDescription().split("\n")) {
				g2.drawString(line, textX, textY);
				textY += lineHeight;
			}
		}
	}
	
	public int getItemIndexFromSlot() {
		return (slotRow * 5) + slotCol;
	}
	
	private void drawSubWindow(int swX, int swY, int swWidth, int swHeight) {

		Color c = new Color(0, 0, 0, 200);
		g2.setColor(c);
		g2.fillRoundRect(swX, swY, swWidth, swHeight, 35, 35);
		
		c = new Color(255, 255, 255);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(5)); // pixel width
		g2.drawRoundRect(swX + 5, swY + 5, swWidth - 10, swHeight - 10, 25, 25);
	}

	private int getXForCenteredText(String text) {
		int textSize = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		return (gp.getScreenWidth() / 2) - (textSize / 2);
	}

	private int getXForAlignToRightText(String text, int tailX) {
		int textSize = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int newX = tailX - textSize;
		return newX;
	}
	
// Treasure Hunt version of draw
//
//	public void draw(Graphics2D g2) {
//		
//		if (!gameFinished) {
//			g2.setFont(arialP40);
//			g2.setColor(Color.white);
//			g2.drawImage(keyImage, tileSize / 2, tileSize / 2, tileSize, tileSize, null);
//			g2.drawString("x " + gp.getPlayer().getKeys(), 74, 65);
//			
//			// Time
//			playTime += (double) 1/60;
//			g2.drawString("Time: " + dFormat.format(playTime), tileSize * 11, 65);
//			
//			if (messageOn) {
//				g2.setFont(g2.getFont().deriveFont(30F));
//				g2.drawString(message, tileSize / 2, tileSize * 5);
//				
//				messageCounter++;
//				
//				// make text disappear after 2 seconds
//				if (messageCounter > 120) {
//					messageCounter = 0;
//					messageOn = false;
//				}
//			}
//		} else {
//			g2.setFont(arialP40);
//			g2.setColor(Color.white);
//
//			String text = "You found the treasure!";
//			int textSize = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
//			// show finish message in center of screen
//			int textX = (gp.getScreenWidth() / 2) - (textSize / 2);
//			int textY = (gp.getScreenHeight() / 2) - (tileSize * 3);
//
//			// show time
//			text = "Time: " + dFormat.format(playTime);
//			textSize = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
//			textX = (gp.getScreenWidth() / 2) - (textSize / 2);
//			textY = (gp.getScreenHeight() / 2) + (tileSize * 4);
//
//			g2.setFont(g2.getFont().deriveFont(40));
//			g2.drawString(text, textX, textY);
//			
//			// Congratulations
//			g2.setFont(arialB80);
//			g2.setColor(Color.cyan);
//		
//			text = "Congratulations!";
//			textSize = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
//			
//			textX = (gp.getScreenWidth() / 2) - (textSize / 2);
//			textY = (gp.getScreenHeight() / 2) + (tileSize * 2);
//			g2.drawString(text, textX, textY);
//			
//			gp.stopGame();
//		}
//	}

	public boolean getIsGameFinished() {
		return this.gameFinished;
	}
	
	public void setIsGameFinished(boolean isFinished) {
		this.gameFinished = isFinished;
	}
	
	public String getCurrentDialog() {
		return this.currentDialog;
	}
	
	public void setCurrentDialog(String message) {
		this.currentDialog = message;
	}
	
	public int getCommandNum() {
		return this.commandNum;
	}
	
	public void decrementCommandNum() {
		if (commandNum > 0) {
			commandNum--;
		}
	}
	
	public void incrementCommandNum() {
		if (commandNum < 2) {
			commandNum++;
		}
	}
	
	public int getSlotCol () {
		return this.slotCol;
	}
	
	public void setSlotCol (int slotColumn) {
		if (slotColumn >= 0 && slotColumn <= 4) {
			this.slotCol = slotColumn;
		}
	}

	public int getSlotRow () {
		return this.slotRow;
	}
	
	public void setSlotRow (int slotRow) {
		if (slotRow >= 0 && slotRow <= 3) {
			this.slotRow = slotRow;
		}
	}

}
