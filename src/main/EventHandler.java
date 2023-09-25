package main;

import java.awt.Rectangle;

public class EventHandler {
	
	GamePanel gp;
	EventRect eventRect[][];
	
	int previousEventX, previousEventY;
	boolean canTouchEvent = true;
	//Rectangle eventRect;
	//int eventRectDefaultX, eventRectDefaultY;
	
	public EventHandler(GamePanel gp) {
		
		eventRect = new EventRect[gp.maxWorldCol][gp.maxWorldRow];
		
		int col = 0;
		int row = 0;
		while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
			
			this.gp = gp;
			eventRect[col][row] = new EventRect();
			eventRect[col][row].x = 23;
			eventRect[col][row].y = 23;
			eventRect[col][row].width = 2;
			eventRect[col][row].height = 2;
			eventRect[col][row].eventRectDefaultX = eventRect[col][row].x;
			eventRect[col][row].eventRectDefaultY = eventRect[col][row].y;
			
			col++;
			if (col == gp.maxWorldCol) {
				col = 0;
				row++;
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
			if (hit(27, 16, "right") == true) {damagePit(27, 16, gp.dialougeState);}
			// Just a test, walking into a "pit" to receive damage! if (hit(23, 19, "any") == true) {damagePit(27, 16, gp.dialougeState);}
			if (hit(23, 12, "up") == true) {healingPool(23, 12, gp.dialougeState);}
		}
	}
	
	public boolean hit(int col, int row, String reqDirection) {
		
		boolean hit = false;
		
		gp.player.solidArea.x = (int) (gp.player.worldX + gp.player.solidArea.x);
		gp.player.solidArea.y = (int) (gp.player.worldY + gp.player.solidArea.y);
		eventRect[col][row].x = col*gp.tileSize + eventRect[col][row].x;
		eventRect[col][row].y = row*gp.tileSize + eventRect[col][row].y;
		
		if (gp.player.solidArea.intersects(eventRect[col][row]) && eventRect[col][row].eventDone == false) {
			if (gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
				hit = true;
				
				previousEventX = (int) gp.player.worldX;
				previousEventY = (int) gp.player.worldY;
			}
		}
		
		gp.player.solidArea.x = gp.player.solidAreaDefaultX;
		gp.player.solidArea.y = gp.player.solidAreaDefaultY;
		eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
		eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;
		
		return hit;
	}
	
	public void damagePit(int col, int row, int gameState) {
		gp.gameState = gameState;
		gp.ui.currentDIalouge = "Дигни ме, пао сам!";
		gp.player.life -= 1;
		//eventRect[col][row] .eventDone = true;
		canTouchEvent = false;
	}
	
	public void healingPool(int col, int row, int gameState) {
		
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
	
}
