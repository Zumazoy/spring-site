package web_7.PaymentStatuses.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import web_7.PaymentStatuses.model.PaymentStatusModel;
import web_7.PaymentStatuses.repository.PaymentStatusRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentStatusServiceImpl implements PaymentStatusService {
    private final PaymentStatusRepository paymentStatusRepository;

    @Autowired
    public PaymentStatusServiceImpl(PaymentStatusRepository paymentStatusRepository) {
        this.paymentStatusRepository = paymentStatusRepository;
    }

    @Override
    public Page<PaymentStatusModel> getAllPaymentStatuses(int page, int size) {
        return paymentStatusRepository.findAllByOrderByPaymentStatusIdAsc(PageRequest.of(page, size));
    }

    @Override
    public List<PaymentStatusModel> getAllPaymentStatuses() {
        return paymentStatusRepository.findAllByOrderByPaymentStatusIdAsc();
    }

    @Override
    public void addPaymentStatus(PaymentStatusModel paymentStatus) {
        paymentStatusRepository.save(paymentStatus);
    }

    @Override
    public void updatePaymentStatus(PaymentStatusModel paymentStatus) {
        paymentStatusRepository.save(paymentStatus);
    }

    @Override
    public void deletePaymentStatus(Long id) {
        paymentStatusRepository.deleteById(id);
    }

    @Override
    public PaymentStatusModel findById(Long id) {
        return paymentStatusRepository.findById(id).orElse(null);
    }

    @Override
    public List<PaymentStatusModel> searchPaymentStatuses(String keyword, String searchBy) {
        List<PaymentStatusModel> paymentStatuses;
        switch (searchBy) {
            case "id":
                try {
                    Long id = Long.parseLong(keyword);
                    return paymentStatusRepository.findById(id).map(List::of).orElse(List.of());
                } catch (NumberFormatException e) {
                    return List.of();
                }
            case "title":
                paymentStatuses = paymentStatusRepository.findByPaymentStatusTitle(keyword);
                break;
            default:
                paymentStatuses = paymentStatusRepository.findAllByOrderByPaymentStatusIdAsc();
        }
        return paymentStatuses.stream()
                .sorted(Comparator.comparing(PaymentStatusModel::getPaymentStatusId))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByPaymentStatusTitle(String paymentStatusTitle) {
        return paymentStatusRepository.existsByPaymentStatusTitle(paymentStatusTitle);
    }
}