package com.urise.webapp.web;

import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class ResumeServletTest {

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    RequestDispatcher requestDispatcher;

    @Mock
    Storage storage;

    @Captor
    ArgumentCaptor<Resume> argumentCaptor;

    ResumeServlet underTest;

    @BeforeEach
    void setUp() throws ServletException {
        MockitoAnnotations.openMocks(this);
        underTest = new ResumeServlet();
        underTest.init(storage);
    }

    @Test
    @DisplayName("Save new resume and send redirect in doPost")
    void itShouldSaveNewResumeAndRedirectOnDoPost() throws IOException {
        // Given
        String fullName = "fullName";
        Resume resume = new Resume(fullName);

        doNothing().when(storage).save(any());

        when(request.getParameter("uuid")).thenReturn(null);
        when(request.getParameter("fullName")).thenReturn(fullName);

        // When
        underTest.doPost(request, response);

        // Then
        then(storage).should().save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue())
                .usingRecursiveComparison()
                .ignoringFields("uuid")
                .isEqualTo(resume);
        verify(response).sendRedirect("resume");
    }

    @Test
    @DisplayName("Update existing resume and send redirect in doPost")
    void itShouldUpdateResumeAndRedirectOnDoPost() throws IOException {
        // Given
        String uuid = "uuid";
        String fullName = "fullName";
        Resume resume = new Resume(uuid, fullName);

        doReturn(resume).when(storage).get(uuid);

        doNothing().when(storage).update(any());

        when(request.getParameter("uuid")).thenReturn(uuid);
        when(request.getParameter("fullName")).thenReturn(fullName);

        // When
        underTest.doPost(request, response);

        // Then
        then(storage).should().update(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isEqualTo(resume);
        verify(response).sendRedirect("resume");
    }

    @Test
    @DisplayName("Get list of resumes and redirect to resumes list in doGet")
    void itShouldGetAllResumesAndRedirectToListDoGet() throws ServletException, IOException {
        // Given
        List<Resume> resumesList = List.of(new Resume("Some name"));
        doReturn(resumesList).when(storage).getAllSorted();

        doReturn(requestDispatcher).when(request).getRequestDispatcher("/WEB-INF/jsp/list.jsp");
        doNothing().when(requestDispatcher).forward(request, response);

        when(request.getParameter("action")).thenReturn(null);

        // When
        underTest.doGet(request, response);

        // Then
        verify(request).setAttribute("resumes", resumesList);
        verify(request).getRequestDispatcher("/WEB-INF/jsp/list.jsp");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    @DisplayName("Delete resume and redirect to resumes list in doGet")
    void itShouldDeleteAndRedirectToListDoGet() throws ServletException, IOException {
        // Given
        String uuid = "uuid";
        when(request.getParameter("uuid")).thenReturn(uuid);
        when(request.getParameter("action")).thenReturn("delete");
        doNothing().when(storage).delete(any());

        // When
        underTest.doGet(request, response);

        // Then
        verify(storage).delete(uuid);
        verify(response).sendRedirect("resume");
    }

    @Test
    @DisplayName("Get resume from storage and redirect to resume view in doGet")
    void itShouldGetResumeRedirectToViewDoGet() throws ServletException, IOException {
        // Given
        String uuid = "uuid";
        String fullName = "fullName";
        Resume resume = new Resume(uuid, fullName);

        when(request.getParameter("uuid")).thenReturn(uuid);
        when(request.getParameter("action")).thenReturn("view");
        doReturn(resume).when(storage).get(uuid);

        doReturn(requestDispatcher).when(request).getRequestDispatcher("/WEB-INF/jsp/view.jsp");
        doNothing().when(requestDispatcher).forward(request, response);

        // When
        underTest.doGet(request, response);

        // Then
        verify(request).setAttribute("resume", resume);
        verify(request).getRequestDispatcher("/WEB-INF/jsp/view.jsp");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    @DisplayName("Get resume from storage and redirect to edit resume in doGet")
    void itShouldGetResumeRedirectToEditDoGet() throws ServletException, IOException {
        // Given
        String uuid = "uuid";
        String fullName = "fullName";
        Resume resume = new Resume(uuid, fullName);

        when(request.getParameter("uuid")).thenReturn(uuid);
        when(request.getParameter("action")).thenReturn("edit");
        doReturn(resume).when(storage).get(uuid);

        doReturn(requestDispatcher).when(request).getRequestDispatcher("/WEB-INF/jsp/edit.jsp");
        doNothing().when(requestDispatcher).forward(request, response);

        // When
        underTest.doGet(request, response);

        // Then
        verify(request).setAttribute("resume", resume);
        verify(request).getRequestDispatcher("/WEB-INF/jsp/edit.jsp");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    @DisplayName("Create new resume and redirect to edit resume in doGet")
    void itShouldCreateNewResumeRedirectToEditDoGet() throws ServletException, IOException {
        // Given
        Resume resume = Resume.EMPTY;

        when(request.getParameter("action")).thenReturn("new");

        doReturn(requestDispatcher).when(request).getRequestDispatcher("/WEB-INF/jsp/edit.jsp");
        doNothing().when(requestDispatcher).forward(request, response);

        // When
        underTest.doGet(request, response);

        // Then
        verify(request).setAttribute("resume", resume);
        verify(request).getRequestDispatcher("/WEB-INF/jsp/edit.jsp");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    @DisplayName("Throw if action is unknown")
    void itShouldThrowIfActionIsUnknownDoGet() {
        // Given
        String unknownAction = "unknown action";
        when(request.getParameter("action")).thenReturn(unknownAction);

        // When
        // Then
        assertThatThrownBy(() -> underTest.doGet(request, response))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Action " + unknownAction + " is illegal");
    }
}