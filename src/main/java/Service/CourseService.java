
import java.io.*;
import java.util.*;

public class CourseService {
    private CourseRepository courseRepository;
    private DepartmentRepository departmentRepository;
    private TeacherRepository teacherRepository;

    public CourseService(CourseRepository courseRepository, DepartmentRepository departmentRepository, TeacherRepository teacherRepository) {
    	this.courseRepository = courseRepository;
    	this.departmentRepository = departmentRepository;
    	this.teacherRepository = teacherRepository;
    }
    
    public Course createCourse(CourseCreationRequest courseData) {
    	if (courseRepository.findByCourseId(courseData.getCourseId()).isPresent()) {
    		throw new RuntimeException(
    				"Course with id '" + courseData.getCourseId() + "' already exists"
    				);
    	}
    	Course course = new Course();
    	course.setCourseId(courseData.getCourseId());
    	course.setName(courseData.getName());
    	course.setCredits(courseData.getCredits());
    	course.setCourseType(courseData.getCourseType());
    	
        return courseRepository.save(course);
    }

    public Course updateCourse(String courseId, CourseUpdateRequest courseData) {
        Course course = getCourseById(courseId);
        
        if (courseData.getName() != null) {
        	course.setName(courseData.getName());
        }
        if (courseData.getCredits() != null) {
        	course.setCredits(courseData.getCredits());
        }
        if (courseData.getCourseType() != null) {
        	course.setCourseType(courseData.getCourseType());
        }
        
        return courseRepository.save(course);
    }

    public void deleteCourse(String courseId) {
        Course course = getCourseById(courseId);
        List<Enrollment> activeEnrollments = course.getEnrollments();
        
        if (activeEnrollments != null && !activeEnrollments.isEmpty()) {
        	throw new RuntimeException(
        			"Can not delete course '" + course.getName() +
        			"': " + activeEnrollments.size() + " enrollment(s) exist. "
        					+ "Remove enrollments first."
        			);
        	
        }
        courseRepository.deleteById(courseId);;
    }

    public Course getCourseById(String courseId) {
        return courseRepository.findByCourseId(courseId)
        		.orElseThrow(() -> new RuntimeException(
        				"Course not found with id: " + courseId
        				));
    }

    public List<Course> searchCourses(CourseSearchFilters filters) {
        List<Course> all = courseRepository.findAll();
        List<Course> result = new ArrayList<>();

        for (Course course : all) {
            boolean matches = true;

            if (filters != null) {
                if (filters.getCourseType() != null &&
                        !filters.getCourseType().equals(course.getCourseType())) {
                    matches = false;
                }

                if (filters.getNameKeyword() != null &&
                        !filters.getNameKeyword().isEmpty()) {
                    String lower = filters.getNameKeyword().toLowerCase();
                    if (course.getName() == null ||
                            !course.getName().toLowerCase().contains(lower)) {
                        matches = false;
                    }
                }

                if (filters.getMaxCredits() != null &&
                        course.getCredits() > filters.getMaxCredits()) {
                    matches = false;
                }
            }

            if (matches) {
                result.add(course);
            }
        }

        return result;
    }

    public void assignInstructor(String courseId, Long teacherId) {
        Course course = getCourseById(courseId);
        
        String teacherEmpId = String.valueOf(teacherId);
        Teacher teacher = teacherRepository.findByEmployeeId(teacherEmpId)
        		.orElseThrow(() -> new RuntimeException(
        				"Teacher not found with employeeId: " + teacherEmpId
        				));
        for (Teacher t : course.getTeachers()) {
        	if (teacherEmpId.equals(t.getEmployeeId())) {
        		throw new RuntimeException(
        				"Teacher is already assigned to this course"
        				);
        	}
        }
        course.addTeacher(teacher);
        
        courseRepository.save(course);
    }


    public List<Course> getAvailableCoursesForStudent(Long studentId) {
        List<Course> allCourses = courseRepository.findAll();
        String studentIdStr = String.valueOf(studentId);

        List<Course> available = new ArrayList<>();
        for (Course course : allCourses) {
            boolean alreadyEnrolled = false;

            for (Enrollment e : course.getEnrollments()) {
                if (e.getStudent() != null &&
                        studentIdStr.equals(e.getStudent().getStudentId())) {
                    alreadyEnrolled = true;
                    break;
                }
            }

            if (!alreadyEnrolled) {
                available.add(course);
            }
        }

        return available;
    }

}