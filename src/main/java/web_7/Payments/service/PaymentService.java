package web_7.Payments.service;

import org.springframework.data.domain.Page;
import web_7.Payments.model.PaymentModel;

import java.util.List;
import java.util.Optional;

public interface PaymentService {
    Page<PaymentModel> getAllPayments(int page, int size);
    void addPayment(PaymentModel payment);
    void updatePayment(PaymentModel payment);
    void deletePayment(Long paymentId);
    Optional<PaymentModel> findById(Long paymentId);
    List<PaymentModel> searchPayments(String keyword, String searchBy);
}