package metro_game.game.physics;

import java.util.HashMap;
import java.util.Map;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.joml.Vector2f;

import metro_game.game.physics.bodies.BoxBody;

public class JBox2dEngine implements Engine {
	private World m_world;
	private Map<Body, metro_game.game.physics.bodies.Body> m_bodies;
	
	public JBox2dEngine() {
		m_world = new World(new Vec2(0, 10));
		m_bodies = new HashMap<Body, metro_game.game.physics.bodies.Body>();
	}
	
	@Override
	public void clearWorld() {
		Body current = m_world.getBodyList();
		while (current != null) {
			m_world.destroyBody(current);
			current = current.getNext();
		}
		m_bodies.clear();
	}
	
	@Override
	public void addBody(metro_game.game.physics.bodies.Body body) {
		BodyDef def = new BodyDef();
		def.type = body.isDynamic() ? BodyType.DYNAMIC : BodyType.STATIC;
		Body newBody = m_world.createBody(def);
		PolygonShape poly = new PolygonShape();
		
		switch (body.getType()) {
		case BOX: {
			BoxBody boxBody = (BoxBody) body;
			poly.setAsBox(boxBody.getWidth() / 2.0f, boxBody.getHeight() / 2.0f);
			break;
		}
		}
		
		newBody.createFixture(poly, 1.0f);
		
		Vector2f position = body.getPosition();
		Vector2f linearVelocity = body.getLinearVelocity();
		
		newBody.setTransform(new Vec2(position.x, position.y), (float) Math.toRadians(body.getRotation()));
		newBody.setLinearVelocity(new Vec2(linearVelocity.x, linearVelocity.y));
		newBody.setAngularVelocity(body.getAngularVelocity());
		
		m_bodies.put(newBody, body);
	}
	
	@Override
	public void update(double delta) {
		m_world.step((float) delta, 8, 3);
		for (Map.Entry<Body, metro_game.game.physics.bodies.Body> entry : m_bodies.entrySet()) {
			Body src = entry.getKey();
			metro_game.game.physics.bodies.Body dst = entry.getValue();
			
			Vec2 position = src.getPosition();
			Vec2 linearVelocity = src.getLinearVelocity();
			
			dst.getPosition().set(position.x, position.y);
			dst.getLinearVelocity().set(linearVelocity.x, linearVelocity.y);
			dst.setRotation((float) Math.toDegrees(src.getAngle()));
			dst.setAngularVelocity(src.getAngularVelocity());
		}
	}
}
