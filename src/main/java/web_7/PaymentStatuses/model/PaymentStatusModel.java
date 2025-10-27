package web_7.PaymentStatuses.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import web_7.Payments.model.PaymentModel;

import java.util.List;

@Entity
@Table(name = "payment_statuses")
public class PaymentStatusModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentStatusId;

    @Size(min = 3, max = 30, message = "Название статуса платежа должно содержать от 3 до 30 символов")
    @NotNull(message = "Название статуса платежа не может быть null")
    @NotBlank(message = "Название статуса платежа не может быть пустым")
    @Column(unique = true)
    private String paymentStatusTitle;

    @OneToMany(mappedBy = "paymentStatus", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<PaymentModel> payments;

    public PaymentStatusModel() {}

    public PaymentStatusModel(Long paymentStatusId, String paymentStatusTitle) {
        this.paymentStatusId = paymentStatusId;
        this.paymentStatusTitle = paymentStatusTitle;
    }

    public Long getPaymentStatusId() {
        return paymentStatusId;
    }
    public String getPaymentStatusTitle() {
        return paymentStatusTitle;
    }

    public void setPaymentStatusId(Long roleId) {
        this.paymentStatusId = roleId;
    }
    public void setPaymentStatusTitle(String titleRole) {
        this.paymentStatusTitle = titleRole;
    }

    public List<PaymentModel> getPayments() {
        return payments;
    }
    public void setPayments(List<PaymentModel> payments) {
        this.payments = payments;
    }
}

