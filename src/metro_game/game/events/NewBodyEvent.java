package metro_game.game.events;

import metro_game.game.physics.bodies.Body;

public class NewBodyEvent extends GameEvent {
	private Body m_body;
	
	public NewBodyEvent(Body body) {
		super(Type.NEW_BODY);
		m_body = body;
	}
	
	public Body getBody() {
		return m_body;
	}
}
