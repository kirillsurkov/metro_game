package metro_game.ui.events;

public class InputEvent {
	public enum Type {
		MOUSE_BUTTON,
		BACK
	}
	
	private Type m_type;
	
	public InputEvent(Type type) {
		m_type = type;
	}
	
	public Type getType() {
		return m_type;
	}
}
