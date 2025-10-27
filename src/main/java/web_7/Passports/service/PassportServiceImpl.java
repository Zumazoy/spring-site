package web_7.Passports.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import web_7.Passports.model.PassportModel;
import web_7.Passports.repository.PassportRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PassportServiceImpl implements PassportService {
    private final PassportRepository passportRepository;

    @Autowired
    public PassportServiceImpl(PassportRepository passportRepository) {
        this.passportRepository = passportRepository;
    }

    @Override
    public List<PassportModel> getAllPassports() {
        return passportRepository.findAllByOrderByIdAsc();
    }

    @Override
    public Page<PassportModel> getAllPassports(int page, int size) {
        return passportRepository.findAllByOrderByIdAsc(PageRequest.of(page, size));
    }

    @Override
    public void addPassport(PassportModel passport) {
        passportRepository.save(passport);
    }

    @Override
    public void updatePassport(PassportModel passport) {
        passportRepository.save(passport);
    }

    @Override
    public void deletePassport(Long id) {
        passportRepository.deleteById(id);
    }

    @Override
    public Optional<PassportModel> findById(Long id) {
        return passportRepository.findById(id);
    }

    @Override
    public List<PassportModel> searchPassports(String keyword, String searchBy) {
        List<PassportModel> passports;
        switch (searchBy) {
            case "id":
                try {
                    Long id = Long.parseLong(keyword);
                    return passportRepository.findById(id).map(List::of).orElse(List.of());
                } catch (NumberFormatException e) {
                    return List.of();
                }
            case "serial":
                try {
                    Short serial = Short.parseShort(keyword);
                    passports = passportRepository.findBySerial(serial);
                    break;
                } catch (NumberFormatException e) {
                    return List.of();
                }
            case "number":
                try {
                    Integer number = Integer.parseInt(keyword);
                    passports = passportRepository.findByNumber(number);
                    break;
                } catch (NumberFormatException e) {
                    return List.of();
                }
            default:
                passports = passportRepository.findAllByOrderByIdAsc();
        }
        return passports.stream()
                .sorted(Comparator.comparing(PassportModel::getId))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsBySerialAndNumber(Short serial, Integer number) {
        return passportRepository.existsBySerialAndNumber(serial, number);
    }

    @Override
    public boolean isPassportAssignedToAnotherUser(Long passportId, Long userId) {
        Optional<PassportModel> passportOpt = passportRepository.findById(passportId);
        if (passportOpt.isPresent()) {
            PassportModel passport = passportOpt.get();
            return passport.getUser() != null &&
                    !passport.getUser().getId().equals(userId);
        }
        return false;
    }
}