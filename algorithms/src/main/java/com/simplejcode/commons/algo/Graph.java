package com.simplejcode.commons.algo;

import java.util.*;

@SuppressWarnings({"unchecked"})
public class Graph {

    public static final class Edge {
        public int i, j, k;

        public Edge(int i, int j, int k) {
            this.i = i;
            this.j = j;
            this.k = k;
        }

        public boolean equals(Object obj) {
            return super.equals(obj);
        }
    }

    private List<Edge>[] edges;
    private boolean[][] hasEdge;
    private int nEdges;

    public Graph(int size) {
        edges = new List[size];
        hasEdge = new boolean[size][size];
        for (int i = 0; i < size; i++) {
            edges[i] = new ArrayList<>(size);
        }
    }

    public Graph(boolean[][] b) {
        this(b.length);
        int n = numberOfVertexes();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (b[i][j]) {
                    addEdge(i, j);
                }
            }
        }
    }

    public Graph(int[][] f, int ignoreValue) {
        this(f.length);
        int n = numberOfVertexes();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (f[i][j] != ignoreValue) {
                    addEdge(i, j, f[i][j]);
                }
            }
        }
    }

    public int numberOfVertexes() {
        return edges.length;
    }

    public int numberOfEdges() {
        return nEdges;
    }

    public void addEdge(int i, int j) {
        addEdge(i, j, numberOfEdges());
    }

    public void addEdge(int i, int j, int k) {
        if (hasEdge[i][j]) {
            return;
        }
        hasEdge[i][j] = true;
        nEdges++;
        edges[i].add(new Edge(i, j, k));
    }

    public void removeEdge(int i, int j) {
        if (!hasEdge[i][j]) {
            return;
        }
        hasEdge[i][j] = false;
        nEdges--;
        List<Edge> list = edgesFrom(i);
        for (int ind = 0; ind < list.size(); ind++) {
            if (list.get(ind).j == j) {
                list.remove(ind);
                break;
            }
        }
    }

    public List<Edge> edgesFrom(int i) {
        return edges[i];
    }


    public Graph getUndirectedGraph() {
        int n = numberOfVertexes();
        Graph r = new Graph(n);
        for (List<Edge> l : edges) {
            for (Edge e : l) {
                r.addEdge(e.i, e.j, e.k);
            }
        }
        for (List<Edge> l : edges) {
            for (Edge e : l) {
                if (!r.hasEdge[e.j][e.i]) {
                    r.addEdge(e.j, e.i, 0);
                }
            }
        }
        return r;
    }


    public List<Integer> getBridges() {
        int n = numberOfVertexes(), m = numberOfEdges();
        int[] d = new int[n];
        int[] f = new int[n];
        boolean[] b = new boolean[m];
        for (int i = 0; i < n; i++) {
            if (d[i] == 0) {
                d[i] = 1;
                dfs(i, d, f, -1, b);
            }
        }
        List<Integer> r = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            if (!b[i]) {
                r.add(i);
            }
        }
        return r;
    }

    private int dfs(int i, int[] d, int[] f, int j, boolean[] b) {
        f[i] = d[i];
        for (Edge e : edgesFrom(i)) {
            if (e.k != j) {
                if (d[e.j] > 0) {
                    b[e.k] = true;
                    f[i] = Math.min(f[i], d[e.j]);
                } else {
                    d[e.j] = d[i] + 1;
                    if (dfs(e.j, d, f, e.k, b) <= d[i]) {
                        b[e.k] = true;
                        f[i] = Math.min(f[i], f[e.j]);
                    }
                }
            }
        }
        return f[i];
    }


    public List<List<Integer>> getComponents() {
        int n = numberOfVertexes();
        int[] t = new int[n];
        int[] f = new int[n];
        boolean[] b = new boolean[n];
        List<List<Integer>> r = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (t[i] == 0) {
                dfs(i, 1, t, f, b, new ArrayList<>(), r);
            }
        }
        return r;
    }

    private int dfs(int i, int c, int[] t, int[] f, boolean[] b,
                    List<Integer> l, List<List<Integer>> r)
    {
        f[i] = t[i] = c++;
        l.add(i);
        b[i] = true;
        for (Edge e : edgesFrom(i)) {
            if (t[e.j] > 0) {
                if (b[e.j]) {
                    f[i] = Math.min(f[i], t[e.j]);
                }
            } else {
                c = dfs(e.j, c, t, f, b, l, r);
                f[i] = Math.min(f[i], f[e.j]);
            }
        }
        if (f[i] == t[i]) {
            List<Integer> list = new ArrayList<>();
            int n;
            do {
                n = l.remove(l.size() - 1);
                b[n] = false;
                list.add(n);
            } while (n != i);
            r.add(list);
        }
        return c;
    }

}
