package metro_game.game.physics.bodies.modifiers;

public class BodyModifierAngularVelocity extends BodyModifier {
	private float m_angularVelocity;
	
	public BodyModifierAngularVelocity(float angularVelocity) {
		super(Type.ANGULAR_VELOCITY);
		m_angularVelocity = angularVelocity;
	}
	
	public float getAngularVelocity() {
		return m_angularVelocity;
	}
}
