package com.simplejcode.commons.gui;

import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Method;

import static java.lang.Math.*;

public final class GraphicUtils {
    private static final String VIS = "Visualization";

    public static int getIndex(int i, int j) {
        return i < j ? getIndex(j, i) : i * (i - 1) / 2 + j;
    }

    public static void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //--------------------------------- Painting ----------------------------------------

    public static int grayScale(int x) {
        return x << 16 | x << 8 | x;
    }

    public static int color(double d) {
        double r, g, b;
        if (d < 1. / 6) {
            b = d * 6;
            r = 0;
            g = 0;
        } else if (d < 2. / 6) {
            b = 1;
            r = 0;
            g = (d - 1. / 6) * 6;
        } else if (d < 1.0 / 2) {
            b = 1 - (d - 2. / 6) * 6;
            r = 0;
            g = 1;
        } else if (d < 4. / 6) {
            b = 0;
            r = (d - 3. / 6) * 6;
            g = 1;
        } else if (d < 5. / 6) {
            b = 0;
            r = 1;
            g = 1 - (d - 4. / 6) * 6;
        } else {
            r = 1;
            b = (d - 5. / 6) * 6;
            g = (d - 5. / 6) * 6;
        }
        return ((int) (255.99 * r) << 16) | ((int) (255.99 * g) << 8) | (int) (255.99 * b);
    }

    public static void showImage(BufferedImage image) {
        showImage(image, 1, 1);
    }

    public static void showImage(BufferedImage image, double sx, double sy) {
        JFrame f = new JFrame(VIS);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setSize((int) (image.getWidth() * sx + 10), (int) (image.getHeight() * sy + 50));
        f.setContentPane(new JPanel() {
            public void paint(Graphics g) {
                ((Graphics2D) g).scale(sx, sy);
                g.drawImage(image, 0, 0, this);
            }
        });
        f.setVisible(true);
        centerOnScreen(f);
    }

    public static void showPixels(int[] pixels, int w, int h) {
        final BufferedImage image = new BufferedImage(
                w, h, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, w, h, pixels, 0, w);
        showImage(image);
    }

    public static void showPixels(float[] pixels, int w, int h) {
        int[] p = new int[w * h];
        for (int i = 0; i < pixels.length; i++) {
            p[i] = grayScale((int) (256 * pixels[i]));
        }
        showPixels(p, w, h);
    }

    public static void showPixels(int[][] pixels) {
        int h = pixels.length, w = pixels[0].length;
        int[] p = new int[w * h];
        for (int i = 0; i < h; i++) {
            System.arraycopy(pixels[i], 0, p, i * w, w);
        }
        showPixels(p, w, h);
    }

    public static void showPixels(float[][] pixels) {
        int h = pixels.length, w = pixels[0].length;
        float[] p = new float[w * h];
        for (int i = 0; i < h; i++) {
            System.arraycopy(pixels[i], 0, p, i * w, w);
        }
        showPixels(p, w, h);
    }

    public static void showMatrix(int[][] a) {
        final int d1 = 40, d2 = 40, x = 10, y = 20, n = a.length, m = a[0].length;
        JFrame f = new JFrame(VIS);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setSize(d1 * m + 2 * x, d2 * n + 2 * y);
        f.setContentPane(new JPanel() {
            public void paint(Graphics g) {
                g.setColor(Color.white);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.black);
                for (int i = 0; i <= n; i++) {
                    g.drawLine(x, y + d2 * i, x + d1 * m, y + d2 * i);
                }
                for (int i = 0; i <= m; i++) {
                    g.drawLine(x + d1 * i, y, x + d1 * i, y + d2 * n);
                }
                g.setFont(new Font("Serif", Font.PLAIN, 30));
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < m; j++) {
                        g.drawString("" + a[i][j], x + d1 * j + d1 / 2, y + d2 * i + d2 / 2);
                    }
                }
            }
        });
        f.setVisible(true);
        centerOnScreen(f);
    }

    /**
     * @param a  left x
     * @param b  right x
     * @param d  gap
     * @param sx yourself
     * @param sy yourself
     * @param fs functions
     */
    public static void showFunctions(int a, int b, int d,
                                     double sx, double sy,
                                     double[]... fs)
    {
        JFrame frame = new JFrame(VIS);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize((int) (sx * (b - a) + 10), 700);
        frame.setLocation(1024 - frame.getWidth() >> 1, 0);
        frame.setContentPane(new JPanel() {
            public void paint(Graphics g) {
                for (int i = 0; i < b - a; i += 500) {
                    int x = (int) (sx * i);
                    g.drawLine(x, 600, x, 650);
                    g.drawString("" + i, x, 660);
                }
                g.translate(0, getHeight() >> 1);
                ((Graphics2D) g).scale(sx, -sy);
                for (double[] f : fs) {
                    for (int i = a; i < b - d; i++) {
                        g.drawLine(i - a, (int) f[i], i - a + d, (int) f[i + d]);
                    }
                }
            }
        });
        frame.setVisible(true);
    }

    /**
     * Draws graph
     *
     * @param w width
     * @param h height
     * @param x x
     * @param y y
     * @param b graph
     */
    public static void showGraph(int w, int h, int[] x, int[] y, boolean[][] b) {
        JFrame f = new JFrame(VIS);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(w + 20, h + 50);
        f.setContentPane(new JPanel() {
            public void paint(Graphics g) {
                int N = b.length;
                int maxX = 0, maxY = 0;
                for (int i = 0; i < N; i++) {
                    maxX = Math.max(maxX, x[i]);
                    maxY = Math.max(maxY, y[i]);
                }
                if (maxX > w || maxY > h) {
                    for (int i = 0; i < N; i++) {
                        x[i] = x[i] * w / maxX;
                        y[i] = y[i] * h / maxY;
                    }
                }
                for (int i = 0; i < N; i++) {
                    g.drawRect(x[i] - 1, y[i] - 1, 2, 2);
                    for (int j = 0; j < i; j++) {
                        if (b[i][j]) {
                            g.drawLine(x[i], y[i], x[j], y[j]);
                        }
                    }
                }
            }
        });
        f.setVisible(true);
        centerOnScreen(f);
    }

    public static void showGraph(boolean[][] b) {
        JFrame f = new JFrame(VIS);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(900, 600);
        f.setContentPane(new JPanel() {
            public void paint(Graphics g) {
                int R = 200, N = b.length;
                int[] x = new int[N];
                int[] y = new int[N];
                for (int i = 0; i < N; i++) {
                    x[i] = (int) (400 + R * cos(PI / N * 2 * i));
                    y[i] = (int) (300 + R * sin(PI / N * 2 * i));
                    g.drawRect(x[i] - 1, y[i] - 1, 2, 2);
                    for (int j = 0; j < i; j++) {
                        if (b[i][j]) {
                            g.drawLine(x[i], y[i], x[j], y[j]);
                        }
                    }
                }
            }
        });
        f.setVisible(true);
        centerOnScreen(f);
    }

    public static void showGraph(int[] a, int n) {
        boolean[][] b = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                int x = getIndex(i, j);
                b[i][j] = b[j][i] = (a[x >> 5] & 1 << x) != 0;
            }
        }
        showGraph(b);
    }

    //-----------------------------------------------------------------------------------

    public static GraphicsDevice getDevice() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    }

    public static void centerOnScreen(Component c) {
        centerComponent(c, GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
    }

    public static void centerOnParent(Component c) {
        if (c != null && c.getParent() != null && c.getParent().getBounds() != null) {
            centerComponent(c, c.getParent().getBounds());
        }
    }

    public static void centerComponent(Component c, Rectangle r) {
        c.setLocation(((int) r.getWidth() - c.getWidth()) / 2,
                ((int) r.getHeight() - c.getHeight()) / 2);
    }

    public static void fullScreenMode(Component c) {
        Rectangle r = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

        c.setBounds(r);
    }

    public static JMenuBar createMenuBar(String[][] menuItemNames, Object handler, Method onException, Method... actions) {

        int actionIndex = 0;

        JMenuBar menuBar = new JMenuBar();

        for (String[] menuStrings : menuItemNames) {
            JMenu menu = new JMenu(menuStrings[0]);
            menu.setMnemonic(menuStrings[0].charAt(0));
            for (int i = 1; i < menuStrings.length; i++) {
                if (menuStrings[i] == null) {
                    menu.addSeparator();
                } else {
                    Method action = actionIndex < actions.length ? actions[actionIndex++] : null;
                    menu.add(createAction(menuStrings[i], handler, action, onException));
                }
            }
            menuBar.add(menu);
        }

        return menuBar;
    }

    public static AbstractAction createAction(String name, Object handler, Method action) {
        return createAction(name, handler, action, null);
    }

    public static AbstractAction createAction(String name, Object handler, Method action, Method onException) {
        return new AbstractAction(name) {
            public void actionPerformed(ActionEvent e) {
                if (action == null) {
                    return;
                }
                new Thread(() -> {
                    try {
                        action.invoke(handler);
                    } catch (Exception ex) {
                        if (onException != null) {
                            try {
                                onException.invoke(handler, ex);
                            } catch (Exception e1) {
                                // how many times?
                            }
                        } else {
                            ex.printStackTrace();
                            System.out.println("Exception thrown when handling the action.");
                        }
                    }
                }).start();
            }
        };
    }

    //-----------------------------------------------------------------------------------

    public static File chooseFile(Window window, String def, int mode) {
        return chooseFile(window, def, mode, null);
    }

    public static File chooseFile(Window window, String def, int mode, FileFilter filter) {
        return chooseFile(window, def, mode, filter, (String[]) null);
    }

    public static File chooseFile(Window window, String def, int mode, String desc, String... ext) {
        return chooseFile(window, def, mode, false, desc, ext);
    }

    public static File chooseFile(Window window, String def, int mode, boolean ensureExtension, String desc, String... ext) {
        return chooseFile(window, def, mode, new FileNameExtensionFilter(desc, ext), ensureExtension ? ext : null);
    }

    private static File chooseFile(Window window, String def, int mode, FileFilter filter, String... ext) {
        JFileChooser fileChooser = new JFileChooser(def);
        fileChooser.setFileFilter(filter);
        centerOnScreen(fileChooser);
        fileChooser.setFileSelectionMode(mode);
        if (fileChooser.showOpenDialog(window) != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        File file = fileChooser.getSelectedFile();
        if (ext == null) {
            return file;
        }
        for (String s : ext) {
            if (file.getName().endsWith("." + s)) {
                return file;
            }
        }
        return new File(file.getAbsolutePath() + "." + ext[0]);
    }

}
