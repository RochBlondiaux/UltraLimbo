package me.rochblondiaux.ultralimbo.utils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Vector implements Cloneable {

    private static final double epsilon = 0.000001;

    protected double x;
    protected double y;
    protected double z;

    public Vector(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Vector add(Vector vec) {
        x += vec.x;
        y += vec.y;
        z += vec.z;
        return this;
    }

    public Vector subtract(Vector vec) {
        x -= vec.x;
        y -= vec.y;
        z -= vec.z;
        return this;
    }

    /**
     * Multiplies the vector by another.
     *
     * @param vec The other vector
     * @return the same vector
     */
    public Vector multiply(Vector vec) {
        x *= vec.x;
        y *= vec.y;
        z *= vec.z;
        return this;
    }

    /**
     * Divides the vector by another.
     *
     * @param vec The other vector
     * @return the same vector
     */
    public Vector divide(Vector vec) {
        x /= vec.x;
        y /= vec.y;
        z /= vec.z;
        return this;
    }

    /**
     * Copies another vector
     *
     * @param vec The other vector
     * @return the same vector
     */
    public Vector copy(Vector vec) {
        x = vec.x;
        y = vec.y;
        z = vec.z;
        return this;
    }

    /**
     * Gets the magnitude of the vector, defined as sqrt(x^2+y^2+z^2). The
     * value of this method is not cached and uses a costly square-root
     * function, so do not repeatedly call this method to get the vector's
     * magnitude. NaN will be returned if the inner result of the sqrt()
     * function overflows, which will be caused if the length is too long.
     *
     * @return the magnitude
     */
    public double length() {
        return Math.sqrt(NumberConversions.square(x) + NumberConversions.square(y) + NumberConversions.square(z));
    }

    /**
     * Gets the magnitude of the vector squared.
     *
     * @return the magnitude
     */
    public double lengthSquared() {
        return NumberConversions.square(x) + NumberConversions.square(y) + NumberConversions.square(z);
    }

    /**
     * Get the distance between this vector and another. The value of this
     * method is not cached and uses a costly square-root function, so do not
     * repeatedly call this method to get the vector's magnitude. NaN will be
     * returned if the inner result of the sqrt() function overflows, which
     * will be caused if the distance is too long.
     *
     * @param o The other vector
     * @return the distance
     */
    public double distance(Vector o) {
        return Math.sqrt(NumberConversions.square(x - o.x) + NumberConversions.square(y - o.y) + NumberConversions.square(z - o.z));
    }

    /**
     * Get the squared distance between this vector and another.
     *
     * @param o The other vector
     * @return the distance
     */
    public double distanceSquared(Vector o) {
        return NumberConversions.square(x - o.x) + NumberConversions.square(y - o.y) + NumberConversions.square(z - o.z);
    }

    /**
     * Gets the angle between this vector and another in radians.
     *
     * @param other The other vector
     * @return angle in radians
     */
    public float angle(Vector other) {
        double dot = dot(other) / (length() * other.length());

        return (float) Math.acos(dot);
    }

    /**
     * Sets this vector to the midpoint between this vector and another.
     *
     * @param other The other vector
     * @return this same vector (now a midpoint)
     */
    public Vector midpoint(Vector other) {
        x = (x + other.x) / 2;
        y = (y + other.y) / 2;
        z = (z + other.z) / 2;
        return this;
    }

    /**
     * Gets a new midpoint vector between this vector and another.
     *
     * @param other The other vector
     * @return a new midpoint vector
     */
    public Vector getMidpoint(Vector other) {
        double x = (this.x + other.x) / 2;
        double y = (this.y + other.y) / 2;
        double z = (this.z + other.z) / 2;
        return new Vector(x, y, z);
    }

    /**
     * Performs scalar multiplication, multiplying all components with a
     * scalar.
     *
     * @param m The factor
     * @return the same vector
     */
    public Vector multiply(int m) {
        x *= m;
        y *= m;
        z *= m;
        return this;
    }

    /**
     * Performs scalar multiplication, multiplying all components with a
     * scalar.
     *
     * @param m The factor
     * @return the same vector
     */
    public Vector multiply(double m) {
        x *= m;
        y *= m;
        z *= m;
        return this;
    }

    /**
     * Performs scalar multiplication, multiplying all components with a
     * scalar.
     *
     * @param m The factor
     * @return the same vector
     */
    public Vector multiply(float m) {
        x *= m;
        y *= m;
        z *= m;
        return this;
    }

    /**
     * Calculates the dot product of this vector with another. The dot product
     * is defined as x1*x2+y1*y2+z1*z2. The returned value is a scalar.
     *
     * @param other The other vector
     * @return dot product
     */
    public double dot(Vector other) {
        return x * other.x + y * other.y + z * other.z;
    }

    /**
     * Calculates the cross product of this vector with another. The cross
     * product is defined as:
     * <ul>
     * <li>x = y1 * z2 - y2 * z1
     * <li>y = z1 * x2 - z2 * x1
     * <li>z = x1 * y2 - x2 * y1
     * </ul>
     *
     * @param o The other vector
     * @return the same vector
     */
    public Vector crossProduct(Vector o) {
        double newX = y * o.z - o.y * z;
        double newY = z * o.x - o.z * x;
        double newZ = x * o.y - o.x * y;

        x = newX;
        y = newY;
        z = newZ;
        return this;
    }

    /**
     * Converts this vector to a unit vector (a vector with length of 1).
     *
     * @return the same vector
     */
    public Vector normalize() {
        double length = length();

        x /= length;
        y /= length;
        z /= length;

        return this;
    }

    /**
     * Zero this vector's components.
     *
     * @return the same vector
     */
    public Vector zero() {
        x = 0;
        y = 0;
        z = 0;
        return this;
    }

    /**
     * Returns whether this vector is in an axis-aligned bounding box.
     * <p>
     * The minimum and maximum vectors given must be truly the minimum and
     * maximum X, Y and Z components.
     *
     * @param min Minimum vector
     * @param max Maximum vector
     * @return whether this vector is in the AABB
     */
    public boolean isInAABB(Vector min, Vector max) {
        return x >= min.x && x <= max.x && y >= min.y && y <= max.y && z >= min.z && z <= max.z;
    }

    /**
     * Returns whether this vector is within a sphere.
     *
     * @param origin Sphere origin.
     * @param radius Sphere radius
     * @return whether this vector is in the sphere
     */
    public boolean isInSphere(Vector origin, double radius) {
        return (NumberConversions.square(origin.x - x) + NumberConversions.square(origin.y - y) + NumberConversions.square(origin.z - z)) <= NumberConversions.square(radius);
    }

    /**
     * Gets the X component.
     *
     * @return The X component.
     */
    public double x() {
        return x;
    }

    /**
     * Gets the floored value of the X component, indicating the block that
     * this vector is contained with.
     *
     * @return block X
     */
    public int blockX() {
        return NumberConversions.floor(x);
    }

    /**
     * Gets the Y component.
     *
     * @return The Y component.
     */
    public double y() {
        return y;
    }

    /**
     * Gets the floored value of the Y component, indicating the block that
     * this vector is contained with.
     *
     * @return block y
     */
    public int blockY() {
        return NumberConversions.floor(y);
    }

    /**
     * Gets the Z component.
     *
     * @return The Z component.
     */
    public double z() {
        return z;
    }

    /**
     * Gets the floored value of the Z component, indicating the block that
     * this vector is contained with.
     *
     * @return block z
     */
    public int blockZ() {
        return NumberConversions.floor(z);
    }

    /**
     * Set the X component.
     *
     * @param x The new X component.
     * @return This vector.
     */
    public Vector x(int x) {
        this.x = x;
        return this;
    }

    /**
     * Set the X component.
     *
     * @param x The new X component.
     * @return This vector.
     */
    public Vector x(double x) {
        this.x = x;
        return this;
    }

    /**
     * Set the X component.
     *
     * @param x The new X component.
     * @return This vector.
     */
    public Vector x(float x) {
        this.x = x;
        return this;
    }

    /**
     * Set the Y component.
     *
     * @param y The new Y component.
     * @return This vector.
     */
    public Vector y(int y) {
        this.y = y;
        return this;
    }

    /**
     * Set the Y component.
     *
     * @param y The new Y component.
     * @return This vector.
     */
    public Vector y(double y) {
        this.y = y;
        return this;
    }

    /**
     * Set the Y component.
     *
     * @param y The new Y component.
     * @return This vector.
     */
    public Vector y(float y) {
        this.y = y;
        return this;
    }

    /**
     * Set the Z component.
     *
     * @param z The new Z component.
     * @return This vector.
     */
    public Vector z(int z) {
        this.z = z;
        return this;
    }

    /**
     * Set the Z component.
     *
     * @param z The new Z component.
     * @return This vector.
     */
    public Vector z(double z) {
        this.z = z;
        return this;
    }

    /**
     * Set the Z component.
     *
     * @param z The new Z component.
     * @return This vector.
     */
    public Vector z(float z) {
        this.z = z;
        return this;
    }

    /**
     * Get the block vector of this vector.
     *
     * @return A block vector.
     */
    public BlockVector toBlockVector() {
        return new BlockVector(x, y, z);
    }

    /**
     * Checks to see if two objects are equal.
     * <p>
     * Only two Vectors can ever return true. This method uses a fuzzy match
     * to account for floating point errors. The epsilon can be retrieved
     * with epsilon.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector other))
            return false;
        return Math.abs(x - other.x) < epsilon
               && Math.abs(y - other.y) < epsilon
               && Math.abs(z - other.z) < epsilon
               && (this.getClass().equals(obj.getClass()));
    }

    /**
     * Returns a hash code for this vector
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        int hash = 7;

        hash = 79 * hash + Long.hashCode(Double.doubleToLongBits(this.x));
        hash = 79 * hash + Long.hashCode(Double.doubleToLongBits(this.y));
        hash = 79 * hash + Long.hashCode(Double.doubleToLongBits(this.z));
        return hash;
    }

    /**
     * Get a new vector.
     *
     * @return vector
     */
    @Override
    public Vector clone() {
        try {
            return (Vector) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    /**
     * Returns this vector's components as x,y,z.
     */
    @Override
    public String toString() {
        return x + "," + y + "," + z;
    }

    public static Vector getMinimum(Vector v1, Vector v2) {
        return new Vector(Math.min(v1.x, v2.x), Math.min(v1.y, v2.y), Math.min(v1.z, v2.z));
    }

    public static Vector getMaximum(Vector v1, Vector v2) {
        return new Vector(Math.max(v1.x, v2.x), Math.max(v1.y, v2.y), Math.max(v1.z, v2.z));
    }
}
