package me.rochblondiaux.ultralimbo.world;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.jetbrains.annotations.Unmodifiable;

import net.kyori.adventure.key.Key;

public record Environment(Key key, boolean skyLight) {

    public static final Set<Environment> REGISTERED_ENVIRONMENTS = new CopyOnWriteArraySet<>();

    public static final Environment NORMAL = new Environment(Key.key("minecraft:overworld"), true);
    public static final Environment NETHER = new Environment(Key.key("minecraft:the_nether"), false);
    public static final Environment END = new Environment(Key.key("minecraft:the_end"), false);

    public Environment(Key key, boolean skyLight) {
        this.key = key;
        this.skyLight = skyLight;

        REGISTERED_ENVIRONMENTS.add(this);
    }


    public static Optional<Environment> findByKey(Key key) {
        return REGISTERED_ENVIRONMENTS.stream()
                .filter(env -> env.key().equals(key))
                .findFirst();
    }

    @Unmodifiable
    public static Set<Environment> registeredEnvironments() {
        return Set.copyOf(REGISTERED_ENVIRONMENTS);
    }
}