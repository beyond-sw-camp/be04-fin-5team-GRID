package org.highfives.grid.user.query.vo;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Getter
@Builder
public class ResFindListVO {

    private int statusCode;
    private String message;
    private String href;
    private List<SimpleInfo> result;
    private long totalElements; // 총 요소 수
    private int totalPages; // 총 페이지 수
    private int currentPage; // 현재 페이지

    public ResFindListVO(int statusCode, String message, String href, List<SimpleInfo> result) {
        this.statusCode = statusCode;
        this.message = message;
        this.href = href;
        this.result = result;
    }
    
    public ResFindListVO(int statusCode, String message, String href, List<SimpleInfo> result, long totalElements, int totalPages, int currentPage) {
        this.statusCode = statusCode;
        this.message = message;
        this.href = href;
        this.result = result;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
    }
}
