package code;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class PatternToolTip {
	private int TT_SPACING = 3;
	private int TT_MARGIN = 5;
	private int TT_SIZE = 5;
	public void draw(Graphics g, String[][] colorArray, float desX, float desY, float shiftX, float shiftY){
	    if(colorArray != null && colorArray[0] != null){
		int rowSize = colorArray.length;
		int colSize = colorArray[0].length;

		g.setColor(Color.gray);
		g.fillRoundRect(desX - shiftX, desY - shiftY, colSize*TT_SIZE, rowSize*TT_SIZE, 2);
		for(int r = colorArray.length - 1; r >= 0 ; r--){
			for(int c = 0; c < colorArray[r].length; c++){
				String temp = colorArray[r][c];
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
//					System.out.println("no color recognized");
				//Rectangle rec = pRectArray[r][c];
				System.out.println("Pattern Manager: at " + r + ", " + c + " || temp=" + temp);
				float x = desX + TT_SIZE*c;
				float y = desY + TT_SIZE*r;
				float w = TT_SIZE;
				float h = TT_SIZE;
				g.fillRect(x - shiftX,y - shiftY,w,h);
//				g.fillRect(c*rec.getWidth() + playerX + playerWidth - vpX, r*rec.getHeight() + playerY - vpY, rec.getWidth(), rec.getHeight());
			}
		}
	    }
	}
}
