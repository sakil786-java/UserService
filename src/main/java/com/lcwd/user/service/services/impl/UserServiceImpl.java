package com.lcwd.user.service.services.impl;

import com.lcwd.user.service.entities.Hotel;
import com.lcwd.user.service.entities.Rating;
import com.lcwd.user.service.entities.User;
import com.lcwd.user.service.exception.ResourceNotFoundException;
import com.lcwd.user.service.external.services.HotelService;
import com.lcwd.user.service.external.services.RatingService;
import com.lcwd.user.service.repository.UserRepository;
import com.lcwd.user.service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private RatingService ratingService;

    @Override
    public User saveUSer(User user) {

        //Unique User Id
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        List<User> userList = userRepository.findAll();
        List<User> newUserList = new ArrayList<>();
        List<Hotel> allHotel = hotelService.getAllHotel();
        for (User u : userList) {
            String userid = u.getUserId();
            List<Rating> ratingList = ratingService.getRatingsByUserId(userid);


            for (Rating r : ratingList) {

                String hotelId = r.getHotelId();
                Hotel hotel = hotelService.getHotel(hotelId);
                r.setHotel(hotel);
                List<Rating> newRatingList = new ArrayList<>();
                newRatingList.add(r);
                u.setRatings(newRatingList);
            }


            newUserList.add(u);

        }


        return newUserList;
    }

    @Override
    public User getUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User With Given ID is not found!!! " + userId));
        Rating[] ratingOfUser = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/" + user.getUserId(), Rating[].class);

        List<Rating> ratings = Arrays.stream(ratingOfUser).toList();

        List<Rating> ratingList = ratings.stream().map(rating -> {
            //api call to hotel service to get the hotel
            //http://localhost:8082/hotels/41e4464e-ebfa-4f95-83b0-605d898ed626
//            ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/" + rating.getHotelId(), Hotel.class);
//            Hotel hotel = forEntity.getBody();
//               ||
//               ||
            //Same Process using feign Client by using HotelService
            Hotel hotel = hotelService.getHotel(rating.getHotelId());

            //set the hotel to rating

            rating.setHotel(hotel);
            //return the rating

            return rating;

        }).collect(Collectors.toList());
        user.setRatings(ratingList);

        return user;
    }

    @Override
    public String deleteUser(String userId) {
        User findUser = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User With Given ID is not found!!! " + userId));
        if (findUser != null) {
            userRepository.deleteById(userId);
        }

        return findUser.getUserId() + " Deleted Successfully";
    }
}
