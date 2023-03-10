package com.example.demo.src.data.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "from_status")
@NoArgsConstructor
public class FromStatus {
    @Id
    public int coupleId;
    @Column
    public int fromCount;
    @Column
    public boolean todayFromFlag;
    @Builder
    public FromStatus(int coupleId, int fromCount, boolean todayFromFlag){
        this.coupleId = coupleId;
        this.fromCount = fromCount;
        this.todayFromFlag = todayFromFlag;
    }
    public int getTodayFrom(){
        this.todayFromFlag=true;
        this.fromCount = this.getFromCount()+3;
        return this.fromCount;
    }

    public int useFrom(int count){this.fromCount -= count; return this.fromCount;}
    public int getFrom(int count){this.fromCount += count; return this.fromCount;}
}
