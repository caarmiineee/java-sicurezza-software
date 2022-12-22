package net.alviano.archivioMusicale;

import org.apache.commons.lang3.Validate;

public final class Canzone {
	private final Autore autore;
	private final Titolo titolo;
	private final Genere genere;
	private final Durata durata;
	
	public Canzone(Autore autore, Titolo titolo, Genere genere, Durata durata) {
		this.autore = Validate.notNull(autore);
		this.titolo = Validate.notNull(titolo);
		this.genere = Validate.notNull(genere);
		this.durata = Validate.notNull(durata);
	}
	
	public Autore autore() {
		return autore;
	}
	
	public Titolo titolo() {
		return titolo;
	}
	
	public Genere genere() {
		return genere;
	}
	
	public Durata durata() {
		return durata;
	}
	
	@Override
	public String toString() {
		return "Canzone: autore=" + autore + "; titolo=" + titolo + "; genere=" + genere + "; durata=" + durata;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!obj.getClass().equals(this.getClass())) {
			return false;
		}
		Canzone o = (Canzone) obj;
		return this.autore.equals(o.autore) && this.titolo.equals(o.titolo) && this.genere.equals(o.genere) && this.durata.equals(o.durata);
	}
	
	@Override
	public int hashCode() {
		throw new RuntimeException("Canzone::hashCode is disabled");
	}
}
