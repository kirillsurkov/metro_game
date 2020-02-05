package metro_game.game.entities;

import org.joml.Vector2f;

import metro_game.Context;
import metro_game.game.physics.bodies.Body.BodyGameInterface;
import metro_game.game.Route;
import metro_game.game.entities.ChairEntity.ChairOccupier;
import metro_game.game.physics.bodies.CircleBody;
import metro_game.render.primitives.CirclePrimitive;
import metro_game.render.primitives.ColorPrimitive;
import metro_game.render.primitives.ShaderPrimitive;
import metro_game.render.primitives.ShaderPrimitive.ShaderType;

public class EnemyEntity extends PhysicsEntity implements ChairOccupier {
	private CirclePrimitive m_circle;
	private BodyGameInterface m_body;
	private Route m_route;
	
	public EnemyEntity(Context context, float x, float y) {
		super(context);
		float radius = 0.6f;
		
		m_route = new Route(x, y, true);
		
		addPrimitive(new ShaderPrimitive(ShaderType.DEFAULT_GAME));
		addPrimitive(new ColorPrimitive(1.0f, 0.5f, 0.0f, 1.0f));
		m_circle = addPrimitive(new CirclePrimitive(x, y, radius, 0.0f));
		
		m_body = addBody(new CircleBody(true, x, y, 0.0f, 0.0f, 0.0f, 0.0f, radius));
	}
	
	public void addWaypoint(float x, float y) {
		m_route.addWaypoint(x, y);
	}
	
	@Override
	public void update(double delta) {
		float srcX = m_body.getPositionX();
		float srcY = m_body.getPositionY();
		
		m_circle.setPosition(srcX, srcY);
		m_circle.setRotation(m_body.getRotation());
		
		m_route.update(srcX, srcY);
		Vector2f vel = m_route.getDirection(srcX, srcY).mul(5.0f);
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
