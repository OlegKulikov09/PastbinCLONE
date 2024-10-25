package com.OlegKulikov.pastbinclone.try_1.services;

import com.OlegKulikov.pastbinclone.try_1.Repositories.RatingRepository;
import com.OlegKulikov.pastbinclone.try_1.Repositories.TextRepository;
import com.OlegKulikov.pastbinclone.try_1.model.Rating;
import com.OlegKulikov.pastbinclone.try_1.model.Text;
import com.OlegKulikov.pastbinclone.try_1.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class RatingService {
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private TextRepository textRepository;

    public void changeRating(String textId, User user) {
        Text text = textRepository.findById(textId)
                .orElseThrow(() -> new IllegalArgumentException("Text not found"));

        // Ищем существующий рейтинг
        Optional<Rating> currentRate = ratingRepository.findByTextAndUser(text, user);

        if (currentRate.isPresent()) {
            text.setRate(text.getRate() - 1);
            ratingRepository.delete(currentRate.get());
        } else {
            text.setRate(text.getRate() + 1);
            Rating rating = new Rating();
            rating.setText(text);
            rating.setUser(user);
            ratingRepository.save(rating);
        }

        textRepository.save(text);
    }
}
