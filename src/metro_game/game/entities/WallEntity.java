package metro_game.game.entities;

import metro_game.Context;
import metro_game.game.physics.bodies.BoxBody;
import metro_game.game.physics.bodies.Body.BodyGameInterface;
import metro_game.render.primitives.ColorPrimitive;
import metro_game.render.primitives.RectPrimitive;
import metro_game.render.primitives.ShaderPrimitive;
import metro_game.render.primitives.ShaderPrimitive.ShaderType;

public class WallEntity extends PhysicsEntity {
	private RectPrimitive m_rect;
	private BodyGameInterface m_body;
	private float m_rotation;
	
	public WallEntity(Context context, float x, float y, float width, float rotation) {
		super(context);
		addPrimitive(new ShaderPrimitive(ShaderType.DEFAULT_GAME));
		addPrimitive(new ColorPrimitive(1.0f, 0.0f, 0.0f, 1.0f));
		m_rect = addPrimitive(new RectPrimitive(x, y, width, 0.1f, rotation, true));
		m_body = addBody(new BoxBody(false, x, y, width, 0.1f));
		m_body.setRotation(rotation);
		m_rotation = rotation;
	}
	
	@Override
	public void update(double delta) {
		if (m_rotation != m_body.getRotation()) {
			System.out.println("ERRRR");
		}
		m_rect.setPosition(m_body.getPositionX(), m_body.getPositionY());
		m_rect.setRotation(m_body.getRotation());
	}
}
