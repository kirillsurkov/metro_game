package metro_game.game.physics.bodies.modifiers;

public class BodyModifierRotation extends BodyModifier {
	private float m_rotation;
	
	public BodyModifierRotation(float degrees) {
		super(Type.ROTATION);
		m_rotation = degrees;
	}
	
	public float getRotation() {
		return m_rotation;
	}
}
