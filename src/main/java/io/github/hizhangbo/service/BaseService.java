package io.github.hizhangbo.service;

import io.github.hizhangbo.annotations.Document;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

/**
 * @author Bob
 * @date 2020-02-29 20:38
 */
@Log4j2
@Getter
public abstract class BaseService<T> {

    private final String TYPE = "_doc";
    private String index;
    private Class<T> clazz;

    BaseService() {
        setClazz();
        setIndex();
    }

    public abstract String add(T doc);

    public abstract String remove(String id);

    public abstract String update(String id, T doc);

    public abstract T findById(String id);

    public abstract long count(T doc);

    public T transferDoc(Map<String, Object> doc) {
        if (clazz.isAnnotationPresent(Document.class)) {
            try {
                T obj = clazz.newInstance();
                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    field.set(obj, doc.get(field.getName()));
                }

                return obj;
            } catch (IllegalAccessException | InstantiationException e) {
                log.error(e);
            }
        }
        return null;
    }

    public XContentBuilder transferBuilder(T doc) {
        if (clazz.isAnnotationPresent(Document.class)) {
            XContentBuilder builder;
            try {
                builder = XContentFactory.jsonBuilder();
                builder.startObject();
                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    try {
                        if (field.get(doc) != null) {
                            builder.field(field.getName(), field.get(doc));
                        }
                    } catch (IllegalAccessException e) {
                        log.error(e);
                    }
                }
                builder.endObject();
                return builder;
            } catch (IOException e) {
                log.error(e);
            }
        }
        return null;
    }

    private void setClazz() {
        this.clazz = getTemplateType();
    }

    public Class<T> getTemplateType() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public void setIndex() {
        if (clazz.isAnnotationPresent(Document.class)) {
            Document annotation = clazz.getAnnotation(Document.class);
            index = annotation.indexName();
        } else {
            index = null;
        }
    }
}
