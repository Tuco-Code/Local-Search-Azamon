package IA.Azamon;

import java.util.Comparator;

public class CustomPackageComparator implements Comparator<Paquete> {
    public int compare(Paquete p1, Paquete p2) {
        if (p1.getPrioridad() == p2.getPrioridad())
            return Double.compare(p1.getPeso(), p2.getPeso());
        return Integer.compare(p1.getPrioridad(), p2.getPrioridad());
    }
}
