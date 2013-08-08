package code;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Map{
	//info about how the map is rendered. not sure how this fits with the new setup
	public static int blockWidth = 70;
	public static int blockHeight = 70;
	public static int DEFAULT_MAPTILEWIDTH = 70;
	public static int DEFAULT_MAPTILEHEIGHT = 70;
	public static int DEFAULT_MAPWIDTH = 1000;
	public static int DEFAULT_MAPHEIGHT = 1000;
	public static String COLLLECTABLEBLOCK_RED_PATH =  "assets/blocks/collectableblock_red.png";
	public static String COLLLECTABLEBLOCK_BLUE_PATH =  "assets/blocks/collectableblock_blue.png";
	public static String COLLLECTABLEBLOCK_GREEN_PATH =  "assets/blocks/collectableblock_green.png";
	public static String COLLLECTABLEBLOCK_YELLOW_PATH =  "assets/blocks/collectableblock_yellow.png";

	//game objects that belong to the map
	public List<TileSet> tilesets = new ArrayList<>();
	public List<Platform> platforms = new ArrayList<>();
	public List<CollectableBlock> collectableBlocks = new ArrayList<>();
	public List<CollectableBlock> placedCollectableBlocks = new ArrayList<>();
	public List<Pattern> patterns = new ArrayList<>();
	Image[][] tiles;
	PatternManager pManager = new PatternManager();

	//map settings
	TiledMap tiledMap;
	public List<Rectangle> mapCollision = new ArrayList<>();
	Image bg;
	public int mapTileWidth;
	public int mapTileHeight;
	public int mapWidth;
	public int mapHeight;
	public int playerX, playerY;

	/**
	 * Updates the map, and all game objects that are part of the map:
	 * {Platform, CollectableBlock}
	 *
	 * @param player the map uses the player to react given the circumestances
	 */
	public void update(Player player){
		for(Platform p : platforms){
			double distance = Math.sqrt(Math.pow((p.x + p.width/2) - (player.x + player.width/2), 2) + Math.pow((p.y + p.height/2) - (player.y + player.height/2), 2));
			if(!p.isRandom && distance < 30){
				p.setRandomColor(System.currentTimeMillis());
			}else if(p.isRandom && distance >= 16)
				p.resetColor();

			p.update();
		}
	}

	/**
	 * 	draws the map and all contained objects
	 * {Platform, CollectableBlock}
	 *
	 * @param g
	 * @param viewportX
	 * @param viewportY
	 */
	public void draw(Graphics g, int viewportX, int viewportY){
		tiledMap.render(-viewportX, -viewportY);
//		for(int r = 0; r < tiles.length; r++){
//			for(int c = 0; c < tiles[r].length; c++){
//				if(tiles[r][c] != null)
//					g.drawImage(tiles[r][c], c*mapTileWidth - viewportX, r*mapTileHeight - viewportY);
//			}
//		}

		for(Platform p : platforms)
			p.draw(g, viewportX, viewportY);
		for(CollectableBlock b : collectableBlocks)
			b.draw(g, viewportX, viewportY);

		g.setColor(Color.yellow);
		for(Rectangle r : pManager.patternRects){
			g.drawRect(r.getX() - viewportX, r.getY() - viewportY, mapTileWidth, mapTileHeight);
		}

		for(Pattern p : patterns){
			p.draw(g, viewportX, viewportY);
		}

		for(CollectableBlock b : placedCollectableBlocks){
			b.draw(g, viewportX, viewportY);
		}

		pManager.draw(g, viewportX, viewportY);
	}

	public void placeCollectableBlock(Player p, Color c){
		//do check for collision with other world items
		String path = null;
		if(c == Color.red){
			path = COLLLECTABLEBLOCK_RED_PATH;
		}else if(c == Color.green){
			path = COLLLECTABLEBLOCK_GREEN_PATH;
		}else if(c == Color.blue){
			path = COLLLECTABLEBLOCK_BLUE_PATH;
		}else if(c == Color.yellow){
			path = COLLLECTABLEBLOCK_YELLOW_PATH;
		}
		CollectableBlock cb =  new CollectableBlock(p.x + (p.width - blockWidth)/2, p.y + p.width - blockHeight, path);
		p.y -= cb.width + 15;
		placedCollectableBlocks.add(cb);
		pManager.checkPattern(placedCollectableBlocks, p);
		//check if the placed pattern
		double mdist = 9999999;
		int saveIndex = -1;
		Pattern pattern = null;
		for(int i = 0; i < patterns.size(); i++){
			pattern = patterns.get(i);
			double temp = Math.sqrt(Math.pow(p.x - pattern.x, 2) + Math.pow(p.y - pattern.y, 2));
			if(temp < mdist){
				mdist = temp;
				saveIndex = i;
			}
		}
		if(saveIndex != -1 && patterns.size() > saveIndex && pManager.matchPatternArray(patterns.get(saveIndex).pArr))
			patterns.remove(saveIndex);
		else{
			System.out.println("fail");

		}
	}

	public void removeBlock(CollectableBlock b){
		if(collectableBlocks.contains(b))
			collectableBlocks.remove(b);
		if(placedCollectableBlocks.contains(b))
			placedCollectableBlocks.remove(b);
	}

	/**
	 * TODO implement checkpoint variability
	 *  4-
	 *  5-
	 * @param filename				map filename
	 * @return						true if the map is loaded, false otherwise
	 * @throws SlickException
	 * @throws IOException
	 */
	public boolean loadMap(String path) throws SlickException, IOException{
		tiledMap = new TiledMap(path);
		mapTileWidth = Integer.parseInt(tiledMap.getMapProperty("tilewidth", "" + DEFAULT_MAPTILEWIDTH));
		mapTileHeight = Integer.parseInt(tiledMap.getMapProperty("tileheight", "" + DEFAULT_MAPTILEHEIGHT));
		mapWidth = mapTileWidth * Integer.parseInt(tiledMap.getMapProperty("width", "" + DEFAULT_MAPWIDTH));;
		mapHeight = mapTileHeight * Integer.parseInt(tiledMap.getMapProperty("height", "" + DEFAULT_MAPWIDTH));
		//System.out.println("object group count = " + tiledMap.getObjectGroupCount());
		for(int i = 0; i < tiledMap.getObjectGroupCount(); i++){
			for(int j = 0; j < tiledMap.getObjectCount(i); j++){
				String objectType = tiledMap.getObjectName(j, i);
				System.out.println("OBJECT TITLE = " + objectType);
				loadObject(i, j);
			}
		}

		return true;
	}

	public void loadObject(int groupid, int objectid){
		String object = tiledMap.getObjectName(groupid, objectid);
		System.out.println(object);
		int x = tiledMap.getObjectX(groupid, objectid);
		int y = tiledMap.getObjectY(groupid, objectid);
		int width = tiledMap.getObjectWidth(groupid, objectid);
		int height = tiledMap.getObjectHeight(groupid, objectid);
		if(object.equals("player")){
			System.out.println("setting player x/y = " + x + ", " + y);
			 playerX = x;
			 playerY = y;
		}else if(object.equals("collectable-block")){
			String color = tiledMap.getObjectProperty(groupid, objectid, "color", "");
			if(color.equals("r"))
				collectableBlocks.add(new CollectableBlock(x, y, COLLLECTABLEBLOCK_RED_PATH));
			else if(color.equals("b"))
				collectableBlocks.add(new CollectableBlock(x, y, COLLLECTABLEBLOCK_BLUE_PATH));
			else if(color.equals("g"))
				collectableBlocks.add(new CollectableBlock(x, y, COLLLECTABLEBLOCK_GREEN_PATH));
			else if(color.equals("y"))
				collectableBlocks.add(new CollectableBlock(x, y, COLLLECTABLEBLOCK_YELLOW_PATH));
		}else if(object.equals("collision")){
			mapCollision.add(new Rectangle(x, y, width, height));
			System.out.println("adding collision " + x + ", " + y);
		}else{
			System.out.println("map: object not recognized");
		}



	}

	/***
	 * This method checks if the item is in topleft, this is a sort of "multi block" structures
	 *
	 * @deprecated
	 * @param item which item you are using 0 = blank space, 2 = block, 3 = block
	 * @param r
	 * @param c
	 * @param map
	 * @return
	 */
	private boolean isTopLeft(int item, int r, int c, String[][] map){
		//width and height measured in # of map "steps" (10px ea)
		int width = 1;
		int height = 1;

		if(item == 0){

		}else if(item == 1){
			width = blockWidth/10;
			height = blockHeight/10;
		}else if(item == 2){
			width = blockWidth/10;
			height = blockHeight/10;
		}else if(item == 3){

		}else if(item == 4){

		}else if(item == 5){

		}else if(item == 6){

		}else if(item == 7){

		}else if(item == 8){

		}else if(item == 9){

		}

		//counts backwards and upwards to see how many blocks of the same type preceed it.
		int curR = r - 1;
		int prevWidthCount = 0;
		int prevHeightCount = 0;
		while(curR >= 0 && map[curR][c].equals("" + item)){
			curR -= 1;
			prevWidthCount += 1;
		}
		int curC = c - 1;
		while(curC >= 0 && map[r][curC].equals("" + item)){
			curC -= 1;
			prevHeightCount += 1;
		}


		if(prevWidthCount % width == 0 && prevHeightCount % height == 0){
			return true;
		}

		return false;

	}

	public int countObjectsLeft(String[][] map, int r, int c){
		int counter = 0;
		while(c >= 0){
			if(isTopLeft(Integer.parseInt(map[r][c]), r, c, map))
				counter++;
			c -= 1;
		}
		return counter;
	}
	public int countObjectsUp(String[][] map, int r, int c){
		int counter = 0;
		while(r >= 0){
			if(isTopLeft(Integer.parseInt(map[r][c]), r, c, map))
				counter++;
			r -= 1;
		}
		return counter;
	}

}
