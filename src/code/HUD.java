package code;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class HUD implements Oberserver{
	private Image hudBottom, hudTop;
	private int rBlockCount, bBlockCount, pBlockCount, gBlockCount;
	
	public HUD(Image hubBottom){
		this.hudBottom = hubBottom;
		rBlockCount = 1;
		bBlockCount = 1;
		pBlockCount = 1;
		gBlockCount = 1;
	}
	
	public void draw(Graphics g){
		int width = hudBottom.getWidth();
		int height = hudBottom.getHeight();
		
		g.drawImage(hudBottom,0, PlatformerGame.HEIGHT - height);
		g.drawString("" + bBlockCount, width*4/8, PlatformerGame.HEIGHT - height/2);
		g.drawString("" + gBlockCount, width*5/8, PlatformerGame.HEIGHT - height/2);
		g.drawString("" + rBlockCount, width*6/8, PlatformerGame.HEIGHT - height/2);
		g.drawString("" + pBlockCount, width*7/8, PlatformerGame.HEIGHT - height/2);
	}

	@Override
	public void changeColorNotification(int[] colors) {
		bBlockCount = colors[0];
		gBlockCount = colors[1];
		rBlockCount = colors[2];
		pBlockCount = colors[3];
	}

	@Override
	public void changePositionNotification(float x, float y) {
		// TODO Auto-generated method stub
		
	}
}
