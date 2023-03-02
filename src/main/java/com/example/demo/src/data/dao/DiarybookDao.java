package com.example.demo.src.data.dao;

import com.example.demo.src.data.entity.DiarybookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DiarybookDao {
    @Autowired
    DiarybookRepository diarybookRepository;
}
