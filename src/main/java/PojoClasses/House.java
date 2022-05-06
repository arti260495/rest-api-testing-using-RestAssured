package PojoClasses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class House {
    private Integer id;
    private Integer floorCount;
    private Integer price;
    private List<ParkingPlace> parkingPlaces;
    private List<Object> lodgers;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParkingPlace{
        private Boolean isWarm;
        private Boolean isCovered;
        private Integer placesCount;
    }
}
