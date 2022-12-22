package net.alviano.menu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.apache.commons.lang3.Validate;

import net.alviano.menu.Operazione.ON_ACTIVATION_RESULT;

public class Menu {
	private final BufferedReader reader;
	private final Titolo titolo;
	private final Supplier<Void> dopoIlTitolo;
	private final List<Operazione> operazioni = new ArrayList<Operazione>();
	
	private Menu(BufferedReader reader, Titolo titolo, Supplier<Void> dopoIlTitolo) {
		this.reader = Validate.notNull(reader);
		this.titolo = Validate.notNull(titolo);
		this.dopoIlTitolo = Validate.notNull(dopoIlTitolo);
	}
	
	public void run() {
		for (;;) {
			System.out.println();
			System.out.println(this.titolo);
			this.dopoIlTitolo.get();
			for (Operazione op : this.operazioni) {
				System.out.println(op.chiave() + " " + op.descrizione());
			}
			System.out.print("? ");
			String line = this.readline();
			try {
				ChiaveOperazione chiave = new ChiaveOperazione(Integer.parseInt(line));
				boolean ok = false;
				for (Operazione op : operazioni) {
					if (op.chiave().equals(chiave)) {
						ok = true;
						ON_ACTIVATION_RESULT res = op.activate();
						if (res == ON_ACTIVATION_RESULT.EXIT) {
							return;
						}
						break;
					}
				}
				if (!ok) {
					System.out.println("Operazione non trovata");
				}
			} catch (IllegalArgumentException e) {
				System.out.println("Chiave non valida");
			}
		}
	}
	
	public String readline() {
		try {
			return reader.readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static class Builder {
		private Menu instance;
		
		public Builder(Titolo titolo) {
			instance = new Menu(new BufferedReader(new InputStreamReader(System.in)), titolo, () -> null);
		}
		public Builder(Titolo titolo, Supplier<Void> dopoIlTitolo) {
			instance = new Menu(new BufferedReader(new InputStreamReader(System.in)), titolo, dopoIlTitolo);
		}
		
		public Builder add(Operazione op) {
			Validate.notNull(op);
			Validate.validState(instance != null);
			instance.operazioni.add(op);
			return this;
		}
		
		public Builder add(int chiave, String descrizione, Supplier<ON_ACTIVATION_RESULT> onActivation) {
			return this.add(new Operazione(new ChiaveOperazione(chiave), new DescrizioneOperazione(descrizione), onActivation));
		}
		
		public Menu build() {
			Validate.validState(instance != null);
			Validate.validState(!instance.operazioni.isEmpty());
//			instance.operazioni.stream().map(op -> op.chiave());
			long unique = instance.operazioni.stream().map(Operazione::chiave).sorted().distinct().count();
			Validate.validState(instance.operazioni.size() == unique);
			
			Menu res = instance;
			instance = null;
			return res;
		}
	}
}
