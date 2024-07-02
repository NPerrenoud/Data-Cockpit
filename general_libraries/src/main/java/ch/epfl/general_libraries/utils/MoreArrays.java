package ch.epfl.general_libraries.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MoreArrays {

	public static double max(double[] t) {
		double max = -Double.MAX_VALUE;
		for (int i = 0 ; i < t.length ; i++) {
			if (t[i] > max) max = t[i];
		}
		return max;
	}

	public static double min(double[] t) {
		if (t.length == 0) {
			return Double.MIN_VALUE;
		}		
		double min = Double.MAX_VALUE;
		for (int i = 0 ; i < t.length ; i++) {
			if (t[i] < min) min = t[i];
		}
		return min;
	}


	public static void main(String[] args) {
		System.out.println(Arrays.toString(exclude(new int[]{1, 2, 3 ,4 ,5,6,7,8}, 4)));
	}

	public static int[] exclude(int[] dimensions, int exception) {
		int[] newT = new int[dimensions.length-1];
		int index = 0;
		for (int i = 0 ; i < dimensions.length ; i++) {
			if (i == exception) continue;
			newT[index] = dimensions[i];
			index++;
		}
		return newT;
	}


	@SuppressWarnings("all")
	public static <T> ArrayList<T> getArrayList(T... t ) {
		int l = t.length;
		ArrayList<T> al = new ArrayList<T>(l);
		for (int i = 0 ; i < l ; i++) {
			al.add(t[i]);
		}
		return al;
	}

	public static int[] toIntArray(List<Integer> ff) {
		int[] a = new int[ff.size()];
		int index = 0;
		for (Integer g : ff) {
			a[index] = g;
			index++;
		}
		return a;
	}
}
