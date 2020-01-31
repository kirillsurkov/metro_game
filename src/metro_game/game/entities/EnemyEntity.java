package metro_game.game.entities;

import metro_game.Context;
import metro_game.game.physics.bodies.Body.BodyGameInterface;
import metro_game.game.entities.ChairEntity.ChairOccupier;
import metro_game.game.physics.bodies.CircleBody;
import metro_game.render.primitives.CirclePrimitive;
import metro_game.render.primitives.ColorPrimitive;
import metro_game.render.primitives.ShaderPrimitive;
import metro_game.render.primitives.ShaderPrimitive.ShaderType;

public class EnemyEntity extends PhysicsEntity implements ChairOccupier {
	private CirclePrimitive m_circle;
	private BodyGameInterface m_body;
	
	public EnemyEntity(Context context, float x, float y) {
		super(context);
		float radius = 0.6f;
		
		addPrimitive(new ShaderPrimitive(ShaderType.DEFAULT_GAME));
		addPrimitive(new ColorPrimitive(1.0f, 0.5f, 0.0f, 1.0f));
		m_circle = addPrimitive(new CirclePrimitive(x, y, radius, 0.0f));
		
		m_body = addBody(new CircleBody(true, x, y, 0.0f, 0.0f, 0.0f, 0.0f, radius));
	}
	
	@Override
	public void update(double delta) {
		m_circle.setPosition(m_body.getPositionX(), m_body.getPositionY());
		m_circle.setRotation(m_body.getRotation());
	}
	
	@Override
	public void stickToChair(float chairX, float chairY) {
		m_body.setPosition(chairX, chairY);
		m_body.setLinearVelocity(0.0f, 0.0f);
		m_body.setRotation(0.0f);
		m_body.setAngularVelocity(0.0f);
	}
}
