package com.plazoleta.foodcourtmicroservice.domain.utils.pagination;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PageInfoTest {

    @Test
    void when_CreatePageInfoWithValidData_Expect_FieldsAreSetCorrectly() {
        // Arrange
        List<String> content = Arrays.asList("A", "B", "C");
        long totalElements = 30L;
        int totalPages = 10;
        int currentPage = 2;
        int pageSize = 3;
        boolean hasNext = true;
        boolean hasPrevious = true;

        // Act
        PageInfo<String> pageInfo = new PageInfo<>(content, totalElements, totalPages, currentPage, pageSize, hasNext, hasPrevious);

        // Assert
        assertEquals(content, pageInfo.getContent());
        assertEquals(totalElements, pageInfo.getTotalElements());
        assertEquals(totalPages, pageInfo.getTotalPages());
        assertEquals(currentPage, pageInfo.getCurrentPage());
        assertEquals(pageSize, pageInfo.getPageSize());
        assertTrue(pageInfo.isHasNext());
        assertTrue(pageInfo.isHasPrevious());
    }

    @Test
    void when_CreatePageInfoWithEmptyContent_Expect_EmptyListAndCorrectFields() {
        // Arrange
        List<String> content = Collections.emptyList();
        long totalElements = 0L;
        int totalPages = 0;
        int currentPage = 0;
        int pageSize = 10;
        boolean hasNext = false;
        boolean hasPrevious = false;

        // Act
        PageInfo<String> pageInfo = new PageInfo<>(content, totalElements, totalPages, currentPage, pageSize, hasNext, hasPrevious);

        // Assert
        assertNotNull(pageInfo.getContent());
        assertTrue(pageInfo.getContent().isEmpty());
        assertEquals(totalElements, pageInfo.getTotalElements());
        assertEquals(totalPages, pageInfo.getTotalPages());
        assertEquals(currentPage, pageInfo.getCurrentPage());
        assertEquals(pageSize, pageInfo.getPageSize());
        assertFalse(pageInfo.isHasNext());
        assertFalse(pageInfo.isHasPrevious());
    }

    @Test
    void when_CreatePageInfoWithSingleElement_Expect_CorrectFields() {
        // Arrange
        List<Integer> content = Collections.singletonList(42);
        long totalElements = 1L;
        int totalPages = 1;
        int currentPage = 0;
        int pageSize = 1;
        boolean hasNext = false;
        boolean hasPrevious = false;

        // Act
        PageInfo<Integer> pageInfo = new PageInfo<>(content, totalElements, totalPages, currentPage, pageSize, hasNext, hasPrevious);

        // Assert
        assertEquals(content, pageInfo.getContent());
        assertEquals(totalElements, pageInfo.getTotalElements());
        assertEquals(totalPages, pageInfo.getTotalPages());
        assertEquals(currentPage, pageInfo.getCurrentPage());
        assertEquals(pageSize, pageInfo.getPageSize());
        assertFalse(pageInfo.isHasNext());
        assertFalse(pageInfo.isHasPrevious());
    }

    @Test
    void when_CreatePageInfoWithNegativeValues_Expect_FieldsSetAsGiven() {
        // Arrange
        List<String> content = Arrays.asList("X");
        long totalElements = -5L;
        int totalPages = -1;
        int currentPage = -2;
        int pageSize = -10;
        boolean hasNext = false;
        boolean hasPrevious = true;

        // Act
        PageInfo<String> pageInfo = new PageInfo<>(content, totalElements, totalPages, currentPage, pageSize, hasNext, hasPrevious);

        // Assert
        assertEquals(content, pageInfo.getContent());
        assertEquals(totalElements, pageInfo.getTotalElements());
        assertEquals(totalPages, pageInfo.getTotalPages());
        assertEquals(currentPage, pageInfo.getCurrentPage());
        assertEquals(pageSize, pageInfo.getPageSize());
        assertFalse(pageInfo.isHasNext());
        assertTrue(pageInfo.isHasPrevious());
    }
}
