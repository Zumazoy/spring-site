package web_7.PaymentStatuses.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web_7.PaymentStatuses.model.PaymentStatusModel;

import java.util.List;

@Repository
public interface PaymentStatusRepository extends JpaRepository<PaymentStatusModel, Long> {
    List<PaymentStatusModel> findByPaymentStatusTitle(String paymentStatusTitle);
    Page<PaymentStatusModel> findAllByOrderByPaymentStatusIdAsc(Pageable pageable);
    List<PaymentStatusModel> findAllByOrderByPaymentStatusIdAsc();

    boolean existsByPaymentStatusTitle(String paymentStatusTitle);
}