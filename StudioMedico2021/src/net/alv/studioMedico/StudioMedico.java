package net.alviano.studioMedico;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.Validate;

public final class StudioMedico {
	public static final String FILENAME = "default.csv";
	public static final int MAX_PRENOTAZIONI = 100000;

	private final List<Prenotazione> prenotazioni = new ArrayList<Prenotazione>();
	
	public int numeroPrenotazioni() {
		return prenotazioni.size();
	}
	
	public Prenotazione prenotazione(int index) {
		Validate.inclusiveBetween(0, numeroPrenotazioni() - 1, index);
		return prenotazioni.get(index);
	}
	
	public void aggiungi(Prenotazione prenotazione) {
		Validate.notNull(prenotazione);
		prenotazioni.add(prenotazione);
	}
	
	public void rimuovi(Prenotazione prenotazione) {
		Validate.notNull(prenotazione);
		boolean removed = prenotazioni.remove(prenotazione);
		Validate.validState(removed);
	}
	
	public void ordinaPerOrarioCrescente() {
		Collections.sort(prenotazioni, new Comparator<Prenotazione>() {

			@Override
			public int compare(Prenotazione a, Prenotazione b) {
				return a.orario().compareTo(b.orario());
			}
		});
	}
	
	public List<Prenotazione> prenotazioniDopoDiIncluso(Orario orario) {
		Validate.notNull(orario);
		List<Prenotazione> res = new ArrayList<>();
		for (Prenotazione p : prenotazioni) {
			if (p.orario().dopoDiIncluso(orario)) {
				res.add(p);
			}
		}
		return res;
	}
	
	public void salva() {
		List<String> list = new ArrayList<String>();
		for (Prenotazione p : this.prenotazioni) {
			String str = String.format(
					"%s\t%s\t%s\t%s", 
					p.paziente(),
					p.orario(),
					p.tipoVisita(),
					p.costo()
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
		prenotazioni.clear();
		List<String> lines;
		try {
			lines = Files.readAllLines(Paths.get(FILENAME));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		Validate.inclusiveBetween(0, MAX_PRENOTAZIONI, lines.size());
		for (String line : lines) {
			Validate.inclusiveBetween(0, 1000, line.length());
			String[] atts = line.split("\t");
			Validate.validState(atts.length == 4);
			Paziente paziente = new Paziente(atts[0]);
			Orario orario = Orario.parse(atts[1]);
			TipoVisita tipoVisita = new TipoVisita(atts[2]);
			Costo costo = Costo.parse(atts[3]);
			prenotazioni.add(new Prenotazione(paziente, orario, tipoVisita, costo));
		}
	}
}
