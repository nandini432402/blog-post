package com.example.blogNest.domain.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Cloudinary Configuration for File Storage
 *
 * Configures Cloudinary service for handling:
 * - User profile images
 * - Blog featured images
 * - Blog content images
 * - Document uploads
 *
 * Provides automatic image optimization, transformation, and CDN delivery
 */
@Configuration
public class CloudinaryConfig {

    @Value("${cloudinary.cloud-name:}")
    private String cloudName;

    @Value("${cloudinary.api-key:}")
    private String apiKey;

    @Value("${cloudinary.api-secret:}")
    private String apiSecret;

    @Value("${cloudinary.secure:true}")
    private boolean secure;

    /**
     * Configure Cloudinary instance with credentials
     * Falls back to local storage if Cloudinary credentials are not provided
     */
    @Bean
    public Cloudinary cloudinary() {
        // Check if Cloudinary credentials are provided
        if (cloudName == null || cloudName.isEmpty() ||
                apiKey == null || apiKey.isEmpty() ||
                apiSecret == null || apiSecret.isEmpty()) {

            System.out.println("WARNING: Cloudinary credentials not provided. File uploads will use local storage.");
            System.out.println("To enable Cloudinary, set the following properties:");
            System.out.println("- cloudinary.cloud-name");
            System.out.println("- cloudinary.api-key");
            System.out.println("- cloudinary.api-secret");

            // Return dummy configuration for local development
            return new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", "dummy",
                    "api_key", "dummy",
                    "api_secret", "dummy"
            ));
        }

        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        config.put("secure", String.valueOf(secure));

        System.out.println("Cloudinary configured successfully with cloud name: " + cloudName);

        return new Cloudinary(config);
    }

    /**
     * Configuration properties for file upload validation
     */
    @Configuration
    public static class FileUploadConfig {

        @Value("${app.file.max-size:10485760}") // 10MB default
        private long maxFileSize;

        @Value("${app.file.allowed-image-types:jpg,jpeg,png,gif,webp}")
        private String allowedImageTypes;

        @Value("${app.file.allowed-document-types:pdf,doc,docx,txt}")
        private String allowedDocumentTypes;

        @Value("${app.file.upload-dir:uploads}")
        private String uploadDirectory;

        public long getMaxFileSize() {
            return maxFileSize;
        }

        public String[] getAllowedImageTypes() {
            return allowedImageTypes.split(",");
        }

        public String[] getAllowedDocumentTypes() {
            return allowedDocumentTypes.split(",");
        }

        public String getUploadDirectory() {
            return uploadDirectory;
        }

        /**
         * Check if file type is allowed for images
         */
        public boolean isImageTypeAllowed(String fileType) {
            String[] allowedTypes = getAllowedImageTypes();
            for (String type : allowedTypes) {
                if (type.trim().equalsIgnoreCase(fileType)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Check if file type is allowed for documents
         */
        public boolean isDocumentTypeAllowed(String fileType) {
            String[] allowedTypes = getAllowedDocumentTypes();
            for (String type : allowedTypes) {
                if (type.trim().equalsIgnoreCase(fileType)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Get human-readable file size limit
         */
        public String getMaxFileSizeFormatted() {
            long sizeInMB = maxFileSize / (1024 * 1024);
            return sizeInMB + " MB";
        }
    }
}