package com.blockwit.booking.service;

import com.blockwit.booking.entity.Room;
import com.blockwit.booking.repository.RoomRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@AllArgsConstructor
@Slf4j
public class RoomService {

    private RoomRepository roomRepository;

    public Iterable<Room> hotels() {
        return roomRepository.findAll();
    }

    public boolean saveRoomResponse(Room room) {
        return roomRepository.save(room) != null;
    }
//
//    public Optional<Hotel> showDetail(Long hotelId) throws HotelNotFoundException {
//        return hotelRepository.findById(hotelId);
//    }
//
//    public Hotel hotelUpdate(long hotelId, Hotel hotel) throws HotelNotFoundException {
//        Hotel hotelForUpdate = hotelRepository.findById(hotelId).orElseThrow(() -> new HotelNotFoundException());
//        hotelForUpdate.setName(hotel.getName());
//        hotelForUpdate.setDescription(hotel.getDescription());
//        return hotelRepository.save(hotelForUpdate);
//    }

    public Optional<Room> getBookedRooms(Long userId) {
        return roomRepository.getBookedRooms(userId);
    }

}
