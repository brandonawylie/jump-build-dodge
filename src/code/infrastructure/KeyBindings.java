package code.infrastructure;
import java.util.HashMap;

import org.newdawn.slick.Input;
public class KeyBindings {
	public static java.util.Map<String, Integer> keyBindingsMap;
	static{
		keyBindingsMap = new HashMap<String, Integer>();
		keyBindingsMap.put("left", Input.KEY_LEFT);
		keyBindingsMap.put("right", Input.KEY_RIGHT);
		keyBindingsMap.put("jump", Input.KEY_SPACE);
		keyBindingsMap.put("shoot", Input.KEY_Z);
		keyBindingsMap.put("blue block", Input.KEY_Q);
		keyBindingsMap.put("red block", Input.KEY_W);
		keyBindingsMap.put("green block", Input.KEY_E);
		keyBindingsMap.put("yellow block", Input.KEY_R);
	}
}
