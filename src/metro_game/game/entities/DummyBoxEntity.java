package metro_game.game.entities;

import metro_game.game.physics.bodies.BoxBody;
import metro_game.render.primitives.ColorPrimitive;
import metro_game.render.primitives.RectPrimitive;
import metro_game.render.primitives.TextPrimitive;
import metro_game.Context;
import metro_game.game.physics.bodies.Body.BodyGameInterface;

public class DummyBoxEntity extends PhysicsEntity {
	private boolean m_fragile;
	private RectPrimitive m_rect;
	private TextPrimitive m_text;
	private BodyGameInterface m_body;
	
	public DummyBoxEntity(Context context, boolean fragile, boolean dynamic, float x, float y, float width, float height, float rotation) {
		super(context);
		m_fragile = fragile;
		addPrimitive(new ColorPrimitive(0.0f, 0.5f, 0.75f, 1.0f));
		m_rect = addPrimitive(new RectPrimitive(x, y, width, height, rotation, true));
		
		addPrimitive(new ColorPrimitive(1.0f, 0.0f, 0.0f, 1.0f));
		m_text = addPrimitive(new TextPrimitive("TEST", false, 48, 0.0f, x, y, 0.0f, TextPrimitive.AlignmentX.CENTER, TextPrimitive.AlignmentY.CENTER));
		
		m_body = addBody(new BoxBody(dynamic, x, y, width, height));
		m_body.setRotation(rotation);
	}
	
	public boolean isFragile() {
		return m_fragile;
	}
	
	@Override
	public void update(double delta) {
		m_rect.setPosition(m_body.getPositionX(), m_body.getPositionY());
		m_rect.setRotation(m_body.getRotation());
		
		m_text.setPosition(m_body.getPositionX(), m_body.getPositionY());
		m_text.setRotation(m_body.getRotation());
	}
}
