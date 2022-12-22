package net.alviano.concessionaria;

import org.apache.commons.lang3.Validate;

public final class Targa {
	public static final int LENGTH = 7;
	public static final String PATTERN = "[A-Z]{2}[0-9]{3}[A-Z]{2}";
	
	private final String value;
	
	public Targa(String value) {
		Validate.notNull(value);
		Validate.inclusiveBetween(LENGTH, LENGTH, value.length(),
				"Le targhe devono essere di lunghezza " + LENGTH + " e non di lunghezza " + value.length());
		Validate.matchesPattern(value, PATTERN);
		this.value = value;
	}
	
	public String value() {
		return value;
	}
	
	public String toString() {
		return value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !obj.getClass().equals(this.getClass())) {
			return false;
		}
		Targa o = (Targa) obj;
		return this.value.equals(o.value);
	}
	
	@Override
	public int hashCode() {
		return this.value.hashCode();
	}
}
