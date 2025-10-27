package web_7.Payments.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import web_7.Payments.model.PaymentModel;
import web_7.Payments.repository.PaymentRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Page<PaymentModel> getAllPayments(int page, int size) {
        return paymentRepository.findAllByOrderByIdAsc(PageRequest.of(page, size));
    }

    @Override
    public void addPayment(PaymentModel payment) {
        paymentRepository.save(payment);
    }

    @Override
    public void updatePayment(PaymentModel payment) {
        paymentRepository.save(payment);
    }

    @Override
    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }

    @Override
    public Optional<PaymentModel> findById(Long id) {
        return paymentRepository.findById(id);
    }

    @Override
    public List<PaymentModel> searchPayments(String keyword, String searchBy) {
        List<PaymentModel> payments;
        switch (searchBy) {
            case "id":
                try {
                    Long id = Long.parseLong(keyword);
                    return paymentRepository.findById(id).map(List::of).orElse(List.of());
                } catch (NumberFormatException e) {
                    return List.of();
                }
            default:
                payments = paymentRepository.findAllByOrderByIdAsc();
        }
        return payments.stream()
                .sorted(Comparator.comparing(PaymentModel::getId))
                .collect(Collectors.toList());
    }
}