package code;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Projectile {
	//speed of bullet
	final float DEFAULT_BULLET_SPEED = 2f;
	//stores the x/y position, velocity
	private float x;
	private float y;
	float dx;
	float dy;
	float speed;
	//width & height
	int width = 5;
	int height = 6;
	int damage = 5;
	//color of the bullets
	Color color = Color.red;

	//constructors that take x and y of the starting position
	public Projectile(float x, float y, float desX, float desY){
	    this.setX(x);
		this.setY(y);
		speed = DEFAULT_BULLET_SPEED;
		if(x < desX && y < desY){
			dx = x - desX;
			dy = y - desY;
		}
		else if(x > desX && y < desY){
			dx = x - desX;
			dy = y - desY;
		}
		else if(x < desX && y > desY){
			dx = x - desX;
			dy = y - desY;

		}else{
			dx = x - desX;
			dy = y - desY;
		}
		dx = -dx;
		dy = -dy;
		float v = (float) Math.sqrt(dx*dx + dy*dy);
		dx /= v;
		dy /= v;
	}

	public Projectile(float x, float y, float[] dir){
	    this.setX(x);
		this.setY(y);
		speed = DEFAULT_BULLET_SPEED;
		float dx = dir[0];
		float dy = dir[1];
		float v = (float) Math.sqrt(dx*dx + dy*dy);
		dx /= v;
		dy /= v;
		this.dx = dx;
		this.dy = dy;
	}

	public void draw(Graphics g, float shiftX, float shiftY){
		g.setColor(color);
		g.drawRect(getX() - shiftX, getY() - shiftY, width, height);
	}

	public void update(int delta){
		setX(getX() + dx*speed*delta/10);
		setY(getY() + dy*speed * delta/10);
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
}
