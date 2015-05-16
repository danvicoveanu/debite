
class ComboItem {

    public int id;
    public String text;

    public ComboItem(int i, String t) {
        this.id = i;// aduc variabilele din constr. si le bag in clasa
        this.text = t;
    }

    @Override
    public String toString() {
        return text;// afiseaza textul corect
    }

}