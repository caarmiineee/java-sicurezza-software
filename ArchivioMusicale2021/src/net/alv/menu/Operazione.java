package net.alviano.menu;

import java.util.function.Supplier;

import org.apache.commons.lang3.Validate;

public final class Operazione {
	public static enum ON_ACTIVATION_RESULT {EXIT, CONTINUE};
	
	private final ChiaveOperazione chiave;
	private final DescrizioneOperazione descrizione;
	private final Supplier<ON_ACTIVATION_RESULT> onActivation;
	
	public Operazione(ChiaveOperazione chiave, DescrizioneOperazione descrizione, Supplier<ON_ACTIVATION_RESULT> onActivation) {
		this.chiave = Validate.notNull(chiave);
		this.descrizione = Validate.notNull(descrizione);
		this.onActivation = Validate.notNull(onActivation);
	}
	
	public ChiaveOperazione chiave() {
		return chiave;
	}
	
	public DescrizioneOperazione descrizione() {
		return descrizione;
	}
	
	public ON_ACTIVATION_RESULT activate() {
		return onActivation.get();
	}
	
}
