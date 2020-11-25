package primitives;

/**
 * Class Point3D is the basic class representing a point in a
 * 3D system.
 *
 * @author Eliezer
 */
public class Point3D {
    public final static Point3D ZERO = new Point3D(0.0, 0.0, 0.0);

    final Coordinate _x;
    final Coordinate _y;
    final Coordinate _z;

    /**
     * _x coordinate on the X axis
     * _y coordinate on the Y axis
     * _z coordinate on the Z axis
     */
    public Point3D(Coordinate x, Coordinate y, Coordinate z) {
        this(x._coord, y._coord, z._coord);
    }

    public Point3D(Point3D point3D) {
        this(point3D._x._coord, point3D._y._coord, point3D._z._coord);
    }


    public Point3D(double x, double y, double z) {
        this._x = new Coordinate(x);
        this._y = new Coordinate(y);
        this._z = new Coordinate(z);
    }

    public Coordinate getX() {
        return new Coordinate(_x);
    }

    public Coordinate getY() {
        return new Coordinate(_y);
    }

    public Coordinate getZ() {
        return new Coordinate(_z);
    }

    public double distanceSquared(Point3D otherPoint3D) {
        return ((otherPoint3D._x._coord - this._x._coord) * (otherPoint3D._x._coord - this._x._coord) +
                (otherPoint3D._y._coord - this._y._coord) * (otherPoint3D._y._coord - this._y._coord) +
                (otherPoint3D._z._coord - this._z._coord) * (otherPoint3D._z._coord - this._z._coord));
    }

    public double distance(Point3D otherPoint3D) {
        return Math.sqrt(distanceSquared(otherPoint3D));
    }

    /**
     * produce a new Point3D with the vector head values added
     * to current point values
     *
     * @param vector vector to add to current point value
     * @return new Point3D with added vector
     */
    public Point3D add(Vector vector) {
        return new Point3D(
                this._x._coord + vector._head._x._coord,
                this._y._coord + vector._head._y._coord,
                this._z._coord + vector._head._z._coord);
    }

    /**
     * produce a new Point3D with the other Point3D values added
     * to current point values
     *
     * @param point3D other point
     * @return new Point3D with added values
     */
    public Point3D add(Point3D point3D) {
        return new Point3D(
                this._x._coord + point3D._x._coord,
                this._y._coord + point3D._y._coord,
                this._z._coord + point3D._z._coord);
    }

    /**
     * this.values(x,y,z) - vector.head.values(x,y,z)
     *
     * @param vector parameter
     * @return new Point3D
     */
    public Point3D subtract(Vector vector) {
        return new Point3D(
                this._x._coord - vector._head._x._coord,
                this._y._coord - vector._head._y._coord,
                this._z._coord - vector._head._z._coord);
    }

    /**
     * substract otherpoint from me
     *
     * @param otherPoint3D other point
     * @return new Point(this.values - otherpoint.values)
     */
    public Vector subtract(Point3D otherPoint3D) {
        return new Vector(
                this._x._coord - otherPoint3D._x._coord,
                this._y._coord - otherPoint3D._y._coord,
                this._z._coord - otherPoint3D._z._coord);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point3D point3D = (Point3D) o;
        return _x.equals(point3D._x) &&
                _y.equals(point3D._y) &&
                _z.equals(point3D._z);
    }

    @Override
    public String toString() {
        return "(" +
                _x +
                ", " + _y +
                ", " + _z +
                ')';
    }
}
