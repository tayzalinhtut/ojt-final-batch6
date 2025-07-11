package com.ojt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ojt.entity.Holiday;
import com.ojt.repository.HolidayRepository;

@Service
public class HolidayServiceImplementation implements HolidayService {

    @Autowired
    private HolidayRepository holidayRepository;

    @Override
    public List<Holiday> getHolidays() {
        // TODO Auto-generated method stub
        return holidayRepository.findAll();
    }



}