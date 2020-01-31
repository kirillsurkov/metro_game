package metro_game.game.entities;

import org.joml.Vector2f;

import metro_game.Context;
import metro_game.game.events.CameraEvent;
import metro_game.game.physics.bodies.BoxBody;
import metro_game.game.physics.bodies.Body.BodyGameInterface;
import metro_game.render.primitives.ColorPrimitive;
import metro_game.render.primitives.RectPrimitive;
import metro_game.render.primitives.ShaderPrimitive;
import metro_game.render.primitives.ShaderPrimitive.ShaderType;
import metro_game.ui.events.MouseButtonEvent;
import metro_game.ui.events.UIEvent;

public class PlayerEntityWalk extends PhysicsEntity {
	private RectPrimitive m_rect;
	private BodyGameInterface m_body;
	private Vector2f m_clickPos;
	private Vector2f m_aimVector;
	
	public PlayerEntityWalk(Context context, float x, float y) {
		super(context);
		
		addPrimitive(new ShaderPrimitive(ShaderType.DEFAULT_GAME));
		addPrimitive(new ColorPrimitive(1.0f, 0.0f, 0.0f, 1.0f));
		m_rect = addPrimitive(new RectPrimitive(x, y, 1.0f, 1.0f, 0.0f, true));
		
		m_body = addBody(new BoxBody(true, x, y, 1.0f, 1.0f));
		
		m_clickPos = null;
		m_aimVector = new Vector2f(0.0f, 1.0f);
	}
	
	@Override
	public void update(double delta) {
		float aspect = m_context.getAspect();
		
		for (UIEvent uiEvent : m_context.getUIEvents().getEvents()) {
			if (uiEvent.getType() == UIEvent.Type.MOUSE_BUTTON) {
				MouseButtonEvent event = (MouseButtonEvent) uiEvent;
				if (event.isUp()) {
					m_clickPos = null;
				} else {
					m_clickPos = new Vector2f(aspect * m_context.getMouseX(), m_context.getMouseY());
				}
			}
		}
		
		if (m_clickPos != null) {
			Vector2f aimVector = new Vector2f(aspect * m_context.getMouseX(), m_context.getMouseY()).sub(m_clickPos).normalize();
			if (aimVector.lengthSquared() > 0) {
				m_aimVector = aimVector;
			}
		}
		
		m_body.setLinearVelocity(m_aimVector.x, m_aimVector.y);
		m_body.setRotation((float) Math.toDegrees(new Vector2f(1.0f, 0.0f).angle(m_aimVector)));
		
		m_rect.getPosition().set(m_body.getPositionX(), m_body.getPositionY());
		m_rect.setRotation(m_body.getRotation());
		
		CameraEvent cameraEvent = new CameraEvent();
		cameraEvent.getPosition().set(m_body.getPositionX(), m_body.getPositionY());
		m_context.getGameEvents().pushEvent(cameraEvent);
	}
}
