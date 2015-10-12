import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class ImageContainer extends JComponent{
	public ImageContainer(BufferedImage image) {
		
		this.image=image;
		setSize(image.getWidth(), image.getHeight());
		setVisible(true);
		for(int i=0;i<1000;i++)setLayout(null);
//		for(int i=0;i<100;i++)
		
//		setVisible(true);
	}
	
	Color[][] pixels;
	public ImageContainer(Color[][] pixels) {
		this.pixels=pixels;
		setSize(500,400);
		setVisible(true);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(new Color(255,255,255,255));
		if(image!=null)g.drawImage(image, 0, 0, null);
		if(pixels!=null){
			for(int x=0;x<getWidth();x++){
				for(int y=0;y<getHeight();y++){
					g.setColor(pixels[x][y]);
					g.fillRect(x, y, 1, 1);
				}
			}
		}
		super.paintComponent(g);
	}
	BufferedImage image;

		
}
