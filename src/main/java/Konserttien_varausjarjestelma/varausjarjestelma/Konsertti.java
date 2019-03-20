package Konserttien_varausjarjestelma.varausjarjestelma;


public class Konsertti {
	protected final String esiintyja;
	protected final String paivamaara;
	protected int hinta;
	protected int paikkojenMaara;
	protected String konserttisali;
	protected int[] varaustilanne; 
	
	
	public Konsertti(String esiintyja, String paivamaara, int hinta, int paikkojenMaara, String konserttisali) {
		this.esiintyja = esiintyja;
		this.paivamaara = paivamaara;
		this.hinta = hinta;
		this.paikkojenMaara = paikkojenMaara;
		this.konserttisali = konserttisali;
		this.varaustilanne = new int[paikkojenMaara];
	}

	/**
	 * Metodi antaa listan, johon kirjattu IstumaKonsertti-olion paikat ja kuhunkin paikkaan liittyvä varausnro
	 * 
	 * @return int-lista, jossa paikat ja niiden varausnumerot (0=vapaa paikka)
	 */
	public int[] annaVaraustilanne(){
		return varaustilanne;
	}
	
	public void asetaVaraustilanne(int[] varaustilanne){
		this.varaustilanne = varaustilanne;
	}

	public String annaEsiintyja() {
		return esiintyja;
	}

	public String annaPaivamaara() {
		return paivamaara;
	}

	public int annaHinta() {
		return hinta;
	}

	public void asetaHinta(int hinta) {
		this.hinta = hinta;
	}

	public int annaPaikkojenMaara() {
		return paikkojenMaara;
	}

	public void asetaPaikkojenMaara(int paikkojenMaara) {
		this.paikkojenMaara = paikkojenMaara;
	}

	public String annaKonserttisali() {
		return konserttisali;
	}

	public void asetaKonserttisali(String konserttisali) {
		this.konserttisali = konserttisali; 
	}
	
	/**
	 * Metodi, jolla saa selville onko konsertti täynnä vai ei
	 * 
	 * @return palauttaa true, jos konsertti on täynnä 
	 */ 
	public boolean onkoTaynna() {
		int mittari=0;
		for(int i=0; i < annaPaikkojenMaara(); i++) {
			if(varaustilanne[i]>0) {
				mittari++;
			} 
		}
		if(mittari==annaPaikkojenMaara()) {
			return true;
		} else {
			return false;
		}
		}
	
	/**
	 * Metodi tulostaa konsertin vapaiden paikkojen numerot (eli varaustilanne[]-indeksit vapailta paikoilta)
	 */
	public void tulostaVapaatPaikat() {
		for(int i=0; i<paikkojenMaara; i++) {
			if(varaustilanne[i]==0) {
				System.out.println(i); 
			}
		}
	}
	
	/**
	 * Metodi, joka tekee paikkavarauksen ja pitää kirjaa konsertin varaustilanteesta. 
	 * Parametrina varausnumero, jonka metodi lisää int[] varaustilanne-listaan 
	 * seuraavaan vapaaseen paikkaan.
	 * 
	 * @param varausnumero 
	 * @throws KonserttiTaynnaException
	 */
	public void teeVaraus(int varausnumero) throws KonserttiTaynnaException {
		if(onkoTaynna()==true) {
			throw new KonserttiTaynnaException("Konsertti on täynnä!");
		}
		for(int i=0; i < annaPaikkojenMaara(); i++) {
			if(varaustilanne[i] == 0) {
				varaustilanne[i] = varausnumero;
				break;
			} 
		}
	}
	
	public class KonserttiTaynnaException extends Exception {
		public KonserttiTaynnaException(String v) {
			super(v);
		}
	}

}
