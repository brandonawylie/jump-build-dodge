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
	int width = 40;
	int height = 48;
	int idleRow = 0;
	int runningRow = 1;
	int jumpingRow = 2;
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

	org.newdawn.slick.Animation walkingLeft, walkingRight, idle, startJumping, jumpingLeft, jumpingRight, landingLeft, landingRight;
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

	SpriteSheet spriteSheet = null;
	int n = 0;
	public Player(String path, float x, float y){
		this.x = x; this.y = y;
		dx = 0; dy = Helper.GRAVITY;
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

		//walking left animation
		Image[] walkL = {spriteSheet.getSprite(0, runningRow).getFlippedCopy(true, false).getSubImage(0, 0, width, height), spriteSheet.getSprite(1, runningRow).getFlippedCopy(true, false).getSubImage(0, 0, width, height),
			spriteSheet.getSprite(2, runningRow).getFlippedCopy(true, false).getSubImage(0, 0, width, height), spriteSheet.getSprite(3, runningRow).getFlippedCopy(true, false).getSubImage(0, 0, width, height), spriteSheet.getSprite(4, runningRow).getFlippedCopy(true, false).getSubImage(0, 0, width, height),
			spriteSheet.getSprite(5, runningRow).getFlippedCopy(true, false).getSubImage(0, 0, width, height), spriteSheet.getSprite(6, runningRow).getFlippedCopy(true, false).getSubImage(0,0,width, height),spriteSheet.getSprite(7, runningRow).getFlippedCopy(true, false).getSubImage(0, 0, width, height)};
		int[] walkDur = {200,200,200,200,200,200,200,200};
		walkingLeft = new Animation(walkL, walkDur);

		//walking right animation
		Image[] walkR = {spriteSheet.getSprite(0, runningRow).getSubImage(0, 0, width, height), spriteSheet.getSprite(1, runningRow).getSubImage(0, 0, width, height),
			spriteSheet.getSprite(2, runningRow).getSubImage(0, 0, width, height), spriteSheet.getSprite(3, runningRow).getSubImage(0, 0, width, height), spriteSheet.getSprite(4, runningRow).getSubImage(0, 0, width, height),
			spriteSheet.getSprite(5, runningRow).getSubImage(0, 0, width, height),spriteSheet.getSprite(6, runningRow).getSubImage(0, 0, width, height),spriteSheet.getSprite(7, runningRow).getSubImage(0, 0, width, height)};
		walkingRight = new Animation(walkR, walkDur);

		//start jumping animation
		Image[] startJump = {spriteSheet.getSprite(0, jumpingRow).getFlippedCopy(true, false).getSubImage(0, 0, width, height), spriteSheet.getSprite(1, jumpingRow).getFlippedCopy(true, false).getSubImage(0, 0, width, height),
				spriteSheet.getSprite(2, jumpingRow).getFlippedCopy(true, false).getSubImage(0, 0, width, height), spriteSheet.getSprite(3, jumpingRow).getFlippedCopy(true, false).getSubImage(0, 0, width, height)};
		int[] startJumpDur = {200,200,200,200};
		startJumping = new Animation(startJump, startJumpDur);

		//jumping right animation
		Image[] jumpR = {spriteSheet.getSprite(4, jumpingRow).getFlippedCopy(false, false).getSubImage(0, 0, width, height),
				spriteSheet.getSprite(5, jumpingRow).getFlippedCopy(false, false).getSubImage(0, 0, width, height)};
		int[] jumpDur = {200,200};
		jumpingRight = new Animation(jumpR, jumpDur);

		//jumping left animation
		Image[] jumpL = {spriteSheet.getSprite(4, jumpingRow).getFlippedCopy(true, false).getSubImage(0, 0, width, height),
				spriteSheet.getSprite(5, jumpingRow).getFlippedCopy(true, false).getSubImage(0, 0, width, height)};
		jumpingLeft = new Animation(jumpL, jumpDur);

		Image[] idleImg = {spriteSheet.getSprite(0, idleRow), spriteSheet.getSprite(1, idleRow),
				spriteSheet.getSprite(2, idleRow), spriteSheet.getSprite(3, idleRow)};
		int[] idleDur = {300,300,300,300};
		idle = new Animation(idleImg, idleDur);
		//height = 30;
	}
	long timer = 0;
	boolean startJump = false;
	int startJumpFrames = 0;
	public void render(Graphics g, float shiftX, float shiftY){

		for(Projectile p : projectiles)
			p.draw(g, shiftX, shiftY);
		//jumpingRight.draw(x - shiftX, y - shiftY);
		if(startJump){
		  System.out.println("start jumping");
		  startJumping.draw(x - shiftX, y - shiftY);
		  if(startJumping.getFrame() == startJumping.getFrameCount() - 1){
		      startJump = false;
		  }
		}else if(inAir){
		    System.out.println("in the air");
		    if(movingRight)
			jumpingRight.draw(x-shiftX, y-shiftY);
		    else
			jumpingLeft.draw(x-shiftX, y-shiftY);
		}else if(movingRight)
		    	walkingRight.draw(x - shiftX, y - shiftY);
		else if(movingLeft)
		    	walkingLeft.draw(x - shiftX, y - shiftY);
		else
		    idle.draw(x-shiftX, y-shiftY);

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

		if(inAir)
		    dy += Helper.GRAVITY;

//		if(Math.abs(dy) < Helper.GRAVITY)
//		    dy = Helper.GRAVITY;
		boolean isInAir = true;
		boolean updatex = true;
		boolean updatey = true;
		Rectangle prx = new Rectangle(x + dx, y, width, height);
		Rectangle pry = new Rectangle(x, y + dy, width, height);
		for(Rectangle r : m.mapCollision){
		    if(r.intersects(prx)){
			updatex = false;
			if(dx > 0){
			    x += ((int)r.getX() - (x + width)) - 1;
			}else{
			    x -= ((int)r.getX() - (x + width)) - 1;
			}
		    }
		    if(r.intersects(pry)){
			updatey = false;
			dy = Helper.GRAVITY;
			if(dy > 0){
			    inAir = false;
			    y += ((int)r.getY() - (y + height)) - 1;
			}else{
			    y -= ((int)r.getY() - (y + height)) - 1;
			}
		    }
		}
		if(updatex){
		    x += dx;
		}

		if(updatey){
		    y += dy;
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
			startJump = true;
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
