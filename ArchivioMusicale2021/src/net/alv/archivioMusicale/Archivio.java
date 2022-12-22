package net.alviano.archivioMusicale;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.Validate;

public final class Archivio {
	public static final String FILENAME = "default.csv";
	public static final int MAX_CANZONI = 100000;

	private final List<Canzone> canzoni = new ArrayList<Canzone>();
	
	public int numeroDiCanzoni() {
		return canzoni.size();
	}
	
	public Canzone canzone(int index) {
		Validate.inclusiveBetween(0, numeroDiCanzoni() - 1, index);
		return canzoni.get(index);
	}
	
	public void aggiungi(Canzone canzone) {
		Validate.notNull(canzone);
		canzoni.add(canzone);
	}
	
	public void rimuovi(Canzone canzone) {
		Validate.notNull(canzone);
		boolean removed = canzoni.remove(canzone);
		Validate.validState(removed);
	}
	
	public void salva() {
		List<String> list = new ArrayList<String>();
		for (Canzone c : this.canzoni) {
			String str = String.format(
					"%s\t%s\t%s\t%s", 
					c.autore(),
					c.titolo(),
					c.genere(),
					c.durata()
			);
			list.add(str);
		}
		try {
			Files.write(Paths.get(FILENAME), list);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void load() {
		canzoni.clear();
		List<String> lines;
		try {
			lines = Files.readAllLines(Paths.get(FILENAME));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		Validate.inclusiveBetween(0, MAX_CANZONI, lines.size());
		for (String line : lines) {
			Validate.inclusiveBetween(0, 1000, line.length());
			String[] atts = line.split("\t");
			Validate.validState(atts.length == 4);
			Autore autore = new Autore(atts[0]);
			Titolo titolo = new Titolo(atts[1]);
			Genere genere = new Genere(atts[2]);
			Durata durata = Durata.parse(atts[3]);
			canzoni.add(new Canzone(autore, titolo, genere, durata));
		}
	}
	
	public List<Autore> autori() {
		Set<Autore> res = new HashSet<Autore>();
		for (Canzone c : canzoni) {
			res.add(c.autore());
		}
		return new ArrayList<Autore>(res);
	}
	
	public List<Canzone> canzoniDi(Autore autore) {
		Validate.notNull(autore);
		List<Canzone> res = new ArrayList<>();
		for (Canzone c : canzoni) {
			if (c.autore().equals(autore)) {
				res.add(c);
			}
		}
		return res;
	}
	
	public void ordinaPerDurataCrescente() {
		Collections.sort(canzoni, new Comparator<Canzone>() {

			@Override
			public int compare(Canzone a, Canzone b) {
				return a.durata().compareTo(b.durata());
			}
		});
	}
	
	public void ordinaPerGenereCrescente() {
		Collections.sort(canzoni, new Comparator<Canzone>() {

			@Override
			public int compare(Canzone a, Canzone b) {
				return a.genere().compareTo(b.genere());
			}
		});
	}

	public void ordinaPerTitoloCrescente() {
		Collections.sort(canzoni, new Comparator<Canzone>() {

			@Override
			public int compare(Canzone a, Canzone b) {
				return a.titolo().compareTo(b.titolo());
			}
		});
	}
	
	public Durata durataTotaleDelleCanzoniDi(Autore autore) {
		Validate.notNull(autore);
		Durata res = new Durata();
		List<Canzone> canzoniDi = canzoniDi(autore);
		for (Canzone c : canzoniDi) {
			res = res.add(c.durata());
		}
		return res;
	}

}
