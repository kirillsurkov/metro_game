package metro_game.game.entities;

import org.joml.Vector2f;

import metro_game.Context;
import metro_game.game.events.CameraEvent;
import metro_game.game.physics.bodies.BoxBody;
import metro_game.render.primitives.Rect;
import metro_game.render.primitives.ShaderPrimitive;
import metro_game.render.primitives.ShaderPrimitive.ShaderType;
import metro_game.ui.events.UIEvent;
import metro_game.ui.events.MouseButtonEvent;

public class PlayerEntity extends GameEntity {
	private Rect m_rect;
	private BoxBody m_body;
	private Vector2f m_clickPos;
	private Rect m_aimRect;
	private float m_aimShapeLength;
	private float m_aimAngle;
	private float m_aimPower;
	private float m_aimPowerMax;
	
	public PlayerEntity(Context context, float x, float y) {
		super(context);
		float width = 1.0f;
		float height = 1.0f;
		addPrimitive(new ShaderPrimitive(ShaderType.DEFAULT_GAME));
		m_rect = addPrimitive(new Rect(x, y, width, height, 0.0f));
		m_body = addBody(new BoxBody(true, x, y, width, height));
		m_clickPos = null;
		m_aimRect = addPrimitive(new Rect(0, 0, 0.0f, 0.1f, 0.0f));
		m_aimRect.setVisible(false);
		m_aimShapeLength = 5.0f;
		m_aimAngle = 0;
		m_aimPower = 0;
		m_aimPowerMax = 10.0f;
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
		float aspect = m_context.getAspect();
		
		for (UIEvent uiEvent : m_context.getUIEvents().getEvents()) {
			if (uiEvent.getType() == UIEvent.Type.MOUSE_BUTTON) {
				MouseButtonEvent event = (MouseButtonEvent) uiEvent;
				if (event.isUp()) {
					Vector2f linearVelocity = new Vector2f((float) Math.cos(m_aimAngle), (float) Math.sin(m_aimAngle)).mul(-m_aimPower * m_aimPowerMax);
					m_body.getLinearVelocity().set(linearVelocity);
					m_aimRect.setVisible(false);
					m_clickPos = null;
					m_aimPower = 0;
				} else {
					if (m_clickPos == null) {
						m_aimRect.setVisible(true);
						m_clickPos = new Vector2f(aspect * m_context.getMouseX(), m_context.getMouseY());
						m_body.setAngularVelocity(0.0f);
					}
				}
			}
		}
		
		if (m_clickPos != null) {
			Vector2f angleVector = new Vector2f(aspect * m_context.getMouseX(), m_context.getMouseY()).sub(m_clickPos);
			m_aimAngle = new Vector2f(1.0f, 0.0f).angle(angleVector);
			m_aimPower = Math.min(angleVector.length() * 20.0f, m_aimPowerMax) / m_aimPowerMax;
			m_body.setRotation((float) Math.toDegrees(m_aimAngle));
		}

		float aimLength = m_aimPower * m_aimShapeLength;
		m_aimRect.getSize().x = aimLength;
		Vector2f newPos = new Vector2f((float) Math.cos(m_aimAngle), (float) Math.sin(m_aimAngle)).mul(-0.5f * aimLength).add(m_body.getPosition());
		m_aimRect.setRotation((float) Math.toDegrees(m_aimAngle));
		m_aimRect.getPosition().set(newPos);
		
		m_rect.getPosition().set(m_body.getPosition());
		m_rect.setRotation(m_body.getRotation());
		
		CameraEvent cameraEvent = new CameraEvent();
		cameraEvent.getPosition().set(m_body.getPosition());
		m_context.getGameEvents().pushEvent(cameraEvent);
	}
}
