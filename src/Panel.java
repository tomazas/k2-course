import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Panel extends JFrame{

    final int WIDTH = 200;
    final int HEIGHT = 400;

    JButton ivesti;
    JRadioButton normal, pirmos, antros, pele, klav;
    JLabel taskmo, taskmn, taskx, tasky;
    JTextField taskasmo, taskasmn, taskasX, taskasY;
    private Canvas canvas = null;
    private Langas langas = null;
    private Panel self = this;
    
    public Panel(Langas frame, Canvas canvas)
    {
        super("Valdymas");

        this.langas = frame;
        this.canvas = canvas;

        // style
        this.setLayout(null);
        int cx = langas.getX()+langas.getWidth();
        int cy = langas.getY();
        this.setBounds(cx,cy,WIDTH,HEIGHT);

        //
        //  meniu
        //
        
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

       
        //
        //            normaliosios, pirmosios, antrosios
        //

        normal = new JRadioButton("Normalios salygos");
        self.add(normal);
        normal.setBounds(10, 30, 150, 20);
        normal.setSelected(true);

        pirmos = new JRadioButton("Pirmosios isvestines");
        self.add(pirmos);
        pirmos.setBounds(10, 50, 150, 20);
        pirmos.setSelected(false);

        antros = new JRadioButton("Antrosios isvestines");
        self.add(antros);
        antros.setBounds(10, 70, 150, 20);
        antros.setSelected(false);

        pele = new JRadioButton("Pele");
        self.add(pele);
        pele.setBounds(10, 180, 150, 20);
        pele.setSelected(true);

        klav = new JRadioButton("Klaviatura");
        self.add(klav);
        klav.setBounds(10, 200, 150, 20);

        //
        //            info labels
        //

        JLabel salygos = new JLabel( "Krastines salygos ");
        self.add(salygos);
        salygos.setBounds(5, 5, 150, 20);

        taskmo = new JLabel("m(o)");
        self.add(taskmo);
        taskmo.setBounds(30, 100, 30, 20);
        taskmo.setEnabled(false);

        taskmn = new JLabel("m(n)");
        self.add(taskmn);
        taskmn.setBounds(30, 120, 30, 20);
        taskmn.setEnabled(false);

        JLabel taskgen = new JLabel("Tasku generavimas");
        self.add(taskgen);
        taskgen.setBounds(5, 160, 150, 20);

        taskx = new JLabel("x");
        self.add(taskx);
        taskx.setBounds(30, 220, 15, 20);
        taskx.setEnabled(false);

        tasky = new JLabel("y");
        self.add(tasky);
        tasky.setBounds(30, 240, 15, 20);
        tasky.setEnabled(false);

        //
        //             TextFields
        //
        taskasmo = new JTextField();
        self.add(taskasmo);
        taskasmo.setBounds(65, 100, 40, 20);
        taskasmo.setEnabled(false);

        taskasmn = new JTextField();
        self.add(taskasmn);
        taskasmn.setBounds(65, 120, 40, 20);
        taskasmn.setEnabled(false);

        taskasX = new JTextField();
        self.add(taskasX);
        taskasX.setBounds(50, 220, 40, 20);
        taskasX.setEnabled(false);

        taskasY = new JTextField();
        self.add(taskasY);
        taskasY.setBounds(50, 240, 40, 20);
        taskasY.setEnabled(false);

        //
        //          mygtukas
        //
        ivesti = new JButton("Ivesti");
        self.add(ivesti);
        ivesti.setBounds(30, 260, 80, 20);
        ivesti.setEnabled(false);


                // Open listener
        menuFailaiIsFailo.addActionListener(
            new ActionListener(){
                    public void actionPerformed(ActionEvent e)  {
                        File failas = self.dialogas( 1 );
                        if(failas == null) return;

                        try{
                            self.canvas.clear();
                            Scanner scanner = new Scanner(failas);  // scanner failo skaitymui
                            Lentele func = new Lentele(scanner);
                            func.Uzpildyti(self.canvas);
                        }
                        catch ( NumberFormatException a )  {
                            System.out.println("Blogi duomenys faile");
                            System.exit(0);
                        }
                        catch( FileNotFoundException a )  {
                            System.out.println("Failas nerastas");
                            System.exit(0);
                        }
                        catch( IOException a )  {
                            System.out.println("Klaida atidarant faila");
                            System.exit(0);
                        }
                    }
       
             }
     );

        // Save listener
        menuFailaiIFaila.addActionListener(
            new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        try{
                            File failas = self.dialogas( 2 );
                            if(failas == null) return;

                                PrintWriter rasymui;
                                rasymui = new PrintWriter(failas);
                                //rasymui.println("Ivestos/Nurodytos koodrinates");
                                //rasymui.println("   X     Y");
                                for (int i = 0; i < self.canvas.getn(); i ++ )
                                    rasymui.printf("%.6f %.6f\n", self.canvas.getX(i), self.canvas.getY(i));
                                rasymui.flush();
                            }
                            catch ( NumberFormatException a )  {
                                System.out.println("Blogi duomenys faile");
                                System.exit(0);
                            }
                            catch( FileNotFoundException a )  {
                                System.out.println("Failas nerastas");
                                System.exit(0);
                            }
                            catch( IOException a )  {
                                System.out.println("Klaida atidarant faila");
                                System.exit(0);
                            }
			}
                 }
         );


        normal.addActionListener(
		new ActionListener(){
			public void actionPerformed(ActionEvent e)  {
                            normal.setSelected(true);
                            pirmos.setSelected(false);
                            antros.setSelected(false);
                            taskmo.setEnabled(false);
                            taskmn.setEnabled(false);
                            taskasmo.setEnabled(false);;
                            taskasmn.setEnabled(false);
			}
                 }
         );

        antros.addActionListener(
		new ActionListener(){
			public void actionPerformed(ActionEvent e)  {
                            normal.setSelected(false);
                            pirmos.setSelected(false);
                            antros.setSelected(true);
                            taskmo.setEnabled(true);
                            taskmn.setEnabled(true);
                            taskasmo.setEnabled(true);
                            taskasmn.setEnabled(true);
			}
                 }
         );

        pirmos.addActionListener(
		new ActionListener(){
			public void actionPerformed(ActionEvent e)  {
                            normal.setSelected(false);
                            pirmos.setSelected(true);
                            antros.setSelected(false);
                            taskmo.setEnabled(true);
                            taskmn.setEnabled(true);
                            taskasmo.setEnabled(true);
                            taskasmn.setEnabled(true);
			}
                 }
         );

        pele.addActionListener(
		new ActionListener(){
			public void actionPerformed(ActionEvent e)  {
                            self.canvas.set_mouse(true);
                            pele.setSelected(true);
                            klav.setSelected(false);
                            taskx.setEnabled(false);
                            tasky.setEnabled(false);
                            taskasX.setEnabled(false);
                            taskasY.setEnabled(false);
                            ivesti.setEnabled(false);
			}
                 }
         );

        klav.addActionListener(
		new ActionListener(){
			public void actionPerformed(ActionEvent e)  {
                            self.canvas.set_mouse(false);
                            pele.setSelected(false);
                            klav.setSelected(true);
                            taskx.setEnabled(true);
                            tasky.setEnabled(true);
                            taskasX.setEnabled(true);
                            taskasY.setEnabled(true);
                            ivesti.setEnabled(true);
			}
                 }
         );

        ivesti.addActionListener(
		new ActionListener(){
			public void actionPerformed(ActionEvent e)  {
                            try  {
                                float x = Float.parseFloat( taskasX.getText() );
                                float y = Float.parseFloat( taskasY.getText() );
                                
                                self.canvas.add_point(x, y, false);
                            }  catch ( NumberFormatException a )  {
                                    JOptionPane.showMessageDialog(null, "Neteisingai ivesti duomenys",
		"Klaida", JOptionPane.ERROR_MESSAGE);
                            }
                            
			}
                 }
         );

                   
         this.addWindowListener(new WindowListener(){
            public void windowOpened(WindowEvent e){}
            public void windowClosing(WindowEvent e){}
            public void windowClosed(WindowEvent e){}
            public void windowIconified(WindowEvent e){
                langas.setExtendedState(langas.getExtendedState() | Frame.ICONIFIED);
            }
            public void windowDeiconified(WindowEvent e){
                langas.setExtendedState(langas.getExtendedState() & ~Frame.ICONIFIED);
            }
            public void windowActivated(WindowEvent e){}
            public void windowDeactivated(WindowEvent e){}
        });

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

     public File dialogas(int forma ) {
        // forma
        // 1 = open file
        // 2 = save file
        File failas = null;
	JFileChooser fc = new JFileChooser();
      //  fileTypes.set
        FileNameExtensionFilter ff;
        // file filters
        if ( forma == 1 )
             ff = new FileNameExtensionFilter("Duomenu failai, *.duom", "duom");
        else
            ff = new FileNameExtensionFilter("Rezultatu failai,  *.rez", "rez");
        fc.addChoosableFileFilter(ff);

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
