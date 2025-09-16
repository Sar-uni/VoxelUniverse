package cosmos;

import java.util.List;

import org.lwjglx.util.vector.Vector3f;

import cosmos.Block.BlockTypes;
import models.RawModel;
import models.TexModel;
import renderer.Loader;
import textures.ModelTex;

public class Chunk {
	public static final int CHUNK_SIZE = 16;
	private Block[][][] blocks;;
	private Vector3f position;
	private TexModel model;
	public static final float BLOCK_RENDER_SIZE = 0.5f;
	private boolean loaded = false;
    private boolean setup  = false;

	public Chunk(Vector3f pos) {
		this.position = pos;
		blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
		for (int x = 0; x < CHUNK_SIZE; x++) {
			for (int y = 0; y < CHUNK_SIZE; y++) {
				for (int z = 0; z < CHUNK_SIZE; z++) {
					blocks[x][y][z] = new Block();
				}
			}
		}
	}

	private boolean inBounds(int x, int y, int z) {
		return x >= 0 && y >= 0 && z >= 0 && x < CHUNK_SIZE && y < CHUNK_SIZE && z < CHUNK_SIZE;
	}

	public Block getBlock(int x, int y, int z) {
		if (inBounds(x, y, z)) {
			return blocks[x][y][z];
		}
		return null;
	}
	
	public TexModel getModel() {
        return model;
    }

    public Vector3f getPosition() {
        return position;
    }
    
    public boolean shouldRender() { return model != null; }

	public void createMesh(Loader loader, ModelTex texture) {
		MeshBuilder builder = new MeshBuilder();

		for (int x = 0; x < CHUNK_SIZE; x++) {
			for (int y = 0; y < CHUNK_SIZE; y++) {
				for (int z = 0; z < CHUNK_SIZE; z++) {
					if (!blocks[x][y][z].isActive())
						continue;
					addCube(builder, x, y, z);
				}
			}
		}
		float[] verts = toFloatArray(builder.vertices);
        int[] inds = toIntArray(builder.indices);
        float[] texs  = toFloatArray(builder.texCoords);
        
        //load into vao
        RawModel raw = loader.loadToVAO(verts, inds, texs);
        this.model = new TexModel(raw, texture);
	}
	
	

	private void addCube(MeshBuilder builder, int x, int y, int z) {
		float r = 1.0f, g = 1.0f, b = 1.0f, a = 1.0f;
		float bx = x, by = y, bz = z;
		float s = BLOCK_RENDER_SIZE;

		// 8 cube vertices
		Vector3f p1 = new Vector3f(bx - s, by - s, bz + s);
		Vector3f p2 = new Vector3f(bx + s, by - s, bz + s);
		Vector3f p3 = new Vector3f(bx + s, by + s, bz + s);
		Vector3f p4 = new Vector3f(bx - s, by + s, bz + s);
		Vector3f p5 = new Vector3f(bx + s, by - s, bz - s);
		Vector3f p6 = new Vector3f(bx - s, by - s, bz - s);
		Vector3f p7 = new Vector3f(bx - s, by + s, bz - s);
		Vector3f p8 = new Vector3f(bx + s, by + s, bz - s);

		if (shouldRenderFace(x, y, z + 1)) 
		{builder.addFace(new Vector3f[] { p1, p2, p3, p4 }, new Vector3f(0, 0, 1), r, g, b, a);} //front
		
		if (shouldRenderFace(x, y, z - 1)) 
		{		builder.addFace(new Vector3f[] { p5, p6, p7, p8 }, new Vector3f(0, 0, -1), r, g, b, a);}//back
		
		if (shouldRenderFace(x + 1, y, z)) 
		{builder.addFace(new Vector3f[] { p2, p5, p8, p3 }, new Vector3f(1, 0, 0), r, g, b, a);} //right 
		
		if (shouldRenderFace(x - 1, y, z)) 
		{builder.addFace(new Vector3f[] { p6, p1, p4, p7 }, new Vector3f(-1, 0, 0), r, g, b, a);} //left
		
		if (shouldRenderFace(x, y + 1, z)) 
		{builder.addFace(new Vector3f[] { p4, p3, p8, p7 }, new Vector3f(0, 1, 0), r, g, b, a);}//top
		
		if (shouldRenderFace(x, y - 1, z)) 
		{builder.addFace(new Vector3f[] { p6, p5, p2, p1 }, new Vector3f(0, -1, 0), r, g, b, a);}//bottom
	}
	private boolean shouldRenderFace(int x, int y, int z) {
	    Block neighbor = getBlock(x, y, z);
	    return neighbor == null || !neighbor.isActive(); // null = outside chunk
	}
	
	
	public void setup() {
        //Here we already activated blocks in load(), sooo
        setup = true;
    }
	public void load() {
        // For demonstration—activate all blocks in a flat layer:
        
        loaded = true;
    }
	
	public void unload() {
        // 
        // loader.deleteVAO(model.getRawModel().getVaoID());
        model = null;
        loaded = false;
        setup  = false;
    }
	
	public void setBlock(int x, int y, int z, BlockTypes type, boolean active) {
        if (!inBounds(x, y, z)) return;
        //blocks[x][y][z].setType(type);  // if your Block supports types
        blocks[x][y][z].setActive(active);
    }

	public boolean isLoaded() { return loaded; }
	public boolean isSetup()  { return setup;  }
	
	private float[] toFloatArray(List<Float> list) {
        float[] arr = new float[list.size()];
        for (int i = 0; i < list.size(); i++) arr[i] = list.get(i);
        return arr;
    }
	
	public static int[] toIntArray(List<Integer> list) {
        int[] arr = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }

	public float getChunkSize() {
		// TODO Auto-generated method stub
		return CHUNK_SIZE;
	}

	public void setBlock(int bx, int by, int bz, BlockTypes default1) {
		blocks[bx][by][bz].setActive(true);
	}
	

}
