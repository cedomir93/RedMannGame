package tile_interactive;

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
}
