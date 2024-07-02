package ch.epfl.datacockpit.database;

public interface AbstractResultsManager {

	public void addExecution(Execution e);

    public void clear();

	public void addDataPoint(DataPoint dp);

}