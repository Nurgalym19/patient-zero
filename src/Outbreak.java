import java.util.*;

/**
 * Mission 5. Dynamic programming on the tree of infections. Root the tree in person 0.
 * Input.Tree.neighbours() gives the neighbours of every person. The number of people is at most about 100, so a recursive DFS is fine here.
 */
public class Outbreak {

    /**
     * Choose people to test so that for every link (somebody infected somebody) at least one of the two is tested,
     * and the total cost of testing is as small as possible. cost[v] is the price of testing person v.
     * Returns tested[v] = true if person v is tested.
     * <p>
     * State for every person v (looking only at v and everything below it): the smallest cost when v IS tested, and
     * the smallest cost when v is NOT tested. What must then be true about the children of v in each case?
     * Do not forget to rebuild the answer (who exactly is tested), not only the cost: the picture needs it.
     * Main calls this twice: with all costs equal to 1 (the line "Tests needed") and with the real costs ("Cheapest testing").
     */
    public static boolean[] cheapestTests(int n, int[][] nb, int[] cost) {
        long[] in = new long[n];
        long[] out = new long[n];
        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        dfsCost(0, -1, nb, cost, in, out, parent);

        boolean[] tested = new boolean[n];

        chooseTested(0, in[0] <= out[0], nb, parent, in, out, tested);
        return tested;
    }

    private static void dfsCost(int v, int par, int[][] nb, int[] cost,
                                long[] in, long[] out, int[] parent) {
        parent[v] = par;
        in[v] = cost[v];
        out[v] = 0;
        for (int c : nb[v]) {
            if (c == par) continue;
            dfsCost(c, v, nb, cost, in, out, parent);
            in[v] += Math.min(in[c], out[c]);
            out[v] += in[c];
        }
    }

    private static void chooseTested(int v, boolean isTested, int[][] nb, int[] parent,
                                     long[] in, long[] out, boolean[] tested) {
        tested[v] = isTested;
        for (int c : nb[v]) {
            if (c == parent[v]) continue;
            if (isTested) {
                chooseTested(c, in[c] <= out[c], nb, parent, in, out, tested);
            } else {
                chooseTested(c, true, nb, parent, in, out, tested);
            }
        }
    }

    private static int best;

    /**
     * The number of links on the longest chain of infections, that is the longest path in the tree (counted in links).
     * A path goes up from one person and down to another; for every person you need the two longest ways down.
     * Example: a line 0-1-2-3 has chain 3; a star with centre 0 and leaves 1, 2, 3 has chain 2.
     */
    public static int longestChain(int n, int[][] nb) {
        best = 0;
        down(0, -1, nb);
        return best;
    }


    private static int down(int v, int par, int[][] nb) {
        int first = 0, second = 0;
        for (int c : nb[v]) {
            if (c == par) continue;
            int len = down(c, v, nb) + 1;
            if (len > first) {
                second = first;
                first = len;
            } else if (len > second) {
                second = len;
            }
        }
        best = Math.max(best, first + second);
        return first;
    }
}
