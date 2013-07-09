package code;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;


public class Player {
	float x, y, dx, dy, speed, ratioX, ratioY;
	float width = 10;
	float height = 10;
	float jumpSpeed = 4f*GameplayState.VIEWPORT_RATIO_Y;
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
	
	
	Animation walkingAnimation, idleAnimation;
	Image spriteSheet;
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

	int n = 0;
	public Player(Image i, float x, float y){
		spriteSheet = i;
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
	
		idleAnimation = new Animation(0,0,56,80,spriteSheet);
		walkingAnimation = new Animation(0, 80, 56, 80, spriteSheet);
		width = idleAnimation.width;
		height = idleAnimation.height;
		//height = 30;
	}

	public void render(Graphics g, float shiftX, float shiftY){
		for(Projectile p : projectiles)
			p.draw(g, shiftX, shiftY);

		//g.drawImage(walkingAnimation.images[0].getFlippedCopy(true, false), x - shiftX, y - shiftY);
		if(movingLeft){
			walkingAnimation.draw(g, x - shiftX, y - shiftY, true);
		}else if(movingRight){
			walkingAnimation.draw(g, x - shiftX, y - shiftY);
		}else
			idleAnimation.draw(g, x - shiftX, y - shiftY);
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
		walkingAnimation.update(delta);

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
		for(Platform p : m.platforms){
			Rectangle r = new Rectangle(p.getX() - 2, p.getY() - 2, p.width + 2, p.height + 2);
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
				
				if(p.getColor().equals(Color.red)){
					redBlocks++;
				}else if(p.getColor().equals(Color.green)){
					greenBlocks++;
				}else if(p.getColor().equals(Color.blue)){
					blueBlocks++;
				}else if(p.getColor().equals(Color.pink)){
					pinkBlocks++;
				}
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

		if(updateX)
			x+=dx*(delta/10);
		if(updateY)
			y+=dy*(delta/10);
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
		//if(c == Color.blue)
			blueBlocks++;
		//else if(c == Color.yellow)
				yellowBlocks++;
		//else if (c == Color.pink)
				pinkBlocks++;
		//else if(c == Color.green)
				greenBlocks++;
	}
}
