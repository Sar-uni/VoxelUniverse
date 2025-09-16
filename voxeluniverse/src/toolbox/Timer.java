package toolbox;

import org.lwjgl.glfw.GLFW;

public class Timer {
	
	static float timeCount;
	static int fps;
	static int fpsCount;
	static int ups;//updates per second
	static int upsCount;
	static double lastLoopTime;

	public static void init() {
	    lastLoopTime = getTime();
	}
	
	public static double getTime(){
		return GLFW.glfwGetTime();
	}
	
	public static float getDelta() {
	    double time = getTime();
	    float delta = (float) (time - lastLoopTime);
	    lastLoopTime = time;
	    timeCount += delta;
	    return delta;
	}
	
	public static void updateFPS() {
	    fpsCount++;
	}

	public void updateUPS() {
	    upsCount++;
	}
	
	public static void update() {
	    if (timeCount > 1f) {
	        fps = fpsCount;
	        fpsCount = 0;

	        ups = upsCount;
	        upsCount = 0;

	        timeCount -= 1f;
	    }
	}
	
	public static int getFPS() {
			
        return fps > 0 ? fps : fpsCount;
    }
	//get fps method
}
