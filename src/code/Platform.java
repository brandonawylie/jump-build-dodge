package code;
import java.util.Random;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Platform {

	//basic variables to store position, width/height and the x/y ratios of the map
	public float x, y, ratioX, ratioY, width, height;
	//colors for the inside and outline of the platform
	private Color color = Color.black;
	private Color outline = Color.white;
	//have only ONE random color generated for this block at a time
	public boolean isRandom = false;
	public long resetRandomTime = -1;
	//this determines how long the randomly generated color stays on the block
	public int colorStayTimeSecs = 2;

	public Platform(int x, int y, float width, float height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	//contains most the logic for this platform
	public void update(){
		//if the color on the block has lasted its duration, reset it
		if( !isRandom && (resetRandomTime == -1 || (System.currentTimeMillis()/100 - resetRandomTime) >= colorStayTimeSecs)){
			color = Color.black;
			outline = Color.white;
		}

	}

	//draws this platform
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


	//randomly assings this block a random color
	public void setRandomColor(long randomseed){
		isRandom = true;
		Random r = new Random();
		r.setSeed(randomseed);
		switch(r.nextInt(8)){
			case 0:
				color = Color.blue;
				break;
			case 1:
				color = Color.pink;
				break;
			case 2:
				color = Color.magenta;
				break;
			case 3:
				color = Color.cyan;
				break;
			case 4:
				color = Color.orange;
				break;
			case 5:
				color = Color.green;
			case 6:
				color = Color.yellow;
				break;
			case 7:
				color = Color.blue;
				break;
			default:
				break;
		}
	}

	//starts the timer to remove the random color from the block
	public void resetColor(){
		isRandom = false;
		resetRandomTime = System.currentTimeMillis()/100;
	}
}
