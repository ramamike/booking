package com.blockwit.booking.entity;

import com.blockwit.booking.model.Role;
import com.blockwit.booking.model.Status;
import com.sun.istack.NotNull;
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
@Table(name="tbl_users")
public class User {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName ="user_sequence" ,
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long userId;

    @Column(unique = true, name = "email")
    @NotNull
    private String email;

    @Column(name = "firstName")
    @NotNull
    private String firstName;

    @Column(name = "lastName")
    @NotNull
    private String lastName;

    @Column(name = "password")
    @NotNull
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role", columnDefinition = "varchar(20) default 'USER'")
    @NotNull
    private Role role;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(20) default 'ACTIVE'")
    @NotNull
    private Status status;
}
