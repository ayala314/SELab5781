package geometries;

import elements.Material;
import primitives.Color;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

public class Plane extends FlatGeometry {

    Point3D _p; //Q
    Vector _normal;

    // REMEMBER: never refer to _plane field from FlatGeometry
    // just keep it as null;
    public Plane(Color emissionLight, Material material, Point3D p1, Point3D p2, Point3D p3) {
        super(emissionLight, material);
        _p = setPoint(p1);
        _normal = setNormal(p1, p2, p3);
    }

    public Plane(Color emissionLight, Point3D p1, Point3D p2, Point3D p3) {
        super(emissionLight);
        _p = setPoint(p1);
        _normal = setNormal(p1, p2, p3);
    }

    public Plane(Point3D p1, Point3D p2, Point3D p3) {
        super();
        _p = setPoint(p1);
        _normal = setNormal(p1, p2, p3);
    }

    public Plane(Color emissionLight, Material material, Point3D point, Vector _normal) {
        super(emissionLight, material);
        _p = setPoint(point);
        this._normal = new Vector(_normal);
    }

    public Plane(Color emissionLight, Point3D point, Vector _normal) {
        super(emissionLight);
        _p = setPoint(point);
        this._normal = new Vector(_normal);
    }

    public Plane(Point3D point, Vector _normal) {
        super();
        _p = setPoint(point);
        this._normal = new Vector(_normal);
    }

    @Override
    public Vector getNormal(Point3D p) {
        return _normal;
    }

    private Vector setNormal(Point3D p1, Point3D p2, Point3D p3) {
        setPoint(p1);

        Vector U = new Vector(p1, p2);
        Vector V = new Vector(p1, p3);

        Vector N = U.crossProduct(V);

        return N.normalize();
    }

    private Point3D setPoint(Point3D p1) {
        return new Point3D(p1);
    }

    @Override
    public List<GeoPoint> findIntersections(Ray ray, double maxDistance) {
        Vector vectorp0Q;
        try {
            vectorp0Q = this._p.subtract(ray.getOriginPoint());
        } catch (IllegalArgumentException e) {
            return null; // ray starts from point Q - no intersections
        }

        double nv = _normal.dotProduct(ray.getDirection());
        if (isZero(nv)) { // ray is parallel to the plane - no intersections
            return null;
        }
        double t = alignZero(_normal.dotProduct(vectorp0Q) / nv);
        double tdist = alignZero(maxDistance - t);

        if ((t <= 0) || (tdist <= 0)) {
            return null;
        } else {
            return List.of(new GeoPoint(this, ray.getTargetPoint(t)));
        }
    }

}
