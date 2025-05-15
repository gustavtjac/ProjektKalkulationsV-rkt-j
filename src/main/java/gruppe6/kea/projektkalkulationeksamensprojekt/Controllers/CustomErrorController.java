package gruppe6.kea.projektkalkulationeksamensprojekt.Controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class CustomErrorController implements ErrorController {


    @GetMapping("/error")
    public String errorHandler(HttpServletRequest request, Model model ) {
        String errorPage = "error"; //Håndtere alle default errors
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        model.addAttribute("code" ,status);
        model.addAttribute("message",message);

        return errorPage;
    }

    public String getErrorPath() {
        return "/error";
    }
}



