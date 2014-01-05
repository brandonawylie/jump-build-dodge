package code.uielements;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.GameState;
import code.infrastructure.Map;
import code.infrastructure.PauseMenuObserver;

public class Menu {
	Rectangle menu;
	Rectangle resume, levelSelect, settings, exit;
	int buttonPadding = 16;
	int textHeight = 7;
	public boolean visible = false;
	PauseMenuObserver ob;
	
	Color[] innerColors, outerColors;
	public Menu(int windowWidth, int windowHeight, PauseMenuObserver ob){
		menu = new Rectangle(windowWidth/3, 0, windowWidth/3, windowHeight);
		this.ob = ob;
		int topSpacing = windowHeight/5;
		int buttonWidth = (int) (menu.getWidth() - 2*buttonPadding);
		int buttonHeight = 20;
		
		int n = 1;
		int x = windowWidth/3 + buttonPadding;
		int y = n*topSpacing;
		resume = new Rectangle(x, y, buttonWidth, buttonHeight);
		
		n += 1;
		x = windowWidth/3 + buttonPadding;
		y = n*topSpacing;
		levelSelect = new Rectangle(x, y, buttonWidth, buttonHeight);
		
		n += 1;
		x = windowWidth/3 + buttonPadding;
		y = n*topSpacing;
		settings = new Rectangle(x, y, buttonWidth, buttonHeight);
		
		n += 1;
		x = windowWidth/3 + buttonPadding;
		y = n*topSpacing;
		exit = new Rectangle(x, y, buttonWidth, buttonHeight);
		
		innerColors = new Color[4];
		outerColors = new Color[4];
		for(int i = 0; i < innerColors.length; i++){
			innerColors[i] = Color.black;
		}
		for(int i = 0; i < outerColors.length; i++){
			outerColors[i] = Color.white;
		}
	}
	
	public void update(GameState pauseState, GameContainer gc,  Map m){
		Input input = gc.getInput();
		int x = input.getMouseX();
		int y = input.getMouseY();
		Rectangle r = new Rectangle(x,y,2,2);
		if(r.intersects(resume)){
			if(gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)){
				visible = false;
			}
			innerColors[0] = Color.white;
			outerColors[0] = Color.black;
		}else{
			innerColors[0] = Color.black;
			outerColors[0] = Color.white;
		}
		
		if(r.intersects(levelSelect)){
			if(gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)){
				ob.levelSelect();
			}
			innerColors[1] = Color.white;
			outerColors[1] = Color.black;
		}else{
			innerColors[1] = Color.black;
			outerColors[1] = Color.white;
		}
		
		
		if(r.intersects(settings)){
			if(gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)){
				ob.settings();
			}
			innerColors[2] = Color.white;
			outerColors[2] = Color.black;
		}else{
			innerColors[2] = Color.black;
			outerColors[2] = Color.white;
		}
		
		if(r.intersects(exit)){
			if(gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)){
				ob.exit();
			}
			innerColors[3] = Color.white;
			outerColors[3] = Color.black;;
		}else{
			innerColors[3] = Color.black;
			outerColors[3] = Color.white;
		}
	}
	
	public void draw(Graphics g){
		//outline, present for all
		//g.fill(menu);
		
		//main settings menu;
		g.setColor(innerColors[0]);
		g.fill(resume);
		g.setColor(innerColors[1]);
		g.fill(levelSelect);
		g.setColor(innerColors[2]);
		g.fill(settings);
		g.setColor(innerColors[3]);
		g.fill(exit);
		
		
		g.setColor(outerColors[0]);
		g.draw(resume);
		String str = "resume";
		g.drawString(str, resume.getX() + resume.getWidth()/2 - str.length()*4, resume.getY() + resume.getHeight()/2 - textHeight);
		g.setColor(outerColors[1]);
		g.draw(levelSelect);
		str = "level select";
		g.drawString(str, levelSelect.getX() + levelSelect.getWidth()/2 - str.length()*4, levelSelect.getY() + levelSelect.getHeight()/2 - textHeight);
		g.setColor(outerColors[2]);
		g.draw(settings);
		str = "settings";
		g.drawString(str, settings.getX() + settings.getWidth()/2 - str.length()*4, settings.getY() + settings.getHeight()/2 - textHeight);
		g.setColor(outerColors[3]);
		g.draw(exit);
		str = "exit";
		g.drawString(str, exit.getX() + exit.getWidth()/2 - str.length()*4, exit.getY() + exit.getHeight()/2 - textHeight);
	}
}
