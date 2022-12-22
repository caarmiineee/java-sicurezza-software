package net.alviano.ristorante;

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

public final class Ristorante {
	public static final String FILENAME = "default.csv";
	
	private final List<Ordine> ordini = new ArrayList<Ordine>();
	
	public List<Ordine> ordini() {
		return Collections.unmodifiableList(ordini);
	}
	
	public int numeroDiOrdini() {
		return ordini.size();
	}
	
	public Ordine ordine(int index) {
		Validate.inclusiveBetween(0, ordini.size() - 1, index);
		return ordini.get(index);
	}
	
	public void aggiungi(Ordine ordine) {
		Validate.notNull(ordine);
		ordini.add(ordine);
	}
	
	public void rimuoviOrdine(int index) {
		Validate.inclusiveBetween(0, ordini.size() - 1, index);
		ordini.remove(index);
	}
	
	public List<Cliente> clienti() {
		Set<Cliente> res = new HashSet<Cliente>();
		for (Ordine o : ordini) {
			res.add(o.cliente());
		}
		return new ArrayList<Cliente>(res);
	}
	
	public List<Ordine> ordiniDi(Cliente cliente) {
		Validate.notNull(cliente);
		List<Ordine> res = new ArrayList<Ordine>();
		for (Ordine o : ordini) {
			if (o.cliente().equals(cliente)) {
				res.add(o);
			}
		}
		return res;
	}
	
	public void ordinaPerPrezzoCrescente() {
		Collections.sort(ordini, new Comparator<Ordine>() {

			@Override
			public int compare(Ordine a, Ordine b) {
				return a.prezzo().compareTo(b.prezzo());
			}
		});
//		Collections.sort(ordini, (a, b) -> a.prezzo().compareTo(b.prezzo()));
//		ordini.sort(new Comparator<Ordine>() {
//
//			@Override
//			public int compare(Ordine a, Ordine b) {
//				return a.prezzo().compareTo(b.prezzo());
//			}
//		});
//		ordini.sort((a, b) -> a.prezzo().compareTo(b.prezzo())); 
	}
	
	public void salva() {
		List<String> list = new ArrayList<String>();
		for (Ordine o : this.ordini) {
			String str = String.format(
					"%s;%s;%s", 
					o.cliente(),
					o.descrizione(),
					o.prezzo()
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
		ordini.clear();
		List<String> lines;
		try {
			lines = Files.readAllLines(Paths.get(FILENAME));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		for (String line : lines) {
			Validate.inclusiveBetween(0, 1000, line.length());
			String[] atts = line.split(";");
			Validate.validState(atts.length == 3);
			Cliente cliente = new Cliente(atts[0]);
			Descrizione descrizione = new Descrizione(atts[1]);
			Prezzo prezzo = Prezzo.parse(atts[2]);
			ordini.add(new Ordine(cliente, descrizione, prezzo));
		}
	}
}
