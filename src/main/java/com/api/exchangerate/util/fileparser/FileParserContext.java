package com.api.exchangerate.util.fileparser;

import com.api.exchangerate.exception.BaseException;
import com.api.exchangerate.exception.constants.ErrorCode;
import com.api.exchangerate.exception.constants.ErrorMessages;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class FileParserContext<T> {
    private final Map<String, FileParser<T>> parserMap;

    public FileParserContext(List<FileParser<T>> parsers) {
        this.parserMap = parsers.stream()
                .collect(Collectors.toMap(FileParser::getParserType, p -> p));
    }

    public FileParser<T> getParserByType(String getParserByType) {
        return Optional.ofNullable(parserMap.get(getParserByType))
                .orElseThrow(() -> new BaseException(ErrorCode.UNSUPPORTED_PARSER_TYPE,
                        ErrorMessages.UNSUPPORTED_PARSER_TYPE,
                        HttpStatus.BAD_REQUEST));
    }
}

