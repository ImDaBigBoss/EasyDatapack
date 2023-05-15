package com.github.imdabigboss.easydatapack.backend.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.imdabigboss.easydatapack.backend.EasyDatapack;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class FileUtils {
    public static byte[] sha1Hash(Path file) throws NoSuchAlgorithmException, IOException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        try (FileInputStream fileStream = new FileInputStream(file.toFile())) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fileStream.read(buffer)) != -1) {
                messageDigest.update(buffer, 0, bytesRead);
            }
        }
        return messageDigest.digest();
    }

    public static void zipFolder(Path sourceFolder, Path zipFile) throws IOException {
        ZipOutputStream stream = new ZipOutputStream(Files.newOutputStream(zipFile, StandardOpenOption.CREATE));
        stream.setLevel(8);

        Files.walkFileTree(sourceFolder, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                ZipEntry entry = new ZipEntry(sourceFolder.relativize(file).toString());
                stream.putNextEntry(entry);
                Files.copy(file, stream);
                stream.closeEntry();
                return FileVisitResult.CONTINUE;
            }
        });

        stream.close();
    }

    public static void unzipFile(Path file, Path destination) throws IOException {
        if (!Files.isRegularFile(file)) {
            throw new IOException("You can't unzip a folder");
        }

        ZipFile zip = new ZipFile(file.toFile());

        ZipEntry entry;
        final Enumeration<? extends ZipEntry> entries = zip.entries();
        while (entries.hasMoreElements()) {
            entry = entries.nextElement();

            if (!entry.isDirectory()) {
                File newFile = destination.resolve(entry.getName()).toFile();
                newFile.getParentFile().mkdirs();

                InputStream fileStream = zip.getInputStream(entry);
                FileOutputStream outStream = new FileOutputStream(newFile);

                byte[] buf = new byte[fileStream.available()];
                int length;
                while ((length = fileStream.read(buf)) != -1) {
                    outStream.write(buf, 0, length);
                }

                outStream.flush();
                outStream.close();
            }
        }
    }

    public static void mergeFolders(Path source, Path destination) throws IOException {
        List<Path> files = Files.walk(source).filter(Files::isRegularFile).toList();

        for (Path file : files) {
            Path relative = source.relativize(file);
            Path target = destination.resolve(relative);
            Files.createDirectories(target.getParent());
            if (!Files.exists(target)) {
                Files.copy(file, target);
            }
        }
    }

    public static void extractResource(EasyDatapack datapack, String sourcePath, Path destination) throws IOException {
        InputStream stream = datapack.getResource(sourcePath);
        if (stream == null) {
            throw new IOException("Resource \"" + sourcePath + "\" not found in jar");
        }

        FileOutputStream ouput = new FileOutputStream(destination.toFile());
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = stream.read(buffer)) != -1) {
            ouput.write(buffer, 0, bytesRead);
        }

        ouput.flush();
        ouput.close();
        stream.close();
    }

    public static void deleteFolder(Path folder) throws IOException {
        Files.walkFileTree(folder, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
