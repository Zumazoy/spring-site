package web_7;

import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaginationValidUtils {
    private static final int DEFAULT_PAGE_SIZE = 3;
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static void addPaginationAttributes(Model model, Page<?> page, int currentPage, int pageSize) {
        model.addAttribute("items", page.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("pageSize", pageSize);
    }

    public static void addPaginationAttributes(Model model, List<?> items, int currentPage, int pageSize, int totalItems) {
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
        model.addAttribute("items", items);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", pageSize);
    }

    public static int getPageSize() {
        return DEFAULT_PAGE_SIZE;
    }

    public static Map<String, String> getErrors(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }

    public static void addUpdateErrorsAttributes(@RequestParam(value = "currentPage", defaultValue = "0") int currentPage,
                                                 RedirectAttributes redirectAttributes, BindingResult bindingResult, Long id) {
        Map<String, String> bindingErrors = PaginationValidUtils.getErrors(bindingResult);
        Map<String, String> formattedErrors = new HashMap<>();
        bindingErrors.forEach((field, error) ->
                formattedErrors.put("update_" + field + "_" + id, error));

        redirectAttributes.addFlashAttribute("updateValidationErrors", formattedErrors);
        redirectAttributes.addAttribute("page", currentPage);
    }
}
