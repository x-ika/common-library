package com.simplejcode.commons.algo;

import java.util.*;

public class FibonacciHeap<T extends Comparable<T>> {

    public static class Node<E extends Comparable<E>> {
        boolean mark;
        int degree;
        E key;

        Node<E> child, left, parent, right;

        public Node(E key) {
            right = this;
            left = this;
            this.key = key;
            //this.key = key;
        }

        public final E getKey() {
            return key;
        }
    }

    private static final double oneOverLogPhi = 1.0 / Math.log((1.0 + Math.sqrt(5.0)) / 2.0);

    private Node<T> minNode;

    private int nNodes;

    public FibonacciHeap() {
    }

    public boolean isEmpty() {
        return minNode == null;
    }

    public void clear() {
        minNode = null;
        nNodes = 0;
    }

    public boolean insert(T key) {
        Node<T> node = new Node<>(key);
        node.key = key;

        // concatenate node into min list
        if (minNode != null) {
            node.left = minNode;
            node.right = minNode.right;
            minNode.right = node;
            node.right.left = node;

            if (key.compareTo(minNode.key) < 0) {
                minNode = node;
            }
        } else {
            minNode = node;
        }

        nNodes++;
        return true;
    }

    public Node<T> min() {
        return minNode;
    }

    public Node<T> removeMin() {
        Node<T> z = minNode;

        if (z != null) {
            int numKids = z.degree;
            Node<T> x = z.child;
            Node<T> tempRight;

            // for each child of z do...
            while (numKids > 0) {
                tempRight = x.right;

                // remove x from child list
                x.left.right = x.right;
                x.right.left = x.left;

                // add x to root list of heap
                x.left = minNode;
                x.right = minNode.right;
                minNode.right = x;
                x.right.left = x;

                // set parent[x] to null
                x.parent = null;
                x = tempRight;
                numKids--;
            }

            // remove z from root list of heap
            z.left.right = z.right;
            z.right.left = z.left;

            if (z == z.right) {
                minNode = null;
            } else {
                minNode = z.right;
                consolidate();
            }

            // decrement size of heap
            nNodes--;
        }

        return z;
    }

    public int size() {
        return nNodes;
    }

    public String toString() {
        if (minNode == null) {
            return "FibonacciHeap=[]";
        }

        // create a new stack and put root on it
        Stack<Node<T>> stack = new Stack<>();
        stack.push(minNode);

        StringBuffer buf = new StringBuffer(512);
        buf.append("FibonacciHeap=[");

        // do a simple breadth-first traversal on the tree
        while (!stack.empty()) {
            Node<T> curr = stack.pop();
            buf.append(curr);
            buf.append(", ");

            if (curr.child != null) {
                stack.push(curr.child);
            }

            Node<T> start = curr;
            curr = curr.right;

            while (curr != start) {
                buf.append(curr);
                buf.append(", ");

                if (curr.child != null) {
                    stack.push(curr.child);
                }

                curr = curr.right;
            }
        }

        buf.append(']');

        return buf.toString();
    }

    protected void cascadingCut(Node<T> y) {
        Node<T> z = y.parent;

        // if there's a parent...
        if (z != null) {
            // if y is unmarked, set it marked
            if (!y.mark) {
                y.mark = true;
            } else {
                // it's marked, cut it from parent
                cut(y, z);

                // cut its parent as well
                cascadingCut(z);
            }
        }
    }

    protected void consolidate() {
        int arraySize =
                ((int) Math.floor(Math.log(nNodes) * oneOverLogPhi)) + 1;

        List<Node<T>> array =
                new ArrayList<>(arraySize);

        // Initialize degree array
        for (int i = 0; i < arraySize; i++) {
            array.add(null);
        }

        // Find the number of root nodes.
        int numRoots = 0;
        Node<T> x = minNode;

        if (x != null) {
            numRoots++;
            x = x.right;

            while (x != minNode) {
                numRoots++;
                x = x.right;
            }
        }

        // For each node in root list do...
        while (numRoots > 0) {
            // Access this node's degree..
            int d = x.degree;
            Node<T> next = x.right;

            // ..and see if there's another of the same degree.
            for (; ; ) {
                Node<T> y = array.get(d);
                if (y == null) {
                    // Nope.
                    break;
                }

                // There is, make one of the nodes a child of the other.
                // Do this based on the key value.
                if (x.key.compareTo(y.key) > 0) {
                    Node<T> temp = y;
                    y = x;
                    x = temp;
                }

                // Node<E> y disappears from root list.
                link(y, x);

                // We've handled this degree, go to next one.
                array.set(d, null);
                d++;
            }

            // Save this node for later when we might encounter another
            // of the same degree.
            array.set(d, x);

            // Move forward through list.
            x = next;
            numRoots--;
        }

        // Set min to null (effectively losing the root list) and
        // reconstruct the root list from the array entries in array[].
        minNode = null;

        for (int i = 0; i < arraySize; i++) {
            Node<T> y = array.get(i);
            if (y == null) {
                continue;
            }

            // We've got a live one, add it to root list.
            if (minNode != null) {
                // First remove node from root list.
                y.left.right = y.right;
                y.right.left = y.left;

                // Now add to root list, again.
                y.left = minNode;
                y.right = minNode.right;
                minNode.right = y;
                y.right.left = y;

                // Check if this is a new min.
                if (y.key.compareTo(minNode.key) < 0) {
                    minNode = y;
                }
            } else {
                minNode = y;
            }
        }
    }

    protected void cut(Node<T> x, Node<T> y) {
        // remove x from childlist of y and decrement degree[y]
        x.left.right = x.right;
        x.right.left = x.left;
        y.degree--;

        // reset y.child if necessary
        if (y.child == x) {
            y.child = x.right;
        }

        if (y.degree == 0) {
            y.child = null;
        }

        // add x to root list of heap
        x.left = minNode;
        x.right = minNode.right;
        minNode.right = x;
        x.right.left = x;

        // set parent[x] to nil
        x.parent = null;

        // set mark[x] to false
        x.mark = false;
    }

    protected void link(Node<T> y, Node<T> x) {
        // remove y from root list of heap
        y.left.right = y.right;
        y.right.left = y.left;

        // make y a child of x
        y.parent = x;

        if (x.child == null) {
            x.child = y;
            y.right = y;
            y.left = y;
        } else {
            y.left = x.child;
            y.right = x.child.right;
            x.child.right = y;
            y.right.left = y;
        }

        // increase degree[x]
        x.degree++;

        // set mark[y] false
        y.mark = false;
    }

    public T pop() {
        return removeMin().key;
    }

}
