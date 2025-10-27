package web_7.Stations.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import web_7.RouteStations.model.RouteStationModel;

import java.util.List;

@Entity
@Table(name = "stations")
public class StationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50, message = "Название станции должно содержать не более 50 символов")
    @NotNull(message = "Название станции не может быть null")
    @NotBlank(message = "Название станции не может быть пустым")
    @Column(unique = true)
    private String stationName;

    @Size(max = 50, message = "Город должен содержать не более 50 символов")
    @NotNull(message = "Город не может быть null")
    @NotBlank(message = "Город не может быть пустым")
    private String city;

    @Size(max = 50, message = "Страна должна содержать не более 50 символов")
    @NotNull(message = "Страна не может быть null")
    @NotBlank(message = "Страна не может быть пустая")
    private String country;

    @OneToMany(mappedBy = "station", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<RouteStationModel> routeStations;

    public StationModel() {}

    public StationModel(Long id, String stationName, String city, String country) {
        this.id = id;
        this.stationName = stationName;
        this.city = city;
        this.country = country;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getStationName() {
        return stationName;
    }
    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public List<RouteStationModel> getRouteStations() {
        return routeStations;
    }
    public void setRouteStations(List<RouteStationModel> routeStations) {
        this.routeStations = routeStations;
    }
}