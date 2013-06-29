package VC;
import java.util.*;

import org.newdawn.slick.geom.Rectangle;

public class PatternManager {
	//public List<CollectableBlock> blocks = new ArrayList<CollectableBlock>();
	public List<Rectangle> patternRects = new ArrayList<Rectangle>();
	public List<String[][]> patterns = new ArrayList<>();

	
	/***
	 * This method goes through the blocks and tries to guess the pattern in a String[][]
	 * 
	 */
	public void checkPattern(List<CollectableBlock> blocks){
		//go through all the blocks and see which one is leftmost, topmost, bottommost and rightmost
		/*
		 *              top, start here
		 *           [0,0,R,R,B,R,R,0,0,0 right end, stop here
		 *  leftmost->B,0,0,0,0,0,0,0,0,B]
		 *  			bottom, stop here
		 */
		float highestX = -1;
		float highestY = -1;
		float lowestX = 999999;
		float lowestY = 999999;
		float x,  y;
		CollectableBlock leftmost, rightmost, topmost, bottommost;
		for(int i = 0; i < blocks.size(); i++){
			CollectableBlock b = blocks.get(i);
			x = b.x;
			y = b.y;
			if(x + b.width > highestX)
				highestX = x + b.width;
			if(x < lowestX)
				lowestX = x;
			if(y + b.height> highestY)
				highestY = y + b.height;
			if(y < lowestY)
				lowestY = y;
			
		}
		
		//go to the right until 2 empty blocks are there
		/*
		 *Start Here->[0,0,R,R,B,R,R,0,0,0
		 *            B,0,0,0,0,0,0,0,0,B]
		 */
		Rectangle rect = new Rectangle(lowestX, lowestY, 10, 10);
		int r = 0;
		while(rect.getY() + 10 < highestY){
			int c = 0;
			
			//System.out.println("++ rows ==> (" + rect.getX() + "," + rect.getY() + ")");
			while(rect.getX() + 10 < highestX){
				System.out.println("++ columns");
				rect = new Rectangle(lowestX + 10*c, lowestY + 10*r, 10, 10);
				patternRects.add(rect);
				c++;
			}
			r++;
			rect = new Rectangle(lowestX + 10*c, lowestY + 10*r, 10, 10);
		}
	}
}
