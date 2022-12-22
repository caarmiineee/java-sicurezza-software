package net.alviano.archivioMusicale;

import org.apache.commons.lang3.Validate;

public final class Durata implements Comparable<Durata> {
	public static final int SECONDS_IN_A_MINUTE = 60;
	public static final int MAX_SECONDS = 120 * SECONDS_IN_A_MINUTE;  // 120 minutes
	public static final int MIN_LENGTH = 4;		// example:   0:00
	public static final int MAX_LENGTH = 6;		// example: 120:00
	
	private final int valueInSeconds;
	
	public static Durata fromValueInSeconds(int value) {
		return new Durata(value / SECONDS_IN_A_MINUTE, value % SECONDS_IN_A_MINUTE);
	}
	
	public static Durata parse(String str) {
		Validate.notNull(str);
		Validate.inclusiveBetween(MIN_LENGTH, MAX_LENGTH, str.length());
		String[] split = str.split(":");
		Validate.inclusiveBetween(2, 2, split.length);
		int minutes = Integer.parseInt(split[0]);
		Validate.inclusiveBetween(2, 2, split[1].length());
		int seconds = Integer.parseInt(split[1]);
		return new Durata(minutes, seconds);
	}
	
	public Durata() {
		this.valueInSeconds = 0;
	}
	
	public Durata(int minutes, int seconds) {
		Validate.inclusiveBetween(0, SECONDS_IN_A_MINUTE - 1, seconds);
		Validate.inclusiveBetween(0, MAX_SECONDS / SECONDS_IN_A_MINUTE, minutes);
		this.valueInSeconds = minutes * SECONDS_IN_A_MINUTE + seconds;
		Validate.inclusiveBetween(0, MAX_SECONDS, this.valueInSeconds);
	}
	
	public int minutes() {
		return valueInSeconds / SECONDS_IN_A_MINUTE;
	}
	
	public int seconds() {
		return valueInSeconds % SECONDS_IN_A_MINUTE;
	}
	
	public String toString() {
		return String.format("%d:%02d", minutes(), seconds());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!obj.getClass().equals(this.getClass())) {
			return false;
		}
		Durata o = (Durata) obj;
		return this.valueInSeconds == o.valueInSeconds;
	}
	
	@Override
	public int hashCode() {
		return Integer.hashCode(this.valueInSeconds);
	}
	
	public Durata add(Durata other) {
		Validate.notNull(other);
		return Durata.fromValueInSeconds(this.valueInSeconds + other.valueInSeconds);
	}
	
	@Override
	public int compareTo(Durata other) {
		return Integer.compare(this.valueInSeconds, other.valueInSeconds);
	}
}
