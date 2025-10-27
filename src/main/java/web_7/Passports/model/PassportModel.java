package web_7.Passports.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import web_7.Users.model.UserModel;

import java.time.LocalDate;

@Entity
@Table(name = "passports", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"serial", "number"})
})
public class PassportModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Серия паспорта не может быть null")
    @Min(value = 1000, message = "Серия паспорта должна быть 4-значным числом")
    @Max(value = 9999, message = "Серия паспорта должна быть 4-значным числом")
    @Positive(message = "Серия паспорта должна быть положительным числом")
    private Short serial;

    @NotNull(message = "Серия паспорта не может быть null")
    @Min(value = 100000, message = "Номер паспорта должен быть 6-значным числом")
    @Max(value = 999999, message = "Номер паспорта должен быть 6-значным числом")
    @Positive(message = "Номер паспорта должен быть положительным числом")
    private Integer number;

    @NotNull(message = "Дата регистрации паспорта не может быть null")
    @PastOrPresent(message = "Дата регистрации паспорта должна содержать прошедшую дату или сегодняшнее число")
    private LocalDate DatePassport;

    @OneToOne(mappedBy = "passport")
    private UserModel user;

    public PassportModel() {}

    public PassportModel(Long id, Short serial, Integer number, LocalDate datePassport) {
        this.id = id;
        this.serial = serial;
        this.number = number;
        DatePassport = datePassport;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Short getSerial() {
        return serial;
    }
    public void setSerial(Short serial) {
        this.serial = serial;
    }

    public Integer getNumber() {
        return number;
    }
    public void setNumber(Integer number) {
        this.number = number;
    }

    public LocalDate getDatePassport() {
        return DatePassport;
    }
    public void setDatePassport(LocalDate datePassport) {
        DatePassport = datePassport;
    }

    public UserModel getUser() {
        return user;
    }
    public void setUser(UserModel user) {
        this.user = user;
    }

    public String serial_number() {
        return serial.toString() + '-' + number.toString();
    }
}