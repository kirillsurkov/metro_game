package metro_game.render.primitives;

public class Primitive {
	public enum Type {
		SHADER,
		COLOR,
		RECT,
		CIRCLE,
		TEXT
	}
	
	private Type m_type;
	private boolean m_visible;
	
	public Primitive(Type type) {
		m_type = type;
		m_visible = true;
	}
	
	public Type getType() {
		return m_type;
	}
	
	public void setVisible(boolean visible) {
		m_visible = visible;
	}
	
	public boolean isVisible() {
		return m_visible;
	}
}
