import java.lang.*;
import java.lang.reflect.*;
import java.lang.annotation.*;
import java.io.*;
import java.util.*;
import java.nio.*;
import org.graalvm.nativeimage.*;
import org.graalvm.nativeimage.hosted.*;
//import com.oracle.svm.core.annotate.*;

//@AutomaticFeature
class RuntimeReflectionRegistrationFeature implements Feature {
	public void beforeAnalysis(BeforeAnalysisAccess access) {
		try
		{
			for(Class clazz: getClassesInDir("src"))
			{
				registerClassFM(clazz);
			}
		}
		catch(ClassNotFoundException ex)
		{
			System.err.println(ex.getMessage());
		}
	}
	
	public List<Class> getClassesInDir(String dirpath) throws ClassNotFoundException
	{
		File f = new File(dirpath);
		List<Class> classList = new ArrayList<Class>();
		for(File file: f.listFiles())
		{
			if(file.isFile() && file.getName().endsWith(".class"))
			{
				String canonicalClassName = dirpath.replaceAll("/", ".") + "." + (file.getName().replaceAll("\\.class$", ""));
				classList.add(Class.forName(canonicalClassName));
			}
			else if(file.isDirectory())
			{
				classList.addAll(getClassesInDir(dirpath + "/" + file.getName()));
			}
		}
		return classList;
	}

	public void recursiveJarList(List<File> fileList, File startPath)
	{
		if(fileList == null) fileList = new ArrayList<File>();
		File directory = new File(startPath);
		for(File f: directory.listFiles())
		{
			if(f.isDirectory()) recursiveJarList(fileList, f.getAbsolutePath());
			else if(f.isFile() && file.getName().endsWith(".jar")) fileList.append(f);
		}
	}

	// From baeldung
    public static Set<String> getClassNamesFromJarFile(File givenFile) throws IOException {
        Set<String> classNames = new HashSet<>();
        try (JarFile jarFile = new JarFile(givenFile)) {
            Enumeration<JarEntry> e = jarFile.entries();
            while (e.hasMoreElements()) {
                JarEntry jarEntry = e.nextElement();
                if (jarEntry.getName().endsWith(".class")) {
                    classNames.add(jarEntry.getName().replace("/", ".").replace(".class", ""));
                }
            }
            return classNames;
        }
    }

	// From baeldung
    public static Set<Class> getClassesFromJarFile(File jarFile) throws IOException, ClassNotFoundException {
        Set<String> classNames = getClassNamesFromJarFile(jarFile);
        Set<Class> classes = new HashSet<>(classNames.size());
        try (URLClassLoader cl = URLClassLoader.newInstance(
            new URL[] { new URL("jar:file:" + jarFile + "!/") })) {
            for (String name : classNames) {
                Class clazz = cl.loadClass(name); // Load the class by its name
                classes.add(clazz);
            }
        }
        return classes;
    }
    
    public List<Class> getClassesInPackage(String packagePath)
    {
		return null;
    }
	
	// Register Class with Fields and Methods
	public void registerClassFM(Class clazz)
	{
		try {
		RuntimeReflection.register(clazz);
		for(Field field: clazz.getDeclaredFields()) RuntimeReflection.register(field);
		for(Method method: clazz.getDeclaredMethods()) RuntimeReflection.register(method);
		for(Constructor constr: clazz.getDeclaredConstructors()) RuntimeReflection.register(constr);
		//for(Annotation annotation: clazz.getDeclaredAnnotations()) RuntimeReflection.register(annotation);
		} catch (/*NoSuchMethodException | NoSuchFieldException*/ Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
