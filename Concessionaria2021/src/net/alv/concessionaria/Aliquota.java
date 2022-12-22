package net.alviano.concessionaria;

import org.apache.commons.lang3.Validate;

public final class Aliquota {
	private final int valueInDecimals;
	
	public static Aliquota parse(String str) {
		Validate.notNull(str);
		String[] split = str.split("\\.");
		Validate.inclusiveBetween(2, 2, split.length);
		int value = Integer.parseInt(split[0]);
		int decimals = Integer.parseInt(split[1]);
		Validate.inclusiveBetween(1, 1, split[1].length());
		return new Aliquota(value, decimals);
	}
	
	public Aliquota() {
		this.valueInDecimals = 0;
	}
	
	public Aliquota(int value) {
		Validate.inclusiveBetween(0, 100, value);
		this.valueInDecimals = value * 10;
	}
	
	public Aliquota(int value, int decimal) {
		Validate.inclusiveBetween(0, 100, value);
		Validate.inclusiveBetween(0, 9, decimal);
		this.valueInDecimals = value * 10 + decimal;
	}
	
	public int value() {
		return valueInDecimals / 10;
	}
	
	public int decimals() {
		return valueInDecimals % 10;
	}
	
	public long apply(long value) {
		Validate.inclusiveBetween(0, Long.MAX_VALUE / 1000, value);
		return value * (1000 - this.valueInDecimals) / 1000;
	}
	
	public String toString() {
		if (decimals() == 0) {
			return String.format("%d%%", value());
		}
		return String.format("%d.%d%%", value(), decimals());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!obj.getClass().equals(this.getClass())) {
			return false;
		}
		Aliquota o = (Aliquota) obj;
		return this.valueInDecimals == o.valueInDecimals;
	}
	
	@Override
	public int hashCode() {
		return Integer.hashCode(this.valueInDecimals);
	}
}
