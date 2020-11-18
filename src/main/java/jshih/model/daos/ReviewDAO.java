package jshih.model.daos;

import java.util.List;

import jshih.model.models.Review;

public interface ReviewDAO extends DAO<Review> {
    Review getReview(String reviewUid);

    List<Review> getUserReviews(String userUid);

    List<Review> getBusinessReviews(String businessUid);
}
