package web_7.Users.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import web_7.PaginationValidUtils;
import web_7.Passports.model.PassportModel;
import web_7.Reviews.model.ReviewModel;
import web_7.Tickets.model.TicketModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name="passport_id")
    private PassportModel passport;

    @ElementCollection(targetClass = roles.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<roles> roles;

    @Size(max = 30, message = "Имя должно содержать не более 30 символов")
    @NotNull(message = "Имя не может быть null")
    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @Size(max = 30, message = "Фамилия должна содержать не более 30 символов")
    @NotNull(message = "Фамилия не может быть null")
    @NotBlank(message = "Фамилия не может быть пустой")
    private String surname;

    @Size(max = 30, message = "Отчество должно содержать не более 30 символов")
    private String middleName;

    @Size(min = 6, max = 30, message = "Логин должен содержать от 6 до 30 символов")
    @NotNull(message = "Логин не может быть null")
    @NotBlank(message = "Логин не может быть пустым")
    @Column(unique = true)
    private String login;

    @Size(min = 8, message = "Пароль должен содержать от 8 символов")
    @NotNull(message = "Пароль не может быть null")
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;

    @Size(max = 70, message = "Email должен содержать не более 70 символов")
    @Email(message = "Некорректный формат email")
    @Column(unique = true)
    private String email;

    @Size(min = 11, max = 11, message = "Номер телефона должен содержать 11 цифр")
    @NotNull(message = "Номер телефона не может быть null")
    @NotBlank(message = "Номер телефона не может быть пустым")
    @Pattern(regexp = "^8\\d{10}$", message = "Номер должен быть в формате: 81234567890 (11 цифр)")
    @Column(unique = true)
    private String phoneNumber;

    @PastOrPresent(message = "Дата регистрации не может быть в будущем")
    private LocalDateTime registrationDate;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<ReviewModel> review;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<TicketModel> tickets;

    private boolean active;

    public UserModel() {}

    public UserModel(Long id, PassportModel passport, Set<roles> roles, String name, String surname, String middleName, String login,
                     String password, String email, String phoneNumber, LocalDateTime registrationDate, boolean active) {
        this.id = id;
        this.passport = passport;
        this.roles = roles;
        this.name = name;
        this.surname = surname;
        this.middleName = middleName;
        this.login = login;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.registrationDate = registrationDate;
        this.active = active;
    }

    public Long getId() {
        return id;
    }
    public PassportModel getPassport() {
        return passport;
    }
    public Set<roles> getRoles() {
        return roles;
    }
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public String getMiddleName() {
        return middleName;
    }
    public String getLogin() {
        return login;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setPassport(PassportModel passport) {
        this.passport = passport;
    }
    public void setRoles(Set<roles> roles) {
        this.roles = roles;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public List<ReviewModel> getReview() {
        return review;
    }
    public void setReview(List<ReviewModel> review) {
        this.review = review;
    }

    public List<TicketModel> getTickets() {
        return tickets;
    }
    public void setTickets(List<TicketModel> tickets) {
        this.tickets = tickets;
    }

    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

    public String getFormattedRegistrationDate() {
        if (registrationDate == null) {
            return null;
        }
        return registrationDate.format(PaginationValidUtils.DATE_TIME_FORMATTER);
    }

    public String getFormattedRoles() {
        if (roles == null || roles.isEmpty()) {
            return "не указаны";
        }
        return roles.stream()
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }
}
