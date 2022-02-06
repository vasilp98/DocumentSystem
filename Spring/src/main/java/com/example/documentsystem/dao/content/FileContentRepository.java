package com.example.documentsystem.dao.content;

import java.io.IOException;
import java.io.InputStream;

public interface FileContentRepository {
    InputStream retrieve(FileContentId id);
    void add(FileContentId id, InputStream stream);
    void update(FileContentId id, InputStream stream);
    void delete(FileContentId id);
}
