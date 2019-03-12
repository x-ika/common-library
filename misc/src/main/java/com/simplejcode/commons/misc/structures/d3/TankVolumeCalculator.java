package com.simplejcode.commons.misc.structures.d3;

public class TankVolumeCalculator {

    private Polyhedron tank;

    private Point3D underground;

    public TankVolumeCalculator(Polyhedron tank) {
        this.tank = tank;
        underground = new Point3D(0, 0, -1e9);
    }

    public double getVolume(double x, double y, double z, double ax, double ay) {

        Plane fuelSurface = new Plane(x, y, z, ax, ay);

        double s = fuelSurface.signum(underground);

        Polyhedron fuel = tank.divide(fuelSurface)[s < 0 ? 0 : 1];

        return fuel.getVolume();
    }

}
