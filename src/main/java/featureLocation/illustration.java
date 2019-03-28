package featureLocation;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import drawingAlgorithms.FruchtermanReingold;

public class illustration {

	private static double coef;

	public static void drawings(double[][] pos, ArrayList<Entity> methods) {
		
		if (pos.length > 0) {
			int size = pos[0].length;
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					if (pos[i][j] == 1)
						pos[j][i] = 1;
					if (pos[j][i] == 1)
						pos[i][j] = 1;
				}
			}

			FruchtermanReingold FR = new FruchtermanReingold(pos, false, 10);

			double[][][] FRpos = FR.getPositions();

			Display display = new Display();
			Shell shell = new Shell(display);
			shell.setText("Canvas Example");
			shell.setLayout(new FillLayout());

			Canvas canvas = new Canvas(shell, SWT.NONE);

			double min = 0;
			double max = 0;
			coef = 0;

			for (int i = 0; i < pos.length; i++) {
				//System.out.println(FRpos[0][0][i] + " " + FRpos[0][1][i]);
				if (FRpos[0][0][i] < min)
					min = (int) FRpos[0][0][i];
				if (FRpos[0][1][i] < min)
					min = (int) FRpos[0][1][i];
			}
			//System.out.println("MIN   " + min);
			min = Math.abs(min);
			min++;

			for (int i = 0; i < size; i++) {

				FRpos[0][0][i] = (FRpos[0][0][i] + min);
				FRpos[0][1][i] = (FRpos[0][1][i] + min);
				if (FRpos[0][0][i] > max)
					max = (int) FRpos[0][0][i];

				if (FRpos[0][1][i] > max)
					max = (int) FRpos[0][1][i];

				//System.out.println(FRpos[0][0][i] + " " + FRpos[0][1][i]);
			}
			max++;
			//System.out.println("MAX   " + max);

			coef = 550 / max;

			canvas.addPaintListener(new PaintListener() {
				public void paintControl(PaintEvent e) {
					
					int count=1;

					for (int i = 0; i < size; i++) {

						//System.out.println(FRpos[0][0][i] * coef + " " + FRpos[0][1][i] * coef);

						e.gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
						e.gc.drawOval((int) ((FRpos[0][0][i] * coef)-5), (int) ((FRpos[0][1][i] * coef)-5), 22, 22);
						e.gc.setForeground(display.getSystemColor(SWT.COLOR_RED));
						e.gc.drawText(String.valueOf(count), (int) (FRpos[0][0][i] * coef), (int) (FRpos[0][1][i] * coef));
						e.gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
						for (int j = 0; j < size; j++) {
							if (pos[i][j] == 1) {
								e.gc.drawLine((int) (FRpos[0][0][i] * coef), (int) (FRpos[0][1][i] * coef),
										(int) (FRpos[0][0][j] * coef), (int) (FRpos[0][1][j] * coef));
							}
							//System.out.print(pos[i][j] + " ");
						}
						//System.out.println();
						count++;
					}

					for (count=0; count<methods.size(); count++) {
						if(count<30)
							e.gc.drawText((count+1+" "+methods.get(count).getName()), 600, 20*count);
						else
							e.gc.drawText((count+1+" "+methods.get(count).getName()), 900, 20*(count-30));
					    //System.out.println(count+1+" "+methods.get(count).getName());
					}
					
				}
			});
			
			 
			

			shell.open();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			display.dispose();
		}
	}

}
