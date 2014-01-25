package code.gamestates;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import code.CollectableBlock;
import code.Player;
import code.infrastructure.Map;
import code.uielements.HUD;

public class GameplayState extends BasicGameState{
    public static int VIEWPORT_WIDTH = 1280;
    public static int VIEWPORT_HEIGHT = 720;
    public static int VIEWPORT_X, VIEWPORT_Y;
    public static int VIEWPORT_RATIO_X, VIEWPORT_RATIO_Y;
    public static int ID = 1;
	public boolean paused = false;

	Player player;
	long lastClickTime = 0;
	Map map;
	HUD hud;
	int mouseX = 0; int mouseY = 0;
	public GameplayState(int id){
		GameplayState.ID = id;
	}

	public int getID() {
		return ID;
	}

	/**
	 * This method initiates the GameState, creating the map and initializing all the objects
	 *
	 */
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {	
		//initialize the viewport & associated ratios
		VIEWPORT_RATIO_X = PlatformerGame.WIDTH/VIEWPORT_WIDTH;
		VIEWPORT_RATIO_Y = PlatformerGame.HEIGHT/VIEWPORT_HEIGHT;

		//Initialize the map, to be loaded in the next line
		map = new Map();
		try {
			map.loadMap(getClass().getClassLoader().getResource("assets/level3.tmx").getPath());
		}catch (Exception e) {
			e.printStackTrace();
		}
		player = new Player(map, "assets/player.png", 500, 100);
		player.setX(map.playerX);
		player.setY(map.playerY);

		hud = new HUD();

		player.addObserver(hud);
		player.addObserver(map.pManager);
		Display.setVSyncEnabled(true);
	}

	/**
	 * This method draws the game state.
	 *
	 */
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		map.draw(g, VIEWPORT_X, VIEWPORT_Y);
		player.draw(g, VIEWPORT_X, VIEWPORT_Y);
		hud.draw(g);		
	}

	/**
	 * This method contains all the logic for the game state.
	 *
	 */
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		
		if(gc.getInput().isKeyPressed(Input.KEY_ESCAPE)){
			sbg.enterState(PauseState.ID, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
		}
		if(gc.getInput().isKeyPressed(Input.KEY_F1))
			gc.exit();
		//update game elements

		map.update(delta, player);
		player.update(gc, delta, map);


		//adjust the vieaaaaaaort to center around the player. everything will obey this shift.
		VIEWPORT_X = (int) (player.getX() - PlatformerGame.WIDTH/2);
		VIEWPORT_Y = (int) (player.getY() - PlatformerGame.HEIGHT/2);
		if(VIEWPORT_X < 0)
			VIEWPORT_X = 0;
		if(VIEWPORT_Y < 0)
			VIEWPORT_Y = 0;

		//translate click events with the viewport coordinates
		int x = gc.getInput().getMouseX() + VIEWPORT_X;
		int y = gc.getInput().getMouseY() + VIEWPORT_Y;

		//check for input
		//up
		if(gc.getInput().isKeyDown(Input.KEY_UP)){
			player.jump();
		}else{

		//down
		}if(gc.getInput().isKeyDown(Input.KEY_DOWN)){

		}else{

		//left
		}if(gc.getInput().isKeyDown(Input.KEY_LEFT)){
			player.moveLeft();

		}else {
			player.setMovingLeft(false);
		}
		//right
		if(gc.getInput().isKeyDown(Input.KEY_RIGHT)){
			player.moveRight();
		}else{
			player.setMovingRight(false);
		}

		if(System.currentTimeMillis()/100 - lastClickTime/100 > 4){
			if(gc.getInput().isKeyDown(Input.KEY_Q)){
				map.placeCollectableBlock(player, Color.blue);
				lastClickTime = System.currentTimeMillis();
			}else if(gc.getInput().isKeyDown(Input.KEY_W)){
				map.placeCollectableBlock(player, Color.red);
				lastClickTime = System.currentTimeMillis();
			}else if(gc.getInput().isKeyDown(Input.KEY_E)){
				map.placeCollectableBlock(player , Color.yellow);
				lastClickTime = System.currentTimeMillis();
			}else if(gc.getInput().isKeyDown(Input.KEY_R)){
				map.placeCollectableBlock(player, Color.green);
				lastClickTime = System.currentTimeMillis();
			}else if(gc.getInput().isKeyDown(Input.KEY_A)){
				player.shoot();
				lastClickTime = System.currentTimeMillis();
			}
		}


		if(gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)){
			//shoot a bullet, doo
			player.shoot();
			mouseX = x;
			mouseY = y;

			//doesn't inclide the viewport shift
			Rectangle mouseclick = new Rectangle(gc.getInput().getMouseX(), gc.getInput().getMouseY(), 1, 1);

			//includes the viewport shift
			mouseclick = new Rectangle(gc.getInput().getMouseX() + VIEWPORT_X, gc.getInput().getMouseY()+ VIEWPORT_Y, 1, 1);
			for(int i = 0; i < map.collectableBlocks.size(); i++){
				CollectableBlock b = map.collectableBlocks.get(i);
				Rectangle r = new Rectangle(b.getX() - 2, b.getY() - 2, b.width + 12, b.height + 12);
				if(r.intersects(mouseclick)){
					player.addBlock(b.color);
					map.removeBlock(b);

					//System.out.println("got it");
					map.collectableBlocks.remove(b);
				}
			}

			for(int i = 0; i < map.placedCollectableBlocks.size(); i++){
				CollectableBlock b = map.placedCollectableBlocks.get(i);
				Rectangle r = new Rectangle(b.getX() - 2, b.getY() - 2, b.width + 12, b.height + 12);
				if(r.intersects(mouseclick)){
				    	player.addBlock(b.color);
					map.removeBlock(b);

					//System.out.println("got it");
					map.collectableBlocks.remove(b);
				}
			}

		}



	}
}
