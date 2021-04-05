package com.devtech.service;

import com.devtech.entity.City;
import com.devtech.entity.Rating;
import com.devtech.entity.User;
import com.devtech.exception.IncorrectSessionLoginException;
import com.devtech.exception.NotAuthroizedException;
import com.devtech.exception.UserAlreadyExistsException;
import com.devtech.repository.CityRepository;
import com.devtech.repository.RatingRepository;
import com.devtech.repository.UserRepository;
import com.devtech.request_response.user.UserCURequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.devtech.exception.ExceptionList.CITY_NOT_FOUND;
import static com.devtech.exception.ExceptionList.USER_NOT_FOUND;

@RestController
@RequiredArgsConstructor
public class UserService {
    @Resource(name = "bCryptPasswordEncoder")
    private BCryptPasswordEncoder encoder;

    private final UserRepository userRepo;
    private final CityRepository cityRepo;
    private final RatingRepository ratingRepo;

    public User create(@NotNull UserCURequest request) {
        User user = userRepo.findByLogin(request.getLogin()).orElse(null);
        if (user == null) {
            //  if (LocalDate.now().getYear() - Instant.ofEpochMilli(request.getBirthDate())
            //          .atZone(ZoneId.systemDefault()).toLocalDate().getYear() < 16)
            //      throw new TooYoungUserException();
            City city = cityRepo.findByCityAndCountry_Country(request.getCityName(),
                    request.getCountryName()).orElseThrow(CITY_NOT_FOUND);
            user.setLogin(request.getLogin());
            user.setPassword(encoder.encode(request.getPassword()));
            // user.setSurname(request.getSurname());
            user.setName(request.getName());
            // user.setPatronymic(request.getPatronymic());
            // user.setBirthDate(request.getBirthDate());
            // user.setGender(request.getGender());
            user.setCity(city);
            user.setPhone(request.getPhone());
            user.setEmail(request.getEmail());
            // user.setOrganization(request.getOrganization());
            userRepo.save(user);
            return user;
        } else throw new UserAlreadyExistsException(request.getLogin());
    }

    @Transactional
    public User update(@NotNull UserCURequest request) {
        if (!(SecurityContextHolder.
                getContext().getAuthentication().getPrincipal() instanceof User))
            throw new NotAuthroizedException();
        User user = userRepo.findById(((User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal()).getId()).orElseThrow(USER_NOT_FOUND);
        if (request.getCityName() != null && !request.getCityName().isEmpty()) {
            City city = cityRepo.findByCityAndCountry_Country(request.getCityName(),
                    request.getCountryName()).orElseThrow(CITY_NOT_FOUND);
            user.setCity(city);
        }
        if (request.getName() != null && !request.getName().isEmpty())
            user.setName(request.getName());
        if (request.getPhone() != null && !request.getPhone().isEmpty())
            user.setPhone(request.getPhone());
        if (request.getEmail() != null && !request.getEmail().isEmpty())
            user.setEmail(request.getEmail());
        userRepo.save(user);
        return user;
    }

    public User changePassword(@NotNull @NotEmpty String newPassword) {
        if (!(SecurityContextHolder.
                getContext().getAuthentication().getPrincipal() instanceof User))
            throw new NotAuthroizedException();
        User user = userRepo.findById(((User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal()).getId()).orElseThrow(USER_NOT_FOUND);
        user.setPassword(encoder.encode(newPassword));
        return user;
    }

    public User get(String login) {
        List<Rating> ratings = ratingRepo.findAllByProduct_User_Login(login);
        User user = userRepo.findByLogin(login).orElseThrow(USER_NOT_FOUND);
        if (ratings.size() > 0)
            user.setRating((int) Math.round(ratings.stream().mapToDouble(Rating::getRating).sum() / ratings.size()));
        if (SecurityContextHolder.
                getContext().getAuthentication().getPrincipal() instanceof User)
            if (((User) SecurityContextHolder.
                    getContext().getAuthentication().getPrincipal()).getLogin().equals(user.getLogin()))
                user.setEditable(true);
        return user;
    }

    public boolean findUser(@NotNull String login, @NotNull String password) {
        User user = userRepo.findByLogin(login).orElse(null);
        if (user == null)
            return false;
        if (!encoder.matches(password, user.getPassword()))
            return false;
        return true;
    }

    public User delete() {
        if (!(SecurityContextHolder.
                getContext().getAuthentication().getPrincipal() instanceof User))
            throw new NotAuthroizedException();
        User user = userRepo.findById(((User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal()).getId()).orElseThrow(USER_NOT_FOUND);
        userRepo.delete(user);
        return user;
    }

}
