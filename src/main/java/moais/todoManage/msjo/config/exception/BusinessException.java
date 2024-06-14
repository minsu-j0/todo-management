package moais.todoManage.msjo.config.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

// 처리가능한 Business 프로세스 상의 예외 발생시 사용(httpStatus - 상황에 따라 설정)
@Getter
@ToString
public class BusinessException extends RuntimeException {

	private HttpStatus httpStatus;
	
	public BusinessException(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

}
