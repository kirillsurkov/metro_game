package metro_game.game.entities;

import metro_game.Context;
import metro_game.game.physics.bodies.Body;
import metro_game.game.physics.bodies.BoxBody;
import metro_game.render.primitives.ColorPrimitive;
import metro_game.render.primitives.RectPrimitive;
import metro_game.render.primitives.ShaderPrimitive;
import metro_game.render.primitives.ShaderPrimitive.ShaderType;

public class DoorEntity extends GameEntity {
	private boolean m_opened;
	private boolean m_processing;
	private float m_timer;
	private float m_timeout;
	private RectPrimitive m_rect_top;
	private RectPrimitive m_rect_bottom;
	private Body m_body_top;
	private Body m_body_bottom;
	private float m_origin_top;
	private float m_origin_bottom;
	private float m_offset;
	
	public DoorEntity(Context context, float x, float y) {
		super(context);
		
		m_opened = false;
		m_timer = 0.0f;
		m_timeout = 1.0f;
		m_offset = 1.0f;
		m_origin_top = y + m_offset;
		m_origin_bottom = y - m_offset;
		
		addPrimitive(new ShaderPrimitive(ShaderType.DEFAULT_GAME));
		addPrimitive(new ColorPrimitive(1.0f, 0.0f, 0.0f, 1.0f));
		
		m_rect_top = addPrimitive(new RectPrimitive(x, m_origin_top, 0.1f, 2.0f, 0.0f, true));
		m_rect_bottom = addPrimitive(new RectPrimitive(x, m_origin_bottom, 0.1f, 2.0f, 0.0f, true));
		
		m_body_top = addBody(new BoxBody(false, x, m_origin_top, 0.1f, 1.0f));
		m_body_bottom = addBody(new BoxBody(false, x, m_origin_bottom, 0.1f, 1.0f));
	}
	
	public boolean isOpened() {
		return m_opened;
	}
	
	public void open() {
		if (m_opened) {
			return;
		}
		if (!m_processing) {
			m_timer = 0.0f;
		}
		m_processing = true;
		m_opened = true;
	}
	
	public void close() {
		if (!m_opened) {
			return;
		}
		if (!m_processing) {
			m_timer = 0.0f;
		}
		m_processing = true;
		m_opened = false;
	}
	
	@Override
	public void update(double delta) {
		m_rect_top.getPosition().set(m_body_top.getPosition());
		m_rect_bottom.getPosition().set(m_body_bottom.getPosition());
		
		if (m_processing) {
			m_timer += delta;
			if (m_timer >= m_timeout) {
				m_processing = false;
				m_timer = m_timeout;
			}
			float mul = 2.0f * (m_opened ? m_timer : (1 - m_timer)) / m_timeout;
			m_body_top.getPosition().y = m_origin_top + m_offset * mul;
			m_body_bottom.getPosition().y = m_origin_bottom - m_offset * mul;
		}
	}
}
