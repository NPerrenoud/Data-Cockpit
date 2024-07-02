package ch.epfl.general_libraries.results;

import javax.swing.JFrame;

public abstract class AbstractResultsDisplayer implements ResultDisplayService {
	
	protected AdvancedDataRetriever retriever;

	public AbstractResultsDisplayer(AdvancedDataRetriever retriever) {
		this.retriever = retriever;
	}

	public abstract JFrame showFrame();


}
