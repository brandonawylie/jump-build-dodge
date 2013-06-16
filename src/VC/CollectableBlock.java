package VC;
import java.util.Random;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class CollectableBlock {

	//basic variables to store position, width/height and the x/y ratios of the map
	public float x, y, ratioX, ratioY, width, height;
	//colors for the inside and outline of the platform
	private Color color = Color.black;
	private Color outline = Color.white;

	public CollectableBlock(float x, float y, float ratioX, float ratioY, Color color){
		this.x = x;
		this.y = y;
		width = 10*ratioX;
		height = 10*ratioY;
		this.color = color;
	}

	//contains most the logic for this collectable block
	public void update(){

	}

	//draws this collectableblock
	public void draw(Graphics g, int shiftX, int shiftY) {
		//draw the outline of the block
		g.setColor(outline);
	    g.drawRect(x + width/4 - shiftX, y + height/4 - shiftY, width/2, height/2);

		//fill the block
	    g.setColor(color);
	    g.fillRect(x + width/4 - shiftX + 1, y + width/4 - shiftY + 1, width/2 - 1, height/2 - 1);
    }

	//returns the x coordinate of this platform
	public float getX(){
		return x;
	}

	//returns the y coordinate of this platform
	public float getY(){
		return y;
	}

	//returns the color of this collectable block
	public Color getColor(){
		return color;
	}
}