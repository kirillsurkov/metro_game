package metro_game;

import java.util.HashMap;
import java.util.Map;

public class Strings {
	private Map<String, String> m_translations;
	
	public enum Language {
		EN,
		RU
	}
	
	public Strings(Language language) {
		m_translations = new HashMap<String, String>();
		switch(language) {
		case EN: {
			m_translations.put("MENU_LEVELS", "Levels");
			m_translations.put("MENU_SETTINGS", "Settings");
			m_translations.put("MENU_LANG", "Language");
			m_translations.put("MENU_EXIT", "Exit game");
		}
		case RU: {
			m_translations.put("MENU_LEVELS", "Уровни");
			m_translations.put("MENU_SETTINGS", "Настройки");
			m_translations.put("MENU_LANG", "Язык");
			m_translations.put("MENU_EXIT", "Выйти из игры");
		}
		}
	}
	
	public String get(String key) {
		return m_translations.containsKey(key) ? m_translations.get(key) : key;
	}
}
