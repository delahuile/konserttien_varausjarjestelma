package Konserttien_varausjarjestelma.varausjarjestelma;



import java.util.Random;
import java.sql.SQLException;
import java.util.ArrayList;

public class Asiakas {
	private String nimi;
	private ArrayList<Integer> lista;

	public Asiakas(String nimi) {
		ArrayList<Integer> lista = new ArrayList<Integer>();

		this.nimi = nimi;
		this.lista = lista;
	}

	public void uusiNimi(String nimi) {
		this.nimi = nimi;
	}

	public String annaNimi() {
		return nimi;
	}

	public int annaVaraus(int indexi) {
		return lista.get(indexi);
	}

	public ArrayList<Integer> annaVaraukset() {
		return lista;
	}

		/**
		 * Metodi lisää asiakkaalle uuden varauksen varaukset-listaan. LISÄKSI metodi palauttaa tehdyn varauksen varausnumeron.
		 * @return varausnumero
		 */
	public int lisaaVaraus() {


		Varausjärjestelmä vj = null; 

		try {
			vj = new Varausjärjestelmä();
		} catch (SQLException e) {
			System.out.println("Tietokantaan ei saada yhteyttä!");
		}

		Random rnd = new Random();
		int varausnumero = 1;

		while (true) {
			varausnumero = rnd.nextInt(99999);
			try {
				if (!(vj.onkoVarausJoTietokannassa(varausnumero))) {
					break;
				} else if (vj.onkoVarausJoTietokannassa(varausnumero)) {
					continue;
				}

			} catch (SQLException e) {
				System.out.println("Tietokantaan ei saada yhteyttä!");
			}
		}
		this.lista.add(varausnumero);
		
		return varausnumero;
	}
	
	public void asetaVaraukset(ArrayList<Integer> lista) {
		this.lista = lista;
	}

}
