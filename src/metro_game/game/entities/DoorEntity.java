package metro_game.game.entities;

import metro_game.Context;
import metro_game.game.physics.bodies.BoxBody;
import metro_game.game.physics.bodies.Body.BodyGameInterface;
import metro_game.render.primitives.ColorPrimitive;
import metro_game.render.primitives.RectPrimitive;

public class DoorEntity extends PhysicsEntity {
	private boolean m_opened;
	private boolean m_processing;
	private float m_timer;
	private float m_timeout;
	private RectPrimitive m_rect_top;
	private RectPrimitive m_rect_bottom;
	private BodyGameInterface m_body_top;
	private BodyGameInterface m_body_bottom;
	private float m_width;
	private float m_origin_top;
	private float m_origin_bottom;
	
	public DoorEntity(Context context, float width, float x, float y) {
		super(context);
		
		m_opened = false;
		m_timer = 0.0f;
		m_timeout = 1.0f;
		m_width = width;
		m_origin_top = y + m_width * 0.25f;
		m_origin_bottom = y - m_width * 0.25f;
		
		addPrimitive(new ColorPrimitive(1.0f, 0.0f, 0.0f, 1.0f));
		
		m_rect_top = addPrimitive(new RectPrimitive(x, m_origin_top, 0.1f, m_width * 0.5f, 0.0f, true));
		m_rect_bottom = addPrimitive(new RectPrimitive(x, m_origin_bottom, 0.1f, m_width * 0.5f, 0.0f, true));
		
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
		if (m_processing) {
			m_timer = m_timeout - m_timer;
		} else {
			m_timer = 0.0f;
		}
		m_processing = true;
		m_opened = false;
	}
	
	@Override
	public void update(double delta) {
		m_rect_top.setPosition(m_body_top.getPositionX(), m_body_top.getPositionY());
		m_rect_bottom.setPosition(m_body_bottom.getPositionX(), m_body_bottom.getPositionY());
		
		if (m_processing) {
			m_timer += delta;
			if (m_timer >= m_timeout) {
				m_processing = false;
				m_timer = m_timeout;
			}
			float mul = 2.0f * (m_opened ? m_timer : (1 - m_timer)) / m_timeout;
			m_body_top.setPosition(m_body_top.getPositionX(), m_origin_top + 0.25f * m_width * mul);
			m_body_bottom.setPosition(m_body_bottom.getPositionX(), m_origin_bottom - 0.25f * m_width * mul);
		}
	}
}
