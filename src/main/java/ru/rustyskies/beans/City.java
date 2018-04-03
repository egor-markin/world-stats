package ru.rustyskies.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum City {

    Berlin("Berlin", Country.Germany),
    Moscow("Moscow", Country.Russia);

    public final String name;
    public final Country country;

}
