package metro_game.render;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTruetype;

import metro_game.Context;
import metro_game.Utils;

public class Font {
	public class GlyphInfo {
		private boolean m_isDrawable;
		private float m_advanceWidth;
		private float m_width;
		private float m_height;
		private float m_offsetX;
		private float m_offsetY;
		private int m_texID;
		
		public GlyphInfo(float advanceWidth) {
			m_isDrawable = false;
			m_advanceWidth = advanceWidth;
		}
		
		public GlyphInfo(float advanceWidth, float width, float height, float offsetX, float offsetY, int texID) {
			m_isDrawable = true;
			m_advanceWidth = advanceWidth;
			m_width = width;
			m_height = height;
			m_offsetX = offsetX;
			m_offsetY = offsetY;
			m_texID = texID;
		}
		
		public boolean isDrawable() {
			return m_isDrawable;
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
	private float m_fontDescent;
	private Map<Integer, GlyphInfo> m_glyphCache;
	
	public static int sdfPadding = 8;
	public static int sdfOnEdge = 180;
	
	public Font(Context context, String path, int size) throws IOException {
		m_context = context;
		ByteBuffer ttf = Utils.readFile("res/fonts/" + path);
		
		m_glyphCache = new HashMap<Integer, GlyphInfo>();
		
		m_fontInfo = STBTTFontinfo.create();
		if (!STBTruetype.stbtt_InitFont(m_fontInfo, ttf)) {
			System.out.println("InitFont failed");
		}
		
		m_fontScale = STBTruetype.stbtt_ScaleForMappingEmToPixels(m_fontInfo, size);
		
		int[] ascent = new int[1];
		int[] descent = new int[1];
		int[] lineGap = new int[1];
		STBTruetype.stbtt_GetFontVMetrics(m_fontInfo, ascent, descent, lineGap);
		
		m_fontAscent = ascent[0] * m_fontScale;
		m_fontDescent = descent[0] * m_fontScale;
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
		
		int[] advanceWidth = new int[1];
		int[] leftSideBearing = new int[1];
		STBTruetype.stbtt_GetCodepointHMetrics(m_fontInfo, charCode, advanceWidth, leftSideBearing);

		GlyphInfo glyphInfo;

		boolean drawable = STBTruetype.stbtt_GetCodepointBox(m_fontInfo, charCode, (int[]) null, null, null, null);
		if (drawable) {
			int[] w = new int[1];
			int[] h = new int[1];
			int[] xoff = new int[1];
			int[] yoff = new int[1];
			ByteBuffer glyph = STBTruetype.stbtt_GetCodepointSDF(m_fontInfo, m_fontScale, charCode, sdfPadding, (byte) sdfOnEdge, 1.0f * sdfOnEdge / sdfPadding, w, h, xoff, yoff);
	
			int texID = GL30.glGenTextures();
			GL30.glPixelStorei(GL30.GL_UNPACK_ALIGNMENT, 1);
			GL30.glBindTexture(GL30.GL_TEXTURE_2D, texID);
			GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGBA, w[0], h[0], 0, GL30.GL_RED, GL30.GL_UNSIGNED_BYTE, glyph);
			GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR);
			GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR);
			GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_S, GL30.GL_CLAMP_TO_EDGE);
			GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_T, GL30.GL_CLAMP_TO_EDGE);
			
			glyphInfo = new GlyphInfo(advanceWidth[0] * m_fontScale, w[0], h[0], xoff[0], yoff[0], texID);
		} else {
			glyphInfo = new GlyphInfo(advanceWidth[0] * m_fontScale);
		}
		
		m_glyphCache.put(charCode, glyphInfo);
		return glyphInfo;
	}
	
	public float getAscent() {
		return m_fontAscent / m_context.getHeight();
	}
	
	public float getDescent() {
		return m_fontDescent / m_context.getHeight();
	}
}
