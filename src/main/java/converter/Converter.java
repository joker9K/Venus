package converter;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zhangwt
 * @date 2017/9/29 11:43.
 * 转换器
 * 具体实现{@link UserConverter}
 */
public class Converter<T,U> {
    private final Function<T, U> fromDto;
    private final Function<U, T> fromEntity;


    public Converter(Function<T, U> fromDto, Function<U, T> fromEntity) {
        this.fromDto = fromDto;
        this.fromEntity = fromEntity;
    }


    public final T convertFromEntity(final U user){
        return fromEntity.apply(user);
    }

    public final U convertFromDto(final T userDto){
        return fromDto.apply(userDto);
    }

    public final List<U> createFromDtos(final Collection<T> dtoUsers) {
        return dtoUsers.stream().map(this::convertFromDto).collect(Collectors.toList());
    }

    public final List<T> createFromEntities(final Collection<U> users) {
        return users.stream().map(this::convertFromEntity).collect(Collectors.toList());
    }
}
