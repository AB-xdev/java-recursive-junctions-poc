package org.example;


import org.example.file.Files;

import java.io.IOException;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

public class App
{
    // See dummy/sub/README.txt
    public static void main(String[] args) throws Exception {

        reportAttrs("dummy/sub/junction");
        reportAttrs("dummy/sub/symlink");

        System.out.println("Found files:" + Files.find(
                        Paths.get("dummy"),
                        Integer.MAX_VALUE,
                        (path, attr) -> attr.isRegularFile())
                .toList());

        System.out.println("Found files:" + java.nio.file.Files.find(
                        Paths.get("dummy"),
                        Integer.MAX_VALUE,
                        (path, attr) -> attr.isRegularFile())
                .toList());
    }

    private static void reportAttrs(String path) throws IOException {
        BasicFileAttributes attrs = java.nio.file.Files.readAttributes(
                Paths.get(path), BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
        System.out.println(path);
        System.out.println("Regular file: " + attrs.isRegularFile());
        System.out.println("Directory:    " + attrs.isDirectory());
        System.out.println("Symlink:      " + attrs.isSymbolicLink());
        System.out.println("Other:        " + attrs.isOther());
        System.out.println();
    }
}
