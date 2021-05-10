import java.util.ArrayList;

public class RefreshServerFilesListMessage extends AbstractMessage{

    private ArrayList<String> serverFilesList;

    public RefreshServerFilesListMessage(ArrayList<String> serverFilesList) {
        this.serverFilesList = serverFilesList;
    }

    public RefreshServerFilesListMessage() {
    }

    public ArrayList<String> getServerFilesList() {
        return serverFilesList;
    }
}
