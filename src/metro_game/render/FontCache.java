package metro_game.render;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import metro_game.Context;

public class FontCache {
	private Context m_context;
	private Map<String, Map<Integer, Font>> m_cache;
	
	public FontCache(Context context) {
		m_context = context;
		m_cache = new HashMap<String, Map<Integer, Font>>();
	}
	
	public Font getFont(String name, int size) {
		Map<Integer, Font> sizeCache = null;
		if (m_cache.containsKey(name)) {
			sizeCache = m_cache.get(name);
		} else {
			sizeCache = new HashMap<Integer, Font>();
			m_cache.put(name, sizeCache);
		}
		Font cachedFont = null;
		if (sizeCache.containsKey(size)) {
			cachedFont = sizeCache.get(size);
		} else {
			try {
				cachedFont = new Font(m_context, name, size);
			} catch (IOException e) {
				System.out.println("Font " + name + " not found");
				return null;
			}
			sizeCache.put(size, cachedFont);
		}
		return cachedFont;
	}
}
