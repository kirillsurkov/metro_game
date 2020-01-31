package metro_game.game.physics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

import metro_game.game.entities.PhysicsEntity;
import metro_game.game.physics.Physics.OwnerBodyPair;
import metro_game.game.physics.bodies.BoxBody;
import metro_game.game.physics.bodies.CircleBody;
import metro_game.game.physics.bodies.Body.BodyPhysicsInterface;
import metro_game.game.physics.bodies.modifiers.BodyModifier;
import metro_game.game.physics.bodies.modifiers.BodyModifierAngularVelocity;
import metro_game.game.physics.bodies.modifiers.BodyModifierLinearVelocity;
import metro_game.game.physics.bodies.modifiers.BodyModifierPosition;
import metro_game.game.physics.bodies.modifiers.BodyModifierRotation;
import metro_game.game.physics.bodies.modifiers.BodyModifierSensor;

public class JBox2dEngine implements Engine, ContactListener {
	private World m_world;
	private Map<Body, OwnerBodyPair> m_bodies;
	private List<Contact> m_contactsStart;
	private List<Contact> m_contactsEnd;
	
	public JBox2dEngine() {
		m_world = new World(new Vec2(0, 0));
		m_bodies = new HashMap<Body, OwnerBodyPair>();
		m_contactsStart = new ArrayList<Contact>();
		m_contactsEnd = new ArrayList<Contact>();
		m_world.setContactListener(this);
	}
	
	@Override
	public void clearWorld() {
		Body current = m_world.getBodyList();
		while (current != null) {
			m_world.destroyBody(current);
			current = current.getNext();
		}
		m_bodies.clear();
		m_contactsStart.clear();
		m_contactsEnd.clear();
	}
	
	@Override
	public void addBody(OwnerBodyPair pair) {
		BodyPhysicsInterface body = pair.getBody();
		BodyDef def = new BodyDef();
		def.linearDamping = 0.9f;
		def.angularDamping = 0.9f;
		def.type = body.isDynamic() ? BodyType.DYNAMIC : BodyType.STATIC;
		Body newBody = m_world.createBody(def);
		Shape shape = null;
		
		switch (body.getType()) {
		case BOX: {
			BoxBody boxBody = body.cast();
			shape = new PolygonShape();
			((PolygonShape) shape).setAsBox(boxBody.getWidth() / 2.0f, boxBody.getHeight() / 2.0f);
			break;
		}
		case CIRCLE: {
			CircleBody circleBody = body.cast();
			shape = new CircleShape();
			shape.setRadius(circleBody.getRadius());
			break;
		}
		}
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1.0f;
		fixtureDef.restitution = 1.0f;
		fixtureDef.isSensor = body.isSensor();
		newBody.createFixture(fixtureDef);
		
		newBody.setTransform(new Vec2(body.getPositionX(), body.getPositionY()), (float) Math.toRadians(body.getRotation()));
		newBody.setLinearVelocity(new Vec2(body.getLinearVelocityX(), body.getLinearVelocityY()));
		newBody.setAngularVelocity(body.getAngularVelocity());
		
		m_bodies.put(newBody, pair);
	}
	
	@Override
	public void removeBody(OwnerBodyPair pair) {
		Stack<Body> toRemove = new Stack<Body>();
		for (Map.Entry<Body, OwnerBodyPair> entry : m_bodies.entrySet()) {
			if (entry.getValue() == pair) {
				Body body = entry.getKey();
				m_world.destroyBody(body);
				toRemove.push(body);
			}
		}
		while (toRemove.size() > 0) {
			m_bodies.remove(toRemove.pop());
		}
	}
	
	@Override
	public void update(double delta) {
		for (Map.Entry<Body, OwnerBodyPair> entry : m_bodies.entrySet()) {
			Body dst = entry.getKey();
			
			for (BodyModifier bodyModifier : entry.getValue().getBody().getModifiers()) {
				switch (bodyModifier.getType()) {
				case SENSOR: {
					BodyModifierSensor modifier = (BodyModifierSensor) bodyModifier;
					dst.getFixtureList().setSensor(modifier.isSensor());
					break;
				}
				case POSITION: {
					BodyModifierPosition modifier = (BodyModifierPosition) bodyModifier;
					dst.setTransform(new Vec2(modifier.getX(), modifier.getY()), dst.getAngle());
					break;
				}
				case LINEAR_VELOCITY: {
					BodyModifierLinearVelocity modifier = (BodyModifierLinearVelocity) bodyModifier;
					dst.setLinearVelocity(new Vec2(modifier.getVelocityX(), modifier.getVelocityY()));
					break;
				}
				case ROTATION: {
					BodyModifierRotation modifier = (BodyModifierRotation) bodyModifier;
					dst.setTransform(dst.getPosition(), (float) Math.toRadians(modifier.getRotation()));
					break;
				}
				case ANGULAR_VELOCITY: {
					BodyModifierAngularVelocity modifier = (BodyModifierAngularVelocity) bodyModifier;
					dst.setAngularVelocity(modifier.getAngularVelocity());
					break;
				}
				}
			}
			entry.getValue().getBody().clearModifiers();
		}
		m_world.step((float) delta, 8, 3);
		for (Map.Entry<Body, OwnerBodyPair> entry : m_bodies.entrySet()) {
			Body src = entry.getKey();
			BodyPhysicsInterface dst = entry.getValue().getBody();
			
			Vec2 position = src.getPosition();
			Vec2 linearVelocity = src.getLinearVelocity();
			
			dst.setPosition(position.x, position.y);
			dst.setLinearVelocity(linearVelocity.x, linearVelocity.y);
			dst.setRotation((float) Math.toDegrees(src.getAngle()));
			dst.setAngularVelocity(src.getAngularVelocity());
			dst.setSensor(src.getFixtureList().isSensor());
		}
		for (Contact contact : m_contactsStart) {
			PhysicsEntity entity1 = m_bodies.get(contact.getFixtureA().getBody()).getOwner();
			PhysicsEntity entity2 = m_bodies.get(contact.getFixtureB().getBody()).getOwner();
			entity1.onCollideStart(entity2);
			entity2.onCollideStart(entity1);
		}
		m_contactsStart.clear();
		for (Contact contact : m_contactsEnd) {
			PhysicsEntity entity1 = m_bodies.get(contact.getFixtureA().getBody()).getOwner();
			PhysicsEntity entity2 = m_bodies.get(contact.getFixtureB().getBody()).getOwner();
			entity1.onCollideEnd(entity2);
			entity2.onCollideEnd(entity1);
		}
		m_contactsEnd.clear();
	}

	@Override
	public void beginContact(Contact contact) {
		m_contactsStart.add(contact);
	}

	@Override
	public void endContact(Contact contact) {
		m_contactsEnd.add(contact);
	}
	
	@Override
	public void preSolve(Contact contact, Manifold manifold) {
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}
}
