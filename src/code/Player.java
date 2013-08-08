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
    	Dragon petDragon;

	float x, y, dx, dy, speed, ratioX, ratioY;
	int width = 40;
	int height = 48;

	//animation info
	float tileWidth = .75f;
	float tileHeight = .75f;
	int idleRow = 0;
	int runningRow = 1;
	int jumpingRow = 2;
	org.newdawn.slick.Animation walkingLeft, walkingRight, idleLeft, idleRight, startJumpingLeft, startJumpingRight, jumpingLeft, jumpingRight, landingLeft, landingRight;
	int jumpAnimationFrames = 3;
	int fallAnimationFrames = 4;

	float jumpSpeed = 5;
	boolean movingLeft = false;
	boolean movingRight = false;
	boolean facingRight = true;
	boolean inAir = false;
	long gravityTimer;

	int blueBlocks = 0;
	int redBlocks = 0;
	int yellowBlocks = 0;
	int greenBlocks = 0;

	public float MAX_SPEED = 5*GameplayState.VIEWPORT_RATIO_X;


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
	public Player(Map m, String path, float x, float y){
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
			spriteSheet = new SpriteSheet(path, width, height);
		} catch (SlickException e) { }
		int realWidth = (int) (m.mapTileWidth*tileWidth);
		int realHeight= (int) (m.mapTileHeight*tileHeight);
		width = realWidth - 2;
		height = realHeight - 2;
		Image[] walkL = {spriteSheet.getSprite(0, runningRow).getFlippedCopy(true, false).getScaledCopy(realWidth, realHeight), spriteSheet.getSprite(1, runningRow).getFlippedCopy(true, false).getScaledCopy(realWidth, realHeight),
				spriteSheet.getSprite(2, runningRow).getFlippedCopy(true, false).getScaledCopy(realWidth, realHeight), spriteSheet.getSprite(3, runningRow).getFlippedCopy(true, false).getScaledCopy(realWidth, realHeight), spriteSheet.getSprite(4, runningRow).getFlippedCopy(true, false).getScaledCopy(realWidth, realHeight),
				spriteSheet.getSprite(5, runningRow).getFlippedCopy(true, false).getScaledCopy(realWidth, realHeight), spriteSheet.getSprite(6, runningRow).getFlippedCopy(true, false).getScaledCopy(realWidth, realHeight), spriteSheet.getSprite(7, runningRow).getFlippedCopy(true, false).getScaledCopy(realWidth, realHeight)};

		int[] walkDur = {200,200,200,200,200,200,200,200};
		walkingLeft = new Animation(walkL, walkDur);

		//walking right animation
		Image[] walkR = {spriteSheet.getSprite(0, runningRow).getScaledCopy(realWidth, realHeight), spriteSheet.getSprite(1, runningRow).getScaledCopy(realWidth, realHeight),
			spriteSheet.getSprite(2, runningRow).getScaledCopy(realWidth, realHeight), spriteSheet.getSprite(3, runningRow).getScaledCopy(realWidth, realHeight), spriteSheet.getSprite(4, runningRow).getScaledCopy(realWidth, realHeight),
			spriteSheet.getSprite(5, runningRow).getScaledCopy(realWidth, realHeight),spriteSheet.getSprite(6, runningRow).getScaledCopy(realWidth, realHeight),spriteSheet.getSprite(7, runningRow).getScaledCopy(realWidth, realHeight)};
		walkingRight = new Animation(walkR, walkDur);

		//start jumping animation
		Image[] startJumpLeft = {spriteSheet.getSprite(0, jumpingRow).getFlippedCopy(true, false).getScaledCopy(realWidth, realHeight), spriteSheet.getSprite(1, jumpingRow).getFlippedCopy(true, false).getScaledCopy(realWidth, realHeight),
				spriteSheet.getSprite(2, jumpingRow).getFlippedCopy(true, false).getScaledCopy(realWidth, realHeight), spriteSheet.getSprite(3, jumpingRow).getFlippedCopy(true, false).getScaledCopy(realWidth, realHeight)};
		int[] startJumpDur = {200,200,200,200};
		startJumpingLeft = new Animation(startJumpLeft, startJumpDur);

		Image[] startJumpRight = {spriteSheet.getSprite(0, jumpingRow).getFlippedCopy(false, false).getScaledCopy(realWidth, realHeight), spriteSheet.getSprite(1, jumpingRow).getFlippedCopy(false, false).getScaledCopy(realWidth, realHeight),
			spriteSheet.getSprite(2, jumpingRow).getFlippedCopy(false, false).getScaledCopy(realWidth, realHeight), spriteSheet.getSprite(3, jumpingRow).getFlippedCopy(false, false).getScaledCopy(realWidth, realHeight)};
		startJumpingRight = new Animation(startJumpRight, startJumpDur);

		//jumping right animation
		Image[] jumpR = {spriteSheet.getSprite(4, jumpingRow).getFlippedCopy(false, false).getScaledCopy(realWidth, realHeight),
				spriteSheet.getSprite(5, jumpingRow).getFlippedCopy(false, false).getScaledCopy(realWidth, realHeight)};
		int[] jumpDur = {200,200};
		jumpingRight = new Animation(jumpR, jumpDur);

		//jumping left animation
		Image[] jumpL = {spriteSheet.getSprite(4, jumpingRow).getFlippedCopy(true, false).getScaledCopy(realWidth, realHeight),
				spriteSheet.getSprite(5, jumpingRow).getFlippedCopy(true, false).getScaledCopy(realWidth, realHeight)};
		jumpingLeft = new Animation(jumpL, jumpDur);



		Image[] idleImgL = {spriteSheet.getSprite(0, idleRow).getFlippedCopy(true, false).getScaledCopy(realWidth, realHeight), spriteSheet.getSprite(1, idleRow).getFlippedCopy(true, false).getScaledCopy(realWidth, realHeight),
			spriteSheet.getSprite(2, idleRow).getFlippedCopy(true, false).getScaledCopy(realWidth, realHeight), spriteSheet.getSprite(3, idleRow).getFlippedCopy(true, false).getScaledCopy(realWidth, realHeight)};
        	int[] idleDur = {300,300,300,300};
		idleLeft = new Animation(idleImgL, idleDur);

		Image[] idleImg = {spriteSheet.getSprite(0, idleRow).getScaledCopy(realWidth, realHeight), spriteSheet.getSprite(1, idleRow).getScaledCopy(realWidth, realHeight),
			spriteSheet.getSprite(2, idleRow).getScaledCopy(realWidth, realHeight), spriteSheet.getSprite(3, idleRow).getScaledCopy(realWidth, realHeight)};
		idleRight = new Animation(idleImg, idleDur);

		//height = 30;

		petDragon = new Dragon("assets/characters/dragon_red.png", (int)(x + width), (int)y);
	}
	long timer = 0;
	boolean startJump = false;
	int startJumpFrames = 0;
	public void render(Graphics g, float shiftX, float shiftY){
	    petDragon.draw(g, shiftX, shiftY);
		for(Projectile p : projectiles)
			p.draw(g, shiftX, shiftY);
		//jumpingRight.draw(x - shiftX, y - shiftY);
		if(startJump){
		  if(facingRight){
		      startJumpingRight.draw(x-shiftX, y-shiftY);
		      startJumpingLeft.setCurrentFrame(startJumpingRight.getFrame());
		  }else{
		      startJumpingLeft.draw(x-shiftX, y-shiftY);
		      startJumpingRight.setCurrentFrame(startJumpingRight.getFrame());
		  }
		  if(startJumpingLeft.getFrame() == startJumpingLeft.getFrameCount() - 1){
		      startJump = false;
		  }
		}else if(inAir){
		    if(facingRight)
			jumpingRight.draw(x-shiftX, y-shiftY);
		    else
			jumpingLeft.draw(x-shiftX, y-shiftY);
		}else if(movingRight)
		    	walkingRight.draw(x - shiftX, y - shiftY);
		else if(movingLeft)
		    	walkingLeft.draw(x - shiftX, y - shiftY);
		else{
		    if(!facingRight)
			idleLeft.draw(x-shiftX, y-shiftY);
		    else
			idleRight.draw(x-shiftX, y-shiftY);
		}
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

		if(prx != null && pry != null){
			g.setColor(Color.orange);
			g.drawRect(prx.getX() - shiftX, prx.getY() - shiftY, prx.getWidth(), prx.getHeight());
			g.setColor(Color.pink);
			g.drawRect(pry.getX() - shiftX, pry.getY() - shiftY, pry.getWidth(), pry.getHeight());
		}
	}
	Rectangle prx;
    Rectangle pry;

	//logic for the player
	public void update(GameContainer gc, int delta, Map m){
	    petDragon.update(this);
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



		boolean inAirCheck = true;
		boolean updatex = true;
		boolean updatey= true;
		Rectangle prx = new Rectangle(x + dx, y, width, height);
		Rectangle pry = new Rectangle(x, y + dy, width, height + 1);
		System.out.println("===========================");
	    for(Rectangle r : m.mapCollision){
	    	if(prx.intersects(r)){
	    		updatex = false;
	    		//dx = 0;
	    		System.out.println("prx is intersecting!, dx = " + dx + ", dy = " + dy + ", inAir = " + inAir);
	    	}

	    	if(pry.intersects(r)){
	    	    updatey = false;
	    	    inAirCheck = false;
	    		//dy = 0;
	    		System.out.println("pry is intersecting!, y = " + (y+height) + ", ry = " + r.getY()+ ", inAir = " + inAir);
	    	}
	    }

	    for(int i = 0; i < m.collectableBlocks.size(); i++){
		CollectableBlock b = m.collectableBlocks.get(i);
		Rectangle r = new Rectangle(b.x, b.y, b.width, b.height);

		if(prx.intersects(r)){
	    		addBlock(b.color);
	    		m.collectableBlocks.remove(b);
	    		continue;
	    	}

	    	if(pry.intersects(r)){
	    	    updatey = false;
	    	    inAirCheck = false;
	    		//dy = 0;
	    		System.out.println("pry is intersecting!, y = " + (y+height) + ", ry = " + r.getY()+ ", inAir = " + inAir);
	    	}
	    }

	    for(int i = 0; i < m.placedCollectableBlocks.size(); i++){
		CollectableBlock b = m.placedCollectableBlocks.get(i);
		Rectangle r = new Rectangle(b.x, b.y, b.width, b.height);

		if(prx.intersects(r)){
	    		addBlock(b.color);
	    		m.collectableBlocks.remove(b);
	    		continue;
	    	}

	    	if(pry.intersects(r)){
	    	    updatey = false;
	    	    inAirCheck = false;
	    		//dy = 0;
	    		System.out.println("pry is intersecting!, y = " + (y+height) + ", ry = " + r.getY()+ ", inAir = " + inAir);
	    	}
	    }
	    inAir = inAirCheck;

		if(inAir)
		    dy += Helper.GRAVITY;

		if(updatex){
		    x+=dx;
		    notifyPositionChange();
		}
	    if(updatey){
	    	y+=dy;
	    	notifyPositionChange();
	    }

	}

	public void moveLeft(){
	    facingRight = false;
		movingLeft = true;
		if(dx > -MAX_SPEED){
			dx -= .2f;
		}
	}

	public void moveRight(){
	    facingRight = true;
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
	public void addBlock(String c){
		if(c.equals("blue"))
			blueBlocks++;
		else if(c.equals("yellow"))
				yellowBlocks++;
		else if (c.equals("red"))
				redBlocks++;
		else if(c.equals("green"))
				greenBlocks++;
		notifyColorChange();
	}

	@Override
	public void notifyColorChange() {
		int[] res = new int[4];
		res[0] = blueBlocks;
		res[1] = greenBlocks;
		res[2] = redBlocks;
		res[3] = redBlocks;
		for(Oberserver o : colorObs)
			o.changeColorNotification(res);

	}

	@Override
	public void notifyPositionChange() {
		for(Oberserver o : positionObs)
			o.changePositionNotification(x, y);

	}
}
