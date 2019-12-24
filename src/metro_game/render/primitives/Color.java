package metro_game.render.primitives;

public class Color extends Primitive {
	private float m_r;
	private float m_g;
	private float m_b;
	private float m_a;
	
	public Color(float r, float g, float b, float a) {
		super(Type.COLOR);
		set(r, g, b, a);
	}
	
	public void set(float r, float g, float b, float a) {
		m_r = r;
		m_g = g;
		m_b = b;
		m_a = a;
	}
	
	public float getR() {
		return m_r;
	}
	
	public float getG() {
		return m_g;
	}
	
	public float getB() {
		return m_b;
	}
	
	public float getA() {
		return m_a;
	}
}
