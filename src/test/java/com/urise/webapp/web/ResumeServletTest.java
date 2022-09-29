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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class ResumeServletTest {

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

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
        when(request.getParameter("uuid")).thenReturn(null);
        when(request.getParameter("fullName")).thenReturn(fullName);
        doNothing().when(storage).save(any());

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
        when(request.getParameter("uuid")).thenReturn(uuid);
        when(request.getParameter("fullName")).thenReturn(fullName);
        doReturn(resume).when(storage).get(uuid);
        doNothing().when(storage).update(any());

        // When
        underTest.doPost(request, response);

        // Then
        then(storage).should().update(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isEqualTo(resume);
        verify(response).sendRedirect("resume");
    }

    @Test
    @DisplayName("Get list of resumes and redirect to resumes list in doGet")
    void itShouldGetAllResumesAndRedirectToListDoGet() {
        // Given
        // When
        // Then
    }

    @Test
    @DisplayName("Delete resume and redirect to resumes list in doGet")
    void itShouldDeleteAndRedirectToListDoGet() {
        // Given
        // When
        // Then
    }
}