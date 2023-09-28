package main;

import java.awt.Rectangle;

public class EventHandler {
	
	GamePanel gp;
	EventRect eventRect[][][]; //using 3 dimensional
	
	int previousEventX, previousEventY;
	boolean canTouchEvent = true;
	//Rectangle eventRect;
	//int eventRectDefaultX, eventRectDefaultY;
	
	public EventHandler(GamePanel gp) {
		
		eventRect = new EventRect[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];
		
		int map = 0;
		int col = 0;
		int row = 0;
		while (map < gp.maxMap && col < gp.maxWorldCol && row < gp.maxWorldRow) {
			
			this.gp = gp;
			eventRect[map][col][row] = new EventRect();
			eventRect[map][col][row].x = 23;
			eventRect[map][col][row].y = 23;
			eventRect[map][col][row].width = 2;
			eventRect[map][col][row].height = 2;
			eventRect[map][col][row].eventRectDefaultX = eventRect[map][col][row].x;
			eventRect[map][col][row].eventRectDefaultY = eventRect[map][col][row].y;
			
			col++;
			if (col == gp.maxWorldCol) {
				col = 0;
				row++;
				
				if (row == gp.maxWorldRow) { //we can create rectangles for each map
					row = 0;
					map++;
				}
				
			}
		}
	}
	public void checkEvent() {
		
		//Check if the player is more then 1 tile away from a last event
		int xDistance = (int) Math.abs(gp.player.worldX - previousEventX);
		int yDistance = (int) Math.abs(gp.player.worldY - previousEventY);
		int distance = Math.max(xDistance, yDistance);
		if (distance > gp.tileSize) {
			canTouchEvent = true;
		}
		
		if (canTouchEvent == true) {
			if (hit(0, 27, 16, "right") == true) {damagePit(gp.dialougeState);}
			// Just a test, walking into a "pit" to receive damage! if (hit(23, 19, "any") == true) {damagePit(27, 16, gp.dialougeState);}
			else if (hit(0, 23, 12, "up") == true) {healingPool(gp.dialougeState);}
			//Transition event bellow
			else if (hit(0, 10, 39, "any") == true) {
				teleport(1, 12, 13);
			}
			else if (hit(1, 12, 13, "any") == true) {
				teleport(0, 10, 39);
			}
			
			
		}
	}
	
	public boolean hit(int map, int col, int row, String reqDirection) { //pass a map number to each map
		
		boolean hit = false;
		
		if (map == gp.currentMap) {
			gp.player.solidArea.x = (int) (gp.player.worldX + gp.player.solidArea.x);
			gp.player.solidArea.y = (int) (gp.player.worldY + gp.player.solidArea.y);
			eventRect[map][col][row].x = col*gp.tileSize + eventRect[map][col][row].x;
			eventRect[map][col][row].y = row*gp.tileSize + eventRect[map][col][row].y;
			
			if (gp.player.solidArea.intersects(eventRect[map][col][row]) && eventRect[map][col][row].eventDone == false) {
				if (gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
					hit = true;
					
					previousEventX = (int) gp.player.worldX;
					previousEventY = (int) gp.player.worldY;
				}
			}
			
			gp.player.solidArea.x = gp.player.solidAreaDefaultX;
			gp.player.solidArea.y = gp.player.solidAreaDefaultY;
			eventRect[map][col][row].x = eventRect[map][col][row].eventRectDefaultX;
			eventRect[map][col][row].y = eventRect[map][col][row].eventRectDefaultY;
		}
	/*	gp.player.solidArea.x = (int) (gp.player.worldX + gp.player.solidArea.x);
		gp.player.solidArea.y = (int) (gp.player.worldY + gp.player.solidArea.y);
		eventRect[map][col][row].x = col*gp.tileSize + eventRect[map][col][row].x;
		eventRect[map][col][row].y = row*gp.tileSize + eventRect[map][col][row].y;
		
		if (gp.player.solidArea.intersects(eventRect[map][col][row]) && eventRect[map][col][row].eventDone == false) {
			if (gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
				hit = true;
				
				previousEventX = (int) gp.player.worldX;
				previousEventY = (int) gp.player.worldY;
			}
		}
		
		gp.player.solidArea.x = gp.player.solidAreaDefaultX;
		gp.player.solidArea.y = gp.player.solidAreaDefaultY;
		eventRect[map][col][row].x = eventRect[map][col][row].eventRectDefaultX;
		eventRect[map][col][row].y = eventRect[map][col][row].eventRectDefaultY;
		*/
		return hit;
	}
	
	public void damagePit(int gameState) {
		gp.gameState = gameState;
		gp.ui.currentDIalouge = "Дигни ме, пао сам!";
		gp.player.life -= 1;
		//eventRect[col][row] .eventDone = true;
		canTouchEvent = false;
	}
	
	public void healingPool(int gameState) {
		
		if (gp.keyH.enterPressed == true) {
			gp.gameState = gameState;
			gp.player.attackCancel = true;
			gp.playSE(2);
			gp.ui.currentDIalouge = "ДАЈ СРПСКУ РАКИЈУ БРЕ!";
			gp.player.life = gp.player.maxLife;
			//CODE BELOW IS HEALING POOL EFECT FOR CRYSTALS, SAME AREA WHERE HEALING POOL IS
			gp.player.mana = gp.player.maxLife;
			gp.aSetter.setMonster(); //Every time we heal up at the pool, new monsters appears!
		}
	}
	public void teleport(int map, int col, int row) {
		
		gp.currentMap = map;
		gp.player.worldX = gp.tileSize * col;
		gp.player.worldY = gp.tileSize * row;
		previousEventX = (int) gp.player.worldX;
		previousEventY = (int) gp.player.worldY;
		canTouchEvent = false;
		gp.playSE(13);
	}
}
