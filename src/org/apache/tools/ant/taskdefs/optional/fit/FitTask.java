package org.apache.tools.ant.taskdefs.optional.fit;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;

public class FitTask extends Task {
	
	private ArrayList tests;
	private boolean haltOnFailure;
	private boolean haltOnError;
	private boolean haltOnMismatch;
	private String dir;
	
	private Path classpath;
	
	public void init() {
		tests = new ArrayList();
		dir = "";
	}
	
	public void execute() {
		AntClassLoader classLoader = getProject().createClassLoader(classpath);
		classLoader.setThreadContextLoader();
		try {
			for (Iterator it = tests.iterator(); it.hasNext(); ) {
				Document doc = (Document) it.next();
				execute(doc);
			}
		} finally {
			classLoader.resetThreadContextLoader();
			classLoader.cleanup();
		}
	}

	void execute(Document doc) {
		try {
			doc.setDir(dir);
			doc.run();
			if (haltOnFailure && doc.hasFailures())
				throw new BuildException(doc.getFailureMessage());
			if (haltOnError && doc.hasErrors())
				throw new BuildException(doc.getErrorMessage());
			if (doc.hasErrors() && !doc.isChecked())
				log(doc.getName() + " run: " + doc.counts(), Project.MSG_ERR);
			else
				log(doc.getName() + " run: " + doc.counts());
		} catch (WrongResultException e) {
			if (haltOnMismatch)
				throw new BuildException(e.getMessage());
			log(e.getMessage(), Project.MSG_ERR);
		}
	}
	
	public Document createDocument() {
		Document doc = new Document();
		tests.add(doc);
		return doc;
	}
	
	public void setHaltOnFailure(boolean flag) {
		haltOnFailure = flag;
	}
	
	public void setHaltOnError(boolean flag) {
		haltOnError = flag;
	}
	
	public void setHaltOnMismatch(boolean flag) {
		haltOnMismatch = flag;
	}
	
	public Path createClasspath() {
		classpath = new Path(getProject());
		return classpath;
	}
	
	public void setClasspathRef(Reference r) {
		createClasspath().setRefid(r);
	}
	
	public void setDir(String rootDirectory) {
		dir = rootDirectory;
	}
	
}
