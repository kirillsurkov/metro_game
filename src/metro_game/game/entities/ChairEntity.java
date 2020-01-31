package metro_game.game.entities;

import metro_game.Context;
import metro_game.game.physics.bodies.BoxBody;
import metro_game.game.physics.bodies.Body.BodyGameInterface;
import metro_game.render.primitives.ColorPrimitive;
import metro_game.render.primitives.RectPrimitive;
import metro_game.render.primitives.ShaderPrimitive;
import metro_game.render.primitives.ShaderPrimitive.ShaderType;

public class ChairEntity extends PhysicsEntity {
	private RectPrimitive m_rect;
	private BodyGameInterface m_body;
	
	public ChairEntity(Context context, float x, float y) {
		super(context);
		addPrimitive(new ShaderPrimitive(ShaderType.DEFAULT_GAME));
		addPrimitive(new ColorPrimitive(1.0f, 0.0f, 0.0f, 1.0f));
		m_rect = addPrimitive(new RectPrimitive(x, y, 1.5f, 1.5f, 0.0f, true));
		m_body = addBody(new BoxBody(false, x, y, 1.5f, 1.5f));
	}
	
	public void setOccupiedBy(PhysicsEntity physicsEntity) {
	}
	
	@Override
	public void update(double delta) {
		m_rect.getPosition().set(m_body.getPositionX(), m_body.getPositionY());
		m_rect.setRotation(m_body.getRotation());
	}
}
