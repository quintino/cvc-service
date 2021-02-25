package br.com.cvc.search.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Hotel {
    private long id;
    private String name;
    private long cityCode;
    private String cityName;
    private List<Room> rooms;
}
