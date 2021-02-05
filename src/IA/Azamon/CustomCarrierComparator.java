package IA.Azamon;

import java.util.Comparator;

public class CustomCarrierComparator implements Comparator<Oferta> {
    public int compare(Oferta o1, Oferta o2) {
        return Integer.compare(o1.getDias(),o2.getDias());
    }
}
