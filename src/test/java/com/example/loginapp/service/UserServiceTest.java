package com.example.loginapp.service;

import com.example.loginapp.model.User;
import com.example.loginapp.repository.UserRepository;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    // Helper method to setup mock UserService
    private UserService setupService(String email, String userPassword, User returnedUser) {
        UserRepository mockRepo = mock(UserRepository.class);
        when(mockRepo.findByEmailAndPassword(email, userPassword)).thenReturn(returnedUser);
        return new UserService(mockRepo);
    }

    @Test
    void testLoginWithValidCredentials() {
        User user = new User();
        user.setEmail("john@example.com");
        user.setUserPassword("secure123");

        UserService service = setupService("john@example.com", "secure123", user);

        assertTrue(service.login("john@example.com", "secure123"));
    }

    @Test
    void testLoginWithInvalidPassword() {
        UserService service = setupService("john@example.com", "wrongpass", null);
        assertFalse(service.login("john@example.com", "wrongpass"));
    }

    @Test
    void testLoginWithInvalidEmail() {
        UserService service = setupService("wrong@example.com", "secure123", null);
        assertFalse(service.login("wrong@example.com", "secure123"));
    }

    @Test
    void testLoginWithNullEmail() {
        UserService service = setupService(null, "secure123", null);
        assertFalse(service.login(null, "secure123"));
    }

    @Test
    void testLoginWithNullPassword() {
        UserService service = setupService("john@example.com", null, null);
        assertFalse(service.login("john@example.com", null));
    }

    @Test
    void testLoginWithBothNull() {
        UserService service = setupService(null, null, null);
        assertFalse(service.login(null, null));
    }

    @Test
    void testLoginWithEmptyEmail() {
        UserService service = setupService("", "secure123", null);
        assertFalse(service.login("", "secure123"));
    }

    @Test
    void testLoginWithEmptyPassword() {
        UserService service = setupService("john@example.com", "", null);
        assertFalse(service.login("john@example.com", ""));
    }

    @Test
    void testLoginWithBothEmpty() {
        UserService service = setupService("", "", null);
        assertFalse(service.login("", ""));
    }

    @Test
    void testLoginWithWhitespaceEmail() {
        UserService service = setupService("   ", "secure123", null);
        assertFalse(service.login("   ", "secure123"));
    }

    @Test
    void testLoginWithWhitespacePassword() {
        UserService service = setupService("john@example.com", "   ", null);
        assertFalse(service.login("john@example.com", "   "));
    }

    @Test
    void testLoginWithUppercaseEmail() {
        User user = new User();
        user.setEmail("JOHN@EXAMPLE.COM");
        user.setUserPassword("secure123");

        UserService service = setupService("JOHN@EXAMPLE.COM", "secure123", user);
        assertTrue(service.login("JOHN@EXAMPLE.COM", "secure123"));
    }

    @Test
    void testLoginWithMixedCasePassword() {
        User user = new User();
        user.setEmail("john@example.com");
        user.setUserPassword("SeCuRe123");

        UserService service = setupService("john@example.com", "SeCuRe123", user);
        assertTrue(service.login("john@example.com", "SeCuRe123"));
    }

    @Test
    void testLoginWithSQLInjectionAttempt() {
        UserService service = setupService("john@example.com", "' OR '1'='1", null);
        assertFalse(service.login("john@example.com", "' OR '1'='1"));
    }

    @Test
    void testLoginWithSpecialCharacters() {
        UserService service = setupService("john@example.com", "!@#$%^&*", null);
        assertFalse(service.login("john@example.com", "!@#$%^&*"));
    }

    @Test
    void testLoginWithLongPassword() {
        String longPassword = "a".repeat(100);
        UserService service = setupService("john@example.com", longPassword, null);
        assertFalse(service.login("john@example.com", longPassword));
    }

    @Test
    void testLoginWithLongEmail() {
        String longEmail = "a".repeat(200) + "@test.com";
        UserService service = setupService(longEmail, "secure123", null);
        assertFalse(service.login(longEmail, "secure123"));
    }

    @Test
    void testLoginWithEmailHavingSpacesInside() {
        UserService service = setupService("john @example.com", "secure123", null);
        assertFalse(service.login("john @example.com", "secure123"));
    }

    @Test
    void testLoginWithPasswordHavingTabs() {
        UserService service = setupService("john@example.com", "pass\tword", null);
        assertFalse(service.login("john@example.com", "pass\tword"));
    }

    @Test
    void testLoginWithEmailHavingTrailingSpaces() {
        UserService service = setupService("john@example.com ", "secure123", null);
        assertFalse(service.login("john@example.com ", "secure123"));
    }

    @Test
    void testLoginWithEmojiInPassword() {
        UserService service = setupService("john@example.com", "😊secure", null);
        assertFalse(service.login("john@example.com", "😊secure"));
    }

    @Test
    void testLoginWithJsonStylePassword() {
        UserService service = setupService("john@example.com", "{\"pass\":\"123\"}", null);
        assertFalse(service.login("john@example.com", "{\"pass\":\"123\"}"));
    }

    @Test
    void testLoginWithNewLineInPassword() {
        UserService service = setupService("john@example.com", "pass\nword", null);
        assertFalse(service.login("john@example.com", "pass\nword"));
    }

    @Test
    void testLoginWithCorrectDetailsMultipleTimes() {
        User user = new User();
        user.setEmail("john@example.com");
        user.setUserPassword("secure123");

        UserService service = setupService("john@example.com", "secure123", user);
        assertTrue(service.login("john@example.com", "secure123"));
        assertTrue(service.login("john@example.com", "secure123"));
    }
}
