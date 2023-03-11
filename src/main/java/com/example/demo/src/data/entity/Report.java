package com.example.demo.src.data.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "report")
@NoArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int reportId;
    @Column
    public String content;
    @Column
    public int reporterUserId;
    @Column
    public int letterId;

    @Builder
    public Report(String content, int reporterUserId, int letterId) {
        this.content = content;
        this.reporterUserId = reporterUserId;
        this.letterId = letterId;
    }
}
