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

import code.enemies.ShootingEnemy;
import code.gamestates.*;
import code.infrastructure.Helper;
import code.infrastructure.Map;
import code.infrastructure.PlayerObserver;
import code.patterns.Pattern;

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
    private float x, y, dx, dy, currentHealth, maxHealth;
    private int width = 40;
    private int height = 48;
    //END

    /////////////////////////////////////
    // player animation and display    //
    /////////////////////////////////////
    private SpriteSheet spriteSheet = null;
    private float tileWidth = .75f;
    private float tileHeight = .75f;
    private int idleRow = 0;
    private int runningRow = 1;
    private int jumpingRow = 2;
    private org.newdawn.slick.Animation walkingLeft, walkingRight, idleLeft, idleRight, startJumpingLeft, startJumpingRight, jumpingLeft, jumpingRight;
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private boolean facingRight = true;
    private boolean inAir = false;
    //END
    
    /////////////////////////////////////
    // player inventory and stats      //
    /////////////////////////////////////
    private int blueBlocks = 0;
    private int redBlocks = 0;
    private int yellowBlocks = 0;
    private int greenBlocks = 0;
    List<Projectile> projectiles = new ArrayList<Projectile>();
    //END
    
    /////////////////////////////////////
    // player movement and jumping     //
    /////////////////////////////////////
    private boolean doubleJump = true;
    private boolean jumpKeyReset = true;
    private float MAX_SPEED = 7.5f*GameplayState.VIEWPORT_RATIO_X;
    private float jumpSpeed = 8.2f;
    //END
    
    //observers for player
    private List<PlayerObserver> observers = new ArrayList<PlayerObserver>();
    
    
    /**
     * Constructor for the Player class, loads the animations, sets the position
     * @param m map for the player to have knowledge of the tileset width and height
     * @param path ?
     * @param x x position of the player
     * @param y
     */
    public Player(Map m, String path, float x, float y){
        this.x = x; this.y = y;
        setCurrentHealth(200);
        setMaxHealth(200);
        notifyHealthChange();
        dx = 0; dy = Helper.GRAVITY;
        int realWidth = (int) (m.mapTileWidth*tileWidth);
        int realHeight= (int) (m.mapTileHeight*tileHeight);
        width = realWidth - 2;
        height = realHeight - 2;
        // TODO if this fails, program should fail
        try {
            spriteSheet = new SpriteSheet(path, width, height);
        } catch (SlickException e) {  }

        // Setup walking animation, and durations
        Image[] walkL = {spriteSheet.getSprite(0, runningRow).getFlippedCopy(true, false).getScaledCopy(realWidth, realHeight), 
                         spriteSheet.getSprite(1, runningRow).getFlippedCopy(true, false).getScaledCopy(realWidth, realHeight),
                         spriteSheet.getSprite(2, runningRow).getFlippedCopy(true, false).getScaledCopy(realWidth, realHeight), 
                         spriteSheet.getSprite(3, runningRow).getFlippedCopy(true, false).getScaledCopy(realWidth, realHeight), 
                         spriteSheet.getSprite(4, runningRow).getFlippedCopy(true, false).getScaledCopy(realWidth, realHeight),
                         spriteSheet.getSprite(5, runningRow).getFlippedCopy(true, false).getScaledCopy(realWidth, realHeight), 
                         spriteSheet.getSprite(6, runningRow).getFlippedCopy(true, false).getScaledCopy(realWidth, realHeight), 
                         spriteSheet.getSprite(7, runningRow).getFlippedCopy(true, false).getScaledCopy(realWidth, realHeight)};

        int[] walkDur = {200,200,200,200,200,200,200,200};
        walkingLeft = new Animation(walkL, walkDur);

        //walking right animation
        Image[] walkR = {spriteSheet.getSprite(0, runningRow).getScaledCopy(realWidth, realHeight), 
                         spriteSheet.getSprite(1, runningRow).getScaledCopy(realWidth, realHeight),
                         spriteSheet.getSprite(2, runningRow).getScaledCopy(realWidth, realHeight), 
                         spriteSheet.getSprite(3, runningRow).getScaledCopy(realWidth, realHeight), 
                         spriteSheet.getSprite(4, runningRow).getScaledCopy(realWidth, realHeight),
                         spriteSheet.getSprite(5, runningRow).getScaledCopy(realWidth, realHeight),
                         spriteSheet.getSprite(6, runningRow).getScaledCopy(realWidth, realHeight),
                         spriteSheet.getSprite(7, runningRow).getScaledCopy(realWidth, realHeight)};
        walkingRight = new Animation(walkR, walkDur);

        //start jumping animation
        Image[] startJumpLeft = {spriteSheet.getSprite(0, jumpingRow).getFlippedCopy(true, false).getScaledCopy(realWidth, realHeight), 
                                 spriteSheet.getSprite(1, jumpingRow).getFlippedCopy(true, false).getScaledCopy(realWidth, realHeight),
                                 spriteSheet.getSprite(2, jumpingRow).getFlippedCopy(true, false).getScaledCopy(realWidth, realHeight), 
                                 spriteSheet.getSprite(3, jumpingRow).getFlippedCopy(true, false).getScaledCopy(realWidth, realHeight)};
        int[] startJumpDur = {200,200,200,200};
        startJumpingLeft = new Animation(startJumpLeft, startJumpDur);

        Image[] startJumpRight = {spriteSheet.getSprite(0, jumpingRow).getFlippedCopy(false, false).getScaledCopy(realWidth, realHeight), 
                                  spriteSheet.getSprite(1, jumpingRow).getFlippedCopy(false, false).getScaledCopy(realWidth, realHeight),
                                  spriteSheet.getSprite(2, jumpingRow).getFlippedCopy(false, false).getScaledCopy(realWidth, realHeight), 
                                  spriteSheet.getSprite(3, jumpingRow).getFlippedCopy(false, false).getScaledCopy(realWidth, realHeight)};
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


        // Setup the idle animation, durations and images
        Image[] idleImgL = {spriteSheet.getSprite(0, idleRow).getFlippedCopy(true, false).getScaledCopy(realWidth, realHeight), 
                            spriteSheet.getSprite(1, idleRow).getFlippedCopy(true, false).getScaledCopy(realWidth, realHeight),
                            spriteSheet.getSprite(2, idleRow).getFlippedCopy(true, false).getScaledCopy(realWidth, realHeight), 
                            spriteSheet.getSprite(3, idleRow).getFlippedCopy(true, false).getScaledCopy(realWidth, realHeight)};
        int[] idleDur = {300,300,300,300};
        idleLeft = new Animation(idleImgL, idleDur);

        Image[] idleImg = {spriteSheet.getSprite(0, idleRow).getScaledCopy(realWidth, realHeight), spriteSheet.getSprite(1, idleRow).getScaledCopy(realWidth, realHeight),
                           spriteSheet.getSprite(2, idleRow).getScaledCopy(realWidth, realHeight), spriteSheet.getSprite(3, idleRow).getScaledCopy(realWidth, realHeight)};
        idleRight = new Animation(idleImg, idleDur);

        // Setup the pet dragon
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
        // Draw the pet dragon
        petDragon.draw(g, shiftX, shiftY);
        
        // Draw all the player's projectiles
        for(Projectile p : projectiles)
            p.draw(g, shiftX, shiftY);
        //jumpingRight.draw(x - shiftX, y - shiftY);
        // Differentiate between when the player starts jumping + in the air
        if(startJump){
            // Different animations for if the player is facing left/right
            if(isFacingRight()){
                startJumpingRight.draw(x-shiftX, y-shiftY);
                startJumpingLeft.setCurrentFrame(startJumpingRight.getFrame());
            }else{
                startJumpingLeft.draw(x-shiftX, y-shiftY);
                startJumpingRight.setCurrentFrame(startJumpingRight.getFrame());
            }
            // Stop the start jumping animation and start the inAir animation
            if(startJumpingLeft.getFrame() == startJumpingLeft.getFrameCount() - 1){
                startJump = false;
            }
        // player has already started jumping, and now is in the air
        }else if(inAir){
            if(isFacingRight())
                jumpingRight.draw(x-shiftX, y-shiftY);
            else
                jumpingLeft.draw(x-shiftX, y-shiftY);
        // Player isn't in the air, so check if he's moving right
        }else if(isMovingRight())
            walkingRight.draw(x - shiftX, y - shiftY);
        // Not moving right, check moving left
        else if(isMovingLeft())
            walkingLeft.draw(x - shiftX, y - shiftY);
        // Not walking at all, must be idle
        else{
            if(!isFacingRight())
                idleLeft.draw(x-shiftX, y-shiftY);
            else
                idleRight.draw(x-shiftX, y-shiftY);
        }

        // TODO remove this, it's for debugging only
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
        // Heads up for when the player is starting the double jump
        if(!gc.getInput().isKeyDown(Input.KEY_UP))
            jumpKeyReset = true;

        // Don't forget to update the pet!
        petDragon.update(this);

        //update all of the player's projectiles
        for(Projectile p : projectiles)
            p.update(delta);

        // deceleration left, right, and the edge case where the player
        // has a low enough acceleration where we just set it to 0
        if(!isMovingLeft() && dx < 0f){
            dx += .2f;
        }
        if(!isMovingRight() && dx > 0f){
            dx -= .2f;
        }
        if(!isMovingLeft() && !isMovingRight() && ((dx < 0 && dx > -.2f) || (dx > 0 && dx < .2f))){
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
            for(int j = 0; j < se.getProjectiles().size(); j++){
                Projectile p = se.getProjectiles().get(j);
                Rectangle r = new Rectangle(p.getX(), p.getY(), p.width, p.height);
	    		
                if(prx.intersects(r) || pry.intersects(r)){
                    collideWith(p);
                    se.getProjectiles().remove(j);
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
        this.setCurrentHealth(this.getCurrentHealth() - 10);
        notifyHealthChange();
    }
	
    /**
     * Method to handle starting, or accelerating leftwards movement
     */
    public void moveLeft(){
        setFacingRight(false);
        setMovingLeft(true);
        if(dx > -MAX_SPEED){
            dx -= .2f;
        }
    }
	
    /**
     * Method to handle starting, or accelerating rightwards movement
     */
    public void moveRight(){
        setFacingRight(true);
        setMovingRight(true);
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
    public void shoot(){
        float[] dir = new float[2];
        float projectileX, projectileY, destinationX, destinationY;
        if(!isFacingRight()){
            destinationY = y + height/2;
            destinationX = x - 1;
            projectileX = x;
            projectileY = y + height/2;
        }else{
            destinationY = y + height/2;
            destinationX = x + width + 1;
            projectileX = x + width;
            projectileY = y + height/2;
        }
        dir[1]	 = 0;
		
        Projectile p = new Projectile(projectileX, projectileY, destinationX, destinationY);
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
            setBlueBlocks(getBlueBlocks() + 1);
        else if(c.equals("yellow"))
            setYellowBlocks(getYellowBlocks() + 1);
        else if (c.equals("red"))
            setRedBlocks(getRedBlocks() + 1);
        else if(c.equals("green"))
            setGreenBlocks(getGreenBlocks() + 1);
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
	
    ////////////////////////////////
    //	Getters/Setters           //
    ////////////////////////////////
    public float getX(){
        return x;
    }
    public void setX(float x){
        this.x = x;
    }
    public float getY(){
        return y;
    }
    public void setY(float y){
        this.y = y;
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public int getBlueBlocks() {
        return blueBlocks;
    }
    public void setBlueBlocks(int blueBlocks) {
        this.blueBlocks = blueBlocks;
    }
    public int getRedBlocks() {
        return redBlocks;
    }
    public void setRedBlocks(int redBlocks) {
        this.redBlocks = redBlocks;
    }
    public int getYellowBlocks() {
        return yellowBlocks;
    }
    public void setYellowBlocks(int yellowBlocks) {
        this.yellowBlocks = yellowBlocks;
    }
    public int getGreenBlocks() {
        return greenBlocks;
    }
    public void setGreenBlocks(int greenBlocks) {
        this.greenBlocks = greenBlocks;
    }
    public List<Projectile> getProjectiles(){
        return projectiles;
    }
    public float getMaxHealth() {
        return maxHealth;
    }
    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }
    public float getCurrentHealth() {
        return currentHealth;
    }
    public void setCurrentHealth(float currentHealth) {
        this.currentHealth = currentHealth;
    }
    public boolean isFacingRight() {
        return facingRight;
    }
    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }
    public boolean isMovingLeft() {
        return movingLeft;
    }
    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }
    public boolean isMovingRight() {
        return movingRight;
    }
    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }
}
