package net.alviano.menu;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import net.alviano.concessionaria.Automobile;
import net.alviano.concessionaria.CasaProduttrice;
import net.alviano.concessionaria.Concessionaria;
import net.alviano.concessionaria.Modello;
import net.alviano.concessionaria.Moto;
import net.alviano.concessionaria.Prezzo;
import net.alviano.concessionaria.Targa;
import net.alviano.concessionaria.Veicolo;
import net.alviano.menu.Operazione.ON_ACTIVATION_RESULT;

public class Main {
	private final Concessionaria concessionaria = new Concessionaria();
	private final Menu menu;
	
	public Main() {
		menu = new Menu.Builder(new Titolo("IL MIO MENU DI PROVA"), () -> {
				stampaVeicoli();
				return null;
			})
			.add(1, "Aggiungi automobile", () -> {
				aggiungiAutomobile();
				salva();
				return ON_ACTIVATION_RESULT.CONTINUE;
			})
			.add(2, "Aggiungi moto", () -> {
				aggiungiMoto();
				salva();
				return ON_ACTIVATION_RESULT.CONTINUE;
			})
			.add(3, "Ordina per modello crescente", () -> {
				ordinaPerModelloCrescente();
				salva();
				return ON_ACTIVATION_RESULT.CONTINUE;
			})
			.add(0, "Esci", () -> {
				System.out.println("Sei uscito!");
				return ON_ACTIVATION_RESULT.EXIT;
			})
			.build();
	}
	
	private void ordinaPerModelloCrescente() {
		concessionaria.ordinaPerModelloCrescente();
	}

	public void run() throws IOException {
		try {
			concessionaria.load();
		} catch (IOException e) {
			System.out.println("INIZIA CON LISTA DI VEICOLI VUOTI");
		}
		menu.run();
	}
	
	private Targa leggiTarga() {
		for (;;) {
			try {
				System.out.print("Targa: ");
				String line = menu.readline();
				return new Targa(line);
			} catch (IOException e) {
				throw new Error(e);
			} catch (IllegalArgumentException e) {
				System.out.println("Valore non valido");
			}
		}
	}
	
	private CasaProduttrice leggiCasaProduttrice() {
		for (;;) {
			try {
				System.out.print("Casa produttrice: ");
				String line = menu.readline();
				return new CasaProduttrice(line);
			} catch (IOException e) {
				throw new Error(e);
			} catch (IllegalArgumentException e) {
				System.out.println("Valore non valido");
			}
		}
	}
	
	private Modello leggiModello() {
		for (;;) {
			try {
				System.out.print("Modello: ");
				String line = menu.readline();
				return new Modello(line);
			} catch (IOException e) {
				throw new Error(e);
			} catch (IllegalArgumentException e) {
				System.out.println("Valore non valido");
			}
		}
	}

	private Prezzo leggiPrezzoPrimaDelloSconto() {
		for (;;) {
			try {
				System.out.print("Prezzo prima dello sconto: ");
				String line = menu.readline();
				return Prezzo.parse(line);
			} catch (IOException e) {
				throw new Error(e);
			} catch (IllegalArgumentException e) {
				System.out.println("Valore non valido");
			}
		}
	}
	
	private void salva() {
		try {
			concessionaria.salva();
		} catch (IOException e) {
			throw new Error(e);
		}
	}

	private void aggiungiAutomobile() {
		System.out.println("INSERIRE I DATI DELL'AUTOMOBILE");
		Targa targa = leggiTarga();
		CasaProduttrice casaProduttrice = leggiCasaProduttrice();
		Modello modello = leggiModello();
		Prezzo prezzoPrimaDelloSconto = leggiPrezzoPrimaDelloSconto();
		Automobile automobile = new Automobile(targa, casaProduttrice, modello, prezzoPrimaDelloSconto);
		concessionaria.aggiungi(automobile);
		System.out.println("AUTOMOBILE AGGIUNTA!");
	}
	
	private void aggiungiMoto() {
		System.out.println("INSERIRE I DATI DELLA MOTO");
		Targa targa = leggiTarga();
		CasaProduttrice casaProduttrice = leggiCasaProduttrice();
		Modello modello = leggiModello();
		Prezzo prezzoPrimaDelloSconto = leggiPrezzoPrimaDelloSconto();
		Moto moto = new Moto(targa, casaProduttrice, modello, prezzoPrimaDelloSconto);
		concessionaria.aggiungi(moto);
		System.out.println("MOTO AGGIUNTA!");
	}
	
	private void stampaVeicoli() {
		System.out.println();
		System.out.println(String.format("%-7s %-20s %-20s %12s %12s", "TARGA", "CASA PRODUTTRICE", "MODELLO", "PR. PS", "PR. FI"));
		System.out.println("------------------------------------------------------------------------------------");
		for (int i = 0; i < concessionaria.size(); i++) {
			Veicolo v = concessionaria.get(i);
			System.out.println(String.format("%-7s %-20s %-20s %12s %12s", v.targa(), StringUtils.abbreviate(v.casaProduttrice().toString(), 20), StringUtils.abbreviate(v.modello().toString(), 20), v.prezzoPrimaDelloSconto(), v.prezzoFinale()));
		}
		System.out.println();
	}
	
	
	public static void main(String[] args) {
		boolean DEBUG = true;
		try {
			new Main().run();
		} catch (IOException e) {
			System.out.println("Oops! Errore negli stream. Forse hai premuto CTRL+C?");
		} catch (Exception e) {
			System.out.println("Errore fatale");
			if (DEBUG) {
				e.printStackTrace();
			}
		}
	}
	
}
