package tile_interactive;

import entity.Entity;
import main.GamePanel;

public class interactiveTile extends Entity {

	GamePanel gp;
	public boolean destructible = false;
	
	public interactiveTile(GamePanel gp, int col, int row) {
		super(gp);
		this.gp = gp;
	}
	public void update() {
		
	}
}
