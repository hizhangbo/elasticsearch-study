package io.github.hizhangbo.model;

import io.github.hizhangbo.annotations.Document;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @author Bob
 * @date 2020-02-29 17:54
 */
@Data
@Builder
@ToString
@Document(indexName = "books")
public class Book {
    private String name;
    private String ISBN;
    private String author;
    private String jd_id;
    private String publisher;
    private String publish_date;
    private String commit;
    private String high_opinion;
    private String middle_opinion;
    private String low_opinion;
    private String page_count;
    private String paper_size;
    private String paper_type;
    private String pack_type;
    private String fixed_price;
    private String discounted_price;
    private String classify;
    private String tags;
    private String edition;
}
