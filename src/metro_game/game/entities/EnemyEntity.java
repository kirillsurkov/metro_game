package metro_game.game.entities;

import org.joml.Vector2f;
import org.joml.Vector3f;

import metro_game.Context;
import metro_game.Utils;
import metro_game.game.physics.bodies.Body.BodyGameInterface;
import metro_game.game.Route;
import metro_game.game.entities.ChairEntity.ChairOccupier;
import metro_game.game.physics.bodies.CircleBody;
import metro_game.render.primitives.CirclePrimitive;
import metro_game.render.primitives.ColorPrimitive;
import metro_game.render.primitives.ShaderPrimitive;
import metro_game.render.primitives.ShaderPrimitive.ShaderType;
import metro_game.render.primitives.TextPrimitive;
import metro_game.render.primitives.TextPrimitive.AlignmentX;
import metro_game.render.primitives.TextPrimitive.AlignmentY;

public class EnemyEntity extends PhysicsEntity implements ChairOccupier {
	private CirclePrimitive m_circle;
	private TextPrimitive m_text;
	private BodyGameInterface m_body;
	private Route m_route;
	
	public EnemyEntity(Context context, Route.Mode routeMode, float x, float y, float speed) {
		super(context);
		float radius = 0.6f;
		
		m_route = new Route(x, y, speed, routeMode);
		
		Vector3f rgb = Utils.hsv2rgb((float) Math.random(), 1.0f, 1.0f);
		
		addPrimitive(new ShaderPrimitive(ShaderType.DEFAULT_GAME));
		addPrimitive(new ColorPrimitive(rgb.x, rgb.y, rgb.z, 1.0f));
		m_circle = addPrimitive(new CirclePrimitive(x, y, radius, 0.0f));
		
		addPrimitive(new ShaderPrimitive(ShaderType.FONT));
		addPrimitive(new ColorPrimitive(0.0f, 0.0f, 0.0f, 1.0f));
		m_text = addPrimitive(new TextPrimitive("E", false, 32, 0.0f, x, y, 0.0f, AlignmentX.CENTER, AlignmentY.CENTER));
		
		m_body = addBody(new CircleBody(true, x, y, 0.0f, 0.0f, 0.0f, 0.0f, radius));
	}
	
	public void addWaypoint(float x, float y, float speed) {
		m_route.addWaypoint(x, y, speed);
	}
	
	@Override
	public void update(double delta) {
		float srcX = m_body.getPositionX();
		float srcY = m_body.getPositionY();
		
		m_circle.setPosition(srcX, srcY);
		m_circle.setRotation(m_body.getRotation());
		
		m_text.setPosition(srcX, srcY);
		m_text.setRotation(m_body.getRotation());
		
		m_route.update(srcX, srcY);
		Vector2f vel = m_route.getDirection(srcX, srcY).mul(m_route.getSpeed());
		m_body.applyLinearImpulse(vel.x - m_body.getLinearVelocityX(), vel.y - m_body.getLinearVelocityY());
	}
	
	@Override
	public void stickToChair(float chairX, float chairY) {
		m_body.setPosition(chairX, chairY);
		m_body.setLinearVelocity(0.0f, 0.0f);
		m_body.setRotation(0.0f);
		m_body.setAngularVelocity(0.0f);
	}
}
