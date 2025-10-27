package web_7.Schedules.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import web_7.Schedules.model.ScheduleModel;
import web_7.Schedules.repository.ScheduleRepository;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public Page<ScheduleModel> getAllSchedules(int page, int size) {
        return scheduleRepository.findAllByOrderByIdAsc(PageRequest.of(page, size));
    }

    @Override
    public List<ScheduleModel> getAllSchedules() {
        return scheduleRepository.findAllByOrderByIdAsc();
    }

    @Override
    public void addSchedule(ScheduleModel schedule) {
        scheduleRepository.save(schedule);
    }

    @Override
    public void updateSchedule(ScheduleModel schedule) {
        scheduleRepository.save(schedule);
    }

    @Override
    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }

    @Override
    public Optional<ScheduleModel> findById(Long id) {
        return scheduleRepository.findById(id);
    }

    @Override
    public List<ScheduleModel> searchSchedules(String keyword, String searchBy) {
        List<ScheduleModel> schedules;
        switch (searchBy) {
            case "id":
                try {
                    Long id = Long.parseLong(keyword);
                    return scheduleRepository.findById(id).map(List::of).orElse(List.of());
                } catch (NumberFormatException e) {
                    return List.of();
                }
            case "departureDate":
                try {
                    LocalDate date = LocalDate.parse(keyword);
                    schedules = scheduleRepository.findByDepartureDate(date);
                    break;
                } catch (DateTimeParseException e) {
                    return List.of();
                }
            default:
                schedules = scheduleRepository.findAllByOrderByIdAsc();
        }
        return schedules.stream()
                .sorted(Comparator.comparing(ScheduleModel::getId))
                .collect(Collectors.toList());
    }
}