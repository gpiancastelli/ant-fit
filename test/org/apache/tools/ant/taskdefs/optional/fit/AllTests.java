package org.apache.tools.ant.taskdefs.optional.fit;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.apache.tools.ant.taskdefs.optional.fit");
		suite.addTestSuite(FitTaskTest.class);
		suite.addTestSuite(DocumentTest.class);
		return suite;
	}

}
