package metro_game;

import java.util.HashMap;
import java.util.Map;

public class Strings {
	public enum Language {
		EN,
		RU
	}
	
	private Map<Language, Map<String, String>> m_translations;
	private Language m_language;
	
	public Strings(Language language) {
		m_language = language;
		m_translations = new HashMap<Language, Map<String, String>>();
		initTranslations();
	}
	
	private void initTranslations() {
		Map<String, String> translations;
		
		translations = new HashMap<String, String>();
		m_translations.put(Language.EN, translations);
		translations.put("MENU_LEVELS", "Levels");
		translations.put("MENU_SETTINGS", "Settings");
		translations.put("MENU_LANG", "Language");
		translations.put("MENU_EXIT", "Exit game");
		translations.put("MENU_BACK", "Back");
		translations.put("ALERT_EXIT_CONFIRM", "Are you sure you want to exit?");
		translations.put("ALERT_EXIT_TO_LEVELS", "Exit to levels?");
		translations.put("ALERT_BUTTON_YES", "Yes");
		translations.put("ALERT_BUTTON_NO", "No");
		translations.put("ALERT_BUTTON_EXIT", "Exit");
		translations.put("ALERT_BUTTON_RESUME", "Resume");
		
		translations = new HashMap<String, String>();
		m_translations.put(Language.RU, translations);
		translations.put("MENU_LEVELS", "Уровни");
		translations.put("MENU_SETTINGS", "Настройки");
		translations.put("MENU_LANG", "Язык");
		translations.put("MENU_EXIT", "Выйти из игры");
		translations.put("MENU_BACK", "Назад");
		translations.put("ALERT_EXIT_CONFIRM", "Вы точно хотите выйти?");
		translations.put("ALERT_EXIT_TO_LEVELS", "Вернуться к выбору уровня?");
		translations.put("ALERT_BUTTON_YES", "Да");
		translations.put("ALERT_BUTTON_NO", "Нет");
		translations.put("ALERT_BUTTON_EXIT", "Выйти");
		translations.put("ALERT_BUTTON_RESUME", "Продолжить");
	}

	public void setLanguage(Language language) {
		m_language = language;
	}
	
	public Language getLanguage() {
		return m_language;
	}
	
	public String getString(String key) {
		Map<String, String> translations = m_translations.get(m_language);
		return translations.containsKey(key) ? translations.get(key) : key;
	}
}
