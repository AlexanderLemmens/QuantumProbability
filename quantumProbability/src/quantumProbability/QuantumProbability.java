package quantumProbability;

import java.awt.Button;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
/**
 * De blauwe stralen zijn de verschillende mogelijke waarden van y,
 * de rode strepen rechts komen overeen met de ki,
 * de rode strepen op de blauwe figuur komen overeen met de ki*d.
 * 
 * @author Alexander
 *
 */
public class QuantumProbability extends Frame implements MouseListener, MouseMotionListener, ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		QuantumProbability frame = new QuantumProbability();
	}
	
	public QuantumProbability() {
		this.addMouseMotionListener(this);
		this.addMouseListener(this);		
		this.window = new Window(this);
		this.addWindowListener(window);
		this.setSize(ORIGINALWIDTH, ORIGINALHEIGHT);
		this.img = new BufferedImage(ORIGINALWIDTH, ORIGINALHEIGHT, BufferedImage.TYPE_INT_RGB);
		this.setLayout(null);
		this.setVisible(true);
		int i;
		int j;
		for(i=0; i<ORIGINALWIDTH; i++) {
			for(j=0; j<ORIGINALHEIGHT; j++) {
				this.img.setRGB(i,j,0xFFFFFFFF);
			}
		}
		Button probabilities = new Button("show probabilities");
		probabilities.setBounds(0, 30, 150, 30);
		probabilities.addActionListener(this);
		this.add(probabilities);
		this.probabilityButton = probabilities;
		//drawing the big figure
		drawDial(this.img, 300, 310, 210, 180, 0, 32, 0xFF0000FF, true);
		drawDial(this.img, 300, 310, 170, 150, 0, 32, 0xFF0000FF, false);
		drawDial(this.img, 300, 310, 140, 120, 0, 32, 0xFF9090FF, false);
		drawDial(this.img, 300, 310, 110, 90, 0, 32, 0xFF9090FF, false);
		drawDial(this.img, 300, 310, 80, 60, 0, 32, 0xFF9090FF, false);
		drawDial(this.img, 300, 310, 50, 30, 0, 32, 0xFF9090FF, false);
		drawDial(this.img, 300, 310, 180, 150, 0, 16, 0xFF0000FF, true);
		drawDial(this.img, 300, 310, 150, 120, 0, 8, 0xFF0000FF, true);
		drawDial(this.img, 300, 310, 120, 90, 0, 4, 0xFF0000FF, true);
		drawDial(this.img, 300, 310, 90, 60, 0, 2, 0xFF0000FF, true);
		drawDial(this.img, 300, 310, 60, 30, 0, 1, 0xFF0000FF, true);
		// drawing the red figures on the right
		double NN = N;
		drawDial(this.img, 650, 140, 30, 24, 0, N, 0xFF000000, false);
		drawDial(this.img, 650, 140, 30, 20, 0, 1, 0xFFCF0000, false);
		drawDial(this.img, 650, 220, 30, 24, 0, N, 0xFF000000, false);
		drawDial(this.img, 650, 220, 30, 20, 0, 1, 0xFFCF0000, false);
		drawDial(this.img, 650, 300, 30, 24, 0, N, 0xFF000000, false);
		drawDial(this.img, 650, 300, 30, 20, 0, 1, 0xFFCF0000, false);
		drawDial(this.img, 650, 380, 30, 24, 0, N, 0xFF000000, false);
		drawDial(this.img, 650, 380, 30, 20, 0, 1, 0xFFCF0000, false);
		drawDial(this.img, 650, 460, 30, 24, 0, N, 0xFF000000, false);
		drawDial(this.img, 650, 460, 30, 20, 0, 1, 0xFFCF0000, false);
		drawDial(this.img, 750, 140, 30, 24, 0, N, 0xFF000000, false);
		drawDial(this.img, 750, 140, 30, 20, 0, 1, 0xFFCF9000, false);
		this.markedImage = deepCopy(this.img);
		this.paint(this.img);
		this.probability = new double[32];
		for(i = 0; i<32; i++){
			this.probability[i] = 1;
		}
	}
	
	private Window window;
	
	private static final int ORIGINALWIDTH = 900;
	
	private static final int ORIGINALHEIGHT = 600;
	
	private static final int GREEN = 0xFF00D000;
	
	private static final int RED = 0xFFDF0000;
	
	private static final int PURPLE = 0xFFFF00FF;
	
	private BufferedImage img;
	
	private BufferedImage markedImage;
	
	private boolean flagY = false;
	
	private boolean flagD = false;
	
	private boolean flag1 = false;

	private boolean flag2 = false;

	private boolean flag3 = false;

	private boolean flag4 = false;

	private boolean flag5 = false;
	
	private double angleY;
	
	private int angleD;
	
	private int angle1;

	private int angle2;

	private int angle3;

	private int angle4;

	private int angle5;
	
	private Button probabilityButton;
	
	private boolean showingProbabilities = false;
	
	private double probability[];

	private static final int M = 5;
	
	private static final int N = 23;
	
	private static final double NN = N;
	
	private void paint(BufferedImage img) {
		Graphics g=getGraphics();
		g.drawImage(img, 0, 0,this);
	}
	
	private static void drawDial(BufferedImage image, int centerX, int centerY, int outerRadius, int innerRadius, double angle, int n, int rgb, boolean thick) {
		double nn = n;
		for(int i=0; i<n;i++) {
			double argument = (angle + i)*2*Math.PI/nn;
			int[] outerPoint = exponential(centerX, centerY, outerRadius, argument);
			int[] innerPoint = exponential(centerX, centerY, innerRadius, argument);
			if(thick) {
				drawThickLine(image, rgb, outerPoint[0], outerPoint[1], innerPoint[0], innerPoint[1]);
			}
			else {
				drawThinLine(image, rgb, outerPoint[0], outerPoint[1], innerPoint[0], innerPoint[1]);				
			}
		}
	}
	
	private static int round(double x) {
		double one = 1;
		int y = (int) (Math.abs(x) + one/2);
		if(x>0) return y;
		else return -y;
	}
	
	private static int[] exponential(int centerX, int centerY, int radius, double argument) {
		int[] P = new int[2];
		P[0] = round(centerX + radius* Math.cos(argument));
		P[1] = round(centerY + radius* Math.sin(argument));
		return P;
	}
	
	private static int multiplyModN(int x, int y, int n) {
		return (x*y)%n;
	}
	
	private static double argument(int x, int y, int centerX, int centerY) {
		int xx = x - centerX;
		int yy = y - centerY;
		double r = Math.sqrt(xx*xx+yy*yy);
		double t = Math.acos(xx/r);
		if(yy>0) return t/(2*Math.PI);
		else return 1-t/(2*Math.PI);
	}
	
	private static void drawDisk(BufferedImage image, int x, int y, int radiusSquared, int rgb) {
		int radius = (int) Math.sqrt(radiusSquared);
		int width = image.getWidth();
		int height = image.getHeight();
		int xx;
		int yy;
		int xMin = Math.max(x - radius, 0);
		int xMax = Math.min(x + radius, width);
		int yMin = Math.max(y - radius, 0);
		int yMax = Math.min(y + radius, height);
		for(xx = xMin; xx<=xMax; xx++) {
			for(yy = yMin; yy<=yMax; yy++) {
				if((xx-x)*(xx-x)+(yy-y)*(yy-y)<=radiusSquared) {
					image.setRGB(xx, yy, rgb);
				}
			}
		}
	}
	
	private static void drawThinLine(BufferedImage img, int rgb, int x1, int y1, int x2, int y2) {
		int width = img.getWidth();
		int height = img.getHeight();
		int[][] dots = joinDots(x1, y1, x2, y2);
		int l = dots.length;
		int x;
		int y;
		for(int i = 0; i<l; i++) {
			x = dots[i][0];
			y = dots[i][1];
			if(0<=x && 0<=y && x<width && y<height) {
				img.setRGB(x, y, rgb);
			}
		}
	}
	
	private static void drawThickLine(BufferedImage img, int rgb, int x1, int y1, int x2, int y2) {
		int width = img.getWidth();
		int height = img.getHeight();
		int[][] dots = joinDots(x1, y1, x2, y2);
		int l = dots.length;
		int x;
		int y;
		for(int i = 0; i<l; i++) {
			x = dots[i][0];
			y = dots[i][1];
			if(0<x && 0<y && x<width-1 && y<height-1) {
				img.setRGB(x, y, rgb);
				img.setRGB(x+1, y, rgb);
				img.setRGB(x-1, y, rgb);
				img.setRGB(x, y+1, rgb);
				img.setRGB(x, y-1, rgb);
			}
		}
	}

	private static int[][] joinDots(int x1, int y1, int x2, int y2) {
		boolean right = (x2>=x1);
		boolean below = (y2>=y1);
		int vx = x2-x1;
		int vy = y2-y1;
		int wx1;
		int wy1;
		int wx2;
		int wy2;
		int wx3;
		int wy3;
		int px = 0;
		int py = 0;
		int n = 0;
		int m = Math.abs(vx) + Math.abs(vy);
		int[][] A = new int[m][2];
		while(!(px == vx && py == vy) && n<=m) {
			if (right){
				if (below){
					wx1 = px;
					wy1 = py + 1;
					wx2 = px + 1;
					wy2 = py;
					wx3 = px + 1;
					wy3 = py + 1;
				}else {
					wx1 = px;
					wy1 = py - 1;
					wx2 = px + 1;
					wy2 = py;
					wx3 = px + 1;
					wy3 = py - 1;
				}
			}else {
				if (below){
					wx1 = px;
					wy1 = py + 1;
					wx2 = px - 1;
					wy2 = py;
					wx3 = px - 1;
					wy3 = py + 1;					
				}else {
					wx1 = px;
					wy1 = py - 1;
					wx2 = px - 1;
					wy2 = py;
					wx3 = px - 1;
					wy3 = py - 1;
				}				
			}
			if(Math.abs(vx*wy1-vy*wx1)<Math.abs(vx*wy2-vy*wx2)) {
				if(Math.abs(vx*wy1-vy*wx1)<Math.abs(vx*wy3-vy*wx3)) {
					px = wx1;
					py = wy1;
				}else {
					px = wx3;
					py = wy3;					
				}
			}else {
				if(Math.abs(vx*wy2-vy*wx2)<Math.abs(vx*wy3-vy*wx3)) {
					px = wx2;
					py = wy2;
				}else {
					px = wx3;
					py = wy3;
				}
			}
			A[n][0] = x1+px;
			A[n][1] = y1+py;
			n++;
		}
		int[][] B = new int[n][2];
		for(int i=0; i<n; i++) {
			B[i][0] = A[i][0];
			B[i][1] = A[i][1];
		}
		return B;
	}
	
	private int normSquared(int x, int y) {
		return x*x+y*y;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		int normSquared = normSquared(x-300,y-310);
		if(normSquared<210*210 && normSquared>100) {
			flagY = true;
			double argument = argument(x, y, 300, 310);
			double arg = round(32*argument);
			this.angleY = arg/32;
		}else {
			normSquared = normSquared(x-650, y-140);
			if(normSquared>25 && normSquared<1600) {
				flag1 = true;
				double argument = argument(x, y, 650, 140);
				this.angle1 = round(N*argument);
		}else {
			normSquared = normSquared(x-650, y-220);
			if(normSquared>25 && normSquared<1600) {
				flag2 = true;
				double argument = argument(x, y, 650, 220);
				this.angle2 = round(N*argument);
		}else {
			normSquared = normSquared(x-650, y-300);
			if(normSquared>25 && normSquared<1600) {
				flag3 = true;
				double argument = argument(x, y, 650, 300);
				this.angle3 = round(N*argument);
		}else {
			normSquared = normSquared(x-650, y-380);
			if(normSquared>25 && normSquared<1600) {
				flag4 = true;
				double argument = argument(x, y, 650, 380);
				this.angle4 = round(N*argument);
		}else {
			normSquared = normSquared(x-650, y-460);
			if(normSquared>25 && normSquared<1600) {
				flag5 = true;
				double argument = argument(x, y, 650, 460);
				this.angle5 = round(N*argument);
		}else {
			normSquared = normSquared(x-750, y-140);
			if(normSquared>25 && normSquared<1600) {
				flagD = true;
				double argument = argument(x, y, 750, 140);
				this.angleD = round(N*argument);
		}else {
			flagY = false;
			flag1 = false;
			flag2 = false;
			flag3 = false;
			flag4 = false;
			flag5 = false;
			flagD = false;
			this.paint(this.img);
			return;
		}
		}
		}
		}
		}
		}
		}
		double one = 1;
		double half = one/2;
		double angle;
		double cos;
		this.markedImage = deepCopy(this.img);
		int i;
		for(i = 0; i<32; i++){
			probability[i] = 1;
		}
		if(flagY) {
			drawDial(this.markedImage, 300, 310, 210, 30, angleY, 1, PURPLE, true);
		}
		if(flag1) {
			drawDial(this.markedImage, 650, 140, 30, 10, angle1/NN, 1, GREEN, true);
			if(flagD) {
			angle = -multiplyModN(angleD, angle1, N);
			drawDial(this.markedImage, 300, 310, 210, 180, angle/NN, 16, GREEN, true);
			drawDial(this.markedImage, 300, 310, 210, 180, angle/NN+half, 16, RED, true);
			for(i = 0; i<32; i++) {
			cos = Math.cos((angle/NN-i*half)*Math.PI);
			probability[i] = probability[i]*cos*cos;
			}
			}
		}
		if(flag2) {
			drawDial(this.markedImage, 650, 220, 30, 10, angle2/NN, 1, GREEN, true);
			if(flagD) {
			angle = -multiplyModN(angleD, angle2, N);
			drawDial(this.markedImage, 300, 310, 180, 150, angle/NN, 8, GREEN, true);
			drawDial(this.markedImage, 300, 310, 180, 150, angle/NN+half, 8, RED, true);
			for(i = 0; i<32; i++) {
			cos = Math.cos((angle/NN-i*half/2)*Math.PI);
			probability[i] = probability[i]*cos*cos;
			}
			}
		}
		if(flag3) {
			drawDial(this.markedImage, 650, 300, 30, 10, angle3/NN, 1, GREEN, true);
			if(flagD) {
			angle = -multiplyModN(angleD, angle3, N);
			drawDial(this.markedImage, 300, 310, 150, 120, angle/NN, 4, GREEN, true);
			drawDial(this.markedImage, 300, 310, 150, 120, angle/NN+half, 4, RED, true);
			for(i = 0; i<32; i++) {
			cos = Math.cos((angle/NN-i*half/4)*Math.PI);
			probability[i] = probability[i]*cos*cos;
			}
			}
		}
		if(flag4) {
			drawDial(this.markedImage, 650, 380, 30, 10, angle4/NN, 1, GREEN, true);
			if(flagD) {
			angle = -multiplyModN(angleD, angle4, N);
			drawDial(this.markedImage, 300, 310, 120, 90, angle/NN, 2, GREEN, true);
			drawDial(this.markedImage, 300, 310, 120, 90, angle/NN+half, 2, RED, true);
			for(i = 0; i<32; i++) {
			cos = Math.cos((angle/NN-i*half/8)*Math.PI);
			probability[i] = probability[i]*cos*cos;
			}
			}
		}
		if(flag5) {
			drawDial(this.markedImage, 650, 460, 30, 10, angle5/NN, 1, GREEN, true);
			if(flagD) {
			angle = -multiplyModN(angleD, angle5, N);
			drawDial(this.markedImage, 300, 310, 90, 60, angle/NN, 1, GREEN, true);
			drawDial(this.markedImage, 300, 310, 90, 60, angle/NN+half, 1, RED, true);
			for(i = 0; i<32; i++) {
			cos = Math.cos((angle/NN-i*half/16)*Math.PI);
			probability[i] = probability[i]*cos*cos;
			}
			}
		}
		if(flagD) {
			drawDial(this.markedImage, 750, 140, 30, 10, angleD/NN, 1, 0xFFEF9000, true);
		}
		if(this.showingProbabilities) {
		for(i=0; i<32; i++) {
			int[] center = exponential(300, 310, 230, (2*Math.PI*i)/32);
			drawDisk(this.markedImage, center[0], center[1], (int) (probability[i]*100), PURPLE);
		}
		}
		this.paint(this.markedImage);
	}
	
	static BufferedImage deepCopy(BufferedImage bi) {
		 ColorModel cm = bi.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = bi.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	private void showProbabilities() {
		for(int i=0; i<32; i++) {
			int[] center = exponential(300, 310, 230, (2*Math.PI*i)/32);
			drawDisk(this.markedImage, center[0], center[1], (int) (probability[i]*100), PURPLE);
		}
		this.paint(this.markedImage);
	}
	
	private void hideProbabilities() {
		for(int i=0; i<32; i++) {
			int[] center = exponential(300, 310, 230, (2*Math.PI*i)/32);
			drawDisk(this.markedImage, center[0], center[1], 100, 0xFFFFFF);
		}
		this.paint(this.markedImage);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		this.showingProbabilities = !this.showingProbabilities;
		if(this.showingProbabilities) {
			this.probabilityButton.setLabel("hide probabilities");
			this.showProbabilities();
		}else {
			this.probabilityButton.setLabel("show probabilities");
			this.hideProbabilities();
		}
	}
	
	class Window extends WindowAdapter{
		
		Window(QuantumProbability frame){
			this.frame = frame;
		}
		
		QuantumProbability frame;
		
		public void windowClosing(WindowEvent e) {
			frame.dispose();
		}
	}

}
