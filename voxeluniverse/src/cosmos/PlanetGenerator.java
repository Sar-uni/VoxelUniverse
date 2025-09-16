package cosmos;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import org.joml.SimplexNoise;
import org.lwjglx.util.vector.Vector3f;

import cosmos.ChunkManager.Vector3i;
import entities.Camera;

public class PlanetGenerator {
    public final Queue<Vector3i> generationQueue = new LinkedList<>();
    private final int size;
    private final Vector3f center;
    private final float chunkSizeWorld;
    private final ChunkManager chunkManager;
    private final float planetRadius;

    public PlanetGenerator(ChunkManager chunkManager, Vector3f center, int size) {
        this.center = center;
        this.size = size;
        this.chunkSizeWorld = Chunk.CHUNK_SIZE * Chunk.BLOCK_RENDER_SIZE;
        this.planetRadius = size * chunkSizeWorld * 0.5f; // Approximate planet radius
        this.chunkManager = chunkManager;
        generateChunkQueue();
    }

    public void updatePlanetGeneration(Camera camera) {
        int limit = 10; // limit chunks per frame

        while (hasNextChunk() && limit-- > 0) {
            Vector3i chunkPos = generationQueue.peek();
            System.out.println(chunkPos.x);

            Chunk chunk = generateNextChunk();
            chunkManager.addChunk(chunk);
        }
    }

    private void generateChunkQueue() {
        int r = size;
        for (int x = -r; x <= r; x++) {
            for (int y = -r; y <= r; y++) {
                for (int z = -r; z <= r; z++) {
                    generationQueue.add(new Vector3i(x, y, z));
                }
            }
        }
    }

    public boolean hasNextChunk() {
        return !generationQueue.isEmpty();
    }

    private Chunk generateNextChunk() {
        if (generationQueue.isEmpty())
            return null;

        Vector3i gridPos = generationQueue.poll();

        Vector3f chunkPos = new Vector3f(
            center.x + gridPos.x * chunkSizeWorld,
            center.y + gridPos.y * chunkSizeWorld,
            center.z + gridPos.z * chunkSizeWorld
        );

        Chunk chunk = new Chunk(chunkPos);

        Random random = new Random(1234); // Seeded for consistent world gen
        float scale = 0.01f;              // Larger = smoother terrain
        float maxHeightVariation = 20.0f; // Maximum height variation from the cube surface

        for (int bx = 0; bx < Chunk.CHUNK_SIZE; bx++) {
            for (int by = 0; by < Chunk.CHUNK_SIZE; by++) {
                for (int bz = 0; bz < Chunk.CHUNK_SIZE; bz++) {
                    // World position of the block relative to planet center
                    float worldX = chunkPos.x + bx * Chunk.BLOCK_RENDER_SIZE - center.x;
                    float worldY = chunkPos.y + by * Chunk.BLOCK_RENDER_SIZE - center.y;
                    float worldZ = chunkPos.z + bz * Chunk.BLOCK_RENDER_SIZE - center.z;

                    // Calculate distance to the nearest face of the cube
                    float distanceToSurface = calculateCubeDistance(worldX, worldY, worldZ, planetRadius);

                    // Generate terrain noise
                    float noise = SimplexNoise.noise(worldX * scale, worldY * scale, worldZ * scale);
                    float heightVariation = (noise + 1) / 2 * maxHeightVariation;

                    // If the block is within the planet surface with terrain variation
                    if (distanceToSurface <= heightVariation && distanceToSurface >= -maxHeightVariation) {
                        // Determine block type based on distance (surface vs interior)
                        if (distanceToSurface >= -1.0f && distanceToSurface <= 1.0f) {
                            chunk.setBlock(bx, by, bz, Block.BlockTypes.DEFAULT); // Surface
                        } else {
                            chunk.setBlock(bx, by, bz, Block.BlockTypes.DEFAULT); // Interior
                        }
                    }
                }
            }
        }

        return chunk;
    }

    /**
     * Calculates the signed distance to the surface of a cube planet
     * Positive values = outside the planet, Negative values = inside the planet
     */
    private float calculateCubeDistance(float x, float y, float z, float radius) {
        // Calculate distance to the nearest face of the cube
        float dx = Math.abs(x) - radius;
        float dy = Math.abs(y) - radius;
        float dz = Math.abs(z) - radius;

        // For points inside the cube, the distance is negative
        if (dx <= 0 && dy <= 0 && dz <= 0) {
            // Inside the cube - return negative distance to nearest face
            return Math.max(Math.max(dx, dy), dz);
        } else {
            // Outside the cube - return positive distance
            float outsideDist = (float) Math.sqrt(
                Math.max(0, dx) * Math.max(0, dx) +
                Math.max(0, dy) * Math.max(0, dy) +
                Math.max(0, dz) * Math.max(0, dz)
            );
            return outsideDist;
        }
    }

    /**
     * Alternative approach: Spherical to cube mapping for more spherical appearance
     */
    private Vector3f sphericalToCube(float x, float y, float z, float radius) {
        float fx = Math.abs(x) / radius;
        float fy = Math.abs(y) / radius;
        float fz = Math.abs(z) / radius;

        float sx = (float) Math.signum(x);
        float sy = (float) Math.signum(y);
        float sz = (float) Math.signum(z);

        // Cube projection
        float a = (float) (Math.max(fx, Math.max(fy, fz)));

        return new Vector3f(
            sx * radius * fx / a,
            sy * radius * fy / a,
            sz * radius * fz / a
        );
    }
}