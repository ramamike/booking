package com.blockwit.booking.service;

import com.blockwit.booking.entity.Booking;
import com.blockwit.booking.entity.Hotel;
import com.blockwit.booking.entity.Room;
import com.blockwit.booking.entity.User;
import com.blockwit.booking.exceptions.HotelNotFoundException;
import com.blockwit.booking.exceptions.RoomNotFoundException;
import com.blockwit.booking.exceptions.UserNotFoundException;
import com.blockwit.booking.repository.BookingRepository;
import com.blockwit.booking.repository.HotelRepository;
import com.blockwit.booking.repository.RoomRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class BookingService {

    private BookingRepository bookingRepository;
    private HotelRepository hotelRepository;
    private RoomRepository roomRepository;

    private UserService userService;

    public boolean bookRoom(long roomId, String userName)
            throws  UserNotFoundException, RoomNotFoundException {

        User user=userService.getUserByUsername(userName)
                .orElseThrow(()->new UserNotFoundException());

        Room room=roomRepository.findById(roomId)
                .orElseThrow(()->new RoomNotFoundException());

        Booking booking = Booking.builder()
                .userId(user.getId())
                .roomId(room.getId())
//                .rooms(List.of(room))
                .build();
        return bookingRepository.save(booking)!=null;
    }

}