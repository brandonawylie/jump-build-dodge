package code.infrastructure;

import java.util.ArrayList;
import java.util.List;

public interface Oberservable {
	public List<Oberserver> colorObs = new ArrayList<>();
	public List<Oberserver> positionObs = new ArrayList<>();
	public void notifyColorChange();
	public void notifyPositionChange();
}
