package com.blockwit.booking.service;

import com.blockwit.booking.entity.Room;
import com.blockwit.booking.exceptions.RoomNotFoundException;
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

    public Iterable<Room> rooms() {
        return roomRepository.findAll();
    }

    public boolean saveRoomResponse(Room room) {
        return roomRepository.save(room) != null;
    }

    public Optional<Room> showDetail(Long roomId) {
        return roomRepository.findById(roomId);
    }

    public Room roomUpdate(long roomId, Room room) throws RoomNotFoundException {
        Room roomForUpdate = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException());
        roomForUpdate.setName(room.getName());
        roomForUpdate.setDescription(room.getDescription());
        return roomRepository.save(roomForUpdate);
    }

    public Iterable<Room> getBookedRooms(Long userId) throws RoomNotFoundException {
        return roomRepository.getBookedRoomsOrderedByName(userId);
    }

    public Iterable<Room> getHotelRooms(Long hotelId) throws RoomNotFoundException {
        return roomRepository.getRoomByHotelIdOrderedByName(hotelId);
    }
}
