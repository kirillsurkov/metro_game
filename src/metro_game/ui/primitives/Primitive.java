package metro_game.ui.primitives;

public class Primitive {
	public enum Type {
		SHADER,
		COLOR,
		RECT,
		TEXT
	}
	
	private Type m_type;
	
	public Primitive(Type type) {
		m_type = type;
	}
	
	public Type getType() {
		return m_type;
	}
}
