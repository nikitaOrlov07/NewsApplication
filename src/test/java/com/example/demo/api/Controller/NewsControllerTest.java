package com.example.demo.api.Controller;

import com.example.demo.DTO.NewsDto;
import com.example.demo.DTO.NewsPagination;
import com.example.demo.controller.MainController;
import com.example.demo.services.CommentService;
import com.example.demo.services.impl.NewsServiceimpl;
import com.example.demo.services.security.UserService;
import org.junit.jupiter.api.Assertions;
import org.mockito.ArgumentMatchers;
import com.example.demo.models.News;
import com.example.demo.models.security.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Map;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.ModelAndView;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = MainController.class) // used for testing controllers , automatically configures MockMVC
@AutoConfigureMockMvc(addFilters = false)       //Spring Security filters will not be applied to tests
@ExtendWith(MockitoExtension.class)
public class NewsControllerTest {
    @Autowired
    private MockMvc mockMvc; // Implement MockMVC in an application

    @MockBean
    private CommentService commentService;
    @MockBean
    private NewsServiceimpl newsService;
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    private NewsDto newsDto; private UserEntity user = mock(UserEntity.class); private  News news ;

    @BeforeEach // executed before each test methods
    private void init()
    {
        newsDto = NewsDto.builder()
                .id(1L)
                .title("News")
                .language("english")
                .category(Collections.singletonList("Top"))
                .country(Collections.singletonList("Slovakia"))
                .description("Successfull test")
                .pubdate("2015-22-07")
                .build();
        news = News.builder()
                .id(1L)
                .title("News")
                .language("english")
                .category(new ArrayList<>(Collections.singletonList("top")))
                .country(new ArrayList<>(Collections.singletonList("Slovakia")))
                .description("Successfull test")
                .pubdate("2015-22-07")
                .build();

        when(user.hasAdminRole()).thenReturn(true);
        when(userService.findByUsername(Mockito.any(String.class))).thenReturn(user);


    }


    @Test
    public void MainController_CreateNews_ReturnCreated() throws Exception
    {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("username");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        given(newsService.createNews(ArgumentMatchers.any())).willAnswer(invocation -> invocation.getArgument(0)); //when the createNews method is called with any argument, it should simply return that argument ; given is a BDD method

        MvcResult result = mockMvc.perform(post("/news/create/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newsDto)))
                .andExpect(status().isOk())  // in application if saving was successful --> program is returned to /news
                .andReturn();
        Assertions.assertNotEquals(500, result.getResponse().getStatus()); // checking that status is not equal to 500 Internal Server Error
    }
    @Test
    public void MainController_GetAllNews_ReturnsCorrectModel() throws Exception {
        // Arrange
        int pageNo = 1;
        int pageSize = 10;
        NewsPagination expectedNewsPagination = NewsPagination.builder()
                .pageSize(pageSize)
                .last(true)
                .pageNo(pageNo)
                .data(Arrays.asList(news))
                .build();
        when(newsService.getAllNews(pageNo, pageSize)).thenReturn(expectedNewsPagination);

        // Act
        MvcResult result = mockMvc.perform(get("/news")
                        .param("pageNo", String.valueOf(pageNo))
                        .param("pageSize", String.valueOf(pageSize)))
                .andExpect(status().isOk())
                .andExpect(view().name("home-page"))
                .andReturn(); // returns an MvcResult object that contains the result of executing a request to the controller.

        // Assert
        ModelAndView modelAndViewContainer = result.getModelAndView(); // retrieves the ModelAndView object from the MvcResult.
        NewsPagination actualNewsPagination = (NewsPagination) modelAndViewContainer.getModel().get("news"); // retrieves the object associated with the “news” key from the data model. In our case, this should be a NewsPagination object that is added to the model in the controller.
        //maps the extracted object to the NewsPagination type and saves it to the actualNewsPagination variable.
        Assertions.assertEquals(expectedNewsPagination, actualNewsPagination);
    }

    @Test
    public void MainController_DetailNews_ReturnNews() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("username");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(newsService.getNewsById(1)).thenReturn(news);

        MvcResult result = mockMvc.perform(get("/news/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newsDto)))
                .andExpect(status().isOk())
                .andExpect(view().name("detail-page"))
                .andReturn();

        // Extract the News object from the model
        ModelAndView modelAndView = result.getModelAndView(); // get object ModelAndView from  MvcResult
        Map<String, Object> model = modelAndView.getModel(); // retrieves the model from ModelAndView. A model is a Map that contains attributes that have been added to the model by the controller.
        News actualNews = (News) model.get("news");   // get news from model

        // Assert that the actual News object matches the expected one
        Assertions.assertEquals(news, actualNews);
    }
    @Test
    public void MainController_UpdateNews_ReturnTpNewsDetail() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("username");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(userService.findByUsername("username")).thenReturn(user);
        when(newsService.updateNews(Mockito.any())).thenReturn(news);


        ResultActions resultActions = mockMvc.perform(post("/news/update/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newsDto)))
                .andExpect(status().isFound());

        // Verify the redirect URL
        String expectedRedirectURL = "/news/" + newsDto.getId();
        resultActions.andExpect(MockMvcResultMatchers.redirectedUrl(expectedRedirectURL));

        // Verify that the response status is not 500 Internal Server Error
        MvcResult result = resultActions.andReturn();
        Assertions.assertNotEquals(500, result.getResponse().getStatus());
    }
    @Test
    public void MainController_DeleteNews_Delete() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("username");
        SecurityContextHolder.getContext().setAuthentication(authentication);


        ResultActions resultActions = mockMvc.perform(post("/news/delete/1")
                        .contentType(MediaType.APPLICATION_JSON));


      resultActions.andExpect(MockMvcResultMatchers.status().isFound());
        // Verify the redirect URL
        String expectedRedirectURL = "/news";
        resultActions.andExpect(MockMvcResultMatchers.redirectedUrl(expectedRedirectURL));
    }

}
