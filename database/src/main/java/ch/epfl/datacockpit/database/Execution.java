package ch.epfl.datacockpit.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.general_libraries.utils.DateAndTimeFormatter;

public class Execution implements Serializable {

	private static final long serialVersionUID = 1L;
	private final String application;
	private final String version;
	private final List<DataPoint> dataPoints = new ArrayList<DataPoint>();
	private final String date;

	public Execution(String application, String version, String date) {
		this.application = application;
		this.version = version;
		this.date = date;
	}

	public Execution(String application, String version) {
		this.application = application;
		this.version = version;
		this.date = DateAndTimeFormatter.getDate(System.currentTimeMillis());
	}
	
	public Execution() {
		this.application = "Unnamed application";
		this.version = "version 0.0";
		this.date = DateAndTimeFormatter.getDate(System.currentTimeMillis());
	}


	public void addDataPoint(DataPoint p) {
		if (p == null) {
			throw new NullPointerException();
		}
		dataPoints.add(p);
	}

	public List<DataPoint> getDataPoints() {
		return dataPoints;
	}

}