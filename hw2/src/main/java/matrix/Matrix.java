package matrix;

import java.util.*;

/**
 * Created by oquechy on 08.09.17.
 *
 * type "exit" in console to stop program
 *
 * type "sort" and than number of rows and columns
 * to get random matrix sorted by first elements in columns
 *
 * type "twist" and than number of rows and columns
 * to see elements in twisted order
 *
 */
public class Matrix {

    static private String USAGE = "choose command: exit | sort | twist";

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        int[][] mx;
        System.out.println(USAGE);
        String s = in.nextLine();
        while (true) {
            switch (s) {
                case "exit":
                    return;
                case "sort":
                    System.out.println("input number of rows and cols: ");
                    mx = getRandomMatrix(in.nextInt());
                    System.out.println("random matrix:");
                    System.out.print(show(mx));
                    transpose(mx);
                    System.out.println("sorted random matrix:");
                    sortByRows(mx);
                    transpose(mx);
                    System.out.print(show(mx));
                    System.out.println(USAGE);
                    s = getString(in);
                    break;
                case "twist":
                    System.out.println("input number of rows and cols: ");
                    int n = in.nextInt();
                    if (n % 2 == 1) {
                        mx = getMatrix(n);
                        System.out.println(twist(mx));
                    } else {
                        System.err.println("number must be odd");
                    }
                default:
                    System.out.println(USAGE);
                    s = getString(in);
            }
        }
    }

    private static String getString(Scanner in) {
        String s;
        do {
            s = in.nextLine();
        } while (Objects.equals(s, ""));
        return s;
    }

    static void sortByRows(int[][] mx) {
        Arrays.sort(mx, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[0] - o2[0];
            }
        });
    }


    static String show(int[][] mx) {
        StringBuilder builder = new StringBuilder();
        for (int[] row : mx) {
            for (int j = 0; j < mx.length; j++) {
                builder.append(row[j])
                        .append('\t');
            }
            builder.append('\n');
        }
        return builder.toString();
    }

    static void transpose(int[][] mx) {
        for (int i = 0; i < mx.length; i++) {
            for (int j = i + 1; j < mx.length; j++) {
                int tmp = mx[j][i];
                mx[j][i] = mx[i][j];
                mx[i][j] = tmp;
            }
        }
    }

    static int[][] getRandomMatrix(int n) {
        Random r = new Random();
        int[][] mx = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mx[i][j] = r.nextInt(30);
            }
        }
        return mx;
    }

    static class XMode {
        int[] STATES = new int[]{0, 1, 0, -1};
        int idx = 0;

        int step() {
            return STATES[idx++ % 4];
        }
    }

    static class YMode {
        int[] STATES = new int[]{-1, 0, 1, 0};
        int idx = 0;

        int step() {
            return STATES[idx++ % 4];
        }
    }

    static String twist(int[][] mx) {
        StringBuilder twisted = new StringBuilder();
        XMode xMode = new XMode();
        YMode yMode = new YMode();

        Integer x, y;
        x = y = mx.length / 2;

        int dx, dy;

        twisted.append(mx[x][y]).append(' ');

        for (int i = 1; i < mx.length; i++) {
            for (int t = 0; t < 2; t++) {
                dx = xMode.step();
                dy = yMode.step();
                for (int j = 0; j < i; j++) {
                    twisted.append(mx[x += dx][y += dy]).append(' ');
                }
            }
        }

        dx = xMode.step();
        dy = yMode.step();
        for (int j = 0; j < mx.length - 1; j++) {
            twisted.append(mx[x += dx][y += dy]).append(' ');
        }

        return twisted.toString();
    }


    static int[][] getMatrix(int n) {
        int[][] mx = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mx[i][j] = i * n + j + 1;
            }
        }

        return mx;
    }
}
