package code;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.tiled.TiledMapPlus;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Map{
	//info about how the map is rendered. not sure how this fits with the new setup
	public static int blockWidth = 70;
	public static int blockHeight = 70;
	public static int DEFAULT_MAPTILEWIDTH = 70;
	public static int DEFAULT_MAPTILEHEIGHT = 70;
	public static int DEFAULT_MAPWIDTH = 1000;
	public static int DEFAULT_MAPHEIGHT = 1000;
	//different paths
	public static String COLLLECTABLEBLOCK_RED_PATH =  "assets/blocks/collectableblock_red.png";
	public static String COLLLECTABLEBLOCK_BLUE_PATH =  "assets/blocks/collectableblock_blue.png";
	public static String COLLLECTABLEBLOCK_GREEN_PATH =  "assets/blocks/collectableblock_green.png";
	public static String COLLLECTABLEBLOCK_YELLOW_PATH =  "assets/blocks/collectableblock_yellow.png";
	public static String PATTERNBLOCK_RED_PATH =  "assets/blocks/patternblock_red.png";
	public static String PATTERNBLOCK_BLUE_PATH =  "assets/blocks/patternblock_blue.png";
	public static String PATTERNBLOCK_GREEN_PATH =  "assets/blocks/patternblock_green.png";
	public static String PATTERNBLOCK_YELLOW_PATH =  "assets/blocks/patternblock_yellow.png";

	//game objects that belong to the map
	public List<TileSet> tilesets = new ArrayList<>();
	public List<Platform> platforms = new ArrayList<>();
	public List<CollectableBlock> collectableBlocks = new ArrayList<>();
	public List<CollectableBlock> placedCollectableBlocks = new ArrayList<>();
	public List<ShootingEnemy> shootingEnemies = new ArrayList<>();
	public List<Pattern> patterns = new ArrayList<>();
	Image[][] tiles;
	public PatternManager pManager = new PatternManager();

	//map settings
	TiledMapPlus tiledMap;
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
	public void update(int delta, Player player){
		pManager.update(this);

		for(ShootingEnemy e : shootingEnemies)
		    e.update(delta, player, this);
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

		///////////////////////////////////////////////////////


		for(ShootingEnemy e : shootingEnemies)
			e.draw(g, viewportX, viewportY);

		///////////////////////////////////////////////////////

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
		System.out.println("path = " + path);
		CollectableBlock cb =  new CollectableBlock(this, p.x + (p.width - blockWidth)/2, p.y + p.width - blockHeight, path);
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
		tiledMap = new TiledMapPlus(path);
		mapTileWidth = Integer.parseInt(tiledMap.getMapProperty("tilewidth", "" + DEFAULT_MAPTILEWIDTH));
		mapTileHeight = Integer.parseInt(tiledMap.getMapProperty("tileheight", "" + DEFAULT_MAPTILEHEIGHT));
		mapWidth = mapTileWidth * Integer.parseInt(tiledMap.getMapProperty("width", "" + DEFAULT_MAPWIDTH));;
		mapHeight = mapTileHeight * Integer.parseInt(tiledMap.getMapProperty("height", "" + DEFAULT_MAPWIDTH));
		Document dom = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			dom = db.parse(path);
		} catch (ParserConfigurationException e) { }
		catch (SAXException e) {	}

		NodeList nl = dom.getElementsByTagName("objectgroup");
		System.out.println(nl.getLength());

		for(int i = 0; i < nl.getLength(); i++){
			Node objectGroup = nl.item(i);
			String objectGroupName = objectGroup.getNodeName();
			loadObjectGroup(objectGroup);

		}

		return true;
	}

	public void loadObjectGroup(Node objectGroup){
		NamedNodeMap groupAttributes = objectGroup.getAttributes();
		String objectGroupName = groupAttributes.getNamedItem("name").getNodeValue();
		System.out.println("in loadObjectGroup: " + objectGroupName);
		NodeList objectGroupChildNodes = objectGroup.getChildNodes();

		List<Node> objectNodes = new ArrayList<Node>();
		HashMap<String, String> properties = getProperties(objectGroupChildNodes, objectNodes);

		//put the nodes that aren't marked properties into this list
		if(objectGroupName.contains("pattern-")){
			Pattern newPattern = new Pattern(0,0);
			String arrayStr = properties.get("array");
			//System.out.println(arrayStr);
			String[] rowsStr = arrayStr.substring(1, arrayStr.length() - 1).split(";");
			int columnLength = 0;
			List<String[]> columns = new ArrayList<>();
			for(String row : rowsStr){
			    //System.out.println("row = " + row);
			    String[] column = row.split(",");
			    columns.add(column);
			}
			String[][] array = new String[columns.size()][columns.get(0).length];
			int i = 0;
			int j = 0;
			for(String[] pp : columns){
			    j=0;
			    for(String gg : pp){
				//System.out.print(gg + ", ");
				array[i][j] = gg;
				j++;
			    }
			    //System.out.println();
			    i++;
			}

			newPattern.pArr = array;
			for(Node n: objectNodes){
				//System.out.println("in pattern: " + n.getNodeName());
				List<Node> realObjects = new ArrayList<Node>();
				HashMap<String, String> objectProperties = getProperties(n.getChildNodes(), objectNodes);
				//System.out.println(attr.getNamedItem("name").getNodeValue());
				NamedNodeMap attr = n.getAttributes();
				int x = Integer.parseInt(attr.getNamedItem("x").getNodeValue());
				int y = Integer.parseInt(attr.getNamedItem("y").getNodeValue());
				int width = Integer.parseInt(attr.getNamedItem("width").getNodeValue());
				int height = Integer.parseInt(attr.getNamedItem("height").getNodeValue());
				String color = objectProperties.get("color");
				//String array = properties.get("array");
				String cbPath = "";
				if(color.equals("r")){
					cbPath = PATTERNBLOCK_RED_PATH;
				}else if(color.equals("b")){
					cbPath = PATTERNBLOCK_BLUE_PATH;
				}else if(color.equals("g")){
					cbPath = PATTERNBLOCK_GREEN_PATH;
				}else if(color.equals("y")){
					cbPath = PATTERNBLOCK_YELLOW_PATH;
				}
				System.out.println(array);
				newPattern.addBlock(new CollectableBlock(this, x, y, cbPath));
			}
			patterns.add(newPattern);
		}else if(objectGroupName.contains("collision")){
			System.out.println("in collision");
			for(Node n : objectNodes){
				NamedNodeMap attr = n.getAttributes();
				int x = Integer.parseInt(attr.getNamedItem("x").getNodeValue());
				int y = Integer.parseInt(attr.getNamedItem("y").getNodeValue());
				int width = Integer.parseInt(attr.getNamedItem("width").getNodeValue());
				int height = Integer.parseInt(attr.getNamedItem("height").getNodeValue());
				//System.out.println("adding rectanlge, " + x + ", " + y + ", " + width + ", " + height);
				mapCollision.add(new Rectangle(x, y, width, height));
			}
		}else if(objectGroupName.contains("collectable-block")){
			//System.out.println("BOOM COLLECTABLEBLOCK");
			for(Node n : objectNodes){
				NamedNodeMap attr = n.getAttributes();
				int x = Integer.parseInt(attr.getNamedItem("x").getNodeValue());
				int y = Integer.parseInt(attr.getNamedItem("y").getNodeValue());
				int width = Integer.parseInt(attr.getNamedItem("width").getNodeValue());
				int height = Integer.parseInt(attr.getNamedItem("height").getNodeValue());
				HashMap<String, String> props = getProperties(n.getChildNodes(), new ArrayList<Node>());
				String color = props.get("color");
				//System.out.println("found collectable with color: " + color);
				if(color.equals("r"))
					collectableBlocks.add(new CollectableBlock(this, x, y, COLLLECTABLEBLOCK_RED_PATH));
				else if(color.equals("b"))
					collectableBlocks.add(new CollectableBlock(this, x, y, COLLLECTABLEBLOCK_BLUE_PATH));
				else if(color.equals("g"))
					collectableBlocks.add(new CollectableBlock(this, x, y, COLLLECTABLEBLOCK_GREEN_PATH));
				else if(color.equals("y"))
					collectableBlocks.add(new CollectableBlock(this, x, y, COLLLECTABLEBLOCK_YELLOW_PATH));
			}
		}else if(objectGroupName.contains("player")){
			for(Node n : objectNodes){
				NamedNodeMap attr = n.getAttributes();
				int x = Integer.parseInt(attr.getNamedItem("x").getNodeValue());
				int y = Integer.parseInt(attr.getNamedItem("y").getNodeValue());
				int width = Integer.parseInt(attr.getNamedItem("width").getNodeValue());
				int height = Integer.parseInt(attr.getNamedItem("height").getNodeValue());
				playerX = x;
				playerY = y;
			}
		}else if(objectGroupName.contains("enemies")){
			for(Node n : objectNodes){
				NamedNodeMap attr = n.getAttributes();
				int x = Integer.parseInt(attr.getNamedItem("x").getNodeValue());
				int y = Integer.parseInt(attr.getNamedItem("y").getNodeValue());
				int width = Integer.parseInt(attr.getNamedItem("width").getNodeValue());
				int height = Integer.parseInt(attr.getNamedItem("height").getNodeValue());
				HashMap<String, String> props = getProperties(n.getChildNodes(), new ArrayList<Node>());
				String type = attr.getNamedItem("name").getNodeValue();

				if(type.equals("shooting-enemy")){
					//shootingEnemies.add(new ShootingEnemy(x, y, width, height));
				}else if(type.equals("four-fire-enemy")){
					shootingEnemies.add(new FourFireEnemy(x,y,width,height));
				}else if(type.equals("single-fire-enemy")){
					shootingEnemies.add(new SingleFireEnemy(x,y,width,height));
				}
			}

		}
	}

	public HashMap<String, String> getProperties(NodeList nl, List<Node> objectNodes){
		HashMap<String, String> properties = new HashMap<String, String>();
		for(int i = 0; i < nl.getLength(); i++){
			Node n = nl.item(i);
			if(n.getNodeName().equals("properties")){
				NodeList props = n.getChildNodes();
				for(int j = 0; j < props.getLength(); j++){
					Node n2 = props.item(j);
					//System.out.println("int second for loop: " + n2.getNodeName());
					if(n2.getNodeName().equals("property")){
						//System.out.println("found property = " + n2.getAttributes().getNamedItem("name").getNodeValue() + ", " + n2.getAttributes().getNamedItem("value").getNodeValue());
						String key = n2.getAttributes().getNamedItem("name").getNodeValue();
						String value = n2.getAttributes().getNamedItem("value").getNodeValue();
	//					if(value.equals("collectable-blocks"))
	//						printNode(n);
						properties.put(n2.getAttributes().getNamedItem("name").getNodeValue(), n2.getAttributes().getNamedItem("value").getNodeValue());

					}
				}
			}else if(!n.getNodeName().equals("#text")){
				//System.out.println("adding: " + n.getNodeName());
				objectNodes.add(n);
			}
		}
		return properties;
	}

	  /** Prints the specified node, recursively. */
	  public static void printNode(Node node)
	  {
	    int type = node.getNodeType();
	    switch (type)
	    {
	      // print the document element
	      case Node.DOCUMENT_NODE:
	        {
	          System.out.println("<?xml version=\"1.0\" ?>");
	          printNode(((Document)node).getDocumentElement());
	          break;
	        }

	         // print element and any attributes
	      case Node.ELEMENT_NODE:
	        {
	          System.out.print("<");
	          System.out.print(node.getNodeName());

	          if (node.hasAttributes())
	          {
	            NamedNodeMap attrs = node.getAttributes();
	            for (int i = 0; i < attrs.getLength(); i++)
	              printNode(attrs.item(i));
	          }
	          System.out.print(">");

	          if (node.hasChildNodes())
	          {
	            NodeList children = node.getChildNodes();
	            for (int i = 0; i < children.getLength(); i++)
	              printNode(children.item(i));
	          }

	          break;
	        }

	      // Print attribute nodes
	      case Node.ATTRIBUTE_NODE:
	        {
	          System.out.print(" " + node.getNodeName() + "=\"");
	          if (node.hasChildNodes())
	          {
	            NodeList children = node.getChildNodes();
	            for (int i = 0; i < children.getLength(); i++)
	              printNode(children.item(i));
	          }
	          System.out.print("\"");
	          break;
	        }

	        // handle entity reference nodes
	      case Node.ENTITY_REFERENCE_NODE:
	        {
	          System.out.print("&");
	          System.out.print(node.getNodeName());
	          System.out.print(";");
	          break;
	        }

	        // print cdata sections
	      case Node.CDATA_SECTION_NODE:
	        {
	          System.out.print("<![CDATA[");
	          System.out.print(node.getNodeValue());
	          System.out.print("]]>");
	          break;
	        }

	        // print text
	      case Node.TEXT_NODE:
	        {
	          System.out.print(node.getNodeValue());
	          break;
	        }

	      case Node.COMMENT_NODE:
	        {
	          System.out.print("<!--");
	          System.out.print(node.getNodeValue());
	          System.out.print("-->");
	          break;
	        }

	        // print processing instruction
	      case Node.PROCESSING_INSTRUCTION_NODE:
	        {
	          System.out.print("<?");
	          System.out.print(node.getNodeName());
	          String data = node.getNodeValue();
	          {
	            System.out.print(" ");
	            System.out.print(data);
	          }
	          System.out.print("?>");
	          break;
	        }
	    }

	    if (type == Node.ELEMENT_NODE)
	    {
	      System.out.print("</");
	      System.out.print(node.getNodeName());
	      System.out.print('>');
	    }
	  } // printNode(Node)

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
