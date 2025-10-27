package web_7.Payments.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web_7.Payments.model.PaymentModel;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentModel, Long> {
    Page<PaymentModel> findAllByOrderByIdAsc(Pageable pageable);
    List<PaymentModel> findAllByOrderByIdAsc();
}