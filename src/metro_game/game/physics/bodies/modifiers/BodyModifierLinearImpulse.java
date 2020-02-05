package metro_game.game.physics.bodies.modifiers;

import org.joml.Vector2f;

public class BodyModifierLinearImpulse extends BodyModifier {
	private Vector2f m_impulse;
	
	public BodyModifierLinearImpulse(float impulseX, float impulseY) {
		super(Type.LINEAR_IMPULSE);
		m_impulse = new Vector2f(impulseX, impulseY);
	}
	
	public float getImpulseX() {
		return m_impulse.x;
	}
	
	public float getImpulseY() {
		return m_impulse.y;
	}
}
