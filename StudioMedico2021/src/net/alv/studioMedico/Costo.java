package net.alviano.studioMedico;

import org.apache.commons.lang3.Validate;

public final class Costo implements Comparable<Costo> {
	public static final int MAX_EURO = 1999999999;
	private final long valueInCents;
	
	public static Costo fromValueInCents(long value) {
		return new Costo(value / 100, (int)(value % 100));
	}
	
	public static Costo parse(String str) {
		Validate.notNull(str);
		String[] split = str.split("\\.");
		Validate.inclusiveBetween(2, 2, split.length);
		int euro = Integer.parseInt(split[0]);
		int cents = Integer.parseInt(split[1]);
		Validate.inclusiveBetween(1, 2, split[1].length());
		if (split[1].length() == 1) {
			cents *= 10;
		}
		return new Costo(euro, cents);
	}
	
	public Costo() {
		this.valueInCents = 0;
	}
	
	public Costo(long euro) {
		Validate.inclusiveBetween(0, MAX_EURO, euro);
		this.valueInCents = euro * 100;
	}
	
	public Costo(long euro, int cents) {
		Validate.inclusiveBetween(0, 99, cents);
		Validate.inclusiveBetween(0, MAX_EURO, euro);
		this.valueInCents = euro * 100 + cents;
	}
	
	public int euro() {
		return (int) (this.valueInCents / 100);
	}
	
	public int cents() {
		return (int) (this.valueInCents % 100);
	}
	
	public long valueInCents() {
		return valueInCents;
	}
	
	public String toString() {
		int euro = this.euro();
		int cents = this.cents();
		return String.format("%d.%02d", euro, cents);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!obj.getClass().equals(this.getClass())) {
			return false;
		}
		Costo o = (Costo) obj;
		return this.valueInCents == o.valueInCents;
	}
	
	@Override
	public int hashCode() {
		return Long.hashCode(this.valueInCents);
	}
	
	public Costo add(Costo other) {
		Validate.notNull(other);
		return Costo.fromValueInCents(this.valueInCents + other.valueInCents);
	}
	
	@Override
	public int compareTo(Costo other) {
		return Long.compare(this.valueInCents, other.valueInCents);
	}
}
