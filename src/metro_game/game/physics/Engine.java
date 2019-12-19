package metro_game.game.physics;

import metro_game.game.physics.bodies.Body;

public interface Engine {
	void clearWorld();
	void addBody(Body body);
	void update(double delta);
}
