package org.apache.tools.ant.taskdefs.optional.fit;

import org.apache.tools.ant.BuildFileTest;

public class FitTaskTest extends BuildFileTest {
	
	public FitTaskTest(String name) {
		super(name);
	}
	
	public void setUp() {
		configureProject("build.xml");
	}
	
	public void testWithout() {
		executeTarget("use.without");
		assertEquals(getLog(), "");
	}
	
	public void testHaltOnFailure() {
		expectBuildException("use.haltOnFailure", "Test document has failures.");
	}
	
	public void testHaltOnError() {
		expectBuildException("use.haltOnError", "Test document has errors.");
	}
	
	public void testMultipleDocuments() {
		executeTarget("use.multipleDocuments");
		assertLogContaining("doc/arithmetic.html run: 37 right, 10 wrong, 0 ignored, 2 exceptions");
		assertLogContaining("doc/NetworkExample.html run: 5 right, 0 wrong, 0 ignored, 0 exceptions");
	}
	
	public void testCheckGoodResults() {
		executeTarget("use.checkGoodResults");
		assertLogContaining("doc/arithmetic.html run: 37 right, 10 wrong, 0 ignored, 2 exceptions");
	}
	
	public void testCheckBadResults() {
		executeTarget("use.checkBadResults");
		assertLogContaining("doc/arithmetic.html run: 0 wrong expected, but found 10.");
	}
	
	public void testHaltOnMismatch() {
		expectBuildException("use.haltOnMismatch", "Expected results are not verified.");
	}
	
	public void testClasspathAsNestedElement() {
		executeTarget("use.classpathAsNestedElement");
		assertLogContaining("doc/CalculateDiscount.html run: 7 right, 1 wrong, 0 ignored, 0 exceptions");
	}
	
	public void testClasspathAsAttribute() {
		executeTarget("use.classpathAsAttribute");
		assertLogContaining("doc/CalculateDiscount.html run: 7 right, 1 wrong, 0 ignored, 0 exceptions");
	}
	
	public void testUseDir() {
		executeTarget("use.dir");
		assertLogContaining("doc/NetworkExample.html run: 5 right, 0 wrong, 0 ignored, 0 exceptions");
	}

}
