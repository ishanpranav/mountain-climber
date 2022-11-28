package project4;

public class SupplyCollection {
    private int foodRations;
    private int rafts;
    private int axes;

    public SupplyCollection() {}
    
    public SupplyCollection(int foodRations, int rafts, int axes) {
        this.foodRations = foodRations;
        this.rafts = rafts;
        this.axes = axes;
    }

    public void transferTo(SupplyCollection other) {
        other.foodRations += foodRations;
        other.rafts += rafts;
        other.axes += axes;
    }
}
