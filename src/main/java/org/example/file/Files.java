package org.example.file;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiPredicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Files {
    public static Stream<Path> find(Path start,
                                    int maxDepth,
                                    BiPredicate<Path, BasicFileAttributes> matcher,
                                    FileVisitOption... options)
            throws IOException
    {
        FileTreeIterator iterator = new FileTreeIterator(start, maxDepth, options);
        try {
            Spliterator<FileTreeWalker.Event> spliterator =
                    Spliterators.spliteratorUnknownSize(iterator, Spliterator.DISTINCT);
            return StreamSupport.stream(spliterator, false)
                    .onClose(iterator::close)
                    .filter(entry -> matcher.test(entry.file(), entry.attributes()))
                    .map(FileTreeWalker.Event::file);
        } catch (Error|RuntimeException e) {
            iterator.close();
            throw e;
        }
    }
}
