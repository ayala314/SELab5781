package geometries;

import elements.Material;
import primitives.Color;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Represents an infinite tube in the 3D space.
 * That is, the cylinder does not have a length.
 */

public class Tube extends RadialGeometry {
    /**
     * represents the direction and the reference point
     */
    protected final Ray _axisRay;

    /**
     * constructor for a new Tube object
     *
     * @param _radius       the radius of the tube
     * @param _ray          the direction of the tube from the referenced point
     * @param material      the material of the tube
     * @param emissionLight the emission light of the tube
     *                      <p>
     *                      throws Exception in case of negative or zero radius from RadialGeometry constructor
     */
    public Tube(Color emissionLight, Material material, double _radius, Ray _ray) {
        super(emissionLight, material, _radius);
        this._axisRay = new Ray(_ray);

    }

    public Tube(Color emissionLight, double radius, Ray ray) {
        super(emissionLight, radius);
        this._axisRay = new Ray(ray);
    }

    public Tube(double radius, Ray ray) {
        super(radius);
        this._axisRay = new Ray(ray);
    }

    public Ray getAxisRay() {
        return _axisRay;
    }


//    @Override
//    public boolean equals(Object obj) {
//        if (obj == null || !(obj instanceof Tube))
//            return false;
//        if (this == obj)
//            return true;
//        Tube other = (Tube) obj;
//
//        //the two vectors needs to be in the same direction,
//        //but not necessary to have the same length.
//        try {
//            Vector v = _ray.getDirection().crossProduct(other._ray.getDirection());
//        } catch (IllegalArgumentException ex) {
//            return (Util.isZero(this._radius - other._radius) && _ray.getPoint().equals((_ray.getPoint())));
//        }
//        throw new IllegalArgumentException("direction cross product with parameter.direction == Vector(0,0,0)");
//    }

    @Override
    public String toString() {
        return "ray: " + _axisRay +
                ", radius: " + _radius;
    }

    /**
     * @param point point to calculate the normal
     * @return returns normal vector
     */
    @Override
    public Vector getNormal(Point3D point) {
        //The vector from the point of the cylinder to the given point
        Point3D o = _axisRay.getOriginPoint(); // at this point o = p0
        Vector v = _axisRay.getDirection();

        Vector vector1 = point.subtract(o);

        //We need the projection to multiply the _direction unit vector
        double projection = vector1.dotProduct(v);
        if (!isZero(projection)) {
            // projection of P-O on the ray:
            o = o.add(v.scale(projection));
        }

        //This vector is orthogonal to the _direction vector.
        Vector check = point.subtract(o);
        return check.normalize();
    }

    @Override
    public List<GeoPoint> findIntersections(Ray ray, double maxDistance) {
        //TODO implementation according properly to tube attributes

        Point3D p = ray.getOriginPoint();
        Point3D p0 = this._axisRay.getOriginPoint();
        Vector deltaP = p.subtract(p0);
        Vector v = ray.getDirection();
        Vector va = this._axisRay.getDirection();
        double vDotVa = v.dotProduct(va);
        double deltaPDotVa = deltaP.dotProduct(va);
        Vector temp_for_use1, temp_for_use2;
        double A, B, C, determinant;
        if (isZero(vDotVa) && !isZero(deltaPDotVa)) {
            A = v.lengthSquared();
            B = 2 * (v.dotProduct(deltaP.subtract(va.scale(deltaPDotVa))));
            C = deltaP.subtract(va.scale(deltaPDotVa)).lengthSquared() - this._radius * this._radius;
        } else if (isZero(vDotVa) && isZero(deltaPDotVa)) {
            A = v.lengthSquared();
            B = 2 * v.dotProduct(deltaP);
            C = deltaP.lengthSquared() - this._radius * this._radius;
        } else if (!isZero(vDotVa) && isZero(deltaPDotVa)) {
            A = v.subtract(va.scale(vDotVa)).lengthSquared();
            B = 2 * v.subtract(va.scale(vDotVa)).dotProduct(deltaP);
            C = deltaP.lengthSquared() - this._radius * this._radius;
        } else {
            A = v.subtract(va.scale(vDotVa)).lengthSquared();
            B = 2 * v.subtract(va.scale(vDotVa)).dotProduct(deltaP.subtract(va.scale(deltaPDotVa)));
            C = deltaP.subtract(va.scale(deltaPDotVa)).lengthSquared() - this._radius * this._radius;
        }

        determinant = B * B - 4 * A * C;
        if (determinant < 0) {//No solution
            return null;
        }

        double t1 = (-B + Math.sqrt(determinant)) / (2 * A),
                t2 = (-B - Math.sqrt(determinant)) / (2 * A);

        if (isZero(determinant)) {//One solution
            return List.of(new GeoPoint(this, ray.getTargetPoint(t1)));

        }
        if (alignZero(t1 - maxDistance) <= 0) {
            if (alignZero(t2 - maxDistance) <= 0 && t1 > 0 && t2 > 0) {
                return List.of(
                        new GeoPoint(this, ray.getTargetPoint(t1)),
                        new GeoPoint(this, ray.getTargetPoint(t2)));
            } else if (t1 > 0) {
                return List.of
                        (new GeoPoint(this, ray.getTargetPoint(t1)));
            }
        } else if (alignZero(t2 - maxDistance) <= 0 && t2 > 0) {
            return List.of(
                    new GeoPoint(this, ray.getTargetPoint(t2)));
        }

        return null;
    }
}