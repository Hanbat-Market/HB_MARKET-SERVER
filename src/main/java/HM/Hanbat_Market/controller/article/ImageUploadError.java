package HM.Hanbat_Market.controller.article;

public class ImageUploadError extends RuntimeException {

    private String fieldName;
    private String errorMessage;

    public ImageUploadError(String fieldName, String errorMessage) {
        this.fieldName = fieldName;
        this.errorMessage = errorMessage;
    }

    // Getter and Setter methods
}
