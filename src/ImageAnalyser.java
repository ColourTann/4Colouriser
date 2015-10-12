import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ImageAnalyser {
	static PotentialColour[] bestValues = new PotentialColour[4];
	public static final boolean wordy = false;
	public static final boolean showColours = false;
	static byte[] pixels;
	public static void main (String... args){
		BufferedImage img= null;
		try {
			img = ImageIO.read(new File("D:/Code/Eclipse/image stuff/field.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		JFrame frame = new JFrame("fuckswingforever");
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(img.getWidth()*2+20, img.getHeight()+60);
		frame.setLocation(100, 100);
		ImageContainer icon = new ImageContainer(img);
		frame.getContentPane().add(icon);
		frame.setVisible(true);
		pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
		int[][][] allColours = new int[256][256][256];
		for(int i=0;i<pixels.length;i+=3){

			int r = pixels[i+2];
			int g = pixels[i+1];
			int b = pixels[i+0];
			if(r<0)r+=256;
			if(g<0)g+=256;
			if(b<0)b+=256;

			int dist=6;
			int minDist=-dist/2;
			int maxDist=dist/2;
			for(int x=minDist;x<=maxDist;x++){
				for(int y=-dist;y<=maxDist;y++){
					for(int z=-dist;z<=maxDist;z++){

						int totalDistance = Math.abs(x)+Math.abs(y)+Math.abs(z);

						int redIndex = r+x;
						int greenIndex = g+y;
						int blueIndex = b+z;
						if(redIndex<0||redIndex>255||greenIndex<0||greenIndex>255||blueIndex<0||blueIndex>255)continue;
						//						allColours[redIndex][greenIndex][blueIndex]+=(dist/2*3)-totalDistance;
						allColours[redIndex][greenIndex][blueIndex]+=1;
					}	
				}
			}						
		}


		for(int i=0;i<bestValues.length;i++){
			for(int r=0;r<255;r++){
				for(int g=0;g<255;g++){
					for(int b=0;b<255;b++){
						int num = allColours[r][g][b];
						if(bestValues[i]==null||num>bestValues[i].frequency){
							bestValues[i]=new PotentialColour(r, g, b, num);
						}
					}	
				}	
			}
			PotentialColour best = bestValues[i];
			int distanceToDecrement=70;
			float divider = distanceToDecrement*300;
			for(int x=-distanceToDecrement;x<=distanceToDecrement;x++){
				for(int y=-distanceToDecrement;y<=distanceToDecrement;y++){
					for(int z=-distanceToDecrement;z<=distanceToDecrement;z++){
						int totalDistance = Math.abs(x)+Math.abs(y)+Math.abs(z);
						int redIndex = best.r+x;
						int greenIndex = best.g+y;
						int blueIndex = best.b+z;
						if(redIndex<0||redIndex>255||greenIndex<0||greenIndex>255||blueIndex<0||blueIndex>255)continue;
						allColours[best.r+x][best.g+y][best.b+z]*=(totalDistance/divider);
					}	
				}
			}						
		}


		Color[][] cols = new Color[img.getWidth()][img.getHeight()];

		for(PotentialColour p:bestValues){
			//									System.out.println(p);
			if(showColours)System.out.println(p.toRGB());
		}
		for(int y=0;y<img.getHeight();y++){
			for(int x=0;x<img.getWidth();x++){

				int loc = (y*1500) + (x*3);
				//						System.out.println(loc%3);
				if(wordy){
					System.out.println();
					System.out.println();
					System.out.println("checking loc "+loc);
					System.out.println("x: "+(loc%1500)/3+", y:"+loc/1500);
				}

				int r = pixels[loc+2];
				int g = pixels[loc+1];
				int b = pixels[loc+0];
				if(r<0)r+=256;
				if(g<0)g+=256;
				if(b<0)b+=256;
				int bestSim=9999;
				int bestIndex=0;
				for(int i=0;i<4;i++){
					int sim =bestValues[i].getSim(r, g, b);
					if(sim<bestSim){
						bestSim=sim;
						bestIndex=i;
					}
				}
				cols[x][y]= bestValues[bestIndex].getColor();

			}
		}
		ImageContainer reducedColor = new ImageContainer(cols);
		frame.getContentPane().add(reducedColor);
		reducedColor.setLocation(img.getWidth(), 0);
		frame.repaint();
		chip8It();

	}
	public static void chip8It(){
		int wFactor = 500/128;
		int hFactor = 400/64;

		for(int y=0;y<64;y++){
			for(int x=0;x<128;x+=8){
				for(int plane=0;plane<2;plane++){
					String byt = "0b";
					for(int nib=0;nib<8;nib++){
						int loc = (y*400/64*1500) + ((x+nib)*500/128*3);
						while(loc%3!=0)loc++;
						//						System.out.println(loc%3);
						if(wordy){
							System.out.println();
							System.out.println();
							System.out.println("checking loc "+loc);
							System.out.println("x: "+(loc%1500)/3+", y:"+loc/1500);
						}

						
						int[] bestIndices = new int[4];
						for(int xWig=0;xWig<3;xWig++){
							for(int yWig=0;yWig<3;yWig++){
								int extra = xWig*3+yWig*1500;
								int r = pixels[loc+2+extra];
								int g = pixels[loc+1+extra];
								int b = pixels[loc+0+extra];
								if(r<0)r+=256;
								if(g<0)g+=256;
								if(b<0)b+=256;
								int bestSim=9999;
								int bestIndex=0;
								for(int i=0;i<4;i++){
									int sim =bestValues[i].getSim(r, g, b);
									if(sim<bestSim){
										bestSim=sim;
										bestIndex=i;
									}
								}
								bestIndices[bestIndex]++;
							}	
						}
						
						int bestThing=0;
						int bestIndex=0;
						for(int i=0;i<bestIndices.length;i++){
							if(bestIndices[i]>bestThing){
								bestThing=bestIndices[i];
								bestIndex=i;
							}
						}
						
						switch(bestIndex){
						case 0:
							byt+="0";
							break;
						case 1:
							if(plane ==0)byt+="1";
							else byt+="0";
							break;
						case 2:
							if(plane==1)byt+="1";
							else byt+="0";
							break;
						case 3:
							byt += "1";
							break;
						}

					}
					System.out.println(byt);
				}
			}
		}
	}
	//	public static int[] RGBtoHSV(int inputRed, int inputGreen, int inputBlue){
	//		if(inputRed<0)inputRed=256+inputRed;
	//		if(inputGreen<0)inputGreen=256+inputGreen;
	//		if(inputBlue<0)inputBlue=256+inputBlue;
	//		float r = inputRed/255f;
	//		float g = inputGreen/255f;
	//		float b = inputBlue/255f;
	//		float max = Math.max(r, Math.max(g, b));
	//		float min = Math.min(r, Math.min(g, b));
	//		float delta = max-min;
	//		float hue=0;
	//		if(delta==0){
	//			hue=0;
	//		}
	//		else if(max==r){
	//			hue=((g-b)/delta);
	//		}
	//		else if(max==g){
	//			hue=((b-r)/delta)+2;
	//		}
	//		else if(max==b){
	//			hue=((r-g)/delta)+4;
	//		}
	//		if (hue<0)hue+=6;
	//		hue*=16.667;
	//
	//		float saturation = max==0?0:delta/max;
	//
	//		return new int[]{(int) (hue),(int) (saturation*100),(int) (max*100)};
	//	}
}

