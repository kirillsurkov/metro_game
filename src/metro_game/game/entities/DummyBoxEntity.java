package metro_game.game.entities;

import metro_game.game.physics.bodies.BoxBody;

import metro_game.Context;
import metro_game.game.events.CameraEvent;
import metro_game.game.physics.bodies.Body;
import metro_game.game.shapes.RectShape;

public class DummyBoxEntity extends GameEntity {
	private boolean m_dynamic;
	private boolean m_fragile;
	private RectShape m_shape;
	private Body m_body;
	
	public DummyBoxEntity(Context context, boolean fragile, boolean dynamic, float x, float y, float width, float height, float rotation) {
		super(context);
		m_fragile = fragile;
		m_dynamic = dynamic;
		m_shape = addShape(new RectShape(x, y, width, height, rotation));
		m_body = addBody(new BoxBody(dynamic, x, y, width, height));
		m_body.setRotation(rotation);
	}
	
	public boolean isFragile() {
		return m_fragile;
	}
	
	@Override
	public void onCollide(GameEntity gameEntity) {
		if (gameEntity instanceof DummyBoxEntity) {
			DummyBoxEntity entity = (DummyBoxEntity) gameEntity;
			if (entity.isFragile()) {
				entity.setNeedRemove(true);
			}
		}
	}
	
	@Override
	public void update(double delta) {
		m_shape.getPosition().set(m_body.getPosition());
		m_shape.setRotation(m_body.getRotation());
		
		if (m_dynamic) {
			CameraEvent cameraEvent = new CameraEvent();
			cameraEvent.getPosition().set(m_body.getPosition());
			m_context.getGameEvents().pushEvent(cameraEvent);
		}
	}
}
