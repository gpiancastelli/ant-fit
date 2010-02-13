package org.apache.tools.ant.taskdefs.optional.fit;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

import org.apache.tools.ant.BuildException;

import fit.Fixture;
import fit.Parse;

public class Document {
	
	String name;
	int right, wrong, ignores, exceptions;
	boolean checked;
	private Fixture fixture;
	
	public Document() {
		name = "";
		right = -1;
		wrong = -1;
		ignores = -1;
		exceptions = -1;
	}
	
	void setDir(String rootDirectory) {
		if (rootDirectory.length() > 0)
			name = rootDirectory + "/" + name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setRight(int expected) {
		right = expected;
		checked = true;
	}
	
	public void setWrong(int expected) {
		wrong = expected;
		checked = true;
	}
	
	public void setIgnores(int expected) {
		ignores = expected;
		checked = true;
	}
	
	public void setExceptions(int expected) {
		exceptions = expected;
		checked = true;
	}
	
	boolean isChecked() {
		return checked;
	}

	public String counts() {
		return fixture.counts();
	}
	
	public int right() {
		return fixture.counts.right;
	}
	
	public int wrong() {
		return fixture.counts.wrong;
	}
	
	public int ignores() {
		return fixture.counts.ignores;
	}
	
	public int exceptions() {
		return fixture.counts.exceptions;
	}
	
	public boolean hasFailures() {
		return wrong() > 0;
	}
	
	public boolean hasErrors() {
		return wrong() > 0 || exceptions() > 0;
	}
	
	public String getFailureMessage() {
		return getName() + " run: " + wrong() + " errors.";
	}
	
	public String getErrorMessage() {
		return getName() + " run: " + wrong() + " errors, " + exceptions() + " exceptions.";
	}
	
	public void run() throws WrongResultException {
		fixture = new Fixture();
		try {
			Parse tables = new Parse(read());
			fixture.doTables(tables);
		} catch (ParseException e) {
			throw new BuildException(e);
		}
		checkRight();
		checkWrong();
		checkIgnores();
		checkExceptions();
	}

	void checkRight() throws WrongResultException {
		if (right >= 0)
			if (right != right()) {
				String message = getName() + " run: " +
				                 right + " right expected, but found " + right() + ".";
				throw new WrongResultException(message);
			}
	}
	
	void checkWrong() throws WrongResultException {
		if (wrong >= 0)
			if (wrong != wrong()) {
				String message = getName() + " run: " +
				                 wrong + " wrong expected, but found " + wrong() + ".";
				throw new WrongResultException(message);
			}
	}
	
	void checkIgnores() throws WrongResultException {
		if (ignores >= 0)
			if (ignores != ignores()) {
				String message = getName() + " run: " +
				                 ignores + " ignores expected, but found " + ignores() + ".";
				throw new WrongResultException(message);
			}
	}
	
	void checkExceptions() throws WrongResultException {
		if (exceptions >= 0)
			if (exceptions != exceptions()) {
				String message = getName() + " run: " +
				                 exceptions + " exceptions expected, but found " + exceptions() + ".";
				throw new WrongResultException(message);
			}
	}
	
	String read() {
		File input = new File(name);
		char[] chars = new char[(int) input.length()];
		FileReader reader;
		try {
			reader = new FileReader(input);
			reader.read(chars);
			reader.close();
		} catch (IOException e) {
			throw new BuildException(e);
		}
		return new String(chars);
	}
	
}

class WrongResultException extends Exception {
	
	static final long serialVersionUID = 4242626900705233437L;
	
	public WrongResultException(String message) {
		super(message);
	}
	
}