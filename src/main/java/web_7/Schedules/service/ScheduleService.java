package web_7.Schedules.service;

import org.springframework.data.domain.Page;
import web_7.Schedules.model.ScheduleModel;

import java.util.List;
import java.util.Optional;

public interface ScheduleService {
    Page<ScheduleModel> getAllSchedules(int page, int size);
    void addSchedule(ScheduleModel schedule);
    void updateSchedule(ScheduleModel schedule);
    void deleteSchedule(Long scheduleId);
    Optional<ScheduleModel> findById(Long scheduleId);
    List<ScheduleModel> searchSchedules(String keyword, String searchBy);
    List<ScheduleModel> getAllSchedules();
}