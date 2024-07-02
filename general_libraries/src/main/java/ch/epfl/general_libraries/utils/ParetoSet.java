package ch.epfl.general_libraries.utils;

import java.util.ArrayList;
import java.util.Iterator;


public class ParetoSet<X extends ParetoPoint> implements Iterable<X> {
	
	private ArrayList<X> points;
	
	private int dimensions;
	
	public ParetoSet(int dimensions) {
		this.dimensions = dimensions;
		points = new ArrayList<X>();
	}

	public boolean addCandidate(X pp) {
		if (pp.getDimensions() > dimensions) throw new IllegalStateException();
		if (points.size() == 0) {
			points.add(pp);
			return true;
		}
		// a point is added :
			// 1. as soon as it dominates another
				// in which case dominated point must be removed
			// 2. If it is dominated by no existing point
		ArrayList<X> toRem = new ArrayList<X>();
		boolean keep = false;
		boolean passedAll = true;
		for (int i = 0 ; i < points.size() ; i++) {
			X alt = points.get(i);
			if (pp.dominates(alt)) {
				toRem.add(alt);
				keep = true;
			}
			if (keep == false) {
				if (alt.dominates(pp)) {
					passedAll = false;
					break;
				}
			}
		}
		if (keep || passedAll) {
			points.removeAll(toRem);
			points.add(pp);
			return true;
		}
		return false;
	}

	@Override
	public Iterator<X> iterator() {
		return points.iterator();
	}

}
