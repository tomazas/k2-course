

public class Splainas {

    // solves tridiagonal matrix: Ax = d
    // a, b, c - values of the diagonals
    // x - column of unknowns
    private void spresti_1(int n, float a[], float b[], float c[], float d[], float x[]){

        // solving system
        for(int k=1; k<=n; k++){
            float m = a[k]/b[k-1];
            b[k] = b[k] - m*c[k-1];
            d[k] = d[k] - m*d[k-1];
        }

        x[n] = d[n]/b[n];
        for(int k=n-1; k>=0; k--)
            x[k] = (d[k] - c[k]*x[k+1])/b[k];
    }

    // solves tridiagonal matrix: Ax = d
    private void spresti_2(int n, float a[], float b[], float c[], float d[], float x[]){
        
        float s[] = new float[n+2];
        float e[] = new float[n+2];
        s[1] = 0.0f;
        e[1] = 0.0f;

        // forward step
        for(int i=1; i<=n; i++){
                float t = b[i] - a[i]*s[i];
                s[i+1] = c[i]/t;
                e[i+1] = (a[i]*e[i]-d[i])/t;
        }

        // backward step
        x[n] = e[n+1];
        for(int i=n-1; i>=1; i--)
                x[i] = s[i+1]*x[i+1] + e[i+1];
    }

    // spline construction via second order derivatives
    // m0 ir mn - f(x) second order derivatives at points x0 and xn
    // x[], y[] - spline points
    // n - number of intervals
    public void spline_2s(int n, float m0, float mn, float x[], float y[], float m[])
    {
        float a[] = new float[n+1];
        float b[] = new float[n+1];
        float c[] = new float[n+1];
        float d[] = new float[n+1];
        float h[] = new float[n+1];
        
        for(int i=1; i<=n; i++)
            h[i] = x[i] - x[i-1]; // step
        
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

    // spline construction via first order derivatives
    // m0 ir mn - f(x) first order derivatives at points x0 and xn
    // x[], y[] - spline points, m[] - derivative values
    // n - number of intervals (number of points = n+1)
    public void spline_1s(int n, float m0, float mn, float x[], float y[], float m[])
    {
        float a[] = new float[n+1];
        float b[] = new float[n+1];
        float c[] = new float[n+1];
        float d[] = new float[n+1];
        float h[] = new float[n+1];

        for(int i=0; i <= n-1; i++)
            h[i] = x[i+1] - x[i]; // step

        for(int i=0; i <= n; i++)
        {
            if(i == 0){
                a[0] = 0.0f;
                b[0] = h[0]/3.0f;
                c[0] = h[0]/6.0f;
                d[0] = (y[1]-y[0])/h[0] + m0;
            }
            else if(i == n)
            {
                a[n] = h[n-1]/6.0f;
                b[n] = h[n-1]/3.0f;
                c[n] = 0.0f;
                d[n] = -mn - (y[n] - y[n-1])/h[n-1];
            }
            else
            {
                a[i] = h[i-1]/6.0f;
                b[i] = (h[i-1]+h[i])/3.0f;
                c[i] = h[i]/6.0f;
                d[i] = (y[i+1]-y[i])/h[i] - (y[i]-y[i-1])/h[i-1];
            }
        }

        // solving system
        spresti_1(n, a, b, c, d, m);
    }

    // computes the y-axis value on spline at given time t
    // x[], y[] - spline points, m[] - derivative values
    // n - number of intervals (number of points = n+1)
    public float eval(int n, float x[], float y[], float m[], float t)
    {
        if(t < x[0] || t > x[n-1]) return 0.0f; // t is out of range
        
        int i = 1;
        while(t > x[i]) i++; // advance to time step where: x[i-1] >= t <= x[i]

        float h = x[i]-x[i-1]; // time step
        // compute time deltas
        float t1 = x[i]-t;
        float t2 = t-x[i-1];
        // cubic time
        float p1 = t1*t1*t1/6.0f;
        float p2 = t2*t2*t2/6.0f;
        float s1 = y[i-1]-m[i-1]*h*h/6.0f;
        float s2 = y[i]-m[i]*h*h/6.0f;
        // interpolate
        return (m[i-1]*p1 + m[i]*p2 + s1*t1 + s2*t2)/h;
    }


}
