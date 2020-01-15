package metro_game.game.entities;

import metro_game.Context;
import metro_game.game.physics.bodies.Body;
import metro_game.game.physics.bodies.BoxBody;
import metro_game.render.primitives.ColorPrimitive;
import metro_game.render.primitives.RectPrimitive;
import metro_game.render.primitives.ShaderPrimitive;
import metro_game.render.primitives.ShaderPrimitive.ShaderType;

public class SensorEntity extends GameEntity {
	private ColorPrimitive m_color;
	private RectPrimitive m_rect;
	private Body m_body;
	private boolean m_active;
	private float m_timeout;
	private float m_timer;
	
	public SensorEntity(Context context, float x, float y, float width, float height, float timeout) {
		super(context);

		m_active = false;
		addPrimitive(new ShaderPrimitive(ShaderType.DEFAULT_GAME));
		m_color = addPrimitive(new ColorPrimitive(1.0f, 0.0f, 0.0f, 1.0f));
		m_rect = addPrimitive(new RectPrimitive(x, y, width, height, 0.0f, true));
		m_body = addBody(new BoxBody(false, x, y, width, height));
		m_body.setSensor(true);
		
		m_timeout = timeout;
		m_timer = 0;
	}
	
	@Override
	public void onCollideStart(GameEntity gameEntity) {
		if (gameEntity instanceof PlayerEntityGolf) {
			m_active = true;
			m_timer = 0.0f;
			m_color.set(0.5f, 0.0f, 0.0f, 1.0f);
		}
	}
	
	@Override
	public void onCollideEnd(GameEntity gameEntity) {
		if (gameEntity instanceof PlayerEntityGolf) {
			m_active = false;
			onDeactivated();
		}
	}
	
	@Override
	public void update(double delta) {
		m_rect.getPosition().set(m_body.getPosition());
		
		if (m_active) {
			m_timer += delta;
		}
		
		if (m_timer >= m_timeout) {
			m_active = false;
			m_timer = 0.0f;
			onActivated();
		}
	}
	
	public void onActivated() {
		m_color.set(1.0f, 0.25f, 0.25f, 1.0f);
	}
	
	public void onDeactivated() {
		m_color.set(1.0f, 0.0f, 0.0f, 1.0f);
	}
}