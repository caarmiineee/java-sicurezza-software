package net.alviano.ristorante;

import org.apache.commons.lang3.Validate;

public final class Ordine {
	private final Cliente cliente;
	private final Descrizione descrizione;
	private final Prezzo prezzo;
	
	public Ordine(Cliente cliente, Descrizione descrizione, Prezzo prezzo) {
		this.cliente = Validate.notNull(cliente);
		this.descrizione = Validate.notNull(descrizione);
		this.prezzo = Validate.notNull(prezzo);
	}
	
	public Cliente cliente() {
		return cliente;
	}
	
	public Descrizione descrizione() {
		return descrizione;
	}
	
	public Prezzo prezzo() {
		return prezzo;
	}
	
	@Override
	public String toString() {
		return "Ordine: cliente=" + cliente + "; descrizione=" + descrizione + "; prezzo=" + prezzo;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!obj.getClass().equals(this.getClass())) {
			return false;
		}
		Ordine o = (Ordine) obj;
		return this.cliente.equals(o.cliente) && this.descrizione.equals(o.descrizione) && this.prezzo.equals(o.prezzo);
	}
	
	@Override
	public int hashCode() {
		throw new RuntimeException("Ordine::hashCode is disabled");
	}
}
