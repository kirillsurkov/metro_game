package metro_game;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.joml.Vector3f;

public class Utils {
	public static boolean pointInside(float x0, float y0, float x1, float y1, float dx1, float dy1) {
		return x0 >= x1 && x0 <= x1 + dx1 && y0 >= y1 && y0 <= y1 + dy1;
	}
	
	public static boolean intersects(float x0, float y0, float dx0, float dy0, float x1, float y1, float dx1, float dy1) {
		return true;
	}
	
	public static ByteBuffer readFile(String path) throws IOException {
		byte[] bytes = Files.readAllBytes(Paths.get(path));
		ByteBuffer buf = ByteBuffer.allocateDirect(bytes.length).order(ByteOrder.nativeOrder());
		buf.put(bytes);
		return (ByteBuffer) buf.flip();
	}
	
	public static Vector3f hsv2rgb(float h, float s, float v) {
		Vector3f rgb = new Vector3f();
		float v_min = (1.0f - s) * v;
		float a = (v - v_min) * ((h * 360.0f) % 60) / 60.0f;
		float v_inc = v_min + a;
		float v_dec = v - a;
		
		switch ((int) (h * 6.0f) % 6) {
		case 0: {
			rgb.set(v, v_inc, v_min);
			break;
		}
		case 1: {
			rgb.set(v_dec, v, v_min);
			break;
		}
		case 2: {
			rgb.set(v_min, v, v_inc);
			break;
		}
		case 3: {
			rgb.set(v_min, v_dec, v);
			break;
		}
		case 4: {
			rgb.set(v_inc, v_min, v);
			break;
		}
		case 5: {
			rgb.set(v, v_min, v_dec);
			break;
		}
		}
		
		return rgb;
	}
}
