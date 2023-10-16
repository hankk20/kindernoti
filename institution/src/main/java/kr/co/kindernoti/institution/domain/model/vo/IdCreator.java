package kr.co.kindernoti.institution.domain.model.vo;

import lombok.Getter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

@Getter
public class IdCreator<T extends PlainId> {

    private final Class<T> clazz;

    public static <T extends PlainId> IdCreator<T> creator(Class<T> clazz) {
        return new IdCreator<>(clazz);
    }

    public IdCreator(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T from(UUID id) {
        return newInstance(id);
    }

    public T from(String id) {
        try {
            UUID uuid = UUID.fromString(id);
        }catch (IllegalArgumentException e){
            throw new InvalidIdException(clazz.getSimpleName(), id);
        }
        return newInstance(UUID.fromString(id));
    }

    public T create() {
        return newInstance(UUID.randomUUID());
    }

    private T newInstance(UUID id) {
        try {
            Constructor<T> declaredConstructor = clazz.getDeclaredConstructor(UUID.class);
            return declaredConstructor.newInstance(id);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
