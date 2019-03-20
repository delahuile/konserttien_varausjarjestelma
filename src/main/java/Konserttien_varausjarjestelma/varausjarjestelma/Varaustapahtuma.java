package Konserttien_varausjarjestelma.varausjarjestelma;


import java.util.ArrayList;

import Konserttien_varausjarjestelma.varausjarjestelma.IstumaKonsertti.PaikkaVarattuException;
import Konserttien_varausjarjestelma.varausjarjestelma.Konsertti.KonserttiTaynnaException;

import java.io.IOException;
import java.sql.SQLException;

public class Varaustapahtuma {


	

	public static void main(String[] args) {

		// Luodaan uusi apumetodiolio, jolla voidaan lukea käyttäjän syötteitä eli
		// kokonaislukuja ja merkkijonoja.
		Apumetodit apu = new Apumetodit();

		/*
		 * Luodaan uusi varausjärjestelmäolio
		 */
		Varausjärjestelmä vj = null;

		try {
			vj = new Varausjärjestelmä();
		} catch (SQLException e) {
			System.out.println("Tietokantaan ei saada yhteyttä!");
		}

		
		//Tässä tuodaan ohjelman alussa konsertit ja asiakkaat varauksineen tietokannasta. 
		ArrayList<Konsertti> konserttilista= tuoKonserttiLista();
		
		ArrayList<Asiakas> asiakaslista = tuoAsiakasLista();
		
		
		
		
	
		while (true) {
		/*
		 * Kysytään, mitä käyttäjä haluaa seuraavaksi tehdä:
		 */
		System.out.println("Mitä haluat tehdä seuraavaksi? Syötä sen toiminnon numero jota haluat käyttää");
		System.out.println("1 - Lisää uusi konsertti varausjärjestelmään");
		System.out.println("2 - Tee lippuvarauksia");
		System.out.println("3 - tarkastele olemassaolevia varauksia ja konsertteja");
		System.out.println("4 - lopeta ohjelma");

		// luetaan vastaus ja siirretään merkkijonon mukaiseen menuun
		try {
			int vastaus = apu.lueKokonaisluku();
			if (vastaus < 1 || vastaus > 4) {
				System.out.println("Syötä luku 1, 2, 3 tai 4!");
//					continue;
			}
			if (vastaus == 1) {
				konserttilista.add(konsertinLisaysMenu(vj, apu));
			}
			if (vastaus == 2) {
				asiakaslista.add(varausMenu(vj, apu, konserttilista));
			}

			if (vastaus == 3)
				tilanneMenu(vj, apu);
			
			if (vastaus == 4)
				break;

		} catch (Exception e) {
			System.out.println("Anna kokonaisluku.");
	            continue;
		}
		}

	}

	private static Konsertti konsertinLisaysMenu(Varausjärjestelmä vj, Apumetodit apu)
			throws SQLException, IOException {
		// TODO Täytyy myös pystyä lisäämään IstumaKonsertteja
		// Kysytään konsertin tiedot
		System.out.println("Syötä konsertin esiintyjä");
		String esiintyja = apu.lueMerkkijono();
		System.out.println("Syötä konsertin päivämäärä: ");
		String paivamaara = apu.lueMerkkijono();
		System.out.println("Syötä konsertin hinta");
		int hinta = apu.lueKokonaisluku();
		System.out.println("Syötä konsertin paikkamäärä");
		int paikkojenMaara = apu.lueKokonaisluku();
		System.out.println("Syötä konserttisalin nimi");
		String konserttisali = apu.lueMerkkijono();
		System.out.println("Onko konsertti istumakonsertti vai ei? Vastaa 1 jos on, 0 jos ei");
		int istumakeikka = apu.lueKokonaisluku();

		// Luodaan tiedoista Konsertti-olio (istuma- tai tavallinen) ja palautetaan
		// Konsertti-tyyppisenä
		// Lisätään tiedot myös tietokantaan
		if (istumakeikka == 1) {
			IstumaKonsertti k1 = new IstumaKonsertti(esiintyja, paivamaara, hinta, paikkojenMaara, konserttisali);
			vj.lisaaIstumaKonsertti(k1);
			vj.lisaaKonsertinVaraukset(k1);
			return k1;
		} else {
			Konsertti k1 = new Konsertti(esiintyja, paivamaara, hinta, paikkojenMaara, konserttisali);
			vj.lisaaKonsertti(k1);
			vj.lisaaKonsertinVaraukset(k1);
			return k1;
		}

	}

	private static Asiakas varausMenu(Varausjärjestelmä vj, Apumetodit apu, ArrayList<Konsertti> konserttilista) throws SQLException, IOException {
		
		//TODO myös testaus siitä, onko uusi vai vanha asiakas - edit 10.3. klo 12:45 tämä taitaa siis jo toimia?
		
		// TODO palauttaa asiakkaan - näin ei tarvitsisi tehdä jos asiakas tallennetaan listalle täällä?
		System.out.println("Kirjoita asiakkaan nimi");
		String nimi = apu.lueMerkkijono();
		
		Asiakas asiakas = null;
		boolean nimiJoListassa = false;
		
		if(nimiJoLisätty(nimi)) {
			nimiJoListassa = true;
		} else if (!nimiJoLisätty(nimi)) {
			nimiJoListassa = false;
		}
		
		if (nimiJoListassa) {
			asiakas = tuoAsiakas(nimi);
		} else if (!nimiJoListassa) {
			asiakas = new Asiakas(nimi);
		}
		
		ArrayList<Integer> varaukset = new ArrayList<Integer>(); 
		
		//TODO allaolevan metodin tarkoitus luoda uusi asiakas ja lisätä se tietokantaan+asiakaslistaan. Lisäksi ko asiakkaalle tulee syöttää
		//varausnumero myöhemmin tässä metodissa... miten ikinä se tehdäänkään... Ehkä asiakaslistaan lisäys pitää tehdä tästä metodista?
		
		//Jos luodaan uusi asiakas, niin oikeastaan ainut tapa lisätä asiakas tietokantaan+asiakaslistaan on tehdä asiakkaalle
		//aluksi tässä metodissa varaukset valmiiksi ja sen jälkeen returnilla palauttaa valmis asiakasolio asiakaslistaan
		//ja ennen return-Asiakas -lausetta ajaa vj.lisaaAsiakas ja vj.lisaaAsiakkaanVaraukset.
		
		// TODO Äh, ehkä sittenkin pitäisi tehdä tähän eri metodit uudelle ja vanhalle asiakkaalle... Nyt ongelmana myös asiakkaan palautus - jos 
		// vanha asiakas, niin tulee kaiketi tupla-asiakas kun tästä metodista palautetaan jo olemassaoleva asiakas asiakaslistaan...
		
		
		System.out.println("Mihin konserttiin haluat varata paikan?");
		vj.tulostaKonsertit();
			
		System.out.println("");
		System.out.println("Ilmoita haluamasi konsertin numero");
		int varattavaKonsertti = apu.lueKokonaisluku();
			
			
		// Jos konsertti on istumakonsertti...
		//TODO Tätä ei ole testattu - en tiedä tunnistaako varmasti istumakonsertit?
		while (true) {
			if (konserttilista.get(varattavaKonsertti) instanceof IstumaKonsertti) {
					
				System.out.println("Konsertti on istumakonsertti. Ilmoita haluamasi paikkanumero seuraavista paikoista:");
				IstumaKonsertti tmp = (IstumaKonsertti) konserttilista.get(varattavaKonsertti);
				tmp.tulostaVapaatPaikat();
					
				int paikka = apu.lueKokonaisluku();
					
				// Testataan, onko paikka vapaana vai ei. Jos on, tehdään varaus. Jos ei, ilmoitetaan että paikka varattu ja mennään loopin alkuun.
				try {
					
					int varausnumero = asiakas.lisaaVaraus();
					tmp.teePaikkavaraus(varausnumero, paikka);
					varaukset.add(varausnumero);

					// Lisäykset databaseen:
					vj.lisaaKonsertinVaraukset(konserttilista.get(varattavaKonsertti));
					vj.lisaaAsiakkaanVaraukset(asiakas);
					
					return asiakas;
					
				} catch (PaikkaVarattuException e) {
					System.out.println("Paikka on jo varattu!");
					continue;
				}catch (KonserttiTaynnaException e) {
					System.out.println("Konsertti on täynnä");
					return asiakas;
				}
				
			// Jos kyseessä ei ole istumakonsertti:
			}else {
				// Asiakas.luokan lisaaVaraus siis lisää varauksen Asiakas-oliolle ja palauttaa generoimansa varausnumeron.
				// Varausnumero tallennetaan siis myös Konsertti-olion varauksiin allaolevalla rivillä.

				try {
					konserttilista.get(varattavaKonsertti).teeVaraus(asiakas.lisaaVaraus());
				} catch (KonserttiTaynnaException e) {
					System.out.println("Konsertti on täynnä");
					return asiakas;
				}
				
				// Lisäys databaseen
				vj.lisaaKonsertinVaraukset(konserttilista.get(varattavaKonsertti));
				vj.lisaaAsiakkaanVaraukset(asiakas);
				return asiakas;
				
				
				
			}	
		}

		}
	
	

	private static void tilanneMenu(Varausjärjestelmä vj, Apumetodit apu) {
		while (true) {



			System.out.println("Haluatko tulostaa ruudulle järjestelmään tallennetut konsertit vai omat varauksesi?");
			System.out.println(
					"Kirjoita 1, jos haluat tarkastella konsertteja ja 2, jos haluat tarkastella varauksiasi tai luku 3 jos haluat keskeyttää konserttien ja varausten tarkastelun.");

			try {
				int vastaus = apu.lueKokonaisluku();
				if (vastaus < 1 || vastaus > 3) {
					System.out.println("Syötä luku 1 tai 2 tai 3 ");
					continue;
				}
				if (vastaus == 1) {
					System.out.println("Saatavilla olevat konsertit ja niiden varaustilanne on:");

					for (int i = 0; i < tuoKonserttiLista().size(); i++) {

						String tyyppi = "";
						if (tuoKonserttiLista().get(i) instanceof IstumaKonsertti) {
							tyyppi = tyyppi + "Istumakonsertti";
						} else {
							tyyppi = tyyppi + "Tavallinen konsertti";
						}

						String täynnä = "";
						if (tuoKonserttiLista().get(i).onkoTaynna()) {
							täynnä = täynnä + "Kyllä";
						} else if (!tuoKonserttiLista().get(i).onkoTaynna()) {
							täynnä = täynnä + "Ei";
						}
						System.out.println(i + ": " + "Konsertin esiintyjä: " + tuoKonserttiLista().get(i).annaEsiintyja()
								+ ", päivämäärä: " + tuoKonserttiLista().get(i).annaPaivamaara() + ", hinta: "
								+ tuoKonserttiLista().get(i).annaHinta() + ", paikkojen määrä:"
								+ tuoKonserttiLista().get(i).annaPaikkojenMaara() + ", onko konsertti täynnä: " + täynnä
								+ ", konserttisali: " + tuoKonserttiLista().get(i).annaKonserttisali() + ",konsertin tyyppi: "
								+ tyyppi);

					}
				}
				if (vastaus == 2) {
					try {
						
						ArrayList<Konsertti> konsertit = tuoKonserttiLista();
						ArrayList<Asiakas> asiakkaat = tuoAsiakasLista();
						
						
						System.out.println("Järjestelmään tallennetut asiakkaat ovat:");
						
						for (int i = 0; i <asiakkaat.size(); i++) {
							System.out.println(asiakkaat.get(i).annaNimi());
						}
						
						
						System.out.println("Anna tarkasteltavan asiakkaan nimi:");
						
						String vastaus3 = apu.lueMerkkijono();
								
						

						Asiakas a = null;

						for (int i = 0; i < asiakkaat.size(); i++) {
							if ((asiakkaat.get(i).annaNimi()).equals(vastaus3)) {
								a = asiakkaat.get(i);
								break;
							}
						}

						ArrayList<Integer> asiakkaanVaraukset = a.annaVaraukset();

						
						ArrayList<String> konsertinEsiintyjä = new ArrayList<String>();
						ArrayList<String> konsertinPäivämäärä = new ArrayList<String>();
						ArrayList<Integer> konsertinVarausnumero = new ArrayList<Integer>();
						ArrayList<Integer> konsertinPaikkanumero = new ArrayList<Integer>();
						
						
						ArrayList<Integer> kaikkiVarausnumerot = new ArrayList<Integer>();
						ArrayList<String> kaikkiKonsertinEsiintyjät = new ArrayList<String>();
						ArrayList<String> kaikkiKonsertinPäivämäärät = new ArrayList<String>();
						
						

						
						
						for (int i = 0; i<konsertit.size(); i++) {
							for (int j = 0; j < konsertit.get(i).annaVaraustilanne().length; j++) {
								kaikkiVarausnumerot.add(konsertit.get(i).annaVaraustilanne()[j]);
								kaikkiKonsertinEsiintyjät.add(konsertit.get(i).annaEsiintyja());
								kaikkiKonsertinPäivämäärät.add(konsertit.get(i).annaPaivamaara());
							}
						}
						
						for (int j = 0; j<kaikkiVarausnumerot.size(); j++) {
							for(int i = 0; i<asiakkaanVaraukset.size(); i++) {
								if (kaikkiVarausnumerot.get(j).equals(asiakkaanVaraukset.get(i))) {
									konsertinEsiintyjä.add(kaikkiKonsertinEsiintyjät.get(j));
									konsertinPäivämäärä.add(kaikkiKonsertinPäivämäärät.get(j));
									konsertinVarausnumero.add(kaikkiVarausnumerot.get(j));
									konsertinPaikkanumero.add(j);
						
		
								}
							}
						}
						
						
						try {
							int paikkanumero = 0;
							for (int i = 0; i < konsertinVarausnumero.size(); i++) {
								for (int j = 0; j < konsertit.size(); j++) {
									for (int k = 0; k < konsertit.get(j).annaVaraustilanne().length; k++) {
										if (konsertit.get(j).annaVaraustilanne()[k]==(konsertinVarausnumero.get(i))) {
											paikkanumero = k;
										}
									}
								}
								System.out.println("Varausnumero: " + konsertinVarausnumero.get(i) + ", paikkanumero: " + paikkanumero + ", konsertin esiintyjä: "
										+ konsertinEsiintyjä.get(i) + ", konsertin päivämäärä: " + konsertinPäivämäärä.get(i));
							}
						} catch (Exception n) {
							System.out.println("Ei onnistunut!");
						}
						
					} catch (Exception e) {
						System.out.println("Nimeä ei ole tietokannassa!");
					}
					
				}
				if (vastaus == 3) {
					break;
				}

			} catch (Exception e) {
			
				break;
			}
		}

	}

	
	private static Asiakas tuoAsiakas(String nimi) {
		Asiakas palautettava = null;
		for (int i = 0; i < tuoAsiakasLista().size(); i++) {
			if (tuoAsiakasLista().get(i).annaNimi().equals(nimi)) {
				palautettava = tuoAsiakasLista().get(i);
			}
		}
		return palautettava;
	}
	
	private static boolean nimiJoLisätty(String nimi) {
		
		int laskuri = 0;
		boolean totuusarvo = false;
		for (int i = 0; i < tuoAsiakasLista().size(); i++) {
			if (tuoAsiakasLista().get(i).annaNimi().equals(nimi)) {
				laskuri +=1;
			}
		}
		if (laskuri > 0) {
			totuusarvo = true;
		} else if (laskuri == 0) {
			totuusarvo = false;
		}
		return totuusarvo;
	}
	
	/*
	 * Metodissa haetaan tietokantaan tallennetut konsertit ja istumakonsertit
	 * sekä niiden varaukset ja tallennetaan.
	 * 
	 * HUOM! Metodi toimii ainoastaan siinä tapauksessa että tietokantatiedoston kaikki konsertit
	 * löytyvät (mahdollisesti puuttuvine) varaustietoineen VARAUKSET-taulusta!
	 * 
	 * @return ArrayList<Konsertti><
	 */
	public static ArrayList<Konsertti> tuoKonserttiLista(){
		
		Varausjärjestelmä vj = null;

		try {
			vj = new Varausjärjestelmä();
		} catch (SQLException e) {
			System.out.println("Tietokantaan ei saada yhteyttä!");
		}

		ArrayList<Konsertti> konserttilista = null;

		// asetetaan kaikille konserttilistan olioille oikeat varaustilanteet hakemalla
		// ne tietokannasta.

		try {
			konserttilista = vj.haeKonsertitTietokannasta();
			for (int i = 0; i < konserttilista.size(); i++) {
				int[] varaustilanne = vj.haeKonsertinVaraustilanne(konserttilista.get(i));
				(konserttilista.get(i)).asetaVaraustilanne(varaustilanne);
			}
		} catch (SQLException e) {
			System.out.println("Tietokantaan ei saada yhteyttä!");
		}
		return konserttilista;
	}
	
	/*
	 * Metodi hakee tietokantaan tallennetut asiakkaat ja näiden varaukset ja palauttaa
	 * Asiakasoliot ArrayListina.
	 * @return ArrayList<Asiakas>
	 */
	public static ArrayList<Asiakas> tuoAsiakasLista(){
		
		Varausjärjestelmä vj = null;

		try {
			vj = new Varausjärjestelmä();
		} catch (SQLException e) {
			System.out.println("Tietokantaan ei saada yhteyttä!");
		}
		
		ArrayList<String> nimilista = null;
		ArrayList<Asiakas> asiakaslista = null;
		try {
			nimilista = vj.haeAsiakkaat();
			asiakaslista = new ArrayList<Asiakas>();
			for (int i = 0; i < nimilista.size(); i++) {
				asiakaslista.add(new Asiakas(nimilista.get(i)));
				ArrayList<Integer> varaukset = vj.haeAsiakkaanVaraukset(asiakaslista.get(i));
				asiakaslista.get(i).asetaVaraukset(varaukset);

			}

		} catch (SQLException e) {
			System.out.println("Tietokantaan ei saada yhteyttä!");
		}
		
		return asiakaslista;
		
	}

}
