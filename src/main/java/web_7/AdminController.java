package web_7;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN', 'SCHEDULE_CONTROLLER')")
public class AdminController {

    @GetMapping()
    public String adminMenu(){
        return "admin_menu";
    }
}
