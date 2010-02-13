package org.apache.tools.ant.taskdefs.optional.fit;

import java.io.*;
import java.text.ParseException;
import java.util.Vector;

import org.apache.tools.ant.*;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.types.*;

import fit.Fixture;
import fit.Parse;

/**
 * Ant task to run <a href="http://fit.c2.com/">FIT</a>.
 * 
 * @author     <a href="mailto:sen@cookiehome.com">Steven E. Newton</a>
 * @version    $Revision: 1.4 $ $Date: 2004/01/17 17:34:37 $
 */
public class FitReportTask extends Task {

	/*
	private File inputDoc;
	private File outputDoc;
	*/
	private Vector filesets = new Vector();
	private Path myClassPath;
	File destDir = null;
	private String wikitags[] = {"wiki", "table", "tr", "td"};
	private boolean useWiki = true;
	private boolean fork;

	/**
	 * no-args constructor 
	 */
	public FitReportTask() {
		super();
	}

	public void execute() {
		AntClassLoader myLoader = new AntClassLoader(null, getProject(), myClassPath, true);                 
		myLoader.setThreadContextLoader();
        log("Classpath is "+myLoader.getClasspath(), Project.MSG_VERBOSE);
        try {
    		processFiles();
        } finally {
            myLoader.resetThreadContextLoader();
    		myLoader.cleanup();
        }
	}
	
	/**
	 * @param tables
	 * @param outputDoc
	 */
	private void write(Parse tables, File outputDoc) {
		FileWriter fw;
		try {
			fw = new FileWriter(outputDoc);
			PrintWriter out = new PrintWriter(fw);
			tables.print(out);
			out.close();
		} catch (IOException e) {
			log(e.getMessage());
		}	
	}

    private void processFiles() {
		for (int i = 0; i < filesets.size(); i++) {
			FileSet fs = (FileSet) filesets.elementAt(i);
			DirectoryScanner ds = fs.getDirectoryScanner(getProject());
			File fromDir = fs.getDir(getProject());
	
			String[] srcFiles = ds.getIncludedFiles();
	
			for (int j = 0; j < srcFiles.length ; j++) {
				try {
					File inputDoc = new File(fromDir, srcFiles[j]);
					File outputDoc = new File(destDir, srcFiles[j]);
					if (fork) {
                        forkFit(inputDoc, outputDoc, srcFiles[j]);
				    } else {
                        callFit(inputDoc, outputDoc, srcFiles[j]);
                    }
				} catch (Exception e) {
					log(e.getMessage());
				}
			}         
		}
    }
 
    private void callFit(File inputDoc, File outputDoc, String fileName) throws ParseException {
        Fixture fixture = new Fixture();
        Parse tables;
        if (useWiki) {
            tables = new Parse(read(inputDoc), wikitags);
            fixture.doTables(tables.parts);
        } else {
            tables = new Parse(read(inputDoc));
            fixture.doTables(tables);
        }
        log(fileName+" complete, "+fixture.counts());
        write(tables, outputDoc);
    }
    
    private void forkFit(File inputDoc, File outputDoc, String fileName) throws ParseException {
        Java javaTask = new Java();
        javaTask.setOwningTarget(getOwningTarget());
        javaTask.setProject(getProject());
        javaTask.setTaskName(getTaskName());
        javaTask.setFork(true);
        javaTask.setClasspath(myClassPath);
        javaTask.setClassname(useWiki ? "fit.WikiRunner" : "fit.FileRunner");
        javaTask.createArg().setFile(inputDoc);
        javaTask.createArg().setFile(outputDoc);
        // ignore the return code, because the fit results are already printed to standard out
        log(fileName + " results:");
        javaTask.executeJava();
    }
    
	/**
	 * @param inputDoc
	 */
	private String read(File inputDoc) {
		char chars[] = new char[(int)inputDoc.length()];
		FileReader in;
		try {
			in = new FileReader(inputDoc);
			in.read(chars);
			in.close();
		} catch (Exception e) {
			log(e.getMessage());
		}
		return new String(chars);
	}

	/*
	public void setInputDoc(File anInputDoc) {
		inputDoc = anInputDoc;
	}
	
	public void setOutputDoc(File anOutputDoc) {
		outputDoc = anOutputDoc;
	}
	*/

	public void addFileSet(FileSet fitSet) {
		filesets.addElement(fitSet);
	}
	
	/**
	 * Sets the destination directory.
	 */
	public void setDestdir(File destDir) {
		this.destDir = destDir;
	}
	
	public Path createClasspath() {
		if (myClassPath == null) {
			myClassPath = new Path(getProject()); 
		}
		return myClassPath.createPath();
	}
	
	public void setUseWiki(boolean useWiki) {
	   this.useWiki = useWiki;
    	}
	
	public void setFork(boolean fork) {
	   this.fork = fork;
    	}

	public void setClasspath(Path p) {
		createClasspath().append(p);
	}

	public void setClasspathRef(Reference r) {
		createClasspath().setRefid(r);
	}
}