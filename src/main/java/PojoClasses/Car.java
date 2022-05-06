package PojoClasses;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    private Integer id;
    private String engineType;
    private String mark;
    private String model;
    private Double price;
}
