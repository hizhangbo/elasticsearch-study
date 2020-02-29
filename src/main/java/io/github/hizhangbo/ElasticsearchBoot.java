package io.github.hizhangbo;

import io.github.hizhangbo.model.Book;
import io.github.hizhangbo.service.BookService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;

/**
 * @author Bob
 * @date 2020-02-29 17:21
 */
@ComponentScan(value = "io.github.hizhangbo")
public class ElasticsearchBoot {

    public static void main(String[] args) throws IOException {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ElasticsearchBoot.class);

        BookService bookService = applicationContext.getBean(BookService.class);

        // insert
//        Book book = Book.builder().author("斯诺登").name("永久记录").classify("自传").build();
//        String id = bookService.add(book);
//        System.out.println(id);

        // update
        Book book = Book.builder().author("斯诺登").name("永久记录").classify("自传").ISBN("112233").build();

        String id = "BEqikXAB14VHtwTpc5ur";
        bookService.update(id, book);

        // find
//        Book book = bookService.findById(id);
//        System.out.println(book);

        // delete
//        bookService.remove("AUpakXAB14VHtwTp4Zun");

        applicationContext.close();
    }
}
