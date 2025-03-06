package me.rochblondiaux.ultralimbo.utils;

public class BlockVector extends Vector {

    public BlockVector() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    /**
     * Construct the vector with another vector.
     *
     * @param vec The other vector.
     */
    public BlockVector(Vector vec) {
        this.x = vec.x();
        this.y = vec.y();
        this.z = vec.z();
    }

    /**
     * Construct the vector with provided integer components.
     *
     * @param x X component
     * @param y Y component
     * @param z Z component
     */
    public BlockVector(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Construct the vector with provided double components.
     *
     * @param x X component
     * @param y Y component
     * @param z Z component
     */
    public BlockVector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Construct the vector with provided float components.
     *
     * @param x X component
     * @param y Y component
     * @param z Z component
     */
    public BlockVector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Checks if another object is equivalent.
     *
     * @param obj The other object
     * @return whether the other object is equivalent
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BlockVector other))
            return false;
        return (int) other.x() == (int) this.x && (int) other.y() == (int) this.y && (int) other.z() == (int) this.z;

    }

    /**
     * Returns a hash code for this vector.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return (Integer.valueOf((int) x).hashCode() >> 13) ^ (Integer.valueOf((int) y).hashCode() >> 7) ^ Integer.valueOf((int) z).hashCode();
    }

    /**
     * Get a new block vector.
     *
     * @return vector
     */
    @Override
    public BlockVector clone() {
        return (BlockVector) super.clone();
    }
}
