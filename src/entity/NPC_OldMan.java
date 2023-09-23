package entity;

import java.util.Random;

//import java.awt.image.BufferedImage;
//import java.io.IOException;

//import javax.imageio.ImageIO;

import main.GamePanel;
//import main.UtilityTool;

public class NPC_OldMan extends Entity{

	GamePanel gp;
	
	public NPC_OldMan(GamePanel gp) {
		super(gp);
		this.gp = gp;
		direction = "down";
		speed = 1;
		
		getImage();
		setDialouge();
	}
	
	public void getImage() {
		
		up1 = setup("/npc/oldman_up_1",gp.tileSize, gp.tileSize);
		up2 = setup("/npc/oldman_up_2",gp.tileSize, gp.tileSize);
		down1 = setup("/npc/oldman_down_1",gp.tileSize, gp.tileSize);
		down2 = setup("/npc/oldman_down_2",gp.tileSize, gp.tileSize);
		left1 = setup("/npc/oldman_left_1",gp.tileSize, gp.tileSize);
		left2 = setup("/npc/oldman_left_2",gp.tileSize, gp.tileSize);
		right1 = setup("/npc/oldman_right_1",gp.tileSize, gp.tileSize);
		right2 = setup("/npc/oldman_right_2",gp.tileSize, gp.tileSize);
		
	}
	
	public void setDialouge() {
		
		dialouges[0] = "Поздрав, странче!";
		dialouges[1] = "Да, ово је димензија снова.";
		dialouges[2] = "Постоји решење да се вратиш у реалност...";
		dialouges[3] = "Видиш ону кућицу на северо-истоку?\n Унутар се налази подрум. Само, мораш да\n нађеш кључ.";
		dialouges[4] = "Барем имаш мач - а острво је пуно\n чудовишта. Пази се!";
	}
	
	public void setAction() {
		
		actionLockCounter ++ ;
		
		if (actionLockCounter == 120) {
			
			Random random = new Random();
			int i = random.nextInt(100)+1; // pick up a number from 1 -100
			if (i <= 25) {
				direction = "up";
			}
			if (i > 25 && i <= 50) {
				direction = "down";
			}
			if (i > 50 && i <= 75) {
				direction = "left";
			}
			if (i > 75 && i < 100) {
				direction = "right";
			}
			
			actionLockCounter = 0;
		}
	}
	
	public void speak() {
		/* THIS LINE CAN BE USED EVERYWHERE
		if (dialouges[dialougeIndex] == null) {
			dialougeIndex = 0;
		}
		
		gp.ui.currentDIalouge = dialouges[dialougeIndex];
		dialougeIndex++;
		
		switch (gp.player.direction) {
		case "up":
			direction = "down";
			break;
		case "down":
			direction = "up";
			break;
		case "left":
			direction = "right";
			break;
		case "right":
			direction = "left";
			break;
		}
		*/
		super.speak();
		
	}
}
