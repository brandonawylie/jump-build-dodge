package code;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class ColoredBlock {

		//basic variables to store position, width/height and the x/y ratios of the map
		public float x, y, ratioX, ratioY, width, height;
		//colors for the inside and outline of the platform
		private Color color = Color.black;
		private Color outline = Color.white;

		public ColoredBlock(float x, float y, float ratioX, float ratioY, Color color){
			this.x = x;
			this.y = y;
			width = 10*ratioX;
			height = 10*ratioY;
			this.color = color;
		}

		//contains most the logic for this  block
		public void update(){

		}

		//draws this collectableblock
		public void draw(Graphics g, int shiftX, int shiftY) {
			//draw the outline of the block
			g.setColor(outline);
		    g.drawRect(x - shiftX, y - shiftY, width, height);

			//fill the block
		    g.setColor(color);
		    g.fillRect(x - shiftX + 1, y - shiftY + 1, width - 1, height - 1);
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
