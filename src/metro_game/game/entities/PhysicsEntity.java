package metro_game.game.entities;

import metro_game.Context;
import metro_game.game.events.NewBodyEvent;
import metro_game.game.physics.bodies.Body;

public class PhysicsEntity extends GameEntity {
	public PhysicsEntity(Context context) {
		super(context);
	}
	
	public void onCollideStart(GameEntity gameEntity) {
	}
	
	public void onCollideEnd(GameEntity gameEntity) {
	}
	
	public <T extends Body> T addBody(T body) {
		m_context.getGameEvents().pushEvent(new NewBodyEvent(this, body));
		return body;
	}
}
