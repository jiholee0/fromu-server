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
@Table(name = "stamp_status")
@NoArgsConstructor
public class StampStatus {
    @Id
    public int coupleId;
    @Column
    public int stampCount1;
    @Column
    public int stampCount2;
    @Column
    public int stampCount3;
    @Column
    public int stampCount4;
    @Column
    public int stampCount5;
    @Column
    public int stampCount6;


    @Builder
    public StampStatus(int coupleId, int stampCount1, int stampCount2, int stampCount3, int stampCount4, int stampCount5, int stampCount6){
        this.coupleId = coupleId;
        this.stampCount1 = stampCount1;
        this.stampCount2 = stampCount2;
        this.stampCount3 = stampCount3;
        this.stampCount4 = stampCount4;
        this.stampCount5 = stampCount5;
        this.stampCount6 = stampCount6;
    }

    public void buyStamp1(){this.stampCount1 += 1;}
    public void buyStamp2(){this.stampCount2 += 1;}
    public void buyStamp3(){this.stampCount3 += 1;}
    public void buyStamp4(){this.stampCount4 += 1;}
    public void buyStamp5(){this.stampCount5 += 1;}
    public void buyStamp6(){this.stampCount6 += 1;}
    public void useStamp1(){this.stampCount1 -= 1;}
    public void useStamp2(){this.stampCount2 -= 1;}
    public void useStamp3(){this.stampCount3 -= 1;}
    public void useStamp4(){this.stampCount4 -= 1;}
    public void useStamp5(){this.stampCount5 -= 1;}
    public void useStamp6(){this.stampCount6 -= 1;}
}
