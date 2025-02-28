package ch.epfl.general_libraries.results;

import java.util.List;
import java.util.Set;
import java.util.Vector;


public interface AbstractDataRetriever {

	public static final String CONSTANT = "CONSTANT";

	public abstract List<String> getMetrics();
	public abstract Set<String> getParameters();
	public abstract boolean isInput(String param);
	public abstract Set<String> getPossibleValuesOfGivenProperty(String property);
	public abstract Vector[] getVariableAndConstantPropertiesForGivenMetric(String metric);
}
