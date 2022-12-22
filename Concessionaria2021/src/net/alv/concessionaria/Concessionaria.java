package net.alviano.concessionaria;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.Validate;

public final class Concessionaria {
	public static final String FILENAME = "data.csv";
	
	private final List<Veicolo> veicoli = new ArrayList<Veicolo>();
	
	public void aggiungi(Veicolo v) {
		Validate.notNull(v);
		veicoli.add(v);
	}
	
	public void rimuovi(Veicolo v) {
		Validate.notNull(v);
		final int idx = veicoli.indexOf(v);
		Validate.isTrue(idx != -1);
		veicoli.remove(idx);
	}
	
	public void rimuovi(int index) {
		Validate.inclusiveBetween(0, size() - 1, index);
		veicoli.remove(index);
	}
	
	public int size() {
		return veicoli.size();
	}
	
	public Veicolo get(int index) {
		Validate.inclusiveBetween(0, size() - 1, index);
		return veicoli.get(index);
	}
	
	public Prezzo calcolaSommaPrezziFinali() {
		Prezzo res = new Prezzo();
		for (Veicolo v : veicoli) {
			res = res.add(v.prezzoFinale());
		}
		return res;
		//return veicoli.stream()
		//		.map(v -> v.prezzoFinale())
		//		.reduce(new Prezzo(), (cum, curr) -> cum.add(curr));
	}
	
	public void ordinaPerPrezzoFinaleCrescente() {
		veicoli.sort((a, b) -> a.prezzoFinale().compareTo(b.prezzoFinale())); 
//		veicoli.sort(new Comparator<Veicolo>() {
//
//			@Override
//			public int compare(Veicolo arg0, Veicolo arg1) {
//				return arg0.prezzoFinale().compareTo(arg1.prezzoFinale());
//			}
//		});
	}
	
	public void ordinaPerModelloCrescente() {
		veicoli.sort((a, b) -> a.modello().compareTo(b.modello()));
	}
	
	public void salva() throws IOException {
		List<String> list = new ArrayList<String>();
		for (Veicolo v : this.veicoli) {
			String str = String.format("%s;%s;%s;%s;%s", v.tipo(), v.targa(), v.casaProduttrice(), v.modello(), v.prezzoPrimaDelloSconto());
			list.add(str);
		}
		Files.write(Paths.get(FILENAME), list);
	}
	
	public void load() throws IOException {
		veicoli.clear();
		List<String> lines = Files.readAllLines(Paths.get(FILENAME));
		for (String line : lines) {
			String[] atts = line.split(";");
			Validate.validState(atts.length == 5);
			String tipo = atts[0];
			Targa targa = new Targa(atts[1]);
			CasaProduttrice casaProduttrice = new CasaProduttrice(atts[2]);
			Modello modello = new Modello(atts[3]);
			Prezzo prezzoPrimaDelloSconto = Prezzo.parse(atts[4]);
			Veicolo toAdd = null;
			switch (tipo) {
			case "MOTO":
				toAdd = new Moto(targa, casaProduttrice, modello, prezzoPrimaDelloSconto);
				break;
			case "AUTOMOBILE":
				toAdd = new Automobile(targa, casaProduttrice, modello, prezzoPrimaDelloSconto);
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + tipo);
			}
			veicoli.add(toAdd);
		}
	}
}
