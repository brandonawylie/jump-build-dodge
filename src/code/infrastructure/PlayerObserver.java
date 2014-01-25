package code.infrastructure;

import code.Player;

public interface PlayerObserver {
	public void playerPostitionChanged(Player p);
	public void playerInventoryChanged(Player p);
	public void playerHealthChanged(Player p);
}
