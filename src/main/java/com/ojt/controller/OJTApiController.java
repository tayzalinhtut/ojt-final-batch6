
package com.ojt.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ojt.dto.OJTCustomDTO;
import com.ojt.dto.OJTDTO;
import com.ojt.entity.OJT;
import com.ojt.service.AttendanceService;
import com.ojt.service.OJTService;

@RestController
@RequestMapping("/admin/api")
public class OJTApiController {

    @Autowired
    private OJTService ojtService;

    @Autowired
    private AttendanceService attendanceService;

    // API to get paginated OJT members in JSON format
    @GetMapping("/ojt-members")
    public ResponseEntity<Map<String, Object>> getOjtMembers(@RequestParam(name = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 5);
        Page<OJT> ojtPage = ojtService.getAllOJTWithoutAttendance(pageable);

        List<OJTCustomDTO> dashboardData = new ArrayList<>();

        for(OJT ojt : ojtPage.getContent()) {
            Long ojtId = ojt.getId();
            Long batchId = ojt.getCv().getBatch().getId();

            OJTDTO ojtDto = new OJTDTO();

            ojtDto.setName(ojt.getCv().getName());
            ojtDto.setBatchName(ojt.getCv().getBatch().getName());
            ojtDto.setStatusName(ojt.getStatus().getStatusType());
            ojtDto.setId(ojt.getId());
            ojtDto.setBankAccount(ojt.getBankAccount());
            ojtDto.setCvId(ojt.getCv().getId());
            ojtDto.setBatchId(ojt.getCv().getBatch().getId());
            ojtDto.setStatusId(ojt.getStatus().getId());
            ojtDto.setEmail(ojt.getCv().getEmail());

            int attendance = (int) attendanceService.calculatedAttendancePercentage(ojtId, batchId);
            System.out.println("Attendance : " + attendance);

            dashboardData.add(new OJTCustomDTO(ojtDto, attendance));
        }

        Map<String, Object> res = new HashMap<>();

        res.put("data", dashboardData);
        res.put("currentPage", ojtPage.getNumber());
        res.put("totalPages", ojtPage.getTotalPages());
        res.put("totalElements", ojtPage.getTotalElements());

        return ResponseEntity.ok(res);
    }
}