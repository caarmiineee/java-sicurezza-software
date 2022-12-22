package net.alviano.concessionaria;

import org.apache.commons.lang3.Validate;

public final class Modello implements Comparable<Modello> {
	public static final int MIN_LENGTH = 1;
	public static final int MAX_LENGTH = 255;
	public static final String PATTERN = "[A-Za-z0-9 _-]*";
	
	
	private final String value;
	
	public Modello(String value) {
		Validate.notNull(value);
		Validate.inclusiveBetween(MIN_LENGTH, MAX_LENGTH, value.length());
		Validate.matchesPattern(value, PATTERN);
		this.value = value;
	}
	
	public String value() {
		return value;
	}
	
	@Override
	public String toString() {
		return value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!obj.getClass().equals(this.getClass())) {
			return false;
		}
		Modello o = (Modello) obj;
		return this.value.equals(o.value);
	}
	
	@Override
	public int hashCode() {
		return this.value.hashCode();
	}

	@Override
	public int compareTo(Modello other) {
		return this.value.compareToIgnoreCase(other.value);
	}
}
