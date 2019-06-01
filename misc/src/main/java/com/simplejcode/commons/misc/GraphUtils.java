package com.simplejcode.commons.misc;

import com.simplejcode.commons.misc.structures.Graph;

import java.util.*;

public final class GraphUtils {

    private GraphUtils() {
    }

    //-----------------------------------------------------------------------------------

    public static int[] dijkstra1(int s, int[][] d) {
        int n = d.length, inf = (int) 1e9;
        int[] f = new int[n];
        Arrays.fill(f, inf);
        int[] q = new int[n];
        q[0] = s;
        f[s] = 0;
        for (int z = 1; z > 0; ) {
            int ind = 0;
            for (int i = 0; i < z; i++) {
                if (f[q[ind]] > f[q[i]]) {
                    ind = i;
                }
            }
            int v = q[ind];
            System.arraycopy(q, ind + 1, q, ind, --z - ind);
            for (int i = 0; i < n; i++) {
                if (d[v][i] != -1) {
                    if (f[i] == inf) {
                        q[z++] = i;
                    }
                    f[i] = Math.min(f[i], f[v] + d[v][i]);
                }
            }
        }
        for (int i = 0; i < n; i++) {
            if (f[i] == inf) {
                f[i] = -1;
            }
        }
        return f;
    }

    public static int[] dijkstra2(int s, int[][] d) {
        class V implements Comparable<V> {
            int i, d;

            public V(int i, int d) {
                this.i = i;
                this.d = d;
            }

            public int compareTo(V o) {
                int x = d - o.d;
                return x != 0 ? x : i - o.i;
            }
        }
        int n = d.length, inf = (int) 1e9;
        int[] f = new int[n];
        Arrays.fill(f, inf);
        f[s] = 0;
        PriorityQueue<V> q = new PriorityQueue<>(n);
        q.add(new V(s, 0));
        while (!q.isEmpty()) {
            V v = q.poll();
            for (int i = 0; i < n; i++) {
                if (d[v.i][i] != -1) {
                    int newD = f[v.i] + d[v.i][i];
                    if (f[i] > newD) {
                        q.remove(new V(i, f[i]));
                        f[i] = newD;
                        q.add(new V(i, newD));
                    }
                }
            }
        }
        for (int i = 0; i < n; i++) {
            if (f[i] == inf) {
                f[i] = -1;
            }
        }
        return f;
    }

    public static abstract class State implements Comparable<State> {

        protected static State[] nextStates = new State[1 << 10];

        protected static void checkSize(int size) {
            if (size == nextStates.length) {
                State[] tmp = new State[2 * nextStates.length];
                System.arraycopy(nextStates, 0, tmp, 0, nextStates.length);
                nextStates = tmp;
            }
        }

        public int dist, hash;
        public State p;

        protected State(State p, int dist) {
            this.dist = dist;
            this.p = p;
        }

        public int compareTo(State v) {
            return dist != v.dist ? dist - v.dist : compareState(v);
        }

        public int hashCode() {
            return hash;
        }

        public boolean equals(Object o) {
            return o instanceof State && hash == ((State) o).hash && compareState((State) o) == 0;
        }

        public abstract int compareState(State v);

        public abstract State[] getNextStates();

    }

    public static List<State> dijkstra3(State start, State end) {

        TreeSet<State> queue = new TreeSet<>();
        Map<State, Integer> dist = new HashMap<>();
        queue.add(start);
        dist.put(start, 0);

        while (!queue.isEmpty()) {

            State s = queue.first();
            queue.remove(s);
            if (s.equals(end)) {
                List<State> path = new ArrayList<>();
                while (s != null) {
                    path.add(0, s);
                    s = s.p;
                }
                return path;
            }
            State next;
            State[] nextStates = s.getNextStates();
            for (int i = 0; (next = nextStates[i++]) != null; ) {
                Integer x = dist.get(next);
                if (x == null || x > next.dist) {
                    if (x != null) {
                        // decrease key
                        int tmp = next.dist;
                        next.dist = x;
                        queue.remove(next);
                        next.dist = tmp;
                    }
                    queue.add(next);
                    dist.put(next, next.dist);
                }
            }

        }

        return null;
    }

    //-----------------------------------------------------------------------------------

    public static void floydWarshall(int[][] d) {
        int n = d.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    d[j][k] = Math.min(d[j][k], d[j][i] + d[i][k]);
                }
            }
        }
    }

    public static int[] fordBellman(int s, Graph g) {
        int n = g.numberOfVertexes();
        int[] d = new int[n];
        Arrays.fill(d, Integer.MAX_VALUE);
        d[s] = 0;
        for (int iter = 0; iter < n - 1; iter++) {
            for (int i = 0; i < n; i++) {
                for (Graph.Edge e : g.edgesFrom(i)) {
                    int x = d[e.i] + e.k;
                    if (d[e.j] > x) {
                        d[e.j] = x;
                    }
                }
            }
        }
        return d;
    }


    public static int maxMatching(boolean[][] b) {
        int n = b.length;
        int[] res = new int[n];
        boolean[] use = new boolean[n];
        Arrays.fill(res, -1);
        int nMatched = 0;
        for (int i = 0; i < n; i++) {
            Arrays.fill(use, false);
            if (dfs(i, n, res, use, b)) {
                nMatched++;
            }
        }
        return nMatched;
    }

    private static boolean dfs(int i, int n, int[] res, boolean[] use, boolean[][] b) {
        if (use[i]) {
            return false;
        }
        use[i] = true;
        for (int j = 0; j < n; j++) {
            if (b[j][i] && (res[j] == -1 || dfs(res[j], n, res, use, b))) {
                res[j] = i;
                return true;
            }
        }
        return false;
    }

    public static int maxMatching(int[] z, int[][] g) {
        int n = z.length;
        int[] res = new int[n];
        boolean[] use = new boolean[n];
        Arrays.fill(res, -1);
        int nMatched = 0;
        for (int i = 0; i < n; i++) {
            Arrays.fill(use, false);
            if (dfs(i, res, use, z, g)) {
                nMatched++;
            }
        }
        return nMatched;
    }

    private static boolean dfs(int i, int[] res, boolean[] use, int[] z, int[][] g) {
        if (use[i]) {
            return false;
        }
        use[i] = true;
        for (int j = 0; j < z[i]; j++) {
            if (res[j] == -1 || dfs(res[g[i][j]], res, use, z, g)) {
                res[j] = i;
                return true;
            }
        }
        return false;
    }


    /**
     * Maximizes sum in cost[][].
     *
     * @param cost matrix of costs
     * @return Returns answer
     */
    public static int hungarian(int[][] cost) {
        int n = cost.length;
        int[] lx = new int[n];
        int[] xy = new int[n];
        int[] yx = new int[n];
        for (int x = 0; x < n; x++) {
            xy[x] = yx[x] = -1;
            for (int y = 0; y < n; y++) {
                lx[x] = Math.max(lx[x], cost[x][y]);
            }
        }
        augment(cost, 0, lx, new int[n], xy, yx, new int[n], new int[n]);
        int ret = 0;
        for (int x = 0; x < n; x++) {
            ret += cost[x][xy[x]];
        }
        return ret;
    }

    private static void augment(int[][] cost, int m, int[] lx, int[] ly,
                                int[] xy, int[] yx, int[] sl, int[] slx)
    {
        int n = cost.length;
        if (m == n) {
            return;
        }
        int x, y, root = -1, wr = 0, rd = 0;
        int[] q = new int[n];
        boolean[] S = new boolean[n];
        boolean[] T = new boolean[n];
        int[] prev = new int[n];
        Arrays.fill(prev, -1);
        for (x = 0; x < n; x++) {
            if (xy[x] == -1) {
                q[wr++] = root = x;
                prev[x] = -2;
                S[x] = true;
                break;
            }
        }
        for (y = 0; y < n; y++) {
            sl[y] = lx[root] + ly[y] - cost[root][y];
            slx[y] = root;
        }
        M:
        while (true) {
            while (rd < wr) {
                x = q[rd++];
                for (y = 0; y < n; y++) {
                    if (cost[x][y] == lx[x] + ly[y] && !T[y]) {
                        if (yx[y] == -1) {
                            break M;
                        }
                        T[y] = true;
                        q[wr++] = yx[y];
                        addToTree(yx[y], x, cost, lx, ly, prev, sl, slx, S);
                    }
                }
            }
            int delta = Integer.MAX_VALUE;
            for (y = 0; y < n; y++) {
                if (!T[y] && delta > sl[y]) {
                    delta = sl[y];
                }
            }
            for (int i = 0; i < n; i++) {
                if (S[i]) {
                    lx[i] -= delta;
                }
                if (T[i]) {
                    ly[i] += delta;
                } else {
                    sl[i] -= delta;
                }
            }
            wr = rd = 0;
            for (y = 0; y < n; y++) {
                if (!T[y] && sl[y] == 0) {
                    if (yx[y] == -1) {
                        x = slx[y];
                        break M;
                    }
                    T[y] = true;
                    if (!S[yx[y]]) {
                        q[wr++] = yx[y];
                        addToTree(yx[y], slx[y], cost, lx, ly, prev, sl, slx, S);
                    }
                }
            }
        }
        if (y < n) {
            for (int cx = x, cy = y, ty; cx != -2; cx = prev[cx], cy = ty) {
                ty = xy[cx];
                yx[cy] = cx;
                xy[cx] = cy;
            }
            augment(cost, m + 1, lx, ly, xy, yx, sl, slx);
        }
    }

    private static void addToTree(int x, int prevx, int[][] cost, int[] lx, int[] ly,
                                  int[] prev, int[] sl, int[] slx, boolean[] S)
    {
        S[x] = true;
        int n = cost.length;
        prev[x] = prevx;
        for (int y = 0; y < n; y++) {
            if (lx[x] + ly[y] - cost[x][y] < sl[y]) {
                sl[y] = lx[x] + ly[y] - cost[x][y];
                slx[y] = x;
            }
        }
    }

    //-----------------------------------------------------------------------------------

    public static int edmondsKarp(Graph g, int s, int t) {
        int n = g.numberOfVertexes();
        int maxFlow = 0;
        Graph.Edge[] p = new Graph.Edge[n];
        int[] q = new int[n];
        q[0] = s;
        int[][] f = new int[n][n];
        while (bfs(g, s, t, p, q, f)) {
            int flow = Integer.MAX_VALUE;
            for (Graph.Edge e = p[t]; e.i != -1; e = p[e.i]) {
                flow = Math.min(flow, e.k - f[e.i][e.j]);
            }
            for (Graph.Edge e = p[t]; e.i != -1; e = p[e.i]) {
                f[e.i][e.j] += flow;
                f[e.j][e.i] -= flow;
            }
            maxFlow += flow;
        }
        return maxFlow;
    }

    private static boolean bfs(Graph g, int s, int t, Graph.Edge[] p, int[] q, int[][] f) {
        Arrays.fill(p, null);
        p[s] = new Graph.Edge(-1, 0, 0);
        for (int r = 0, w = 1; r < w; ) {
            int u = q[r++], ft[] = f[u];
            for (Graph.Edge e : g.edgesFrom(u)) {
                int v = e.j;
                if (p[v] == null && ft[v] < e.k) {
                    q[w++] = v;
                    p[v] = e;
                    if (v == t) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public static int pushRelabel(Graph g, int s, int t) {
        int n = g.numberOfVertexes();
        int[][] f = new int[n][n];
        int[] h = new int[n];
        int[] ex = new int[n];
        int[] seen = new int[n];
        int[] list = new int[n];
        for (int v = 0, z = 0; v < n; v++) {
            if (v != s && v != t) {
                list[z++] = v;
            }
        }

        h[s] = n;
        ex[s] = Integer.MAX_VALUE;
        for (Graph.Edge e : g.edgesFrom(s)) {
            push(f, e, ex);
        }

        for (int p = 0; p < n - 2; p++) {
            int u = list[p];
            int old_height = h[u];
            discharge(g, f, u, ex, h, seen);
            if (h[u] > old_height && p > 0) {
                System.arraycopy(list, 0, list, 1, p);
                list[p = 0] = u;
            }
        }

        int maxFlow = 0;
        for (int out : f[s]) {
            maxFlow += out;
        }
        return maxFlow;
    }

    private static void discharge(Graph g, int[][] f, int u, int[] ex, int[] h, int[] seen) {
        while (ex[u] > 0) {
            if (seen[u] < g.edgesFrom(u).size()) {
                Graph.Edge e = g.edgesFrom(u).get(seen[u]);
                if (e.k - f[u][e.j] > 0 && h[u] > h[e.j]) {
                    push(f, e, ex);
                } else {
                    seen[u]++;
                }
            } else {
                relabel(g, f, u, h);
                seen[u] = 0;
            }
        }
    }

    private static void push(int[][] f, Graph.Edge e, int[] ex) {
        int u = e.i, v = e.j;
        int send = Math.min(ex[u], e.k - f[u][v]);
        f[u][v] += send;
        f[v][u] -= send;
        ex[u] -= send;
        ex[v] += send;
    }

    private static void relabel(Graph g, int[][] f, int u, int[] h) {
        int min_height = h[u];
        for (Graph.Edge e : g.edgesFrom(u)) {
            if (e.k - f[u][e.j] > 0) {
                min_height = Math.min(min_height, h[e.j]);
                h[u] = min_height + 1;
            }
        }
    }

    //-----------------------------------------------------------------------------------

    private static final int maxnode = 5000;
    private static final int maxedge = 250000;

    private static int[] head = new int[maxnode];
    private static int[] point = new int[maxedge];
    private static int[] next = new int[maxedge];
    private static int[] flow = new int[maxedge];
    private static int[] capa = new int[maxedge];
    private static int[] dist = new int[maxnode];
    private static int[] Q = new int[maxnode];
    private static int[] work = new int[maxnode];

    public static int dinicFlow(int[][] c, int s, int t) {
        int n = c.length, nedge = 0, maxFlow = 0;
        Arrays.fill(head, -1);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (c[i][j] > 0 || c[j][i] > 0) {
                    addedge(nedge++, i, j, c[i][j]);
                    addedge(nedge++, j, i, c[j][i]);
                }
            }
        }
        while (dinicBfs(s, t)) {
            System.arraycopy(head, 0, work, 0, n);
            while (true) {
                int delta = dinicDfs(s, t, Integer.MAX_VALUE);
                if (delta == 0) break;
                maxFlow += delta;
            }
        }
        return maxFlow;
    }

    private static void addedge(int nedge, int u, int v, int c) {
        point[nedge] = v;
        capa[nedge] = c;
        flow[nedge] = 0;
        next[nedge] = head[u];
        head[u] = nedge;
    }

    private static boolean dinicBfs(int s, int t) {
        Arrays.fill(dist, 0, 300, -1);
        dist[Q[0] = s] = 0;
        for (int r = 0, w = 1; r < w; r++) {
            for (int k = Q[r], i = head[k]; i >= 0; i = next[i]) {
                if (flow[i] < capa[i] && dist[point[i]] < 0) {
                    dist[point[i]] = dist[k] + 1;
                    Q[w++] = point[i];
                }
            }
        }
        return dist[t] >= 0;
    }

    private static int dinicDfs(int x, int t, int exp) {
        if (x == t) return exp;
        for (int i = work[x]; i >= 0; i = next[i]) {
            int v = point[i], tmp;
            if (flow[i] < capa[i] && dist[v] == dist[x] + 1 && (tmp = dinicDfs(v, t, Math.min(exp, capa[i] - flow[i]))) > 0) {
                flow[i] += tmp;
                flow[i ^ 1] -= tmp;
                return tmp;
            }
        }
        return 0;
    }


    public static int fordFulkerson(int[][] c, int s, int t) {
        int n = c.length, maxFlow = 0;
        int[] p = new int[n];
        int[][] f = new int[n][n];
        while (true) {
            Arrays.fill(p, -1);
            p[s] = 0;
            if (!dfs(c, s, t, n, p, f)) {
                break;
            }
            int flow = Integer.MAX_VALUE;
            for (int v = t, u = p[t]; v != s; v = u, u = p[u]) {
                flow = Math.min(flow, c[u][v] - f[u][v]);
            }
            for (int v = t, u = p[v]; v != s; v = u, u = p[u]) {
                f[u][v] += flow;
                f[v][u] -= flow;
            }
            maxFlow += flow;
        }
        return maxFlow;
    }

    private static boolean dfs(int[][] c, int u, int t, int n, int[] p, int[][] f) {
        int[] ct = c[u], ft = f[u];
        for (int v = 0; v < n; v++) {
            if (p[v] == -1 && ft[v] < ct[v]) {
                p[v] = u;
                if ((v == t || dfs(c, v, t, n, p, f))) {
                    return true;
                }
            }
        }
        return false;
    }


    public static int edmondsKarp(int[][] c, int s, int t) {
        int n = c.length, maxFlow = 0;
        int[] p = new int[n];
        int[] q = new int[n];
        q[0] = s;
        int[][] f = new int[n][n];
        while (bfs(c, s, t, p, q, f)) {
            int flow = Integer.MAX_VALUE;
            for (int v = t, u = p[t]; v != s; v = u, u = p[u]) {
                flow = Math.min(flow, c[u][v] - f[u][v]);
            }
            for (int v = t, u = p[v]; v != s; v = u, u = p[u]) {
                f[u][v] += flow;
                f[v][u] -= flow;
            }
            maxFlow += flow;
        }
        return maxFlow;
    }

    private static boolean bfs(int[][] c, int s, int t, int[] p, int[] q, int[][] f) {
        Arrays.fill(p, -1);
        p[s] = 0;
        for (int r = 0, w = 1, n = p.length; r < w; ) {
            int u = q[r++];
            int[] ct = c[u], ft = f[u];
            for (int v = 0; v < n; v++) {
                if (p[v] == -1 && ft[v] < ct[v]) {
                    q[w++] = v;
                    p[v] = u;
                    if (v == t) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public static int edmondsKarpM(int[][] c, int s, int t) {
        int n = c.length, maxFlow = 0;
        int[] p = new int[n];
        int[] q = new int[n];
        q[0] = s;
        int[][] f = new int[n][n];
        for (int min = Integer.MAX_VALUE; min > 0; min >>= 1) {
            while (bfs(c, s, t, p, q, f, min)) {
                int flow = Integer.MAX_VALUE;
                for (int v = t, u = p[t]; v != s; v = u, u = p[u]) {
                    flow = Math.min(flow, c[u][v] - f[u][v]);
                }
                for (int v = t, u = p[v]; v != s; v = u, u = p[u]) {
                    f[u][v] += flow;
                    f[v][u] -= flow;
                }
                maxFlow += flow;
            }
        }
        return maxFlow;
    }

    private static boolean bfs(int[][] c, int s, int t, int[] p, int[] q, int[][] f, int min) {
        Arrays.fill(p, -1);
        p[s] = 0;
        for (int r = 0, w = 1, n = p.length; r < w; ) {
            int u = q[r++];
            int[] ct = c[u], ft = f[u];
            for (int v = 0; v < n; v++) {
                if (p[v] == -1 && ft[v] + min <= ct[v]) {
                    q[w++] = v;
                    p[v] = u;
                    if (v == t) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public static int pushRelabel(int[][] c, int s, int t) {
        int n = c.length;
        int[][] f = new int[n][n];

        int[] h = new int[n];
        int[] ex = new int[n];
        int[] seen = new int[n];
        int[] list = new int[n];
        for (int v = 0, z = 0; v < n; v++) {
            if (v != s && v != t) {
                list[z++] = v;
            }
        }

        h[s] = n;   // longest path from s to t is less than n long
        ex[s] = Integer.MAX_VALUE; // send as much flow as possible to neighbours of s
        for (int v = 0; v < n; v++) {
            push(s, v, ex, f, c);
        }

        int p = 0;
        while (p < n - 2) {
            int u = list[p];
            int old_height = h[u];
            discharge(u, ex, h, seen, f, c);
            if (h[u] > old_height && p > 0) {
                System.arraycopy(list, 0, list, 1, p);
                list[p = 0] = u;
            }
            p++;
        }

        int maxFlow = 0;
        for (int out : f[s]) {
            maxFlow += out;
        }
        return maxFlow;
    }

    private static void discharge(int u, int[] ex, int[] h, int[] seen, int[][] f, int[][] c) {
        int n = c.length;
        while (ex[u] > 0) {
            if (seen[u] < n) { // check next neighbour
                int v = seen[u];
                if (c[u][v] - f[u][v] > 0 && h[u] > h[v]) {
                    push(u, v, ex, f, c);
                } else {
                    seen[u]++;
                }
            } else { // we have checked all neighbours. must relabel
                relabel(u, h, f, c);
                seen[u] = 0;
            }
        }
    }

    private static void push(int u, int v, int[] ex, int[][] f, int[][] c) {
        int send = Math.min(ex[u], c[u][v] - f[u][v]);
        f[u][v] += send;
        f[v][u] -= send;
        ex[u] -= send;
        ex[v] += send;
    }

    private static void relabel(int u, int[] h, int[][] f, int[][] c) {
        int n = c.length;
        // find smallest new h making a push possible,
        // if such a push is possible at all
        int min_height = h[u];
        for (int v = 0; v < n; v++) {
            if (c[u][v] - f[u][v] > 0) {
                min_height = Math.min(min_height, h[v]);
                h[u] = min_height + 1;
            }
        }
    }


    public static long edmondsKarpM(long[][] c, int s, int t) {
        int n = c.length;
        long maxFlow = 0;
        int[] p = new int[n];
        int[] q = new int[n];
        q[0] = s;
        long[][] f = new long[n][n];
        for (long min = Long.MAX_VALUE; min > 0; min >>= 1) {
            while (bfs(c, s, t, p, q, f, min)) {
                long flow = Long.MAX_VALUE;
                for (int v = t, u = p[t]; v != s; v = u, u = p[u]) {
                    flow = Math.min(flow, c[u][v] - f[u][v]);
                }
                for (int v = t, u = p[v]; v != s; v = u, u = p[u]) {
                    f[u][v] += flow;
                    f[v][u] -= flow;
                }
                maxFlow += flow;
            }
        }
        return maxFlow;
    }

    private static boolean bfs(long[][] c, int s, int t, int[] p, int[] q, long[][] f, long min) {
        Arrays.fill(p, -1);
        p[s] = 0;
        for (int r = 0, w = 1, n = p.length; r < w; ) {
            int u = q[r++];
            long[] ct = c[u], ft = f[u];
            for (int v = 0; v < n; v++) {
                if (p[v] == -1 && ft[v] + min <= ct[v]) {
                    q[w++] = v;
                    p[v] = u;
                    if (v == t) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //-----------------------------------------------------------------------------------

}
