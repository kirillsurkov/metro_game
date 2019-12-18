package metro_game.scenes;

import metro_game.Context;
import metro_game.ui.AlertWidget;

public class LevelBase extends Scene {
	public LevelBase(Context context) {
		super(context);
	}
	
	@Override
	public boolean onBack() {
		if (getAlert() == null) {
			setAlert(new AlertWidget(m_context, m_context.getString("ALERT_EXIT_TO_LEVELS"), new String[] {"ALERT_BUTTON_EXIT", "ALERT_BUTTON_RESUME"}) {
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
