package ch.epfl.datacockpit.tree.object_enum;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ServiceLoader;

import ch.epfl.datacockpit.database.AbstractResultsManager;
import ch.epfl.datacockpit.tree.clazzes.ClassRepository;
import ch.epfl.general_libraries.results.AdvancedDataRetriever;
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


	// A passer dans le constructeur
	final private static String DEFAULT_RESULT_DISPLAY_SERVICE_ENV_VAR_NAME =
			"ch.epfl.datacockpit.tree.object_enum.ExperimentExecutionManager.ResultDisplayService";

	// A passer dans le constructeur
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

			String defaultResultDisplayServiceClass = System.getenv(
					DEFAULT_RESULT_DISPLAY_SERVICE_ENV_VAR_NAME);

			if (defaultResultDisplayServiceClass != null) {
				System.out.println("Found value for environment variable " + DEFAULT_RESULT_DISPLAY_SERVICE_ENV_VAR_NAME);
				System.out.println("Value is: " + defaultResultDisplayServiceClass);
				try {
					Class<?> clazz = Class.forName(defaultResultDisplayServiceClass);
					ResultDisplayService service = (ResultDisplayService) clazz.getDeclaredConstructor().newInstance();
					service.displayResults(db);
					System.out.println("Visualizer found and display method invoked.");
					return;
				} catch (Exception e) {
					throw new IllegalStateException("Failed to display results: " + e.getMessage());
				}
			}

			// Fall-back case :
			// Load the first class that implements the ResultDisplayService interface
			// (using the ClassRepository for that)
			try {
				ClassRepository defaultClassRepo = ClassRepository.getClassRepository();
				if (defaultClassRepo == null) {
					defaultClassRepo = ClassRepository.getClassRepository(new String[] { "ch" });
				}
				ResultDisplayService service = defaultClassRepo.
						getClasses((Class<ResultDisplayService>)ResultDisplayService.class).iterator().next().
						getDeclaredConstructor().newInstance();
				service.displayResults(db);
				System.out.println("Visualizer found and display method invoked.");
				return;
			} catch (Exception e) {
				throw new IllegalStateException("Failed to display results: " + e.getMessage());
			}
		}
	}

	@Override
	public Object getObjectToWaitFor() {
		return db;
	}
}
