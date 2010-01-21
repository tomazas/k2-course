

import java.awt.*;
import javax.swing.*;

public class Canvas extends JComponent{

    private Splainas sp = new Splainas();
    private SplineCode sc = new SplineCode();
    private spline1dinterpolant si = new spline1dinterpolant();
    
    private int N = 300; // TODO: change to dynamic size
    private float x_orig[] = new float[N];
    private float y_orig[] = new float[N];
   
    // transformuoti (i ekrana) taskai
    private float x[] = new float[N];
    private float y[] = new float[N];
    private float m[] = new float[N];
    
    private int n = 0; // tasku skaicius
    private boolean dirty = true; // ar reikia per-generuoti splaina?

    private boolean bMouseActive = true;

    public enum DerivType{
        NATURAL,
        FIRST,
        SECOND,
    };

    // isvestiniu reiksmes
    private DerivType m_type = DerivType.NATURAL;
    private float m0 = 0.0f;
    private float mn = 0.0f;

    // ekrano deze
    private float boundsXMin;
    private float boundsXMax;
    private float boundsYMin;
    private float boundsYMax;
   
    public Canvas(int scr_width, int scr_height){
        super();
        this.set_bounds(-1, -1, 1, 1);
    }

    // nustato isvestiniu reiksmes
    public void set_deriv(DerivType type, float m0, float mn)
    {
        this.m_type = type;
        this.m0 = m0;
        this.mn = mn;
    }

    // nustato tasku generavima pele
    public void set_mouse(boolean state)
    {
        this.bMouseActive = state;
    }

    // grazina ar tasku generavimas yra ijungtas
    public boolean is_mouse()
    {
        return this.bMouseActive;
    }

    // nustato funkcijos koordinaciu ribas visam ekranui
    public void set_bounds(float xMin, float yMin, float xMax, float yMax)
    {
        float borderX = (xMax - xMin) * 0.1f;
        float borderY = (yMax - yMin) * 0.1f;
        // + 10% laisvo ploto aplink
        boundsXMin = xMin - borderX;
        boundsYMin = yMin - borderY;
        boundsXMax = xMax + borderX;
        boundsYMax = yMax + borderY;
        this.rescale();
    }

    // transformuoja is funkcijos lenteles koordinaciu i ekrano koordinates
    public void transf_iEkrana(float x, float y, float[] out)
    {
        out[0] = (x - boundsXMin)/(boundsXMax - boundsXMin) * this.getWidth();
        out[1] = this.getHeight() - (y - boundsYMin)/(boundsYMax - boundsYMin) * this.getHeight();
    }

    //transformuoja is ekrano koordinaciu i funkcijos lenteles koordinates
    public void transf_iFunc(float x, float y, float[] out)
    {
        out[0] = x * (boundsXMax - boundsXMin)/(float)this.getWidth() + boundsXMin;
        out[1] = (this.getHeight()-y) * (boundsYMax - boundsYMin)/(float)this.getHeight() + boundsYMin;
    }

    // prideda nauja taska
    // jei mouse = true - pridedame taska paspausdami ant ekrano
    public void add_point(float xp, float yp, boolean mouse)
    {
        dirty = true;
        float o[] = new float[2];

        if(mouse == true){
            // transformuojam is ekrano lango i realius
            this.transf_iFunc(xp, yp, o);
            this.x_orig[n] = o[0];
            this.y_orig[n] = o[1];
            // transformuoti nereikia, nes taskai atidedami 1:1 ekrane
            this.x[n] = xp;
            this.y[n] = yp;
        }else{
            // issaugom realias koordinates
            this.x_orig[n] = xp;
            this.y_orig[n] = yp;
            // reikia transformuoti i ekrano langa
            this.transf_iEkrana(xp, yp, o);
            this.x[n] = o[0];
            this.y[n] = o[1];
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

        Rikiuoti();   // rikiavimas

        // generuojame
        if(this.m_type == DerivType.FIRST)
            sp.spline_1s(n-1, m0, mn, x, y, m);
        else if(this.m_type == DerivType.NATURAL)
            sp.spline_2s(n-1, 0.0f, 0.0f, x, y, m);
        else
            sp.spline_2s(n-1, m0, mn, x, y, m);
        
        this.repaint();// reikalinga perpiesti
    }

    // perskaiciuoja ekrado zoom'a
    public void rescale()
    {
        // transformuojam taskus
        float o[] = new float[2];
        for(int i=0; i<n; i++){
            this.transf_iEkrana(x_orig[i], y_orig[i], o);
            x[i] = o[0];
            y[i] = o[1];
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

        // markeriai
        int count = 6;
        float y_marker = 0.02f;// 0.02% ekrano
        int hm = Math.round(h*y_marker)/2;
        int step_w = w/count;
        int step_h = h/count;

        float xmin = boundsXMin;
        float ymin = boundsYMax;
        float x_step = (boundsXMax - boundsXMin)/(float)count;
        float y_step = (boundsYMax - boundsYMin)/(float)count;
        
        for(int i=0; i <= count; i++){
            // tekstas
            g.drawString(Float.toString(Math.round(ymin*100)/100.0f), mid_w+hm*2, i*step_h+hm/2);

            ymin -= y_step;
            
            if(i == count/2){
                xmin += x_step;
                continue;
            }
            
            // tekstas
            g.drawString(Float.toString(Math.round(xmin*100)/100.0f), i*step_w-hm, mid_h+hm*4);
            xmin += x_step;

            // markeriai
            g.drawLine(mid_w-hm, (int)(i*step_h), mid_w+hm, (int)(i*step_h)); // y-asis
            g.drawLine((int)(i*step_w), mid_h-hm, (int)(i*step_w), mid_h+hm); // x-asis
        }
    }

    // nupiesia taskus ekrane
    public void drawDots(Graphics g)
    {
        g.setColor(Color.red);

        // draw points
        int height = this.getHeight();
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

        if(dirty == true) return;// taskai pasikeite

        int height = this.getHeight();
        while(t < x[n-1]-h)
        {
            float t1 = t+h;
            int x0 = Math.round(t);
            int x1 = Math.round(t+h);
            int y0 = Math.round(sp.eval(n, x, y, m, t));
            int y1 = Math.round(sp.eval(n, x, y, m, t1));
            g.drawLine(x0, y0, x1, y1);
            t += h;
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

    private void Rikiuoti()  {

        for ( int i = 0; i < n-1; i++ )
            for ( int j = i+1; j < n; j++ ){
                if ( x[i] > x[j] + 1e-6f )  {
                    float tempX = x[j];
                    float tempY = y[j];

                    x[j] = x[i];
                    y[j] = y[i];
                    x[i] = tempX;
                    y[i] = tempY;

                    // originaliu koordinaciu rikiavimas
                    tempX = x_orig[j];
                    tempY = y_orig[j];

                    x_orig[j] = x_orig[i];
                    y_orig[j] = y_orig[i];
                    x_orig[i] = tempX;
                    y_orig[i] = tempY;
                }
            }
    }
    
    public int getn()  {
        return n;
    }

    public float getX(int sk)  {
        return x_orig[sk];
    }
    
    public float getY(int sk)  {
        return y_orig[sk];
    }
}
