public class BigFileMessage extends AbstractMessage{

    private String filename;
    private String login;
    private byte [] data;
    private int dataPartsNumber;
    private int currentDataPart;

    public BigFileMessage(String filename, String login, byte[] data, int dataPartsNumber, int currentDataPart) {
        this.filename = filename;
        this.login = login;
        this.data = data;
        this.dataPartsNumber = dataPartsNumber;
        this.currentDataPart = currentDataPart;
    }

    public String getLogin() {
        return login;
    }

    public byte[] getData() {
        return data;
    }

    public String getFilename() {
        return filename;
    }

    public int getDataPartsNumber() {
        return dataPartsNumber;
    }

    public int getCurrentDataPart() {
        return currentDataPart;
    }
}
