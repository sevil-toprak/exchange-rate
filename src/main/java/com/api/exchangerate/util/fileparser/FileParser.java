package com.api.exchangerate.util.fileparser;

import java.io.InputStream;
import java.util.List;

public interface FileParser<T> {
    List<T> parse(InputStream inputStream);
    String getParserType();
}
