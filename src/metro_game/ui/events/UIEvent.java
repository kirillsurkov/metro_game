package metro_game.ui.events;

public class UIEvent {
	public enum Type {
		MOUSE_BUTTON,
		BACK
	}
	
	private Type m_type;
	
	public UIEvent(Type type) {
		m_type = type;
	}
	
	public Type getType() {
		return m_type;
	}
}
