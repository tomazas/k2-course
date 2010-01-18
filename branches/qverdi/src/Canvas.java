

import java.awt.*;
import javax.swing.*;

public class Canvas extends JComponent{

    Splainas sp = new Splainas();
    int N = 100; // TODO: change to dynamic size
    float x_orig[] = new float[N];
    float y_orig[] = new float[N];
   
    // transformuoti taskai
    float x[] = new float[N];
    float y[] = new float[N];
    float m[] = new float[N];
    
    int n = 0; // tasku skaicius
    boolean dirty = true; // ar reikia per-generuoti splaina?

    // scaling factors
    float scale_x = 1.0f;
    float scale_y = 1.0f;
    int defaultWidth; // standartine ekrano rezoliucija
    int defaultHeight;
   
    public Canvas(int scr_width, int scr_height){
        super();
        defaultWidth = scr_width;
        defaultHeight = scr_height;
    }

    // prideda nauja taska
    // jei mouse = true - pridedame taska paspausdami ant ekrano
    public void add_point(float xp, float yp, boolean mouse)
    {
        dirty = true;

        if(mouse == true){
            // transformuojam is ekrano lango i realius
            this.x_orig[n] = xp/scale_x;
            this.y_orig[n] = yp/scale_y;

            // transformuoti nereikia, nes taskai atidedami 1:1 ekrane
            this.x[n] = xp;
            this.y[n] = yp;
        }else{
            // issaugom realias koordinates
            this.x_orig[n] = xp;
            this.y_orig[n] = yp;

            // reikia transformuoti i ekrano langa
            this.x[n] = xp*scale_x;
            this.y[n] = yp*scale_y;
        }

        n += 1;
        
        this.build();
        this.repaint();
    }

    // isvalo taskus
    public void clear(){ n = 0; this.repaint(); }

    // apskaiciuoja kubini splaina
    public void build()
    {
        if(n < 2) return; // per mazai tasku, min 2
        dirty = false;

        // 2-uju isvestiniu reiksmes x0 ir xn taskuose
        float m0 = 0;
        float mn = 0;
        sp.spline(n-1, m0, mn, x, y, m);
        
        this.repaint();// reikalinga perpiesti
    }

    // perskaiciuoja ekrado zoom'a
    public void rescale()
    {
        // perskaiciuojam zoom'a
        scale_x = this.getWidth()/(float)defaultWidth;
        scale_y = this.getHeight()/(float)defaultHeight;

        // transformuojam taskus
        for(int i=0; i<n; i++){
            x[i] = x_orig[i]*scale_x;
            y[i] = y_orig[i]*scale_y;
        }

        this.build();
    }

    // piesia koordinaciu asis
    public void drawAxes(Graphics g)
    {
        int w = this.getWidth();
        int h = this.getHeight();
        int mid_w = this.getWidth()/2;
        int mid_h = this.getHeight()/2;

        g.drawLine(0, mid_h, w, mid_h); // x-asis
        g.drawLine(mid_w, 0, mid_w, h); // y-asis

        // koordinaciu pradzia
        g.drawString("0", mid_w+1, mid_h+12);

        // markeriai
        int count = 6;
        float y_marker = 0.025f;// 0.025% ekrano
        int hm = Math.round(h*y_marker);
        int step = w/count;

        for(int i=0; i<count; i++){
            g.drawLine(mid_w-hm/2, i*step, mid_w+hm/2, i*step);
            g.drawLine(i*step, mid_h-hm/2, i*step, mid_h+hm/2); // x-asis
        }
    }

    // nupiesia taskus ekrane
    public void drawDots(Graphics g)
    {
        g.setColor(Color.red);

        // draw points
        for(int i=0; i<n; i++){
            int x0 = Math.round(x[i]);
            int y0 = Math.round(y[i]);
            g.fillOval(x0, y0, 6, 6); // 6px dydis
        }

    }
    
    // piesia splaina linijomis pastoviu zingsniu
    public void drawLines(Graphics g)
    {
        float t = x[0]; // pradinis taskas
        float h = 0.01f; // zingsnis
        g.setColor(Color.blue);

        if(dirty == false) // taskai nepasikeite
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

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        drawAxes(g);

        if(n == 0) return; // nera jokiu tasku

        drawDots(g);
        drawLines(g);
    }

}
