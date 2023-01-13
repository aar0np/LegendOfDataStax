package game2d;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

	private boolean upPressed = false;
	private boolean downPressed = false;
	private boolean leftPressed = false;
	private boolean rightPressed = false;
	private boolean enterPressed = false;
	private boolean shotKeyPressed = false;
	private GamePanel gp;
	
	public KeyHandler(GamePanel gp) {
		this.gp = gp;
	}
	
	public void keyTyped(KeyEvent e) {
		
	}

	public void keyPressed(KeyEvent e) {

		int code = e.getKeyCode();
		
		if (gp.getGameState() ==  gp.PLAY_STATE) {
			// PLAY STATE
			playState(code);
		} else if (gp.getGameState() == gp.PAUSE_STATE) {
			// PAUSE STATE
			pauseState(code);
		} else if (gp.getGameState() == gp.DIALOG_STATE) {
			// DIALOG STATE
			dialogState(code);
		} else if (gp.getGameState() == gp.CHARACTER_SHEET_STATE) {
			// CHARACTER SHEET STATE
			characterSheetState(code);
		} else {
		// } else if (gp.getGameState() == gp.TITLE_STATE) {
			titleState(code);
		}
	}

	private void playState(int code) {
		
		if (code == KeyEvent.VK_W) {
			upPressed = true;
		}
		if (code == KeyEvent.VK_A) {
			leftPressed = true;
		}
		if (code == KeyEvent.VK_S) {
			downPressed = true;
		}
		if (code == KeyEvent.VK_D) {
			rightPressed = true;
		}
		if (code == KeyEvent.VK_ENTER) {
			enterPressed = true;
		}
		if (code == KeyEvent.VK_F) {
			shotKeyPressed = true;
		}
		if (code == KeyEvent.VK_ESCAPE) {
			gp.setGameState(gp.PAUSE_STATE);
		}
		if (code == KeyEvent.VK_C) {
			gp.setGameState(gp.CHARACTER_SHEET_STATE);
		}
	}
	
	private void pauseState(int code) {
		if (code == KeyEvent.VK_ESCAPE) {
			gp.setGameState(gp.PLAY_STATE);
		}
	}

	private void dialogState(int code) {

		if (code == KeyEvent.VK_ENTER) {
			gp.setGameState(gp.PLAY_STATE);
		}
	}
	
	private void characterSheetState(int code) {
		// need to pre-increment our variables with the setters.
		// ex: ++row instead of row++
		int row = gp.getGameUI().getSlotRow();
		int col = gp.getGameUI().getSlotCol();
		
		if (code == KeyEvent.VK_C) {
			gp.setGameState(gp.PLAY_STATE);
		}
		if (code == KeyEvent.VK_W) {
			gp.getGameUI().setSlotRow(--row);
			//gp.playSoundEffect(15);
		}
		if (code == KeyEvent.VK_A) {
			gp.getGameUI().setSlotCol(--col);
			//gp.playSoundEffect(15);
		}
		if (code == KeyEvent.VK_S) {
			gp.getGameUI().setSlotRow(++row);
			//gp.playSoundEffect(15);
		}
		if (code == KeyEvent.VK_D) {
			gp.getGameUI().setSlotCol(++col);
			//gp.playSoundEffect(15);
		}
		if (code == KeyEvent.VK_ENTER) {
			gp.getPlayer().equipItem();
		}
		
	}
	
	private void titleState(int code) {
		
		if (code == KeyEvent.VK_W) {
			gp.getGameUI().decrementCommandNum();
		} else if (code == KeyEvent.VK_S) {
			gp.getGameUI().incrementCommandNum();
		}
		
		if (code == KeyEvent.VK_ENTER) {
			if (gp.getGameUI().getCommandNum() == 0) {
				// new game
				gp.setGameState(gp.PLAY_STATE);
				//gp.playMusic(0);
			} else if (gp.getGameUI().getCommandNum() == 1) {
				// load game
				
			} else {
				// quit
				System.exit(0);
			}
		}		
	}
	
	public void keyReleased(KeyEvent e) {
		
		int code = e.getKeyCode();
		
		if (code == KeyEvent.VK_W) {
			upPressed = false;
		}
		if (code == KeyEvent.VK_A) {
			leftPressed = false;
		}
		if (code == KeyEvent.VK_S) {
			downPressed = false;
		}
		if (code == KeyEvent.VK_D) {
			rightPressed = false;
		}
		if (code == KeyEvent.VK_ENTER) {
			enterPressed = false;
		}
		if (code == KeyEvent.VK_F) {
			shotKeyPressed = false;
		}
	}

	public boolean isUpPressed() {
		return upPressed;
	}

	public void setUpPressed(boolean upPressed) {
		this.upPressed = upPressed;
	}

	public boolean isDownPressed() {
		return this.downPressed;
	}

	public void setDownPressed(boolean downPressed) {
		this.downPressed = downPressed;
	}

	public boolean isLeftPressed() {
		return this.leftPressed;
	}

	public void setLeftPressed(boolean leftPressed) {
		this.leftPressed = leftPressed;
	}

	public boolean isRightPressed() {
		return this.rightPressed;
	}

	public void setRightPressed(boolean rightPressed) {
		this.rightPressed = rightPressed;
	}
	
	public boolean isEnterPressed() {
		return this.enterPressed;
	}
	
	public void setEnterPressed(boolean enterPressed) {
		this.enterPressed = enterPressed;
	}

	public boolean isShotKeyPressed() {
		return this.shotKeyPressed;
	}
	
	public void setShotKeyPressed(boolean shotKeyPressed) {
		this.shotKeyPressed = shotKeyPressed;
	}
}
