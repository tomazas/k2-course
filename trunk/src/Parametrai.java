import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Parametrai extends JDialog{

    final int WIDTH = 300;
    final int HEIGHT = 160;
    private Canvas canvas;
    private Parametrai self = this;
    private JTextField t_xmin, t_xmax, t_ymin, t_ymax;

    public Parametrai(Canvas canvas)
    {
        //super(null, "Nustatymai");
        this.setTitle("Nustatymai");
        this.canvas = canvas;

        // style
        this.setLayout(null);
        this.setResizable(false);
        this.setModal(true);
        Dimension dim = this.getToolkit().getScreenSize();
        int cx = dim.width/2;
        int cy = dim.height/2;
        this.setBounds(cx-WIDTH/2,cy-HEIGHT/2,WIDTH,HEIGHT);

        // labels
        JLabel label_dek = new JLabel("Dekarto sistema:");
        label_dek.setBounds(10, 5, 100, 20);
        this.add(label_dek);
        
        JLabel label0 = new JLabel("X min:");
        label0.setBounds(10,30,50,20);
        this.add(label0);

        JLabel label1 = new JLabel("Y min:");
        label1.setBounds(10,60,50,20);
        this.add(label1);

        JLabel label2 = new JLabel("X max:");
        label2.setBounds(140,30,50,20);
        this.add(label2);

        JLabel label3 = new JLabel("Y max:");
        label3.setBounds(140,60,50,20);
        this.add(label3);

        // edit boxes
        float b[] = canvas.get_bounds();

        t_xmin = new JTextField(Float.toString(Math.round(b[0]*1000)/1000.0f));
        t_xmin.setBounds(50, 30, 80, 20);
        this.add(t_xmin);

        t_ymin = new JTextField(Float.toString(Math.round(b[1]*1000)/1000.0f));
        t_ymin.setBounds(50, 60, 80, 20);
        this.add(t_ymin);

        t_xmax = new JTextField(Float.toString(Math.round(b[2]*1000)/1000.0f));
        t_xmax.setBounds(190, 30, 80, 20);
        this.add(t_xmax);

        t_ymax = new JTextField(Float.toString(Math.round(b[3]*1000)/1000.0f));
        t_ymax.setBounds(190, 60, 80, 20);
        this.add(t_ymax);

        // buttons
        JButton btn_set = new JButton("Nustatyti");
        btn_set.setBounds(WIDTH/2-50, 95, 100, 24);
        btn_set.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try{
                    float xmin = Float.parseFloat(self.t_xmin.getText());
                    float ymin = Float.parseFloat(self.t_ymin.getText());
                    float xmax = Float.parseFloat(self.t_xmax.getText());
                    float ymax = Float.parseFloat(self.t_ymax.getText());

                    self.canvas.set_bounds(xmin, ymin, xmax, ymax);
                    self.canvas.repaint();
                    self.setVisible(false); // close
                }catch(NumberFormatException err){
                    JOptionPane.showMessageDialog(null, "Neteisingai ivesti duomenys",
                    "Klaida", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        this.add(btn_set);

        // ok, show
        this.setVisible(true);
    }
}
