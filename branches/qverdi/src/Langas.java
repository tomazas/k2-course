
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Langas extends JFrame{

    final int WIDTH = 500;
    final int HEIGHT = 400;
    Canvas canvas = new Canvas(WIDTH, HEIGHT);
    JFrame frame = this;

    public Langas(){
        super("Kubinio splaino generavimas");
        this.setLayout(null);
        Dimension dim = this.getToolkit().getScreenSize();
        int cx = dim.width/2;
        int cy = dim.height/2;
        this.setBounds(cx-WIDTH/2,cy-HEIGHT/2,WIDTH,HEIGHT);

        canvas.setBounds(0,0,WIDTH,HEIGHT);
        canvas.addMouseListener(new MouseListener(){
            public void mouseClicked(MouseEvent e){}
            public void mousePressed(MouseEvent e){
                switch(e.getButton()){
                case MouseEvent.BUTTON1:
                    canvas.add_point(e.getX(), e.getY(), true);
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

        // resize check
        this.addComponentListener(new ComponentListener(){
            public void componentResized(ComponentEvent e){
                canvas.setBounds(0,0,frame.getWidth(),frame.getHeight());
                canvas.rescale();
            }
            public void componentMoved(ComponentEvent e){}
            public void componentShown(ComponentEvent e){}
            public void componentHidden(ComponentEvent e){}
        });

        // close
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(canvas);

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
    
}
