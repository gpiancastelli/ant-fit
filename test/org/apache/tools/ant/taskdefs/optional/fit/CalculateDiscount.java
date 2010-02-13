package org.apache.tools.ant.taskdefs.optional.fit;

import fit.ColumnFixture;

/**
 * @author Rick Mugridge 6/12/2003
 *
 * Copyright (c) 2003 Rick Mugridge, University of Auckland, NZ
 * Released under the terms of the GNU General Public License version 2 or later.
 */
public class CalculateDiscount extends ColumnFixture {
	
	public double amount;
	private Discount application = new Discount();
	
	public double discount() {
		return application.getDiscount(amount);
	}

}
