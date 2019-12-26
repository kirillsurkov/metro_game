package metro_game.render.primitives;

import org.joml.Vector2f;
import org.joml.Vector4f;

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
	private int m_fontSize;
	private float m_border;
	private Vector4f m_borderColor;
	private Vector2f m_position;
	private float m_rotation;
	private AlignmentX m_alignmentX;
	private AlignmentY m_alignmentY;
	
	public TextPrimitive(String text, boolean translated, int fontSize, float border, float x, float y, float rotation, AlignmentX alignmentX, AlignmentY alignmentY) {
		super(Type.TEXT);
		m_font = "NotoSerif-Regular.ttf";
		m_text = text;
		m_translated = translated;
		m_fontSize = fontSize;
		m_border = border;
		m_borderColor = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
		m_position = new Vector2f(x, y);
		m_rotation = rotation;
		m_alignmentX = alignmentX;
		m_alignmentY = alignmentY;
	}
	
	public void setFont(String font) {
		m_font = font;
	}
	
	public String getFont() {
		return m_font;
	}
	
	public void setText(String text) {
		m_text = text;
	}
	
	public String getText() {
		return m_text;
	}
	
	public void setTranslated(boolean translated) {
		m_translated = translated;
	}
	
	public boolean isTranslated() {
		return m_translated;
	}
	
	public void setFontSize(int fontSize) {
		m_fontSize = fontSize;
	}
	
	public int getFontSize() {
		return m_fontSize;
	}
	
	public void setBorder(float border) {
		m_border = border;
	}
	
	public float getBorder() {
		return m_border;
	}
	
	public Vector4f getBorderColor() {
		return m_borderColor;
	}
	
	public Vector2f getPosition() {
		return m_position;
	}
	
	public void setRotation(float rotation) {
		m_rotation = rotation;
	}
	
	public float getRotation() {
		return m_rotation;
	}
	
	public void setAlignmentX(AlignmentX alignment) {
		m_alignmentX = alignment;
	}
	
	public AlignmentX getAlignmentX() {
		return m_alignmentX;
	}
	
	public void setAlignmentY(AlignmentY alignment) {
		m_alignmentY = alignment;
	}
	
	public AlignmentY getAlignmentY() {
		return m_alignmentY;
	}
}
