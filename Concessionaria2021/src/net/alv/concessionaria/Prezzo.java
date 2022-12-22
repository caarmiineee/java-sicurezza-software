package net.alviano.concessionaria;

import java.util.Comparator;

import org.apache.commons.lang3.Validate;

public final class Prezzo implements Comparable<Prezzo> {
	public static final int MAX_EURO = 1999999999;
	private final long valueInCents;
	
	public static Prezzo fromValueInCents(long value) {
		return new Prezzo(value / 100, (int)(value % 100));
	}
	
	public static Prezzo parse(String str) {
		Validate.notNull(str);
		String[] split = str.split("\\.");
		Validate.inclusiveBetween(2, 2, split.length);
		int euro = Integer.parseInt(split[0]);
		int cents = Integer.parseInt(split[1]);
		Validate.inclusiveBetween(1, 2, split[1].length());
		if (split[1].length() == 1) {
			cents *= 10;
		}
		return new Prezzo(euro, cents);
	}
	
	public Prezzo() {
		this.valueInCents = 0;
	}
	
	public Prezzo(long euro) {
		Validate.inclusiveBetween(0, MAX_EURO, euro);
		this.valueInCents = euro * 100;
	}
	
	public Prezzo(long euro, int cents) {
		Validate.inclusiveBetween(0, 99, cents);
		Validate.inclusiveBetween(0, MAX_EURO, euro);
		// this.valueInCents = ((long) euro) * 100 + cents;
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
//		if (cents >= 10) {
//			return "" + euro + "." + cents;
//		}
//		return "" + euro + ".0" + cents;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!obj.getClass().equals(this.getClass())) {
			return false;
		}
		Prezzo o = (Prezzo) obj;
		return this.valueInCents == o.valueInCents;
	}
	
	@Override
	public int hashCode() {
		return Long.hashCode(this.valueInCents);
	}
	
	public Prezzo add(Prezzo other) {
		Validate.notNull(other);
		return Prezzo.fromValueInCents(this.valueInCents + other.valueInCents);
	}
	
	public Prezzo applicaSconto(Aliquota aliquota) {
		Validate.notNull(aliquota);
		long prezzoFinale = aliquota.apply(this.valueInCents);
		return Prezzo.fromValueInCents(prezzoFinale);
	}
	
	public boolean minoreDi(Prezzo other) {
		Validate.notNull(other);
		return this.valueInCents < other.valueInCents;
	}

	public boolean minoreDiOUgualeA(Prezzo other) {
		Validate.notNull(other);
		return this.valueInCents <= other.valueInCents;
	}

	@Override
	public int compareTo(Prezzo other) {
		long diff = this.valueInCents - other.valueInCents;
		if (diff < 0) {
			return -1;
		}
		if (diff > 0) {
			return 1;
		}
		return 0;
	}
}
