package org.apache.tools.ant.taskdefs.optional.fit;

import org.apache.tools.ant.taskdefs.optional.fit.Document;

import junit.framework.TestCase;

public class DocumentTest extends TestCase {

	private Document d;

	public void setUp() {
		d = new Document();
		d.setName("doc/arithmetic.html");
	}
	
	public void testRead() {
		assertTrue(d.read().length() > 0);
	}
	
	public void testRun() throws WrongResultException {
		d.run();
		assertEquals(37, d.right());
		assertEquals(10, d.wrong());
		assertEquals(0, d.ignores());
		assertEquals(2, d.exceptions());
	}
	
	public void testCheckRightResult() {
		d.setRight(36);
		assertTrue(d.isChecked());
		try {
			d.run();
			fail();
		} catch (WrongResultException e) {
			String expected = d.getName() + " run: " + d.right + " right expected, but found " + d.right() + ".";
			assertEquals(expected, e.getMessage());
		}
	}
	
	public void testCheckWrongResult() {
		d.setWrong(8);
		assertTrue(d.isChecked());
		try {
			d.run();
			fail();
		} catch (WrongResultException e) {
			String expected = d.getName() + " run: " + d.wrong + " wrong expected, but found " + d.wrong() + ".";
			assertEquals(expected, e.getMessage());
		}
	}
	
	public void testCheckIgnoresResult() {
		d.setIgnores(2);
		assertTrue(d.isChecked());
		try {
			d.run();
			fail();
		} catch (WrongResultException e) {
			String expected = d.getName() + " run: " + d.ignores + " ignores expected, but found " + d.ignores() + ".";
			assertEquals(expected, e.getMessage());
		}
	}
	
	public void testCheckExceptionsResult() {
		d.setExceptions(0);
		assertTrue(d.isChecked());
		try {
			d.run();
			fail();
		} catch (WrongResultException e) {
			String expected = d.getName() + " run: " + d.exceptions + " exceptions expected, but found " + d.exceptions() + ".";
			assertEquals(expected, e.getMessage());
		}
	}
	
}
