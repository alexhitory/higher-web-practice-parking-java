package ru.yandex.practicum;

import java.util.*;

public class ParkingLot extends AbstractParkingLot {

    private final List<Spot> spots;
    private final Map<String, Spot> parkedCars;

    public ParkingLot(int totalSpots, int electricSpots, int premiumSpots) {
        super(totalSpots, electricSpots, premiumSpots);

        this.spots = new ArrayList<>();
        this.parkedCars = new HashMap<>();

        int normalSpots = totalSpots - electricSpots - premiumSpots;

        for (int i = 0; i < normalSpots; i++) spots.add(new Spot(Spot.SpotType.NORMAL));
        for (int i = 0; i < premiumSpots; i++) spots.add(new Spot(Spot.SpotType.PREMIUM));
        for (int i = 0; i < electricSpots; i++) spots.add(new Spot(Spot.SpotType.ELECTRIC));
    }

    @Override
    public void enter(String carType, String number) throws ParkingException {
        if (parkedCars.containsKey(number)) {
            throw new ParkingException("Машина уже припаркована: " + number);
        }

        Spot.SpotType type;
        try {
            type = Spot.SpotType.valueOf(carType);
        } catch (IllegalArgumentException e) {
            throw new ParkingException("Неизвестный тип машины: " + carType);
        }

        for (Spot spot : spots) {
            if (spot.isFree() && spot.canPark(type)) {
                spot.park();
                parkedCars.put(number, spot);
                return;
            }
        }

        throw new ParkingException("Нет свободного места для " + carType);
    }

    @Override
    public void leave(String number) throws ParkingException {
        Spot spot = parkedCars.remove(number);
        if (spot == null) {
            throw new ParkingException("Машины с номером " + number + " нет на парковке");
        }
        spot.leave();
    }

    @Override
    public Set<String> getNumbers() {
        return new HashSet<>(parkedCars.keySet());
    }

    @Override
    boolean isEmpty() {
        return parkedCars.isEmpty();
    }
}