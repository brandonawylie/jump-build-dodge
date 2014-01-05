package code.uielements;


import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

import code.Player;
import code.gamestates.PlatformerGame;
import code.infrastructure.PlayerObserver;
public class HUD implements PlayerObserver{
	private Image hudBottom, hudTop;
	private int rBlockCount, bBlockCount, yBlockCount, gBlockCount;
	
	public HUD(Image hubBottom){
		this.hudBottom = hubBottom;
		rBlockCount = 1;
		bBlockCount = 1;
		yBlockCount = 1;
		gBlockCount = 1;
	}
	
	double currentHealth;
	double maxHealth;
	float iwidth = 20;
	float iheight = 20;
	int width = PlatformerGame.WIDTH;
	int height = PlatformerGame.HEIGHT;
	float startx = width - 50;
	float starty = 1;

	public void draw(Graphics g){  

		
//		g.drawImage(hudBottom,0, PlatformerGame.HEIGHT - height);
		
		g.drawString("" + gBlockCount, width*5/8, PlatformerGame.HEIGHT - height/2);
		g.drawString("" + rBlockCount, width*6/8, PlatformerGame.HEIGHT - height/2);
		g.drawString("" + yBlockCount, width*7/8, PlatformerGame.HEIGHT - height/2);
		
	
		////////////////////////////////////////////////////////
		//             Block Inventory Container              //
		////////////////////////////////////////////////////////
		int blockContainerWidth = 400;
		int blockContainerHeight = 100;
		int blockContainerX = 0;
		int blockContainerY = height - blockContainerHeight;
		g.setColor(Color.white);
		g.fillRoundRect(blockContainerX, blockContainerY, blockContainerWidth, blockContainerHeight, 5);
		g.setColor(Color.black);
		g.drawString("inventory", blockContainerX, blockContainerY);
		g.drawRoundRect(blockContainerX + 2, blockContainerY + 2, blockContainerWidth - 2, blockContainerHeight - 2, 5);
		
		int n = 0;
		int delta = (int) ((int) (iwidth/2) + iwidth) + 20;
		startx = blockContainerX + blockContainerWidth/2 - (iwidth + 3*delta)/2;
		starty = blockContainerY + blockContainerHeight/2;
		//BLUE
		g.setColor(org.newdawn.slick.Color.blue);
		g.fillRect(startx + n*delta,  starty, iwidth, iheight);
		g.setColor(Color.black);
		g.drawString("" + bBlockCount,startx + n*delta + 5, starty - 20);
		g.drawString("q", startx + n*delta + 5, starty + 20);

		n++;
		g.setColor(Color.red);
		g.fillRect(startx + n*delta,  starty, iwidth, iheight);
		g.setColor(Color.black);
		g.drawString("" + rBlockCount,startx + n*delta + 5, starty - 20);
		g.drawString("w", startx + n*delta + 5, starty + 20);

		n++;
		g.setColor(Color.yellow);
		g.fillRect(startx + n*delta,  starty, iwidth, iheight);
		g.setColor(Color.black);
		g.drawString("" + yBlockCount,startx + n*delta + 5, starty - 20);
		g.drawString("e", startx + n*delta + 5, starty + 20);

		n++;
		g.setColor(Color.green);
		g.fillRect(startx + n*delta,  starty, iwidth, iheight);
		g.setColor(Color.black);
		g.drawString("" + gBlockCount,startx + n*delta + 5, starty - 20);
		g.drawString("r", startx + n*delta + 5, starty + 20);
		//END
		
		////////////////////////////////////////////////
		//				Health Container              //
		////////////////////////////////////////////////
		int healthContainerWidth = 200;
		int healthContainerHeight = 75;
		int healthContainerX = 0;
		int healthContainerY = 0;
		g.setColor(Color.white);
		g.fillRoundRect(healthContainerX, healthContainerY, healthContainerWidth, healthContainerHeight, 5);
		g.setColor(Color.black);
		g.drawString("health", healthContainerX, healthContainerY);
		double currentHealthDistance = (currentHealth/maxHealth)*3*healthContainerWidth/4;
		g.drawRoundRect(healthContainerX + 2, healthContainerY + 2, healthContainerWidth - 2, healthContainerHeight - 2, 5);
		Rectangle r1 = new Rectangle(healthContainerX + healthContainerWidth/8, healthContainerY + healthContainerHeight/4, 3*healthContainerWidth/4, healthContainerHeight/2);
		Rectangle r2 = new Rectangle(healthContainerX + healthContainerWidth/8, healthContainerY + healthContainerHeight/4, (float) currentHealthDistance, healthContainerHeight/2);
		g.setColor(Color.orange);
		g.fill(r1);
		if(currentHealth >= 0){
			g.setColor(Color.green);
			g.fill(r2);
		}
		//END
	}

	@Override
	public void playerPostitionChanged(Player p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerInventoryChanged(Player p) {
		bBlockCount = p.getBlueBlocks();
		rBlockCount = p.getRedBlocks();
		yBlockCount = p.getYellowBlocks();
		gBlockCount = p.getGreenBlocks();
		
	}

	@Override
	public void playerHealthChanged(Player p) {
		this.maxHealth = p.maxHealth;
		this.currentHealth = p.currentHealth;
		System.out.println("swag");
	}
}
