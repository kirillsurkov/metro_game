package metro_game.game.physics.bodies;

public class CircleBody extends Body {
	private float m_radius;
	
	public CircleBody(boolean dynamic, float x, float y, float xVel, float yVel, float rotation, float angVel, float radius) {
		super(Type.CIRCLE, dynamic, x, y, xVel, yVel, rotation, angVel);
		m_radius = radius;
	}
	
	public float getRadius() {
		return m_radius;
	}
}
