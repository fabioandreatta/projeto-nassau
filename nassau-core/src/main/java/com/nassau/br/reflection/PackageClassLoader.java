package com.nassau.br.reflection;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Loads all classes in a package
 * 	Thanks to stackoverflow: http://stackoverflow.com/questions/15519626/how-to-get-all-classes-names-in-a-package
 * 
 * @author fsantos
 */
public class PackageClassLoader {
	// Define the log object for this class
	// private static final Logger log = LoggerFactory.getLogger(PackageClassLoader.class);
	  
	private static final String CLASS_FILE_SUFFIX = ".class";
	private static final String JAR_FILE_SUFFIX = ".jar";
	
	public static List<Class<?>> find(String pack) {
		Set<String> all = findAll();
		List<Class<?>> classes = new ArrayList<Class<?>>();
		for (String clazz : all) {
			try {
				if (clazz.startsWith(pack)) 
					classes.add(Class.forName(clazz));
			} catch (ClassNotFoundException e) {}
		}
		return classes;
    }
	
	private static Set<String> findAll() {
		String classpath = System.getProperty("java.class.path");
        String[] paths = classpath.split(System.getProperty("path.separator"));
        
        Set<String> classes = new HashSet<String>();
        for (String path : paths) {
        	File file = new File(path);
            if (file.exists()) {
                classes.addAll(findClasses(file, file));
            }
        }
        return classes;
	}

    private static Set<String> findClasses(File root, File file) {
    	Set<String> classes = new HashSet<String>();
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
            	classes.addAll(findClasses(root, child));
            }
        } else {
            if (file.getName().toLowerCase().endsWith(JAR_FILE_SUFFIX)) {
                JarFile jar = null;
                try {
                    jar = new JarFile(file);
                } catch (Exception ex) {

                }
                if (jar != null) {
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        String name = entry.getName();
                        if (name.endsWith(JAR_FILE_SUFFIX)) {
                        	classes.addAll(findClasses(root, new File("jar:file:./" + name + "!/")));
                        } else {
	                        int extIndex = name.lastIndexOf(CLASS_FILE_SUFFIX);
	                        if (extIndex > 0) {
								classes.add(name.substring(0, extIndex).replace("/", "."));
	                        }
                        }
                    }
                }
            } else if (file.getName().toLowerCase().endsWith(CLASS_FILE_SUFFIX)) {
				classes.add(createClassName(root, file));
            }
        }
        return classes;
    }

    private static String createClassName(File root, File file) {
        StringBuffer sb = new StringBuffer();
        String fileName = file.getName();
        sb.append(fileName.substring(0, fileName.lastIndexOf(CLASS_FILE_SUFFIX)));
        file = file.getParentFile();
        while (file != null && !file.equals(root)) {
            sb.insert(0, '.').insert(0, file.getName());
            file = file.getParentFile();
        }
        return sb.toString();
    }
}
