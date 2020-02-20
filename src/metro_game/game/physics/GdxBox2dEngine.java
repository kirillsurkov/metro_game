package metro_game.game.physics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import metro_game.game.entities.PhysicsEntity;
import metro_game.game.physics.Physics.OwnerBodyPair;
import metro_game.game.physics.bodies.BoxBody;
import metro_game.game.physics.bodies.CircleBody;
import metro_game.game.physics.bodies.Body.BodyPhysicsInterface;
import metro_game.game.physics.bodies.modifiers.BodyModifier;
import metro_game.game.physics.bodies.modifiers.BodyModifierAngularVelocity;
import metro_game.game.physics.bodies.modifiers.BodyModifierLinearImpulse;
import metro_game.game.physics.bodies.modifiers.BodyModifierLinearVelocity;
import metro_game.game.physics.bodies.modifiers.BodyModifierPosition;
import metro_game.game.physics.bodies.modifiers.BodyModifierRotation;
import metro_game.game.physics.bodies.modifiers.BodyModifierSensor;

public class GdxBox2dEngine implements Engine, ContactListener {
	private World m_world;
	private Map<Body, OwnerBodyPair> m_bodies;
	private List<Contact> m_contactsStart;
	private List<Contact> m_contactsEnd;
	
	public GdxBox2dEngine() {
		m_world = new World(new Vector2(0, 0), false);
		m_bodies = new HashMap<Body, OwnerBodyPair>();
		m_contactsStart = new ArrayList<Contact>();
		m_contactsEnd = new ArrayList<Contact>();
		m_world.setContactListener(this);
	}
	
	@Override
	public void clearWorld() {
		Array<Body> bodies = new Array<Body>();
		m_world.getBodies(bodies);
		for (Body body : bodies) {
			m_world.destroyBody(body);
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
		def.type = body.isDynamic() ? BodyType.DynamicBody : BodyType.StaticBody;
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
		
		newBody.setTransform(new Vector2(body.getPositionX(), body.getPositionY()), (float) Math.toRadians(body.getRotation()));
		newBody.setLinearVelocity(new Vector2(body.getLinearVelocityX(), body.getLinearVelocityY()));
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
					dst.getFixtureList().get(0).setSensor(modifier.isSensor());
					break;
				}
				case POSITION: {
					BodyModifierPosition modifier = (BodyModifierPosition) bodyModifier;
					dst.setTransform(new Vector2(modifier.getX(), modifier.getY()), dst.getAngle());
					break;
				}
				case LINEAR_IMPULSE: {
					BodyModifierLinearImpulse modifier = (BodyModifierLinearImpulse) bodyModifier;
					float mass = dst.getMass();
					dst.applyLinearImpulse(new Vector2(modifier.getImpulseX() * mass, modifier.getImpulseY() * mass), dst.getWorldCenter(), true);
					break;
				}
				case LINEAR_VELOCITY: {
					BodyModifierLinearVelocity modifier = (BodyModifierLinearVelocity) bodyModifier;
					dst.setLinearVelocity(new Vector2(modifier.getVelocityX(), modifier.getVelocityY()));
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
			
			Vector2 position = src.getPosition();
			Vector2 linearVelocity = src.getLinearVelocity();
			
			dst.setPosition(position.x, position.y);
			dst.setLinearVelocity(linearVelocity.x, linearVelocity.y);
			dst.setRotation((float) Math.toDegrees(src.getAngle()));
			dst.setAngularVelocity(src.getAngularVelocity());
			dst.setSensor(src.getFixtureList().get(0).isSensor());
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
		PhysicsEntity entity1 = m_bodies.get(contact.getFixtureA().getBody()).getOwner();
		PhysicsEntity entity2 = m_bodies.get(contact.getFixtureB().getBody()).getOwner();
		if (!entity1.isNeedCollide(entity2) || !entity2.isNeedCollide(entity1)) {
			contact.setEnabled(false);
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}
}