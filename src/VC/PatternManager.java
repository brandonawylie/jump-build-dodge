package VC;
import java.util.*;

public class PatternManager {
	public List<CollectableBlock> blocks = new ArrayList<CollectableBlock>();
	public List<String[][]> patterns = new ArrayList<>();
	public void addBlock(CollectableBlock b){
		blocks.add(b);	
	}
	
	/***
	 * This method goes through the blocks and tries to guess the pattern in a String[][]
	 * 
	 */
	public void checkPattern(){
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
			if(x > highestX)
				highestX = x;
			if(x < lowestX)
				lowestX = x;
			if(y > highestY)
				highestY = y;
			if(y < lowestY)
				lowestY = y;
			
		}
		
		//go to the right until 2 empty blocks are there
		/*
		 *Start Here->[0,0,R,R,B,R,R,0,0,0
		 *            B,0,0,0,0,0,0,0,0,B]
		 */
	}
}
