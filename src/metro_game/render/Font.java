package metro_game.render;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTruetype;

import metro_game.Context;
import metro_game.Utils;

public class Font {
	public class GlyphInfo {
		private float m_advanceWidth;
		private float m_width;
		private float m_height;
		private float m_offsetX;
		private float m_offsetY;
		private int m_texID;
		
		public GlyphInfo(float advanceWidth, float width, float height, float offsetX, float offsetY, int texID) {
			m_advanceWidth = advanceWidth;
			m_width = width;
			m_height = height;
			m_offsetX = offsetX;
			m_offsetY = offsetY;
			m_texID = texID;
		}
		
		public float getAdvanceWidth() {
			return 1.0f * m_advanceWidth / m_context.getWidth();
		}
		
		public float getWidth() {
			return 1.0f * m_width / m_context.getWidth();
		}
		
		public float getHeight() {
			return 1.0f * m_height / m_context.getHeight();
		}
		
		public float getOffsetX() {
			return 1.0f * m_offsetX / m_context.getWidth();
		}
		
		public float getOffsetY() {
			return 1.0f * m_offsetY / m_context.getHeight();
		}
		
		public int getTexID() {
			return m_texID;
		}
	}
	
	private Context m_context;
	private STBTTFontinfo m_fontInfo;
	private float m_fontScale;
	private float m_fontAscent;
	private Map<Integer, GlyphInfo> m_glyphCache;
	
	public Font(Context context, String path, int size) {
		m_context = context;
		ByteBuffer ttf = null;
		try {
			ttf = Utils.ioResourceToByteBuffer(path, 512 * 1024);
		} catch (IOException e) {
			System.out.println("Font not found");
		}
		
		m_glyphCache = new HashMap<Integer, GlyphInfo>();
		
		m_fontInfo = STBTTFontinfo.create();
		if (!STBTruetype.stbtt_InitFont(m_fontInfo, ttf)) {
			System.out.println("InitFont failed");
		}
		
		m_fontScale = STBTruetype.stbtt_ScaleForPixelHeight(m_fontInfo, size);
		
		int[] ascent = new int[1];
		int[] descent = new int[1];
		int[] lineGap = new int[1];
		STBTruetype.stbtt_GetFontVMetrics(m_fontInfo, ascent, descent, lineGap);
		
		m_fontAscent = ascent[0] * m_fontScale;
	}
	
	public float getStringWidth(String str) {
		float width = 0;
		for (int i = 0; i < str.length(); i++) {
			width += renderGlyph(str.charAt(i)).getAdvanceWidth();
		}
		return width;
	}
	
	public GlyphInfo renderGlyph(int charCode) {
		if (m_glyphCache.containsKey(charCode)) {
			return m_glyphCache.get(charCode);
		}
		
		int glyphIndex = STBTruetype.stbtt_FindGlyphIndex(m_fontInfo, charCode);
		
		int[] advanceWidth = new int[1];
		int[] leftSideBearing = new int[1];
		STBTruetype.stbtt_GetGlyphHMetrics(m_fontInfo, glyphIndex, advanceWidth, leftSideBearing);

		int[] w = new int[1];
		int[] h = new int[1];
		int[] xoff = new int[1];
		int[] yoff = new int[1];
		ByteBuffer glyph = STBTruetype.stbtt_GetGlyphBitmap(m_fontInfo, m_fontScale, m_fontScale, glyphIndex, w, h, xoff, yoff);
		
		int texID = GL11.glGenTextures();
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, w[0], h[0], 0, GL11.GL_ALPHA, GL11.GL_UNSIGNED_BYTE, glyph);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		
		GlyphInfo glyphInfo = new GlyphInfo(advanceWidth[0] * m_fontScale, w[0], h[0], xoff[0], yoff[0], texID); 
		m_glyphCache.put(charCode, glyphInfo);
		return glyphInfo;
	}
	
	public float getAscent() {
		return m_fontAscent / m_context.getHeight();
	}
}
