package code.gamestates;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import code.Map;
import code.uielements.Menu;
import code.uielements.PauseMenuObserver;

public class PauseState extends BasicGameState implements PauseMenuObserver{
	public static int ID = 2;
	
	Menu menu;
	Map map;
	
	public PauseState(int id){
		this.ID = id;
	}
	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		menu = new Menu(gc.getWidth(), gc.getHeight(), this);
		menu.visible = true;
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		menu.draw(g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int arg2) throws SlickException {
		menu.update(this, gc, map);
		if(gc.getInput().isKeyPressed(Input.KEY_ESCAPE)){
			sbg.enterState(GameplayState.ID, new FadeOutTransition(Color.black), new FadeInTransition(Color.black));
		}
	}

	@Override
	public int getID() {
		return ID;
	}
	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void settings() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void exit() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void levelSelect() {
		// TODO Auto-generated method stub
		
	}

}
