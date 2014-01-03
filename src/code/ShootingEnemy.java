package code;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

/**
 * Represents the base shooting enemy class, contains logic for firing based on rate etc
 * fireRate is an [] of integers, it's how many miliseconds to delay that round
 * @author brandon
 *
 */
public class ShootingEnemy {
	
	//AF: Enemy at the cartesian coordinates (x, y), with the given width/height.
	// fireRate is the delay before each shot (cycles from length-1 -> 0) so for instance {1000, 2000} would fire after 1 second, 2 second, 1 second ...
	// range is the range in which the enemy can fire
	
	int range = 1000;//range that the ShootingEnemy can fire in.
	int fireRate[] = {1000};//rate the ShootingEnemy fires (in secs).
	int fireRateIndex = 0;
	float x, y, dx, dy, width, height;;

	protected int SPEED = 1;
	public float tileWidth = .5f;
	public float tileHeight = .5f;

	List<Projectile> projectiles = new ArrayList<>();
	
	/**
	 * Initializes the shooting enemy
	 * @see range is default 1000
	 * 		fireRate is default {1000} (1 second)
	 * @param x the x coordinate of the shooting enemy
	 * @param y the y coordinate of the shooting enemy
	 * @param width the width of the shooting enemy
	 * @param height the height of the shooting enenmy
	 */
	protected ShootingEnemy(float x, float y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width*tileWidth;
		this.height = height*tileHeight;
	}

	//pre : graphics != null
	//post: draws the ShootingEnemy,projectiles
	public void draw(Graphics g, int shiftX, int shiftY){
		//draw the ShootingEnemy
		g.fillRect(x - shiftX, y - shiftY, width, height);

		for(Projectile p : projectiles)
			p.draw(g, shiftX, shiftY);

	}

	//pre : player != null
	//post: this contains the "logic" of a ShootingEnemy
	long timeLastFired = 0;//in milliseconds
	Vector2f direction = null;
	public void update(int delta, Player player, Map map){
		//TODO calculate the distance + compare it to the range.
		boolean canFire = true;
		float distanceToPlayer = (float) Math.sqrt(Math.pow(player.x - x, 2) + Math.pow(player.y - y, 2));

		if(distanceToPlayer > range){
			canFire = false;
			direction = Helper.pointsToDirectionVector(x, y, player.x, player.y);
		}//DO NOT USE direction ABOVE THIS LINE---------------------------------------------------------------------------------------------------------------------------------------------
		else{
			canFire = true;
		}
		//update the ShootingEnemy's projectiles
		for(Projectile p : projectiles)
			p.update(delta);

		float targetX = player.x + player.width/2;
		float targetY = player.y + player.height/2;

		//if the time since the last shot was fired >= fireRate, then shoot again
		if(System.currentTimeMillis() - timeLastFired > fireRate[fireRateIndex] && canFire){
			fire(targetX, targetY);
			timeLastFired = System.currentTimeMillis();
		}
		fireRateIndex++;
		if(fireRateIndex >= fireRate.length)
			fireRateIndex = 0;
		for(int i =0; i < projectiles.size(); i++){
		    Projectile p = projectiles.get(i);
		    if((p.x >= map.mapWidth || p.x <= 0) && (p.y >= map.mapHeight || p.y <= 0)){
		    	projectiles.remove(p);
		    }
		}
	}

	//pre : --
	//post: fires a Projectile at the given point
	public void fire(float targetX, float targetY){
		Projectile p = new Projectile(x + width/2, y + height/2, targetX, targetY);
		projectiles.add(p);
	}
	
	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int[] getFireRate() {
		return fireRate;
	}

	public void setFireRate(int[] fireRate) {
		this.fireRate = fireRate;
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
}
