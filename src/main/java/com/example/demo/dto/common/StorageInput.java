package com.example.demo.dto.common;

import java.io.InputStream;

public class StorageInput {

    private final InputStream fileInputStream;
    private final String fileName;
    private final String mimeType;
    private final long fileSize;

    private StorageInput(Builder builder) {
        this.fileInputStream = builder.fileInputStream;
        this.fileName = builder.fileName;
        this.mimeType = builder.mimeType;
        this.fileSize = builder.fileSize;
    }

    public InputStream getFileInputStream() {
        return fileInputStream;
    }

    public String getFileName() {
        return fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getPrefix() {
        if (mimeType == null)
            throw new RuntimeException("Tipo MIME não identificado");

        if (mimeType.startsWith("image"))
            return "imagens/";
        else
            return "documentos/";
    }

    public static class Builder {
        private InputStream fileInputStream;
        private String fileName;
        private String mimeType;
        private long fileSize;

        public Builder withFileInputStream(InputStream fileInputStream) {
            this.fileInputStream = fileInputStream;
            return this;
        }

        public Builder withFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder withMimeType(String mimeType) {
            this.mimeType = mimeType;
            return this;
        }

        public Builder withFileSize(long fileSize) {
            this.fileSize = fileSize;
            return this;
        }

        public StorageInput build() {
            if (fileInputStream == null || fileName == null || mimeType == null || fileSize == 0) {
                throw new IllegalArgumentException("Todos os campos são obrigatórios.");
            }
            return new StorageInput(this);
        }
    }
}

