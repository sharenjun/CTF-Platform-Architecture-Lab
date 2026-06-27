package com.aegis.backend.common;
//ApiResponse是一个泛型类
public class ApiResponse<T> {
//它是所有接口统一返回给前端的JSON外壳。
//比如health下的HealthController要用，返回的就是OK，那么直接调用ErrorCode中的OK即可。
//core column
    private String  code;
    private String message;
    //这里的T是泛类型。因为data传过来可能是String/Object等等(使用者决定data)
    private T data;
    public ApiResponse(String code,String message,T data){
        this.code=code;
        this.message=message;
        this.data=data;
    }
    //写getter，因为是private-code/message/data。
    public String getCode(){
        return code;
    }
    public String getMessage(){
        return message;
    }
    public T getData(){
        return data;
    }
    //如果每次调用return new ApiResponse<>("xx","xx","xx")太麻烦。直接定义静态
    public static <T> ApiResponse<T> ok(T data){
        //public static 公开静态方法。
        //<T> 声明这个静态方法自己也使用一个泛类型T
        //ApiRespone<T> 返回的value为泛类型。
        //以后Controller中直接return ApiResponse.ok("OK");就好了。
        //类上的T属于对象ApiResponse<T>,static方法属于类，static方法不属于某一个对象(new 不了。)
        /*
        这里是赋予了<T>类型为String/Integer
        ApiResponse<String> response1 = new ApiResponse<>("OK", "成功", "hello");
        ApiResponse<Integer> response2 = new ApiResponse<>("OK", "成功", 100);
        但是static方法不属于某一个对象。

        ApiResponse.ok("OK");的时候没有赋予<T>为任何类型
        调用的时候没有创建对象。new ApiResponse<String>(...)
        static方法执行时，还没有具体的ApiResponse<T>对象。
        既然没有对象，就没有对象上的T。
        所以static方法不能直接使用类上的T。
        */
        return new ApiResponse<>(ErrorCode.OK.getCode(),ErrorCode.OK.getMessage(),data);
    }
    public static ApiResponse<Void> fail(ErrorCode errorCode){
        //失败没有value，所以直接返回void
        return new ApiResponse<>(errorCode.getCode(),errorCode.getMessage(),null);
    }
}
