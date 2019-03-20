package Konserttien_varausjarjestelma.varausjarjestelma;



import java.io.IOException;
import java.util.Scanner;

public class Apumetodit {

	private Scanner scanner;
	private Scanner scanner2;

	public Apumetodit() {
	}

	public String lueMerkkijono() {
		scanner = new Scanner(System.in);
		return scanner.nextLine();
	}

	public int lueKokonaisluku() throws IOException {
		scanner2 = new Scanner(System.in);
		return scanner2.nextInt();
	}
	
	

}
