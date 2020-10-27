package io.github.cottonmc.cottonrpg.data.rpgskill;

import net.minecraft.util.math.BlockPos;

import java.util.Collection;
import java.util.Random;

public interface Target<T> {
    /**
     * @return All the subjects that can be affected.
     */
    Collection<T> allSubjects();

    /**
     * @return A random subject that can be affected
     */
    @SuppressWarnings("unchecked")
    default T randomSubject() {
        return (T) allSubjects().toArray()[new Random().nextInt(allSubjects().size())];
    }

    /**
     * @param pos The position to check closeness to.
     * @return The closest subject to this position.
     */
    T closestSubject(BlockPos pos);
}
