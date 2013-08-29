package code;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

public class ShootingEnemy {
	int range = 100;//range that the ShootingEnemy can fire in.
	int fireRate = 2;//rate the ShootingEnemy fires (in secs).
	float x, y, dx, dy;
	int width, height;
	private int SPEED = 1;
	
	List<Projectile> projectiles = new ArrayList<>();
	//post: initializes an enemy that shoots at the player
	public ShootingEnemy(float x, float y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
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
	public void update(int delta, Player player){
		//TODO calculate the distance + compare it to the range.
		boolean canFire = true;
		float distanceToPlayer = (float) Math.sqrt(Math.pow(player.x - x, 2) + Math.pow(player.y - y, 2));
		
		boolean updatex = true;
		boolean updatey = true;
		if(distanceToPlayer > range){
			updatex = true;
			updatey = true;
			canFire = false;
			direction = Helper.pointsToDirectionVector(x, y, player.x, player.y);
		}//DO NOT USE direction ABOVE THIS LINE---------------------------------------------------------------------------------------------------------------------------------------------
		else{
			updatex = false;
			updatey = false;
			canFire = true;
		}
		//update the ShootingEnemy's projectiles
		for(Projectile p : projectiles)
			p.update(delta);
		
		float targetX = player.x + player.width/2;
		float targetY = player.y + player.height/2;
		
		//if the time since the last shot was fired >= fireRate, then shoot again
		if(System.currentTimeMillis() - timeLastFired > fireRate*1000 && canFire){
			fireAtPoint(targetX, targetY);
			timeLastFired = System.currentTimeMillis();
		}
		
		
		if(updatex)
			x -= direction.x*SPEED;
		if(updatey)
			y -= direction.y*SPEED;
	}
	
	//pre : --
	//post: fires a Projectile at the given point
	public void fireAtPoint(float targetX, float targetY){
		Projectile p = new Projectile(x + width/2, y + height/2, targetX, targetY);
		projectiles.add(p);
	}
}
