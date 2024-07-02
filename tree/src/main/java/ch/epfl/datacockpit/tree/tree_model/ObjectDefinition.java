package ch.epfl.datacockpit.tree.tree_model;

import java.util.ArrayList;

import ch.epfl.general_libraries.utils.TypeParser;

public class ObjectDefinition extends AbstractDefinition {
	
	private static final long serialVersionUID = 1L;
	ArrayList<AbstractDefinition> list;
	String constructorDef;
	
	public ObjectDefinition(String className) {	
		this(className, null);
	}

	public ObjectDefinition(String className, String constructorDef) {
		this.def = className;
		this.constructorDef = constructorDef;
	}
	
	private void initList() {
		if (list == null) {
			list = new ArrayList<AbstractDefinition>();
		}
	}
	
	public void addDefinition(AbstractDefinition d) {
		//if (d == null) throw new NullPointerException();
		initList();
		list.add(d);		
	}


	protected Class getDefinedType(ClassLoader loader) throws ClassNotFoundException {
		if (constructorDef != null) {
			try {
				return Class.forName(def, false, loader);
			}
			catch (ClassNotFoundException e) {
				int index = def.lastIndexOf(".");
				String newName = def.substring(0, index);
				newName = newName + "$" + def.substring(index+1, def.length());
				return Class.forName(newName, false, loader);
			}
		} else {
			return TypeParser.getRawType(def);
		}
	}	
	
	protected Class getDefinedClass(ClassLoader loader) throws ClassNotFoundException {
		if (constructorDef != null) {
			return Class.forName(constructorDef, false, loader);
		} else return getDefinedType(loader);
	}
	
	private Class[] getParameterTypes(ClassLoader loader) throws ClassNotFoundException {
		if (list != null) {
			Class[] types = new Class[list.size()];
			for (int i = 0 ; i < list.size(); i++) {
				ObjectDefinition defin = (ObjectDefinition)list.get(i);
				types[i] = defin.getDefinedType(loader);
			}
			return types;
		} else {
			return new Class[]{};
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		String prefix = "";
		localToString(prefix, sb);
		return sb.toString();
	}
	
	public void localToString(String prefix, StringBuilder sb) {
		if (constructorDef != null) {
			sb.append(prefix + "-" + def + " -- " + constructorDef + "\r\n");
		} else {
			sb.append(prefix + "-" + def + "\r\n");
		}
		if (list != null) {
			for (AbstractDefinition d : list) {
				d.localToString(prefix + "  ", sb);
			}
		}
	}
}

class StringDefinition extends AbstractDefinition {

	private static final long serialVersionUID = 1L;

	StringDefinition(String s) {
		def = s;
	}
	
	void localToString(String prefix, StringBuilder sb) {
		sb.append(prefix + "-" + def + "\r\n");
	}
}