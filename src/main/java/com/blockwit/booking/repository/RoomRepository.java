package com.blockwit.booking.repository;

import com.blockwit.booking.entity.Room;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoomRepository extends CrudRepository<Room, Long> {

    @Query("SELECT r FROM Room r join r.bookings b where b.userId=?1")
    Iterable<Room> getBookedRoomsOrderedByName(Long userId);

    @Query("SELECT r FROM Room r join r.hotel h where h.id=?1")
    Iterable<Room> getRoomByHotelIdOrderedByName(Long hotelId);

}
