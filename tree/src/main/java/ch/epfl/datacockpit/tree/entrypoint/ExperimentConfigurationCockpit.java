package ch.epfl.datacockpit.tree.entrypoint;

import java.io.File;

import ch.epfl.datacockpit.tree.experiment_aut.Experiment;
import ch.epfl.general_libraries.logging.Logger;
import ch.epfl.datacockpit.tree.object_enum.AbstractEnumerator;
import ch.epfl.datacockpit.tree.object_enum.ExperimentExecutionManager;
import ch.epfl.datacockpit.tree.gui.SwingObjectConfigurationAndEnumerator;

// TODO : a mettre dans le module experiment directement, et construire le ExperimentExecutionManager
// en lui fournissant les deux implems : le SmartDataPointCollector (Class"InOut" à recréer) et le Visualiser (ResultDisplayService)

// Créer une autre classe juste à coté avec juste le main suivant
/*
public static void main(String[] args) {
		DefaultResultDisplayingGUI gui = new DefaultResultDisplayingGUI();
		SmartDataPointCollector sdpc = new SmartDataPointCollector();
		if (args.length > 0) {
		sdpc.loadFromFile(new File(args[0]));
		}
		gui.displayResults(sdpc);
		}*/

// ExperimentConfigurationCockpit should no longer extends SwingObjectConfigurationAndEnumerator<Experiment>
// It should instead serve as an entrypoint configuration class that directly recieves a "Tree" object to use (method show())
// which is currently the SwingObjectConfigurationAndEnumerator<Experiment> object
public class ExperimentConfigurationCockpit extends SwingObjectConfigurationAndEnumerator<Experiment>  {
	
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		
		try {
			String claz = null;
			String pre = null;
			String log4jFile = null;	
			String defaultFile = null;
			if (args.length > 0) {
				if (args[0].equals("-help") || args[0].equals("help") || args[0].equals("-h") || args[0].equals("usage")) {
					printUsage();
				}
				for (int i = 0 ; i < args.length ; i++) {
					if (args[i].equals("-c")) {
						claz = args[i+1];
					}
					if (args[i].equals("-p")) {
						pre = args[i+1];
					}
					if (args[i].equals("-l")) {
						log4jFile = args[i+1];
					}
					if (args[i].equals("-default")) {
						defaultFile = args[i+1];
					}
				}
			}
			if (log4jFile != null) {
				Logger.initLogger(new File(log4jFile));
			}
			ExperimentConfigurationCockpit co;
			if (claz != null) {
				Class c = Class.forName(claz);
				Class<? extends Experiment> cc = (Class<? extends Experiment>)c;
				if (pre != null) {
					co = new ExperimentConfigurationCockpit(cc, pre.split(";"));
				} else {
					co = new ExperimentConfigurationCockpit(cc, "".split(";"));
				}
			} else {
				if (pre != null) {
					co = new ExperimentConfigurationCockpit(null, pre.split(";"));
				} else {
					co = new ExperimentConfigurationCockpit();
				}
			}
			if (defaultFile != null) {
				co.show(defaultFile);
			} else {
				co.show();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void printUsage() {
		System.out.println("Usage : [-c <class_to_configure>][-p <prefixes, ;-separated>][-l <log4j config file>]");
		System.exit(0);
	}


	public ExperimentConfigurationCockpit() {
		this((Class<? extends Experiment>)null);
	}
	
	public ExperimentConfigurationCockpit(Class<? extends Experiment> c) {
		this(c, new String[]{"ch", "test", "org.optsquare", "umontreal", "edu.columbia", "archives"});
	}
	
	public ExperimentConfigurationCockpit(String[] prefixes) {	
		this(null, prefixes);
	}	

	public ExperimentConfigurationCockpit(Class<? extends Experiment> c, String[] prefixes) {
		this(c, new ExperimentExecutionManager(), prefixes);
	}
	
	public ExperimentConfigurationCockpit(Class<? extends Experiment> c, AbstractEnumerator<Experiment> t, String[] pre) {
		super(check(c), t, pre);
	}
	
	private static Class<? extends Experiment> check(Class<? extends Experiment> c) {
		if (c == null) {
			return Experiment.class;
		} else {
			return c;
		}
	}

}
