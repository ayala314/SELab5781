package geometries;

import elements.Material;
import primitives.Color;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Cylinder is afinite Tube with a certain _height
 */
public class Cylinder extends Tube {
    /**
     *
     */
    private final double _height;


    /**
     * constructor for a new Tube object
     *
     * @param radius        the radius of the tube
     * @param ray           the direction of the tube from the referenced point
     * @param material      the material of the tube
     * @param emissionLight the emission light of the tube
     * @param height        height of the cylinder (from the referenced point)
     *                      <p>
     *                      throws Exception in case of negative or zero radius from RadialGeometry constructor
     */
    public Cylinder(Color emissionLight, Material material, double radius, Ray ray, double height) {
        super(emissionLight, material, radius, ray);
        this._height = height;
    }

    public Cylinder(Color emissionLight, double radius, Ray ray, double height) {
        super(emissionLight, radius, ray);
        this._height = height;
    }

    /**
     * Cylinder constructor
     *
     * @param radius רradius of the Cylinder
     * @param ray    direction and reference point  of the cylinder
     * @param height height of the cylinder (from the referenced point)
     */
    public Cylinder(double radius, Ray ray, double height) {
        super(radius, ray);
        this._height = height;
    }

    /**
     * @param emissionLight
     * @param material
     * @param radius
     * @param p1
     * @param p2
     */
    public Cylinder(Color emissionLight, Material material, double radius, Point3D p1, Point3D p2) {
        super(emissionLight, material, radius, new Ray(p1, p2.subtract(p1)));
        this._height = p1.distance(p2);
    }

    /**
     * @param emissionLight
     * @param radius
     * @param p1
     * @param p2
     */
    public Cylinder(Color emissionLight, double radius, Point3D p1, Point3D p2) {
        super(emissionLight, radius, new Ray(p1, p2.subtract(p1)));
        this._height = p1.distance(p2);
    }

    /**
     * @param radius
     * @param p1
     * @param p2
     */
    public Cylinder(double radius, Point3D p1, Point3D p2) {
        super(radius, new Ray(p1, p2.subtract(p1)));
        this._height = p1.distance(p2);
    }

    public double get_height() {
        return _height;
    }

    /**
     * Getter for the starting point of the cylinder.
     *
     * @return e new Point3D that represents
     * the starting point of the cylinder.
     */
    public Point3D getP1() {
        return new Point3D(this._axisRay.getOriginPoint());
    }

    /**
     * Getter for the ending point of the cylinder.
     *
     * @return e new Point3D that represents
     * the ending point of the cylinder.
     */
    public Point3D getP2() {
        return this._axisRay.getTargetPoint(_height);
    }

    /**
     * @param point point to calculate the normal
     * @return normal
     * @author Dan Zilberstein adapted vy E.G.
     */
    @Override
    public Vector getNormal(Point3D point) {
        Point3D o = _axisRay.getOriginPoint();
        Vector v = _axisRay.getDirection();

        // projection of P-O on the ray:
        double t = 0;
        try {
            t = alignZero(point.subtract(o).dotProduct(v));
        } catch (IllegalArgumentException e) { // P = O
            return v;
        }

        // if the point is at a base
        if (t == 0 || isZero(_height - t)) // if it's close to 0, we'll get ZERO vector exception
            return v;

        return point.subtract(o.add(v.scale(t))).normalize();
    }

    @Override
    public List<GeoPoint> findIntersections(Ray ray, double maxDistance) {

        //provided by a student ( i forgot his name)
        List<GeoPoint> intersections = super.findIntersections(ray, maxDistance);
        if (intersections == null) {
            return null;
        }
        Vector va = this._axisRay.getDirection();
        Point3D A = this._axisRay.getOriginPoint();
        Point3D B = this._axisRay.getTargetPoint(_height);
        List<GeoPoint> intersectionsCylinder = new LinkedList<>();
        double lowerBound, upperBound;
        for (GeoPoint gPoint : intersections) {
            lowerBound = va.dotProduct(gPoint._point.subtract(A));
            upperBound = va.dotProduct(gPoint._point.subtract(B));
            if (lowerBound > 0 && upperBound < 0) {
                // the check for distance, if the intersection point is beyond the distance
                if (alignZero(gPoint._point.distance(ray.getOriginPoint()) - maxDistance) <= 0)
                    intersectionsCylinder.add(gPoint);
            }
        }
        Plane topA = new Plane(_emissionColor, _material, A, va);
        Plane bottomB = new Plane(_emissionColor, _material, B, va);
        List<GeoPoint> intersectionPlaneA = topA.findIntersections(ray, maxDistance);
        List<GeoPoint> intersectionPlaneB = bottomB.findIntersections(ray, maxDistance);
        if (intersectionPlaneA == null && intersectionPlaneB == null) {
            return intersectionsCylinder;
        }
        Point3D q3, q4;
        if (intersectionPlaneA != null) {
            q3 = intersectionPlaneA.get(0)._point;
            if (q3.subtract(A).lengthSquared() < _radius * _radius) {
                intersectionsCylinder.add(intersectionPlaneA.get(0));
            }
        }
        if (intersectionPlaneB != null) {
            q4 = intersectionPlaneB.get(0)._point;
            if (q4.subtract(B).lengthSquared() < _radius * _radius) {
                intersectionsCylinder.add(intersectionPlaneB.get(0));
            }
        }
        if (intersectionsCylinder.isEmpty()) {
            return null;
        }
        return intersectionsCylinder;
    }
}


/*
*************************************************

    @Override
    public List<GeoPoint> findIntersections(Ray ray, double maxDistance) {
        // Step 1: intersect with tube
        List<GeoPoint> intersections = super.findIntersections(ray, maxDistance);
        if (intersections == null) return null;
        // Step 2: intersect is between caps
        Vector va = _axisRay.get_dir();
        Point3D A = _axisRay.get_p();
        Point3D B = _axisRay.getTargetPoint(height);
        double lowerBound, upperBound;
        List<GeoPoint> intersectionsCylinder = new ArrayList<>();
        for (GeoPoint gPoint : intersections) {
            lowerBound = va.dotProduct(gPoint._point.subtract(A));
            upperBound = va.dotProduct(gPoint._point.subtract(B));
            if (lowerBound > 0 && upperBound < 0) {
                // the check for distance, if the intersection point is beyond the distance
                if (alignZero(gPoint._point.distance(ray.get_p()) - maxDistance) <= 0)
                    intersectionsCylinder.add(gPoint);
            }
        }
        // Step 3: intersect with each plane which belongs to the caps
        Plane planeA = new Plane(this._emissionLight, this._material, A, va);
        Plane planeB = new Plane(this._emissionLight, this._material, B, va);
        List<GeoPoint> intersectionPlaneA = planeA.findIntersections(ray, maxDistance);
        List<GeoPoint> intersectionPlaneB = planeB.findIntersections(ray, maxDistance);
        if (intersectionPlaneA == null && intersectionPlaneB == null)
            return intersectionsCylinder;
        // Step 4: intersect inside caps
        Point3D q3, q4;
        if (intersectionPlaneA != null) {
            q3 = intersectionPlaneA.get(0)._point;
            if (q3.subtract(A).lengthSquared() < _radius * _radius) {
                intersectionsCylinder.add(intersectionPlaneA.get(0));
            }
        }
        if (intersectionPlaneB != null) {
            q4 = intersectionPlaneB.get(0)._point;
            if (q4.subtract(B).lengthSquared() < _radius * _radius) {
                intersectionsCylinder.add(intersectionPlaneB.get(0));
            }
        }
        if (intersectionsCylinder.isEmpty()) return null;
        return intersectionsCylinder;
    }

    @Override
    public String toString() {
        return "height = " + height +
                " , _axisRay = " + _axisRay +
                " , _radius = " + _radius;
    }

*************************************************
 */
