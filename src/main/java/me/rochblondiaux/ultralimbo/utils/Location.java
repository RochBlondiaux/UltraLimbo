package me.rochblondiaux.ultralimbo.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Location extends Vector implements Cloneable {

    private float yaw = 0;
    private float pitch = 0;

    public Location(double x, double y, double z) {
        super(x, y, z);
    }

    @Override
    public Location clone() {
        Location clone = (Location) super.clone();
        clone.x = this.x();
        clone.y = this.y();
        clone.z = this.z();
        clone.yaw = this.yaw;
        clone.pitch = this.pitch;
        return clone;
    }
}
