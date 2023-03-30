package com.github.imdabigboss.easydatapack.api.utils.math;

/**
 * A 3D vector.
 */
public class Vector3f implements Cloneable {
    private float x;
    private float y;
    private float z;

    /**
     * Creates a new vector.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Creates a new vector with all coordinates set to 0.
     */
    public Vector3f() {
        this(0, 0, 0);
    }

    /**
     * Gets the x coordinate.
     * @return the x coordinate
     */
    public float getX() {
        return this.x;
    }

    /**
     * Gets the y coordinate.
     * @return the y coordinate
     */
    public float getY() {
        return this.y;
    }

    /**
     * Gets the z coordinate.
     * @return the z coordinate
     */
    public float getZ() {
        return this.z;
    }

    /**
     * Adds a vector to this vector.
     * @param vector the vector to add
     */
    public Vector3f add(Vector3f vector) {
        this.x += vector.getX();
        this.y += vector.getY();
        this.z += vector.getZ();

        return this;
    }

    /**
     * Adds a vector to this vector.
     * @param x the x coordinate to add
     * @param y the y coordinate to add
     * @param z the z coordinate to add
     */
    public Vector3f add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;

        return this;
    }

    /**
     * Subtracts a vector from this vector.
     * @param vector the vector to subtract
     */
    public Vector3f subtract(Vector3f vector) {
        this.x -= vector.getX();
        this.y -= vector.getY();
        this.z -= vector.getZ();

        return this;
    }

    /**
     * Subtracts a vector from this vector.
     * @param x the x coordinate to subtract
     * @param y the y coordinate to subtract
     * @param z the z coordinate to subtract
     */
    public Vector3f subtract(float x, float y, float z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;

        return this;
    }

    /**
     * Sets this vector's values to the given vector's values.
     * @param vector the vector to set to
     */
    public void set(Vector3f vector) {
        this.x = vector.getX();
        this.y = vector.getY();
        this.z = vector.getZ();
    }

    /**
     * Sets this vector's values to the given coordinates.
     * @param x the x coordinate to set to
     * @param y the y coordinate to set to
     * @param z the z coordinate to set to
     */
    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Clones the vector.
     * @return the cloned vector
     */
    @Override
    public Vector3f clone() {
        //No deep copy needed
        try {
            return (Vector3f) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /**
     * Converts this vector to a JOML vector with the coordinates divided by 16.
     * @return the converted vector
     */
    public org.joml.Vector3f toBlockJOML() {
        return new org.joml.Vector3f(this.x / 16, this.y / 16, this.z / 16);
    }
}
