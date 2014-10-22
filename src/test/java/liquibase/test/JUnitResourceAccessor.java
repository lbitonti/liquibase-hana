package liquibase.test;

import liquibase.resource.ResourceAccessor;
import liquibase.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JUnitResourceAccessor implements ResourceAccessor {
    private URLClassLoader classLoader;

//    public JUnitResourceAccessor() throws Exception {
//        File srcDir = new File(TestContextHanaDB.getInstance().findCoreProjectRoot(), "src");
//        File integrationClassesDir = new File(TestContextHanaDB.getInstance().findIntegrationTestProjectRoot(), "target/classes");
//        File integrationTestClassesDir = new File(TestContextHanaDB.getInstance().findIntegrationTestProjectRoot(), "target/test-classes");
//         classLoader = new URLClassLoader(new URL[]{
//                //integrationClassesDir.toURL(),
//                 //integrationTestClassesDir.toURL(),
//                //new File(srcDir, "test/java").toURL(),
//                 new File(TestContextHanaDB.getInstance().findIntegrationTestProjectRoot(), "src/test/resources/packaged-changelog.jar").toURL(),
//                new File(System.getProperty("java.io.tmpdir")).toURL(),
//        });
//
//    }

    public java.util.Set<java.io.InputStream> getResourcesAsStream(String file) throws IOException {
        Set<InputStream> inputStreamSet = new HashSet<InputStream>();
		inputStreamSet.add(classLoader.getResourceAsStream(file));
		return inputStreamSet;
    }

//    public Enumeration<URL> getResources(String packageName) throws IOException {
//        return classLoader.getResources(packageName);
//    }

	public Set<String> list(String relativeTo, String path, boolean includeFiles, boolean includeDirectories, boolean recursive) throws IOException {
//		Set<String> listSet = new HashSet<String>();
		return new HashSet<String>();
	}
    public ClassLoader toClassLoader() {
        return classLoader;
    }

    @Override
    public String toString() {
        List<String> urls = new ArrayList<String>();
        for (URL url : classLoader.getURLs()) {
            urls.add(url.toExternalForm());
        }
        
        return getClass().getName() + "(" + StringUtils.join(urls, ",") + ")";
    }
}
