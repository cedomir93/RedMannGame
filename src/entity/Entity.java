package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class Entity {

	GamePanel gp;
	public double worldX, worldY;
	public double speed;
	public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2, idle;
	public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
	public String direction = "down";
	public int spriteCounter = 0;
	public int spriteNum = 1;
	public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
	public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
	public int solidAreaDefaultX, solidAreaDefaultY;
	public boolean collisionOn = false;
	public int actionLockCounter = 0;
	public boolean invincible = false;
	public boolean attacking = false;
	public boolean alive = true;
	public boolean dying = false;
	boolean hpBarOn = false;
	public int invincibleCounter = 0;
	int dyingCounter = 0;
	public int shotAvaibleCounter = 0; //make sure that fire ball is fired only once for now and after sometimes!
	int hpBarCounter = 0;
	String dialouges[] = new String[20];
	int dialougeIndex = 0;
	public BufferedImage image, image2, image3;
	public String name;
	public boolean collision = false;
	
	
	//CHARACTER STATUS
	public int maxLife;
	public int life;
	public int maxMana;
	public int mana;
	public int level;
	public int ammo;
	public int strenght;
	public int dexterity;
	public int attack;
	public int defence;
	public int exp;
	public int nextLevelExp;
	public int coin;
	public Entity currentWeapon;
	public Entity currentShield;
	public Projectile projectile;
	
	//ITEM ATTRIBUTRES
	public int value;
	public int attackValue;
	public int defenseValue;
	public String description = ""; 
	public int useCost; // cost to shoot a projectile
	//TYPE
	public int type; // 0 - player, 1 - npc .............
	public final int type_player = 0;
	public final int type_npc = 1;
	public final int type_monster = 2;
	public final int type_sword = 3;
	public final int type_axe = 4;
	public final int type_shield = 5;
	public final int type_consumable = 6;
	public final int type_pickUpOnly = 7;
	
	public Entity(GamePanel gp) {
		this.gp = gp;
	}
	public void setAction() {}
	public void damageReaction() {}
	public void speak() {
		
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
		
		
	}
	public void use(Entity entity) {}
	public void checkDrop() {
		
	}
	public void dropItem(Entity droppedItem) {
		for (int i = 0; i < gp.obj.length; i++) {
			if (gp.obj[i] == null) {
				gp.obj[i] = droppedItem;
				gp.obj[i].worldX = worldX; // the dead monster
				gp.obj[i].worldY = worldY;
				break;
			}
		}
	}
	public Color getParticleColor() {
		Color color = null;
		return color;
	}
	public int getParticleSize() {
		int size = 0;
		return size;
	}
	public int getParticleSpeed() {
		int speed = 0;
		return speed;
	}
	public int getParticleMaxLife() {
		int maxLife = 0;
		return maxLife;
	}
	public void generateParticle(Entity generator, Entity target) {
		Color color = generator.getParticleColor();
		int size = generator.getParticleSize();
		int speed  = generator.getParticleSpeed();
		int maxLife = generator.getParticleMaxLife();
		
		Particle p1 = new Particle(gp, generator, color, size, speed, maxLife, -1, -1);
		Particle p2 = new Particle(gp, generator, color, size, speed, maxLife, 1, -1);
		Particle p3 = new Particle(gp, generator, color, size, speed, maxLife, -1, 1);
		Particle p4 = new Particle(gp, generator, color, size, speed, maxLife, 1, 1);
		gp.particleList.add(p1);
		gp.particleList.add(p2);
		gp.particleList.add(p3);
		gp.particleList.add(p4);
	}
	public void update() {
		
		setAction();
		collisionOn = false;
		gp.cChecker.checkTile(this);
		gp.cChecker.checkObject(this, false);
		gp.cChecker.checkEntity(this, gp.npc);
		gp.cChecker.checkEntity(this, gp.monster);
		gp.cChecker.checkEntity(this, gp.iTile);
		boolean contactPlayer = gp.cChecker.checkPlayer(this);
		
		if (this.type == type_monster && contactPlayer == true) {
			damagePlayer(attack); 
		/*	if (gp.player.invincible == false) {
				//we can give damage
				int damage = attack - gp.player.defence;
				if (damage < 0) {
					damage = 0;
				}
				gp.player.life -= 1;
				gp.player.invincible = true;
			} */
		}
		
		if (collisionOn == false ) {
			
			switch (direction) {
				case "up": worldY -= speed; break;
				case "down": worldY += speed; break;
				case "left": worldX -= speed; break;
				case "right": worldX += speed; break;
				}
			}
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
			
			if (invincible == true) {
				invincibleCounter++;
				if (invincibleCounter > 40) {
					invincible = false;
					invincibleCounter = 0;
				}
			}
			if (shotAvaibleCounter < 30) {
				shotAvaibleCounter++;
			}
	}
	public void damagePlayer(int attack) {
		
		if (gp.player.invincible = false) {
			//we can give damage
			gp.playSE(6);
			int damage = attack - gp.player.defence;
			if (damage < 0) {
				damage = 0;
			}
			gp.player.life -= damage;
			//gp.player.life -= 1;
			gp.player.invincible =  true;
		}
		
	}
	public void draw(Graphics2D g2) {
		
		BufferedImage image = null;
		
		double screenX = worldX - gp.player.worldX + gp.player.screenX;
		double screenY = worldY - gp.player.worldY + gp.player.screenY;
		
		if (worldX + gp.tileSize> gp.player.worldX - gp.player.screenX &&
			worldX - gp.tileSize< gp.player.worldX + gp.player.screenX &&
			worldY + gp.tileSize> gp.player.worldY - gp.player.screenY &&
			worldY - gp.tileSize< gp.player.worldY + gp.player.screenY) {
			
			switch (direction) {
			case "up":
				if (spriteNum == 1) {image = up1;}
				if (spriteNum == 2) {image = up2;}
				break;
			case "down":
				if (spriteNum == 1) {image = down1;}
				if (spriteNum == 2) {image = down2;}
				break;
			case "left":
				if (spriteNum == 1) {image = left1;}
				if (spriteNum == 2) {image = left2;}
				break;
			case "right":
				if (spriteNum == 1) {image = right1;}
				if (spriteNum == 2) {image = right2;}
				break;
			}
			
			//Monster HP bar
			if (type == 2 && hpBarOn == true) {
				
				double oneScale = (double)gp.tileSize/maxLife;
				double hpBarValue = oneScale*life;
				
				g2.setColor(new Color(35, 35, 35));
				g2.fillRect((int)screenX-1, (int)screenY - 16, gp.tileSize+2, 12);
				g2.setColor(new Color(255, 0, 30));
				g2.fillRect((int)screenX, (int)screenY - 15, (int)hpBarValue, 10);
				hpBarCounter++;
				if (hpBarCounter > 600) {
					hpBarCounter = 0;
					hpBarOn = false;
				}
			}
			if (invincible == true) {
				hpBarOn = true;
				hpBarCounter = 0;
				changeAlpha(g2, 0.4f);
			}
			if (dying == true) {
				 dyingAnimation(g2);
			}
			g2.drawImage(image, (int)screenX, (int)screenY, null);
			changeAlpha(g2, 1f);
		}
	}
	public void dyingAnimation(Graphics2D g2) {
		
		dyingCounter++;
		int i = 5;
		if (dyingCounter <= i) {changeAlpha(g2, 0f);}
		if (dyingCounter > i && dyingCounter <= i*2) {changeAlpha(g2, 1f);}
		if (dyingCounter > i*2 && dyingCounter <= i*3) {changeAlpha(g2, 0f);}
		if (dyingCounter > i*3 && dyingCounter <= i*4) {changeAlpha(g2, 1f);}
		if (dyingCounter > i*4 && dyingCounter <= i*5) {changeAlpha(g2, 0f);}
		if (dyingCounter > i*5 && dyingCounter <= i*6) {changeAlpha(g2, 1f);}
		if (dyingCounter > i*6 && dyingCounter <= i*7) {changeAlpha(g2, 0f);}
		if (dyingCounter > i*7 && dyingCounter <= i*8) {changeAlpha(g2, 1f);}
		if (dyingCounter > i*8) {
			//dying = false;
			alive = false;
		}
		
	}
	public void changeAlpha(Graphics2D g2, float alphaValue) {
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
	}
	public BufferedImage setup(String imagePath, int width, int height) {
		
		UtilityTool uTool = new UtilityTool();
		BufferedImage image = null;
		
		try {
			
			image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
			//image = ImageIO.read(getClass().getResourceAsStream("/player/"+ imagePath + ".png"));
			image = uTool.scaleImage(image, width, height);
			
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return image;
	}
	
}
