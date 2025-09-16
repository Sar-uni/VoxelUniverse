package toolbox;

public class WorldUtils {
    public static int wrapCoord(int value, int size) {
        return (value % size + size) % size;
    }

    public static float wrapCoord(float value, float size) {
        return (value % size + size) % size;
    }
}