package ch.epfl.datacockpit.database.math;

import java.util.Collection;

public class Formulas {

    private static float getMeanF(Collection<Float> d) {
        float total = 0;
        for (float de : d) {
            total += de;
        }
        return (total / (float)d.size());
    }
}
