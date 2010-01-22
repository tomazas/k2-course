/*************************************************************************
This subroutine builds cubic spline interpolant.

INPUT PARAMETERS:
    X           -   spline nodes, array[0..N-1]
    Y           -   function values, array[0..N-1]
    N           -   points count, N>=2
    BoundLType  -   boundary condition type for the left boundary
    BoundL      -   left boundary condition (first or second derivative,
                    depending on the BoundLType)
    BoundRType  -   boundary condition type for the right boundary
    BoundR      -   right boundary condition (first or second derivative,
                    depending on the BoundRType)

OUTPUT PARAMETERS:
    C           -   spline interpolant

The BoundLType/BoundRType parameters can have the following values:
    * 0, which  corresponds  to  the  parabolically   terminated  spline
         (BoundL/BoundR are ignored).
    * 1, which corresponds to the first derivative boundary condition
    * 2, which corresponds to the second derivative boundary condition

  -- ALGLIB PROJECT --
     Copyright 23.06.2007 by Bochkanov Sergey
*************************************************************************/

public class SplineCode  {

    /*************************************************************************
    This subroutine builds cubic spline interpolant.

    INPUT PARAMETERS:
        X           -   spline nodes, array[0..N-1]
        Y           -   function values, array[0..N-1]
        N           -   points count, N>=2
        BoundLType  -   boundary condition type for the left boundary
        BoundL      -   left boundary condition (first or second derivative,
                        depending on the BoundLType)
        BoundRType  -   boundary condition type for the right boundary
        BoundR      -   right boundary condition (first or second derivative,
                        depending on the BoundRType)

    OUTPUT PARAMETERS:
        C           -   spline interpolant

    The BoundLType/BoundRType parameters can have the following values:
        * 0, which  corresponds  to  the  parabolically   terminated  spline
             (BoundL/BoundR are ignored).
        * 1, which corresponds to the first derivative boundary condition
        * 2, which corresponds to the second derivative boundary condition

      -- ALGLIB PROJECT --
         Copyright 23.06.2007 by Bochkanov Sergey
    *************************************************************************/
    spline1dinterpolant spline1dbuildcubic(float x[],
         float y[],
         int n,
         int boundltype,
         float boundl,
         int boundrtype,
         float boundr,
         spline1dinterpolant c)
    {
        float a1[] = new float[n];
        float a2[] = new float[n];
        float a3[] = new float[n];
        float b[] = new float[n];

        if( boundltype==1 ) // first derivative
        {
            a1[0] = 0;
            a2[0] = 1;
            a3[0] = 0;
            b[0] = boundl;
        }
        if( boundltype==2 ) // second derivative
        {
            a1[0] = 0;
            a2[0] = 2;
            a3[0] = 1;
            b[0] = 3f*(y[1]-y[0])/(x[1]-x[0])-0.5f*boundl*(x[1]-x[0]);
        }

        // Central conditions
        for(int i = 1; i <= n-2; i++)
        {
            a1[i] = x[i+1]-x[i]; // zingsnis
            a2[i] = 2*(x[i+1]-x[i-1]);
            a3[i] = x[i]-x[i-1];
            b[i] = 3*(y[i]-y[i-1])/(x[i]-x[i-1])*(x[i+1]-x[i])+3*(y[i+1]-y[i])/(x[i+1]-x[i])*(x[i]-x[i-1]);
        }

        // Right boundary conditions
        if( boundrtype==1 ) // first derivative
        {
            a1[n-1] = 0;
            a2[n-1] = 1;
            a3[n-1] = 0;
            b[n-1] = boundr;
        }
        if( boundrtype==2 ) // second derivative
        {
            a1[n-1] = 1;
            a2[n-1] = 2;
            a3[n-1] = 0;
            b[n-1] = 3f*(y[n-1]-y[n-2])/(x[n-1]-x[n-2])+0.5f*boundr*(x[n-1]-x[n-2]);
        }

        // Solve
        float d[] = solvetridiagonal(a1, a2, a3, b, n);

        // Now problem is reduced to the cubic Hermite spline
        c = spline1dbuildhermite(x, y, d, n, c);
        return c;
    }


    /*************************************************************************
    Internal subroutine. Tridiagonal solver.
    *************************************************************************/
    static float[] solvetridiagonal(float a[],
         float b[],
         float c[],
         float d[],
         int n)
    {
        int k;
        float t;

        float x[] = new float[n];
        a[0] = 0;
        c[n-1] = 0;
        for(k = 1; k <= n-1; k++)
        {
            t = a[k]/b[k-1];
            b[k] = b[k]-t*c[k-1];
            d[k] = d[k]-t*d[k-1];
        }
        x[n-1] = d[n-1]/b[n-1];
        for(k = n-2; k >= 0; k--)
        {
            x[k] = (d[k]-c[k]*x[k+1])/b[k];
        }
        return x;
    }


    /*************************************************************************
    This subroutine builds Hermite spline interpolant.

    INPUT PARAMETERS:
        X           -   spline nodes, array[0..N-1]
        Y           -   function values, array[0..N-1]
        D           -   derivatives, array[0..N-1]
        N           -   points count, N>=2

    OUTPUT PARAMETERS:
        C           -   spline interpolant.

      -- ALGLIB PROJECT --
         Copyright 23.06.2007 by Bochkanov Sergey
    *************************************************************************/
    spline1dinterpolant spline1dbuildhermite(float x[],
         float y[],
         float d[],
         int n,
         spline1dinterpolant c)
    {
        c.xsetlength(n);
        c.csetlength(4*(n-1));
        c.k = 3;
        c.n = n;
        for(int i = 0; i <= n-1; i++)
            c.x[i] = x[i];
        for(int i = 0; i <= n-2; i++)
        {
            float delta = x[i+1]-x[i];
            float delta2 = delta*delta;
            float delta3 = delta*delta2;
            c.c[4*i+0] = y[i];
            c.c[4*i+1] = d[i];
            c.c[4*i+2] = (3*(y[i+1]-y[i])-2*d[i]*delta-d[i+1]*delta)/delta2;
            c.c[4*i+3] = (2*(y[i]-y[i+1])+d[i]*delta+d[i+1]*delta)/delta3;
        }
        return c;
    }


    /*************************************************************************
    This subroutine calculates the value of the spline at the given point X.

    INPUT PARAMETERS:
        C   -   spline interpolant
        X   -   point

    Result:
        S(x)

      -- ALGLIB PROJECT --
         Copyright 23.06.2007 by Bochkanov Sergey
    *************************************************************************/
    double spline1dcalc( spline1dinterpolant c, double x)
    {
        double result;
        int l;
        int r;
        int m;

        // Binary search in the [ x[0], ..., x[n-2] ] (x[n-1] is not included)
        l = 0;
        r = c.n-2+1;
        while(l!=r-1)
        {
            m = (l+r)/2;
            if( c.x[m] >= x )
                r = m;
            else
                l = m;
        }

        // Interpolation
        x = x-c.x[l];
        m = 4*l;
        result = c.c[m]+x*(c.c[m+1]+x*(c.c[m+2]+x*c.c[m+3]));
        return result;
    }
}