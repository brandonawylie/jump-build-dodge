package code;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Projectile {
	//speed of bullet
	final float BULLET_SPEED = 1.5f;
	//stores the x/y position, velocity
	float x, y, dx, dy;
	//width & height
	int width = 2;
	int height = 2;
	//color of the bullets
	Color color = Color.green;
	
	//constructors that take x and y of the starting position
	public Projectile(float x, float y){
		this.x = x;
		this.y = y;
		dx = BULLET_SPEED;
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
