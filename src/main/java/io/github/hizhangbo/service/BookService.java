package io.github.hizhangbo.service;

import io.github.hizhangbo.model.Book;
import lombok.extern.log4j.Log4j2;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * @author Bob
 * @date 2020-02-29 19:51
 */
@Log4j2
@Service
public class BookService extends BaseService<Book> {

    protected RestHighLevelClient client;

    private BookService(RestHighLevelClient client) {
        this.client = client;
    }

    @Override
    public String add(Book book) {
        XContentBuilder builder = transferBuilder(book);
        if (builder == null) {
            return null;
        }
        IndexRequest indexRequest = new IndexRequest(getIndex()).source(builder);
        IndexResponse indexResponse = null;
        try {
            indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error(e);
        }
        return indexResponse == null ? null : indexResponse.getId();
    }


    @Override
    public String remove(String id) {
        DeleteRequest deleteRequest = new DeleteRequest(getIndex(), id);
        DeleteResponse deleteResponse = null;
        try {
            deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error(e);
        }
        return deleteResponse == null ? null : deleteResponse.getId();
    }

    @Override
    public String update(String id, Book book) {
        XContentBuilder builder = transferBuilder(book);
        if (builder == null) {
            return null;
        }
        UpdateRequest request = new UpdateRequest(getIndex(), id).doc(builder);
        UpdateResponse updateResponse = null;
        try {
            updateResponse = client.update(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error(e);
        }
        log.info(id);
        return updateResponse == null ? null : updateResponse.getId();
    }

    @Override
    public Book findById(String id) {
        GetRequest getRequest = new GetRequest(getIndex(), id);
        GetResponse getResponse = null;
        try {
            getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error(e);
        }

        Map<String, Object> resultMap = getResponse.getSource();
        return transferDoc(resultMap);
    }

    @Override
    public long count(Book doc) {
        CountRequest countRequest = new CountRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        countRequest.source(searchSourceBuilder);
        CountResponse countResponse = null;
        try {
            countResponse = client.count(countRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error(e);
        }
        return countResponse == null ? 0 : countResponse.getCount();
    }
}
