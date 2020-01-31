package metro_game.game.entities;

import metro_game.Context;
import metro_game.game.physics.bodies.BoxBody;
import metro_game.game.physics.bodies.Body.BodyGameInterface;
import metro_game.render.primitives.ColorPrimitive;
import metro_game.render.primitives.RectPrimitive;
import metro_game.render.primitives.ShaderPrimitive;
import metro_game.render.primitives.ShaderPrimitive.ShaderType;

public class SensorEntity extends PhysicsEntity {
	protected boolean m_active;
	protected ColorPrimitive m_color;
	private RectPrimitive m_rect;
	private BodyGameInterface m_body;
	
	public SensorEntity(Context context, float x, float y, float width, float height) {
		super(context);

		m_active = false;
		addPrimitive(new ShaderPrimitive(ShaderType.DEFAULT_GAME));
		m_color = addPrimitive(new ColorPrimitive(1.0f, 0.0f, 0.0f, 1.0f));
		m_rect = addPrimitive(new RectPrimitive(x, y, width, height, 0.0f, true));
		m_body = addBody(new BoxBody(false, x, y, width, height));
		m_body.setSensor(true);
	}
	
	@Override
	public void onCollideStart(PhysicsEntity physicsEntity) {
		if (physicsEntity instanceof PlayerEntityGolf) {
			m_active = true;
			onActivated();
		}
	}
	
	@Override
	public void onCollideEnd(PhysicsEntity physicsEntity) {
		if (physicsEntity instanceof PlayerEntityGolf) {
			m_active = false;
			onDeactivated();
		}
	}
	
	@Override
	public void update(double delta) {
		m_rect.setPosition(m_body.getPositionX(), m_body.getPositionY());
	}
	
	public void onActivated() {
		m_color.set(1.0f, 0.25f, 0.25f, 1.0f);
	}
	
	public void onDeactivated() {
		m_color.set(1.0f, 0.0f, 0.0f, 1.0f);
	}
}
