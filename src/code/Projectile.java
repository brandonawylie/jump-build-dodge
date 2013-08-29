package code;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Projectile {
	//speed of bullet
	final float BULLET_SPEED = 1.5f;
	//stores the x/y position, velocity
	float x, y, dx, dy;
	//width & height
	int width = 5;
	int height = 6;
	//color of the bullets
	Color color = Color.red;
	
	//constructors that take x and y of the starting position
	public Projectile(float x, float y, float desX, float desY){
		this.x = x;
		this.y = y;

		if(x < desX && y < desY){
			System.out.println("quadrant 4");
			dx = x - desX;
			dy = y - desY;
		}
		else if(x > desX && y < desY){
			System.out.println("quadrant 3");
			dx = x - desX;
			dy = y - desY;
		}
		else if(x < desX && y > desY){
			System.out.println("quadrant 1");
			dx = x - desX;
			dy = y - desY;

		}else{
			System.out.println("quadrant 2");
			dx = x - desX;
			dy = y - desY;
		}
		dx = -dx;
		dy = -dy;


		float v = (float) Math.sqrt(dx*dx + dy*dy);
		dx /= v;
		dy /= v;
	}
	
	public void draw(Graphics g, float shiftX, float shiftY){
		g.setColor(color);
		g.drawRect(x - shiftX, y - shiftY, width, height);
	}
	
	public void update(int delta){
		x += dx*BULLET_SPEED*delta/10;
		y += dy*BULLET_SPEED * delta/10;
	}
}
