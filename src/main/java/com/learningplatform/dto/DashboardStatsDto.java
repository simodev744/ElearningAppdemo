package com.learningplatform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatsDto {
    
    private long totalStudents;
    private long totalFormateurs;
    private long totalCourses;
    private long validatedCourses;
    private long pendingCourses;
    private long totalArticles;
    private long activeUsers;
}