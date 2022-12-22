package net.alviano.studioMedico;

import org.apache.commons.lang3.Validate;

public final class Prenotazione {
	private final Paziente paziente;
	private final Orario orario;
	private final TipoVisita tipoVisita;
	private final Costo costo;
	
	public Prenotazione(Paziente paziente, Orario orario, TipoVisita tipoVisita, Costo costo) {
		this.paziente = Validate.notNull(paziente);
		this.orario = Validate.notNull(orario);
		this.tipoVisita = Validate.notNull(tipoVisita);
		this.costo = Validate.notNull(costo);
	}
	
	public Paziente paziente() {
		return paziente;
	}
	
	public Orario orario() {
		return orario;
	}
	
	public TipoVisita tipoVisita() {
		return tipoVisita;
	}
	
	public Costo costo() {
		return costo;
	}
	
	@Override
	public String toString() {
		return "Prenotazione: paziente=" + paziente + "; orario=" + orario + "; tipoVisita=" + tipoVisita + "; costo=" + costo;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!obj.getClass().equals(this.getClass())) {
			return false;
		}
		Prenotazione o = (Prenotazione) obj;
		return this.paziente.equals(o.paziente) && this.orario.equals(o.orario) && this.tipoVisita.equals(o.tipoVisita) && this.costo.equals(o.costo);
	}
	
	@Override
	public int hashCode() {
		throw new RuntimeException("Prenotazione::hashCode is disabled");
	}
}
