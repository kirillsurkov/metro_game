package metro_game.render.primitives;

public class TextPrimitive extends Primitive {
	public enum AlignmentX {
		LEFT,
		CENTER
	}
	
	public enum AlignmentY {
		TOP,
		CENTER
	}
	
	private String m_font;
	private String m_text;
	private boolean m_translated;
	private int m_size;
	private float m_x;
	private float m_y;
	private AlignmentX m_alignmentX;
	private AlignmentY m_alignmentY;
	
	public TextPrimitive(String text, boolean translated, int size, float x, float y, AlignmentX alignmentX, AlignmentY alignmentY) {
		super(Type.TEXT);
		m_font = "NotoSerif-Regular.ttf";
		m_text = text;
		m_translated = translated;
		m_size = size;
		m_x = x;
		m_y = y;
		m_alignmentX = alignmentX;
		m_alignmentY = alignmentY;
	}
	
	public String getFont() {
		return m_font;
	}
	
	public String getText() {
		return m_text;
	}
	
	public boolean isTranslated() {
		return m_translated;
	}
	
	public int getSize() {
		return m_size;
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
