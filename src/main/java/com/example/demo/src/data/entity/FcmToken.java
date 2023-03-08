package com.example.demo.src.data.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Getter
@Entity
@Table(name="fcm_token")
@NoArgsConstructor
public class FcmToken {
    @Id
    public int userId;
    @Column
    public String tokenValue;

}
