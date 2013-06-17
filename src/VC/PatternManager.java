package VC;
import java.util.*;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;

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
		
		System.out.println("Pattern Manager: \t\t lowest X = " + lowestX);
		System.out.println("Pattern Manager: \t\t lowest Y = " + lowestY);
		System.out.println("Pattern Manager: \t\t highest X = " + highestX);
		System.out.println("Pattern Manager: \t\t highest Y = " + highestY);
		
		
		//go to the right until 2 empty blocks are there
		/*
		 *Start Here->[0,0,R,R,B,R,R,0,0,0
		 *            B,0,0,0,0,0,0,0,0,B]
		 */
		List<List<String>> brd = new ArrayList<>();
		
		Rectangle rect = new Rectangle(lowestX, lowestY, 10, 10);
		int r = 0;
		//iterating over rows
		while(rect.getY() <= highestY){
			//DEBUG STATEMENTS
			System.out.println("Pattern Manager: \t\t looping");
			//
			//row strings
			List<String> row = new ArrayList<>();
			int c = 0;
			//iterating over the columns
			while(rect.getX() <= highestX){
				//rectangle for the current selection
				rect = new Rectangle(rect.getX() + rect.getWidth()*c, rect.getY(), 10, 10);
				//iterate over the WHOLE COLLECTION OF COLLECTABLE ROCKS
				//TODO FIIIIIIIIIIX THIS .64
				for(CollectableBlock b : blocks){
					if(rect.intersects(new Rectangle(b.x, b.y, b.width, b.height))){
						Color col = b.getColor();
						if(col == Color.blue){
							System.out.println("Pattern Manager: \t\t looking at a blue ");
							row.add("B");
						}else if(col == Color.yellow){
							row.add("Y");
						}else if (col == Color.pink){
							row.add("P");	
						}else if(col == Color.green){
							row.add("G");
						}
					}
				}
				c++;
			}
			brd.add(row);
			rect = new Rectangle(lowestX, rect.getY() + rect.getHeight(), rect.getWidth(), rect.getHeight());
			r++;
		}
		
		//SYS OUT HERE
		System.out.println(brd.toString());
	}
}
