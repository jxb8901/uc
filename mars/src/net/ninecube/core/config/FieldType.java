/**
 * 
 * created on 2007-1-23
 */
package net.ninecube.core.config;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author jxb
 * 
 */
public enum FieldType {
	string, bool, integer, date, amount, url, email, password, model, enumt;
	
	public Class toClass() {
		switch (this) {
		case bool: return Boolean.class;
		case integer: return Integer.class;
		case date: return Date.class;
		case amount: return BigDecimal.class;
		case model: return Object.class;
		case enumt: return String.class;
		default: return String.class;
		}
	}
}
