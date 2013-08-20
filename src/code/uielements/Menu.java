package code.uielements;

import java.io.IOException;

import javax.swing.JFileChooser;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import code.Map;
import code.gamestates.GameplayState;

public class Menu {
	Rectangle menu;
	Rectangle resume, levelSelect, settings, exit;
	int buttonPadding = 10;
	int textHeight = 6;
	public boolean visible = false;
	public Menu(int windowWidth, int windowHeight){
		menu = new Rectangle(windowWidth/3, 0, windowWidth/3, windowHeight);
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
	}
	
	public void update(GameplayState game, GameContainer gc,  Map m){
		if(visible){
			Input input = gc.getInput();
			int x = input.getMouseX();
			int y = input.getMouseY();
			Rectangle r = new Rectangle(x,y,2,2);
			if(r.intersects(resume)){
				if(gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)){
					visible = false;
					game.paused = false;
				}
			}
			
			if(r.intersects(levelSelect)){
				if(gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)){
					System.out.println("pressing level select");
					JFileChooser chooser = new JFileChooser();
					int returnValue = chooser.showOpenDialog(null);
					if(returnValue == JFileChooser.APPROVE_OPTION){
						visible = false;
						game.paused = false;
						try {
							m.loadMap(chooser.getSelectedFile().getAbsolutePath());
						} catch (SlickException | IOException e) {	}
					}
				}
			}
			
			if(r.intersects(resume)){
				if(gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)){
					visible = false;
					game.paused = false;
				}
			}
			
			if(r.intersects(resume)){
				if(gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)){
					visible = false;
					game.paused = false;
				}
			}
			
			if(r.intersects(resume)){
				if(gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)){
					visible = false;
					game.paused = false;
				}
			}
		}
	}
	
	public void draw(Graphics g){
		if(visible){
			//outline, present for all
			g.setColor(Color.black);
			g.fill(menu);
			
			//main settings menu;
			g.setColor(Color.white);
			g.draw(resume);
			g.draw(levelSelect);
			g.draw(settings);
			g.draw(exit);
			g.setColor(Color.white);
			String str = "resume";
			g.drawString(str, resume.getX() + resume.getWidth()/2 - str.length()*4, resume.getY() + resume.getHeight()/2 - textHeight);
			str = "level select";
			g.drawString(str, levelSelect.getX() + levelSelect.getWidth()/2 - str.length()*4, levelSelect.getY() + levelSelect.getHeight()/2 - textHeight);
			str = "settings";
			g.drawString(str, settings.getX() + settings.getWidth()/2 - str.length()*4, settings.getY() + settings.getHeight()/2 - textHeight);
			str = "exit";
			g.drawString(str, exit.getX() + exit.getWidth()/2 - str.length()*4, exit.getY() + exit.getHeight()/2 - textHeight);
		}
	}
}
