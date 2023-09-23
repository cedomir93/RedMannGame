package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Potion_Red extends Entity{
	GamePanel gp;
	int value = 5;
	public OBJ_Potion_Red(GamePanel gp) {
		super(gp);
		this.gp = gp;
		type = type_consumable;
		name = "Црвени напитак";
		down1 = setup("/objects/potion_red", gp.tileSize, gp.tileSize);
		description = "Лечи ваш живот за " + value + "!";
	}
	public void use(Entity entity) {
		gp.gameState = gp.dialougeState;
		gp.ui.currentDIalouge = "Попио си " + name + "!\n"
				+ "Опорављен си за" + value + "!";
		entity.life += value;
		if (gp.player.life > gp.player.maxLife) {
			gp.player.life = gp.player.maxLife;
		}
		gp.playSE(2);
	}
}
