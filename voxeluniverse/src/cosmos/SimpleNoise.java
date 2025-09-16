package cosmos;

import java.util.Random;

public class SimpleNoise {
    private static final int SEED = 12345;
    private static final Random random = new Random(SEED);

    public static float noise(float x, float y, float z) {
        //A very basic fake noise, replace later with Perlin
        int n = (int)(x * 49632 + y * 325176 + z * 94733);
        random.setSeed(n);
        return random.nextFloat() * 2f - 1f; // range: [-1, 1]
    }
}