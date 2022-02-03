package com.example.documentsystem.dao.content;

import java.io.IOException;
import java.io.InputStream;

public interface FileContentRepository {
    InputStream retrieve(FileContentId id);
    void add(FileContentId id, InputStream stream) throws IOException;
    void update(FileContentId id, InputStream stream) throws IOException;
    void delete(FileContentId id) throws IOException;
}
