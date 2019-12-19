package metro_game.game.events;

import metro_game.game.entities.GameEntity;
import metro_game.game.physics.bodies.Body;

public class NewBodyEvent extends GameEvent {
	private GameEntity m_owner;
	private Body m_body;
	
	public NewBodyEvent(GameEntity owner, Body body) {
		super(Type.NEW_BODY);
		m_owner = owner;
		m_body = body;
	}
	
	public GameEntity getOwner() {
		return m_owner;
	}
	
	public Body getBody() {
		return m_body;
	}
}
