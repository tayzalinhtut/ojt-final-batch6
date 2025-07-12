package com.ojt.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ojt.dto.CustomAttendanceDTO;
import com.ojt.dto.CustomBatchDataDTO;
import com.ojt.dto.CustomBatchDataMonthDTO;
import com.ojt.dto.CustomBatchDataStudentDTO;
import com.ojt.dto.DropoffReportDTO;
import com.ojt.dto.FunnelReportDTO;
import com.ojt.entity.Attendance;
import com.ojt.entity.Batch;
import com.ojt.entity.Evaluation;
import com.ojt.entity.Holiday;
import com.ojt.entity.OJT;
import com.ojt.service.AttendanceService;
import com.ojt.service.BatchService;
import com.ojt.service.EvaluationService;
import com.ojt.service.HolidayService;
import com.ojt.service.OJTService;
import com.ojt.service.ReportService;

@RestController
@RequestMapping("/admin/api/report")
public class ReportApiController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private BatchService batchService;

    @Autowired
    private OJTService ojtService;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private EvaluationService evaluationService;

    @GetMapping("/report-data")
    public ResponseEntity<Map<String, Object>> getReportData(@RequestParam(name = "year", defaultValue = "0") int year,
                                                             @RequestParam(name = "batchId", defaultValue = "0") Long batchId) {
        Map<String, Object> res = new HashMap<>();

        try {
            // is drop-off chart, doesn't need to pass cv received data to client
            Map<Integer, Object> funnelReport = null;
            int cvReceived = reportService.calculateCvPerYear(year, batchId);
            int scannPassCv = reportService.calculateCvScan(year, batchId, false);
            int codeTestPassCv = reportService.calculateCodeTest(year, batchId, false);
            int interviewPassCv = reportService.calculateInterview(year, batchId, false);
            int offerAcceptCv = reportService.calculateOffer(year, batchId, false);

            // for funnel chart
            FunnelReportDTO funnelReportData = new FunnelReportDTO();
            // for key-value pair(e.g. year => 2021: {...data}, batch => batch 1:
            // {....data})
            funnelReport = new HashMap<>();

            funnelReportData.setCvReceived(cvReceived);
            funnelReportData.setScanPassCv(scannPassCv);
            funnelReportData.setCodeTestPassCv(codeTestPassCv);
            funnelReportData.setInterviewPassCv(interviewPassCv);
            funnelReportData.setOfferAcceptCv(offerAcceptCv);

            if (year > 0) {
                funnelReport.put(year, funnelReportData);
            } else {
                int batchIdCast = batchId.intValue();
                funnelReport.put(batchIdCast, funnelReportData);
            }

            // for drop-off chart
            Map<Integer, Object> dropoffReport = null;
            int scannFailCv = reportService.calculateCvScan(year, batchId, true);
            int codeTestFailCv = reportService.calculateCodeTest(year, batchId, true);
            int interviewFailCv = reportService.calculateInterview(year, batchId, true);
            int offerRejectCv = reportService.calculateOffer(year, batchId, true);

            dropoffReport = new HashMap<>();
            DropoffReportDTO dropoffReportData = new DropoffReportDTO();

            dropoffReportData.setScanFailCv(scannFailCv);
            dropoffReportData.setCodeTestFailCv(codeTestFailCv);
            dropoffReportData.setInterviewFailCv(interviewFailCv);
            dropoffReportData.setOfferRejectCv(offerRejectCv);

            if (year > 0) {
                dropoffReport.put(year, dropoffReportData);
            } else {
                int batchIdCast = batchId.intValue();
                dropoffReport.put(batchIdCast, dropoffReportData);
            }

            System.out.println("Year param: " + year);

            // batch data for attendance
            // for sending data to UI
            Map<Integer, CustomBatchDataDTO> batchData = new LinkedHashMap<Integer, CustomBatchDataDTO>();

            // get all batch
            List<Batch> batches = batchService.getAllBatches();

            // calculate batch date between two months
            Map<String, String> rawBatchDates = new LinkedHashMap<String, String>();

            for (Batch batch : batches) {
                System.out.println("Start Date" + batch.getStartDate());
                System.out.println("End date" + batch.getEndDate());

                // DTO for CustomBatchDataDTO
                CustomBatchDataDTO batchDataDTO = new CustomBatchDataDTO();

                rawBatchDates = reportService.getMonthsBetween(batch.getStartDate(), batch.getEndDate());
                batchDataDTO.setName(batch.getName());
                batchDataDTO.setStartDate(batch.getStartDate().toString());
                batchDataDTO.setEndDate(batch.getEndDate().toString());

                // get all student
                List<OJT> students = ojtService.getOJTByBatch(batch.getId());

                // collect as array month key-value pair
                List<CustomBatchDataMonthDTO> formattedEntries = new ArrayList<>();

                // add key-value pair data to array
                for (Map.Entry<String, String> entry : rawBatchDates.entrySet()) {
                    CustomBatchDataMonthDTO monthDto = new CustomBatchDataMonthDTO(); // NEW object each time
                    monthDto.setValue(entry.getKey());
                    monthDto.setName(entry.getValue());
                    formattedEntries.add(monthDto); // Add the new object

                    System.out.println("Added: " + monthDto.getValue() + " - " + monthDto.getName());
                }

                List<CustomBatchDataStudentDTO> formattedEntriesforStudent = new ArrayList<>();

                // add student data to array
                for (OJT s : students) {
                    CustomBatchDataStudentDTO customBatchDataStudent = new CustomBatchDataStudentDTO();
                    customBatchDataStudent.setId(s.getId().intValue());
                    customBatchDataStudent.setName(s.getCv().getName());
                    formattedEntriesforStudent.add(customBatchDataStudent);
                }

                batchDataDTO.setMonths(formattedEntries);
                batchDataDTO.setStudents(formattedEntriesforStudent);

                batchData.put(batch.getId().intValue(), batchDataDTO);
            }

            // for holidays sending to UI
            List<Holiday> holidays = holidayService.getHolidays();
            Map<String, List<Map<String, String>>> holidayData = new LinkedHashMap<>();

            // Group holidays by month (e.g., "2024-01")
            for (Holiday holiday : holidays) {
                LocalDate date = holiday.getDate();
                String monthKey = YearMonth.from(date).toString();

                // Create the month entry if it doesn't exist
                holidayData.putIfAbsent(monthKey, new ArrayList<>());

                // Add holiday to month group
                Map<String, String> holidayEntry = new LinkedHashMap<>();
                holidayEntry.put("date", date.toString());
                holidayEntry.put("name", holiday.getName());
                holidayData.get(monthKey).add(holidayEntry);
            }

            System.out.println("holiday data: " + holidayData);

            // 1. Initialize the attendance data structure
            // Structure: Batch ID -> Month -> List of Student Attendance Records
            Map<Integer, Map<String, List<CustomAttendanceDTO>>> attendanceData = new LinkedHashMap<>();

            // 2. Process each batch
            for (Batch batch : batches) {
                int batchid = batch.getId().intValue();
                LocalDate batchStart = batch.getStartDate();
                LocalDate batchEnd = batch.getEndDate();

                // 3. Get all months in this batch's duration ({"2025-01": "JANUARY"})
                Map<String, String> monthData = reportService.getMonthsBetween(batchStart, batchEnd);

                // 4. Initialize batch entry if not exists
                attendanceData.putIfAbsent(batchid, new LinkedHashMap<>());

                // 5. Process each month in this batch
                for (Map.Entry<String, String> monthEntry : monthData.entrySet()) {
                    String monthKey = monthEntry.getKey(); // "2025-01"

                    // 6. Initialize month entry if not exists
                    attendanceData.get(batchid).putIfAbsent(monthKey, new ArrayList<>());

                    // 7. Get all students in this batch
                    List<OJT> batchStudents = ojtService.getOJTByBatch(batch.getId());

                    // 8. Process each student's attendance for this month
                    for (OJT student : batchStudents) {
                        // 9. Create attendance DTO for this student
                        CustomAttendanceDTO studentAttendance = new CustomAttendanceDTO();
                        studentAttendance.setStudentId(student.getId().intValue());
                        studentAttendance.setStudentName(student.getCv().getName());

                        // 10. Get daily attendance status for this month
                        List<String> dailyStatus = getMonthlyAttendanceStatus(student.getId(), monthKey, batch.getStartDate());
                        studentAttendance.setAttendance(dailyStatus);

                        // 11. Add to the month's attendance list
                        attendanceData.get(batchid).get(monthKey).add(studentAttendance);
                    }
                }
            }

            // for evaluation sending to UI
            Map<Long, Map<Long, Map<String, List<Integer>>>> evaluationData = new LinkedHashMap<>();

            Map<String, Integer> evaluationByOJT = new LinkedHashMap<>();

            // process each batch
            for (Batch batch : batches) {
                Long batchid = batch.getId();

                evaluationData.putIfAbsent(batchid, new LinkedHashMap<>());
                List<OJT> ojts = ojtService.getOJTByBatch(batchid);

                for (OJT ojt : ojts) {
                    List<Integer> evaluationMark = new ArrayList<>();
                    Long ojtId = ojt.getId();
                    evaluationData.get(batchid).putIfAbsent(ojt.getId(), new LinkedHashMap<>());

                    evaluationByOJT = evaluationService.getSummedSkillsByOjt(ojt.getId());
                    for (Map.Entry<String, Integer> eval : evaluationByOJT.entrySet()) {
                        System.out.println("Evaluation Mark Key: " + eval.getKey());
                        System.out.println("Evaluation Mark Value: " + eval.getValue());
                        evaluationMark.add(eval.getValue());
                        System.out.println(evaluationMark);
                    }
                    evaluationData.get(batchid).get(ojtId).put("scores", evaluationMark);
                }
            }

            res.put("funnelReportData", funnelReport);
            res.put("dropoffReportData", dropoffReport);
            res.put("holidays", holidayData);
            res.put("batchData", batchData);
            res.put("attendanceData", attendanceData);
            res.put("evaluationData", evaluationData);

            return ResponseEntity.ok(res);
        } catch (Exception e) {
            System.out.println("Print stack trace");
            e.printStackTrace();
            // Return error response with empty data structures
            res.put("error", "Failed to generate report data");
            res.put("funnelReportData", new HashMap<>());
            res.put("dropoffReportData", new HashMap<>());
            res.put("holidays", new HashMap<>());
            res.put("batchData", new HashMap<>());
            res.put("attendanceData", new HashMap<>());
            res.put("evaluationData", new HashMap<>());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
        }
    }

    private List<String> getMonthlyAttendanceStatus(Long studentId, String monthKey, LocalDate batchStartDate) {
        System.out.println("Batch start date: " + batchStartDate.getDayOfMonth());
        List<String> dailyAttendance = new ArrayList<>();
        YearMonth yearMonth = YearMonth.parse(monthKey);
        System.out.println("Year Month: " + yearMonth);
        LocalDate firstDay = yearMonth.atDay(batchStartDate.getDayOfMonth());
        LocalDate lastDay = yearMonth.atEndOfMonth();

        // Check attendance for each day in the month
        for (LocalDate date = firstDay; !date.isAfter(lastDay); date = date.plusDays(1)) {
            String status = attendanceService.getAttendanceStatus(studentId, date);
            dailyAttendance.add(status);
        }

        return dailyAttendance;
    }

}