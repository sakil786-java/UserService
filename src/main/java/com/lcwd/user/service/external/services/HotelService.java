package com.lcwd.user.service.external.services;

import com.lcwd.user.service.entities.Hotel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="HOTEL-SERVICE")
public interface HotelService
{
    @GetMapping("/hotels/{hotelID}")
    Hotel getHotel(@PathVariable String hotelID);

    //
    @GetMapping("/hotels")
    List<Hotel> getAllHotel();

}
