package metro_game.game.entities;

import metro_game.game.physics.bodies.BoxBody;
import metro_game.game.physics.bodies.Body;
import metro_game.game.shapes.RectShape;

public class DummyBoxEntity extends GameEntity {
	private RectShape m_shape;
	private Body m_body;
	
	public DummyBoxEntity(boolean dynamic, float x, float y, float width, float height, float rotation) {
		m_shape = addShape(new RectShape(x, y, width, height, rotation));
		m_body = addBody(new BoxBody(dynamic, x, y, width, height));
		m_body.setRotation(rotation);
	}
	
	@Override
	public void update(double delta) {
		m_shape.getPosition().set(m_body.getPosition());
		m_shape.setRotation(m_body.getRotation());
	}
}
