package com.blockwit.booking.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="tbl_hotel")
public class Hotel {

    @Id
    @SequenceGenerator(
            name = "hotel_sequence",
            sequenceName ="hotel_sequence" ,
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "hotel_sequence"
    )
    private Long hotelId;

    private String name;

    private String description;

//    private

}
