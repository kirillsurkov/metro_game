package metro_game.game.physics;

import metro_game.game.physics.Physics.OwnerBodyPair;

public interface Engine {
	void clearWorld();
	void addBody(OwnerBodyPair pair);
	void removeBody(OwnerBodyPair pair);
	void update(double delta);
}
