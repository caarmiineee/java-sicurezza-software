package net.alviano.ristorante;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import net.alviano.menu.Menu;
import net.alviano.menu.Titolo;
import net.alviano.menu.Menu.Builder;
import net.alviano.menu.Operazione.ON_ACTIVATION_RESULT;

public class Main {
	private final Ristorante ristorante = new Ristorante();
	private final Menu menu;
	
	public Main() {
		menu = new Menu.Builder(new Titolo("IL MIO RISTORANTE"), () -> {
				stampaOrdini();
				return null;
			})
			.add(1, "Aggiungi ordine", () -> {
				aggiungiOrdine();
				salva();
				return ON_ACTIVATION_RESULT.CONTINUE;
			})
			.add(2, "Rimuovi ordine", () -> {
				rimuoviOrdine();
				salva();
				return ON_ACTIVATION_RESULT.CONTINUE;
			})
			.add(3, "Stampa lista clienti", () -> {
				stampaListaClienti();
				return ON_ACTIVATION_RESULT.CONTINUE;
			})
			.add(4, "Filtra per cliente", () -> {
				filtraPerCliente();
				return ON_ACTIVATION_RESULT.CONTINUE;
			})
			.add(5, "Ordina per prezzo crescente", () -> {
				ordinaPerPrezzoCrescente();
				salva();
				return ON_ACTIVATION_RESULT.CONTINUE;
			})
			.add(0, "Esci", () -> {
				System.out.println("Bye!");
				return ON_ACTIVATION_RESULT.EXIT;
			})
			.build();
	}
	
	public void run() {
		try {
			ristorante.load();
		} catch (RuntimeException e) {
			System.out.println("INIZIA CON LISTA VUOTA");
		}
		menu.run();
	}
	
	private Cliente leggiCliente() {
		for (;;) {
			try {
				System.out.print("Cliente: ");
				String line = menu.readline();
				return new Cliente(line);
			} catch (IllegalArgumentException e) {
				System.out.println("Valore non valido");
			}
		}
	}
	
	private int leggiIndice() {
		for (;;) {
			try {
				System.out.print("Indice (-1 per annullare): ");
				String line = menu.readline();
				int index = Integer.parseInt(line);
				if (index == -1) {
					return -1;
				}
				Validate.inclusiveBetween(1, ristorante.numeroDiOrdini(), index);
				return index;
			} catch (IllegalArgumentException e) {
				System.out.println("Valore non valido");
			}
		}
	}
	
	private Descrizione leggiDescrizione() {
		for (;;) {
			try {
				System.out.print("Descrizione: ");
				String line = menu.readline();
				return new Descrizione(line);
			} catch (IllegalArgumentException e) {
				System.out.println("Valore non valido");
			}
		}
	}

	private Prezzo leggiPrezzo() {
		for (;;) {
			try {
				System.out.print("Prezzo: ");
				String line = menu.readline();
				return Prezzo.parse(line);
			} catch (IllegalArgumentException e) {
				System.out.println("Valore non valido");
			}
		}
	}
	
	private void salva() {
		ristorante.salva();
	}

	private void aggiungiOrdine() {
		System.out.println("INSERIRE I DATI DELL'ORDINE");
		Cliente cliente = leggiCliente();
		Descrizione descrizione = leggiDescrizione();
		Prezzo prezzo = leggiPrezzo();
		Ordine ordine = new Ordine(cliente, descrizione, prezzo);
		ristorante.aggiungi(ordine);
		System.out.println("ORDINE AGGIUNTO!");
	}
	
	private void stampaOrdini() {
		System.out.println();
		System.out.println(String.format("%3s %-20s %-20s %12s", "#", "CLIENTE", "DESCRIZIONE", "PREZZO"));
		System.out.println("------------------------------------------------------------------------------------");
		for (int i = 0; i < ristorante.numeroDiOrdini(); i++) {
			Ordine o = ristorante.ordine(i);
			System.out.println(String.format("%3s %-20s %-20s %12s", i + 1, StringUtils.abbreviate(o.cliente().toString(), 20), StringUtils.abbreviate(o.descrizione().toString(), 20), o.prezzo()));
		}
		System.out.println();
	}
	
	private void rimuoviOrdine() {
		System.out.println("RIMOZIONE ORDINE");
		int index = leggiIndice();
		if (index != -1) {
			ristorante.rimuoviOrdine(index - 1);
			System.out.println("RIMOSSO!");
		} else {
			System.out.println("OPERAZIONE ANNULLATA");
		}
	}
	
	private void stampaListaClienti() {
		System.out.println("LISTA CLIENTI");
		System.out.println("--------------");
		List<Cliente> clienti = ristorante.clienti();
		for (Cliente c : clienti) {
			System.out.println(c);
		}
	}
	
	private void filtraPerCliente() {
		System.out.println("FILTRA ORDINI PER CLIENTE");
		Cliente cliente = leggiCliente();
		List<Ordine> ordini = ristorante.ordiniDi(cliente);
		System.out.println();
		System.out.println(String.format("%-20s %12s", "DESCRIZIONE", "PREZZO"));
		System.out.println("------------------------------------------------------------------------------------");
		for (Ordine o : ordini) {
			System.out.println(String.format("%-20s %12s", StringUtils.abbreviate(o.descrizione().toString(), 20), o.prezzo()));
		}
		System.out.println();
		
		System.out.println("PREMI INVIO PER CONTINUARE");
		menu.readline();
	}
	
	private void ordinaPerPrezzoCrescente() {
		ristorante.ordinaPerPrezzoCrescente();
	}
	
	public static void main(String[] args) {
		boolean DEBUG = true;
		try {
			new Main().run();
		} catch (Exception e) {
			System.out.println("Errore fatale");
			if (DEBUG) {
				e.printStackTrace();
			}
		}
	}
	
}
