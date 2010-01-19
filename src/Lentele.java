import java.util.*;
import java.util.Scanner;

public class Lentele {
    
    private ArrayList<Float> x = new ArrayList<Float>();
    private ArrayList<Float> y = new ArrayList<Float>();
    private float xMin, xMax, yMin, yMax; // grafiko apribota deze/ribos (bounding box)

    public Lentele(Scanner scanner){
        Ivedimas(scanner);
    }

    // iveda taskus is srauto
    public void Ivedimas(Scanner scanner)
    {
        x.clear();
        y.clear();
        
        while(scanner.hasNextFloat()){
            x.add(scanner.nextFloat());
            y.add(scanner.nextFloat());
        }

        System.out.println("tasku ivesta: "+x.size());

        this.RastiRibas();
    }

    // perskaiciuoja dezes/ribu matmenis
    public void RastiRibas()
    {
        xMin = 9999999;
        yMin = 9999999;
        xMax = -xMin;
        yMax = -yMin;
        
        for(int i=0; i<x.size(); i++)
        {
            if(x.get(i) < xMin) xMin = x.get(i);
            if(x.get(i) > xMax) xMax = x.get(i);
            if(y.get(i) < yMin) yMin = y.get(i);
            if(y.get(i) > yMax) yMax = y.get(i);
        }

        System.out.printf("bounds: x: %.2f %.2f y: %.2f %.2f\n",xMin,xMax,yMin,yMax);
    }

    // uzpildo canvas struktura grafiko duomenimis
    public void Uzpildyti(Canvas canvas)
    {
        canvas.set_bounds(xMin, yMin, xMax, yMax);
        
        for(int i=0; i<x.size(); i++)
            canvas.add_point(x.get(i), y.get(i), false);
    }
}
