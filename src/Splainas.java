

public class Splainas {

    public Splainas(){}

    // sprendzia triistrizine matrica: Ax = d
    /*
      A matrica:
        | b c      |
        | a b c    |
        |   ...    |
        |    a b c |
        |      a b |
     */
    // a, b, c - istrizainiu reiksmiu masyvai
    // x - nezinomuju matrica stulpelis
    // http://www.cfd-online.com/Wiki/Tridiagonal_matrix_algorithm_-_TDMA_(Thomas_algorithm)
    private void spresti_1(int n, float a[], float b[], float c[], float d[], float x[]){

        // sistemos sprendimas
        for(int k=1; k<=n; k++){
            float m = a[k]/b[k-1];
            b[k] = b[k] - m*c[k-1];
            d[k] = d[k] - m*d[k-1];
        }

        x[n] = d[n]/b[n];
        for(int k=n-1; k>=0; k--)
            x[k] = (d[k] - c[k]*x[k+1])/b[k];
    }

    private void spresti_2(int n, float a[], float b[], float c[], float d[], float x[]){
        
        float s[] = new float[n+2];
        float e[] = new float[n+2];
        // e, s - temp matrica
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

    // Splainas su antrosiomis isvestinemis
    // m0 ir mn - f(x) antruju isvestiniu reiksmes taskuose x0 ir xn
    // x[], y[] - splaino taskai
    // n - intervalu sk
    public void spline_2s(int n, float m0, float mn, float x[], float y[], float m[])
    {
        float a[] = new float[n+1];
        float b[] = new float[n+1];
        float c[] = new float[n+1];
        float d[] = new float[n+1];
        float h[] = new float[n+1];

        for(int i=1; i<=n; i++)
            h[i] = x[i] - x[i-1];
        
        for(int i=1; i<n; i++){
        	if(i==1) a[i] = 0.0f; else a[i] = h[i]/6.0f;
                b[i] = -(h[i]+h[i+1])/3.0f;
                if(i==n-1) c[i] = 0.0f; else c[i] = h[i+1]/6.0f;
                d[i] = y[i-1]/h[i] - y[i]/h[i] - y[i]/h[i+1] + y[i+1]/h[i+1];
        }
        d[1] = d[1]-m0*h[1]/6.0f;
        d[n-1] = d[n-1] - mn*h[n]/6.0f;

        spresti_2(n-1, a, b, c, d, m);
        m[0] = m0;
        m[n] = mn;
    }

    // Splainas su pirmosiomis isvestinemis
    // m0 ir mn - f(x) pirmuju isvestiniu reiksmes taskuose x0 ir xn
    // x[], y[] - splaino taskai
    // n - intervalu sk (tasku n+1)
    public void spline_1s(int n, float m0, float mn, float x[], float y[], float m[])
    {
        float a[] = new float[n+1];
        float b[] = new float[n+1];
        float c[] = new float[n+1];
        float d[] = new float[n+1];
        float h[] = new float[n+1];

        for(int i=0; i <= n-1; i++)
            h[i] = x[i+1] - x[i];

        for(int i=0; i <= n; i++)
        {
            if(i == 0){
                a[0] = 0.0f;
                b[0] = 2*h[0];
                c[0] = h[0];
                d[0] = 3.0f*(y[1]-y[0])/h[0] - 3.0f*m0;
            }
            else if(i == n)
            {
                a[n] = h[n-1];
                b[n] = 2*h[n-1];
                c[n] = 0.0f;
                d[n] = 3.0f*mn - 3.0f*(y[n] - y[n-1])/h[n-1];
            }
            else
            {
                a[i] = h[i-1];
                b[i] = 2*(h[i-1]+h[i]);
                c[i] = h[i];
                d[i] = 3.0f*(y[i+1]-y[i])/h[i] - 3.0f*(y[i]-y[i-1])/h[i-1];
            }
        }
        
        // sistemos sprendimas
        spresti_1(n, a, b, c, d, m);
    }

    // splaino reiksmiu skaiciavimas
    public float eval(int n, float x[], float y[], float m[], float t)
    {
        if(t < x[0] || t > x[n-1]) return 0.0f;

        int i = 1;
        while(t > x[i]) i++;

        float h = x[i]-x[i-1];
        float t1 = x[i]-t;
        float t2 = t-x[i-1];
        float p1 = t1*t1*t1/6.0f;
        float p2 = t2*t2*t2/6.0f;
        float s1 = y[i-1]-m[i-1]*h*h/6.0f;
        float s2 = y[i]-m[i]*h*h/6.0f;
        return (m[i-1]*p1 + m[i]*p2 + s1*t1 + s2*t2)/h;
    }


}