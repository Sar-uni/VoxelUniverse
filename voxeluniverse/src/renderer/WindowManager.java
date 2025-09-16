package renderer;

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class WindowManager {
	// Add JSON in future
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static final int FPS_CAP = 60;
	private static final String TITLE = "hi";
	static long window;
	
	
	

	
	private static GLFWErrorCallback errorCallback = GLFWErrorCallback.createPrint(System.err);
	
	private static GLFWKeyCallback keyCallback = new GLFWKeyCallback() {

		@Override
		public void invoke(long window, int key, int scancode, int action, int mods) {
			if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
				glfwSetWindowShouldClose(window, true);
			}
		}
	};
	

	public static long getWindow() {
		return window;
	}

	public static void windowCreate() {
		glfwSetErrorCallback(errorCallback);
		/* Initialize GLFW */
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		/* Create window */
		window = glfwCreateWindow(WIDTH, HEIGHT, TITLE, NULL, NULL);
		if (window == NULL) {
			glfwTerminate();
			throw new RuntimeException("Failed to create the GLFW window");
		}

		/* Center the window on screen */
		GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (vidMode.width() - WIDTH) / 2, (vidMode.height() - HEIGHT) / 2);
		/* Create OpenGL context */
		glfwMakeContextCurrent(window);
		GL.createCapabilities();

		/* Enable v-sync */
		glfwSwapInterval(1);
		/* Set the key callback */
		glfwSetKeyCallback(WindowManager.getWindow(), keyCallback);

		

	}


	public static void windowUpdate() {
		
	}
	
	public static void windowDelete(IntBuffer width, IntBuffer height) {
		/* Free buffers */
        MemoryUtil.memFree(width);
        MemoryUtil.memFree(height);

        /* Release window and its callbacks */
        glfwDestroyWindow(window);
		keyCallback.free();


        /* Terminate GLFW and release the error callback */
        glfwTerminate();
        errorCallback.free();

	}

}
