package com.swp.ArtQuack.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swp.ArtQuack.entity.Review;
import com.swp.ArtQuack.entity.Student;
import com.swp.ArtQuack.repository.ReviewRepository;

@Service
public class ReviewService {

	@Autowired
	private ReviewRepository reviewRepoService;
	
	public List<Review> findAll(){
		return reviewRepoService.findAll();
	}
	
	public Review findById(int id) {
		Optional<Review> review = reviewRepoService.findById(id);
		if(review.isPresent()) return review.get();
		else return null;
	}
	
	public List<Review> findByCourseID(int courseID){
		return reviewRepoService.findByCourseCourseID(courseID);
	}
	
	
	//ADD
	public Review add(Review review) {
		return reviewRepoService.save(review);
	}
	
	//UPDATE
	public Review update(Review newReview) {
		return reviewRepoService.save(newReview);
	}
	
	//DELETE
	public boolean delete(int reviewID) {
		Review review = findById(reviewID);
		if(review == null) return false;
		review.setStatus(false);
		update(review);
		return !review.isStatus();
	}
}
