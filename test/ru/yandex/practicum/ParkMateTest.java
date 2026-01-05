package ru.yandex.practicum;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ParkMateTest {

    @Test
    void testEnterAndLeaveNormalCar() throws ParkingException {
        ParkingLot lot = new ParkingLot(5, 1, 1);

        lot.enter("NORMAL", "ABC123");
        Set<String> cars = lot.getNumbers();
        assertTrue(cars.contains("ABC123"), "Машина ABC123 должна быть припаркована");
        assertFalse(lot.isEmpty(), "Парковка не должна быть пустой после въезда машины");

        lot.leave("ABC123");
        assertFalse(lot.getNumbers().contains("ABC123"), "Машина ABC123 должна покинуть парковку");
        assertTrue(lot.isEmpty(), "Парковка должна быть пустой после выезда машины");
    }

    @Test
    void testEnterPremiumCarOnNormalSpot() throws ParkingException {
        ParkingLot lot = new ParkingLot(1, 0, 0);

        lot.enter("PREMIUM", "PREM1");
        Set<String> cars = lot.getNumbers();
        assertTrue(cars.contains("PREM1"), "Премиум-машина должна занять обычное место, если премиум мест нет");
    }

    @Test
    void testEnterPremiumCarOnPremiumSpot() throws ParkingException {
        ParkingLot lot = new ParkingLot(2, 0, 1);

        lot.enter("PREMIUM", "PREM1");
        assertTrue(lot.getNumbers().contains("PREM1"), "Премиум-машина должна занять премиум место");
    }

    @Test
    void testEnterElectricCarFailsIfNoSpot() {
        ParkingLot lot = new ParkingLot(1, 0, 0);
        assertThrows(ParkingException.class,
                () -> lot.enter("ELECTRIC", "ELEC1"),
                "Попытка припарковать электромобиль без электроместа");
    }

    @Test
    void testLeaveNonExistingCar() {
        ParkingLot lot = new ParkingLot(1, 1, 1);
        assertThrows(ParkingException.class,
                () -> lot.leave("NOTPARKED"),
                "Попытка выезда машины, которой нет на парковке");
    }

    @Test
    void testMultipleCars() throws ParkingException {
        ParkingLot lot = new ParkingLot(3, 1, 1);

        lot.enter("NORMAL", "N1");
        lot.enter("PREMIUM", "P1");
        lot.enter("ELECTRIC", "E1");

        assertEquals(3, lot.getNumbers().size(), "На парковке должно быть три машины");
        lot.leave("N1");
        assertEquals(2, lot.getNumbers().size(), "После выезда N1 на парковке должно остаться две машины");
    }

    @Test
    void testReenterSameCarThrows() throws ParkingException {
        ParkingLot lot = new ParkingLot(5, 2, 1);

        lot.enter("NORMAL", "CAR1"); // первый въезд

        ParkingException ex = assertThrows(ParkingException.class, () -> lot.enter("NORMAL", "CAR1"));
        assertTrue(ex.getMessage().contains("Машина уже припаркована"), "Ожидалось исключение о повторной парковке");
    }

    @Test
    void testFullParkingThrows() throws ParkingException {
        ParkingLot lot = new ParkingLot(3, 1, 1);

        lot.enter("NORMAL", "N1");
        lot.enter("ELECTRIC", "E1");
        lot.enter("PREMIUM", "P1");

        assertThrows(ParkingException.class,
                () -> lot.enter("NORMAL", "N2"),
                "Попытка припарковать NORMAL на полной парковке");
        assertThrows(ParkingException.class,
                () -> lot.enter("ELECTRIC", "E2"),
                "Попытка припарковать ELECTRIC на полной парковке");
        assertThrows(ParkingException.class,
                () -> lot.enter("PREMIUM", "P2"),
                "Попытка припарковать PREMIUM на полной парковке");
    }

    @Test
    void testUnknownCarTypeThrows() {
        ParkingLot lot = new ParkingLot(2, 1, 1);
        ParkingException ex = assertThrows(ParkingException.class,
                () -> lot.enter("FLYING", "F1"),
                "Попытка припарковать машину неизвестного типа");
        assertTrue(ex.getMessage().contains("Неизвестный тип машины"));
    }

    @Test
    void testLeaveAllCars() throws ParkingException {
        ParkingLot lot = new ParkingLot(3, 1, 1);
        lot.enter("NORMAL", "N1");
        lot.enter("PREMIUM", "P1");
        lot.enter("ELECTRIC", "E1");

        lot.leave("N1");
        lot.leave("P1");
        lot.leave("E1");
        assertTrue(lot.isEmpty(), "После выезда всех машин парковка должна быть пустой");
        assertEquals(0, lot.getNumbers().size(), "Количество машин на парковке должно быть 0");
    }
}
