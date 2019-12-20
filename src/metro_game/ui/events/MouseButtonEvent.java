package metro_game.ui.events;

public class MouseButtonEvent extends UIEvent {
	private boolean m_up;
	
	public MouseButtonEvent(boolean up) {
		super(Type.MOUSE_BUTTON);
		m_up = up;
	}
	
	public boolean isUp() {
		return m_up;
	}
}
