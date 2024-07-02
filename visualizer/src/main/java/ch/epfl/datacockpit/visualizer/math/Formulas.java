package ch.epfl.datacockpit.visualizer.math;

import umontreal.ssj.probdist.StudentDist;

import java.util.Arrays;
import java.util.Collection;

public class Formulas {

    public static double[] getAll(Collection<Double> d, double confidence) {
        double mean = getMeanD(d);
        double var = getVarianceInternalD(d, mean);
        double[] confInter;
        if (d.size() > 1) {
            confInter = getConfidenceIntervalInternal(d, mean, var, confidence);
        } else {
            double dou = d.iterator().next();
            confInter = new double[]{dou, dou};
        }
        double[] quartiles = getQuartilesInternalD(d);
        double[] dcop = new double[]{mean, getMedianD(d), confInter[0], confInter[1],
                quartiles[0], quartiles[3], quartiles[1],
                quartiles[2]};
        return dcop;
    }

    public static float[] getAll(Collection<Float> f, float confidence) {
        float mean = getMeanF(f);
        float var = getVarianceInternalF(f, mean);
        float[] confInter;
        if (f.size() > 1) {
            confInter = getConfidenceIntervalInternal(f, mean, var, confidence);
        } else {
            float dou = f.iterator().next();
            confInter = new float[]{dou, dou};
        }
        float[] quartiles = getQuartilesInternalF(f);
        float[] dcop = new float[]{mean, getMedianF(f), confInter[0], confInter[1],
                quartiles[0], quartiles[3], quartiles[1],
                quartiles[2]};
        return dcop;
    }

    public static float[] getAll95(Collection<Float> f, float confidence) {
        float mean = getMeanF(f);
        float var = getVarianceInternalF(f, mean);
        float[] confInter;
        if (f.size() > 1) {
            confInter = getConfidenceIntervalInternal(f, mean, var, confidence);
        } else {
            float dou = f.iterator().next();
            confInter = new float[]{dou, dou};
        }
        float[] quartiles = get95InternalF(f);
        float[] dcop = new float[]{mean, getMedianF(f), confInter[0], confInter[1],
                quartiles[0], quartiles[3], quartiles[1],
                quartiles[2]};
        //	 System.out.println(java.util.Arrays.toString(dcop));
        return dcop;
    }

    private static double getMeanD(Collection<Double> d) {
        double total = 0;
        for (double de : d) {
            total += de;
        }
        return (total / (double)d.size());
    }

    public static float getMeanF(Collection<Float> d) {
        float total = 0;
        for (float de : d) {
            total += de;
        }
        return (total / (float)d.size());
    }

    private static double getVarianceInternalD(Collection<Double> d, double mean) {
        double total = 0;
        for (double de : d) {
            total += Math.pow(de - mean, 2);
        }
        return total / (d.size()-1);
    }

    private static float getVarianceInternalF(Collection<Float> d, double mean) {
        float total = 0;
        for (float de : d) {
            total += Math.pow(de - mean, 2);
        }
        return total / (d.size()-1);
    }

    private static double[] getConfidenceIntervalInternal(Collection<Double> d,
                                                          double mean, double var, double confidenceLevel) {
        double studT = -StudentDist.inverseF(d.size()-1, (1-confidenceLevel)/2);
        double term1 = Math.pow((var/d.size()), 0.5);
        return new double[]{(mean - (studT*term1)), (mean + (studT*term1))};
    }

    private static float[] getConfidenceIntervalInternal(Collection<Float> d,
                                                         float mean, float var, float confidenceLevel) {
        float studT = (float)-StudentDist.inverseF(d.size()-1, (1-confidenceLevel)/2);
        float term1 = (float)Math.pow((var/d.size()), 0.5);
        return new float[]{(mean - (studT*term1)), (mean + (studT*term1))};
    }

    private static double[] getQuartilesInternalD(Collection<Double> d) {
        Double[] ddd = d.toArray(new Double[d.size()]);
        Arrays.sort(ddd);
        if (ddd.length > 0) {
            return new double[]{ddd[0], ddd[ddd.length/4], ddd[ddd.length*3/4], ddd[ddd.length-1]};
        }
        return new double[]{-1,-1,-1,-1};
    }

    private static float[] getQuartilesInternalF(Collection<Float> d) {
        Float[] ddd = d.toArray(new Float[d.size()]);
        Arrays.sort(ddd);
        if (ddd.length > 0) {
            return new float[]{ddd[0], ddd[ddd.length/4], ddd[ddd.length*3/4], ddd[ddd.length-1]};
        }
        return new float[]{-1,-1,-1,-1};
    }

    public static double getMedianD(Collection<Double> d) {
        Double[] ddd = d.toArray(new Double[d.size()]);
        Arrays.sort(ddd);
        if (ddd.length > 0) {
            return ddd[ddd.length/2];
        }
        return -1f;
    }

    public static float getMedianF(Collection<Float> d) {
        Float[] ddd = d.toArray(new Float[d.size()]);
        Arrays.sort(ddd);
        if (ddd.length > 0) {
            return ddd[ddd.length/2];
        }
        return -1f;
    }

    private static float[] get95InternalF(Collection<Float> d) {
        Float[] ddd = d.toArray(new Float[d.size()]);
        Arrays.sort(ddd);
        if (ddd.length > 0) {
            return new float[]{ddd[0], ddd[ddd.length/20], ddd[ddd.length*19/20], ddd[ddd.length-1]};
        }
        return new float[]{-1,-1,-1,-1};
    }
}
