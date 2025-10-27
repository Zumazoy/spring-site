package web_7.Passports.service;

import org.springframework.data.domain.Page;
import web_7.Passports.model.PassportModel;

import java.util.List;
import java.util.Optional;

public interface PassportService {
    List<PassportModel> getAllPassports();
    Page<PassportModel> getAllPassports(int page, int size);
    void addPassport(PassportModel passport);
    void updatePassport(PassportModel passport);
    void deletePassport(Long id);
    Optional<PassportModel> findById(Long id);
    List<PassportModel> searchPassports(String keyword, String searchBy);

    boolean existsBySerialAndNumber(Short serial, Integer number);
    boolean isPassportAssignedToAnotherUser(Long passportId, Long userId);
}