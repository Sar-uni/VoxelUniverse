package cosmos;

import java.util.ArrayList;
import java.util.List;

import org.lwjglx.util.vector.Vector3f;

public class MeshBuilder {
    public List<Float> vertices = new ArrayList<>();
    public List<Integer> indices = new ArrayList<>();
    public List<Float> texCoords = new ArrayList<>();
    public List<Float> normals = new ArrayList<>();
    public List<Float> colors = new ArrayList<>();

    private int vertexCount = 0;

    public void addFace(Vector3f[] positions, Vector3f normal, float r, float g, float b, float a) {
    	if (positions.length != 4) {
            throw new IllegalArgumentException("addFace expects exactly 4 positions");
        }

        // 1) Add vertex positions & normals & colors & default texcoords
        //    We'll use (0,0), (1,0), (1,1), (0,1) in that order
        float[][] defaultUV = {
            {0f, 1f},
            {1f, 1f},
            {1f, 0f},
            {0f, 0f}
        };

        for (int i = 0; i < 4; i++) {
            Vector3f pos = positions[i];
            vertices.add(pos.x);
            vertices.add(pos.y);
            vertices.add(pos.z);

            //normals.add(normal.x);
            //normals.add(normal.y);
            //normals.add(normal.z);
            //Not really needed...for now

            //colors.add(r);
            //colors.add(g);
            //colors.add(b);
            //colors.add(a);
            //probably not needed too, texture always used

            texCoords.add(defaultUV[i][0]);
            texCoords.add(defaultUV[i][1]);
        }

        // 2) Add two triangles indices for this quad
        indices.add(vertexCount);
        indices.add(vertexCount + 1);
        indices.add(vertexCount + 2);

        indices.add(vertexCount);
        indices.add(vertexCount + 2);
        indices.add(vertexCount + 3);

        vertexCount += 4;
    }
}

