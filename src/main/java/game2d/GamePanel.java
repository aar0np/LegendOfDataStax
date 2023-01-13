package game2d;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JPanel;

import entity.Entity;
import entity.Player;
import entity.Projectile;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {

	private static final long serialVersionUID = 8734664941941553022L;

	final int originalTileSize = 16;
	final int scale = 3;

	// screen settings
	final int tileSize = originalTileSize * scale;  // 48x48 by default
	final int maxScreenCol = 16;
	final int maxScreenRow = 12;
	final int screenWidth = tileSize * maxScreenCol; // 768x
	final int screenHeight = tileSize * maxScreenRow; // 576
	
	// world map settings
	final int maxWorldCol = 50;
	final int maxWorldRow = 50;
	
	// frames per second
	final int fPS = 60;
	
	// Game engine
	private TileManager tileMgr = new TileManager(this);
	private Thread gameThread;
	KeyHandler keyHandler = new KeyHandler(this);
	Sound sound = new Sound(false);
	Sound music = new Sound(true);
	CollisionChecker cChecker = new CollisionChecker(this);
	ObjectFactory oFactory = new ObjectFactory(this);
	GameUserInterface gameUI = new GameUserInterface(this);
	EventHandler eventHandler = new EventHandler(this);
	
	// Entities and objects
	Player player = new Player(this,keyHandler);
	Entity objects[] = new Entity[40];
	Entity npcs[] = new Entity[10];
	Entity monsters[] = new Entity[20];
	List<Projectile> projectileList = new ArrayList<>();
	List<Entity> entityList = new ArrayList<>();
	
	private int gameState;
	// game state constants
	public final int TITLE_STATE = 0;
	public final int PLAY_STATE = 1;
	public final int PAUSE_STATE = 2;
	public final int DIALOG_STATE = 3;
	public final int CHARACTER_SHEET_STATE = 4;
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyHandler);
		this.setFocusable(true);
	}

	public void setupGame() {
		oFactory.generateObjects();
		oFactory.generateNPCs();
		oFactory.generateMonsters();
		//playMusic(0);
		//stopMusic();
		gameState = TITLE_STATE;
	}
	
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	public void stopGame() {
		gameThread = null;
	}
	
	public void run() {
		
		double drawInterval = 1000000000/fPS;
		double nextDrawTime = System.nanoTime() + drawInterval;
		
		while (gameThread != null) {
			// testing
			// System.out.println("Game loop is running");
			
			update();
			
			repaint();
			
			try {
				double remainingTime = nextDrawTime - System.nanoTime();
				remainingTime = remainingTime / 1000000;
				
				if (remainingTime < 0) {
					remainingTime = 0;
				}
				
				Thread.sleep((long)remainingTime);
				nextDrawTime += drawInterval;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void update() {
		
		if (gameState == PLAY_STATE) {
			player.update();
			
			for (Entity npc : npcs) {
				if (npc != null) {
					npc.update();
				}
			}
			
			//for (Entity monster : monsters) {
			// difference of reference vs value
			for (int index = 0; index < monsters.length; index++) {
				if (monsters[index] != null) {
					if (monsters[index].isAlive() && !monsters[index].isDying()) {
						monsters[index].update();
					} else if (!monsters[index].isAlive()) {
						monsters[index].checkDrop();
						monsters[index] = null;
					}
				}
			}
			
			for (int index = 0; index < projectileList.size(); index++) {
				if (projectileList.get(index) != null) {
					if (projectileList.get(index).isAlive()) {
						projectileList.get(index).update();
					} else if (!projectileList.get(index).isAlive()) {
						projectileList.remove(index);
					}
				}
			}
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		//long drawStart = System.currentTimeMillis();
		
	    if (gameState == TITLE_STATE) {
	    	// title screen 
	    	gameUI.draw(g2);
	    } else {
			
			// tiles
			tileMgr.draw(g2);

			// entities (players, npcs, objects)
			entityList.add(player);
			
			for (Entity npc : npcs) {
				if (npc != null) {
					entityList.add(npc);
				}
			}
			
			for (Entity object : objects) {
				if (object != null) {
					entityList.add(object);
				}
			}
			
			for (Entity monster : monsters) {
				if (monster != null) {
					entityList.add(monster);
				}
			}
			
			for (Projectile projectile : projectileList) {
				if (projectile != null) {
					entityList.add(projectile);
				}
			}
			
			//Sort by WorldY
			Collections.sort(entityList, new Comparator<Entity>() {
				@Override
				public int compare(Entity e0, Entity e1) {
					
					int result = Integer.compare(e0.getWorldY(), e1.getWorldY());
					
					return result;
				}
			});

			//Draw entity list
			for (Entity entity : entityList) {
				entity.draw(g2);
			}
			
			//remove all entities
			entityList.clear();
			
			// UI
			gameUI.draw(g2);
		}
		
		//long drawEnd = System.currentTimeMillis();
		//long drawTime = drawEnd - drawStart;
		
		//System.out.println("draw time = " + drawTime);
		// getting 200-300ms initially	

		g2.dispose();
	}
	
	public void playMusic(int index) {
		music.setFile(index);
		//music.play();
		music.loop();
	}
	
	public void stopMusic() {
		music.stop();
	}
	
	public void playSoundEffect(int index) {
		sound.setFile(index);
		sound.play();
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public CollisionChecker getCollisionChecker() {
		return this.cChecker;
	}
	
	public TileManager getTileManager() {
		return this.tileMgr;
	}

	public KeyHandler getKeyHandler() {
		return this.keyHandler;
	}
	
	public Entity[] getObjects() {
		return this.objects;
	}
	
	public Entity[] getNPCs() {
		return this.npcs;
	}
	
	public Entity[] getMonsters() {
		return this.monsters;
	}
	
	public List<Projectile> getProjectiles() {
		return this.projectileList;
	}
	
	public GameUserInterface getGameUI() {
		return this.gameUI;
	}
	
	public EventHandler getEventHandler() {
		return this.eventHandler;
	}
	
	public ObjectFactory getObjectFactory() {
		return this.oFactory;
	}
	
	public int getTileSize() {
		return tileSize;
	}
	
	public int getMaxScreenColumn() {
		return maxScreenCol;
	}
	
	public int getMaxScreenRow() {
		return maxScreenRow;
	}
	
	public int getScreenWidth() {
		return screenWidth;
	}
	
	public int getScreenHeight() {
		return screenHeight;
	}
	
	public int getMaxWorldColumn() {
		return maxWorldCol;
	}
	
	public int getMaxWorldRow() {
		return maxWorldRow;
	}
	
	public int getGameState() {
		return this.gameState;
	}
	
	public void setGameState(int newState) {
		this.gameState = newState;
	}
		
}
