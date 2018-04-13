package com.platzi.profesoresplatzi.teacherControllerTest;

import com.platzi.profesoresplatzi.controller.TeacherController;
import com.platzi.profesoresplatzi.model.Teacher;
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
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(PowerMockRunner.class)
public class TeacherControllerTest extends EasyMockSupport {
    @TestSubject
    public TeacherController _TeacherController = new TeacherController();

    @Mock
    private TeacherService _teacherService;
    @Mock
    private Teacher _teacher;
    @Mock
    private UriComponentsBuilder uri;
    @Mock
    private UriComponents uriComponents;
    @Mock
    private HttpHeaders headersMock;
    @Mock
    private MultipartFile file;

    @Before
    public void setUp() {
        //_socialMediaController
        injectMocks(_TeacherController);
    }

    @Test
    public void getTeachersNullNameTest() throws Exception {
        List<Teacher> teachers = new ArrayList<Teacher>();
        String name = null;
        EasyMock.expect(_teacher.getName()).andReturn(null);
        EasyMock.expect(_teacherService.findAllTeachers()).andReturn(teachers);
        EasyMock.replay(_teacherService);
        ResponseEntity response = _TeacherController.getTeachers(name);
        EasyMock.verify(_teacherService);
        Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void getTeachersTeacherNullTest() throws Exception {
        List<Teacher> teachers = new ArrayList<Teacher>();
        String name = "asd";
        EasyMock.expect(_teacher.getName()).andReturn(name);
        EasyMock.expect(_teacherService.findByName(name)).andReturn(null);
        EasyMock.replay(_teacherService);
        ResponseEntity response = _TeacherController.getTeachers(name);
        EasyMock.verify(_teacherService);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getTeachersNameNotNullTest() throws Exception {
        List<Teacher> teachers = new ArrayList<Teacher>();
        String name = null;
        EasyMock.expect(_teacherService.findAllTeachers()).andReturn(Arrays.asList(_teacher));
        EasyMock.replay(_teacherService, _teacher);
        ResponseEntity response = _TeacherController.getTeachers(null);
        EasyMock.verify(_teacherService, _teacher);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    public void getTeachersTest() throws Exception {
        List<Teacher> teachers = new ArrayList<Teacher>();
        String name = "asd";
        EasyMock.expect(_teacherService.findByName(name)).andReturn(_teacher);
        teachers.add(_teacher);
        EasyMock.replay(_teacherService, _teacher);
        ResponseEntity response = _TeacherController.getTeachers(name);
        EasyMock.verify(_teacherService, _teacher);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getTeacherByIdNullTeacherTest() throws Exception {
        Long id = 1L;
        EasyMock.expect(_teacherService.findById(id)).andReturn(null);
        EasyMock.replay(_teacherService);
        ResponseEntity response = _TeacherController.getTeacherById(id);
        EasyMock.verify(_teacherService);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void getTeacherByIdTest() throws Exception {
        Long id = 1L;
        EasyMock.expect(_teacherService.findById(id)).andReturn(_teacher);
        EasyMock.replay(_teacherService, _teacher);
        ResponseEntity response = _TeacherController.getTeacherById(id);
        EasyMock.verify(_teacherService, _teacher);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void deleteTeacherByIdNullTeacherTest() throws Exception {
        Long id = 1L;
        EasyMock.expect(_teacherService.findById(id)).andReturn(null);
        EasyMock.replay(_teacherService);
        ResponseEntity response = _TeacherController.deleteTeacherById(id);
        EasyMock.verify(_teacherService);
        Assert.assertEquals("Unable to delete. teacher with id " + id + " not found.", ((CustomErrorType) response.getBody()).getErrorMessage());
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void deleteTeacherByIdTest() throws Exception {
        Long id = 1L;
        EasyMock.expect(_teacherService.findById(id)).andReturn(_teacher);
        _teacherService.deleteTeacherById(id);
        EasyMock.expectLastCall();
        EasyMock.replay(_teacherService);
        ResponseEntity response = _TeacherController.deleteTeacherById(id);
        EasyMock.verify(_teacherService);
        Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }


    @Test
    public void uploadTeacherImageIdNullTest() throws Exception {
        Long id = null;
        EasyMock.replay(_teacherService, file, uri);
        ResponseEntity response = _TeacherController.uploadTeacherImage(id, file, uri);
        EasyMock.verify(_teacherService, file, uri);
        Assert.assertEquals("Please set id_teacher", ((CustomErrorType) response.getBody()).getErrorMessage());
        Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void uploadTeacherImageMultipartFileEmptyTest() throws Exception {
        Long id = 1L;
        EasyMock.expect(file.isEmpty() == true).andReturn(true);
        EasyMock.replay(_teacherService, file, uri);
        ResponseEntity response = _TeacherController.uploadTeacherImage(id, file, uri);
        EasyMock.verify(_teacherService, file, uri);
        Assert.assertEquals("Please select a file to upload", ((CustomErrorType) response.getBody()).getErrorMessage());
        Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void uploadTeacherImageNullTeacherTest() throws Exception {
        Long id = 1L;
        EasyMock.expect(file.isEmpty() == false).andReturn(false);
        EasyMock.expect(_teacherService.findById(id)).andReturn(null);
        EasyMock.replay(_teacherService, file, uri);
        ResponseEntity response = _TeacherController.uploadTeacherImage(id, file, uri);
        EasyMock.verify(_teacherService, file, uri);
        Assert.assertEquals("Teacher with id_teacher: " + id + " not dfound", ((CustomErrorType) response.getBody()).getErrorMessage());
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void uploadTeacherImageAvatarEmptyTest() throws Exception {
        Long id = 1L;
        String fileName = "";
        EasyMock.expect(file.isEmpty() == false).andReturn(false);
        EasyMock.expect(_teacherService.findById(id)).andReturn(_teacher);
        EasyMock.expect(_teacher.getAvatar().isEmpty() == false).andReturn(false);
        EasyMock.expect(_teacher.getAvatar()).andReturn(fileName);
        


        EasyMock.replay(_teacherService, file, uri);
        ResponseEntity response = _TeacherController.uploadTeacherImage(id, file, uri);
        EasyMock.verify(_teacherService, file, uri);

    }


    @Test
    public void getTeacherImageTeacherNullTest() throws Exception {

    }
}
