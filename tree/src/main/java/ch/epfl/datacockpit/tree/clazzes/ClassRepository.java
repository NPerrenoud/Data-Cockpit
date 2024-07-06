package ch.epfl.datacockpit.tree.clazzes;

import java.util.*;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.ClassFormatException;
import org.apache.bcel.classfile.JavaClass;

import ch.epfl.general_libraries.logging.Logger;

public class ClassRepository {
	
	private static final Logger logger = new Logger(ClassRepository.class);	
	
	private HashSet<JavaClass> cache;
	/**
	 * This is a cache of ClassRepository objects. The key is the list of prefixes
	 */
	private static HashMap<String[], ClassRepository> cacheClassRepositories = new HashMap<String[], ClassRepository>();

	/**
	 * Returns the first ClassRepository object in the cache, or null if the cache is empty
	 * Does not pay any attention to prefixes
	 * @return the first ClassRepository object in the cache, or null if the cache is empty
	 */
	public static ClassRepository getClassRepository() {
		for (ClassRepository cr : cacheClassRepositories.values()) {
			return cr;
		}
		return null;
	}

	/**
	 * Returns a classRepository that has the given prefixes. If such an object is in the cache already,
	 * it is returned. Otherwise, a new object is created and put in the cache.
	 * The prefixes are sorted alphabetically before being used as a key in the cache.
	 * @param prefixes the prefixes to look for
	 * @return the ClassRepository object in the cache that has the given prefixes, or null if no such object is in the cache
	 */
	public static ClassRepository getClassRepository(String[] prefixes) {
		Arrays.sort(prefixes);
		if (cacheClassRepositories.containsKey(prefixes)) {
			return cacheClassRepositories.get(prefixes);
		}
		ClassRepository cr = new ClassRepository(prefixes);
		cacheClassRepositories.put(prefixes, cr);
		return cr;
	}
	
	private ClassRepository(String[] prefixes) {
		cache = new HashSet<JavaClass>();
		ClasspathClassesEnumerator.Processor p = new ClasspathClassesEnumerator.Processor() {
			@Override
			public void process(String className) throws Exception {
				try {
					JavaClass cl = Repository.lookupClass(className);
					cache.add(cl);
				} catch (Exception e) {
					System.out.println("Class name is : " + className);
					throw e;
				}
			}	
		};	
		ClasspathClassesEnumerator.enumerateClasses(p, prefixes);		
	}

	public <T> Collection<Class<T>> getClasses(Class<T> mod) throws Exception {
		logger.debug("Getting classes of model " + mod);
		JavaClass model = Repository.lookupClass(mod);
		ArrayList<Class<T>> list = new ArrayList<Class<T>>();
		if (mod.isInterface()) {
			for (JavaClass c : cache) {
				//System.out.print(".");
				//logger.debug("Testing class " + c.getClassName());
				boolean flag = false;
				try {
					flag = Repository.implementationOf(c, model);
				}
				catch(ClassNotFoundException e) {
				/*	System.out.print("e");
					e.printStackTrace();*/
					continue;
				}
				catch(ClassFormatException e) {
					System.out.println("ClassFormatException for class :" + c.getClassName());
					continue;
				}
				if (flag) {
					if (c.isAbstract() == false) {
						logger.debug("Found class " + c.getClassName());						
						list.add((Class<T>)Class.forName(c.getClassName()));
					}
				} else {
					for (JavaClass superClass : c.getSuperClasses()) {
						flag = false;
						try {
							flag = Repository.implementationOf(superClass, model);
						}
						catch (ClassNotFoundException e) {
							continue;
						}
						if (flag) {
							if (c.isAbstract() == false) {
								list.add((Class<T>)Class.forName(c.getClassName()));
								break;
							}
						}
					}
				}
			}
		} else {
			for (Iterator<JavaClass> ite = cache.iterator() ; ite.hasNext() ; ) {
				JavaClass c = ite.next();
				try {
					if (Repository.instanceOf(c, model)) {
						if (c.isAbstract() == false) {
							list.add((Class<T>)Class.forName(c.getClassName()));
						}
					}
				}
				catch (Throwable e) {
					System.out.println("ERROR with class " + c.getClassName());
				//	System.out.println(e);
					ite.remove();
				}
			}
		}
		return list;
	}	
}
