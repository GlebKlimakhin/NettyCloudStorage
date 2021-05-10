public class FileRequestMessage extends AbstractMessage{

    private String filename;

    public FileRequestMessage(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
