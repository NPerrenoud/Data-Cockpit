package ch.epfl.datacockpit.tree.experiment_aut;

import ch.epfl.datacockpit.tree.clazzes.ClassRepository;
import ch.epfl.general_libraries.results.AbstractResultsDisplayer;
import ch.epfl.datacockpit.database.AbstractResultsManager;

public interface Experiment {
	
	public void run(AbstractResultsManager man, AbstractResultsDisplayer dis) throws WrongExperimentException;
	
	public static class globals {
		public static ClassRepository classRepo = null;
	}
	
	
	
}
