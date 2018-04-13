package com.platzi.profesoresplatzi.courseControllerTest;

import com.platzi.profesoresplatzi.controller.CourseController;

import com.platzi.profesoresplatzi.model.Course;
import com.platzi.profesoresplatzi.model.Teacher;
import com.platzi.profesoresplatzi.service.CourseService;
import com.platzi.profesoresplatzi.service.TeacherService;
import com.platzi.profesoresplatzi.util.CustomErrorType;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(HttpHeaders.class)
public class CourseControllerTest extends EasyMockSupport {
    @TestSubject
    public CourseController _CourseController = new CourseController();
    @Mock
    private CourseService _CourseService;
    @Mock
    private TeacherService _teacherService;
    @Mock
    private Course _Course;
    @Mock
    private Teacher _teacher;
    @Mock
    private UriComponentsBuilder uri;
    @Mock
    private HttpHeaders headersMock;
    @Mock
    private UriComponents uriComponents;

    @Before
    public void setUp() {
        //_socialMediaController
        injectMocks(_CourseController);
    }

    @Test
    public void getCoursesEmptyTest() throws Exception {
        String name = "a";
        Long id_teacher = 2L;
        List<Course> courses = new ArrayList<>();
        EasyMock.expect(_CourseService.findByIdTeacher(id_teacher)).andReturn(courses);
        EasyMock.replay(_CourseService, _Course);
        ResponseEntity response = _CourseController.getCourses(null, id_teacher);
        EasyMock.verify(_CourseService, _Course);
        Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void getCoursesNameNotNullTest() throws Exception {
        Long id_teacher = null;
        String name = "asd";
        List<Course> courses = new ArrayList<>();
        EasyMock.expect(_CourseService.findByName(name)).andReturn(null);
        EasyMock.replay(_CourseService, _Course);
        ResponseEntity response = _CourseController.getCourses(name, id_teacher);
        EasyMock.verify(_CourseService, _Course);//asserts
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());


    }

    @Test
    public void getCoursesNameAndIdNullTest() throws Exception {
        Long id_teacher = null;
        String name = null;
        List<Course> courses = new ArrayList<>();
        EasyMock.expect(_CourseService.findAllCourses()).andReturn(courses);
        EasyMock.replay(_CourseService, _Course);
        ResponseEntity response = _CourseController.getCourses(name, id_teacher);
        EasyMock.verify(_CourseService, _Course);
        Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void getCoursesTest() throws Exception {
        Long id_teacher = null;
        String name = null;
        List<Course> courses = new ArrayList<>();
        courses.add(_Course);
        EasyMock.expect(_CourseService.findAllCourses()).andReturn(courses);
        EasyMock.replay(_CourseService, _Course);
        ResponseEntity response = _CourseController.getCourses(name, id_teacher);
        EasyMock.verify(_CourseService, _Course);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    public void getCoursesByIdCourseNullTest() throws Exception {
        Long id = 1L;
        EasyMock.expect(_CourseService.findById(id)).andReturn(null);
        EasyMock.replay(_CourseService, _Course);
        ResponseEntity response = _CourseController.getCourseById(id);
        EasyMock.verify(_CourseService, _Course);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    public void getCoursesByIdTest() throws Exception {
        Long id = 1L;
        EasyMock.expect(_CourseService.findById(id)).andReturn(_Course);
        EasyMock.replay(_CourseService, _Course);
        ResponseEntity response = _CourseController.getCourseById(id);
        EasyMock.verify(_CourseService, _Course);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    public void createCoursesNameExistTest() throws Exception {
        String name = "asd";
        EasyMock.expect(_Course.getName()).andReturn(name).times(2);
        EasyMock.expect(_CourseService.findByName(name)).andReturn(_Course);
        EasyMock.replay(_CourseService, _Course);
        ResponseEntity response = _CourseController.createCourse(_Course, uri);
        EasyMock.verify(_CourseService, _Course);
        Assert.assertEquals("Unable to create. A course with name " +
                name +
                " already exist.", ((CustomErrorType) response.getBody()).getErrorMessage());
        Assert.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void createCoursesTest() throws Exception {
        String name = null;
        EasyMock.expect(_Course.getName()).andReturn(null);
        EasyMock.expect(_CourseService.findByName(name)).andReturn(null);
        _CourseService.saveCourse(_Course);
        EasyMock.expectLastCall();
        EasyMock.expect(uri.path("/v1/courses/{id}")).andReturn(uri);
        EasyMock.expect(_Course.getIdCourse()).andReturn(1L);
        EasyMock.expect(uri.buildAndExpand(1L)).andReturn(uriComponents);
        URI testUri = new URI("http://example.com");
        EasyMock.expect(uriComponents.toUri()).andReturn(testUri);
        PowerMock.replay(_Course, _CourseService, uri, HttpHeaders.class, uriComponents, headersMock);
        ResponseEntity response = _CourseController.createCourse(_Course, uri);
        PowerMock.verify(_Course, _CourseService, uri, HttpHeaders.class, uriComponents, headersMock);
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void updateNullCourseTest() throws Exception {
        Long id = 1L;
        EasyMock.expect(_CourseService.findById(id)).andReturn(null);
        PowerMock.replay(_CourseService);
        ResponseEntity response = _CourseController.updateCourses(id, null);
        PowerMock.verify(_CourseService);
        Assert.assertEquals("Unable to upate. Social Media" +
                " with id " + id + " not found.", ((CustomErrorType) response.getBody()).getErrorMessage());
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void updateCourseTest() throws Exception {
        Long id = 1L;
        String name = "";
        String theme = "";
        String project = "";
        EasyMock.expect(_CourseService.findById(id)).andReturn(_Course);
        _CourseService.updateCourse(_Course);
        EasyMock.expectLastCall();
        EasyMock.expect(_Course.getName()).andReturn(name);
        _Course.setName(name);
        EasyMock.expectLastCall();
        EasyMock.expect(_Course.getThemes()).andReturn(theme);
        _Course.setThemes(theme);
        EasyMock.expectLastCall();
        EasyMock.expect(_Course.getProject()).andReturn(project);
        _Course.setProject(project);
        EasyMock.expectLastCall();
        PowerMock.replay(_CourseService, _Course);
        ResponseEntity response = _CourseController.updateCourses(id, _Course);
        PowerMock.verify(_CourseService, _Course);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void deleteNullCourseTest() throws Exception {
        Long id = 1L;
        EasyMock.expect(_CourseService.findById(id)).andReturn(null);
        EasyMock.replay(_CourseService);
        ResponseEntity response = _CourseController.deleteCourse(id);
        EasyMock.verify(_CourseService);
        Assert.assertEquals("Unable to delete. course with id " + id +
                " not found.", ((CustomErrorType) response.getBody()).getErrorMessage());
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void deleteCourseTest() throws Exception {
        Long id = 1L;
        EasyMock.expect(_CourseService.findById(id)).andReturn(_Course);
        _CourseService.deleteCourseById(id);
        EasyMock.expectLastCall();
        EasyMock.replay(_CourseService);
        ResponseEntity response = _CourseController.deleteCourse(id);
        EasyMock.verify(_CourseService);
        Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void assignTeacherToCourseNullIdTest() throws Exception {
        EasyMock.expect(_Course.getIdCourse()).andReturn(null);
        EasyMock.replay(_CourseService, _Course);
        ResponseEntity response = _CourseController.assignTeacherToCourse(_Course, uri);
        EasyMock.verify(_CourseService, _Course);
        Assert.assertEquals("We need almost id_course and id_teacher", ((CustomErrorType) response.getBody()).getErrorMessage());
        Assert.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void assignTeacherToCourseNullTest() throws Exception {
        Long id = 1L;
        EasyMock.expect(_Course.getIdCourse()).andReturn(id).times(3);

        EasyMock.expect(_Course.getTeacher()).andReturn(_teacher);
        EasyMock.expect(_teacher.getIdTeacher()).andReturn(id);
        EasyMock.expect(_CourseService.findById(id)).andReturn(null);
        EasyMock.replay(_CourseService, _Course, uri, _teacher);
        ResponseEntity response = _CourseController.assignTeacherToCourse(_Course, uri);
        EasyMock.verify(_CourseService, _Course, uri, _teacher);
        Assert.assertEquals("The id_course: " + id
                + " not found.", ((CustomErrorType) response.getBody()).getErrorMessage());
        Assert.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void assignTeacherToCourseTeacherNullTest() throws Exception {
        Long id = 1L;
        EasyMock.expect(_Course.getIdCourse()).andReturn(id).times(2);
        EasyMock.expect(_Course.getTeacher()).andReturn(_teacher).times(3);
        EasyMock.expect(_teacher.getIdTeacher()).andReturn(id).times(3);
        EasyMock.expect(_CourseService.findById(id)).andReturn(_Course);
        EasyMock.expect(_teacherService.findById(id)).andReturn(null);
        EasyMock.replay(_CourseService, _Course, uri, _teacherService, _teacher);
        ResponseEntity response = _CourseController.assignTeacherToCourse(_Course, uri);
        EasyMock.verify(_CourseService, _Course, uri, _teacherService, _teacher);
        Assert.assertEquals("The id_teacher: " + id + " not found.", ((CustomErrorType) response.getBody()).getErrorMessage());
        Assert.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
}
