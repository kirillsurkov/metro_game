package metro_game;

import org.lwjgl.glfw.*;

import metro_game.ui.events.BackEvent;
import metro_game.ui.events.MouseButtonEvent;

public class Window {
	private long m_window;
	
	public Window(Context context) {
		GLFWErrorCallback.createPrint(System.err).set();
		
		if (!GLFW.glfwInit()) {
			throw new IllegalStateException("glfwInit failed");
		}
		
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		
		m_window = GLFW.glfwCreateWindow(context.getWidth(), context.getHeight(), "Game", 0, 0);
		
		GLFW.glfwSetKeyCallback(m_window, (wnd, key, scancode, action, mode) -> {
			if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
				context.getInputEvents().pushEvent(new BackEvent());
			}
		});
		
		GLFW.glfwSetCursorPosCallback(m_window, (wnd, x, y) -> {
			context.setMousePos((int) x, (int) y);
		});
		
		GLFW.glfwSetMouseButtonCallback(m_window, (wnd, button, action, mods) -> {
			if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
				context.getInputEvents().pushEvent(new MouseButtonEvent(action == GLFW.GLFW_RELEASE));
			}
		});
		
		GLFW.glfwMakeContextCurrent(m_window);
		GLFW.glfwSwapInterval(1);
		GLFW.glfwShowWindow(m_window);
	}
	
	public void destroy() {
		Callbacks.glfwFreeCallbacks(m_window);
		GLFW.glfwDestroyWindow(m_window);
		
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();		
	}
	
	public void swapBuffers() {
		GLFW.glfwSwapBuffers(m_window);
		GLFW.glfwPollEvents();
	}
	
	public void close() {
		GLFW.glfwSetWindowShouldClose(m_window, true);
	}
	
	public boolean isAlive() {
		return !GLFW.glfwWindowShouldClose(m_window);
	}
}
