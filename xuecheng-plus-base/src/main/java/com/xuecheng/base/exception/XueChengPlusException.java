

package com.xuecheng.base.exception;


/**
 * @description 学成在线项目异常类
 * @param null
 * @return
 * @author Starry
 * @date 2023/3/8 19:43
*/
public class XueChengPlusException extends RuntimeException {

	private static final long serialVersionUID = 5565760508056698922L;

	private String errMessage;

	public XueChengPlusException() {
		super();
	}

	public XueChengPlusException(String errMessage) {
		super(errMessage);
		this.errMessage = errMessage;
	}

	public String getErrMessage() {
		return errMessage;
	}

	public static void cast(com.xuecheng.base.exception.CommonError commonError){
		 throw new XueChengPlusException(commonError.getErrMessage());
	}
	public static void cast(String errMessage){
		 throw new XueChengPlusException(errMessage);
	}

}
