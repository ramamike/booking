package com.blockwit.booking.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name="pictures")
public class Picture {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long owner_id;

    private String path;

    private String format;

//    @Lob
//    private byte[] data;
}
