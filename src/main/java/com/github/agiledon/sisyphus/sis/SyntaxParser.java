package com.github.agiledon.sisyphus.sis;

import com.github.agiledon.sisyphus.exception.FailedDeserializationException;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.github.agiledon.sisyphus.sis.rule.ParsingRule.asnClassTree;
import static com.github.agiledon.sisyphus.sis.rule.ParsingRule.createRootClass;
import static com.google.common.base.Preconditions.checkArgument;

public class SyntaxParser {
    private final Logger logger = LoggerFactory.getLogger(SyntaxParser.class);
    private static final String LINE_BREAK = "\n";

    public SisClass parseClass(String content) {
        SisClass rootClass;

        try {
            List<String> lines = splitLines(content);
            checkArgument(lines != null && lines.size() >= 1, "data file is error");
            rootClass = createRootClass(lines.get(0));
            for (String line : lines) {
                rootClass = asnClassTree(rootClass, line);
            }
            return rootClass;
        } catch (IllegalArgumentException ex) {
            return logAndRethrowException(ex);
        } catch (Exception ex) {
            return logAndRethrowException(ex);
        }
    }

    private SisClass logAndRethrowException(Exception ex) {
        logger.error(ex.getMessage());
        throw new FailedDeserializationException(ex);
    }

    private List<String> splitLines(String content) {
        Iterable<String> lines = Splitter.on(LINE_BREAK)
                .omitEmptyStrings()
                .split(content);

        return Lists.newArrayList(lines);
    }
}