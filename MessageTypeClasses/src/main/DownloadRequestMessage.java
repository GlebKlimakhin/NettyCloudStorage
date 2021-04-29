public class DownloadRequestMessage extends AbstractMessage{

    private String filename;

    public DownloadRequestMessage(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
