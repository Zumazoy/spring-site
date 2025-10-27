package web_7.Passports.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web_7.Passports.model.PassportModel;

import java.util.List;

@Repository
public interface PassportRepository extends JpaRepository<PassportModel, Long> {
    List<PassportModel> findBySerial(Short serial);
    List<PassportModel> findByNumber(Integer number);
    List<PassportModel> findAllByOrderByIdAsc();
    Page<PassportModel> findAllByOrderByIdAsc(Pageable pageable);


    boolean existsBySerialAndNumber(Short serial, Integer number);
}