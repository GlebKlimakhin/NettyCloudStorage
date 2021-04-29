public class DeleteRequestMessage extends AbstractMessage{
    private String filename;

    public DeleteRequestMessage(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
