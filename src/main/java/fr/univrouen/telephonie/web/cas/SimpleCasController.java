package fr.univrouen.telephonie.web.cas;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cas")
public class SimpleCasController {

	@RequestMapping
    public String getIndex() {
		return "redirect:/";
    }

	
}
