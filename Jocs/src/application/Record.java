package application;

public class Record {
    private final String nom;
    private final String nivell;
    private final int temps;
    

    public Record(String nom, String nivell, int temps) {
        this.nom =  nom;
        this.nivell = nivell;
        this.temps =temps;
    }
    public String getNom() {
		return nom;
	}
    public String getNivell() {
		return nivell;
	}
	public int getTemps() {
		return temps;
	}

   
}

