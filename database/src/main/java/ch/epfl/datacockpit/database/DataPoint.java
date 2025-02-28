package ch.epfl.datacockpit.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DataPoint implements Serializable {

	private static final long serialVersionUID = 1L;

	private final ArrayList<Property> properties = new ArrayList<Property>();

	private DataPoint parent;

	public DataPoint() {
		parent = null;
	}
	
	public DataPoint(DataPoint parent) {
		this.parent = parent;
	}
	
	public DataPoint(Map<String, String> params) {
		for (Map.Entry<String, String> p : params.entrySet()) {
			this.addProperty(new Property(p));
		}
	}
	
	public Object clone() {
		DataPoint dp = new DataPoint();
		return clone(dp);
	}
	
	public Object clone(DataPoint dp) {
		dp.properties.addAll(this.properties);
		dp.parent = this.parent;
		return dp;		
	}
	
	public DataPoint getDerivedDataPoint() {
		DataPoint derive = new DataPoint(this);
		return derive;
	}

	public void addProperty(String name, String value) {
		properties.add(new Property(name, value));
	}	
	public void addProperty(String name, int value) {
		properties.add(new Property(name, value));		
	}
	public void addProperty(String name, double value) {
		properties.add(new Property(name, value));		
	}


	public void addResultProperty(String name, String value) {
		properties.add(new ResultProperty(name, value));
	}

	public void addResultProperty(String name, double value) {
		properties.add(new ResultProperty(name, value));		
	}


	public void addProperty(Property p) {
		properties.add(p);
	}


	@SuppressWarnings("unchecked")
	public List<Property> getProperties() {
		List<Property> list;
		if (parent == null) {
			list = (List<Property>)properties.clone();
		} else {
			list = parent.getProperties();
			list.addAll(properties);
		}
		return list;
	}
	
	public Property getProperty(String name) {
		for (Property p : properties) {
			if (p.getName().equals(name)) return p;
		}
		if (parent != null) {
			return parent.getProperty(name);
		}
		return null;
	}

	public List<String> getInputPropertiesNames() {
		List<String> list;
		if (parent != null) {
			list = parent.getInputPropertiesNames();
		} else {
			list = new ArrayList<String>();
		}
		for (Property p : properties) {
			if (!(p instanceof ResultProperty)) {
				list.add(p.getName());
			}
		}				
		return list;
	}

	public List<String> getOutputPropertiesNames() {
		List<String> list;
		if (parent != null) {
			list = parent.getOutputPropertiesNames();
		} else {
			list = new ArrayList<String>();
		}
		for (Property p : properties) {
			if ((p instanceof ResultProperty)) {
				list.add(p.getName());
			}
		}				
		return list;		
	}
	

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (Property p : getProperties()) {
			sb.append(p.toString()+"\n");
		}
		return sb.toString();
	}


}
