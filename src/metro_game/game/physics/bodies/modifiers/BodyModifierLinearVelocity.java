package metro_game.game.physics.bodies.modifiers;

public class BodyModifierLinearVelocity extends BodyModifier {
	private float m_velocityX;
	private float m_velocityY;
	
	public BodyModifierLinearVelocity(float velocityX, float velocityY) {
		super(Type.LINEAR_VELOCITY);
		m_velocityX = velocityX;
		m_velocityY = velocityY;
	}
	
	public float getVelocityX() {
		return m_velocityX;
	}
	
	public float getVelocityY() {
		return m_velocityY;
	}
}
