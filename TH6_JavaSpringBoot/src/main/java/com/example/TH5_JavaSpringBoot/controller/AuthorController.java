package com.example.TH5_JavaSpringBoot.controller;

import com.example.TH5_JavaSpringBoot.entity.Author;
import com.example.TH5_JavaSpringBoot.entity.Course;
import com.example.TH5_JavaSpringBoot.repositories.AuthorRepositories;
import com.example.TH5_JavaSpringBoot.repositories.CourseRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping(path = "api/v1/Author")
public class AuthorController {
    @Autowired
    private RedisTemplate template;

    @Autowired
    private AuthorRepositories authorRepositories;
    @Autowired
    private CourseRepositories courseRepositories;
    public static final String HASH_KEY = "Author";
    public static final String HASH_KEY_COURSE = "Course";

    //TODO: Get all list
    @GetMapping("")
    public List<Author> getAllAuthor() {
        return template.opsForHash().values(HASH_KEY);
    }

    //TODO: Insert
    @PostMapping("/Insert")
    public ResponseEntity<Object> insertAuthorAndCourse(@RequestBody Author author) {
        try {
            String key = HASH_KEY + "_" + author.getId();
            template.opsForHash().put(HASH_KEY, key, author);
            // Lưu Author
            Author savedAuthor = authorRepositories.save(author);
            // Kiểm tra nếu có danh sách Course
            if (author.getCourses() != null && !author.getCourses().isEmpty()) {
                for (Course course : author.getCourses()) {
                    course.setAuthor(savedAuthor);
                    courseRepositories.save(course);
                }
            }
            return ResponseEntity.ok(author);
        } catch (Exception e) {
            throw  new RuntimeException("Lỗi khi thêm",e);
        }
    }

//TODO: Find by id
    @GetMapping("/{id}")
    ResponseEntity<Author> findById(@PathVariable Long id) {
        Author foundAuthor = authorRepositories.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy id = " + id));
        return ResponseEntity.ok(foundAuthor);
    }
//TODO: Update Author và Course
//    @PutMapping("/Update/{id}")
//    ResponseEntity<Author> updateAuthorAndCourse(@PathVariable Long id, @RequestBody Author author) {
//        try {
//            Author foundAuthor = authorRepositories.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy id = " + id));
//            foundAuthor.setName(author.getName());
//            // Kiểm tra nếu có danh sách Course
//            if (author.getCourses() != null && !author.getCourses().isEmpty()) {
//                for (Course course : author.getCourses()) {
//                    Course newCourse = new Course();
//                    newCourse.setName(course.getName());
//                    newCourse.setTitle(course.getTitle());
//                    newCourse.setCost(course.getCost());
//                    newCourse.setCurPrice(course.getCurPrice());
//                    newCourse.setAuthor(foundAuthor);
//                    foundAuthor.getCourses().add(newCourse);
//                    courseRepositories.save(newCourse);
//
//                    // Thực hiện lưu thông tin Course vào Redis
//                    insertAuthorAndCourse(foundAuthor);
//                }
//            }
//            return ResponseEntity.ok(foundAuthor);
//        } catch (Exception e) {
//            throw  new RuntimeException("Lỗi khi thêm",e);
//        }
//    }
@PutMapping("/Update/{id}")
public ResponseEntity<Author> updateAuthorAndCourse(@PathVariable Long id, @RequestBody Author author) {
    try {
        Author foundAuthor = authorRepositories.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy id = " + id));
        foundAuthor.setName(author.getName());

        foundAuthor.getCourses().clear();

        if (author.getCourses() != null && !author.getCourses().isEmpty()) {
            for (Course course : author.getCourses()) {
                // Tạo key cho Course trong Redis dựa trên id của Course
                String courseKey = "course:" + course.getId();

                // Kiểm tra xem Course đã tồn tại trong Redis chưa
                boolean courseExists = template.opsForHash().hasKey(HASH_KEY_COURSE, courseKey);

                // Nếu Course đã tồn tại, cập nhật thông tin mới cho Course trong Redis
                if (courseExists) {
                    template.opsForHash().put(HASH_KEY_COURSE, courseKey, course);
                } else {
                    System.out.println("Course " + courseKey + " không tồn tại");
                }

                // Thêm Course vào danh sách Courses của Author
                foundAuthor.getCourses().add(course);
            }
        }

        // Lưu thông tin về Author vào Redis
        String authorKey = "Author_" + foundAuthor.getId();
        template.opsForHash().put(HASH_KEY, authorKey, foundAuthor);

        return ResponseEntity.ok(foundAuthor);
    } catch (Exception e) {
        throw new RuntimeException("Lỗi khi cập nhật", e);
    }
}
    //TODO: Delete Author và Course
//    @DeleteMapping("/Delete/{id}")
//    ResponseEntity<Author> deleteAuthorAndCourse(@PathVariable Long id) {
//        try {
//            Author foundAuthor = authorRepositories.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy id = " + id));
//            authorRepositories.delete(foundAuthor);
//            return ResponseEntity.ok(foundAuthor);
//        } catch (Exception e) {
//            throw  new RuntimeException("Lỗi khi thêm",e);
//        }
//    }
    @DeleteMapping("/Delete/{id}")
    ResponseEntity<String> deleteAuthorAndCourse(@PathVariable Long id) {
        try {
            Author foundAuthor = authorRepositories.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy id = " + id));

            // Xóa các Khóa học liên quan từ Redis và cơ sở dữ liệu
            if (foundAuthor.getCourses() != null && !foundAuthor.getCourses().isEmpty()) {
                for (Course course : foundAuthor.getCourses()) {
                    // Xóa Khóa học từ Redis
                    String courseKey = "course:" + course.getId();
                    template.opsForHash().delete(HASH_KEY_COURSE, courseKey);

                    // Xóa Khóa học từ cơ sở dữ liệu
                    courseRepositories.delete(course);
                }
            }

            // Xóa Tác giả từ Redis
            String authorKey = "Author_" + foundAuthor.getId();
            template.opsForHash().delete(HASH_KEY, authorKey);

            // Xóa Tác giả từ cơ sở dữ liệu
            authorRepositories.delete(foundAuthor);

            return ResponseEntity.ok("Xóa thành công!");
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xóa", e);
        }
    }
}
