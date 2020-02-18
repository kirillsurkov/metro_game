package metro_game.game.entities;

import org.joml.Vector2f;

import metro_game.Context;
import metro_game.game.physics.bodies.CircleBody;
import metro_game.game.physics.bodies.Body.BodyGameInterface;
import metro_game.render.primitives.CirclePrimitive;
import metro_game.render.primitives.ColorPrimitive;
import metro_game.render.primitives.ParticleEmitterPrimitive;
import metro_game.render.primitives.ShaderPrimitive;
import metro_game.render.primitives.TrailPrimitive;
import metro_game.render.primitives.ShaderPrimitive.ShaderType;

public class ElectronEntity extends PhysicsEntity {
	private CirclePrimitive m_circle;
	private TrailPrimitive m_trail;
	private ParticleEmitterPrimitive m_particleEmitterPrimitive;
	private BodyGameInterface m_body;
	
	public ElectronEntity(Context context, float x, float y) {
		super(context);
		float radius = 0.05f;
		
		addPrimitive(new ShaderPrimitive(ShaderType.TRAIL));
		addPrimitive(new ColorPrimitive(0.0f, 1.0f, 1.0f, 0.25f));
		m_trail = addPrimitive(new TrailPrimitive());
		
		addPrimitive(new ShaderPrimitive(ShaderType.PARTICLE));
		addPrimitive(new ColorPrimitive(1.0f, 1.0f, 1.0f, 1.0f));
		m_particleEmitterPrimitive = addPrimitive(new ParticleEmitterPrimitive(x, y));
		
		addPrimitive(new ShaderPrimitive(ShaderType.DEFAULT_GAME));
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

		m_circle.setPosition(srcX, srcY);
		m_circle.setRotation(m_body.getRotation());
		
		m_trail.update(delta, srcX, srcY);
		
		m_particleEmitterPrimitive.setPosition(srcX, srcY);
		
		m_particleEmitterPrimitive.setEmitCount(1);
		
		float[] dstX = new float[1];
		float[] dstY = new float[1];
		getPosition(delta, dstX, dstY);
		
		Vector2f vel = new Vector2f(dstX[0] - srcX, dstY[0] - srcY);
		vel.mul(60.0f);
		
		m_body.applyLinearImpulse(vel.x - m_body.getLinearVelocityX(), vel.y - m_body.getLinearVelocityY());
	}
}
