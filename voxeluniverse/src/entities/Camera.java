package entities;

import org.lwjgl.glfw.GLFW;
import org.lwjglx.util.vector.Vector3f;

import renderer.WindowManager;

public class Camera {

    private Vector3f position = new Vector3f(0, 0, 0);
    private float pitch; // up/down
    private float yaw;   // left/right
    private float roll;  // tilt
    private Vector3f last = new Vector3f(0, 0, 0);
    private double lastMouseX, lastMouseY;
    private boolean firstMouse = true;

    private float worldSize = 10f; // total world size in units (e.g. chunks * chunkSize)

    public void initMouseCallback() {
        GLFW.glfwSetCursorPosCallback(WindowManager.getWindow(), (window, xpos, ypos) -> {
            if (firstMouse) {
                lastMouseX = xpos;
                lastMouseY = ypos;
                firstMouse = false;
            }

            double xOffset = xpos - lastMouseX;
            double yOffset = lastMouseY - ypos;

            lastMouseX = xpos;
            lastMouseY = ypos;

            float sensitivity = 0.1f;
            yaw += xOffset * sensitivity;
            pitch -= yOffset * sensitivity;

            pitch = Math.max(-89.0f, Math.min(89.0f, pitch));
        });
    }

    public void move() {
        GLFW.glfwPollEvents();
        GLFW.glfwSetInputMode(WindowManager.getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);

        Vector3f forward = getForwardVector();
        Vector3f right = Vector3f.cross(forward, new Vector3f(0, 1, 0), null);
        right.normalise(right);

        if (GLFW.glfwGetKey(WindowManager.getWindow(), GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
            Vector3f.add(position, (Vector3f) new Vector3f(forward).scale(0.5f), position);
        }
        if (GLFW.glfwGetKey(WindowManager.getWindow(), GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
            Vector3f.sub(position, (Vector3f) new Vector3f(forward).scale(0.5f), position);
        }
        if (GLFW.glfwGetKey(WindowManager.getWindow(), GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
            Vector3f.add(position, (Vector3f) new Vector3f(right).scale(0.5f), position);
        }
        if (GLFW.glfwGetKey(WindowManager.getWindow(), GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
            Vector3f.sub(position, (Vector3f) new Vector3f(right).scale(0.5f), position);
        }
        if (GLFW.glfwGetKey(WindowManager.getWindow(), GLFW.GLFW_KEY_Q) == GLFW.GLFW_PRESS) {
            last.y = position.y;
            position.y += 0.5f;
        }
        if (GLFW.glfwGetKey(WindowManager.getWindow(), GLFW.GLFW_KEY_E) == GLFW.GLFW_PRESS) {
            last.y = position.y;
            position.y -= 0.5f;
        }


    }



    public Vector3f getForwardVector() {
        float pitchRad = (float) Math.toRadians(pitch);
        float yawRad = (float) Math.toRadians(yaw);

        float x = (float) (Math.cos(pitchRad) * Math.sin(yawRad));
        float z = (float) (Math.cos(pitchRad) * Math.cos(yawRad));

        return new Vector3f(x, 0, -z); //OpenGL Z is backward
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    public Vector3f getLastView() {
        return last;
    }

    public void setWorldSize(float worldSize) {
        this.worldSize = worldSize;
    }
}
