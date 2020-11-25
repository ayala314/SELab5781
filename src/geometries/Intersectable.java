package geometries;

import primitives.*;


import java.util.List;

/**
 * Intersectable is a common interface for all geometries that are able
 * to intersect from a ray to their entity (Shape)
 */
public interface Intersectable {


    /**
     * @param ray         current ray
     * @param maxDistance max distance
     * @return list of GeoPoint that intersect with the ray
     */
    List<GeoPoint> findIntersections(Ray ray, double maxDistance);

    /**
     * @param ray current ray
     * @return list of GeoPoint that intersect with the ray
     */
    default List<GeoPoint> findIntersections(Ray ray) {
        return findIntersections(ray, Double.POSITIVE_INFINITY);
    }

    /**
     * GeoPoint is just a tuple holding
     * references to a specific geometry and it's specific point
     * acting as a public static class (inner class)
     */
    class GeoPoint {

        protected Geometry _geometry;
        protected Point3D _point;

        public GeoPoint(Geometry geometry, Point3D pt) {
            this._geometry = geometry;
            this._point = pt;
        }

        public Point3D getPoint() {
            return _point;
        }

        public Geometry getGeometry() {
            return _geometry;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            GeoPoint geoPoint = (GeoPoint) o;

            return ((_geometry.equals(geoPoint._geometry)) && (_point.equals(geoPoint._point)));
        }
    }
    //end of GeoPoint
}
//end of Intersectable