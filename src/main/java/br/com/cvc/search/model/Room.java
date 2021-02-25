package br.com.cvc.search.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Room {
    private long roomID;
    private String categoryName;
    private Price price;
}
