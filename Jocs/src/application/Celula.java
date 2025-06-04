package application;

public class Celula {
	
	public static int contadorId = 0;
	
	public int id;
	
	public boolean viva;
	
	public boolean zombie;
	
	public String estado;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isViva() {
		return viva;
	}

	public void setViva(boolean viva) {
		this.viva = viva;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public boolean isZombie() {
		return zombie;
	}
	
	public void setZombie(boolean zombie) {
		this.zombie = zombie;
	}

	public Celula(int id, boolean viva, String estado) {
		super();
		this.id = contadorId++;
		this.viva = viva;
		this.estado = estado;
		
	}
	
}
