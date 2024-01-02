package agh.ics.oop.model;

public enum MapDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;

    @Override
    public String toString() {
        return switch (this) {
            case NORTH -> "Polnoc";
            case NORTHEAST -> "Polnocny-Wschod";
            case EAST -> "Wschod";
            case SOUTHEAST -> "Poludniowy-Wschod";
            case SOUTH -> "Poludnie";
            case SOUTHWEST -> "Poludniowy-Zachod";
            case WEST -> "Zachod";
            case NORTHWEST -> "Polnocny-Zachod";
        };
    }

    /*
    values() -> tablica wszystkich wartosci enum
    ordinal() -> zwraca pozycje biezacego elementu enuma
    modulo zapewnia ze nie wyjdziemy poza zakres tablicy
     */
    public MapDirection next () {
        return values()[(ordinal() + 1) % values().length];
    }

    public MapDirection previous() {
        int length = values().length;
        int index = (ordinal() - 1 + length) % length;

        return values()[index];
    }

    public Vector2d toUnitVector() {
        return switch (this) {
            case NORTH -> new Vector2d(0, 1);
            case NORTHEAST -> new Vector2d(1, 1);
            case EAST -> new Vector2d(1, 0);
            case SOUTHEAST -> new Vector2d(1, -1);
            case SOUTH -> new Vector2d(0, -1);
            case SOUTHWEST -> new Vector2d(-1, -1);
            case WEST -> new Vector2d(-1, 0);
            case NORTHWEST -> new Vector2d(-1, 1);
        };
    }
}
