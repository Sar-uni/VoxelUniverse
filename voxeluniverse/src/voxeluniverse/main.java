package voxeluniverse;

import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import renderer.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;



public class main {


	public static void main(String[] args) {
		WindowManager.windowCreate();
		new App().run();

	}


	
	 static void testCube(long window, IntBuffer width, IntBuffer height) {
		/* Get width and height to calcualte the ratio */
		glfwGetFramebufferSize(window, width, height);
		float ratio = width.get() / (float) height.get();

		/* Rewind buffers for next get */
		width.rewind();
		height.rewind();

		/* Set viewport and clear screen */
		glViewport(0, 0, width.get(), height.get());

		/* Set ortographic projection */
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(-ratio, ratio, -1f, 1f, 1f, -1f);
		glMatrixMode(GL_MODELVIEW);
		/* Rotate matrix */
		glLoadIdentity();
		glRotatef((float) glfwGetTime() * 50f, 1f, 1f, 0f);
        // A cube has 6 faces, each face is a square made of 2 triangles
        GL11.glBegin(GL11.GL_QUADS);
        
        // Front face (z = 0.5)
        GL11.glColor3f(1f, 0.1f, 0.1f); // red color
        GL11.glVertex3f(-0.5f, -0.5f, 0.5f);
        GL11.glVertex3f(0.5f, -0.5f, 0.5f);
        GL11.glVertex3f(0.5f, 0.5f, 0.5f);
        GL11.glVertex3f(-0.5f, 0.5f, 0.5f);
        
        // Back face (z = -0.5)
        GL11.glColor3f(0f, 0f, 0f);
        GL11.glVertex3f(-0.5f, -0.5f, -0.5f);
        GL11.glVertex3f(-0.5f, 0.5f, -0.5f);
        GL11.glVertex3f(0.5f, 0.5f, -0.5f);
        GL11.glVertex3f(0.5f, -0.5f, -0.5f);
        
        // Top face (y = 0.5)
        GL11.glColor3f(0f, 1f, 0.5f);
        GL11.glVertex3f(-0.5f, 0.5f, -0.5f);
        GL11.glVertex3f(-0.5f, 0.5f, 0.5f);
        GL11.glVertex3f(0.5f, 0.5f, 0.5f);
        GL11.glVertex3f(0.5f, 0.5f, -0.5f);
        
        // Bottom face (y = -0.5)
        GL11.glColor3f(0.5f, 0f, 1.0f);
        GL11.glVertex3f(-0.5f, -0.5f, -0.5f);
        GL11.glVertex3f(0.5f, -0.5f, -0.5f);
        GL11.glVertex3f(0.5f, -0.5f, 0.5f);
        GL11.glVertex3f(-0.5f, -0.5f, 0.5f);
        
        // Right face (x = 0.5)
        GL11.glColor3f(0.5f, 0.5f, 0.5f);
        GL11.glVertex3f(0.5f, -0.5f, -0.5f);
        GL11.glVertex3f(0.5f, 0.5f, -0.5f);
        GL11.glVertex3f(0.5f, 0.5f, 0.5f);
        GL11.glVertex3f(0.5f, -0.5f, 0.5f);
        
        // Left face (x = -0.5)
        GL11.glColor3f(1f, 1f, 1f);
        GL11.glVertex3f(-0.5f, -0.5f, -0.5f);
        GL11.glVertex3f(-0.5f, -0.5f, 0.5f);
        GL11.glVertex3f(-0.5f, 0.5f, 0.5f);
        GL11.glVertex3f(-0.5f, 0.5f, -0.5f);
        
        GL11.glEnd();

    }

}
