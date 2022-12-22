package net.alviano.concessionaria;

import org.apache.commons.lang3.Validate;

public final class Moto implements Veicolo {
	public static final Prezzo PRIMA_SOGLIA = new Prezzo(7000);
	public static final Prezzo SECONDA_SOGLIA = new Prezzo(15000);
	public static final Aliquota PRIMO_SCONTO = new Aliquota(3);
	public static final Aliquota SECONDO_SCONTO = new Aliquota(7, 5);
	
	private final Targa targa;
	private final CasaProduttrice casaProduttrice;
	private final Modello modello;
	private final Prezzo prezzoPrimaDelloSconto;

	public Moto(Targa targa, CasaProduttrice casaProduttrice, Modello modello, Prezzo prezzoPrimaDelloSconto) {
		this.targa = Validate.notNull(targa);
		this.casaProduttrice = Validate.notNull(casaProduttrice);
		this.modello = Validate.notNull(modello);
		this.prezzoPrimaDelloSconto = Validate.notNull(prezzoPrimaDelloSconto);
	}
	
	@Override
	public Targa targa() {
		return targa;
	}

	@Override
	public CasaProduttrice casaProduttrice() {
		return casaProduttrice;
	}

	@Override
	public Modello modello() {
		return modello;
	}

	@Override
	public Prezzo prezzoPrimaDelloSconto() {
		return prezzoPrimaDelloSconto;
	}

	@Override
	public Prezzo prezzoFinale() {
		if (this.prezzoPrimaDelloSconto.minoreDiOUgualeA(PRIMA_SOGLIA)) {
			return this.prezzoPrimaDelloSconto.applicaSconto(PRIMO_SCONTO);
		}
		if (this.prezzoPrimaDelloSconto.minoreDiOUgualeA(SECONDA_SOGLIA)) {
			return this.prezzoPrimaDelloSconto.applicaSconto(SECONDO_SCONTO);
		}
		return this.prezzoPrimaDelloSconto;
	}
	
	@Override
	public String toString() {
		return String.format("Targa: %s\nCasa produttrice: %s\nModello: %s\nPrezzo prima dello sconto: %s\nPrezzo finale: %s", this.targa, this.casaProduttrice, this.modello, this.prezzoPrimaDelloSconto(), this.prezzoFinale());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!obj.getClass().equals(this.getClass())) {
			return false;
		}
		Moto o = (Moto) obj;
		return this.targa.equals(o.targa) && this.casaProduttrice.equals(o.casaProduttrice) && this.modello.equals(o.modello) && this.prezzoPrimaDelloSconto.equals(o.prezzoPrimaDelloSconto);
	}
	
	@Override
	public String tipo() {
		return "MOTO";
	}
}
