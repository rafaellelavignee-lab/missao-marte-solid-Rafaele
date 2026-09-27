package solidexercicio10;
public enum Dificuldade {
    FACIL, MEDIO, DIFICIL;
    public static Dificuldade deString(String s) {
        if (s == null) return MEDIO;
        switch (s.trim().toLowerCase()) {
            case "facil":
                return FACIL;
            case "dificil":
                return DIFICIL;
            default:
                return MEDIO;
        }
    }
    @Override
    public String toString() {
        switch (this) {
            case FACIL: return "Facil";
            case DIFICIL: return "Dificil";
            default: return "Medio";
        }
    }
}
