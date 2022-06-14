package com.blockwit.booking.service;

import com.blockwit.booking.entity.Booking;
import com.blockwit.booking.entity.Hotel;
import com.blockwit.booking.entity.User;
import com.blockwit.booking.exceptions.HotelNotFoundException;
import com.blockwit.booking.exceptions.UserNotFoundException;
import com.blockwit.booking.repository.BookingRepository;
import com.blockwit.booking.repository.HotelRepository;
import com.blockwit.booking.repository.RoomRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class BookingService {

    private BookingRepository bookingRepository;
    private HotelRepository hotelRepository;
    private RoomRepository roomRepository;

    private UserService userService;

    public Hotel bookHotel(long hotelId, String userName)
            throws HotelNotFoundException, UserNotFoundException{

        Hotel hotelForBook = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException());

        User user=userService.getUserByUsername(userName)
                .orElseThrow(()->new UserNotFoundException());

        Booking booking = Booking.builder()
                .userId(user.getId())
                .build();
        bookingRepository.save(booking);

        return hotelForBook;
    }

}