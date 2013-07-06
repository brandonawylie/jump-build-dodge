package code;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
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
		patternRects.clear();
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
		float bx = lowestX;
		float by = lowestY;
		float bwidth = Map.blockWidth*GameplayState.VIEWPORT_RATIO_X;
		float bheight = Map.blockWidth*GameplayState.VIEWPORT_RATIO_Y;
		int r = 0;
		do{
			bx = lowestX;
			by = lowestY + r*bheight;
			int c = 0;
			do{
				bx = lowestX + c*bwidth;
				c++;
				Rectangle rect = new Rectangle(bx, by, bwidth, bheight);
				patternRects.add(rect);
				rect = new Rectangle(bx + bwidth/2, by + bheight/2, 1, 1);
				for(CollectableBlock block : blocks){
					Rectangle brect = new Rectangle(block.getX(), block.getY(), block.width, block.height);
					if(brect.intersects(rect)){
						Color color = block.getColor();
						if(color.equals(color.red)){
							
						}
							
					}
				}
			}while(highestX - (bx + bwidth) > bwidth/2);
			r++;
		}while(highestY - (by + bheight) > bheight/2);
	}
}
