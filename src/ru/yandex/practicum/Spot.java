package ru.yandex.practicum;

public class Spot {

    public enum SpotType {
        NORMAL, PREMIUM, ELECTRIC
    }

    private final SpotType type;
    private boolean occupied;

    public Spot(SpotType type) {
        this.type = type;
        this.occupied = false;
    }

    public boolean isFree() {
        return !occupied;
    }

    public boolean canPark(SpotType carType) {
        switch (carType) {
            case NORMAL:
                return type == SpotType.NORMAL;
            case PREMIUM:
                return type == SpotType.PREMIUM || type == SpotType.NORMAL;
            case ELECTRIC:
                return type == SpotType.ELECTRIC;
            default:
                return false;
        }
    }

    public void park() {
        this.occupied = true;
    }

    public void leave() {
        this.occupied = false;
    }

}
