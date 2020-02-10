package metro_game.game.scenes.quantum;

import metro_game.Context;
import metro_game.game.Route;
import metro_game.game.entities.ElectronEntity;
import metro_game.game.entities.EnemyEntity;
import metro_game.game.entities.PlayerEntityGolf;

public class LevelQuantum0 extends LevelQuantumBase {
	public LevelQuantum0(Context context) {
		super(context);
	}
	
	@Override
	public void init() {
		int electrons = 35;
		float atomX = 0.0f;
		float atomY = 0.0f;
		float atomRadius = 4.0f;
		float atomRadiusStep = 2.0f;
		addGameEntity(new EnemyEntity(m_context, Route.Mode.LOOP_START_END, atomX, atomY, 0.0f));
		
		int level = 0;
		int index = 0;
		int maxIndex = 2;
		float radius = atomRadius;
		for (int i = 0; i < electrons; i++) {
			System.out.println("Level " + level + ", index: " + index + " / " + maxIndex);
			float ang = (float) (Math.PI  * 2.0f * 1.0f * index / maxIndex);
			float x = atomX + (float) Math.cos(ang) * radius;
			float y = atomY + (float) Math.sin(ang) * radius;
			float currentRadius = radius;
			addGameEntity(new ElectronEntity(m_context, x, y) {
				private double m_timer = 0.0;
				
				@Override
				public void getPosition(double delta, float[] x, float[] y) {
					m_timer += delta;
					x[0] = atomX + (float) Math.cos(0.5f * m_timer * Math.PI + ang) * currentRadius;
					y[0] = atomY + (float) Math.sin(0.5f * m_timer * Math.PI + ang) * currentRadius;
				}
			});
			index++;
			if (index >= maxIndex) {
				index = 0;
				level++;
				maxIndex = Math.min(2 * (level + 1) * (level + 1), electrons - i - 1);
				radius += Math.pow(atomRadiusStep, 3) / Math.pow(level + 1, 2);
			}
		}
		
		addGameEntity(new PlayerEntityGolf(m_context, 0.0f, 6.0f));
	}
}
