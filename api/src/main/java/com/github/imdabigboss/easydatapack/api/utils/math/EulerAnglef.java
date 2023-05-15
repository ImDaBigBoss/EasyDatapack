package com.github.imdabigboss.easydatapack.api.utils.math;

import org.joml.Quaternionf;

/**
 * This class represents an Euler angle.
 */
public class EulerAnglef implements Cloneable {
    private float roll; // x
    private float pitch; // y
    private float yaw; // z

    /**
     * Creates a new Euler angle.
     * @param roll the roll (x)
     * @param pitch the pitch (y)
     * @param yaw the yaw (z)
     */
    public EulerAnglef(float roll, float pitch, float yaw) {
        this.roll = roll;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    /**
     * Creates a new Euler angle with all angles set to 0.
     */
    public EulerAnglef() {
        this(0, 0, 0);
    }

    /**
     * Creates a new Euler angle from a quaternion.
     * @param quaternion the quaternion
     */
    public EulerAnglef(Quaternionf quaternion) {
        float sinrCosp = 2 * (quaternion.w * quaternion.x + quaternion.y * quaternion.z);
        float cosrCosp = 1 - 2 * (quaternion.x * quaternion.x + quaternion.y * quaternion.y);
        this.roll = (float) Math.atan2(sinrCosp, cosrCosp);

        double sinp = Math.sqrt(1 + 2 * (quaternion.w * quaternion.y - quaternion.z * quaternion.x));
        double cosp = Math.sqrt(1 - 2 * (quaternion.w * quaternion.y - quaternion.z * quaternion.x));
        this.pitch = (float) Math.atan2(sinp, cosp);

        float sinyCosp = 2 * (quaternion.w * quaternion.z + quaternion.x * quaternion.y);
        float cosyCosp = 1 - 2 * (quaternion.y * quaternion.y + quaternion.z * quaternion.z);
        this.yaw = (float) Math.atan2(sinyCosp, cosyCosp);
    }

    /**
     * Gets the roll (x).
     * @return the roll
     */
    public float getRoll() {
        return this.roll;
    }

    /**
     * Gets the pitch (y).
     * @return the pitch
     */
    public float getPitch() {
        return this.pitch;
    }

    /**
     * Gets the yaw (z).
     * @return the yaw
     */
    public float getYaw() {
        return this.yaw;
    }

    /**
     * Converts this Euler angle to a quaternion.
     * @return the quaternion
     */
    public Quaternionf toQuaternion() {
        double cr = Math.cos(this.roll * 0.5);
        double sr = Math.sin(this.roll * 0.5);
        double cp = Math.cos(this.pitch * 0.5);
        double sp = Math.sin(this.pitch * 0.5);
        double cy = Math.cos(this.yaw * 0.5);
        double sy = Math.sin(this.yaw * 0.5);

        double w = cr * cp * cy + sr * sp * sy;
        double x = sr * cp * cy - cr * sp * sy;
        double y = cr * sp * cy + sr * cp * sy;
        double z = cr * cp * sy - sr * sp * cy;

        return new Quaternionf(x, y, z, w);
    }

    /**
     * Adds a Euler angle to this one
     * @param angle the angle to add
     */
    public EulerAnglef add(EulerAnglef angle) {
        this.roll += angle.getRoll();
        this.pitch += angle.getPitch();
        this.yaw += angle.getYaw();

        return this;
    }

    /**
     * Adds a Euler angle to this one
     * @param roll the roll to add
     * @param pitch the pitch to add
     * @param yaw the yaw to add
     */
    public EulerAnglef add(float roll, float pitch, float yaw) {
        this.roll += roll;
        this.pitch += pitch;
        this.yaw += yaw;

        return this;
    }

    /**
     * Subtracts a Euler angle from this one
     * @param angle the angle to subtract
     */
    public EulerAnglef subtract(EulerAnglef angle) {
        this.roll -= angle.getRoll();
        this.pitch -= angle.getPitch();
        this.yaw -= angle.getYaw();

        return this;
    }

    /**
     * Subtracts a Euler angle from this one
     * @param roll the roll to subtract
     * @param pitch the pitch to subtract
     * @param yaw the yaw to subtract
     */
    public EulerAnglef subtract(float roll, float pitch, float yaw) {
        this.roll -= roll;
        this.pitch -= pitch;
        this.yaw -= yaw;

        return this;
    }

    /**
     * Sets this Euler angle's values to the given angle's values.
     * @param angle the angle to set to
     */
    public void set(EulerAnglef angle) {
        this.roll = angle.getRoll();
        this.pitch = angle.getPitch();
        this.yaw = angle.getYaw();
    }

    /**
     * Sets this Euler angle's values to the given values.
     * @param roll the roll to set to
     * @param pitch the pitch to set to
     * @param yaw the yaw to set to
     */
    public void set(float roll, float pitch, float yaw) {
        this.roll = roll;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    /**
     * Clones the vector.
     * @return the cloned vector
     */
    @Override
    public EulerAnglef clone() {
        //No deep copy needed
        try {
            return (EulerAnglef) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
