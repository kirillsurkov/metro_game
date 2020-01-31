package metro_game.game.physics.bodies;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import metro_game.game.physics.bodies.modifiers.BodyModifier;
import metro_game.game.physics.bodies.modifiers.BodyModifierAngularVelocity;
import metro_game.game.physics.bodies.modifiers.BodyModifierLinearVelocity;
import metro_game.game.physics.bodies.modifiers.BodyModifierPosition;
import metro_game.game.physics.bodies.modifiers.BodyModifierRotation;
import metro_game.game.physics.bodies.modifiers.BodyModifierSensor;

public class Body {
	public enum Type {
		BOX,
		CIRCLE
	}
	
	private class BodyData {
		public Type type;
		public List<BodyModifier> modifiers;
		public boolean dynamic;
		public boolean sensor;
		public Vector2f position;
		public Vector2f linearVelocity;
		public float rotation;
		public float angularVelocity;
	}
	
	private class BodyReadInterface {
		protected BodyData m_data;
		
		public BodyReadInterface(BodyData data) {
			m_data = data;
		}
		
		public Type getType() {
			return m_data.type;
		}
		
		public boolean isDynamic() {
			return m_data.dynamic;
		}
		
		public boolean isSensor() {
			return m_data.sensor;
		}
		
		public float getPositionX() {
			return m_data.position.x;
		}
		
		public float getPositionY() {
			return m_data.position.y;
		}
		
		public float getLinearVelocityX() {
			return m_data.linearVelocity.x;
		}
		
		public float getLinearVelocityY() {
			return m_data.linearVelocity.y;
		}
		
		public float getRotation() {
			return m_data.rotation;
		}
		
		public float getAngularVelocity() {
			return m_data.angularVelocity;
		}
	}
	
	public class BodyPhysicsInterface extends BodyReadInterface {
		private Body m_origin;
		
		public BodyPhysicsInterface(Body origin, BodyData data) {
			super(data);
			m_origin = origin;
		}
		
		@SuppressWarnings("unchecked")
		public <T extends Body> T cast() {
			return (T) m_origin;
		}
		
		public List<BodyModifier> getModifiers() {
			return m_data.modifiers;
		}
		
		public void clearModifiers() {
			m_data.modifiers.clear();
		}
		
		public void setSensor(boolean sensor) {
			m_data.sensor = sensor;
		}
		
		public void setPosition(float x, float y) {
			m_data.position.x = x;
			m_data.position.y = y;
		}
		
		public void setLinearVelocity(float vx, float vy) {
			m_data.linearVelocity.x = vx;
			m_data.linearVelocity.y = vy;
		}
		
		public void setRotation(float degrees) {
			m_data.rotation = degrees;
		}
		
		public void setAngularVelocity(float angularVelocity) {
			m_data.angularVelocity = angularVelocity;
		}
	}
	
	public class BodyGameInterface extends BodyReadInterface {
		public BodyGameInterface(BodyData data) {
			super(data);
		}
		
		private <T extends BodyModifier> void pushModifier(T modifier) {
			m_data.modifiers.add(modifier);
		}
		
		public void setSensor(boolean sensor) {
			pushModifier(new BodyModifierSensor(sensor));
		}
		
		public void setPosition(float x, float y) {
			pushModifier(new BodyModifierPosition(x, y));
		}
		
		public void setLinearVelocity(float vx, float vy) {
			pushModifier(new BodyModifierLinearVelocity(vx, vy));
		}
		
		public void setRotation(float degrees) {
			pushModifier(new BodyModifierRotation(degrees));
		}
		
		public void setAngularVelocity(float angularVelocity) {
			pushModifier(new BodyModifierAngularVelocity(angularVelocity));
		}
	}
	
	private BodyData m_data;
	private BodyGameInterface m_gameInterface;
	private BodyPhysicsInterface m_physicsInterface;
	
	public Body(Type type, boolean dynamic, float x, float y, float xVel, float yVel, float rotation, float angVel) {
		m_data = new BodyData();
		m_gameInterface = new BodyGameInterface(m_data);
		m_physicsInterface = new BodyPhysicsInterface(this, m_data);
		
		m_data.type = type;
		m_data.modifiers = new ArrayList<BodyModifier>();
		m_data.dynamic = dynamic;
		m_data.sensor = false;
		m_data.position = new Vector2f(x, y);
		m_data.linearVelocity = new Vector2f(xVel, yVel);
		m_data.rotation = rotation;
		m_data.angularVelocity = angVel;
	}
	
	public BodyGameInterface getGameInterface() {
		return m_gameInterface;
	}
	
	public BodyPhysicsInterface getPhysicsInterface() {
		return m_physicsInterface;
	}
}
