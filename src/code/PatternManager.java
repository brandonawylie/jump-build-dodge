package code;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class PatternManager implements Oberserver{
	//public List<CollectableBlock> blocks = new ArrayList<CollectableBlock>();
	public List<Rectangle> patternRects = new ArrayList<Rectangle>();
	public List<String[][]> patterns = new ArrayList<>();
	String[][] curArr;
	Rectangle[][] pRectArray;
	float playerX, playerY, playerWidth, playerHeight;
	private int TT_SPACING = 3;
	private int TT_MARGIN = 5;
	private int TT_SIZE = 5;
	public void draw(Graphics g, int vpX, int vpY){
		
		if(pRectArray != null){
			System.out.println("===================================");
			for(int r = 0; r < curArr.length; r++){
				for(int c = 0; c < curArr[r].length; c++){
					System.out.println(curArr[r][c]);
				}
			}
			int rowSize = pRectArray.length;
			int colSize = pRectArray[0].length;
			
			float startX = pRectArray[0][0].getX();
			float startY = pRectArray[0][0].getY();
			
			g.setColor(Color.gray);
			//g.fillRoundRect(startX - vpX, startY - vpY, rowSize*pRectArray[0][0].getHeight(), colSize*pRectArray[0][0].getWidth(), 2);
			for(int r = 0; r < pRectArray.length; r++){
				for(int c = 0; c < pRectArray[r].length; c++){
					String temp = curArr[r][c];
					if(temp.equals("b")){
						g.setColor(Color.blue);
					}else if(temp.equals("g")){
						g.setColor(Color.green);
					}else if(temp.equals("r")){
						g.setColor(Color.red);
					}else if(temp.equals("p")){
						g.setColor(Color.pink);
					}else if(temp.equals("e")){
						g.setColor(Color.white);
					}//else
//						System.out.println("no color recognized");
					//Rectangle rec = pRectArray[r][c];
//					System.out.println("Pattern Manager: at " + r + ", " + c + " || temp=" + temp);
					float x = playerX + playerWidth + TT_SIZE*c;
					float y = playerY + TT_SIZE*r;
					float w = TT_SIZE;
					float h = TT_SIZE;
					g.fillRect(x - vpX,y - vpY,w,h);
//					g.fillRect(c*rec.getWidth() + playerX + playerWidth - vpX, r*rec.getHeight() + playerY - vpY, rec.getWidth(), rec.getHeight());
				}
			}
//			System.out.println("PM===============================");
			//			g.setColor(Color.white);
//			g.drawLine(pRectArray[0][0].getX(), pRectArray[0][0].getY(), pRectArray[0][0].getX() + pRectArray[pRectArray.length - 1][0].getX(), pRectArray[0][0].getY() + pRectArray[pRectArray.length - 1][0].getY() + 2);
		}//else
//			System.out.println("PM: null");
	}
	/***
	 * This method goes through the blocks and tries to guess the pattern in a String[][]
	 *
	 */
	public void checkPattern(List<CollectableBlock> blocks, Player player){
		if(playerWidth == 0){
			playerWidth = player.width;
			playerHeight = player.height;
		}
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
		//TODO change this + 5 to a class variable

		int r = 0;
		int c = 0;
		Rectangle[][] tempArray = new Rectangle[(int) ((highestX - lowestX)/bwidth + 1)][(int) ((highestY - lowestY)/bheight) + 1	];
		//the String[][] holding the pattern
		List<List<String>> pArr = new ArrayList<>();
		do{
			bx = lowestX;
			by = lowestY + r*bheight;
			c = 0;
			List<String> row = new ArrayList<>();
			do{
				bx = lowestX + c*bwidth;
				c++;
				Rectangle rect = new Rectangle(bx, by, bwidth, bheight);
				patternRects.add(rect);
				rect = new Rectangle(bx + bwidth/2, by + bheight/2, 1, 1);
				//patternRects.add(rect);
				//tempArray[r][c] = new Rectangle(ttLowestX + c*2, ttLowestY + r*2, 2, 2);
				String result = "e";
				for(CollectableBlock block : blocks){
					Rectangle brect = new Rectangle(block.getX(), block.getY(), block.width, block.height);
					if(brect.intersects(rect)){ 
						Color color = block.getColor();
						if(color.equals(color.red)){
							result = "r";
							break;
						}else if(color.equals(color.blue)){
							result = "b";
							break;
						}else if(color.equals(color.yellow)){
							result = "y";
							break;
						}else if(color.equals(color.pink)){
							result = "p";
							break;
						}else if(color.equals(color.green)){
						    result = "g";
						    break;
						}
					}
				}
				row.add(result);
			}while(highestX - (bx + bwidth) > bwidth);
			pArr.add(row);
			r++;
		}while(highestY - (by + bheight) > bheight);
		
		float ttLowestX = (playerX + player.width) + 5;
		float ttLowestY = playerY + 5;
		tempArray = new Rectangle[r][c];
		for(int i = 0; i < r; i++){
			for(int j = 0; j < c; j++){
				tempArray[i][j] = new Rectangle(ttLowestX + j*8, ttLowestY + i*8,8,8);
			}
		}
		pRectArray = tempArray;
		
		//change the List<List<String>> to a String arr
		System.out.println("=================");
		if(pArr.size() > 0 && pArr.get(0).size() > 0){
		    r = pArr.size();
		    c = pArr.get(0).size();
		    curArr = new String[r][c];
		    for(int i = 0; i < r;i++){
			boolean isFirst = true;
			for(int j = 0; j < c; j++){
			    curArr[i][j] = pArr.get(i).get(j);

			    if(isFirst){
			    System.out.print(curArr[i][j]);
			    isFirst = false;
			    }else
				System.out.print("," + curArr[i][j]);

			}
			System.out.println();
		    }
		}
	}
	
	public boolean matchPatternArray(String[][] arr){
		System.out.println("---------------------------------------");
		String[][] pArr = arr;
		for(int r = 0; r < pArr.length; r++){
			boolean isFirst = true;
			for(int cc = 0; cc < pArr[r].length; cc++){
				if(isFirst){
					System.out.print(pArr[r][cc]);
					isFirst = false;
				}else
					System.out.print(", " + pArr[r][cc]);
					
			}
			System.out.println();
		}
		pArr = curArr;
		for(int r = 0; r < pArr.length; r++){
			boolean isFirst = true;
			for(int cc = 0; cc < pArr[r].length; cc++){
				if(isFirst){
					System.out.print(pArr[r][cc]);
					isFirst = false;
				}else
					System.out.print(", " + pArr[r][cc]);
					
			}
			System.out.println();
		}
		System.out.println("---------------------------------------");
		try{
			for(int r = 0; r < arr.length; r++){
				for(int c = 0; c < arr[r].length; c++){
					if(!curArr[r][c].equalsIgnoreCase(arr[r][c]))
						return false;
				}
			}
		}catch(Exception e){ return false; }
		return true;
	}
	
	public void updateToolTipBlocks(){
		for(int r = 0; r < pRectArray.length; r++){
			for(int c = 0; c < pRectArray[r].length; c++){
				pRectArray[r][c].setX(playerX + playerWidth + TT_MARGIN + TT_SPACING);
				pRectArray[r][c].setY(playerY);
			}
		}
	}
	@Override
	public void changeColorNotification(int[] colors) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void changePositionNotification(float x, float y) {
		playerX = x;
		playerY = y;
		if(pRectArray != null)
			updateToolTipBlocks();
		//System.out.println("change in pos");
	}
}
