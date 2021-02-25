package br.com.cvc.search.service;

import br.com.cvc.search.model.Hotel;
import br.com.cvc.search.model.response.HotelResponse;
import br.com.cvc.search.model.response.PriceDetailResponse;
import br.com.cvc.search.model.Room;
import br.com.cvc.search.model.response.RoomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BudgetService {
    private final static double COMISSION = 0.7d;

    @Autowired
    HotelAPIService hotelAPIService;

    public List<HotelResponse> calculateByCity(final long cityCode, final String checkIn, final String checkOut,
                                         final int adults, final int children) {
        final List<HotelResponse> result = new ArrayList<>();
        final List<Hotel> hotels = this.hotelAPIService.getHotelsByCityId(String.valueOf(cityCode));
        if (hotels != null) {
            try {
                final long days = this.travelDays(checkIn, checkOut);
                if (days >= 0) {
                    for (Hotel hotel : hotels) {
                        result.add(this.calculateHotel(hotel, days, adults, children));
                    }
                }
            } catch (Exception exp) {
                exp.printStackTrace();
                return new ArrayList<>();
            }
        }
        return result;
    }

    private long travelDays(final String checkIn, final String checkOut) throws Exception {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        final Date chkIn = simpleDateFormat.parse(checkIn);
        final Date chkOut = simpleDateFormat.parse(checkOut);
        return (chkOut.getTime() - chkIn.getTime()) / (1000*60*60*24);
    }

    private HotelResponse calculateHotel(final Hotel hotel, final long days, final int adults, final int children) {
        final HotelResponse response = new HotelResponse();
        response.setId(hotel.getId());
        response.setCityName(hotel.getCityName());
        response.setRooms(new ArrayList<>());
        for (Room room: hotel.getRooms()) {
            final PriceDetailResponse priceDetailResponse = new PriceDetailResponse();
            priceDetailResponse.setPricePerDayAdult(room.getPrice().getAdult());
            priceDetailResponse.setPricePerDayChild(room.getPrice().getChild());
            final RoomResponse roomResponse = new RoomResponse();
            roomResponse.setRoomID(room.getRoomID());
            roomResponse.setCategoryName(room.getCategoryName());
            roomResponse.setTotalPrice(this.totalPrice(days, priceDetailResponse, adults, children));
            roomResponse.setPriceDetail(priceDetailResponse);
            response.getRooms().add(roomResponse);
        }
        return response;
    }

    private BigDecimal totalPrice(final long days, final PriceDetailResponse priceDetailResponse, final int adults,
                              final int children) {
        final double adult = priceDetailResponse.getPricePerDayAdult() * days * adults;
        final double child = priceDetailResponse.getPricePerDayChild() * days * children;
        final double total = adult + child;
        return new BigDecimal(total / BudgetService.COMISSION).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public HotelResponse calculateByHotel(final long hotelId, final String checkIn, final String checkOut,
                                                final int adults, final int children) {
        final Hotel hotel = this.hotelAPIService.getByHotelId(String.valueOf(hotelId));
        if (hotel != null) {
            try {
                final long days = this.travelDays(checkIn, checkOut);
                if (days >= 0) {
                    return this.calculateHotel(hotel, days, adults, children);
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }
        return null;
    }
}
