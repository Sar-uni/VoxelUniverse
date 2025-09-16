package toolbox;

//Stefan Gustavson’s Simplex noise algorithm
//reference: https://github.com/SRombauts/SimplexNoise/blob/master/references/SimplexNoise.java
//joml has simplex noise function....maybe modify this and see what happens

public class SimplexNoise {
    private static final int grad3[][] = {
        {1,1,0},{-1,1,0},{1,-1,0},{-1,-1,0},
        {1,0,1},{-1,0,1},{1,0,-1},{-1,0,-1},
        {0,1,1},{0,-1,1},{0,1,-1},{0,-1,-1}
    };
    private static final int p[] = new int[256];
    private final static int perm[] = new int[512];

    public SimplexNoise(long seed) {
        java.util.Random rand = new java.util.Random(seed);
        for (int i = 0; i < 256; i++) p[i] = i;
        for (int i = 255; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            int temp = p[i];
            p[i] = p[index];
            p[index] = temp;
        }
        for (int i = 0; i < 512; i++) perm[i] = p[i & 255];
    }

    private static final double F3 = 1.0 / 3.0;
    private static final double G3 = 1.0 / 6.0;

    public static double noise(double xin, double yin, double zin) {
        double n0, n1, n2, n3;
        double s = (xin + yin + zin) * F3;
        int i = fastfloor(xin + s);
        int j = fastfloor(yin + s);
        int k = fastfloor(zin + s);
        double t = (i + j + k) * G3;
        double X0 = i - t, Y0 = j - t, Z0 = k - t;
        double x0 = xin - X0, y0 = yin - Y0, z0 = zin - Z0;

        int i1, j1, k1;
        int i2, j2, k2;

        if (x0 >= y0) {
            if (y0 >= z0) { i1=1; j1=0; k1=0; i2=1; j2=1; k2=0; }
            else if (x0 >= z0) { i1=1; j1=0; k1=0; i2=1; j2=0; k2=1; }
            else { i1=0; j1=0; k1=1; i2=1; j2=0; k2=1; }
        } else {
            if (y0 < z0) { i1=0; j1=0; k1=1; i2=0; j2=1; k2=1; }
            else if (x0 < z0) { i1=0; j1=1; k1=0; i2=0; j2=1; k2=1; }
            else { i1=0; j1=1; k1=0; i2=1; j2=1; k2=0; }
        }

        double x1 = x0 - i1 + G3;
        double y1 = y0 - j1 + G3;
        double z1 = z0 - k1 + G3;
        double x2 = x0 - i2 + 2.0 * G3;
        double y2 = y0 - j2 + 2.0 * G3;
        double z2 = z0 - k2 + 2.0 * G3;
        double x3 = x0 - 1.0 + 3.0 * G3;
        double y3 = y0 - 1.0 + 3.0 * G3;
        double z3 = z0 - 1.0 + 3.0 * G3;

        int ii = i & 255, jj = j & 255, kk = k & 255;
        n0 = contrib(ii, jj, kk, x0, y0, z0);
        n1 = contrib(ii + i1, jj + j1, kk + k1, x1, y1, z1);
        n2 = contrib(ii + i2, jj + j2, kk + k2, x2, y2, z2);
        n3 = contrib(ii + 1, jj + 1, kk + 1, x3, y3, z3);

        return 32.0 * (n0 + n1 + n2 + n3);
    }

    private static double contrib(int i, int j, int k, double x, double y, double z) {
        double t = 0.6 - x * x - y * y - z * z;
        if (t < 0) return 0.0;
        int gi = perm[i + perm[j + perm[k]]] % 12;
        t *= t;
        return t * t * dot(grad3[gi], x, y, z);
    }

    private static int fastfloor(double x) {
        return x > 0 ? (int)x : (int)x - 1;
    }

    private static double dot(int[] g, double x, double y, double z) {
        return g[0]*x + g[1]*y + g[2]*z;
    }
}

