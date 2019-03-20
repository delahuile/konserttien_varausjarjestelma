package Konserttien_varausjarjestelma.varausjarjestelma;


public class IstumaKonsertti extends Konsertti {
	
	public IstumaKonsertti(String esiintyja, String paivamaara, int hinta, int paikkojenMaara, String konserttisali) {
		super(esiintyja,paivamaara, hinta, paikkojenMaara, konserttisali);
		this.varaustilanne = new int[paikkojenMaara]; 
	}
	
	/**
	 * Paikkavaraus-metodille annetaan varausnumero sekä varattava paikka.
	 * 
	 * 
	 * @param paikka varattava paikka 
	 * @param varausnumero varausnumero, jolle varaus tehdään
	 */	 
	public void teePaikkavaraus(int varausnumero, int paikka) throws PaikkaVarattuException, KonserttiTaynnaException {
		if(onkoTaynna()==true) {
			throw new KonserttiTaynnaException("Konsertti on täynnä!");
		}
		if(varaustilanne[paikka]>0) {
			throw new PaikkaVarattuException("Paikka on varattu!");
		}
		if(varaustilanne[paikka]==0) {
			varaustilanne[paikka] = varausnumero;
		} 	 

	}
	/**Metodi kertoo, mikä konsertin paikkanumero yksittäisellä varauksella on
	 * 
	 * @param varausnumero
	 * @return varausnumeroa vastaava paikkanumero (eli paikat-listan indeksi)
	 */ 
	
	public int annaVarauksenPaikka(int varausnumero) {
		int paikkanro = 0;
		for(int i=0; i<annaPaikkojenMaara(); i++) {
			if(varausnumero==varaustilanne[i]) {
				paikkanro = i;
			}
		}
		return paikkanro;
	} 

	
	public class PaikkaVarattuException extends Exception {
		public PaikkaVarattuException(String v) {
			super(v);
		}
	}
	
}


