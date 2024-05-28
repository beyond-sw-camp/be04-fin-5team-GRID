package org.highfives.grid.user.query.vo;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Getter
public class ResFindListVO {

    private int statusCode;
    private String message;
    private String href;
    private List<SimpleInfo> result;

    @Builder
    public ResFindListVO(int statusCode, String message, String href, List<SimpleInfo> result) {
        this.statusCode = statusCode;
        this.message = message;
        this.href = href;
        this.result = result;
    }
}
