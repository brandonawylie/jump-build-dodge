package code;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Graphics;

public class Pattern {
	public int x, y;
	public List<ColoredBlock> blocks = new ArrayList<>();
	public String[][] pArr;
	
	public Pattern(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void draw(Graphics g, int shiftX, int shiftY){
		for(ColoredBlock b : blocks)
			b.draw(g, shiftX, shiftY);
	}
	
	public void addBlock(ColoredBlock block){
		blocks.add(block);
	}
	
	public void setPatternArray(String[][] s){
		pArr = s;
	}
}
