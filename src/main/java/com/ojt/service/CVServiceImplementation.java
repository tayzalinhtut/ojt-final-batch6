package com.ojt.service;

import com.ojt.entity.Batch;
import com.ojt.entity.CV;
import com.ojt.enumeration.StatusType;
import com.ojt.repository.BatchRepository;
import com.ojt.repository.CVRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CVServiceImplementation implements CVService {
    @Autowired
    private CVRepository cvRepository;

    @Autowired
    private BatchRepository batchRepository;

}
