package metro_game.scenes;

import metro_game.Context;
import metro_game.ui.ButtonWidget;
import metro_game.ui.events.BackEvent;

public class MainMenuSettings extends Scene {
	public MainMenuSettings(Context context) {
		super(context);
		addUIChild(new ButtonWidget(context, "MENU_BACK", 0.25f, 0.7f, 0.5f, 0.1f) {
			@Override
			protected void onClick(boolean up) {
				super.onClick(up);
				if (up) {
					context.getInputEvents().pushEvent(new BackEvent());
				}
			}
		});
	}
}
