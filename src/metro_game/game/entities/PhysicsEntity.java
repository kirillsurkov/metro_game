package metro_game.game.entities;

import metro_game.Context;
import metro_game.game.events.NewBodyEvent;
import metro_game.game.physics.bodies.Body;
import metro_game.game.physics.bodies.Body.BodyGameInterface;

public class PhysicsEntity extends GameEntity {
	public PhysicsEntity(Context context) {
		super(context);
	}
	
	public void onCollideStart(PhysicsEntity physicsEntity) {
	}
	
	public void onCollideEnd(PhysicsEntity physicsEntity) {
	}
	
	public boolean isNeedCollide(PhysicsEntity physicsEntity) {
		return true;
	}
	
	public <T extends Body> BodyGameInterface addBody(T body) {
		m_context.getGameEvents().pushEvent(new NewBodyEvent(this, body));
		return body.getGameInterface();
	}
}
