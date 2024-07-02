package ch.epfl.datacockpit.tree.object_enum;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ServiceLoader;

import ch.epfl.general_libraries.results.ResultDisplayService;
import ch.epfl.datacockpit.tree.experiment_aut.Experiment;
import ch.epfl.datacockpit.tree.experiment_aut.WrongExperimentException;
import ch.epfl.datacockpit.database.SmartDataPointCollector;

/**
 * Handles enumerated experiment objects (thus implements ObjectEnumerationManager)
 * This mainly consists in calling the run() method of each object, and placing
 * the results in the db object. 
 * @author Rumley
 *
 */
public class ExperimentExecutionManager<T extends Experiment> extends AbstractEnumerator<T> {
	protected SmartDataPointCollector db = new SmartDataPointCollector();
	protected int i;
	protected long start;
	protected boolean success = true;
	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm.ss"); 
	private static ArrayList<Class> registeredCachedClasses = new ArrayList<Class>();

	@Override
	public void clearEnumerationResults() {
		db.clear();
	}
	
	@Override
	public void clearCaches() {
		for (Class<?> c : registeredCachedClasses) {
			try {
				c.getMethod("clearCache", new Class[]{}).invoke(null, new Object[]{});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void beforeIteration() {
		this.start = System.currentTimeMillis();
		this.i = 1;
	}

	@Override
	public void iterating(Experiment object) throws Exception {
		success = true;
		long yourmilliseconds = System.currentTimeMillis();   
		Date resultdate = new Date(yourmilliseconds);
		System.out.print(sdf.format(resultdate));
		synchronized(this) {
		
			System.out.println(": Experiment " + this.i);
			++this.i;
		}
		try {
			object.run(db, null);
		}
		catch (WrongExperimentException e) {
			System.out.println("Warning: wrong experiment: " + e.getMessage());
		}
		catch (Throwable e) {
			success = false;
			if (e instanceof Exception) {
				throw (Exception)e;
			} else {
				throw new IllegalStateException(e);
			}
		}
	}


	@Override
	public void afterIteration() {
		if (success) {
			System.out.println(this.i - 1 + " experiments run in: "
					+ (System.currentTimeMillis() - this.start) + " ms");

			ServiceLoader<ResultDisplayService> serviceLoader = ServiceLoader.load(ResultDisplayService.class);
			for (ResultDisplayService service : serviceLoader) {
				try {
					service.displayResults(db);
					System.out.println("Visualizer found and display method invoked.");
				} catch (Exception e) {
					System.out.println("Failed to display results: " + e.getMessage());
				}
				return;
			}
			System.out.println("No visualizer found.");
		}
	}

	/*
	@Override
	public void afterIteration() {
		if (success) {
			System.out.println(this.i-1 + " experiments runned in : "
					+ (System.currentTimeMillis() - this.start) + " ms");
			try {
				// Load visualizer default display class at runtime
				Class<?> clazz =
						Class.forName("ch.epfl.datacockpit.visualizer.global_gui.DefaultResultDisplayingGUI");

				// Retrieve the static method by name and parameter types
				Method method = clazz.getMethod("displayDefault", AdvancedDataRetriever.class);

				// Invoke the static method with arguments (null for the instance parameter)
				method.invoke(null, db);
			} catch (Exception e) {
				System.out.println("No visualizer found : " + e.getMessage());
			}
		}
	}

	 */

	@Override
	public Object getObjectToWaitFor() {
		return db;
	}
}
