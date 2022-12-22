package net.alviano.menu;

import org.apache.commons.lang3.Validate;

public final class ChiaveOperazione implements Comparable<ChiaveOperazione> {
	public static final int MIN = 0;
	public static final int MAX = 100;

	private final int value;
	
	public ChiaveOperazione(int value) {
		Validate.inclusiveBetween(MIN, MAX, value);
		this.value = value;
	}
	
	public int value() {
		return value;
	}
	
	@Override
	public String toString() {
		return String.format("[%3d]", value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!obj.getClass().equals(this.getClass())) {
			return false;
		}
		ChiaveOperazione o = (ChiaveOperazione) obj;
		return this.value == o.value;
	}
	
	@Override
	public int hashCode() {
		return Long.hashCode(this.value);
	}

	@Override
	public int compareTo(ChiaveOperazione other) {
		return Integer.compare(this.value, other.value);
	}
}
