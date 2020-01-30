package metro_game.game.physics.bodies.modifiers;

public class BodyModifierSensor extends BodyModifier {
	private boolean m_sensor;
	
	public BodyModifierSensor(boolean sensor) {
		super(Type.SENSOR);
		m_sensor = sensor;
	}
	
	public boolean isSensor() {
		return m_sensor;
	}
}
