package cosmos;

import java.util.*;

import org.lwjglx.util.vector.Vector3f;

import renderer.FrustumCuller;
import renderer.Loader;
import models.RawModel;
import models.TexModel;
import textures.ModelTex;

public class ChunkManager {
	private static final int CHUNKS_PER_FRAME = 156;
	int size;
	
	//Bruh
    public final Map<Vector3i, Chunk> chunkMap = new HashMap<>();
    private final List<Chunk> loadList = new ArrayList<>();
    private final List<Chunk> setupList = new ArrayList<>();
    private final List<Chunk> rebuildList = new ArrayList<>();
    private final List<Chunk> unloadList = new ArrayList<>();
    private final List<Chunk> visibilityList = new ArrayList<>();
    private final List<Chunk> renderList = new ArrayList<>();
    private final List<Chunk> updateFlagsList = new ArrayList<>();

    
    //For render updates
    private final Vector3f lastCameraPos  = new Vector3f();
    private final Vector3f lastCameraView = new Vector3f();
    private boolean forceVisibilityUpdate = true;
    
    //For mesh building
    private final Loader loader;
    private final ModelTex texture;
	
	public ChunkManager(Loader loader, ModelTex texture, int size) {
		this.loader = loader;
        this.texture = texture;
        this.size = size;
		
	}
	
	public void addChunk(Chunk chunk) {
	    Vector3i key = new Vector3i(
	        (int)(chunk.getPosition().x / (chunk.getChunkSize() * Chunk.BLOCK_RENDER_SIZE)),
	        (int)(chunk.getPosition().y / (chunk.getChunkSize() * Chunk.BLOCK_RENDER_SIZE)),
	        (int)(chunk.getPosition().z / (chunk.getChunkSize() * Chunk.BLOCK_RENDER_SIZE))
	    );

	    chunkMap.put(key, chunk);
	    loadList.add(chunk); // Queue for loading
	}
	
	public void update(Vector3f cameraPos, Vector3f cameraView) {
		//updateAsyncChunker
        updateLoadList();
        updateSetupList();
        updateRebuildList();
        //updateFlagsList();
        updateUnloadList();
        updateVisibilityList(cameraPos);

        if (!cameraPos.equals(lastCameraPos) || !cameraView.equals(lastCameraView) || forceVisibilityUpdate) 
        {
            updateRenderList(cameraPos, cameraView);
            forceVisibilityUpdate = false;
        }

        lastCameraPos.set(cameraPos);
        lastCameraView.set(cameraView);
    }
	
	
	
	private void updateRenderList(Vector3f cameraPos, Vector3f cameraView) {
		renderList.clear();
        for (Chunk chunk : visibilityList) {
            if (chunk.shouldRender()) {
                Vector3f center = chunk.getPosition();
                float halfSize = (Chunk.CHUNK_SIZE * Chunk.BLOCK_RENDER_SIZE); // half‐extent
                if (FrustumCuller.isBoxInFrustum(center.x, center.y, center.z, halfSize)) {
                    renderList.add(chunk);
                }
            }
        }
		
	}

	private void updateVisibilityList(Vector3f cameraPos) {
		visibilityList.clear();
        // Convert camera world pos → chunk coordinates
        int camCx = (int) Math.floor(cameraPos.x / (Chunk.CHUNK_SIZE * Chunk.BLOCK_RENDER_SIZE * 2));
        int camCy = (int) Math.floor(cameraPos.y / (Chunk.CHUNK_SIZE * Chunk.BLOCK_RENDER_SIZE * 2));
        int camCz = (int) Math.floor(cameraPos.z / (Chunk.CHUNK_SIZE * Chunk.BLOCK_RENDER_SIZE * 2));

        int renderRadius = 5; //render distance
        for (int dx = -renderRadius; dx <= renderRadius; dx++) {
            for (int dy = -renderRadius; dy <= renderRadius; dy++) {
                for (int dz = -renderRadius; dz <= renderRadius; dz++) {
                    Vector3i key = new Vector3i(camCx + dx, camCy + dy, camCz + dz);
                    Chunk chunk = chunkMap.get(key);
            
                    if (chunk != null && chunk.isLoaded() && chunk.isSetup()) {
                        visibilityList.add(chunk);
                    }

                }
            }
        }
		
	}

	private void updateUnloadList() {
		
		
		for (Chunk chunk : unloadList) {
            if (chunk.isLoaded()) {
                chunk.unload();
                forceVisibilityUpdate = true;
            }
        }
        unloadList.clear();
		
	}

	private void updateFlagsList() {
		updateFlagsList.clear();//for now
		
	}

	private void updateRebuildList() {
		int rebuildCount = 0;
        Iterator<Chunk> it = rebuildList.iterator();
        while (it.hasNext() && rebuildCount < CHUNKS_PER_FRAME) {
            Chunk chunk = it.next();
            if (chunk.isLoaded() && chunk.isSetup()) {
                chunk.createMesh(loader, texture);
                updateFlagsList.add(chunk);
                //case their faces change at borders
                for (Chunk neighbor : getNeighbors(chunk)) {
                    if (neighbor != null) updateFlagsList.add(neighbor);
                }
                rebuildCount++;
                forceVisibilityUpdate = true;
            }
        }
        rebuildList.clear();
		
	}

	private void updateSetupList() {
		for (Chunk chunk : setupList) {
            if (chunk.isLoaded() && !chunk.isSetup()) {
                chunk.setup(); // e.g., randomly activate some blocks or load heightmap
                if (chunk.isSetup()) {
                    rebuildList.add(chunk);
                    forceVisibilityUpdate = true;
                }
            }
        }
        setupList.clear();
		
	}
	
	//loads sync chunks per frame
	private void updateLoadList() {
		int loadedCount = 0;
        Iterator<Chunk> it = loadList.iterator();
        while (it.hasNext() && loadedCount < CHUNKS_PER_FRAME) {
            Chunk chunk = it.next();
            if (!chunk.isLoaded()) {
                chunk.load(); //generate block data
                setupList.add(chunk);
                loadedCount++;
                forceVisibilityUpdate = true;
            }
        }
        //loadList.clear();//clear list every frame
		
	}
	
    private List<Chunk> getNeighbors(Chunk chunk) {
    	List<Chunk> neighbors = new ArrayList<>(6);
        int cx = wrapCoordinate((int) chunk.getPosition().x);
        int cy = wrapCoordinate((int) chunk.getPosition().y);
        int cz = wrapCoordinate((int) chunk.getPosition().z);
        neighbors.add(chunkMap.get(new Vector3i(wrapCoordinate(cx + 1), cy, cz)));
        neighbors.add(chunkMap.get(new Vector3i(wrapCoordinate(cx - 1), cy, cz)));
        neighbors.add(chunkMap.get(new Vector3i(cx, wrapCoordinate(cy + 1), cz)));
        neighbors.add(chunkMap.get(new Vector3i(cx, wrapCoordinate(cy - 1), cz)));
        neighbors.add(chunkMap.get(new Vector3i(cx, cy, wrapCoordinate(cz + 1))));
        neighbors.add(chunkMap.get(new Vector3i(cx, cy, wrapCoordinate(cz - 1))));
        return neighbors;
    }
    
    private int wrapCoordinate(int coord) {
        return (coord % size + size) % size;
    }

	
	public List<Chunk> getRenderList() {
        return renderList;
    }

	public static class Vector3i {
        public final int x, y, z;
        
        public Vector3i(int x, int y, int z) {
            this.x = x; this.y = y; this.z = z;
        }
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Vector3i)) return false;
            Vector3i v = (Vector3i) o;
            return v.x == x && v.y == y && v.z == z;
        }
        @Override
        public int hashCode() {
            int hash = 17;
            hash = 31 * hash + x;
            hash = 31 * hash + y;
            hash = 31 * hash + z;
            return hash;
        }
    }



}