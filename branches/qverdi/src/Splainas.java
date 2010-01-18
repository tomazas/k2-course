

public class Splainas {

    public Splainas(){}

    private void spresti(int n, float a[], float b[], float c[], float d[], float x[])
    {
        float s[] = new float[n+2];
        float e[] = new float[n+2];
            // e, s - temp matrica (float[101])
        s[1] = 0.0f;
        e[1] = 0.0f;

        // tiesioginis etapas
        for(int i=1; i<=n; i++){
                float t = b[i] - a[i]*s[i];
                s[i+1] = c[i]/t;
                e[i+1] = (a[i]*e[i]-d[i])/t;
        }

        // atvirkstinis etapas
        x[n] = e[n+1];
        for(int i=n-1; i>=1; i--)
                x[i] = s[i+1]*x[i+1] + e[i+1];
    }

    // m0 ir mn - splainu antruju isvestiniu reiksmes taskuose x0 ir xn
    // x[], y[] - splaino taskai
    // n - intervalu sk
    public void spline(int n, float m0, float mn, float x[], float y[], float m[])
    {
        float a[] = new float[n+1];
        float b[] = new float[n+1];
        float c[] = new float[n+1];
        float d[] = new float[n+1];
        float h[] = new float[n+1];

        // a,b,c,d,h - temp masyvai
        // A*m=H*y formavimas
        for(int i=1; i<=n; i++) h[i] = x[i] - x[i-1];
        for(int i=1; i<n; i++){
        	/*if(i==1) a[i] = 0.0f; else*/ a[i] = h[i]/6.0f;
                b[i] = -(h[i]+h[i+1])/3.0f;
                /*if(i==n-1) c[i] = 0.0f; else*/ c[i] = h[i+1]/6.0f;
                d[i] = y[i-1]/h[i] - y[i]/h[i] - y[i]/h[i+1] + y[i+1]/h[i+1];
        }
        d[1] = d[1]-m0*h[1]/6.0f;
        d[n-1] = d[n-1] - mn*h[n]/6.0f;

        // sistemos sprendimas
        int n1 = n-1;
        spresti(n1, a, b, c, d, m);
        m[0] = m0;
        m[n] = mn;
    }

    public float g(int n, float x[], float y[], float m[], float t)
    {
        if(t < x[0] || t > x[n-1]) return 0.0f;

        int i = 1;
        while(x[i] < t) i++;
        float h = x[i] - x[i-1];
        float t1 = x[i] - t;
        float t2 = t - x[i-1];
        float p1 = t1*t1*t1/6.0f;
        float p2 = t2*t2*t2/6.0f;
        float s1 = y[i-1]-m[i-1]*h*h/6.0f;
        float s2 = y[i]-m[i]*h*h/6.0f;
        return (m[i-1]*p1 + m[i]*p2 + s1*t1 + s2*t2)/h;
    }


}
