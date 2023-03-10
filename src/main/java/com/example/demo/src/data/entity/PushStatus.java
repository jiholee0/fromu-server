package com.example.demo.src.data.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Entity
@Table(name = "push_status")
@NoArgsConstructor
public class PushStatus {
    @Id
    public int userId;
    @Column
    public int pushCount;
    @Column
    public Date chargeTime;
    @Builder
    public PushStatus(int userId, int pushCount){
        this.userId = userId;
        this.pushCount = pushCount;
    }

    public int push(){this.pushCount -= 1; return this.pushCount;}
    public int charge(){
        this.pushCount = 15;
        return this.pushCount;
    }
    public void startCharge(){
        this.chargeTime = new Date();
    }
}
