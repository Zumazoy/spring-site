package web_7.Trains.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import web_7.PaginationValidUtils;
import web_7.Trains.model.TrainModel;
import web_7.Trains.service.TrainService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN', 'SCHEDULE_CONTROLLER')")
public class TrainController {

    @Autowired
    private TrainService trainService;

    @GetMapping("/trains")
    public String trains(
            @RequestParam(value = "page", defaultValue = "0") int page, Model model,
            @ModelAttribute("validationErrors") Map<String, String> validationErrors) {

        if (!model.containsAttribute("train")) {
            model.addAttribute("train", new TrainModel());
        }
        if (validationErrors != null && !validationErrors.isEmpty()) {
            model.addAttribute("validationErrors", validationErrors);
        }

        int pageSize = PaginationValidUtils.getPageSize();
        Page<TrainModel> trainPage = trainService.getAllTrains(page, pageSize);

        PaginationValidUtils.addPaginationAttributes(model, trainPage, page, pageSize);
        model.addAttribute("trains", trainPage.getContent());

        return "trains";
    }

    @PostMapping("/trains/add")
    public String addTrain(@Valid @ModelAttribute("train") TrainModel train,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        if (trainService.existsByTrainTitleAndTrainType(train.getTrainTitle(), train.getTrainType())) {
            bindingResult.rejectValue("trainTitle", "train.exists", "Поезд с таким названием и типом уже существует");
            bindingResult.rejectValue("trainType", "trainType.exists", "Поезд с таким названием и типом уже существует");
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.train", bindingResult);
            redirectAttributes.addFlashAttribute("train", train);
            redirectAttributes.addFlashAttribute("validationErrors", PaginationValidUtils.getErrors(bindingResult));
            return "redirect:/admin/trains";
        }

        trainService.addTrain(train);
        return "redirect:/admin/trains";
    }

    @PostMapping("/trains/update")
    public String updateTrain(@RequestParam(value = "currentPage", defaultValue = "0") int currentPage,
                              RedirectAttributes redirectAttributes,
                              @Valid @ModelAttribute("train") TrainModel train,
                              BindingResult bindingResult) {

        TrainModel updatedTrain = trainService.findById(train.getId())
                .orElseThrow(() -> new RuntimeException("Поезд не найден"));

        if (!train.getTrainTitle().equals(updatedTrain.getTrainTitle()) ||
                !train.getTrainType().equals(updatedTrain.getTrainType())) {
            if (trainService.existsByTrainTitleAndTrainType(train.getTrainTitle(), train.getTrainType())) {
                bindingResult.rejectValue("trainTitle", "train.exists", "Поезд с таким названием и типом уже существует");
                bindingResult.rejectValue("trainType", "trainType.exists", "Поезд с таким названием и типом уже существует");
            }
        }

        if (bindingResult.hasErrors()) {
            PaginationValidUtils.addUpdateErrorsAttributes(currentPage, redirectAttributes, bindingResult, train.getId());
            return "redirect:/admin/trains";
        }

        updatedTrain.setTrainTitle(train.getTrainTitle());
        updatedTrain.setTrainType(train.getTrainType());
        updatedTrain.setCarriageCount(train.getCarriageCount());
        updatedTrain.setCarrierCompany(train.getCarrierCompany());

        trainService.updateTrain(updatedTrain);
        redirectAttributes.addAttribute("page", currentPage);
        return "redirect:/admin/trains";
    }

    @PostMapping("/trains/delete")
    public String deleteTrain(@RequestParam Long id) {
        try {
            trainService.deleteTrain(id);
        } catch (Exception e) {
            // Типа обработка, что его нельзя удалить
        }
        return "redirect:/admin/trains";
    }

    @GetMapping("/trains/search")
    public String searchTrains(
            @RequestParam String keyword,
            @RequestParam String searchBy,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model) {

        if (!model.containsAttribute("train")) {
            model.addAttribute("train", new TrainModel());
        }

        int pageSize = PaginationValidUtils.getPageSize();
        List<TrainModel> trains = trainService.searchTrains(keyword, searchBy);
        int totalItems = trains.size();

        int fromIndex = page * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalItems);

        if (fromIndex > toIndex) {
            fromIndex = 0;
            page = 0;
        }

        List<TrainModel> pagedTrains = trains.subList(fromIndex, toIndex);

        PaginationValidUtils.addPaginationAttributes(model, pagedTrains, page, pageSize, totalItems);
        model.addAttribute("trains", pagedTrains);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchBy", searchBy);

        return "trains";
    }
}