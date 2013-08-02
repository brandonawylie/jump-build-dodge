package code;

import org.newdawn.slick.Image;

public class TileSet{
   public int firstgid;
   public int lastgid;
   public String name;
   public int tileWidth;
   public String source;
   public int tileHeight;
   public int imageWidth;
   public int imageHeight;
   public Image bitmap;
   public Image[] tiles;
   public int tileAmountWidth;

   public TileSet(int firstgid, String name, int tileWidth, int tileHeight, Image bitmap, int imageWidth, int imageHeight){
		this.firstgid = firstgid;
		this.name = name;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.bitmap = bitmap;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		tileAmountWidth = (int) Math.floor(imageWidth / tileWidth);
		lastgid = (int) (tileAmountWidth * Math.floor(imageHeight / tileHeight) + firstgid - 1);

		System.out.println("fgid = " + firstgid + ", lastgid = " + lastgid + ", length = " + (lastgid - firstgid));
		
		// load images for tileset
		tiles = new Image[lastgid - firstgid];
		int r = 0;
		int c = 0;
		for(int i = 0; i < tiles.length; i++){
			r = i*tileWidth/imageWidth;
			c = (i*tileWidth%imageWidth)/tileWidth;
			tiles[i] = bitmap.getSubImage(c*tileWidth, r*tileHeight, tileWidth, tileHeight);
			i++;
		}
   }

	public boolean hasgid(int gid){
		System.out.println("gid=" + gid);
		if(gid >= firstgid && gid <= lastgid){
			return true;
		}
		return false;
	}

	public Image getImageAt(int gid){
		if(hasgid(gid))
			return tiles[gid - firstgid];
		else return null;
	}
}
