package geometries;

import elements.Material;
import primitives.Color;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;

public class Sphere extends RadialGeometry {
    /**
     * The center of the sphere
     */
    private final Point3D _center;


    /**
     * constructor for a new sphere object.
     *
     * @param emissionLight emission light color
     * @param material      material
     * @param radius        the radius of the sphere
     * @param center        the center point of the sphere
     *                      <p>
     *                      throws Exception in case of negative or zero radius from RadialGeometry constructor
     */

    public Sphere(Color emissionLight, Material material, double radius, Point3D center) {
        super(emissionLight, material, radius);
        this._center = new Point3D(center);
    }

    public Sphere(Color emissionLight, double radius, Point3D center) {
        super(emissionLight, radius);
        this._center = new Point3D(center);
    }

    public Sphere(double radius, Point3D center) {
        super(radius);
        this._center = new Point3D(center);

    }

    @Override
    public String toString() {
        return ("point: " + _center + ", radius: " + _radius);
    }

    /**
     * getter for the center property
     *
     * @return the center of the sphere
     */
    public Point3D getCenter() {
        return new Point3D(_center);
    }


    /**
     * get the normal to this sphere in a given point
     */
    @Override
    public Vector getNormal(Point3D point) {
        Vector normal = point.subtract(_center);
        return normal.normalize();
    }

    @Override
    public List<GeoPoint> findIntersections(Ray ray, double maxDistance) {
        Point3D p0 = ray.getOriginPoint();
        Vector v = ray.getDirection();
        Vector u;
        try {
            u = _center.subtract(p0);   // p0 == _center
        } catch (IllegalArgumentException e) {
            return List.of(new GeoPoint(this, (ray.getTargetPoint(this._radius))));
        }
        double tm = alignZero(v.dotProduct(u));
        double dSquared = (tm == 0) ? u.lengthSquared() : u.lengthSquared() - tm * tm;
        double thSquared = alignZero(this._radius * this._radius - dSquared);

        if (thSquared <= 0) return null;

        double th = alignZero(Math.sqrt(thSquared));
        if (th == 0) return null;

        double t1 = alignZero(tm - th);
        double t2 = alignZero(tm + th);

        double t1dist = alignZero(maxDistance - t1);
        double t2dist = alignZero(maxDistance - t2);

        if (t1 <= 0 && t2 <= 0) {
            return null;
        }

        if (t1 > 0 && t2 > 0) {
            if (t1dist > 0 && t2dist > 0) {
                return List.of(
                        new GeoPoint(this, (ray.getTargetPoint(t1))),
                        new GeoPoint(this, (ray.getTargetPoint(t2)))); //P1 , P2
            } else if (t1dist > 0) {
                return List.of(
                        new GeoPoint(this, (ray.getTargetPoint(t1))));
            } else if (t2dist > 0) {
                return List.of(
                        new GeoPoint(this, (ray.getTargetPoint(t2))));
            }
        }

        if ((t1 > 0) && (t1dist > 0))
            return List.of(new GeoPoint(this, (ray.getTargetPoint(t1))));
        else if ((t2 > 0) && (t2dist > 0))
            return List.of(new GeoPoint(this, (ray.getTargetPoint(t2))));
        return null;
    }
}
