package com.simplejcode.commons.algo;

import java.util.*;

import static java.lang.Math.min;

@SuppressWarnings({"PrimitiveArrayArgumentToVariableArgMethod"})
public final class FastGraph {

    private FastGraph() {
    }

    //-----------------------------------------------------------------------------------

    private static int[] T1, T2;

    // subgraph index by vertex
    public static int[] GP;
    // subgraph vertext index by vertex
    public static int[] VP;

    public static class Graph {

        private static int TZ, TI[], TJ[], TE[];

        public int n, m, id[], z[], g[][], e[][];

        public Graph(int nVertex) {
            n = nVertex;
            id = new int[n];
            z = new int[n];
            g = new int[n][];
            e = new int[n][];
            for (int i = 0; i < n; i++) {
                id[i] = i;
            }
        }

        public void add(int i, int j, int k) {
            TI[TZ] = i;
            TJ[TZ] = j;
            TE[TZ] = k;
            TZ++;
            z[i]++;
            z[j]++;
        }

        public void init(boolean directed) {
            for (int i = 0; i < n; i++) {
                g[i] = new int[z[i]];
                e[i] = new int[z[i]];
                z[i] = 0;
            }
            for (int t = 0; t < TZ; t++) {
                edge(TI[t], TJ[t], TE[t]);
                if (!directed) {
                    edge(TJ[t], TI[t], TE[t]);
                }
            }
            m = TZ;
            TZ = 0;
        }

        public void edge(int i, int j, int k) {
            g[i][z[i]] = j;
            e[i][z[i]] = k;
            z[i]++;
        }

    }

    public static void init(int maxN, int maxM) {
        T1 = new int[maxN];
        T2 = new int[maxN];
        GP = new int[maxN];
        VP = new int[maxN];
        Graph.TI = new int[maxM];
        Graph.TJ = new int[maxM];
        Graph.TE = new int[maxM];
    }

    /**
     * Works even if some edges are negative (bridges)
     *
     * @param graph graph
     * @return components
     */
    public static Graph[] getComponents(Graph graph) {

        int n = graph.n;
        int[] gp = GP, vp = VP, ind = T1;

        int c = 0;
        Arrays.fill(gp, -1);
        Arrays.fill(vp, -1);
        List<Graph> list = new ArrayList<>();
        for (int p = 0; p < n; p++) {
            if (gp[p] == -1) {
                Graph g = new Graph(dfs(p, c++, gp, graph));
                for (int i = 0, k = 0; i < n; i++) {
                    if (gp[i] == c - 1) {
                        g.id[k] = graph.id[i];
                        vp[ind[k] = i] = k++;
                    }
                }
                for (int i = 0; i < g.n; i++) {
                    int fi = ind[i];
                    for (int t = 0; t < graph.z[fi]; t++) {
                        int fj = graph.g[fi][t];
                        if (fj > fi) {
                            g.add(i, vp[fj], graph.e[fi][t]);
                        }
                    }
                }
                g.init(false);
                list.add(g);
            }
        }

        Graph[] ret = list.toArray(new Graph[c]);

        int[] gz = new int[c];
        for (int i = 0; i < c; i++) {
            gz[i] = -ret[i].n;
        }
        ArraySort.quicksort(0, c, gz);
        ArraySort.sortBy(0, c, ArraySort.P, ret);
        ArraySort.transformBy(0, n, ArraySort.PI, gp);

        return ret;
    }

    private static int dfs(int i, int c, int[] f, Graph g) {
        if (i < 0 || f[i] == c) {
            return 0;
        }
        int z = g.z[i], gg[] = g.g[i];
        f[i] = c;
        int s = 1;
        for (int t = 0; t < z; t++) {
            s += dfs(gg[t], c, f, g);
        }
        return s;
    }

    public static Graph[] getStronglyConnectedComponents(Graph g) {
        int[] d = T1, f = T2;
        Arrays.fill(d, 0);
        d[0] = 1;
        dfs(0, -1, d, f, g);
        revertEdges(g, true);
        Graph[] ret = getComponents(g);
        revertEdges(g, false);
        return ret;
    }

    private static int dfs(int i, int p, int[] d, int[] f, Graph g) {
        int z = g.z[i], gg[] = g.g[i];
        f[i] = d[i];
        for (int t = 0; t < z; t++) {
            int j = gg[t];
            if (j != p) {
                if (d[j] > 0) {
                    gg[t] = -gg[t] - 1;
                    f[i] = min(f[i], d[j]);
                } else {
                    d[j] = d[i] + 1;
                    if (dfs(j, i, d, f, g) <= d[i]) {
                        gg[t] = -gg[t] - 1;
                        int zz = g.z[j], ggg[] = g.g[j];
                        for (int tt = 0; tt < zz; tt++) {
                            if (ggg[tt] == i) {
                                ggg[tt] = -ggg[tt] - 1;
                                break;
                            }
                        }
                        f[i] = min(f[i], f[j]);
                    }
                }
            }
        }
        return f[i];
    }

    private static void revertEdges(Graph g, boolean all) {
        int n = g.n;
        for (int i = 0; i < n; i++) {
            int z = g.z[i], gg[] = g.g[i];
            for (int t = 0; t < z; t++) {
                if (all || gg[t] < 0) {
                    gg[t] = -gg[t] - 1;
                }
            }
        }
    }

    public static Graph projectGraph(Graph g, int[] gp, int n, int type) {
        Graph projection = new Graph(n);
        for (int i = 0; i < g.n; i++) {
            int z = g.z[i], gg[] = g.g[i], ee[] = g.e[i];
            for (int t = 0; t < z; t++) {
                int j = gg[t];
                if (gp[i] < gp[j]) {
                    projection.add(gp[i], gp[j], type == 0 ? ee[t] : type == 1 ? i : j);
                }
            }
        }
        projection.init(false);
        return projection;
    }

}
