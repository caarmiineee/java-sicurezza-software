package net.alviano.concessionaria;

import java.io.IOException;

public class Main {
	public static void main(String[] args) {
		Concessionaria c = new Concessionaria();
		try {
			c.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(c.size());
		System.out.println(c.get(0).prezzoPrimaDelloSconto());
	}
}
