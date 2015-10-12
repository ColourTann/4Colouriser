import java.awt.Color;

public class PotentialColour {
	int r;
	int g;
	int b;
	int frequency;
	public PotentialColour(int r, int g, int b, int frequency) {
		if(r<0)r+=256;
		if(g<0)g+=256;
		if(b<0)b+=256;
		this.r=r;
		this.g=g;
		this.b=b;
		
		this.frequency=frequency;
	}
	public String toString(){
		return "Red: "+r+", Green: "+g+", Blue: "+b;
	}
	public String toRGB(){
		
		String red = Integer.toHexString(r);
		while(red.length()<2)red="0"+red;
		String green = Integer.toHexString(g);
		while(green.length()<2)green="0"+green;
		String blue = Integer.toHexString(b);
		while(blue.length()<2)blue="0"+blue;
		
		return "0x"+red+green+blue;
	}
	public int getSim(int r, int g, int b) {
		int rDiff=r-this.r;
		int gDiff=g-this.g;
		int bDiff=b-this.b;
		
		double power =1.5;
		
		rDiff=Math.abs(rDiff);
		gDiff=Math.abs(gDiff);
		bDiff=Math.abs(bDiff);
		
		int total=(int) (Math.pow(rDiff, power)+Math.pow(gDiff, power)+Math.pow(bDiff, power));
		if(ImageAnalyser.wordy){
		System.out.println(r+":"+g+":"+b+"compared to "+this.r+":"+this.g+":"+this.b);
		System.out.println(total);
		}
		return total;
	}
	Color col;
	public Color getColor() {
		if(col==null) col = new Color(r, g, b);
		return col;
	}
}

