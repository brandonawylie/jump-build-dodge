package code;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Animation {
	Image[] images;
	int curIndex = 0;
	int timeElapsed = 0;
	float scale;
	public Animation(int x, int y, int width, int height, Image spriteSheet){
		int len = spriteSheet.getWidth()/width;
		images = new Image[len];
		for(int i = 0; i < len; i++){
			images[i] = spriteSheet.getSubImage(x + width*i, y, width, height);
			scale = Map.blocksize/width;
		}
		System.out.println("len = " + len);
	}
	
	public void draw(Graphics g, float x, float y){
		g.drawImage(images[curIndex], x - images[curIndex].getWidth()/2, y - images[curIndex].getHeight()/2);
		System.out.println("index = " + curIndex);
		g.drawOval(x, y, 10, 10);
	}
	
	public void draw(Graphics g, float x, float y, boolean isFlipped){
		images[curIndex].getFlippedCopy(true, false).draw(x - images[curIndex].getWidth()/2, y - images[curIndex].getHeight()/2, scale);
	}
	
	public void update(int delta){
		System.out.println("delta = " + delta);
		if(timeElapsed >= 100){
			timeElapsed = 0;
			curIndex++;
			if(curIndex >= images.length)
				curIndex = 0;
		}			
		timeElapsed += delta;
	}
}
