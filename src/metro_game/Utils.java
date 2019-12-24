package metro_game;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Paths;

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
		return buf.flip();
	}
}
