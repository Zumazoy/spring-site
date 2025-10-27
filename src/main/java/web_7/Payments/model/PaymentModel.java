package web_7.Payments.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import web_7.PaginationValidUtils;
import web_7.PaymentStatuses.model.PaymentStatusModel;
import web_7.Tickets.model.TicketModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class PaymentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name="ticket_id")
    private TicketModel ticket;

    @NotNull(message = "Статус платежа не выбран")
    @ManyToOne
    @JoinColumn(name = "payment_status_id", nullable = false)
    private PaymentStatusModel paymentStatus;

    @NotNull(message = "Сумма платежа не может быть null")
    @Positive(message = "Сумма платежа должна быть положительной")
    @Digits(integer = 8, fraction = 2, message = "Сумма платежа должна быть в формате 8 целых и 2 дробных знака")
    private BigDecimal amount;

    @PastOrPresent(message = "Дата платежа не может быть в будущем")
    private LocalDateTime paymentDate;

    public PaymentModel() {}

    public PaymentModel(Long id, TicketModel ticket, PaymentStatusModel paymentStatus, BigDecimal amount, LocalDateTime paymentDate) {
        this.id = id;
        this.ticket = ticket;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
        this.paymentDate = paymentDate;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public TicketModel getTicket() {
        return ticket;
    }
    public void setTicket(TicketModel ticket) {
        this.ticket = ticket;
    }

    public PaymentStatusModel getPaymentStatus() {
        return this.paymentStatus;
    }
    public void setPaymentStatus(PaymentStatusModel paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }
    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getFormattedPaymentDate() {
        if (paymentDate == null) {
            return null;
        }
        return paymentDate.format(PaginationValidUtils.DATE_TIME_FORMATTER);
    }
}