package net.alviano.concessionaria;

public interface Veicolo {
	public String tipo();
	public Targa targa();
	public CasaProduttrice casaProduttrice();
	public Modello modello();
	public Prezzo prezzoPrimaDelloSconto();
	public Prezzo prezzoFinale();
}
