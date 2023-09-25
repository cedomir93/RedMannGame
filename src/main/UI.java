package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import object.OBJ_Key;
import object.OBJ_ManaCrystal;
import object.OBJ_Heart;
import entity.Entity;

public class UI {

	GamePanel gp;
	Graphics2D g2;
	Font arial_40, arial_80B;
	BufferedImage heart_full, heart_half, heart_blank, crystal_full, crystal_blank;
	//BufferedImage keyImage;
	public boolean messageOn = false;
//	public String message = "";
//	int messageCounter = 0;
	ArrayList<String> message = new ArrayList<>();
	ArrayList<Integer> messageCounter = new ArrayList<>();
	public boolean gameFinished = false;
	public String currentDIalouge = "";
	public int commandNum = 0;
	public int slotCol = 0;
	public int slotRow = 0;
	
	public UI(GamePanel gp) {
		this.gp=gp;
		arial_40 = new Font("Arial", Font.PLAIN, 40);
		arial_80B = new Font("Arial", Font.BOLD, 80);
	//	OBJ_Key key = new OBJ_Key(gp);
	//	keyImage = key.image;
		//CREATE HUD OBJECT
		Entity heart = new OBJ_Heart(gp);
		heart_full = heart.image;
		heart_half = heart.image2;
		heart_blank = heart.image3;
		//MANA KRISTALI
		Entity crystal = new OBJ_ManaCrystal(gp);
		crystal_full = crystal.image;
		crystal_blank = crystal.image2;
	}
	public void addMessage(String text) {
		
//		message = text;
//		messageOn = true;
		message.add(text);
		messageCounter.add(0);
		
		
	}
	public void draw(Graphics2D g2) {
		
		this.g2 = g2;
		
		g2.setFont(arial_40);
		g2.setColor(Color.white);
		
		//TITLE STATE
		if (gp.gameState == gp.titleState) {
			drawTitleScreen();
		}
		
		//PLAY STATE
		if (gp.gameState == gp.playState) {
			drawPlayerLife();
			drawMessage();
		}
		//PAUSE STATE
		if (gp.gameState == gp.pauseState) {
			drawPlayerLife();
			drawPauseScreen();
		}
		//DIALOUGE STATE
		if (gp.gameState == gp.dialougeState) {
			drawPlayerLife();
			drawDialougeScreen();
		}
		//CHARACTER STATE
		if (gp.gameState == gp.characterState) {
			drawCharacterScreen();
			drawInventory();
		}
	}
	public void drawPlayerLife() {
		
		int x = gp.tileSize/2;
		int y = gp.tileSize/2;
		int i = 0;
		
		//DRAW MAX LIFE
		while(i < gp.player.maxLife/2) {
			g2.drawImage(heart_blank, x, y, null);
			i++;
			x += gp.tileSize;
		}
		
		//RESET
		
		x = gp.tileSize/2;
		y = gp.tileSize/2;
		i = 0;
		
		//DRAW CURRENT LIFE
		while(i < gp.player.life) {
			g2.drawImage(heart_half, x, y, null);
			i++;
			if (i < gp.player.life) {
				g2.drawImage(heart_full, x, y, null);
			}
			i++;
			x += gp.tileSize;
		}
		//DRAW MAX KRISTALI
		x = (gp.tileSize/2) - 5;
		y = (int) (gp.tileSize*1.5);
		i = 0;
		while(i < gp.player.maxMana) {
			g2.drawImage(crystal_blank, x, y,null);
			i++;
			x += 35;
		}
		//DRAW MANA
		x = (gp.tileSize/2) - 5;
		y = (int) (gp.tileSize*1.5);
		i = 0;
		while(i < gp.player.mana) {
			g2.drawImage(crystal_full, x, y,null);
			i++;
			x += 35;
		}
	}
	public void drawMessage() {
		int messageX = gp.tileSize;
		int messageY = gp.tileSize*4;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));
		for (int i = 0; i < message.size(); i++) {
			if (message.get(i) != null) {
				g2.setColor(Color.black);
				g2.drawString(message.get(i), messageX+2, messageY+2);
				g2.setColor(Color.white);
				g2.drawString(message.get(i), messageX, messageY);
				
				int counter = messageCounter.get(i) + 1;
				messageCounter.set(i, counter);
				messageY += 50;
				
				if (messageCounter.get(i) > 180) {
					message.remove(i);
					messageCounter.remove(i);
				}
			}
		}
	}
	public void drawTitleScreen() {
		
		g2.setColor(new Color(0, 0, 0));
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		
		//TITLE NAME
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
		String text = "The Red Mann";
		int x = getXforCenteredText(text);
		int y = gp.tileSize*3;
		//SHADOW TEXT
		g2.setColor(Color.DARK_GRAY);
		g2.drawString(text, x+5, y+5);
		
		//MAIN COLOR
		g2.setColor(Color.RED);
		g2.drawString(text, x, y);
		
		//MAIN IMAGE
		x = gp.screenWidth/2 - (gp.tileSize*2)/2;
		y += gp.tileSize*2;
		g2.drawImage(gp.player.idle, x, y, gp.tileSize*2, gp.tileSize*2, null);
		
		//MENU
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48F));
		
		text = "Nova Igra";
		x = getXforCenteredText(text);
		y += gp.tileSize*3.5;
		g2.drawString(text, x, y);
		if (commandNum == 0) {
			g2.drawString(">", x-gp.tileSize, y);
		}
		
		text = "Učitaj";
		x = getXforCenteredText(text);
		y += gp.tileSize;
		g2.drawString(text, x, y);
		if (commandNum == 1) {
			g2.drawString(">", x-gp.tileSize, y);
		}
		
		text = "Izlaz";
		x = getXforCenteredText(text);
		y += gp.tileSize;
		g2.drawString(text, x, y);
		if (commandNum == 2) {
			g2.drawString(">", x-gp.tileSize, y);
		}
		
	}
	public void drawPauseScreen() {
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN,80F));
		String text = "PAUSED!";
		int x = getXforCenteredText(text);
		int y = gp.screenHeight/2;
		
		g2.drawString(text, x, y);
	}
	public void drawDialougeScreen() {
		
		//WINDOW
		int x = gp.tileSize*2;
		int y = gp.tileSize/2;
		int width = gp.screenWidth - (gp.tileSize*4);
		int height = gp.tileSize*4;
		drawSubWindow(x, y, width, height);
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 25F));
		x += gp.tileSize;
		y += gp.tileSize;
		
		for (String line : currentDIalouge.split("\n")) {
			g2.drawString(line, x, y);
			y += 40;
		}
		
		//g2.drawString(currentDIalouge, x, y);
	}
	public void drawCharacterScreen() {
		
		//CREATE A FRAME
		final int frameX = gp.tileSize; 
		final int frameY = gp.tileSize;
		final int frameWidth= gp.tileSize*6;
		final int frameHeight= gp.tileSize*10;
		drawSubWindow(frameX, frameY ,frameWidth, frameHeight);
		
		//TEXT
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(30F));
		
		int textX = frameX + 20;
		int textY = frameY + gp.tileSize;
		final int lineHeight = 35;
		
		//NAMES
		g2.drawString("Ниво", textX, textY);textY += lineHeight;
		g2.drawString("Живот", textX, textY);textY += lineHeight;
		g2.drawString("Кристал моћи", textX, textY);textY += lineHeight;
		g2.drawString("Јачина", textX, textY);textY += lineHeight;
		g2.drawString("Cпретност", textX, textY);textY += lineHeight;
		g2.drawString("Напад", textX, textY);textY += lineHeight;
		g2.drawString("Одбрана", textX, textY);textY += lineHeight;
		g2.drawString("Искуство", textX, textY);textY += lineHeight;
		g2.drawString("Следећи ниво", textX, textY);textY += lineHeight;
		g2.drawString("Новчић", textX, textY);textY += lineHeight + 10;
		g2.drawString("Оружије", textX, textY);textY += lineHeight + 15;
		g2.drawString("Штит", textX, textY);textY += lineHeight;
		
		//VALUES
		int tailX = (frameX + frameWidth) - 30;
		//Reset textY
		textY = frameY + gp.tileSize;
		String value;
			//Level...
		value = String.valueOf(gp.player.level);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, tailX, textY);
		textY += lineHeight;
			//Life...
		value = String.valueOf(gp.player.life + "/" + gp.player.maxLife);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, tailX, textY);
		textY += lineHeight;
			//Mana
		value = String.valueOf(gp.player.mana + "/" + gp.player.maxMana);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, tailX, textY);
		textY += lineHeight;
			//Strength....
		value = String.valueOf(gp.player.strenght);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, tailX, textY);
		textY += lineHeight;
			//Dexterity...
		value = String.valueOf(gp.player.dexterity);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, tailX, textY);
		textY += lineHeight;
			//
		value = String.valueOf(gp.player.attack);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, tailX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.defence);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, tailX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.exp);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, tailX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.nextLevelExp);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, tailX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.coin);
		textX = getXforAlignToRightText(value, tailX);
		g2.drawString(value, tailX, textY);
		textY += lineHeight;
		
		g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tileSize, textY-24, null);
		textY += gp.tileSize;
		g2.drawImage(gp.player.currentShield.down1, tailX - gp.tileSize, textY-24, null);
		
	}
	public void drawInventory() {
		//FRAME
		int frameX = gp.tileSize*9;
		int frameY = gp.tileSize;
		int frameWidth = gp.tileSize*6;
		int frameHeight = gp.tileSize*5;
		drawSubWindow(frameX, frameY, frameWidth, frameHeight);
		//SLOT
		final int slotXstart = frameX + 20;
		final int slotYstart = frameY + 20;
		int slotX = slotXstart;
		int slotY = slotYstart;
		int slotSize = gp.tileSize+3;
		//DRAW PLAYERS'S ITEMS
		for (int i = 0; i < gp.player.inventory.size(); i++) {
			//EQUIPT CURSOR
			if (gp.player.inventory.get(i) == gp.player.currentWeapon ||
					gp.player.inventory.get(i) == gp.player.currentShield) {
				g2.setColor(new Color(240, 190, 90));
				g2.fillRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);
			}
			
			
			g2.drawImage(gp.player.inventory.get(i).down1, slotX, slotY, null);
			
			slotX += slotSize;
			if (i == 4 || i == 9 || i == 14) {
				slotX = slotXstart;
				slotY += slotSize;
			}
			
			
		}
		//CURSOR
		int cursorX = slotXstart + (slotSize * slotCol);
		int cursorY = slotYstart + (slotSize * slotRow);
		int cursorWidth = gp.tileSize;
		int cursorHeight = gp.tileSize;
		//DRAW CURSOR
		g2.setColor(Color.white);
		g2.setStroke(new BasicStroke(3));
		g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);
		//DESCRIPTION FRAME
		int dFrameX = frameX;
		int dFrameY = frameY + frameHeight;
		int dFrameWidth = frameWidth;
		int dFrameHeight = gp.tileSize*3;
	//	drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);
		//DRAW DESCRIPTION TEXT
		int textX = dFrameX + 20;
		int textY = dFrameY + gp.tileSize;
		g2.setFont(g2.getFont().deriveFont(28F));
		int itemIndex = getItemIndexOnSlot();
		if (itemIndex < gp.player.inventory.size()) {
			drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);
			for (String line : gp.player.inventory.get(itemIndex).description.split("\n")) {
				g2.drawString(line, textX, textY);
				textY += 32;
			}
		}
		
	}
	public int getItemIndexOnSlot() {
		int itemIndex = slotCol + (slotRow*5);
		return itemIndex;
	}
	public void drawSubWindow(int x, int y, int width, int height) {
		
		Color c = new Color(0,0,0, 210);
		g2.setColor(c);
		g2.fillRoundRect(x, y, width, height, 35, 35);
		
		c = new Color(255,255,255);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
	}
	public int getXforCenteredText(String text) {
		int lenght = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth(); 
		int x = gp.screenWidth/2 - lenght/2;
		return x;
	}
	public int getXforAlignToRightText(String text, int taliX) {
		int lenght = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth(); 
		int x = taliX - lenght;
		return x;
	}
}
