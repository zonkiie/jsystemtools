import java.lang.*;
import java.lang.reflect.*;
import java.lang.annotation.*;
import java.io.*;
import java.util.*;
import java.util.jar.*;
import java.util.zip.*;
import java.net.*;
import java.nio.*;
import utils.*;
import org.graalvm.nativeimage.*;
import org.graalvm.nativeimage.hosted.*;
//import com.oracle.svm.core.annotate.*;

//@AutomaticFeature
class RuntimeReflectionRegistrationFeature implements Feature {
	public void beforeAnalysis(BeforeAnalysisAccess access) {
		try
		{
			for(Class clazz: ClassScannerUtils.getAllClassesInPath(new File("build/jar/MainProgram.jar"), true))
			{
				if(clazz == null || clazz.getCanonicalName() == null) continue;
				registerClassFM(clazz);
			}
		}
		catch(IOException ex)
		{
			System.err.println(ex.getMessage());
		}
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
