package metro_game.game.physics.bodies.modifiers;

public class BodyModifierPosition extends BodyModifier {
	private float m_x;
	private float m_y;
	
	public BodyModifierPosition(float x, float y) {
		super(Type.POSITION);
		m_x = x;
		m_y = y;
	}
	
	public float getX() {
		return m_x;
	}
	
	public float getY() {
		return m_y;
	}
}
