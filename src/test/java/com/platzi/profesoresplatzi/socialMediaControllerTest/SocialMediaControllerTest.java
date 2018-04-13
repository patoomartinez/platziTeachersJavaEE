package com.platzi.profesoresplatzi.socialMediaControllerTest;

import com.platzi.profesoresplatzi.controller.SocialMediaController;

import com.platzi.profesoresplatzi.model.SocialMedia;
import com.platzi.profesoresplatzi.service.SocialMediaService;
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
@PrepareForTest({HttpHeaders.class})
public class SocialMediaControllerTest extends EasyMockSupport {
    @TestSubject
    public SocialMediaController _socialMediaController = new SocialMediaController();

    @Test
    public void getSocialMediasNullTest() {
        EasyMock.expect(_socialMediaService.findAllSocialMedias()).andReturn(Arrays.asList(_socialMedia));

        EasyMock.replay(_socialMediaService);
        ResponseEntity response = _socialMediaController.getSocialMedias(null);

        EasyMock.verify(_socialMediaService);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Mock
    private SocialMediaService _socialMediaService;
    @Mock
    private SocialMedia _socialMedia;

    @Mock
    private UriComponentsBuilder uri;
    @Mock
    private UriComponents uriComponents;

    @Mock
    private HttpHeaders headersMock;

    @Before
    public void setUp() {
        //_socialMediaController
        injectMocks(_socialMediaController);
    }

    @Test
    public void getSocialMediasTest() {
        String name = "Test";
        EasyMock.expect(_socialMediaService.findByName(name)).andReturn(_socialMedia);
        EasyMock.replay(_socialMediaService);
        ResponseEntity response = _socialMediaController.getSocialMedias(name);
        EasyMock.verify(_socialMediaService);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    public void getSocialMediasNullEmptyTest() {
        List<SocialMedia> socialMediaList = new ArrayList<>();

        EasyMock.expect(_socialMediaService.findAllSocialMedias()).andReturn(socialMediaList);

        EasyMock.replay(_socialMediaService);
        ResponseEntity response = _socialMediaController.getSocialMedias(null);

        EasyMock.verify(_socialMediaService);

        Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

    }

    @Test
    public void getSocialMediasNotFoundTest() {
        String name = "Test";
        EasyMock.expect(_socialMediaService.findByName(name)).andReturn(null);
        EasyMock.replay(_socialMediaService);
        ResponseEntity response = _socialMediaController.getSocialMedias(name);
        EasyMock.verify(_socialMediaService);

        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    //Test get by id

    @Test
    public void getSocialMediasByIdTest() {
        long id = 1;
        EasyMock.expect(_socialMediaService.findById(id)).andReturn(_socialMedia);
        EasyMock.replay(_socialMediaService);
        ResponseEntity response = _socialMediaController.getSocialMediaById(id);
        EasyMock.verify(_socialMediaService);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

    }


    @Test
    public void getSocialMediasNullNoContentByIdTest() {
        long id = 1;
        EasyMock.expect(_socialMediaService.findById(id)).andReturn(null);
        EasyMock.replay(_socialMediaService);
        ResponseEntity response = _socialMediaController.getSocialMediaById(id);
        EasyMock.verify(_socialMediaService);

        Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void getSocialMediasNullConflictByIdTest() {
        Long id = 0L;

        EasyMock.replay(_socialMediaService);
        ResponseEntity response = _socialMediaController.getSocialMediaById(id);
        EasyMock.verify(_socialMediaService);
        Assert.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assert.assertEquals("idSocialMedia is required", ((CustomErrorType) response.getBody()).getErrorMessage());

        //CUSTOM ERROR TYPE


    }


    //DELETE
    @Test
    public void deleteSocialMediasNullTest() {
        Long id = 1L;
        EasyMock.expect(_socialMediaService.findById(id)).andReturn(null);
        EasyMock.replay(_socialMediaService);
        ResponseEntity response = _socialMediaController.deleteSocialMedia(id);
        EasyMock.verify(_socialMediaService);

        Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

    }

    @Test
    public void deleteSocialMediasIdNullTest() {
        Long id = 0L;


        EasyMock.replay(_socialMediaService);
        ResponseEntity response = _socialMediaController.deleteSocialMedia(id);

        EasyMock.verify(_socialMediaService);


        Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

    }


    @Test
    public void deleteSocialMediasTest() {
        Long id = 1L;
        EasyMock.expect(_socialMediaService.findById(id)).andReturn(_socialMedia);
        _socialMediaService.deleteSocialMediaById(id);
        EasyMock.expectLastCall();

        EasyMock.replay(_socialMediaService);

        ResponseEntity response = _socialMediaController.deleteSocialMedia(id);

        EasyMock.verify(_socialMediaService);


        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

    }


    //update
    @Test
    public void updateSocialMediaNullTest() {
        Long id = 1L;
        EasyMock.expect(_socialMediaService.findById(id)).andReturn(null);
        EasyMock.replay(_socialMediaService);

        ResponseEntity response = _socialMediaController.updateSocialMedia(id, _socialMedia);
        EasyMock.verify(_socialMediaService);
        Assert.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assert.assertEquals("idSocialMedia is required", ((CustomErrorType) response.getBody()).getErrorMessage());


    }

    @Test
    public void updateSocialMediaTest() {
        Long id = 1L;

        String name = "";
        String icon = "";

        EasyMock.expect(_socialMediaService.findById(id)).andReturn(_socialMedia);
        EasyMock.expect(_socialMedia.getName()).andReturn(name);
        _socialMedia.setName(name);
        EasyMock.expectLastCall();

        EasyMock.expect(_socialMedia.getIcon()).andReturn(icon);

        _socialMedia.setIcon(icon);
        EasyMock.expectLastCall();

        _socialMediaService.updateSocialMedia(_socialMedia);
        EasyMock.expectLastCall();

        EasyMock.replay(_socialMediaService, _socialMedia);

        ResponseEntity response = _socialMediaController.updateSocialMedia(id, _socialMedia);

        EasyMock.verify(_socialMediaService, _socialMedia);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

    }

    @Test
    public void createSocialMediaNameNullTest() {
        String name = "";
        EasyMock.expect(_socialMedia.getName()).andReturn(name).times(2);


        EasyMock.replay(_socialMediaService, _socialMedia);
        ResponseEntity response = _socialMediaController.createSocialMedia(_socialMedia, uri);
        EasyMock.verify(_socialMediaService, _socialMedia);
        Assert.assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Assert.assertEquals("SOCIAL MEDIA NAME is required",
                ((CustomErrorType) response.getBody()).getErrorMessage());

    }

    @Test
    public void createSocialMediaTest() {
        String name = "asd";

        EasyMock.expect(_socialMedia.getName()).andReturn(name).times(3);
        EasyMock.expect(_socialMediaService.findByName(name)).andReturn(_socialMedia);
        EasyMock.replay(_socialMediaService, _socialMedia);
        ResponseEntity response = _socialMediaController.createSocialMedia(_socialMedia, uri);
        EasyMock.verify(_socialMediaService, _socialMedia);
        Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

    }

    @Test
    public void createSocialMedia() throws Exception {
        String name = "aa";
        EasyMock.expect(_socialMedia.getName()).andReturn(name).times(4);
        _socialMediaService.saveSocialMedia(_socialMedia);
        EasyMock.expectLastCall();

        EasyMock.expect(_socialMediaService.findByName(name)).andReturn(null);
        //PowerMock.expectNew(HttpHeaders.class, new HashMap<>(), true).andReturn(headersMock).times(1);

        EasyMock.expect(_socialMediaService.findByName(name)).andReturn(_socialMedia);

        EasyMock.expect(uri.path("/v1/socialMedias/{id}")).andReturn(uri);
        EasyMock.expect(_socialMedia.getIdSocialMedia()).andReturn(1L);
        EasyMock.expect(uri.buildAndExpand(1L)).andReturn(uriComponents);
        URI testUri = new URI("http://example.com");
        EasyMock.expect(uriComponents.toUri()).andReturn(testUri);
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("Location", Arrays.asList("http://example.com"));
        PowerMock.expectNew(HttpHeaders.class, headers, true).andReturn(headersMock).times(1);

        PowerMock.replay(_socialMedia, _socialMediaService, uri, HttpHeaders.class, uriComponents);
        UriComponentsBuilder uri2 = UriComponentsBuilder.fromUri(new URI("http://www.example.com"));
        ResponseEntity response = _socialMediaController.createSocialMedia(_socialMedia, uri);
        PowerMock.verify(_socialMedia, _socialMediaService, uri, HttpHeaders.class, uriComponents);
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

}



