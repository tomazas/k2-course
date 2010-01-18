

import java.awt.*;
import javax.swing.*;

public class Canvas extends JComponent{

    Splainas sp = new Splainas();
    int N = 100;
    float x[] = new float[N];
    float y[] = new float[N];
    float m[] = new float[N];
    int n = 0;
    boolean dirty = true;

    public Canvas(){
        super();
    }

    // prideda nauja taska
    public void add_point(float xp, float yp)
    {
        dirty = true;
        this.x[n] = xp;
        this.y[n++] = yp;
        Rikiuoti();            // rikiavimas
        if(n>=2) build();
        else this.repaint();
    }

    // isvalo taskus
    public void clear(){ n = 0; this.repaint(); }

    // apskaiciuoja kubini splaina
    public void build()
    {
        dirty = false;
        // 2-uju isvestiniu reiksmes x0 ir xn taskuose
        float m0 = 0;
        float mn = 0;
        sp.spline(n-1, m0, mn, x, y, m);
        this.repaint();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        float t = x[0]; // pradinis taskas
        float h = 0.01f; // zingsnis
        
        g.setColor(Color.red);

        // draw points
        for(int i=0; i<n; i++){
            int x0 = Math.round(x[i]);
            int y0 = Math.round(y[i]);
            g.fillOval(x0, y0, 6, 6);
        }

        if(n == 0) return;
        g.setColor(Color.blue);

        if(dirty == false)
        {
            while(t < x[n-1]-h)
            {
                float t1 = t+h;
                int x0 = Math.round(t);
                int x1 = Math.round(t+h);
                int y0 = Math.round(sp.g(n, x, y, m, t));
                int y1 = Math.round(sp.g(n, x, y, m, t1));
                g.drawLine(x0, y0, x1, y1);
                t += h;
            }
        }
    }

    private void Rikiuoti()  {
        for ( int i = 0; i < n-1; i++ )
            for ( int j = 1; j < n; j++ )  {
                if ( x[i] > x[i+1] )  {
                    float tempX = x[i];
                    float tempY = y[i];

                    x[i] = x[i+1];
                    x[i+1] = tempX;

                    y[i] = y[i+1];
                    y[i+1] = tempY;
                }
            }
    }
    public int getn()  {
        return n;
    }

    public float getX(int sk)  {
        return x[sk];
    }
    public float getY(int sk)  {
        return y[sk];
    }

}
