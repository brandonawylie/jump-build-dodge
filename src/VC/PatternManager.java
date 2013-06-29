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
		int bWidth = Map.blockWidth*GameplayState.VIEWPORT_RATIO_X;
		int bHeight = Map.blockHeight*GameplayState.VIEWPORT_RATIO_Y;
		int r = 0;
		int c = 0;
		float bx = (lowestX - 1) + bWidth*c;
		float by = (lowestY - 1) + bHeight*r;
		while(by + bHeight <= highestY){
			bx = (lowestX - 1) + bWidth*c;
			by = (lowestY - 1) + bHeight*r;
			while(bx + bWidth <= highestX){
				bx = (lowestX - 1) + bWidth*c;
				by = (lowestY - 1) + bHeight*r;
				Rectangle rect = new Rectangle(bx,by,bWidth,bHeight);
				patternRects.add(rect);
				c++;
			}
			r++;
			bx = (lowestX - 1) + bWidth*c;
			by = (lowestY - 1) + bHeight*r;
		}
	}
}
