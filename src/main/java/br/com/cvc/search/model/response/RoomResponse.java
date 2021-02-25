package br.com.cvc.search.model.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RoomResponse {
    private long roomID;
    private String categoryName;
    private BigDecimal totalPrice;
    private PriceDetailResponse priceDetail;
}
