package metro_game.game.physics.bodies.modifiers;

public class BodyModifier {
	public enum Type {
		SENSOR,
		POSITION,
		LINEAR_IMPULSE,
		LINEAR_VELOCITY,
		ROTATION,
		ANGULAR_VELOCITY
	}
	
	private Type m_type;
	
	public BodyModifier(Type type) {
		m_type = type;
	}
	
	public Type getType() {
		return m_type;
	}
}
