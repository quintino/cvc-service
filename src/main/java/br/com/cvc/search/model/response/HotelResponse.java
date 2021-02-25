package br.com.cvc.search.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HotelResponse {
    private long id;
    private String cityName;
    private List<RoomResponse> rooms;
}
