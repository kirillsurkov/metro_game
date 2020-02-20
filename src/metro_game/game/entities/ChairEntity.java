package metro_game.game.entities;

import metro_game.Context;
import metro_game.game.physics.bodies.BoxBody;
import metro_game.game.physics.bodies.Body.BodyGameInterface;
import metro_game.render.primitives.ColorPrimitive;
import metro_game.render.primitives.RectPrimitive;

public class ChairEntity extends PhysicsEntity {
	public interface ChairOccupier {
		void stickToChair(float chairX, float chairY);
	}
	
	private RectPrimitive m_rect;
	private BodyGameInterface m_body;
	private ChairOccupier m_occupier;
	private boolean m_sticked;
	
	public ChairEntity(Context context, float x, float y) {
		super(context);
		addPrimitive(new ColorPrimitive(1.0f, 0.0f, 0.0f, 1.0f));
		m_rect = addPrimitive(new RectPrimitive(x, y, 1.5f, 1.5f, 0.0f, true));
		m_body = addBody(new BoxBody(false, x, y, 1.5f, 1.5f));
		m_sticked = false;
	}
	
	private void setOccupiedBy(ChairOccupier occupier) {
		if (m_occupier == null) {
			m_occupier = occupier;
			m_sticked = false;
		}
	}
	
	@Override
	public void onCollideStart(PhysicsEntity physicsEntity) {
		if (physicsEntity instanceof ChairOccupier) {
			setOccupiedBy((ChairOccupier) physicsEntity);
		}
	}
	
	@Override
	public void onCollideEnd(PhysicsEntity physicsEntity) {
		if (physicsEntity == m_occupier) {
			m_occupier = null;
		}
	}
	
	@Override
	public boolean isNeedCollide(PhysicsEntity physicsEntity) {
		return physicsEntity != m_occupier;
	}
	
	@Override
	public void update(double delta) {
		m_rect.setPosition(m_body.getPositionX(), m_body.getPositionY());
		m_rect.setRotation(m_body.getRotation());
		
		if (m_occupier != null && !m_sticked) {
			m_occupier.stickToChair(m_body.getPositionX(), m_body.getPositionY());
			m_sticked = true;
		}
	}
}
