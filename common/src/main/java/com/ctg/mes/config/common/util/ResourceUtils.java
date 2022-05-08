package com.ctg.mes.config.common.util;

import com.google.common.base.CharMatcher;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;

import java.io.*;
import java.net.URL;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.nio.charset.StandardCharsets.UTF_8;

public class ResourceUtils {
    /**
     * save resource under root classpath
     *
     * @param resourcePath path relative root classpath.
     */
    public static BufferedWriter saveResourceUnderClassPath(String resourcePath) {
        checkNotNull(resourcePath, "path can't be null.");

        /**
         * root classpath path + resource path.
         */
        File file = new File(loadFileUnderClassPath(""), trimLeadingSlash(resourcePath));
        try {
            return Files.newWriter(file, UTF_8);
        } catch (FileNotFoundException e) {
                throw new RuntimeException("can't find resource: " + file.getAbsolutePath());
        }
    }

    /**
     * load resource under root classpath
     *
     * @param resourcePath path relative root classpath.
     */
    public static BufferedReader loadResourceUnderClassPath(String resourcePath) {
        File file = loadFileUnderClassPath(resourcePath);
        try {
            return Files.newReader(file, UTF_8);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("can't find resource: " + file.getAbsolutePath());
        }
    }

    /**
     * load resource from resourcePath under root classpath. it will remove leading /.
     *
     * @param resourcePath path
     * @return this path file object.
     */
    public static File loadFileUnderClassPath(String resourcePath) {
        checkNotNull(resourcePath, "path can't be null.");
        resourcePath = trimLeadingSlash(resourcePath);

        // get resource under root classpath
        URL url = ClassLoader.getSystemResource(resourcePath);

        if (url == null) {
            throw new RuntimeException("can't find resource: " + resourcePath);
        }

        return new File(url.getPath());
    }


    /**
     * load resource under root classpath
     *
     * @param resourcePath path relative root classpath.
     */
    public static <T> T loadResourceUnderClassPath(LineProcessor<T> lineProcessor, String resourcePath) {
        File file = loadFileUnderClassPath(resourcePath);

        return processLine(lineProcessor, file);
    }

    /**
     * trim leading slash: example: /a/b/ -> a/b/;
     */
    private static String trimLeadingSlash(String resourcePath) {
        return CharMatcher.is('/').trimLeadingFrom(resourcePath);
    }

    private static <T> T processLine(LineProcessor<T> lineProcessor, File file) {
        try {
            return Files.readLines(file, UTF_8, lineProcessor);
        } catch (IOException e) {
            throw new RuntimeException("processor resource error: " + e.getMessage(), e.getCause());
        }
    }

    /**
     * 读取文件内容
     */
    public static String readResourceUnderClassPath(String resourcePath) throws IOException {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = ResourceUtils.loadResourceUnderClassPath(resourcePath)) {
            char[] buf = new char[4096];
            int read;
            while ((read = br.read(buf)) >= 0) {
                builder.append(buf, 0, read);
            }
        }
        return builder.toString();
    }

    /**
     * load resource exists or not under root classpath
     */
    public static boolean loadResourceUnderClassPathExist(String resourcePath) {
        try {
            return loadFileUnderClassPath(resourcePath).exists();
        } catch (Exception e) {
            return false;
        }
    }
}
