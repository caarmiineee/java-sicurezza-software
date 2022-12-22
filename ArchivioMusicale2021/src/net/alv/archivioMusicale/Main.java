package net.alviano.archivioMusicale;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import net.alviano.menu.Menu;
import net.alviano.menu.Menu.Builder;
import net.alviano.menu.Operazione.ON_ACTIVATION_RESULT;

public final class Main {
	private final Archivio archivio = new Archivio();
	private final Menu menu;
	
	public Main() {
		menu = new Menu.Builder(new net.alviano.menu.Titolo("IL MIO ARCHIVIO MUSICALE"), () -> {
				stampaArchivio();
				return null;
			})
			.add(1, "Aggiungi canzone", () -> {
				aggiungiCanzone();
				salva();
				return ON_ACTIVATION_RESULT.CONTINUE;
			})
			.add(2, "Rimuovi canzone", () -> {
				rimuoviCanzone();
				salva();
				return ON_ACTIVATION_RESULT.CONTINUE;
			})
			.add(3, "Stampa lista autori", () -> {
				stampaListaAutori();
				return ON_ACTIVATION_RESULT.CONTINUE;
			})
			.add(4, "Filtra per autore", () -> {
				filtraPerAutore();
				return ON_ACTIVATION_RESULT.CONTINUE;
			})
			.add(5, "Ordina per durata crescente", () -> {
				ordinaPerDurataCrescente();
				salva();
				return ON_ACTIVATION_RESULT.CONTINUE;
			})
			.add(6, "Ordina per titolo crescente", () -> {
				ordinaPerTitoloCrescente();
				salva();
				return ON_ACTIVATION_RESULT.CONTINUE;
			})
			.add(7, "Ordina per genere crescente", () -> {
				ordinaPerGenereCrescente();
				salva();
				return ON_ACTIVATION_RESULT.CONTINUE;
			})
			.add(8, "Durata totale delle canzoni di un autore", () -> {
				durataTotaleDelleCanzoniDiUnAutore();
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
			archivio.load();
		} catch (RuntimeException e) {
			System.out.println("INIZIA CON LISTA VUOTA");
		}
		menu.run();
	}
	
	private Autore leggiAutore() {
		for (;;) {
			try {
				System.out.print("Autore: ");
				String line = menu.readline();
				return new Autore(line);
			} catch (IllegalArgumentException e) {
				System.out.println("Valore non valido");
			}
		}
	}
	
	private Titolo leggiTitolo() {
		for (;;) {
			try {
				System.out.print("Titolo: ");
				String line = menu.readline();
				return new Titolo(line);
			} catch (IllegalArgumentException e) {
				System.out.println("Valore non valido");
			}
		}
	}

	private Genere leggiGenere() {
		for (;;) {
			try {
				System.out.print("Genere: ");
				String line = menu.readline();
				return new Genere(line);
			} catch (IllegalArgumentException e) {
				System.out.println("Valore non valido");
			}
		}
	}

	private Durata leggiDurata() {
		for (;;) {
			try {
				System.out.print("Durata: ");
				String line = menu.readline();
				return Durata.parse(line);
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
				Validate.inclusiveBetween(1, archivio.numeroDiCanzoni(), index);
				return index;
			} catch (IllegalArgumentException e) {
				System.out.println("Valore non valido");
			}
		}
	}
	
	private void salva() {
		archivio.salva();
	}

	private void aggiungiCanzone() {
		System.out.println("INSERIRE I DATI DELLA CANZONE");
		Autore autore = leggiAutore();
		Titolo titolo = leggiTitolo();
		Genere genere = leggiGenere();
		Durata durata = leggiDurata();
		Canzone canzone = new Canzone(autore, titolo, genere, durata);
		archivio.aggiungi(canzone);
		System.out.println("CANZONE AGGIUNTA!");
	}
	
	private void stampaArchivio() {
		System.out.println();
		System.out.println(String.format("%3s %-20s %-20s %-15s %6s", "#", "AUTORE", "TITOLO", "GENERE", "DURATA"));
		System.out.println("------------------------------------------------------------------------------------");
		for (int i = 0; i < archivio.numeroDiCanzoni(); i++) {
			Canzone c = archivio.canzone(i);
			System.out.println(String.format("%3s %-20s %-20s %-15s %6s", i + 1, StringUtils.abbreviate(c.autore().toString(), 20), StringUtils.abbreviate(c.titolo().toString(), 20), StringUtils.abbreviate(c.genere().toString(), 15), c.durata()));
		}
		System.out.println();
	}
	
	private void rimuoviCanzone() {
		System.out.println("RIMOZIONE CANZONE");
		int index = leggiIndice();
		if (index != -1) {
			Canzone canzone = archivio.canzone(index - 1);
			archivio.rimuovi(canzone);
			System.out.println("RIMOSSA!");
		} else {
			System.out.println("OPERAZIONE ANNULLATA");
		}
	}
	
	private void stampaListaAutori() {
		System.out.println("LISTA AUTORI");
		System.out.println("-------------");
		List<Autore> autori = archivio.autori();
		for (Autore a : autori) {
			System.out.println(a);
		}
	}
	
	private void filtraPerAutore() {
		System.out.println("FILTRA ORDINI PER AUTORE");
		Autore autore = leggiAutore();
		List<Canzone> canzoni = archivio.canzoniDi(autore);
		System.out.println();
		System.out.println(String.format("%-20s %-15s %6s", "TITOLO", "GENERE", "DURATA"));
		System.out.println("------------------------------------------------------------------------------------");
		for (Canzone c : canzoni) {
			System.out.println(String.format("%-20s %-15s %6s", StringUtils.abbreviate(c.titolo().toString(), 20), StringUtils.abbreviate(c.genere().toString(), 15), c.durata()));
		}
		System.out.println();
		
		System.out.println("PREMI INVIO PER CONTINUARE");
		menu.readline();
	}
	
	private void durataTotaleDelleCanzoniDiUnAutore() {
		System.out.println("DURATA TOTALE DELLE CANZONI DI");
		Autore autore = leggiAutore();
		Durata durata = archivio.durataTotaleDelleCanzoniDi(autore);
		System.out.println("Durata totale: " + durata);
		
		System.out.println("PREMI INVIO PER CONTINUARE");
		menu.readline();
	}

	private void ordinaPerDurataCrescente() {
		archivio.ordinaPerDurataCrescente();
	}
	
	private void ordinaPerGenereCrescente() {
		archivio.ordinaPerGenereCrescente();
	}
	
	private void ordinaPerTitoloCrescente() {
		archivio.ordinaPerTitoloCrescente();
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
