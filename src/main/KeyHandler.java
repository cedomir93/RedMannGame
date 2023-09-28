package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

	public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, shotKeyPressed;
	//DEBUG
	public boolean chechDrawTime = false;
	GamePanel gp;
	//public boolean enterPressed = false;
	
	
	public KeyHandler(GamePanel gp) {
		this.gp=gp;
	}
	
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		//TITLE STATE
		if (gp.gameState == gp.titleState) {
			titleState(code);
		}
		
		//PLAY STATE
		else if (gp.gameState == gp.playState) {
			playState(code);
		}
		
		
		// PAUSE STATE
		
		else if (gp.gameState == gp.pauseState) {
			pauseState(code);
		}
		
		//DIALOUGE STATE
		
		else if (gp.gameState == gp.dialougeState) {
			dialougeState(code);
		}
		
		//CHARACTER STATE
		else if (gp.gameState == gp.characterState) {
			characterState(code);
		}
		//OPTION STATE
		else if (gp.gameState == gp.optionState) {
			optionState(code);
		}
		//GAME OVER STATE
		else if (gp.gameState == gp.gameOverState) {
			gameOverState(code);
		}
	}
	public void titleState(int code) {
		if (code == KeyEvent.VK_UP) {
			gp.ui.commandNum--;
			if (gp.ui.commandNum <0) {
				gp.ui.commandNum = 2;
			}
		}
		if (code == KeyEvent.VK_DOWN) {
			gp.ui.commandNum++;
			if (gp.ui.commandNum > 2) {
				gp.ui.commandNum = 0;
			}
		}
		if (code == KeyEvent.VK_ENTER) {
			if (gp.ui.commandNum == 0) {
				gp.gameState = gp.playState;
				gp.playMusic(0);
			}
			if (gp.ui.commandNum == 1) {
				//Load in progress..........
			}
			if (gp.ui.commandNum == 2) {
				System.exit(0);
			}
		}
	}
	public void playState(int code) {
		if (code == KeyEvent.VK_UP) {
			upPressed = true;
		}
		if (code == KeyEvent.VK_DOWN) {
			downPressed = true;
		}
		if (code == KeyEvent.VK_LEFT) {
			leftPressed = true;
		}
		if (code == KeyEvent.VK_RIGHT) {
			rightPressed = true;
		}
		if (code == KeyEvent.VK_C) {
			gp.gameState = gp.characterState;
		}
		if (code == KeyEvent.VK_ENTER) {
			enterPressed = true;
		}
		if (code == KeyEvent.VK_F) {
			shotKeyPressed = true;
		}
		if (code == KeyEvent.VK_ESCAPE) {
			gp.gameState = gp.optionState;
		}
		//DEBUG
		if (code == KeyEvent.VK_T) {
			if (chechDrawTime == false) {
				chechDrawTime = true;
			}
			else if (chechDrawTime == true) {
				chechDrawTime = false;
			}
		}
	}
	public void pauseState(int code) {
		if (code == KeyEvent.VK_P) {
			gp.gameState = gp.playState;
		}
	}
	public void dialougeState(int code) {
		if (code == KeyEvent.VK_ENTER) {
			gp.gameState = gp.playState;
		}
	}
	public void characterState(int code) {
		if (code == KeyEvent.VK_C) {
			gp.gameState = gp.playState;
		}
		if (code == KeyEvent.VK_UP) {
			if (gp.ui.slotRow != 0) {
				gp.ui.slotRow--;
			gp.playSE(9);
			}
		}
		if (code == KeyEvent.VK_DOWN) {
			if (gp.ui.slotRow != 3) {
				gp.ui.slotRow++;
			gp.playSE(9);
			}
		}
		if (code == KeyEvent.VK_LEFT) {
			if (gp.ui.slotCol != 0) {
				gp.ui.slotCol--;
			gp.playSE(9);
			}
		}
		if (code == KeyEvent.VK_RIGHT) {
			if (gp.ui.slotCol != 4) {
				gp.ui.slotCol++;
				gp.playSE(9);
			}
		}
		if (code == KeyEvent.VK_ENTER) {
			gp.player.selectItem();
		}
	}
	public void optionState(int code) {
		if (code == KeyEvent.VK_ESCAPE) {
			gp.gameState = gp.playState;
		}
		if (code == KeyEvent.VK_ENTER) {
			enterPressed = true;
		}
		
		int maxCommandNum = 0;
		switch (gp.ui.subState) {
		case 0: maxCommandNum = 5; break;
		case 3: maxCommandNum = 1; break;
		}
		if (code == KeyEvent.VK_UP) {
			gp.ui.commandNum--;
			gp.playSE(9);
			if (gp.ui.commandNum < 0) {
				gp.ui.commandNum = maxCommandNum;
			}
		}
		if (code == KeyEvent.VK_DOWN) {
			gp.ui.commandNum++;
			gp.playSE(9);
			if (gp.ui.commandNum > maxCommandNum) {
				gp.ui.commandNum = 0;
			}
		}
		if (code == KeyEvent.VK_LEFT) {
			if (gp.ui.subState == 0) {
				if (gp.ui.commandNum == 1 && gp.music.volumeScale > 0) {
					gp.music.volumeScale--;
					gp.music.checkVolume();
					gp.playSE(9);
				}
				if (gp.ui.commandNum == 2 && gp.se.volumeScale > 0) {
					gp.se.volumeScale--;
					gp.playSE(9);
				}
			}
		}
		if (code == KeyEvent.VK_RIGHT) {
			if (gp.ui.subState == 0) {
				if (gp.ui.commandNum == 1 && gp.music.volumeScale < 5) {
					gp.music.volumeScale++;
					gp.music.checkVolume();
					gp.playSE(9);
				}
				if (gp.ui.commandNum == 2 && gp.se.volumeScale < 5) {
					gp.se.volumeScale++;
					gp.playSE(9);
				}
			}
		}
	}
	public void gameOverState(int code) {
		if (code == KeyEvent.VK_UP) {
			gp.ui.commandNum--;
			if (gp.ui.commandNum < 0) {
				gp.ui.commandNum = 1;
			}
			gp.playSE(9);
		}
		if (code == KeyEvent.VK_DOWN) {
			gp.ui.commandNum++;
			if (gp.ui.commandNum > 1) {
				gp.ui.commandNum = 0;
			}
			gp.playSE(9);
		}
		if (code == KeyEvent.VK_ENTER) {
			if (gp.ui.commandNum == 0) {
				gp.gameState = gp.playState;
				gp.retry();
			}
			else if (gp.ui.commandNum == 1) {
				gp.gameState = gp.titleState;
				gp.restart();
			}
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		
		if (code == KeyEvent.VK_UP) {
			upPressed = false;
		}
		if (code == KeyEvent.VK_DOWN) {
			downPressed = false;
		}
		if (code == KeyEvent.VK_LEFT) {
			leftPressed = false;
		}
		if (code == KeyEvent.VK_RIGHT) {
			rightPressed = false;
		}
		if (code == KeyEvent.VK_F) {
			shotKeyPressed = false;
		}
		
	}
	
}
