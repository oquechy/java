package matrix;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MatrixTest {

    private static final int[] SIZES = {0, 1, 10, 111, 1100};

    @Test
    public void testGetRandomMatrix() {
        for (int size : SIZES) {
            checkNotNull(size);
        }
    }

    private void checkNotNull(int size) {
        int[][] mx = Matrix.getRandomMatrix(size);
        Assert.assertEquals(size, mx.length);
        for (int[] row : mx) {
            Assert.assertNotNull(row);
            Assert.assertEquals(size, row.length);
        }
    }

    @Test
    public void testGetMatrix() {
        for (int size : SIZES) {
            checkOrdered(Matrix.getMatrix(size), size);
        }
    }

    private void checkOrdered(int[][] mx, int size) {
        Assert.assertEquals(size, mx.length);
        for (int i = 0; i < mx.length; i++) {
            int[] row = mx[i];
            Assert.assertNotNull(row);
            Assert.assertEquals(size, row.length);
            for (int j = 0; j < row.length; j++) {
                Assert.assertEquals(i * size + j + 1, row[j]);
            }
        }
    }

    @Test
    public void testShow() {
        for (int size : SIZES) {
            String s = Matrix.show(Matrix.getMatrix(size));

            if (size == 0) {
                Assert.assertEquals("", s);
                continue;
            }

            String[] rows = s.split("\n");
            Assert.assertEquals(size, rows.length);
            for (int i = 0; i < rows.length; i++) {
                String[] nums = rows[i].split("\t");
                Assert.assertEquals(size, nums.length);
                for (int j = 0; j < rows.length; j++) {
                    Assert.assertEquals(i * size + j + 1, Integer.parseInt(nums[j]));
                }
            }
        }
    }

    @Test
    public void testTranspose() {
        for (int size : SIZES) {
            int mx[][] = Matrix.getMatrix(size);
            int mxCopy[][] = copy2dArray(mx);

            Matrix.transpose(mx);

            for (int i = 0; i < mx.length; i++) {
                int[] row = mx[i];
                for (int j = 0; j < row.length; j++) {
                    Assert.assertEquals(mxCopy[j][i], row[j]);
                }
            }

            Matrix.transpose(mx);

            assertEq2dArrays(mx, mxCopy);
        }
    }

    private int[][] copy2dArray(int[][] mx) {
        int[][] mxCopy = new int[mx.length][mx.length];

        for (int i = 0; i < mx.length; i++)
            System.arraycopy(mx[i], 0, mxCopy[i], 0, mx.length);

        return mxCopy;
    }

    private void assertEq2dArrays(int[][] mx, int[][] mxCopy) {
        for (int i = 0; i < mx.length; i++) {
            Assert.assertArrayEquals(mxCopy[i], mx[i]);
        }
    }


    @Test
    public void testSortByRows() {
        for (int size : SIZES) {
            int[][] mx = Matrix.getMatrix(size);
            int[][] mxShuff = new int[size][size];
            int[] perm = shuffle(size);

            for (int i = 0; i < size; i++) {
                System.arraycopy(mx[perm[i]], 0, mxShuff[i], 0, size);
            }

            Matrix.sortByRows(mxShuff);

            assertEq2dArrays(mx, mxShuff);
        }
    }

    private int[] shuffle(int size) {
        List<Integer> l = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            l.add(i);
        }

        Collections.shuffle(l);

        int lSize = l.size();
        int[] res = new int[lSize];
        for (int i = 0; i < lSize; i++) {
            res[i] = l.get(i);
        }

        return res;
    }

    @Test
    public void testTwist() {
        {
            int[][] mx = Matrix.getMatrix(3);
            Assert.assertEquals(
                    "5 4 7 8 9 6 3 2 1 ",
                    Matrix.twist(mx)
            );
        }

        {
            int[][] mx = Matrix.getMatrix(5);
            Assert.assertEquals(
                    "13 12 17 18 19 14 9 8 7 6 11 16 21 22 23 24 25 20 15 10 5 4 3 2 1 ",
                    Matrix.twist(mx)
            );
        }

        for (int size : SIZES) {
            if (size % 2 == 0) size++;

            String s = Matrix.twist(Matrix.getMatrix(size));
            String[] split = s.split(" ");
            int[] nums = new int[split.length];

            for (int i = 0; i < nums.length; i++) {
                nums[i] = Integer.parseInt(split[i]);
            }

            Arrays.sort(nums);
            for (int i = 0; i < nums.length; i++) {
                Assert.assertEquals(i + 1, nums[i]);
            }
        }
    }

    @Test(expected = java.lang.ArrayIndexOutOfBoundsException.class)
    public void testTwistEven() {
        Matrix.twist(Matrix.getMatrix(2));
    }
}