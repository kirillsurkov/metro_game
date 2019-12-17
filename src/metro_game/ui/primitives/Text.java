package metro_game.ui.primitives;

public class Text extends Primitive {
	public enum AlignmentX {
		LEFT,
		RIGHT,
		CENTER
	}
	
	public enum AlignmentY {
		TOP,
		BOTTOM,
		CENTER
	}
	
	private String m_text;
	private float m_x;
	private float m_y;
	private AlignmentX m_alignmentX;
	private AlignmentY m_alignmentY;
	
	public Text(String text, float x, float y, AlignmentX alignmentX, AlignmentY alignmentY) {
		super(Type.TEXT);
		m_text = text;
		m_x = x;
		m_y = y;
		m_alignmentX = alignmentX;
		m_alignmentY = alignmentY;
	}
	
	public String getText() {
		return m_text;
	}
	
	public float getX() {
		return m_x;
	}
	
	public float getY() {
		return m_y;
	}
	
	public AlignmentX getAlignmentX() {
		return m_alignmentX;
	}
	
	public AlignmentY getAlignmentY() {
		return m_alignmentY;
	}
}
