package ewha.efub.zeje.util.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //400 BAD_REQUEST : 잘못된 요청
    CANNOT_EMPTY_CONTENT(BAD_REQUEST, "내용이 비어있을 수 없습니다."),
    INVALID_VALUE(BAD_REQUEST, "올바르지 않은 값입니다."),
    INVALID_IMAGE_FILE(BAD_REQUEST, "잘못된 이미지 파일입니다."),
    INVALID_SESSION_USER(BAD_REQUEST, "세션 유저가 비어있습니다."),
    DUPLICATE_INFORMATION(BAD_REQUEST, "이미 방문했습니다."),
    NOT_FLOWER_SPOT(BAD_REQUEST, "꽃봉우리 스팟이 아닙니다."),

    //401 UNAUTHORIZED : 비인증 상태
    USER_UNAUTHORIZED(UNAUTHORIZED, "로그인이 필요합니다."),

    //403 FORBIDDEN : 권한 없음
    ACCESS_DENIED(FORBIDDEN, "해당 페이지를 요청할 권한이 없습니다."),

    //404 NOT_FOUND : Resource를 찾을 수 없음
    USER_NOT_FOUND(NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다."),
    REVIEW_NOT_FOUND(NOT_FOUND, "해당 리뷰 정보를 찾을 수 없습니다."),
    SPOT_NOT_FOUND(NOT_FOUND, "해당 스팟 정보를 찾을 수 없습니다."),
    DIARY_NOT_FOUND(NOT_FOUND, "해당 다이어리 정보를 찾을 수 없습니다."),
    MEMORY_NOT_FOUND(NOT_FOUND, "해당 일기 정보를 찾을 수 없습니다."),
    WISH_NOT_FOUND(NOT_FOUND, "해당 위시리스트 정보를 찾을 수 없습니다."),


    //500 INTERNAL_SERVER_ERROR : 서버 내 문제
    EXCEPTION(INTERNAL_SERVER_ERROR, "서버 내에 알 수 없는 오류가 발생했습니다."),
    IO_EXCEPTION(INTERNAL_SERVER_ERROR, "이미지 업로드/다운로드 중 알 수 없는 오류가 발생했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String detail;

}