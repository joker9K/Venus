package converter;

import java.lang.reflect.Type;

/**
 * @author zhangwt
 * @date 2017/9/29 11:54.
 */
public class UserConverter extends Converter<UserDto,User>{
    public UserConverter() {
        super(userDto -> new User(userDto.getFirstName(), userDto.getLastName(), userDto.isActive(),
                        userDto.getEmail()),
                user -> new UserDto(user.getFirstName(), user.getLastName(), user.isActive(),
                        user.getUserId()));
    }


    public static void main(String[] args) {
        Type type = (Type) User.class;
        System.out.println(type);
    }
}
