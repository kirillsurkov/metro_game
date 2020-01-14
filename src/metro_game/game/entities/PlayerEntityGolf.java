package metro_game.game.entities;

import org.joml.Vector2f;

import metro_game.Context;
import metro_game.game.events.CameraEvent;
import metro_game.game.physics.bodies.Body;
import metro_game.game.physics.bodies.CircleBody;
import metro_game.render.primitives.CirclePrimitive;
import metro_game.render.primitives.ColorPrimitive;
import metro_game.render.primitives.RectPrimitive;
import metro_game.render.primitives.ShaderPrimitive;
import metro_game.render.primitives.TextPrimitive;
import metro_game.render.primitives.ShaderPrimitive.ShaderType;
import metro_game.ui.events.UIEvent;
import metro_game.ui.events.MouseButtonEvent;

public class PlayerEntityGolf extends GameEntity {
	private Body m_body;
	private Vector2f m_clickPos;
	private CirclePrimitive m_rect;
	private RectPrimitive m_aimRect;
	private TextPrimitive m_text;
	private float m_aimShapeLength;
	private float m_aimAngle;
	private float m_aimPower;
	private float m_aimPowerMax;
	
	public PlayerEntityGolf(Context context, float x, float y) {
		super(context);
		float radius = 0.5f;
		addPrimitive(new ShaderPrimitive(ShaderType.DEFAULT_GAME));
		addPrimitive(new ColorPrimitive(1.0f, 1.0f, 1.0f, 1.0f));
		m_aimRect = addPrimitive(new RectPrimitive(0.0f, 0.0f, 0.0f, 0.1f, 0.0f, true));
		m_aimRect.setVisible(false);
		m_rect = addPrimitive(new CirclePrimitive(x, y, radius, 0.0f));
		
		addPrimitive(new ShaderPrimitive(ShaderType.FONT));
		addPrimitive(new ColorPrimitive(0.0f, 0.0f, 0.0f, 1.0f));
		m_text = addPrimitive(new TextPrimitive("X", false, 32, 0.0f, x, y, 0.0f, TextPrimitive.AlignmentX.CENTER, TextPrimitive.AlignmentY.CENTER));
		
		m_body = addBody(new CircleBody(true, x, y, 0.0f, 0.0f, 0.0f, 0.0f, radius));
		m_body.setRotation(30);
		m_clickPos = null;
		m_aimShapeLength = 5.0f;
		m_aimAngle = 0;
		m_aimPower = 0;
		m_aimPowerMax = 10.0f;
	}
	
	@Override
	public void onCollideStart(GameEntity gameEntity) {
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
		
		m_text.getPosition().set(m_body.getPosition());
		m_text.setRotation(m_body.getRotation());
		
		CameraEvent cameraEvent = new CameraEvent();
		cameraEvent.getPosition().set(m_body.getPosition());
		m_context.getGameEvents().pushEvent(cameraEvent);
	}
}
