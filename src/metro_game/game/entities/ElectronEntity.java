package metro_game.game.entities;

import org.joml.Vector2f;
import org.joml.Vector3f;

import metro_game.Context;
import metro_game.Utils;
import metro_game.game.physics.bodies.CircleBody;
import metro_game.game.physics.bodies.Body.BodyGameInterface;
import metro_game.render.primitives.CirclePrimitive;
import metro_game.render.primitives.ColorPrimitive;
import metro_game.render.primitives.ParticleEmitterPrimitive;

public class ElectronEntity extends PhysicsEntity {
	private CirclePrimitive m_circle;
	private ParticleEmitterPrimitive m_particleEmitterPrimitive;
	private BodyGameInterface m_body;
	private int m_color;
	
	public ElectronEntity(Context context, float x, float y, float lifetime) {
		super(context);
		float radius = 0.05f;
		m_color = 0;
		
		m_particleEmitterPrimitive = addPrimitive(new ParticleEmitterPrimitive(lifetime));
		
		addPrimitive(new ColorPrimitive(0.0f, 1.0f, 1.0f, 1.0f));
		m_circle = addPrimitive(new CirclePrimitive(x, y, radius, 0.0f));
		m_body = addBody(new CircleBody(true, x, y, 0.0f, 0.0f, 0.0f, 0.0f, radius));
	}
	
	public void getPosition(double delta, float[] x, float[] y) {
		x[0] = m_body.getPositionX();
		y[0] = m_body.getPositionY();
	}
	
	@Override
	public boolean isNeedCollide(PhysicsEntity physicsEntity) {
		return !(physicsEntity instanceof ElectronEntity);
	}
	
	@Override
	public void update(double delta) {
		float srcX = m_body.getPositionX();
		float srcY = m_body.getPositionY();
		
		Vector2f dpos = new Vector2f(m_circle.getPositionX(), m_circle.getPositionY()).sub(srcX, srcY);
		int count = (int) Math.ceil(dpos.length() / 0.1f);
		for (int i = 0; i < count; i++) {
			float x = srcX + dpos.x * (float) i / count;
			float y = srcY + dpos.y * (float) i / count;
			Vector3f color = Utils.hsv2rgb((m_color++ % 256) / 256.0f, 1.0f, 1.0f);
			m_particleEmitterPrimitive.emit(x, y, color.x, color.y, color.z);
		}

		m_circle.setPosition(srcX, srcY);
		m_circle.setRotation(m_body.getRotation());
		
		float[] dstX = new float[1];
		float[] dstY = new float[1];
		getPosition(delta, dstX, dstY);
		
		Vector2f vel = new Vector2f(dstX[0] - srcX, dstY[0] - srcY);
		vel.mul(60.0f);
		
		m_body.applyLinearImpulse(vel.x - m_body.getLinearVelocityX(), vel.y - m_body.getLinearVelocityY());
	}
}
