package cosmos;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import org.joml.SimplexNoise;
import org.lwjglx.util.vector.Vector3f;

import cosmos.ChunkManager.Vector3i;
import entities.Camera;

public class WorldGenerator {
    public final Queue<Vector3i> generationQueue = new LinkedList<>();
    private final int worldSizeChunks;  // World size in chunks (e.g., 16 = 16x16 chunks)
    private final ChunkManager chunkManager;
    private final long seed;
    private final Random random;
    private final float chunkSizeWorld;

    public WorldGenerator(ChunkManager chunkManager, int worldSizeChunks, long seed) {
        this.chunkManager = chunkManager;
        this.worldSizeChunks = worldSizeChunks;
        this.seed = seed;
        this.random = new Random(seed);
        this.chunkSizeWorld = Chunk.CHUNK_SIZE * Chunk.BLOCK_RENDER_SIZE;
        generateChunkQueue();
    }

    /**
     * Updates chunk generation based on camera position.
     */
    public void updateWorldGeneration(Camera camera) {
        int limit = 10; // Max chunks generated per frame
        while (!generationQueue.isEmpty() && limit-- > 0) {
            Chunk chunk = generateNextChunk();
            chunkManager.addChunk(chunk);
        }

    }

    private void generateChunkQueue() {
		int r = worldSizeChunks;
		for (int x = -r; x <= r; x++) {
			for (int y = -r; y <= r; y++) {
				for (int z = -r; z <= r; z++) {
					
						generationQueue.add(new Vector3i(x, y, z));
				}
			}
		}
	}
    /**
     * Queues chunks around the camera for generation.
     */
    private void queueNearbyChunks(Vector3f cameraPos) {
        int renderRadius = 8; // Chunks to load around the player
        int camChunkX = (int) Math.floor(cameraPos.x / chunkSizeWorld);
        int camChunkZ = (int) Math.floor(cameraPos.z / chunkSizeWorld);

        for (int dx = -renderRadius; dx <= renderRadius; dx++) {
            for (int dz = -renderRadius; dz <= renderRadius; dz++) {
                int chunkX = wrapCoordinate(camChunkX + dx);
                int chunkZ = wrapCoordinate(camChunkZ + dz);
                Vector3i chunkCoord = new Vector3i(chunkX, 0, chunkZ);

                if (!chunkManager.chunkMap.containsKey(chunkCoord)){
                    generationQueue.add(chunkCoord);
                }
            }
        }
    }

    /**
     * Generates the next chunk in the queue.
     */
    private Chunk generateNextChunk() {
        Vector3i gridPos = generationQueue.poll();
        Vector3f chunkPos = new Vector3f(
            gridPos.x * chunkSizeWorld,
            0,  // Flat world (adjust for 3D)
            gridPos.z * chunkSizeWorld
        );

        Chunk chunk = new Chunk(chunkPos);
        generateTerrain(chunk, gridPos.x, gridPos.z);
        return chunk;
    }

    /**
     * Generates terrain for a chunk using Simplex noise.
     */
    private void generateTerrain(Chunk chunk, int chunkX, int chunkZ) {
        for (int bx = 0; bx < Chunk.CHUNK_SIZE; bx++) {
            for (int bz = 0; bz < Chunk.CHUNK_SIZE; bz++) {
                // Calculate global block coordinates (wrapped)
                int worldX = wrapBlockCoord(chunkX * Chunk.CHUNK_SIZE + bx);
                int worldZ = wrapBlockCoord(chunkZ * Chunk.CHUNK_SIZE + bz);

                // Sample noise (scale down for smoother terrain)
                float noiseX = worldX * 0.01f;
                float noiseZ = worldZ * 0.01f;
                float height = SimplexNoise.noise(noiseX, noiseZ) * 20 + 64; // Base height ~64

                // Fill blocks below the height
                for (int by = 0; by < height && by < Chunk.CHUNK_SIZE; by++) {
                    if (by < height - 4) {
                        chunk.setBlock(bx, by, bz, Block.BlockTypes.DEFAULT);
                    } else if (by < height - 1) {
                        chunk.setBlock(bx, by, bz, Block.BlockTypes.DEFAULT);
                    } else {
                        chunk.setBlock(bx, by, bz, Block.BlockTypes.DEFAULT);
                    }
                }
            }
        }
    }

    /**
     * Wraps a chunk coordinate to stay within world bounds.
     */
    private int wrapCoordinate(int coord) {
        return (coord % worldSizeChunks + worldSizeChunks) % worldSizeChunks;
    }

    /**
     * Wraps a block coordinate for seamless noise.
     */
    private int wrapBlockCoord(int coord) {
        int worldSizeBlocks = worldSizeChunks * Chunk.CHUNK_SIZE;
        return (coord % worldSizeBlocks + worldSizeBlocks) % worldSizeBlocks;
    }
}