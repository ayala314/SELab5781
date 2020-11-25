package geometries;

import elements.Material;
import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

/**
 * interface Geometry is the basic interface for all geometric objects
 * who are implementing getNormal method.
 *
 * @author Bobby McFerrin don't worry be Happy
 */
public abstract class Geometry implements Intersectable {

    protected final Color _emissionColor;
    protected final Material _material;


    public Geometry(Color emission, Material material) {
        //we are never changing those values
        this._emissionColor = emission;
        this._material = material;
//        this._emissionColor = new Color(emission);
//        this._material = new Material(material);

    }

    public Geometry(Color emission) {
        this(emission, new Material());
    }

    public Geometry() {
        this(Color.BLACK);
    }

    /**
     * @return emissionColor instance
     */
    public Color getEmissionLight() {
        return new Color(_emissionColor);
    }

    /**
     * @return material instance
     */
    public Material getMaterial() {
        return _material;
    }

    public abstract Vector getNormal(Point3D p);
}
