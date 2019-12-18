package metro_game.ui;

import metro_game.Context;
import metro_game.ui.primitives.Color;
import metro_game.ui.primitives.Rect;
import metro_game.ui.primitives.Text;
import metro_game.ui.primitives.Text.AlignmentX;
import metro_game.ui.primitives.Text.AlignmentY;

public class ButtonWidget extends Widget {
	private Color m_color;
	
	public ButtonWidget(Context context, String text, float x, float y, float width, float height) {
		super(context, x, y, width, height);
		m_color = new Color(1.0f, 0.0f, 0.0f, 1.0f);
		addPrimitive(m_color);
		addPrimitive(new Rect(0, 0, width, height));
		addPrimitive(new Color(1.0f, 1.0f, 1.0f, 1.0f));
		addPrimitive(new Text(text, true, 56, width / 2.0f, 0, AlignmentX.CENTER, AlignmentY.TOP));
	}
	
	@Override
	public void onHover(boolean onWidget) {
		if (onWidget) {
			if (isMouseDown()) {
				m_color.set(0.5f, 0.0f, 0.0f, 1.0f);
			} else {
				m_color.set(1.0f, 0.25f, 0.25f, 1.0f);
			}
		} else {
			m_color.set(1.0f, 0.0f, 0.0f, 1.0f);
		}
	}
}
