package com.blockwit.booking.service;

import com.blockwit.booking.entity.Room;
import com.blockwit.booking.exceptions.RoomNotFoundException;
import com.blockwit.booking.model.Error;
import com.blockwit.booking.repository.RoomRepository;
import com.blockwit.booking.service.Utils.WithOptional;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Optional<Room> getById(Long roomId) {
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

    //    @Transactional
    public Either<Error, Room> deleteById(Long roomId) {
        return WithOptional.process(roomRepository.findById(roomId),
                () -> Either.left(new Error(Error.ROOM_NOT_FOUND + roomId)),
                () -> {
                    Either<Error, Room> either = Either.right(roomRepository.findById(roomId).get());
                    System.out.println("process");
                    roomRepository.deleteById(roomId);
                    return either;
                }

        );
    }
}
