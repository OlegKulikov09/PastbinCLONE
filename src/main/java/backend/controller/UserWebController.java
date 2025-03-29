package backend.controller;

import backend.Repositories.TextRepository;
import backend.Repositories.UserRepository;
import backend.model.Text;
import backend.model.User;
import backend.model.UserDTO;
import backend.services.MyUserDetailsService;
import jakarta.persistence.Tuple;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

import java.util.List;

@Controller
@AllArgsConstructor
public class UserWebController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TextRepository textRepository;
    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @GetMapping("/home")
    @ResponseBody
    public String welcome(){
        return "Welcome to the unprotected page";
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public String getUserPage(@PathVariable("id") int id, Model model, @AuthenticationPrincipal UserDetails currentUser) {
        User user = userRepository.findByLogin(currentUser.getUsername());
        if (user == null || user.getId() != id) {
            throw new AccessDeniedException("You are not allowed to view this page.");
        }
        // Загрузка текстов по ID пользователя
        List<Text> texts = textRepository.findByUserId(id);
        model.addAttribute("user", user);
        model.addAttribute("texts", texts);

        return "user_page";
    }
    @PostMapping("/user/{id}/delete")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String deleteUser(@PathVariable("id") int id) {
        myUserDetailsService.deleteUser(id);
        return "redirect:/logout";
    }
    @GetMapping("/users_rate")
    public String showUsersRate(Model model) {
        List<Tuple> usersRate = userRepository.allUsersOrderByRate();
        List<UserDTO> usersList = new ArrayList<>();

        for (Tuple tuple : usersRate) {
            int id = tuple.get(0, Integer.class);
            String login = tuple.get(1, String.class);
            int sumRateOfUser = tuple.get(2, Long.class).intValue();
            usersList.add(new UserDTO(id, login, sumRateOfUser));
        }

        model.addAttribute("users_rate", usersList);
        return "users_rate";
    }
}