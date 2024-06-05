package com.example.demo.api.Controller;

import com.example.demo.DTO.NewsDto;
import com.example.demo.controller.ActionController;
import com.example.demo.models.Comment;
import com.example.demo.models.News;
import com.example.demo.models.security.RoleEntity;
import com.example.demo.models.security.UserEntity;
import com.example.demo.security.SecurityUtil;
import com.example.demo.services.CommentService;
import com.example.demo.services.NewsService;
import com.example.demo.services.security.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ActionController.class) // used for testing controllers , automatically configures MockMVC
@AutoConfigureMockMvc(addFilters = false)       //Spring Security filters will not be applied to tests
@ExtendWith(MockitoExtension.class)
public class ActionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private NewsService newsService;
    @MockBean
    private UserService userService;
    @MockBean
    private CommentService commentService;
    private NewsDto newsDto; private UserEntity user ; private UserEntity admin; private News news ; private Comment comment ; private Comment comment2;
    private SecurityUtil securityUtil = mock(SecurityUtil.class);

    @BeforeEach // execute before each test
    private void init() {
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

        comment2 = Comment.builder()
                .id(2L)
                .text("comment2")
                .author("author2")
                .pubDate("2022-02-07")
                .news(news)
                .build();

        List<Comment> comments = new ArrayList<>();
        comments.add(comment2);

        user = UserEntity.builder()
                .id(1L)
                .username("username")
                .comments(comments)
                .likedNews(Arrays.asList(news))
                .seenNews(Arrays.asList(news))
                .build();
        admin = UserEntity.builder()
                .id(2L)
                .username("admin")
                .build();

        comment = Comment.builder()
                .id(1L)
                .text("comment")
                .author("author")
                .pubDate("2022-02-07")
                .user(user)
                .build();
        when(userService.findByUsername(Mockito.any(String.class))).thenReturn(user);
        when(userService.findById(user.getId())).thenReturn(user);
    }
    @Test
    public void ActionController_SaveComment_ReturnTNewsDetail() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("username");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(userService.findByUsername("username")).thenReturn(user);


        when(commentService.saveComment(comment, news.getId())).thenReturn(comment);


        RedirectAttributes mockRedirectAttributes = mock(RedirectAttributes.class); // Mock RedirectAttributes

        ResultActions resultActions = mockMvc.perform(post("/comments/{newsId}/save", news.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .flashAttr("comments", comment)
                        .sessionAttr("redirectAttributes", mockRedirectAttributes))
                .andExpect(status().isFound());

        // Verify the redirect URL
        String expectedRedirectURL = "/news/" + news.getId();
        resultActions.andExpect(MockMvcResultMatchers.redirectedUrl(expectedRedirectURL));
    }
    @Test
    public void ActionController_DeleteComment_ReturnTNewsDetail() throws Exception {
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);


        when(userService.findByUsername(Mockito.any(String.class))).thenReturn(user);
        when(commentService.findCommentById(comment.getId())).thenReturn(comment);
        when(commentService.saveComment(comment, news.getId())).thenReturn(comment);

        ResultActions resultActions = mockMvc.perform(post("/comments/{newsId}/delete/{commentId}", news.getId(),comment.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFound());

        // Verify the redirect URL
        String expectedRedirectURL = "/news/" + news.getId();
        resultActions.andExpect(MockMvcResultMatchers.redirectedUrl(expectedRedirectURL));
    }
    // ----------------------------------------------------------likeAndDislikeForNews----------------------------------------------------------------
    @Test
    public void ActionController_LikeNews_ReturnTNewsDetail() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("username");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userService.findByUsername("username")).thenReturn(user);
        when(newsService.getNewsById(news.getId())).thenReturn(news);

        RedirectAttributes mockRedirectAttributes = mock(RedirectAttributes.class);

        ResultActions resultActions = mockMvc.perform(post("/news/actions/{newsId}?interaction=like", news.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .sessionAttr("redirectAttributes", mockRedirectAttributes))
                .andExpect(status().isFound());

        // Verify the redirect URL
        String expectedRedirectURL = "/news/" + news.getId();
        resultActions.andExpect(MockMvcResultMatchers.redirectedUrl(expectedRedirectURL));

        verify(userService, times(1)).actionNews("like", news, "news", null); // Verify that userService.actionNews was called with correct arguments
    }
    @Test
    public void ActionController_DislikeNews_ReturnTNewsDetail() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("username");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userService.findByUsername("username")).thenReturn(user);
        when(newsService.getNewsById(news.getId())).thenReturn(news);

        RedirectAttributes mockRedirectAttributes = mock(RedirectAttributes.class);

        ResultActions resultActions = mockMvc.perform(post("/news/actions/{newsId}?interaction=dislike", news.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .sessionAttr("redirectAttributes", mockRedirectAttributes))
                .andExpect(status().isFound());

        // Verify the redirect URL
        String expectedRedirectURL = "/news/" + news.getId();
        resultActions.andExpect(MockMvcResultMatchers.redirectedUrl(expectedRedirectURL));

        verify(userService, times(1)).actionNews("dislike", news, "news", null); // Verify that userService.actionNews was called with correct arguments
    }

    // ----------------------------------------------------------likeAndDislikeForComments----------------------------------------------------------------
    @Test
    public void ActionController_LikeComment_ReturnTNewsDetail() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("username");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userService.findByUsername("username")).thenReturn(user);
        when(newsService.getNewsById(news.getId())).thenReturn(news);
        when(commentService.findCommentById(comment.getId())).thenReturn(comment);

        RedirectAttributes mockRedirectAttributes = mock(RedirectAttributes.class);

        ResultActions resultActions = mockMvc.perform(post("/news/actions/{newsId}/comments/{commentId}?interaction=like", news.getId(),comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .sessionAttr("redirectAttributes", mockRedirectAttributes))
                .andExpect(status().isFound());

        // Verify the redirect URL
        String expectedRedirectURL = "/news/" + news.getId();
        resultActions.andExpect(MockMvcResultMatchers.redirectedUrl(expectedRedirectURL));

        verify(userService, times(1)).actionNews("like", null, "comment", comment); // Verify that userService.actionNews was called with correct arguments
    }
    @Test
    public void ActionController_DislikeComment_ReturnTNewsDetail() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("username");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userService.findByUsername("username")).thenReturn(user);
        when(newsService.getNewsById(news.getId())).thenReturn(news);
        when(commentService.findCommentById(comment.getId())).thenReturn(comment);

        RedirectAttributes mockRedirectAttributes = mock(RedirectAttributes.class);

        ResultActions resultActions = mockMvc.perform(post("/news/actions/{newsId}/comments/{commentId}?interaction=dislike", news.getId(),comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .sessionAttr("redirectAttributes", mockRedirectAttributes))
                .andExpect(status().isFound());

        // Verify the redirect URL
        String expectedRedirectURL = "/news/" + news.getId();
        resultActions.andExpect(MockMvcResultMatchers.redirectedUrl(expectedRedirectURL));

        verify(userService, times(1)).actionNews("dislike", null, "comment", comment); // Verify that userService.actionNews was called with correct arguments
    }
    //--------------------------------------------DeleteUser---------------------------------
    @Test
    public void ActionController_UserDeleteHimSelf_ReturnToLogOut() throws Exception {
        RoleEntity roleUser = RoleEntity.builder()
                .id(1L)
                .name("USER")
                .build();

        List<RoleEntity> roles = new ArrayList<>();
        roles.add(roleUser);
        user.setRoles(roles);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("username");
        SecurityContextHolder.getContext().setAuthentication(authentication);


        ResultActions resultActions = mockMvc.perform(post("/users/delete/{userId}",user.getId())
                .contentType(MediaType.APPLICATION_JSON));


        resultActions.andExpect(MockMvcResultMatchers.status().isFound());

        // Verify the redirect URL
        String expectedRedirectURL = "/logout";
        resultActions.andExpect(MockMvcResultMatchers.redirectedUrl(expectedRedirectURL));
    }
    @Test
    public void ActionController_AdminDeleteUser_ReturnToCabinet() throws Exception {
        RoleEntity roleUser = RoleEntity.builder()
                .id(1L)
                .name("ADMIN")
                .build();

        List<RoleEntity> roles = new ArrayList<>();
        roles.add(roleUser);
        admin.setRoles(roles);


        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("username");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(SecurityUtil.getSessionUser()).thenReturn("admin"); // get username of user who delete

        when(userService.findByUsername("admin")).thenReturn(admin); // get user-object , who delete
        when(userService.findById(user.getId())).thenReturn(user);   // get user-object which we gonna delete

        ResultActions resultActions = mockMvc.perform(post("/users/delete/{userId}",user.getId())
                .contentType(MediaType.APPLICATION_JSON));


        resultActions.andExpect(MockMvcResultMatchers.status().isFound());

        // Verify the redirect URL
        String expectedRedirectURL = "/cabinet"; // when admin delete user , he returns to /cabinet
        resultActions.andExpect(MockMvcResultMatchers.redirectedUrl(expectedRedirectURL));

        verify(userService, times(1)).deleteUserById(user.getId()); // verify delete method
    }
    //-------------------------------------------------------UserCabinet-----------------------------------
    @Test
    public void ActionController_Cabinet_ReturnsCabinet() throws Exception {

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("username");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(userService.findByUsername(user.getUsername())).thenReturn(user);

        // Act
        MvcResult result = mockMvc.perform(get("/cabinet"))
                .andExpect(status().isOk())
                .andExpect(view().name("personal-cabinet")) // expected view
                .andReturn(); // returns an MvcResult object that contains the result of executing a request to the controller.

        // Assert
        ModelAndView modelAndViewContainer = result.getModelAndView(); // retrieves the ModelAndView object from the MvcResult.
        UserEntity returnedUser = (UserEntity) modelAndViewContainer.getModel().get("user");
        //maps the extracted object to the NewsPagination type and saves it to the actualNewsPagination variable.
        Assertions.assertEquals(user, returnedUser);
    }
    //-----------------------------------------------------UsersCabinet------------------------------------------
    @Test
    public void ActionController_UsersList_ReturnsCabinet() throws Exception {
        RoleEntity roleUser = RoleEntity.builder()
                .id(1L)
                .name("ADMIN")
                .build();

        List<RoleEntity> roles = new ArrayList<>();
        roles.add(roleUser);
        admin.setRoles(roles);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("admin");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(userService.findByUsername(admin.getUsername())).thenReturn(admin);
        when(userService.searchUser(Mockito.any(String.class))).thenReturn(Arrays.asList(user));
       MvcResult resultMvc =  mockMvc.perform(get("/users/find?query=username"))
                              .andExpect(status().isOk())
                              .andExpect(view().name("personal-cabinet :: userList")) // expected view
                              .andReturn();
       ModelAndView modelAndViewContainer = resultMvc.getModelAndView();
       List<UserEntity> users = (List<UserEntity>) modelAndViewContainer.getModel().get("users");
       Assertions.assertEquals(Arrays.asList(user), users);
    }
    }




