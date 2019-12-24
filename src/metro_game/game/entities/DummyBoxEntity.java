package metro_game.game.entities;

import metro_game.game.physics.bodies.BoxBody;
import metro_game.render.primitives.Rect;
import metro_game.render.primitives.ShaderPrimitive;
import metro_game.render.primitives.ShaderPrimitive.ShaderType;
import metro_game.Context;
import metro_game.game.physics.bodies.Body;

public class DummyBoxEntity extends GameEntity {
	private boolean m_fragile;
	private Rect m_rect;
	private Body m_body;
	
	public DummyBoxEntity(Context context, boolean fragile, boolean dynamic, float x, float y, float width, float height, float rotation) {
		super(context);
		m_fragile = fragile;
		addPrimitive(new ShaderPrimitive(ShaderType.DEFAULT_GAME));
		m_rect = addPrimitive(new Rect(x, y, width, height, rotation));
		m_body = addBody(new BoxBody(dynamic, x, y, width, height));
		m_body.setRotation(rotation);
	}
	
	public boolean isFragile() {
		return m_fragile;
	}
	
	@Override
	public void update(double delta) {
		m_rect.getPosition().set(m_body.getPosition());
		m_rect.setRotation(m_body.getRotation());
	}
}
