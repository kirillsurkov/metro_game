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
			m_translations.put("MENU_BACK", "Back");
			m_translations.put("ALERT_EXIT_CONFIRM", "Are you sure you want to exit?");
			m_translations.put("ALERT_BUTTON_YES", "Yes");
			m_translations.put("ALERT_BUTTON_NO", "No");
			break;
		}
		case RU: {
			m_translations.put("MENU_LEVELS", "Уровни");
			m_translations.put("MENU_SETTINGS", "Настройки");
			m_translations.put("MENU_LANG", "Язык");
			m_translations.put("MENU_EXIT", "Выйти из игры");
			m_translations.put("MENU_BACK", "Назад");
			m_translations.put("ALERT_EXIT_CONFIRM", "Вы точно хотите выйти?");
			m_translations.put("ALERT_BUTTON_YES", "Да");
			m_translations.put("ALERT_BUTTON_NO", "Нет");
			break;
		}
		}
	}
	
	public String get(String key) {
		return m_translations.containsKey(key) ? m_translations.get(key) : key;
	}
}
