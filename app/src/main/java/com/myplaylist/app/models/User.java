package com.myplaylist.app.models;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;


@Table(name = "USERS")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID", unique = true, nullable = false)
    private Long id;

    @Column(name = "USERNAME", nullable = false, length = 50, unique = true)
    private String username;

    @Column(name = "PASSWORD", nullable = false, length = 100)
    private String password;

    @ElementCollection
    @CollectionTable(name = "USER_LIKED_VIDEOS", joinColumns = @JoinColumn(name = "USER_ID"))
    @Column(name = "VIDEO_ID")
    private List<Long> likedVideoIds = new ArrayList<>();


}
