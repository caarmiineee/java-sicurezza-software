package net.alviano.studioMedico;

import org.apache.commons.lang3.Validate;

public final class Orario implements Comparable<Orario> {
	public static final int MINUTES_IN_AN_HOUR = 60;
	public static final int MAX_MINUTES = 24 * MINUTES_IN_AN_HOUR - 1;
	public static final int MIN_LENGTH = 5;		// example: 00:00
	public static final int MAX_LENGTH = 5;		// example: 23:59
	public static final String PATTERN = "[0-9]{2}:[0-9]{2}";
	
	private final int valueInMinutes;
	
	public static Orario fromValueInMinutes(int value) {
		return new Orario(value / MINUTES_IN_AN_HOUR, value % MINUTES_IN_AN_HOUR);
	}
	
	public static Orario parse(String str) {
		Validate.notNull(str);
		Validate.inclusiveBetween(MIN_LENGTH, MAX_LENGTH, str.length());
		Validate.matchesPattern(str, PATTERN);
		String[] split = str.split(":");
		int hours = Integer.parseInt(split[0]);
		int minutes = Integer.parseInt(split[1]);
		return new Orario(hours, minutes);
	}
	
	public Orario() {
		this.valueInMinutes = 0;
	}
	
	public Orario(int hours, int minutes) {
		Validate.inclusiveBetween(0, MINUTES_IN_AN_HOUR - 1, minutes);
		Validate.inclusiveBetween(0, MAX_MINUTES / MINUTES_IN_AN_HOUR, hours);
		this.valueInMinutes = hours * MINUTES_IN_AN_HOUR + minutes;
		Validate.inclusiveBetween(0, MAX_MINUTES, this.valueInMinutes);
	}
	
	public int hours() {
		return valueInMinutes / MINUTES_IN_AN_HOUR;
	}
	
	public int minutes() {
		return valueInMinutes % MINUTES_IN_AN_HOUR;
	}
	
	public String toString() {
		return String.format("%02d:%02d", hours(), minutes());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!obj.getClass().equals(this.getClass())) {
			return false;
		}
		Orario o = (Orario) obj;
		return this.valueInMinutes == o.valueInMinutes;
	}
	
	@Override
	public int hashCode() {
		return Integer.hashCode(this.valueInMinutes);
	}
	
	@Override
	public int compareTo(Orario other) {
		return Integer.compare(this.valueInMinutes, other.valueInMinutes);
	}
	
	public boolean dopoDiIncluso(Orario other) {
		Validate.notNull(other);
		return this.valueInMinutes >= other.valueInMinutes;
	}
}
