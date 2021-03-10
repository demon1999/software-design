package demon199;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private Dao dao = new InMemoryDao();

    @RequestMapping("/add_user")
    
}
