package Konserttien_varausjarjestelma.varausjarjestelma;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Varausjärjestelmä {
	private Connection c;

	public Varausjärjestelmä() throws SQLException {
		c = DriverManager.getConnection("jdbc:sqlite:varaustilanne6.db");
	}

	
	
	
	/**
	 * Metodi lisää konserttiolion tiedot tietokannan Konsertti-relaatiotauluun
	 * Konserttia ei lisätä tietokantaan, jos konsertti on jo tietokannassa.
	 * 
	 * @param Konsertti k
	 * @throws SQLException, KonserttiJoLisattyException
	 */
	public void lisaaKonsertti(Konsertti k) throws SQLException {

		// Tarkistetaan onkoKonserttiJoTietokannassa -metodilla että onko konserttiolio
		// k jo tietokannassa
		// ja annetaan KonserttiJoLisattyException -poikkeus jos konsertti on jo
		// tietokannassa.
		if (!(onkoKonserttiJoTietokannassa(k))) {
			String sq1 = "INSERT INTO KONSERTTI VALUES (?,?,?,?,?,?);";

			try (PreparedStatement ps1 = c.prepareStatement(sq1)) {

				ps1.setString(1, k.annaEsiintyja());
				ps1.setString(2, k.annaPaivamaara());
				ps1.setInt(3, k.annaHinta());
				ps1.setInt(4, k.annaPaikkojenMaara());
				ps1.setString(5, k.annaKonserttisali());
				ps1.setInt(6, 0);
				ps1.execute();

			}
		}
	}

	/**
	 * Metodi lisää konserttiolion tiedot tietokannan Konsertti-relaatiotauluun.
	 * Konserttia ei lisätä tietokantaan, jos konsertti on jo tietokannassa.
	 * HUOM! Luultavasti turha metodi.
	 * 
	 * @param Konsertti k
	 * @throws SQLException, KonserttiJoLisattyException
	 */

	public void lisaaIstumaKonsertti(IstumaKonsertti k) throws SQLException {

		// Tarkistetaan onkoKonserttiJoTietokannassa -metodilla että onko konserttiolio
		// k jo tietokannassa
		// ja annetaan KonserttiJoLisattyException -poikkeus jos konsertti on jo
		// tietokannassa.
		if (!(onkoKonserttiJoTietokannassa(k))) {
			String sq1 = "INSERT INTO KONSERTTI VALUES (?,?,?,?,?,?);";

			try (PreparedStatement ps1 = c.prepareStatement(sq1)) {

				ps1.setString(1, k.annaEsiintyja());
				ps1.setString(2, k.annaPaivamaara());
				ps1.setInt(3, k.annaHinta());
				ps1.setInt(4, k.annaPaikkojenMaara());
				ps1.setString(5, k.annaKonserttisali());
				ps1.setInt(6, 1);
				ps1.execute();

			}
		}
	}

	/**
	 * Metodi lisää databaseen useamman Konsertti-olion ArrayListinä. Konserttilistan oliota ei lisätä
	 * jos se on jo tietokannassa.
	 * 
	 * Listassa voi olla myös IstumaKonsertteja Konsertti-viittaustyyppisesti.
	 * 
	 * 
	 * 
	 * @param konserttilista
	 */
	public void lisaaKonserttilista(ArrayList<Konsertti> konserttilista) throws SQLException {
		for (Konsertti ko : konserttilista) {
			try {
				if (!(onkoKonserttiJoTietokannassa(ko))) {
					if (ko instanceof IstumaKonsertti) {
						lisaaIstumaKonsertti((IstumaKonsertti) ko);
					} else {
						lisaaKonsertti(ko);
					}
				}
			} catch (SQLException s) {
				System.out.println("Tietokantaan ei saada yhteyttä");
			}
		}
	}

	/**
	 * Metodilla selvitetään, että onko konsertti jo tietokannassa.
	 * 
	 * @param k
	 * @return Totuusarvo siitä että onko konsertti jo tietokannassa
	 * @throws SQLException
	 */
	public boolean onkoKonserttiJoTietokannassa(Konsertti k) throws SQLException {
		// lasketaan niiden rivien lukumäärä (COUNT(*)) taulussa KONSERTTI, joiden
		// attribuutit vastaavat konserttiolion k attribuutteja,
		// uudelleennimetään COUNT(*) total:ksi AS-komennolla
		String sq1 = "SELECT COUNT(*) AS total FROM KONSERTTI WHERE esiintyja=? AND pvmaara=? ;";

		try (PreparedStatement ps1 = c.prepareStatement(sq1)) {
			ps1.setString(1, k.annaEsiintyja());
			ps1.setString(2, k.annaPaivamaara());
			ResultSet r = ps1.executeQuery();
			int summa = r.getInt("total");
			if (summa > 0) {
				return true;
			} else {
				return false;
			}

		}

	}

	
	
	
	
	
	
	
	
	
	/**
	 * Metodi joko lisää uuden konserttiolion varaukset tietokantaan, tai jos
	 * konsertin varaukset ovat jo taulussa VARAUKSET, päivittää konsertin
	 * varaustiedot
	 * 
	 * @param Konsertti k
	 * @throws SQLException
	 */

	public void lisaaKonsertinVaraukset(Konsertti k) throws SQLException {

		if (!(onkoKonsertinVarauksetJoTietokannassa(k))) {
			int[] Varaukset = k.annaVaraustilanne();

			String sq1 = "INSERT INTO VARAUS VALUES (?,?,?,?);";

			PreparedStatement ps1 = null;

			try {
				ps1 = c.prepareStatement(sq1);
				for (int i = 0; i < k.annaPaikkojenMaara(); i++) {
					int varausnumero = Varaukset[i];

					ps1.setInt(1, varausnumero);
					ps1.setString(2, k.annaEsiintyja());
					ps1.setString(3, k.annaPaivamaara());
					ps1.setInt(4, i);
					ps1.execute();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				ps1.close();

			}

		} else if (onkoKonsertinVarauksetJoTietokannassa(k)) {
			int[] Varaukset = k.annaVaraustilanne();

			String sq1 = "UPDATE VARAUS SET varausnumero=? WHERE konsertin_esiintyja=? AND pvmaara=? AND paikka=?;";

			PreparedStatement ps1 = null;

			try {
				ps1 = c.prepareStatement(sq1);
				for (int i = 0; i < k.annaPaikkojenMaara(); i++) {
					int varausnumero = Varaukset[i];

					ps1.setInt(1, varausnumero);
					ps1.setString(2, k.annaEsiintyja());
					ps1.setString(3, k.annaPaivamaara());
					ps1.setInt(4, i);
					ps1.execute();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				ps1.close();
			}

		}

	}

	public boolean onkoKonsertinVarauksetJoTietokannassa(Konsertti k) throws SQLException {
		String sq1 = "SELECT COUNT(*) AS total FROM VARAUS WHERE konsertin_esiintyja=? AND pvmaara=?;";

		try (PreparedStatement ps1 = c.prepareStatement(sq1)) {
			ps1.setString(1, k.annaEsiintyja());
			ps1.setString(2, k.annaPaivamaara());
			ResultSet r = ps1.executeQuery();
			int summa = r.getInt("total");
			if (summa > 0) {
				return true;
			} else {
				return false;
			}

		}
	}

	
	
	
	
	

	/**
	 * Metodi hakee konsertit tietokannan Konsertit-taulusta, luo niistä uudet
	 * konserttioliot ja tallentaa ne konserttiolio-ArrayListiin, joka palautetaan
	 * käyttäjälle.
	 * 
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Konsertti> haeKonsertitTietokannasta() throws SQLException {

		ArrayList<Konsertti> palautettava = new ArrayList<Konsertti>();

		String sq1 = "SELECT * FROM KONSERTTI";

		try (PreparedStatement ps = c.prepareStatement(sq1)) {
			ResultSet r = ps.executeQuery();
			// Tarkistetaan r.next()-lausekkella, onko ResultSet oliossa vielä rivejä
			// jälkellä eli että onko konserttiolioita jäljellä.
			while (r.next()) {
				String esiintyja = r.getString("esiintyja");
				String paivamaara = r.getString("pvmaara");
				int hinta = r.getInt("hinta");
				int paikkojenMaara = r.getInt("paikkojenMaara");
				String konserttisali = r.getString("konserttisali");
				int istumakonserttiko = r.getInt("istumakonserttiko");

				if (istumakonserttiko == 1) {
					Konsertti k = new IstumaKonsertti(esiintyja, paivamaara, hinta, paikkojenMaara, konserttisali);
					palautettava.add(k);
				} else if (istumakonserttiko == 0) {
					Konsertti k = new Konsertti(esiintyja, paivamaara, hinta, paikkojenMaara, konserttisali);
					palautettava.add(k);
				}
			}
		}
		return palautettava;

	}

	
	
	
	
	/**
	 * Metodilla haetaan tietokannan VARAUS-taulusta taulukkomuotoinen
	 * varaustilanne.
	 * 
	 * @param k
	 * @return
	 * @throws SQLException
	 */
	public int[] haeKonsertinVaraustilanne(Konsertti k) throws SQLException {
		ArrayList<Integer> tmp = new ArrayList<Integer>();
		int[] palautettava = new int[k.annaPaikkojenMaara()];

		String sq1 = "SELECT * FROM VARAUS WHERE konsertin_esiintyja=? AND pvmaara=?";

		try (PreparedStatement ps = c.prepareStatement(sq1)) {
			ps.setString(1, k.annaEsiintyja());
			ps.setString(2, k.annaPaivamaara());
			ResultSet r = ps.executeQuery();
			// Tarkistetaan r.next()-lausekkella, onko ResultSet oliossa vielä rivejä
			// jälkellä eli että onko konsertin varauksia jäljellä.
			while (r.next()) {
				int varausnumero = r.getInt("varausnumero");
				tmp.add(varausnumero);
			}
		}

		// siirretään ArrayListin luvut Arrays:iin
		for (int i = 0; i < k.annaPaikkojenMaara(); i++) {
			palautettava[i] = tmp.get(i);
		}
		return palautettava;

	}

	/**
	 * Metodi tulostaa kaikki tietokantaan tallennetut konserttien ja
	 * istumakonserttien tiedot lukuun ottamatta varaustilannetta.
	 * 
	 * @throws SQLException
	 */
	public void tulostaKonsertit() throws SQLException {
		String sq1 = "SELECT * FROM KONSERTTI";

		try (PreparedStatement ps = c.prepareStatement(sq1)) {
			int indeksi = 0;
			ResultSet r = ps.executeQuery();
			// Tarkistetaan r.next()-lausekkella, onko ResultSet oliossa vielä rivejä
			// jälkellä eli että onko konserttiolioita jäljellä
			while (r.next()) {
				
				String esiintyja = r.getString("esiintyja");
				String paivamaara = r.getString("pvmaara");
				int hinta = r.getInt("hinta");
				int paikkojenMaara = r.getInt("paikkojenMaara");
				String konserttisali = r.getString("konserttisali");
				int istumakonserttiko = r.getInt("istumakonserttiko");
				String istumako = "";
				if (istumakonserttiko == 1) {
					istumako = istumako + "istumakonsertti";
				} else if (istumakonserttiko == 0) {
					istumako = istumako + "tavallinen konsertti";
				}

				System.out.println(indeksi + ": " + "Konsertin esiintyjä: " + esiintyja + ", päivämäärä: " + paivamaara + ", hinta: "
						+ hinta + ", paikkojen määrä: " + paikkojenMaara + ", konserttisali: " + konserttisali
						+ ",konsertin tyyppi: " + istumako);
				
				indeksi += 1;

			}
		}

	}

	/**
	 * Metodi lisää ja päivittää tietokantaan asiakkaan uudet varaukset ja muut
	 * asiakkaaseen liittyvät tiedot.
	 * 
	 * 
	 * @param a
	 * @throws SQLException
	 */
	public void lisaaAsiakkaanVaraukset(Asiakas a) throws SQLException {
		ArrayList<Integer> Varaukset = a.annaVaraukset();

		String sq1 = "INSERT INTO ASIAKKAAN_VARAUKSET VALUES (?,?);";

		PreparedStatement ps1 = null;

		try {
			ps1 = c.prepareStatement(sq1);
			for (int i = 0; i < Varaukset.size(); i++) {
				// Tarkistetaan onkoVarausJoTietokannassa -metodilla onko kyseinen varaus
				// jo tietokannassa: jos on, jatketaan for-loopin seuraavaan sykliin.
				if (onkoVarausJoTietokannassa(Varaukset.get(i))) {
					continue;
				} else {
					String nimi = a.annaNimi();
					int varausnumero = a.annaVaraus(i);

					ps1.setString(1, nimi);
					ps1.setInt(2, varausnumero);
					ps1.execute();
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			ps1.close();

		}
	}

	/**
	 * Metodi tarkistaa, että onko kyseinen varausnumero jo tietokannan
	 * ASIAKKAN_VARAUKSET-taulussa.
	 * 
	 * @param varausnumero
	 * @return
	 * @throws SQLException
	 */

	public boolean onkoVarausJoTietokannassa(int varausnumero) throws SQLException {
		String sq1 = "SELECT COUNT(*) AS total FROM ASIAKKAAN_VARAUKSET WHERE varausnumero=? ;";

		try (PreparedStatement ps1 = c.prepareStatement(sq1)) {
			ps1.setInt(1, varausnumero);
			ResultSet r = ps1.executeQuery();
			int summa = r.getInt("total");
			if (summa > 0) {
				return true;
			} else {
				return false;
			}

		}
	}

	/**
	 * Tulostaa asiakkaan varauksien varausnumerot, ehkä turha metodi, jos tämä
	 * halutaan toetuttaa paremmin main-metodissa.
	 * 
	 * @param a
	 * @throws SQLException
	 */

	public void tulostaAsiakkaanVaraukset(Asiakas a) throws SQLException {

		String sq1 = "SELECT * FROM ASIAKKAAN_VARAUKSET WHERE nimi=? ;";

		try (PreparedStatement ps = c.prepareStatement(sq1)) {
			ps.setString(1, a.annaNimi());
			ResultSet r = ps.executeQuery();
			// Tarkistetaan r.next()-lausekkella, onko ResultSet oliossa vielä rivejä
			// jälkellä eli että onko konserttiolioita jäljellä
			while (r.next()) {
				String nimi = r.getString("nimi");
				int varausnumero = r.getInt("varausnumero");
				System.out.println("Asiakas; " + nimi + ", varaus: " + varausnumero);

			}
		}
	}

	/**
	 * Hakee tietokannasta asiakkaan varaukset ja palauttaa ne ArrayListina.
	 * 
	 * @param a
	 * @return
	 * @throws SQLException
	 */

	public ArrayList<Integer> haeAsiakkaanVaraukset(Asiakas a) throws SQLException {

		ArrayList<Integer> palautettava = new ArrayList<Integer>();

		String sq1 = "SELECT * FROM ASIAKKAAN_VARAUKSET WHERE nimi=? ;";

		try (PreparedStatement ps = c.prepareStatement(sq1)) {
			ps.setString(1, a.annaNimi());
			ResultSet r = ps.executeQuery();
			// Tarkistetaan r.next()-lausekkella, onko ResultSet oliossa vielä rivejä
			// jälkellä eli että onko konserttiolioita jäljellä
			while (r.next()) {
				int varausnumero = r.getInt("varausnumero");
				palautettava.add(varausnumero);
			}
			return palautettava;
		}

	}
	
	/**
	 * Hakee tietokannasta asiakkaan nimet ja palauttaa ne ArrayListina.
	 * 
	 * @param a
	 * @return
	 * @throws SQLException
	 */

	public ArrayList<String> haeAsiakkaat() throws SQLException {

		ArrayList<String> palautettava = new ArrayList<String>();

		String sq1 = "SELECT DISTINCT nimi FROM ASIAKKAAN_VARAUKSET;";

		try (PreparedStatement ps = c.prepareStatement(sq1)) {
			ResultSet r = ps.executeQuery();
			// Tarkistetaan r.next()-lausekkella, onko ResultSet oliossa vielä rivejä
			// jälkellä eli että onko konserttiolioita jäljellä
			while (r.next()) {
				String nimi = r.getString("nimi");
				palautettava.add(nimi);
			}
			return palautettava;
		}

	}
	
	

	
	
	
	
	
	
	
	public boolean onkoVarausnumeroJoTietokannassa(int varausnumero) throws SQLException {
		

		String sq1 = "SELECT * FROM ASIAKKAAN_VARAUKSET WHERE varausnumero=? ;";

		try (PreparedStatement ps1 = c.prepareStatement(sq1)) {
			ps1.setInt(1, varausnumero);
			ResultSet r = ps1.executeQuery();
			int summa = r.getInt("total");
			if (summa > 0) {
				return true;
			} else {
				return false;
			}

		}
	}
		


	/*
	 * Luodaan uusi sisäluokka KonserttiJoLisattyException, jota käytetään
	 * lisaaKonsertti -metodissa antamaan poikkeus, jossa ilmoitetaan, että
	 * konsertti on jo lisäty tietokantaan.
	 * 
	 */
	public class KonserttiJoLisattyException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public KonserttiJoLisattyException(String v) {
			super(v);
		}
	}

}