package web_7.PaymentStatuses.service;

import org.springframework.data.domain.Page;
import web_7.PaymentStatuses.model.PaymentStatusModel;

import java.util.List;

public interface PaymentStatusService {
    Page<PaymentStatusModel> getAllPaymentStatuses(int page, int size);
    void addPaymentStatus(PaymentStatusModel paymentStatus);
    void updatePaymentStatus(PaymentStatusModel paymentStatus);
    void deletePaymentStatus(Long paymentStatusId);
    PaymentStatusModel findById(Long paymentStatusId);
    List<PaymentStatusModel> searchPaymentStatuses(String keyword, String searchBy);
    List<PaymentStatusModel> getAllPaymentStatuses();

    boolean existsByPaymentStatusTitle(String paymentStatusTitle);
}