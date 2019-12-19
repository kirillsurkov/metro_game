package metro_game.game.entities;

import metro_game.game.physics.bodies.BoxBody;
import metro_game.game.physics.bodies.Body;
import metro_game.game.shapes.RectShape;

public class DummyBoxEntity extends GameEntity {
	private RectShape m_shape;
	
	public DummyBoxEntity(float x, float y) {
		m_shape = addShape(new RectShape(x, y, 0.1f, 0.1f, 0.0f));
		addBody(new BoxBody(x, y));
	}
	
	@Override
	public void onPhysicsUpdate(Body body) {
		m_shape.getPosition().set(body.getPosition());
		m_shape.setRotation(body.getRotation());
	}
	
	@Override
	public void update(double delta) {
		m_shape.getPosition().x += delta * 0.5f;
		m_shape.setRotation(m_shape.getRotation() + (float) delta * 180);
	}
}
