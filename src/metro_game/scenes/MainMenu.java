package metro_game.scenes;

import metro_game.Context;
import metro_game.game.events.SwitchSceneEvent;
import metro_game.ui.AlertWidget;
import metro_game.ui.ButtonWidget;
import metro_game.ui.events.BackEvent;

public class MainMenu extends Scene {
	public MainMenu(Context context) {
		super(context);
		
		addUIChild(new ButtonWidget(context, context.getString("MENU_LEVELS"), 0.25f, 0.25f, 0.5f, 0.1f) {
			@Override
			protected void onClick(boolean up) {
				super.onClick(up);
				if (up) {
					context.getGameEvents().pushEvent(new SwitchSceneEvent(new MainMenuLevels(context)));
				}
			}
		});
		addUIChild(new ButtonWidget(context, context.getString("MENU_SETTINGS"), 0.25f, 0.4f, 0.5f, 0.1f) {
			@Override
			protected void onClick(boolean up) {
				super.onClick(up);
				if (up) {
					context.getGameEvents().pushEvent(new SwitchSceneEvent(new MainMenuSettings(context)));
				}
			}
		});
		addUIChild(new ButtonWidget(context, context.getString("MENU_LANG"), 0.25f, 0.55f, 0.5f, 0.1f));
		addUIChild(new ButtonWidget(context, context.getString("MENU_EXIT"), 0.25f, 0.7f, 0.5f, 0.1f) {
			@Override
			protected void onClick(boolean up) {
				super.onClick(up);
				if (up) {
					context.getInputEvents().pushEvent(new BackEvent());
				}
			}
		});
	}
	
	@Override
	public boolean onBack() {
		if (getAlert() == null) {
			setAlert(new AlertWidget(m_context, m_context.getString("ALERT_EXIT_CONFIRM"), new String[] {m_context.getString("ALERT_BUTTON_YES"), m_context.getString("ALERT_BUTTON_NO")}) {
				@Override
				public void onButton(int index) {
					if (index == 0) {
						setNeedClose(true);
					}
					closeAlert();
				}
			});
		} else {
			closeAlert();
		}
		return true;
	}
}