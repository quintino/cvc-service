package br.com.cvc.search.controller;

import br.com.cvc.search.model.Hotel;
import br.com.cvc.search.model.response.HotelResponse;
import br.com.cvc.search.service.BudgetService;
import br.com.cvc.search.service.HotelAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/hotel")
public class HotelController {

    @Autowired
    HotelAPIService hotelAPIService;

    @Autowired
    BudgetService budgetService;

    @GetMapping(path = "/city/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Hotel> getByCityId(@PathVariable("id") final String id) {
        return this.hotelAPIService.getHotelsByCityId(id);
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Hotel getByHotelId(@PathVariable("id") final String id) {
        return this.hotelAPIService.getByHotelId(id);
    }

    @GetMapping(path = "/budgetByCity")
    @ResponseStatus(HttpStatus.OK)
    public List<HotelResponse> requestBudgetByCity(
            @RequestParam(name = "CityCode") long cityCode,
            @RequestParam(name = "Checkin") String checkIn,
            @RequestParam(name = "Checkout") String checkOut,
            @RequestParam(name = "Adults") int adults,
            @RequestParam(name = "Children") int children
    ) {
        return this.budgetService.calculateByCity(cityCode, checkIn, checkOut, adults, children);
    }

    @GetMapping(path = "/budgetByHotel")
    @ResponseStatus(HttpStatus.OK)
    public HotelResponse requestBudgetByHotel(
            @RequestParam(name = "HotelID") long hotelId,
            @RequestParam(name = "Checkin") String checkIn,
            @RequestParam(name = "Checkout") String checkOut,
            @RequestParam(name = "Adults") int adults,
            @RequestParam(name = "Children") int children
    ) {
        return this.budgetService.calculateByHotel(hotelId, checkIn, checkOut, adults, children);
    }

}
