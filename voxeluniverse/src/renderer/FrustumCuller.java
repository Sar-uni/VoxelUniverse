package renderer;

import org.lwjglx.util.vector.Matrix4f;
import org.lwjglx.util.vector.Vector3f;
import org.lwjglx.util.vector.Vector4f;

import entities.Camera;
import toolbox.MathTime;

public class FrustumCuller {
	
	private final static Plane[] planes = new Plane[6]; // left, right, bottom, top, near, far

	
	public FrustumCuller() {
        for (int i = 0; i < planes.length; i++) {
            planes[i] = new Plane();
        }
    }

    public void updateFrustum(Matrix4f projectionMatrix, Camera camera) {
        Matrix4f viewMatrix = MathTime.createViewMatrix(camera);
        Matrix4f comboMatrix = Matrix4f.mul(projectionMatrix, viewMatrix, null);

        extractPlanes(comboMatrix);
    }
    
    private void extractPlanes(Matrix4f m) {
        // Left plane
        planes[0].set(
            m.m03 + m.m00,
            m.m13 + m.m10,
            m.m23 + m.m20,
            m.m33 + m.m30
        );
        // Right plane
        planes[1].set(
            m.m03 - m.m00,
            m.m13 - m.m10,
            m.m23 - m.m20,
            m.m33 - m.m30
        );
        // Bottom plane
        planes[2].set(
            m.m03 + m.m01,
            m.m13 + m.m11,
            m.m23 + m.m21,
            m.m33 + m.m31
        );
        // Top plane
        planes[3].set(
            m.m03 - m.m01,
            m.m13 - m.m11,
            m.m23 - m.m21,
            m.m33 - m.m31
        );
        // Near plane
        planes[4].set(
            m.m03 + m.m02,
            m.m13 + m.m12,
            m.m23 + m.m22,
            m.m33 + m.m32
        );
        // Far plane
        planes[5].set(
            m.m03 - m.m02,
            m.m13 - m.m12,
            m.m23 - m.m22,
            m.m33 - m.m32
        );}
    
    public static boolean isBoxInFrustum(float x, float y, float z, float size) {
    	float halfSize = size * 2.0f;

        for (Plane plane : planes) {
            if (!plane.isBoxInFront(x, y, z, halfSize)) {
                return false;
            }
        }
        return true;
    }
    
    
    
    private static class Plane {//mini class
        private float a, b, c, d;

        public void set(float a, float b, float c, float d) {
            float length = (float) Math.sqrt(a * a + b * b + c * c);
            this.a = a / length;
            this.b = b / length;
            this.c = c / length;
            this.d = d / length;
        }

        public boolean isBoxInFront(float cx, float cy, float cz, float halfSize) {
            // 8 corners of AABB centered at (cx, cy, cz)
            for (int i = 0; i < 8; i++) {
                float cornerX = cx + ((i & 1) == 0 ? -halfSize : halfSize);
                float cornerY = cy + ((i & 2) == 0 ? -halfSize : halfSize);
                float cornerZ = cz + ((i & 4) == 0 ? -halfSize : halfSize);

                if (a * cornerX + b * cornerY + c * cornerZ + d > 0) {
                    return true; // At least one corner is in front
                }
            }
            return false;
        }}

}
