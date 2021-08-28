package com.gox.ssstash.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class MimeType {

    private final Map<String, String> extensionToMimeType = Map.ofEntries(
            Map.entry(".aac", "audio/aac"),
            Map.entry(".abw", "application/x-abiword"),
            Map.entry(".arc", "application/x-freearc"),
            Map.entry(".avi", "video/x-msvideo"),
            Map.entry(".azw", "application/vnd.amazon.ebook"),
            Map.entry(".bin", "application/octet-stream"),
            Map.entry(".bmp", "image/bmp"),
            Map.entry(".bz", "application/x-bzip"),
            Map.entry(".bz2", "application/x-bzip2"),
            Map.entry(".cda", "application/x-cdf"),
            Map.entry(".csh", "application/x-csh"),
            Map.entry(".css", "text/css"),
            Map.entry(".csv", "text/csv"),
            Map.entry(".doc", "application/msword"),
            Map.entry(".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
            Map.entry(".eot", "application/vnd.ms-fontobject"),
            Map.entry(".epub", "application/epub+zip"),
            Map.entry(".gz", "application/gzip"),
            Map.entry(".gif", "image/gif"),
            Map.entry(".htm", "text/html"),
            Map.entry(".ico", "image/vnd.microsoft.icon"),
            Map.entry(".ics", "text/calendar"),
            Map.entry(".jar", "application/java-archive"),
            Map.entry(".jpeg", "image/jpeg"),
            Map.entry(".jpg", "image/jpeg"),
            Map.entry(".js", "text/javascript"),
            Map.entry(".json", "application/json"),
            Map.entry(".jsonld", "application/ld+json"),
            Map.entry(".mid", "audio/midi"),
            Map.entry(".midi", "audio/midi"),
            Map.entry(".mjs", "text/javascript"),
            Map.entry(".mp3", "audio/mpeg"),
            Map.entry(".mp4", "video/mp4"),
            Map.entry(".mpeg", "video/mpeg"),
            Map.entry(".mpkg", "application/vnd.apple.installer+xml"),
            Map.entry(".odp", "application/vnd.oasis.opendocument.presentation"),
            Map.entry(".ods", "application/vnd.oasis.opendocument.spreadsheet"),
            Map.entry(".odt", "application/vnd.oasis.opendocument.text"),
            Map.entry(".oga", "audio/ogg"),
            Map.entry(".ogv", "video/ogg"),
            Map.entry(".ogx", "application/ogg"),
            Map.entry(".opus", "audio/opus"),
            Map.entry(".otf", "font/otf"),
            Map.entry(".png", "image/png"),
            Map.entry(".pdf", "application/pdf"),
            Map.entry(".php", "application/x-httpd-php"),
            Map.entry(".ppt", "application/vnd.ms-powerpoint"),
            Map.entry(".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"),
            Map.entry(".rar", "application/vnd.rar"),
            Map.entry(".rtf", "application/rtf"),
            Map.entry(".sh", "application/x-sh"),
            Map.entry(".svg", "image/svg+xml"),
            Map.entry(".swf", "application/x-shockwave-flash"),
            Map.entry(".tar", "application/x-tar"),
            Map.entry(".tif", "image/tiff"),
            Map.entry(".ts", "video/mp2t"),
            Map.entry(".ttf", "font/ttf"),
            Map.entry(".txt", "text/plain"),
            Map.entry(".vsd", "application/vnd.visio"),
            Map.entry(".wav", "audio/wav"),
            Map.entry(".weba", "audio/webm"),
            Map.entry(".webm", "video/webm"),
            Map.entry(".webp", "image/webp"),
            Map.entry(".woff", "font/woff"),
            Map.entry(".woff2", "font/woff2"),
            Map.entry(".xhtml", "application/xhtml+xml"),
            Map.entry(".xls", "application/vnd.ms-excel"),
            Map.entry(".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
            Map.entry(".xml", "text/xml if readable from casual users (RFC 3023, section 3)"),
            Map.entry(".xul", "application/vnd.mozilla.xul+xml"),
            Map.entry(".zip", "application/zip"),
            Map.entry(".3gp", "audio/3gpp if it doesn't contain video"),
            Map.entry(".3g2", "audio/3gpp2 if it doesn't contain video"),
            Map.entry(".7z", "application/x-7z-compressed")
    );

    public String getMimeType(String filename){
        String extension = filename.substring(filename.lastIndexOf("."));
        String mimeType = extensionToMimeType.get(extension);
        log.info("File name: {}; extension: {}; mime-type: {}", filename, extension, mimeType);
        return mimeType;
    }
}
