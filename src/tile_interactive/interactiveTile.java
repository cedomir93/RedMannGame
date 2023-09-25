package tile_interactive;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import entity.Entity;
import main.GamePanel;

public class interactiveTile extends Entity {

	GamePanel gp;
	public boolean destructible = false;
	
	public interactiveTile(GamePanel gp, double d, double e) {
		super(gp);
		this.gp = gp;
	}
	public boolean isCorrectItem(Entity entity) {
		boolean isCorrectItem = false;
		return isCorrectItem;
	}
	public void playSE() {}
	public interactiveTile getDestroyedForm() {
		interactiveTile tile = null;
		return tile;
	}
	public void update() {
		if (invincible == true) {
			invincibleCounter++;
			if (invincibleCounter > 20) {
				invincible = false;
				invincibleCounter = 0;
			}
		}
	}
	public void draw(Graphics2D g2) {
		
		
		double screenX = worldX - gp.player.worldX + gp.player.screenX;
		double screenY = worldY - gp.player.worldY + gp.player.screenY;
		
		if (worldX + gp.tileSize> gp.player.worldX - gp.player.screenX &&
			worldX - gp.tileSize< gp.player.worldX + gp.player.screenX &&
			worldY + gp.tileSize> gp.player.worldY - gp.player.screenY &&
			worldY - gp.tileSize< gp.player.worldY + gp.player.screenY) {
			
			g2.drawImage(down1, (int)screenX, (int)screenY, null);
		}
		
	}
}
