package ewha.efub.zeje.util.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //400 BAD_REQUEST : 잘못된 요청
    CANNOT_EMPTY_CONTENT(BAD_REQUEST, "내용이 비어있을 수 없습니다."),
    BIGGER_LATE_MONEY(BAD_REQUEST, "지각비가 결석비를 초과할 수 없습니다."),
    INVALID_VALUE(BAD_REQUEST, "올바르지 않은 값입니다."),
    INVALID_IMAGE_FILE(BAD_REQUEST, "잘못된 이미지 파일입니다."),

    //404 NOT_FOUND : Resource를 찾을 수 없음
    USER_NOT_FOUND(NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다."),
    REVIEW_NOT_FOUND(NOT_FOUND, "해당 리뷰 정보를 찾을 수 없습니다."),
    SPOT_NOT_FOUND(NOT_FOUND, "해당 스팟 정보를 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String detail;

}