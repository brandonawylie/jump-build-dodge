package code;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

import org.newdawn.slick.Animation;
public class Player implements Oberservable{
	float x, y, dx, dy, speed, ratioX, ratioY;
	int width = 49;
	int height = 40;
	int idleRow = 58;
	int runningRow = 0;
	int jumpingRow = 1;
	float jumpSpeed = 5f*GameplayState.VIEWPORT_RATIO_Y;
	boolean movingLeft = false;
	boolean movingRight = false;
	boolean inAir = false;
	long gravityTimer;
	int blueBlocks = 0;
	int redBlocks = 0;
	int yellowBlocks = 0;
	int pinkBlocks = 0;
	int greenBlocks = 0;
	
	public float MAX_SPEED = 5*GameplayState.VIEWPORT_RATIO_X;
	
	org.newdawn.slick.Animation walkingLeft, walkingRight, idle, jumping;
	int jumpAnimationFrames = 3;
	int fallAnimationFrames = 4;
	List<Projectile> projectiles = new ArrayList<Projectile>();

	public Rectangle redR;
	public Rectangle blueR;
	public Rectangle yellowR;
	public Rectangle pinkR;
	public Rectangle greenR;
	float startx = 1;
	float starty = 1;

	float iwidth = 10;
	float iheight = 10;
	
	float delta = 22f;

	SpriteSheet spriteSheet;
	int n = 0;
	public Player(String path, float x, float y){
		this.x = x; this.y = y;
		dx = 0; dy = 0;
		speed = .075f*GameplayState.VIEWPORT_RATIO_X;
		redR = new Rectangle(startx, starty + delta*n, iwidth, iheight);
		n++;
		blueR = new Rectangle(startx, starty + delta*n, iwidth, iheight);
		n++;
		yellowR = new Rectangle(startx, starty + delta*n, iwidth, iheight);
		n++;
		pinkR = new Rectangle(startx, starty + delta*n, iwidth, iheight);
		n++;
		greenR = new Rectangle(startx, starty + delta*n, iwidth, iheight);
		
		try {
			spriteSheet = new SpriteSheet(path, width,height);
		} catch (SlickException e) { }
		Image[] walk = {spriteSheet.getSprite(0, runningRow).getSubImage(0, 0, width, height), spriteSheet.getSprite(1, runningRow).getSubImage(0, 0, width, height), 
				spriteSheet.getSprite(2, runningRow).getSubImage(0, 0, width, height), spriteSheet.getSprite(3, runningRow).getSubImage(0, 0, width, height), spriteSheet.getSprite(4, runningRow).getSubImage(0, 0, width, height), 
				spriteSheet.getSprite(5, runningRow).getSubImage(0, 0, width, height),spriteSheet.getSprite(6, runningRow).getSubImage(0, 0, width, height),spriteSheet.getSprite(7, runningRow).getSubImage(0, 0, width, height),spriteSheet.getSprite(8, runningRow).getSubImage(0, 0, width, height)};
		int[] dur = {200,200,200,200,200,200,200,200,200};
		walkingLeft = new Animation(walk, dur);
		Image[] walkR = {spriteSheet.getSprite(0, runningRow).getFlippedCopy(true, false).getSubImage(0, 0, 50, 40), spriteSheet.getSprite(1, runningRow).getFlippedCopy(true, false).getSubImage(0, 0, 50, 40), 
				spriteSheet.getSprite(2, runningRow).getFlippedCopy(true, false).getSubImage(0, 0, 50, 40), spriteSheet.getSprite(3, runningRow).getFlippedCopy(true, false).getSubImage(0, 0, 50, 40), spriteSheet.getSprite(4, runningRow).getFlippedCopy(true, false).getSubImage(0, 0, 50, 40), 
				spriteSheet.getSprite(5, runningRow).getFlippedCopy(true, false),spriteSheet.getSprite(6, runningRow).getFlippedCopy(true, false),spriteSheet.getSprite(7, runningRow).getFlippedCopy(true, false),spriteSheet.getSprite(8, runningRow).getFlippedCopy(true, false)};
		walkingRight = new Animation(walkR, dur);
		Image[] jump = {spriteSheet.getSprite(0, jumpingRow).getFlippedCopy(true, false).getSubImage(0, 0, 50, 40), spriteSheet.getSprite(1, jumpingRow).getFlippedCopy(true, false).getSubImage(0, 0, 50, 40), 
				spriteSheet.getSprite(2, jumpingRow).getFlippedCopy(true, false).getSubImage(0, 0, 50, 40), spriteSheet.getSprite(3, jumpingRow).getFlippedCopy(true, false).getSubImage(0, 0, 50, 40), spriteSheet.getSprite(4, runningRow).getFlippedCopy(true, false).getSubImage(0, 0, 50, 40), 
				spriteSheet.getSprite(5, jumpingRow).getFlippedCopy(true, false),spriteSheet.getSprite(6, jumpingRow).getFlippedCopy(true, false),spriteSheet.getSprite(7, jumpingRow).getFlippedCopy(true, false),spriteSheet.getSprite(8, runningRow).getFlippedCopy(true, false)};
		jumping = new Animation(jump, dur);
		Image[] run = {spriteSheet.getSprite(0, runningRow), spriteSheet.getSprite(1, runningRow), 
				spriteSheet.getSprite(2, runningRow), spriteSheet.getSprite(3, runningRow), spriteSheet.getSprite(4, runningRow), 
				spriteSheet.getSprite(5, runningRow),spriteSheet.getSprite(6, runningRow),spriteSheet.getSprite(7, runningRow),spriteSheet.getSprite(8, runningRow)};
		idle = new Animation(run, dur);
		//height = 30;
	}
	long timer = 0;
	public void render(Graphics g, float shiftX, float shiftY){
		idle.draw(x-shiftX, y-shiftY);
		for(Projectile p : projectiles)
			p.draw(g, shiftX, shiftY);

		//g.drawImage(walkingAnimation.images[0].getFlippedCopy(true, false), x - shiftX, y - shiftY);
		if(movingLeft){
			//walkingAnimation.draw(g, x - shiftX, y - shiftY, true);
			walkingLeft.draw(x-shiftX, y-shiftY);
		}else if(movingRight){
//			walkingAnimation.draw(g, x - shiftX, y - shiftY);
			walkingRight.draw(x-shiftX, y-shiftY);
		}else if(inAir){
			
		}//else
//			idleAnimation.draw(g, x - shiftX, y - shiftY);
//			idle.draw(x-shiftX, y-shiftY);
		int n = 0;
		g.setColor(Color.blue);
		g.fillRect(startx, starty + delta*n, iwidth, iheight);

		n++;
		g.setColor(Color.red);
		g.fillRect(startx, starty + delta*n, iwidth, iheight);

		n++;
		g.setColor(Color.yellow);
		g.fillRect(startx, starty + delta*n, iwidth, iheight);

		n++;
		g.setColor(Color.pink);
		g.fillRect(startx, starty + delta*n, iwidth, iheight);

		n++;
		g.setColor(Color.green);
		g.fillRect(startx, starty + delta*n, iwidth, iheight);

		g.drawString("blue blocks: " + blueBlocks
					+ "\nred blocks" + redBlocks
					+ "\nyellow blocks" + yellowBlocks
					+ "\npink blocks" + pinkBlocks
					+ "\ngreen blocks" + greenBlocks, startx + delta*2, starty);


	}

	//logic for the player
	public void update(GameContainer gc, int delta, Map m){


		//update all of the player's projectiles
		for(Projectile p : projectiles)
			p.update(delta);

		//update flag based on whether the player's movements will cause him to hit something
		boolean updateX = true;
		boolean updateY = true;

		//deceleration
		if(!movingLeft && dx < 0f){
			dx += .2f;
		}
		if(!movingRight && dx > 0f){
			dx -= .2f;
		}
		if(!movingLeft && !movingRight && ((dx < 0 && dx > -.2f) || (dx > 0 && dx < .2f))){
			dx = 0f;
		}
		//handle falling
		boolean fallFlag = true;
		if(inAir)
			dy += .075f;

		//detect the player's collisions with things on the map
		Rectangle prx = new Rectangle(x + dx, y, width, height);
		Rectangle pry = new Rectangle(x, y + dy, width, height);
		//collides with platforms
		for(Rectangle r : m.mapCollision){
			if(prx.intersects(r)){
				updateX = false;
				dx = 0;
			}
			//check if my dy will cause me to hit a platform
			if(pry.intersects(r)){
				//if I am moving down, set falling to false
				if(dy > 0)
					fallFlag = false;
				updateY = false;
				dy = -Helper.GRAVITY;
			}
		}
	
		//collides with platforms
		for(Pattern p : m.patterns){
			for(ColoredBlock b : p.blocks){
				
				Rectangle r = new Rectangle(b.getX() - 2, b.getY() - 2, b.width + 2, b.height + 2);
				if(prx.intersects(r)){
					updateX = false;
					dx = 0;
				}
				//check if my dy will cause me to hit a platform
				if(pry.intersects(r)){
					//if I am moving down, set falling to false
					if(dy > 0)
						fallFlag = false;
					updateY = false;
					dy = -Helper.GRAVITY;
				}
			}
		}
		
		//collides with platforms
		for(int i = 0; i < m.collectableBlocks.size(); i++){
			CollectableBlock p = m.collectableBlocks.get(i);
			Rectangle r = new Rectangle(p.getX() - 2, p.getY() - 2, p.width + 2, p.height + 2);
			if(prx.intersects(r)){
				updateX = false;
				dx = 0;
				
				addBlock(p.getColor());
				m.collectableBlocks.remove(p);
			}
			//check if my dy will cause me to hit a platform
			if(pry.intersects(r)){
				//if I am moving down, set falling to false
				if(dy > 0)
					fallFlag = false;
				updateY = false;
				dy = -Helper.GRAVITY;
			}
		}
		


		//Collision with the collectible blocks, implement this with "to the side" pickup later
		for(CollectableBlock b : m.placedCollectableBlocks){
			Rectangle r = new Rectangle(b.getX() - 2, b.getY() - 2, b.width + 2, b.height + 2);
			if(prx.intersects(r)){
				updateX = false;
				dx = 0;
			}
			//check if my dy will cause me to hit a platform
			if(pry.intersects(r)){
				//if I am moving down, set falling to false
				if(dy > 0)
					fallFlag = false;
				updateY = false;
				dy = -Helper.GRAVITY;
			}
		}
		inAir = fallFlag;

		if(updateX){
			x+=dx*(delta/10);
			notifyPositionChange();
		}
		if(updateY){
			y+=dy*(delta/10);
			notifyPositionChange();
		}
	}

	public void moveLeft(){
		movingLeft = true;
		if(dx > -MAX_SPEED){
			dx -= .2f;
		}
	}

	public void moveRight(){
		movingRight = true;
		if(dx < MAX_SPEED){
			dx += .2f;
		}
	}

	public void jump(){
		if(!inAir){
			dy = (float) -jumpSpeed;
			inAir = true;
		}
	}

	public void shoot(float x, float y){
		Projectile p = new Projectile(this.x + this.width/2, this.y + this.height/2);
		float dx, dy;

		if(x < this.x && y < this.y){
			System.out.println("quadrant 2");
			dx = x - this.x;
			dy = y - this.y;
		}
		else if(x > this.x && y < this.y){
			System.out.println("quadrant 1");
			dx = x - this.x;
			dy = y - this.y;
		}
		else if(x < this.x && y > this.y){
			System.out.println("quadrant 3");
			dx = x - this.x;
			dy = y - this.y;
		}else{
			System.out.println("quadrant 4");
			dx = x - this.x;
			dy = y - this.y;
		}



		float v = (float) Math.sqrt(dx*dx + dy*dy);
		dx /= v;
		dy /= v;
		p.dx = dx;
		p.dy = dy;
		projectiles.add(p);
		System.out.println("Bullet\ndx=" + dx + ", dy=" + dy);
	}

	/*Adds the color block to the player's inventory

		colors supported:
		yellow
		blue
		green
	*/
	public void addBlock(Color c){
		if(c == Color.blue)
			blueBlocks++;
		else if(c == Color.yellow)
				yellowBlocks++;
		else if (c == Color.pink)
				pinkBlocks++;
		else if(c == Color.green)
				greenBlocks++;
		notifyColorChange();
	}

	@Override
	public void notifyColorChange() {
		int[] res = new int[4];
		res[0] = blueBlocks;
		res[1] = greenBlocks;
		res[2] = redBlocks;
		res[3] = pinkBlocks;
		for(Oberserver o : colorObs)
			o.changeColorNotification(res);
		
	}

	@Override
	public void notifyPositionChange() {
		for(Oberserver o : positionObs)
			o.changePositionNotification(x, y);
				
	}
}
