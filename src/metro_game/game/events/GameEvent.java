package metro_game.game.events;

public class GameEvent {
	public enum Type {
		SWITCH_SCENE,
		NEW_BODY,
		CAMERA,
		DESTROY_ENTITY
	}
	
	private Type m_type;
	
	public GameEvent(Type type) {
		m_type = type;
	}
	
	public Type getType() {
		return m_type;
	}
}
