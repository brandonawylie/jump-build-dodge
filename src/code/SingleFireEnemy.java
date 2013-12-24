package code;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

public class SingleFireEnemy extends ShootingEnemy{

    public SingleFireEnemy(float x, float y, int width, int height) {
    	super(x, y, width, height);
	// TODO Auto-generated constructor stub
    }

    public void draw(Graphics g, int shiftX, int shiftY){
		super.draw(g, shiftX, shiftY);
		g.fillRect(x - shiftX, y - shiftY, width, height);
    }

	long timeLastFired = 0;//in milliseconds
	Vector2f direction = null;
	public void update(int delta, Player player){
//		//TODO calculate the distance + compare it to the range.
//		boolean canFire = true;
//		float distanceToPlayer = (float) Math.sqrt(Math.pow(player.x - x, 2) + Math.pow(player.y - y, 2));
//
//		boolean updatex = true;
//		boolean updatey = true;
//		if(distanceToPlayer > range){
//			updatex = true;
//			updatey = true;
//			direction = Helper.pointsToDirectionVector(x, y, player.x, player.y);
//		}//DO NOT USE direction ABOVE THIS LINE---------------------------------------------------------------------------------------------------------------------------------------------
//		else{
//			updatex = false;
//			updatey = false;
//		}
//
//
//		if(updatex)
//			x -= direction.x*SPEED;
//		if(updatey)
//			y -= direction.y*SPEED;
	}

}
