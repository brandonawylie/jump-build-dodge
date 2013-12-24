package code.gamestates;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;


public class PlatformerGame extends StateBasedGame{
	public static int WIDTH = 1280;
	public static int HEIGHT = 720;
	public static void main(String[] args) throws SlickException{
		AppGameContainer app = new AppGameContainer(new PlatformerGame());
		app.setDisplayMode(WIDTH, HEIGHT, false);
		app.setTargetFrameRate(60);
		app.start();
	}

	public static final int menu = 0;
	public static final int game = 1;


	public PlatformerGame() {
		super("platformer");
		this.addState(new GameplayState(1));
		this.addState(new PauseState(2));

	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		this.getState(2).init(gc, this);
		this.getState(game).init(gc, this);
		this.enterState(game);
		
	}

}
