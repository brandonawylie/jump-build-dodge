package code;

import java.util.ArrayList;
import java.util.List;

public interface ColorObservable {
	public List<ColorObserver> obs = new ArrayList<>();
	public void notifyColorChange();
}
