package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileCacheImageInputStream;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;
import object.OBJ_Fireball;
import object.OBJ_Key;
import object.OBJ_Rock;
import object.OBJ_Shield_Wood;
import object.OBJ_Sword_Normal;

public class Player extends Entity {

	GamePanel gp;
	KeyHandler keyH;
	
	public final int screenX;
	public final int screenY;
	
	public int hasKey = 0;
	int standCounter = 0;
	public boolean attackCancel = false;
	public ArrayList<Entity> inventory = new ArrayList<>();
	public final int maxInventorySize = 20;
	
	
	public Player(GamePanel gp, KeyHandler keyH) {
		super(gp);
		this.gp = gp;
		this.keyH = keyH;
		
		
		screenX = gp.screenWidth/2 - (gp.tileSize/2);
		screenY = gp.screenHeight/2 - (gp.tileSize/2);
		
		solidArea = new Rectangle();
		solidArea.x = 8;
		solidArea.y = 16;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 32;
		solidArea.height = 32;
		
	//	attackArea.width = 36;
	//	attackArea.height = 36;
		
		setDefaultValues();
		getPlayerImage();
		getPlayerAttackImage();
		setItems();
	}
	public void setDefaultValues() {
		worldX = gp.tileSize * 23;
		worldY = gp.tileSize * 21;
		speed = 4;
		direction = "down";
		
		//PLAYER STATUS
		level = 1;
		strenght = 1; // More strenght, more damage he gives!
		maxLife = 6; // More dexterity, less damage
		life = maxLife;
		maxMana = 4;
		mana = maxMana;
		ammo = 10;
		dexterity = 1;
		exp = 0;
		nextLevelExp = 6;
		coin = 0;
		currentWeapon = new OBJ_Sword_Normal(gp);
		currentShield = new OBJ_Shield_Wood(gp);
		projectile = new OBJ_Fireball(gp);
		attack = getAttack(); 
		defence = getDefence();
		
	}
	public void setItems() {
		
		inventory.add(currentWeapon);
		inventory.add(currentShield);
		inventory.add(new OBJ_Key(gp));
		inventory.add(new OBJ_Key(gp));
		
	}
	public int getAttack() {
		attackArea = currentWeapon.attackArea;
		return attack = strenght * currentWeapon.attackValue;
	}
	public int getDefence() {
		return defence = dexterity * currentShield.defenseValue;
	}
	public void getPlayerImage() {
		
		up1 = setup("/player/boy_up_1");
		up2 = setup("/player/boy_up_2");
		down1 = setup("/player/boy_down_1");
		down2 = setup("/player/boy_down_2");
		left1 = setup("/player/boy_left_1");
		left2 = setup("/player/boy_left_2");
		right1 = setup("/player/boy_right_1");
		right2 = setup("/player/boy_right_2");
		idle = setup("/player/boy_idle_1");
		
		
	}
	public void getPlayerAttackImage() {
		
		if (currentWeapon.type == type_sword) {
			attackUp1 = setup("/player/boy_attack_up_1",gp.tileSize, gp.tileSize*2);
			attackUp2 = setup("/player/boy_attack_up_2",gp.tileSize, gp.tileSize*2);
			attackDown1 = setup("/player/boy_attack_down_1",gp.tileSize, gp.tileSize*2);
			attackDown2 = setup("/player/boy_attack_down_2",gp.tileSize, gp.tileSize*2);
			attackLeft1 = setup("/player/boy_attack_left_1",gp.tileSize*2, gp.tileSize);
			attackLeft2 = setup("/player/boy_attack_left_2",gp.tileSize*2, gp.tileSize);
			attackRight1 = setup("/player/boy_attack_right_11",gp.tileSize*2, gp.tileSize);
			attackRight2 = setup("/player/boy_attack_right_22",gp.tileSize*2, gp.tileSize);
		}
		
		if (currentWeapon.type == type_axe) {
			attackUp1 = setup("/player/boy_axe_up_1",gp.tileSize, gp.tileSize*2);
			attackUp2 = setup("/player/boy_axe_up_2",gp.tileSize, gp.tileSize*2);
			attackDown1 = setup("/player/boy_axe_down_1",gp.tileSize, gp.tileSize*2);
			attackDown2 = setup("/player/boy_axe_down_2",gp.tileSize, gp.tileSize*2);
			attackLeft1 = setup("/player/boy_axe_left_1",gp.tileSize*2, gp.tileSize);
			attackLeft2 = setup("/player/boy_axe_left_2",gp.tileSize*2, gp.tileSize);
			attackRight1 = setup("/player/boy_axe_right_1",gp.tileSize*2, gp.tileSize);
			attackRight2 = setup("/player/boy_axe_right_2",gp.tileSize*2, gp.tileSize);
		}	
	}
	public BufferedImage setup(String imageName) {
		
		UtilityTool uTool = new UtilityTool();
		BufferedImage image = null;
		
		try {
			
			//image = ImageIO.read(getClass().getResourceAsStream("/player/"+ imageName + ".png"));
			image = ImageIO.read(getClass().getResourceAsStream(imageName + ".png"));
			image= uTool.scaleImage(image, gp.tileSize, gp.tileSize);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
		
	}
	public void update() {
		
		if (attacking == true) {
			 attacking();
		}
		else if (keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true
				|| keyH.enterPressed == true) {
			if (keyH.upPressed == true) {
			direction = "up";
			}
			else if (keyH.downPressed == true) {
				direction = "down";
			}
			else if (keyH.leftPressed == true) {
				direction = "left";
			}
			else if (keyH.rightPressed == true) {
				direction = "right";
			}
			
			//CHECK TILE COLLISION
			collisionOn = false;
			gp.cChecker.checkTile(this);
			
			//CHECK OBJECT COLLISION
			int objIndex = gp.cChecker.checkObject(this, true);
			pickUpObject(objIndex);
			
			//CHECK NPC COLLISION!
			int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
			interactNPC(npcIndex);
			
			//CHECK MONSTER COLLISION!
			int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
			contactMonster(monsterIndex);
			//CHECK INTERACTIVE TILE COLLISION
			int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);
			
			//CHECK EVENT
			  gp.eHandler.checkEvent();
			
			//gp.keyH.enterPressed = false;
			
			//IF COLLISION IS FALSE, PLAYER CAN MOVE
			
			if (collisionOn == false && keyH.enterPressed == false) {
				
				switch (direction) {
				case "up": worldY -= speed; break;
				case "down": worldY += speed; break;
				case "left":worldX -= speed; break;
				case "right": worldX += speed; break;
				}
			}
			
			if (keyH.enterPressed == true && attackCancel == false) {
				gp.playSE(7);
				attacking = true;
				spriteCounter = 0;
			}
			
			attackCancel = false;
			
			gp.keyH.enterPressed = false;
			
			spriteCounter++;
			if (spriteCounter > 12) {
				if (spriteNum == 1) {
					spriteNum = 2;
				}
				else if (spriteNum == 2) {
					spriteNum = 1;
				}
				spriteCounter = 0;
			}
		}
		else {
			standCounter++;
			if (standCounter == 20) {
				spriteNum = 1;
				standCounter = 0;
			}
		}
		
		if (gp.keyH.shotKeyPressed == true && projectile.alive == false
				&& shotAvaibleCounter == 30 && projectile.haveResource(this) == true) {
			//SET DEFAULT COORDINATES, DIRECTION AND USER
			projectile.set(worldX, worldY, direction, true, this);
			//SUBTRACT THE COST (POWERS, AMMO, ETC.....)
			projectile.subtractResource(this);
			
			// ADD IT TO THE LIST
			gp.projectileList.add(projectile);
			shotAvaibleCounter = 0;
			gp.playSE(10);
		}
		
		//THE REST OF OUTSIDE OF A KEY OF STATEMENT!
		if (invincible == true) {
			invincibleCounter++;
			if (invincibleCounter > 60) {
				invincible = false;
				invincibleCounter = 0;
			}
		}
		if (shotAvaibleCounter < 30) {
			shotAvaibleCounter++;
		}
		if (life > maxLife) {
			life = maxLife;
		}
		if (mana > maxMana) {
			mana = maxMana;
		}
	}
	public void attacking() {
		
		spriteCounter++;
		
		if (spriteCounter <= 5) {
			spriteNum = 1;
		}
		if (spriteCounter > 5 && spriteCounter <= 25) {
			spriteNum = 2;
			
			//Save the current worldX, worldY, solidArea
			int currentWorldX = (int) worldX;
			int currentWorldY = (int) worldY;
			int solidAreaWidth = solidArea.width;
			int solidAreaHeight = solidArea.height;
			
			//Adjust players worldX/Y for the attackArea
			
			switch (direction) {
			case "up": worldY -= attackArea.height; break;
			case "down": worldY += attackArea.height; break;
			case "left": worldX -= attackArea.width; break;
			case "right": worldX += attackArea.width; break;
			}
			
			//attack area becomes solidArea
			solidArea.width = attackArea.width;
			solidArea.height = attackArea.height;
			//Check monster collision with the updated worldX, worldY and solidArea
			int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
			damageMonster(monsterIndex, attack);
			
			int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);
			damageInteractiveTile(iTileIndex);
			
			worldX = currentWorldX;
			worldY = currentWorldY;
			solidArea.width = solidAreaWidth;
			solidArea.height = solidAreaHeight;
		}
		if (spriteCounter > 25) {
			spriteNum = 1;
			spriteCounter = 0;
			attacking = false;
		}
		
		
	}
	public void pickUpObject(int i) {
		
		if (i != 999) {
			
			//PICK UP ONLY ITEMS
			if (gp.obj[i].type == type_pickUpOnly) {
				gp.obj[i].use(this);
				gp.obj[i] = null;
			}
			//INVENTORY ITMES
			else {
				String text;
				if (inventory.size() != maxInventorySize) {
					
					inventory.add(gp.obj[i]);
					gp.playSE(1);
					text = "Нашао си " + gp.obj[i].name + "!";
				}
				else {
					text = "Нема места!";
				}
				gp.ui.addMessage(text);
				gp.obj[i] = null;
			}
			
		/*	String text;
			if (inventory.size() != maxInventorySize) {
				
				inventory.add(gp.obj[i]);
				gp.playSE(1);
				text = "Нашао си " + gp.obj[i].name + "!";
			}
			else {
				text = "Нема места!";
			}
			gp.ui.addMessage(text);
			gp.obj[i] = null; */
		}
	}
	public void interactNPC(int i) {
		
		if (gp.keyH.enterPressed == true) {
			if (i != 999) {
				attackCancel = true;
			//	if (gp.keyH.enterPressed == true) {
					gp.gameState = gp.dialougeState;
					gp.npc[i].speak();
			//	}
			}
			
		}
		//gp.keyH.enterPressed = false;
	}
	public void contactMonster(int i) {
		
		if (i != 999) {
			
			if (invincible == false && gp.monster[i].dying == false) {
				gp.playSE(6);
				int damage = gp.monster[i].attack - defence;
				if (damage < 0) {
					damage = 0;
				}
				life -= damage;
				invincible = true;
			}
		}
	}
	public void damageMonster(int i, int attack) {
		
		if (i != 999) {
			if (gp.monster[i].invincible == false) {
				gp.playSE(5);
				int damage = attack - gp.monster[i].defence;
				if (damage < 0) {
					damage = 0;
				}
				gp.monster[i].life -= damage;
				gp.ui.addMessage(damage + " damage!");
				gp.monster[i].invincible = true;
				gp.monster[i].damageReaction();
				
				if (gp.monster[i].life <= 0) {
					gp.monster[i].dying = true;
					gp.ui.addMessage("Killed the " + gp.monster[i].name + "!");
					gp.ui.addMessage("Exp + " + gp.monster[i].exp);
					exp += gp.monster[i].exp;
					checkLevelUp();
					//gp.playSE(8);
					//gp.gameState = gp.dialougeState;
					//gp.ui.currentDIalouge = "You are level " + level + " now!\n"
					//		+ "You feel stronger!";
				}
			}
		}
		
	}
	public void damageInteractiveTile(int i) {
		if (i != 999 && gp.iTile[i].destructible == true && gp.iTile[i].isCorrectItem(this) == true) {
			gp.iTile[i] = null;
		}
	}
	public void checkLevelUp() {
		
		if (exp >= nextLevelExp) {
			level++;
			nextLevelExp = nextLevelExp*2;
			maxLife += 2;
			strenght++;
			dexterity++;
			attack = getAttack();
			defence = getDefence();
			gp.playSE(8);
			gp.gameState = gp.dialougeState;
			gp.ui.currentDIalouge = "Tou are level " + level + " now!";
		}
	}
	public void selectItem() {
		
		int itemIndex = gp.ui.getItemIndexOnSlot();
		if (itemIndex < inventory.size()) {
			Entity selectedItem = inventory.get(itemIndex);
			if (selectedItem.type == type_sword || selectedItem.type == type_axe) {
				currentWeapon = selectedItem;
				attack = getAttack();
				getPlayerAttackImage();
			}
			if (selectedItem.type == type_shield) {
				currentShield = selectedItem;
				defence = getDefence();
			}
			if (selectedItem.type == type_consumable) {
				selectedItem.use(this);
				inventory.remove(itemIndex);
			}
		}
		
	}
	public void draw(Graphics2D g2) {
		
		BufferedImage image = null;
		int tempScreenX = screenX;
		int tempScreenY = screenY;
		
		switch (direction) {
		case "up":
			if (attacking == false) {
				if (spriteNum == 1) {image = up1;}
				if (spriteNum == 2) {image = up2;} 
			}
			if (attacking == true) {
				tempScreenY = screenY - gp.tileSize;
				if (spriteNum == 1) {image = attackUp1;}
				if (spriteNum == 2) {image = attackUp2;} 
			}
			break;
		case "down":
			if (attacking== false) {
				if (spriteNum == 1) {image = down1;}
				if (spriteNum == 2) {image = down2;}
			}
			if (attacking== true) {
				if (spriteNum == 1) {image = attackDown1;}
				if (spriteNum == 2) {image = attackDown2;}
			}
			break;
		case "left":
			if (attacking == false) {
				if (spriteNum == 1) {image = left1;}
				if (spriteNum == 2) {image = left2;}
			}
			if (attacking == true) {
				tempScreenX = screenX - gp.tileSize;
				if (spriteNum == 1) {image = attackLeft1;}
				if (spriteNum == 2) {image = attackLeft2;}
			}
			break;
		case "right":
			if (attacking == false) {
				if (spriteNum == 1) {image = right1;}
				if (spriteNum == 2) {image = right2;}
			}
			if (attacking == true) {
				if (spriteNum == 1) {image = attackRight1;}
				if (spriteNum == 2) {image = attackRight2;}
			}
			break;
		}
		
		if (invincible == true) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
		}
		g2.drawImage(image, tempScreenX, tempScreenY, null);
		//Reset alpha
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		
		//DEBUG
//		g2.setFont(new Font("Arial", Font.PLAIN, 25));
//		g2.setColor(Color.white);
//		g2.drawString("Invincible: " + invincibleCounter, 10, 400);
	}
}
