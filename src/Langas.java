
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;


public class Langas extends JFrame{

    final int WIDTH = 500;
    final int HEIGHT = 400;
    Canvas canvas = new Canvas();

    public Langas(){
        super("Kubinio splaino generavimas");
        this.setLayout(null);
        Dimension dim = this.getToolkit().getScreenSize();
        int cx = dim.width/2;
        int cy = dim.height/2;
        this.setBounds(cx-WIDTH/2,cy-HEIGHT/2,WIDTH,HEIGHT);


        // menu juostos formavimas
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        // Failai ir jo sub items
        JMenuItem menuFailai = new JMenu("Failai");
        menuBar.add(menuFailai);

        JMenuItem menuFailaiIsFailo = new JMenuItem("Is failo");
        menuFailai.add( menuFailaiIsFailo );

        JMenuItem menuFailaiIFaila = new JMenuItem("I faila");
        menuFailai.add( menuFailaiIFaila );

        //JMenuItem menuParinktys = new JMenu("Parinktys");
        //menuBar.add(menuParinktys);
        
        canvas.setBounds(0,0,WIDTH,HEIGHT);
        canvas.addMouseListener(new MouseListener(){
            public void mouseClicked(MouseEvent e){}
            public void mousePressed(MouseEvent e){
                switch(e.getButton()){
                case MouseEvent.BUTTON1:
                    canvas.add_point(e.getX(), e.getY());
                    break;
                    
                case MouseEvent.BUTTON3:
                    canvas.clear();
                    break;
                    
                default: break;
                }
            }
            public void mouseReleased(MouseEvent e){}
            public void mouseEntered(MouseEvent e){}
            public void mouseExited(MouseEvent e){}
        });
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(canvas);


        // IsFailo listener
        menuFailaiIsFailo.addActionListener(
		new ActionListener(){
			public void actionPerformed(ActionEvent e)  {
        File failas = dialogas( 1 );
        try{
            canvas.clear();
            Scanner scanner = new Scanner(failas);  // scanner failo skaitymui
            while( scanner.hasNextFloat() )  {
                canvas.add_point( scanner.nextFloat(), scanner.nextFloat() );
            }
        }
        catch ( NumberFormatException a )  {
            System.out.println("Blogi duomenys faile");
            System.exit(0);
        }
        catch( FileNotFoundException a )  {
            System.out.println("Failas netastas");
            System.exit(0);
        }
        catch( IOException a )  {
            System.out.println("Klaida atidarant faila");
            System.exit(0);
        }
			}
                 }
         );

        menuFailaiIFaila.addActionListener(
		new ActionListener(){
			public void actionPerformed(ActionEvent e)  {
        try{
            File failas = dialogas( 2 );
            PrintWriter rasymui;
            rasymui = new PrintWriter(failas);
            rasymui.println("Ivestos/Nurodytos koodrinates");
            rasymui.println("   X     Y");
            for (int i = 0; i < canvas.getn(); i ++ )  {
                    rasymui.print( ilgioFormatas( (int)canvas.getX(i), 6) );
                    rasymui.print( ilgioFormatas( (int)canvas.getY(i), 6) );
                    rasymui.println();
            }

            rasymui.flush();

        }
        catch ( NumberFormatException a )  {
            System.out.println("Blogi duomenys faile");
            System.exit(0);
        }
        catch( FileNotFoundException a )  {
            System.out.println("Failas netastas");
            System.exit(0);
        }
        catch( IOException a )  {
            System.out.println("Klaida atidarant faila");
            System.exit(0);
        }
			}
                 }
         );


/*
        JButton btn = new JButton("Build");
        btn.setBounds(WIDTH-100, HEIGHT-80, 80, 24);
        btn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                canvas.build();
            }
        });
        this.add(btn);
*/
        this.setVisible(true);
        this.repaint();
    }

    public File dialogas(int forma ) {
        // forma
        // 1 = open file
        // 2 = save file
        File failas = null;
	JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory( new File(".") );
        if ( forma == 1 )  {
            fc.setDialogTitle("Pasirinkite faila skaitymui");
            fc.setApproveButtonText("Open");
        }
        if ( forma == 2 )  {
            fc.setDialogTitle("Pasirinkite faila rasymui");
            fc.setApproveButtonText("Save");
        }
	int rez = fc.showOpenDialog(null);
	if (rez == JFileChooser.APPROVE_OPTION) {
		failas = fc.getSelectedFile();
	} else if (rez == JFileChooser.CANCEL_OPTION) {
		// kai pasirenkame "cancel"
	} else if (rez == JFileChooser.ERROR_OPTION) {
		// kažkas blogai
		JOptionPane.showMessageDialog(null, "Klaida skaitant faila",
		"Skaitymas - rasymas", JOptionPane.ERROR_MESSAGE);
	}
        return failas;
    }

    public String ilgioFormatas( int sk, int formatas )  {
        //formatuoja duota skaiciu i tam tikro ilgio formata
        String skaicius = Integer.toString(sk);
        int skaiciausIlgis = formatas - skaicius.length();
        while ( skaiciausIlgis > 0 )  {
            skaicius = " " + skaicius;
            skaiciausIlgis--;
        }
        return skaicius;
    }
    
}
