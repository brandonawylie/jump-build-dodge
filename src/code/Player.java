package code;
import java.util.ArrayList;
import java.util.List;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.Animation;

import code.gamestates.*;

/**
 * Player class, this class handles both the display, updating and interactions between various game objects
 * @author brandon
 *
 */
public class Player{
    Dragon petDragon;//^_^


	//////////////////////////////////////////////////
	// player position, speed and width/height      //
	//////////////////////////////////////////////////
	public float x, y, dx, dy, currentHealth, maxHealth;
	int width = 40;
	int height = 48;
	//END

	/////////////////////////////////////
	// player animation and display    //
	/////////////////////////////////////
	SpriteSheet spriteSheet = null;
	float tileWidth = .75f;
	float tileHeight = .75f;
	int idleRow = 0;
	int runningRow = 1;
	int jumpingRow = 2;
	org.newdawn.slick.Animation walkingLeft, walkingRight, idleLeft, idleRight, startJumpingLeft, startJumpingRight, jumpingLeft, jumpingRight, landingLeft, landingRight;
	int jumpAnimationFrames = 3;
	int fallAnimationFrames = 4;
	public boolean movingLeft = false;
	public boolean movingRight = false;
	boolean facingRight = true;
	boolean inAir = false;
	//END
	
	/////////////////////////////////////
	// player inventory and stats      //
	/////////////////////////////////////
	int blueBlocks = 0;
	int redBlocks = 0;
	int yellowBlocks = 0;
	int greenBlocks = 0;
	int health = 10000;
	List<Projectile> projectiles = new ArrayList<Projectile>();
	//END
	
	/////////////////////////////////////
	// player movement and jumping     //
	/////////////////////////////////////
	boolean doubleJump = true;
	boolean jumpKeyReset = true;
	public float MAX_SPEED = 7.5f*GameplayState.VIEWPORT_RATIO_X;
	float jumpSpeed = 7.5f;
	long gravityTimer;
	//END

	//observers for player
	List<PlayerObserver> observers = new ArrayList<PlayerObserver>();

	
	/**
	 * Constructor for the Player class, loads the animations, sets the position
	 * @param m map for the player to have knowledge of the tileset width and height
	 * @param path ?
	 * @param x x position of the player
	 * @param y
	 */
	public Player(Map m, String path, float x, float y){
		this.x = x; this.y = y;
		currentHealth = 200;
		maxHealth = 200;
		notifyHealthChange();
		dx = 0; dy = Helper.GRAVITY;

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
	
	
	/**
	 * draw method for this player, draws the player's animations and projectiles
	 * @param g graphics object for Player to draw to
	 * @param shiftX the current x shift of viewport
	 * @param shiftY the current y shift of viewport
	 */
	//variables that live just for this  method
	long timer = 0;
	boolean startJump = false;
	int startJumpFrames = 0;
	public void draw(Graphics g, float shiftX, float shiftY){
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


		if(prx != null && pry != null){
			g.setColor(Color.orange);
			g.drawRect(prx.getX() - shiftX, prx.getY() - shiftY, prx.getWidth(), prx.getHeight());
			g.setColor(Color.pink);
			g.drawRect(pry.getX() - shiftX, pry.getY() - shiftY, pry.getWidth(), pry.getHeight());
		}
	}

	/*
	 * Method: update
	 * Handles all the collision detection between the map and the player as well as current velocity.
	 */
	
	/**
	 * Handles all the Player logic, running, jumping and shooting
	 * @param gc GameContainer for this game
	 * @param delta the delta since the last update
	 * @param m map for the player's interactions with the map
	 */
	//variables that live only for update method
	Rectangle prx;//draws the player trajectory 
	Rectangle pry;
	public void update(GameContainer gc, int delta, Map m){
	    if(!gc.getInput().isKeyDown(Input.KEY_UP))
	    	jumpKeyReset = true;

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
		//should player update x or y, we shall see
		boolean updatex = true;
		boolean updatey= true;
		//where the player will be
		prx = new Rectangle(x + dx, y, width, height);
		pry = new Rectangle(x, y + dy, width, height + 1);

		//start collision with static map collision rects
	    for(Rectangle r : m.mapCollision){
	    	if(prx.intersects(r)){
	    		updatex = false;
	    	}

	    	if(pry.intersects(r)){
	    	    updatey = false;
	    	    inAirCheck = false;
	    	    doubleJump = true;
	    	    dy = 0;
	    	}
	    }
	    //end collision with static map collision rects

	    //start collision with collectable blocks, both placed and those on the map by default
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
	    	}
	    }
	    
	    for(int i = 0; i < m.patterns.size(); i++){
	    	Pattern p = m.patterns.get(i);
	    	for(int j = 0; j < p.blocks.size(); j++){
				CollectableBlock b = p.blocks.get(j);
				Rectangle r = new Rectangle(b.x, b.y, b.width, b.height);
	
				if(prx.intersects(r)){
			    		updatex = false;
			    	}
	
			    	if(pry.intersects(r)){
			    	    updatey = false;
			    	    inAirCheck = false;
			    	}
		    }
	    }
	    
	    for(int i = 0; i < m.shootingEnemies.size(); i++){
	    	ShootingEnemy se = m.shootingEnemies.get(i);
	    	for(int j = 0; j < se.projectiles.size(); j++){
	    		Projectile p = se.projectiles.get(j);
	    		Rectangle r = new Rectangle(p.x, p.y, p.width, p.height);
	    		
	    		if(prx.intersects(r) || pry.intersects(r)){
	    			collideWith(p);
	    			se.projectiles.remove(j);
	    		}
	    	}
	    }
	    
	    
	    //end collision with collectable blocks

	    inAir = inAirCheck;

		if(inAir)
		    dy += Helper.GRAVITY*delta/25;

		if(updatex){
		    x+=dx;
		    notifyPositionChange();
		}
	    if(updatey){
	    	y+=dy;
	    	notifyPositionChange();
	    }

	}
	
	public void collideWith(Projectile p){
		this.currentHealth -= 10;
		notifyHealthChange();
	}
	
	/**
	 * Method to handle starting, or accelerating leftwards movement
	 */
	public void moveLeft(){
	    facingRight = false;
		movingLeft = true;
		if(dx > -MAX_SPEED){
			dx -= .2f;
		}
	}
	
	/**
	 * Method to handle starting, or accelerating rightwards movement
	 */
	public void moveRight(){
	    facingRight = true;
		movingRight = true;
		if(dx < MAX_SPEED){
			dx += .2f;
		}
	}

	/**
	 * Method to handle jumping, and doublejumping
	 */
	public void jump(){
		if((!inAir || doubleJump) && jumpKeyReset){
		    jumpKeyReset = false;
		    if(inAir)
		    	doubleJump = false;
			dy = (float) -jumpSpeed;
			inAir = true;
			startJump = true;
		}
	}

	/**
	 * Method to handle shooting
	 * @param x the dx of the bullet
	 * @param y the dy of the bullet
	 */
	public void shoot(float x, float y){
		Projectile p = new Projectile(this.x + this.width/2, this.y + this.height/2, x, y);
		projectiles.add(p);
	}

	/**
	 * Adds the color block to the player's inventory
		colors supported:
		red, yellow, blue, green
	 * @param c string of the color
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
		notifyInventoryChange();
	}
	
	/**
	 * Add an observer to the player
	 * @param observer Object who wants to view the change of the player
	 */
	public void addObserver(PlayerObserver observer){
		observer.playerHealthChanged(this);
		observer.playerInventoryChanged(this);
		observer.playerPostitionChanged(this);
		observers.add(observer);
	}
	
	/**
	 * Notify the observers of a position change
	 */
	public void notifyPositionChange(){
		for(PlayerObserver p : observers)
			p.playerPostitionChanged(this);
	}
	
	/**
	 * Notify the observers of a inventory change
	 */
	public void notifyInventoryChange(){
		for(PlayerObserver p : observers)
			p.playerInventoryChanged(this);
	}
	
	/**
	 * Notify the observers of a health change
	 */
	public void notifyHealthChange(){
		for(PlayerObserver p : observers)
			p.playerHealthChanged(this);
	}
}
