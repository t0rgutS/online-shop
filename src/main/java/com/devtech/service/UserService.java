package com.devtech.service;

import com.devtech.dto.user.UserCURequest;
import com.devtech.dto.user.UserResponse;
import com.devtech.entity.City;
import com.devtech.entity.Rating;
import com.devtech.entity.User;
import com.devtech.exception.IncorrectSessionLoginException;
import com.devtech.exception.TooYoungUserException;
import com.devtech.repository.CityRepository;
import com.devtech.repository.RatingRepository;
import com.devtech.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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

    public UserResponse create(@NotNull UserCURequest request) {
        User user = new User();
        if (LocalDate.now().getYear() - Instant.ofEpochMilli(request.getBirthDate())
                .atZone(ZoneId.systemDefault()).toLocalDate().getYear() < 16)
            throw new TooYoungUserException();
        City city = cityRepo.findByCityAndCountry_Country(request.getCityName(),
                request.getCountryName()).orElseThrow(CITY_NOT_FOUND);
        user.setLogin(request.getLogin());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setSurname(request.getSurname());
        user.setName(request.getName());
        user.setPatronymic(request.getPatronymic());
        user.setBirthDate(request.getBirthDate());
        user.setGender(request.getGender());
        user.setCity(city);
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        userRepo.save(user);
        return new UserResponse(user);
    }

    public UserResponse update(@NotNull Long id, @NotNull UserCURequest request) {
        User user = userRepo.findById(id).orElseThrow(USER_NOT_FOUND);
        if (request.getCityName() != null && !request.getCityName().isEmpty()) {
            City city = cityRepo.findByCityAndCountry_Country(request.getCityName(),
                    request.getCountryName()).orElseThrow(CITY_NOT_FOUND);
            user.setCity(city);
        }
        if (request.getBirthDate() != null) {
            if (LocalDate.now().getYear() - Instant.ofEpochMilli(request.getBirthDate())
                    .atZone(ZoneId.systemDefault()).toLocalDate().getYear() < 16)
                throw new TooYoungUserException();
            user.setBirthDate(request.getBirthDate());
        }
        if (request.getPassword() != null && !request.getPassword().isEmpty())
            user.setPassword(encoder.encode(request.getPassword()));
        if (request.getSurname() != null && !request.getSurname().isEmpty())
            user.setSurname(request.getSurname());
        if (request.getName() != null && !request.getName().isEmpty())
            user.setName(request.getName());
        if (request.getPatronymic() != null && !request.getPatronymic().isEmpty())
            user.setPatronymic(request.getPatronymic());
        if (request.getPhone() != null && !request.getPhone().isEmpty())
            user.setPhone(request.getPhone());
        if (request.getEmail() != null && !request.getEmail().isEmpty())
            user.setEmail(request.getEmail());
        if (request.getGender() != null)
            user.setGender(request.getGender());
        userRepo.save(user);
        return new UserResponse(user);
    }

    public UserResponse get(@NotNull Long id) {
        List<Rating> ratings = ratingRepo.findAllByProduct_User_Login(((User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal()).getLogin());
        if (ratings.size() > 0) {
            return new UserResponse(userRepo.findById(id).orElseThrow(USER_NOT_FOUND),
                    ratings.stream().mapToDouble(Rating::getRating).sum() / ratings.size());
        } else
            return new UserResponse(userRepo.findById(id).orElseThrow(USER_NOT_FOUND));
    }

    public boolean findUser(@NotNull String login, @NotNull String password) {
        User user = userRepo.findByLogin(login).orElse(null);
        if (user == null)
            return false;
        if (!user.getPassword().equals(encoder.encode(password)))
            return false;
        return true;
    }

    public UserResponse delete(@NotNull Long id) {
        User user = userRepo.findById(id).orElseThrow(USER_NOT_FOUND);
        if (!user.getLogin().equals(((User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal()).getLogin()))
            throw new IncorrectSessionLoginException("Вы не можете удалить чужой аккаунт!");
        userRepo.delete(user);
        return new UserResponse(user);
    }

}
