package com.swp.ArtQuack.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swp.ArtQuack.entity.Chapter;
import com.swp.ArtQuack.entity.Course;
import com.swp.ArtQuack.entity.Item;
import com.swp.ArtQuack.service.ChapterService;
import com.swp.ArtQuack.service.CourseService;
import com.swp.ArtQuack.service.ItemService;
import com.swp.ArtQuack.view.ChapterObject;
import com.swp.ArtQuack.view.ItemObject;

@RequestMapping("/api")
@RestController
public class ChapterController {

	@Autowired
	private ChapterService chapterService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private ItemService itemService;
	
	@GetMapping("/chapters")
	public ResponseEntity<List<ChapterObject>> retrieveAllChapters() {
		List<ChapterObject> ls = new ArrayList<ChapterObject>();
		List<Chapter> chapterList = chapterService.findAll();
		for(Chapter x: chapterList) {
			ls.add(chapterService.displayRender(x));
		}
		return ResponseEntity.ok(ls);
	}
	
	@GetMapping("/chapter/{chapterID}")
	public ResponseEntity<ChapterObject> retrieveChapter(@PathVariable int chapterID) {
		Chapter chapter = chapterService.findById(chapterID);
		if (chapter != null) {
			return ResponseEntity.status(HttpStatus.OK).body(chapterService.displayRender(chapter));
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/course/{courseID}/chapters")
	public ResponseEntity<List<ChapterObject>> findByCourseID(@PathVariable("courseID") int courseID){
		Course course = courseService.findById(courseID);
		if(course == null)
			return ResponseEntity.notFound().header("message", "No Course found for such ID").build();
		List<ChapterObject> ls = new ArrayList<ChapterObject>();
		List<Chapter> chapterList = chapterService.findByCourseID(courseID);
		for(Chapter x: chapterList) {
			ls.add(chapterService.displayRender(x));
		}
		return ResponseEntity.ok(ls);
	}
	
	@PostMapping("/course/{courseID}/chapter")
	public ResponseEntity<Chapter> createChapter(@PathVariable int courseID, @RequestBody Chapter chapter){
		try {
			Course course = courseService.findById(courseID);
			if(course == null) return ResponseEntity.notFound().header("message", "Course not found. Adding failed").build();
			
			if(chapterService.findById(chapter.getChapterID()) != null) 
				return ResponseEntity.badRequest().header("message", "Chapter with such ID already exists").build();
			
			chapter.setCourse(course);
			chapter.setStatus(true);
			Chapter savedChapter = chapterService.add(chapter);
			if(savedChapter != null)
				return ResponseEntity.status(HttpStatus.CREATED).body(savedChapter);
			else return ResponseEntity.internalServerError().build();
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Failed to add new chapter").build();
		}
	}
	
	@PutMapping("/chapter/{chapterID}/updatechapter")
	public ResponseEntity<Chapter> updateChapter(@PathVariable("chapterID") int chapterID , @RequestBody Chapter chapter){
		Chapter available = chapterService.findById(chapter.getChapterID());
		if(available == null)
			return  ResponseEntity.notFound().header("message", "No Chapter found for such ID").build();
		
		chapter.setStatus(true);
		Chapter updatedChapter = chapterService.update(chapter);
		if(updatedChapter != null)
			return ResponseEntity.ok(updatedChapter);
		else 
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		
	}
	
	@DeleteMapping("/deletechapter/{chapterID}")
	public ResponseEntity<Void> deleteChapter(@PathVariable int chapterID){
		try{
			Chapter chapter = chapterService.findById(chapterID);
			if(chapter == null) return ResponseEntity.notFound().header("message", "Chapter not found. Delete failed").build();
			
			chapterService.delete(chapterID);
			return ResponseEntity.noContent().header("message", "Chapter deleted successfully").build();
		}
		catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("message", "Chapter deletion failed").build();
		}
	}
	
//	@GetMapping("/course/{courseID}/chapters/chapter/{chapterID}/items")
//	public ResponseEntity<List<Object>> findByCourseAndChapterID(
//	        @PathVariable("courseID") int courseID,
//	        @PathVariable("chapterID") int chapterID) {
//
//	    Course course = courseService.findById(courseID);
//	    if (course == null) {
//	        return ResponseEntity.notFound()
//	                .header("message", "No Course found for such ID")
//	                .build();
//	    }
//
//	    List<Object> chapterItemList = new ArrayList<>();
//
//	    // Get all chapters with the courseID
//	    List<Chapter> chapterList = chapterService.findByCourseID(courseID);
//	    for (Chapter chapter : chapterList) {
//	        ChapterObject chapterObject = chapterService.displayRender(chapter);
//	        chapterItemList.add(chapterObject);
//
//	        // Check if the chapterID matches the provided chapterID
//	        if (chapter.getChapterID() == chapterID) {
//	            // Get all items with the chapterID
//	            List<Item> itemList = itemService.findByChapterID(chapterID);
//	            for (Item item : itemList) {
//	                ItemObject itemObject = itemService.displayRender(item);
//	                chapterItemList.add(itemObject);
//	            }
//	        }
//	    }
//
//	    return ResponseEntity.ok(chapterItemList);
//	}
	
}
