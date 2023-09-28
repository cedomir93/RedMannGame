package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;

import entity.Entity;
import entity.Player;
import monster.MON_GreenSlime;
import tile.TileManager;
import tile_interactive.interactiveTile;

public class GamePanel extends JPanel implements Runnable {

	final int originalTileSize = 16;
	final int scale = 3;
	
	public int tileSize = originalTileSize * scale;
	public int maxScreenCol = 20;
	public int maxScreenRow = 12;
	public int screenWidth = tileSize * maxScreenCol;
	public int screenHeight = tileSize * maxScreenRow;
	//FULL SCREEN
	int screenWidth2 = screenWidth;
	int screenHeight2 = screenHeight;
	BufferedImage tempScreen;
	Graphics2D g2;
	public boolean fullScreenOn = false;
	
	//WORLD SETTINGS
	public final int maxWorldCol = 50;
	public final int maxWorldRow = 50;
	public final int worldWidth = tileSize * maxWorldCol;
	public final int worldHeight = tileSize * maxWorldRow;
	public final int maxMap = 10; //10 different maps
	public int currentMap = 0; // 0 = island, 1 = hut .......
	
	
	int FPS = 60;
	TileManager tileM = new TileManager(this);
	public KeyHandler keyH = new KeyHandler(this);
	Sound music = new Sound();
	Sound se = new Sound();
	Config config = new Config(this);
	public Thread gameThread;
	public CollisionChecker cChecker = new CollisionChecker(this);
	public AssetSetter aSetter = new AssetSetter(this);
	public UI ui = new UI(this);
	public EventHandler eHandler = new EventHandler(this);
	public Player player = new Player(this, keyH);
	public Entity obj[][] = new Entity[maxMap][30]; // adding 2 dimensional
	public Entity npc[][] = new Entity[maxMap][10];
	public Entity monster[][] = new Entity[maxMap][20];
	public interactiveTile iTile[][] = new interactiveTile[maxMap][50];
	public ArrayList<Entity> projectileList = new ArrayList<>();
	public ArrayList<Entity> particleList = new ArrayList<>();
	ArrayList<Entity> entityList = new ArrayList<>();
	
	//GAME STATE
	public int gameState;
	public final int titleState = 0; 
	public final int playState = 1;
	public final int pauseState = 2;
	public final int dialougeState = 3;
	public final int characterState = 4;
	public final int optionState = 5;
	public final int gameOverState = 6;
	
	
	public GamePanel() 
	{
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
	}
	public void setupGame() {
		aSetter.setObject();
		aSetter.setNPC();
		aSetter.setMonster();
		aSetter.setInteractiveTile();
		//playMusic(0);
		gameState = titleState;
		tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
		g2 = (Graphics2D)tempScreen.getGraphics();
		
		if (fullScreenOn == true) {
			setFullScreen();
		}
			
	}
	public void retry() {
		player.setDefaultPositions();
		player.restoreLifeAndMana();
		aSetter.setNPC();
		aSetter.setMonster();
	}
	public void restart() {
		
		player.setDefaultValues();
		player.setDefaultPositions();
		player.restoreLifeAndMana();
		player.setItems();
		aSetter.setObject();
		aSetter.setNPC();
		aSetter.setMonster();
		aSetter.setInteractiveTile();
	}
	public void zoomInOut(int i) {
		
		int oldWorldWidth = tileSize * maxWorldCol;
		tileSize += i;
		int newWorldWidth = tileSize * maxWorldCol;
		
		player.speed = (double)newWorldWidth/600;
		
		double multiplier = (double)newWorldWidth/oldWorldWidth;
		
		double newPlayerWorldX = player.worldX * multiplier;
		double newPlayerWorldY = player.worldY * multiplier;
		
		player.worldX = newPlayerWorldX;
		player.worldY = newPlayerWorldY;
	}
	public void setFullScreen() {
		//GET LOCAL SCREEN DEVICE
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		gd.setFullScreenWindow(Main.window);
		//GET FULL SCREEN WIDTH AND HEIGHT
		screenWidth2 = Main.window.getWidth();
		screenHeight2 = Main.window.getHeight();
	}
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	@Override
	public void run() {
		
		double drawInterval = 1000000000/FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		
		while (gameThread != null) {
			
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			lastTime = currentTime;
			
			if (delta >= 1) {
				update();
				//repaint();
				drawToTempScreen(); //draw everything to the buffered image
				drawToScreen(); //draw the buffered image
				delta--;
			}
		}
	}
	public void update() {
		
		if (gameState == playState) {
			//PLAYER
			player.update();
			//NPC
			for (int i = 0; i < npc[1].length; i++) {
				if (npc[currentMap][i] != null) {
					npc[currentMap][i].update();
				}
			}
			//MONSTERS
			for (int i = 0; i < monster[1].length; i++) {
				if (monster[currentMap][i] != null) {
					if(monster[currentMap][i].alive == true && monster[currentMap][i].dying == false) {
						monster[currentMap][i].update();
					}
					//monster[i].update();
					if(monster[currentMap][i].alive == false) {
						monster[currentMap][i].checkDrop();
						monster[currentMap][i] = null;
					}
				}
			}
			//PROJECTILE
			for (int i = 0; i < projectileList.size(); i++) {
				if (projectileList.get(i) != null) {
					if(projectileList.get(i).alive == true) {
						projectileList.get(i).update();
					}
					if(projectileList.get(i).alive == false) {
						projectileList.remove(i);
					}
				}
			}
			for (int i = 0; i < particleList.size(); i++) {
				if (particleList.get(i) != null) {
					if(particleList.get(i).alive == true) {
						particleList.get(i).update();
					}
					if(particleList.get(i).alive == false) {
						particleList.remove(i);
					}
				}
			}
			for (int i = 0; i < iTile[1].length; i++) {
				if (iTile[currentMap][i] != null) {
					iTile[currentMap][i].update();
				}
			}
		}
		if (gameState == pauseState) {
			//nothing is happening!
		}
		
	}
	public void drawToTempScreen() {
		//DEBUG
		
		long drawStart = 0;
		if (keyH.chechDrawTime == true) {
			drawStart = System.nanoTime();
		}
		//TITLE SCREEN
		if (gameState == titleState) {
			ui.draw(g2);
		}
		//OTHERS
		else {
			//TILE
			tileM.draw(g2);
			
			//INTERACTIVE TILE
			for (int i = 0; i < iTile[1].length; i++) {
				if (iTile[currentMap][i] != null) {
					iTile[currentMap][i].draw(g2);
				}
			}
			//ADD ENTITIES TO THE LIST
			//add player
			entityList.add(player);
			//add npc
			for (int i = 0; i < npc[1].length; i++) {
				if (npc[currentMap][i] != null) {
					entityList.add(npc[currentMap][i]);
				}
			}
			// add object
			for (int i = 0; i < obj[1].length; i++) {
				if (obj[currentMap][i] != null) {
					entityList.add(obj[currentMap][i]);
				}
			}
			//add monster
			for (int i = 0; i < monster[1].length; i++) {
				if (monster[currentMap][i] != null) {
					entityList.add(monster[currentMap][i]);
				}
			}
			//add projectile
			for (int i = 0; i < projectileList.size(); i++) {
				if (projectileList.get(i) != null) {
					entityList.add(projectileList.get(i));
				}
			}
			for (int i = 0; i < particleList.size(); i++) {
				if (particleList.get(i) != null) {
					entityList.add(particleList.get(i));
				}
			}
			//SORT
			Collections.sort(entityList, new Comparator<Entity>() {
						@Override
						public int compare(Entity e1, Entity e2) {
							int result = Double.compare(e1.worldY, e2.worldY);
							return result;
						}
			});
			//DRAW ENTITIES
			for (int i = 0; i < entityList.size(); i++) {
				entityList.get(i).draw(g2);
			}
			//EMPTY ENTITY LIST
			//	for (int i = 0; i < entityList.size(); i++) {
			//		entityList.remove(i);
			//	}
			entityList.clear();
			//PLAYER
			//player.draw(g2);
			//UI
			ui.draw(g2);
			}
			//DEBUG
			if (keyH.chechDrawTime == true) {
				long drawEnd = System.nanoTime();
				long passed = drawEnd - drawStart;
				g2.setColor(Color.white);
				g2.drawString("Draw Time: "+ passed, 10, 400);
				System.out.println("Draw time: " + passed);
				}
	}
	public void drawToScreen() {
		Graphics g = getGraphics();
		g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
		g.dispose();
	}
/*	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		//DEBUG
		
		long drawStart = 0;
		if (keyH.chechDrawTime == true) {
			drawStart = System.nanoTime();
		}
		//TITLE SCREEN
		
		if (gameState == titleState) {
			ui.draw(g2);
		}
		//OTHERS
		else {
			
			//TILE
			tileM.draw(g2);
			
			for (int i = 0; i < iTile.length; i++) {
				if (iTile[i] != null) {
					iTile[i].draw(g2);
				}
			}
			
			//ADD ENTITIES TO THE LIST
			
			//add player
			entityList.add(player);
			//add npc
			for (int i = 0; i < npc.length; i++) {
				if (npc[i] != null) {
					entityList.add(npc[i]);
				}
			}
			// add object
			
			for (int i = 0; i < obj.length; i++) {
				if (obj[i] != null) {
					entityList.add(obj[i]);
				}
			}
			//add monster
			for (int i = 0; i < monster.length; i++) {
				if (monster[i] != null) {
					entityList.add(monster[i]);
				}
			}
			//add projectile
			for (int i = 0; i < projectileList.size(); i++) {
				if (projectileList.get(i) != null) {
					entityList.add(projectileList.get(i));
				}
			}
			for (int i = 0; i < particleList.size(); i++) {
				if (particleList.get(i) != null) {
					entityList.add(particleList.get(i));
				}
			}
			
			//SORT
			Collections.sort(entityList, new Comparator<Entity>() {

				@Override
				public int compare(Entity e1, Entity e2) {
					
					int result = Double.compare(e1.worldY, e2.worldY);
					return result;
				}
			});
			
			//DRAW ENTITIES
			for (int i = 0; i < entityList.size(); i++) {
				entityList.get(i).draw(g2);
			}
			//EMPTY ENTITY LIST
		//	for (int i = 0; i < entityList.size(); i++) {
		//		entityList.remove(i);
		//	}
			entityList.clear();
			
			//PLAYER
			//player.draw(g2);
			//UI
			ui.draw(g2);
			
		}
		
		
		
		//DEBUG
		
		if (keyH.chechDrawTime == true) {
			long drawEnd = System.nanoTime();
		long passed = drawEnd - drawStart;
		g2.setColor(Color.white);
		g2.drawString("Draw Time: "+ passed, 10, 400);
		System.out.println("Draw time: " + passed);
		}
		g2.dispose();
	} */
	public void playMusic(int i) {
		music.setFilie(i);
		music.play();
		music.loop();
	}
	public void stopMusic() {
		music.stop();
	}
	public void playSE(int i) {
		se.setFilie(i);
		se.play();
	}
}
