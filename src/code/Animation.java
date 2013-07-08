package code;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Animation {
	Image[] images;
	int curIndex = 0;
	int timeElapsed = 0;
	public Animation(int x, int y, int width, int height, Image spriteSheet){
		int len = spriteSheet.getWidth()/width;
		images = new Image[len];
		for(int i = 0; i < len; i++){
			images[i] = spriteSheet.getSubImage(x + width*i, y, width, height);
		}
		System.out.println("len = " + len);
	}
	
	public void draw(Graphics g, float x, float y){
		g.drawImage(images[curIndex], x, y);
		System.out.println("index = " + curIndex);
	}
	
	public void draw(Graphics g, float x, float y, float f, boolean isFlipped){
		g.drawImage(images[curIndex].getFlippedCopy(true, false), x, y);
	}
	
	public void update(int delta){
		System.out.println("delta = " + delta);
		if(timeElapsed >= 500){
			curIndex++;
			if(curIndex >= images.length)
				curIndex = 0;
		}			
		timeElapsed += delta;
	}
}
