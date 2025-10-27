package web_7.Routes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import web_7.RouteStations.model.RouteStationModel;
import web_7.Schedules.model.ScheduleModel;

import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "routes")
public class RouteModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Начальная станция не может быть null")
    @NotBlank(message = "Начальная станция не может быть пустой")
    @Size(max = 50, message = "Название начальной станции не должно превышать 50 символов")
    private String startStation;

    @NotNull(message = "Конечная станция не может быть null")
    @NotBlank(message = "Конечная станция не может быть пустой")
    @Size(max = 50, message = "Название конечной станции не должно превышать 50 символов")
    private String endStation;

    @NotNull(message = "Расстояние не может быть null")
    @Min(value = 1, message = "Расстояние должно быть не менее 1 км")
    @Max(value = 1000000, message = "Расстояние должно быть не более 1000000 км")
    private Integer distance;

    @NotNull(message = "Время в пути не может быть null")
    private LocalTime travelTime;

    @OneToMany(mappedBy = "route", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<RouteStationModel> routeStations;

    @OneToMany(mappedBy = "route", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<ScheduleModel> schedule;

    public RouteModel() {}

    public RouteModel(Long id, String startStation, String endStation, Integer distance, LocalTime travelTime) {
        this.id = id;
        this.startStation = startStation;
        this.endStation = endStation;
        this.distance = distance;
        this.travelTime = travelTime;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getStartStation() {
        return startStation;
    }
    public void setStartStation(String startStation) {
        this.startStation = startStation;
    }

    public String getEndStation() {
        return endStation;
    }
    public void setEndStation(String endStation) {
        this.endStation = endStation;
    }

    public Integer getDistance() {
        return distance;
    }
    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public LocalTime getTravelTime() {
        return travelTime;
    }
    public void setTravelTime(LocalTime travelTime) {
        this.travelTime = travelTime;
    }

    public List<RouteStationModel> getRouteStations() {
        return routeStations;
    }
    public void setRouteStations(List<RouteStationModel> routeStations) {
        this.routeStations = routeStations;
    }

    public List<ScheduleModel> getSchedule() {
        return schedule;
    }
    public void setSchedule(List<ScheduleModel> schedule) {
        this.schedule = schedule;
    }

    public String getRoute() {
        return startStation + "-" + endStation;
    }
}