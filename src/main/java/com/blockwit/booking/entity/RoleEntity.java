package com.blockwit.booking.entity;

import com.blockwit.booking.model.Role;
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
@Table(name = "tbl_roles")
public class RoleEntity {
    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role", columnDefinition = "varchar(20) default 'USER'")
    @NotNull
    private Role role;
}
