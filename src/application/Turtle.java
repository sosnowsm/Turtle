package application;

import javafx.scene.image.Image;

public class Turtle {
	private Image img;
	private int wspX;
	private int wspY;
	private int kat;
	private boolean czyOpuszczony;
	
	public Turtle(Image i, int x, int y, int k, boolean o){
		img=i;
		wspX=x;
		wspY=y;
		kat=k;
		czyOpuszczony=o;
	}

	public int getWspX() {
		return wspX;
	}

	public void setWspX(int wspX) {
		this.wspX = wspX;
	}

	public int getWspY() {
		return wspY;
	}

	public void setWspY(int wspY) {
		this.wspY = wspY;
	}

	public int getKat() {
		return kat;
	}

	public void setKat(int kat) {
		this.kat = kat;
	}

	public boolean isCzyOpuszczony() {
		return czyOpuszczony;
	}

	public void setCzyOpuszczony(boolean czyOpuszczony) {
		this.czyOpuszczony = czyOpuszczony;
	}

	public Image getImg() {
		return img;
	}

	public void setImg(Image img) {
		this.img = img;
	}

}
