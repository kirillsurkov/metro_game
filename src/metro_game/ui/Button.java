package metro_game.ui;

import metro_game.Context;
import metro_game.ui.primitives.Color;
import metro_game.ui.primitives.Rect;
import metro_game.ui.primitives.Text;
import metro_game.ui.primitives.Text.AlignmentX;
import metro_game.ui.primitives.Text.AlignmentY;

public class Button extends Widget {
	private Color m_color;
	
	public Button(Context context, String text, float x, float y, float width, float height) {
		super(context, x, y, width, height);
		m_color = new Color(1.0f, 0.0f, 0.0f, 1.0f);
		addPrimitive(m_color);
		addPrimitive(new Rect(0, 0, width, height));
		addPrimitive(new Text(text, x + width / 2, y + height / 2, AlignmentX.CENTER, AlignmentY.CENTER));
	}
	
	@Override
	public void onHover(boolean onWidget) {
		if (onWidget) {
			m_color.set(1.0f, 0.25f, 0.25f, 1.0f);
		} else {
			m_color.set(1.0f, 0.0f, 0.0f, 1.0f);
		}
	}
	
	@Override
	protected void onClick(boolean up) {
		if (up) {
			m_color.set(1.0f, 0.25f, 0.25f, 1.0f);
		} else {
			m_color.set(0.5f, 0.0f, 0.0f, 1.0f);
		}
	}
	
	public void update(float delta) {
	}
}
