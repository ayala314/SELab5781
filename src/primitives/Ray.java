package primitives;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static primitives.Util.isZero;

/**
 * Ray class
 */
public class Ray {

    private static final double EPSILON = 0.1;
    private static final Random rnd = new Random();

    /**
     * The point from which the ray starts.
     */
    private final Point3D _point;
    /**
     * The direction of the ray.
     */
    private final Vector _direction;

    /**
     * Constructor for creating a new instance of this class
     *
     * @param point     the start of the ray.
     * @param direction the direction of the ray.
     */
    public Ray(Point3D point, Vector direction) {
        _point = new Point3D(point);
        _direction = direction.normalized();
    }

    public Ray(Point3D point, Vector direction, Vector normal) {
        //point + normal.scale(±EPSILON)
        _direction = direction.normalized();

        double nv = normal.dotProduct(_direction);

        Vector normalEpsilon = normal.scale((nv > 0 ? EPSILON : -EPSILON));
        _point = point.add(normalEpsilon);
    }

    /**
     * Copy constructor for a deep copy of an Ray object.
     *
     * @param other the object that being copied
     */
    public Ray(Ray other) {
        this._point = new Point3D(other._point);
        this._direction = new Vector(other._direction);

    }

    /**
     * Getter for the point from which the ray starts.
     *
     * @return A new Point3D that represents the
     * point from which the ray starts.
     */
    public Point3D getOriginPoint() {
//        return new Point3D(_point);
        return _point;
    }

    /**
     * Getter for the direction of the ray that is
     * represented by this object.
     *
     * @return A new Vector that represents the
     * direction of the ray that is
     * represented by this object.
     */
    public Vector getDirection() {
//        return new Vector(_direction);
        return _direction;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Ray)) {
            return false;
        }
        if (this == obj) {
            return true;
        }

        Ray other = (Ray) obj;
        return (_point.equals(other._point) && _direction.equals(other._direction));
    }

    @Override
    public String toString() {
        return "point: " + _point + ", direction: " + _direction;
    }

    /**
     * @param length length to reach the target point
     * @return new Point3D
     * @author Dan Zilberstein
     */
    public Point3D getTargetPoint(double length) {
        if (isZero(length)) {
            return new Point3D(_point);
        }

        Vector targetVector = _direction.scale(length);

        return _point.add(targetVector);
    }

    public List<Ray> getBeamThroughPoint(Point3D destPoint, double radius, int amount) {
//
//        Vector lightDirection = l.scale(-1); // from point to light source
//        Point3D pointGeo = geopoint.getPoint();
//        Ray lightRay = new Ray(pointGeo, lightDirection, n);
//
        double distance = this._point.distance(destPoint);

        if (isZero(distance)) {
            throw new IllegalArgumentException("distance cannot be 0");
        }

        //Thanks to Raphael Knoll
        Vector v = this._direction;
        Vector normX;
        // when v._head == (0,0,w) we have a problem
        if (isZero(v._head._y._coord) && isZero(v._head._x._coord)) {
            normX = new Vector(v._head._z._coord * -1, 0, 0).normalize();
        } else {
            normX = new Vector(v._head._y._coord * -1, v._head._x._coord, 0).normalize();
        }
        Vector normY = v.crossProduct(normX).normalize();

        List<Ray> rays = new LinkedList<>();


        for (int counter = 0; counter < amount; counter++) {
            Point3D newPoint = new Point3D(destPoint);
            double cosTheta = 2 * rnd.nextDouble() - 1;
            double sinTheta = Math.sqrt(1d - cosTheta * cosTheta);

            //d ranged between -radius and  +radius
            double d = radius * (2 * rnd.nextDouble() - 1);
            double x = d * cosTheta;
            double y = d * sinTheta;

            if (isZero(d)) {
                continue;
            }
            if (!isZero(x)) {
                newPoint = newPoint.add(normX.scale(x));
            }
            if (!isZero(y)) {
                newPoint = newPoint.add(normY.scale(y));
            }
            rays.add(new Ray(this._point, newPoint.subtract(this._point)));
        }
        rays.add(this);
        return rays;
    }

}
